package editor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 */
public class ExperimentListCellRenderer extends DefaultListCellRenderer
{
  public ExperimentListCellRenderer()
  {
  }

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    File experimentDir = (File)value;
    String text = experimentDir == null ? "" : experimentDir.getName();

    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );

    if( experimentDir != null )
    {
      setIcon( LabFrame.loadLabIcon() );
    }

    return renderer;
  }

}