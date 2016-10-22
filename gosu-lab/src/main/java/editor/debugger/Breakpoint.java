package editor.debugger;

import com.sun.jdi.BooleanValue;
import editor.FileTree;
import editor.FileTreeUtil;
import editor.RunMe;
import gw.lang.parser.IParseTree;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.json.IJsonIO;
import gw.util.concurrent.LocklessLazyVar;

/**
 */
public class Breakpoint implements IJsonIO
{
  public static final LocklessLazyVar<Breakpoint> ANY_EXCEPTION =
    new LocklessLazyVar<Breakpoint>()
    {
      protected Breakpoint init()
      {
        Breakpoint anyException = new Breakpoint( "java.lang.Throwable", true, true, true );
        anyException.setActive( false );
        return anyException;
      }
    };

  private String _fqn;

  private int _line;
  private String _expr;
  private boolean _active;

  private boolean _static;

  private boolean _suspend;
  private boolean _bRunScript;
  private String _runScript;
  private boolean _caughtException;
  private boolean _uncaughtException;

  private transient int _offset;
  private transient String _fileName;
  private transient String _immediateClass;


  public Breakpoint( String fqnException, boolean notifyCaught, boolean notifyUncaught )
  {
    this( fqnException, notifyCaught, notifyUncaught, false );
  }
  public Breakpoint( String fqnException, boolean notifyCaught, boolean notifyUncaught, boolean staticBp )
  {
    this( fqnException, 0, null );
    _caughtException = notifyCaught;
    _uncaughtException = notifyUncaught;
    _static = staticBp;
  }

  public Breakpoint( String fqn, int line )
  {
    this( fqn, line, null );
  }

  public Breakpoint( String fqn, int line, String expr )
  {
    _fqn = fqn;
    _line = line;
    _active = true;
    _expr = expr;
    _suspend = true;
  }

  // for JsonIO
  @SuppressWarnings("UnusedDeclaration")
  private Breakpoint()
  {
  }

  public boolean isLineBreakpoint()
  {
    return _line > 0;
  }

  public boolean isExceptionBreakpoint()
  {
    return !isLineBreakpoint();
  }

  public boolean isActive()
  {
    return _active;
  }
  public void setActive( boolean active )
  {
    _active = active;
  }

  public String getTitle()
  {
    if( _line <= 0 )
    {
      // exception breakpoint
      return this == ANY_EXCEPTION.get() ? "Any exception" : _fqn;
    }
    // line breakpoint
    return "Line " + _line + " in " + getFileName();
  }

  private String getFileName()
  {
    if( _line > 0 && _fileName == null )
    {
      FileTree fileTree = FileTreeUtil.find( _fqn );
      if( fileTree != null )
      {
        _fileName = fileTree.getName();
      }
    }
    return _fileName;
  }

  public String getFqn()
  {
    return _fqn;
  }

  public int getLine()
  {
    return _line;
  }

  public int getOffset()
  {
    locate();
    return _offset;
  }

  public String getExpression()
  {
    return _expr;
  }
  public void setExpression( String expr )
  {
    _expr = expr;
  }

  public boolean isActiveWhenMuted()
  {
    return false;
  }

  public boolean isTemporary()
  {
    return false;
  }

  public boolean condition()
  {
    if( isRunScriptOn() )
    {
      String runScript = getRunScript();
      if( runScript != null && runScript.length() > 0 )
      {
        DebuggerExpression debugExpr = new DebuggerExpression( runScript, isLineBreakpoint() ? _fqn : null, _immediateClass, _offset );
        debugExpr.evaluate( RunMe.getEditorFrame().getGosuPanel().getDebugger() );
      }
    }

    boolean suspend = isSuspend();
    if( suspend )
    {
      String expr = getExpression();
      if( expr != null && !expr.isEmpty() )
      {
        boolean canEvaluateCondition = isLineBreakpoint() && locate();
        if( canEvaluateCondition )
        {
          DebuggerExpression debugExpr = new DebuggerExpression( expr, isLineBreakpoint() ? _fqn : null, _immediateClass, _offset );
          BooleanValue result = (BooleanValue)debugExpr.evaluate( RunMe.getEditorFrame().getGosuPanel().getDebugger() );
          suspend = result.value();
        }
      }
    }
    return suspend;
  }

  private boolean locate()
  {
    if( _immediateClass != null )
    {
      return true;
    }

    if( _line <= 0 )
    {
      return false;
    }

    IGosuClass topLevelClass = (IGosuClass)TypeSystem.getByFullName( _fqn );
    _offset = getLineOffset( topLevelClass.getSource() );
    topLevelClass.isValid();
    IParseTree loc = topLevelClass.getClassStatement().getLocation().getDeepestLocation( _offset, true );
    if( loc == null )
    {
      return false;
    }
    _immediateClass = loc.getParsedElement().getGosuClass().getName();
    return true;
  }

  private int getLineOffset( String content )
  {
    int lineCsr = 1;
    int offset = 0;
    for( ; offset < content.length(); offset++ )
    {
      if( lineCsr == _line )
      {
        return offset;
      }
      char c = content.charAt( offset );
      if( c == '\n' )
      {
        lineCsr++;
      }
    }
    return offset;
  }

  public boolean isStatic()
  {
    return _static;
  }
  public void setStatic( boolean aStatic )
  {
    _static = aStatic;
  }

  public boolean isSuspend()
  {
    return _suspend;
  }
  public void setSuspend( boolean suspend )
  {
    _suspend = suspend;
  }

  public boolean isRunScriptOn()
  {
    return _bRunScript;
  }
  public void setRunScriptOn( boolean bRunScript )
  {
    _bRunScript = bRunScript;
  }

  public String getRunScript()
  {
    return _runScript;
  }
  public void setRunScript( String runScript )
  {
    _runScript = runScript;
  }

  public boolean isCaughtException()
  {
    return _caughtException;
  }
  public void setCaughtException( boolean caughtException )
  {
    _caughtException = caughtException;
  }

  public boolean isUncaughtException()
  {
    return _uncaughtException;
  }
  public void setUncaughtException( boolean uncaughtException )
  {
    _uncaughtException = uncaughtException;
  }
}
