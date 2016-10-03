package editor.util;

import editor.Scheme;

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
    setBackground( Scheme.active().getControl() );
    //setContentAreaFilled( false );
    setOpaque( true );
    addChangeListener( this );
  }

  public void handleArmed()
  {
    if( isEnabled() )
    {
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
  }

  public void handleUnarmed()
  {
    setBorderPainted( false );
    setBackground( Scheme.active().getControl() );
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
