package editor;

/**
*/
public enum MessageKind
{
  Root( false ),
  File( false ),
  Directory( false ),
  Info( true ),
  Warning( true ),
  Error( true ),
  Failure( true );

  private final boolean _bTerminal;

  MessageKind( boolean bTerminal )
  {
    _bTerminal = bTerminal;
  }

  public boolean isTerminal()
  {
    return _bTerminal;
  }
}
