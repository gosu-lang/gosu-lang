package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 */
public class ProjectListCellRenderer extends DefaultListCellRenderer
{
  public ProjectListCellRenderer()
  {
  }

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    File projectDir = (File)value;
    String text = projectDir == null ? "" : projectDir.getName();

    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );

    if( projectDir != null )
    {
      setIcon( EditorUtilities.loadIcon( "images/g_16.png"  ) );
    }

    return renderer;
  }

}