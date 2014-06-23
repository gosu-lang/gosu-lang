/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IModifierListClause;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuModifierListImpl extends GosuPsiElementImpl<IModifierListClause> implements IGosuModifierList {
  private static final Logger LOG = Logger.getInstance("#gw.plugin.ij.lang.psi.impl.statements.params.GosuModifierListImpl");

  public GosuModifierListImpl( @NotNull GosuCompositeElement node ) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitModifierList( this );
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitModifierList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  @NotNull
  public PsiElement[] getModifiers() {
    final PsiElement[] modifiersKeywords = findChildrenByType( GosuTokenSets.MODIFIERS, PsiElement.class);
    final IGosuAnnotation[] modifiersAnnotations = findChildrenByClass(IGosuAnnotation.class);

    if (modifiersAnnotations.length == 0) {
      return modifiersKeywords;
    } else {
      final PsiElement[] result = new PsiElement[modifiersAnnotations.length + modifiersKeywords.length];

      int i = 0;
      for (PsiElement keyword : modifiersKeywords) {
        result[i++] = keyword;
      }

      for (IGosuAnnotation annotation : modifiersAnnotations) {
        result[i++] = annotation;
      }

      return result;
    }
  }

  public boolean hasExplicitVisibilityModifiers() {
    return findChildByType(GosuTokenSets.VISIBILITY_MODIFIERS) != null;
  }

  public boolean hasModifierProperty(@NotNull @NonNls String modifier) {
    final PsiElement parent = getParent();
    if (parent instanceof IGosuVariable &&
        parent.getParent() instanceof IGosuTypeDefinition ) {
      PsiElement pParent = parent.getParent().getParent();
      if (!hasExplicitVisibilityModifiers()) { //properties are backed by private fields
        if (pParent instanceof PsiClass && ((PsiClass) pParent).isInterface()) {
          if (modifier.equals( IGosuModifier.STATIC)) {
            return true;
          }
          if (modifier.equals(IGosuModifier.FINAL)) {
            return true;
          }
        } else {
          if (modifier.equals(IGosuModifier.PRIVATE)) {
            return true;
          }
          if (modifier.equals(IGosuModifier.PROTECTED)) {
            return false;
          }
          if (modifier.equals(IGosuModifier.PUBLIC)) {
            return false;
          }
        }
      }
    }

    if (hasExplicitModifier(modifier)) {
      return true;
    }

    if (modifier.equals(IGosuModifier.PUBLIC)) {
      // Gosu type definitions and methods are public by default
      return !hasExplicitModifier(IGosuModifier.PRIVATE) &&
             !hasExplicitModifier(IGosuModifier.PROTECTED) &&
             !hasExplicitModifier(IGosuModifier.INTERNAL);
    }

    if (parent instanceof IGosuTypeDefinition) {
      if (modifier.equals(IGosuModifier.STATIC)) {
        final PsiClass containingClass = ((IGosuTypeDefinition) parent).getContainingClass();
        return containingClass != null && containingClass.isInterface();
      }
      if (modifier.equals(IGosuModifier.ABSTRACT)) {
        return ((IGosuTypeDefinition) parent).isInterface();
      }
    }

    return false;
  }

  public boolean hasExplicitModifier(@NotNull @NonNls String name) {
    if (name.equals(IGosuModifier.PUBLIC)) {
      return findChildByType( GosuElementTypes.TT_public) != null;
    }
    if (name.equals(IGosuModifier.ABSTRACT)) {
      return findChildByType(GosuElementTypes.TT_abstract) != null;
    }
    if (name.equals(IGosuModifier.PRIVATE)) {
      return findChildByType(GosuElementTypes.TT_private) != null;
    }
    if (name.equals(IGosuModifier.PROTECTED)) {
      return findChildByType(GosuElementTypes.TT_protected) != null;
    }
    if (name.equals(IGosuModifier.PACKAGE_LOCAL) || name.equals(IGosuModifier.INTERNAL)) {
      return findChildByType(GosuElementTypes.TT_internal) != null;
    }

    if (name.equals(IGosuModifier.STATIC)) {
      return findChildByType(GosuElementTypes.TT_static) != null;
    }
    if (name.equals(IGosuModifier.FINAL)) {
      return findChildByType(GosuElementTypes.TT_final) != null;
    }
    if (name.equals(IGosuModifier.TRANSIENT)) {
      return findChildByType(GosuElementTypes.TT_transient) != null;
    }
    return false;
  }

  public void setModifierProperty(@NotNull @NonNls String name, boolean doSet) throws IncorrectOperationException {
    if (hasModifierProperty(name) == doSet) {
      return;
    }

    if (doSet) {
      if (IGosuModifier.PRIVATE.equals(name) ||
          IGosuModifier.PROTECTED.equals(name) ||
          IGosuModifier.PUBLIC.equals(name) ||
          IGosuModifier.PACKAGE_LOCAL.equals(name)) {
        setModifierPropertyInternal(IGosuModifier.PUBLIC, false);
        setModifierPropertyInternal(IGosuModifier.PROTECTED, false);
        setModifierPropertyInternal(IGosuModifier.INTERNAL, false);
        setModifierPropertyInternal(IGosuModifier.PRIVATE, false);
      }
    }
    setModifierPropertyInternal(name, doSet);
  }

  private void setModifierPropertyInternal(@NotNull String name, boolean doSet) {
    if (doSet) {
      final ASTNode modifierNode = GosuPsiParseUtil.createModifierFromText( name, getManager() ).getNode();
      addInternal(modifierNode, modifierNode, null, false);
    } else {
      final PsiElement[] modifiers = findChildrenByType(GosuTokenSets.MODIFIERS, PsiElement.class);
      for (PsiElement modifier : modifiers) {
        if (name.equals(modifier.getText())) {
          deleteChildRange(modifier, modifier);
          break;
        }
      }
    }
  }

  public void checkSetModifierProperty(@NotNull @NonNls String name, boolean value) throws IncorrectOperationException {
  }

  @NotNull
  public IGosuAnnotation[] getAnnotations() {
    return findChildrenByClass(IGosuAnnotation.class);
  }

  @NotNull
  public PsiAnnotation[] getApplicableAnnotations() {
    return getAnnotations();
  }

  @Nullable
  public PsiAnnotation findAnnotation(@NotNull @NonNls String qualifiedName) {
    PsiElement child = getFirstChild();
    while (child != null) {
      if (child instanceof PsiAnnotation && qualifiedName.equals(((PsiAnnotation) child).getQualifiedName())) {
        return (PsiAnnotation) child;
      }
      child = child.getNextSibling();
    }
    return null;
  }

  @NotNull
  public IGosuAnnotation addAnnotation(@NotNull @NonNls String qualifiedName) {
    throw new UnsupportedOperationException("This operation does not make sense for Gosu");
  }
}
