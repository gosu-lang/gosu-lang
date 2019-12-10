package gw.lang.reflect;

/**
 */
public class SimpleTypeLazyTypeResolver extends LazyTypeResolver {
  private final String _fqn;

  public SimpleTypeLazyTypeResolver( String fqn ) {
    _fqn = fqn;
  }

  @Override
  protected IType init() {
    //noinspection deprecation
    return TypeSystem.getByFullName( _fqn );
  }
}
