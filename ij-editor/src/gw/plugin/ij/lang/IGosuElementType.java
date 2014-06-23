/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import gw.lang.parser.IParsedElement;

public interface IGosuElementType {
  Class<? extends IParsedElement> getParsedElementType();
}
