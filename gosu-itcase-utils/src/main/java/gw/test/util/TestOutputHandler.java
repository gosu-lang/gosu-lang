/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.util;

import gw.util.process.OutputHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Chang
 */
public class TestOutputHandler implements OutputHandler {
  private final List<String> _lines = new ArrayList<String>();

  @Override
  public void handleLine(String line) {
    _lines.add(line);
  }

  public List<String> getLines() {
    return _lines;
  }

}
