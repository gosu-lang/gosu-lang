/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.process;

import java.io.PrintStream;

/**
 */
class EchoOutputEmitter {

  private String _processDescription;
  private PrintStream _out;
  private PrintStream _err;

  EchoOutputEmitter(String processDescription, PrintStream out, PrintStream err) {
    _processDescription = processDescription;
    _out = out;
    _err = err;
  }

  void processStarted() {
    _out.println("/--- " + _processDescription);
  }

  OutputHandler getStdOutHandler() {
    return new OutputHandler() {
      @Override
      public void handleLine(String line) {
        _out.println("| " + line);
      }
    };
  }

  OutputHandler getStdErrHandler() {
    return new OutputHandler() {
      @Override
      public void handleLine(String line) {
        _err.println("| " + line);
      }
    };
  }

  void processFinished() {
    _out.println("\\---");
  }
}
