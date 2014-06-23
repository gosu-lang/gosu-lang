/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Document;
import gw.plugin.ij.completion.proposals.ICompletionHasAdditionalSyntax;
import org.jetbrains.annotations.NotNull;

public class GosuAdditionalSyntaxLookupElement extends LookupElement {
  private final ICompletionHasAdditionalSyntax _gosuCompletionProposal;

  public GosuAdditionalSyntaxLookupElement(ICompletionHasAdditionalSyntax kwProposal) {
    _gosuCompletionProposal = kwProposal;
  }

  @NotNull
  @Override
  public String getLookupString() {
    return _gosuCompletionProposal.toString();
  }

  @Override
  public void handleInsert(@NotNull InsertionContext context) {
    final Document document = context.getDocument();
    int parenStart = context.getStartOffset() + _gosuCompletionProposal.toString().length();
    if (context.getCompletionChar() != ' ') {
      document.insertString(parenStart, _gosuCompletionProposal.getTrailingText());
      int caretPosition = parenStart + _gosuCompletionProposal.getTrailingText().length() + _gosuCompletionProposal.getCaretOffsetFromEnd();
      context.getEditor().getCaretModel().moveToOffset(caretPosition);
    }
  }
}
