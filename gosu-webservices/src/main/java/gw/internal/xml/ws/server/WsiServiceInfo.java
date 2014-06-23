/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.internal.schema.gw.xsd.gw.gw_schema_additions.attributes.Deprecated;
import gw.internal.schema.gw.xsd.w3c.wsdl.Definitions;
import gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Import;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Schema;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.enums.FormChoice;
import gw.internal.xml.ws.WsiAdditions;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.ws.server.marshal.XmlMarshaller;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IExceptionInfo;
import gw.xml.XmlElement;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.xml.namespace.QName;

public class WsiServiceInfo {

  private final Definitions _wsdl;
  private final Schema _schema;
  private final String _xsdRootURL;
  private final List<XmlSchemaIndex> _schemas;
  private final Map<String,Schema> _targetnamespaceToSchema = new HashMap<String,Schema>();
  private final Map<IType, QName> _seenTypes = new HashMap<IType, QName>();
  private final Map<QName, Map<QName, MarshalInfo>> _marshalInfoMap = new HashMap<QName, Map<QName, MarshalInfo>>();
  private final Map<QName, IMethodInfo> _originalMethods = new HashMap<QName, IMethodInfo>();
  private final IType _webserviceType;
  private final Set<IType> _exposeEnumAsStringTypes;
  private Set<IType> _thrownExceptions = new HashSet<IType>();

  public WsiServiceInfo( Definitions wsdl, Schema schema, String xsdRootURL, IType webserviceType, Set<IType> exposeEnumAsStringTypes ) {
    _wsdl = wsdl;
    _schema = schema;
    _xsdRootURL = xsdRootURL;
    _schemas = new ArrayList<XmlSchemaIndex>();
    _webserviceType = webserviceType;
    _exposeEnumAsStringTypes = exposeEnumAsStringTypes;
    for (IMethodInfo m : _webserviceType.getTypeInfo().getMethods()) {
      if (m.isPublic() && !m.isStatic()) {
        for ( IExceptionInfo exceptionInfo : m.getExceptions() ) {
          _thrownExceptions.add( exceptionInfo.getExceptionType() );
        }
        for ( IType exceptionType : WsiUtilities.EXCEPTIONS_THROWN_BY_INFRASTRUCTURE.get() ) {
          _thrownExceptions.add( exceptionType );
        }
      }
    }
  }

  public Definitions getWsdl() {
    return _wsdl;
  }

  public Schema getSchema() {
    return _schema;
  }

  public String getXsdRootURL() {
    return _xsdRootURL;
  }

  public Set<IType> getThrownExceptions() {
    return _thrownExceptions;
  }

  public ComplexType getComplexTypeIfNeededFor(IType type) {
    if ( _seenTypes.containsKey( type ) ) {
      return null;
    }
    Schema schema = getSchemaFor( WsiAdditions.getInstance().getTargetNamespace( type ) );
    URI uri = schema.TargetNamespace();
    _seenTypes.put( type, new QName( uri.toString(), type.getRelativeName() ) );
    ComplexType complexType = new ComplexType();
    complexType.setName$( type.getRelativeName() );
    if (type.getTypeInfo().isDeprecated()) {
      gw.internal.schema.gw.xsd.gw.gw_schema_additions.attributes.Deprecated.set( complexType, type.getTypeInfo().getDeprecatedReason() );
    }
    schema.ComplexType().add(complexType);
    return complexType;
  }

  public SimpleType getSimpleTypeIfNeededFor(IType type) {
    if ( _seenTypes.containsKey( type ) ) {
      return null;
    }
    Schema schema = getSchemaFor( WsiAdditions.getInstance().getTargetNamespace( type ) );
    URI uri = schema.TargetNamespace();
    _seenTypes.put( type, new QName( uri.toString(), type.getRelativeName() ) );
    SimpleType simpleType = new SimpleType();
    simpleType.setName$( type.getRelativeName() );
    if (type.getTypeInfo().isDeprecated()) {
      Deprecated.set( simpleType, type.getTypeInfo().getDeprecatedReason() );
    }
    schema.SimpleType().add(simpleType);
    return simpleType;
  }

  public Schema getSchemaFor( String namespace ) {
    Schema schema = _targetnamespaceToSchema.get(namespace);
    if (schema == null) {
      try {
        schema = new Schema();
        URI nsURI = new URI(namespace);
        schema.setTargetNamespace$(nsURI);
        _wsdl.declareNamespace(nsURI, "pogo");
        boolean skippedWsdlSchema = false;
        for ( XmlElement other : _wsdl.Types().get(0).getChildren()) {
          Import importEl = new Import();
          importEl.setNamespace$(nsURI);
          Schema otherSchema = (Schema) other;
          otherSchema.getChildren().add( 0, importEl );
          if (!skippedWsdlSchema) {
           skippedWsdlSchema = true;
          }
          else {
            Import reverseImportEl = new Import();
            reverseImportEl.setNamespace$(otherSchema.TargetNamespace());
            schema.getChildren().add( 0, reverseImportEl );
          }
        }
        schema.setElementFormDefault$( FormChoice.Qualified );
        schema.setComplexType$(new ArrayList<ComplexType>());
        _wsdl.Types().get( 0 ).addChild( schema );
        _targetnamespaceToSchema.put(namespace, schema);
      } catch ( URISyntaxException e) {
        throw new RuntimeException("Should not have happended", e);
      }
    }
    return schema;
  }

  public QName getQName(IType type) {
    return _seenTypes.get( type );
  }

  public List<XmlSchemaIndex> getSchemas() {
    return _schemas;
  }

  public Map<QName, Map<QName, MarshalInfo>> getMarshalInfoMap() {
    return _marshalInfoMap;
  }

  public Map<QName, IMethodInfo> getOriginalMethods() {
    return _originalMethods;
  }

  public IType getWebserviceType() {
    return _webserviceType;
  }

  public Set<IType> getExposeEnumAsStringTypes() {
    return _exposeEnumAsStringTypes;
  }
}
