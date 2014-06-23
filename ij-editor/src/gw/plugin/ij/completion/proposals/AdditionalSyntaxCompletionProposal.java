/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import org.jetbrains.annotations.Nullable;

public class AdditionalSyntaxCompletionProposal extends GosuCompletionProposal implements ICompletionHasAdditionalSyntax {
  private String _displayText;
  private String _trailingText;
  private int _caretOffsetFromEnd;
  private boolean _bold;

  public AdditionalSyntaxCompletionProposal(String displayText, String trailingText, int caretOffsetFromEnd) {
    init(displayText, trailingText, caretOffsetFromEnd);
  }

  public AdditionalSyntaxCompletionProposal(String displayText, String trailingText, int caretOffsetFromEnd, boolean bold) {
    init(displayText, trailingText, caretOffsetFromEnd);
    _bold = bold;
  }

  private void init(String displayText, String trailingText, int caretOffsetFromEnd) {
    _displayText = displayText;
    _trailingText = trailingText;
    _caretOffsetFromEnd = caretOffsetFromEnd;
  }

  @Override
  public String toString() {
    return _displayText;
  }

  @Nullable
  @Override
  public PsiElement resolve(PsiElement context) {
    return null;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return null;
  }

  @Override
  public String getGenericName() {
    return _displayText;
  }

  public String getTrailingText() {
    return _trailingText;
  }

  public int getCaretOffsetFromEnd() {
    return _caretOffsetFromEnd;
  }
}