package editor.search;

import editor.BasicGosuEditor;
import editor.RunMe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static editor.util.EditorUtilities.handleUncaughtException;

/**
 * Sundry ui utility methods.
 */
public class StudioUtilities implements SwingConstants
{
  static final HashMap<String, ImageIcon> ICON_TABLE = new HashMap<String, ImageIcon>();

  static protected Rectangle g_rcClipCursor;
  static protected ClipCursorHandler g_clipCursorListener;
  static protected Robot g_robot;

  static final Rectangle RCTEXT = new Rectangle();
  static final Rectangle SHOULDPAINT_RECT = new Rectangle();
  static final Rectangle SHOULDPAINT_RECT_IN = new Rectangle();
  static Map<Component, Boolean> CONTAINS_FOCUS;
  static Map<Component, Boolean> FOCUS_CONTAINS;

  private StudioUtilities()
  {
  }

  public static void enableComponent( Component c, boolean bEnabled )
  {
    if( c.isEnabled() != bEnabled )
    {
      c.setEnabled( bEnabled );
    }

    if( c instanceof Container )
    {
      Component[] children = ((Container)c).getComponents();
      if( children != null )
      {
        for( Component child : children )
        {
          enableComponent( child, bEnabled );
        }
      }
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
    CONTAINS_FOCUS = new HashMap<Component, Boolean>();
    FOCUS_CONTAINS = new HashMap<Component, Boolean>();
    KeyboardFocusManager focusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusMgr.addPropertyChangeListener( "permanentFocusOwner",
                                        new PropertyChangeListener()
                                        {
                                          public void propertyChange( PropertyChangeEvent evt )
                                          {
                                            CONTAINS_FOCUS.clear();
                                            FOCUS_CONTAINS.clear();
                                          }
                                        } );
  }

  public static boolean isInFocusLineage( Component c )
  {
    return containsFocus( c ) || focusContains( c );
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

//  public Point getCenterOfActiveWindow()
//  {
//    Window activeWin = getActiveWindow();
//    Rectangle rc = null;
//    if( activeWin == null )
//    {
//      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//      rc = new Rectangle( 0, 0, dim.width, dim.height );
//    }
//    else
//    {
//      rc = activeWin.getBounds();
//    }
//  }

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

  /**
   * @param comp the component to analyze
   *
   * @return the root pane for the component
   */
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
    Component key = StudioUtilities.showWaitCursor( true );
    try
    {
      op.run();
    }
    finally
    {
      showWaitCursor( false, key );
    }
  }

  public static String wrapText( String strText )
  {
    return wrapText( strText, 60 );
  }

  public static String wrapText( String strText, int iLineLen )
  {
    Vector<String> vStrings = drawTextWrapped( null, new Rectangle(), strText, new Font( "Tahoma", Font.PLAIN, 11 ), iLineLen, false, true );
    String strRet = "";
    for( int i = 0; i < vStrings.size(); i++ )
    {
      strRet += vStrings.elementAt( i );
    }

    return strRet;
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

  public static Rectangle drawStringInRectClipped( Graphics g, String strText, Rectangle rcClip, int iHorzAlign, int iVertAlign )
  {
    return drawStringInRectClipped( g, null, strText, rcClip, iHorzAlign, iVertAlign, true );
  }

  public static Rectangle drawStringInRectClipped( Graphics g, FontMetrics fm, String strText, Rectangle rcClip, int iHorzAlign, int iVertAlign, boolean bReturnBounds )
  {
    RCTEXT.setBounds( 0, 0, 0, 0 );
    MessageDisplay.SCRATCH_RECT.setBounds( 0, 0, 0, 0 );
    fm = fm == null ? g.getFontMetrics() : fm;

    String str = SwingUtilities.layoutCompoundLabel( fm,
                                                     strText,
                                                     null, // no icon
                                                     iVertAlign, // vertical alignment
                                                     iHorzAlign, // horizontal alignment
                                                     BOTTOM, // vertical text pos relative to icon
                                                     RIGHT, // horizontal text pos relative to icon
                                                     rcClip,
                                                     MessageDisplay.SCRATCH_RECT,
                                                     RCTEXT,
                                                     0 );    // text/icon gap

    if( str != null && !str.equals( "" ) )
    {
      g.drawString( str, RCTEXT.x, RCTEXT.y + fm.getAscent() );
    }

    return bReturnBounds ? new Rectangle( RCTEXT ) : null;
  }

  public static void drawStringInRect( Graphics g, String strText, Rectangle rc, int justification, boolean bClip )
  {
    drawStringInRect( g, strText, rc.x, rc.y, rc.width, rc.height, justification, bClip );
  }

  public static void drawStringInRect( Graphics g, String strText, int x, int y,
                                       int width, int height, int justification, boolean bClip )
  {
    FontMetrics fontMetrics;
    int drawWidth, startX, startY, delta;

    if( g.getFont() == null )
    {
      return;
    }
    fontMetrics = g.getFontMetrics();
    if( fontMetrics == null )
    {
      return;
    }

    if( bClip )
    {
      MessageDisplay.SCRATCH_RECT.setBounds( g.getClipBounds() );

      MessageDisplay.SCRATCH_RECT2.setBounds( x, y, width, height );
      if( MessageDisplay.SCRATCH_RECT.intersects( MessageDisplay.SCRATCH_RECT2 ) )
      {
        MessageDisplay.SCRATCH_RECT2.setBounds( MessageDisplay.SCRATCH_RECT.intersection( MessageDisplay.SCRATCH_RECT2 ) );
        g.setClip( MessageDisplay.SCRATCH_RECT2 );
      }
      else
      {
        return;
      }
    }

    if( justification == CENTER )
    {
      drawWidth = fontMetrics.stringWidth( strText );
      if( drawWidth > width )
      {
        drawWidth = width;
      }
      startX = x + (width - drawWidth) / 2;
    }
    else if( justification == RIGHT )
    {
      drawWidth = fontMetrics.stringWidth( strText );
      if( drawWidth > width )
      {
        drawWidth = width;
      }
      startX = x + width - drawWidth;
    }
    else
    {
      startX = x;
    }

    delta = (height - fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;
    if( delta < 0 )
    {
      delta = 0;
    }

    startY = y + height - delta - fontMetrics.getDescent();

    g.drawString( strText, startX, startY );

    if( bClip )
    {
      g.setClip( MessageDisplay.SCRATCH_RECT );
    }
  }

  public static boolean shouldPaint( Graphics g, Component comp )
  {
    Rectangle rcBounds = comp.getBounds();
    return shouldPaint( g, rcBounds.x, rcBounds.y, rcBounds.width, rcBounds.height );
  }

  public static boolean shouldPaint( Graphics g, int x, int y, int iWidth, int iHeight )
  {
    Rectangle rcClipBounds = g.getClipBounds();
    if( rcClipBounds == null )
    {
      return true;
    }

    SHOULDPAINT_RECT.setBounds( rcClipBounds );
    SHOULDPAINT_RECT_IN.setBounds( x, y, iWidth, iHeight );

    return SHOULDPAINT_RECT.intersects( SHOULDPAINT_RECT_IN );
  }

  public static Vector<String> drawTextWrapped( Graphics g, Rectangle rect, String strText )
  {
    return drawTextWrapped( g, rect, strText, null, 0, true, true );
  }

  public static Vector<String> drawTextWrapped( Graphics g, Rectangle rect, String strText, Font font, int iLineLengthChars, boolean bClip, boolean bAppendLineFeed )
  {
    try
    {
      Vector<String> vStrings = (g == null) ? new Vector<String>() : null;

      if( bClip && (g != null) && (rect != null) && !rect.isEmpty() )
      {
        MessageDisplay.SCRATCH_RECT.setBounds( g.getClipBounds() );

        MessageDisplay.SCRATCH_RECT2.setBounds( rect.x, rect.y, rect.width, rect.height );
        if( MessageDisplay.SCRATCH_RECT.intersects( MessageDisplay.SCRATCH_RECT2 ) )
        {
          MessageDisplay.SCRATCH_RECT2.setBounds( MessageDisplay.SCRATCH_RECT.intersection( MessageDisplay.SCRATCH_RECT2 ) );
          g.setClip( MessageDisplay.SCRATCH_RECT2 );
        }
        else
        {
          return vStrings;
        }
      }
      else
      {
        bClip = false;
      }

      if( strText == null || strText.length() == 0 ||
          ((iLineLengthChars <= 0) && (rect == null || rect.width == 0)) )
      {
        return vStrings;
      }

      // Replace tabs with single spaces
      strText = (strText.indexOf( '\t' ) >= 0) ? strText.replace( '\t', ' ' ) : strText;

      FontMetrics fm = (g == null) ? Toolkit.getDefaultToolkit().getFontMetrics( font ) : g.getFontMetrics();

      if( iLineLengthChars > 0 )
      {
        rect.width = fm.charWidth( 'p' ) * iLineLengthChars;
      }

      int x = rect.x;
      int y = rect.y + fm.getAscent();
      int iWidth = rect.width;
      if( iWidth < 0 )
      {
        return vStrings;
      }

      boolean bSkip;
      String strLine = "";

      boolean bNeedToDrawLastToken = false;

      StringTokenizer tokenizer = new StringTokenizer( strText, MessageDisplay.STRING_DELIMITERS, true );
      String token = null;
      while( tokenizer.hasMoreTokens() || bNeedToDrawLastToken )
      {
        token = bNeedToDrawLastToken ? token : tokenizer.nextToken();

        bSkip = false;

        for( int i = 0; i < MessageDisplay.STRING_DELIMITERS.length(); i++ )
        {
          if( token.charAt( 0 ) == MessageDisplay.STRING_DELIMITERS.charAt( i ) )
          {
            // Skip all delimiters but spaces and new lines, they have effect on output.
            if( MessageDisplay.STRING_DELIMITERS.charAt( i ) != ' ' &&
                MessageDisplay.STRING_DELIMITERS.charAt( i ) != '\n' )
            {
              bSkip = true;
            }
            break;
          }
        }

        if( bSkip )
        {
          continue;
        }

        bNeedToDrawLastToken = !tokenizer.hasMoreTokens();

        int iLinePlusToken = fm.stringWidth( strLine ) + fm.stringWidth( token );

        // if( bNeedToDrawLastToken && (iLinePlusToken <= iWidth) )
        // {
        //   strLine = strLine.concat( token );
        //   bNeedToDrawLastToken = false;
        // }

        boolean bLastLineOfRect = false;
        if( bNeedToDrawLastToken )
        {
          if( (iLinePlusToken <= iWidth) ||
              (bLastLineOfRect = (rect.height > 0) && ((rect.y + rect.height) < (y - fm.getAscent() + fm.getHeight() + fm.getAscent() / 2))) )// last line of the rectangle
          {
            // Force the last token to paint if:
            //
            // 1.) We have room for it on the line
            // 2.) We are on the last *visible* line in our bounds.
            //     Note determining the last visible line is a fuzzy scheme as the math above shows.

            strLine = strLine.concat( token );
            bNeedToDrawLastToken = false;
          }
        }


        if( (iLinePlusToken > iWidth) ||
            (token.charAt( 0 ) == '\n') ||
            !tokenizer.hasMoreTokens() ) // Force draw of last line
        {
          if( (strLine.length() > 0) &&
              ((token.charAt( 0 ) == '\n') || tokenizer.hasMoreTokens() || (iLinePlusToken <= iWidth) || bNeedToDrawLastToken || bLastLineOfRect) )
          {
            if( g != null )
            {
              g.drawString( strLine, x, y );
            }
            if( vStrings != null )
            {
              vStrings.addElement( bAppendLineFeed ? (strLine + "\n") : strLine );
            }

            y += fm.getHeight();

            strLine = "";

            if( token.charAt( 0 ) == ' ' ||
                token.charAt( 0 ) == '\n' )
            {
              continue;
            }
          }
          else if( token.charAt( 0 ) == '\n' )
          {
            y += fm.getHeight();
            continue;
          }
          else
          {
            do
            {
              if( token.length() == 1 )
              {
                // Rectangle isn't wide enough to display a single character.
                return vStrings;
              }

              int iSeg = token.length() / 2 + token.length() % 2;

              int iLen = iSeg;

              String strTokenPart = null;

              while( iSeg >= 1 )
              {
                try
                {
                  strTokenPart = token.substring( 0, iLen - 1 );
                }
                catch( Exception e )
                {
                  // ignore
                }

                int iTestWidth = fm.stringWidth( strTokenPart );

                iSeg = iSeg / 2 + (iSeg == 1 ? 0 : iSeg % 2);

                if( iTestWidth > iWidth )
                {
                  iLen -= iSeg;
                }
                else if( iTestWidth == iWidth )
                {
                  break;
                }
                else
                {
                  iLen += iSeg;
                }
              }

              if( strTokenPart != null )
              {
                if( strText.startsWith( "<html>" ) && strTokenPart.lastIndexOf( "<" ) > strTokenPart.lastIndexOf( ">" ) )
                {
                  strTokenPart = strTokenPart.substring( 0, strTokenPart.lastIndexOf( "<" ) );
                  iLen = strTokenPart.length();
                }
                if( g != null )
                {
                  g.drawString( strTokenPart, x, y );
                }
                if( vStrings != null )
                {
                  vStrings.addElement( bAppendLineFeed ? (strTokenPart + "\n") : strTokenPart );
                }
              }
              else
              {
                break;
              }

              y += fm.getHeight();

              if( iLen >= token.length() )
              {
                // We're drawing the token anyway, so we might as well just leave
                bNeedToDrawLastToken = false;
                break;
              }

              token = token.substring( strTokenPart.length() );

            }
            while( fm.stringWidth( token ) > iWidth );
          }
        }

        strLine = bNeedToDrawLastToken ? "" : strLine.concat( token );
      }

      if( bAppendLineFeed && !vStrings.isEmpty() )
      {
        // Remove the last line feed

        String strLast = vStrings.lastElement();
        if( strLast.endsWith( "\n" ) )
        {
          vStrings.setElementAt( strLast.substring( 0, strLast.length() - 1 ), vStrings.size() - 1 );
        }
      }

      return vStrings;
    }
    finally
    {
      if( bClip )
      {
        g.setClip( MessageDisplay.SCRATCH_RECT );
      }
    }
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

  public static Robot getRobot()
  {
    if( g_robot == null )
    {
      try
      {
        g_robot = new Robot();
      }
      catch( AWTException e )
      {
        throw new RuntimeException( e );
      }
    }
    return g_robot;
  }

  public static void clipCursor( Rectangle rcScreen )
  {
    if( rcScreen == null || rcScreen.isEmpty() )
    {
      if( g_clipCursorListener != null )
      {
        Toolkit.getDefaultToolkit().removeAWTEventListener( g_clipCursorListener );
        g_clipCursorListener = null;
      }

      g_rcClipCursor = null;

      return;
    }

    g_clipCursorListener = new ClipCursorHandler();
    g_rcClipCursor = rcScreen;

    Toolkit.getDefaultToolkit().addAWTEventListener( g_clipCursorListener, AWTEvent.MOUSE_MOTION_EVENT_MASK );
  }

  public static boolean clipPoint( Point pt )
  {
    if( g_rcClipCursor == null || g_rcClipCursor.contains( pt ) )
    {
      return false;
    }

    if( pt.x < g_rcClipCursor.x )
    {
      pt.x = g_rcClipCursor.x;
    }

    if( pt.x >= g_rcClipCursor.x + g_rcClipCursor.width )
    {
      pt.x = g_rcClipCursor.x + g_rcClipCursor.width - 1;
    }

    if( pt.y < g_rcClipCursor.y )
    {
      pt.y = g_rcClipCursor.y;
    }

    if( pt.y >= g_rcClipCursor.y + g_rcClipCursor.height )
    {
      pt.y = g_rcClipCursor.y + g_rcClipCursor.height - 1;
    }

    getRobot().mouseMove( pt.x, pt.y );

    return true;
  }

  public static Dimension boundDimensionWithin( Dimension preferredSize, Dimension minimumSize, Dimension maxSize )
  {
    return boundWithMax( maxSize, boundWithMin( minimumSize, preferredSize ) );
  }

  private static Dimension boundWithMin( Dimension minimumSize, Dimension preferredSize )
  {
    if( minimumSize == null )
    {
      return preferredSize;
    }

    if( preferredSize == null )
    {
      return minimumSize;
    }


    return new Dimension( Math.max( preferredSize.width, minimumSize.width ),
                          Math.max( preferredSize.height, minimumSize.height ) );
  }

  private static Dimension boundWithMax( Dimension maxSize, Dimension preferredSize )
  {
    if( maxSize == null )
    {
      return preferredSize;
    }

    if( preferredSize == null )
    {
      return maxSize;
    }


    return new Dimension( Math.min( preferredSize.width, maxSize.width ),
                          Math.min( preferredSize.height, maxSize.height ) );
  }

  public static void invalidateTree( Component component )
  {
    component.invalidate();
    if( component instanceof Container )
    {
      for( Component child : ((Container)component).getComponents() )
      {
        invalidateTree( child );
      }
    }
  }

  /**
   * Finds the first widget above the passed in widget of the given class
   *
   * @param start  the start component
   * @param aClass the class to find
   *
   * @return the found component
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
   *
   * @param start  the start component
   * @param aClass the class to find
   *
   * @return the found component
   */
  public static <T> T findAtOrAbove( Component start, Class<T> aClass )
  {
    Component comp = start;
    while( comp != null )
    {
      if( aClass.isInstance( comp ) )
      {
        //noinspection unchecked
        return (T)comp;
      }
      else
      {
        comp = comp.getParent();
      }
    }
    return null;
  }

  public static void ensureWindowIsVisible( Window w )
  {
    int width = w.getWidth();
    int height = w.getHeight();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    GraphicsConfiguration gc = w.getGraphicsConfiguration();
    Insets screenInsets = toolkit.getScreenInsets( gc );
    Rectangle screenSize = gc.getBounds();
    w.setLocation( Math.max( screenInsets.left, Math.min( w.getX(), screenSize.width - screenInsets.right - width ) ),
                   Math.max( screenInsets.top, Math.min( w.getY(), screenSize.height - screenInsets.bottom - height ) ) );
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

  public static void convertRectangleToScreen( Rectangle rectangle, Component component )
  {
    Point loc = rectangle.getLocation();
    SwingUtilities.convertPointToScreen( loc, component );
    rectangle.setLocation( loc );
  }

  public static void handleUncaughtException( Throwable e )
  {
    throw new RuntimeException( e );
  }

  /**
   */
  static final class ClipCursorHandler implements AWTEventListener
  {
    public void eventDispatched( AWTEvent e )
    {
      if( e instanceof MouseEvent )
      {
        MouseEvent me = (MouseEvent)e;

        switch( me.getID() )
        {
          //
          // Process MouseMotion events only
          //
          case MouseEvent.MOUSE_MOVED:
          case MouseEvent.MOUSE_DRAGGED:
          {
            Component comp = me.getComponent();
            if( comp == null )
            {
              break;
            }

            Point pt = me.getPoint();
            SwingUtilities.convertPointToScreen( pt, comp );

            if( clipPoint( pt ) )
            {
              me.consume();
            }

            break;
          }

          default:
            break;
        }
      }
    }
  }


  /**
   */
  static final class WaitCursorRunner
  {
    private static final WaitCursorRunner WAIT_CURSOR_RUNNER = new WaitCursorRunner();

    private WeakHashMap<JRootPane, RefCounter> _waitCursorMap;
    private WeakHashMap<Component, JRootPane> _rootPaneMap;
    private boolean _bWait;
    private WeakReference<Component> _c;

    private WaitCursorRunner()
    {
      _waitCursorMap = new WeakHashMap<JRootPane, RefCounter>();
      _rootPaneMap = new WeakHashMap<Component, JRootPane>();
    }

    static Component showWaitCursor( boolean bWait )
    {
      if( !EventQueue.isDispatchThread() )
      {
        // Can only do this safely from within the AWT Dispatch thread.
        // Also note we can't make any assumptions about interaction between
        // the AWT Dispatch thread and other threads e.g., we can't call invokeAndWait() here.
        return null;
      }

      WAIT_CURSOR_RUNNER._bWait = bWait;
      WAIT_CURSOR_RUNNER._c = null;
      return WAIT_CURSOR_RUNNER.run();
    }

    static void showWaitCursor( boolean bWait, Component c )
    {
      if( !EventQueue.isDispatchThread() )
      {
        // Can only do this safely from within the AWT Dispatch thread.
        // Also note we can't make any assumptions about interaction between
        // the AWT Dispatch thread and other threads e.g., we can't call invokeAndWait() here.
        return;
      }

      WAIT_CURSOR_RUNNER._bWait = bWait;
      WAIT_CURSOR_RUNNER._c = new WeakReference<Component>( c );
      WAIT_CURSOR_RUNNER.run();
    }

    public Component run()
    {
      if( _c != null )
      {
        JRootPane rootPane = _rootPaneMap.get( _c.get() );
        if( rootPane != null )
        {
          showWaitCursorNow( _bWait, rootPane );
          return rootPane;
        }
      }

      Component focus = _c == null ? getActiveWindow() : _c.get();
      focus = focus == null ? getFocusedWindow() : focus;

      JRootPane rootPane = rootPaneForComponent( focus );
      if( rootPane == null )
      {
        // Just use the active window's rootpane.
        // (e.g., the component is no longer attatched to a rootpane
        focus = getActiveWindow();
        focus = focus == null ? getFocusedWindow() : focus;

        rootPane = rootPaneForComponent( focus );
        if( rootPane == null )
        {
          return null;
        }
      }

      if( !_bWait && !rootPane.isShowing() )
      {
        Window win = StudioUtilities.windowForComponent( rootPane );
        if( win != null )
        {
          win = win.getOwner();
          if( win != null )
          {
            rootPane = rootPaneForComponent( win );
          }
        }
      }

      _rootPaneMap.put( focus, rootPane );

      showWaitCursorNow( _bWait, rootPane );

      return rootPane;
    }

    void showWaitCursorNow( boolean bWait, JRootPane rootPane )
    {
      if( rootPane == null )
      {
        return;
      }

      RefCounter refCounter = _waitCursorMap.get( rootPane );
      try
      {
        Component glassPane = rootPane.getGlassPane();
        if( bWait && refCounter == null )
        {
          Cursor cursorWait = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );
          rootPane.setCursor( cursorWait );
          glassPane.setCursor( cursorWait );
          glassPane.setVisible( true );
          glassPane.invalidate();

          refCounter = new RefCounter();
          _waitCursorMap.put( rootPane, refCounter );
        }
        else if( !bWait && (refCounter == null || refCounter._iRefCount == 1) )
        {
          Cursor cursorDefault = Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR );
          glassPane.setVisible( false );
          glassPane.setCursor( cursorDefault );
          rootPane.setCursor( cursorDefault );
          glassPane.invalidate();

          _waitCursorMap.remove( rootPane );
        }

        //## NOTE: Do NOT validate here. Side effects could include perpetual validation.
        // rootPane.validate();
      }
      catch( Exception e )
      {
        handleUncaughtException( e );
      }
      finally
      {
        if( refCounter != null )
        {
          refCounter._iRefCount += (bWait ? 1 : -1);
          if( refCounter._iRefCount < 0 )
          {
            _waitCursorMap.remove( rootPane );
          }
        }
      }
    }

    static final class RefCounter
    {
      public int _iRefCount;
    }
  }

  public static Rectangle getPrimaryMonitorScreenRect()
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gds = ge.getScreenDevices();
    BasicGosuEditor frame = RunMe.getEditorFrame();
    Rectangle foundBounds = null;
    if( frame != null )
    {
      Rectangle windowBounds = frame.getBounds();
      for( GraphicsDevice gd : gds )
      {
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();
        if( bounds.contains( windowBounds.getX(), windowBounds.getY() ) )
        {
          foundBounds = bounds;
          break;
        }
      }
    }
    if( foundBounds == null )
    {
      foundBounds = gds[0].getDefaultConfiguration().getBounds();
    }

    return foundBounds;
  }

  public static Point getXYForDialogRelativeToStudioFrame( int width, int height )
  {
    Rectangle screenRect = getPrimaryMonitorScreenRect();
    return new Point( (int)(screenRect.getX() + (screenRect.getWidth() - width) / 2),
                      (int)(screenRect.getY() + (screenRect.getHeight() - height) / 2) );
  }

  public static void mapCancelKeystroke( final JDialog dialog, Action cancelAction )
  {
    Object key = dialog.getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Cancel";
      dialog.getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    dialog.getRootPane().getActionMap().put( key, cancelAction );
  }

  public static List<String> filterStrings( Collection<? extends CharSequence> collection, String filter )
  {
    return filterStrings( collection, filter, false );
  }

  public static List<String> filterStrings( Collection<? extends CharSequence> collection, String filter, boolean showChoicesIfEmpty )
  {
    if( filter == null )
    {
      filter = "";
    }
    int iDotIndex = filter.lastIndexOf( '.' );
    if( iDotIndex >= 0 )
    {
      filter = filter.substring( iDotIndex + 1 );
    }

    List<String> filteredTypes = new ArrayList<String>();
    if( showChoicesIfEmpty || filter.length() > 0 )
    {
      int iFlags = 0;
      if( filter.length() > 0 && filter.indexOf( '*' ) < 0 &&
          Character.isUpperCase( filter.charAt( 0 ) ) )
      {
        filter = camelCasePrefix( filter );
      }
      else
      {
        iFlags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
      }

      // Replace all wildcard '*' chars with the regex ".*" expression
      filter = filter.replaceAll( "\\*", "\\.\\*" );

      // A '#' char indicates that the proper regex syntax has already been embedded in the string;
      // we only need to replace the '#' with '*'
      filter = filter.replaceAll( "\\#", "\\*" );

      boolean bHasDot = filter.indexOf( '~' ) >= 0;
      filter = filter.replaceAll( "~", "(\\\\.|" + '\u2024' + ")" );

      boolean exactMatch = filter.endsWith( " " );
      filter = filter.trim();

      // Match the expression string followed by any number of chars
      try
      {
        Pattern pattern = Pattern.compile( '^' + filter + (exactMatch ? "" : (filter.startsWith( ".*" ) ? "" : ".*")), iFlags );
        for( CharSequence cs : collection )
        {
          String strType = cs.toString();
          String strName = bHasDot ? strType : getRelativeTypeName( strType );
          boolean shouldAdd = exactMatch ? pattern.matcher( strName ).matches() : pattern.matcher( strName ).find();
          if( shouldAdd )
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

  public static void htmlEncode( StringBuilder sb )
  {
    for( int i = 0; i < sb.length(); i++ )
    {
      char ch = sb.charAt( i );
      switch( ch )
      {
        case '<':
          sb.replace( i, i + 1, "&lt;" );
          i += 3;
          break;
        case '>':
          sb.replace( i, i + 1, "&gt;" );
          i += 3;
          break;
        case '&':
          sb.replace( i, i + 1, "&amp;" );
          i += 4;
          break;
        case '"':
          sb.replace( i, i + 1, "&quot;" );
          i += 5;
          break;
      }
    }
  }

}
