package editor.settings;


/**
 */
public abstract class AbstractSettings<T extends AbstractSettingsParameters<T>> implements ISettings<T>
{
  private T _params;

  private transient String _name;
  private transient String _path;


  public AbstractSettings( T params, String name, String path )
  {
    _params = params;
    _name = name;
    _path = path;
  }

  @Override
  public String getName()
  {
    return _name;
  }

  @Override
  public String getPath()
  {
    return _path;
  }

  @Override
  public T getParams()
  {
    return _params;
  }
  public void setParams( T params )
  {
    _params = params;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof AbstractSettings) )
    {
      return false;
    }

    AbstractSettings that = (AbstractSettings)o;

    return _params.equals( that._params );
  }

  @Override
  public int hashCode()
  {
    int result = _params.hashCode();
    result = 31 * result + _params.hashCode();
    return result;
  }
}
