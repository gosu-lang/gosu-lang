/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers.filter;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.core.GosuAppComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletionFilterExtensionPointBean extends AbstractExtensionPointBean {

  public static final ExtensionPointName<CompletionFilterExtensionPointBean> EP_NAME = new ExtensionPointName<>(GosuAppComponent.EDITOR_PLUGIN_ID.getIdString() + ".completionFilter");

  @Attribute("class")
  public String className;

  private final LazyInstance<CompletionFilter> filter = new LazyInstance<CompletionFilter>() {
    @NotNull
    protected Class<CompletionFilter> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  static class FiltersHolders  {
    static final List<CompletionFilter> filters = getFilters();
    protected static List<CompletionFilter> getFilters() {
      final CompletionFilterExtensionPointBean[] extensions = Extensions.getExtensions(CompletionFilterExtensionPointBean.EP_NAME);
      List<CompletionFilter> filters = new ArrayList<>(extensions.length);
      for (CompletionFilterExtensionPointBean ep : extensions) {
        filters.add(ep.filter.getValue());
      }
      return Collections.unmodifiableList(filters);
    }
  };

  public static List<CompletionFilter> getFilters() {
    return FiltersHolders.filters;
  }
}
