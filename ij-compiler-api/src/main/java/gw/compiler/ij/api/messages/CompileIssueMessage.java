/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.File;
import java.io.Serializable;


public class CompileIssueMessage implements Serializable {
  public static enum Category {
    ERROR, WARNING;
  }

  public final File file;
  public final Category category;
  public final int offset;
  public final int line;
  public final int column;
  public final String message;

  public CompileIssueMessage(File file, Category category, int offset, int line, int column, String message) {
    this.file = file;
    this.category = category;
    this.offset = offset;
    this.line = line;
    this.column = column;
    this.message = message;
  }

  public String toString() {
    return String.format("CompileIssueMessage: %s - %s - %s (%d:%d)", category, message, file, line, column);
  }
}