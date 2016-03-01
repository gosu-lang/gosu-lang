package editor;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class PopupHandler<E> extends MouseAdapter
{
  protected E _owner;
  private IContextMenuHandler<E> _contextMenuHandler;

  public PopupHandler( E owner, IContextMenuHandler<E> contextMenuHandler )
  {
    _owner = owner;
    _contextMenuHandler = contextMenuHandler;
  }

  /** */
  @Override
  public void mousePressed( MouseEvent e )
  {
    // Note we do this here because Unix variants tend to set the PopupTrigger
    // modifier only on the Pressed event, while Windows sets it on the
    // Released event.
    handleContextMenu( e );
  }

  @Override
  public void mouseReleased( MouseEvent e )
  {
    handleContextMenu( e );
  }

  private void handleContextMenu( MouseEvent e )
  {
    int iXPos = e.getX();
    int iYPos = e.getY();

    if( e.isPopupTrigger() )
    {
      handleRightClick( iXPos, iYPos, e.getComponent() );
    }
  }

  /** */
  protected void handleRightClick( final int iXPos, final int iYPos, final Component component )
  {
    EventQueue.invokeLater(
      new Runnable()
      {
        public void run()
        {
          // Hack to make disabled ui controls wake up.
//          UpdateNotifier.instance().notifyActionComponentsNow();
          _contextMenuHandler.displayContextMenu( _owner, iXPos, iYPos, component );
        }
      } );
  }

}
