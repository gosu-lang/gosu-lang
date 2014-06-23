/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import gw.compiler.ij.processors.DependencySink;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaCompilerParser implements ICompilerParser {
  // ICompilerParser
  public boolean accepts(@NotNull VirtualFile file) {
    return file.getFileType() == StdFileTypes.JAVA;
  }

  public boolean parse(CompileContext context, VirtualFile file, List<TranslatingCompiler.OutputItem> outputItems, DependencySink sink) {
    return true; // Compiled successfully :-)
  }
}
