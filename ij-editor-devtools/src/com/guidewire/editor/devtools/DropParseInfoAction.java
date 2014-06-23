/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.plugin.ij.actions.TypeSystemAwareAction;

import java.lang.reflect.Method;

public class DropParseInfoAction extends TypeSystemAwareAction {
  @Override
  public void actionPerformed(AnActionEvent e) {
    try {
      final Class<?> aClass = Class.forName("gw.internal.gosu.parser.GosuClassParseInfo");
      final Method method = aClass.getMethod("clear");
      method.invoke(null);
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
  }
}

