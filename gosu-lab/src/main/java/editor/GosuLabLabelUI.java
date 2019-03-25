package editor;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;

@SuppressWarnings("unused")
public class GosuLabLabelUI extends BasicLabelUI
{
  private static final GosuLabLabelUI INSTANCE = new GosuLabLabelUI();

  public static ComponentUI createUI( JComponent c )
  {
    return INSTANCE;
  }

  protected void paintEnabledText( JLabel l, Graphics g, String s, int textX, int textY )
  {
    int mnemonicIndex = l.getDisplayedMnemonicIndex();

    g.setColor( l.getForeground() );
    SwingUtilities2.drawStringUnderlineCharAt( l, g, s, mnemonicIndex, textX, textY );
  }

  protected void paintDisabledText( JLabel l, Graphics g, String s, int textX, int textY )
  {
    int mnemonicIndex = l.getDisplayedMnemonicIndex();
    if( UIManager.getColor( "Label.disabledForeground" ) != null &&
        UIManager.getColor( "Label.disabledShadow" ) != null )
    {
      g.setColor( UIManager.getColor( "Label.disabledShadow" ) );
      SwingUtilities2.drawStringUnderlineCharAt( l, g, s, mnemonicIndex, textX + 1, textY + 1 );
      g.setColor( UIManager.getColor( "Label.disabledForeground" ) );
      SwingUtilities2.drawStringUnderlineCharAt( l, g, s, mnemonicIndex, textX, textY );
    }
    else
    {
      Color background = l.getBackground();
      g.setColor( background.brighter() );
      SwingUtilities2.drawStringUnderlineCharAt( l, g, s, mnemonicIndex, textX + 1, textY + 1 );
      g.setColor( background.darker() );
      SwingUtilities2.drawStringUnderlineCharAt( l, g, s, mnemonicIndex, textX, textY );
    }
  }
}
