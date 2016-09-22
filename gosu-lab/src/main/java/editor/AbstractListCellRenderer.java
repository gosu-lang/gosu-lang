package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;

/**
 */
public abstract class AbstractListCellRenderer<T> extends JLabel implements ListCellRenderer<T>
{
  private boolean _bSelected;
  private T _node;
  private JComponent _list;

  public AbstractListCellRenderer( JComponent list )
  {
    _list = list;
  }

  public Component getListCellRendererComponent( JList tree, Object value, int index, boolean bSelected, boolean cellHasFocus )
  {
    if( value != null )
    {
      //noinspection unchecked
      _node = (T)value;
      _bSelected = bSelected;
      configure();
    }

    return this;
  }

  protected T getNode()
  {
    return _node;
  }

  public void update()
  {
    _list.repaint();
  }

  public abstract void configure();

  /** */
  public void paint( Graphics g )
  {
    Color bkColor;

    boolean bFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == _list;
    if( _bSelected )
    {
      bkColor = _list.isEnabled() && bFocus
                ? EditorUtilities.ACTIVE_CAPTION
                : EditorUtilities.CONTROL;
    }
    else
    {
      bkColor = _list.getBackground();
      if( bkColor == null )
      {
        bkColor = getBackground();
      }
    }

    if( bkColor != null )
    {
      g.setColor( bkColor );
      g.fillRect( 0, 0, getWidth() - 1, getHeight() - 1 );

      if( _bSelected )
      {
        g.setColor( _list.isEnabled() && bFocus ? EditorUtilities.XP_BORDER_COLOR : EditorUtilities.CONTROL_SHADOW );
        g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
      }
      g.setColor( bkColor );
    }

    setForeground( EditorUtilities.CONTROL_TEXT );

    super.paint( g );
  }

  public Dimension getPreferredSize()
  {
    Dimension dim = super.getPreferredSize();

    if( dim != null )
    {
      dim = new Dimension( dim.width + 3, dim.height );
    }

    return dim;
  }
}
