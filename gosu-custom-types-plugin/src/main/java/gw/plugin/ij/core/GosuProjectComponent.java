/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.codeInsight.daemon.impl.EditorTracker;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.impl.DebuggerStateManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.colors.EditorColorsListener;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.DefaultProject;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

public class GosuProjectComponent implements ProjectComponent {
  public static final PluginId GOSU_TYPES_PLUGIN_ID = PluginId.getId("gosu.lang.types");
  @NotNull
  private final Project _project;

  protected GosuProjectComponent(@NotNull Project project, EditorTracker editorTracker, @NotNull final EditorColorsManager colorsManager) {
    _project = project;
  }

  private DebuggerStateManager getContextManager() {
    return DebuggerManagerEx.getInstanceEx(_project).getContextManager();
  }


  // ProjectComponent
  @Override
  public void projectOpened() {
    PluginLoaderUtil.instance( _project ).projectOpened();
  }

  @Override
  public void projectClosed() {
    PluginLoaderUtil.instance( _project ).projectClosed();
  }

  // BaseComponent
  @Override
  public void initComponent() {
    if (!(_project instanceof DefaultProject) && !PluginLoaderUtil.instance( _project ).isStarted()) {
      PluginLoaderUtil.instance( _project ).setProject();
    }
  }

  @Override
  public void disposeComponent() {
  }

  // NamedComponent
  @NotNull
  @Override
  public String getComponentName() {
    return "Gosu Project Component";
  }
}
