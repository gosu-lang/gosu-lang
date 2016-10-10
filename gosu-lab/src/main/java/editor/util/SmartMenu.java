package editor.util;

import editor.Scheme;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
*/
public class SmartMenu extends JMenu implements MenuListener
{
  public SmartMenu( String strLabel )
  {
    super( strLabel );
    init();
  }

  private void init()
  {
    setBorderPainted( false );
    setOpaque( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    //setContentAreaFilled( false );
    addMenuListener( this );
    addItemListener( createMouseListener() );
  }

  protected ItemListener createMouseListener()
  {
    return
      new ItemListener()
      {
        Color _bkColor;

        @Override
        public void itemStateChanged( ItemEvent e )
        {
          switch( e.getStateChange() )
          {
            case ItemEvent.SELECTED:
              if( isEnabled() )
              {
                _bkColor = getBackground();
                setBorderPainted( true );
                if( getModel().isArmed() )
                {
                  setBackground( Scheme.active().getXpHighlightSelectedColor() );
                }
                else
                {
                  setBackground( Scheme.active().getXpHighlightColor() );
                }
              }
              break;

            case ItemEvent.DESELECTED:
              setBorderPainted( false );
              setBackground( _bkColor != null ? _bkColor : Scheme.active().getMenu() );
              break;
          }
        }
      };
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
