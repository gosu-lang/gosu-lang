package editor;

import gw.lang.reflect.IEnumType;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 *
 */
public class EnumerationListPopup extends ListPopup
{

  /** */
  public EnumerationListPopup( IEnumType enumAccess, JTextComponent editor )
  {
    super( editor, new EnumerationPopupListModel( enumAccess ) );
  }

  protected JPanel getSortedByPanel()
  {
    JPanel sortPanel = new JPanel();
    sortPanel.setBorder( BorderFactory.createEmptyBorder( 0, 3, 3, 3 ) );
    return sortPanel;
  }


  protected ListCellRenderer makeCellRenderer()
  {
    return new EnumerationCellRenderer( getJList() );
  }
}