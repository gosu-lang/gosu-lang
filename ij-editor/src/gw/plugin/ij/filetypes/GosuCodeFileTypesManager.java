/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.impl.FileTypeManagerImpl;
import com.intellij.testFramework.PlatformTestUtil;


import java.util.List;
import java.util.Map;

// Only for GosuCodeFileType
public enum GosuCodeFileTypesManager {
  INSTANCE;

  private final Map<String, IGosuFileTypeProvider> providers = Maps.newLinkedHashMap();

  GosuCodeFileTypesManager() {
    GosuFileTypeExtensionBean[] extensions = Extensions.getExtensions( GosuFileTypeExtensionBean.EP_NAME );
    for (GosuFileTypeExtensionBean provider : extensions ) {
      providers.put(provider.extension, provider.getHandler());
    }
  }

  public IGosuFileTypeProvider getFileTypeProvider(String ext) {
    return providers.get(ext);
  }

  public List<String> getRegisteredExtensions() {
    return ImmutableList.copyOf(providers.keySet());
  }
}
