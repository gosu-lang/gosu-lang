/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiAnnotationOwner;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.java.PsiAnnotationParamListImpl;
import com.intellij.psi.meta.PsiMetaData;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuAnnotationExpressionImpl extends GosuNewExpressionImpl implements IGosuAnnotation {
  public GosuAnnotationExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  @Override
  public String getShortName() {
    final PsiType type = getType();
    if (type instanceof PsiClassReferenceType) {
      final PsiClass klass = ((PsiClassReferenceType) type).resolve();
      if (klass != null) {
        return klass.getName();
      }
    }

    return null;
  }

  @Override
  public String getQualifiedName() {
    final PsiType type = getType();
    if (type instanceof PsiClassReferenceType) {
      final PsiClass klass = ((PsiClassReferenceType) type).resolve();
      if (klass != null) {
        return klass.getQualifiedName();
      }
    }

    return null;
  }

  public PsiClass resolve() {
    final PsiType type = getType();
    if (type instanceof PsiClassReferenceType) {
      final PsiClass klass = ((PsiClassReferenceType) type).resolve();
      return klass;
    }
    return null;
  }

  @NotNull
  @Override
  public PsiAnnotationParameterList getParameterList() {
    // This is incorect but we have no another implementation
    return new PsiAnnotationParamListImpl(getNode());
  }

  @Override
  public PsiJavaCodeReferenceElement getNameReferenceElement() {
    return null;  
  }

  @Override
  public PsiAnnotationMemberValue findAttributeValue(@NonNls String attributeName) {
    return null;  
  }

  @Override
  public PsiAnnotationMemberValue findDeclaredAttributeValue(@NonNls String attributeName) {
    return null;  
  }

  @Nullable
  @Override
  public <T extends PsiAnnotationMemberValue> T setDeclaredAttributeValue(@NonNls String attributeName, @Nullable T value) {
    return null;  
  }

  @Nullable
  @Override
  public PsiAnnotationOwner getOwner() {
    return null;  
  }

  @Override
  public PsiMetaData getMetaData() {
    return null;  
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitAnnotation(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
