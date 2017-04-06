package editor.util;

import editor.LabFrame;
import editor.Scheme;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import java.nio.file.Path;
import gw.util.PathUtil;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

public class EditorUtilities
{
  static final HashMap<String, ImageIcon> ICON_TABLE = new HashMap<>();

  private static final String BACKGROUND_QUEUE_NAME = "backgroundTasks";

  static Map<Component, Boolean> CONTAINS_FOCUS;
  static Map<Component, Boolean> FOCUS_CONTAINS;

  /**
   * Platform dependent keystroke info
   */
  public static final String CONTROL_KEY_NAME;
  public static final int CONTROL_KEY_MASK;

  static
  {
    if( PlatformUtil.isMac() )
    {
      CONTROL_KEY_MASK = KeyEvent.META_MASK;
      CONTROL_KEY_NAME = "meta";
    }
    else
    {
      CONTROL_KEY_MASK = KeyEvent.CTRL_MASK;
      CONTROL_KEY_NAME = "control";
    }
  }

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
      () -> {
        Container p = c;
        while( p != null )
        {
          if( p instanceof JComponent )
          {
            ((JComponent)p).setBorder( null );
          }
          p = p.getParent();
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
        if( Scheme.active().isDark() && !strRes.contains( "_dark." ) )
        {
          int iDot = strRes.lastIndexOf( '.' );
          if( iDot >= 0 )
          {
            String strResDark = strRes.substring( 0, iDot ) + "_dark" + strRes.substring( iDot );
            icon = loadIcon( strResDark );
          }
        }
        if( icon == null )
        {
          URL resource = editor.util.EditorUtilities.class.getClassLoader().getResource( strRes );
          if( resource != null )
          {
            icon = new ImageIcon( resource );
          }
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

  public static Icon findIcon( Path fileOrDir )
  {
    if( PathUtil.isDirectory( fileOrDir ) )
    {
      if( LabFrame.instance().getGosuPanel().getExperimentView().getExperiment().getSourcePath().contains( PathUtil.getAbsolutePathName( fileOrDir ) ) )
      {
        return loadIcon( "images/srcfolder.png" );
      }
      return loadIcon( "images/folder.png" );
    }

    String classNameForFile = TypeNameUtil.getTypeNameForFile( fileOrDir );
    if( classNameForFile != null )
    {
      IType type = TypeSystem.getByFullNameIfValid( classNameForFile );
      if( type != null )
      {
        return findIcon( type );
      }
    }
    if( PathUtil.isFile( fileOrDir ) )
    {
      try
      {
        return FileSystemView.getFileSystemView().getSystemIcon( fileOrDir.toFile() );
      }
      catch( UnsupportedOperationException e )
      {
        // eat
      }
    }
    return loadIcon( "images/FileText.png" );
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
    else if( type != null )
    {
      ITypeFactory factory = type.getTypeLoader().getInterface( ITypeFactory.class );
      if( factory != null )
      {
        return EditorUtilities.loadIcon( factory.getIcon() );
      }
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
      case JavaClass:
        return EditorUtilities.loadIcon( "images/javaclass.png" );
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
    return LabFrame.instance().getGosuPanel().getClipboard();
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

  public static Window windowForComponent( Component comp )
  {
    for( Component p = comp; p != null; p = p.getParent() )
    {
      if( p instanceof Window )
      {
        return (Window)p;
      }
    }

    return null;
  }

  public static String buildFunctionIntellisenseString( boolean bFeatureLiteralCompletion, IFunctionType functionType )
  {
    StringBuilder sb = new StringBuilder();
    String rawName = functionType.getDisplayName();
    sb.append( rawName );
    sb.append( "()" );
    return sb.toString();
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

  public static void invokeNowOrLater( Runnable task )
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
        EventQueue.invokeLater( task );
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

  public static <T> List<T> findDecendents( Component configUI, Class<T> aClass )
  {
    return findDecendents( configUI, aClass, c -> true );
  }

  private static <T> void _findDecendents( ArrayList<T> comps, Component component, Class<T> aClass, Predicate<Container> recurseToChildren )
  {
    if( aClass.isInstance( component ) )
    {
      //noinspection unchecked
      comps.add( (T)component );
    }
    if( component instanceof Container )
    {
      Container container = (Container)component;
      Component[] components = container.getComponents();
      if( recurseToChildren.test( container ) )
      {
        for( Component child : components )
        {
          _findDecendents( comps, child, aClass, recurseToChildren );
        }
        if( container instanceof JMenu )
        {
          JPopupMenu popupMenu = ((JMenu)container).getPopupMenu();
          if( popupMenu != null )
          {
            _findDecendents( comps, popupMenu, aClass, recurseToChildren );
          }
        }
      }
    }
  }

  public static <T> List<T> findDecendents( Component configUI, Class<T> aClass, Predicate<Container> recurseToChildren )
  {
    ArrayList<T> comps = new ArrayList<>();
    _findDecendents( comps, configUI, aClass, recurseToChildren );
    return comps;
  }

  public static boolean isRunnable( IType type )
  {
    if( type == null || !type.isValid() )
    {
      return false;
    }
    if( LabFrame.instance().getGosuPanel().isRunning() ||
        LabFrame.instance().getGosuPanel().isDebugging() )
    {
      return false;
    }

    // Is Program?
    if( type instanceof IGosuProgram )
    {
      return true;
    }

    if( type instanceof IGosuClass && !type.isAbstract() &&
        ((IGosuClassTypeInfo)type.getTypeInfo()).isPublic() )
    {
      // Is Main class?
      IMethodInfo main = type.getTypeInfo().getMethod( "main", JavaTypes.STRING().getArrayType() );
      if( main != null && main.isStatic() && main.getReturnType() == JavaTypes.pVOID() )
      {
        return true;
      }

      // Is Test class?
      if( type.getTypeInfo().getConstructor() != null )
      {
        IType baseTest = TypeSystem.getByFullNameIfValid( "junit.framework.Assert" );
        if( baseTest != null )
        {
          return baseTest.isAssignableFrom( type );
        }
        return type.getName().endsWith( "Test" );
      }
    }
    return false;
  }

  public static void fixSwingFocusBugWhenPopupCloses( Component c )
  {
    // This is a fix to workaround a bug with Swing JPopupMenu.  Withou this
    // focus is stolen from a subsequent selected field. See Bug CC-1140.
    editor.util.EditorUtilities.rootPaneForComponent( c ).dispatchEvent( new MouseEvent( c, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 3, 3, 1, false ) );
  }

  public static Component showWaitCursor( final boolean bWait )
  {
    return WaitCursorRunner.showWaitCursor( bWait );
  }

  public static void showWaitCursor( boolean bWait, Component c )
  {
    WaitCursorRunner.showWaitCursor( bWait, c );
  }

  public static void doWaitOperation( Runnable op )
  {
    Component key = showWaitCursor( true );
    try
    {
      op.run();
    }
    finally
    {
      showWaitCursor( false, key );
    }
  }

  public static Component getFocus()
  {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
  }

  public static Window getFocusedWindow()
  {
    return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
  }

  public static Window getActiveWindow()
  {
    Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
    if( activeWindow == null )
    {
      Frame[] frames = Frame.getFrames();
      if( frames != null && frames.length > 0 )
      {
        return frames[0];
      }
    }
    return activeWindow;
  }

  public static boolean containsFocus( Component c )
  {
    addFocusListener();
    Boolean containsFocus = CONTAINS_FOCUS.get( c );
    if( containsFocus != null )
    {
      return containsFocus;
    }
    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
    // Verify focusOwner is a descendant of c
    for( Component temp = focusOwner; temp != null; temp = (temp instanceof Window) ? null : temp.getParent() )
    {
      if( temp == c )
      {
        CONTAINS_FOCUS.put( c, true );
        return true;
      }
    }
    CONTAINS_FOCUS.put( c, false );
    return false;
  }

  public static boolean focusContains( Component c )
  {
    addFocusListener();

    Boolean focusContains = FOCUS_CONTAINS.get( c );
    if( focusContains != null )
    {
      return focusContains;
    }
    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
    // Verify c is a descendant of focusOwner
    for( Component temp = c; temp != null; temp = (temp instanceof Window) ? null : temp.getParent() )
    {
      if( temp == focusOwner )
      {
        FOCUS_CONTAINS.put( c, true );
        return true;
      }
    }
    FOCUS_CONTAINS.put( c, false );
    return false;
  }

  private static void addFocusListener()
  {
    if( CONTAINS_FOCUS != null )
    {
      return;
    }
    CONTAINS_FOCUS = new HashMap<>();
    FOCUS_CONTAINS = new HashMap<>();
    KeyboardFocusManager focusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusMgr.addPropertyChangeListener( "permanentFocusOwner",
                                        evt -> {
                                          CONTAINS_FOCUS.clear();
                                          FOCUS_CONTAINS.clear();
                                        } );
  }

  public static boolean isInFocusLineage( Component c )
  {
    return containsFocus( c ) || focusContains( c );
  }

  public static Point getXYForDialogRelativeToStudioFrame( int width, int height )
  {
    Rectangle screenRect = getPrimaryMonitorScreenRect();
    return new Point( (int)(screenRect.getX() + (screenRect.getWidth() - width) / 2),
                      (int)(screenRect.getY() + (screenRect.getHeight() - height) / 2) );
  }

  public static String hex( Color color )
  {
    return Integer.toHexString( (color.getRGB() & 0xffffff) | 0x1000000 ).substring( 1 );
  }

  public static String getFontFamilyOrDefault( String name, String defaultFont )
  {
    if( Arrays.stream( GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames() ).anyMatch( e -> e.equalsIgnoreCase( name ) ) )
    {
      return name;
    }
    return defaultFont;
  }
}