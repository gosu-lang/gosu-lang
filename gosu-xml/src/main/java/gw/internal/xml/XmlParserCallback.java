/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.xml.XmlElement;
import org.xml.sax.Locator;

import java.net.URL;

public interface XmlParserCallback {

    void onStartElement( Locator locator, XmlElement element, URL schemaEF );

}
