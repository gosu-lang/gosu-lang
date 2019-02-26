package editor;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;

@SuppressWarnings("unused")
public class GosuLabRadioButtonUI extends BasicRadioButtonUI
{
  private static final GosuLabRadioButtonUI INSTANCE = new GosuLabRadioButtonUI();

  protected int dashedRectGapX;
  protected int dashedRectGapY;
  protected int dashedRectGapWidth;
  protected int dashedRectGapHeight;

  protected Color focusColor;

  private boolean initialized = false;

  // ********************************
  //          Create PLAF
  // ********************************
  public static ComponentUI createUI( JComponent c )
  {
    return INSTANCE;
  }

  // ********************************
  //           Defaults
  // ********************************
  public void installDefaults( AbstractButton b )
  {
    super.installDefaults( b );
    if( !initialized )
    {
      dashedRectGapX = ((Integer)UIManager.get( "Button.dashedRectGapX" )).intValue();
      dashedRectGapY = ((Integer)UIManager.get( "Button.dashedRectGapY" )).intValue();
      dashedRectGapWidth = ((Integer)UIManager.get( "Button.dashedRectGapWidth" )).intValue();
      dashedRectGapHeight = ((Integer)UIManager.get( "Button.dashedRectGapHeight" )).intValue();
      focusColor = UIManager.getColor( getPropertyPrefix() + "focus" );
      initialized = true;
    }
  }

  protected void uninstallDefaults( AbstractButton b )
  {
    super.uninstallDefaults( b );
    initialized = false;
  }

  protected Color getFocusColor()
  {
    return focusColor;
  }

  // ********************************
  //          Paint Methods
  // ********************************

  /**
   * Overridden method to render the text without the mnemonic
   */
  protected void paintText( Graphics g, AbstractButton b, Rectangle textRect, String text )
  {
    GraphicsUtil.paintText( g, b, textRect, text, getTextShiftOffset() );
  }


  protected void paintFocus( Graphics g, Rectangle textRect, Dimension d )
  {
    g.setColor( getFocusColor() );
    BasicGraphicsUtils.drawDashedRect( g, textRect.x, textRect.y, textRect.width, textRect.height );
  }

  // ********************************
  //          Layout Methods
  // ********************************
  public Dimension getPreferredSize( JComponent c )
  {
    Dimension d = super.getPreferredSize( c );

    /* Ensure that the width and height of the button is odd,
     * to allow for the focus line if focus is painted
     */
    AbstractButton b = (AbstractButton)c;
    if( d != null && b.isFocusPainted() )
    {
      if( d.width % 2 == 0 )
      {
        d.width += 1;
      }
      if( d.height % 2 == 0 )
      {
        d.height += 1;
      }
    }
    return d;
  }

}