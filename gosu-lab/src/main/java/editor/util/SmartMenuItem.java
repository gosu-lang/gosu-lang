package editor.util;

import editor.Scheme;
import editor.actions.UpdateNotifier;

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
    this( action, true );
  }
  public SmartMenuItem( Action action, boolean autoEnable )
  {
    super( action );
    if( autoEnable )
    {
      UpdateNotifier.instance().addActionComponent( this );
    }
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
    setBackground( Scheme.active().getMenu() );
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
    setBackground( Scheme.active().getMenu() );
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
