/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionSorter;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.handlers.*;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuNotAStatementImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnonymousClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.types.CompletionVoter;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.*;
import static com.google.common.base.Objects.firstNonNull;

public class GosuCompletionContributor extends CompletionContributor {
  private static final ElementPattern<PsiElement> AFTER_DOT = psiElement().afterLeaf(".");
  private static final ElementPattern<PsiElement> AFTER_SHARP = psiElement().afterLeaf("#");
  private static final ElementPattern<PsiElement> AFTER_QUESTION_DOT = psiElement().afterLeaf("?.");
  private static final ElementPattern<PsiElement> AFTER_STAR_DOT = psiElement().afterLeaf("*.");
  private static final ElementPattern<PsiElement> AFTER_COLON = psiElement().afterLeaf(":").withParent(IGosuPsiElement.class);

  public GosuCompletionContributor() {
// You can use this to debug where certain code completions are being invoked
//    extend(CompletionType.BASIC,
//            psiElement().withParents(PsiElement.class),
//            new CompletionProvider<CompletionParameters>() {
//              @Override
//              protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
//                System.out.println("here");
//              }
//            });

    extend(CompletionType.BASIC,
        psiElement(),
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            safeComplete(new TemplateSyntaxHandler(parameters, updateResult(parameters, result)));
          }
        });

    extend(CompletionType.BASIC,
        psiElement().withParent(GosuTypeLiteralImpl.class),
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result) {
            safeComplete(new TypeCompletionHandler(parameters, updateResult(parameters, result), 0));
          }
        }
    );

    extend(CompletionType.BASIC,
        or(psiElement().withParents(PsiElement.class, GosuClassDefinitionImpl.class),
            psiElement().withParents(PsiElement.class, GosuClassFileImpl.class),
            psiElement().withParents(PsiElement.class, GosuAnonymousClassDefinitionImpl.class),
            psiElement().withParents(PsiElement.class, GosuNotAStatementImpl.class, GosuSyntheticClassDefinitionImpl.class), // for Gosu Scratch Pad
            psiElement().withParents(PsiElement.class, GosuRawPsiElement.class, GosuSyntheticClassDefinitionImpl.class)),    // for *.gsp
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            safeComplete(new ClassKeywordsHandler(parameters, updateResult(parameters, result)));
          }
        });

    extend(CompletionType.BASIC,
        and(
            or(psiElement().withParent(GosuIdentifierExpressionImpl.class),
                psiElement().withParent(GosuIdentifierImpl.class)),
            not(AFTER_COLON)),
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result) {
            safeComplete(new SymbolCompletionHandler(parameters, updateResult(parameters, result)));
          }
        }
    );

    extend(
        CompletionType.BASIC,
        and(psiElement().withParent(GosuRawPsiElement.class),
            not(AFTER_COLON)),
        new CompletionProvider<CompletionParameters>() {
          @Override
          protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result) {
            if (((GosuRawPsiElement) parameters.getPosition().getParent()).getNode().getElementType() == GosuElementTypes.ELEM_TYPE_NoOpStatement) {
              safeComplete(new SymbolCompletionHandler(parameters, updateResult(parameters, result)));
            }
          }
        }
    );

    extend(CompletionType.BASIC,
        or(AFTER_DOT, AFTER_QUESTION_DOT, AFTER_STAR_DOT),
        new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
        safeComplete(new MemberPathCompletionHandler(parameters, updateResult(parameters, result)));
      }
    });

    extend(CompletionType.BASIC, AFTER_DOT, new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
        safeComplete(new PackageCompletionHandler(parameters, updateResult(parameters, result)));
      }
    });

    extend(CompletionType.BASIC, AFTER_COLON, new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
        safeComplete(new InitializerCompletionHandler(parameters, updateResult(parameters, result)));
      }
    });

    extend(CompletionType.BASIC, AFTER_SHARP, new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters parameters,
                                    ProcessingContext context,
                                    @NotNull CompletionResultSet result) {
        safeComplete(new FeatureRefCompletionHandler(parameters, updateResult(parameters, result)));
      }
    });
  }

  @NotNull
  private CompletionResultSet updateResult(CompletionParameters params, @NotNull CompletionResultSet result) {
    CompletionResultSet completionResultSet = result;
    CompletionSorter completionSorter =
        CompletionSorter.defaultSorter(params, completionResultSet.getPrefixMatcher())
            .weighBefore("priority", new LookupElementWeigher("gosuWeight") {
              @NotNull
              @Override
              public Comparable weigh(@NotNull LookupElement element) {
                final Integer weight = element.getUserData(AbstractCompletionHandler.COMPLETION_WEIGHT);
                return firstNonNull(weight, 0);
              }
            });
    completionResultSet = completionResultSet.withRelevanceSorter(completionSorter);
    return completionResultSet;
  }

  private static boolean relevant(AbstractCompletionHandler handler) {
    CompletionParameters params = handler.getContext();
    PsiFile file = params.getPosition().getContainingFile();
    if (file instanceof CompletionVoter) {
      return ((CompletionVoter) file).isCompletionAllowed(handler);
    }
    return true;
  }

  private static void safeComplete(@NotNull AbstractCompletionHandler handler) {
    if (!relevant(handler)) {
      return;
    }
    // hack to short circuit completion
    if (atNumber(handler.getContext())) {
      return;
    }

    final IModule module = GosuModuleUtil.findModuleForPsiElement(handler.getContext().getPosition());
    TypeSystem.pushModule(module);
    try {
      PsiFile psiFile = handler.getContext().getOriginalFile();
      if( psiFile instanceof AbstractGosuClassFileImpl ) {
        ((AbstractGosuClassFileImpl)psiFile).reparsePsiFromContent();
      }
      handler.handleCompletePath();
    } catch (RuntimeException e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw e instanceof ProcessCanceledException ? e : new ProcessCanceledException(e);
      }
      throw e;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private static boolean atNumber(@NotNull CompletionParameters parameters) {
    PsiElement originalPosition = parameters.getOriginalPosition();
    if (originalPosition != null) {
      ASTNode node = originalPosition.getNode();
      if (node != null) {
        ASTNode treePrev = node.getTreePrev();
        if (treePrev != null && treePrev.getElementType() != GosuElementTypes.ELEM_TYPE_NumericLiteral) {
          treePrev = treePrev.getLastChildNode();
        }
        if (treePrev != null && treePrev.getElementType() == GosuElementTypes.ELEM_TYPE_NumericLiteral) {
          ASTNode lastChildNode = treePrev.getLastChildNode();
          if (lastChildNode != null && lastChildNode.getText().equals(".")) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public void duringCompletion(@NotNull CompletionInitializationContext context) {
    Document document = context.getEditor().getDocument();
    String toReplace = document.getText().substring(context.getStartOffset(), context.getIdentifierEndOffset());
    int newLine = toReplace.indexOf("\n");
    if (newLine >= 0) {
      context.getOffsetMap().addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, context.getStartOffset() + newLine);
    }
  }
}
