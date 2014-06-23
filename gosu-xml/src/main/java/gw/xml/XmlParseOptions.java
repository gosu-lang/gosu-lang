/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;

import java.util.ArrayList;
import java.util.List;

@PublishInGosu
public final class XmlParseOptions {

  private List<XmlSchemaAccess> _additionalSchemas = new ArrayList<XmlSchemaAccess>();
  private boolean _validate = true;

  /**
   * Any additional schemas that should be present when parsing.
   */
  public List<XmlSchemaAccess> getAdditionalSchemas() {
    return _additionalSchemas;
  }

  /**
   * Any additional schemas that should be present when parsing.
   */
  public void setAdditionalSchemas( List<XmlSchemaAccess> additionalSchemas ) {
    if ( additionalSchemas == null ) {
      throw new IllegalArgumentException( "additionalSchemas cannot be null" );
    }
    _additionalSchemas = additionalSchemas;
  }

  /**
   * Determines whether XML will be validated against the schemas.
   * @deprecated Turning off validation is an experimental feature. Use at your own risk.
   */
  public boolean getValidate() {
    return _validate;
  }

  /**
   * Determines whether XML will be validated against the schemas.
   * @deprecated Turning off validation is an experimental feature. Use at your own risk.
   */
  public void setValidate( boolean validate ) {
    _validate = validate;
  }

  /**
   * Makes a deep copy of this object.
   * @return a deep copy of this object
   */
  public XmlParseOptions copy() {
    XmlParseOptions copy = new XmlParseOptions();
    copy._additionalSchemas = new ArrayList<XmlSchemaAccess>( _additionalSchemas );
    copy._validate = _validate;
    return copy;
  }

}
