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
import gw.fs.IFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CompilerDriver {
  private ObjectClient client;

  public CompilerDriver(ObjectClient client) {
    this.client = client;
  }

  public void connect(CompilerCallback callback) throws IOException {
    while (true) {
      final Object message = client.read();
      try {
        if ((message instanceof RequestCompileMessage)) {
          final RequestCompileMessage m = (RequestCompileMessage) message;
          callback.compile(m.moduleName, m.items);
        }

        if ((message instanceof RequestTerminateMessage)) {
          break;
        }
      } catch (Throwable e) {
        sendUncaughtException(e);
      }
    }
  }

  private void sendUncaughtException(Throwable e) throws IOException {
    client.write(new UncaughtExceptionMessage(e), true);
  }

  public void sendCompiling(CompilationItem item) throws IOException {
    client.write(new CompilingMessage(item), true);
  }

  public void sendCompileIssue(File file, CompileIssueMessage.Category category, int offset, int line, int column, String message) throws IOException {
    client.write(new CompileIssueMessage(file, category, offset, line, column, message), true);
  }

  public void sendCompiled(CompilationItem item, int compilationTime, boolean successfully, Set<IFile> dependencies, Set<String> displayKeyDependencies, long fingerprint) throws IOException {
    client.write(new CompiledMessage(item, compilationTime, successfully, dependencies, displayKeyDependencies, fingerprint), true);
  }

  public void sendCompilationDone() throws IOException {
    client.write(CompilationDoneMessage.INSTANCE, true);
  }
}
