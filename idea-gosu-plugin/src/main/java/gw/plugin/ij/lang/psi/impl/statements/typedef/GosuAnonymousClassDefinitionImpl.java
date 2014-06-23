/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiNewExpression;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;

public class GosuAnonymousClassDefinitionImpl extends GosuTypeDefinitionImpl implements IGosuAnonymousClassDefinition {
  public GosuAnonymousClassDefinitionImpl(GosuCompositeElement node) {
    super(node);
  }

  public GosuAnonymousClassDefinitionImpl(final GosuTypeDefinitionStub stub) {
    super(stub, GosuElementTypes.ANONYMOUS_CLASS_DEFINITION);
  }

  @Override
  public boolean isAnonymous() {
    return true;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitAnonymousClassDefinition(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  @NotNull
  public String[] getSuperClassNames() {
//    final GosuTypeDefinitionStub stub = getStub();
//    if( stub != null )
//    {
//      return stub.getSuperClassNames();
//    }
    GosuBaseElementImpl parent = (GosuBaseElementImpl) getParent();
    PsiIdentifier nameIdentifier = parent.getNameIdentifierImpl();
    return new String[] {nameIdentifier.getText()};
  }

  private IType getBaseType() {
    IClassStatement parsedElement = getParsedElement();
    IGosuClass gosuClass = parsedElement.getGosuClass();
    IType supertype = gosuClass.getSupertype();
    if (supertype == null) {
      supertype = gosuClass.getInterfaces()[0];
    }
    return supertype;
  }

  @Override
  public PsiExpressionList getArgumentList() {
    throw new UnsupportedOperationException("Not yet implemented, do it yourself!");
  }

  @NotNull
  @Override
  public PsiJavaCodeReferenceElement getBaseClassReference() {
    final PsiElementFactory factory = JavaPsiFacadeUtil.getElementFactory(getProject());
    return factory.createReferenceElementByType(getBaseClassType());
  }

  @NotNull
  @Override
  public PsiClassType getBaseClassType() {
    IType baseType = getBaseType();
    return (PsiClassType) createType(baseType);
  }

  @Override
  public boolean isInQualifiedNew() {
    final GosuTypeDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.isAnonymousInQualifiedNew();
    }

    final PsiElement parent = getParent();
    return parent instanceof PsiNewExpression && ((PsiNewExpression)parent).getQualifier() != null;
  }

  @Override
  public boolean isUnder(IGosuTypeDefinition psiClass) {
    PsiClass d = this;
    while (d != null) {
      if (d == psiClass) {
        return true;
      }
      d = d.getContainingClass();
    }
    return false;
  }

//  public String getName() {
//    final GosuTypeDefinitionStub stub = getStub();
//    if (stub != null) {
//      return stub.getName();
//    }
//    IClassStatement classStatement = getParsedElement();
//    return classStatement.getGosuClass().getRelativeName();
//  }
}
