/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.Serializable;


public class CompilationDoneMessage implements Serializable {
  public static final CompilationDoneMessage INSTANCE = new CompilationDoneMessage();

  private CompilationDoneMessage() {

  }

  public String toString() {
    return "CompilationDoneMessage";
  }
}

