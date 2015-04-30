/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.config.CommonServices;
import gw.config.IPlatformHelper;
import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.IGosuParser;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class TypeSystemSetup
{
  public enum StartupStatus {NOT_STARTED, STARTED, FAILED}

  private volatile StartupStatus status = StartupStatus.NOT_STARTED;
  private IExecutionEnvironment _execEnv;
  private IModule _globalModule;

  public TypeSystemSetup( IExecutionEnvironment execEnv )
  {
    _execEnv = execEnv;
  }

  public void initializeGosu() {
    // make sure IJavaType is initialized, because if another thread tries
    // to initialize it without the typesystem lock, we'll deadlock
    Class c = IJavaType.class;
    c = IGosuParser.class;

    List<IDirectory> javaClassPath = _execEnv.getGlobalModule().getJavaClassPath();
    GosuInitialization.instance( _execEnv ).uninitializeRuntime();
    CommonServices.getFileSystem().setCachingMode( IFileSystem.CachingMode.FUZZY_TIMESTAMPS );
    FileFactory.instance().setDefaultPhysicalFileSystem( FileFactory.instance().getRootPhysicalFileSystem() );
    List<IModule> modules = defineModules( javaClassPath );

    CommonServices.getKernel().redefineService_Privileged( IPlatformHelper.class, new TestPlatformHelper() );

    GosuInitialization.instance( _execEnv ).initializeMultipleModules( modules );

    //## todo: now with support for multiple projects we should not push and leave pushed the uber module for a given project
    TypeSystem.pushModule(_globalModule);
  }

  private List<IModule> defineModules( List<IDirectory> javaClassPath ) {
    _execEnv.createJreModule( );
    IModule jreModule = _execEnv.getJreModule();
    jreModule.configurePaths(javaClassPath, Collections.<IDirectory>emptyList());

    _globalModule = GosuShop.createGlobalModule(_execEnv);
    _globalModule.configurePaths(Collections.<IDirectory>emptyList(), Collections.<IDirectory>emptyList());
    _globalModule.addDependency(new Dependency(jreModule, false));

    List<IModule> allModules = new ArrayList<IModule>();
    IModule module = defineModule();
    if( module != null ) {
      allModules.add( module );
    }

    addImplicitJreModuleDependency(allModules);
    allModules.add(_globalModule);
    return allModules;
  }

  private void addImplicitJreModuleDependency( List<IModule> modules ) {
    IJreModule jreModule = (IJreModule)_execEnv.getJreModule();
    for( IModule module : modules ) {
      module.addDependency( new Dependency( jreModule, false ) );
    }
    modules.add( jreModule );
  }

  public IModule defineModule() {
    IModule gosuModule = GosuShop.createModule( _execEnv, "myModule" );

    URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
    File path = new File( url.getFile() );

    String strPath = path.getAbsolutePath();
    int iClasses = strPath.indexOf( File.separatorChar + "classes" );
    if( iClasses >= 0 ) {
      strPath = strPath.substring( 0, iClasses ) + File.separatorChar + "src";
    }
    else if( !strPath.endsWith( ".jar" ) ) {
      throw new IllegalStateException( "Curious path: " + strPath );
    }
    IDirectory root = CommonServices.getFileSystem().getIDirectory( new File( strPath ) );
    gosuModule.configurePaths(Collections.singletonList(root), Collections.singletonList(root));

    //Fix this
//    ModuleRootManager rootManager = ModuleRootManager.getInstance(ijModule);
//    File outputPath = rootManager.getOutputLocation().removeFirstSegments(1).toFile();
//    outputPath = new File(moduleLocation, outputPath.getPath());
//    gosuModule.setOutputPath(CommonServices.getFileSystem().getIDirectory(outputPath));

//    gosuModule.setNativeModule(new IJNativeModule(ijModule));
    _globalModule.addDependency(new Dependency(gosuModule, false));
    return gosuModule;
  }
}
