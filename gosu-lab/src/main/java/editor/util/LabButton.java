package editor.util;

import editor.LabButtonUI;
import editor.Scheme;
import editor.actions.UpdateNotifier;
import gw.util.GosuObjectUtil;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

/**
 */
public class LabButton extends JButton
{
  public LabButton( String text, Icon icon )
  {
    super( text, icon );

    setUI( LabButtonUI.createUI( this ) );
    addMouseListener( createMouseListener() );
  }

  public LabButton( Action action )
  {
    this();
    EventQueue.invokeLater( () -> setAction( action ) );
    UpdateNotifier.instance().addActionComponent( this );
  }

  public LabButton( Icon icon )
  {
    this( null, icon );
  }

  public LabButton( String text )
  {
    this( text, null );
  }

  public LabButton()
  {
    this( null, null );
  }

  @Override
  public void addNotify()
  {
    super.addNotify();
    setBackground( getParent().getBackground() );
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
          setBackground( _bkColor != null ? _bkColor : getParent().getBackground() );
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
  public String getToolTipText()
  {
    if( getAction() != null )
    {
      String tip;

      tip = GosuObjectUtil.toString( getAction().getValue( Action.SHORT_DESCRIPTION ) );
      if( tip == null || tip.isEmpty() )
      {
        tip = GosuObjectUtil.toString( getAction().getValue( Action.NAME ) );
      }

      String value = (String)getAction().getValue( Action.ACCELERATOR_KEY );
      if( value != null && !value.isEmpty() )
      {
        tip += " (" + value + ")";
      }
      return tip;
    }
    else
    {
      return super.getToolTipText();
    }
  }
}
