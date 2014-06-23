/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api;

import gw.compiler.ij.api.messages.CompilationItem;
import gw.compiler.ij.api.messages.CompileIssueMessage;
import gw.fs.IFile;

import java.io.File;
import java.util.Set;

public interface IDECallback {
  void compiling(CompilationItem item);

  void compileIssue(File file, CompileIssueMessage.Category category, int offset, int line, int column, String message);

  void compiled(CompilationItem item, int compilationTime, boolean successfully, Set<IFile> dependencies, Set<String> displayKeysDependencies, long fingerprint);

  void fatalError(String message, Throwable e);
}
