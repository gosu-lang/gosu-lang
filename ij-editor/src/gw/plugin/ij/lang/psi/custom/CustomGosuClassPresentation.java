/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.presentation.java.ClassPresentationUtil;

import javax.swing.*;

public class CustomGosuClassPresentation implements ColoredItemPresentation {
  private final CustomGosuClass psiClass;

  public CustomGosuClassPresentation(CustomGosuClass psiClass) {

    this.psiClass = psiClass;
  }

  @Override
  public String getPresentableText() {
    return ClassPresentationUtil.getNameForClass(psiClass, false);
  }

  @Override
  public String getLocationString() {
    return "(" + psiClass.getNamespace() + ")";
  }

  @Override
  public TextAttributesKey getTextAttributesKey() {
    if (psiClass.isDeprecated()) {
      return CodeInsightColors.DEPRECATED_ATTRIBUTES;
    }
    return null;
  }

  @Override
  public Icon getIcon(boolean open) {
    return psiClass.getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
  }

}
