/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;


import editor.util.EditorUtilities;
import editor.util.XPToolbarButton;
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
  private Color _clrBkgnd;
  private boolean _bRollover;
  private boolean _bMenu;

  public static final Color XP_BORDER_COLOR = EditorUtilities.XP_BORDER_COLOR;
  private static final Color XP_TOGGLE_ROLLOVER_COLOR = EditorUtilities.XP_HIGHLIGHT_COLOR;
  private static final Color XP_TOGGLE_SELECTED_COLOR = EditorUtilities.XP_HIGHLIGHT_SELECTED_COLOR;

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

    _clrBkgnd = editor.util.EditorUtilities.CONTROL_BACKGROUND;

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
      setBackground( _clrBkgnd );
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
      g.setColor( editor.util.EditorUtilities.CONTROL_SHADOW );
      g.drawLine( iMenuIconX - 2, 0, iMenuIconX - 2, getHeight() );
      g.setColor( editor.util.EditorUtilities.CONTROL_LIGHT );
      g.drawLine( iMenuIconX - 1, 0, iMenuIconX - 1, getHeight() );
    }
  }


  public void setClrBkgnd( Color clrBkgnd )
  {
    _clrBkgnd = clrBkgnd;
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
  public String getToolTipText()
  {
    String superText = super.getToolTipText();
    if( superText == null || superText.length() == 0 )
    {
      return null;  // Swing will not register us with the tooltip manager unless it detects a change
    }
    if( getAction() != null )
    {
      return GosuObjectUtil.toString( getAction().getValue( Action.SHORT_DESCRIPTION ) );
    }
    else
    {
      return superText;
    }
  }
}
