package editor.util;

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
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setBackground( EditorUtilities.CONTROL );
    //setContentAreaFilled( false );
    setOpaque( false );
    EventQueue.invokeLater( () -> {
      setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      setBackground( EditorUtilities.CONTROL );
    } );

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
                  setBackground( EditorUtilities.XP_HIGHLIGHT_SELECTED_COLOR );
                }
                else
                {
                  setBackground( EditorUtilities.XP_HIGHLIGHT_COLOR );
                }
              }
              break;

            case ItemEvent.DESELECTED:
              setBorderPainted( false );
              setBackground( _bkColor != null ? _bkColor : EditorUtilities.CONTROL );
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
