package editor;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;

@SuppressWarnings("unused")
public class GosuLabEditorPaneUI extends BasicEditorPaneUI
{
  public static ComponentUI createUI( JComponent c )
  {
    return new GosuLabEditorPaneUI();
  }

  protected javax.swing.text.Caret createCaret()
  {
    return new GosuLabTextUI.Caret();
  }
}