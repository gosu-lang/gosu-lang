/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestCompileMessage implements Serializable {
  public final String moduleName;
  public final List<CompilationItem> items;

  public RequestCompileMessage(String moduleName, List<CompilationItem> items) {
    this.moduleName = moduleName;
    this.items = new ArrayList(items);
  }

  public String toString() {
    return String.format("RequestCompileMessage: [%s] %d items", moduleName, items.size());
  }
}