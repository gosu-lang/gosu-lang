/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.util.Computable;
import org.jetbrains.annotations.NotNull;

public class ExecutionUtil
{
  public static final int WRITE = 1;
  public static final int DISPATCH = 2;
  public static final int BLOCKING = 4;

  public static <R> R execute(@NotNull SafeCallable<R> operation) {
    return execute(0, operation);
  }

  public static void execute(@NotNull SafeRunnable operation) {
    execute(0, operation);
  }

  public static void execute(int options, @NotNull SafeRunnable operation) {
    Runnable task = wrap(operation, options);
    if (isDispatch(options)) {
      Application app = ApplicationManager.getApplication();
      if (isBlocking(options)) {
        app.invokeAndWait(task, ModalityState.defaultModalityState());
      } else {
        app.invokeLater(task);
      }
    } else {
      task.run();
    }
  }

  public static <R> R execute(int options, @NotNull SafeCallable<R> operation) {
    Runnable task = wrap(operation, options);
    if (isDispatch(options)) {
      Application app = ApplicationManager.getApplication();
      if (app.isDispatchThread()) {
        task.run();
      } else if (isBlocking(options)) {
        app.invokeAndWait(task, ModalityState.defaultModalityState());
      } else {
        app.invokeLater(task);
      }
    } else {
      task.run();
    }
    return operation.getResult();
  }

  private static Runnable wrap(final SafeRunnable operation, int options) {
    if (isWrite(options)) {
      return new Runnable() {
        public void run() {
          ApplicationManager.getApplication().runWriteAction(operation);
        }
      };
    } else {
      return operation;
    }
  }

  private static Runnable wrap(final SafeCallable operation, int options) {
    if (isWrite(options)) {
      return new Runnable() {
        public void run() {
          ApplicationManager.getApplication().runWriteAction((Computable)operation);
        }
      };
    } else {
      return operation;
    }
  }

  private static boolean isWrite(int options) {
    return (options & WRITE) > 0;
  }

  private static boolean isDispatch(int options) {
    return (options & DISPATCH) > 0;
  }

  private static boolean isBlocking(int options) {
    return (options & BLOCKING) > 0;
  }

}
