/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

public interface ICompletionHasAdditionalSyntax {
  String getTrailingText();

  int getCaretOffsetFromEnd();
}
