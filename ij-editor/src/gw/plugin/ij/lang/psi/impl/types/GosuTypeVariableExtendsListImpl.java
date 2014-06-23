/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.types;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReferenceList;
import gw.lang.parser.statements.ITypeVariableExtendsListClause;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.GosuClassReferenceType;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuTypeVariableExtendsListImpl extends GosuPsiElementImpl<ITypeVariableExtendsListClause> implements IGosuPsiElement, PsiReferenceList {
  public GosuTypeVariableExtendsListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  public PsiJavaCodeReferenceElement[] getReferenceElements() {
    return PsiJavaCodeReferenceElement.EMPTY_ARRAY;
  }

  @NotNull
  public PsiClassType[] getReferencedTypes() {
    final IGosuCodeReferenceElement[] refs = findChildrenByClass(IGosuCodeReferenceElement.class);
    PsiClassType[] result = new PsiClassType[refs.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = new GosuClassReferenceType(refs[i]);
    }
    return result;
  }

  @NotNull
  public Role getRole() {
    return Role.EXTENDS_BOUNDS_LIST;
  }

  @NotNull
  public String toString() {
    return "Type extends bounds list";
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeVariableExtendsList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
