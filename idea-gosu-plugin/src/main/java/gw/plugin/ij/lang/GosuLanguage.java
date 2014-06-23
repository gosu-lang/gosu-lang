/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.lang.InjectableLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import org.jetbrains.annotations.NotNull;

public class GosuLanguage extends Language implements InjectableLanguage {
  private static final GosuLanguage INSTANCE = new GosuLanguage();

  @NotNull
  public static GosuLanguage instance() {
    return INSTANCE;
  }

  private GosuLanguage() {
    super("Gosu", "text/gosu", "text/x-gosu");
  }

  @Override
  public LanguageFileType getAssociatedFileType() {
    return GosuCodeFileType.INSTANCE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Gosu";
  }
}
