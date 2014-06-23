/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.internal.xml.ws.StaticallyTypedWsdlFaultImpl;
import gw.xml.XmlElement;
import gw.internal.xml.xsd.typeprovider.XmlTypeData;
import gw.internal.xml.xsd.typeprovider.IWsdlFaultTypeData;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPart;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.util.ILogger;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.ws.WsdlFault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WsdlFaultTypeData extends XmlTypeData implements IWsdlFaultTypeData {

  private final Wsdl _wsdl;
  private final String _typeName;


  private boolean _initialized;
  private List<IPropertyInfo> _props;
  private List<IMethodInfo> _methods;
  private List<IConstructorInfo> _constructors;
  private final WsdlPart _part;
  private final LockingLazyVar<IType> _detailElementType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return _part.getSchemaIndex().getGosuTypeByXmlSchemaElementQName( _part.getElementName() );
    }
  };


  /**
   * Create a new port definition
   *
   * @param wsdl the wsdl that this port is defined in
   * @param part
   */
  public WsdlFaultTypeData( final Wsdl wsdl, String typeName, WsdlPart part ) {
    _wsdl = wsdl;
    _typeName = typeName;
    _part = part;
    if ( getLogger().isDebugEnabled() ) {
      getLogger().debug( "WsdlFaultTypeInfo created for " + typeName );
    }
  }

  public ILogger getLogger() {
    return _wsdl.getLogger();
  }

  public void maybeInit() {
    if ( !_initialized ) {
      _methods = new ArrayList<IMethodInfo>();
      _props = new ArrayList<IPropertyInfo>();
      _constructors = new ArrayList<IConstructorInfo>();
      addPropertiesAndMethods( _props, _constructors );
      if ( getLogger().isDebugEnabled() ) {

        getLogger().debug( "WsdlFaultTypeInfo for " + _typeName + " properties=" + _props );
        getLogger().debug( "WsdlFaultTypeInfo for " + _typeName + " methods=" + _methods );
      }
      _initialized = true;
    }
  }

  public void addPropertiesAndMethods( List<IPropertyInfo> props,
                                       List<IConstructorInfo> constructors ) {

    constructors.add( new ConstructorInfoBuilder()
            .withConstructorHandler( new IConstructorHandler() {
              @Override
              public Object newInstance( Object... args ) {
                return createWsdlFault( null, null );
              }
            } )
            .build( this ) );
    constructors.add( new ConstructorInfoBuilder()
            .withParameters( new ParameterInfoBuilder().withName( "message" ).withType( String.class ) )
            .withConstructorHandler( new IConstructorHandler() {
              @Override
              public Object newInstance( Object... args ) {
                return createWsdlFault( null, (String) args[ 0 ] );
              }
            } )
            .build( this ) );
    constructors.add( new ConstructorInfoBuilder()
            .withParameters( new ParameterInfoBuilder().withName( "cause" ).withType( Throwable.class ) )
            .withConstructorHandler( new IConstructorHandler() {
              @Override
              public Object newInstance( Object... args ) {
                return createWsdlFault( (Throwable) args[ 0 ], null );
              }
            } )
            .build( this ) );
    constructors.add( new ConstructorInfoBuilder()
            .withParameters( new ParameterInfoBuilder().withName( "message" ).withType( String.class ), new ParameterInfoBuilder().withName( "cause" ).withType( Throwable.class ) )
            .withConstructorHandler( new IConstructorHandler() {
              @Override
              public Object newInstance( Object... args ) {
                return createWsdlFault( (Throwable) args[ 1 ], (String) args[ 0 ] );
              }
            } )
            .build( this ) );


    props.add( new PropertyInfoBuilder().withName( "Detail" )
            .withType( _detailElementType.get() )
            .withWritable( false )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                WsdlFault castFault = (WsdlFault) ctx;
                return castFault.getDetail();
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                throw new UnsupportedOperationException();
              }
            } ).build( this ) );

  }


  @Override
  public String getName() {
    return _typeName;
  }

  @Override
  public List<IPropertyInfo> getDeclaredProperties() {
    maybeInit();
    return _props;
  }

  @Override
  public List<IMethodInfo> getDeclaredMethods() {
    maybeInit();
    return _methods;
  }

  public List<IConstructorInfo> getDeclaredConstructors() {
    maybeInit();
    return _constructors;
  }

  @Override
  public boolean isFinal() {
    return true;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public IType getSuperType() {
    return TypeSystem.get( WsdlFault.class );
  }

  @Override
  public boolean prefixSuperProperties() {
    return false;
  }

  @Override
  public long getFingerprint() {
    return 0; // TODO
  }

  @Override
  public Class getBackingClass() {
    return WsdlFault.class;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return JavaTypes.getSystemType(WsdlFault.class).getBackingClassInfo();
  }

  @Override
  public XmlSchemaIndex<?> getSchemaIndex() {
    return _part.getSchemaIndex();
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
    return Collections.<Class<?>>singletonList( IWsdlFaultTypeData.class );
  }

  private WsdlFault createWsdlFault( Throwable cause, String message ) {
    final XmlElement detailElement = (XmlElement) _detailElementType.get().getTypeInfo().getConstructor().getConstructor().newInstance();
    StaticallyTypedWsdlFaultImpl fault = new StaticallyTypedWsdlFaultImpl( message, cause, getType() );
    fault.setCode( WsdlFault.FaultCode.Sender );
    fault.setDetail( detailElement );
    return fault;
  }

}
