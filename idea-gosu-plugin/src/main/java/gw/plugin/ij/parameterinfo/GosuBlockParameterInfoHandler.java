/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.lang.parameterInfo.ParameterInfoUtils;
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;
import com.intellij.psi.PsiElement;
import gw.lang.parser.expressions.IBlockInvocation;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.lang.parser.GosuBlockInvocationImpl;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class GosuBlockParameterInfoHandler implements ParameterInfoHandler<PsiElement, GosuBlockInvocationImpl> {
  @Override
  public boolean couldShowInLookup() {
    return true;
  }

  @Override
  public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public Object[] getParametersForDocumentation(GosuBlockInvocationImpl p, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public GosuBlockInvocationImpl findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
    final IModule module = GosuModuleUtil.findModuleForPsiElement(context.getFile());
    if( module == null ) {
      return null;
    }

    TypeSystem.pushModule(module);
    try {
      final PsiElement owner = ParameterInfoUtil.findParamOwnerAtOffset(context.getOffset(), context.getFile());
      if (owner instanceof GosuBlockInvocationImpl) {
        processBlockInvocation((GosuBlockInvocationImpl) owner, context);
        return (GosuBlockInvocationImpl) owner;
      }
      return null;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private void processBlockInvocation(GosuBlockInvocationImpl owner, @NotNull CreateParameterInfoContext context) {
    context.setItemsToShow(new GosuBlockInvocationImpl[] {owner});
  }

  @Override
  public void showParameterInfo(@NotNull PsiElement element, @NotNull CreateParameterInfoContext context) {
    String error = ParameterInfoUtil.verify(context);
    if (error != null) {
      ExceptionUtil.showNonFatalError(GosuBundle.message("parameter.info.problem"), error);
    } else {
      context.showHint(element, element.getTextRange().getStartOffset(), this);
    }
  }

  @Override
  public PsiElement findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
    return ParameterInfoUtil.findParamOwnerAtOffset(context.getOffset(), context.getFile());
  }

  @Override
  public void updateParameterInfo(@NotNull PsiElement o, @NotNull UpdateParameterInfoContext context) {
    if (context.getParameterOwner() != o) {
      context.removeHint();
      return;
    }
    int index = ParameterInfoUtil.getCurrentParameterIndex(o, context.getOffset());
    context.setCurrentParameter(index);
  }

  @Override
  public String getParameterCloseChars() {
    return ParameterInfoUtils.DEFAULT_PARAMETER_CLOSE_CHARS;
  }

  @Override
  public boolean tracksParameterIndex() {
    return true;
  }

  @Override
  public void updateUI(@NotNull GosuBlockInvocationImpl owner, @NotNull ParameterInfoUIContext context) {
//    CodeInsightSettings settings = CodeInsightSettings.getInstance();

    IModule module = GosuModuleUtil.findModuleForPsiElement(owner);
    TypeSystem.pushModule(module);
    try {
      IBlockInvocation expression = (IBlockInvocation) ((GosuBaseElementImpl) owner).getParsedElement();
      IBlockType blockType = expression.getBlockType();
      String[] parameterNames = blockType.getParameterNames();
      IType[] parameterTypes = blockType.getParameterTypes();

      StringBuilder buffer = new StringBuilder();
//      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {}

      int highlightStartOffset = -1;
      int highlightEndOffset = -1;
      final int currentParameter = context.getCurrentParameterIndex();

      for (int i = 0; i < parameterNames.length; i++) {
        if (currentParameter == i) {
          highlightStartOffset = buffer.length();
        }

        buffer.append(parameterNames[i]);
        buffer.append(": ");
        buffer.append(parameterTypes[i].getRelativeName().replace(":", ": "));

        if (currentParameter == i) {
          highlightEndOffset = buffer.length();
        }

        if (i < parameterNames.length - 1) {
          buffer.append(", ");
        }
      }

//      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
//        buffer.append(")");
//      }

      if (context != null) {
        context.setupUIComponentPresentation(
            buffer.toString(),
            highlightStartOffset,
            highlightEndOffset,
            !context.isUIComponentEnabled(),
            false,
            false,
            context.getDefaultParameterColor()
        );
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }

}
