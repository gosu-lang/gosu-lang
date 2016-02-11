package editor;


import editor.util.EditorUtilities;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import java.awt.*;


/**
 */
public class TypeCellRenderer extends DefaultListCellRenderer
{

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    Icon icon = EditorUtilities.findIcon( TypeSystem.getByFullNameIfValid( (String)value ) );

    String text = getDisplayText( value );
    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );
    ((JLabel)renderer).setIcon( icon );

    return renderer;
  }

  protected String getDisplayText( Object value )
  {
    String strType = (String)value;
    String strRelativeType = TypePopup.getRelativeTypeName( strType );
    return (strRelativeType + "  (" + strType + ")").replace( '\u2024', '.' );
  }
}
