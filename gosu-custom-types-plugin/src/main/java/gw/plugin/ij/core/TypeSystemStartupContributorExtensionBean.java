/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.lang.psi.impl.resolvers.TypeResolverExtensionBean;
import org.jetbrains.annotations.NotNull;

public class TypeSystemStartupContributorExtensionBean extends AbstractExtensionPointBean {
  public static final ExtensionPointName<TypeSystemStartupContributorExtensionBean> EP_NAME =
          new ExtensionPointName<>( TypeResolverExtensionBean.EDITOR_PLUGIN_ID.getIdString() + ".typesystemStartupContributor");

  @Attribute("class")
  public String className;

  private final LazyInstance<ITypeSystemStartupContributor> myHandler = new LazyInstance<ITypeSystemStartupContributor>() {
    @NotNull
    protected Class<ITypeSystemStartupContributor> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public ITypeSystemStartupContributor getHandler() {
    return myHandler.getValue();
  }
}
