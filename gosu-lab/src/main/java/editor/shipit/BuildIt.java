package editor.shipit;

import editor.FileTree;
import editor.RunMe;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
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
    boolean[] bRes = {false};
    boolean[] bFinished = {false};
    ProgressFeedback.runWithProgress( "Compiling...",
      progress -> {
        progress.setLength( root.getTotalSourceFiles() );
        bRes[0] = new Compiler().compileTree( root, consumer, progress );
        TypeSystem.refresh( false );
        bFinished[0] = true;
      } );
    new ModalEventQueue( () -> !bFinished[0] ).run();
    return bRes[0];
  }
}
