package editor.shipit;

import editor.FileTree;
import editor.GosuPanel;
import editor.IMessageTreeNode;
import editor.MessageKind;
import editor.MessageTree;
import editor.MessagesPanel;
import editor.RunMe;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
import gw.lang.reflect.TypeSystem;

import javax.swing.tree.TreeModel;
import java.awt.*;

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
    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    MessagesPanel messages = gosuPanel.getMessagesPanel();
    messages.clear();
    gosuPanel.showMessages( true );

    TreeModel model = RunMe.getEditorFrame().getGosuPanel().getExperimentView().getTree().getModel();
    FileTree root = (FileTree)model.getRoot();
    boolean[] bRes = {false};
    boolean[] bFinished = {false};
    Compiler compiler = new Compiler();
    ProgressFeedback.runWithProgress( "Compiling...",
      progress -> {
        progress.setLength( root.getTotalSourceFiles() );
        bRes[0] = compiler.compileTree( root, consumer, progress, messages );
        TypeSystem.refresh( false );
        bFinished[0] = true;
      } );
    new ModalEventQueue( () -> !bFinished[0] ).run();
    MessageTree doneMessage;
    if( bRes[0] )
    {
      int errors = compiler.getErrors();
      int warnings = compiler.getWarnings();
      String message = "Compilation completed with " +
                       errors + (errors == 1 ? " error " : " errors ") + " and " +
                       warnings + (warnings == 1 ? " warning " : " warnings ");
      messages.insertAtTop( doneMessage = new MessageTree( message, errors > 0 ? MessageKind.Error : warnings > 0 ? MessageKind.Warning : MessageKind.Info, IMessageTreeNode.empty() ) );
    }
    else
    {
      messages.insertAtTop( doneMessage = new MessageTree( "Compilation failed to complete", MessageKind.Failure, IMessageTreeNode.empty() ) );
    }
    EventQueue.invokeLater( doneMessage::select );
    //messages.expandAll();
    return bRes[0];
  }
}
