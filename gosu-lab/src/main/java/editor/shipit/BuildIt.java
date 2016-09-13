package editor.shipit;

import editor.FileTree;
import editor.RunMe;
import gw.lang.reflect.TypeSystem;

import javax.swing.tree.TreeModel;

/**
 */
public class BuildIt
{
  private static BuildIt INSTANCE;

  public static BuildIt instance()
  {
    return INSTANCE == null ? INSTANCE = new BuildIt() : INSTANCE;
  }

  public boolean buildIt( ICompileConsumer consumer )
  {
    TreeModel model = RunMe.getEditorFrame().getGosuPanel().getExperimentView().getTree().getModel();
    FileTree root = (FileTree)model.getRoot();
    boolean bRes = new Compiler().compileTree( root, consumer );
    TypeSystem.refresh( false );
    return bRes;
  }
}
