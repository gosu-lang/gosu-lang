/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo;

import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;

public class UIContext implements ParameterInfoUIContext {
  private final int curentParamIndex;
  public final ArrayList<String> texts = new ArrayList<>();

  public UIContext(int curentParamIndex) {
    this.curentParamIndex = curentParamIndex;
  }
  
  @Override
  public boolean isUIComponentEnabled() {
    return true;
  }

  @Override
  public void setUIComponentEnabled(boolean enabled) {
    
  }

  @Override
  public int getCurrentParameterIndex() {
    return curentParamIndex;
  }

  @Nullable
  @Override
  public PsiElement getParameterOwner() {
    return null;  
  }

  @Nullable
  @Override
  public Color getDefaultParameterColor() {
    return null;  
  }

  @Override
  public void setupUIComponentPresentation(String text, int highlightStartOffset, int highlightEndOffset, boolean isDisabled, boolean strikeout, boolean isDisabledBeforeHighlight, Color background) {
    texts.add(text);
  }

}
