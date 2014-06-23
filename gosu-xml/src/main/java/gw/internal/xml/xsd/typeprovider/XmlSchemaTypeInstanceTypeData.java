/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSimpleValueInternals;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.XmlTypeInstanceInternals;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.ILocationAwareFeature;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.LocationInfo;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IAsmJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.IXmlSchemaEnumValue;
import gw.xml.XmlBase;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlTypeInstance;

import javax.xml.namespace.QName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IType for statically typed XmlTypeInstance types. See XmlTypeInstance for a discussion of what an XmlTypeInstance is. 
 */
public class XmlSchemaTypeInstanceTypeData<T> extends XmlSchemaTypeData<T> implements IXmlSchemaTypeInstanceTypeData<T>, ILocationAwareFeature {

  private boolean _initialized;
  private XmlSchemaTypeSchemaInfo _schemaInfo;

  private static final String VALUE_PROPERTY_NAME = "$Value";
  private static final String QNAME_PROPERTY_NAME = "$QNAME";

  private final XmlSchemaResourceTypeLoaderBase _typeLoader;
  private final String _typeName;
  private final boolean _anonymousType;
  private final XmlSchemaType _xsdType;

  private final T _context;
  private static final String ELEMENT_QNAME_PREFIX = "$ELEMENT_QNAME_";
  private static final String ATTRIBUTE_QNAME_PREFIX = "$ATTRIBUTE_QNAME_";

