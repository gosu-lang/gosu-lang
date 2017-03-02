package editor.run;

/**
 */
public class AbstractRunConfigParameters<T extends AbstractRunConfigParameters<T>> implements IRunConfigParameters<T>
{
  private String _name;

  public AbstractRunConfigParameters()
  {
  }

  public AbstractRunConfigParameters( String name )
  {
    _name = name;
  }

  @Override
  public String getName()
  {
    return _name;
  }
  public void setName( String name )
  {
    _name = name;
  }

  @Override
  public void copy( T to )
  {
    to.setName( getName() );
  }
}
