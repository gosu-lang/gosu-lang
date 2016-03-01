///////////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2002 Centrica Software
//
///////////////////////////////////////////////////////////////////////////////////////
package editor.util;

import gw.util.ILogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * A general purpose concurrent task queue. Facilitates creating named queues
 * containing any number of Runnable tasks. A task is posted to its queue
 * asynchronously. Each queue maintains a single thread and tasks are executed
 * in the order they are posted.
 * <p/>
 * Improvements to this class might include some sort of priority scheduling
 * for tasks.  For now they execute with equal priority.
 */
public class TaskQueue extends Thread
{
  /**
   * A static map of the uniquely named queues.
   */
  private static final HashMap<String, TaskQueue> QUEUE_MAP = new HashMap<String, TaskQueue>();

  private ILogger _logger;
  private final LinkedList<Runnable> _queue; // of Runnable tasks
  private boolean _shutdown = false; // used to stop queue

  /**
   * Use one of the getInstance() methods to create a TaskQueue.
   */
  private TaskQueue( ILogger logger, String strName )
  {
    super( strName );
    _logger = logger;
    _queue = new LinkedList<Runnable>();
    setPriority( Thread.MIN_PRIORITY + 2 );
  }

  /**
   * Fetch a TaskQueue by name.  If the TaskQueue doesn't already exist,
   * creates the TaskQueue.
   */
  public static TaskQueue getInstance( String strQueueName )
  {
    return getInstance( null, strQueueName );
  }

  /**
   * Fetch a TaskQueue by name.  If the TaskQueue doesn't already exist,
   * creates the TaskQueue.
   *
   * @param logger       An optional logger.
   * @param strQueueName The unique name for the queue.
   *
   * @return The TaskQueue associated with the specified name.
   */
  public static TaskQueue getInstance( ILogger logger, String strQueueName )
  {
    if( strQueueName == null )
    {
      return null;
    }

    TaskQueue taskQueue = QUEUE_MAP.get( strQueueName );
    if( taskQueue == null )
    {
      taskQueue = new TaskQueue( logger, strQueueName );
      QUEUE_MAP.put( strQueueName, taskQueue );
    }

    return taskQueue;
  }

  /**
   * Clears all the inactive tasks in the specified queue.
   */
  public static void emptyAndRemoveQueue( String strQueueName )
  {
    TaskQueue taskQueue = QUEUE_MAP.get( strQueueName );
    if( taskQueue != null )
    {
      taskQueue.emptyQueue();
      synchronized( taskQueue._queue )
      {
        taskQueue._shutdown = true;
        taskQueue._queue.notifyAll();
      }
      QUEUE_MAP.remove( strQueueName );
    }
  }

  /**
   * Stops all task queues and dumps thier queue.  <b>This is a very dangerous method and should
   * only be called from tests.</b>
   */
  public static void killAll()
  {
    Collection<TaskQueue> taskQueues = new ArrayList<TaskQueue>( QUEUE_MAP.values() );
    for( TaskQueue taskQueue : taskQueues )
    {
      emptyAndRemoveQueue( taskQueue.getName() );
    }
  }

  /**
   * Posts a task to the queue (asynchronously).
   *
   * @param task A task to run in the TaskQueue's thread.
   */
  public void postTask( Runnable task )
  {
    synchronized( _queue )
    {
      _queue.add( task );

      if( !isAlive() )
      {
        start();
      }

      _queue.notifyAll();
    }
  }

  public void waitUntilAllCurrentTasksFinish()
  {

    final Object lock = new Object();

    Runnable runnable = new Runnable()
    {
      @Override
      public void run()
      {
        synchronized( lock )
        {
          lock.notifyAll();
        }
      }
    };

    synchronized( lock )
    {
      postTask( runnable );
      try
      {
        lock.wait();
      }
      catch( InterruptedException e )
      {
        //ignore
      }
    }

  }

  /**
   * Peek at the "current" task in the queue. It may or may not be running.
   *
   * @return The "current" task in the queue.
   */
  public Runnable peekTask()
  {
    synchronized( _queue )
    {
      return _queue.isEmpty() ? null : _queue.get( 0 );
    }
  }

  /**
   * Get a list of all the tasks in this TaskQueue.
   *
   * @return A cloned list of all the tasks in this TaskQueue.
   */
  public List<Runnable> getTasks()
  {
    synchronized( _queue )
    {
      return (List<Runnable>)_queue.clone();
    }
  }

  /**
   * Empty the queue
   */
  public void emptyQueue()
  {
    synchronized( _queue )
    {
      _queue.clear();
    }
  }

  /**
   * Get the size of the queue at the instance this is called.
   */
  public int size()
  {
    return (_queue.size());
  }

  /**
   * Do NOT ever call this!  Public only by contract.
   */
  @Override
  public void run()
  {
    while( !_shutdown )
    {
      try
      {
        Runnable task;

        synchronized( _queue )
        {
          while( _queue.isEmpty() )
          {
            if( _shutdown )
            {
              return;
            }
            _queue.wait();
          }

          // The double sync get/remove thing is done to support peekTask above.
          task = _queue.get( 0 );

          _queue.notifyAll();
        }

        try
        {
          task.run();
        }
        catch( Exception t ) // Don't catch ThreadDeath, even though we can rethrow it, it still prints to std err, which is undesirable sometimes
        {
          log( t );
        }
        synchronized( _queue )
        {
          if( _queue.size() > 0 )
          {
            _queue.removeFirst();
          }
        }
      }
      catch( Exception t ) // Don't catch ThreadDeath, even though we can rethrow it, it still prints to std err, which is undesirable sometimes
      {
        log( t );
      }
    }
  }

  /**
   * Log an exception or error.
   */
  protected void log( Throwable t )
  {
    if( _logger == null )
    {
      t.printStackTrace();
    }
    else
    {
      _logger.warn( "Error running job.", t );
    }
  }

  public static boolean hasWork()
  {
    for( TaskQueue taskQueue : QUEUE_MAP.values() )
    {
      if( taskQueue.peekTask() != null )
      {
        return true;
      }
    }
    return false;
  }

  public void postTaskAndWait( final Runnable runnable, long timeout )
  {
    if( Thread.currentThread() == this )
    {
      throw new IllegalStateException( "Cannot postTaskAndWait from within the TaskQueue thread" );
    }

    final Object wait = new Object();
    Runnable waitForTask = new Runnable()
    {
      @Override
      public void run()
      {
        runnable.run();
        synchronized( wait )
        {
          wait.notify();
        }
      }
    };
    synchronized( wait )
    {
      postTask( waitForTask );
      try
      {
        if( timeout < 0 )
        {
          wait.wait();
        }
        else
        {
          wait.wait( timeout );
        }
      }
      catch( InterruptedException e )
      {
        //ignore
      }
    }
  }

  public void postTaskAndWait( final Runnable runnable )
  {
    postTaskAndWait( runnable, -1 );
  }

  public void cancelTasks( Class taskClass )
  {
    synchronized( _queue )
    {
      Iterator iterator = getTasks().iterator();
      while( iterator.hasNext() )
      {
        Object task = iterator.next();
        if( taskClass.isInstance( task ) )
        {
          iterator.remove();
        }
      }
    }
  }

  public void setILogger( final ILogger logger )
  {
    postTask( new Runnable()
    {
      @Override
      public void run()
      {
        _logger = logger;
      }
    } );
  }
}