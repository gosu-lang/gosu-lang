package editor.util;


import javax.swing.*;
import java.awt.*;
import java.beans.BeanInfo;

/**
 */
public class LabelListCellRenderer extends DefaultListCellRenderer
{
  public LabelListCellRenderer()
  {
  }

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    ILabel label = (ILabel)value;
    String text = label == null ? "" : label.getDisplayName();

    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );

    if( label != null )
    {
      setIcon( label.getIcon( BeanInfo.ICON_COLOR_16x16 ) );
    }

    return renderer;
  }

}