/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Schema;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ExplicitGroup_Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.Marshaller;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.WsiAdditions;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.concurrent.LocklessLazyVar;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ClassBasedMarshalInfo extends MarshalInfo implements AdditionalCheckType {
  static final LocklessLazyVar<URI> GW_XSD_NAMESPACE = new LocklessLazyVar<URI>() {
    @Override
    protected URI init() {
      try {
        return new URI("http://guidewire.com/xsd");
      } catch (URISyntaxException e) {
        throw new RuntimeException("could not create a URI", e);
      }

    }
  };
  protected final IType _type;
  protected boolean _isComponent;

  public ClassBasedMarshalInfo(IType type, boolean isComponent) {
    super(type);
    _isComponent = isComponent;
    _type = type;
  }

  static boolean isExportable(IType type) {
    return RemotableMarshalInfo._gwRemotableType.get().isAssignableFrom(type);
  }

  @Override
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    // TODO required when added xml elements and/or types
    return new HashMap<String, Set<XmlSchemaAccess>>();
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    ComplexType complexType = createInfo.getComplexTypeIfNeededFor(_type);
    if (complexType != null) {
      Sequence sequence = new Sequence();
      complexType.setSequence$(sequence);
      boolean addedGWXsd = false;
      for (Map.Entry<String,IPropertyInfo> prop : getProperties(_type).entrySet()) {
        ExplicitGroup_Element typeEl = new ExplicitGroup_Element();
        String name = prop.getKey();
        typeEl.setName$(name);
        final IType featureType = prop.getValue().getFeatureType();
        if (!addedGWXsd && JavaTypes.DATE().isAssignableFrom(featureType)) {
          addGWNamespaceIfNotAlreadyPresent(createInfo.getSchemaFor(WsiAdditions.getInstance().getTargetNamespace(_type)));
          addedGWXsd = true;
        }
        XmlMarshaller.addType(featureType, typeEl.getTypeInstance(), createInfo );
        if (featureType.isPrimitive()) {
          typeEl.setMinOccurs$( BigInteger.ZERO );
        }
        sequence.Element().add( typeEl );
      }
    }
    element.setType$( createInfo.getQName(_type) );
  }

  protected abstract Map<String,IPropertyInfo> getProperties(IType type);

  public static void addGWNamespaceIfNotAlreadyPresent(Schema schema) {
    for (Pair<String, URI> entry : schema.getDeclaredNamespaces()) {
       if (entry.getSecond().equals(GW_XSD_NAMESPACE.get())) {
         return;
       }
    }
    schema.declareNamespace(GW_XSD_NAMESPACE.get(), "gw");
  }

  @Override
  public Object unmarshal( XmlElement componentElement, UnmarshalContext context ) {
    URI nsURI;
    try {
      nsURI = new URI( WsiAdditions.getInstance().getTargetNamespace( _type ) );
    }
    catch ( URISyntaxException e ) {
      throw GosuExceptionUtil.forceThrow(e);
    }
    Object obj = null;
    if (componentElement != null) {
      obj = _type.getTypeInfo().getConstructor().getConstructor().newInstance();
      for (Map.Entry<String,IPropertyInfo> prop : getProperties(_type).entrySet()) {
        XmlElement child = componentElement.getChild(new QName(nsURI.toString(), prop.getKey()));
        if (child != null) {
          Object childObj = XmlServices.unmarshal(prop.getValue().getFeatureType(), child, context);
          if (childObj != null) {
            prop.getValue().getAccessor().setValue(obj, childObj);
          }
        }
      }
    }
    return obj;
  }

  @Override
  public void marshal(XmlElement returnEl, IType type, Object obj, MarshalContext context) {
    if (obj != null && context.getSeen().contains(obj)) {
      throw new RuntimeException("Recursion on " + obj);
    }
    context.getSeen().add(obj);
    URI nsURI;
    try {
      nsURI = new URI( WsiAdditions.getInstance().getTargetNamespace( type ) );
      returnEl.declareNamespace(nsURI, "pogo");
    } catch (URISyntaxException e) {
      throw new RuntimeException("on " + returnEl, e);
    }
    for (Map.Entry<String,IPropertyInfo> prop : getProperties(_type).entrySet()) {
      Object childObj = prop.getValue().getAccessor().getValue(obj);
      if (childObj != null) {
        XmlElement childEl = new XmlElement(new QName(nsURI.toString(), prop.getKey()));
        returnEl.addChild(childEl);
        XmlServices.marshal(childEl, prop.getValue().getFeatureType(), childObj, context);
      }
    }
    context.getSeen().remove(obj);
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
    else if (marshalInfo instanceof RemotableMarshalInfo) {
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

}
