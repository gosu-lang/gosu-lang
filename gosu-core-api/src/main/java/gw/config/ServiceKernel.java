/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;


import gw.lang.parser.ILanguageLevel;
import gw.util.Stack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class ServiceKernel
{

  private Map<Class<? extends IService>, IService> _services;
  private Stack<IService> _initingServices = new Stack<>();
  private boolean _definingServices = false;

  protected ServiceKernel()
  {
    resetKernel();
  }

  protected void resetKernel()
  {
    _services = new HashMap<Class<? extends IService>, IService>();
    _definingServices = true;
    try
    {
      defineServices();
    }
    finally
    {
      _definingServices = false;
    }
    redefineServices();
  }

  /**
   * Contains all the definitions of the services provided by this kernel
   */
  protected abstract void defineServices();

  /**
   * Contains the redefinition logic for this kernel
   */
  protected abstract void redefineServices();

  /**
   * @param service
   * @return
   */
  public <T extends IService> T getService( Class<? extends T> service )
  {
    if( _definingServices )
    {
      throw new IllegalStateException( "Service definition in progress, access to " + service.getName() + " is not " +
                                       "allowed.  Move this access to the init() method of the offending service." );
    }
    IService serviceImpl = _services.get( service );
    if( serviceImpl == null )
    {
      throw new IllegalStateException( "The service " + service.getName() + " is not provided by this ServiceKernel." );
    }
    return (T)serviceImpl;
  }

  /**
   * Overrides the default implemenation of the service with a different provider.  Note that the current
   * provider cannot have been accessed (all services must be consistent during runtime.)
   *
   * @param service - the service to provide
   * @param newProvider - the new provider of this service
   */
  public <T extends IService, Q extends T> void redefineService(Class<? extends T> service, Q newProvider)
  {
    if( _definingServices )
    {
      throw new IllegalStateException( "Service definition in progress, so service redefinition is not allowed.  Please " +
                                       "move redefinitions to the redefineServices method." );
    }
    IService existingServiceImpl = _services.get( service );
    if( existingServiceImpl == null )
    {
      throw new IllegalArgumentException( "Service " + service.getName() + " is not defined in this ServiceKernel.");
    }
    if( existingServiceImpl.isInited() )
    {
      throw new IllegalStateException( "Service " + service.getName() + " has already been " +
                                       "initialized with the " + existingServiceImpl.getClass().getName() +
                                       " implementation");
    }
    _services.put( service, newProvider );
  }

  public <T extends IService, Q extends T> void redefineService_Privileged(Class<? extends T> service, Q newProvider)
  {
    IService existingServiceImpl = _services.get( service );
    if( existingServiceImpl == null )
    {
      throw new IllegalArgumentException( "Service " + service.getName() + " is not defined in this ServiceKernel.");
    }
    _services.put( service, newProvider );
  }

  protected <T extends IService> void defineService(Class<? extends T> serviceClass, Class<? extends T> implClass ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException
  {
    Constructor ctor = implClass.getDeclaredConstructor();
    ctor.setAccessible( true );
    T serviceImpl = (T)ctor.newInstance();
    defineService( serviceClass, serviceImpl );
  }
  /**
   * Defines a service provided by this ServiceKernel
   *
   * @param service - the service to provide
   * @param defaultImplementation - the default implementation of this service
   */
  protected <T extends IService, Q extends T> void defineService(Class<? extends T> service, Q defaultImplementation)
  {
    if( !_definingServices )
    {
      throw new IllegalStateException( "Service definition must be done only in the defineServices() method." );
    }
    if( !service.isInterface() )
    {
      throw new IllegalArgumentException( "Services may only be defined as interfaces, and " +
                                          service.getName() +
                                          " is not an interface" );
    }
    IService existingServiceImpl = _services.get( service );
    if( existingServiceImpl != null )
    {
      throw new IllegalStateException( "Service " + service.getName() + " has already been " +
                                       "defined with the " + existingServiceImpl.getClass().getName() +
                                       " default implementation");
    }
    _services.put( service, defaultImplementation );
  }

  /**
   * @param initClassName a class name of a class that implements {@link ServiceKernelInit) and that will be created
   * and given a chance to redefine the service implementations in this kernel.
   */
  protected void redefineServicesWithClass( String initClassName )
  {
    try
    {
      Class<?> aClass = getClass().forName( initClassName );
      ServiceKernelInit init = (ServiceKernelInit)aClass.newInstance();
      init.init( this );
    }
    catch( ClassNotFoundException e )
    {
      try {
        Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass( initClassName );
        ServiceKernelInit init = (ServiceKernelInit)aClass.newInstance();
        init.init( this );
      } catch (Exception e1) {
        e1.printStackTrace();
        throw new RuntimeException( e1 );
      }
    }
    catch (Exception e1) {
      throw new RuntimeException( e1 );
    } finally {
      ILanguageLevel.Util.reset();
    }
  }


  private <T extends IService> void detectCircularInitializationDependencies( IService service )
  {
    if( _initingServices.contains( service ) )
    {
      StringBuilder sb = new StringBuilder( "Circular service initialization dependency detected : " );
      for( IService initingService : _initingServices )
      {
        sb.append( "\n\t" ).append( initingService );
      }
      throw new IllegalStateException( sb.toString() );
    }
  }

}