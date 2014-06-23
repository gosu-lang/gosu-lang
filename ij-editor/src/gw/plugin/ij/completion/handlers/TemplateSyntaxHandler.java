/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import gw.lang.parser.IToken;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.plugin.ij.completion.proposals.AdditionalSyntaxCompletionProposal;
import gw.plugin.ij.completion.proposals.GosuCompletionProposal;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.impl.GosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuTemplateFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Completes a symbol, not a .
 */
public class TemplateSyntaxHandler extends AbstractCompletionHandler {
  public TemplateSyntaxHandler(CompletionParameters params, CompletionResultSet results) {
    super(params, results);
  }

  public void handleCompletePath() {
    IElementType eltType = getContext().getPosition().getNode().getElementType();
    boolean inString = inString(eltType);
    boolean inTemplate = inTemplate(getContext().getPosition());
    if(inString || inTemplate) {
      String text = getContext().getPosition().getText();
      int completionIndex = text.lastIndexOf(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED);
      if (completionIndex > 0) {
        if (text.charAt(completionIndex - 1) == '$') {
          addCompletion(getResult().withPrefixMatcher(""),
            new AdditionalSyntaxCompletionProposal("{}", "", -1));
        }
        if (text.charAt(completionIndex - 1) == '<') {
          CompletionResultSet newResult = getResult().withPrefixMatcher("");
          addCompletion(newResult, new AdditionalSyntaxCompletionProposal("%=%>", "", -2));
          addCompletion(newResult, new AdditionalSyntaxCompletionProposal("%%>", "", -2));
          if (inTemplate) {
            addCompletion(newResult, new AdditionalSyntaxCompletionProposal("%@%>", "", -2));
          }
          addCompletion(newResult, new AdditionalSyntaxCompletionProposal("%----%>", "", -4));
        }
      }
    }
  }

  private boolean inTemplate(@NotNull PsiElement position) {
    return position instanceof PsiWhiteSpace && position.getContainingFile() instanceof GosuTemplateFileImpl;
  }

  private boolean inString(IElementType eltType) {
    return eltType == GosuTokenTypes.TT_OP_quote_double || eltType == GosuTokenTypes.TT_OP_quote_single;
  }
}
