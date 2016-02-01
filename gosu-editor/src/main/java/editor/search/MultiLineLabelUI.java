package editor.search;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.util.Vector;

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

public class MultiLineLabelUI extends javax.swing.plaf.basic.BasicLabelUI implements SwingConstants
{
  private static Rectangle _rcPaintIcon = new Rectangle();
  private static Rectangle _rcPaintText = new Rectangle();
  private static Rectangle _rcPaintView = new Rectangle();
  private static Insets _insetsPaintView = new Insets( 0, 0, 0, 0 );

  protected static final MultiLineLabelUI _multiLineLabelUI = new MultiLineLabelUI();


  //--------------------------------------------------------------------------------------------------
  public static javax.swing.plaf.ComponentUI createUI( JComponent c )
  {
    return _multiLineLabelUI;
  }

  //--------------------------------------------------------------------------------------------------
  protected Vector layoutCL( Graphics g, JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR, Rectangle textR )
  {
    return layoutCompoundLabel( g, (JComponent)label, fontMetrics, text, icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewR, iconR, textR, label.getIconTextGap() );
  }

  //--------------------------------------------------------------------------------------------------
  public void paint( Graphics g, JComponent c )
  {
    JLabel label = (JLabel)c;
    String strText = label.getText();
    Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();

    if( (icon == null) && (strText == null) )
    {
      return;
    }

    FontMetrics fm = g.getFontMetrics();
    if( (c.getHeight() < (fm.getHeight() + fm.getAscent() / 2)) ||
        (c.getClientProperty( "html" ) != null) )
    {
      // Only one *visible* line available, so might as well let base class handle it...
      // ...or the label is displaying html in which case we delegate.
      //
      // Note determining the height of a visible line is a fuzzy scheme i.e., can't simply
      // use height of font because we need to take into account the fact that a partial
      // visible line may not expose any of the characters on the line, so adding getAscent()/2
      // seems to be a nice choice.

      super.paint( g, c );
      return;
    }

    _insetsPaintView = c.getInsets( _insetsPaintView );

    _rcPaintView.x = _insetsPaintView.left;
    _rcPaintView.y = _insetsPaintView.top;
    _rcPaintView.width = c.getWidth() - (_insetsPaintView.left + _insetsPaintView.right);
    _rcPaintView.height = c.getHeight() - (_insetsPaintView.top + _insetsPaintView.bottom);

    _rcPaintIcon.x = _rcPaintIcon.y = _rcPaintIcon.width = _rcPaintIcon.height = 0;
    _rcPaintText.x = _rcPaintText.y = _rcPaintText.width = _rcPaintText.height = 0;

    Vector vtext = layoutCL( g, label, fm, strText, icon, _rcPaintView, _rcPaintIcon, _rcPaintText );

    if( icon != null )
    {
      icon.paintIcon( c, g, _rcPaintIcon.x, _rcPaintIcon.y );
    }

    _rcPaintText.y = _rcPaintText.y < 0 ? 0 : _rcPaintText.y;
    _rcPaintText.height = fm.getHeight();

    if( vtext != null )
    {
      g.setColor( label.getForeground() );

      int iLines = vtext.size();
      for( int i = 0; i < iLines; i++ )
      {
        StudioUtilities.drawStringInRectClipped( g, fm, (String)vtext.elementAt( i ), _rcPaintText, ((JLabel)c).getHorizontalAlignment(), ((JLabel)c).getVerticalAlignment(), false );

        _rcPaintText.y += fm.getHeight();
      }
    }
  }

