/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.intellij.extapi.psi.PsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuXMLField extends PsiElementBase implements PsiField, PsiDocCommentOwner {
  private final PsiElement xmlElement;
  private final PsiClass containingClass;
  private final String name;

  public GosuXMLField(String name, PsiElement element, CustomGosuClass containingClass) {
    this.name = name;
    this.xmlElement = Preconditions.checkNotNull(element);
    this.containingClass = containingClass;
  }

  @Override
  public String getName() {
    return name;
  }

  @NotNull
  public Language getLanguage() {
    return GosuLanguage.instance();
  }

  @NotNull
  @Override
  public PsiElement[] getChildren() {
    return new PsiElement[0];
  }

  @Override
  public PsiClass getContainingClass() {
    return containingClass;
  }

  @Override
  public PsiElement getParent() {
    return xmlElement.getParent();
  }

  @Override
  public PsiElement getFirstChild() {
    return null;
  }

  @Override
  public PsiElement getLastChild() {
    return null;
  }

  @Override
  public PsiElement getNextSibling() {
    return null;
  }

  @Override
  public PsiElement getPrevSibling() {
    return null;
  }

  public TextRange getTextRange() {
    return xmlElement.getTextRange();
  }

  @Override
  public int getStartOffsetInParent() {
    return xmlElement.getStartOffsetInParent();
  }

  @Override
  public int getTextLength() {
    return xmlElement.getTextLength();
  }

  @Override
  public PsiElement findElementAt(int offset) {
    return null;
  }

  @Override
  public int getTextOffset() {
    return xmlElement.getTextOffset();
  }

  @Override
  public String getText() {
    return xmlElement.getText();
  }

  @NotNull
  @Override
  public char[] textToCharArray() {
    return xmlElement.textToCharArray();
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    return xmlElement.getNavigationElement();
  }

  @Override
  public boolean textContains(char c) {
    return xmlElement.textContains(c);
  }

  @Override
  public ASTNode getNode() {
    return xmlElement.getNode();
  }

  @NotNull
  @Override
  public PsiType getType() {
    IPropertyInfo property = getProperty();
    IType type;
    if (property != null) {
      type = property.getFeatureType();
    } else {
      IType containingType = TypeSystem.getByFullNameIfValid(containingClass.getQualifiedName(), TypeSystem.getGlobalModule());
      type = FieldTypeResolverRegistry.INSTANCE.resolve(containingType, getName());
    }
    PsiType theType = GosuBaseElementImpl.createType(type, xmlElement);
    if (theType == null) {
      theType = JavaPsiFacadeUtil.getElementFactory(xmlElement.getProject()).createTypeFromText("java.lang.Object", xmlElement);
    }
    return theType;
  }

  public IPropertyInfo getProperty() {
    IType type = TypeSystem.getByFullNameIfValid(containingClass.getQualifiedName(), TypeSystem.getGlobalModule());
    if (type != null) {
      IModule module = type.getTypeLoader().getModule();
      TypeSystem.pushModule(module);
      try {
        return type.getTypeInfo().getProperty(getName());
      } finally {
        TypeSystem.popModule(module);
      }
    }
    return null;
  }

  @Override
  public PsiTypeElement getTypeElement() {
    return null;
  }

  @NotNull
  @Override
  public PsiIdentifier getNameIdentifier() {
    return new CustomGosuIdentifier(this);
  }

  @NotNull
  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
//    XmlAttribute xmlAttribute = xmlElement.getAttribute("name");
//    xmlAttribute.setValue(name);
    return this;
  }

  @Nullable
  @Override
  public PsiType getTypeNoResolve() {
    return null;
  }

  @Override
  public PsiDocComment getDocComment() {
    IPropertyInfo property = getProperty();
    if (property == null) {
      return null;
    }
    String description = property.getDescription();
    return GosuPsiParseUtil.parseJavadocComment(description, containingClass);
  }

  @Override
  public boolean isDeprecated() {
    return false;
  }

  @Override
  public PsiModifierList getModifierList() {
    return null;
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    if (name.equals("public")) {
      return true;
    }
    return false;
  }

  @Override
  public void setInitializer(@Nullable PsiExpression initializer) throws IncorrectOperationException {
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

  @Override
  public boolean isValid() {
    return xmlElement.isValid() && super.isValid();
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GosuXMLField) {
      final GosuXMLField that = (GosuXMLField) obj;
      return Objects.equal(containingClass, that.containingClass) &&
          Objects.equal(name, that.name) &&
          Objects.equal(xmlElement, that.xmlElement);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(xmlElement, containingClass, name);
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() {
    return null;
  }
}
