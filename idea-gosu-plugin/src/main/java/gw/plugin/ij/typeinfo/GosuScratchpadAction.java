/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.typeinfo;

import com.google.common.collect.ImmutableMap;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.managerThread.DebuggerCommand;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.PlatformProjectOpenProcessor;
import com.sun.jdi.ReferenceType;
import gw.lang.GosuShop;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GosuScratchpadAction extends TypeSystemAwareAction
{
  public GosuScratchpadAction() {
    super(GosuBundle.message("scratchpad.name"), GosuBundle.message("scratchpad.description"), null);
  }

  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    if (project == null &&
        PlatformProjectOpenProcessor.getInstanceIfItExists() == null) {
      return;
    }

    openScratchpadFile(project, null);
  }

  @Nullable
  public static Editor openScratchpadFile(@NotNull Project project, String fileText) {
    VirtualFile vfile = GosuScratchpadFileImpl.getScratchpadFile(project);
    if (vfile != null && vfile.isValid()) {
      if (fileText != null) {
        try {
          VfsUtil.saveText(vfile, fileText);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return openFile(vfile, project);
    }
    return null;
  }

  @Nullable
  public static Editor openFile(@NotNull VirtualFile virtualFile, @NotNull final Project project) {
    OpenFileDescriptor descriptor = new OpenFileDescriptor(project, virtualFile);
    return FileEditorManager.getInstance(project).openTextEditor(descriptor, true);
  }

  public void updateImpl(@NotNull AnActionEvent e) {
    addToolbar(e);
    Presentation presentation = e.getPresentation();
    Project project = PlatformDataKeys
      .PROJECT.getData( e.getDataContext() );
    if (PluginLoaderUtil.instance(project).isStarted()) {
      presentation.setEnabled( (project != null ||
                                PlatformProjectOpenProcessor.getInstanceIfItExists() != null) &&
                               (GosuSdkUtils.isGosuSdkSet( project  ) ||
                                GosuSdkUtils.isGosuApiModuleInProject( project )) );
    }
  }

  private void addToolbar(@NotNull AnActionEvent e) {
    Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
    if (project == null) {
      return;
    }
    VirtualFile vfile = GosuScratchpadFileImpl.getScratchpadFile(e.getProject());
    FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(vfile);
    if (fileEditor instanceof TextEditor) {
      Editor editor = ((TextEditor) fileEditor).getEditor();
      FileDocumentManagerImpl.registerDocument(editor.getDocument(), vfile);
      if (editor.getHeaderComponent() == null) {
        editor.setHeaderComponent(createToolbar());
      }
    }
  }

  private static JComponent createToolbar() {
    DefaultActionGroup actions = new DefaultActionGroup();
    actions.add(new MyRunContextAction());
    actions.add(new MyDebugContextAction());
    actions.addSeparator();
    actions.add(new MyRunInDebugProcessContextAction());
    return ActionManager.getInstance().createActionToolbar(ActionPlaces.EDITOR_TOOLBAR, actions, true).getComponent();
  }

  private static class MyRunContextAction extends TypeSystemAwareAction {
    public MyRunContextAction() {
      super(DefaultRunExecutor.getRunExecutorInstance().getStartActionText(), "", DefaultRunExecutor.getRunExecutorInstance().getIcon());
    }

    public void actionPerformed(AnActionEvent e) {
      ActionManager.getInstance().getAction("RunClass").actionPerformed(e);
    }
  }

  private static class MyDebugContextAction extends TypeSystemAwareAction {
    public MyDebugContextAction() {
      super(DefaultDebugExecutor.getDebugExecutorInstance().getStartActionText(), "", DefaultDebugExecutor.getDebugExecutorInstance().getIcon());
    }

    public void actionPerformed(AnActionEvent e) {
      ActionManager.getInstance().getAction("DebugClass").actionPerformed(e);
    }
  }

  private static class MyRunInDebugProcessContextAction extends TypeSystemAwareAction {
    public MyRunInDebugProcessContextAction() {
      super( GosuBundle.message( "gosu.run.in.server.runner.text" ),
             GosuBundle.message( "gosu.run.in.server.runner.description" ), GosuIcons.EXEC_IN_PROCESS );
    }

    public void updateImpl(@NotNull AnActionEvent e) {
      Presentation presentation = e.getPresentation();
      Project project = PlatformDataKeys
        .PROJECT.getData( e.getDataContext() );
      if (PluginLoaderUtil.instance(project).isStarted()) {
        DebuggerSession session = DebuggerManagerEx.getInstanceEx( project ).getContext().getDebuggerSession();
        presentation.setEnabled( session != null && session.isRunning() );
      }
    }

    public void actionPerformed(AnActionEvent e) {
      final Project project = e.getProject();
      final DebuggerSession session = DebuggerManagerEx.getInstanceEx( project ).getContext().getDebuggerSession();
      if (session != null) {
        final DebugProcessImpl process = session.getProcess();
        final String strScript = GosuScratchpadFileImpl.instance( project ).getText();
        process.getManagerThread().invokeCommand(new DebuggerCommand() {
          public void action() {
            final VirtualMachineProxyImpl vm = process.getVirtualMachineProxy();
            final List<ReferenceType> types = vm.classesByName("gw.internal.gosu.parser.ReloadClassesIndicator");
            vm.redefineClasses( ImmutableMap.of( types.get( 0 ), GosuShop.updateReloadClassesIndicator( Collections.<String>emptyList(), strScript) ));
          }

          public void commandCancelled() {
            // Nothing to do
          }
        });
      }
    }
  }
}
