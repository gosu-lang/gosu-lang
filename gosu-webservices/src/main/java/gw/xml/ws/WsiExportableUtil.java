/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.internal.xml.ws.server.marshal.ExportableMarshalInfo;
import gw.internal.xml.ws.server.marshal.MarshalContext;
import gw.internal.xml.ws.server.marshal.UnmarshalContext;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.xml.XmlElement;

/** this class has methods to work with WsiExportable classes.
 */
public class WsiExportableUtil {
  private WsiExportableUtil() {}

  /** This will marshal the object, if exportable, into xml
   *
   * @param el the el to contain the object
   * @param obj the object
   */
  public static void marshal(XmlElement el, Object obj)  {
    if (obj == null) {
      throw new IllegalArgumentException("Object required");
    }
    final IType type = TypeSystem.getTypeFromObject(obj);
    if (!type.getTypeInfo().hasAnnotation(ExportableMarshalInfo._exportAnnoType.get())) {
      throw new IllegalArgumentException(type.getRelativeName() + " is not exportable");
    }
    gw.internal.xml.config.XmlServices.marshal(el, type, obj, new MarshalContext());
  }

  /** This will unmarshal the element, provided it is exportable
   *
   * @param el the element to unmarshal
   * @param type the expected type
   * @return the value
   */
  public static Object unmarshal(XmlElement el, IType type)  {
    if (!type.getTypeInfo().hasAnnotation(ExportableMarshalInfo._exportAnnoType.get())) {
      throw new IllegalArgumentException(type.getRelativeName() + " is not exportable");
    }
    return gw.internal.xml.config.XmlServices.unmarshal(type, el, new UnmarshalContext(null));
  }

}
