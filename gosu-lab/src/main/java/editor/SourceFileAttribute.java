package editor;

/**
 */
public class SourceFileAttribute
{
  private int _line;
  private String _fqn;

  public SourceFileAttribute( int line, String fqn )
  {
    _line = line;
    _fqn = fqn;
  }

  public int getLine()
  {
    return _line;
  }

  public String getFqn()
  {
    return _fqn;
  }
}
