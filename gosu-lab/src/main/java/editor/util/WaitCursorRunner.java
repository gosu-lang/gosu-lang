package editor.util;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import static editor.util.EditorUtilities.*;

/**
   */
public final class WaitCursorRunner
{
  private static final WaitCursorRunner WAIT_CURSOR_RUNNER = new WaitCursorRunner();

  private WeakHashMap<JRootPane, RefCounter> _waitCursorMap;
  private WeakHashMap<Component, JRootPane> _rootPaneMap;
  private boolean _bWait;
  private WeakReference<Component> _c;

  private WaitCursorRunner()
  {
    _waitCursorMap = new WeakHashMap<>();
    _rootPaneMap = new WeakHashMap<>();
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
    WAIT_CURSOR_RUNNER._c = new WeakReference<>( c );
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
      Window win = windowForComponent( rootPane );
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

