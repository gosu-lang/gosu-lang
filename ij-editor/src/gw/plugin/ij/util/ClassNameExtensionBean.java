/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.util.xmlb.annotations.Attribute;

public class ClassNameExtensionBean extends AbstractExtensionPointBean {

  @Attribute("class")
  public String className;

  public <T> T instantiate(Class<T> iface) {
    try {
      return iface.cast(Class.forName(className).newInstance());
    } catch (Exception e) {
      ExceptionUtil.showNonFatalError("Couldn't instantiate property resolver " + className, e);
    }
    return null;
  }
}
