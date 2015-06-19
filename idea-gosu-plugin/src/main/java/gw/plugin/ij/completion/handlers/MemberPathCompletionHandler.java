/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import gw.config.CommonServices;
import gw.lang.parser.IExpression;
import gw.lang.parser.IHasArguments;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IIntervalExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.completion.model.BeanInfoModel;
import gw.plugin.ij.lang.parser.GosuRawPsiElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.util.IFeatureFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemberPathCompletionHandler extends AbstractPathCompletionHandler {
  private IType _rootType;

  public MemberPathCompletionHandler(@NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
    super(params.getOriginalFile().getProject(), params, results.getPrefixMatcher(), results);
  }

  public void handleCompletePath() {
    // head back to the other side of the dot

    PsiElement element = getContext().getPosition().getPrevSibling();
    if (element == null || element.getPrevSibling() == null) {
      return;
    }

    String text = "";
    while (element instanceof LeafPsiElement) {
      text = element.getText() + text;
      element = element.getPrevSibling();
    }
    text = text.trim();
    boolean starDot = text.equals("*.");

    while (element instanceof GosuRawPsiElement &&
        ((GosuRawPsiElement) element).getParsedElement() instanceof IParenthesizedExpression) {
      element = element.getFirstChild();
      while (element != null && element instanceof LeafPsiElement) {
        element = element.getNextSibling();
      }
    }

    IExpression expr = null;
    if (element instanceof GosuBaseElementImpl) {
      GosuBaseElementImpl gosuRefExpr = (GosuBaseElementImpl) element;
      IParsedElement pe = gosuRefExpr.getParsedElement();
      if( pe instanceof IExpression ) {
        expr = (IExpression)pe;
      }
      else {
        return;
      }
    } else {
      return;
    }

    IParsedElement parentExpr = expr.getParent();
    while (parentExpr instanceof IParenthesizedExpression) {
      parentExpr = parentExpr.getParent();
    }

    String strMemberPath = "";
    if (parentExpr instanceof IMemberAccessExpression) {
      if (parentExpr instanceof IHasArguments) {
//        if(getContext().getInvocationOffset() >= ((IHasArguments)expr).getArgPosition()) {
//          return null;
//        }
      }
      _rootType = expr.getType();
      if (_rootType instanceof IErrorType || _rootType instanceof INamespaceType || (_rootType instanceof IMetaType && ((IMetaType) _rootType).getType() instanceof IErrorType)) {
        return;
      }
        IType type = expr.getType();
        if (starDot) {
          IType componentType = JavaTypes.OBJECT();
          if (type.isArray()) {
            componentType = type.getComponentType();
          } else if (JavaTypes.COLLECTION().isAssignableFrom(type)) {
            IType asCollection = CommonServices.getTypeSystem().findParameterizedType(type, JavaTypes.COLLECTION());
            if (asCollection != null) {
              IType[] typeParams = asCollection.getTypeParameters();
              if (typeParams != null && typeParams.length == 1) {
                componentType = typeParams[0];
              }
            }
          }
          type = componentType;
        }

        BeanInfoModel beanInfoModel = new BeanInfoModel(type, "", expr.getGosuClass(), expr.getGosuClass().getTypeLoader().getModule(),
                new IFeatureFilter() {
                  @Override
                  public boolean acceptFeature(IType beanType, @NotNull IFeatureInfo fi) {
                    if (fi instanceof IAttributedFeatureInfo) {
                      if (_rootType instanceof IMetaType) {
                        return ((IAttributedFeatureInfo) fi).isStatic();
                      } else {
                        return !((IAttributedFeatureInfo) fi).isStatic();
                      }
                    } else {
                      return true;
                    }
                  }
                });
        makeProposals(beanInfoModel);
    } else if (parentExpr instanceof IIntervalExpression) {
      strMemberPath = ".";
      BeanInfoModel beanInfoModel = new BeanInfoModel(expr.getReturnType(), strMemberPath, expr.getGosuClass(), expr.getGosuClass().getTypeLoader().getModule());
      makeProposals(beanInfoModel);
    }
  }

  @Nullable
  @Override
  public String getStatusMessage() {
    return _rootType == null ? null : _rootType.getName();
  }
}
