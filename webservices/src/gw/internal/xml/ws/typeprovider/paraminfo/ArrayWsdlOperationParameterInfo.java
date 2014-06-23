/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.lang.reflect.IType;
import gw.xml.XmlElement;

import java.util.List;

import javax.xml.namespace.QName;

public class ArrayWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  private final WsdlOperationParameterInfo _componentParamInfo;

  public ArrayWsdlOperationParameterInfo( IType elementType, QName elementQName, WsdlOperationParameterInfo componentParamInfo ) {
    super( elementType, elementQName );
    _componentParamInfo = componentParamInfo;
  }

  @Override
  public IType getType() {
    return _componentParamInfo.getType().getArrayType();
  }

  @Override
  public Object unwrap( XmlElement parameterElement ) {
    int idx = 0;
    QName componentQName = _componentParamInfo.getParameterElementName();
    final List<XmlElement> children = parameterElement.getChildren( componentQName );
    Object ret = _componentParamInfo.getType().makeArrayInstance( children.size() );
    for ( XmlElement childElement : children ) {
      if ( childElement.isNil() ) {
        _componentParamInfo.getType().setArrayComponent( ret, idx++, null );
      }
      else {
        _componentParamInfo.getType().setArrayComponent( ret, idx++, _componentParamInfo.unwrap( childElement ) );
      }
    }
    return ret;
  }

  @Override
  public String getName() {
    return "request";
  }

  @Override
  public XmlElement wrap( Object array, XmlElement listElement ) {
    IType componentElementType = _componentParamInfo.getParameterElementType();
    int size = _componentParamInfo.getType().getArrayLength( array );
    for ( int i = 0; i < size; i++ ) {
      Object child = _componentParamInfo.getType().getArrayComponent( array, i );
      XmlElement childElement = (XmlElement) componentElementType.getTypeInfo().getConstructor().getConstructor().newInstance();
      if ( child == null ) {
        childElement.setNil( true );
      }
      else {
        childElement = _componentParamInfo.wrap( child, childElement );
      }
      listElement.addChild( childElement );
    }
    return listElement;
  }

}
