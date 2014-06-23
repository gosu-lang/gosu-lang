/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;

/**
 * Represents a textual entry in an XML mixed content list.
 */
@PublishInGosu
public class XmlMixedContentText implements IXmlMixedContent {

  private final String _text;

  /**
   * Creates an instance with the following text.
   * @param text the text
   */
  public XmlMixedContentText( String text ) {
    if ( text == null ) {
      throw new IllegalArgumentException( "text cannot be null" );
    }
    _text = text;
  }

  /**
   * Returns the text content.
   * @return the text content
   */
  public String getText() {
    return _text;
  }

  /**
   * Returns the text content.
   * @return the text content
   */
  @Override
  public String toString() {
    return _text;
  }

}
