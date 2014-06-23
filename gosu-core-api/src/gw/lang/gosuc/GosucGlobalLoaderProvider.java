/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.BaseService;
import gw.config.IGlobalLoaderProvider;
import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.ITypeLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
