package gw.lang.reflect;

/**
 */
public class SimpleTypeLazyTypeResolver extends LazyTypeResolver {
  private final String _fqn;
  private final String _module;

  public SimpleTypeLazyTypeResolver( String fqn, String module ) {
    _fqn = fqn;
    _module = module;
  }

  @Override
  protected IType init() {
    //noinspection deprecation
    return TypeSystem.getByFullName( _fqn, _module );
  }
}
