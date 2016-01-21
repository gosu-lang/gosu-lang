package editor;


import editor.util.EditorUtilities;
import gw.lang.Gosu;

import java.awt.*;

public class RunMe
{
  private static IGosuEditor _gosuEditor;

  public static void main( String[] args ) throws Exception
  {
    launchEditor();
  }

  public static IGosuEditor getEditorFrame()
  {
    return _gosuEditor;
  }

  private static void launchEditor() throws Exception
  {
    EventQueue.invokeLater(
      () -> {
        Gosu.init();
        _gosuEditor = BasicGosuEditor.create();
        _gosuEditor.restoreState( EditorUtilities.getRecentProject() );
        _gosuEditor.showMe();
      } );
  }

}
