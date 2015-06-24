/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

public class FileListenerExtensionBean extends AbstractExtensionPointBean
{
  public static final ExtensionPointName<FileListenerExtensionBean> PLUGIN_NAME = new ExtensionPointName<>( GosuCustomTypesProjectComponent.GOSU_TYPES_PLUGIN_ID.getIdString() + ".fileListener" );

  @Attribute("class")
  public String className;

  private final LazyInstance<IFileListener> myHandler = new LazyInstance<IFileListener>()
  {
    @NotNull
    protected Class<IFileListener> getInstanceClass() throws ClassNotFoundException
    {
      return findClass( className );
    }
  };

  @NotNull
  public IFileListener getHandler()
  {
    return myHandler.getValue();
  }
}
