/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.IGlobalLoaderProvider;
import gw.lang.reflect.ITypeLoader;

import java.util.ArrayList;
import java.util.List;
import manifold.api.service.BaseService;

/**
 */
public class GosucGlobalLoaderProvider extends BaseService implements IGlobalLoaderProvider {
  private final List<Class<? extends ITypeLoader>> _loaderClasses;

  public GosucGlobalLoaderProvider(List<String> classNames) {
    _loaderClasses = new ArrayList<Class<? extends ITypeLoader>>();
    for (String name : classNames) {
      try {
        _loaderClasses.add((Class<? extends ITypeLoader>) Class.forName(name));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public List<Class<? extends ITypeLoader>> getGlobalLoaderTypes() {
    return _loaderClasses;
  }
}
