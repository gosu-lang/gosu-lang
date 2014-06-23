/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.tester;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.DefaultJavaProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.layout.PlaceInGrid;
import com.intellij.execution.ui.layout.impl.RunnerLayoutUiFactoryImpl;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.content.Content;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.editors.LightweightGosuEditor;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.LightVirtualFileWithModule;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;

public class InternalTesterFactory implements ToolWindowFactory {
  private static final boolean CLEAN_OUTPUT_ON_START = true;
  private static final String DEFAULT_SCRIPT =
      "for (i in 1..10) {\n" +
          "  if (i % 3 == 0) {\n" +
          "    print(\"Hello World \" + i + \"!\")\n" +
          "  }\n" +
          "}";

  private final AtomicReference<DefaultJavaProcessHandler> processHandler = new AtomicReference<DefaultJavaProcessHandler>();

  private boolean isProcessRunning() {
    return processHandler.get() != null;
  }

  private LightweightGosuEditor editor;
  private ConsoleView console;
  private Project project;

  private AnAction createRunAction() {
    return new TypeSystemAwareAction("Run", "Run the Gosu script", IconLoader.getIcon("/general/toolWindowRun.png")) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        executeScript(e.getProject(), editor.getDocument().getText());
      }

      @Override
      public void updateImpl(AnActionEvent e) {
        e.getPresentation().setEnabled(!isProcessRunning());
      }
    };
  }

  private AnAction createClearAction(final ConsoleView console) {
    return new TypeSystemAwareAction("Clear", "", IconLoader.getIcon("/actions/cross.png")) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        console.clear();
      }
    };
  }

  public void createToolWindowContent(final Project project, final ToolWindow toolWindow) {
    this.project = project;

    addCloseListener();

    final LightVirtualFile vf = LightVirtualFileWithModule.create("dummy.gsp", loadScript(), GosuModuleUtil.getGlobalModule(project));
    this.editor = new LightweightGosuEditor(project, vf, null, true);
    this.console = new ConsoleViewImpl(project, true);

    final DefaultActionGroup actionGroup = new DefaultActionGroup(null, false);
    actionGroup.addAction(createRunAction());
    actionGroup.addSeparator();
    actionGroup.addAction(createClearAction(console)).setAsSecondary(true);

    final RunnerLayoutUi ui = new RunnerLayoutUiFactoryImpl(project).create("Gosu Tester", "Gosu Tester", "Default", project);
    ui.getOptions().setMoveToGridActionEnabled(false);
    ui.getOptions().setLeftToolbar(actionGroup, ActionPlaces.DEBUGGER_TOOLBAR);

    final Content script = ui.createContent("GosuTesterScript__", editor.getComponent(), "Script", GosuIcons.CLASS, null);
    script.setCloseable(false);
    ui.addContent(script, 0, PlaceInGrid.center, false);

    final Content output = ui.createContent("GosuTesterOutput__", console.getComponent(), "Output", IconLoader.getIcon("/debugger/console.png"), null);
    output.setCloseable(false);
    ui.addContent(output, 0, PlaceInGrid.bottom, false);

    toolWindow.getComponent().add(ui.getComponent());
  }

  private void executeScript( Project project, String script ) {
    if (CLEAN_OUTPUT_ON_START) {
      console.clear();
    }
    runGosuScriptInProcess(project, script);
  }

  private void runGosuScriptInProcess( Project project, String script ) {
    final PrintStreamWriter writer = new PrintStreamWriter(new StringWriter());
    writer.setListener(new OutListener() {
      @Override
      public void notifyNewText(String text) {
        console.print(text, ConsoleViewContentType.SYSTEM_OUTPUT);
      }
    });
    GosuRunner.getInstance().runScript(GosuModuleUtil.getGlobalModule(project), script, writer);
  }

  private void addCloseListener() {
    ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
      @Override
      public boolean canCloseProject(Project project) {
        return true;
      }

      public void projectClosing(Project project) {
        saveScript();
      }
    });
  }

  private void saveScript() {
    if (editor != null && editor.getDocument() != null) {
      final String script = editor.getDocument().getText();

      try {
        CharStreams.write(script, Files.newWriterSupplier(getScriptFile(), Charsets.UTF_8));
      } catch (IOException e) {
        // Ignore
      }
    }
  }


  private String loadScript() {
    try {
      return Joiner.on('\n').join(CharStreams.readLines(Files.newReader(getScriptFile(), Charsets.UTF_8)));
    } catch (IOException e) {
      // Ignore
    }
    return DEFAULT_SCRIPT;
  }

  private File getScriptFile() {
    return new File(PathManager.getSystemPath() + File.separator + "GosuTester-" + project.getLocationHash() + ".gsp");
  }

//  private static String loadScript(Project project) {
//    final PropertiesComponent pc = PropertiesComponent.getInstance(project);
//    return pc.getValue(SCRIPT_KEY, DEFAULT_SCRIPT);
//  }
//
//  private void saveScript(Project project, String script) {
//    final PropertiesComponent pc = PropertiesComponent.getInstance(project);
//    pc.setValue(SCRIPT_KEY, script);
//  }

}
