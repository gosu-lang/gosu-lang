package editor.util;

import editor.actions.UpdateNotifier;

import java.awt.*;

/**
 * Provides for ui modality using a local event queue to dispatch events while
 * a component is visible e.g., a frame or window.
 */
public class ModalEventQueue implements Runnable
{
  private IModalHandler _modalHandler;

  /**
   * @param visibleComponent A visible component. The modal event queue remains
   *                         operable/modal while the component is visible.
   */
  public ModalEventQueue( final Component visibleComponent )
  {
    _modalHandler = visibleComponent::isVisible;
  }

  public ModalEventQueue( IModalHandler modalHandler )
  {
    _modalHandler = modalHandler;
  }

  public void run()
  {
    while( isModal() )
    {
      handleIdleTime();
      try
      {
        AWTEvent event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
        dispatchEvent( event );
      }
      catch( Throwable e )
      {
        handleUncaughtException( e );
      }
    }
  }

  private void handleIdleTime()
  {
    try
    {
      if( Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() == null )
      {
        executeIdleTasks();
      }
    }
    catch( Throwable t )
    {
      EditorUtilities.handleUncaughtException(t);
    }
  }

  protected void executeIdleTasks()
  {
    UpdateNotifier.instance().notifyActionComponentsNow();
  }

  protected void handleUncaughtException( Throwable t )
  {
    throw new RuntimeException( t );
  }

  protected boolean isModal()
  {
    return _modalHandler.isModal();
  }

  public void dispatchEvent( AWTEvent event )
  {
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
}

