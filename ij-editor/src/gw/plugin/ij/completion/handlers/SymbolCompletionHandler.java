/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.filters.ElementFilter;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.GosuTypes;
import gw.plugin.ij.completion.proposals.AdditionalSyntaxCompletionProposal;
import gw.plugin.ij.completion.proposals.RawCompletionProposal;
import gw.plugin.ij.completion.proposals.SymbolCompletionProposal;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuForEachStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuNotAStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuStatementListImpl;
import gw.plugin.ij.lang.psi.impl.types.CompletionVoter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Completes a symbol, not a .
 */
public class SymbolCompletionHandler extends AbstractCompletionHandler {
  @NotNull
  private final PrefixMatcher _prefixMatcher;
  private IType _thisType;

  public SymbolCompletionHandler(CompletionParameters params, @NotNull CompletionResultSet results) {
    super(params, results);
    _prefixMatcher = results.getPrefixMatcher();
  }

  public void handleCompletePath() {
    final PsiElement position = _params.getOriginalPosition();
    final PsiFile psiFile;

    if (position == null) {
      psiFile = _params.getOriginalFile();
    } else {
      psiFile = position.getContainingFile();
    }

    if (psiFile != null) {
      final ISymbolTable symbolTable = ((AbstractGosuClassFileImpl) psiFile).getParseData().getSnapshotSymbols();
      if (symbolTable != null) {
        final ISymbol thisSymbol = symbolTable.getThisSymbolFromStackOrMap();
        if (thisSymbol != null) {
          _thisType = thisSymbol.getType();
        }

        int weight = 1;

        // Second invocation of completion (Ctrl-Space)
        boolean bSecondControlSpace = _params.getInvocationCount() >= 2;

        if (isAllowed(psiFile, CompletionVoter.Type.SYMBOL)) {
          maybeAddSymbols(symbolTable, weight--);
        }
        if (isAllowed(psiFile, CompletionVoter.Type.ENUM)) {
          maybeAddEnumFields(weight--);
        }
        if (isAllowed(psiFile, CompletionVoter.Type.BLOCK)) {
          maybeAddBlockCompletion(weight--);
        }
        if (isAllowed(psiFile, CompletionVoter.Type.KEYWORD)) {
          maybeAddKeywords(weight--);
        }
        if (isAllowed(psiFile, CompletionVoter.Type.TYPE)) {
          maybeAddTypes(weight--, bSecondControlSpace);
        }
      }
    }
  }

  private boolean isAllowed(PsiFile psiFile, CompletionVoter.Type type) {
    if(!(psiFile instanceof GosuFragmentFileImpl)) {
      return true;
    }
    return psiFile instanceof CompletionVoter && ((CompletionVoter) psiFile).isCompletionAllowed(type);
  }

  private void maybeAddTypes( int weight, boolean bSecondControlSpace ) {
    if (bSecondControlSpace ) {
      // If it's the second Ctrl-Space, add ALL types
      new TypeCompletionHandler(_params, getResult(),
          new ElementFilter() {
            @Override
            public boolean isAcceptable(Object element, PsiElement context) {
              String fqn;
              if( element instanceof PsiClass ) {
                PsiClass element1 = (PsiClass)element;
                fqn = element1.getQualifiedName();
              }
              else if( element instanceof CharSequence ) {
                fqn = element.toString();
              }
              else {
                return false;
              }
              int iLastDot = fqn.lastIndexOf( '.' );
              String simpleName = iLastDot > 0 ? fqn.substring( iLastDot + 1 ) : fqn;
              return simpleName.startsWith( getResult().getPrefixMatcher().getPrefix() );
            }

            @Override
            public boolean isClassAcceptable(Class hintClass) {
              return true;
            }
          }, weight).handleCompletePath();
    } else if( getTotalCompletions() == 0 ) {
      // If there are no other completions available, add local types
      PsiFile file = getContext().getPosition().getContainingFile();
      if (file != null) {
        if (_thisType instanceof IGosuClass) {
          final ITypeUsesMap typeUsesMap = ((IGosuClass) _thisType).getTypeUsesMap();
          if (typeUsesMap != null) {
            new TypeCompletionHandler(_params, getResult(), weight).handleCompletePath();
          }
        }
      }
    }
  }

