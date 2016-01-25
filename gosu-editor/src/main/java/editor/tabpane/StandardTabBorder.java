package editor.tabpane;

import javax.swing.border.Border;
import java.awt.*;

/**
 */
public class StandardTabBorder implements Border
{
  public boolean isBorderOpaque()
  {
    return true;
  }

  public Polygon getBorderPoly( Component c )
  {
    int x = 0;
    int y = 0;
    int width = c.getWidth();
    int height = c.getHeight();
    ITab tab = getTabFromComponent( c );
    Dimension inner = tab.getInnerSize();
    TabPosition tabPosition = tab.getTabPosition();
    if( tabPosition == TabPosition.LEFT ||
        tabPosition == TabPosition.RIGHT )
    {
      int iTemp = inner.width;
      inner.width = inner.height;
      inner.height = iTemp;

      iTemp = width;
      width = height;
      height = iTemp;
    }
    Polygon poly = new Polygon();
    poly.addPoint( x, y + height-1 );
    poly.addPoint( x + inner.height, y+3 );
    poly.addPoint( x + inner.height+2+3, y );
    poly.addPoint( x + width-3, y );
    poly.addPoint( x + width-1, y+2 );
    poly.addPoint( x + width-1, y + height-1 );
    poly.addPoint( x, y + height-1 );

    return poly;
  }

  public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
  {
    Polygon poly = getBorderPoly( c );
    g.setColor( SystemColor.controlShadow );
    g.drawPolygon( poly );
    ITab tab = getTabFromComponent( c );
    if( tab.isSelected() )
    {
      g.setColor( ((StandardTab)tab).getGradient() );
      g.drawLine( x+1, y + height-1, x + width-2, y + height-1 );
    }
  }

  public Insets getBorderInsets( Component c )
  {
    ITab tab = getTabFromComponent( c );
    Dimension inner = tab.getInnerSize();
    TabPosition tabPosition = tab.getTabPosition();
    if( tabPosition == TabPosition.TOP )
    {
      return new Insets( 2, inner.height, 2, 2 );
    }
    else if( tabPosition == TabPosition.BOTTOM )
    {
      return new Insets( 2, inner.height, 2, 2 );
    }
    else if( tabPosition == TabPosition.LEFT )
    {
      return new Insets( 2, 2, inner.width, 2 );
    }
    else if( tabPosition == TabPosition.RIGHT )
    {
      return new Insets( inner.width, 2, 2, 2 );
    }
    else
    {
      throw new IllegalStateException( "Unknown TabPosition" );
    }
  }

  private ITab getTabFromComponent( Component c )
  {
    ITab tab = TabContainer.tabFromComponent( c );
    if( tab == null )
    {
      throw new IllegalArgumentException();
    }
    return tab;
  }
}
