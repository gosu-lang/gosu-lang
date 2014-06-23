/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.fs.IFile;
import gw.internal.xml.ws.WsdlSoapHeaders;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlTypeData;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;
import gw.internal.xml.xsd.typeprovider.schema.WsdlService;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.lang.reflect.*;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlElement;

import java.util.*;

// Intentionally not named WsdlSoapHeadersTypeData to avoid the ClassName/ClassNameTypeData pattern (explicit type data)
public class WsdlSoapHeadersTypeDataClass extends XmlTypeData implements IWsdlSoapHeadersTypeData {

  private final String _typeName;
  private WsdlPort _preferredPort;
  private final List<XmlSchemaElement> _headerElements;

  public WsdlSoapHeadersTypeDataClass( String typeName, WsdlPort preferredPort, Wsdl wsdl, WsdlService service, IFile resourceFile, boolean service1, List<XmlSchemaElement> headerElements ) {
    _typeName = typeName;
    _preferredPort = preferredPort;
    _headerElements = headerElements;
  }

  @Override
  public String getName() {
    return _typeName;
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    List<IPropertyInfo> ret = new ArrayList<IPropertyInfo>();
    Set<String> usedPropertyNames = new HashSet<String>();
    for ( XmlSchemaElement headerElement : _headerElements ) {
      final String propertyName = XmlSchemaIndex.makeUniquePropertyName( usedPropertyNames, headerElement.getName(), XmlSchemaIndex.NormalizationMode.PROPERCASE );
      ret.add( new PropertyInfoBuilder()
              .withName( propertyName )
              .withType( XmlSchemaIndex.getGosuTypeBySchemaObject( headerElement ) )
              .withReadable( true )
              .withWritable( true )
              .withAccessor( new IPropertyAccessor() {
                @Override
                public Object getValue( Object ctx ) {
                  WsdlSoapHeaders soapHeaders = (WsdlSoapHeaders) ctx;
                  return soapHeaders.get( propertyName );
                }

                @Override
                public void setValue( Object ctx, Object value ) {
                  WsdlSoapHeaders soapHeaders = (WsdlSoapHeaders) ctx;
                  soapHeaders.set( propertyName, (XmlElement) value );
                }
              } ).build( this ) );
    }
    return ret;
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return Collections.singletonList(
            new ConstructorInfoBuilder()
                    .withConstructorHandler( new IConstructorHandler() {
                      @Override
                      public Object newInstance( Object... args ) {
                        return new WsdlSoapHeaders( getType() );
                      }
                    } )
                    .build( this )
    );
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
    return true;
  }

  @Override
  public long getFingerprint() {
    return 0; // TODO
  }

  @Override
  public Class getBackingClass() {
    return WsdlSoapHeaders.class;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return JavaTypes.getSystemType(WsdlSoapHeaders.class).getBackingClassInfo();
  }

  @Override
  public XmlSchemaIndex<?> getSchemaIndex() {
    return _preferredPort.getSchemaIndex();
  }

}
