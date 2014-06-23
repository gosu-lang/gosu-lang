/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.google.common.collect.ImmutableMap;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.Modifier;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class GosuDeclaredElementImpl<E extends IParsedElement, T extends StubElement> extends GosuBaseElementImpl<E, T> {
  public static final Map<String, Integer> NAME_TO_MODIFIER_FLAG_MAP = ImmutableMap.<String, Integer>builder()
      .put(IGosuModifier.PUBLIC, Modifier.PUBLIC)
      .put(IGosuModifier.PROTECTED, Modifier.PROTECTED)
      .put(IGosuModifier.PRIVATE, Modifier.PRIVATE)
      .put(IGosuModifier.PACKAGE_LOCAL, Modifier.INTERNAL)
      .put(IGosuModifier.INTERNAL, Modifier.INTERNAL)
      .put(IGosuModifier.STATIC, Modifier.STATIC)
      .put(IGosuModifier.ABSTRACT, Modifier.ABSTRACT)
      .put(IGosuModifier.FINAL, Modifier.FINAL)
      .put(IGosuModifier.NATIVE, Modifier.NATIVE)
      .put(IGosuModifier.SYNCHRONIZED, Modifier.SYNCHRONIZED)
      .put(IGosuModifier.TRANSIENT, Modifier.TRANSIENT)
      .put(IGosuModifier.VOLATILE, Modifier.VOLATILE)
      .build();

  protected GosuDeclaredElementImpl(@NotNull final T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public GosuDeclaredElementImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @Override
  public boolean isEquivalentTo(@NotNull final PsiElement that) {
    if (!(that instanceof GosuBaseElementImpl)) {
      return false;
    }
    if (this.getText().equals(that.getText()) &&
        this.getTextOffset() == that.getTextOffset() &&
        InjectedElementEditor.areInEquivalentFiles(this, that)) {
      return true;
    }

    return super.isEquivalentTo(that);
  }
}
