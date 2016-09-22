package editor;

import editor.actions.UpdateNotifier;
import editor.debugger.BreakpointManager;
import editor.debugger.Debugger;
import editor.util.EditorUtilities;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.JavaTypes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

/**
 */
public class CommonMenus
{
  public static JMenuItem makeCut( Supplier<GosuEditor> editor )
  {
    JMenuItem cutItem = new JMenuItem(
      new AbstractAction( "Cut" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipCut( RunMe.getEditorFrame().getGosuPanel().getClipboard() );
        }
      } );
    cutItem.setMnemonic( 't' );
    cutItem.setAccelerator( KeyStroke.getKeyStroke( "control X" ) );

    return cutItem;
  }

  public static JMenuItem makeCopy( Supplier<GosuEditor> editor )
  {
    JMenuItem copyItem = new JMenuItem(
      new AbstractAction( "Copy" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipCopy( RunMe.getEditorFrame().getGosuPanel().getClipboard() );
        }
      } );
    copyItem.setMnemonic( 'C' );
    copyItem.setAccelerator( KeyStroke.getKeyStroke( "control C" ) );

    return copyItem;
  }

  public static JMenuItem makePaste( Supplier<GosuEditor> editor )
  {
    JMenuItem pasteItem = new JMenuItem(
      new AbstractAction( "Paste" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipPaste( RunMe.getEditorFrame().getGosuPanel().getClipboard(), false );
        }
      } );
    pasteItem.setMnemonic( 'P' );
    pasteItem.setAccelerator( KeyStroke.getKeyStroke( "control V" ) );

    return pasteItem;
  }

  public static JMenuItem makePasteJavaAsGosu( Supplier<GosuEditor> editor )
  {
    return new JMenuItem(
      new AbstractAction( "Paste Java as Gosu" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().clipPaste( RunMe.getEditorFrame().getGosuPanel().getClipboard(), true );
        }
      } );
  }

  public static JMenuItem makeCodeComplete( Supplier<GosuEditor> editor )
  {
    JMenuItem completeItem = new JMenuItem(
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
    JMenuItem paraminfoItem = new JMenuItem(
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
    paraminfoItem.setAccelerator( KeyStroke.getKeyStroke( "control P" ) );

    return paraminfoItem;
  }

  public static JMenuItem makeExpressionType( Supplier<GosuEditor> editor )
  {
    JMenuItem typeItem = new JMenuItem(
      new AbstractAction( "Expression Type" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().displayTypeInfoAtCurrentLocation();
        }
      } );
    typeItem.setMnemonic( 'T' );
    typeItem.setAccelerator( KeyStroke.getKeyStroke( "control T" ) );

    return typeItem;
  }

  public static JMenuItem makeGotoDeclaration( Supplier<GosuEditor> editor )
  {
    JMenuItem navigate = new JMenuItem(
      new AbstractAction( "Goto Declaration" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().gotoDeclaration();
        }
      } );
    navigate.setMnemonic( 'D' );
    navigate.setAccelerator( KeyStroke.getKeyStroke( "control B" ) );
    return navigate;
  }

  public static JMenuItem makeShowFileInTree( Supplier<GosuEditor> editor )
  {
    JMenuItem navigate = new JMenuItem(
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
    JMenuItem quickDoc = new JMenuItem(
      new AbstractAction( "Quick Documentation" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.get().displayJavadocHelp( editor.get().getDeepestLocationAtCaret() );
        }
      } );
    quickDoc.setMnemonic( 'Q' );
    quickDoc.setAccelerator( KeyStroke.getKeyStroke( "control Q" ) );
    return quickDoc;
  }

  public static JMenuItem makeViewBytecode()
  {
    JMenuItem item = new JMenuItem(
      new AbstractAction( "View Bytecode" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          RunMe.getEditorFrame().getGosuPanel().dumpBytecode();
        }
      } );
    item.setMnemonic( 'y' );
    return item;
  }

  public static JMenuItem makeRun( Supplier<IType> type )
  {
    JMenuItem item = new JMenuItem( new ClearAndRunActionHandler( "Run", type ) );
    item.setMnemonic( 'R' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F5" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeDebug( Supplier<IType> type )
  {
    JMenuItem item = new JMenuItem( new ClearAndDebugActionHandler( "Debug", type ) );
    item.setMnemonic( 'D' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F5" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeStop( Supplier<GosuPanel> gosuPanel )
  {
    JMenuItem item = new JMenuItem( new CommonMenus.StopActionHandler( "Stop", gosuPanel::get ) );
    item.setMnemonic( 'S' );
    item.setAccelerator( KeyStroke.getKeyStroke( "control F2" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeToggleBreakpoint( Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
  {
    JMenuItem item = new JMenuItem(
      new AbstractAction( "Toggle Breakpoint", EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" ) )
      {
        public void actionPerformed( ActionEvent e )
        {
          bpm.get().toggleLineBreakpoint( editor.get().getScriptPart().getContainingTypeName(),
                                          editor.get().getLineNumberAtCaret() );
        }

        @Override
        public boolean isEnabled()
        {
          return editor.get() != null && bpm.get().canAddBreakpoint( editor.get().getLineNumberAtCaret() );
        }
      } );
    item.setMnemonic( 'B' );
    item.setAccelerator( KeyStroke.getKeyStroke( "control F8" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeStepOver( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new StepOverActionHandler( "Step Over", debugger ) );
    item.setMnemonic( 'O' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F8" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeStepInto( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new StepIntoActionHandler( "Step Into", debugger ) );
    item.setMnemonic( 'V' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F7" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeStepOut( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new StepOutActionHandler( "Step Out", debugger ) );
    item.setMnemonic( 'T' );
    item.setAccelerator( KeyStroke.getKeyStroke( "shift F8" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeRunToCursor( Supplier<Debugger> debugger, Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
  {
    JMenuItem item = new JMenuItem( new RunToCursorActionHandler( "Run to Cursor", debugger, bpm, editor ) );
    item.setMnemonic( 'S' );
    item.setAccelerator( KeyStroke.getKeyStroke( "alt F9" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeDropFrame( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new DropFrameActionHandler( "Drop Frame", debugger ) );
    item.setMnemonic( 'F' );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makePause( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new CommonMenus.PauseActionHandler( "Pause", debugger ) );
    item.setMnemonic( 'P' );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }
  
  public static JMenuItem makeResume( Supplier<Debugger> debugger )
  {
    JMenuItem item = new JMenuItem( new CommonMenus.ResumeActionHandler( "Resume", debugger ) );
    item.setMnemonic( 'G' );
    item.setAccelerator( KeyStroke.getKeyStroke( "F9" ) );
    UpdateNotifier.instance().addActionComponent( item );
    return item;
  }

  public static JMenuItem makeViewBreakpoints( Supplier<Breakpoint> bp )
  {
    JMenuItem item = new JMenuItem( new CommonMenus.ViewBreakpointsActionHandler( "View Breakpoints...", bp ) );
    item.setMnemonic( 'B' );
    return item;
  }

  public static JMenuItem makeMuteBreakpoints( Supplier<BreakpointManager> bpm )
  {
    JMenuItem item = new JMenuItem( new CommonMenus.MuteBreakpointsActionHandler( "Mute Breakpoints", bpm ) );
    item.setMnemonic( 'M' );
    return item;
  }

  public static JMenuItem makeClear( Supplier<GosuPanel> gosuPanel )
  {
    JMenuItem clearItem = new JMenuItem(
      new AbstractAction( "Clear Console" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          gosuPanel.get().clearOutput();
        }
      } );
    clearItem.setMnemonic( 'C' );
    clearItem.setAccelerator( KeyStroke.getKeyStroke( "alt C" ) );
    return clearItem;
  }

  public static abstract class AbstractRunActionHandler extends AbstractAction
  {
    protected final Supplier<IType> _program;

    AbstractRunActionHandler( String title, Icon icon, Supplier<IType> program )
    {
      super( title, icon );
      _program = program;
    }

    public boolean isEnabled()
    {
      IType type = _program.get();
      if( type == null || !type.isValid() )
      {
        return false;
      }
      if( RunMe.getEditorFrame().getGosuPanel().isRunning() ||
          RunMe.getEditorFrame().getGosuPanel().isDebugging() )
      {
        return false;
      }

      // Is Program?
      if( type instanceof IGosuProgram )
      {
        return true;
      }

      if( type instanceof IGosuClass && !type.isAbstract() &&
          ((IGosuClassTypeInfo)type.getTypeInfo()).isPublic() )
      {
        // Is Main class?
        IMethodInfo main = type.getTypeInfo().getMethod( "main", JavaTypes.STRING().getArrayType() );
        if( main != null && main.isStatic() && main.getReturnType() == JavaTypes.pVOID() )
        {
          return true;
        }

        // Is Test class?
        if( type.getTypeInfo().getConstructor() != null )
        {
          IType baseTest = TypeSystem.getByFullNameIfValid( "junit.framework.Assert" );
          if( baseTest != null )
          {
            return baseTest.isAssignableFrom( type );
          }
        }
      }
      return false;
    }
  }

  public static class ClearAndRunActionHandler extends AbstractRunActionHandler
  {
    ClearAndRunActionHandler( String title, Supplier<IType> program )
    {
      super( title, EditorUtilities.loadIcon( "images/run.png" ), program );
    }

    public void actionPerformed( ActionEvent e )
    {
      RunMe.getEditorFrame().getGosuPanel().clearOutput();
      IType type = _program.get();
      if( type instanceof ITemplateType )
      {
        RunMe.getEditorFrame().getGosuPanel().executeTemplate();
      }
      else
      {
        RunMe.getEditorFrame().getGosuPanel().execute( type.getName() );
      }
    }
  }

  public static class ClearAndDebugActionHandler extends AbstractRunActionHandler
  {
    ClearAndDebugActionHandler( String title, Supplier<IType> program )
    {
      super( title, EditorUtilities.loadIcon( "images/debug.png" ), program );
    }

    public void actionPerformed( ActionEvent e )
    {
      RunMe.getEditorFrame().getGosuPanel().clearOutput();
      IType type = _program.get();
      if( type instanceof ITemplateType )
      {
        RunMe.getEditorFrame().getGosuPanel().debugTemplate();
      }
      else
      {
        RunMe.getEditorFrame().getGosuPanel().debug( type.getName() );
      }
    }
  }

  public static class StopActionHandler extends AbstractAction
  {
    private Supplier<GosuPanel> _gosuPanel;

    public StopActionHandler( String label, Supplier<GosuPanel> gosuPanel )
    {
      super( label, EditorUtilities.loadIcon( "images/rule_stop_execution.png" ) );
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

    public PauseActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/pause.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        _debugger.get().pause();
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

    public ResumeActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/resume.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      _debugger.get().resumeExecution();
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

    public ViewBreakpointsActionHandler( String label, Supplier<Breakpoint> bp )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_breakpoints.png" ) );
      _bp = bp;
    }

    public void actionPerformed( ActionEvent e )
    {
      new EditBreakpointsDialog( _bp.get() ).setVisible( true );
    }
  }

  public static class MuteBreakpointsActionHandler extends AbstractAction
  {
    private final Supplier<BreakpointManager> _bpm;

    public MuteBreakpointsActionHandler( String label, Supplier<BreakpointManager> bpm )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_mutebreakpoint.png" ) );
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

    public StepOverActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_stepover.png" ) );
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

    public StepIntoActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_stepinto.png" ) );
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

  public static class StepOutActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public StepOutActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_stepout.png" ) );
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

  public static class DropFrameActionHandler extends AbstractAction
  {
    private final Supplier<Debugger> _debugger;

    public DropFrameActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_dropframe.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      _debugger.get().dropFrame();
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

    public ShowExecPointActionHandler( String label, Supplier<Debugger> debugger )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_showexecpoint.png" ) );
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      RunMe.getEditorFrame().getGosuPanel().jumptToBreakpoint( _debugger.get().getSuspendedLocation(), true );
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

    public RunToCursorActionHandler( String label,  Supplier<Debugger> debugger, Supplier<BreakpointManager> bpm, Supplier<GosuEditor> editor )
    {
      super( label, EditorUtilities.loadIcon( "images/debug_runtocursor.png" ) );
      _bpm = bpm;
      _editor = editor;
      _debugger = debugger;
    }

    public void actionPerformed( ActionEvent e )
    {
      _bpm.get().runToCursor( _editor.get().getScriptPart().getContainingTypeName(), _editor.get().getLineNumberAtCaret() );
    }

    @Override
    public boolean isEnabled()
    {
      return _debugger.get() != null && _debugger.get().isSuspended() && _editor.get() != null && _bpm.get().canAddBreakpoint( _editor.get().getLineNumberAtCaret() );
    }
  }

}
