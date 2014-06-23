/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.lang.reflect.IMethodInfo;
import gw.xml.XmlElement;
import gw.xml.XmlSerializationOptions;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public abstract class WsiInvocationContext {

  public abstract HttpServletRequest getHttpServletRequest();

  public abstract void preExecute(XmlElement requestElement, IMethodInfo method) throws WebServiceException, WsiAuthenticationException, IOException;

  public abstract HttpHeaders getRequestHttpHeaders();

  public abstract XmlElement getRequestEnvelope();

  public abstract XmlElement getRequestSoapHeaders();

  public abstract HttpHeaders getResponseHttpHeaders();

  public abstract List<XmlElement> getResponseSoapHeaders();

  public abstract XmlSerializationOptions getXmlSerializationOptions();

  public abstract void setXmlSerializationOptions( XmlSerializationOptions serializationOptions );

  public abstract void setMtomEnabled( boolean mtomEnabled );

  public abstract boolean isMtomEnabled();

}
