package editor;

import javax.swing.*;
import java.awt.*;

/**
 */
public class EditorContextMenuHandler implements IContextMenuHandler<IScriptEditor>
{
  private static final EditorContextMenuHandler INSTANCE = new EditorContextMenuHandler();

  private JPopupMenu _contextMenu;

  public static EditorContextMenuHandler instance()
  {
    return INSTANCE;
  }

  private EditorContextMenuHandler()
  {
  }

  @Override
  public void displayContextMenu( IScriptEditor editor, int iX, int iY, Component eventSource )
  {
    _contextMenu = getContextMenu( editor );
    _contextMenu.show( editor.getEditor(), iX, iY );
  }

  //TODO cgross - readd actions?
  @Override
  public JPopupMenu getContextMenu( IScriptEditor editor )
  {
    JPopupMenu contextMenu = new JPopupMenu();

    return contextMenu;
  }

  public boolean isContextMenuShowing()
  {
    return _contextMenu != null && _contextMenu.isShowing();
  }

}
