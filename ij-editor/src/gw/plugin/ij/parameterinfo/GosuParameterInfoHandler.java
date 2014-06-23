/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.reflect.*;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.parser.GosuBlockInvocationImpl;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.impl.GosuPsiClassReferenceType;
import gw.plugin.ij.lang.psi.impl.GosuPsiSubstitutor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuAnnotationExpressionImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterImpl;
import gw.plugin.ij.util.ExceptionUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Objects.firstNonNull;

public class GosuParameterInfoHandler implements ParameterInfoHandler<PsiElement, IGosuResolveResult> {
  @Override
  public boolean couldShowInLookup() {
    return true;
  }

  @Override
  public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public Object[] getParametersForDocumentation(IGosuResolveResult p, ParameterInfoContext context) {
    return new Object[0];
  }

  @Override
  public PsiElement findElementForParameterInfo(@NotNull final CreateParameterInfoContext context) {
    final IModule module = firstNonNull(GosuModuleUtil.findModuleForPsiElement(context.getFile()),
        GosuModuleUtil.getGlobalModule(context.getFile().getProject()));
    TypeSystem.pushModule(module);
    try {
      final PsiElement owner = getPsiAtLocation(context.getOffset(), InjectedElementEditor.getOriginalFile(context.getFile()));
      if (isApplicable(owner)) {
        final ParameterInfoUtil.ParamInfoContext contextForParamInfo = ParameterInfoUtil.getContextForParamInfo(PsiTreeUtil.getParentOfType(InjectedElementEditor.getOriginalElement(owner), owner.getClass(), false));
        if (contextForParamInfo != null) {
          final List<IGosuResolveResult> signatures = Lists.newArrayList();
          contextForParamInfo.addSignatures(InjectedElementEditor.getOriginalFile(context.getFile()), signatures);
          context.setItemsToShow(signatures.toArray(new IGosuResolveResult[signatures.size()]));
        }
        return owner;
      } else {
        return null;
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private boolean isApplicable(PsiElement owner) {
    return owner != null && !(owner instanceof GosuAnnotationExpressionImpl) && !(owner instanceof GosuBlockInvocationImpl);
  }

  private PsiElement getPsiAtLocation(int offset, PsiFile file) {
    final PsiElement element = ParameterInfoUtil.findParamOwnerAtOffset(offset, file);
    return element != null ? element : ParameterInfoUtil.findParamOwnerAtOffset(offset - 1, file);
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
    return getPsiAtLocation(context.getOffset(), context.getFile());
  }

  @Override
  public void updateParameterInfo(@NotNull final PsiElement o, @NotNull final UpdateParameterInfoContext context) {
//we have to remove this check because it removes hint in case of injection
//    if (context.getParameterOwner() != o) {
//      context.removeHint();
//      return;
//    }

    IModule module = GosuModuleUtil.findModuleForPsiElement(o);
    TypeSystem.pushModule(module);
    try {
      ParameterInfoUtil.ParamInfoContext contextForParamInfo = ParameterInfoUtil.getContextForParamInfo(o);
      if (contextForParamInfo != null) {
        ArrayList<IGosuResolveResult> results = Lists.newArrayList();
        IGosuResolveResult highlighted = contextForParamInfo.addSignatures(context.getFile(), results);
        if (results.size() > 1) {
          context.setHighlightedParameter(highlighted);
        }
      }
      context.setCurrentParameter(ParameterInfoUtil.getCurrentParameterIndex(o, context.getOffset()));
    } finally {
      TypeSystem.popModule(module);
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
  public void updateUI(@NotNull IGosuResolveResult resolveResult, @NotNull ParameterInfoUIContext context) {
    PsiElement element = resolveResult.getElement();
    if (element instanceof PsiMethod) {
      PsiMethod method = (PsiMethod) element;
      PsiSubstitutor substitutor = resolveResult.getSubstitutor();
      updateMethodUI(context, method, substitutor);
    } else if (element != null && element.getLanguage().getID().equals("Properties")) {
      updatePropertyUI(context, element);
    } else {
      if (context != null) {
        IFeatureInfo featureInfo = resolveResult.getFeatureInfo();
        if (featureInfo instanceof IHasParameterInfos) {
          updateUIByFeatureInfo(context, (IHasParameterInfos) featureInfo);
        }
      }
    }
  }

  public static void updateUIByFeatureInfo(ParameterInfoUIContext context, IHasParameterInfos infos) {

    ITypeLoader typeLoader = infos.getOwnersType().getTypeLoader();
    IModule module = typeLoader != null ? typeLoader.getModule() : TypeSystem.getGlobalModule();
    TypeSystem.pushModule( module );
    try {
      StringBuilder info = new StringBuilder();

      CodeInsightSettings settings = CodeInsightSettings.getInstance();
      boolean showMethodName = settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO;
      if (showMethodName) {
        info.append(infos.getDisplayName()).append("(");
      }

      IParameterInfo[] params = infos.getParameters();
      int highlightStartOffset = -1;
      int highlightEndOffset = -1;

      if (params.length == 0) {
        info.append("<no parameters>");
      } else {
        final int currentParameter = context.getCurrentParameterIndex();

        for (int i = 0; i < params.length; ++i) {
          int startOffset = info.length();
          IParameterInfo pi = params[i];
          info.append(pi.getName());
          IType featureType = pi.getFeatureType();
          if (featureType != null) {
            info.append(": ").append(getStrType(featureType));
          }
          int endOffset = info.length();
          if (i < params.length - 1) {
            info.append(", ");
          }
          if (context.isUIComponentEnabled() && i == currentParameter) {
            highlightStartOffset = startOffset;
            highlightEndOffset = endOffset;
          }
        }
      }

      if (showMethodName) {
        info.append(")");
        if (infos instanceof IMethodInfo) {
          IType returnType = ((IMethodInfo) infos).getReturnType();
          if (returnType != null) {
            info.append(" : ").append(getStrType(returnType));
          }
        }
      }

      context.setupUIComponentPresentation(
              info.toString(),
              highlightStartOffset,
              highlightEndOffset,
              !context.isUIComponentEnabled(),
              infos.isDeprecated(),
              false,
              context.getDefaultParameterColor());
    }
    finally {
      TypeSystem.popModule( module );
    }
}

  public static void updateMethodUI(@NotNull ParameterInfoUIContext context, @NotNull PsiMethod method, @Nullable PsiSubstitutor substitutor) {
    IModule module = GosuModuleUtil.findModuleForPsiElement(method);
    if (module == null) {
      return;
    }
    CodeInsightSettings settings = CodeInsightSettings.getInstance();
    TypeSystem.pushModule(module);
    try {
      StringBuilder buffer = new StringBuilder();

      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
        buffer.append(method.getName());
        buffer.append("(");
      }

      final int currentParameter = context.getCurrentParameterIndex();

      PsiParameter[] parms = method.getParameterList().getParameters();

      int highlightStartOffset = -1;
      int highlightEndOffset = -1;
      if (parms != null && parms.length > 0) {
        for (int j = 0; j < parms.length; j++) {
          PsiParameter parm = parms[j];
          int startOffset = buffer.length();

          String name = parm.getName();
          if (name != null) {
            buffer.append(name);
            buffer.append(": ");
          }
          PsiType parmType = parm.getType();
          if( substitutor instanceof GosuPsiSubstitutor &&
              parmType instanceof GosuPsiClassReferenceType &&
              parmType.getCanonicalText().startsWith("block")) {

            String p = parmType.getCanonicalText();
            Map<String,IType> typeVarMap = ((GosuPsiSubstitutor) substitutor).getTypeVarMap();
            for(String var : typeVarMap.keySet()){
              p = p.replaceAll(var, getStrType(typeVarMap.get(var)));
            }
            buffer.append(p);
          } else {
            PsiType type = substitutor != null ? substitutor.substitute(parmType) : parmType;
            buffer.append(type.getPresentableText());
          }
          if (parm instanceof GosuParameterImpl) {
            GosuParameterImpl gosuParam = (GosuParameterImpl) parm;
            IGosuPsiElement defaultInitializer = gosuParam.getDefaultInitializer();
            if (defaultInitializer != null) {
              buffer.append(" = " + defaultInitializer.getText());
            }
          }

          int endOffset = buffer.length();

          if (j < parms.length - 1) {
            buffer.append(", ");
          }

          if (context.isUIComponentEnabled() && j == currentParameter) {
            highlightStartOffset = startOffset;
            highlightEndOffset = endOffset;
          }
        }
      } else {
        buffer.append(CodeInsightBundle.message("parameter.info.no.parameters"));
      }

      if (settings.SHOW_FULL_SIGNATURES_IN_PARAMETER_INFO) {
        buffer.append(")");
        PsiType returnType = method.getReturnType();
        if (returnType != null && returnType != PsiType.VOID) {
          buffer.append(" : ");
          buffer.append(returnType.getPresentableText());
        }
      }

      if (context != null && method != null) {
        context.setupUIComponentPresentation(
            buffer.toString(),
            highlightStartOffset,
            highlightEndOffset,
            !context.isUIComponentEnabled(),
            method.isDeprecated(),
            false,
            context.getDefaultParameterColor()
        );
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private static String getStrType(IType iType) {
    String s = iType.toString();
    int begin = s.lastIndexOf('.');
    return s.substring(begin + 1);
  }

  public static void updatePropertyUI(@NotNull ParameterInfoUIContext context, @NotNull PsiElement property) {
    IModule module = GosuModuleUtil.findModuleForPsiElement(property);
    TypeSystem.pushModule(module);
    try {
      StringBuilder buffer = new StringBuilder();
      final int currentParameter = context.getCurrentParameterIndex();
      int highlightStartOffset = -1;
      int highlightEndOffset = -1;
      int paramsCount = findParamCount(property);
      if (paramsCount > 0) {
        for (int j = 0; j < paramsCount; j++) {
          int startOffset = buffer.length();
          buffer.append("{").append(j).append("}").append(": ").append("String");
          int endOffset = buffer.length();
          if (j < paramsCount - 1) {
            buffer.append(", ");
          }
          if (context.isUIComponentEnabled() && j == currentParameter) {
            highlightStartOffset = startOffset;
            highlightEndOffset = endOffset;
          }
        }
      } else {
        buffer.append(CodeInsightBundle.message("parameter.info.no.parameters"));
      }

      context.setupUIComponentPresentation(
          buffer.toString(),
          highlightStartOffset,
          highlightEndOffset,
          !context.isUIComponentEnabled(),
          false,
          false,
          context.getDefaultParameterColor()
      );
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private static int findParamCount(PsiElement property) {
    String propertyText = property.getText();
    boolean insideBrackets = false;
    StringBuilder text = new StringBuilder();
    int paramIndex = 0;
    for (int i = 0; i < propertyText.length(); i++) {
      char c = propertyText.charAt(i);
      switch (c) {
        case '{':
          insideBrackets = true;
          text.setLength(0);
          break;
        case '}':
          if (insideBrackets) {
            Integer index = getInteger(text.toString().trim());
            if (index != null) {
              paramIndex = index;
            }
            text.setLength(0);
            insideBrackets = false;
          }
          break;
        default:
          text.append(c);
      }
    }
    return paramIndex + 1;
  }

  private static Integer getInteger(String text) {
    try {
      return Integer.parseInt(text);
    } catch (NumberFormatException e) {
      return null;
    }
  }

}