  //--------------------------------------------------------------------------------------------------
  public Dimension getPreferredSizeWithVariableHeightOnly( JComponent c )
  {
    JLabel label = (JLabel)c;
    String strText = label.getText();
    Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();

    _insetsPaintView = c.getInsets( _insetsPaintView );

    _rcPaintView.x = _insetsPaintView.left;
    _rcPaintView.y = _insetsPaintView.top;
    _rcPaintView.width = c.getWidth() - (_insetsPaintView.left + _insetsPaintView.right);
    _rcPaintView.height = 0; // Set to zero so we can determine total lines of text

    _rcPaintIcon.x = _rcPaintIcon.y = _rcPaintIcon.width = _rcPaintIcon.height = 0;
    _rcPaintText.x = _rcPaintText.y = _rcPaintText.width = _rcPaintText.height = 0;

    FontMetrics fm = c.getFontMetrics( c.getFont() );
    Vector vtext = layoutCL( null, label, fm, strText, icon, _rcPaintView, _rcPaintIcon, _rcPaintText );

    int iWidth = c.getWidth() - (_insetsPaintView.left + _insetsPaintView.right);
    int iHeight = (fm.getHeight() * (vtext == null ? 0 : vtext.size())) - (_insetsPaintView.top + _insetsPaintView.bottom);

    return new Dimension( iWidth, iHeight );
  }

  //--------------------------------------------------------------------------------------------------
  public static Vector layoutCompoundLabel( Graphics g, JComponent c, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap )
  {
    boolean orientationIsLeftToRight = true;
    int hAlign = horizontalAlignment;
    int hTextPos = horizontalTextPosition;


    if( c != null )
    {
      if( !(c.getComponentOrientation().isLeftToRight()) )
      {
        orientationIsLeftToRight = false;
      }
    }


    // Translate LEADING/TRAILING values in horizontalAlignment
    // to LEFT/RIGHT values depending on the components orientation
    switch( horizontalAlignment )
    {
      case LEADING:
        hAlign = (orientationIsLeftToRight) ? LEFT : RIGHT;
        break;
      case TRAILING:
        hAlign = (orientationIsLeftToRight) ? RIGHT : LEFT;
        break;
    }

    // Translate LEADING/TRAILING values in horizontalTextPosition
    // to LEFT/RIGHT values depending on the components orientation
    switch( horizontalTextPosition )
    {
      case LEADING:
        hTextPos = (orientationIsLeftToRight) ? LEFT : RIGHT;
        break;
      case TRAILING:
        hTextPos = (orientationIsLeftToRight) ? RIGHT : LEFT;
        break;
    }

    return layoutCompoundLabelImpl( g, c, fm, text, icon, verticalAlignment, hAlign, verticalTextPosition, hTextPos, viewR, iconR, textR, textIconGap );
  }

  //--------------------------------------------------------------------------------------------------

  /**
   * Compute and return the location of the icons origin, the
   * location of origin of the text baseline, and a possibly clipped
   * version of the compound labels string.  Locations are computed
   * relative to the viewR rectangle.
   * This layoutCompoundLabel() does not know how to handle LEADING/TRAILING
   * values in horizontalTextPosition (they will default to RIGHT) and in
   * horizontalAlignment (they will default to CENTER).
   * Use the other version of layoutCompoundLabel() instead.
   */
  public static Vector layoutCompoundLabel( Graphics g, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap )
  {
    return layoutCompoundLabelImpl( g, null, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap );
  }

  //--------------------------------------------------------------------------------------------------

