/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.smartcopy;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.impl.EditorImpl;
import gw.plugin.ij.actions.TypeSystemAwareAction;

public class ShowSelectionInfoAction extends TypeSystemAwareAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    EditorComponentImpl editorComponent = (EditorComponentImpl) e.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    EditorImpl editor = editorComponent.getEditor();
    int offset = editor.getSelectionModel().getSelectionStart();
    int end = editor.getSelectionModel().getSelectionEnd();
    Notifications.Bus.notify(new Notification("SmartCopyGroup", "Selection Information",
        "Selection starts at offset " + offset + " and is " + (end - offset) + " characters long." , NotificationType.INFORMATION));
  }
}
