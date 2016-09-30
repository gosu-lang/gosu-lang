package editor.debugger;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.RunMe;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class BreakpointManager
{
  private Map<String, Map<Integer, Breakpoint>> _breakpointsByType;
  private boolean _muted;

  public BreakpointManager()
  {
    _breakpointsByType = new HashMap<>();
  }

  public boolean isMuted()
  {
    return _muted;
  }

  public void setMuted( boolean mute )
  {
    _muted = mute;

    Debugger debugger = getDebugger();
    if( debugger != null )
    {
      debugger.muteBreakpoints( mute );
    }
    repaintEditor();
  }

  public Breakpoint getBreakpoint( Breakpoint bp )
  {
    return getBreakpointAtEditorLine( bp.getFqn(), bp.getLine() );
  }

  public Breakpoint findBreakpoint( String fqn, int line )
  {
    Map<Integer, Breakpoint> byLine = _breakpointsByType.get( fqn );
    if( byLine != null )
    {
      return byLine.get( line );
    }
    return null;
  }

  public void toggleLineBreakpoint( String fqn, int line )
  {
    Breakpoint bp = getBreakpointAtEditorLine( fqn, line );
    if( bp == null )
    {
      if( canAddBreakpoint( line ) )
      {
        addBreakpoint( fqn, line );
      }
    }
    else
    {
      removeBreakpoint( bp );
    }
  }

  public void runToCursor( String fqn, int line )
  {
    if( canAddBreakpoint( line ) )
    {
      Debugger debugger = getDebugger();
      if( debugger != null )
      {
        Breakpoint runToCursorBp = new RunToCursorBreakpoint( fqn, line );
        debugger.addBreakpointJdi( runToCursorBp );
        debugger.resumeExecution();
      }
    }
  }

  public boolean canAddBreakpoint( int line )
  {
    GosuEditor editor = getCurrentEditor();
    if( editor == null )
    {
      return false;
    }
    IParseTree location = editor.getStatementAtLine( line );
    if( location == null )
    {
      return false;
    }
    IParsedElement parsedElement = location.getParsedElement();
    return !(parsedElement instanceof IStatementList ||
             parsedElement instanceof IFunctionStatement ||
             parsedElement instanceof IPropertyStatement ||
             parsedElement instanceof IClassStatement ||
             parsedElement instanceof IClassFileStatement ||
             parsedElement instanceof IUsesStatement ||
             parsedElement instanceof IUsesStatementList ||
             parsedElement instanceof INamespaceStatement ||
             (parsedElement instanceof IVarStatement && !((IVarStatement)parsedElement).getHasInitializer()));
  }

  public List<Breakpoint> getBreakpoints()
  {
    List<Breakpoint> breakpoints = new ArrayList<>();
    _breakpointsByType.values().stream().forEach( m -> m.values().forEach( breakpoints::add ) );
    return breakpoints;
  }

  public Map<Integer, Breakpoint> getBreakpointsByType( String fqn )
  {
    return _breakpointsByType.get( fqn );
  }

  public void removeBreakpoint( Breakpoint bp )
  {
    Map<Integer, Breakpoint> byLine = _breakpointsByType.get( bp.getFqn() );
    if( byLine == null )
    {
      return;
    }
    Breakpoint breakpoint = byLine.get( bp.getLine() );
    if( breakpoint != null )
    {
      byLine.remove( bp.getLine() );
      Debugger debugger = getDebugger();
      if( debugger != null )
      {
        debugger.removeBreakpointJdi( bp );
      }
    }
  }

  public Breakpoint getBreakpointAtEditorLine( String fqn, int line )
  {
    Map<Integer, Breakpoint> byLine = _breakpointsByType.get( fqn );
    if( byLine == null )
    {
      return null;
    }
    return byLine.get( line );
  }

  public Breakpoint getExecPointAtEditorLine( String fqn, int line )
  {
    Debugger debugger = getDebugger();
    if( debugger == null )
    {
      return null;
    }
    Location location = debugger.getSuspendedLocation();
    if( location == null )
    {
      return null;
    }
    if( line == location.lineNumber() && fqn.equals( Debugger.getOutermostType( location.declaringType() ) ) )
    {
      return new Breakpoint( fqn, line );
    }
    return null;
  }

  public Breakpoint getFramePointAtEditorLine( String fqn, int line )
  {
    Debugger debugger = getDebugger();
    if( debugger == null )
    {
      return null;
    }

    ThreadReference thread = getGosuPanel().getDebugPanel().getSelectedThread();
    if( thread == null )
    {
      return null;
    }

    try
    {
      for( StackFrame frame: thread.frames() )
      {
        Location location = frame.location();
        if( line == location.lineNumber() && fqn.equals( Debugger.getOutermostType( location.declaringType() ) ) )
        {
          return new Breakpoint( fqn, line );
        }
      }
    }
    catch( IncompatibleThreadStateException e )
    {
      // eat
    }
    return null;
  }

  private void addBreakpoint( String fqn, int line )
  {
    Map<Integer, Breakpoint> byLine = _breakpointsByType.get( fqn );
    if( byLine == null )
    {
      _breakpointsByType.put( fqn, byLine = new HashMap<>() );
    }
    Breakpoint bp = new Breakpoint( fqn, line );
    byLine.put( line, bp );
    Debugger debugger = getDebugger();
    if( debugger != null )
    {
      debugger.addBreakpointJdi( bp );
    }
  }

  private GosuEditor getCurrentEditor()
  {
    return getGosuPanel().getCurrentEditor();
  }

  private Debugger getDebugger()
  {
    return getGosuPanel().getDebugger();
  }

  private GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  private void repaintEditor()
  {
    GosuEditor editor = getCurrentEditor();
    if( editor != null )
    {
      editor.repaint();
    }
  }
}
