package editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseInEditorHandler extends MouseAdapter
{
  private final EditorHost _editor;

  public MouseInEditorHandler( EditorHost editor )
  {
    assert editor != null;
    _editor = editor;
  }

  @Override
  public void mouseClicked( MouseEvent e )
  {
    if( e.getButton() == MouseEvent.BUTTON1 && e.isControlDown() )
    {
      // Control-click
      _editor.gotoDeclaration();
    }
  }
}