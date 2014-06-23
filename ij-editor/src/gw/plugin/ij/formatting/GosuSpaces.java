/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class GosuSpaces {
  public static final Spacing EMPTY = Spacing.createSpacing(0, 0, 0, false, 0);
  public static final Spacing LINE_FEED = Spacing.createSpacing(0, Integer.MAX_VALUE, 1, false, 0);

  public static Spacing getSpace(boolean hasSpace, @NotNull CommonCodeStyleSettings settings) {
    return getSpace(hasSpace, 0, settings);
  }

  public static Spacing getSpace(boolean hasSpace, int minLineFeeds, @NotNull CommonCodeStyleSettings settings) {
    final boolean keepLineBreaks = settings.KEEP_LINE_BREAKS;
    final int keepBlankLines = settings.KEEP_BLANK_LINES_IN_CODE;
    return hasSpace ? Spacing.createSpacing(1, 1, minLineFeeds, keepLineBreaks, keepBlankLines) : Spacing.createSpacing(0, 0, minLineFeeds, keepLineBreaks, keepBlankLines);
  }

  public static Spacing blankLines(int blankLines) {
    return Spacing.createSpacing(1, Integer.MAX_VALUE, blankLines + 1, false, 0);
  }

  private GosuSpaces() {
  }
}

