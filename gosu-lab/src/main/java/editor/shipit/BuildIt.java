package editor.shipit;

import editor.FileTree;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.NodeKind;
import editor.MessageTree;
import editor.MessagesPanel;
import editor.RunMe;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
import gw.lang.reflect.TypeSystem;

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
    MessagesPanel messages = gosuPanel.showMessages( true );
    messages.clear();

    FileTree root = FileTreeUtil.getRoot();
    boolean[] bRes = {false};
    boolean[] bFinished = {false};
    Compiler compiler = new Compiler();
    ProgressFeedback.runWithProgress( "Compiling...",
      progress -> {
        progress.setLength( root.getTotalFiles() );
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
      messages.insertAtTop( doneMessage = new MessageTree( message, errors > 0 ? NodeKind.Error : warnings > 0 ? NodeKind.Warning : NodeKind.Info, MessageTree.empty() ) );
    }
    else
    {
      messages.insertAtTop( doneMessage = new MessageTree( "Compilation failed to complete", NodeKind.Failure, MessageTree.empty() ) );
    }
    EventQueue.invokeLater( doneMessage::select );
    //messages.expandAll();
    return bRes[0];
  }
}
