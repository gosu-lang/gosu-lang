package editor;

import editor.settings.ISettings;
import editor.util.EditorUtilities;
import editor.util.Experiment;
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
import gw.lang.reflect.module.IFileSystem;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Predicate;
import javax.script.Bindings;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LabFrame extends JFrame implements IGosuEditor
{
  private static LabFrame INSTANCE = null;
  public static LabFrame instance()
  {
    return INSTANCE;
  }

  private GosuPanel _panel;
  private Rectangle _restoreBounds;
  private List<String> _experiments = Collections.emptyList();
  private ISettings _mruSettings;
  private List<ISettings> _settings;

  public LabFrame() throws HeadlessException
  {
    super( "Gosu Editor" );
    INSTANCE = this;
    _settings = Collections.emptyList();
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
            GosuEditor currentEditor = _panel.getCurrentEditor();
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

  public void openInitialFile( IScriptPartId partId, File file )
  {
    _panel.openInitialFile( partId, file );
  }

  public void openFile( File anySourceFile )
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
    _panel.selectTab( (File)contentId );
  }

  public void closeTab( Object contentId )
  {
    _panel.closeTab( (File)contentId );
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
      if( !new File( exp ).exists() )
      {
        iter.remove();
      }
    }
  }
  public void addExperiment( Experiment exp )
  {
    String dir = exp.getExperimentDir().getAbsolutePath();
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
      File userFile = getUserFile( gosuPanel );
      if( !userFile.exists() || getVersion( gosuPanel ) < 1 )
      {
        delete( getUserGosuEditorDir() );
      }
    }
    catch( Exception e )
    {
      delete( getUserGosuEditorDir() );
    }
  }

  public static void delete( File fileOrDirectory )
  {
    if( fileOrDirectory.isDirectory() )
    {
      for( File child : fileOrDirectory.listFiles() )
      {
        delete( child );
      }
    }
    //noinspection ResultOfMethodCallIgnored
    fileOrDirectory.delete();
  }

  public static File getUserGosuEditorDir()
  {
    File gosuDir = new File( System.getProperty( "user.home" ), ".GosuLab" );
    //noinspection ResultOfMethodCallIgnored
    gosuDir.mkdirs();
    return gosuDir;
  }

  public static File getStockExperimentsDir()
  {
    File gosuDir = new File( System.getProperty( "user.home" ) + File.separator + ".GosuLab" + File.separator + "experiments" );
    //noinspection ResultOfMethodCallIgnored
    copyExampleExperiments( getStockExamplesDir() );
    return gosuDir;
  }

  private static void copyExampleExperiments( File gosuDir )
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

  private static void copyExamples( IResource from, File to )
  {
    if( from instanceof IDirectory )
    {
      if( !to.getName().equals( "examples" ) && to.exists() )
      {
        // already have this experiment
        return;
      }

      if( !to.exists() && !to.mkdirs() )
      {
        System.out.println( "Failed to create experiment directory: " + to.getAbsolutePath() );
      }

      for( IDirectory child : ((IDirectory)from).listDirs() )
      {
        copyExamples( child, new File( to, child.getName() ) );
      }
      for( IFile child : ((IDirectory)from).listFiles() )
      {
        copyExamples( child, new File( to, child.getName() ) );
      }
    }
    else
    {
      try
      {
        InputStream in = ((IFile)from).openInputStream();
        OutputStream out = new FileOutputStream( to );
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

  public static File getStockExamplesDir()
  {
    File gosuDir = new File( System.getProperty( "user.home" ) + File.separator + ".GosuLab" + File.separator + "examples" );
    //noinspection ResultOfMethodCallIgnored
    gosuDir.mkdirs();
    return gosuDir;
  }

  public static List<File> getStockExampleExperiments()
  {
    List<File> experiments = new ArrayList<>();
    File experimentsDir = getStockExamplesDir();
    for( File dir : experimentsDir.listFiles() )
    {
      if( dir.isDirectory() )
      {
        File experimentFile = findExperimentFile( dir );
        if( experimentFile != null )
        {
          experiments.add( dir );
        }
      }
    }
    return experiments;
  }

  public static File findExperimentFile( File dir )
  {
    for( File f : dir.listFiles() )
    {
      if( f.getName().equalsIgnoreCase( dir.getName() + ".prj" ) )
      {
        return f;
      }
    }
    return null;
  }

  private static Experiment makeScratchExperiment( GosuPanel gosuPanel )
  {
    File experimentDir = new File( getStockExperimentsDir(), "Scratch" );
    return new Experiment( experimentDir, gosuPanel );
  }

  public static void openFileOrDir( File file )
  {
    try
    {
      File parent;
      if( file.isDirectory() )
      {
        parent = file;
        file = null;
      }
      else
      {
        if( !file.exists() )
        {
          return;
        }
        file = file.getAbsoluteFile();
        parent = file.getParentFile();
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

  private static void doOpen( File dir, File toSelect ) throws IOException
  {
    if( PlatformUtil.isWindows() )
    {
      String cmd;
      if( toSelect != null )
      {
        cmd = "explorer /select," + toSelect.getAbsolutePath();
      }
      else
      {
        cmd = "explorer /root," + dir.getAbsolutePath();
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
          "end tell", toSelect.getAbsolutePath() );
        Runtime.getRuntime().exec( new String[]{"/usr/bin/osascript", "-e", script} );
      }
      else
      {
        Runtime.getRuntime().exec( new String[]{"open", dir.getAbsolutePath()} );
      }
      return;
    }
    String path = dir.getAbsolutePath();
    if( PlatformUtil.hasXdgOpen() )
    {
      Runtime.getRuntime().exec( new String[]{"/usr/bin/xdg-open", path} );
    }
    else if( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported( Desktop.Action.OPEN ) )
    {
      Desktop.getDesktop().open( new File( path ) );
    }
    else
    {
      JOptionPane.showMessageDialog( instance(),
                                     "This action isn't supported on the current platform",
                                     "Cannot Open File",
                                     JOptionPane.ERROR_MESSAGE );
    }
  }

  public static ImageIcon loadLabIcon()
  {
    return EditorUtilities.loadIcon( "images/g_16.png" );
  }

  public static File getUserFile( GosuPanel gosuPanel )
  {
    File file = new File( getUserGosuEditorDir(), "layout.gosulab" );
    if( !file.isFile() )
    {
      Expando bindings = new Expando();
      bindings.put( "Title", "Gosu Lab" );
      bindings.put( "Version", 1 );
      bindings.put( "Experiments", Arrays.asList( makeScratchExperiment( gosuPanel ).getExperimentDir().getAbsolutePath() ) );

      try( FileWriter fw = new FileWriter( file ) )
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
    Bindings bindings = (Bindings)ReflectUtil.getProperty( getUserFile( gosuPanel ).toURI().toURL(), "JsonContent" );
    return (Integer)bindings.get( "Version" );
  }

  public Experiment loadRecentExperiment( GosuPanel gosuPanel )
  {
    Bindings bindings;
    try
    {
      bindings = (Bindings)ReflectUtil.getProperty( getUserFile( gosuPanel ).toURI().toURL(), "JsonContent" );
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }
    //noinspection unchecked
    restoreLabState( bindings );
    return new Experiment( new File( instance().getExperiments().get( 0 ) ), gosuPanel );
  }


  public void saveLabState( Experiment experiment )
  {
    if( !instance().isVisible() )
    {
      return;
    }

    File userFile = getUserFile( experiment.getGosuPanel() );
    try( FileWriter fw = new FileWriter( userFile ) )
    {
      Expando bindings = new Expando();

      bindings.put( "Title", "Gosu Lab" );
      bindings.put( "Version", 1 );

      addExperiment( experiment );
      bindings.put( "Experiments", instance().getExperiments() );

      saveScreenProps( bindings );

      IJsonIO.writeList( "Settings", _settings, bindings );

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

    _settings = IJsonIO.readList( "Settings", bindings );

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

  public List<ISettings> getSettings()
  {
    return _settings;
  }

  public ISettings getMruSettings()
  {
    return _mruSettings;
  }
  public void setMruSettings( ISettings settings )
  {
    _mruSettings = settings;
  }

  public ISettings findSettings( Predicate<ISettings> matcher )
  {
    if( matcher == null )
    {
      return null;
    }

    for( ISettings settings: _settings )
    {
      if( matcher.test( settings ) )
      {
        return settings;
      }
    }
    return null;
  }
}
