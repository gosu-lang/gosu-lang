/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.filters.ClassFilter;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.classes.AnyInnerFilter;
import com.intellij.psi.filters.element.ExcludeDeclaredFilter;
import com.intellij.psi.filters.types.AssignableFromFilter;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.completion.proposals.GosuTypeCompletionProposal;
import gw.plugin.ij.completion.proposals.PrimitiveCompletionProposal;
import gw.plugin.ij.completion.proposals.PsiClassCompletionProposal;
import gw.plugin.ij.completion.proposals.RawCompletionProposal;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsesStatementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class TypeCompletionHandler extends AbstractCompletionHandler {
  private static final List<PsiPrimitiveType> PRIMITIVE_TYPES = ImmutableList.of(
      PsiType.BYTE,
      PsiType.CHAR,
      PsiType.DOUBLE, PsiType.FLOAT,
      PsiType.INT, PsiType.LONG, PsiType.SHORT,
      PsiType.BOOLEAN,
      PsiType.VOID);

  public static final PsiJavaElementPattern.Capture<PsiElement> IN_TYPE_PARAMETER = PsiJavaPatterns.psiElement()
      .afterLeaf(PsiKeyword.EXTENDS, PsiKeyword.SUPER, "&")
      .withParent(PsiJavaPatterns.psiElement(PsiReferenceList.class).withParent(PsiTypeParameter.class));

  public static final PsiJavaElementPattern.Capture<PsiElement> INSIDE_METHOD_THROWS_CLAUSE = PsiJavaPatterns.psiElement()
      .afterLeaf(PsiKeyword.THROWS, ",")
      .inside(PsiMethod.class)
      .andNot(PsiJavaPatterns.psiElement().inside(PsiCodeBlock.class))
      .andNot(PsiJavaPatterns.psiElement().inside(PsiParameterList.class));

  public static final ElementPattern<PsiElement> AFTER_THROW_NEW = psiElement()
      .afterLeaf(psiElement().withText(PsiKeyword.NEW).afterLeaf(psiElement().withText(PsiKeyword.THROW)));

  private final ElementFilter _filter;
  private int _weight;
  @NotNull
  private final PrefixMatcher _prefixMatcher;
  private final boolean _javaCtx;

  public TypeCompletionHandler(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, int weight) {
    super(parameters, result);
    _filter = getDefaultFilter(parameters.getPosition());
    _weight = weight;
    _prefixMatcher = result.getPrefixMatcher();
    _javaCtx = parameters.getPosition() instanceof PsiIdentifier;
  }

  public TypeCompletionHandler(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, ElementFilter filter, int weight) {
    super(parameters, result);
    _filter = filter;
    _weight = weight;
    _prefixMatcher = result.getPrefixMatcher();
    _javaCtx = parameters.getPosition() instanceof PsiIdentifier;
  }

  @NotNull
  private ElementFilter getDefaultFilter(PsiElement insertedElement) {
    return AFTER_THROW_NEW.accepts(insertedElement)
           ? new AssignableFromFilter("java.lang.Throwable")
           : IN_TYPE_PARAMETER.accepts(insertedElement)
             ? new ExcludeDeclaredFilter(new ClassFilter(PsiTypeParameter.class))
             : INSIDE_METHOD_THROWS_CLAUSE.accepts(insertedElement)
               ? new AnyInnerFilter(new AssignableFromFilter("java.lang.Throwable"))
               : getLocalFilter( insertedElement );
  }

  @Override
  public void handleCompletePath() {
    final PsiElement position = getContext().getPosition();
    if (position instanceof GosuIdentifierImpl) {
      if (position.getParent() instanceof GosuTypeLiteralImpl) {
        if (position.getParent().getParent() instanceof GosuUsesStatementImpl) {
          return;
        }
      }
    }

    // primitives are always available
    addPrimitives();

    addScratchpadInnerClasses(position);

//## THIS IS DOGASS SLOW, the eventual call to CustomPsiClassCache.getByShortName() is fucking molasses, don't do this here.
//    AllClassesGetter.processJavaClasses( _params, _prefixMatcher, _params.getInvocationCount() <= 1, new Consumer<PsiClass>() {
//  ...
//    } );

    addTypeLoaderTypes();
    if( getTotalCompletions() == 0 && _params.getInvocationCount() < 2 ) {
      stopFilterByInvocationCount();
      addTypeLoaderTypes();
    }
  }

  private void addTypeLoaderTypes() {
    Set<String> fqnSet = new HashSet<String>();
    for( ITypeLoader loader : TypeSystem.getAllTypeLoaders() ) {
      if( !loader.showTypeNamesInIDE() ) {
        continue;
      }
      for( CharSequence typeName : loader.getAllTypeNames() ) {
        String fqn = typeName.toString();
        int iAngle = fqn.indexOf( '<' );
        if( iAngle >= 0 ) {
          fqn = fqn.substring( 0, iAngle );
        }
        if( fqnSet.contains( fqn ) ) {
          continue;
        }
        int iDot = fqn.lastIndexOf( '.' );
        String relativeName = iDot >= 0 ? fqn.substring( iDot + 1 ) : fqn;
        if( _prefixMatcher.prefixMatches( relativeName ) ) {
          if( _filter == null || _filter.isAcceptable( fqn, _params.getPosition() ) ) {
            IType type = TypeSystem.getByFullNameIfValid( fqn, loader.getModule() );
            if( type == null ) {
              continue;
            }

            // Note this second cache lookup exists because there is a many-to-one mapping
            // between type names and ITypes e.g., several type names may map to an entity type.
            fqn = type.getName();
            if( fqnSet.contains( fqn ) ) {
              continue;
            }
            fqnSet.add( fqn );

            addCompletion( new GosuTypeCompletionProposal( type, getContext().getPosition(), _weight ), _javaCtx );
          }
        }
      }
    }
  }

  private void addScratchpadInnerClasses(@Nullable PsiElement position) {
    if (position == null) {
      return;
    }

    PsiFile psiFile = position.getContainingFile();
    if (!(psiFile instanceof GosuScratchpadFileImpl)) {
      return;
    }

    GosuScratchpadFileImpl scratchpadFile = (GosuScratchpadFileImpl) psiFile;
    for (PsiClass cls : scratchpadFile.getPsiClass().getInnerClasses()) {
      if (cls.getName().startsWith(_prefixMatcher.getPrefix())) {
        addCompletion(new PsiClassCompletionProposal(cls, _weight), _javaCtx);
      }
    }
  }

  private void addPrimitives() {
    for (PsiType type : PRIMITIVE_TYPES) {
      if (type.getCanonicalText().startsWith(_prefixMatcher.getPrefix())) {
        addCompletion(new PrimitiveCompletionProposal(type), _javaCtx);
      }
    }

    if ("block".startsWith(_prefixMatcher.getPrefix())) {
      addCompletion(new RawCompletionProposal("block"));
    }
  }
}