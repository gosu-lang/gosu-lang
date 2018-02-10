package gw.lang.reflect.gs;

import gw.lang.parser.ISource;
import java.util.concurrent.Callable;
import manifold.api.type.ClassType;

/**
 */
public class LazyStringSourceFileHandle extends StringSourceFileHandle
{
  private Callable<String> _sourceGen;
  private String _namespace;

  public LazyStringSourceFileHandle( String nspace, String fqn, Callable<String> sourceGen, ClassType classType )
  {
    super( fqn, null, false, classType );
    _namespace = nspace;
    _sourceGen = sourceGen;
  }

  public String getTypeNamespace()
  {
    return _namespace;
  }

  @Override
  public ISource getSource()
  {
    if( getRawSource() == null )
    {
      try
      {
        setRawSource( _sourceGen.call() );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return super.getSource();
  }
}