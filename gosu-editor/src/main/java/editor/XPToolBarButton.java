/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.util.GosuObjectUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * An toolbar button sporting the XP-style.
 */
public class XPToolBarButton extends JButton
{
  private static final int DEFAULT_MARGIN = 2;
  public static final Color XP_BORDER_COLOR = new Color( 49, 106, 197 );
  public static final Color XP_HIGHLIGHT_TOGGLE_COLOR = new Color( 225, 230, 232 );
  public static final Color XP_HIGHLIGHT_COLOR = new Color( 190, 205, 224 );
  public static final Color XP_HIGHLIGHT_SELECTED_COLOR = new Color( 152, 179, 219 );

  private boolean _bConstantBorder;

  /**
   * Creates a button with an icon.
   *
   * @param icon The icon that should be display for this button.
   */
  public XPToolBarButton( String text, Icon icon, int iMargin )
  {
    super( text, icon );

    final Border border = BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) );
    setBorderPainted( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setContentAreaFilled( false );
    setOpaque( true );
    setBackground( editor.util.EditorUtilities.CONTROL_BACKGROUND );
    EventQueue.invokeLater( new Runnable()
    {
      public void run()
      {
        setBorder( border );
      }
    } );

    addMouseListener( createMouseListener() );
    setToolTipText( "" );  // fake out Swing into registering us with the tooltip manager
  }

  protected MouseListener createMouseListener()
  {
    return
      new MouseAdapter()
      {
        Color _bkColor;

        public void mouseEntered( MouseEvent e )
        {
          if( isEnabled() )
          {
            _bkColor = getBackground();
            setBorderPainted( true );
            if( getModel().isArmed() )
            {
              setBackground( XP_HIGHLIGHT_SELECTED_COLOR );
            }
            else
            {
              setBackground( XP_HIGHLIGHT_COLOR );
            }
          }
        }

        public void mouseExited( MouseEvent e )
        {
          setBorderPainted( isBorderConstant() );
          setBackground( _bkColor != null ? _bkColor : editor.util.EditorUtilities.CONTROL_BACKGROUND );
        }

        public void mousePressed( MouseEvent e )
        {
          setBackground( XP_HIGHLIGHT_SELECTED_COLOR );
        }
      };
  }

  public XPToolBarButton( String text, Icon icon )
  {
    this( text, icon, DEFAULT_MARGIN );
  }

  public XPToolBarButton( Icon icon, int iMargin )
  {
    this( null, icon, iMargin );
  }

  public XPToolBarButton( Icon icon )
  {
    this( icon, DEFAULT_MARGIN );
  }

  public XPToolBarButton( String text )
  {
    this( text, null );
  }

  public XPToolBarButton()
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
    setBorderPainted( isBorderConstant() );
  }

  /**
   * Creates a button from an action and displays only the action's icon, not
   * the name (unless the icon is null, in which case the name will be
   * displayed).
   *
   * @param action the Action associated with this button
   */
  public XPToolBarButton( Action action )
  {
    this();
    setAction( action );
/*
    if(action.getValue(Action.SMALL_ICON) != null) {
      setText(null);
    }
*/
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

  /**
   * Returns true if the border should be painted all the time instead of only during mouse-overs.
   */
  public boolean isBorderConstant()
  {
    return _bConstantBorder;
  }

  public void setBorderConstant( boolean bConstantBorder )
  {
    _bConstantBorder = bConstantBorder;
    setBorderPainted( _bConstantBorder );
  }

}