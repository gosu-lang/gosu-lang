package editor.plugin.typeloader.properties;

import editor.plugin.typeloader.INewFileParams;

/**
 */
public class PropertiesFileParams implements INewFileParams
{
  private String _fqn;

  public PropertiesFileParams( String fqn )
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
