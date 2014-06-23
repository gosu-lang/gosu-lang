/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.lang.ASTNode;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.*;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.*;
import gw.plugin.ij.completion.model.BeanInfoModel;
import gw.plugin.ij.completion.model.BeanTree;
import gw.plugin.ij.completion.proposals.InitializerCompletionProposal;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.util.IFeatureFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InitializerCompletionHandler extends AbstractPathCompletionHandler {
  private IType _rootType;

  public InitializerCompletionHandler(@NotNull CompletionParameters params, @NotNull CompletionResultSet result) {
    super(params.getPosition().getProject(), params, result.getPrefixMatcher(), result);
  }

  public void handleCompletePath() {
    IParsedElement parsedElement = null;

    ASTNode node = getContext().getPosition().getParent().getParent().getNode();
    if (node instanceof GosuCompositeElement) {
      if (node.getPsi() instanceof GosuBaseElementImpl) {
        parsedElement = ((GosuBaseElementImpl) node.getPsi()).getParsedElement();
      }
    } else {
      return;
    }

    if (parsedElement != null) {
      IObjectInitializerExpression oie = findObjectInitializerExpression(parsedElement);
      if (oie != null) {
        IType type = oie.getType();
        String strMemberPath = "";
        strMemberPath = strMemberPath != null ? strMemberPath.trim() : null;

        final List<String> usedProperties = new ArrayList<>();
        for (IInitializerAssignment prop : oie.getInitializers()) {
          usedProperties.add(prop.getPropertyName());
        }

        //TODO cgross - pass in module?
        BeanInfoModel model = new BeanInfoModel(type, strMemberPath, type, type.getTypeLoader().getModule(),
            new IFeatureFilter() {
              @Override
              public boolean acceptFeature(IType beanType, @NotNull IFeatureInfo fi) {
                return fi instanceof IPropertyInfo && ((IPropertyInfo) fi).isPublic() && ((IPropertyInfo) fi).isWritable();
              }
            });
        makeProposals(model);

      } else {
        if (parsedElement instanceof IArgumentListClause) {
          parsedElement = parsedElement.getParent();
        }

        if (parsedElement instanceof IMethodCallExpression) {
          IFunctionType functionType = ((IMethodCallExpression) parsedElement).getFunctionType();
          if (functionType != null) {
            String[] parameterNames = functionType.getParameterNames();
            IType[] parameterTypes = functionType.getParameterTypes();
            IExpression[] defaultValues = functionType.getDefaultValueExpressions();
            for (int i = 0; i < parameterNames.length; i++) {
              String parameterName = parameterNames[i];
              IType parameterType = parameterTypes[i];
              boolean required = defaultValues.length < i || defaultValues[i] == null;
              addCompletion(new InitializerCompletionProposal(parameterName, parameterType, required));
            }
          }
        }

        if (parsedElement instanceof INewExpression) {
          INewExpression mc = (INewExpression) parsedElement;
          IConstructorInfo ci = mc.getConstructor();
          IParameterInfo[] parameterNames = ci.getParameters();
          if (ci instanceof IOptionalParamCapable) {
            IExpression[] defaultValues = ((IOptionalParamCapable) ci).getDefaultValueExpressions();
            for (int i = 0; i < parameterNames.length; i++) {
              IParameterInfo parameterName = parameterNames[i];
              boolean required = defaultValues.length < i || defaultValues[i] == null;
              addCompletion(new InitializerCompletionProposal(parameterName.getName(), parameterName.getFeatureType(), required));
            }
          }
        }

        if (parsedElement instanceof IBeanMethodCallExpression) {
          IBeanMethodCallExpression mc = (IBeanMethodCallExpression) parsedElement;
          IMethodInfo mi = mc.getMethodDescriptor();
          IParameterInfo[] parameterNames = mi.getParameters();
          if (mi instanceof IOptionalParamCapable) {
            IExpression[] defaultValues = ((IOptionalParamCapable) mi).getDefaultValueExpressions();
            for (int i = 0; i < parameterNames.length; i++) {
              IParameterInfo parameterName = parameterNames[i];
              boolean required = defaultValues.length < i || defaultValues[i] == null;
              addCompletion(new InitializerCompletionProposal(parameterName.getName(), parameterName.getFeatureType(), required));
            }
          }
        }
      }
    }
  }

  @NotNull
  @Override
  protected InitializerCompletionProposal makeProposal(@NotNull BeanTree child) {
    return new InitializerCompletionProposal(child.getBeanNode().getName(), child.getBeanNode().getType(), true);
  }

  @Nullable
  @Override
  public String getStatusMessage() {
    return _rootType == null ? null : _rootType.getName();
  }

  private boolean isDoubleColonAtCaret() {
//    final GosuContentAssistantHelper helper = getViewer().getContentAssistParserHelper();
//    if (":".equals(TextViewerUtil.getWordBeforeCaret(getViewer()))) {
//      return helper.getDeepestLocationAtCaret() != null && helper.getDeepestLocationAtCaret().getParsedElement() instanceof IInitializerAssignment
//          && helper.getExpressionAtCaret() == null;
//    }
    return false;
  }

  @Nullable
  private IObjectInitializerExpression findObjectInitializerExpression(@NotNull IParsedElement parsedElement) {
    if (parsedElement instanceof IObjectInitializerExpression) {
      return (IObjectInitializerExpression) parsedElement;
    } else if (parsedElement.getParent() != null) {
      return findObjectInitializerExpression(parsedElement.getParent());
    } else {
      return null;
    }
  }

  private static boolean isInitializerStart(@Nullable IParsedElement parsedElement) {
    if (parsedElement instanceof IInitializerAssignment) {
      IInitializerAssignment iia = (IInitializerAssignment) parsedElement;
      if (parsedElement instanceof IIdentifierExpression) {
        return true;
      } else {
        List<IParseIssue> exceptions = iia.getParseExceptions();
        for (IParseIssue exception : exceptions) {
          if (exception.getMessageKey().equals(Res.MSG_EQUALS_FOR_INITIALIZER_EXPR) || exception.getMessageKey().equals(Res.MSG_NO_PROPERTY_DESCRIPTOR_FOUND)) {
            return true;
          }
        }
        return false;
      }
    } else {
      return parsedElement != null && isInitializerStart(parsedElement.getParent());
    }
  }
}
