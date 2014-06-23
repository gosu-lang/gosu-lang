/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.completion.proposals.RawCompletionProposal;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.transform;

public class FeatureRefCompletionHandler extends AbstractPathCompletionHandler {
  private IType _rootType;

  public FeatureRefCompletionHandler(@NotNull CompletionParameters params, @NotNull CompletionResultSet result) {
    super(params.getOriginalFile().getProject(), params, result.getPrefixMatcher(), result);
  }

  public void handleCompletePath() {
    // head back to the other side of the dot

    PsiElement prevSibling = getContext().getPosition().getPrevSibling();
    if (prevSibling == null) {
      return;
    }


    PsiElement csr = prevSibling.getPrevSibling();
    if (csr != null) {
      if (csr instanceof LeafPsiElement) {
        csr = csr.getParent();
      }

      while (csr instanceof GosuRawPsiElement &&
          ((GosuRawPsiElement) csr).getParsedElement() instanceof IParenthesizedExpression) {
        csr = csr.getFirstChild();
        while (csr != null && csr instanceof LeafPsiElement) {
          csr = csr.getNextSibling();
        }
      }

      //## todo: handle this case better, we could be ignorign valid root expressions
      if (!(csr instanceof GosuBaseElementImpl)) {
        return;
      }

      GosuBaseElementImpl gosuRefExpr = (GosuBaseElementImpl) csr;
      if (!(gosuRefExpr instanceof IGosuReferenceExpression)) {
        return;
      }

      IExpression expr = (IExpression) gosuRefExpr.getParsedElement();

      IParsedElement parentExpr = expr.getParent();
      while (parentExpr instanceof IParenthesizedExpression) {
        parentExpr = parentExpr.getParent();
      }

      if (expr instanceof ITypeLiteralExpression) {
        _rootType = ((ITypeLiteralExpression) expr).getType().getType();
      } else {
        _rootType = expr.getType();
      }
    } else {
      ASTNode fl = prevSibling.getParent().getNode();
      if (fl instanceof GosuCompositeElement) {
        GosuCompositeElement parent = (GosuCompositeElement) fl;
        _rootType = ((GosuBaseElementImpl) parent.getPsi()).getParsedElement().getGosuClass();
      } else {
        return;
      }
    }

    if (_rootType instanceof IErrorType || _rootType instanceof INamespaceType || (_rootType instanceof IMetaType && ((IMetaType) _rootType).getType() instanceof IErrorType)) {
      return;
    }

    ITypeInfo ti = _rootType.getTypeInfo();
    List<? extends IMethodInfo> methods;
    List<? extends IPropertyInfo> properties;
    List<? extends IConstructorInfo> ctors;
    if (ti instanceof IRelativeTypeInfo) {
      methods = ((IRelativeTypeInfo) ti).getMethods(_rootType);
      properties = ((IRelativeTypeInfo) ti).getProperties(_rootType);
      ctors = ((IRelativeTypeInfo) ti).getConstructors(_rootType);
    } else {
      methods = ti.getMethods();
      properties = ti.getProperties();
      ctors = ti.getConstructors();
    }

    for (IMethodInfo method : methods) {
      if (!method.getDisplayName().startsWith("@")) {
        addCompletion(new RawCompletionProposal(makeMethodRef(method)));
      }
    }

    for (IPropertyInfo property : properties) {
      addCompletion(new RawCompletionProposal(property.getName()));
    }

    for (IConstructorInfo ctor : ctors) {
      addCompletion(new RawCompletionProposal(makeConstructorRef(ctor)));
    }
  }


  private String makeConstructorRef(@NotNull IConstructorInfo ctor) {
    return String.format("construct(%s)", getParams(ctor.getParameters()));
  }

  private String makeMethodRef(@NotNull IMethodInfo method) {
    return String.format("%s(%s)", method.getDisplayName(), getParams(method.getParameters()));
  }

  private String getParams(IParameterInfo[] parameters) {
    return Joiner.on(", ").join(transform(Arrays.asList(parameters), new Function<IParameterInfo, String>() {
      @Override
      public String apply(@NotNull IParameterInfo parameterInfo) {
        final IType type = parameterInfo.getFeatureType();
        if (type.equals(TypeSystem.getDefaultTypeUsesMap().resolveType(type.getRelativeName()))) {
          return type.getRelativeName();
        } else {
          return type.getName();
        }
      }
    }));
  }

  @Nullable
  @Override
  public String getStatusMessage() {
    return _rootType == null ? null : _rootType.getName();
  }
}
