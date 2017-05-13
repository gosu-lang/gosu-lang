/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.reflect.ITypeLoader;

import java.util.Collections;
import java.util.List;
import manifold.api.service.BaseService;

public class DefaultGlobalLoaderProvider extends BaseService implements IGlobalLoaderProvider {

  public List<Class<? extends ITypeLoader>> getGlobalLoaderTypes() {
    return Collections.EMPTY_LIST;
  }
}
