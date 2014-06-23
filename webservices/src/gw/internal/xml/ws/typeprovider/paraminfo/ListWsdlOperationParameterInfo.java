/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public class ListWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  private final WsdlOperationParameterInfo _componentParamInfo;

  public ListWsdlOperationParameterInfo( IType elementPropertyGosuType, QName elementQName, WsdlOperationParameterInfo componentParamInfo ) {
    super( elementPropertyGosuType, elementQName );
    _componentParamInfo = componentParamInfo;
  }

  @Override
  public IType getType() {
    return JavaTypes.LIST().getParameterizedType( _componentParamInfo.getType() );
  }

  @Override
  public Object unwrap( XmlElement parameterElement ) {
    List<Object> ret = new ArrayList<Object>();
    QName componentQName = _componentParamInfo.getParameterElementName();
    for ( XmlElement childElement : parameterElement.getChildren( componentQName ) ) {
      if ( childElement.isNil() ) {
        ret.add( null );
      }
      else {
        ret.add( _componentParamInfo.unwrap( childElement ) );
      }
    }
    return ret;
  }

  @Override
  public String getName() {
    return "request";
  }

  @Override
  public XmlElement wrap( Object obj, XmlElement listElement ) {
    IType componentElementType = _componentParamInfo.getParameterElementType();
    @SuppressWarnings( {"unchecked"} )
    List<Object> list = (List<Object>) obj;
    for ( Object child : list ) {
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
