/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.google.common.collect.Lists;
import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.lang.psi.api.IFeatureResolver;
import gw.plugin.ij.core.GosuAppComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeatureResolverExtensionBean extends AbstractExtensionPointBean {
  private static final ExtensionPointName<FeatureResolverExtensionBean> EP_NAME = new ExtensionPointName<>(GosuAppComponent.EDITOR_PLUGIN_ID.getIdString() + ".featureResolver");
  private static final NotNullLazyValue<List<IFeatureResolver>> ALL_RESOLVERS = new NotNullLazyValue<List<IFeatureResolver>>() {
    @NotNull
    @Override
    protected List<IFeatureResolver> compute() {
      final List<IFeatureResolver> result = Lists.newArrayList();
      for (FeatureResolverExtensionBean bean : Extensions.getExtensions(EP_NAME)) {
        result.add(bean.getHandler());
      }
      return result;
    }
  };

  @Attribute("class")
  public String className;

  private final LazyInstance<IFeatureResolver> myHandler = new LazyInstance<IFeatureResolver>() {
    @NotNull
    protected Class<IFeatureResolver> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public IFeatureResolver getHandler() {
    return myHandler.getValue();
  }

  @NotNull
  public static List<IFeatureResolver> getResolvers() {
    if (ALL_RESOLVERS.getValue().isEmpty()) {
      throw new RuntimeException("There are no registered resolvers.");
    }

    return ALL_RESOLVERS.getValue();
  }

}
