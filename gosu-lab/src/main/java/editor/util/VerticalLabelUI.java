package editor.util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 */
public class VerticalLabelUI extends BasicLabelUI
{
  private static Rectangle g_paintIconR = new Rectangle();
  private static Rectangle g_paintTextR = new Rectangle();
  private static Rectangle g_paintViewR = new Rectangle();
  private static Insets g_paintViewInsets = new Insets( 0, 0, 0, 0 );

//  static
//  {
//    labelUI = new VerticalLabelUI( true );
//  }

  protected boolean _bClockwise;

  public VerticalLabelUI( boolean bClockwise )
  {
    super();
    _bClockwise = bClockwise;
  }


  public Dimension getPreferredSize( JComponent c )
  {
    Dimension dim = super.getPreferredSize( c );
    Insets insets = c.getInsets();
    // Invert the border
    dim.width -= (insets.left + insets.right);
    dim.height -= (insets.top + insets.bottom);
    dim.width += (insets.top + insets.bottom);
    dim.height += (insets.left + insets.right);
    return new Dimension( dim.height, dim.width );
  }

  public void paint( Graphics g, JComponent c )
  {
    JLabel label = (JLabel)c;
    String text = label.getText();
    Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

    if( (icon == null) && (text == null) )
    {
      return;
    }

    FontMetrics fm = g.getFontMetrics();
    g_paintViewInsets = c.getInsets( g_paintViewInsets );

    g_paintViewR.x = g_paintViewInsets.top;
    g_paintViewR.y = g_paintViewInsets.left;

    // Use inverted height & width
    g_paintViewR.height = c.getWidth();
    g_paintViewR.width = c.getHeight();
    g_paintViewR.width -= (g_paintViewInsets.top + g_paintViewInsets.bottom);
    g_paintViewR.height -= (g_paintViewInsets.left + g_paintViewInsets.right);

    g_paintIconR.x = g_paintIconR.y = g_paintIconR.width = g_paintIconR.height = 0;
    g_paintTextR.x = g_paintTextR.y = g_paintTextR.width = g_paintTextR.height = 0;

    String clippedText =
      layoutCL( label, fm, text, icon, g_paintViewR, g_paintIconR, g_paintTextR );

    Graphics2D g2 = (Graphics2D)g;
    AffineTransform tr = g2.getTransform();
    if( _bClockwise )
    {
      g2.rotate( Math.PI / 2 );
      g2.translate( 0, -c.getWidth() );
    }
    else
    {
      g2.rotate( -Math.PI / 2 );
      g2.translate( -c.getHeight(), 0 );
    }

    if( icon != null )
    {
      icon.paintIcon( c, g, g_paintIconR.x, g_paintIconR.y );
    }

    if( text != null )
    {
      int textX = g_paintTextR.x;
      int textY = g_paintTextR.y + fm.getAscent();

      if( label.isEnabled() )
      {
        paintEnabledText( label, g, clippedText, textX, textY );
      }
      else
      {
        paintDisabledText( label, g, clippedText, textX, textY );
      }
    }

    g2.setTransform( tr );
  }
}
