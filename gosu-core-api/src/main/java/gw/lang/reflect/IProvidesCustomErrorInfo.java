/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface IProvidesCustomErrorInfo {

  public enum ErrorLevel {
    INFO,
    WARNING,
    ERROR
  }

  public class CustomErrorInfo {
    ErrorLevel _level;
    String _msg;
    int _start;
    int _end;

    public CustomErrorInfo(ErrorLevel level, String msg, int start, int end) {
      _level = level;
      _msg = msg;
      _start = start;
      _end = end;
    }

    public ErrorLevel getLevel() {
      return _level;
    }

    public String getMessage() {
      return _msg;
    }

    public int getStart() {
      return _start;
    }

    public int getEnd() {
      return _end;
    }
  }

  public List<CustomErrorInfo> getCustomErrors();

}
