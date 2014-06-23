/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;
import gw.util.IFeatureFilter;
import org.jetbrains.annotations.NotNull;

public class BeanInfoModel {
  protected IType _type;
  protected final String _strMemberPath;
  private BeanTree _beanTree;

  public BeanInfoModel(IType classBean, String strMemberPath, IType whosAsking, IModule module) {
    this(classBean, strMemberPath, whosAsking, module, null);
  }

  public BeanInfoModel(IType classBean, String strMemberPath, IType whosAsking, IModule module, IFeatureFilter filter) {
    _strMemberPath = strMemberPath;
    _type = classBean;
    _beanTree = new BeanTree(classBean, whosAsking, isForStaticAccess(), filter, module);
  }

  public boolean isForStaticAccess() {
    return false;
  }

  @NotNull
  public BeanTree getRoot() {
    return _beanTree;
  }

  @NotNull
  public String toString() {
    return _type + "." + _strMemberPath;
  }
}

