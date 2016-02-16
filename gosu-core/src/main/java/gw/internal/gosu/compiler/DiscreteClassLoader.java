/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;


public class DiscreteClassLoader extends SingleServingGosuClassLoader
{
  static final DiscreteClassLoader NULL_SENTINAL = new DiscreteClassLoader();

  private String _unloadableNamespace;

  private DiscreteClassLoader()
  {}

  DiscreteClassLoader( String unloadableNamespace, GosuClassLoader parent )
  {
    super( parent);
    _unloadableNamespace = unloadableNamespace;
  }

  public String getUnloadableNamespace()
  {
    return _unloadableNamespace;
  }

  @Override
  protected void unload( String gosuClassName )
  {
    CACHE.remove( gosuClassName );
  }
}
