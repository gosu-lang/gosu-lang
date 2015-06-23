package gw.lang.reflect;


/**
 */
public class NotLazyTypeResolver extends LazyTypeResolver {
  public NotLazyTypeResolver( IType type ) {
    initDirectly( type );
  }

  @Override
  protected IType init() {
    throw new IllegalStateException( "The type should have been initialized directly at construction" );
  }
}
