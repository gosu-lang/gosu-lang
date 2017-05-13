/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import java.util.List;

import gw.lang.reflect.ITypeLoader;
import manifold.api.service.IService;

public interface IGlobalLoaderProvider extends IService
{

  List<Class<? extends ITypeLoader>> getGlobalLoaderTypes();
}
