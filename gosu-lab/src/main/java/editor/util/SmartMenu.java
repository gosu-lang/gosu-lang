package editor.util;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
*/
public class SmartMenu extends JMenu implements MenuListener
{
  public SmartMenu( String strLabel )
  {
    super( strLabel );
    addMenuListener( this );
  }

  @Override
  public void menuSelected( MenuEvent e )
  {
    for( int i = 0; i < getItemCount(); i++ )
    {
      JMenuItem item = getItem( i );
      if( item != null && item.getAction() != null )
      {
        item.setEnabled( item.getAction().isEnabled() );
      }
    }
  }

  @Override
  public void menuDeselected( MenuEvent e )
  {
  }

  @Override
  public void menuCanceled( MenuEvent e )
  {
  }
}
