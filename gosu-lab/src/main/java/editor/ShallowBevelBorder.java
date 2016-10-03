/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;


import javax.swing.border.BevelBorder;
import java.awt.*;


/**
 * A class which implements a simple 1 line bevel border.
 */
public class ShallowBevelBorder extends BevelBorder
{
  private static Color HIGHLIGHT_COLOR = Scheme.active().getControlLight();
  private static Color SHADOW_COLOR = Scheme.active().getControlShadow();


  public ShallowBevelBorder( int bevelType )
  {
    super( bevelType, HIGHLIGHT_COLOR, SHADOW_COLOR );
  }

  public ShallowBevelBorder( int bevelType, Color highlight, Color shadow )
  {
    super( bevelType, highlight.brighter(), highlight, shadow, shadow.brighter() );
  }

  protected void paintRaisedBevel( Component c, Graphics g, int x, int y, int width, int height )
  {
    Color oldColor = g.getColor();
    int h = height;
    int w = width;

    g.translate( x, y );

    g.setColor( getHighlightInnerColor( c ) );
    g.drawLine( 0, 0, 0, h - 2 );
    g.drawLine( 1, 0, w - 2, 0 );

    g.setColor( getShadowOuterColor( c ) );
    g.drawLine( 0, h - 1, w - 1, h - 1 );
    g.drawLine( w - 1, 0, w - 1, h - 2 );


    g.translate( -x, -y );
    g.setColor( oldColor );
  }

  protected void paintLoweredBevel( Component c, Graphics g, int x, int y, int width, int height )
  {
    Color oldColor = g.getColor();
    int h = height;
    int w = width;

    g.translate( x, y );

    g.setColor( getShadowOuterColor( c ) );
    g.drawLine( 0, 0, 0, h - 1 );
    g.drawLine( 1, 0, w - 1, 0 );

    g.setColor( getHighlightInnerColor( c ) );
    g.drawLine( 1, h - 1, w - 1, h - 1 );
    g.drawLine( w - 1, 1, w - 1, h - 2 );

    g.translate( -x, -y );
    g.setColor( oldColor );
  }

  public Insets getBorderInsets( Component c )
  {
    return new Insets( 1, 1, 1, 1 );
  }

  public Insets getBorderInsets( Component c, Insets insets )
  {
    insets.left = insets.top = insets.right = insets.bottom = 1;
    return insets;
  }
}