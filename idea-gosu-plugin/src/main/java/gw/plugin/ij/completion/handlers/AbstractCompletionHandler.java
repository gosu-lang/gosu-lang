/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.PsiTypeLookupItem;
import com.intellij.codeInsight.lookup.VariableLookupItem;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.TrueFilter;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.GosuAdditionalSyntaxLookupElement;
import gw.plugin.ij.completion.GosuClassNameInsertHandler;
import gw.plugin.ij.completion.GosuFeatureCallLookupElement;
import gw.plugin.ij.completion.GosuFeatureInfoLookupItem;
import gw.plugin.ij.completion.InitializerCompletionProposalLookupElement;
import gw.plugin.ij.completion.RawSymbolLookupItem;
import gw.plugin.ij.completion.handlers.filter.CompletionFilter;
import gw.plugin.ij.completion.handlers.filter.CompletionFilterExtensionPointBean;
import gw.plugin.ij.completion.proposals.GosuCompletionProposal;
import gw.plugin.ij.completion.proposals.ICompletionHasAdditionalSyntax;
import gw.plugin.ij.completion.proposals.InitializerCompletionProposal;
import gw.plugin.ij.completion.proposals.PathCompletionProposal;
import gw.plugin.ij.completion.proposals.PrimitiveCompletionProposal;
import gw.plugin.ij.completion.proposals.SymbolCompletionProposal;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCompletionHandler implements IPathCompletionHandler {
  public static final Key<Integer> COMPLETION_WEIGHT = new Key<>("_gosuCompletionWeight");

  protected final CompletionParameters _params;
  private final CompletionResultSet _resultSet;
  private int _completionCount;
  private boolean _bFilterByInvocationCount;

  public AbstractCompletionHandler(CompletionParameters params, CompletionResultSet resultSet) {
    _params = params;
    _resultSet = resultSet;
    _bFilterByInvocationCount = true;
  }

  protected void stopFilterByInvocationCount() {
    _bFilterByInvocationCount = false;
  }

  public CompletionParameters getContext() {
    return _params;
  }

  public CompletionResultSet getResult() {
    return _resultSet;
  }

  @Nullable
  public String getStatusMessage() {
    return null;
  }

  public int getTotalCompletions() {
    return _completionCount;
  }

  @Nullable
  private static LookupElement createLookup(@NotNull CompletionParameters parameters, @NotNull GosuCompletionProposal gosuCompletionProposal) {
    return createLookup(parameters, gosuCompletionProposal, true);
  }

  @Nullable
  private static LookupElement createLookup(@NotNull CompletionParameters parameters, @NotNull GosuCompletionProposal proposal, boolean inJavaContext) {
    if (proposal instanceof InitializerCompletionProposal) {
      return new InitializerCompletionProposalLookupElement((InitializerCompletionProposal) proposal);
    }

    if (proposal instanceof ICompletionHasAdditionalSyntax) {
      return new GosuAdditionalSyntaxLookupElement((ICompletionHasAdditionalSyntax) proposal);
    }

    if (proposal instanceof PathCompletionProposal) {
      return new GosuFeatureInfoLookupItem(((PathCompletionProposal) proposal).getBeanTree(), parameters.getPosition());
    }

    if (proposal instanceof PrimitiveCompletionProposal) {
      return PsiTypeLookupItem.createLookupItem(((PrimitiveCompletionProposal) proposal).getPrimitiveType(), null);
    }

    LookupElement completion = null;
    final PsiElement element = proposal.resolve(parameters.getPosition());
    if (element != null) {
      if (element instanceof PsiClass) {
        completion = AllClassesGetter.createLookupItem((PsiClass) element, inJavaContext ? GosuClassNameInsertHandler.GOSU_CLASS_INSERT_HANDLER : AllClassesGetter.TRY_SHORTENING);
      } else if (element instanceof PsiVariable) {
        completion = new VariableLookupItem((PsiVariable) element);
      } else if (element instanceof PsiMethod) {
        completion = null; // let the feature info fill this completion in
      } else if (element instanceof PsiNamedElement) {
        completion = LookupElementBuilder.create((PsiNamedElement) element);
      } else {
        completion = LookupElementBuilder.create(element.getText());
      }
    }

    if (completion == null) {
      final IFeatureInfo featureInfo = proposal.getFeatureInfo();
      if (featureInfo != null) {
        completion = new GosuFeatureCallLookupElement(featureInfo);
      }
    }

    if (proposal instanceof SymbolCompletionProposal) {
      final IModule module = GosuModuleUtil.findModuleForPsiElement(parameters.getPosition());
      completion = new RawSymbolLookupItem(((SymbolCompletionProposal) proposal).getSymbol(), module);
    }

    if (completion == null) {
      completion = LookupElementBuilder.create(proposal.getGenericName());
    }

    completion.putUserData(COMPLETION_WEIGHT, proposal.getWeight());
    return completion;
  }

  public void addCompletion(@NotNull GosuCompletionProposal gosuCompletionProposal, boolean javaCtx) {
    addCompletion(_resultSet, gosuCompletionProposal, javaCtx);
  }

  public void addCompletion(@NotNull GosuCompletionProposal gosuCompletionProposal) {
    addCompletion(_resultSet, gosuCompletionProposal);
  }

  public void addCompletion(@NotNull CompletionResultSet customResult, @NotNull GosuCompletionProposal gosuCompletionProposal) {
    addCompletion(customResult,  gosuCompletionProposal, false);
  }

  // Stupid telescoping methods
  public void addCompletion(@NotNull CompletionResultSet customResult, @NotNull GosuCompletionProposal gosuCompletionProposal, boolean javaCtx) {
    if (!canBeShown(gosuCompletionProposal)) {
      return;
    }
    _completionCount++;
    final LookupElement completion = createLookup(_params, gosuCompletionProposal, javaCtx);
    if (completion != null) {
      customResult.addElement(completion);
    }
  }

  public boolean canBeShown(GosuCompletionProposal proposal) {
    List<CompletionFilter> filters = CompletionFilterExtensionPointBean.getFilters();
    for (CompletionFilter filter : filters) {
      if (!filter.allows(proposal)) {
        return false;
      }
    }
    return true;
  }

  public IType getCtxType() {
    return ((AbstractGosuClassFileImpl)getContext().getPosition().getContainingFile()).getParseData().getContextType();
  }

  public boolean atAnnotation(PsiElement position) {
    return PsiJavaPatterns.psiElement().afterLeaf("@").accepts(position);
  }

  public static boolean isAnnotation(IType type) {
    return JavaTypes.ANNOTATION().isAssignableFrom(type) ||
      JavaTypes.IANNOTATION().isAssignableFrom(type);
  }

  protected ElementFilter getLocalFilter( PsiElement insertedElement ) {
    PsiFile file = insertedElement.getContainingFile();
    if( file instanceof AbstractGosuClassFileImpl ) {
      final IGosuClass gsClass = ((AbstractGosuClassFileImpl)file).getParseData().getClassFileStatement().getGosuClass();
      if( gsClass != null ) {
        final ITypeUsesMap typeUsesMap = gsClass.getTypeUsesMap();
        if( typeUsesMap != null ) {
          return new ElementFilter() {
            @Override
            public boolean isAcceptable( Object element, PsiElement context ) {
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
              if( simpleName.startsWith( getResult().getPrefixMatcher().getPrefix() ) ) {
                if( _bFilterByInvocationCount && _params.getInvocationCount() < 2 ) {
                  // Limit to Local scope if this is the first Ctrl-Space
                  return isFirstCtrlSpaceWorthy( fqn );
                }
                // Second Ctrl-Space widens scope of completion proposals to All types
                return true;
              }
              return false;
            }

            private boolean isFirstCtrlSpaceWorthy( String fqn ) {
              if( fqn.startsWith( gsClass.getName() ) ) {
                // inner class of the file
                return true;
              }
              if( typeUsesMap.containsType( fqn ) ) {
                // reachable via uses-statements
                return true;
              }
              if( isCommonType( fqn ) ) {
                // is java.lang.* and the like
                return true;
              }
              return false;
            }

            private boolean isCommonType( String fqn ) {
              return fqn.startsWith( "java.lang." ) ||
                     fqn.startsWith( "java.util." );
            }

            @Override
            public boolean isClassAcceptable( Class hintClass ) {
              return true;
            }
          };
        }
      }
    }
    return TrueFilter.INSTANCE;
  }

}
