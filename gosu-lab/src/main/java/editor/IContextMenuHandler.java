package editor;

import javax.swing.*;
import java.awt.*;

/**
 */
public interface IContextMenuHandler<E>
{
  public JPopupMenu getContextMenu( E editor );

  public void displayContextMenu( E editor, int iX, int iY, Component eventSource );
}
