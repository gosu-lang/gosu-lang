/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.pom.java.LanguageLevel;
import org.jetbrains.annotations.NotNull;

public class GosuTemplateFileHighlighter extends GosuCodeFileHighlighter {
  public GosuTemplateFileHighlighter() {
    super();
  }

  public GosuTemplateFileHighlighter(LanguageLevel languageLevel) {
    super(languageLevel);
  }

  @NotNull
  public Lexer getHighlightingLexer() {
    return new GosuHighlightingTemplateLexer(getLanguageLevel());
  }
}