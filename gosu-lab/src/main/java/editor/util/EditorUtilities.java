package editor.util;

import editor.BasicGosuEditor;
import editor.GosuPanel;
import editor.RunMe;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuStringUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class EditorUtilities
{
  static final HashMap<String, ImageIcon> ICON_TABLE = new HashMap<String, ImageIcon>();

  /* colors */
  public static final Color ACTIVE_CAPTION = new Color( 210, 235, 251 );
  public static final Color ACTIVE_CAPTION_TEXT = Color.black;
  public static final Color CONTROL = new Color( 240, 240, 240 ); //UIManager.getColor( "control" );
  public static final Color CONTROL_DARKSHADOW = new Color( 105, 105, 105 ); // UIManager.getColor( "controlDkShadow" );
  public static final Color CONTROL_HIGHLIGHT = new Color( 227, 227, 227 ); //UIManager.getColor( "controlHighlight" );
  public static final Color CONTROL_LIGHT = Color.white; //UIManager.getColor( "controlLtHighlight" );
  public static final Color CONTROL_SHADOW = new Color( 160, 160, 160 ); //EditorUtilities.CONTROL_SHADOW;
  public static final Color CONTROL_TEXT = Color.black; //UIManager.getColor( "controlText" );
  public static final Color TOOLTIP_BACKGROUND = new Color( 255, 255, 225 ); //  UIManager.getColor( "info" );
  public static final Color TOOLTIP_TEXT = Color.black; //  UIManager.getColor( "infoText" );
  public static final Color WINDOW = new Color( 252, 252, 252 );
  public static final Color WINDOW_TEXT = Color.black;
  public static final Color WINDOW_BORDER = new Color( 100, 100, 100 );
  public static final Color TEXT_HIGHLIGHT = new Color( 51, 153, 255 );
  public static final Color TEXT_HIGHLIGHT_TEXT = Color.white;
  public static final Color TEXT_TEXT = Color.black;

  public static final Color XP_BORDER_COLOR = new Color( 49, 106, 197 );
  public static final Color XP_HIGHLIGHT_TOGGLE_COLOR = new Color( 225, 230, 232 );
  public static final Color XP_HIGHLIGHT_COLOR = ACTIVE_CAPTION;//new Color( 190, 205, 224 );
  public static final Color XP_HIGHLIGHT_SELECTED_COLOR = new Color( 152, 179, 219 );

  private static final String BACKGROUND_QUEUE_NAME = "backgroundTasks";


  static public void doBackgroundOp( final Runnable run )
  {
    editor.util.TaskQueue backgroundQueue = getBackgroundQueue();
    if( backgroundQueue != null )
    {
      backgroundQueue.postTask(
        new Runnable()
        {
          public void run()
          {
            run.run();
          }
        } );
    }
    else
    {
      run.run();
    }
  }

  private static editor.util.TaskQueue getBackgroundQueue()
  {
    return editor.util.TaskQueue.getInstance( BACKGROUND_QUEUE_NAME );
  }

  /**
   * Pumps through all current events in the background operation queue.  Note that this is *NOT* a settle.
   * Any operations added after this method is invoked will not be executed.
   */
  public static void settleBackgroundOps()
  {
    editor.util.TaskQueue backgroundQueue = getBackgroundQueue();
    if( backgroundQueue != null )
    {
      final Object wait = new Object();
      synchronized( wait )
      {
        doBackgroundOp( new Runnable()
        {
          public void run()
          {
            synchronized( wait )
            {
              wait.notifyAll();
            }
          }
        } );

        try
        {
          wait.wait();
        }
        catch( InterruptedException e )
        {
          //ignore
        }
      }
    }
  }

  public static void removePopupBorder( final Container c )
  {
    EventQueue.invokeLater(
      new Runnable()
      {
        public void run()
        {
          Container p = c;
          while( p != null )
          {
            if( p instanceof JComponent )
            {
              ((JComponent)p).setBorder( null );
            }
            p = p.getParent();
          }
        }
      } );
  }

  public static void centerWindowInFrame( Component window, Window frame )
  {
    // Note FileDialog instances in jdk 1.4.1 have no size until FileDialog.show()
    // which blocks i.e., we can't center FileDialogs.

    Dimension dimCenter;
    Point ptOffset;
    if( frame != null )
    {
      if( (!(frame instanceof Frame) || ((Frame)frame).getState() != Frame.ICONIFIED) && frame.isShowing() )
      {
        // Center in frame
        dimCenter = frame.getSize();
        ptOffset = frame.getLocation();
      }
      else
      {
        // Retry either containing window or screen
        centerWindowInFrame( window, null );
        return;
      }
    }
    else
    {
      Window owner = SwingUtilities.getWindowAncestor( window );
      if( owner != null && owner.isShowing() && (owner.getMinimumSize().height < owner.getHeight()) )
      {
        // Retry with owner window as frame
        if( (!(owner instanceof Frame) || ((Frame)owner).getState() != Frame.ICONIFIED) && owner.isShowing() )
        {
          // Center in frame
          dimCenter = owner.getSize();
          ptOffset = owner.getLocation();
          window.setLocation( ptOffset.x + (dimCenter.width - window.getWidth()) / 2, ptOffset.y + (dimCenter.height - window.getHeight()) / 2 );
        }

        return;
      }

      // Center in screen
      Rectangle screenRect = getPrimaryMonitorScreenRect();
      dimCenter = new Dimension( (int)screenRect.getWidth(), (int)screenRect.getHeight() );
      ptOffset = new Point( (int)screenRect.getX(), (int)screenRect.getY() );
    }

    window.setLocation( ptOffset.x + (dimCenter.width - window.getWidth()) / 2, ptOffset.y + (dimCenter.height - window.getHeight()) / 2 );
  }


  public static Rectangle getPrimaryMonitorScreenRect()
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gds = ge.getScreenDevices();
    return gds[0].getConfigurations()[0].getBounds();
  }

  public static ImageIcon loadIcon( String strRes )
  {
    if( strRes == null || strRes.length() == 0 )
    {
      return null;
    }

    ImageIcon icon = ICON_TABLE.get( strRes );
    if( icon == null && !strRes.contains( " | " ) )
    {
      try
      {
        URL resource = editor.util.EditorUtilities.class.getClassLoader().getResource( strRes );
        if( resource != null )
        {
          icon = new ImageIcon( resource );
        }
      }
      catch( Exception e )
      {
        // just return null
      }
      if( icon != null )
      {
        ICON_TABLE.put( strRes, icon );
      }
    }

    return icon;

  }

  public static Icon findIcon( File fileOrDir )
  {
    if( fileOrDir.isDirectory() )
    {
      if( RunMe.getEditorFrame().getGosuPanel().getExperimentView().getExperiment().getSourcePath().contains( fileOrDir.getAbsolutePath() ) )
      {
        return loadIcon( "images/srcfolder.png" );
      }
      return loadIcon( "images/folder.png" );
    }

    String classNameForFile = TypeNameUtil.getClassNameForFile( fileOrDir );
    if( classNameForFile != null )
    {
      IType type = TypeSystem.getByFullNameIfValid( classNameForFile );
      if( type != null )
      {
        return findIcon( type );
      }
    }
    return FileSystemView.getFileSystemView().getSystemIcon( fileOrDir );
  }

  public static Icon findIcon( IType type )
  {
    if( type instanceof IGosuClass )
    {
      if( type.isInterface() )
      {
        if( ((IGosuClass)type).isStructure() )
        {
          return findIcon( ClassType.Structure );
        }
        else if( ((IGosuClass)type).isAnnotation() )
        {
          return findIcon( ClassType.Annotation );
        }
        return findIcon( ClassType.Interface );
      }
      else if( type instanceof ITemplateType )
      {
        return findIcon( ClassType.Template );
      }
      else if( type instanceof IGosuEnhancement )
      {
        return findIcon( ClassType.Enhancement );
      }
      else if( type instanceof IGosuProgram )
      {
        return findIcon( ClassType.Program );
      }
      else if( type.isEnum() )
      {
        return findIcon( ClassType.Enum );
      }
      else
      {
        return findIcon( ClassType.Class );
      }
    }
    else if( type instanceof IJavaType )
    {
      return EditorUtilities.loadIcon( "images/javaclass.png" );
    }
    return EditorUtilities.loadIcon( "images/empty16x16.gif" );
  }

  public static Icon findIcon( ClassType classType )
  {
    switch( classType )
    {
      case Class:
        return EditorUtilities.loadIcon( "images/class.png" );
      case Enum:
        return EditorUtilities.loadIcon( "images/enum.png" );
      case Interface:
        return EditorUtilities.loadIcon( "images/interface.png" );
      case Structure:
        return EditorUtilities.loadIcon( "images/structure.png" );
      case Annotation:
        return EditorUtilities.loadIcon( "images/annotation.png" );
      case Enhancement:
        return EditorUtilities.loadIcon( "images/Enhancement.png" );
      case Program:
        return EditorUtilities.loadIcon( "images/program.png" );
      case Template:
        return EditorUtilities.loadIcon( "images/template.png" );
    }
    return null;
  }

  public static void handleUncaughtException( Throwable e )
  {
    handleUncaughtException( "", e );
  }

  public static void handleUncaughtException( String s, Throwable e )
  {
    System.out.println( s );
    e.printStackTrace();
  }

  public static void settleEventQueue()
  {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    while( eventQueue.peekEvent() != null )
    {
      try
      {
        AWTEvent event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
        Object src = event.getSource();
        if( event instanceof ActiveEvent )
        {
          ((ActiveEvent)event).dispatch();
        }
        else if( src instanceof Component )
        {
          ((Component)src).dispatchEvent( event );
        }
        else if( src instanceof MenuComponent )
        {
          ((MenuComponent)src).dispatchEvent( event );
        }
      }
      catch( Throwable e )
      {
        handleUncaughtException( "", e );
      }
    }

  }

  public static Clipboard getClipboard()
  {
    return RunMe.getEditorFrame().getGosuPanel().getClipboard();
  }

  public static Frame frameForComponent( Component comp )
  {
    for( Component p = comp; p != null; p = p.getParent() )
    {
      if( p instanceof Frame )
      {
        return (Frame)p;
      }
    }

    return null;
  }

  public static String buildFunctionIntellisenseString( boolean bFeatureLiteralCompletion, IFunctionType functionType )
  {
    StringBuilder sb = new StringBuilder();
    String rawName = functionType.getDisplayName();
    sb.append( rawName );
    sb.append( "(" );
    buildArgListFromType( functionType, sb, true, bFeatureLiteralCompletion );
    sb.append( ")" );
    return sb.toString();
  }

  private static void buildArgListFromType( IFunctionType functionType, StringBuilder sb, boolean topLevel, boolean bFeatureLiteralCompletion )
  {
    IType[] parameters = functionType.getParameterTypes();
    HashSet<String> generatedNames = new HashSet<String>();
    for( int i = 0; i < parameters.length; i++ )
    {
      if( i != 0 )
      {
        sb.append( "," );
      }
      sb.append( " " );

      IType paramType = parameters[i];
      if( bFeatureLiteralCompletion )
      {
        sb.append( paramType.getRelativeName() );
      }
      else if( topLevel && paramType instanceof IFunctionType )
      {
        IFunctionType blockType = (IFunctionType)paramType;
        sb.append( "\\" );

        buildArgListFromType( blockType, sb, false, bFeatureLiteralCompletion );

        sb.append( "-> " );
      }
      else
      {
        //If we have a method info, we can use the actual parameter name
        String name;
        if( functionType.getMethodInfo() != null )
        {
          IParameterInfo info = functionType.getMethodInfo().getParameters()[i];
          name = info.getName();
        }
        else
        {
          name = createUniqueParamNameFromType( paramType, generatedNames );
        }
        sb.append( GosuStringUtil.uncapitalize( name ) );
      }
    }

    if( parameters.length > 0 )
    {
      sb.append( " " );
    }
  }

  private static String createUniqueParamNameFromType( IType paramType, HashSet<String> generatedNames )
  {
    String initialName = paramType.getRelativeName();
    if( !GosuStringUtil.isAlphanumeric( initialName.substring( 0, 1 ) ) )
    {
      initialName = paramType.getDisplayName();
    }
    initialName = initialName.substring( 0, 1 );
    initialName = initialName.toLowerCase();
    String name = initialName;
    int j = 2;
    while( generatedNames.contains( name ) )
    {
      name = initialName + j;
      j++;
    }
    generatedNames.add( name );
    return name;
  }

  public static JButton createArrowButton()
  {
    return new BasicArrowButton( BasicArrowButton.SOUTH,
                                 UIManager.getColor( "ComboBox.buttonBackground" ),
                                 UIManager.getColor( "ComboBox.buttonShadow" ),
                                 UIManager.getColor( "ComboBox.buttonDarkShadow" ),
                                 UIManager.getColor( "ComboBox.buttonHighlight" ) );
  }

  public static Window getWindow()
  {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
  }

  public static Image createSystemColorImage( Image i )
  {
    SystemColorFilter filter = new SystemColorFilter();
    ImageProducer prod = new FilteredImageSource( i.getSource(), filter );
    return Toolkit.getDefaultToolkit().createImage( prod );
  }

  public static void displayInformation( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.INFORMATION_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayError( Throwable e )
  {
    displayMessageBox( e == null ? "Error performing operation." : e.getMessage(), JOptionPane.ERROR_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayError( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.ERROR_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayWarning( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.WARNING_MESSAGE, false );
  }

  //--------------------------------------------------------------------------------------------------
  public static void displayMessageBox( String strMsg, final int iType, boolean bWrapText )
  {
    final String strWrappedMsg = bWrapText ? wrapText( strMsg ) : strMsg;

    Runnable logMsgBox = new Runnable()
    {
      public void run()
      {
        JOptionPane.showMessageDialog( getWindow(), strWrappedMsg, "", iType );
      }
    };

    if( EventQueue.isDispatchThread() )
    {
      logMsgBox.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( logMsgBox );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }
  }

  public static String wrapText( String strText )
  {
    return wrapText( strText, 60 );
  }

  public static String wrapText( String strText, int iLineLen )
  {
    StringBuilder sb = new StringBuilder();
    while( strText != null )
    {
      if( strText.length() > iLineLen )
      {
        sb.append( strText.substring( 0, 60 ) ).append( "\n" );
        strText = strText.substring( 60 );
      }
      else
      {
        sb.append( strText );
        strText = null;
      }
    }
    return sb.toString();
  }

  public static void convertRectangleToScreen( Rectangle rectangle, Component component )
  {
    Point loc = rectangle.getLocation();
    SwingUtilities.convertPointToScreen( loc, component );
    rectangle.setLocation( loc );
  }

  public static java.util.List<String> filterStrings( Collection<? extends CharSequence> collection, String filter )
  {
    if( filter != null )
    {
      int iDotIndex = filter.lastIndexOf( '.' );
      if( iDotIndex >= 0 )
      {
        filter = filter.substring( iDotIndex + 1 );
      }
    }

    java.util.List<String> filteredTypes = new ArrayList<String>();
    if( filter != null && filter.length() > 0 )
    {
      int iFlags = 0;
      if( filter.indexOf( '*' ) < 0 &&
          Character.isUpperCase( filter.charAt( 0 ) ) )
      {
        filter = camelCasePrefix( filter );
      }
      else
      {
        iFlags = Pattern.CASE_INSENSITIVE;
      }

      // Replace all wildcard '*' chars with the regex ".*" expression
      filter = filter.replaceAll( "\\*", "\\.\\*" );

      // A '#' char indicates that the proper regex syntax has already been embedded in the string;
      // we only need to replace the '#' with '*'
      filter = filter.replaceAll( "\\#", "\\*" );

      boolean bHasDot = filter.indexOf( '~' ) >= 0;
      filter = filter.replaceAll( "~", "(\\\\.|" + '\u2024' + ")" );

      // Match the expression string followed by any number of chars
      try
      {
        Pattern pattern = Pattern.compile( filter + (filter.startsWith( ".*" ) ? "" : ".*"), iFlags );
        for( CharSequence cs : collection )
        {
          String strType = cs.toString();
          String strName = bHasDot ? strType : getRelativeTypeName( strType );
          if( pattern.matcher( strName ).find() )
          {
            filteredTypes.add( strType );
          }
        }
      }
      catch( PatternSyntaxException e )
      {
        // Skip
      }
    }
    return filteredTypes;
  }

  private static String camelCasePrefix( String strPrefix )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = strPrefix.length() - 1; i >= 0; i-- )
    {
      char c = strPrefix.charAt( i );
      sb.insert( 0, c );
      if( i != 0 && Character.isUpperCase( c ) )
      {
        // Each uppercase char in the prefix match all but uppercase chars preceding it e.g.,
        // "AcT" matches any string starting with "Ac" followed by any number of non-uppercase chars followed by "T".
        // It's the same as the regex "Ac[^A-Z]*T"
        sb.insert( 0, "[^A-Z]#" );
      }
    }
    return sb.toString();
  }

  private static String getRelativeTypeName( String strType )
  {
    int iIndex = strType.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      return strType.substring( iIndex + 1 );
    }
    return strType;
  }

  public static void hideToolTip( JComponent c )
  {
    try
    {
      Method hideMethod = ToolTipManager.class.getDeclaredMethod( "hide", JComponent.class );
      hideMethod.setAccessible( true );
      hideMethod.invoke( ToolTipManager.sharedInstance(), c );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }


  public static JRootPane rootPaneForComponent( Component comp )
  {
    for( Component p = comp; p != null; p = p.getParent() )
    {
      if( p instanceof JRootPane )
      {
        return (JRootPane)p;
      }

      if( comp instanceof JFrame )
      {
        return ((JFrame)comp).getRootPane();
      }

      if( comp instanceof JDialog )
      {
        return ((JDialog)comp).getRootPane();
      }

      if( comp instanceof JWindow )
      {
        return ((JWindow)comp).getRootPane();
      }
    }

    return null;
  }

  public static void invokeInDispatchThread( Runnable task )
  {
    if( task == null )
    {
      return;
    }

    if( EventQueue.isDispatchThread() )
    {
      task.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( task );
      }
      catch( Throwable t )
      {
        handleUncaughtException( t );
      }
    }
  }

  public static Process browse( String strURL ) throws IOException
  {
    String strCmd;

    if( PlatformUtil.isWindows() )
    {
      strCmd = "rundll32 url.dll,FileProtocolHandler " + strURL;
    }
    else
    {
      //## todo: barf
      strCmd = "firefox " + strURL;
    }
    return Runtime.getRuntime().exec( strCmd );
  }

  /**
   * Finds the first widget above the passed in widget of the given class
   */
  public static <T> T findAncestor( Component start, Class<T> aClass )
  {
    if( start == null )
    {
      return null;
    }
    return findAtOrAbove( start.getParent(), aClass );
  }

  /**
   * Finds the first widget at or above the passed in widget of the given class
   */
  public static <T> T findAtOrAbove( Component start, Class<T> aClass )
  {
    Component comp = start;
    while( comp != null )
    {
      if( aClass.isInstance( comp ) )
      {
        return (T)comp;
      }
      else
      {
        comp = comp.getParent();
      }
    }
    return null;
  }

  public static File getUserFile( GosuPanel gosuPanel )
  {
    File file = new File( getUserGosuEditorDir(), "layout.properties" );
    if( !file.isFile() )
    {
      Properties props = new Properties();
      props.put( "experiment", makeScratchExperiment( gosuPanel ).getExperimentDir().getAbsolutePath() );

      try( FileWriter writer = new FileWriter( file ) )
      {
        props.store( writer, "Gosu Editor" );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return file;
  }

  public static Experiment loadRecentExperiment( GosuPanel gosuPanel )
  {
    File userFile = getUserFile( gosuPanel );
    Properties props = new Properties();
    try( FileReader reader = new FileReader( userFile ) )
    {
      props.load( reader );
      //noinspection unchecked
      restoreScreenProps( (Map)props );
      return new Experiment( new File( props.getProperty( "experiment" ) ), gosuPanel );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public static void saveLayoutState( Experiment experiment ) {
    if( !RunMe.getEditorFrame().isVisible() )
    {
      return;
    }

    File userFile = getUserFile( experiment.getGosuPanel() );
    try( FileWriter writer = new FileWriter( userFile ) )
    {
      Properties props = new Properties();

      props.put( "experiment", experiment.getExperimentDir().getAbsolutePath() );

      //noinspection unchecked
      saveScreenProps( (Map)props );

      props.store( writer, "Gosu Editor" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void saveScreenProps( Map<String, String> props )
  {
    BasicGosuEditor frame = RunMe.getEditorFrame();
    boolean maximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    props.put( "Frame.Maximized", String.valueOf( maximized ) );
    Rectangle bounds = frame.getRestoreBounds();
    if( bounds != null )
    {
      ScreenUtil.convertToPercentageOfScreenWidth( bounds );
      props.put( "Frame.Bounds.X", String.valueOf( bounds.x ) );
      props.put( "Frame.Bounds.Y", String.valueOf( bounds.y ) );
      props.put( "Frame.Bounds.Width", String.valueOf( bounds.width ) );
      props.put( "Frame.Bounds.Height", String.valueOf( bounds.height ) );
    }
  }
  private static void restoreScreenProps( Map<String, String> props )
  {
    BasicGosuEditor frame = RunMe.getEditorFrame();

    boolean bSet = false;
    Integer x = readInteger( props, "Frame.Bounds.X" );
    if( x != null )
    {
      Integer y = readInteger( props, "Frame.Bounds.Y" );
      if( y != null )
      {
        Integer width = readInteger( props, "Frame.Bounds.Width" );
        if( width != null )
        {
          Integer height = readInteger( props, "Frame.Bounds.Height" );
          if( height != null )
          {
            Rectangle bounds = new Rectangle( x, y, width, height );
            ScreenUtil.convertFromPercentageOfScreenWidth( bounds );
            frame.setBounds( bounds );
            frame.setRestoreBounds( bounds );
            bSet = true;
          }
        }
      }
    }

    if( !bSet )
    {
      setInitialFrameBounds( RunMe.getEditorFrame() );
    }

    if( Boolean.valueOf( props.get( "Frame.Maximized" ) ) == Boolean.TRUE )
    {
      frame.setExtendedState( Frame.MAXIMIZED_BOTH );
    }
  }
  private static Integer readInteger( Map<String, String> props, String prop )
  {
    String value = props.get( prop );
    if( value == null )
    {
      return null;
    }
    return Integer.valueOf( value );
  }

  private static void setInitialFrameBounds( Frame frame )
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width*2/3;
    int height = width*2/3;
    frame.setSize( width, height );
    EditorUtilities.centerWindowInFrame( frame, frame );
  }

  public static File getUserGosuEditorDir()
  {
    File gosuDir = new File( System.getProperty( "user.home" ), ".GosuEditor" );
    //noinspection ResultOfMethodCallIgnored
    gosuDir.mkdirs();
    return gosuDir;
  }

  public static File getStockExperimentsDir()
  {
    File gosuDir = new File( System.getProperty( "user.home" ) + File.separator + ".GosuEditor" + File.separator + "experiments" );
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
    File gosuDir = new File( System.getProperty( "user.home" ) + File.separator + ".GosuEditor" + File.separator + "examples" );
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

  public static File findExperimentFile(File dir) {
    for( File f : dir.listFiles() )
    {
      if(f.getName().equalsIgnoreCase( dir.getName() + ".prj" ))
      {
        return f;
      }
    }
    return null;
  }

  private static Experiment makeScratchExperiment( GosuPanel gosuPanel )
  {
    File experimentDir = new File( getStockExamplesDir(), "Scratch" );
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
      JOptionPane.showMessageDialog( RunMe.getEditorFrame(),
                                     "This action isn't supported on the current platform",
                                     "Cannot Open File",
                                     JOptionPane.ERROR_MESSAGE );
    }
  }

  public static void delete( File fileOrDir )
  {
    if( fileOrDir.isDirectory() )
    {
      for( File f : fileOrDir.listFiles() )
      {
        if( f.isDirectory() )
        {
          delete( f );
        }
        else
        {
          //noinspection ResultOfMethodCallIgnored
          f.delete();
        }
      }
    }
    //noinspection ResultOfMethodCallIgnored
    fileOrDir.delete();
  }

  public static ImageIcon loadLabIcon()
  {
    return loadIcon( "images/project4.png" );
  }
}