  /**
   * Compute and return the location of the icons origin, the
   * location of origin of the text baseline, and a possibly clipped
   * version of the compound labels string.  Locations are computed
   * relative to the viewR rectangle.
   * This layoutCompoundLabel() does not know how to handle LEADING/TRAILING
   * values in horizontalTextPosition (they will default to RIGHT) and in
   * horizontalAlignment (they will default to CENTER).
   * Use the other version of layoutCompoundLabel() instead.
   */
  private static Vector layoutCompoundLabelImpl( Graphics g, JComponent c,
                                                 FontMetrics fm,
                                                 String strText,
                                                 Icon icon,
                                                 int iVerticalAlignment, int iHorizontalAlignment,
                                                 int iVerticalTextPosition, int iHorizontalTextPosition,
                                                 Rectangle rcView, Rectangle rcIcon, Rectangle rcText,
                                                 int iTextIconGap )
  {

    if( icon != null )
    {
      rcIcon.width = icon.getIconWidth();
      rcIcon.height = icon.getIconHeight();
    }
    else
    {
      rcIcon.width = rcIcon.height = 0;
    }

    boolean bEmptyText = (strText == null) || (strText.length() == 0);

    int iGap = (bEmptyText || (icon == null)) ? 0 : iTextIconGap;

    int iAvailTextWidth;
    if( iHorizontalTextPosition == CENTER )
    {
      iAvailTextWidth = rcView.width;
    }
    else
    {
      iAvailTextWidth = rcView.width - (rcIcon.width + iGap);
    }

    Vector vtext = null;
    View viewHtml = null;
    if( bEmptyText )
    {
      vtext = new Vector();
      rcText.width = rcText.height = 0;
      strText = "";
      vtext.addElement( strText );
    }
    else
    {
      viewHtml = (c != null) ? (View)c.getClientProperty( "html" ) : null;
      if( viewHtml != null )
      {
        rcText.width = (int)viewHtml.getPreferredSpan( View.X_AXIS );
        rcText.height = (int)viewHtml.getPreferredSpan( View.Y_AXIS );
        if( rcText.width > iAvailTextWidth )
        {
          rcText.width = iAvailTextWidth;
        }
      }
      else
      {
        rcText.setBounds( rcView.x, rcView.y, iAvailTextWidth, rcView.height );
        vtext = StudioUtilities.drawTextWrapped( null, rcText, strText, fm.getFont(), 0, false, false );
        rcText.height = vtext.size() * fm.getHeight();
      }
    }

    //
    // Compute rcText.x,y given the iVerticalTextPosition and
    // iHorizontalTextPosition properties
    //

    if( iVerticalTextPosition == TOP )
    {
      if( iHorizontalTextPosition != CENTER )
      {
        rcText.y = 0;
      }
      else
      {
        rcText.y = -(rcText.height + iGap);
      }
    }
    else if( iVerticalTextPosition == CENTER )
    {
      rcText.y = (rcIcon.height / 2) - (rcText.height / 2);
    }
    else
    {  // (iVerticalTextPosition == BOTTOM)
      if( iHorizontalTextPosition != CENTER )
      {
        rcText.y = rcIcon.height - rcText.height;
      }
      else
      {
        rcText.y = (rcIcon.height + iGap);
      }
    }

    if( iHorizontalTextPosition == LEFT )
    {
      rcText.x = -(rcText.width + iGap);
    }
    else if( iHorizontalTextPosition == CENTER )
    {
      rcText.x = rcIcon.width / 2 - rcText.width / 2;
    }
    else
    {  // (iHorizontalTextPosition == RIGHT)
      rcText.x = rcIcon.width + iGap;
    }

    //
    // labelR is the rectangle that contains rcIcon and rcText.
    // Move it to its proper position given the labelAlignment
    // properties.
    //
    // To avoid actually allocating a Rectangle, Rectangle.union
    // has been inlined below.
    //

    int iLabel_x = Math.min( rcIcon.x, rcText.x );
    int iLabel_width = Math.max( rcIcon.x + rcIcon.width, rcText.x + rcText.width ) - iLabel_x;
    int iLabel_y = Math.min( rcIcon.y, rcText.y );
    int iLabel_height = Math.max( rcIcon.y + rcIcon.height, rcText.y + rcText.height ) - iLabel_y;

    int dx, dy;

    if( iVerticalAlignment == TOP )
    {
      dy = rcView.y - iLabel_y;
    }
    else if( iVerticalAlignment == CENTER )
    {
      dy = (rcView.y + (rcView.height / 2)) - (iLabel_y + (iLabel_height / 2));
    }
    else
    {  // (iVerticalAlignment == BOTTOM)
      dy = (rcView.y + rcView.height) - (iLabel_y + iLabel_height);
    }

    if( iHorizontalAlignment == LEFT )
    {
      dx = rcView.x - iLabel_x;
    }
    else if( iHorizontalAlignment == RIGHT )
    {
      dx = (rcView.x + rcView.width) - (iLabel_x + iLabel_width);
    }
    else
    {  // (iHorizontalAlignment == CENTER)
      dx = (rcView.x + (rcView.width / 2)) - (iLabel_x + (iLabel_width / 2));
    }

    //
    // Translate rcText and glypyR by dx,dy.
    //

    rcText.x += dx;
    rcText.y += dy;

    rcIcon.x += dx;
    rcIcon.y += dy;

    return vtext;
  }
}
