package editor.util;

import editor.RunMe;
import editor.search.StudioUtilities;
import gw.util.GosuExceptionUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * A helper class for displaying a <code>ProgressWindow</code> while a task
 * executes in a worker thread.
 */
public class ProgressFeedback implements IProgressCallback, ActionListener
{
  private static final int UPDATE_DELAY = 100;

  private String _strNotice;
  private ProgressPanel _pp;
  private ProgressWindow _pw;
  private boolean _bAbort;
  private boolean _bHideAbortButton;
  private boolean _bShowInStudioGlassPane;

  private String _currentMessage = "";
  private int _currentValue = 0;
  private int _length = 0;
  private boolean _changeLength = true;

  private Timer _timer;

  /**
   * A helper method that executes a task in a worker thread and displays feedback
   * in a progress windows.
   *
   * @param strNotice The text notice to display in the ProgressWindow.
   * @param task      The task to execute in a separate (worker) thread.
   */
  public static void runWithProgress( final String strNotice, final IRunnableWithProgress task )
  {
    runWithProgress( strNotice, task, false, false );
  }

  public static void runWithProgress( final String strNotice, final IRunnableWithProgress task, final boolean bHideAbortBtn )
  {
    runWithProgress( task, new ProgressFeedback( strNotice, bHideAbortBtn, false ) );
  }

  public static void runWithProgress( final String strNotice, final IRunnableWithProgress task, final boolean bHideAbortBtn, final boolean bShowInStudioGlassPane )
  {
    runWithProgress( task, new ProgressFeedback( strNotice, bHideAbortBtn, bShowInStudioGlassPane ) );
  }

  protected static void runWithProgress( final IRunnableWithProgress task, final ProgressFeedback progressFeedback )
  {
    BackgroundOperation.instance().doBackgroundWaitOp(
      new Runnable()
      {
        public void run()
        {
          try
          {
            progressFeedback.startTimer();
            task.run( progressFeedback );
          }
          catch( Throwable e )
          {
            StudioUtilities.handleUncaughtException( e );
          }
          finally
          {
            progressFeedback.stopTimer();
            progressFeedback.dispose();
          }
        }
      } );
  }

  public static <T> T runWithPossibleDialog( final Callable<T> callable, String message )
  {
    if( (!RunMe.getEditorFrame().isVisible()) || !SwingUtilities.isEventDispatchThread() )
    {
      try
      {
        return callable.call();
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.convertToRuntimeException( e );
      }
    }
    final ProgressFeedback[] waitDialog = {null};
    final boolean[] done = {false};
    final Object[] ret = {null};
    final Throwable[] throwable = {null};

    new ProgressFeedbackThread()
    {
      @Override
      public void run()
      {
        try
        {
          ret[0] = callable.call();
        }
        catch( Throwable t )
        {
          throwable[0] = t;
        }
        finally
        {
          synchronized( done )
          {
            done[0] = true;
            done.notifyAll();
            SwingUtilities.invokeLater( new Runnable()
            {
              public void run()
              {
                synchronized( waitDialog )
                {
                  if( waitDialog[0] != null )
                  {
                    waitDialog[0].stopTimer();
                    waitDialog[0].dispose();
                  }
                }
              }
            } );
          }
        }
      }
    }.start();

    boolean localDone;
    if( !(localDone = shortWait( 100L, done )) )
    {
      try
      {
        StudioUtilities.showWaitCursor( true );
        localDone = shortWait( 2900L, done );
      }
      finally
      {
        StudioUtilities.showWaitCursor( false );
      }
    }
    if( !localDone )
    {
      synchronized( waitDialog )
      {
        waitDialog[0] = new ProgressFeedback( message, true, false );
        waitDialog[0].setLength( -1 );
        waitDialog[0].startTimer();
      }
      waitForProgress( done );
    }
    if( throwable[0] != null )
    {
      if( throwable[0] instanceof InvocationTargetException )
      {
        throw GosuExceptionUtil.forceThrow( throwable[0].getCause() );
      }
      else
      {
        throw GosuExceptionUtil.forceThrow( throwable[0] );
      }
    }
    return (T)ret[0];
  }

