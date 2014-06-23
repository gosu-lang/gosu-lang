/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import gw.lang.parser.IToken;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.plugin.ij.completion.proposals.AdditionalSyntaxCompletionProposal;
import gw.plugin.ij.lang.psi.impl.GosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Completes a symbol, not a .
 */
public class ClassKeywordsHandler extends AbstractCompletionHandler {
  @NotNull
  private final PrefixMatcher _prefixMatcher;

  public ClassKeywordsHandler(CompletionParameters params, @NotNull CompletionResultSet result) {
    super(params, result);
    _prefixMatcher = result.getPrefixMatcher();
  }

  public void handleCompletePath() {
    maybeAddKeywords();
  }

  private void maybeAddKeywords() {
    for (AdditionalSyntaxCompletionProposal kw : getKeywordsThatMakeSense()) {
      if (_prefixMatcher == null || kw.getGenericName().indexOf(_prefixMatcher.getPrefix()) == 0) {
        kw.setWeight(-1);
        addCompletion(kw);
      }
    }
  }

  public List<AdditionalSyntaxCompletionProposal> getKeywordsThatMakeSense() {
    final PsiElement position = _params.getPosition();
    if (position instanceof PsiComment) {
      return Collections.emptyList();
    }

    final PsiElement parent = position.getParent();
    if (parent instanceof GosuFieldImpl || parent instanceof GosuMethodImpl) {
      return Collections.emptyList();
    }

    PsiElement grandParent = parent == null ? null : parent.getParent();

    // TODO: use Keywords class from core Gosu
    List<String> keywords = Lists.newArrayList(
        "class", "interface", "structure", "enum",
        "private", "public", "protected", "internal",
        "abstract", "final");

    // Enhancement file
    if (position.getContainingFile().getName().endsWith(GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT)) {
      keywords.add("enhancement");
    }

    if (!(grandParent instanceof GosuClassFileImpl) &&
        afterClassCurly(position, grandParent)) {
      keywords.addAll(Arrays.asList("function", "property", "construct",
          "var", "delegate", "override", "static"));
    }

    // Remove all the keywords already present
    PsiElement previous = position.getPrevSibling();
    List<String> previousKeywords = new ArrayList<>();
    while (previous != null) {
      previousKeywords.add(previous.getText());
      previous = previous.getPrevSibling();
    }

    if (previousKeywords.contains("property")) {
      keywords = Arrays.asList("get", "set");
    }

    if (previousKeywords.contains("function")) {
      keywords = Collections.emptyList();
    }

    if (afterClassName(position, grandParent)) {
      keywords = Arrays.asList("implements", "extends");
    }
    // Copy the list b/c it may not be mutable after all the asList() fixed-size stuff
    keywords = new ArrayList<>(keywords);
    keywords.removeAll(previousKeywords);

    if (previousKeywords.contains("get") || previousKeywords.contains("set")) {
      return Collections.emptyList();
    }


    ArrayList<AdditionalSyntaxCompletionProposal> keywordCompletionProposals = new ArrayList<>();
    for (String keyword : keywords) {
      if (keyword.equals("construct")) {
        keywordCompletionProposals.add(new AdditionalSyntaxCompletionProposal(keyword, "()", -1));
      } else {
        keywordCompletionProposals.add(new AdditionalSyntaxCompletionProposal(keyword, " ", 0));
      }
    }

    return keywordCompletionProposals;
  }

  private boolean afterClassName(@NotNull PsiElement position, @NotNull PsiElement grandParent) {
    if (grandParent instanceof GosuClassDefinitionImpl) {
      IClassStatement parsedElement = ((GosuClassDefinitionImpl) grandParent).getParsedElement();
      IClassDeclaration classDeclaration = parsedElement.getClassDeclaration();
      if (classDeclaration != null && classDeclaration.getLocation() != null &&
          classDeclaration.getLocation().getOffset() < position.getTextOffset() &&
          isBeforeFirstCurly(position.getTextOffset(), parsedElement.getTokens())) {
        return true;
      }
    }
    return false;
  }

  private boolean isBeforeFirstCurly(int textOffset, @NotNull List<IToken> tokens) {
    for (IToken token : tokens) {
      if ("{".equals(token.getText())) {
        return textOffset <= token.getTokenStart();
      }
    }
    return true;
  }

  private boolean afterClassCurly(@NotNull PsiElement position, @NotNull PsiElement grandParent) {
    if (grandParent instanceof GosuClassDefinitionImpl) {
      IClassStatement parsedElement = ((GosuClassDefinitionImpl) grandParent).getParsedElement();
      return isAfterFirstCurly(position.getTextOffset(), parsedElement.getTokens());
    }
    return true;
  }

  private boolean isAfterFirstCurly(int textOffset, @NotNull List<IToken> tokens) {
    for (IToken token : tokens) {
      if ("{".equals(token.getText())) {
        return textOffset >= token.getTokenStart();
      }
    }
    return true;
  }
}
