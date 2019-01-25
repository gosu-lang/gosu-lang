package editor;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import sun.swing.SwingUtilities2;

/**
 * Needed to properly handle painting of disabled text
 */
public class LabButtonUI extends BasicButtonUI
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
