package editor.util;

import editor.actions.UpdateNotifier;
import gw.util.GosuObjectUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 */
public class XPToolbarButton extends JButton
{
  private static final int DEFAULT_MARGIN = 2;

  private boolean _bConstantBorder;

  /**
   * Creates a button with an icon.
   *
   * @param icon The icon that should be display for this button.
   */
  public XPToolbarButton( String text, Icon icon, int iMargin )
  {
    super( text, icon );

    setUI( BasicButtonUI.createUI( this ) );
    setBorderPainted( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setBackground(EditorUtilities.CONTROL );
    setContentAreaFilled( false );
    setOpaque( true );
    EventQueue.invokeLater( () -> {
      setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( iMargin, iMargin, iMargin, iMargin ) ) );
      setBackground(EditorUtilities.CONTROL );
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
              setBackground( EditorUtilities.XP_HIGHLIGHT_SELECTED_COLOR );
            }
            else
            {
              setBackground( EditorUtilities.XP_HIGHLIGHT_COLOR );
            }
          }
        }

        public void mouseExited( MouseEvent e )
        {
          setBorderPainted( isBorderConstant() );
          setBackground( _bkColor != null ? _bkColor :EditorUtilities.CONTROL );
        }

        public void mousePressed( MouseEvent e )
        {
          setBackground( EditorUtilities.XP_HIGHLIGHT_SELECTED_COLOR );
        }
      };
  }

  public XPToolbarButton( String text, Icon icon )
  {
    this( text, icon, DEFAULT_MARGIN );
  }

  public XPToolbarButton( Icon icon, int iMargin )
  {
    this( null, icon, iMargin );
  }

  public XPToolbarButton( Icon icon )
  {
    this( icon, DEFAULT_MARGIN );
  }

  public XPToolbarButton( String text )
  {
    this( text, null );
  }

  public XPToolbarButton()
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
  public XPToolbarButton( Action action )
  {
    this();
    setAction( action );
    UpdateNotifier.instance().addActionComponent( this );
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
