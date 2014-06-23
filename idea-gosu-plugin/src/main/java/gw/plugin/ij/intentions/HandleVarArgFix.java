/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class HandleVarArgFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private final IParsedElement parsedElement;

  public HandleVarArgFix(@Nullable PsiElement element, IParsedElement pe) {
    super(element);
    this.parsedElement = pe;
  }


  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable("is null when called from inspection") Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    if(editor != null && !CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile()) ) {
      return;
    }
    IParseTree location = null;
    String functionName = null;
    Expression[] args = null;
    int argPosition = -1;
    IFunctionType functionType = null;
    IGosuClass gosuClass = null;
    VirtualFile vfile = file.getVirtualFile();

    if (parsedElement instanceof MethodCallExpression) {
      MethodCallExpression methCallEx = (MethodCallExpression) parsedElement;
      argPosition = methCallEx.getArgPosition();
      args = methCallEx.getArgs();
      location = methCallEx.getParent().getLocation();
      functionName = methCallEx.getFunctionSymbol().getDisplayName();
      functionType = methCallEx.getFunctionType();
      gosuClass = methCallEx.getGosuClass();
    } else if (parsedElement instanceof BeanMethodCallExpression) {
      BeanMethodCallExpression methCallEx = (BeanMethodCallExpression) parsedElement;
      argPosition = methCallEx.getArgPosition();
      args = methCallEx.getArgs();
      location = methCallEx.getLocation();
      functionName = methCallEx.getMemberName();
      functionType = methCallEx.getFunctionType();
      gosuClass = methCallEx.getGosuClass();
    }
    if (functionName != null && vfile != null) {
      IModule module = GosuModuleUtil.findModuleForFile(vfile, project);
      if (module == null) {
        return;
      }
      IExecutionEnvironment executionEnvironment = module.getExecutionEnvironment();
      TypeSystem.pushModule(module);
      try {
        IMethodInfo methodInfo = functionType.getMethodInfo();
        ITypeInfo typeInfo = methodInfo.getOwnersType().getTypeInfo();
        MethodList methods = ((IRelativeTypeInfo) typeInfo).getMethods(gosuClass);
        for (IMethodInfo m : methods) {
          if (m.getDisplayName().equals(functionName)) {
            if (handleVarArg(editor, location, m.getParameters(), args, argPosition)) {
              break;
            }
          }
        }
      } finally {
        TypeSystem.popModule(module);
      }
    }
  }

  private boolean handleVarArg(Editor editor, IParseTree location, IParameterInfo[] parameterInfos, Expression[] args, int argPosition) {
    boolean handled = false;
    int i = findStartingVarArg(parameterInfos, args);
    int end = location.getLength() + location.getOffset();
    if (i != -1) {
      String lpar = "{";
      String rpar = "}";
      if (i == 0) {
        i = argPosition;
      }
      else if (i == args.length) {
        lpar = ", {";
        i = end - 1;
      } else {
        i = args[i].getLocation().getOffset();
      }
      if (editor != null) {
        Document document = editor.getDocument();
        document.insertString(i, lpar);
        document.insertString(end + lpar.length() - 1, rpar);
        handled = true;
      }
    }
    return handled;
  }

  private int findStartingVarArg(IParameterInfo[] parameterInfos, IExpression[] args) {
    int last = parameterInfos.length - 1;
    if (!parameterInfos[last].getFeatureType().isArray()) {
      return -1;
    }
    if(args == null) {
      return 0;
    }
    int i = 0;
    while (i < last && i < args.length) {
      if (!parameterInfos[i].getFeatureType().isAssignableFrom(args[i].getType())) {
        return -1;
      }
      i++;
    }
    int start = i;

    IType varArgType = parameterInfos[last].getFeatureType().getComponentType();
    while (i < args.length) {
      IType type = args[i].getType();
      type = type.isPrimitive() && varArgType.equals(JavaTypes.OBJECT()) ? JavaTypes.OBJECT() : type;
      if (!varArgType.isAssignableFrom(type)) {
        return -1;
      }
      i++;
    }
    return start;
  }

  @NotNull
  @Override
  public String getText() {
    return "Add array literal";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }
}
