package editor;

import editor.settings.ISettings;
import editor.settings.Settings;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import gw.util.PathUtil;
import editor.util.PlatformUtil;
import editor.util.ScreenUtil;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.Expando;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.json.IJsonIO;
import gw.lang.reflect.json.Json;
import gw.lang.reflect.module.IFileSystem;

import gw.util.StreamUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import javax.script.Bindings;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LabFrame extends JFrame implements IGosuEditor
{
  public static final int VERSION = 2;
  private static final String GOSU_LAB_DIR = ".GosuLab";
  private static final String GOSU_DIR = ".Gosu";
  private static LabFrame INSTANCE = null;
  private static Map<String, ISettings> _settings = Settings.makeDefaultSettings();

  public static LabFrame instance()
  {
    return INSTANCE;
  }

  private GosuPanel _panel;
  private Rectangle _restoreBounds;
  private List<String> _experiments = Collections.emptyList();

  public LabFrame() throws HeadlessException
  {
    super( "Gosu Editor" );

    makeCustomFrameDecorations();

    INSTANCE = this;
    configUI();
    setInitialSize();
    addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing( WindowEvent e )
        {
          exit();
        }

        @Override
        public void windowActivated( WindowEvent e )
        {
          EventQueue.invokeLater( ()-> {
            EditorHost currentEditor = _panel.getCurrentEditor();
            if( currentEditor != null )
            {
              currentEditor.parse();
            }
          } );
        }
        @Override
        public void windowDeactivated( WindowEvent e )
        {
          EventQueue.invokeLater( _panel::saveIfDirty );
        }
      } );
    addComponentListener(
      new ComponentAdapter()
      {
        @Override
        public void componentResized( ComponentEvent e )
        {
          if( (getExtendedState() & Frame.MAXIMIZED_BOTH) != MAXIMIZED_BOTH )
          {
            _restoreBounds = getBounds();
          }
        }

        @Override
        public void componentMoved( ComponentEvent e )
        {
          if( (getExtendedState() & Frame.MAXIMIZED_BOTH) != MAXIMIZED_BOTH )
          {
            _restoreBounds = getBounds();
          }
        }
      });
  }

  private void makeCustomFrameDecorations()
  {
//    setUndecorated( true );
//    getRootPane().setWindowDecorationStyle( JRootPane.FRAME );
//
//    setBackground( new Color( 0, 255, 0, 0 ) );
//    getRootPane().setOpaque( false );
//    getRootPane().setBorder( BorderFactory.createMatteBorder( 7, 7, 7, 7, new Color( 255, 0, 0, 120 ) ) );
  }

  public void exit()
  {
    saveLabState( _panel.getExperimentView().getExperiment() );

    if( _panel.saveIfDirty() )
    {
      getGosuPanel().killProcess();

      System.exit( 0 );
    }
  }

  private void configUI()
  {
    setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
    setIconImage( loadLabIcon().getImage() );
    _panel = new GosuPanel( this );
    Container contentPane = getContentPane();
    contentPane.setLayout( new BorderLayout() );
    contentPane.add( _panel, BorderLayout.CENTER );
  }

  private void setInitialSize()
  {
    _panel.setEditorSplitPosition( 20 );
    _panel.setExperimentSplitPosition( 60 );
  }

  public void reset()
  {
    if( EventQueue.isDispatchThread() )
    {
      resetNow();
      return;
    }

    try
    {
      EventQueue.invokeAndWait( this::resetNow );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private void resetNow()
  {
    _panel.clearTabs();
  }

  public void openInitialFile( IScriptPartId partId, Path file )
  {
    _panel.openInitialFile( partId, file );
  }

  public void openFile( Path anySourceFile )
  {
    _panel.openFile( anySourceFile, true );
  }

  @Override
  public void showMe()
  {
    setVisible( true );

    GosuEventQueue.instance().run();
  }

  public GosuPanel getGosuPanel()
  {
    return _panel;
  }

  public void restoreState( Experiment experiment )
  {
    _panel.restoreExperimentState( experiment );
  }

  public void selectTab( Object contentId )
  {
    _panel.selectTab( (Path)contentId );
  }

  public void closeTab( Object contentId )
  {
    _panel.closeTab( (Path)contentId );
  }

  public Rectangle getRestoreBounds()
  {
    return new Rectangle( _restoreBounds );
  }
  public void setRestoreBounds( Rectangle restoreBounds )
  {
    _restoreBounds = restoreBounds;
  }

  public List<String> getExperiments()
  {
    return _experiments;
  }
  public void setExperiments( List<String> experiments )
  {
    _experiments = experiments;
    for( Iterator<String> iter = experiments.iterator(); iter.hasNext(); )
    {
      String exp = iter.next();
      if( !PathUtil.exists( PathUtil.create( exp ) ) )
      {
        iter.remove();
      }
    }
  }
  public void addExperiment( Experiment exp )
  {
    String dir = PathUtil.getAbsolutePathName( exp.getExperimentDir() );
    if( _experiments.isEmpty() )
    {
      _experiments = new ArrayList<>();
      _experiments.add( dir );
    }
    else
    {
      if( _experiments.contains( dir ) )
      {
        _experiments.remove( dir );
      }
      _experiments.add( 0, dir );
    }
  }

  public static LabFrame create()
  {
    GosuLabLAF.setLookAndFeel();
    CommonServices.getFileSystem().setCachingMode( IFileSystem.CachingMode.NO_CACHING );
    return new LabFrame();
  }

  //## todo: dynamically update Gosu Lab
  public void checkForUpdate( GosuPanel gosuPanel )
  {
    try
    {
      Path userFile = getUserFile( gosuPanel );
      if( !PathUtil.exists( userFile ) || getVersion( gosuPanel ) < 2 )
      {
        PathUtil.delete( getUserGosuEditorDir(), true );
      }
    }
    catch( Exception e )
    {
      PathUtil.delete( getUserGosuEditorDir(), true );
    }
  }

  public static Path getUserGosuDir()
  {
    Path gosuDir = PathUtil.create( System.getProperty( "user.home" ), GOSU_DIR );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( gosuDir );
    return gosuDir;
  }

  public static Path getUserGosuEditorDir()
  {
    Path gosuDir = PathUtil.create( System.getProperty( "user.home" ), GOSU_LAB_DIR );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( gosuDir );
    return gosuDir;
  }

  public static Path getIndexDir()
  {
    Path gosuDir = PathUtil.create( System.getProperty( "user.home" ) + File.separator + GOSU_LAB_DIR + File.separator + "index" );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( gosuDir );
    return gosuDir;
  }

  public static Path getStockExperimentsDir()
  {
    Path gosuDir = PathUtil.create( System.getProperty( "user.home" ) + File.separator + GOSU_LAB_DIR + File.separator + "experiments" );
    copyExampleExperiments( getStockExamplesDir() );
    return gosuDir;
  }

  private static void copyExampleExperiments( Path gosuDir )
  {
    URL marker = EditorUtilities.class.getClassLoader().getResource( "examples/marker.txt" );
    try
    {
      IDirectory examplesDir = CommonServices.getFileSystem().getIFile( marker ).getParent();
      copyExamples( examplesDir, gosuDir );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void copyExamples( IResource from, Path to )
  {
    if( from instanceof IDirectory )
    {
      if( !PathUtil.getName( to ).equals( "examples" ) && PathUtil.exists( to ) )
      {
        // already have this experiment
        return;
      }

      if( !PathUtil.exists( to ) && !PathUtil.mkdirs( to ) )
      {
        System.out.println( "Failed to create experiment directory: " + PathUtil.getAbsolutePathName( to ) );
      }

      for( IDirectory child : ((IDirectory)from).listDirs() )
      {
        copyExamples( child, PathUtil.create( to, child.getName() ) );
      }
      for( IFile child : ((IDirectory)from).listFiles() )
      {
        copyExamples( child, PathUtil.create( to, child.getName() ) );
      }
    }
    else
    {
      try
      {
        InputStream in = ((IFile)from).openInputStream();
        OutputStream out = PathUtil.createOutputStream( to );
        byte[] buf = new byte[1024];
        int len;
        while( (len = in.read( buf )) > 0 )
        {
          out.write( buf, 0, len );
        }
        in.close();
        out.close();
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  public static Path getStockExamplesDir()
  {
    Path gosuDir = PathUtil.create( System.getProperty( "user.home" ) + File.separator + GOSU_LAB_DIR + File.separator + "examples" );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( gosuDir );
    return gosuDir;
  }

  public static List<Path> getStockExampleExperiments()
  {
    List<Path> experiments = new ArrayList<>();
    Path experimentsDir = getStockExamplesDir();
    for( Path dir : PathUtil.listFiles( experimentsDir ) )
    {
      if( PathUtil.isDirectory( dir ) )
      {
        Path experimentFile = findExperimentFile( dir );
        if( experimentFile != null )
        {
          experiments.add( dir );
        }
      }
    }
    return experiments;
  }

  public static Path findExperimentFile( Path dir )
  {
    for( Path f : PathUtil.listFiles( dir ) )
    {
      if( PathUtil.getName( f ).equalsIgnoreCase( PathUtil.getName( dir ) + ".prj" ) )
      {
        return f;
      }
    }
    return null;
  }

  private static Experiment makeScratchExperiment( GosuPanel gosuPanel )
  {
    Path experimentDir = PathUtil.create( getStockExperimentsDir(), "Scratch" );
    return new Experiment( experimentDir, gosuPanel );
  }

  public static void openFileOrDir( Path file )
  {
    try
    {
      Path parent;
      if( PathUtil.isDirectory( file ) )
      {
        parent = file;
        file = null;
      }
      else
      {
        if( !PathUtil.exists( file ) )
        {
          return;
        }
        file = PathUtil.getAbsolutePath( file );
        parent = file.getParent();
        if( parent == null )
        {
          return;
        }
      }
      doOpen( parent, file );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void doOpen( Path dir, Path toSelect ) throws IOException
  {
    if( PlatformUtil.isWindows() )
    {
      String cmd;
      if( toSelect != null )
      {
        cmd = "explorer /select," + PathUtil.getAbsolutePathName( toSelect );
      }
      else
      {
        cmd = "explorer /root," + PathUtil.getAbsolutePathName( dir );
      }
      // no quoting/escaping is needed
      Runtime.getRuntime().exec( cmd );
      return;
    }

    if( PlatformUtil.isMac() )
    {
      if( toSelect != null )
      {
        final String script = String.format(
          "tell application \"Finder\"\n" +
          "\treveal {\"%s\"} as POSIX file\n" +
          "\tactivate\n" +
          "end tell", PathUtil.getAbsolutePathName( toSelect ) );
        Runtime.getRuntime().exec( new String[]{"/usr/bin/osascript", "-e", script} );
      }
      else
      {
        Runtime.getRuntime().exec( new String[]{"open", PathUtil.getAbsolutePathName( dir )} );
      }
      return;
    }
    String path = PathUtil.getAbsolutePathName( dir );
    if( PlatformUtil.hasXdgOpen() )
    {
      Runtime.getRuntime().exec( new String[]{"/usr/bin/xdg-open", path} );
    }
    else if( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported( Desktop.Action.OPEN ) )
    {
      Desktop.getDesktop().open( PathUtil.create( path ).toFile() );
    }
    else
    {
      JOptionPane.showMessageDialog( instance(),
                                     "This action isn't supported on the current platform",
                                     "Cannot Open Path",
                                     JOptionPane.ERROR_MESSAGE );
    }
  }

  public static ImageIcon loadLabIcon()
  {
    return EditorUtilities.loadIcon( "images/g_16.png" );
  }

  public static Path getUserFile( GosuPanel gosuPanel )
  {
    Path file = PathUtil.create( getUserGosuEditorDir(), "layout.gosulab" );
    if( !PathUtil.isFile( file ) )
    {
      Expando bindings = new Expando();
      bindings.put( "Title", "Gosu Lab" );
      bindings.put( "Version", VERSION );
      bindings.put( "Experiments", Arrays.asList( PathUtil.getAbsolutePathName( makeScratchExperiment( gosuPanel ).getExperimentDir() ) ) );

      try( Writer fw = PathUtil.createWriter( file ) )
      {
        String json = (String)ReflectUtil.invokeMethod( bindings, "toJson" );
        fw.write( json );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return file;
  }

  public static Integer getVersion( GosuPanel gosuPanel ) throws MalformedURLException
  {
    Bindings bindings = (Bindings)ReflectUtil.getProperty( getUserFile( gosuPanel ).toUri().toURL(), "JsonContent" );
    return (Integer)bindings.get( "Version" );
  }

  public Experiment loadRecentExperiment( GosuPanel gosuPanel )
  {
    Bindings bindings;
    try
    {
      bindings = (Bindings)ReflectUtil.getProperty( getUserFile( gosuPanel ).toUri().toURL(), "JsonContent" );
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }
    //noinspection unchecked
    restoreLabState( bindings );
    return new Experiment( PathUtil.create( instance().getExperiments().get( 0 ) ), gosuPanel );
  }


  public void saveLabState( Experiment experiment )
  {
    if( !instance().isVisible() )
    {
      return;
    }

    Path userFile = getUserFile( experiment.getGosuPanel() );
    try( Writer fw = PathUtil.createWriter( userFile ) )
    {
      Expando bindings = new Expando();

      bindings.put( "Title", "Gosu Lab" );
      bindings.put( "Version", LabFrame.VERSION );

      addExperiment( experiment );
      bindings.put( "Experiments", instance().getExperiments() );

      saveScreenProps( bindings );

      String json = (String)ReflectUtil.invokeMethod( bindings, "toJson" );
      fw.write( json );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void saveScreenProps( Expando bindings )
  {
    LabFrame frame = instance();
    boolean maximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;

    Expando bindingsFrame = new Expando();
    bindings.put( "Frame", bindingsFrame );
    bindingsFrame.put( "Maximized", maximized ? 1 : 0 );

    Rectangle bounds = frame.getRestoreBounds();
    Expando bindingsBounds = new Expando();
    bindingsFrame.put( "Bounds", bindingsBounds );
    if( bounds != null )
    {
      ScreenUtil.convertToPercentageOfScreenWidth( bounds );
      bindingsBounds.put( "X", bounds.x );
      bindingsBounds.put( "Y", bounds.y );
      bindingsBounds.put( "Width", bounds.width );
      bindingsBounds.put( "Height", bounds.height );
    }
  }

  private void restoreLabState( Bindings bindings )
  {
    LabFrame frame = instance();

    Bindings bindingsFrame = (Bindings)bindings.get( "Frame" );
    boolean bSet = false;
    if( bindingsFrame != null )
    {
      Bindings bindingsBounds = (Bindings)bindingsFrame.get( "Bounds" );
      Integer x = (Integer)bindingsBounds.get( "X" );
      Integer y = (Integer)bindingsBounds.get( "Y" );
      Integer width = (Integer)bindingsBounds.get( "Width" );
      Integer height = (Integer)bindingsBounds.get( "Height" );
      if( height != null )
      {
        Rectangle bounds = new Rectangle( x, y, width, height );
        ScreenUtil.convertFromPercentageOfScreenWidth( bounds );
        frame.setBounds( bounds );
        frame.setRestoreBounds( bounds );
        bSet = true;
      }
    }

    if( !bSet )
    {
      setInitialFrameBounds( instance() );
    }

    if( bindingsFrame != null && (Integer)bindingsFrame.get( "Maximized" ) == 1 )
    {
      frame.setExtendedState( Frame.MAXIMIZED_BOTH );
    }

    //noinspection unchecked
    frame.setExperiments( (List<String>)bindings.get( "Experiments" ) );
  }

  private static void setInitialFrameBounds( Frame frame )
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width * 2 / 3;
    int height = width * 2 / 3;
    frame.setSize( width, height );
    EditorUtilities.centerWindowInFrame( frame, frame );
  }

  public static Map<String, ISettings> getSettings()
  {
    return _settings;
  }

  public ISettings findSettings( Predicate<ISettings> matcher )
  {
    if( matcher == null )
    {
      return null;
    }

    for( ISettings settings: _settings.values() )
    {
      if( matcher.test( settings ) )
      {
        return settings;
      }
    }
    return null;
  }

  public static void loadSettings()
  {
    Bindings bindings;
    Path settingsFile = getSettingsFile();
    if( PathUtil.isFile( settingsFile ) )
    {
      try( Reader reader = StreamUtil.getInputStreamReader( settingsFile.toUri().toURL().openStream() ) )
      {
        bindings = Json.fromJson( StreamUtil.getContent( reader ) );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    else
    {
      return;
    }
    _settings = new TreeMap<>();
    List<ISettings> settingList = IJsonIO.readList( "Settings", bindings );
    settingList.forEach( setting -> _settings.put( setting.getPath(), setting ) );
    _settings = Settings.mergeSettings( _settings );
  }

  public static void saveSettings()
  {
    Path settingsFile = getSettingsFile();
    try( Writer fw = PathUtil.createWriter( settingsFile ) )
    {
      Expando bindings = new Expando();
      IJsonIO.writeList( "Settings", new ArrayList<>( _settings.values() ), bindings );
      String json = (String)ReflectUtil.invokeMethod( bindings, "toJson" );
      fw.write( json );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private static Path getSettingsFile()
  {
    return PathUtil.create( getUserGosuEditorDir(), "settings.gosulab" );
  }
}
