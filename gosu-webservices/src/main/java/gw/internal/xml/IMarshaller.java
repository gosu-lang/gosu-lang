/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.config.IService;
import gw.internal.xml.ws.server.marshal.MarshalContext;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.ws.server.marshal.UnmarshalContext;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;

import java.util.Map;

/**
 * This is an interface that presents the marshaller in the API
 *
 * @author dandrews
 */
public interface IMarshaller extends IService {
  final static ResourceKey ERR_CAN_NOT_MARSHAL_TYPE = Res.WS_ERR_Can_Not_Marshal;
  final static ResourceKey ERR_DUPLICATE_NAMESPACE = Res.WS_ERR_Annotation_Duplicate_Namespace;
  final static ResourceKey ERR_INVALID_NAMESPACE = Res.WS_ERR_Annotation_Invalid_Namespace;
  final static ResourceKey WARN_SOME_WSDL2JAVA_DONT_SUPPORT_SCHEMA = Res.WS_WARN_Annontation_Some_Generators_Dont_Support_Schema;

  /** this will check that the supplied type is valid, if it is not valid it will format a exception entry on the
   * parsedElement.
   *
   * @param parsedElement the element to attach warnings and errors to.
   * @param name the parameter name or "return"
   * @param type the type of the parameter or return value
   * @param seenNamespaces a map of namespaces already seen, a caller should treat this as opaque and ignore
   * @return This will return the marshalInfo if this is valid
   */
  MarshalInfo checkType(IParsedElement parsedElement, String name, IType type, Map<String, Object> seenNamespaces);

  /** this will create the target namespace for this type
   *
   *
   * @param prefix
   * @param type to create a target namespace for
   * @return the uri for this type
   */
  String createTargetNamespace( String prefix, IType type );

  /** this will create the target namespace for this path.  Note that the caller is responsible for insuring
   * no namespace collisions.
   *
   *
   * @param prefix
   * @param path to create a target namespace for
   * @return the uri for this type
   */
  String createTargetNamespace( String prefix, String path );

  /** This will create elements for the type in the target.
   *
   * @param parameterElement where to write the value
   * @param type the expected type of the object (e.g., may be an interface)
   * @param obj the actual object
   * @param context the context for this marshalling
   */
  void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context);

  /** This will create an object from the xml element.  There are several marshalling
   * attributes that will affect this behavior.
   *
   * @param type the type desired
   * @param parameterElement the element to unmarshal
   * @param context the context for this unmarshalling
   * @return the create and populated object
   * @throws Exception on error
   */
  Object unmarshal(IType type, XmlElement parameterElement, UnmarshalContext context);


}
