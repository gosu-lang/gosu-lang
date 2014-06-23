/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class GosuCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
  public GosuCodeStyleMainPanel(@Nullable Language language, CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(language, currentSettings, settings);
  }

}
