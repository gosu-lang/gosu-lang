package editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/**
 * Needed to properly handle painting of disabled text
 */
public class LabButtonUI extends WindowsButtonUI
{
  public static ComponentUI createUI( JComponent c )
  {
    return new LabButtonUI();
  }

  @Override
  protected void paintButtonPressed( Graphics g, AbstractButton b )
  {
    g.setColor( Scheme.active().getControlHighlight() );
    g.fillRect( 1, 1, b.getWidth() - 2, b.getHeight() - 2 );
  }

  @Override
  protected void paintText( Graphics g, AbstractButton b, Rectangle textRect, String text )
  {
    ButtonModel model = b.getModel();
    FontMetrics fm = SwingUtilities2.getFontMetrics( b, g );
    int mnemIndex = b.getDisplayedMnemonicIndex();

    if( model.isEnabled() )
    {
      g.setColor( b.getForeground() );
    }
    else
    {
      g.setColor( UIManager.getColor( "Button.disabledForeground" ) );
    }
    SwingUtilities2.drawStringUnderlineCharAt( b, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent() );
  }
}

class WindowsButtonUI extends BasicButtonUI
{
  protected int dashedRectGapX;
  protected int dashedRectGapY;
  protected int dashedRectGapWidth;
  protected int dashedRectGapHeight;

  protected Color focusColor;

  private boolean defaults_initialized = false;

  private static final Object WINDOWS_BUTTON_UI_KEY = new Object();

  // ********************************
  //          Create PLAF
  // ********************************
  public static ComponentUI createUI(JComponent c) {
    AppContext appContext = AppContext.getAppContext();
    WindowsButtonUI windowsButtonUI =
      (WindowsButtonUI) appContext.get(WINDOWS_BUTTON_UI_KEY);
    if (windowsButtonUI == null) {
      windowsButtonUI = new WindowsButtonUI();
      appContext.put(WINDOWS_BUTTON_UI_KEY, windowsButtonUI);
    }
    return windowsButtonUI;
  }


  // ********************************
  //            Defaults
  // ********************************
  protected void installDefaults(AbstractButton b) {
    super.installDefaults(b);
    if(!defaults_initialized) {
      String pp = getPropertyPrefix();
      dashedRectGapX = UIManager.getInt(pp + "dashedRectGapX");
      dashedRectGapY = UIManager.getInt(pp + "dashedRectGapY");
      dashedRectGapWidth = UIManager.getInt(pp + "dashedRectGapWidth");
      dashedRectGapHeight = UIManager.getInt(pp + "dashedRectGapHeight");
      focusColor = UIManager.getColor(pp + "focus");
      defaults_initialized = true;
    }

  }

  protected void uninstallDefaults(AbstractButton b) {
    super.uninstallDefaults(b);
    defaults_initialized = false;
  }

  protected Color getFocusColor() {
    return focusColor;
  }

  // ********************************
  //         Paint Methods
  // ********************************

  protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect){

    // focus painted same color as text on Basic??
    int width = b.getWidth();
    int height = b.getHeight();
    g.setColor(getFocusColor());
    BasicGraphicsUtils.drawDashedRect(g, dashedRectGapX, dashedRectGapY,
      width - dashedRectGapWidth, height - dashedRectGapHeight);
  }

  protected void paintButtonPressed(Graphics g, AbstractButton b){
    setTextShiftOffset();
  }

  // ********************************
  //          Layout Methods
  // ********************************
  public Dimension getPreferredSize(JComponent c) {
    Dimension d = super.getPreferredSize(c);

    /* Ensure that the width and height of the button is odd,
     * to allow for the focus line if focus is painted
     */
    AbstractButton b = (AbstractButton)c;
    if (d != null && b.isFocusPainted()) {
      if(d.width % 2 == 0) { d.width += 1; }
      if(d.height % 2 == 0) { d.height += 1; }
    }
    return d;
  }
}
