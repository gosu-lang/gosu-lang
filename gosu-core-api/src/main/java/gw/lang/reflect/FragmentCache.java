/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.IGosuFragment;
import gw.util.cache.WeakFqnCache;
import manifold.api.host.ITypeLoaderListener;
import manifold.api.host.RefreshRequest;

public class FragmentCache implements ITypeLoaderListener
{
  private static FragmentCache INSTANCE;
  private WeakFqnCache<IGosuFragment> _fragmentsByName = new WeakFqnCache<IGosuFragment>();

  public static FragmentCache instance() {
    if (INSTANCE == null) {
      INSTANCE = new FragmentCache();
    }
    return INSTANCE;
  }

  public FragmentCache() {
    TypeSystem.addTypeLoaderListenerAsWeakRef(this);
  }

  public IGosuFragment get(String strFullyQualifiedName) {
    return _fragmentsByName.get(strFullyQualifiedName);
  }

  public void addFragment(IGosuFragment fragment) {
    _fragmentsByName.add(fragment.getName(), fragment);
  }

  @Override
  public void refreshedTypes(RefreshRequest request) {
    _fragmentsByName.remove(request.types);
  }

  @Override
  public void refreshed() {
    _fragmentsByName.clear();
  }

}
