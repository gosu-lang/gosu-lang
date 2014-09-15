/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.ui.RowIcon;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IVarStatement;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import gw.plugin.ij.lang.psi.util.GosuDocUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuFieldImpl extends GosuVariableBaseImpl<IVarStatement, GosuFieldStub> implements IGosuField {
  public GosuFieldImpl(GosuCompositeElement node) {
    super(node);
  }

  public GosuFieldImpl(GosuFieldStub stub) {
    this(stub, GosuElementTypes.FIELD);
  }

  public GosuFieldImpl(GosuFieldStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitField(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor) visitor).visitField(this);
    } else {
      visitor.visitElement(this);
    }
  }

  public void setInitializer(@Nullable PsiExpression psiExpression) throws IncorrectOperationException {
  }

  public boolean isDeprecated() {
    final GosuFieldStub stub = getStub();
    boolean byDocTag = stub == null ? PsiImplUtil.isDeprecatedByDocTag(this) : stub.isDeprecatedByDocTag();
    if (byDocTag) {
      return true;
    }

    return GosuPsiImplUtil.isDeprecatedByAnnotation(this);
  }

  public PsiClass getContainingClass() {
    PsiElement parent = getParent();
    if (parent instanceof PsiClass) {
      return (PsiClass) parent;
    }

    final PsiFile file = getContainingFile();
    if (file instanceof IGosuFileBase) {
      return ((IGosuFileBase) file).getPsiClass();
    }

    return null;
  }

  @Override
  public boolean hasModifierProperty(@NotNull @NonNls String modifier) {
    IGosuModifierList modifierList = getModifierList();
    if (modifier.equals(IGosuModifier.PRIVATE)) {
      // Gosu fields are private by default
      return modifierList != null &&
          !modifierList.hasExplicitModifier(IGosuModifier.PUBLIC) &&
          !modifierList.hasExplicitModifier(IGosuModifier.PROTECTED) &&
          !modifierList.hasExplicitModifier(IGosuModifier.INTERNAL);
    }

    if (modifier.equals(IGosuModifier.PUBLIC)) {
      return modifierList != null && modifierList.hasExplicitModifier(IGosuModifier.PUBLIC);
    }

    return modifierList != null && modifierList.hasModifierProperty(modifier);
  }

  @NotNull
  public SearchScope getUseScope() {
    return PsiImplUtil.getMemberUseScope(this);
  }

  @NotNull
  @Override
  public String getName() {
    final GosuFieldStub stub = getStub();
    return stub != null ? stub.getName() : super.getName();
  }

  @Nullable
  @Override
  public String getPropertyName() {
    final IVarStatement parsedElement = getParsedElement();
    String propertyName = parsedElement == null ? null : parsedElement.getPropertyName();
    return propertyName == null ? null : propertyName.toString();
  }

  @Nullable
  public PsiElement getPropertyElement() {
    if (getPropertyName() == null) {
      return null;
    }
    GosuFieldPropertyImpl fieldProperty = (GosuFieldPropertyImpl) findChildByType(GosuElementTypes.FIELD_PROPERTY);
    if (fieldProperty != null) {
      return fieldProperty;
    }
    throw new IllegalStateException("Failed to find element for property: " + getPropertyName());
  }

  @Override
  public ColoredItemPresentation getPresentation() {
    return new ColoredItemPresentation() {
      public String getPresentableText() {
        return getName();
      }

      @Nullable
      public String getLocationString() {
        PsiClass clazz = getContainingClass();
        if (clazz == null) {
          return "";
        }
        String name = clazz.getQualifiedName();
        assert name != null;
        return "(in " + name + ")";
      }

      @Nullable
      public Icon getIcon(boolean open) {
        return GosuFieldImpl.this.getIcon(ICON_FLAG_VISIBILITY | ICON_FLAG_READ_STATUS);
      }

      @Nullable
      public TextAttributesKey getTextAttributesKey() {
        if (isDeprecated()) {
          return CodeInsightColors.DEPRECATED_ATTRIBUTES;
        }
        return null;
      }
    };
  }

  @NotNull
  public PsiElement getOriginalElement() {
    final PsiClass containingClass = getContainingClass();
    if (containingClass == null) {
      return this;
    }
    PsiClass originalClass = (PsiClass) containingClass.getOriginalElement();
    PsiField originalField = originalClass.findFieldByName(getName(), false);
    return originalField != null ? originalField : this;
  }

  public PsiDocComment getDocComment() {
    return GosuDocUtil.findDocCommnentNode(getNode());
  }

  @Override
  public Icon getElementIcon(@IconFlags int flags) {
    final RowIcon baseIcon = ElementPresentationUtil.createLayeredIcon(GosuIcons.FIELD, this, false);
    return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
  }
}
