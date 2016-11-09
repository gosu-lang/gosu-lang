package editor.plugin.typeloader.java;

import editor.plugin.typeloader.INewFileParams;

/**
 */
public class JavaFileParams implements INewFileParams
{
  private String _fqn;

  public JavaFileParams( String fqn )
  {
    _fqn = fqn;
  }

  @Override
  public String getFqn()
  {
    return _fqn;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }
}