  private void maybeAddKeywords(int weight) {
    for (AdditionalSyntaxCompletionProposal kw : getKeywordsThatMakeSense()) {
      if (_prefixMatcher == null || _prefixMatcher.isStartMatch(kw.getGenericName())) {
        kw.setWeight(weight);
        addCompletion(kw);
      }
    }
  }

  @NotNull
  public List<AdditionalSyntaxCompletionProposal> getKeywordsThatMakeSense() {
    List<Keyword> keywords = makeExpressionKeywordList();

    if (!parents(GosuNotAStatementImpl.class, GosuForEachStatementImpl.class)) {
      // remove all for loop expression keywords
      maybeRemoveKeyword(keywords, Keyword.KW_index);
      maybeRemoveKeyword(keywords, Keyword.KW_iterator);
    }

    if (!parents(IGosuPsiElement.class, GosuStatementListImpl.class) &&
        !parents(IGosuPsiElement.class, GosuNotAStatementImpl.class) &&
        !inTopLevelProgramExpression()) {
      // remove all statement starts
      maybeRemoveKeyword(keywords, Keyword.KW_var);
      maybeRemoveKeyword(keywords, Keyword.KW_for);
      maybeRemoveKeyword(keywords, Keyword.KW_while);
      maybeRemoveKeyword(keywords, Keyword.KW_try);
      maybeRemoveKeyword(keywords, Keyword.KW_switch);
      maybeRemoveKeyword(keywords, Keyword.KW_do);
      maybeRemoveKeyword(keywords, Keyword.KW_using);
    }

    if (!(parents(GosuForEachStatementImpl.class)) &&
        !(inProgram() && parents(Object.class, GosuForEachStatementImpl.class))) {
      // remove 'in' if we aren't directly in a for loop
      maybeRemoveKeyword(keywords, Keyword.KW_in);
    }

    return makeKeywordProposals(keywords);
  }

  private void maybeRemoveKeyword(@NotNull List<Keyword> keywords, @NotNull Keyword kw) {
    // never remove exact keyword matches, to avoid annoying completion behavior
    PrefixMatcher prefixMatcher = getResult().getPrefixMatcher();
    if (!prefixMatcher.getPrefix().equals(kw.getName())) {
      keywords.remove(kw);
    }
  }

  //hack to detect if we are at the top level of a program, so expr/stmt is ambiguious and
  // we should include all keywords
  private boolean inTopLevelProgramExpression() {
    if (inProgram()) {
      IParsedElement pe = ((GosuProgramFileImpl) getContext().getOriginalFile()).getParsedElement();
      if (pe != null) {
        IGosuClass gosuClass = pe.getGosuClass();
        if (gosuClass instanceof IGosuProgram) {
          return parents(IGosuPsiElement.class, GosuRawPsiElement.class);
        }
      }
    }
    return false;
  }

  private boolean inProgram() {
    return getContext().getOriginalFile() instanceof GosuProgramFileImpl;
  }

  @NotNull
  private List<AdditionalSyntaxCompletionProposal> makeKeywordProposals(@NotNull List<Keyword> keywords) {
    ArrayList<AdditionalSyntaxCompletionProposal> proposals = new ArrayList<>();
    for (Keyword keyword : keywords) {
      if (keyword == Keyword.KW_for || keyword == Keyword.KW_while || keyword == Keyword.KW_using) {
        proposals.add(makeKeywordProposal(keyword.getName(), "()", -1));
      } else if (keyword == Keyword.KW_in || keyword == Keyword.KW_or || keyword == Keyword.KW_and || keyword == Keyword.KW_not) {
        proposals.add(makeKeywordProposal(keyword.getName(), " ", 0));
      } else if (keyword == Keyword.KW_try) {
        proposals.add(makeKeywordProposal(keyword.getName(), " {}", -1));
      } else {
        proposals.add(makeKeywordProposal(keyword.getName(), "", 0));
      }
    }
    return proposals;
  }

  private boolean parents(@NotNull Class... parents) {
    PsiElement position = _params.getPosition();
    PsiElement parent = position.getParent();
    for (Class aClass : parents) {
      if (!aClass.isInstance(parent)) {
        return false;
      }
      parent = parent.getParent();
    }
    return true;
  }

