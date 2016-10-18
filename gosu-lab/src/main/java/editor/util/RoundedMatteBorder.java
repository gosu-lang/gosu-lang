package editor.util;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 */
public class RoundedMatteBorder extends AbstractBorder
{
  private static final int DEF_DIAMETER = 10;
  private static final int DEF_INSET = DEF_DIAMETER/2;
  private final Color _color;
  private Insets _insets;
  private Insets _margin;

  public RoundedMatteBorder( int thickness, Color color )
  {
    this( thickness, thickness, thickness, thickness, DEF_INSET, color );
  }

  public RoundedMatteBorder( int thickness, int marginThickness, Color color )
  {
    this( thickness, thickness, thickness, thickness, marginThickness, color );
  }

  public RoundedMatteBorder( int top, int left, int bottom, int right, Color color )
  {
    this( top, left, bottom, right, DEF_INSET, color );
  }

  public RoundedMatteBorder( int top, int left, int bottom, int right, int marginThickness, Color color )
  {
    this( top, left, bottom, right, marginThickness, marginThickness, color );
  }

  public RoundedMatteBorder( int top, int left, int bottom, int right, int horzInset, int vertInset, Color color )
  {
    this( top, left, bottom, right, vertInset, horzInset, vertInset, horzInset, color );
  }

  public RoundedMatteBorder( int top, int left, int bottom, int right, int topInset, int leftInset, int bottomInset, int rightInset, Color color )
  {
    _insets = new Insets( top, left, bottom, right );
    _margin = new Insets( topInset, leftInset, bottomInset, rightInset );
    _color = color;
  }

  public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
  {
    Graphics2D g2 = (Graphics2D)g.create();
    g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    int r = DEF_DIAMETER;
    g2.translate( x, y );
    g2.setColor( _color );

    g2.fillRect( r/2, 0, width - r - 1, _insets.top );
    g2.fillRect( 0, r/2, _insets.left, height - r - 1 );
    g2.fillRect( r/2 , height - _insets.bottom - 1, width - r - 1, _insets.bottom );
    g2.fillRect( width - _insets.right - 1, r/2, _insets.right, height - r - 1 );

    RoundRectangle2D round = new RoundRectangle2D.Float( 0, 0, width - 1, height - 1, r, r );
    Container parent = c.getParent();
    if( parent != null )
    {
      g2.setColor( parent.getBackground() );
      Area corner = new Area( new Rectangle2D.Float( 0, 0, width, height ) );
      corner.subtract( new Area( round ) );
      g2.fill( corner );
    }

    BasicStroke stroke = new BasicStroke( 1.0f );
    g2.setStroke( stroke );
    g2.setColor( _color );
    g2.draw( new Arc2D.Double( 0, 0, r, r, 90, 90, Arc2D.OPEN ) );
    g2.draw( new Arc2D.Double( width - r - 2, 0, r, r, 90, -90, Arc2D.OPEN ) );
    g2.draw( new Arc2D.Double( width - r - 2, height - r - 2, r, r, 0, -90, Arc2D.OPEN ) );
    g2.draw( new Arc2D.Double( 0, height - r - 2, r, r, 180, 90, Arc2D.OPEN ) );

    g2.dispose();
  }

  @Override
  public Insets getBorderInsets( Component c )
  {
    return _margin;
  }

  @Override
  public Insets getBorderInsets( Component c, Insets insets )
  {
    insets.set( _margin.top, _margin.left, _margin.bottom, _margin.right );
    return insets;
  }
}