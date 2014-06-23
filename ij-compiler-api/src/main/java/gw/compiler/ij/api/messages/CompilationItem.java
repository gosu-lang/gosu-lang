/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.File;
import java.io.Serializable;

public class CompilationItem implements Serializable {
  public final File sourceFile;
  public final File outputFile;

  public CompilationItem(File sourceFile, File outputFile) {
    this.sourceFile = sourceFile;
    this.outputFile = outputFile;
  }

  public String toString() {
    return String.format("[%s -> %s]", sourceFile, outputFile);
  }
}