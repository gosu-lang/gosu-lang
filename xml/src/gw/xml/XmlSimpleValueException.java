/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;

import java.util.Collections;
import java.util.List;

/**
 * Thrown when an attempt is made to set a simple value that does not match the schema. For example, if an
 * xsd:union is defined with member types xsd:float and xsd:date, the generated property will be of type
 * java.lang.String, but an attempt to assign a value that would not be a valid xsd:float or xsd:date would
 * cause this exception to be thrown.
 */
@PublishInGosu
public final class XmlSimpleValueException extends XmlException {

  private final List<XmlSimpleValueException> _causes;

  public XmlSimpleValueException( String msg ) {
    super( msg );
    _causes = null;
  }

  public XmlSimpleValueException( String msg, List<XmlSimpleValueException> causes ) {
    super( msg );
    _causes = Collections.unmodifiableList( causes );
  }

  public XmlSimpleValueException( String msg, Throwable cause ) {
    super( msg, cause );
    _causes = null;
  }

  /**
   * Returns the XmlSimpleValueException causes of this exception. If this exception was caused by an exception
   * other than an XmlSimpleValueException, returns null, and the getCause() method can be used to get the
   * cause, if any. In the case of an xsd:union failure, this list may contain multiple values.
   * @return the XmlSimpleValueException causes of this exception, or null
   */
  public List<XmlSimpleValueException> getSimpleValueExceptionCauses() {
    List<XmlSimpleValueException> causes;
    if ( _causes != null ) {
      causes = _causes;
    }
    else if ( getCause() instanceof XmlSimpleValueException ) {
      causes = Collections.singletonList( (XmlSimpleValueException) getCause() );
    }
    else {
      causes = null;
    }
    return causes;
  }

}
