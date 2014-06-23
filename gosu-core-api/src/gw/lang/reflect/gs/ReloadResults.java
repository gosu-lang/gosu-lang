/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import java.util.List;

public class ReloadResults {
  
  public enum ReloadStatus {
    SUCCESS,
    NOT_ATTEMPTED,
    PARSE_FAILURE,
    REDEFINE_REJECTED,
    REDEFINE_ERROR,
    OTHER_ERROR
  }

  private ReloadStatus _status;
  private List<String> _classNames;
  private String _errorMessage;

  public ReloadResults(ReloadStatus status, List<String> classNames, String errorMessage) {
    _status = status;
    _classNames = classNames;
    _errorMessage = errorMessage;
  }

  public ReloadStatus getStatus() {
    return _status;
  }

  public List<String> getClassNames() {
    return _classNames;
  }

  public String getErrorMessage() {
    return _errorMessage;
  }
}
