/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo;

import com.google.common.collect.Lists;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.ASTNode;
import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import gw.lang.parser.*;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ISynthesizedMemberAccessExpression;
import gw.lang.reflect.*;
import gw.plugin.ij.lang.parser.GosuBlockInvocationImpl;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import gw.plugin.ij.lang.psi.impl.expressions.*;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterInfoUtil {
  private static final Logger LOG = Logger.getInstance(ParameterInfoUtil.class);

  public static IType getActualType(IType type) {
    if (type instanceof IMetaType) {
      type = ((IMetaType) type).getType();
    }
    type = TypeSystem.getPureGenericType(type);
    return type;
  }

  @Nullable
  public static PsiElement findParamOwnerAtOffset(int offset, @NotNull PsiFile file) {
    PsiElement element = file.findElementAt(offset);
    if (element == null) {
      return null;
    }

    while (!isParamOwner(element)) {
      element = element.getParent();
      if (element instanceof PsiFile) {
        return null;
      }
    }
    return element.getParent();
  }

  private static boolean isParamOwner(@NotNull PsiElement psiElement) {
    final PsiElement parent = psiElement.getParent();
    if (parent instanceof GosuNewExpressionImpl ||
            parent instanceof GosuMethodCallExpressionImpl ||
            parent instanceof GosuBeanMethodCallExpressionImpl ||
            parent instanceof GosuBlockInvocationImpl ||
            parent instanceof GosuPropertyMemberAccessExpressionImpl ||
            parent instanceof GosuIdentifierExpressionImpl) {
      final List<ASTNode> children = Arrays.asList(parent.getNode().getChildren(null));
      final int index = children.indexOf(psiElement.getNode());
      for (int i = index; i >= 0; i--) {
        final ASTNode astNode = children.get(i);
        if (astNode instanceof LeafPsiElement && astNode.getText().equals("(")) {
          return true;
        }
      }
    }
    return false;
  }

  @Nullable
  public static String verify(@NotNull CreateParameterInfoContext context) {
    final Object[] items = context.getItemsToShow();
    if (items != null) {
      for (Object item : items) {
        if (item == null) {
          return "Items to show contains a null element.";
        }
      }
    }
    return null;
  }

  public static int getCurrentParameterIndex(@NotNull PsiElement parameterOwner, int offset) {
    ASTNode argList = parameterOwner.getNode().findChildByType(GosuElementTypes.ELEM_TYPE_ArgumentListClause);
    if (argList == null) {
      return 0;
    }
    ASTNode[] children = argList.getChildren(null);
    if (children.length == 0) {
      return 0;
    }
    int index = 0;
    int parameterStartOffset = 0;
    int curOffset = children[0].getStartOffset();
    for (int i = 0; i < children.length; i++) {
      ASTNode child = children[i];
      curOffset += child.getTextLength();
      if (offset < curOffset) {
        break;
      }
      if (child.getText().equals(",")) {
        parameterStartOffset = i + 1;
        index++;
      }
    }

    while (parameterStartOffset < children.length &&
            (children[parameterStartOffset] instanceof PsiWhiteSpaceImpl ||
                    children[parameterStartOffset].getElementType() == GosuElementTypes.ELEM_TYPE____UnhandledParsedElement)) {
      parameterStartOffset++;
    }

    if (children[parameterStartOffset].getText().equals(":")) {
      final String parameterName = children[parameterStartOffset + 1].getText();
      ParamInfoContext paramInfoContext = ParameterInfoUtil.getContextForParamInfo(parameterOwner);
      Integer paramIndex = paramInfoContext.getParameterIndex(parameterName);
      if (paramIndex != -1) {
        index = paramIndex;
      }
    }

    return index;
  }

  @NotNull
  public static List<? extends IMethodInfo> getMethods(@NotNull IType type) {
    List<IMethodInfo> methods = new ArrayList<>();

    do {
      ITypeInfo typeInfo = type.getTypeInfo();
      if (typeInfo instanceof IRelativeTypeInfo) {
        methods.addAll(((IRelativeTypeInfo) typeInfo).getMethods(type));
      } else {
        methods.addAll(typeInfo.getMethods());
      }
      type = type.getEnclosingType();
    } while (type != null);

    return methods;
  }

  public static List<? extends IConstructorInfo> getConstructors(@NotNull IType type) {
    ITypeInfo typeInfo = type.getTypeInfo();
    if (typeInfo instanceof IRelativeTypeInfo) {
      return ((IRelativeTypeInfo) typeInfo).getConstructors(type);
    } else {
      return typeInfo.getConstructors();
    }
  }

  static abstract class ParamInfoContext {
    protected final IType _rootType;
    protected IType _ownersType;
    protected String _signature;
    protected String[] _parameterNames;

    public ParamInfoContext(IType ownersType, String signature, String[] parameterNames, IType rootType) {
      _rootType = rootType;
      _ownersType = ownersType;
      _signature = signature;
      _parameterNames = parameterNames;
    }

    @Nullable
    public abstract IGosuResolveResult addSignatures(PsiFile file, List<IGosuResolveResult> list);

    public Integer getParameterIndex(String parameterName) {
      for (int i = 0; i < _parameterNames.length; i++) {
        if (_parameterNames[i].equals(parameterName)) {
          return i;
        }
      }
      throw new RuntimeException("Cannot find index of parameter " + parameterName);
    }
  }

  static class MethodParamInfoContext extends ParamInfoContext {
    protected String _methodName;

    public MethodParamInfoContext(IType ownersType, String methodName, String signature, String[] parameterNames, IType rootType) {
      super(ownersType, signature, parameterNames, rootType);
      _methodName = methodName;
    }

    @Nullable
    @Override
    public IGosuResolveResult addSignatures(@NotNull PsiFile file, @NotNull List<IGosuResolveResult> list) {
      final List<IMethodInfo> matchingMethods = Lists.newArrayList();
      for (IMethodInfo method : ParameterInfoUtil.getMethods(_rootType)) {
        if (method.getDisplayName().equals(_methodName)) {
          matchingMethods.add(method);
        }
      }

      if (_ownersType instanceof IMetaType) {
        _ownersType = ((IMetaType) _ownersType).getType();
      }
      _ownersType = TypeSystem.getPureGenericType(_ownersType);
      PsiElement c = PsiTypeResolver.resolveType(_ownersType, file);
      if (c == null) {
        return null;
      }

      IGosuResolveResult highlighted = null;
      for (IMethodInfo mi : matchingMethods) {
        IGosuResolveResult result = PsiFeatureResolver.resolveMethodOrConstructorWithSubstitutor(mi, c);
        if (result == null) {
          result = new GosuResolveResultImpl(null, true, mi);
        }
        list.add(result);
        if (equalsNoWhitespace(mi.getName(), _signature)) {
          highlighted = result;
        }
      }

      return highlighted;
    }

    private boolean equalsNoWhitespace(@NotNull String s1, @NotNull String s2) {
      return strip(s1).equals(strip(s2));
    }

    public String strip(@NotNull String s) {
      StringBuilder builder = new StringBuilder(s.length());
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (!Character.isWhitespace(c)) {
          builder.append(c);
        }
      }
      return builder.toString();
    }

  }

  private static String[] getParameterNames(IHasParameterInfos info) {
    IParameterInfo[] parameters = info.getParameters();
    String[] params = new String[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      params[i] = parameters[i].getName();
    }
    return params;
  }

  private static class ConstructorParamInfoContext extends ParamInfoContext {
    public ConstructorParamInfoContext(IConstructorInfo constructorInfo, IType rootType) {
      super(constructorInfo.getOwnersType(), constructorInfo.getName(), getParameterNames(constructorInfo), rootType);
    }

    @Nullable
    @Override
    public IGosuResolveResult addSignatures(@NotNull PsiFile file, @NotNull List<IGosuResolveResult> list) {
      final PsiElement element = PsiTypeResolver.resolveType(ParameterInfoUtil.getActualType(_rootType), file);
      if (element == null) {
        return null;
      }

      IGosuResolveResult highlightedMethod = null;
      for (IConstructorInfo ci : ParameterInfoUtil.getConstructors(_rootType)) {
        IGosuResolveResult result = PsiFeatureResolver.resolveMethodOrConstructorWithSubstitutor(ci, element);
        if (result == null) {
          result = new GosuResolveResultImpl(null, true, ci);
        }
        list.add(result);
        if (ci.getName().equals(_signature)) {
          highlightedMethod = result;
        }
      }
      return highlightedMethod;
    }
  }

  @Nullable
  public static ParamInfoContext getContextForParamInfo(@NotNull PsiElement owner) {
    IParsedElement pe = ((GosuBaseElementImpl) owner).getParsedElement();
    if (pe instanceof ISynthesizedMemberAccessExpression) {
      ISynthesizedMemberAccessExpression expression = (ISynthesizedMemberAccessExpression) pe;
      IType rootType = expression.getRootType();
      String methodName = expression.getMethodNameForSyntheticAccess();
      IMethodInfo method = rootType.getTypeInfo().getMethod(methodName);
      if (method != null) {
        return new MethodParamInfoContext(method.getOwnersType(), methodName, method.getName(), getParameterNames(method), rootType);
      }
    } else if (pe instanceof IBeanMethodCallExpression) {
      IBeanMethodCallExpression expression = (IBeanMethodCallExpression) pe;
      IType rootType = expression.getRootType();
      if (rootType instanceof IMetaType) {
        rootType = ((IMetaType) rootType).getType();
      }
      IMethodInfo method = expression.getMethodDescriptor();
      if (method != null) {
        String methodName = getMethodName(expression, method);
        return new MethodParamInfoContext(method.getOwnersType(), methodName, method.getName(), getParameterNames(method), rootType);
      }
    } else if (pe instanceof IMethodCallExpression) {
      IMethodCallExpression methodCallExpression = (IMethodCallExpression) pe;
      if (methodCallExpression.getFunctionSymbol() instanceof IConstructorFunctionSymbol) {
        IConstructorFunctionSymbol constructorFunctionSymbol = (IConstructorFunctionSymbol) methodCallExpression.getFunctionSymbol();
        IConstructorInfo constructorInfo = constructorFunctionSymbol.getConstructorInfo();
        if( constructorInfo != null ) {
          return new ConstructorParamInfoContext(constructorInfo, constructorInfo.getType());
        }
      } else {
        IType invokingType = getInvokingType(owner);
        if (invokingType != null) {
          IMethodInfo method = getMethodInfo(methodCallExpression);
          if (method != null) {
            String methodName = getMethodName(methodCallExpression, method);
            return new MethodParamInfoContext(method.getOwnersType(), methodName, method.getName(), getParameterNames(method), invokingType);
          } else {
            IFunctionType functionType = methodCallExpression.getFunctionType();
            if (functionType == null) {
              return null;
            }
            String methodName = getMethodName(methodCallExpression, functionType);
            final IType enclosingType = getEnclosingType(functionType);
            return enclosingType == null ? null : new MethodParamInfoContext(
                    enclosingType, methodName, functionType.getParamSignature().toString(), functionType.getParameterNames(), invokingType);
          }
        }
      }
    } else if (pe instanceof INewExpression) {
      INewExpression newExpression = (INewExpression) pe;
      IConstructorInfo constructorInfo = newExpression.getConstructor();
      return constructorInfo != null ? new ConstructorParamInfoContext(constructorInfo, newExpression.getType()) : null;
    }
    return null;
  }

  private static IType getEnclosingType(IFunctionType functionType) {
    IType enclosingType = functionType.getEnclosingType();
    return enclosingType != null ? enclosingType : functionType.getScriptPart() != null ? functionType.getScriptPart().getContainingType() : null;
  }

  @Nullable
  private static IMethodInfo getMethodInfo(@NotNull IMethodCallExpression methodCallExpression) {
    final IFunctionType type = methodCallExpression.getFunctionType();
    return type != null ? type.getMethodInfo() : null;
  }

  private static String getMethodName(IExpression expression, IFunctionType functionType) {
    return functionType == null ? findMethodName(expression) : functionType.getDisplayName();
  }

  @Nullable
  private static String getMethodName(@NotNull IExpression expression, @Nullable IMethodInfo methodInfo) {
    return methodInfo == null ? findMethodName(expression) : methodInfo.getDisplayName();
  }

  private static IType getInvokingType(PsiElement owner) {
    final PsiClass containingPsiClass = getContainingPsiClass(owner);
    final VirtualFile virtualFile = containingPsiClass.getContainingFile().getVirtualFile();
    if (virtualFile instanceof VirtualFileWindow) {
      final VirtualFile delegate = ((VirtualFileWindow) virtualFile).getDelegate();
      final String[] typesForFile = TypeSystem.getTypesForFile(TypeSystem.getGlobalModule(), FileUtil.toIFile(delegate));
      final IType type = TypeSystem.getByFullNameIfValid(typesForFile[0]);
      return type;
    } else {
      final String qualifiedName = containingPsiClass.getQualifiedName();
      return TypeSystem.getByFullNameIfValid(qualifiedName);
    }
  }

  @Nullable
  private static String findMethodName(@NotNull IExpression expression) {
    for (IToken token : expression.getTokens()) {
      if (token.getType() == ISourceCodeTokenizer.TT_WORD) {
        return token.getText();
      }
    }
    return null;
  }

  @Nullable
  private static PsiClass getContainingPsiClass(@Nullable PsiElement owner) {
    if (owner == null) {
      return null;
    }

    if (owner instanceof PsiClass) {
      return (PsiClass) owner;
    }

    return getContainingPsiClass(owner.getParent());
  }

}
