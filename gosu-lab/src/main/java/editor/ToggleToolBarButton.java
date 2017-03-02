/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;


import gw.util.GosuObjectUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class ToggleToolBarButton extends JToggleButton
{

  private static final ImageIcon MENU_ICON = editor.util.EditorUtilities.loadIcon( "images/drop_down_arrow_3.gif" );

  private Border _activeBorder;
  private boolean _bRollover;
  private boolean _bMenu;

  public static final Color XP_BORDER_COLOR = Scheme.active().getXpBorderColor();
  private static final Color XP_TOGGLE_ROLLOVER_COLOR = Scheme.active().getXpHighlightColor();
  private static final Color XP_TOGGLE_SELECTED_COLOR = Scheme.active().getXpHighlightSelectedColor();
  private boolean _bShowText;

  /**
   * Creates a button with an icon.
   *
   * @param icon The icon that should be display for this button.
   */
  public ToggleToolBarButton( String text, Icon icon )
  {
    super( text, icon );

    setContentAreaFilled( false );
    setOpaque( true );

    _activeBorder = new CompoundBorder( new LineBorder( XP_BORDER_COLOR, 1 ),
                                        BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );

    setBorder( _activeBorder );
    setMargin( new Insets( 1, 1, 1, 1 ) );

    addMouseListener( new MouseAdapter()
    {
      @Override
      public void mouseEntered( MouseEvent e )
      {
        if( isEnabled() )
        {
          _bRollover = true;
          repaint();
        }
      }

      @Override
      public void mouseExited( MouseEvent e )
      {
        _bRollover = false;
        repaint();
      }
    } );
  }

  public ToggleToolBarButton( Icon icon )
  {
    this( null, icon );
  }

  public ToggleToolBarButton( String text )
  {
    this( text, null );
  }

  public ToggleToolBarButton()
  {
    this( null, null );
  }

  @Override
  public void addNotify()
  {
    super.addNotify();
    setBackground( getParent().getBackground() );
  }

  @Override
  public boolean isFocusable()
  {
    return false;
  }

  @Override
  public void setEnabled( boolean b )
  {
    super.setEnabled( b );
  }

  public boolean isMenu()
  {
    return _bMenu;
  }

  public void setMenu( boolean bMenu )
  {
    _bMenu = bMenu;
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    JComponent separator = (JComponent)getClientProperty( "_separatorComp" );
    if( separator != null )
    {
      separator.setVisible( bVisible );
    }
  }

  @Override
  protected void paintComponent( Graphics g )
  {
    if( _bRollover )
    {
      if( !isSelected() )
      {
        setBackground( XP_TOGGLE_ROLLOVER_COLOR );
      }
      else
      {
        setBackground( XP_TOGGLE_SELECTED_COLOR );
      }
    }
    else if( isSelected() )
    {
      setBackground( XP_TOGGLE_SELECTED_COLOR );
    }
    else
    {
      setBackground( getParent().getBackground() );
    }

    super.paintComponent( g );

    if( isMenu() )
    {

      // Paint the menu button

      int iMenuIconX = getWidth() - getInsets().right - MENU_ICON.getIconWidth();
      MENU_ICON.paintIcon( this, g,
                           iMenuIconX,
                           getInsets().top + getMargin().top );

      // Paint the menu button separator
      g.setColor( Scheme.active().getControlShadow() );
      g.drawLine( iMenuIconX - 2, 0, iMenuIconX - 2, getHeight() );
      g.setColor( Scheme.active().getControlLight() );
      g.drawLine( iMenuIconX - 1, 0, iMenuIconX - 1, getHeight() );
    }
  }

  @Override
  protected void paintBorder( Graphics g )
  {
    if( isSelected() || _bRollover )
    {
      _activeBorder.paintBorder( this, g, 0, 0, getWidth(), getHeight() );
    }
  }

  /**
   * Creates a button from an action and displays only the action's icon, not
   * the name (unless the icon is null, in which case the name will be
   * displayed).
   *
   * @param action the Action associated with this button
   */
  public ToggleToolBarButton( Action action )
  {
    this();
    setAction( action );
  }

  @Override
  public void setAction( Action a )
  {
    super.setAction( a );
    ToolTipManager.sharedInstance().registerComponent( this );
  }

  public boolean isShowText()
  {
    return _bShowText;
  }
  public void setShowText( boolean bShowText )
  {
    _bShowText = bShowText;
  }

  @Override
  public String getText()
  {
    return _bShowText ? super.getText() : null;
  }

  @Override
  public String getToolTipText()
  {
    if( getAction() != null )
    {
      String tip = GosuObjectUtil.toString( getAction().getValue( Action.SHORT_DESCRIPTION ) );
      if( tip == null || tip.isEmpty() )
      {
        tip = GosuObjectUtil.toString( getAction().getValue( Action.NAME ) );
      }
      return tip;
    }
    else
    {
      return super.getToolTipText();
    }
  }

}
