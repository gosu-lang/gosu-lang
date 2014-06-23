/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.PsiAwareFileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.filetypes.GosuFileTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class UIUtil {
  public static void closeAllGosuEditors(@NotNull Project project, @Nullable IModule gsModule) {
    final FileEditorManager manager = PsiAwareFileEditorManagerImpl.getInstance(project);
    for (VirtualFile file : manager.getOpenFiles()) {
      if (GosuFileTypes.isGosuFile(file)) {
        if (gsModule == null || GosuModuleUtil.findModuleForFile(file, project) == gsModule) {
          manager.closeFile(file);
        }
      }
    }
  }

  @NotNull
  public static JFrame getFrame(Project project) {
    return (JFrame) WindowManager.getInstance().getIdeFrame(project);
  }

  @NotNull
  public static JFrame getFrame() {
    return (JFrame) WindowManager.getInstance().getIdeFrame(null);
  }

  public static void settleModalEventQueue() {
    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    while (eventQueue.peekEvent() != null) {
      try {
        AWTEvent event = eventQueue.getNextEvent();
        Object src = event.getSource();
        if (event instanceof ActiveEvent) {
          ((ActiveEvent) event).dispatch();
        } else if (src instanceof Component) {
          ((Component) src).dispatchEvent(event);
        } else if (src instanceof MenuComponent) {
          ((MenuComponent) src).dispatchEvent(event);
        }
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
  }
}
