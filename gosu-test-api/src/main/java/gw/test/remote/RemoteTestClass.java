/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.test.TestMetadata;
import junit.framework.AssertionFailedError;
import gw.lang.reflect.IType;
import gw.xml.simple.SimpleXmlNode;
import gw.test.remote.RemoteAssertionFailedError;
import gw.test.remote.RemoteTestException;
import gw.test.TestClass;
import gw.test.TestExecutionManager;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class RemoteTestClass extends TestClass {

  private String _typeName;
  private String _methodName;
  private int _totalNumMethods;

  public RemoteTestClass(String typeName, String methodName, int totalNumMethods, TestExecutionManager executionManager) {
    super(methodName, false);
    _typeName = typeName;
    _methodName = methodName;
    _totalNumMethods = totalNumMethods;
    setExecutionManager(executionManager);
    initInternalData();
  }

  @Override
  public void runBare() throws Throwable {
    byte[] response = makeRemoteRequest(getRemoteServerURL(), _typeName.replace('.', '/') + "/" + _methodName + "/runTestMethod");
    RemoteTestResult result = RemoteTestResult.fromXML(new String(response, "UTF-8"));
    if (!result.successful()) {
      throw result.recreateException();
    }
  }

  @Override
  public void beforeTestClass() {
    makeRemoteRequestThatReturnsRemoteTestResult("/beforeTestClass");
  }

  @Override
  public void afterTestClass() {
    makeRemoteRequestThatReturnsRemoteTestResult("/afterTestClass");
  }

  private void makeRemoteRequestThatReturnsRemoteTestResult(String restOfTheURL) {
    byte[] response = makeRemoteRequest(getRemoteServerURL(), _typeName.replace('.', '/') + restOfTheURL);
    RemoteTestResult result;
    try {
      result = RemoteTestResult.fromXML(new String(response, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    if (!result.successful()) {
      Throwable t = result.recreateException();
      if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new RuntimeException(t);
      }
    }
  }

  private String getRemoteServerURL() {
    return ((ForwardingTestEnvironment) getExecutionManager().getEnvironment()).getRemoteURL();
  }

  @Override
  public String toString() {
    return this.getName() + "(" + _typeName + ")";
  }

  @Override
  public String getTypeName() {
    return _typeName;
  }

  @Override
  public IType getType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTotalNumTestMethods() {
    return _totalNumMethods;
  }

  @Override
  protected String getFullClassNameInternal() {
    return _typeName;
  }

  // We override the method here so that RemoteTestClassWrapper, which is in the same pacakge, can access it
  @Override
  protected void addMetadata(Collection<TestMetadata> metadata) {
    super.addMetadata(metadata);
  }

  public static byte[] makeRemoteRequest(String baseURL, String relativePath) {
    try {
      URL url = new URL(baseURL + "/" + relativePath);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      return readResponse(connection.getInputStream());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] readResponse(InputStream is) throws IOException {
    byte[] readBuffer = new byte[16384];
    byte[] result = new byte[0];
    while (true) {
      int numRead = is.read(readBuffer, 0, readBuffer.length);
      if (numRead < 0) {
        break;
      }
      result = copyInput(result, readBuffer, numRead);
    }
    return result;
  }

  private static byte[] copyInput(byte[] currentResult, byte[] newBuffer, int numRead) {
    byte[] newResult = new byte[currentResult.length + numRead];
    System.arraycopy(currentResult, 0, newResult, 0, currentResult.length);
    System.arraycopy(newBuffer, 0, newResult, currentResult.length, numRead);
    return newResult;
  }
}
