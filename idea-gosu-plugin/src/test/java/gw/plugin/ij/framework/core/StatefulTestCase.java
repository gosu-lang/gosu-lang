/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.core.PluginLoaderUtil;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class StatefulTestCase extends TestCase {
  @Nullable
  private static IModule rootModule = null;
  private static StatefulTestCase statefulTestInstance = new MyStatefulTestCase();
  private static final Map<Class, Set<String>> allTests = new HashMap();
  private static final Map<Class, Set<String>> runTests = new HashMap();

  protected enum ExecutionState {
    PENDING,
    EXECUTING,
    COMPLETE,
  }

  public static final AtomicReference<ExecutionState> _afterClassExecuting = new AtomicReference<>(ExecutionState.PENDING);
  public static final Object _afterClassMonitor = new Object();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          _afterClassExecuting.set(ExecutionState.EXECUTING);
          SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
              if (statefulTestInstance != null) {
                try {
                  statefulTestInstance.afterClass();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
        }
        _afterClassExecuting.set(ExecutionState.COMPLETE);
        synchronized (_afterClassMonitor) {
          _afterClassMonitor.notifyAll();
        }
      }
    });
  }

  @Nullable
  public abstract Project getProject();

  @Override
  public void setName(String name) {
    addTestName(getClass(), name, allTests);
    super.setName(name);
  }

  private void addTestName(Class aClass, String name, @NotNull Map<Class, Set<String>> map){
    Set<String> strings = map.get(aClass);
    if (strings == null) {
      strings = new HashSet<>();
      map.put(aClass, strings);
    }
    strings.add(name);
  }

  protected boolean runInDispatchThread() {
    return true;
  }

  protected void invokeInUIThread(@NotNull final VoidCallback runnable) throws Throwable {
    final Throwable[] exception = {null};
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      @Override
      public void run() {
        Project project = getProject();
        IModule root = null;
        if( project != null ) {
          root = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( project ) ).getGlobalModule();
          if (root != null) {
            TypeSystem.pushModule(root);
          }
        }
        try {
          runnable.run();
        } catch (Throwable tearingDown) {
          if (exception[0] == null) exception[0] = tearingDown;
        } finally {
          if (root != null) {
            TypeSystem.popModule(root);
          }
        }
      }
    });
    if (exception[0] != null) throw exception[0];
  }

  public void runBare() throws Throwable {
    if (runTests.get(getClass()) == null) {
      invokeInUIThread(new VoidCallback( getProject() ) {
        public void run() throws Throwable {
          statefulTestInstance = StatefulTestCase.this.getClass().newInstance();
          statefulTestInstance.beforeClass();
        }
      });
    }

    statefulTestInstance.setName(this.getName());
    invokeInUIThread(new VoidCallback( getProject() ) {
      public void run() throws Throwable {
        statefulTestInstance.beforeMethod();
      }
    });

    Throwable exception = null;
    try {
      if (runInDispatchThread()) {
        invokeInUIThread(new VoidCallback( getProject() ) {
          public void run() throws Throwable {
            statefulTestInstance.runTest();
          }
        });
      } else {
        final IModule rootModule = TypeSystem.getGlobalModule();
        TypeSystem.pushModule(rootModule);
        try {
          statefulTestInstance.runTest();
        } finally {
          TypeSystem.popModule(rootModule);
        }
      }
    } catch (Throwable running) {
      exception = running;
    } finally {
      try {
        invokeInUIThread(new VoidCallback( getProject() ) {
          public void run() throws Throwable {
            statefulTestInstance.afterMethod();
          }
        });
      } catch (Throwable tearingDown) {
        if (exception == null) exception = tearingDown;
      } finally {
        addTestName(getClass(), getName(), runTests);
        if (runTests.get(getClass()).equals(allTests.get(getClass()))) {
          try {
            invokeInUIThread(new VoidCallback( getProject() ) {
              public void run() throws Throwable {
                statefulTestInstance.afterClass();
              }
            });
          } catch (Throwable tearingDown) {
            if (exception == null) exception = tearingDown;
          }
        }
      }
    }
    if (exception != null) throw exception;
  }

  protected void beforeClass() throws Exception {
  }

  protected void afterClass() throws Exception {
  }

  protected void beforeMethod() throws Exception {
  }

  protected void afterMethod() throws Exception {
  }

  private static class MyStatefulTestCase extends StatefulTestCase {
    @NotNull
    public Project getProject() {
      throw new UnsupportedOperationException();
    }
  }
}
