/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.types;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.HierarchicalMethodSignature;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterList;
import com.intellij.psi.PsiTypeParameterListOwner;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.InheritanceImplUtil;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.ITypeVariableDefinitionExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuExtendsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMembersDeclaration;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariableList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.lang.psi.util.GosuClassImplUtil;
import gw.plugin.ij.lang.psi.util.GosuDocUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 */
public class GosuTypeVariableImpl extends GosuPsiElementImpl<ITypeVariableDefinitionExpression> implements IGosuTypeVariable {
  public GosuTypeVariableImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public IGosuExtendsClause getExtendsClause() {
    return null;
  }

  @Nullable
  public IGosuImplementsClause getImplementsClause() {
    return null;
  }

  @NotNull
  public String[] getSuperClassNames() {
    final PsiReference[] types = getExtendsList().getReferences();
    List<String> names = new ArrayList<>(types.length);
    for (PsiReference type : types) {
      names.add(type.getCanonicalText());
    }
    return ArrayUtil.toStringArray(names);
  }

  @NotNull
  public PsiMethod[] findCodeMethodsByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsByName(this, name, checkBases);
  }

  @NotNull
  public PsiMethod[] findCodeMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsBySignature(this, patternMethod, checkBases);
  }

  @Nullable
  public LeafPsiElement getGosuLBrace() {
    return null;
  }

  @Nullable
  public LeafPsiElement getGosuRBrace() {
    return null;
  }

  @NotNull
  public <T extends IGosuMembersDeclaration> T addMemberDeclaration(T decl, PsiElement anchorBefore) throws IncorrectOperationException {
    throw new UnsupportedOperationException("Cannot add member declaration to GosuTypeVariable");
  }

  @NotNull
  public String toString() {
    return "Type variable";
  }

  @Nullable
  @NonNls
  public String getQualifiedName() {
    return null;
  }

  public boolean isInterface() {
    return false;
  }

  public boolean isAnonymous() {
    return false;
  }

  @Override
  public boolean isEnhancement() {
    return false;
  }

  public boolean isAnnotationType() {
    return false;
  }

  public boolean isEnum() {
    return false;
  }

  @NotNull
  public PsiReferenceList getExtendsList() {
    final GosuTypeVariableExtendsListImpl list = findChildByClass(GosuTypeVariableExtendsListImpl.class);
    assert list != null;
    return list;
  }

  @Nullable
  public PsiReferenceList getImplementsList() {
    return null;
  }

  @NotNull
  public PsiClassType[] getExtendsListTypes() {
    return getExtendsList().getReferencedTypes();
  }

  @NotNull
  public PsiClassType[] getImplementsListTypes() {
    return PsiClassType.EMPTY_ARRAY;
  }

  @Nullable
  public PsiClass getSuperClass() {
    return GosuClassImplUtil.getSuperClass(this);
  }

  @NotNull
  public PsiClass[] getInterfaces() {
    return PsiClass.EMPTY_ARRAY;
  }

  @NotNull
  public PsiClass[] getSupers() {
    return GosuClassImplUtil.getSupers(this);
  }

  @NotNull
  public PsiClassType[] getSuperTypes() {
    return GosuClassImplUtil.getSuperTypes(this);
  }

  @NotNull
  public IGosuField[] getFields() {
    return IGosuField.EMPTY_ARRAY;
  }

  @NotNull
  public PsiMethod[] getMethods() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  public PsiMethod[] getConstructors() {
    return PsiMethod.EMPTY_ARRAY;
  }

  @NotNull
  public PsiClass[] getInnerClasses() {
    return PsiClass.EMPTY_ARRAY;
  }

  @NotNull
  public PsiClassInitializer[] getInitializers() {
    return PsiClassInitializer.EMPTY_ARRAY;
  }

  @NotNull
  public PsiField[] getAllFields() {
    return GosuClassImplUtil.getAllFields(this);
  }

  @NotNull
  public PsiMethod[] getAllMethods() {
    return GosuClassImplUtil.getAllMethods(this);
  }

  @NotNull
  public PsiClass[] getAllInnerClasses() {
    return PsiClass.EMPTY_ARRAY;
  }

  @Nullable
  public PsiField findFieldByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findFieldByName(this, name, checkBases);
  }

  @Nullable
  public PsiMethod findMethodBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findMethodBySignature(this, patternMethod, checkBases);
  }

  @NotNull
  public PsiMethod[] findMethodsBySignature(PsiMethod patternMethod, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsBySignature(this, patternMethod, checkBases);
  }

  @NotNull
  public PsiMethod[] findMethodsByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findCodeMethodsByName(this, name, checkBases);
  }

  @NotNull
  public List<Pair<PsiMethod, PsiSubstitutor>> findMethodsAndTheirSubstitutorsByName(@NonNls String name, boolean checkBases) {
    return GosuClassImplUtil.findMethodsAndTheirSubstitutorsByName(this, name, checkBases);
  }

  @NotNull
  public List<Pair<PsiMethod, PsiSubstitutor>> getAllMethodsAndTheirSubstitutors() {
    return GosuClassImplUtil.getAllMethodsAndTheirSubstitutors(this);
  }

  @Nullable
  public PsiClass findInnerClassByName(@NonNls String name, boolean checkBases) {
    return null;
  }

  @Nullable
  public PsiJavaToken getLBrace() {
    return null;
  }

  @Nullable
  public PsiJavaToken getRBrace() {
    return null;
  }

  @Nullable
  public PsiIdentifier getNameIdentifier() {
    PsiIdentifier id = findElement(this, PsiIdentifier.class);
    if(id != null) {
      PsiElement firstChild = id.getFirstChild();
      if(firstChild instanceof PsiIdentifier) {
        // Always return the leaf token node; we always want to patch in just the name and not mess with upper-level tree nodes
        id = (PsiIdentifier) firstChild;
      }
    }
    return id;
  }

  @Nullable
  public PsiElement getScope() {
    return null;
  }

  public boolean isInheritor(@NotNull PsiClass baseClass, boolean checkDeep) {
    return InheritanceImplUtil.isInheritor(this, baseClass, checkDeep);
  }

  public boolean isInheritorDeep(@NotNull PsiClass baseClass, @Nullable PsiClass classToByPass) {
    return InheritanceImplUtil.isInheritorDeep(this, baseClass, classToByPass);
  }

  @Nullable
  public PsiClass getContainingClass() {
    return null;
  }

  @NotNull
  public Collection<HierarchicalMethodSignature> getVisibleSignatures() {
    return Collections.emptyList(); //todo
  }

  public PsiTypeParameterListOwner getOwner() {
    final PsiElement parent = getParent();
    if (parent == null) {
      throw new PsiInvalidElementAccessException(this);
    }
    final PsiElement parentParent = parent.getParent();
    if (parentParent != null && !(parentParent instanceof PsiTypeParameterListOwner)) {
      throw new AssertionError("CCE: " + parentParent);
    }
    return (PsiTypeParameterListOwner) parentParent;
  }

  public int getIndex() {
    final IGosuTypeVariableList list = (IGosuTypeVariableList) getParent();
    return list.getTypeParameterIndex(this);
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    throw new UnsupportedOperationException("Men at work");
//    GosuPsiImplUtil.setName( name, getNameIdentifierGosu() );
//    return this;
  }

  @Nullable
  public PsiModifierList getModifierList() {
    return null;
  }

  public boolean hasModifierProperty(@NonNls @NotNull String name) {
    return false;
  }

  @Nullable
  public PsiDocComment getDocComment() {
    return GosuDocUtil.findDocCommnentNode(getNode());
  }

  public boolean isDeprecated() {
    return GosuPsiImplUtil.isDeprecatedByAnnotation(this) ||  PsiImplUtil.isDeprecatedByDocTag(this);
  }

  public boolean hasTypeParameters() {
    return false;
  }

  @Nullable
  public PsiTypeParameterList getTypeParameterList() {
    return null;
  }

  @NotNull
  public PsiTypeParameter[] getTypeParameters() {
    return PsiTypeParameter.EMPTY_ARRAY;
  }

  public String getName() {
    PsiIdentifier nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? null : nameIdentifier.getText();
  }

  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    return GosuClassImplUtil.processDeclarations(this, processor, state, lastParent, place);
  }

  @NotNull
  public IGosuAnnotation[] getAnnotations() {
    return IGosuAnnotation.EMPTY_ARRAY;
  }

  public PsiAnnotation findAnnotation(@NotNull @NonNls String qualifiedName) {
    return null;
  }

  @NotNull
  public PsiAnnotation addAnnotation(@NotNull @NonNls String qualifiedName) {
    throw new IncorrectOperationException();
  }

  @NotNull
  public PsiAnnotation[] getApplicableAnnotations() {
    return getAnnotations();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeVariable(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
