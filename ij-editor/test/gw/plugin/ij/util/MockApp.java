/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.mock.MockApplicationEx;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.ThrowableComputable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class MockApp extends MockApplicationEx {
  public boolean inWriteAction;

  public MockApp(Disposable paraentDisposable) {
    super(paraentDisposable);
  }

  @Override
  public void invokeAndWait(@NotNull Runnable runnable, @NotNull ModalityState modalityState) {
    try {
      SwingUtilities.invokeAndWait(runnable);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void runWriteAction(@NotNull Runnable action) {
    inWriteAction = true;
    try {
      super.runWriteAction(action);
    } finally {
      inWriteAction = false;
    }
  }

  @Override
  public <T> T runWriteAction(@NotNull Computable<T> computation) {
    inWriteAction = true;
    try {
      return super.runWriteAction(computation);
    } finally {
      inWriteAction = false;
    }
  }

  @Override
  public <T, E extends Throwable> T runWriteAction(@NotNull ThrowableComputable<T, E> computation) throws E {
    inWriteAction = true;
    try {
      return super.runWriteAction(computation);
    } finally {
      inWriteAction = false;
    }
  }

  @Override
  public boolean isDispatchThread() {
    return Thread.currentThread().getName().startsWith("AWT-EventQueue-");
  }
}
