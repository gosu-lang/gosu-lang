package editor.util;

import editor.LabScheme;
import editor.Scheme;
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
public class LabToolbarButton extends JButton
{
  private static final int DEFAULT_MARGIN = 3;

  private boolean _bConstantBorder;
  private boolean _bShowText;


  public LabToolbarButton( String text, Icon icon, int iMargin )
  {
    this( text, icon, iMargin, iMargin );
  }
  public LabToolbarButton( String text, Icon icon, int iMarginW, int iMarginH )
  {
    super( text, icon );

    setUI( BasicButtonUI.createUI( this ) );
    setBorderPainted( false );
    setMargin( new Insets( 1, 1, 1, 1 ) );
    setBackground( Scheme.active().getControl() );
    setContentAreaFilled( false );
    setOpaque( true );
    EventQueue.invokeLater( () -> {
      setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( Scheme.active().getXpBorderColor() ), BorderFactory.createEmptyBorder( iMarginH, iMarginW, iMarginH, iMarginW ) ) );
      setBackground( Scheme.active().getControl() );
    } );
    _bShowText = false;
    addMouseListener( createMouseListener() );
  }

  public LabToolbarButton( Action action )
  {
    this();
    EventQueue.invokeLater( () -> setAction( action ) );
  }

  public LabToolbarButton( String text, Icon icon )
  {
    this( text, icon, DEFAULT_MARGIN );
  }

  public LabToolbarButton( Icon icon, int iMargin )
  {
    this( null, icon, iMargin );
  }

  public LabToolbarButton( Icon icon )
  {
    this( icon, DEFAULT_MARGIN );
  }

  public LabToolbarButton( String text )
  {
    this( text, null );
  }

  public LabToolbarButton()
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
              setBackground( Scheme.active().getXpHighlightSelectedColor() );
            }
            else
            {
              setBackground( Scheme.active().getXpHighlightColor() );
            }
          }
        }

        public void mouseExited( MouseEvent e )
        {
          setBorderPainted( isBorderConstant() );
          setBackground( _bkColor != null ? _bkColor : Scheme.active().getControl() );
        }

        public void mousePressed( MouseEvent e )
        {
          setBackground( Scheme.active().getXpHighlightSelectedColor() );
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
