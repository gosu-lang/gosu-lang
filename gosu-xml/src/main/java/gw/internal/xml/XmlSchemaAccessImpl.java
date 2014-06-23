/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.XmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.internal.xml.xsd.XmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;

import gw.xml.XmlElement;
import gw.xml.XmlParseOptions;
import gw.xml.XmlSchemaAccess;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;

public class XmlSchemaAccessImpl<T extends XmlElement> extends XmlSchemaAccess<T> implements IGosuObject {

  private final IType _schemaSchemaType;
  private final XmlSchemaSource _source;
  private final XmlSchemaIndex<?> _schemaIndex;

  /**
   * Creates a new schema access object.
   *
   * @param schemaSchemaType the schema type
   * @param source the schema source
   * @param schemaIndex the schema index
   * @throws URISyntaxException if there are problemms getting the schema
   */
  public XmlSchemaAccessImpl( IType schemaSchemaType, XmlSchemaSource source, XmlSchemaIndex schemaIndex ) throws URISyntaxException {
    _schemaSchemaType = schemaSchemaType;
    _source = source;
    _schemaIndex = schemaIndex;
  }

  /**
   * Returns the gw.xsd.w3c.xmlschema.Schema for this schema access.
   *
   * @return the schema
   */
  @Override
  public T getSchema() {
    XmlParseOptions options = new XmlParseOptions();
    XmlSchemaIndex<?> soap12Schema = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getSchemaIndex().getTypeLoader().getModule(), "gw.xsd.w3c.soap12" );
    if ( soap12Schema == null ) {
      options.setAdditionalSchemas( Arrays.<XmlSchemaAccess>asList(
              XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getSchemaIndex().getTypeLoader().getModule(), "gw.xsd.w3c.xmlschema" ).getXmlSchemaAccess()
              ) );
    }
    else {
      XmlSchemaIndex<?> soap11Schema = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getSchemaIndex().getTypeLoader().getModule(), "gw.xsd.w3c.soap11" );
      options.setAdditionalSchemas( Arrays.<XmlSchemaAccess>asList(
              XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getSchemaIndex().getTypeLoader().getModule(), "gw.xsd.w3c.xmlschema" ).getXmlSchemaAccess(),
              soap12Schema.getXmlSchemaAccess(),
              soap11Schema.getXmlSchemaAccess()
              ) );
    }
    //noinspection unchecked
    return (T) _schemaSchemaType.getTypeInfo().getMethod( "parse", TypeSystem.get( InputStream.class ), TypeSystem.get( XmlParseOptions.class ) ).getCallHandler().handleCall( null, _source.getInputStream( false ), options );
  }

//  /** Will get the imports
//   *
//   * @return the imports
//   * @throws URISyntaxException on errors
//   */
//  public Map<URI, XmlSchemaAccessImpl> getImports() throws URISyntaxException {
//    LinkedHashMap<URI, XmlSchemaAccessImpl> ret = new LinkedHashMap<URI, XmlSchemaAccessImpl>();
//    for ( Map.Entry<String,String> entry : _schemaIndex.getGosuNamespacesByXmlNamespace().entrySet() ) {
//      String gosuNamespace = entry.getValue();
//      if ( gosuNamespace.equals( _schemaIndex.getPackageName() ) ) {
//        continue;
//      }
//      XmlSchemaIndex targetSchema = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( gosuNamespace );
//      if ( targetSchema == null ) {
//        throw new RuntimeException( "Schema not found for Gosu namespace: ${gosuNamespace}" );
//      }
//      ret.put( new URI( entry.getKey() ), targetSchema.getXmlSchemaAccess() );
//    }
//    return ret;
//  }

  @Override
  public IType getIntrinsicType() {
    return TypeSystem.get( XmlSchemaAccessImpl.class ).getParameterizedType( _schemaSchemaType );
  }

  /** the backing index's package name.
   *
   * @return a string
   */
  @Override
  public String toString() {
    return _schemaIndex.getPackageName();
  }

  /**
   * Returns the schema index for this schema.
   * @return the schema index for this schema
   */
  public XmlSchemaIndex<?> getSchemaIndex() {
    return _schemaIndex;
  }

}
