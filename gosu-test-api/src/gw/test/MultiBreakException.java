/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import java.util.List;

public class MultiBreakException extends RuntimeException {

  private final List<TestBreakInfo> _testBreakInfoList;

  public MultiBreakException( List<TestBreakInfo> testBreakInfoList ) {
    _testBreakInfoList = testBreakInfoList;
  }

  public MultiBreakException( String message, List<TestBreakInfo> testBreakInfoList ) {
    super( message );
    _testBreakInfoList = testBreakInfoList;
  }

  public List<TestBreakInfo> getTestBreakInfoList() {
    return _testBreakInfoList;
  }

}
