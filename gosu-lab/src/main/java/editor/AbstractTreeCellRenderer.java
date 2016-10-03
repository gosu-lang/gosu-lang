package editor;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 */
public abstract class AbstractTreeCellRenderer<T> extends JLabel implements TreeCellRenderer
{
  private boolean _bSelected;
  private T _node;
  private JTree _tree;

  public AbstractTreeCellRenderer( JTree tree )
  {
    _tree = tree;
  }

  public Component getTreeCellRendererComponent( JTree tree, Object value,
                                                 boolean bSelected, boolean bExpanded,
                                                 boolean bLeaf, int iRow, boolean bHasFocus )
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
    _tree.repaint();
  }

  public abstract void configure();

  /** */
  public void paint( Graphics g )
  {
//    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
//                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
//    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_RENDERING,
//                                      RenderingHints.VALUE_RENDER_QUALITY );
    Color bkColor;

    boolean bFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == _tree;
    if( _bSelected )
    {
      bkColor = _tree.isEnabled() && bFocus
                ? Scheme.active().getActiveCaption()
                : Scheme.active().getControl();
    }
    else
    {
      bkColor = _tree.getBackground();
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
        g.setColor( _tree.isEnabled() && bFocus ? Scheme.active().getXpBorderColor() : Scheme.active().getControlShadow() );
        g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
      }
      g.setColor( bkColor );
    }

    setForeground( Scheme.active().getControlText() );

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
