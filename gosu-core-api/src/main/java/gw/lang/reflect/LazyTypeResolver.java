package gw.lang.reflect;

import gw.util.concurrent.LocklessLazyVar;

/**
 */
public class LazyTypeResolver extends LocklessLazyVar<IType> {
  private final ITypeResolver _resolver;

  public LazyTypeResolver( ITypeResolver resolver ) {
    _resolver = resolver;
  }

  LazyTypeResolver() {
    _resolver = null;
  }

  @Override
  protected IType init() {
    return _resolver.resolve();
  }

  public interface ITypeResolver {
    IType resolve();
  }
}
