/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.smartcopy;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.vfs.VirtualFile;
import gw.plugin.ij.actions.TypeSystemAwareAction;

public class CopyQuotedAction extends TypeSystemAwareAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    EditorComponentImpl editorComponent = (EditorComponentImpl) e.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    EditorImpl editor = editorComponent.getEditor();
    String selectedText = editor.getSelectionModel().getSelectedText();
    VirtualFile virtualFile = editor.getVirtualFile();
    if (selectedText != null && selectedText.length() > 0) {
      boolean isCtrlPressed = false;
      ClipboardUtil.copyToClipboard(process(selectedText, virtualFile.getName().endsWith(".gst"), isCtrlPressed));
    }
  }

  public void updateImpl(AnActionEvent event) {
    Presentation presentation = event.getPresentation();
    EditorComponentImpl editorComponent = (EditorComponentImpl) event.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    EditorImpl editor = editorComponent.getEditor();
    String selectedText = editor.getSelectionModel().getSelectedText();
    presentation.setVisible(selectedText != null && selectedText.length() > 0);
  }

  /**
   * XXX is there a smart way to use the 'useCRLF' flag here?  See comments in #split(String)
   */
  private String process(String text, boolean isTemplate, boolean useCRLF) {
    String[] lines = split(text);
    StringBuilder builder = new StringBuilder(text.length() + lines.length * 10);
    StringBuilder lineBuf = new StringBuilder(128);
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].trim().length() > 0) {
        if (lineBuf.length() > 0) {
          if (i < lines.length) {
            lineBuf.append("\\n\" +\n");
          } else {
            lineBuf.append("\"\n");
          }
          builder.append(lineBuf.toString());
          lineBuf.setLength(0);
        }
        lineBuf.append('"');
        lineBuf.append(escape(lines[i], isTemplate));
      } else {
        lineBuf.append("\\n");
      }
    }
    if (lineBuf.length() > 0) {
      lineBuf.append("\"\n");
      builder.append(lineBuf.toString());
    }
    return builder.toString();
  }

  // workaround newline inconsistencies in gst (using \r\n) and gs (using \n) files
  private String[] split(String text) {
    text = text.replace("\r", "");
    String separator = "\r\n";
    if (!text.contains(separator)) {
      separator = "\n";
    }
    return text.split(separator);
  }

  private String escape(String line, boolean isTemplate) {
    StringBuilder builder = new StringBuilder(line.length() + 10);
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '"') {
        builder.append("\\\"");
      } else if (c == '<' && i < line.length() - 1 && isTemplate) {
        i += 1;
        c = line.charAt(i);
        if (c == '%') {
          builder.append("\\<%");
        } else {
          builder.append('<');
          builder.append(c);
        }
      } else if (c == '$' && isTemplate) {
        builder.append("\\$");
      } else if (c == '\\') {
        builder.append("\\\\");
      } else {
        builder.append(c);
      }
    }
    return builder.toString();
  }

}
