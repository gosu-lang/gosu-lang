package editor;

import com.sun.jdi.StackFrame;
import editor.debugger.Breakpoint;
import editor.debugger.BreakpointManager;
import editor.debugger.BreakpointsDialog;
import editor.debugger.Debugger;
import editor.debugger.EvaluateDialog;
import editor.run.IRunConfig;
import editor.run.RunConfigDialog;
import editor.run.RunState;
import editor.search.AbstractSearchDialog;
import editor.search.LocalSearchDialog;
import editor.search.LocalVarFeatureInfo;
import editor.search.MessageDisplay;
import editor.search.SearchDialog;
import editor.search.SearchPanel;
import editor.search.UsageSearcher;
import editor.search.UsageTarget;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.SmartMenuItem;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Supplier;

/**
 */
public class CommonMenus
{
  public static JMenuItem makeCut( Supplier<GosuEditor> editor )
  {
    JMenuItem cutItem = new SmartMenuItem( new CutActionHandler( editor ) );
    cutItem.setMnemonic( 't' );
    cutItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " X" ) );

    return cutItem;
  }

  public static JMenuItem makeCopy( Supplier<GosuEditor> editor )
  {
    JMenuItem copyItem = new SmartMenuItem( new CopyActionHandler( editor ) );
    copyItem.setMnemonic( 'C' );
    copyItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " C" ) );

    return copyItem;
  }

  public static JMenuItem makePaste( Supplier<GosuEditor> editor )
  {
    JMenuItem pasteItem = new SmartMenuItem( new PasteActionHandler( editor ) );
    pasteItem.setMnemonic( 'P' );
    pasteItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " V" ) );

    return pasteItem;
  }

  public static JMenuItem makePasteJavaAsGosu( Supplier<GosuEditor> editor )
  {
    return new SmartMenuItem(
      new AbstractAction( "Paste Java as Gosu" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipPaste( getGosuPanel().getClipboard(), true );
        }
      } );
  }

  public static JMenuItem makeFindUsages( Supplier<FileTree> tree )
  {
    JMenuItem completeItem = new SmartMenuItem( new FindUsagesInPathActionHandler( tree ) );
    completeItem.setMnemonic( 'U' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( "alt F7" ) );

    return completeItem;
  }

  public static JMenuItem makeFindUsagesInFile()
  {
    JMenuItem completeItem = new SmartMenuItem( new FindUsagesInFileActionHandler() );
    completeItem.setMnemonic( 'F' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F7" ) );

    return completeItem;
  }

  public static JMenuItem makeHighlightFindUsagesInFile()
  {
    JMenuItem completeItem = new SmartMenuItem( new HighlightUsagesInFileActionHandler() );
    completeItem.setMnemonic( 'H' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift F7" ) );

    return completeItem;
  }

  public static JMenuItem makeNextOccurrent( Supplier<SearchPanel> search )
  {
    JMenuItem completeItem = new SmartMenuItem( new NextOccurrenceActionHandler( search ) );
    completeItem.setMnemonic( 'X' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " alt DOWN" ) );

    return completeItem;
  }

  public static JMenuItem makePrevOccurrent( Supplier<SearchPanel> search )
  {
    JMenuItem completeItem = new SmartMenuItem( new PrevOccurrenceActionHandler( search ) );
    completeItem.setMnemonic( 'V' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " alt UP" ) );

    return completeItem;
  }

  public static JMenuItem makeCodeComplete( Supplier<GosuEditor> editor )
  {
    JMenuItem completeItem = new SmartMenuItem(
      new AbstractAction( "Complete Code" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().handleCompleteCode();
        }
      } );
    completeItem.setMnemonic( 'L' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( "control SPACE" ) );

    return completeItem;
  }

  public static JMenuItem makeParameterInfo( Supplier<GosuEditor> editor )
  {
    JMenuItem paraminfoItem = new SmartMenuItem(
      new AbstractAction( "Parameter Info" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !editor.get().isIntellisensePopupShowing() )
          {
            editor.get().displayParameterInfoPopup( editor.get().getEditor().getCaretPosition() );
          }
        }
      } );
    paraminfoItem.setMnemonic( 'P' );
    paraminfoItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " P" ) );

    return paraminfoItem;
  }

  public static JMenuItem makeExpressionType( Supplier<GosuEditor> editor )
  {
    JMenuItem typeItem = new SmartMenuItem(
      new AbstractAction( "Expression Type" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().displayTypeInfoAtCurrentLocation();
        }
      } );
    typeItem.setMnemonic( 'T' );
    typeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " T" ) );

    return typeItem;
  }

  public static JMenuItem makeGotoDeclaration( Supplier<GosuEditor> editor )
  {
    JMenuItem navigate = new SmartMenuItem(
      new AbstractAction( "Goto Declaration" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().gotoDeclaration();
        }
      } );
    navigate.setMnemonic( 'D' );
    navigate.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " B" ) );
    return navigate;
  }

  public static JMenuItem makeShowFileInTree( Supplier<GosuEditor> editor )
  {
    JMenuItem navigate = new SmartMenuItem(
      new AbstractAction( "Select File in Tree" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().showFileInTree();
        }
      } );
    navigate.setMnemonic( 'F' );
    navigate.setAccelerator( KeyStroke.getKeyStroke( "alt F" ) );
    return navigate;
  }

  public static JMenuItem makeQuickDocumentation( Supplier<GosuEditor> editor )
  {
    JMenuItem quickDoc = new SmartMenuItem(
      new AbstractAction( "Quick Documentation" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().displayJavadocHelp( editor.get().getDeepestLocationAtCaret() );
        }
      } );
    quickDoc.setMnemonic( 'Q' );
    quickDoc.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " Q" ) );
    return quickDoc;
  }

  public static JMenuItem makeViewBytecode()
  {
    JMenuItem item = new SmartMenuItem(
      new AbstractAction( "View Bytecode" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getGosuPanel().dumpBytecode();
        }
      } );
    item.setMnemonic( 'y' );
    return item;
  }

  public static JMenuItem makeRun( Supplier<IRunConfig> runConfig )
  {
    JMenuItem item =
      new SmartMenuItem( new ClearAndRunActionHandler( runConfig ) )
      {
        @Override
        public String getText()
        {
          IRunConfig rc = runConfig.get();
          return rc == null ? "Run" : "Run '" + rc.getName() + "'";
        }
      };
    item.setMnemonic( 'R' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F5" ) );
    return item;
  }

  public static JMenuItem makeDebug( Supplier<IRunConfig> runConfig )
  {
    JMenuItem item = new SmartMenuItem( new ClearAndDebugActionHandler( runConfig ) )
    {
      @Override
      public String getText()
      {
        IRunConfig rc = runConfig.get();
        return rc == null ? "Debug" : "Debug '" + rc.getName() + "'";
      }
    };
    item.setMnemonic( 'D' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F5" ) );
    return item;
  }

  public static JMenuItem makeRunConfig()
  {
    JMenuItem item = new SmartMenuItem( new RunConfigActionHandler() );
    item.setMnemonic( 'C' );
    item.setAccelerator( KeyStroke.getKeyStroke( "shift F5" ) );
    return item;
  }

  public static JMenuItem makeDebugConfig()
  {
    JMenuItem item = new SmartMenuItem( new DebugConfigActionHandler() );
    item.setMnemonic( 'G' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt shift F5" ) );
    return item;
  }

  public static JMenuItem makeStop( Supplier<GosuPanel> gosuPanel )
  {
    JMenuItem item = new SmartMenuItem( new CommonMenus.StopActionHandler( gosuPanel::get ) );
    item.setMnemonic( 'S' );
    item.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F2" ) );
    return item;
  }

  public static JMenuItem makeToggleBreakpoint( Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
  {
    JMenuItem item = new SmartMenuItem(
      new AbstractAction( "Toggle Breakpoint", EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" ) )
      {
        public void actionPerformed( ActionEvent e )
        {
          bpm.get().toggleLineBreakpoint( editor.get(), editor.get().getScriptPart().getContainingTypeName(),
                                          editor.get().getLineNumberAtCaret() );
        }

        @Override
        public boolean isEnabled()
        {
          return editor.get() != null && bpm.get().canAddBreakpoint( editor.get(), editor.get().getLineNumberAtCaret() );
        }
      } );
    item.setMnemonic( 'B' );
    item.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F8" ) );
    return item;
  }

  public static JMenuItem makeStepOver( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new StepOverActionHandler( debugger ) );
    item.setMnemonic( 'O' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F8" ) );
    return item;
  }

  public static JMenuItem makeStepInto( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new StepIntoActionHandler( debugger ) );
    item.setMnemonic( 'V' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F7" ) );
    return item;
  }

  public static JMenuItem makeStepOut( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new StepOutActionHandler( debugger ) );
    item.setMnemonic( 'T' );
    item.setAccelerator( KeyStroke.getKeyStroke( "shift F8" ) );
    return item;
  }

  public static JMenuItem makeRunToCursor( Supplier<Debugger> debugger, Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
  {
    JMenuItem item = new SmartMenuItem( new RunToCursorActionHandler( debugger, bpm, editor ) );
    item.setMnemonic( 'S' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F9" ) );
    return item;
  }

  public static JMenuItem makeDropFrame( Supplier<Debugger> debugger, Supplier<StackFrame> frame )
  {
    JMenuItem item = new SmartMenuItem( new DropFrameActionHandler( debugger, frame ) );
    item.setMnemonic( 'F' );
    return item;
  }

  public static JMenuItem makePause( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new CommonMenus.PauseActionHandler( debugger ) );
    item.setMnemonic( 'P' );
    return item;
  }
  
  public static JMenuItem makeResume( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new CommonMenus.ResumeActionHandler( debugger ) );
    item.setMnemonic( 'G' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F9" ) );
    return item;
  }

  public static JMenuItem makeEvaluateExpression( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new EvaluateExpressionActionHandler( debugger ) );
    item.setMnemonic( 'E' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F8" ) );
    return item;
  }

  public static JMenuItem makeShowExecutionPoint( Supplier<Debugger> debugger )
  {
    JMenuItem item = new SmartMenuItem( new ShowExecPointActionHandler( debugger ) );
    item.setMnemonic( 'P' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F10" ) );
    return item;
  }

  public static JMenuItem makeViewBreakpoints( Supplier<Breakpoint> bp )
  {
    JMenuItem item = new SmartMenuItem( new CommonMenus.ViewBreakpointsActionHandler( bp ) );
    item.setMnemonic( 'B' );
    return item;
  }

  public static JMenuItem makeMuteBreakpoints( Supplier<BreakpointManager> bpm )
  {
    JMenuItem item = new SmartMenuItem( new CommonMenus.MuteBreakpointsActionHandler( bpm ) );
    item.setMnemonic( 'M' );
    return item;
  }

  public static JMenuItem makeClear( Supplier<GosuPanel> gosuPanel )
  {
    JMenuItem clearItem = new SmartMenuItem(
      new AbstractAction( "Clear Console" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          gosuPanel.get().showConsole( true );
          gosuPanel.get().clearOutput();
        }

        @Override
        public boolean isEnabled()
        {
          return gosuPanel.get().getConsolePanel() != null;
        }
      } );
    clearItem.setMnemonic( 'C' );
    clearItem.setAccelerator( KeyStroke.getKeyStroke( "alt C" ) );
    return clearItem;
  }

  public static abstract class AbstractRunActionHandler extends AbstractAction
  {
    protected final Supplier<IRunConfig> _runConfig;

    AbstractRunActionHandler( String title, Icon icon, Supplier<IRunConfig> runConfig )
    {
      super( title, icon );
      _runConfig = runConfig;
    }

    protected IRunConfig getRunConfig()
    {
      return _runConfig.get();
    }

    public boolean isEnabled()
    {
      GosuPanel gosuPanel = getGosuPanel();
      return !(gosuPanel == null || gosuPanel.isRunning() || gosuPanel.isDebugging());
    }
  }

  private static GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  public static class ClearAndRunActionHandler extends AbstractRunActionHandler
  {
    public ClearAndRunActionHandler( Supplier<IRunConfig> runConfig )
    {
      super( "Run", EditorUtilities.loadIcon( "images/run.png" ), runConfig );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      IRunConfig runConfig = getRunConfig();
      if( runConfig != null && runConfig.isValid() && runConfig.isRunnable() )
      {
        getGosuPanel().showConsole( true );
        getGosuPanel().clearOutput();
        getGosuPanel().execute( runConfig );
      }
      else
      {
        new RunConfigActionHandler().actionPerformed( e );
      }
    }
  }

  public static class ClearAndDebugActionHandler extends AbstractRunActionHandler
  {
    public ClearAndDebugActionHandler( Supplier<IRunConfig> program )
    {
      super( "Debug", EditorUtilities.loadIcon( "images/debug.png" ), program );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      IRunConfig runConfig = getRunConfig();
      if( runConfig != null && runConfig.isValid() && runConfig.isDebuggable() )
      {
        getGosuPanel().showConsole( true );
        getGosuPanel().clearOutput();
        getGosuPanel().debug( runConfig );
      }
      else
      {
        new DebugConfigActionHandler().actionPerformed( e );
      }
    }
  }

  public static class RunConfigActionHandler extends AbstractAction
  {
    RunConfigActionHandler()
    {
      super( "Run...", EditorUtilities.loadIcon( "images/runconfig.png" ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      RunConfigDialog dlg = new RunConfigDialog( getExperiment(), RunState.Run );
      dlg.setVisible( true );

      IRunConfig configToRun = dlg.getConfigToRun();
      if( configToRun != null )
      {
        new ClearAndRunActionHandler( () -> configToRun ).actionPerformed( null );
      }
    }
  }

  private static Experiment getExperiment()
  {
    return getGosuPanel().getExperiment();
  }

  public static class DebugConfigActionHandler extends AbstractAction
  {
    DebugConfigActionHandler()
    {
      super( "Debug...", EditorUtilities.loadIcon( "images/debugconfig.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      RunConfigDialog dlg = new RunConfigDialog( getExperiment(), RunState.Debug );
      dlg.setVisible( true );

      IRunConfig configToRun = dlg.getConfigToRun();
      if( configToRun != null )
      {
        new ClearAndDebugActionHandler( () -> configToRun ).actionPerformed( null );
      }
    }
  }

  public static class StopActionHandler extends AbstractAction
  {
    private Supplier<GosuPanel> _gosuPanel;

    public StopActionHandler( Supplier<GosuPanel> gosuPanel )
    {
      super( "Stop", EditorUtilities.loadIcon( "images/rule_stop_execution.png" ) );
      _gosuPanel = gosuPanel;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        killProcess();
      }

//## this is for in-process exec, which we don't do anymore (right now)
//      if( isEnabled() )
//      {
//        TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
//        TaskQueue.emptyAndRemoveQueue( "_execute_gosu" );
//        //noinspection deprecation
//        queue.stop();
//        removeBusySignal();
//      }
    }

    @Override
    public boolean isEnabled()
    {
      GosuPanel gosuPanel = _gosuPanel.get();
      if( gosuPanel == null )
      {
        return false;
      }
      return gosuPanel.isRunning() || gosuPanel.isDebugging();
    }

    private void killProcess()
    {
      GosuPanel gosuPanel = _gosuPanel.get();
      if( gosuPanel == null )
      {
        return;
      }
      gosuPanel.killProcess();
    }
  }

  public static class PauseActionHandler extends AbstractAction
  {
    private Supplier<Debugger> _debugger;

    public PauseActionHandler( Supplier<Debugger> debugger )
    {
      super( "Pause", EditorUtilities.loadIcon( "images/pause.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().pause();
        getGosuPanel().showDebugger( true );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && !_debugger.get().isSuspended() && !_debugger.get().isPaused();
    }
  }

  public static class ResumeActionHandler extends AbstractAction
  {
    private Supplier<Debugger> _debugger;

    public ResumeActionHandler( Supplier<Debugger> debugger )
    {
      super( "Resume", EditorUtilities.loadIcon( "images/resume.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().resumeExecution();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && (_debugger.get().isSuspended() || _debugger.get().isPaused());
    }
  }

  public static class ViewBreakpointsActionHandler extends AbstractAction
  {
    private final Supplier<Breakpoint> _bp;

    public ViewBreakpointsActionHandler( Supplier<Breakpoint> bp )
    {
      super( "View Breakpoints...", EditorUtilities.loadIcon( "images/debug_breakpoints.png" ) );
      _bp = bp;
    }

    public void actionPerformed( ActionEvent e )
    {
      BreakpointsDialog.getOrCreate( _bp.get() ).setVisible( true );
    }
  }

  public static class OpenProjectActionHandler extends AbstractAction
  {
    public OpenProjectActionHandler()
    {
      super( "Open Project...", EditorUtilities.loadIcon( "images/folder.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      RunMe.getEditorFrame().getGosuPanel().openExperiment();
    }
  }

  public static class SaveActionHandler extends AbstractAction
  {
    public SaveActionHandler()
    {
      super( "Save All", EditorUtilities.loadIcon( "images/save.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      RunMe.getEditorFrame().getGosuPanel().save();
    }
  }

  public static class UndoActionHandler extends AbstractAction
  {
    public UndoActionHandler()
    {
      super( "Undo", EditorUtilities.loadIcon( "images/Undo.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( getGosuPanel().getUndoManager().canUndo() )
      {
        getGosuPanel().getUndoManager().undo();
      }
    }

    @Override
    public boolean isEnabled()
    {
      AtomicUndoManager undoManager = getGosuPanel() != null ? getGosuPanel().getUndoManager() : null;
      return undoManager != null && undoManager.canUndo();
    }
  }

  public static class RedoActionHandler extends AbstractAction
  {
    public RedoActionHandler()
    {
      super( "Redo", EditorUtilities.loadIcon( "images/Redo.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( getGosuPanel().getUndoManager().canRedo() )
      {
        getGosuPanel().getUndoManager().redo();
      }
    }

    @Override
    public boolean isEnabled()
    {
      AtomicUndoManager undoManager = getGosuPanel() != null ? getGosuPanel().getUndoManager() : null;
      return undoManager != null && undoManager.canRedo();
    }
  }
  
  public static class CutActionHandler extends AbstractAction
  {
    private final Supplier<GosuEditor> _editor;

    public CutActionHandler( Supplier<GosuEditor> editor )
    {
      super( "Cut", EditorUtilities.loadIcon( "images/Cut.png" ) );
      _editor = editor;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      _editor.get().clipCut( getGosuPanel().getClipboard() );
    }
    
    @Override
    public boolean isEnabled()
    {
      return _editor.get() != null;
    }
  }
  
  public static class CopyActionHandler extends AbstractAction
  {
    private final Supplier<GosuEditor> _editor;

    public CopyActionHandler( Supplier<GosuEditor> editor )
    {
      super( "Copy", EditorUtilities.loadIcon( "images/Copy.png" ) );
      _editor = editor;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      _editor.get().clipCopy( getGosuPanel().getClipboard() );
    }
    
    @Override
    public boolean isEnabled()
    {
      return _editor.get() != null;
    }
  }
  
  public static class PasteActionHandler extends AbstractAction
  {
    private final Supplier<GosuEditor> _editor;

    public PasteActionHandler( Supplier<GosuEditor> editor )
    {
      super( "Paste", EditorUtilities.loadIcon( "images/Paste.png" ) );
      _editor = editor;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      _editor.get().clipPaste( getGosuPanel().getClipboard(), false );
    }
    
    @Override
    public boolean isEnabled()
    {
      return _editor.get() != null;
    }
  }
  
  public static class FindActionHandler extends AbstractAction
  {
    private final Supplier<GosuEditor> _editor;

    public FindActionHandler( Supplier<GosuEditor> editor )
    {
      super( "Find...", EditorUtilities.loadIcon( "images/Find.png" ) );
      _editor = editor;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      new LocalSearchDialog( getOrMakeLocalFileTree(), false ).setVisible( true );
    }

    @Override
    public boolean isEnabled()
    {
      return _editor.get() != null;
    }
  }
  
  public static class ReplaceActionHandler extends AbstractAction
  {
    private final Supplier<GosuEditor> _editor;

    public ReplaceActionHandler( Supplier<GosuEditor> editor )
    {
      super( "Replace...", EditorUtilities.loadIcon( "images/replace.png" ) );
      _editor = editor;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      new LocalSearchDialog( getOrMakeLocalFileTree(), true ).setVisible( true );
    }

    @Override
    public boolean isEnabled()
    {
      return _editor.get() != null;
    }
  }
  
  public static class FindInPathActionHandler extends AbstractAction
  {
    private final Supplier<FileTree> _dir;

    public FindInPathActionHandler( Supplier<FileTree> dir )
    {
      super( "Find in path..." );
      _dir = dir;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      getGosuPanel().saveIfDirty();
      AbstractSearchDialog searchDialog = new SearchDialog( _dir.get(), false );
      searchDialog.setVisible( true );
    }

    @Override
    public boolean isEnabled()
    {
      return _dir.get() != null && _dir.get().getFileOrDir() != null;
    }
  }

  public static class ReplaceInPathActionHandler extends AbstractAction
  {
    private final Supplier<FileTree> _dir;

    public ReplaceInPathActionHandler( Supplier<FileTree> dir )
    {
      super( "Replace in path..." );
      _dir = dir;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      getGosuPanel().saveIfDirty();
      AbstractSearchDialog searchDialog = new SearchDialog( _dir.get(), true );
      searchDialog.setVisible( true );
    }

    @Override
    public boolean isEnabled()
    {
      return _dir.get() != null && _dir.get().getFileOrDir() != null;
    }
  }

  public static class FindUsagesInPathActionHandler extends AbstractAction
  {
    private final Supplier<FileTree> _dir;

    public FindUsagesInPathActionHandler( Supplier<FileTree> dir )
    {
      super( "Find Usages" );
      _dir = dir;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      getGosuPanel().save();

      // Renew parse tree before we get the selected target element
      getGosuPanel().getCurrentEditor().parseAndWaitForParser();

      UsageTarget target = UsageTarget.makeTargetFromCaret();
      if( target == null )
      {
        MessageDisplay.displayInformation( "Please select a valid usage target in the editor" );
      }
      else
      {
        FileTree tree = _dir.get();
        IFeatureInfo fi = target.getRootFeatureInfo();
        if( fi instanceof LocalVarFeatureInfo || (fi instanceof IAttributedFeatureInfo) && ((IAttributedFeatureInfo)fi).isPrivate() )
        {
          tree = getOrMakeLocalFileTree();

        }
        new UsageSearcher( target, true, false ).search( tree );
      }
    }

    @Override
    public boolean isEnabled()
    {
      File file = getGosuPanel() == null ? null : getGosuPanel().getCurrentFile();
      return file != null;
    }
  }

  public static class NextOccurrenceActionHandler extends AbstractAction
  {
    private final Supplier<SearchPanel> _search;

    public NextOccurrenceActionHandler( Supplier<SearchPanel> search )
    {
      super( "Next Occurrence..." );
      _search = search;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _search.get().gotoNextItem();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _search.get() != null;
    }
  }

  public static class PrevOccurrenceActionHandler extends AbstractAction
  {
    private final Supplier<SearchPanel> _search;

    public PrevOccurrenceActionHandler( Supplier<SearchPanel> search )
    {
      super( "Previous Occurrence..." );
      _search = search;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _search.get().gotoPreviousItem();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _search.get() != null;
    }
  }
  
  private static FileTree getOrMakeLocalFileTree()
  {
    FileTree tree;
    File file = getGosuPanel().getCurrentFile();
    tree = FileTreeUtil.getRoot().find( file );
    if( tree == null )
    {
      // the file is not directly in the the experiment, make a temporary tree for it
      tree = new ExternalFileTree( file, getGosuPanel().getCurrentEditor().getParsedClass().getName() );
    }
    return tree;
  }

  public static class FindUsagesInFileActionHandler extends AbstractAction
  {
    public FindUsagesInFileActionHandler()
    {
      super( "Find Usages in File" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      getGosuPanel().save();

      // Renew parse tree before we get the selected target element
      getGosuPanel().getCurrentEditor().parseAndWaitForParser();

      UsageTarget target = UsageTarget.makeTargetFromCaret();
      if( target == null )
      {
        MessageDisplay.displayInformation( "Please select a valid usage target in the editor" );
      }
      else
      {
        new UsageSearcher( target, true, false ).search( getOrMakeLocalFileTree() );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return findCurrentFile() != null;
    }

    private File findCurrentFile()
    {
      return getGosuPanel() == null ? null : getGosuPanel().getCurrentFile();
    }
  }

  public static class HighlightUsagesInFileActionHandler extends AbstractAction
  {
    public HighlightUsagesInFileActionHandler()
    {
      super( "Highlight Usages in File" );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      getGosuPanel().getCurrentEditor().highlightUsagesOfFeatureUnderCaret();
    }

    @Override
    public boolean isEnabled()
    {
      return findCurrentEditor() != null;
    }

    private File findCurrentEditor()
    {
      return getGosuPanel() == null ? null : getGosuPanel().getCurrentFile();
    }
  }

  public static class CompileActionHandler extends AbstractAction
  {
    public CompileActionHandler()
    {
      super( "Compile", EditorUtilities.loadIcon( "images/compile.png" ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      getGosuPanel().compile();
    }
  }
  
  public static class ShipItActionHandler extends AbstractAction
  {
    public ShipItActionHandler()
    {
      super( "ShipIt...", EditorUtilities.loadIcon( "images/shipit.png" ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      getGosuPanel().shipIt();
    }
  }

  public static class MuteBreakpointsActionHandler extends AbstractAction
  {
    private final Supplier<BreakpointManager> _bpm;

    public MuteBreakpointsActionHandler( Supplier<BreakpointManager> bpm )
    {
      super( "Mute Breakpoints", EditorUtilities.loadIcon( "images/disabled_breakpoint.png" ) );
      _bpm = bpm;
    }

    public void actionPerformed( ActionEvent e )
    {
      _bpm.get().setMuted( !_bpm.get().isMuted() );
    }
  }

  public static class StepOverActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public StepOverActionHandler( Supplier<Debugger> debugger )
    {
      super( "Step Over", EditorUtilities.loadIcon( "images/debug_stepover.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().stepOver();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class StepIntoActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public StepIntoActionHandler( Supplier<Debugger> debugger )
    {
      super( "Step Into", EditorUtilities.loadIcon( "images/debug_stepinto.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().stepInto();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class StepOutActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public StepOutActionHandler( Supplier<Debugger> debugger )
    {
      super( "Step Out", EditorUtilities.loadIcon( "images/debug_stepout.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().stepOut();
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class DropFrameActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;
    private final Supplier<StackFrame> _frame;

    public DropFrameActionHandler( Supplier<Debugger> debugger, Supplier<StackFrame> frame )
    {
      super( "Drop Frame", EditorUtilities.loadIcon( "images/debug_dropframe.png" ) );
      _debugger = debugger;
      _frame = frame;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().dropToFrame( _frame.get() );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class EvaluateExpressionActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public EvaluateExpressionActionHandler( Supplier<Debugger> debugger )
    {
      super( "Evaluate Expression", EditorUtilities.loadIcon( "images/tester.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        new EvaluateDialog().setVisible( true );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class ShowExecPointActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public ShowExecPointActionHandler( Supplier<Debugger> debugger )
    {
      super( "Show Execution Point", EditorUtilities.loadIcon( "images/debug_showexecpoint.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        getGosuPanel().jumptToBreakpoint( _debugger.get().getSuspendedLocation(), true );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended();
    }
  }

  public static class RunToCursorActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;
    private final Supplier<BreakpointManager> _bpm;
    private final Supplier<GosuEditor> _editor;

    public RunToCursorActionHandler( Supplier<Debugger> debugger, Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
    {
      super( "Run to Cursor", EditorUtilities.loadIcon( "images/debug_runtocursor.png" ) );
      _bpm = bpm;
      _editor = editor;
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _bpm.get().runToCursor( _editor.get() );
      }
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended() && _editor.get() != null && _bpm.get().canAddBreakpoint( _editor.get(), _editor.get().getLineNumberAtCaret() );
    }
  }

}
