/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import gw.compiler.ij.processors.DependencySink;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CompilerParser {
  private static final NotNullLazyValue<ICompilerParser[]> PARSERS = new NotNullLazyValue<ICompilerParser[]>() {
    @NotNull
    @Override
    protected ICompilerParser[] compute() {
      final CompilerParserExtensionBean[] extensions = Extensions.getExtensions(CompilerParserExtensionBean.EP_NAME);
      final ICompilerParser[] results = new ICompilerParser[extensions.length];
      for (int i = 0; i < extensions.length; i++) {
        results[i] = extensions[i].getHandler();
      }
      return results;
    }
  };

  public static boolean accepts(VirtualFile file) {
    for (ICompilerParser parser : PARSERS.getValue()) {
      if (parser.accepts(file)) {
        return true;
      }
    }
    return false;
  }

  public static boolean parse(CompileContext context, VirtualFile file, List<TranslatingCompiler.OutputItem> outputItems, DependencySink sink) {
    for (ICompilerParser parser : PARSERS.getValue()) {
      if (parser.accepts(file)) {
        return parser.parse(context, file, outputItems, sink);
      }
    }
    return false;
  }
}
