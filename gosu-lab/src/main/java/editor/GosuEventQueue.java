package editor;


import editor.util.ModalEventQueue;

import java.util.ArrayList;
import java.util.List;


/**
 */
class GosuEventQueue extends ModalEventQueue
{
  private static GosuEventQueue INSTANCE;

  private long _lErrMsgTime;
  private final List<Runnable> _idleListeners;

  public static GosuEventQueue instance()
  {
    if( INSTANCE == null )
    {
      INSTANCE = new GosuEventQueue();
    }
    return INSTANCE;
  }

  private GosuEventQueue()
  {
    super( () -> true );
    _idleListeners = new ArrayList<>();
  }

  public void addIdleListener( Runnable l )
  {
    synchronized( _idleListeners )
    {
      _idleListeners.add( l );
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public boolean removeIdleListener( Runnable l )
  {
    synchronized( _idleListeners )
    {
      return _idleListeners.remove( l );
    }
  }

  protected void handleUncaughtException( Throwable t )
  {
    if( System.currentTimeMillis() - _lErrMsgTime > 5000 )
    {
      editor.util.EditorUtilities.handleUncaughtException( t );
    }
    _lErrMsgTime = System.currentTimeMillis();
  }

  @Override
  protected void executeIdleTasks()
  {
    for( int i = 0; i < _idleListeners.size(); i++ )
    {
      Runnable task;
      try
      {
        task = _idleListeners.get( i );
      }
      catch( Exception e )
      {
        break;
      }

      task.run();
    }

    super.executeIdleTasks();
  }
}
