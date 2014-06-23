/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.test.TestEnvironment;
import gw.test.servlet.RemoteTestServlet;
import gw.util.StreamUtil;

public abstract class ForwardingTestEnvironment extends TestEnvironment {

  public static final String NO_ARGS_STRING = "__noargs__";
  private TestEnvironment _wrappedTestEnvironment;
  private String _wrappedTestEnvironmentName;
  private String _wrappedTestEnvironmentArgs;
  private boolean _initializeTypeSystemLocally;

  protected ForwardingTestEnvironment(TestEnvironment wrappedTestEnvironment, boolean initializeTypeSystemLocally) {
    this(wrappedTestEnvironment, wrappedTestEnvironment.getClass().getName(), formatTestEnvironmentArgs(wrappedTestEnvironment.getConstructorArguments()), initializeTypeSystemLocally);
  }

  protected ForwardingTestEnvironment(TestEnvironment wrappedTestEnvironment, String wrappedTestEnvironmentName, String wrappedTestEnvironmentArgs, boolean initializeTypeSystemLocally) {
    _wrappedTestEnvironment = wrappedTestEnvironment;
    _initializeTypeSystemLocally = initializeTypeSystemLocally;
    _wrappedTestEnvironmentName = wrappedTestEnvironmentName;
    _wrappedTestEnvironmentArgs = wrappedTestEnvironmentArgs;
    if (_wrappedTestEnvironmentName != null && (_wrappedTestEnvironmentArgs == null || _wrappedTestEnvironmentArgs.isEmpty())) {
      _wrappedTestEnvironmentArgs = NO_ARGS_STRING;
    }
  }

  private static String formatTestEnvironmentArgs(Object[] args) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      if (args[i] instanceof Boolean) {
        sb.append(args[i].toString());
      } else {
        throw new IllegalArgumentException("Unhandled object of type " + args[i].getClass());
      }
    }
    return sb.toString();
  }

  @Override
  public void initializeTypeSystem() {
    if (_wrappedTestEnvironment != null) {
      _wrappedTestEnvironment.initializeTypeSystem();
    }

    waitForRemotePlatformToStart(5 * 60 * 1000);

    if (!_initializeTypeSystemLocally) {
      // TODO - AHK - Ensure this is only called once
      RemoteTestClass.makeRemoteRequest(getRemoteURL(), RemoteTestServlet.REFRESH_TYPES_COMMAND);
    }

    // If we're wrapping an existing test environment, we want to remotely execute any necessary setup, such as modifying
    // the server's runlevel, backing up shadow tables, or importing sample data.  To that end, we send the remote test
    // server a request with the name of the wrapped test environment, which the remote server will then reflectively
    // construct and invoke methods on
    if (_wrappedTestEnvironmentName != null) {
      byte[] results = RemoteTestClass.makeRemoteRequest(getRemoteURL(), _wrappedTestEnvironmentName.replace('.', '/') + "/" + _wrappedTestEnvironmentArgs + "/" + RemoteTestServlet.SET_UP_TEST_ENVIRONMENT_COMMAND);
      String resultString = StreamUtil.toString(results);
      Boolean success = Boolean.valueOf(resultString);
      if (!success.booleanValue()) {
        throw new RuntimeException("Remote server setup failed.  Please check the logs on the remote server for details: \"" + resultString + "\"");
      }
    }
  }

  private void waitForRemotePlatformToStart(long timeoutMs) {
    long waitStarts = System.currentTimeMillis();
    while ((System.currentTimeMillis() - waitStarts) < timeoutMs) {
      try {
        String value = StreamUtil.toString(RemoteTestClass.makeRemoteRequest(getRemoteURL(), "isStarted"));
        if (Boolean.valueOf(value)) {
          return;
        }
      } catch (Exception e) {
        // Just wait more.
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException("Wait was interrupted. Platform did not start.");
      }
    }
    throw new RuntimeException("Platform did not start after " + timeoutMs + "ms.");
  }

  @Override
  public void afterTestSuite() {
    if (_wrappedTestEnvironmentName != null) {
      RemoteTestClass.makeRemoteRequest(getRemoteURL(), _wrappedTestEnvironmentName.replace('.', '/') + "/" + _wrappedTestEnvironmentArgs + "/" + RemoteTestServlet.TEAR_DOWN_TEST_ENVIRONMENT_COMMAND);
    }
  }

  @Override
  public boolean isRemoteExecutionEnvironment() {
    return true;
  }

  public abstract String getRemoteURL();

  public String getWrappedEnvironmentClassName() {
    return _wrappedTestEnvironmentName;
  }

  public String getWrappedTestEnvironmentArgs() {
    return _wrappedTestEnvironmentArgs;
  }

}
