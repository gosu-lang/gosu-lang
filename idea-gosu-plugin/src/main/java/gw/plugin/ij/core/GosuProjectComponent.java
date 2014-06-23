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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.DefaultProject;
import com.intellij.openapi.util.Disposer;
import gw.plugin.ij.debugger.GosuPositionHighlighter;
import org.jetbrains.annotations.NotNull;

public class GosuProjectComponent implements ProjectComponent {
  @NotNull
  private final Project _project;
  private final EditorTracker _editorTracker;
  @NotNull
  private final GosuPositionHighlighter myEditorManager;

  protected GosuProjectComponent(@NotNull Project project, EditorTracker editorTracker, @NotNull final EditorColorsManager colorsManager) {
    this._project = project;
    this._editorTracker = editorTracker;

    this.myEditorManager = new GosuPositionHighlighter(_project, getContextManager());
    final EditorColorsListener myColorsListener = new EditorColorsListener() {
      public void globalSchemeChange(EditorColorsScheme scheme) {
        myEditorManager.updateContextPointDescription();
      }
    };
    colorsManager.addEditorColorsListener(myColorsListener);
    Disposer.register(project, new Disposable() {
      public void dispose() {
        colorsManager.removeEditorColorsListener(myColorsListener);
      }
    });
  }

  private DebuggerStateManager getContextManager() {
    return DebuggerManagerEx.getInstanceEx(_project).getContextManager();
  }


  // ProjectComponent
  @Override
  public void projectOpened() {
    PluginLoaderUtil.instance(_project).projectOpened();
  }

  @Override
  public void projectClosed() {
    PluginLoaderUtil.instance(_project).projectClosed();
  }

  // BaseComponent
  @Override
  public void initComponent() {
    if (!(_project instanceof DefaultProject) && !PluginLoaderUtil.instance(_project).isStarted()) {
      PluginLoaderUtil.instance(_project).setProject();
      PluginLoaderUtil.instance(_project).setEditorTracker(_editorTracker);
      PluginLoaderUtil.instance(_project).disableIJExternalCompiler();
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
