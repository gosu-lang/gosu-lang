package editor;

import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import editor.util.EditorUtilities;
import editor.util.LabToolbarButton;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 */
public class GosuLabComboBoxUI extends WindowsComboBoxUI
{
  public static ComponentUI createUI( JComponent c )
  {
    return new GosuLabComboBoxUI();
  }

  protected JButton createArrowButton()
  {
    JButton button = new LabToolbarButton( EditorUtilities.loadIcon( "images/tree_expanded.png" ) );
    button.setName( "ComboBox.arrowButton" );
    return button;
  }

  protected WindowsComboPopup createPopup()
  {
    WindowsComboPopup popup =
      new WindowsComboPopup( comboBox )
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
