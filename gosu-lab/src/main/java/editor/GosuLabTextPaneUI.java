package editor;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.Caret;

@SuppressWarnings("unused")
public class GosuLabTextPaneUI extends BasicTextPaneUI
{
  public static ComponentUI createUI( JComponent c )
  {
    return new GosuLabTextPaneUI();
  }

  protected Caret createCaret()
  {
    return new GosuLabTextUI.Caret();
  }
}
