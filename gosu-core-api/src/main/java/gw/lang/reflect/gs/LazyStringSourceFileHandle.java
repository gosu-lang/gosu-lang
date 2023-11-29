package gw.lang.reflect.gs;

import gw.lang.parser.ISource;
import gw.lang.parser.StringSource;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 */
public class LazyStringSourceFileHandle extends StringSourceFileHandle
{
  private Function<Boolean,String> _sourceGen;
  private String _namespace;

  public LazyStringSourceFileHandle( String nspace, String fqn, Callable<String> sourceGen, ClassType classType )
  {
    this( nspace, fqn,
          __ -> {
            try {
              return sourceGen.call();
            }
            catch( Exception e ) {
              throw new RuntimeException(e);
            }
          },
          classType );
  }

  public LazyStringSourceFileHandle( String nspace, String fqn, Function<Boolean,String> sourceGen, ClassType classType )
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
        setRawSource( _sourceGen.apply( false ) );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return super.getSource();
  }
  @Override
  public ISource getSource( boolean header )
  {
    // don't cache raw source if header, next call to getSource() can be non-header, which includes full source
    return header ? new StringSource( _sourceGen.apply( header ) ) : getSource();
  }
}