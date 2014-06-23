/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.types;

import gw.plugin.ij.completion.handlers.AbstractCompletionHandler;

public interface CompletionVoter {

  static enum Type {
    TYPE, ENUM, SYMBOL, BLOCK, KEYWORD;
  }

  boolean isCompletionAllowed(AbstractCompletionHandler handler);

  boolean isCompletionAllowed(Type type);
}