  private LockingLazyVar<IType> _superType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      XmlSchemaTypeInstanceTypeData superTypeData = _superTypeData.get();
      return superTypeData == null ? TypeSystem.get( XmlTypeInstance.class ) : superTypeData.getType();
    }
  };

  private LockingLazyVar<XmlSchemaTypeInstanceTypeData> _superTypeData = new LockingLazyVar<XmlSchemaTypeInstanceTypeData>() {
    @Override
    protected XmlSchemaTypeInstanceTypeData init() {
      XmlSchemaTypeInstanceTypeData superType = null;
      if ( getXsdType() instanceof XmlSchemaComplexType ) {
        XmlSchemaComplexType complexType = (XmlSchemaComplexType) getXsdType();
        if ( complexType.getContentModel() instanceof XmlSchemaComplexContent ) {
          XmlSchemaComplexContent contentModel = (XmlSchemaComplexContent) complexType.getContentModel();
          if ( contentModel.getContent() instanceof XmlSchemaComplexContentExtension ) {
            XmlSchemaComplexContentExtension extension = (XmlSchemaComplexContentExtension) contentModel.getContent();
            superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaComplexTypeByQName( extension.getBaseTypeName() ) );
          }
          else if ( contentModel.getContent() instanceof XmlSchemaComplexContentRestriction ) {
            XmlSchemaComplexContentRestriction restriction = (XmlSchemaComplexContentRestriction) contentModel.getContent();
            QName baseTypeName = restriction.getBaseTypeName();
            if ( baseTypeName != null ) {
              superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaComplexTypeByQName( baseTypeName ) );
            }
          }
        }
        else if ( complexType.getContentModel() instanceof XmlSchemaSimpleContent ) {
          XmlSchemaSimpleContent contentModel = (XmlSchemaSimpleContent) complexType.getContentModel();
          if ( contentModel.getContent() instanceof XmlSchemaSimpleContentExtension ) {
            XmlSchemaSimpleContentExtension extension = (XmlSchemaSimpleContentExtension) contentModel.getContent();
            superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaTypeByQName( extension.getBaseTypeName() ) );
          }
          else if ( contentModel.getContent() instanceof XmlSchemaSimpleContentRestriction ) {
            XmlSchemaSimpleContentRestriction restriction = (XmlSchemaSimpleContentRestriction) contentModel.getContent();
            superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaTypeByQName( restriction.getBaseTypeName() ) );
          }
        }
      }
      else {
        XmlSchemaSimpleType simpleType = (XmlSchemaSimpleType) getXsdType();
        if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeRestriction ) {
          XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) simpleType.getContent();
          superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaSimpleTypeByQName( restriction.getBaseTypeName() ) );
        }
        if ( superType == null ) {
          if ( XmlSchemaIndex.ANY_SIMPLE_TYPE_QNAME.equals( simpleType.getQName() ) ) {
            superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaComplexTypeByQName( XmlSchemaIndex.ANY_TYPE_QNAME ) );
          }
          else {
            superType = (XmlSchemaTypeInstanceTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( getXsdType().getSchemaIndex().getXmlSchemaSimpleTypeByQName( XmlSchemaIndex.ANY_SIMPLE_TYPE_QNAME ) );
          }
        }
      }
      return superType;
    }
  };

  private final LockingLazyVar<Constructor<?>> _constructorInternal = new LockingLazyVar<Constructor<?>>() {
    @Override
    protected Constructor<?> init() {
      // Find the most specific java-backed type in the hierarchy. There will always be at least one (AnyType)
      IType type = getType();
      while ( true ) {
        try {
          Constructor<?> ctor = Class.forName( "gw.internal.schema." + type.getName() ).getDeclaredConstructor( IType.class, Object.class );
          ctor.setAccessible( true );
          return ctor;
        }
        catch ( ClassNotFoundException ex ) {
          type = type.getSupertype();
          // continue
        }
        catch ( Exception ex ) {
          throw GosuExceptionUtil.forceThrow( ex );
        }
      }
    }
  };

  public XmlSchemaTypeInstanceTypeData( XmlSchemaIndex<T> schemaIndex, XmlSchemaResourceTypeLoaderBase typeLoader, String typeName, XmlSchemaType xsdType, boolean anonymousType, T context ) {
    super( schemaIndex );
    _typeLoader = typeLoader;
    _typeName = typeName;
    _xsdType = xsdType;
    _anonymousType = anonymousType;
    _context = context;
  }

  public XmlSchemaResourceTypeLoaderBase getTypeLoader() {
    return _typeLoader;
  }

  public XmlSchemaType getXsdType() {
    return _xsdType;
  }

  @Override
  public boolean prefixSuperProperties() {
    return getSuperType().equals( TypeSystem.get( XmlTypeInstance.class ) );
  }

  @Override
  public IType getSupertypeToCopyPropertiesFrom() {
    return TypeSystem.get( XmlTypeInstance.class );
  }

  @Override
  public long getFingerprint() {
    return getSchemaIndex().getFingerprint();
  }

  @Override
  public Class getBackingClass() {
    IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(getType().getName());
    return (!(clazz instanceof IAsmJavaClassInfo) && clazz != null) ? clazz.getBackingClass() : XmlTypeInstance.class;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(getType().getName());
    return clazz != null ? clazz : (JavaTypes.getSystemType(XmlTypeInstance.class)).getBackingClassInfo();
  }

  public List<IPropertyInfo> getDeclaredProperties() {
    maybeInit();
    List<IPropertyInfo> props = new ArrayList<IPropertyInfo>();

    // create "QNAME" constant if this is a top-level type
    if ( getXsdType().getQName() != null ) {
      makeQNameConstant( props );
    }

    // create "Value" property if this is a simple type or has a simple content
    if ( _schemaInfo.hasSimpleContent() ) {
      makeSimpleValueProperty( props );
    }

    Map<XmlSchemaPropertyType,Map<QName,XmlSchemaPropertySpec>> todo = new HashMap<XmlSchemaPropertyType, Map<QName, XmlSchemaPropertySpec>>();
    boolean gotRestriction = false;
    XmlSchemaTypeSchemaInfo schemaInfo = _schemaInfo;
    while ( schemaInfo != null ) {
      for ( XmlSchemaPropertySpec prop : schemaInfo.getProperties() ) {
        if ( gotRestriction && prop.getPropertyType() == XmlSchemaPropertyType.ELEMENT ) {
          continue; // stop grabbing super element properties once we hit a complex restriction
        }
        Map<QName, XmlSchemaPropertySpec> map = todo.get( prop.getPropertyType() );
        if ( map == null ) {
          map = new HashMap<QName, XmlSchemaPropertySpec>();
          todo.put( prop.getPropertyType(), map );
        }
        if ( ! map.containsKey( prop.getQName() ) ) {
          map.put( prop.getQName(), prop );
        }
      }
      if ( schemaInfo.isComplexRestriction() ) {
        gotRestriction = true;
      }
      schemaInfo = schemaInfo.getSuperElementInfo();
    }

    for ( Map<QName, XmlSchemaPropertySpec> map : todo.values() ) {
      for ( XmlSchemaPropertySpec prop : map.values() ) {
        if ( ! prop.isProhibited() ) {
          makeChildProperties( props, prop );
        }
      }
    }

    return props;
  }

  private void makeQNameConstant( List<IPropertyInfo> props ) {
    props.add( new PropertyInfoBuilder()
            .withLocation( getLocationInfo() )
            .withName( QNAME_PROPERTY_NAME )
            .withType( JavaTypes.QNAME() )
            .withDescription( "The QName of this type" )
            .withWritable( false )
            .withStatic()
            .withAccessor( new IPropertyAccessor() {
                @Override
                public Object getValue( Object ctx ) {
                  return getXsdType().getQName();
                }

                @Override
                public void setValue( Object ctx, Object value ) {
                  throw new UnsupportedOperationException();
                }
              }
            )
            .build( this ) );
  }

  private void makeChildProperties( List<IPropertyInfo> props, final XmlSchemaPropertySpec prop ) {
    if ( prop.getXmlSchemaObject() instanceof XmlSchemaAny ) {
      return;
    }
    if ( prop.getSimpleTypePropertyName() != null ) {
      makeSimpleValueChildProperty( props, prop );
    }
    if ( prop.getElementPropertyName() != null ) {
      makeChildElementProperty( props, prop );
    }
    if ( prop.getQNamePropertyName() != null ) {
      makeQNameStaticProperty( props, prop );
    }
  }

  private void makeQNameStaticProperty( List<IPropertyInfo> props, final XmlSchemaPropertySpec prop ) {
    props.add( new PropertyInfoBuilder()
            .withLocation( prop.getXmlSchemaObject().getLocationInfo() )
            .withName( prop.getQNamePropertyName() )
            .withStatic()
            .withWritable( false )
            .withType( JavaTypes.QNAME() )
            .withDescription( "Returns " + ( prop.getQName().getNamespaceURI().length() == 0 ? ( "{}" + prop.getQName().getLocalPart() ) : prop.getQName().toString() ) )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue(Object ctx) {
                return prop.getQName();
              }

              @Override
              public void setValue(Object ctx, Object value) {
                throw new UnsupportedOperationException();
              }
            })
            .build( this ) );
  }

  private void makeChildElementProperty( List<IPropertyInfo> props, final XmlSchemaPropertySpec prop ) {
    final IType propType = prop.getElementPropertyGosuType( prop.isPlural() );
    LocationInfo locationInfo = prop.getXmlSchemaObject().getLocationInfo();
    props.add( new PropertyInfoBuilder()
            .withLocation( locationInfo )
            .withName( prop.getElementPropertyName() )
            .withType( propType )
            .withAnnotations( new XmlSchemaAutoinsertAnnotationData( this ), new XmlSchemaAutocreateAnnotationData( getType(), this ) )
            .withWritable( true )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                XmlBase node = (XmlBase) ctx;
                if ( prop.isPlural() ) {
                  return XmlTypeInstanceInternals.instance()._getChildrenBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                } else {
                  return XmlTypeInstanceInternals.instance()._getChildBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                }
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                XmlBase node = (XmlBase) ctx;
                if ( prop.isPlural() ) {
                  XmlTypeInstanceInternals.instance()._removeChildrenBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                  //noinspection unchecked
                  node.getChildren().addAll( (List<XmlElement>) value );
                } else {
                  // first remove any existing children by that name
                  XmlTypeInstanceInternals.instance()._removeChildBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                  if ( value != null ) {
                    XmlElement child = (XmlElement) value;
                    node.addChild( child );
                  }
                }
              }
            } )
            .build( this ) );
  }

  private void makeSimpleValueChildProperty( List<IPropertyInfo> props, final XmlSchemaPropertySpec prop) {
    final IType simpleTypePropertyGosuType = prop.getSimpleTypePropertyGosuType( prop.isPlural() );
    LocationInfo locationInfo = prop.getXmlSchemaObject().getLocationInfo();
    props.add( new PropertyInfoBuilder()
            .withLocation( locationInfo )
            .withName( prop.getSimpleTypePropertyName() )
            .withType( simpleTypePropertyGosuType )
            .withAnnotations( new XmlSchemaAutoinsertAnnotationData( this ), new XmlSchemaAutocreateAnnotationData( simpleTypePropertyGosuType, this ) )
            .withWritable( true )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                XmlBase node = (XmlBase) ctx;
                if ( prop.isPlural() ) {
                  return new SimpleValueList( node, prop );
                } else {
                  switch ( prop.getPropertyType() ) {
                    case ATTRIBUTE: {
                      XmlSimpleValue simpleValue = node.getAttributeSimpleValue( prop.getQName() );
                      if ( simpleValue == null ) {
                        String defaultValue = prop.getDefaultValue();
                        if ( defaultValue != null ) {
                          simpleValue = prop.getSimpleValueFactory().deserialize( new XmlDeserializationContext( null ), defaultValue, true );
                        }
                      }
                      if ( simpleValue == null ) {
                        return null;
                      } else {
                        simpleValue = reserializeSimpleValueIfNecessary( simpleValue, prop );
                        return simpleValue.getGosuValue();
                      }
                    }
                    case ELEMENT: {
                      XmlElement child;
                      child = XmlTypeInstanceInternals.instance()._getChildBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                      XmlSimpleValue simpleValue = null;
                      if ( child != null ) {
                        simpleValue = child.getSimpleValue();
                        if ( simpleValue != null ) {
                          simpleValue = reserializeSimpleValueIfNecessary( simpleValue, prop );
                        }
                      }
                      return simpleValue == null ? null : simpleValue.getGosuValue();
                    }
                    default:
                      throw new RuntimeException( "Unknown schema object type: " + prop.getPropertyType() );
                  }
                }
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                if ( prop.isPlural() ) {
                  if ( value == null ) {
                    throw new IllegalArgumentException( "Simple value list cannot be null" );
                  }
                  @SuppressWarnings( { "MismatchedQueryAndUpdateOfCollection" } )
                  List list = (List) getValue( ctx );
                  list.clear();
                  List toSet = (List) value;
                  //noinspection unchecked
                  list.addAll( toSet );
                } else {
                  XmlBase node = (XmlBase) ctx;
                  switch ( prop.getPropertyType() ) {
                    case ATTRIBUTE: {
                      if ( value instanceof String ) {
                        // XML requires attributes to go through the whitespace "replace" function
                        // This isn't strictly necessary here, but it provides a more uniform view when
                        // compared with how whitespace handling would be done by the validator anyway
                        // this really belongs somewhere else
                        value = new XmlSimpleValueValidationContext().doReplace( (String) value );
                      }
                      XmlSimpleValue storageValue = value == null ? null : gosuValueToStorageValue( prop, prop, value );
                      node.setAttributeSimpleValue( prop.getQName(), storageValue );
                      break;
                    }
                    case ELEMENT: {
                      if ( value == null ) {
                        XmlTypeInstanceInternals.instance()._removeChildBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                      } else {
                        XmlElement child = XmlTypeInstanceInternals.instance()._getChildBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
                        addSimpleContent( value, node, child, prop );
                      }
                      break;
                    }
                    default:
                      throw new RuntimeException( "Unknown schema object type: " + prop.getPropertyType() );
                  }
                }
              }
            } )
            .build( this ) );
  }

  private XmlSimpleValue reserializeSimpleValueIfNecessary( XmlSimpleValue simpleValue, XmlSchemaSimpleValueProvider prop ) {
    IType propGosuValueType = prop.getSimpleValueFactory().getGosuValueType();
    if ( ! propGosuValueType.isAssignableFrom( simpleValue.getGosuValueType() ) ) {
      // xsd -> gosu type mapping mismatch ( byte does not extend int in Gosu as it does in XSD )
      // this isn't quite right of course. you really want to check the simple type hierarchy or something, to see
      // if the two types are coercible
      if ( propGosuValueType instanceof IXmlSchemaEnumerationTypeData ) {
        IXmlSchemaEnumerationTypeData typeData = (IXmlSchemaEnumerationTypeData) propGosuValueType;
        simpleValue = XmlSimpleValue.makeEnumInstance( (IXmlSchemaEnumValue) typeData.deserialize( simpleValue ) );
      }
      else {
        simpleValue = prop.getSimpleValueFactory().deserialize( new XmlDeserializationContext( null ), simpleValue.getStringValue(), false );
      }
    }
    return simpleValue;
  }

  private void addSimpleContent( Object newValue, XmlBase parentElement, XmlElement child, XmlSchemaPropertySpec prop ) {
    if ( child == null ) {
      IType xmlElementType = XmlSchemaIndex.getGosuTypeBySchemaObject( prop.getXmlSchemaObject() );
      child = (XmlElement) xmlElementType.getTypeInfo().getConstructor().getConstructor().newInstance();
      parentElement.addChild( child );
    }
    final XmlSchemaTypeSchemaInfo schemaInfo = XmlSchemaIndex.getSchemaInfoByType( child.getTypeInstance().getIntrinsicType() );
    XmlSimpleValue storageValue = gosuValueToStorageValue( prop, schemaInfo, newValue );
    child.setSimpleValue( storageValue );
  }

  private XmlSimpleValue gosuValueToStorageValue( XmlSchemaSimpleValueProvider staticSchemaInfo, XmlSchemaSimpleValueProvider runtimeSchemaInfo, Object value ) {
    XmlSimpleValue storageValue = null;
    if ( value != null ) {
      XmlSimpleValueValidationContext context = new XmlSimpleValueValidationContext();
      if ( runtimeSchemaInfo.getSimpleValueFactory().getGosuValueType().isAssignableFrom( staticSchemaInfo.getSimpleValueFactory().getGosuValueType() ) ) {
        // use schema factory on the value verbatim, since it handles gosu values of this type
        XmlSimpleValueFactory factory = runtimeSchemaInfo.getSimpleValueFactory();
        if ( value instanceof String ) {
          // this really belongs somewhere else
          value = runtimeSchemaInfo.getValidator().collapseWhitespace( (String) value, context );
        }
        storageValue = factory.gosuValueToStorageValue( value );
      }
      else {
        // convert through property value factory first, then convert to string, then reparse using schema value factory
        String newValue = staticSchemaInfo.getSimpleValueFactory().gosuValueToStorageValue( value ).getStringValue();
        newValue = runtimeSchemaInfo.getValidator().collapseWhitespace( newValue, context );
        storageValue = runtimeSchemaInfo.getSimpleValueFactory().deserialize( new XmlDeserializationContext( null ), newValue, false );
      }
      XmlSimpleValueInternals.instance().validate( storageValue, runtimeSchemaInfo.getValidator(), context );
    }
    return storageValue;
  }

  private void makeSimpleValueProperty( List<IPropertyInfo> props) {
    props.add( new PropertyInfoBuilder()
            .withLocation( _xsdType.getLocationInfo() )
            .withName( VALUE_PROPERTY_NAME )
            .withType( _schemaInfo.getSimpleTypePropertyGosuType() )
            .withAnnotations( new XmlSchemaAutoinsertAnnotationData( this ), new XmlSchemaAutocreateAnnotationData( _schemaInfo.getSimpleTypePropertyGosuType(), this ) )
            .withWritable( true )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                XmlBase xsdTypeOrElement = (XmlBase) ctx;
                XmlSimpleValue simpleValue = xsdTypeOrElement.getSimpleValue();
                if ( simpleValue != null ) {
                  simpleValue = reserializeSimpleValueIfNecessary( simpleValue, _schemaInfo );
                }
                return simpleValue == null ? null : simpleValue.getGosuValue();
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                XmlTypeInstance typeInstance = ( (XmlBase) ctx ).getTypeInstance();
                final XmlSchemaTypeSchemaInfo runtimeSchemaInfo = XmlSchemaIndex.getSchemaInfoByType( typeInstance.getIntrinsicType() );
                XmlSimpleValue storageValue = value == null ? null : gosuValueToStorageValue( _schemaInfo, runtimeSchemaInfo, value );
                typeInstance.setSimpleValue( storageValue );
              }
            } )
            .build( this ) );
  }

  public void maybeInit() {
    if ( ! _initialized ) {
      TypeSystem.lock();
      try {
        if ( ! _initialized ) {
          XmlSimpleValueFactory xmlSimpleValueFactory = XmlSchemaIndex.getSimpleValueFactoryForSchemaType( getXsdType() );
          XmlSimpleValueValidator validator = XmlSchemaIndex.getSimpleValueValidatorForSchemaType( getXsdType() );
          _schemaInfo = new XmlSchemaTypeSchemaInfo( this, xmlSimpleValueFactory, validator );
          LinkedHashMap<String, LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>>> properties = collateProperties();
          Set<String> usedPropertyNames = new HashSet<String>();
          for ( Map.Entry<String,LinkedHashMap<XmlSchemaPropertyType,LinkedHashMap<QName,PropertySpec>>> entry : properties.entrySet() ) {
            final String originalPropertyName = entry.getKey();
            for ( Map.Entry<XmlSchemaPropertyType,LinkedHashMap<QName,PropertySpec>> entry2 : entry.getValue().entrySet() ) {
              final XmlSchemaPropertyType propertyType = entry2.getKey();
              final String originalPropertyName2;
              if ( entry.getValue().size() > 1 ) { // multiple property types - qualify
                originalPropertyName2 = originalPropertyName + propertyType.getSuffix(); // differentiate _Element and _Attribute
              }
              else {
                originalPropertyName2 = originalPropertyName;
              }
              Map<QName, PropertySpec> map = entry2.getValue();
              for ( Map.Entry<QName, PropertySpec> entry3 : map.entrySet() ) {
                QName qname = entry3.getKey();
                PropertySpec propertySpec = entry3.getValue();
                XmlSchemaObject xmlSchemaObject = propertySpec._xmlSchemaObject;
                boolean isPlural = propertySpec._isPlural;
                String elementPropertyName = null;
                String qnamePropertyName;
                String simpleTypePropertyName;
                if ( propertyType == XmlSchemaPropertyType.ELEMENT ) {
                  elementPropertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, originalPropertyName2, _typeLoader.getPropertyNameNormalizationMode() );
                  qnamePropertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, ELEMENT_QNAME_PREFIX + elementPropertyName, _typeLoader.getPropertyNameNormalizationMode() );
                  simpleTypePropertyName = null;
                  // simple type, or complex type with simple content - both cause two properties to be created.... Whatever and Whatever_elem
                  if ( XmlSchemaIndex.hasSimpleContent( xmlSchemaObject ) ) {
                    simpleTypePropertyName = elementPropertyName;
                    elementPropertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, simpleTypePropertyName + "_elem", _typeLoader.getPropertyNameNormalizationMode() );
                  }
                }
                else {
                  simpleTypePropertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, originalPropertyName2, _typeLoader.getPropertyNameNormalizationMode() );
                  qnamePropertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, ATTRIBUTE_QNAME_PREFIX + simpleTypePropertyName, _typeLoader.getPropertyNameNormalizationMode() );
                }
                _schemaInfo.addProperty( new XmlSchemaPropertySpec( elementPropertyName, simpleTypePropertyName, qnamePropertyName, qname, xmlSchemaObject, propertySpec._xmlSchemaType, propertyType, isPlural, propertySpec._defaultValue, propertySpec._isProhibited ) );
              }
            }
          }
          _initialized = true;
        }
      }
      catch ( Exception ex ) {
        throw new RuntimeException( "Error initializing typeinfo for " + _typeName, ex );
      }
      finally {
        TypeSystem.unlock();
      }
    }
  }

  private LinkedHashMap<String, LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>>> collateProperties() {
    LinkedHashMap<String, LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>>> properties =
            new LinkedHashMap<String, LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>>>();
    //noinspection unchecked
    for ( XmlSchemaFlattenedChild child : (List<XmlSchemaFlattenedChild>)getXsdType().getSchemaIndex().getFlattenedChildrenBySchemaType( getXsdType() ) ) {
      XmlSchemaPropertyType propertyType;
      QName propertyName;
      XmlSchemaObject xmlSchemaObject = child.getXmlSchemaObject();
      String defaultValue = null;
      boolean isProhibited;
      XmlSchemaType xmlSchemaType;
      if ( xmlSchemaObject instanceof XmlSchemaElement) {
        XmlSchemaElement xsdElement = (XmlSchemaElement) xmlSchemaObject;
        xsdElement = XmlSchemaIndex.getActualElement( xsdElement );
        propertyName = xsdElement.getQName();
        xmlSchemaType = XmlSchemaIndex.getSchemaTypeForElement( xsdElement );
        propertyType = XmlSchemaPropertyType.ELEMENT;
        isProhibited = false;
        xmlSchemaObject = xsdElement; // in case it changed
      }
      else if ( xmlSchemaObject instanceof XmlSchemaAttribute ) {
        XmlSchemaAttribute xsdAttribute = (XmlSchemaAttribute) xmlSchemaObject;
        xsdAttribute = XmlSchemaIndex.getActualAttribute( xsdAttribute );
        // default and fixed are mutually exclusive
        if ( xsdAttribute.getFixedValue() != null ) {
          defaultValue = xsdAttribute.getFixedValue();
        }
        else {
          defaultValue = xsdAttribute.getDefaultValue();
        }
        propertyName = xsdAttribute.getQName();
        xmlSchemaType = XmlSchemaIndex.getSchemaTypeForAttribute( xsdAttribute );
        propertyType = XmlSchemaPropertyType.ATTRIBUTE;
        isProhibited = xsdAttribute.isProhibited();
        xmlSchemaObject = xsdAttribute; // in case it changed
      }
      else {
        throw new RuntimeException( "Unhandled schema object type: " + xmlSchemaObject.getClass().getName() );
      }
      String normPropName = XmlSchemaIndex.makeCamelCase( XmlSchemaIndex.normalizeName( propertyName.getLocalPart(), _typeLoader.getPropertyNameNormalizationMode() ), _typeLoader.getPropertyNameNormalizationMode() );
      LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>> propertyTypes = properties.get( normPropName );
      if ( propertyTypes == null ) {
        propertyTypes = new LinkedHashMap<XmlSchemaPropertyType, LinkedHashMap<QName,PropertySpec>>();
        properties.put( normPropName, propertyTypes );
      }
      LinkedHashMap<QName, PropertySpec> map = propertyTypes.get( propertyType );
      if ( map == null ) {
        map = new LinkedHashMap<QName, PropertySpec>();
        propertyTypes.put( propertyType, map );
      }
      boolean isPlural = _xsdType.getSchemaIndex().getChildPluralityBySchemaType( _xsdType, propertyType, propertyName );
      PropertySpec schemaType = new PropertySpec( xmlSchemaObject, xmlSchemaType, isPlural, defaultValue, isProhibited );
      map.put( propertyName, schemaType );
    }
    return properties;
  }

  public List<IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  public List<IConstructorInfo> getDeclaredConstructors() {
    maybeInit();
    if ( _xsdType.getName() != null && _xsdType.getName().startsWith( XmlSchemaIndex.REDEFINE_PREFIX ) ) {
      //constructor.withAccessibility( IRelativeTypeInfo.Accessibility.PRIVATE );
      return Collections.emptyList();
    }
    IConstructorHandler constructorHandler;
    final IJavaClassInfo _clazz = getSchemaIndex().getGeneratedClass( getType().getName() );
    IType simpleType = null;
    if ( _schemaInfo.hasSimpleContent() ) {
      simpleType = _schemaInfo.getSimpleValueFactory().getGosuValueType();
    }
    if ( _clazz == null ) {
      constructorHandler = new IConstructorHandler() {
        @Override
        public Object newInstance(Object... args) {
          return XmlTypeInstanceInternals.instance().create( getType(), _schemaInfo, args );
        }
      };
    }
    else {
      final IJavaClassConstructor noParamCtor;
      final IJavaClassConstructor oneParamCtor;
      IJavaType clazz = (IJavaType) TypeSystem.get(_clazz);
      noParamCtor = getConstructor(clazz.getBackingClassInfo());
      if ( simpleType != null ) {
        IJavaClassInfo simpleTypeBackingClass;
        if ( simpleType instanceof IXmlType ) {
          IType type = TypeSystem.getByFullName("gw.internal.schema." + simpleType.getName());
          simpleTypeBackingClass = ((IJavaType)type).getBackingClassInfo();
        }
        else {
          simpleTypeBackingClass = ( (IJavaType) simpleType ).getBackingClassInfo();
        }
        oneParamCtor = getConstructor( clazz.getBackingClassInfo(), simpleTypeBackingClass );
      }
      else {
        oneParamCtor = null;
      }
      constructorHandler = new IConstructorHandler() {
        @Override
        public Object newInstance( Object... args ) {
          try {
            if ( args.length > 0 ) {
              //noinspection ConstantConditions
              return oneParamCtor.newInstance( args[0] );
            }
            else {
              return noParamCtor.newInstance();
            }
          }
          catch ( Throwable t ) {
            while ( t instanceof InvocationTargetException && t.getCause() != null ) {
              t = t.getCause();
            }
            throw GosuExceptionUtil.forceThrow( t );
          }
        }
      };
    }
    List<IConstructorInfo> ctors = new ArrayList<IConstructorInfo>();
    ctors.add( new ConstructorInfoBuilder().withConstructorHandler( constructorHandler ).build( this ) );
    if ( simpleType != null ) {
      ctors.add( new ConstructorInfoBuilder().withConstructorHandler( constructorHandler ).withParameters(
              new ParameterInfoBuilder().withName( "value" ).withType( simpleType )
      ).build( this ) );
    }
    return ctors;
  }
  
  private IJavaClassConstructor getConstructor(IJavaClassInfo clazz, IJavaClassInfo param) {
    IJavaClassConstructor[] declaredConstructors = clazz.getDeclaredConstructors();
    for (IJavaClassConstructor constructor : declaredConstructors) {
      IJavaClassInfo[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length == 1 && parameterTypes[0].equals(param)) {
        return constructor;
      }
    }
    return null;
  }

  private IJavaClassConstructor getConstructor(IJavaClassInfo clazz) {
    IJavaClassConstructor[] declaredConstructors = clazz.getDeclaredConstructors();
    for (IJavaClassConstructor constructor : declaredConstructors) {
      if (constructor.getParameterTypes().length == 0) {
        return constructor;
      }
    }
    return null;
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
    return _superType.get();
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfo() {
    maybeInit();
    return _schemaInfo;
  }

  @Override
  public Constructor<?> getConstructorInternal() {
    return _constructorInternal.get();
  }

  @Override
  public String getName() {
    return _typeName;
  }

  @Override
  public XmlSchemaObject getSchemaObject() {
    return _xsdType;
  }

  @Override
  public boolean isAnonymous() {
    return _anonymousType;
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
    //noinspection unchecked
    return Arrays.asList( IXmlSchemaTypeInstanceTypeData.class, ILocationAwareFeature.class );
  }

  public XmlSchemaTypeInstanceTypeData getSuperTypeData() {
    return _superTypeData.get();
  }

  @Override
  public LocationInfo getLocationInfo() {
    return _xsdType.getLocationInfo();
  }

  private static class PropertySpec {

    private final XmlSchemaObject _xmlSchemaObject;
    private final XmlSchemaType _xmlSchemaType;
    private final boolean _isPlural;
    private final String _defaultValue;
    private final boolean _isProhibited;

    public PropertySpec( XmlSchemaObject xmlSchemaObject, XmlSchemaType xmlSchemaType, boolean isPlural, String defaultValue, boolean isProhibited ) {
      if ( ! ( xmlSchemaObject instanceof XmlSchemaElement || xmlSchemaObject instanceof XmlSchemaAttribute || xmlSchemaObject instanceof XmlSchemaAny ) ) {
        throw new IllegalArgumentException( "INTERNAL ERROR: Unsupported xml schema object: " + xmlSchemaObject );
      }
      _xmlSchemaObject = xmlSchemaObject;
      _xmlSchemaType = xmlSchemaType;
      _isPlural = isPlural;
      _defaultValue = defaultValue;
      _isProhibited = isProhibited;
    }

    @Override
    public String toString() {
      return "PropertySpec{" +
              "xmlSchemaObject=" + _xmlSchemaObject +
              "xmlSchemaType=" + _xmlSchemaType +
              ", isPlural=" + _isPlural +
              ", defaultValue='" + _defaultValue + '\'' +
              '}';
    }
  }

  public T getContext() {
    return _context;
  }

  private class SimpleValueList extends AbstractList<Object> implements IGosuObject {

    private final List<XmlElement> _items;
    private final XmlBase _node;
    private final XmlSchemaPropertySpec _prop;

    public SimpleValueList( XmlBase node, XmlSchemaPropertySpec prop ) {
      _node = node;
      _prop = prop;
      _items = XmlTypeInstanceInternals.instance()._getChildrenBySubstitutionGroup( node.getTypeInstance(), prop.getElementPropertyGosuType() );
    }

    @Override
    public Object get( int index ) {
      XmlElement element = _items.get( index );
      return getElementSimpleValue( element );
    }

    private Object getElementSimpleValue( XmlElement element ) {
      if ( element == null ) {
        return null;
      }
      XmlSimpleValue simpleValue = element.getSimpleValue();
      if ( simpleValue == null ) {
        return null;
      }
      simpleValue = reserializeSimpleValueIfNecessary( simpleValue, _prop );
      return simpleValue.getGosuValue();
    }

    @Override
    public int size() {
      return _items.size();
    }

    @Override
    public IType getIntrinsicType() {
      return _prop.getSimpleTypePropertyGosuType( true );
    }

    @Override
    public boolean add( Object value ) {
      addSimpleContent( value, _node, null, _prop );
      return true;
    }

    @Override
    public void clear() {
      _items.clear();
    }

    @Override
    public Object remove( int index ) {
      return getElementSimpleValue( _items.remove( index ) );
    }

  }

}
