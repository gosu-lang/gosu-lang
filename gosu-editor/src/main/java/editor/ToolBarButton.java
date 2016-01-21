/*
 * ToolBarButton.java [Apr 29, 2002 3:43:50 PM]
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A ToolBarButton is a button represented by an icon for use on a toolbar.  All
 * toolbar buttons that are represented by an icon should use this button
 * object.
 */
public class ToolBarButton extends JButton
{
  public static final int DEFAULT_MARGIN = 2;

  private Border _borderRaised;
  private Border _borderLowered;

  /**
   * Creates a button with an icon.
   *
   * @param icon The icon that should be display for this button.
   */
  public ToolBarButton( String text, Icon icon, int iMargin )
  {
    super( text, icon );

    _borderRaised = new CompoundBorder( new ShallowBevelBorder( BevelBorder.RAISED ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) );
    _borderLowered = new CompoundBorder( new ShallowBevelBorder( BevelBorder.LOWERED ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) );

    setBorderPainted( false );
    setBorder( _borderRaised );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setContentAreaFilled( false );

    addMouseListener( new MouseAdapter()
    {
      public void mouseEntered( MouseEvent e )
      {
        if( isEnabled() )
        {
          setBorderPainted( true );
        }
      }

      public void mouseExited( MouseEvent e )
      {
        setBorderPainted( false );
      }
    } );
  }

  public ToolBarButton( String text, Icon icon )
  {
    this( text, icon, DEFAULT_MARGIN );
  }

  public ToolBarButton( Icon icon, int iMargin )
  {
    this( null, icon, iMargin );
  }

  public ToolBarButton( Icon icon )
  {
    this( icon, DEFAULT_MARGIN );
  }

  public ToolBarButton( Icon icon, Color clrBorderHighlight, Color clrBorderShadow, int iMargin )
  {
    this( null, icon, iMargin );

    _borderRaised = new CompoundBorder( new ShallowBevelBorder( BevelBorder.RAISED, clrBorderHighlight, clrBorderShadow ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) );
    _borderLowered = new CompoundBorder( new ShallowBevelBorder( BevelBorder.LOWERED, clrBorderHighlight, clrBorderShadow ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) );
  }

  public ToolBarButton( Icon icon, Color clrBorderHighlight, Color clrBorderShadow )
  {
    this( icon, clrBorderHighlight, clrBorderShadow, DEFAULT_MARGIN );
  }

  public ToolBarButton( String text )
  {
    this( text, null );
  }

  public ToolBarButton()
  {
    this( null, null );
  }

  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    JComponent separator = (JComponent)getClientProperty( "_separatorComp" );
    if( separator != null )
    {
      separator.setVisible( bVisible );
    }
  }

  public boolean isFocusable()
  {
    return false;
  }

  public void setEnabled( boolean b )
  {
    super.setEnabled( b );
    setBorderPainted( false );
  }

  protected void paintBorder( Graphics g )
  {
    if( isBorderPainted() )
    {
      if( getModel().isPressed() )
      {
        _borderLowered.paintBorder( this, g, 0, 0, getWidth(), getHeight() );
      }
      else
      {
        _borderRaised.paintBorder( this, g, 0, 0, getWidth(), getHeight() );
      }
    }
  }

  /**
   * Creates a button from an action and displays only the action's icon, not
   * the name (unless the icon is null, in which case the name will be
   * displayed).
   *
   * @param action the Action associated with this button
   */
  public ToolBarButton( Action action )
  {
    this();
    setAction( action );
  }
}