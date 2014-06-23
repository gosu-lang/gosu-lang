/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnumConstant;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuEnumConstantImpl extends GosuFieldImpl implements IGosuEnumConstant, PsiPolyVariantReference {

  public GosuEnumConstantImpl(GosuCompositeElement node) {
    super(node);
  }

  public GosuEnumConstantImpl(GosuFieldStub stub) {
    super(stub, GosuElementTypes.ENUM_CONSTANT);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitEnumConstant(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  public boolean hasModifierProperty(@NonNls @NotNull String property) {
    if (property.equals(PsiModifier.STATIC)) {
      return true;
    }
    if (property.equals(PsiModifier.PUBLIC)) {
      return true;
    }
    if (property.equals(PsiModifier.FINAL)) {
      return true;
    }
    return false;
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitEnumConstant(this);
  }

  @Nullable
  public IGosuTypeElement getTypeElementGosu() {
    return null;
  }

  @NotNull
  public PsiType getType() {
    return JavaPsiFacadeUtil.getElementFactory(getProject()).createType(getContainingClass(), PsiSubstitutor.EMPTY);
  }

  public void setType(@Nullable PsiType type) {
    throw new UnsupportedOperationException("Cannot set type for enum constant.");
  }

  @Nullable
  public IGosuExpression getInitializerGosu() {
    return null;
  }

  @Nullable
  public PsiExpressionList getArgumentList() {
    throw new UnsupportedOperationException("Implmenent it yourself!");
//    return findChildByClass(IGosuArgumentList.class);
  }

  @Override
  public PsiMethod resolveMethod() {
    throw new UnsupportedOperationException("Implmenent it yourself!");
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException("Implmenent it yourself!");
  }

  @Override
  public PsiMethod resolveConstructor() {
    throw new UnsupportedOperationException("Implmenent it yourself!");
  }

  @Override
  public PsiEnumConstantInitializer getInitializingClass() {
    return null;
  }

  @NotNull
  @Override
  public PsiEnumConstantInitializer getOrCreateInitializingClass() {
    return null;
  }

//  public IGosuExpression removeArgument(final int number) {
//    final IGosuArgumentList list = getArgumentList();
//    return list != null ? list.removeArgument(number) : null;
//  }
//
//  public IGosuNamedArgument addNamedArgument(final IGosuNamedArgument namedArgument) throws IncorrectOperationException {
//    return null;
//  }
//
//  public IGosuNamedArgument[] getNamedArguments() {
//    final IGosuArgumentList argumentList = getArgumentList();
//    return argumentList == null ? IGosuNamedArgument.EMPTY_ARRAY : argumentList.getNamedArguments();
//  }
//
//  public IGosuExpression[] getExpressionArguments() {
//    final IGosuArgumentList argumentList = getArgumentList();
//    return argumentList == null ? IGosuExpression.EMPTY_ARRAY : argumentList.getExpressionArguments();
//
//  }

//  @NotNull
//  @Override
//  public IGosuResolveResult[] getCallVariants( @Nullable IGosuExpression upToArgument )
//  {
//    return multiResolveConstructorImpl( true );
//  }
//
//  @NotNull
//  @Override
//  public GosuClosableBlock[] getClosureArguments()
//  {
//    return GosuClosableBlock.EMPTY_ARRAY;
//  }

//  @Override
//  public PsiMethod resolveMethod()
//  {
//    return GosuPsiImplUtil.extractUniqueElement( multiResolveConstructor() );
//  }

  @Override
  public PsiReference getReference() {
    return this;
  }

  @NotNull
  public PsiElement getElement() {
    return this;
  }

  @NotNull
  public TextRange getRangeInElement() {
    return getNameIdentifier().getTextRange().shiftRight(-getTextOffset());
  }

  public PsiElement resolve() {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(this) {
      @Nullable
      public PsiElement execute() throws Exception {
        final IExpression asExpression = getParsedElement().getAsExpression();
        if (asExpression instanceof INewExpression) {
          IConstructorInfo constructor = ((INewExpression) asExpression).getConstructor();
          PsiElement element = PsiFeatureResolver.resolveMethodOrConstructor(constructor, getContainingClass());
          if (constructor.isDefault() && element instanceof PsiClass && ((PsiClass) element).isEnum()) {
            return null;
          }
          return element;
        }
        return null;
      }
    });
  }

  @NotNull
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    return new IGosuResolveResult[] {new GosuResolveResultImpl(resolve(), true, null)};
  }

  @NotNull
  public String getCanonicalText() {
    return getText(); //todo
  }

  @NotNull
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return getElement();
  }

  @NotNull
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    throw new IncorrectOperationException("invalid operation");
  }

  public boolean isReferenceTo(@NotNull PsiElement element) {
    return element instanceof IGosuMethod && ((IGosuMethod) element).isConstructor() && getManager().areElementsEquivalent(resolve(), element);
  }

  @NotNull
  public Object[] getVariants() {
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }

  public boolean isSoft() {
    return false;
  }
}
