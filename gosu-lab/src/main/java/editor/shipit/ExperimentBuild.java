package editor.shipit;

import editor.FileTree;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.NodeKind;
import editor.MessageTree;
import editor.MessagesPanel;
import editor.RunMe;
import editor.debugger.Debugger;
import editor.search.IncrementalCompilerUsageSearcher;
import editor.util.IProgressCallback;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 */
public class ExperimentBuild
{
  private static ExperimentBuild INSTANCE;

  private final FileChangeFinder _typeChangeTracker;
  private List<CompiledClass> _listCompiledClasses;

  public static ExperimentBuild instance()
  {
    return INSTANCE == null ? INSTANCE = new ExperimentBuild( true ) : INSTANCE;
  }

  public ExperimentBuild( boolean rebuild )
  {
    _typeChangeTracker = new FileChangeFinder( rebuild );
    _listCompiledClasses = new ArrayList<>();
  }

  public boolean make( ICompileConsumer consumer )
  {
    Debugger debugger = getDebugger();
    if( debugger != null )
    {
      consumer = chainDebuggerConsumer( consumer );
    }

    boolean result;
    if( _typeChangeTracker.isRefreshAll() )
    {
      result = rebuild( consumer );
    }
    else
    {
      result = build( consumer, findTypesToCompile(), true );
    }

    return result;
  }

  public Set<IType> findTypesToCompile()
  {
    Set<IType> types = new HashSet<>();
    for( FileTree ft: _typeChangeTracker.findChangedFiles( ref -> ref.getType() != null ) )
    {
      IncrementalCompilerUsageSearcher searcher = new IncrementalCompilerUsageSearcher( ft.getType() );
      searcher.search( FileTreeUtil.getRoot() );
      types.addAll( searcher.getTypes() );
    }
    return types;
  }

  private Debugger getDebugger()
  {
    return RunMe.getEditorFrame().getGosuPanel().getDebugger();
  }

  private ICompileConsumer chainDebuggerConsumer( ICompileConsumer consumer )
  {
    return cc -> {
      _listCompiledClasses.add( cc );
      return consumer.accept( cc );
    };
  }

  public boolean rebuild( ICompileConsumer consumer )
  {
    boolean result = build( consumer, Collections.singleton( FileTreeUtil.getRoot() ), false );
    TypeSystem.refresh( false );
    return result;
  }

  private boolean build( ICompileConsumer consumer, Set sources, boolean incremental )
  {
    try
    {
      GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
      MessagesPanel messages = gosuPanel.showMessages( true );
      messages.clear();

      boolean[] bRes = {false};
      boolean[] bFinished = {false};
      Compiler compiler = new Compiler();
      //noinspection unchecked
      ProgressFeedback.runWithProgress( "Compiling...",
                                        incremental
                                        ? progress -> incrementalCompileSources( sources, consumer, messages, bRes, bFinished, compiler, progress )
                                        : progress -> compileSources( sources, consumer, messages, bRes, bFinished, compiler, progress ) );
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
    finally
    {
      _typeChangeTracker.reset();
      _listCompiledClasses = new ArrayList<>();
    }
  }

  private void compileSources( Collection<FileTree> sources, ICompileConsumer consumer, MessagesPanel messages, boolean[] bRes, boolean[] bFinished, Compiler compiler, IProgressCallback progress )
  {
    progress.setLength( sources.stream().mapToInt( FileTree::getTotalFiles ).sum() );
    for( FileTree fileTree: sources )
    {
      bRes[0] |= compiler.compileTree( fileTree, consumer, progress, messages );
    }
    Debugger debugger = getDebugger();
    if( debugger != null && !_listCompiledClasses.isEmpty() )
    {
      debugger.redefineClasses( _listCompiledClasses );
    }
    bFinished[0] = true;
  }

  private void incrementalCompileSources( Collection<IType> sources, ICompileConsumer consumer, MessagesPanel messages, boolean[] bRes, boolean[] bFinished, Compiler compiler, IProgressCallback progress )
  {
    try
    {
      progress.setLength( sources.size() );
      for( IType type : sources )
      {
        progress.incrementProgress( type != null ? type.getName() : "" );
        bRes[0] |= compiler.compile( (IGosuClass)type, consumer, messages );
      }
      Debugger debugger = getDebugger();
      if( debugger != null && !_listCompiledClasses.isEmpty() )
      {
        debugger.redefineClasses( _listCompiledClasses );
      }
    }
    finally
    {
      bFinished[0] = true;
    }
  }
}
