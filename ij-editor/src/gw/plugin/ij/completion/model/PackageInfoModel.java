/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.*;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.handlers.AbstractCompletionHandler;

import java.util.Iterator;

public class PackageInfoModel extends BeanInfoModel {

  public PackageInfoModel(IType type, IType whosAsking, IModule module, boolean annotation) {
    super(type, "", whosAsking, module, null);

    if (annotation) {
      final Iterator<BeanTree> it = getRoot().getChildren().iterator();
      while (it.hasNext()) {
        final BeanTree child = it.next();
        final IFeatureInfo info = child.getBeanNode().getFeatureInfo();
        if (info instanceof TypePropertyInfo) {
          final TypeInPackageType featureType = (TypeInPackageType) ((TypePropertyInfo) info).getFeatureType();
          final IType byFullName = TypeSystem.getByFullNameIfValid(featureType.getName());
          if (!AbstractCompletionHandler.isAnnotation(byFullName)) {
            it.remove();
          }
        }
      }
    }
  }

  public boolean isForStaticAccess() {
    return (_type != null &&
        (_type instanceof PackageType || _type instanceof IMetaType)) ||
        _strMemberPath == null ||
        _strMemberPath.length() == 0;
  }
}
