/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.plugin.ij.completion.model.BeanInfoNode;
import gw.plugin.ij.completion.model.BeanTree;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathCompletionProposal extends GosuCompletionProposal {
  private final Project _project;
  private final BeanTree _beanTree;

  public PathCompletionProposal(Project project, BeanTree beanTree) {
    _project = project;
    _beanTree = beanTree;
  }

  public BeanTree getBeanTree() {
    return _beanTree;
  }

 @Override
  public String toString() {
    return _beanTree.toString();
  }

  @Nullable
  @Override
  public PsiElement resolve(@NotNull PsiElement context) {
    BeanInfoNode beanNode = _beanTree.getBeanNode();
    IFeatureInfo featureInfo = beanNode.getFeatureInfo();
    if (featureInfo instanceof IMethodInfo) {
      return PsiFeatureResolver.resolveMethodOrConstructor((IMethodInfo) featureInfo, context);
    } else if (featureInfo instanceof IPropertyInfo) {
      return PsiFeatureResolver.resolveProperty((IPropertyInfo) featureInfo, context);
    }
    return null;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return _beanTree.getBeanNode().getFeatureInfo();
  }

  @Override
  public String getGenericName() {
    return _beanTree.getBeanNode().getFeatureInfo().getDisplayName();
  }
}