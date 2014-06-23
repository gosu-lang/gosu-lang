/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.module.LanguageLevelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.java.LanguageLevel;
import org.jetbrains.annotations.NotNull;

public class GosuSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
  @Override
  @NotNull
  public SyntaxHighlighter getSyntaxHighlighter(final Project project, final VirtualFile virtualFile) {
    return new GosuCodeFileHighlighter(virtualFile != null ? LanguageLevelUtil.getLanguageLevelForFile(virtualFile) : LanguageLevel.HIGHEST);
  }
}