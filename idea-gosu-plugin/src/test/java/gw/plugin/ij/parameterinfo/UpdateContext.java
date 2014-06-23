/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo;

import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpdateContext implements UpdateParameterInfoContext {

  private final PsiFile file;
  private final int offset;
  private int currentParam;
  private PsiElement parameterOwner;
  public Object highlightedParameter;

  @Override
  public PsiFile getFile() {
    return file;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  public UpdateContext(PsiFile file, int offset) {
    this.file = file;
    this.offset = offset;
  }

  @Override
  public void removeHint() {
    
  }

  @Override
  public void setParameterOwner(PsiElement o) {
    parameterOwner = o;
  }

  @Override
  public PsiElement getParameterOwner() {
    return parameterOwner;
  }

  @Override
  public void setHighlightedParameter(Object highlightedParameter) {
    this.highlightedParameter = highlightedParameter;
  }

  @Override
  public void setCurrentParameter(int index) {
    currentParam = index;
  }

  public int getCurrentParameter() {
    return currentParam;
  }

  @Override
  public boolean isUIComponentEnabled(int index) {
    return false;  
  }

  @Override
  public void setUIComponentEnabled(int index, boolean b) {
    
  }

  @Override
  public int getParameterListStart() {
    return 0;  
  }

  @NotNull
  @Override
  public Object[] getObjectsToView() {
    return new Object[0];  
  }

  @Nullable
  @Override
  public Project getProject() {
    return null;  
  }

  @NotNull
  @Override
  public Editor getEditor() {
    return null;  
  }
}
