package editor;

/**
 */
public class Breakpoint
{
  private final String _fqn;
  private final int _line;
  private boolean _active;
  private String _fileName;

  public Breakpoint( String fqn, int line )
  {
    _fqn = fqn;
    _line = line;
    _active = true;
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
    return "Line " + _line + " in " + getFileName();
  }

  private String getFileName()
  {
    if( _fileName == null )
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

  public boolean isActiveWhenMuted()
  {
    return false;
  }

  public boolean isTemporary()
  {
    return false;
  }

  //## todo: this is where we will eval the expression, if exists
  public boolean condition()
  {
    return true;
  }
}
