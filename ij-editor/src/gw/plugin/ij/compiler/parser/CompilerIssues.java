/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompilerIssues {
  private static int reportIssuesImpl(@NotNull CompileContext context, @NotNull VirtualFile file, @NotNull List<IParseIssue> issues, CompilerMessageCategory category) {
    final Project project = context.getProject();
    for (IParseIssue issue : issues) {
      final String url = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, file.getPath());
      final int column = issue.getColumn();
      final int line = issue.getLine();
      final OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file, issue.getTokenStart());

      context.addMessage(category, issue.getUIMessage(), url, line, column, descriptor);
    }
    return issues.size();
  }

  /**
   * Reports issues from exception to compiler context.
   *
   * @param context
   * @param file
   * @param e
   * @return {@code true} if errors were reported
   */
  public static boolean reportIssues(@NotNull CompileContext context, @NotNull VirtualFile file, @NotNull ParseResultsException e) {
    reportIssuesImpl(context, file, e.getParseWarnings(), CompilerMessageCategory.WARNING);
    final int errorCount = reportIssuesImpl(context, file, e.getParseExceptions(), CompilerMessageCategory.ERROR);
    return errorCount > 0;
  }
}
