/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers.filter;

import gw.plugin.ij.completion.proposals.GosuCompletionProposal;

public interface CompletionFilter {

  boolean allows(GosuCompletionProposal proposal);

  boolean allowsImportInsertion(String qualifiedName);
}
