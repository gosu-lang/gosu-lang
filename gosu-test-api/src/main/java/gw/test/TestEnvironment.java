/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;

import java.io.File;
import java.util.List;

public class TestEnvironment {

  public void initializeTypeSystem() {
    if (!GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).isInitialized() ) {
      GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).initializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(constructClasspathFromSystemClasspath()));
    }
  }

  public boolean isRemoteExecutionEnvironment() {
    return false;
  }

  public boolean isDynamicallyDeterminedEnvironment() {
    return false;
  }

  protected List<File> constructClasspathFromSystemClasspath() {
    return ClassPathUtil.constructClasspathFromSystemClasspath();
  }

  public void beforeTestSuite() {

  }

  public void afterTestSuite() {

  }

  public void beforeTestClass() {

  }

  public void afterTestClass() {

  }

  public void beforeTestMethod() {

  }

  public void afterTestMethod() {

  }

  /**
   * This is a hook that gets executed on the remote server (not the local server) prior to execution of a suite
   * that would normally run in this environment when running locally.
   */
  public void beforeRemoteExecution() throws Exception {
    // By default, nothing to do
  }

  public Object[] getConstructorArguments() {
    return new Object[0];
  }

  /**
   * This is a hook that gets executed on the remote server (not the local server) after execution of a suite
   * that would normally run in this environment when running locally.
   */
  public void afterRemoteExecution() throws Exception {
    // By default, nothing to do
  }
}
