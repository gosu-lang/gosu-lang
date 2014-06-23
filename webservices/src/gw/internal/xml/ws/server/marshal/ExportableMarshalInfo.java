/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.xml.Marshaller;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.WsiAdditions;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import gw.util.GosuExceptionUtil;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.xml.namespace.QName;


public class ExportableMarshalInfo extends ClassBasedMarshalInfo {

  public static final LockingLazyVar<IType> _exportAnnoType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiExportable" );
    }
  };

  public ExportableMarshalInfo( IType type, boolean isComponent ) {
    super( type, isComponent );
  }

  static boolean isExportable(IType type) {
    List<IAnnotationInfo> annotation = type.getTypeInfo().getAnnotationsOfType(_exportAnnoType.get());
    return annotation != null && annotation.size() > 0;
  }

    @Override
  public void checkType(Marshaller marshaller, IParsedElement parsedElement, String label, IType type, Map<String, Object> seenNamespaces) {
    if (!type.isValid() && !type.equals(_type)) {
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, type.getDisplayName(), label );
    }
    else {
      for (Map.Entry<String,IPropertyInfo> prop : getProperties(type).entrySet()) {
        String newLabel = label + (label.equals("") ? "" : ".")  + prop.getKey();
        IType partType = prop.getValue().getFeatureType();
        if (!_type.equals(partType) && isExportable(partType)) {
          // do not recurse
        }
        else if (_type.equals(getComponentType(partType))) { // test this before recursing into checkType
          parsedElement.addParseWarning(Res.WS_ERR_Export_Recursive, newLabel );
        }
        else {
          MarshalInfo marshalInfo = marshaller.checkType(parsedElement, newLabel, prop.getValue().getFeatureType(), seenNamespaces);
          checkMarshalInfoType(parsedElement, prop, newLabel, marshalInfo);
        }
      }
    }
  }

  private IType getComponentType(IType partType) {
    if ( partType.isArray()) {
      return getComponentType(partType.getComponentType());
    }
    if ( JavaTypes.LIST().isAssignableFrom( partType )) {
      IType[] parameters = partType.getTypeParameters();
      return parameters.length != 1 ? partType : getComponentType(parameters[0]);
    }
    return partType;
  }

  private void checkMarshalInfoType(IParsedElement parsedElement, Map.Entry<String, IPropertyInfo> prop, String newLabel, MarshalInfo marshalInfo) {
    if (marshalInfo instanceof SimpleValueMarshalInfo) {
    }
    else if (marshalInfo instanceof EnumMarshalInfo) {
    }
    else if (marshalInfo instanceof ExportableMarshalInfo) {
    }
    else if (marshalInfo instanceof ArrayMarshalInfo) {
      ArrayMarshalInfo ami = (ArrayMarshalInfo) marshalInfo;
      MarshalInfo componentMI = ami.getComponentMarshalInfo();
      checkMarshalInfoType(parsedElement, prop, newLabel + "[]", componentMI);
    }
    else if (marshalInfo instanceof ListMarshalInfo) {
      ListMarshalInfo lmi = (ListMarshalInfo) marshalInfo;
      MarshalInfo componentMI = lmi.getComponentMarshalInfo();
      checkMarshalInfoType(parsedElement, prop, newLabel + "[]", componentMI);
    }
    else if (marshalInfo instanceof XmlElementMarshalInfo) {
      // TODO when you change this you need to also deal with seen type names in getAllSchemas()
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, prop.getValue().getDisplayName(), newLabel );
    }
    else if (marshalInfo instanceof XmlTypeInstanceMarshalInfo) {
      // TODO when you change this need to also deal with seen type names in getAllSchemas()
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, prop.getValue().getDisplayName(), newLabel );
    }
    else if (marshalInfo != null) {
      // These other types should just be supported
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, prop.getValue().getDisplayName(), newLabel );
    }
  }

  protected Map<String,IPropertyInfo> getProperties(IType type) {
    Map<String, IPropertyInfo> rtn = new TreeMap<String,IPropertyInfo>();
    ITypeInfo typeInfo = type.getTypeInfo();
    if (typeInfo instanceof IRelativeTypeInfo) {
      IRelativeTypeInfo pogoTI =  ((IRelativeTypeInfo) typeInfo);
      for (IPropertyInfo property : pogoTI.getDeclaredProperties()) {
        if (property.isPublic() && property.isWritable() && property.isReadable()) {
          rtn.put(property.getName(), property);
        }
      }
    }
    return rtn;
  }

}
