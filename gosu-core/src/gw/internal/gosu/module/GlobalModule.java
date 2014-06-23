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
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IGlobalModule;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalModule extends Module implements IGlobalModule
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
    FileSystemGosuClassRepository repository = new FileSystemGosuClassRepository(this);
    CommonServices.getTypeSystem().pushTypeLoader(this, new GosuClassTypeLoader(this, repository));
    createGlobalTypeloaders();
  }

  public void createGlobalTypeloaders() {
    ModuleTypeLoader _moduleTypeLoader = getModuleTypeLoader();

    List<Class<? extends ITypeLoader>> globalLoaderTypes = CommonServices.getGlobalLoaderProvider().getGlobalLoaderTypes();
    if( globalLoaderTypes != null ) {
      Collections.reverse(globalLoaderTypes);
    }
    IFileSystemGosuClassRepository classRepository = new FileSystemGosuClassRepository(this);
    classRepository.setSourcePath(getAllSourcePaths());

    TypeSystem.pushModule(this);
    try {
      if( globalLoaderTypes != null ) {
        for (Class<? extends ITypeLoader> globalLoader : globalLoaderTypes) {
          try {
            ITypeLoader typeLoader = createTypeLoader(classRepository, this, globalLoader);
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
    } finally {
      TypeSystem.popModule(this);
    }
  }

  private IDirectory[] getAllSourcePaths() {
    List<IDirectory> srcs = new ArrayList<IDirectory>();
    for (IModule m : getModuleTraversalList()) {
      srcs.addAll(m.getSourcePath());
    }
    return srcs.toArray(new IDirectory[srcs.size()]);
  }

  /**
   * In global module, all dependencies should be traversed, even non-exported.
   */
  @Override
  protected void traverse(final IModule theModule, List<IModule> traversalList) {
    traversalList.add(theModule);
    for (Dependency dependency : theModule.getDependencies()) {
      IModule dependencyModule = dependency.getModule();
      if (!traversalList.contains(dependencyModule)) {
        traverse(dependencyModule, traversalList);
      }
    }
  }

  protected static ITypeLoader createTypeLoader(
      IFileSystemGosuClassRepository classRepository,
      IModule module,
      Class loaderClass)
      throws ClassNotFoundException, InstantiationException,
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
      } else if (parameterTypes.length == 1 &&
                 parameterTypes[0] == gw.lang.reflect.module.IModule.class) {
        typeLoader = (ITypeLoader) cons.newInstance(module);
      } else if (cons.getParameterTypes().length == 1 && cons.getParameterTypes()[0] == IGosuClassRepository.class) {
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
