package editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 */
public class LabTreeUi extends BasicTreeUI
{
  static protected final int HALF_SIZE = 4;
  static protected final int SIZE = 9;

  public static ComponentUI createUI( JComponent c )
  {
    return new LabTreeUi();
  }

  /**
   * Returns the default cell renderer that is used to do the stamping of each node.
   */
  protected TreeCellRenderer createDefaultCellRenderer()
  {
    return new DefaultTreeCellRenderer();
  }

  /**
   * The minus sign button icon
   */
  public static class ExpandedIcon implements Icon, Serializable
  {
    static public Icon createExpandedIcon()
    {
      return new ExpandedIcon();
    }

    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      Color backgroundColor = c.getBackground();

      if( backgroundColor != null )
      {
        g.setColor( backgroundColor );
      }
      else
      {
        g.setColor( Scheme.active().getWindow() );
      }
      g.fillRect( x, y, SIZE - 1, SIZE - 1 );
      g.setColor( Scheme.active().getTreeHandleBorderColor() );
      g.drawRect( x, y, SIZE - 1, SIZE - 1 );
      g.setColor( Scheme.active().getWindowText() );
      g.drawLine( x + 2, y + HALF_SIZE, x + (SIZE - 3), y + HALF_SIZE );
    }

    public int getIconWidth()
    {
      return SIZE;
    }

    public int getIconHeight()
    {
      return SIZE;
    }
  }

  /**
   * The plus sign button icon
   */
  public static class CollapsedIcon extends ExpandedIcon
  {
    static public Icon createCollapsedIcon()
    {
      return new CollapsedIcon();
    }

    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      super.paintIcon( c, g, x, y );
      g.drawLine( x + HALF_SIZE, y + 2, x + HALF_SIZE, y + (SIZE - 3) );
    }
  }
}
