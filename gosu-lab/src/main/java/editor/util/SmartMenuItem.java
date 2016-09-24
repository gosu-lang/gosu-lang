package editor.util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 */
public class SmartMenuItem extends JMenuItem implements ChangeListener
{
  public SmartMenuItem( Action action )
  {
    super( action );
    init();
  }
  public SmartMenuItem( String text, Icon icon )
  {
    super( text, icon );
    init();
  }

  private void init()
  {
    setBorderPainted( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setBackground( EditorUtilities.CONTROL );
    setContentAreaFilled( false );
    setOpaque( true );
    EventQueue.invokeLater( () -> {
      setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );
      setBackground( EditorUtilities.CONTROL );
    } );

    addChangeListener( this );
  }

  public void handleArmed()
  {
    if( isEnabled() )
    {
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
  }

  public void handleUnarmed()
  {
    setBorderPainted( false );
    setBackground( EditorUtilities.CONTROL );
  }

  public void stateChanged( ChangeEvent e )
  {
    if( isArmed() && !isSelected() )
    {
      handleArmed();
    }
    else
    {
      handleUnarmed();
    }
  }
}
