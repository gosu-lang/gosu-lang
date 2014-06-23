/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.IParsedElement;

public interface IDependencyCollector<T extends IParsedElement> {
  void collect(T parsedElement, DependencySink sink);
}
