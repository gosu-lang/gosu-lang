/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.google.common.collect.Lists;
import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.core.GosuAppComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class QuickFixProviderExtensionBean extends AbstractExtensionPointBean {

  public static final ExtensionPointName<QuickFixProviderExtensionBean> EP_NAME =
          new ExtensionPointName<>(GosuAppComponent.EDITOR_PLUGIN_ID.getIdString() + ".quickFixProvider");

  @Attribute("class")
  public String className;

  private final LazyInstance<IQuickFixProvider> myHandler = new LazyInstance<IQuickFixProvider>() {
    @NotNull
    protected Class<IQuickFixProvider> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };
  private static NotNullLazyValue<List<IQuickFixProvider>> providers = new NotNullLazyValue<List<IQuickFixProvider>>() {

    @NotNull
    @Override
    protected List<IQuickFixProvider> compute() {
      List<IQuickFixProvider> providers = Lists.newArrayList();

      for (QuickFixProviderExtensionBean extension : Extensions.getExtensions(EP_NAME)) {
        providers.add(extension.getHandler());
      }
      return unmodifiableList(providers);
    }
  };

  @NotNull
  public IQuickFixProvider getHandler() {
    return myHandler.getValue();
  }

  public static List<IQuickFixProvider> getProviders() {
    return providers.getValue();
  }
}
