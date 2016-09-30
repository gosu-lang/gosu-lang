package editor.debugger;

/**
 */
public class RunToCursorBreakpoint extends Breakpoint
{
  public RunToCursorBreakpoint( String fqn, int line )
  {
    super( fqn, line );
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
