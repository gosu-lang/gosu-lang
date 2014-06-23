/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.config.BaseService;
import gw.internal.xml.ws.server.marshal.AdditionalCheckType;
import gw.internal.xml.ws.server.marshal.MarshalContext;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.ws.server.marshal.UnmarshalContext;
import gw.internal.xml.ws.server.marshal.XmlMarshaller;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.util.Map;

import javax.xml.XMLConstants;
import java.util.Set;

/**
 * This is what the marshaller does, marshal, unmarshal, valid types, create schemas</desc>
 *
 * @author dandrews
 */
public class Marshaller extends BaseService implements IMarshaller {

  public MarshalInfo checkType(IParsedElement parsedElement, String label, IType type, Map<String, Object> seenNamespaces) {
    final MarshalInfo marshalInfo = XmlMarshaller.getMarshalInfo( type, null );
    if (marshalInfo == null) {
      parsedElement.addParseException(ERR_CAN_NOT_MARSHAL_TYPE, type.getDisplayName(), label );
    }
    else {
      if (marshalInfo instanceof AdditionalCheckType) {
         ((AdditionalCheckType)marshalInfo).checkType(this, parsedElement, label, type, seenNamespaces);
      }
      Map<String,Set<XmlSchemaAccess>> schemaAccesses = marshalInfo.getAllSchemas();
      if (schemaAccesses != null) {
        // print("checkType for ${type} schemaAccesses=${schemaAccesses}")
        for (Map.Entry<String, Set<XmlSchemaAccess>> entry : schemaAccesses.entrySet()) {
          String uri = entry.getKey();
          if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri)) {
             parsedElement.addParseWarning(WARN_SOME_WSDL2JAVA_DONT_SUPPORT_SCHEMA, label, uri);
          }
          else {
            for ( XmlSchemaAccess xmlSchemaAccess : entry.getValue() ) {
              XmlSchemaAccessImpl xmlSchemaAccessImpl = (XmlSchemaAccessImpl) xmlSchemaAccess;
              if (xmlSchemaAccessImpl.getSchemaIndex().getPackageName().startsWith("wsi.local.")) {
                parsedElement.addParseException(ERR_CAN_NOT_MARSHAL_TYPE, label, type.getDisplayName());
              }
              else {
                XmlSchemaAccess found = (XmlSchemaAccess) seenNamespaces.get(uri);
                if (found != null) {
                  if (!found.equals(xmlSchemaAccessImpl)) {
                   parsedElement.addParseException(ERR_DUPLICATE_NAMESPACE, label, uri, found, type.getDisplayName());
                  }
                }
                else {
                  seenNamespaces.put(uri, xmlSchemaAccessImpl);
                }
              }
            }
          }
        }
      }
    }
    return marshalInfo;
  }

  @Override
  public String createTargetNamespace( String prefix, IType type ) {
    return XmlMarshaller.createTargetNamespace( prefix, type);
  }

  @Override
  public String createTargetNamespace( String prefix, String path ) {
    return XmlMarshaller.createTargetNamespace( prefix, path );
  }

  /** This will create elements for the type in the target.
   *
   * @param parameterElement where to write the value
   * @param type the expected type of the object (e.g., may be an interface)
   * @param obj the actual object
   * @param context the context for this marshalling
   */
  public void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    MarshalInfo marshalInfo = XmlMarshaller.getMarshalInfo( type, null );
    if ( marshalInfo == null ) {
      throw new IllegalArgumentException( "Marshal/unmarshal of " + type.getName() + " is not supported" );
    }
    else if (!type.isValid()) {
      throw new RuntimeException("Type " + type + " is not valid");
    }
    marshalInfo.marshal( parameterElement, type, obj, context);
  }

  /** This will create an object from the xml element.  There are several marshalling
   * attributes that will affect this behavior.
   *
   * @param type the type desired
   * @param parameterElement the element to unmarshal
   * @param context the context for this unmarshalling
   * @return the create and populated object
   */
  public Object unmarshal(IType type, XmlElement parameterElement, UnmarshalContext context) {
    if ( parameterElement == null ) {
      // null
      return null;
    }
    else {
      // not null
      MarshalInfo marshalInfo = XmlMarshaller.getMarshalInfo( type, null );
      if (marshalInfo == null) {
        throw new RuntimeException("Could not get a marshal for " + type);
      }
      else if (!type.isValid()) {
        throw new RuntimeException("Type " + type + " is not valid");
      }
      else {
        return marshalInfo.unmarshal( parameterElement, context );
      }
    }
  }

}
