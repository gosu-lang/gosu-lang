/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSchemaAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlSchemaAccessTypeData<T> extends XmlSchemaTypeData<T> implements IXmlSchemaTypeData<T> {

  private final String _typeName;
  private final String _schemaSchemaTypeName;
  private final T _context;

  public XmlSchemaAccessTypeData( String typeName, String schemaSchemaTypeName, T context, XmlSchemaIndex<T> schemaIndex ) {
    super( schemaIndex );
    _typeName = typeName;
    _schemaSchemaTypeName = schemaSchemaTypeName;
    _context = context;
  }

  @Override
  public String getName() {
    return _typeName;
  }

  @Override
  public XmlSchemaObject getSchemaObject() {
    return null;
  }

  @Override
  public boolean isAnonymous() {
    return false;
  }

  public List<IPropertyInfo> getDeclaredProperties() {
    List<IPropertyInfo> props = new ArrayList<IPropertyInfo>( 1 );
    final IType schemaAccessType = TypeSystem.getByFullName( "gw.xml.XmlSchemaAccess" ).getParameterizedType( TypeSystem.getByFullName( _schemaSchemaTypeName ) );
    props.add( new PropertyInfoBuilder()
            .withName( "SchemaAccess" )
            .withType( schemaAccessType )
            .withDescription( "An object that allows programmatic reflective access to the loaded schema" )
            .withStatic()
            .withAccessor( new IPropertyAccessor() {
              @Override
              public Object getValue( Object ctx ) {
                return getSchemaIndex().getXmlSchemaAccess();
                //return TypeSystem.getByFullName( _schemaSchemaTypeName ).getTypeInfo().getMethod( "parse", TypeSystem.get( InputStream.class ) ).getCallHandler().handleCall( null, _xmlSchemaSource.getInputStream() );
              }

              @Override
              public void setValue( Object ctx, Object value ) {
                throw new UnsupportedOperationException();
              }
            } )
            .build( this ) );
    return props;
  }

  @Override
  public List<IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  @Override
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
  public boolean prefixSuperProperties() {
    return false;
  }

  @Override
  public long getFingerprint() {
    return getSchemaIndex().getFingerprint();
  }

  @Override
  public Class getBackingClass() {
    return XmlSchemaAccess.class;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return ((IJavaType)JavaTypes.getSystemType(XmlSchemaAccess.class)).getBackingClassInfo();
  }

  public T getContext() {
    return _context;
  }

  @Override
  public void maybeInit() {
    // nothing to do
  }

}
