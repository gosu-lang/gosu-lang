/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuCodeStyleSettings extends CustomCodeStyleSettings {
  protected GosuCodeStyleSettings(@NonNls @NotNull String tagName, CodeStyleSettings container) {
    super(tagName, container);
  }
  // Don't add "final" to below fields!!!

  public boolean SPACE_AROUND_INTERVAL_OPERATORS = false;

  public boolean SPACE_BEFORE_TYPEOF_PARENTHESES = false;
  public boolean SPACE_BEFORE_USING_PARENTHESES = true;

  public boolean SPACE_BEFORE_USING_LBRACE = true;

  public boolean SPACE_WITHIN_TYPEOF_PARENTHESES = false;

  public boolean SPACE_BEFORE_COLON = false;
  public boolean SPACE_AFTER_COLON = true;

  public boolean SPACE_AFTER_LAMBDA = false;
  public boolean SPACE_AROUND_ARROW = true;
}