  private static boolean shortWait( final long delay, final boolean[] done )
  {
    long waitUntil = System.currentTimeMillis() + delay;
    boolean localDone;
    //noinspection SynchronizationOnLocalVariableOrMethodParameter
    synchronized( done )
    {
      while( true )
      {
        long remainingTime = waitUntil - System.currentTimeMillis();
        if( done[0] || remainingTime <= 0 )
        {
          break;
        }
        try
        {
          done.wait( remainingTime );
        }
        catch( InterruptedException e )
        {
          // ignore
        }
      }
      localDone = done[0];
    }
    return localDone;
  }

  private static void waitForProgress( final boolean[] done )
  {
    new ModalEventQueue(
      new IModalHandler()
      {
        public boolean isModal()
        {
          synchronized( done )
          {
            return !done[0];
          }
        }
      } ).run();
  }

  /**
   * Construct a ProgressFeedback with a given text notice.
   *
   * @param strNotice              The text notice to display in the ProgressWindow.
   * @param bHideAbortButton       will hide abort button
   * @param bShowInStudioGlassPane will show in studio pane
   */
  protected ProgressFeedback( String strNotice, boolean bHideAbortButton, boolean bShowInStudioGlassPane )
  {
    _strNotice = strNotice;
    _bHideAbortButton = bHideAbortButton;
    _bShowInStudioGlassPane = bShowInStudioGlassPane;
  }

  /**
   * Hides and disposes the <code>ProgressWindow</code>.
   */
  void dispose()
  {
    if( _pp == null )
    {
      return;
    }

    StudioUtilities.invokeInDispatchThread( new Runnable()
    {
      public void run()
      {
        if( _bShowInStudioGlassPane )
        {
          LabGlassPane.getInstance().removeModalComponent( _pp );
        }
        else
        {
          _pw.dispose();
        }
        _pp = null;
        _pw = null;
      }
    } );
  }

  private void startTimer()
  {
    _timer = new Timer( UPDATE_DELAY, new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        if( !GraphicsEnvironment.isHeadless() )
        {
          if( _pp == null )
          {
            _pp = new ProgressPanel( _length, _strNotice, ProgressFeedback.this, _bHideAbortButton );
            _pp.setSize( _pp.getPreferredSize() );
            if( _pp.getWidth() < 400 )
            {
              Dimension size = new Dimension( 400, _pp.getHeight() );
              _pp.setSize( size );
              _pp.setPreferredSize( size );
            }
            if( _bShowInStudioGlassPane )
            {
              LabGlassPane.getInstance().addModalComponent( _pp );
            }
            else
            {
              _pw = new ProgressWindow( _pp );
              _pw.show();
            }
          }

          if( _changeLength )
          {
            _pp.setLength( _length );
            _changeLength = false;
          }

          _pp.updateProgress( _currentValue, _currentMessage );
        }
        if( _bAbort )
        {
          _timer.stop();
        }
      }
    } );
    _timer.setRepeats( true );
    _timer.start();
  }

  private void stopTimer()
  {
    if( _timer != null )
    {
      _timer.stop();
    }
  }

  //-------------------------------------------
  //-------- IProgressCallback methods --------

  public void setLength( int iLength )
  {
    _length = iLength;
    _changeLength = true;
  }

  public boolean updateProgress( final int iProgress, final String strMessage, String... args )
  {
    _currentValue = iProgress;
    setCurrentMessage( strMessage, args );

    return _bAbort;
  }

  private void setCurrentMessage( String strMessage, String[] args )
  {
    if( strMessage == null )
    {
      _currentMessage = "";
    }
    else
    {
      String resolved = strMessage;
      _currentMessage = resolved;
    }
  }

  public boolean updateProgress( final String strMessage, String... args )
  {
    setCurrentMessage( strMessage, args );

    return _bAbort;
  }

  public int getProgress()
  {
    return _pp.getProgess();
  }

  public boolean isAbort()
  {
    return _bAbort;
  }

  //-------------------------------------------
  //-------- ActionListener methods -----------

  public void actionPerformed( ActionEvent e )
  {
    _bAbort = true;
    _timer.stop();
  }

  public void operationComplete()
  {
    dispose();
  }

  public static class ProgressFeedbackThread extends Thread
  {
  }
}