  private List<Keyword> makeExpressionKeywordList() {
    return Lists.newArrayList(
        Keyword.KW_and,
        Keyword.KW_as,
        Keyword.KW_block,
        Keyword.KW_break,
        Keyword.KW_case,
        Keyword.KW_catch,
        Keyword.KW_continue,
        Keyword.KW_do,
        Keyword.KW_else,
        Keyword.KW_eval,
        Keyword.KW_false,
        Keyword.KW_final,
        Keyword.KW_finally,
        Keyword.KW_for,
        Keyword.KW_if,
        Keyword.KW_in,
        Keyword.KW_index,
        Keyword.KW_iterator,
        Keyword.KW_new,
        Keyword.KW_not,
        Keyword.KW_null,
        Keyword.KW_or,
        Keyword.KW_outer,
        Keyword.KW_return,
        Keyword.KW_statictypeof,
        Keyword.KW_super,
        Keyword.KW_switch,
        Keyword.KW_this,
        Keyword.KW_throw,
        Keyword.KW_true,
        Keyword.KW_try,
        Keyword.KW_typeis,
        Keyword.KW_typeof,
        Keyword.KW_using,
        Keyword.KW_var,
        Keyword.KW_where,
        Keyword.KW_while);
  }

  @NotNull
  private AdditionalSyntaxCompletionProposal makeKeywordProposal(String name, String trailingText, int offsetForCaret) {
    return new AdditionalSyntaxCompletionProposal(name, trailingText, offsetForCaret);
  }

  private void maybeAddSymbols(@NotNull ISymbolTable transientSymbolTable, int weight) {
    Collection<ISymbol> listSymbols = transientSymbolTable.getSymbols().values();
    filterUnwantedSymbols(listSymbols);

    ISymbol[] symbols = listSymbols.toArray(new ISymbol[listSymbols.size()]);
    Arrays.sort(symbols, new Comparator<ISymbol>() {
      public int compare(@NotNull ISymbol o1, @NotNull ISymbol o2) {
        return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
      }
    });

    for (ISymbol symbol : symbols) {
      if (_prefixMatcher == null || _prefixMatcher.prefixMatches(symbol.getDisplayName())) {
        addCompletion(new SymbolCompletionProposal(symbol, weight));
      }
    }
  }

  private void maybeAddBlockCompletion(int weight) {
    IType type = getCtxType();
    if (type instanceof IBlockType) {
      StringBuilder sb = new StringBuilder("\\");
      final String[] parameterNames = ((IBlockType) type).getParameterNames();
      if (parameterNames.length > 0) {
        sb.append(" ");
        Joiner.on(", ").appendTo(sb, parameterNames);
        sb.append(" ");
      }
      sb.append("-> ");
      RawCompletionProposal element = new RawCompletionProposal(sb.toString());
      element.setWeight(weight);
      addCompletion(element);
    }
  }

  private void maybeAddEnumFields(int weight) {
    final IType type = getCtxType();
    if (type instanceof IEnumType && type.isEnum()) {
      for (String constant : ((IEnumType) type).getEnumConstants()) {
        final RawCompletionProposal proposal = new RawCompletionProposal(constant);
        proposal.setWeight(weight);
        addCompletion(proposal);
      }
    }
  }

  private void filterUnwantedSymbols(@NotNull Collection<ISymbol> listSymbols) {
    List<ISymbol> deleteSyms = new ArrayList<>();
    for (ISymbol s : listSymbols) {
      if (s.getType().getDisplayName().equals(GosuTypes.DEF_CTOR_TYPE().getDisplayName())) {
        // Filter constructor symbols (only applicable when editing a gs class)
        deleteSyms.add(s);
      } else if (s.getName()==null || s.getName().startsWith("@")) {
        deleteSyms.add(s);
      } else if (_prefixMatcher == null || !_prefixMatcher.prefixMatches(s.getDisplayName())) {
        deleteSyms.add(s);
      } else if (s instanceof IDynamicFunctionSymbol && ((IDynamicFunctionSymbol) s).isConstructor()) {
        // Filter any constructors
        deleteSyms.add(s);
      }
    }
    listSymbols.removeAll(deleteSyms);
  }
}
