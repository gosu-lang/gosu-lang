package editor.debugger;

/**
 */
public enum BreakpointKind
{
  Line( "Line" ),
  Exception( "Exception" ),
  Method( "Method" ),
  Field( "Field Watchpoint" );

  private final String _name;

  BreakpointKind( String name )
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }
}
