/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.editors;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;

public interface IEmbedsGosuEditors {
  Editor getGosuEditorAt(OpenFileDescriptor descriptor);
}
