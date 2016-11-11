package editor;

import java.util.function.Supplier;
import javax.swing.*;
import java.awt.*;

/**
 */
public abstract class AbstractListCellRenderer<T> extends JLabel implements ListCellRenderer<T>
{
  private boolean _bSelected;
  private T _node;
  private Supplier<JComponent> _list;
  private int _index;
  private boolean _bRenderAsIfFocused;

  public AbstractListCellRenderer( JComponent list )
  {
    this( list, false );
  }
  public AbstractListCellRenderer( JComponent list, boolean bRenderAsIfFocused )
  {
    this( () -> list, bRenderAsIfFocused );
  }

  public AbstractListCellRenderer( Supplier<JComponent> list )
  {
    this( list, false );
  }

  public AbstractListCellRenderer( Supplier<JComponent> list, boolean bRenderAsIfFocused )
  {
    _list = list;
    _bRenderAsIfFocused = bRenderAsIfFocused;
  }

  public Component getListCellRendererComponent( JList tree, Object value, int index, boolean bSelected, boolean cellHasFocus )
  {
    if( value != null )
    {
      //noinspection unchecked
      _node = (T)value;
      _bSelected = bSelected;
      _index = index;
      configure();
    }

    return this;
  }

  protected T getNode()
  {
    return _node;
  }

  public int getIndex()
  {
    return _index;
  }

  public void update()
  {
    _list.get().repaint();
  }

  public abstract void configure();

  /** */
  public void paint( Graphics g )
  {
    Color bkColor;

    boolean bFocus = _bRenderAsIfFocused || KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == _list.get();
    if( _bSelected )
    {
      bkColor = _list.get().isEnabled() && bFocus
                ? Scheme.active().getActiveCaption()
                : Scheme.active().getControl();
    }
    else
    {
      bkColor = _list.get().getBackground();
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
        g.setColor( _list.get().isEnabled() && bFocus ? Scheme.active().getXpBorderColor() : Scheme.active().getControlShadow() );
        g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
      }
      g.setColor( bkColor );
    }

    setForeground( Scheme.active().getWindowText() );

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
