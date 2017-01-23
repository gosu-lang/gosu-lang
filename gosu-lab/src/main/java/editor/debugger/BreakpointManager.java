package editor.debugger;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import editor.EditorHost;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.LabFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class BreakpointManager
{
  private Map<String, Map<Integer, Breakpoint>> _lineBreakpointsByType;
  private Map<String, Breakpoint> _exceptionBreakpoints;
  private Set<Runnable> _listeners;
  private boolean _muted;

  public BreakpointManager()
  {
    _lineBreakpointsByType = new HashMap<>();
    _exceptionBreakpoints = new HashMap<>();
    _exceptionBreakpoints.put( Breakpoint.ANY_EXCEPTION.get().getFqn(), Breakpoint.ANY_EXCEPTION.get() );
    _listeners = new HashSet<>();
  }

  public void addChangeListener( Runnable listener )
  {
    _listeners.add( listener );
  }
  public void removeChangeLisener( Runnable listener )
  {
    _listeners.remove( listener );
  }
  private void notifyListeners()
  {
    EventQueue.invokeLater( () -> _listeners.forEach( Runnable::run ) );
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
    return getBreakpointAtEditorLine( bp.getDeclaringFqn(), bp.getLine() );
  }

  public Breakpoint getBreakpoint( String exceptionFqn )
  {
    return _exceptionBreakpoints.get( exceptionFqn );
  }

  public Breakpoint findBreakpoint( String fqnDeclaring, int line )
  {
    Map<Integer, Breakpoint> byLine = _lineBreakpointsByType.get( fqnDeclaring );
    if( byLine != null )
    {
      return byLine.get( line );
    }
    return null;
  }

  public void toggleLineBreakpoint( EditorHost editor, String fqn, String fqnDeclaring, int line )
  {
    Breakpoint bp = getBreakpointAtEditorLine( fqnDeclaring, line );
    if( bp == null )
    {
      if( canAddBreakpoint( editor, line ) )
      {
        addBreakpoint( fqn, fqnDeclaring, line );
      }
    }
    else
    {
      removeBreakpoint( bp );
    }
  }

  public Breakpoint addExceptionBreakpoint( String fqnException )
  {
    Breakpoint bp = new Breakpoint( fqnException, true, true ); //## todo: add ui for caught/uncaught
    if( _exceptionBreakpoints.putIfAbsent( fqnException, bp ) == null )
    {
      Debugger debugger = getDebugger();
      if( debugger != null )
      {
        debugger.addBreakpointJdi( bp );
      }
    }
    return _exceptionBreakpoints.get( fqnException );
  }

  public void runToCursor( GosuEditor editor )
  {
    int line = editor.getLineNumberAtCaret();
    if( canAddBreakpoint( editor, line ) )
    {
      Debugger debugger = getDebugger();
      if( debugger != null )
      {
        String fqn = editor.getScriptPart().getContainingTypeName();
        Breakpoint runToCursorBp = new RunToCursorBreakpoint( fqn, editor.getTypeAtLine( line ), line );
        debugger.addBreakpointJdi( runToCursorBp );
        debugger.resumeExecution();
      }
    }
  }

  public boolean canAddBreakpoint( EditorHost editor, int line )
  {
    return editor.canAddBreakpoint( line );
  }

  public List<Breakpoint> getLineBreakpoints()
  {
    List<Breakpoint> breakpoints = new ArrayList<>();
    _lineBreakpointsByType.values().stream().forEach( m -> m.values().forEach( breakpoints::add ) );
    return breakpoints;
  }

  public Map<Integer, Breakpoint> getBreakpointsByType( String fqnDeclaring )
  {
    return _lineBreakpointsByType.get( fqnDeclaring );
  }

  public void removeBreakpoint( Breakpoint bp )
  {
    if( bp.isLineBreakpoint() )
    {
      Map<Integer, Breakpoint> byLine = _lineBreakpointsByType.get( bp.getDeclaringFqn() );
      if( byLine == null )
      {
        return;
      }
      Breakpoint breakpoint = byLine.get( bp.getLine() );
      if( breakpoint != null )
      {
        byLine.remove( bp.getLine() );
        removeFromDebuggerAndNotify( bp );
      }
    }
    else
    {
      Breakpoint removed = _exceptionBreakpoints.remove( bp.getFqn() );
      if( removed != null )
      {
        removeFromDebuggerAndNotify( bp );
      }
    }
  }

  private void removeFromDebuggerAndNotify( Breakpoint bp )
  {
    Debugger debugger = getDebugger();
    if( debugger != null )
    {
      debugger.removeBreakpointJdi( bp );
    }
    notifyListeners();
  }

  public Breakpoint getBreakpointAtEditorLine( String fqnDeclaring, int line )
  {
    Map<Integer, Breakpoint> byLine = _lineBreakpointsByType.get( fqnDeclaring );
    if( byLine == null )
    {
      return null;
    }
    return byLine.get( line );
  }

  public Collection<Breakpoint> getExceptionBreakpoints()
  {
    return _exceptionBreakpoints.values();
  }

  public Breakpoint getExceptionBreakpoint( String fqnException )
  {
    return _exceptionBreakpoints.get( fqnException );
  }

  public Breakpoint getExecPointAtEditorLine( String fqn, String fqnDeclaring, int line )
  {
    Debugger debugger = getDebugger();
    if( debugger == null || (!debugger.isSuspended() && !debugger.isPaused()) || fqn == null )
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
      return new Breakpoint( fqn, fqnDeclaring, line );
    }
    return null;
  }

  public Breakpoint getFramePointAtEditorLine( String fqn, String fqnDeclaring, int line )
  {
    Debugger debugger = getDebugger();
    if( debugger == null || (!debugger.isSuspended() && !debugger.isPaused()) || fqn == null )
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
          return new Breakpoint( fqn, fqnDeclaring, line );
        }
      }
    }
    catch( IncompatibleThreadStateException | VMDisconnectedException e )
    {
      // eat
    }
    return null;
  }

  private void addBreakpoint( String fqn, String fqnDeclaring, int line )
  {
    Map<Integer, Breakpoint> byLine = _lineBreakpointsByType.get( fqnDeclaring );
    if( byLine == null )
    {
      _lineBreakpointsByType.put( fqnDeclaring, byLine = new HashMap<>() );
    }
    Breakpoint bp = new Breakpoint( fqn, fqnDeclaring, line );
    byLine.put( line, bp );
    Debugger debugger = getDebugger();
    if( debugger != null )
    {
      debugger.addBreakpointJdi( bp );
    }
    notifyListeners();
  }

  private EditorHost getCurrentEditor()
  {
    return getGosuPanel().getCurrentEditor();
  }

  private Debugger getDebugger()
  {
    return getGosuPanel().getDebugger();
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  private void repaintEditor()
  {
    EditorHost editor = getCurrentEditor();
    if( editor != null )
    {
      editor.repaint();
    }
  }
}
