/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IExternalCompiler {
  FileDependencyInfo compileFile(@NotNull CompileContext context, @NotNull Module ijModule, @NotNull VirtualFile file, final List<TranslatingCompiler.OutputItem> outputItems);
}
