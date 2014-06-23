/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;

import java.net.URI;

public interface IWsdlPortTypeData extends IWsdlTypeData {

  boolean isService();

  URI getAddress();

  WsdlPort getWsdlDefPort();  

}