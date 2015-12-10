/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.process;

import gw.util.StreamUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
class ChainedOutputHandler implements OutputHandler, Closeable {
  private static final OutputHandler NULL_OUTPUT_HANDLER = new OutputHandler() {
    public void handleLine(String line) {}
  };
  private List<OutputHandler> _handlers = new ArrayList<OutputHandler>();

  void add(OutputHandler handler) {
    _handlers.add(handler);
  }

  OutputHandler maybeReduce() {
    switch (_handlers.size()) {
    case 0:
      return NULL_OUTPUT_HANDLER;
    case 1:
      return _handlers.get(0);
    default:
      return this;
    }
  }

  @Override
  public void handleLine(String line) {
    for (OutputHandler handler : _handlers) {
      handler.handleLine(line);
    }
  }

  @Override
  public void close() throws IOException {
    close(0);
  }

  private void close(int idx) throws IOException {
    if (idx >= _handlers.size()) {
      return; // done
    }
    OutputHandler handler = _handlers.get(idx);
    try {
      if (handler instanceof Closeable) {
        StreamUtil.close((Closeable) handler);
      }
    } finally {
      close(idx + 1);
    }
  }
}
