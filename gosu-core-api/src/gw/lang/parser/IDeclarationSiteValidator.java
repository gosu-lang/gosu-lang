/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.PublishInGosu;

@PublishInGosu
public interface IDeclarationSiteValidator
{
  /**
   * Called after the whole class has been defn compiled.
   * Implementors of this method can inspect the parse tree and add warnings/errors as appropriate
   *
   * @param feature the parsed element that this annotation lives on.
   */
  public void validate( IParsedElement feature );
}
