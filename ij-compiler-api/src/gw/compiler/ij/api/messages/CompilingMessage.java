/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.Serializable;

public class CompilingMessage implements Serializable {
  public final CompilationItem item;

  public CompilingMessage(CompilationItem item) {
    this.item = item;
  }


  public String toString() {
    return "CompilingMessage: " + item;
  }
}