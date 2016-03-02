package editor;


import editor.util.IModalHandler;
import editor.util.ModalEventQueue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 */
class GosuEventQueue extends ModalEventQueue
{
  private static GosuEventQueue INSTANCE;

  private String[] _args;
  private long _lErrMsgTime;
  private final List<Runnable> _idleListeners;

  public static GosuEventQueue instance()
  {
    if( INSTANCE == null )
    {
      INSTANCE = new GosuEventQueue( new String[0] );
    }
    return INSTANCE;
  }

  private GosuEventQueue( String[] args )
  {
    super( new ModalHandler() );
    _args = args;
    _idleListeners = new ArrayList<Runnable>();
  }

  public void addIdleListener( Runnable l )
  {
    synchronized( _idleListeners )
    {
      _idleListeners.add( l );
    }
  }

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
  public void dispatchEvent( AWTEvent event )
  {
    super.dispatchEvent( event );
    checkForIdleTime();
  }

  private void checkForIdleTime()
  {
    try
    {
      if( Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() == null )
      {
        handleIdleTasks();
      }
    }
    catch( Exception t )
    {
      handleUncaughtException( t );
    }
  }

  private void handleIdleTasks()
  {
    synchronized( _idleListeners )
    {
      for( Runnable r : _idleListeners )
      {
        try
        {
          r.run();
        }
        catch( Exception e )
        {
          handleUncaughtException( e );
        }
      }
    }
  }

  private static class ModalHandler implements IModalHandler
  {
    public boolean isModal()
    {
      return true;
    }
  }
}
