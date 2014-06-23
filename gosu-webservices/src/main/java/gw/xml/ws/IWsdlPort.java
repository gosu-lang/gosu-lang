/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.internal.xml.ws.IWsdlConfig;
import gw.lang.PublishedType;
import gw.lang.PublishedTypes;
import gw.util.ILogger;

import javax.xml.namespace.QName;

@PublishedTypes(
        @PublishedType(fromType = "gw.internal.xml.ws.IWsdlConfig", toType = "gw.xml.ws.WsdlConfig")
)
public interface IWsdlPort {

  /** This is the qname for the port
   *
   * @return a QName
   */
  QName getPortQName();

  /** this is the qname for the service
   *
   * @return the QName
   */
  QName getServiceQName();

  /** This is the config used for this service
   *
   * @return the WsdlConfig
   */
  IWsdlConfig getConfig();

  /** the logger used for this web service
   *
   * @return the logger
   */
  ILogger getLogger();

  /** to set the logger for this web service
   *
   * @param logger the ILogger use use for this web service
   */
  void setLogger(ILogger logger);
}
