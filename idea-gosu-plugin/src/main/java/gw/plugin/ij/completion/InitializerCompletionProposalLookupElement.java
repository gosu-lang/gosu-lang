/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.lookup.LookupElementPresentation;
import gw.plugin.ij.completion.proposals.InitializerCompletionProposal;
import org.jetbrains.annotations.NotNull;

public class InitializerCompletionProposalLookupElement extends GosuAdditionalSyntaxLookupElement {
  private final InitializerCompletionProposal _proposal;

  public InitializerCompletionProposalLookupElement(InitializerCompletionProposal completionProposal) {
    super(completionProposal);
    _proposal = completionProposal;
  }

  @Override
  public void renderElement(@NotNull LookupElementPresentation presentation) {
    presentation.setItemText(getLookupString());
    presentation.setTypeText(_proposal.getType());
    presentation.setItemTextBold(_proposal.isRequired());
  }
}
