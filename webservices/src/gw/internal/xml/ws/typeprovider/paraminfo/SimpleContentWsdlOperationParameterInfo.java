/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.xml.XmlElement;

public class SimpleContentWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  private final XmlSchemaPropertySpec _parameterPropertySpec;
  private final boolean _isComponent;
  private final boolean _primitiveOk;

  public SimpleContentWsdlOperationParameterInfo( XmlSchemaPropertySpec parameterPropertySpec, boolean isComponent, boolean primitiveOk ) {
    super( parameterPropertySpec.getElementPropertyGosuType(), parameterPropertySpec.getQName() );
    _parameterPropertySpec = parameterPropertySpec;
    _isComponent = isComponent;
    _primitiveOk = primitiveOk;
  }

  @Override
  public IType getType() {
    XmlSchemaElement element = (XmlSchemaElement) _parameterPropertySpec.getXmlSchemaObject();
    if ( _primitiveOk ) {
      boolean primitive;
      if ( _isComponent && ! element.isNillable() ) {
        primitive = true;
      }
      else //noinspection RedundantIfStatement
        if ( ! _isComponent && element.getMinOccurs() == 1 && element.getMaxOccurs() == 1 ) {
        primitive = true;
      }
      else {
        primitive = false;
      }
      if ( primitive ) {
        IType type = _parameterPropertySpec.getSimpleTypePropertyGosuType();
        type = TypeSystem.getPrimitiveType( type );
        if ( type != null ) {
          return type;
        }
      }
    }
    return _parameterPropertySpec.getSimpleTypePropertyGosuType( false );
  }

  // input: the element containing the value
  // output: the value extracted from the supplied element
  // parameterElement should never be null
  @Override
  public Object unwrap( XmlElement parameterElement ) {
    return parameterElement.getIntrinsicType().getTypeInfo().getProperty( "$Value" ).getAccessor().getValue( parameterElement );
  }

  @Override
  public String getName() {
    return _parameterPropertySpec.getQName().getLocalPart();
  }

  // input: the value and the element to set the value into
  // output: set up the supplied element to contain the value
  // value should never be null
  @Override
  public XmlElement wrap( Object value, XmlElement componentElement ) {
    IType type = _parameterPropertySpec.getElementPropertyGosuType();
    ITypeInfo typeInfo = type.getTypeInfo();
    typeInfo.getProperty( "$Value" ).getAccessor().setValue( componentElement, value );
    return componentElement;
  }

}
