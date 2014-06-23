/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IExpansionPropertyInfo;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.util.ElementTypeMatcher;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GosuFieldAccessExpressionImpl extends GosuReferenceExpressionImpl<IMemberAccessExpression>
  implements IGosuCodeReferenceElement, IGosuTypeElement, MultiRangeReference {

  private static final String[] PROP_PREFIXES = {"get", "set", "is"};

  public GosuFieldAccessExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    PsiElement child = findLastChildByType( GosuTokenTypes.TT_IDENTIFIER );
    if( child == null ) {
      // Could be a non-reserved keyword
      child = getLastChild();
      if( child instanceof GosuTokenImpl ) {
        return child;
      }
    }
    return child;
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    final PsiElement firstChild = getFirstChild();
    return firstChild instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) firstChild : null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  public IGosuTypeParameterList getTypeParameterList() {
    return null;
  }

  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Override
  public PsiElement resolve() {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(this) {
      @Nullable
      public PsiElement execute() throws Exception {
        IMemberAccessExpression parsedElement = getParsedElement();
        if (parsedElement == null) {
          return null;
        }
        IType type = parsedElement.getType();
        if (type instanceof INamespaceType) {
          String packageName = type.getName();
          PsiPackage aPackage = JavaPsiFacade.getInstance(getProject()).findPackage(packageName);
          return (aPackage == null || !aPackage.isValid()) ? null : aPackage;
        }
        final IPropertyInfo pi = getPropertyInfo(parsedElement);
        return pi != null ? PsiFeatureResolver.resolveProperty(pi, GosuFieldAccessExpressionImpl.this) : null;
      }
    });
  }

  @Nullable
  private IPropertyInfo getPropertyInfo(IMemberAccessExpression parsedElement) {
    try {
      IPropertyInfo pi = parsedElement.getPropertyInfo();
      // dlank: Moved the delegation resolution to the resolveProperty() method so that delegating property infos would
      // have a chance to resolve to a different place than the property to which they are delegating.
//      while (pi instanceof IPropertyInfoDelegate) {
//        pi = ((IPropertyInfoDelegate) pi).getSource();
//      }
      while (pi instanceof IExpansionPropertyInfo) {
        pi = ((IExpansionPropertyInfo) pi).getDelegate();
      }
      return pi;

    } catch (RuntimeException e) {
      // No property exists, pe is errant
      return null;
    }
  }

  //TODO-dp consider adding an extension point for these kinds of special cases

  protected PsiElement handleElementRenameInner(String newElementName) throws IncorrectOperationException {
    newElementName = makePropertyName( newElementName );
    return super.handleElementRenameInner(newElementName);
  }

  private String makePropertyName( String name ) {
    for( String prefix: PROP_PREFIXES ) {
      if( name.length() > prefix.length() &&
          name.startsWith( prefix ) &&
          Character.isUpperCase( name.charAt( prefix.length() ) ) ) {
        return name.substring( prefix.length() );
      }
    }
    return name;
  }

  @Override
  public List<TextRange> getRanges() {
    return Collections.singletonList(getRangeInElement());
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
//    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
//      ((JavaElementVisitor)visitor).visitCallExpression( this );
//    } else
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitFieldAccessExpression(this);
    } else {
      visitor.visitElement( this );
    }
  }
}
