/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.vfs.VirtualFile;
import gw.compiler.ij.processors.DependencySink;

import java.util.List;

public interface ICompilerParser {
  boolean accepts(VirtualFile file);

  /**
   *
   * @param context
   * @param file
   * @param outputItems
   * @param sink
   * @return {@code true} if compiled WITHOUT errors
   */
  boolean parse(CompileContext context, VirtualFile file, List<TranslatingCompiler.OutputItem> outputItems, DependencySink sink);
}
