/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.actions.TypeSystemAwareAction;

public class ClearTypeSystemInfoAction extends TypeSystemAwareAction {
  @Override
  public void actionPerformed(AnActionEvent e) {
    TypeSystem.tyeRequestCounter.clear();
    TypeSystem.tyeLoadingCounter.clear();
  }
}

