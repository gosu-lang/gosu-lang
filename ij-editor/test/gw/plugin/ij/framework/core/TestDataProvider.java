/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.framework.core;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author peter
 */
public class TestDataProvider implements DataProvider {
  private final Project myProject;

  public TestDataProvider(@NotNull Project project) {
    myProject = project;
  }

  @Override
  public Object getData(@NonNls String dataId) {
    if (myProject.isDisposed()) {
      throw new RuntimeException("TestDataProvider is already disposed for " + myProject + "\n" +
              "If you closed a project in test, please reset IdeaTestApplication.setDataProvider.");
    }

    if (PlatformDataKeys.PROJECT.is(dataId)) {
      return myProject;
    }
    else if (PlatformDataKeys.EDITOR.is(dataId) || OpenFileDescriptor.NAVIGATE_IN_EDITOR.is(dataId)) {
      return FileEditorManager.getInstance(myProject).getSelectedTextEditor();
    }
    else {
      Editor editor = (Editor)getData(PlatformDataKeys.EDITOR.getName());
      if (editor != null) {
        FileEditorManagerEx manager = FileEditorManagerEx.getInstanceEx(myProject);
        Object managerData = manager.getData(dataId, editor, manager.getSelectedFiles()[0]);
        if (managerData != null) {
          return managerData;
        }
        JComponent component = editor.getContentComponent();
        if (component instanceof EditorComponentImpl) {
          return ((EditorComponentImpl)component).getData(dataId);
        }
      }
      return null;
    }
  }
}
