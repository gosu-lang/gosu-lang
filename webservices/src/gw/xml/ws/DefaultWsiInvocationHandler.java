/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.WsiInvocationContextImpl;
import gw.internal.xml.ws.server.marshal.MarshalContext;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.ws.server.marshal.UnmarshalContext;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlElement;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

public class DefaultWsiInvocationHandler {

  private final LockingLazyVar<IType> _wsiInvocationContextType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.WsiInvocationContext" );
    }
  };

  public XmlElement invoke( XmlElement requestElement, WsiInvocationContext context ) throws Throwable {
    QName opName = requestElement.getQName();
    WsiInvocationContextImpl contextImpl = (WsiInvocationContextImpl) context;
    final WsiInvocationContextImpl.WebService webservice = contextImpl.getWebService();
    IMethodInfo method = webservice._serviceInfo.getOriginalMethods().get( opName );
    if ( method == null ) {
      throw new WebServiceException( "No such operation " + opName + " in service " + webservice._backingClass.getName(), true );
    }
    context.preExecute(requestElement, method);
    final List<Object> parameters = new ArrayList<Object>();

    for (IParameterInfo paramInfo : method.getParameters()) {
      if ( paramInfo.getFeatureType().equals( _wsiInvocationContextType.get() ) ) {
        parameters.add( context );
      }
      else {
        QName elName = new QName( requestElement.getNamespace().getNamespaceURI(), paramInfo.getName());
        MarshalInfo marshalInfo = webservice._serviceInfo.getMarshalInfoMap().get( opName ).get( elName );
        XmlElement paramElement = requestElement.getChild( elName );
        Object param = paramElement == null ? null : marshalInfo.unmarshal( paramElement, new UnmarshalContext( null ) );
        parameters.add(param);
      }
    }

    Object methodReturn = method.getCallHandler().handleCall( webservice._worker, parameters.toArray() );
    XmlElement resp = new XmlElement( new QName( opName.getNamespaceURI(), opName.getLocalPart() + "Response" ) );
    if ( methodReturn != null ) {
      XmlElement parameterElement = new XmlElement( new QName( opName.getNamespaceURI(), "return" ) );
      resp.addChild( parameterElement );
      XmlServices.marshal( parameterElement, method.getReturnType(), methodReturn, new MarshalContext() );
    }
    return resp;
  }

}
