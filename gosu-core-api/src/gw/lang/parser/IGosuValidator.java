/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IGosuValidator
{
  /**
   * Provides additional semantic checks to a Gosu parser
   *
   * @param rootParsedElement - the root parsed element to validate from
   */
  void validate( IParsedElement rootParsedElement, String scriptSrc );
}