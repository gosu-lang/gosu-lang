/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.refactoring.actions.RenameElementAction;
import org.jetbrains.annotations.NotNull;

public class AppComponent implements ApplicationComponent {

  @Override
  public void initComponent() {
    ActionManagerImpl.getInstance().addAnActionListener(new AnActionListener() {
      long t1;

      @Override
      public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        if (action instanceof RenameElementAction) {
//          System.out.println("refactoring started....");
          t1 = System.nanoTime();
        }
      }

      @Override
      public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
        if (action instanceof RenameElementAction) {
          long dt = System.nanoTime() - t1;
//          System.out.println("Refactring done: " + (dt * 1.e-9));
        }
      }

      @Override
      public void beforeEditorTyping(char c, DataContext dataContext) {
      }
    });
  }

  @Override
  public void disposeComponent() {
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "devtools app comp";
  }
}
