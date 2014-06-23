/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import gw.plugin.ij.completion.proposals.GosuCompletionProposal;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IPathCompletionHandler {
//  public ContentAssistInvocationContext getContext();
//  public void setParameters( ContentAssistInvocationContext ctx );

  void handleCompletePath();

  @Nullable
  String getStatusMessage();
}
