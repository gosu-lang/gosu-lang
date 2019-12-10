/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.config.Registry;
import gw.config.TypeLoaderSpec;
import gw.fs.IDirectory;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
import gw.internal.gosu.parser.ModuleTypeLoader;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IGlobalModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

public abstract class GlobalModule extends Module implements IGlobalModule
{
  public GlobalModule(IExecutionEnvironment execEnv, String moduleName)
  {
    super( execEnv, moduleName);
  }

  @Override
  protected void createExtensionTypeLoaders() {
    // do nothing
  }

  @Override
  protected void createStandardTypeLoaders() {
    FileSystemGosuClassRepository repository = new FileSystemGosuClassRepository();
    CommonServices.getTypeSystem().pushTypeLoader( new GosuClassTypeLoader( repository) );
    createGlobalTypeloaders();
  }

  public void createGlobalTypeloaders() {
    ModuleTypeLoader _moduleTypeLoader = getModuleTypeLoader();

    List<Class<? extends ITypeLoader>> globalLoaderTypes = CommonServices.getGlobalLoaderProvider().getGlobalLoaderTypes();
    if( globalLoaderTypes != null ) {
      Collections.reverse(globalLoaderTypes);
    }
    IFileSystemGosuClassRepository classRepository = new FileSystemGosuClassRepository();
    classRepository.setSourcePath( getSourcePath().toArray( new IDirectory[0] ) );

    if( globalLoaderTypes != null ) {
      for (Class<? extends ITypeLoader> globalLoader : globalLoaderTypes) {
        try {
          ITypeLoader typeLoader = createTypeLoader(classRepository, globalLoader);
          if (typeLoader != null) {
            _moduleTypeLoader.pushTypeLoader(typeLoader);
          } else {
            throw new NullPointerException();
          }
        } catch (Throwable t) {
          throw new RuntimeException("Cannot create type loader: " + globalLoader, t);
        }
      }
    }

    //TODO - remove this if/when we no longer support typeloaders in the registry.xml file
    List<TypeLoaderSpec> typeLoaderList = Registry.instance().getAdditionalTypeLoaders();
    for (TypeLoaderSpec typeLoaderSpec : typeLoaderList) {
      ITypeLoader typeLoader = typeLoaderSpec.createTypeLoader(this.getExecutionEnvironment());
      if (typeLoader != null) {
        _moduleTypeLoader.pushTypeLoader(typeLoader);
      }
    }

    // initialize loaders
    List<ITypeLoader> loaders = _moduleTypeLoader.getTypeLoaders();
    for (int i = loaders.size() - 1; i >= 0; i--) {
      loaders.get(i).init();
    }

    CommonServices.getGosuInitializationHooks().afterTypeLoaderCreation();
  }

  protected static ITypeLoader createTypeLoader( IFileSystemGosuClassRepository classRepository, Class loaderClass )
    throws InstantiationException,
           IllegalAccessException, InvocationTargetException {

    try {
    ITypeLoader typeLoader;
    CommonServices.getGosuInitializationHooks().beforeTypeLoaderCreation(loaderClass);
    Constructor[] constructors = loaderClass.getConstructors();
    typeLoader = null;
    for (Constructor cons : constructors) {
      Class[] parameterTypes = cons.getParameterTypes();
      if (parameterTypes.length == 0) {
        typeLoader = (ITypeLoader) cons.newInstance();
      }  else if (cons.getParameterTypes().length == 1 && cons.getParameterTypes()[0] == IGosuClassRepository.class) {
        typeLoader = (ITypeLoader) cons.newInstance(classRepository);
      } else {
        // Ignore it
      }
    }
    return typeLoader;
    } catch (LinkageError le) {
      throw le;
    }
  }

}
