/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.core.GosuAppComponent;
import org.jetbrains.annotations.NotNull;

public class GosuFileTypeExtensionBean extends AbstractExtensionPointBean {
  public static final ExtensionPointName<GosuFileTypeExtensionBean> EP_NAME =
          new ExtensionPointName<>(GosuAppComponent.EDITOR_PLUGIN_ID.getIdString() + ".gosuFileTypeProvider");

  @Attribute("extension")
  public String extension;
  @Attribute("provider")
  public String className;

  private final LazyInstance<IGosuFileTypeProvider> myHandler = new LazyInstance<IGosuFileTypeProvider>() {
    @NotNull
    protected Class<IGosuFileTypeProvider> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public IGosuFileTypeProvider getHandler() {
    return myHandler.getValue();
  }
}
