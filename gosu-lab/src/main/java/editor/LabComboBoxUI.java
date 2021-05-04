package editor;

import editor.util.EditorUtilities;
import editor.util.LabToolbarButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 */
public class LabComboBoxUI extends BasicComboBoxUI
{
  public static ComponentUI createUI( JComponent c )
  {
    return new LabComboBoxUI();
  }

  protected JButton createArrowButton()
  {
    JButton button = new LabToolbarButton( EditorUtilities.loadIcon( "images/tree_expanded.png" ) );
    button.setName( "ComboBox.arrowButton" );
    return button;
  }
}
