/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.livetemplate;

import com.intellij.codeInsight.template.FileTypeBasedContextType;
import gw.plugin.ij.filetypes.GosuCodeFileType;

public class GosuCodeLiveTemplateContextType extends FileTypeBasedContextType {
  protected GosuCodeLiveTemplateContextType() {
    super("gosu-code", "Gosu code", GosuCodeFileType.INSTANCE);
  }
}
