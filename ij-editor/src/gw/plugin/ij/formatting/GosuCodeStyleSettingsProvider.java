/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import gw.plugin.ij.lang.GosuLanguage;
import org.jetbrains.annotations.NotNull;

public class GosuCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @NotNull
  @Override
  public Configurable createSettingsPage(@NotNull CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new CodeStyleAbstractConfigurable(settings, originalSettings, "Gosu") {
      @NotNull
      @Override
      protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
        return new GosuCodeStyleMainPanel(GosuLanguage.instance(), getCurrentSettings(), settings);
      }

      @Override
      public String getHelpTopic() {
        return "reference.settingsdialog.codestyle.gosu";
      }
    };
  }

  @Override
  public String getConfigurableDisplayName() {
    return "Gosu";
  }

  @Override
  public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
    return new GosuCodeStyleSettings("gosu-code-style-settings", settings);
  }
}
