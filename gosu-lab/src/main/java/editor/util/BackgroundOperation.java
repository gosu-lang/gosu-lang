package editor.util;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundOperation
{
  private static BackgroundOperation g_instance;
  private ExecutorService _jobRunner;


  public static BackgroundOperation instance()
  {
    if( g_instance == null )
    {
      g_instance = new BackgroundOperation();
    }
    return g_instance;
  }

  private BackgroundOperation()
  {
  }

  /**
   * Sets the mouse cursor to the wait cursor, blocks input, and queues the runnable
   * up in the ExecutorService.
   */
  public void doBackgroundWaitOp( final Runnable run )
  {
    doBackgroundOp( run, true );
  }

  /**
   * Runs a job in a background thread, using the ExecutorService, and optionally
   * sets the cursor to the wait cursor and blocks input.
   */
  public void doBackgroundOp( final Runnable run, final boolean showWaitCursor )
  {
    final Component[] key = new Component[1];
    ExecutorService jobRunner = getJobRunner();
    if( jobRunner != null )
    {
      jobRunner.submit( () -> performBackgroundOp( run, key, showWaitCursor ) );
    }
    else
    {
      run.run();
    }
  }

  void performBackgroundOp( Runnable run, final Component[] key, boolean showWaitCursor )
  {
    if( showWaitCursor )
    {
      SwingUtilities.invokeLater( () -> key[0] = EditorUtilities.showWaitCursor( true ) );
    }

    try
    {
      run.run();
    }
    finally
    {
      if( showWaitCursor )
      {
        SwingUtilities.invokeLater( () -> EditorUtilities.showWaitCursor( false, key[0] ) );
      }
    }
  }

  /**
   * Returns the instance of the job dispatcher, used for scheduling time-consuming
   * operations (element state updates) on a separate thread.
   */
  private ExecutorService getJobRunner()
  {
    if( _jobRunner == null )
    {
      _jobRunner = Executors.newSingleThreadExecutor();
    }
    return _jobRunner;
  }


  /**
   * Pumps through all current events in the background operation queue.  Note that this is *NOT* a settle.
   * Any operations added after this method is invoked will not be executed.
   */
  public void waitOnBackgroundOp()
  {
    ExecutorService jobRunner = getJobRunner();
    if( jobRunner != null )
    {
      final Object wait = new Object();
      synchronized( wait ) //## wtf?
      {
        BackgroundOperation.instance().doBackgroundOp( () -> {
          synchronized( wait )
          {
            wait.notifyAll();
          }
        }, false );

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

  public void waitOnBackgroundOp( long timeout )
  {
    Thread waiter = new Thread( this::waitOnBackgroundOp, "Background Operation Waiter" );
    waiter.start();
    try
    {
      waiter.join( timeout );
    }
    catch( InterruptedException ex )
    {
      //ignore
    }
  }

}
