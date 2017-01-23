package editor.debugger;

/**
 */
public class RunToCursorBreakpoint extends Breakpoint
{
  public RunToCursorBreakpoint( String fqn, String fqnDeclaring, int line )
  {
    super( fqn, fqnDeclaring, line );
  }

  @Override
  public boolean isActiveWhenMuted()
  {
    return true;
  }

  @Override
  public boolean isTemporary()
  {
    return true;
  }
}
