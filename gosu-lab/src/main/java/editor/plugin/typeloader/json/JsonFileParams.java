package editor.plugin.typeloader.json;

import editor.plugin.typeloader.INewFileParams;

/**
 */
public class JsonFileParams implements INewFileParams
{
  private String _fqn;

  public JsonFileParams( String fqn )
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
