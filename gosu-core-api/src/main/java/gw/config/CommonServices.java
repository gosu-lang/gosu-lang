/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.IGosuShop;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IGosuParserFactory;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.ITypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import manifold.api.fs.IFileSystem;

import java.lang.reflect.Constructor;

public class CommonServices extends ServiceKernel
{
  private static CommonServices _kernel = new CommonServices();
  private static ITypeSystem _typeSystem;  //maintained outside the kernel for perf reasons
  private static IFileSystem _fileSystem = getDefaultFileSystemInstance(); // Currently not technically a service, since it needs to be available all the time

  private CommonServices()
  {
    Registry.addLocationListener( e -> _kernel.resetKernel() );
  }

  protected void defineServices()
  {
    _kernel = this;
    
    try
    {
      defineService( IFileSystem.class, getDefaultFileSystemInstance() );
      defineService( IEntityAccess.class, (IEntityAccess)Class.forName( "gw.internal.gosu.parser.DefaultEntityAccess" ).newInstance() );
      _typeSystem = (ITypeSystem)Class.forName( "gw.internal.gosu.parser.TypeLoaderAccess" ).newInstance();
      //noinspection unchecked
      defineService( ICoercionManager.class, (ICoercionManager)Class.forName( "gw.lang.parser.StandardCoercionManager" ).newInstance() );
      defineService( IGosuParserFactory.class, (IGosuParserFactory)Class.forName( "gw.internal.gosu.parser.GosuParserFactoryImpl" ).newInstance() );
      defineService( IGosuShop.class, (IGosuShop)Class.forName( "gw.internal.gosu.parser.GosuIndustrialParkImpl" ).newInstance() );
      defineService( IGosuLocalizationService.class, (IGosuLocalizationService)Class.forName("gw.internal.gosu.DefaultLocalizationService").newInstance());
      defineService( IXmlSchemaCompatibilityConfig.class, (IXmlSchemaCompatibilityConfig)Class.forName( "gw.config.DefaultXmlSchemaCompatibilityConfig" ).newInstance() );
      defineService( IPlatformHelper.class, (IPlatformHelper)Class.forName( "gw.internal.gosu.parser.DefaultPlatformHelper" ).newInstance() );
      defineService( IExtensionFolderLocator.class, (IExtensionFolderLocator)Class.forName( "gw.config.DefaultExtensionFolderLocator" ).newInstance() );
      defineService( IMemoryMonitor.class, (IMemoryMonitor)Class.forName( "gw.internal.gosu.memory.DefaultMemoryMonitor" ).newInstance() );
      defineService( IGosuInitializationHooks.class, new DefaultGosuInitializationHooks());
      defineService( IGlobalLoaderProvider.class, new DefaultGlobalLoaderProvider());
      defineService( IGosuProfilingService.class, new DefaultGosuProfilingService() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    catch( NoClassDefFoundError e)  {
      e.printStackTrace();
      throw e;
    }
  }

  private static IFileSystem getDefaultFileSystemInstance() {
    try {
      Class cls = Class.forName("manifold.api.fs.def.FileSystemImpl");
      Constructor m = cls.getConstructor(IFileSystem.CachingMode.class);
      if( BytecodeOptions.JDWP_ENABLED.get() )
      {
        return (IFileSystem) m.newInstance(IFileSystem.CachingMode.NO_CACHING);
      }
      return (IFileSystem) m.newInstance(IFileSystem.CachingMode.FULL_CACHING);
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  protected void redefineServices()
  {
    redefineServicesWithClass( Registry.instance().getCommonServiceInit() );
  }

  public static IEntityAccess getEntityAccess()
  {
    return _kernel.getService( IEntityAccess.class );
  }

  public static ICoercionManager getCoercionManager()
  {
    return _kernel.getService( ICoercionManager.class );
  }

  @SuppressWarnings("UnusedDeclaration")
  public static IGosuProfilingService getGosuProfilingService()
  {
    return _kernel.getService( IGosuProfilingService.class );
  }

  public static ITypeSystem getTypeSystem()
  {
    return _typeSystem;
  }

  public static void sneakySetTypeSystem(ITypeSystem typeSystem) {
    _typeSystem = typeSystem;
  }

  public static IGosuParserFactory getGosuParserFactory()
  {
    return _kernel.getService( IGosuParserFactory.class );
  }

  public static IGosuShop getGosuIndustrialPark()
  {
    return _kernel.getService( IGosuShop.class );
  }

  public static IGosuLocalizationService getGosuLocalizationService()
  {
    return _kernel.getService( IGosuLocalizationService.class );
  }

  public static IXmlSchemaCompatibilityConfig getXmlSchemaCompatibilityConfig() {
    return _kernel.getService( IXmlSchemaCompatibilityConfig.class );
  }

  public static IPlatformHelper getPlatformHelper() {
    return _kernel.getService( IPlatformHelper.class );
  }

  public static IGosuInitializationHooks getGosuInitializationHooks() {
    return _kernel.getService( IGosuInitializationHooks.class );
  }

  public static IGlobalLoaderProvider getGlobalLoaderProvider() {
    return _kernel.getService(IGlobalLoaderProvider.class);
  }

  public static IExtensionFolderLocator getExtensionFolderLocator() {
    return _kernel.getService(IExtensionFolderLocator.class);
  }

  public static IFileSystem getFileSystem() {
    return _kernel.getService(IFileSystem.class);
  }

  public static IMemoryMonitor getMemoryMonitor() {
    return _kernel.getService(IMemoryMonitor.class);
  }

  public static CommonServices getKernel() {
    return _kernel;
  }
}
