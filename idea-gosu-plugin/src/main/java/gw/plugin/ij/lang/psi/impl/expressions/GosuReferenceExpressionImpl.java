/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.gs.ICompilableType;
import gw.plugin.ij.codeInsight.GosuTargetElementEvaluator;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GosuReferenceExpressionImpl<T extends IExpression> extends GosuPsiElementImpl<T> implements IGosuReferenceExpression {
  public GosuReferenceExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  public PsiReference getReference() {
    return this;
  }

  public String getReferenceName() {
    final PsiElement element = getReferenceNameElement();
    return element != null ? element.getText() : null;
  }

  @Nullable
  abstract public PsiElement getReferenceNameElement();

  @NotNull
  public PsiElement getElement() {
    return this;
  }

  public TextRange getRangeInElement() {
    final PsiElement refNameElement = getReferenceNameElement();
    if (refNameElement != null) {
      final int offsetInParent = refNameElement.getStartOffsetInParent();
      return new TextRange(offsetInParent, offsetInParent + refNameElement.getTextLength());
    }
    return new TextRange(0, getTextLength());
  }

  @Override
  public PsiType getType() {
    return ExecutionUtil.execute(new SafeCallable<PsiType>(this) {
      public PsiType execute() throws Exception {
        IType type = getTypeReferenced();
        if (type instanceof ITypeVariableType) {
          PsiElement typeVariable = null;
          if (type.getEnclosingType() instanceof IFunctionType) {
            for (PsiElement parent = getParent(); typeVariable == null && parent != null; parent = parent.getParent()) {
              if (parent instanceof PsiTypeParameterListOwner) {
                typeVariable = PsiTypeResolver.findTypeVariable((ITypeVariableType) type, (PsiTypeParameterListOwner) parent);
              }
            }
          } else {
            PsiElement enclosingType = PsiTypeResolver.resolveType(type.getEnclosingType(), GosuReferenceExpressionImpl.this);
            if( enclosingType != null ) {
              typeVariable = PsiTypeResolver.findTypeVariable((ITypeVariableType) type, (PsiTypeParameterListOwner) enclosingType);
            }
          }
          return typeVariable == null ? null : JavaPsiFacadeUtil.getElementFactory(getProject()).createType((PsiClass) typeVariable);
        } else {
          if (type instanceof ICompilableType && ((ICompilableType) type).isAnonymous()) {
            IType superType = type.getSupertype();
            if (superType == null) {
              superType = type.getInterfaces()[0];
            }
            type = superType;
          }

          PsiType psiType = null;
          if (type instanceof IBlockType && !getNode().getText().contains(":")) {
            // handle case where the block type has a default void return type: block(...) vs block(...) : void
            psiType = createType(type, getNode().getPsi());
          } else {
            psiType = createType(type, getNameIdentifierImpl());
          }
          return psiType;
        }
      }
    });
  }

  @Nullable
  protected IType getTypeReferenced() {
    IParsedElement parsedElement = getParsedElementImpl();
    if (parsedElement instanceof IExpression) {
      return ((IExpression) parsedElement).getType();
    } else {
      return null;
    }
  }

  public boolean isAssigned() {
    IElementType parent = getParent().getNode().getElementType();
    return parent == GosuElementTypes.ELEM_TYPE_AssignmentStatement ||
        parent == GosuElementTypes.ELEM_TYPE_MemberAssignmentStatement ||
        (getParent().getParent() != null && getParent().getParent().getNode().getElementType() == GosuElementTypes.ELEM_TYPE_ObjectInitializerExpression);
  }

  protected PsiElement handleElementRenameInner(String newElementName) throws IncorrectOperationException {
    PsiElement nameElement = getReferenceNameElement();
    if (nameElement != null) {
      ASTNode node = nameElement.getNode();
      ASTNode newNameNode = GosuPsiParseUtil.createReferenceNameFromText(this, newElementName).getNode();
      CodeEditUtil.setOldIndentation((TreeElement) newNameNode, 0); // this is to avoid a stupid exception
      node.getTreeParent().replaceChild(node, newNameNode);
    }

    return this;
  }

  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return handleElementRenameInner(newElementName);
  }

  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
//    throw new UnsupportedOperationException("Men at work");
    return this;
  }

  @NotNull
  public String getCanonicalText() {
    return getRangeInElement().substring(getElement().getText());
  }

  public boolean isReferenceTo(final PsiElement element) {
    ProgressManager.checkCanceled();

    return ExecutionUtil.execute(new SafeCallable<Boolean>(this) {
      public Boolean execute() throws Exception {
        PsiElement resolveResult = resolve();
        PsiElement referenceTo = GosuTargetElementEvaluator.correctSearchTargets(resolveResult);
        if (getManager().areElementsEquivalent(element, referenceTo)) {
          return true;
        }
        if (referenceTo instanceof PsiNameIdentifierOwner) {
          final PsiElement identifier = ((PsiNameIdentifierOwner) referenceTo).getNameIdentifier();
          if (identifier == null) {
            return false;
          }
          if (getManager().areElementsEquivalent(element, identifier)) {
            return true;
          }

          PsiElement child = identifier.getFirstChild();
          while (child != null) {
            if (child.getNode().getElementType() == GosuElementTypes.TT_IDENTIFIER &&
                getManager().areElementsEquivalent(element, child)) {
              return true;
            }
            child = child.getNextSibling();
          }
        }
        return false;
      }
    });
  }

  @NotNull
  public Object[] getVariants() {
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  public boolean isSoft() {
    return false;
  }

  @NotNull
  public IGosuResolveResult[] multiResolve(boolean incomplete) {
    final PsiElement element = resolve();
    return element != null ? new IGosuResolveResult[]{new GosuResolveResultImpl(element, !incomplete, null)} : IGosuResolveResult.EMPTY_ARRAY;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitReferenceExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
