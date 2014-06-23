/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.test.servlet.RemoteTestServlet;
import gw.test.TestEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteForwardingTestEnvironment extends ForwardingTestEnvironment {

  private final String _remoteURL;

  public RemoteForwardingTestEnvironment(String remoteURL) {
    this(remoteURL, null, null);
  }

  public RemoteForwardingTestEnvironment(String remoteURL, String wrappedTestEnvironmentName, String wrappedTestEnvironmentArgs) {
    this(remoteURL, null, wrappedTestEnvironmentName, wrappedTestEnvironmentArgs, false);
  }

  public RemoteForwardingTestEnvironment(String remoteURL, TestEnvironment wrappedTestEnvironment) {
    super(wrappedTestEnvironment, false);
    _remoteURL = remoteURL;
  }

  private RemoteForwardingTestEnvironment(String remoteURL, TestEnvironment wrappedTestEnvironment, String wrappedTestEnvironmentName, String wrappedTestEnvironmentArgs, boolean initializeTypeSystemLocally) {
    super(wrappedTestEnvironment, wrappedTestEnvironmentName, wrappedTestEnvironmentArgs, initializeTypeSystemLocally);
    _remoteURL = remoteURL;
  }

  @Override
  public String getRemoteURL() {
    return _remoteURL;
  }

  @Override
  public void beforeTestSuite() {
    Thread systemOutThread = new OutputRedirectThread(_remoteURL, RemoteTestServlet.REDIRECT_SYSTEM_OUT_COMMAND) {
      @Override
      void write(byte[] buf, int off, int len) {
        System.out.write(buf, off, len);
      }
    };
    Thread systemErrThread = new OutputRedirectThread(_remoteURL, RemoteTestServlet.REDIRECT_SYSTEM_ERR_COMMAND) {
      @Override
      void write(byte[] buf, int off, int len) {
        System.err.write(buf, off, len);
      }
    };
    systemOutThread.setDaemon(true);
    systemErrThread.setDaemon(true);
    systemOutThread.start();
    systemErrThread.start();
  }

  @Override
  public void afterTestSuite() {
    super.afterTestSuite();
    RemoteTestClass.makeRemoteRequest(_remoteURL, RemoteTestServlet.STOP_REDIRECTING_OUTPUT_COMMAND);
  }

  private abstract static class OutputRedirectThread extends Thread {
    private String _remoteURL;
    private String _command;

    protected OutputRedirectThread(String remoteURL, String command) {
      _remoteURL = remoteURL;
      _command = command;
    }

    @Override
    public void run() {
      try {
        URL url = new URL(_remoteURL + "/" + _command);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream is = connection.getInputStream();
        byte[] buffer = new byte[16384];
        while (true) {
          int numRead = is.read(buffer, 0, 16384);
          if (numRead <= 0) {
            break;
          } else {
            write(buffer, 0, numRead);
          }
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    abstract void write(byte buf[], int off, int len);
  }
}
