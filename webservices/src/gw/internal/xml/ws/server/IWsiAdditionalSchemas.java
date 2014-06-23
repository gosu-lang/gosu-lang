/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.xml.XmlParseOptions;
import gw.xml.XmlSchemaAccess;

import java.util.List;

public interface IWsiAdditionalSchemas {

  List<XmlSchemaAccess> getAdditionalSchemas();

}
