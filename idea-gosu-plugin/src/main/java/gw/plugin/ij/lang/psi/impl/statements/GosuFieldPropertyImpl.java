/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.ui.RowIcon;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.statements.IGosuFieldProperty;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuDeclaredElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.lang.psi.stubs.GosuFieldPropertyStub;
import gw.plugin.ij.lang.psi.util.GosuDocUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuFieldPropertyImpl extends GosuDeclaredElementImpl<INameInDeclaration, GosuFieldPropertyStub> implements IGosuFieldProperty {
  public GosuFieldPropertyImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public GosuFieldPropertyImpl(@NotNull GosuFieldPropertyStub stub) {
    this(stub, GosuElementTypes.FIELD_PROPERTY);
  }

  public GosuFieldPropertyImpl(@NotNull GosuFieldPropertyStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitElement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitFieldProperty(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  public void setInitializer(@Nullable PsiExpression psiExpression) throws IncorrectOperationException {
  }

  public boolean isDeprecated() {
    final GosuFieldPropertyStub stub = getStub();
    final boolean deprecatedByDocTag = stub != null ? stub.isDeprecatedByDocTag() : PsiImplUtil.isDeprecatedByDocTag(this);
    return deprecatedByDocTag && GosuPsiImplUtil.isDeprecatedByAnnotation(this);
  }

  @Nullable
  public PsiClass getContainingClass() {
    final PsiElement parent = getParent();
    if (parent != null && parent.getParent() instanceof PsiClass) {
      return (PsiClass) parent.getParent(); // the parent is the Field, it's parent is the class
    }

    final PsiFile file = getContainingFile();
    if (file instanceof IGosuFileBase) {
      return ((IGosuFileBase) file).getPsiClass();
    }

    return null;
  }

  @Override
  public boolean hasModifierProperty(@NotNull @NonNls String modifier) {
    return modifier.equals( IGosuModifier.PUBLIC );
  }

  @NotNull
  public SearchScope getUseScope() {
    return PsiImplUtil.getMemberUseScope(this);
  }

  @NotNull
  @Override
  public String getName() {
    final GosuFieldPropertyStub stub = getStub();
    if (stub != null) {
      return stub.getName();
    }
    final PsiElement firstChild = getFirstChild();
    return firstChild != null ? getFirstChild().getText() : "";
  }

  @Override
  public ColoredItemPresentation getPresentation() {
    return new ColoredItemPresentation() {
      public String getPresentableText() {
        return getName();
      }

      @Nullable
      public String getLocationString() {
        final PsiClass clazz = getContainingClass();
        return clazz != null ? String.format("(in %s)", clazz.getQualifiedName()) : "";
      }

      @Nullable
      public Icon getIcon(boolean open) {
        return GosuFieldPropertyImpl.this.getIcon(ICON_FLAG_VISIBILITY | ICON_FLAG_READ_STATUS);
      }

      @Nullable
      @Override
      public TextAttributesKey getTextAttributesKey() {
        if (isDeprecated()) {
          return CodeInsightColors.DEPRECATED_ATTRIBUTES;
        }
        return null;
      }
    };
  }

  @Nullable
  public IGosuTypeElement getTypeElementGosu() {
    return GosuBaseElementImpl.findElement(getParentByTree(), IGosuTypeElement.class);
  }

  public PsiDocComment getDocComment() {
    return GosuDocUtil.findDocCommnentNode(getNode());
  }

  @NotNull
  public PsiType getType() {
    final PsiType type = getDeclaredType();
    return type != null ? type : PsiType.getJavaLangObject(getManager(), getResolveScope());
  }

  @Override
  public PsiTypeElement getTypeElement() {
    return ((GosuFieldImpl) getParent()).getTypeElement();
  }

  @Override
  public PsiExpression getInitializer() {
    return null;
  }

  @Override
  public boolean hasInitializer() {
    return false;
  }

  @Override
  public void normalizeDeclaration() throws IncorrectOperationException {

  }

  @Override
  public Object computeConstantValue() {
    return null;
  }

  @Nullable
  public PsiType getDeclaredType() {
    IGosuTypeElement typeElement = getTypeElementGosu();
    if (typeElement != null) {
      return typeElement.getType();
    }

    return null;
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    PsiImplUtil.setName(getNameIdentifier(), name);
    return this;
  }

  @NotNull
  public PsiIdentifier getNameIdentifier() {
    PsiIdentifier id = findElement(this, PsiIdentifier.class);
    if (id.getFirstChild() instanceof PsiIdentifier) {
      // Always return the leaf token node; we always want to patch in just the name and not mess with upper-level tree nodes
      id = (PsiIdentifier) id.getFirstChild();
    }
    return id;
  }

  @Nullable
  public IGosuModifierList getModifierList() {
    return ((GosuFieldImpl)getParent()).getModifierList();
  }

  @NotNull
  public PsiType getTypeNoResolve() {
    return getType();
  }

  @Override
  public Icon getElementIcon(final int flags) {
    final RowIcon baseIcon = ElementPresentationUtil.createLayeredIcon(GosuIcons.PROPERTY, this, false);
    return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
  }
}
