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
  private boolean _bShowText;


  public XPToolbarButton( String text, Icon icon, int iMargin )
  {
    this( text, icon, iMargin, iMargin );
  }
  public XPToolbarButton( String text, Icon icon, int iMarginW, int iMarginH )
  {
    super( text, icon );

    setUI( BasicButtonUI.createUI( this ) );
    setBorderPainted( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setBackground( EditorUtilities.CONTROL );
    setContentAreaFilled( false );
    setOpaque( true );
    EventQueue.invokeLater( () -> {
      setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( EditorUtilities.XP_BORDER_COLOR ), BorderFactory.createEmptyBorder( iMarginH, iMarginW, iMarginH, iMarginW ) ) );
      setBackground( EditorUtilities.CONTROL );
    } );
    _bShowText = false;
    addMouseListener( createMouseListener() );
  }

  public XPToolbarButton( Action action )
  {
    this();
    EventQueue.invokeLater( () -> setAction( action ) );
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

  @Override
  public void setAction( Action a )
  {
    super.setAction( a );
    UpdateNotifier.instance().addActionComponent( this );
    ToolTipManager.sharedInstance().registerComponent( this );
  }

  public boolean isText()
  {
    return _bShowText;
  }
  public void setShowText( boolean bShowText )
  {
    _bShowText = bShowText;
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
