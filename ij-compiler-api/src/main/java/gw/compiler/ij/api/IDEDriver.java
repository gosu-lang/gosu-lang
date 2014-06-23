/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api;

import gw.compiler.ij.api.messages.CompilationDoneMessage;
import gw.compiler.ij.api.messages.CompilationItem;
import gw.compiler.ij.api.messages.CompileIssueMessage;
import gw.compiler.ij.api.messages.CompiledMessage;
import gw.compiler.ij.api.messages.CompilingMessage;
import gw.compiler.ij.api.messages.RequestCompileMessage;
import gw.compiler.ij.api.messages.RequestTerminateMessage;
import gw.compiler.ij.api.messages.UncaughtExceptionMessage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class IDEDriver {
  private static class State {
    public final Process process;
    public final ObjectClient client;

    public State(Process process, ObjectClient client) {
      this.process = process;
      this.client = client;
    }
  }

  private final AtomicReference<State> state = new AtomicReference();

  protected static void close(State s, boolean force) {
    if (force) {
      s.process.destroy();
    } else {
      try {
        s.client.write(RequestTerminateMessage.INSTANCE, true);
      } catch (IOException e) {
        s.process.destroy();
      }
    }

    try {
      s.process.waitFor();
    } catch (InterruptedException e) {
      Thread.interrupted();
    }
  }

  protected abstract Process launchProcess() throws Exception;

  public void compile(String moduleName, List<CompilationItem> items, IDECallback callback) {
    State s = state.get();
    if (s == null) {
      try {
        final Process process = launchProcess();
        final ObjectClient client = new ObjectClient(process.getInputStream(), process.getOutputStream());
        s = new State(process, client);
        state.set(s);
      } catch (Exception e) {
        callback.fatalError("Can't launch external compiler: " + e.getMessage(), null);
        return;
      }
    }

    try {
      s.client.write(new RequestCompileMessage(moduleName, items), true);
      while (true) {
        final Object message = s.client.read();

        if ((message instanceof CompilingMessage)) {
          callback.compiling(((CompilingMessage) message).item);
        }

        if ((message instanceof CompileIssueMessage)) {
          final CompileIssueMessage m = (CompileIssueMessage) message;
          callback.compileIssue(m.file, m.category, m.offset, m.line, m.column, m.message);
        }

        if ((message instanceof CompiledMessage)) {
          final CompiledMessage m = (CompiledMessage) message;
          callback.compiled(m.item, m.compilationTime, m.successfully, m.dependencies, m.displayKeyDependencies, m.fingerprint);
        }

        if ((message instanceof CompilationDoneMessage)) {
          return;
        }

        if ((message instanceof UncaughtExceptionMessage)) {
          close(s, true);
          state.set(null);
          callback.fatalError("Exception in remote compiler process", ((UncaughtExceptionMessage) message).e);
          return;
        }
      }
    } catch (IOException e) {
      close(s, true);
      state.set(null);
      callback.fatalError("Exception during compilation", e);
    }
  }

  public void close() {
    final State s = state.getAndSet(null);
    if (s != null) {
      close(s, false);
    }
  }
}