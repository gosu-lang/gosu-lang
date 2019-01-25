package editor;

import editor.util.EditorUtilities;
import editor.util.LabToolbarButton;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

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

  protected BasicComboPopup createPopup()
  {
    BasicComboPopup popup =
      new BasicComboPopup( comboBox )
      {
        @Override
        protected Rectangle computePopupBounds( int px, int py, int pw, int ph )
        {
          return super.computePopupBounds(
            px, py, Math.max( comboBox.getPreferredSize().width, pw ), ph
          );
        }
      };
    popup.getAccessibleContext().setAccessibleParent( comboBox );
    return popup;
  }
}
