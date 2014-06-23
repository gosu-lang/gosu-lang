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
import com.intellij.psi.PsiAnnotationMethod;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IAnnotationExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Objects.firstNonNull;

public class GosuAnnotationParameterInfoHandler implements ParameterInfoHandler<PsiElement, PsiMethod> {
  @Override
  public boolean couldShowInLookup() {
    return true;
  }

  @Override
  public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public Object[] getParametersForDocumentation(PsiMethod p, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public PsiElement findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
    final IModule module = firstNonNull(GosuModuleUtil.findModuleForPsiElement(context.getFile()),
        GosuModuleUtil.getGlobalModule(context.getFile().getProject()));

    TypeSystem.pushModule(module);
    try {
      final PsiElement owner = ParameterInfoUtil.findParamOwnerAtOffset(context.getOffset(), context.getFile());
      if (owner != null) {
        final IParsedElement pe = ((GosuBaseElementImpl) owner).getParsedElement();
        if (pe instanceof IAnnotationExpression) {
          processAnnotation(((IAnnotationExpression) pe).getType(), context);
        } else {
          return null;
        }
      }
      return owner;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private void processAnnotation(IType type, @NotNull CreateParameterInfoContext context) {
    final PsiElement klass = PsiTypeResolver.resolveType(ParameterInfoUtil.getActualType(type), context.getFile());
    if (klass instanceof PsiClass) {
      context.setItemsToShow(((PsiClass) klass).getMethods());
    }
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
    Object[] objectsToView = context.getObjectsToView();
    if (index >= 0 && index < objectsToView.length) {
      context.setHighlightedParameter(objectsToView[index]);
    }
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
  public void updateUI(@NotNull PsiMethod method, ParameterInfoUIContext context) {
    if (method instanceof PsiAnnotationMethod) {
      // Java annotation
      updateAnnotationUI(method, context);
    } else {
      // Gosu annotation
      GosuParameterInfoHandler.updateMethodUI(context, method, null);
    }
  }

  private void updateAnnotationUI(@NotNull PsiMethod method, @Nullable ParameterInfoUIContext context) {
    IModule module = GosuModuleUtil.findModuleForPsiElement(method);
    TypeSystem.pushModule(module);
    try {
      StringBuilder sb = new StringBuilder();

//      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
//        sb.append(method.getReturnType().getPresentableText());
//        sb.append(" ");
//        sb.append(method.getName());
//        sb.append("(");
//      }

      PsiType returnType = method.getReturnType();
      if (returnType != null) {
        sb.append(returnType.getPresentableText());
      }
      sb.append(" ");
      int highlightStartOffset = sb.length();
      sb.append(method.getName());
      int highlightEndOffset = sb.length();
      sb.append("()");

//      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
//        buffer.append(")");
//      }

      if (context != null) {
        context.setupUIComponentPresentation(
            sb.toString(),
            highlightStartOffset,
            highlightEndOffset,
            !context.isUIComponentEnabled(),
            method.isDeprecated(),
            false,
            context.getDefaultParameterColor());
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }
}
