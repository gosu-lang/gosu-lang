/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.lookup.LookupElementPresentation;
import org.jetbrains.annotations.Nullable;

public class ElementPresentation extends LookupElementPresentation {

  @Nullable
  public String toString() {
    return /*getTypeText() + " " + */getItemText() + getTailText();
  }

  public String getTypeText() {
    return super.getTypeText() != null ? super.getTypeText() : "";
  }

  public String getItemText() {
    return super.getItemText() != null ? super.getItemText() : "";
  }

  public String getTailText() {
    return super.getTailText() != null ? super.getTailText() : "";
  }
}
