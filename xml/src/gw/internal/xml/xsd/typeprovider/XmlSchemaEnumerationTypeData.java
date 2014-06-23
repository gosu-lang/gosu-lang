/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaEnumerationFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.*;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.IXmlSchemaEnumValue;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IType for enumerations defined in an XSD.
 */
public class XmlSchemaEnumerationTypeData<T> extends XmlSchemaTypeData<T> implements IXmlSchemaEnumerationTypeData<T> {

  private final String _typeName;

  // by normalized gosu name ( Any -> ##any )
  private final LinkedHashMap<String,IEnumValue> _enumMapByGosuName = new LinkedHashMap<String, IEnumValue>();

  // by schema-defined name ( ##any -> ##any )
  private final LinkedHashMap<Object,IEnumValue> _enumMapByXmlSchemaName = new LinkedHashMap<Object, IEnumValue>();

  private final List<XmlSchemaEnumerationFacet> _enumerations;
  private LinkedHashMap<IEnumValue,XmlSimpleValue> _enumerationSimpleValues;
  private final LockingLazyVar<XmlSimpleValueFactory> _simpleValueFactory = new LockingLazyVar<XmlSimpleValueFactory>() {
    @Override
    protected XmlSimpleValueFactory init() {
      XmlSchemaType baseType;
      if ( _baseTypeName == null ) {
        baseType = _baseType;
      }
      else {
        baseType = _parentType.getSchemaIndex().getXmlSchemaTypeByQName( _baseTypeName );
      }
      return XmlSchemaIndex.getSimpleValueFactoryForSchemaType( baseType );
    }
  };

  private boolean _initialized;

  private final T _context;
  private final XmlSchemaType _parentType;
  private final XmlSchemaType _baseType;
  private final QName _baseTypeName;
  private static final IJavaType IXMLSCHEMAENUMVALUE_TYPE = (IJavaType) TypeSystem.get( IXmlSchemaEnumValue.class );  

  public XmlSchemaEnumerationTypeData( String typeName, List<XmlSchemaEnumerationFacet> enumerations, T context, XmlSchemaType baseType, QName baseTypeName, XmlSchemaType parentType ) {
    //noinspection unchecked
    super( parentType.getSchemaIndex() );
    _parentType = parentType;
    _typeName = typeName;
    _enumerations = enumerations;
    _context = context;
    _baseType = baseType;
    _baseTypeName = baseTypeName;
  }

  private String makeUniqueCode( String enumeration, Set<String> usedEnumValues ) {
    enumeration = XmlSchemaIndex.normalizeName( enumeration, XmlSchemaIndex.NormalizationMode.PRESERVECASE );
    enumeration = Character.toUpperCase( enumeration.charAt( 0 ) ) + enumeration.substring( 1 );
    String code = enumeration;
    int suffix = 2;
    while ( ! usedEnumValues.add( code ) ) {
      code = enumeration + suffix++;
    }
    return code;
  }

  public List<IEnumValue> getEnumValues() {
    maybeInit();
    return new ArrayList<IEnumValue>( _enumMapByGosuName.values() ); // Have to clone this list - EnumerationPopupListModel tries to sort this
  }

  public List<String> getEnumConstants() {
    List<String> enumConstants = new ArrayList<String>();
    for( IEnumValue v : getEnumValues() ) {
      enumConstants.add( v.getCode() );
    }
    return enumConstants;
  }

