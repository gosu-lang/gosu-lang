/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import junit.framework.Test;

import java.util.Collections;
import java.util.List;

public class TestBreakInfo {

  private final String _name;
  private final List<? extends ITestMetadata> _metadata;
  private final Throwable _throwable;

  public TestBreakInfo( String name, Throwable throwable ) {
    this( name, Collections.<ITestMetadata>emptyList(), throwable );
  }

  public TestBreakInfo( String name, List<? extends ITestMetadata> metadata, Throwable throwable ) {
    _name = name;
    _metadata = metadata == null ? Collections.<ITestMetadata>emptyList() : metadata;
    _throwable = throwable;
  }

  public String getName() {
    return _name;
  }

  public List<? extends ITestMetadata> getMetadata() {
    return _metadata;
  }

  public Throwable getThrowable() {
    return _throwable;
  }

}
