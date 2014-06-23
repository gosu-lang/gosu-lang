/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework;

import com.intellij.compiler.impl.CompileDriver;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.util.concurrency.Semaphore;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.compiler.GosuCompiler;
import gw.plugin.ij.core.ModuleClasspathListener;
import gw.plugin.ij.core.PluginFailureReason;
import gw.plugin.ij.core.PluginLoaderUtil;
import gw.plugin.ij.framework.core.DaemonAnalyzerTestCase;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.ExceptionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

abstract public class GosuTestCase extends DaemonAnalyzerTestCase {
  private static IModule pushedModule;

  @Override
  protected void beforeClass() throws Exception {
    boolean previousValue = ModuleClasspathListener.ENABLED;
    ModuleClasspathListener.ENABLED = false;
    try {
      // setup Modules for the test class
      super.beforeClass();
      ExceptionUtil._inTestMode = true;

    } finally {
      ModuleClasspathListener.ENABLED = previousValue;
    }
  }

  @Override
  protected void runStartupActivities() {
    // reinitialize type system with new set modules for the test class
    if (PluginLoaderUtil.instance(getProject()).isStarted()) {
      throw new RuntimeException("The plugin should have been stopped by the previous test class.");
    }
    // start IJEditorPluginLoader
    try {
      startPlugin();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      fail("Unable to initialize type system");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unable to start plugin");
    }

    super.runStartupActivities();
  }

  @Override
  protected void afterClass() throws Exception {
    boolean previousValue = ModuleClasspathListener.ENABLED;
    ModuleClasspathListener.ENABLED = false;
    try {
      if (PluginLoaderUtil.instance(getProject()).isStarted()) {
        deleteAllFiles();
        stopPlugin();
      }
    } finally {
      super.afterClass();
      ModuleClasspathListener.ENABLED = previousValue;
    }
  }

  @Override
  protected void invokeTestRunnable(@NotNull final Runnable runnable) throws Exception {
    final IModule module = TypeSystem.getExecutionEnvironment(PluginLoaderUtil.getFrom(getProject())).getGlobalModule();
    final Runnable runnable2 = new Runnable() {
      @Override
      public void run() {
        TypeSystem.pushModule( module );
        try {
          runnable.run();
        }
        finally {
          TypeSystem.popModule( module );
        }
      }
    };
    super.invokeTestRunnable(runnable2);
  }

  private void startPlugin() throws Exception {
    getProject().putUserData( PluginLoaderUtil.TEST_ROOT_DISPOSABLE, myTestRootDisposable );
    PluginLoaderUtil.instance(getProject()).startPLugin();
    System.out.println("Starting plugin on project " + getProject().getBasePath());
    if (!PluginLoaderUtil.instance(getProject()).isStartupOk()) {
      throw new Exception("Unable to start Gosu plugin", PluginLoaderUtil.instance(getProject()).getStartupError());
    }
  }

  private void stopPlugin() {
    System.out.println("Closing project " + getProject().getBasePath());
    PluginLoaderUtil.instance(getProject()).closeProject( PluginFailureReason.NONE );
  }

  public ProcessHandler runProcess(String className, Module module, final Class<? extends Executor> executorClass,
                                   final ProcessListener listener, @NotNull final ProgramRunner runner) throws ExecutionException {
    final ApplicationConfiguration configuration = new ApplicationConfiguration("app", getProject(), ApplicationConfigurationType.getInstance());
    configuration.setModule(module);
    configuration.setMainClassName(className);

    final Executor executor = Executor.EXECUTOR_EXTENSION_NAME.findExtension(executorClass);
    final ExecutionEnvironment environment = new ExecutionEnvironment(configuration, getProject(),
        new RunnerSettings<>(null, null), null, null);

    final Semaphore semaphore = new Semaphore();
    semaphore.down();

    final AtomicReference<ProcessHandler> processHandler = new AtomicReference<>();
    runner.execute(executor, environment, new ProgramRunner.Callback() {
      @Override
      public void processStarted(@NotNull final RunContentDescriptor descriptor) {
        disposeOnTearDown(new Disposable() {
          @Override
          public void dispose() {
            descriptor.dispose();
          }
        });
        final ProcessHandler handler = descriptor.getProcessHandler();
        assert handler != null;
        handler.addProcessListener(listener);
        processHandler.set(handler);
        semaphore.up();
      }
    });
    semaphore.waitFor();
    return processHandler.get();
  }

  public void rebuildProject(boolean onlyGosu) {
    final CompileDriver compileDriver = new CompileDriver(getProject());
    if (onlyGosu) {
      compileDriver.setCompilerFilter(new CompilerFilter() {
        public boolean acceptCompiler(com.intellij.openapi.compiler.Compiler compiler) {
          return compiler instanceof GosuCompiler;
        }
      });
    }
    compileDriver.rebuild(new CompileStatusNotification() {
      public void finished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
        synchronized (compileDriver) {
          compileDriver.notifyAll();
        }
      }
    });
    synchronized (compileDriver) {
      try {
        compileDriver.wait();
      } catch (InterruptedException e) {
      }
    }
  }

  @NotNull
  public static String removeHeaderComment(@NotNull String code) {
    final int beginComment = code.indexOf("/*");
    final int endComment = code.indexOf("*/");
    final String firstSegment = code.substring(0, beginComment);
    final String secondSegment = code.substring(endComment + 2, code.length());
    return firstSegment + secondSegment;
  }

  @Nullable
  @Override
  protected Sdk getTestProjectJdk() {
    Sdk gdk = GosuSdkUtils.initDefaultGosuSDK();
    if (gdk == null) {
      throw new RuntimeException("Default Gosu SDK could not be found!");
    }
    List<File> extraPaths = getExtraSDKPaths();

    if (extraPaths != null) {
      SdkModificator modificator = gdk.getSdkModificator();
      GosuSdkUtils.addSdkElements(modificator, extraPaths);
      modificator.commitChanges();
    }
    return gdk;
  }

  private List<File> getExtraSDKPaths( ) {
    List<File> extraClasspath = new ArrayList<>();
    String classpath = System.getProperty("java.class.path", "");
    for( String path: classpath.split( File.pathSeparator ) ) {
      if( !extraClasspath.contains( path ) ) {
        extraClasspath.add( new File(path) );
      }
    }
    return extraClasspath;
  }

  public static void runWriteActionInDispatchThread(@NotNull final Runnable operation, boolean blocking) {
    final Application application = ApplicationManager.getApplication();

    final Runnable action = new Runnable() {
      public void run() {
        application.runWriteAction(operation);
      }
    };

    if (application.isDispatchThread()) {
      action.run();
    } else if (blocking) {
      application.invokeAndWait(action, ModalityState.defaultModalityState());
    } else {
      application.invokeLater(action);
    }
  }

  public static void settleModalEventQueue() {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
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
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