  public void maybeInit() {
    if ( ! _initialized ) {
      TypeSystem.lock();
      try {
        if ( ! _initialized ) {
          LinkedHashMap<IEnumValue, XmlSimpleValue> enumerationSimpleValues = new LinkedHashMap<IEnumValue, XmlSimpleValue>();
          IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(_typeName);
          IEnumType javaEnumType = clazz == null ? null : (IEnumType) TypeSystem.get( clazz );
          int ordinal = 0;
          Set<String> usedEnumValues = new HashSet<String>();
          for ( final XmlSchemaEnumerationFacet enumeration : _enumerations ) {
            XmlSimpleValueFactory valueFactory = getSimpleValueFactory();
            XmlDeserializationContext context = new XmlDeserializationContext( null );
            for ( Map.Entry<String, String> entry : enumeration.getNamespaceContext().entrySet() ) {
              context.addNamespace( entry.getKey(), entry.getValue() );
            }
            XmlSimpleValueBase simpleValue = (XmlSimpleValueBase) valueFactory.deserialize( context, enumeration.getValue(), false );

            if ( _enumMapByXmlSchemaName.containsKey( simpleValue.getGosuValue() ) ) {
              continue;
            }

            final String code = makeUniqueCode( simpleValue.getStringValue( true ), usedEnumValues );

            IEnumValue value;
            if ( javaEnumType != null && clazz.getBackingClass() != null ) {
              value = javaEnumType.getEnumValue( code );
              if ( value instanceof EnumValuePlaceholder ) {
                try {
                  Class enumClass = Class.forName( javaEnumType.getName() );
                  value = new EnumAdapter( Enum.valueOf( enumClass, code ), javaEnumType );
                } catch ( Exception e ) {
                  value = new XmlSchemaEnumValue( this, simpleValue, code, ordinal++ );
                  e.printStackTrace();
                }
              }
            }
            else {
              value = new XmlSchemaEnumValue( this, simpleValue, code, ordinal++ );
            }

            _enumMapByGosuName.put(code, value);
            _enumMapByXmlSchemaName.put( simpleValue.getGosuValue(), value );
            enumerationSimpleValues.put( (IEnumValue) value.getValue(), simpleValue );

          }
          _enumerationSimpleValues = enumerationSimpleValues;
          _initialized = true;
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
  }

//  public void maybeInit() {
//    if (!_initialized) {
//      TypeSystem.lock();
//      try {
//        _enumerationSimpleValues = new LinkedHashMap<IEnumValue, XmlSimpleValue>();
//        IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(_typeName);
//        IEnumType javaEnumType = clazz == null ? null : (IEnumType) TypeSystem.get(clazz);
//        int ordinal = 0;
//        Set<String> usedEnumValues = new HashSet<String>();
//        for (final XmlSchemaEnumerationFacet enumeration : _enumerations) {
//          XmlSimpleValueFactory valueFactory = getSimpleValueFactory();
//          XmlDeserializationContext context = new XmlDeserializationContext(null);
//          for (Map.Entry<String, String> entry : enumeration.getNamespaceContext().entrySet()) {
//            context.addNamespace(entry.getKey(), entry.getValue());
//          }
//          XmlSimpleValueBase simpleValue = (XmlSimpleValueBase) valueFactory.deserialize(context, enumeration.getValue(), false);
//
//          if (_enumMapByXmlSchemaName.containsKey(simpleValue.getGosuValue())) {
//            continue;
//          }
//
//          final String code = makeUniqueCode(simpleValue.getStringValue(true), usedEnumValues);
//
//          IEnumValue value;
//          if (javaEnumType != null) {
//            value = javaEnumType.getEnumValue(code);
//          } else {
//            value = new XmlSchemaEnumValue(this, simpleValue, code, ordinal++);
//          }
//
//        }
//        _initialized = true;
//      } finally {
//        TypeSystem.unlock();
//      }
//    }
//  }

  public XmlSimpleValue getEnumSimpleValue( IEnumValue value ) {
    maybeInit();
    //noinspection RedundantCast
    return _enumerationSimpleValues.get( (IEnumValue) value.getValue() );
  }

  public IEnumValue getEnumValue( String strName ) {
    maybeInit();
    return _enumMapByGosuName.get( strName );
  }

  public Map<IEnumValue, XmlSimpleValue> getEnumSimpleValues() {
    return _enumerationSimpleValues; // added to assist in debugging PL-17888
  }

  public IEnumValue deserialize( XmlSimpleValue value ) {
    maybeInit();
    if (value == null) {
      return null;
    }
    if ( ! value.getGosuValueType().equals( getSimpleValueFactory().getGosuValueType() ) ) {
      // coerce
      value = getSimpleValueFactory().deserialize( value.getStringValue() );
    }
    IEnumValue enumValue = _enumMapByXmlSchemaName.get( value.getGosuValue() );
    if ( enumValue == null ) {
      throw new XmlSimpleValueException( "Enum value not found for type " + this + ": " + value );
    }
    return enumValue;
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
    //noinspection unchecked
    return Arrays.asList( IEnumType.class, IXmlSchemaEnumerationTypeData.class );
  }

  @Override
  public boolean prefixSuperProperties() {
    return false;
  }

  public List<IPropertyInfo> getDeclaredProperties() {
    List<IEnumValue> enumValues = getEnumValues();
    List<IPropertyInfo> props = new ArrayList<IPropertyInfo>( enumValues.size() );
    for ( final IEnumValue value : enumValues ) {
      props.add( new PropertyInfoBuilder()
              .withName( value.getCode() )
              .withType( getType() )
              .withStatic()
              .withAccessor( new IPropertyAccessor() {
        @Override
        public Object getValue( Object ctx ) {
          return value.getValue();
        }

        @Override
        public void setValue( Object ctx, Object value ) {
          throw new UnsupportedOperationException();
        }
      } ).build( this ) );
    }
    props.add( new PropertyInfoBuilder()
            .withName( "SerializedValue" )
            .withType( JavaTypes.STRING() )
            .withWritable( false )
            .withDescription( "Returns the serialized value of this " + getType().getRelativeName() )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                return ( (IEnumValue) ctx ).getValue().toString();
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                throw new UnsupportedOperationException();
              }
            } )
            .build( this )
    );
    props.add( new PropertyInfoBuilder()
            .withName( "GosuValue" )
            .withType( getSimpleValueFactory().getGosuValueType() )
            .withWritable( false )
            .withDescription( "Returns the value of this " + getType().getRelativeName() )
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                //noinspection RedundantCast
                return _enumerationSimpleValues.get( (IEnumValue) ctx ).getGosuValue();
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                throw new UnsupportedOperationException();
              }
            } )
            .build( this )
    );
    return props;
  }

  public List<IMethodInfo> getDeclaredMethods() {
    List<IMethodInfo> methods = new ArrayList<IMethodInfo>();

    methods.add( new MethodInfoBuilder()
            .withStatic()
            .withName( "forGosuValue" )
            .withParameters( new ParameterInfoBuilder().withName( "gosuValue" ).withDescription( "The value of the enumeration" ).withType( getSimpleValueFactory().getGosuValueType() ) )
            .withReturnType( getType() )
            .withCallHandler( new IMethodCallHandler() {
              @Override
              public Object handleCall( Object ctx, Object... args ) {
                IEnumValue value = _enumMapByXmlSchemaName.get( args[0] );
                return value == null ? null : value.getValue();
              }
            } )
            .build( this ) );

    methods.add( new MethodInfoBuilder()
            .withStatic()
            .withName( "valueOf" )
            .withParameters( new ParameterInfoBuilder().withName( "name" ).withDescription( "The name of the enum constant to look up" ).withType( JavaTypes.STRING() ) )
            .withReturnType( getType() )
            .withCallHandler( new IMethodCallHandler() {
              @Override
              public Object handleCall( Object ctx, Object... args ) {
                String name = (String) args[0];
                return _enumMapByGosuName.get( name );
              }
            } )
            .build( this )
    );
    methods.add( new MethodInfoBuilder()
            .withStatic()
            .withName( "values" )
            .withReturnType( getType().getArrayType() )
            .withCallHandler( new IMethodCallHandler() {
              @Override
              public Object handleCall( Object ctx, Object... args ) {
                Collection<IEnumValue> enumValues = _enumMapByXmlSchemaName.values();
                @SuppressWarnings( { "SuspiciousToArrayCall" } )
                IGosuObject[] values = enumValues.toArray( new IGosuObject[enumValues.size()] );
                Object result = getType().makeArrayInstance(values.length);
                for (int i = 0; i < values.length; i++) {
                  IGosuObject value = values[i];
                  getType().setArrayComponent(result, i, value);
                }
                return result;
              }
            } )
            .build( this )
    );
    methods.add( new MethodInfoBuilder()
            .withName( "name" )
            .withReturnType( JavaTypes.STRING() )
            .withCallHandler( new IMethodCallHandler() {
              @Override
              public Object handleCall( Object ctx, Object... args ) {
                IEnumValue enumValue = (IEnumValue) ctx;
                IXmlSchemaEnumValue value = (IXmlSchemaEnumValue) enumValue.getValue();
                return value.getCode();
              }
            } )
            .build( this )
    );
    methods.add( new MethodInfoBuilder()
            .withName( "ordinal" )
            .withReturnType( JavaTypes.pINT() )
            .withCallHandler( new IMethodCallHandler() {
              @Override
              public Object handleCall( Object ctx, Object... args ) {
                IEnumValue enumValue = (IEnumValue) ctx;
                IXmlSchemaEnumValue value = (IXmlSchemaEnumValue) enumValue.getValue();
                return value.getOrdinal();
              }
            } )
            .build( this )
    );
    return methods;
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
    return true;
  }

  @Override
  public IType getSuperType() {
    return JavaTypes.OBJECT();
  }

  @Override
  public String getName() {
    return _typeName;
  }

  @Override
  public XmlSchemaObject getSchemaObject() {
    return _parentType;
  }

  @Override
  public boolean isAnonymous() {
    return false;
  }

  @Override
  public T getContext() {
    return _context;
  }

  @Override
  public List<? extends IType> getInterfaces() {
    return Collections.singletonList( IXMLSCHEMAENUMVALUE_TYPE );
  }

  @Override
  public long getFingerprint() {
    return getSchemaIndex().getFingerprint();
  }

  @Override
  public Class getBackingClass() {
    IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(_typeName);
    if (clazz != null && !(clazz instanceof IAsmJavaClassInfo) && clazz.getBackingClass() != null) {
      return clazz.getBackingClass();
    } else {
      return XmlSchemaEnumValue.class;
    }
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    IJavaClassInfo clazz = getSchemaIndex().getGeneratedClass(_typeName);
    if (clazz != null) {
      return clazz;
    } else {
      return (JavaTypes.getSystemType(XmlSchemaEnumValue.class)).getBackingClassInfo();
    }
  }

  public XmlSimpleValueFactory getSimpleValueFactory() {
    return _simpleValueFactory.get();
  }

  private class EnumAdapter implements IEnumValue, IGosuObject
  {
    private final Enum _enum;
    private final IEnumType javaEnumType;

    public EnumAdapter(Enum e, IEnumType javaEnumType)
    {
      _enum = e;
      this.javaEnumType = javaEnumType;
    }

    public String getCode()
    {
      return _enum.name();
    }

    public Object getValue()
    {
      return _enum;
    }

    public int getOrdinal()
    {
      return _enum.ordinal();
    }

    public String getDisplayName()
    {
      return getCode();
    }

    public IType getIntrinsicType()
    {
      return TypeSystem.getOrCreateTypeReference( javaEnumType );
    }
  }


}
