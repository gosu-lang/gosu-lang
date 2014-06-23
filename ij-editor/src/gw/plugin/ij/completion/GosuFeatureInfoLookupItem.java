/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.google.common.base.Joiner;
import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.DefaultLookupItemRenderer;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.codeInsight.lookup.impl.JavaElementLookupRenderer;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.LayeredIcon;
import com.intellij.ui.RowIcon;
import com.intellij.util.PlatformIcons;
import com.intellij.util.VisibilityIcons;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.java.IJavaFieldPropertyInfo;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.model.BeanInfoNode;
import gw.plugin.ij.completion.model.BeanTree;
import gw.plugin.ij.completion.model.MethodNode;
import gw.plugin.ij.completion.model.PackagePropertyInfo;
import gw.plugin.ij.completion.model.PropertyInfoNode;
import gw.plugin.ij.completion.model.TypeInPackageType;
import gw.plugin.ij.completion.model.TypePropertyInfo;
import gw.plugin.ij.icons.GosuIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * We define our own FeatureInfo based lookup item because:
 * 1. We already have *all* the type information provided by our parser, including parameterized types (no Substitutor shenanigans)
 * 2. Our type information is different from Java's in IJ e.g., we have properties, enhancements, and closures
 * 3. It's a lot easier and faster because we don't have to *resolve* everything in the completion list because of #1
 */
public class GosuFeatureInfoLookupItem extends LookupItem<BeanTree> {
  private Icon getIcon(@NotNull IAttributedFeatureInfo info) {
    Icon baseIcon = null;
    if (info instanceof IPropertyInfo) {
      if (info instanceof PackagePropertyInfo) {
        baseIcon = PlatformIcons.PACKAGE_ICON;
      } else if (info instanceof TypePropertyInfo) {
        TypeInPackageType featureType = (TypeInPackageType) ((TypePropertyInfo) info).getFeatureType();
        baseIcon = featureType.getIcon();
      } else if (info instanceof IJavaFieldPropertyInfo || info instanceof IGosuVarPropertyInfo) {
        baseIcon = GosuIcons.FIELD;
      } else {
        baseIcon = GosuIcons.PROPERTY;
      }
    } else if (info instanceof IMethodInfo) {
      baseIcon = GosuIcons.METHOD;
    }

    final LayeredIcon temp = new LayeredIcon(2);
    temp.setIcon(baseIcon, 0);
    if (info.getOwnersType() instanceof IGosuEnhancement) {
      temp.setIcon(GosuIcons.ENH, 1);
    }

    final RowIcon result = new RowIcon(2);
    result.setIcon(temp, 0);
    VisibilityIcons.setVisibilityIcon(getVisibility(info), result);
    return result;
  }

  private int getVisibility(@NotNull IAttributedFeatureInfo fi) {
    if (fi.isPublic()) {
      return PsiUtil.ACCESS_LEVEL_PUBLIC;
    }
    if (fi.isProtected()) {
      return PsiUtil.ACCESS_LEVEL_PROTECTED;
    }
    if (fi.isInternal()) {
      return PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL;
    }
    return PsiUtil.ACCESS_LEVEL_PRIVATE;
  }

  @NotNull
  private IAttributedFeatureInfo getFeatureInfo() {
    return (IAttributedFeatureInfo) getObject().getBeanNode().getFeatureInfo();
  }

  private static String getSubstitution(IFeatureInfo info) {
    if (info instanceof IPropertyInfo) {
      return info.getName();
    }
    return info.getDisplayName();
  }

  public GosuFeatureInfoLookupItem(@NotNull BeanTree beanTree, PsiElement context) {
    //noinspection deprecation
    super(beanTree, getSubstitution(beanTree.getBeanNode().getFeatureInfo()));

    setIcon(getIcon(getFeatureInfo()));
    setTailType(TailType.NONE);
  }

  @Override
  public void handleInsert(@NotNull InsertionContext context) {
    final Document document = context.getDocument();
    final IAttributedFeatureInfo info = getFeatureInfo();
    if (info instanceof IMethodInfo) {
      int parentStart = context.getStartOffset() + info.getDisplayName().length();

      final StringBuilder sb = new StringBuilder("(");
      boolean hasArguments = false;
      if (info instanceof IHasParameterInfos) {
        final IParameterInfo[] parameters = ((IHasParameterInfos) info).getParameters();
        hasArguments  = parameters.length > 0;

        if (parameters.length == 1 && parameters[0].getFeatureType() instanceof IBlockType) {
          final IBlockType blockType = (IBlockType) parameters[0].getFeatureType();
          sb.append(" \\");
          final String[] parameterNames = blockType.getParameterNames();
          if (parameterNames.length > 0) {
            sb.append(" ");
            Joiner.on(", ").appendTo(sb, parameterNames);
            sb.append(" ");
          }
          sb.append("-> ");
        }
      }
      sb.append(")");

      document.insertString(parentStart, sb);
      final int caretPosition = parentStart + sb.length() - (hasArguments ? 1 : 0);
      context.getEditor().getCaretModel().moveToOffset(caretPosition);
      context.setAddCompletionChar(false);
    }
  }

  @Override
  public void renderElement(@NotNull LookupElementPresentation presentation) {
    final BeanTree beanTree = getObject();
    final BeanInfoNode node = beanTree.getBeanNode();
    ITypeLoader typeLoader = beanTree.getWhosAsking().getTypeLoader();
    final IModule module = typeLoader != null ? typeLoader.getModule() : node.getModule();

    TypeSystem.pushModule(module);
    try {
      final IFeatureInfo info = node.getFeatureInfo();
      presentation.setIcon(DefaultLookupItemRenderer.getRawIcon(this, presentation.isReal()));
      presentation.setItemText(getSubstitution(info));
      presentation.setStrikeout(JavaElementLookupRenderer.isToStrikeout(this));

      boolean declaredOnRootType = beanTree.getParent().getBeanNode().getType() == info.getOwnersType();
      presentation.setItemTextBold(declaredOnRootType || getAttribute(HIGHLIGHTED_ATTR) != null);

      if (info instanceof IPropertyInfo) {
        final PropertyInfoNode property = (PropertyInfoNode) node;
        presentation.setTypeText(PropertyInfoNode.getTypeName(property.getType()));
      } else if (info instanceof IMethodInfo) {
        final MethodNode method = (MethodNode) node;
        presentation.setTypeText(method.getReturnTypeName());
        presentation.setTailText(method.getParameterDisplay());
      }
    } finally {
      TypeSystem.popModule(module);
    }
  }
}
