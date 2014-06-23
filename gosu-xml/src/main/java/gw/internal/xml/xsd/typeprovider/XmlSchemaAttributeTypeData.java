/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodInfoBuilder;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlTypeInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * IType for statically typed XmlAttribute types.
 */
public class XmlSchemaAttributeTypeData<T> extends XmlSchemaTypeData<T> implements IXmlSchemaAttributeTypeData<T> {

  private static final String QNAME_PROPERTY_NAME = "$QNAME";

  private final T _context;
  private final XmlSchemaResourceTypeLoaderBase<T> _typeLoader;
  private final String _typeName;
  private XmlSchemaType _xsdType;
  private final XmlSchemaAttribute _xsdAttribute;
  private final String _defaultValue;
  private XmlSimpleValueFactory _simpleValueFactory;

  public XmlSchemaAttributeTypeData( XmlSchemaResourceTypeLoaderBase<T> typeLoader, String typeName, XmlSchemaType xsdType, XmlSchemaAttribute xsdAttribute, String defaultValue, T context ) {
    //noinspection unchecked
    super( xsdAttribute.getSchemaIndex() );
    _typeLoader = typeLoader;
    _typeName = typeName;
    _xsdType = xsdType;
    _xsdAttribute = xsdAttribute;
    _defaultValue = defaultValue;
    _context = context;
  }

  public XmlSchemaResourceTypeLoaderBase getTypeProvider() {
    return _typeLoader;
  }

  public XmlSchemaAttribute getXsdAttribute() {
    return _xsdAttribute;
  }

  public XmlSimpleValueFactory getSimpleValueFactory() {
    if ( _simpleValueFactory == null ) {
      _simpleValueFactory = XmlSchemaIndex.getSimpleValueFactoryForSchemaType( getXsdType() );
    }
    return _simpleValueFactory;
  }

  public XmlSchemaType getXsdType() {
    if ( _xsdType == null ) {
      _xsdType = XmlSchemaIndex.getSchemaTypeForAttribute( _xsdAttribute );
    }
    return _xsdType;
  }

  @Override
  public boolean prefixSuperProperties() {
    return true;
  }

  @Override
  public long getFingerprint() {
    return _xsdAttribute.getSchemaIndex().getFingerprint();
  }

  @Override
  public Class getBackingClass() {
    return Object.class; // Change this if constructors are ever added
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return JavaTypes.OBJECT().getBackingClassInfo();
  }

  public List<IPropertyInfo> getDeclaredProperties() {
    List<IPropertyInfo> props = new ArrayList<IPropertyInfo>();

    props.add( new PropertyInfoBuilder()
            .withName( QNAME_PROPERTY_NAME )
            .withType( JavaTypes.QNAME() )
            .withDescription( "The QName of this attribute" )
            .withWritable( false )
            .withStatic()
            .withAccessor( new IPropertyAccessor() {
                @Override
                public Object getValue( Object ctx ) {
                  QName qname = _xsdAttribute.getQName();
                  if ( qname == null ) {
                    qname = _xsdAttribute.getRefName();
                  }
                  return qname;
                }

                @Override
                public void setValue( Object ctx, Object value ) {
                  throw new UnsupportedOperationException();
                }
              }
            )
            .build( this ) );

    return props;
  }

//  private XmlSimpleValue getDefaultSimpleValue() {
//    if ( getDefaultValue() != null ) {
//      return getSimpleValueFactory().deserialize( new XmlDeserializationContext( null, null, null, null ), getDefaultValue(), true );
//    }
//    return null;
//  }

  public List<IMethodInfo> getDeclaredMethods() {
    List<IMethodInfo> methods = new ArrayList<IMethodInfo>();
    methods.add( new MethodInfoBuilder().withName( "set" ).withStatic().withParameters(
            new ParameterInfoBuilder().withName( "anyType" ).withType( TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.complex.AnyType" ) ),
            new ParameterInfoBuilder().withName( "value" ).withType( getSimpleValueFactory().getGosuValueType() )
    ).withCallHandler( new IMethodCallHandler() {
      @Override
      public Object handleCall( Object ctx, Object... args ) {
        XmlTypeInstance typeInstance = (XmlTypeInstance) args[0];
        typeInstance.setAttributeSimpleValue( getQName(), getSimpleValueFactory().gosuValueToStorageValue( args[1] ) );
        return null;
      }
    } ).build( this ) );
    methods.add( new MethodInfoBuilder().withName( "set" ).withStatic().withParameters(
            new ParameterInfoBuilder().withName( "element" ).withType( TypeSystem.get( XmlElement.class ) ),
            new ParameterInfoBuilder().withName( "value" ).withType( getSimpleValueFactory().getGosuValueType() )
    ).withCallHandler( new IMethodCallHandler() {
      @Override
      public Object handleCall( Object ctx, Object... args ) {
        XmlElement element = (XmlElement) args[0];
        element.setAttributeSimpleValue( getQName(), getSimpleValueFactory().gosuValueToStorageValue( args[1] ) );
        return null;
      }
    } ).build( this ) );
    methods.add( new MethodInfoBuilder().withName( "createSimpleValue" ).withStatic().withParameters(
            new ParameterInfoBuilder().withName( "value" ).withType( getSimpleValueFactory().getGosuValueType() )
    ).withReturnType( XmlSimpleValue.class ).withCallHandler( new IMethodCallHandler() {
      @Override
      public Object handleCall( Object ctx, Object... args ) {
        return getSimpleValueFactory().gosuValueToStorageValue( args[1] );
      }
    } ).build( this ) );
    return methods;
  }

  private QName getQName() {
    QName qname = _xsdAttribute.getQName();
    if ( qname == null ) {
      qname = _xsdAttribute.getRefName();
    }
    return qname;
  }

  public List<IConstructorInfo> getDeclaredConstructors() {
    return Collections.emptyList();
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
    return JavaTypes.OBJECT();
  }

  @Override
  public String getName() {
    return _typeName;
  }

  public String getDefaultValue() {
    return _defaultValue;
  }

  @Override
  public XmlSchemaObject getSchemaObject() {
    return _xsdAttribute;
  }

  @Override
  public boolean isAnonymous() {
    return false;
  }

  public T getContext() {
    return _context;
  }

  @Override
  public void maybeInit() {
    // nothing to do
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
    return Collections.<Class<?>>singletonList( IXmlSchemaAttributeTypeData.class );
  }

}
