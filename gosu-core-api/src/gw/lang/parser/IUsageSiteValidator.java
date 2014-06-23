/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.PublishInGosu;

@PublishInGosu
public interface IUsageSiteValidator
{
  /**
   * Called after the Gosu source corresponding to the parsed element is fully parsed.
   *
   * @param pe The parsed element to validate.
   */
  public void validate( IParsedElement pe );
}
