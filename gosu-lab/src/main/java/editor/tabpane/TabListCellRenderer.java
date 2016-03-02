package editor.tabpane;

import javax.swing.*;
import java.awt.*;
import java.beans.BeanInfo;

/**
 */
public class TabListCellRenderer extends DefaultListCellRenderer
{
  public TabListCellRenderer()
  {
  }

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    ITab tab = (ITab)value;
    String text = tab == null ? "" : tab.getLabel().getDisplayName();

    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );

    if( tab != null )
    {
      setIcon( tab.getLabel().getIcon( BeanInfo.ICON_COLOR_16x16 ) );
    }

    return renderer;
  }

}