/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.process;

import java.io.StringWriter;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
class OutputBuffer extends StringWriter implements OutputHandler {
  private final ReentrantLock _lock = new ReentrantLock();

  @Override
  public void handleLine(String line) {
    _lock.lock();
    try {
      append(line).append('\n');
    } finally {
      _lock.unlock();
    }
  }
}
