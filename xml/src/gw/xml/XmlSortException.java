/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSortException extends XmlException {

  private final List<QName> _qnames;

  public XmlSortException() {
    _qnames = Collections.emptyList();
  }

  public XmlSortException( String msg ) {
    super( msg );
    _qnames = Collections.emptyList();
  }

  public XmlSortException( String msg, XmlSortException cause ) {
    super( msg, cause );
    _qnames = Collections.emptyList();
  }

  public XmlSortException( List<QName> qnames ) {
    _qnames = qnames;
  }

  public List<QName> getQNames() {
    return _qnames;
  }

}
