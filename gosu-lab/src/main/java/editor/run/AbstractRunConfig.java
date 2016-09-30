package editor.run;

/**
 */
public abstract class AbstractRunConfig<T extends AbstractRunConfigParameters<T>> implements IRunConfig<T>
{
  private T _params;

  public AbstractRunConfig( T params )
  {
    _params = params;
  }

  // this if for reading from json
  protected AbstractRunConfig()
  {
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
    if( !(o instanceof AbstractRunConfig) )
    {
      return false;
    }

    AbstractRunConfig that = (AbstractRunConfig)o;

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
