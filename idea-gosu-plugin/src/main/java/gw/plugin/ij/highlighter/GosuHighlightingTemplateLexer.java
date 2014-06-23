/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.pom.java.LanguageLevel;
import gw.plugin.ij.lang.GosuTemplateLexer;

public class GosuHighlightingTemplateLexer extends LayeredLexer {
  public GosuHighlightingTemplateLexer(LanguageLevel languageLevel) {
    super(new GosuTemplateLexer());
  }
}
