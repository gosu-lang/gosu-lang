/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.xml.XmlElement;

public class XmlElementWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  private final XmlSchemaPropertySpec _childPropertySpec;

  public XmlElementWsdlOperationParameterInfo( XmlSchemaPropertySpec parameterPropertySpec, XmlSchemaPropertySpec childPropertySpec ) {
    super( parameterPropertySpec.getElementPropertyGosuType(), parameterPropertySpec.getQName() );
    _childPropertySpec = childPropertySpec;
  }

  @Override
  public IType getType() {
    return _childPropertySpec == null ? TypeSystem.get( XmlElement.class ) : _childPropertySpec.getElementPropertyGosuType( _childPropertySpec.isPlural() );
  }

  @Override
  public Object unwrap( XmlElement node ) {
    return node.getChildren().get( 0 );
  }

  @Override
  public String getName() {
    return getParameterElementName().getLocalPart();
  }

  @Override
  public XmlElement wrap( Object obj, XmlElement componentElement ) {
    componentElement.addChild( (XmlElement) obj );
    return componentElement;
  }

}
