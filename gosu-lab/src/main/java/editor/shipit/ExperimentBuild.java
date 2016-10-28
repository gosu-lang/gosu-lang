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

  private final FileChangeFinder _fileChangeFinder;
  private List<CompiledClass> _compiledClassesNoErrors;
  private Set<IType> _errantTypes = new HashSet<>();

  public static ExperimentBuild instance()
  {
    return INSTANCE == null ? INSTANCE = new ExperimentBuild( true ) : INSTANCE;
  }

  public ExperimentBuild( boolean rebuild )
  {
    _fileChangeFinder = new FileChangeFinder( rebuild );
    _compiledClassesNoErrors = new ArrayList<>();
  }

  public boolean make( ICompileConsumer consumer )
  {
    boolean result;
    consumer = getDebugger() == null ? chainForNotDebugging( consumer ) : chainForDebugging( consumer );
    if( _fileChangeFinder.isRefreshAll() )
    {
      result = rebuild( consumer );
    }
    else
    {
      result = build( consumer, findTypesToCompile(), true );
    }
    return result;
  }

  public boolean rebuild( ICompileConsumer consumer )
  {
    boolean result = build( chainForNotDebugging( consumer ), Collections.singleton( FileTreeUtil.getRoot() ), false );
    TypeSystem.refresh( false );
    return result;
  }

  private Set<IType> findTypesToCompile()
  {
    Set<IType> types = new HashSet<>();
    for( FileTree ft: _fileChangeFinder.findChangedFiles( ref -> ref.getType() != null ) )
    {
      IncrementalCompilerUsageSearcher searcher = new IncrementalCompilerUsageSearcher( ft.getType() );
      searcher.headlessSearch( FileTreeUtil.getRoot() );
      types.addAll( searcher.getTypes() );
    }
    types.addAll( _errantTypes );
    return types;
  }

  private Debugger getDebugger()
  {
    return RunMe.getEditorFrame().getGosuPanel().getDebugger();
  }

  private ICompileConsumer chainForDebugging( ICompileConsumer consumer )
  {
    return cc -> {
      if( cc.getGosuClass().isValid() )
      {
        _compiledClassesNoErrors.add( cc );
      }
      else
      {
        _errantTypes.add( cc.getGosuClass() );
      }
      return consumer.accept( cc );
    };
  }

  private ICompileConsumer chainForNotDebugging( ICompileConsumer consumer )
  {
    return cc -> {
      if( !cc.getGosuClass().isValid() )
      {
        _errantTypes.add( cc.getGosuClass() );
      }
      return consumer.accept( cc );
    };
  }

  private boolean build( ICompileConsumer consumer, Set sources, boolean incremental )
  {
    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    _errantTypes = new HashSet<>();
    try
    {
      MessagesPanel messages = gosuPanel.showMessages( true );
      messages.clear();

      boolean[] bRes = {false};
      boolean[] bFinished = {false};
      Compiler compiler = new Compiler();
      //noinspection unchecked
      ProgressFeedback.runWithProgress( "Compiling...",
                                        incremental
                                        ? progress -> incrementalCompileSources( sources, consumer, messages, bRes, bFinished, compiler, progress )
                                        : progress -> fullCompileSources( sources, consumer, messages, bRes, bFinished, compiler, progress ) );
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
      _fileChangeFinder.reset();
      _compiledClassesNoErrors = new ArrayList<>();
    }
  }

  private void fullCompileSources( Collection<FileTree> sources, ICompileConsumer consumer, MessagesPanel messages, boolean[] bRes, boolean[] bFinished, Compiler compiler, IProgressCallback progress )
  {
    progress.setLength( sources.stream().mapToInt( FileTree::getTotalFiles ).sum() );
    for( FileTree fileTree: sources )
    {
      bRes[0] |= compiler.compileTree( fileTree, consumer, progress, messages );
    }
    redefineClassInDebugger();
    bFinished[0] = true;
  }

  private void incrementalCompileSources( Collection<IType> sources, ICompileConsumer consumer, MessagesPanel messages, boolean[] bRes, boolean[] bFinished, Compiler compiler, IProgressCallback progress )
  {
    try
    {
      progress.setLength( sources.size() );
      bRes[0] = true;
      for( IType type : sources )
      {
        progress.incrementProgress( type != null ? type.getName() : "" );
        bRes[0] |= compiler.compile( (IGosuClass)type, consumer, messages );
      }
      redefineClassInDebugger();
    }
    finally
    {
      bFinished[0] = true;
    }
  }

  private void redefineClassInDebugger()
  {
    Debugger debugger = getDebugger();
    if( debugger != null && !_compiledClassesNoErrors.isEmpty() )
    {
      debugger.redefineClasses( _compiledClassesNoErrors );
    }
  }
}
