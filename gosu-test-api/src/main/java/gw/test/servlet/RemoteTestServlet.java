/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.servlet;

import gw.config.CommonServices;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.test.TestClass;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestEnvironment;
import gw.test.TestMetadata;
import gw.test.TestSpec;
import gw.test.remote.ForwardingTestEnvironment;
import gw.test.remote.RemoteTestResult;
import gw.util.GosuStringUtil;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.test.remote.RemoteTestResult;
import gw.xml.simple.SimpleXmlNode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;

public class RemoteTestServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    handleRequest(request, response);
  }

  public static String TEST_INFO_COMMAND = "testInfo";
  public static String RUN_TEST_METHOD_COMMAND = "runTestMethod";
  public static String BEFORE_TEST_CLASS_COMMAND = "beforeTestClass";
  public static String AFTER_TEST_CLASS_COMMAND = "afterTestClass";
  public static String REFRESH_TYPES_COMMAND = "refreshTypes";
  public static String PING = "ping";
  public static String REDIRECT_SYSTEM_OUT_COMMAND = "redirectSystemOut";
  public static String REDIRECT_SYSTEM_ERR_COMMAND = "redirectSystemErr";
  public static String STOP_REDIRECTING_OUTPUT_COMMAND = "stopRedirectingOutput";
  public static final String SET_UP_TEST_ENVIRONMENT_COMMAND = "setUpTestEnvironment";
  public static final String TEAR_DOWN_TEST_ENVIRONMENT_COMMAND = "tearDownTestEnvironment";

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
    _originalOut.println("Incoming request for " + request.getPathInfo());

    String[] pathComponents = GosuStringUtil.split(request.getPathInfo(), "/");
    if (!handleRequest(pathComponents, response)) {
      throw new IllegalArgumentException("Unrecognized path " + request.getContextPath());
    }
  }

  protected boolean handleRequest(String[] pathComponents, HttpServletResponse response) {
    String lastComponent = pathComponents[pathComponents.length - 1];
    if (lastComponent.equals(TEST_INFO_COMMAND)) {
      handleTestInfoRequest(pathComponents, response);
    } else if (lastComponent.equals(RUN_TEST_METHOD_COMMAND)) {
      handleRunTestMethodRequest(pathComponents, response);
    } else if (lastComponent.equals(BEFORE_TEST_CLASS_COMMAND)) {
      handleBeforeTestClassRequest(pathComponents, response);
    } else if (lastComponent.equals(AFTER_TEST_CLASS_COMMAND)) {
      handleAfterTestClassRequest(pathComponents, response);
    } else if (lastComponent.equals(REFRESH_TYPES_COMMAND)) {
      handleRefreshTypesRequest(response);
    } else if (lastComponent.equals(PING)) {
      handlePingRequest(response);
    } else if (lastComponent.equals(REDIRECT_SYSTEM_OUT_COMMAND)) {
      handleRedirectSystemOutRequest(response);
    } else if (lastComponent.equals(REDIRECT_SYSTEM_ERR_COMMAND)) {
      handleRedirectSystemErrRequest(response);
    } else if (lastComponent.equals(STOP_REDIRECTING_OUTPUT_COMMAND)) {
      handleStopRedirectingOutputRequest(response);
    } else if (lastComponent.equals(SET_UP_TEST_ENVIRONMENT_COMMAND)) {
      handleSetUpTestEnvironmentRequest(pathComponents, response);
    } else if (lastComponent.equals(TEAR_DOWN_TEST_ENVIRONMENT_COMMAND)) {
      handleTearDownTestEnvironmentRequest(pathComponents, response);
    } else {
      // Return false if the request falls through and isn't handled
      return false;
    }

    // Return true if any other branch was taken, meaning that something handled it
    return true;
  }

  private void handleTestInfoRequest(String[] pathComponents, HttpServletResponse response) {
    String typeName = constructTypeName(pathComponents, 0, pathComponents.length - 1);
    String testInfoResults;
    try {
      testInfoResults = constructTestInfo(typeName);
    } catch (Throwable t) {
      t.printStackTrace();
      // If there's an error, return an empty test info with no methods, rather than just nothing at all
      // TODO - AHK - Return additional error information back
      SimpleXmlNode classInfoNode = new SimpleXmlNode("TestClassInfo");
      classInfoNode.getAttributes().put("name", typeName);
      testInfoResults = classInfoNode.toXmlString();
    }

    try {
      response.setContentType("text/html; charset=UTF8");
      PrintWriter out = response.getWriter();
      out.print(testInfoResults);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String constructTestInfo(String typeName) {
    IType testType = TypeSystem.getByFullName(typeName);

    // If the type doesn't parse, then we need to bail out
    if (!testType.isValid()) {
      if (testType instanceof IGosuClass) {
        throw new RuntimeException(((IGosuClass)testType).getParseResultsException());
      } else {
        throw new IllegalStateException("Test type " + testType + " is not valid.");
      }
    }

    String[] methodNames = TestSpec.extractTestMethods(testType);

    // TODO - AHK - Should be consolidated with code in TestClassWrapper
    ITypeInfo typeInfo = testType.getTypeInfo();
    IConstructorInfo noArgCons = typeInfo.getConstructor();
    TestClass test;
    if (noArgCons != null) {
      test = (TestClass) noArgCons.getConstructor().newInstance();
      test.createClassMetadata();
    } else {
      IConstructorInfo oneArgCons = typeInfo.getConstructor(JavaTypes.STRING());
      if (oneArgCons != null) {
        test = (TestClass) oneArgCons.getConstructor().newInstance("dummy");
        test.createClassMetadata();
      } else {
        throw new IllegalStateException("Test type " + testType + " does not have either a no-arg constructor or a one-arg constructor taking a String");
      }
    }

    SimpleXmlNode classInfoNode = new SimpleXmlNode("TestClassInfo");
    classInfoNode.getAttributes().put("name", typeName);

    Collection<TestMetadata> classMetadata = test.createClassMetadata();
    for (String methodName : methodNames) {
      SimpleXmlNode methodInfoNode = new SimpleXmlNode("TestMethodInfo");
      methodInfoNode.getAttributes().put("name", methodName);
      classInfoNode.getChildren().add(methodInfoNode);

      for (TestMetadata md : classMetadata) {
        methodInfoNode.getChildren().add(md.serializeToXml());
      }

      Collection<TestMetadata> methodMetadata = test.createMethodMetadata(methodName);
      for (TestMetadata md : methodMetadata) {
        methodInfoNode.getChildren().add(md.serializeToXml());
      }
    }

    return classInfoNode.toXmlString();
  }

  private void handleBeforeTestClassRequest(String[] pathComponents, HttpServletResponse response) {
    handleTestClassMethod(pathComponents, pathComponents.length - 1, response, new TestClassCallback() {
      @Override
      public void invoke(TestClass testClass) {
        testClass.beforeTestClass();
      }
    });
  }



  private void handleAfterTestClassRequest(String[] pathComponents, HttpServletResponse response) {
    handleTestClassMethod(pathComponents, pathComponents.length - 1, response, new TestClassCallback() {
      @Override
      public void invoke(TestClass testClass) {
        testClass.afterTestClass();
      }
    });
  }

  private void handleRunTestMethodRequest(final String[] pathComponents, HttpServletResponse response) {
    handleTestClassMethod(pathComponents, pathComponents.length - 2, response, new TestClassCallback() {
      @Override
      public void invoke(TestClass testClass) throws Throwable {
        // TODO - AHK - This should reuse more of the code from TestClassWrapper
        String methodName = pathComponents[pathComponents.length - 2];
        testClass.setName(methodName);
        testClass.initMetadata(methodName);
        callTestMethod(testClass);
      }
    });
  }

  private void callTestMethod(TestClass testClass) throws Throwable {
    // TODO - AHK - This should probably just work through the TestExecutionManager
    testClass.beforeTestMethod();
    try {
      testClass.reallyRunBare();
    } catch (Throwable e) {
      testClass.afterTestMethod(e);
      throw e;
    }
    testClass.afterTestMethod(null);
  }

  private interface TestClassCallback {
    void invoke(TestClass testClass) throws Throwable;
  }

  private void handleTestClassMethod(String[] pathComponents, int lastNameIndex, HttpServletResponse response, TestClassCallback callback) {
    RemoteTestResult testResult = new RemoteTestResult();
    try {
      String typeName = constructTypeName(pathComponents, 0, lastNameIndex);
      TestClass testClass = createTestClassInstance(typeName);
      callback.invoke(testClass);
    } catch (Throwable t) {
      testResult.setException(t);
    }

    sendRemoteTestResult(response, testResult);
  }

  private String constructTypeName(String[] components, int start, int end) {
    StringBuilder typeName = new StringBuilder();
    for (int i = start; i < end; i++) {
      if (i > start) {
        typeName.append(".");
      }
      typeName.append(components[i]);
    }

    return typeName.toString();
  }

  private TestClass createTestClassInstance(String typeName) {
    IType testType = TypeSystem.getByFullName(typeName);
    if( !(testType instanceof IJavaType) ){
      try {
        if( !(testType instanceof IGosuClass) ||
            ((IGosuClass)testType).getBackingClass() != getTestClass( typeName, testType ) ) {
          // So we have a java class and an non-java class. Go boom
          throw(new IllegalStateException("Type " + typeName + " exists as both a java type and a non-java type."));
        }
      } catch (ClassNotFoundException e) {
        // This is fine
      }
    }
    return TestClass.createTestClass(testType);
  }

  private Class<?> getTestClass( String typeName, IType testType ) throws ClassNotFoundException {
    return Class.forName( typeName, false, testType.getTypeLoader().getModule().getModuleTypeLoader().getDefaultTypeLoader().getGosuClassLoader().getActualLoader() );
  }

  private void sendRemoteTestResult(HttpServletResponse response, RemoteTestResult testResult) {
    try {
      response.setContentType("text/html; charset=UTF8");
      PrintWriter out = response.getWriter();
      out.print(testResult.toXML());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }   

  private void handleRefreshTypesRequest(HttpServletResponse response) {
    try {
      ChangedTypesRefresher.getInstance().reloadChangedTypes();
      response.getOutputStream().close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private void handlePingRequest(HttpServletResponse response) {
    try {
      response.getOutputStream().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void handleSetUpTestEnvironmentRequest(String[] pathComponents, HttpServletResponse response) {
    TestEnvironment testEnvironment = createTestEnvironment(pathComponents);


    boolean success;
    try {
      testEnvironment.beforeRemoteExecution();
      success = true;
    } catch (Exception e) {
      e.printStackTrace();
      success = false;
    }

    try {
      response.setContentType("text/html; charset=UTF8");
      PrintWriter out = response.getWriter();
      out.print(success);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void handleTearDownTestEnvironmentRequest(String[] pathComponents, HttpServletResponse response) {
    TestEnvironment testEnvironment = createTestEnvironment(pathComponents);

    boolean success;
    try {
      testEnvironment.afterRemoteExecution();
      success = true;
    } catch (Exception e) {
      e.printStackTrace();
      success = false;
    }

    try {
      response.setContentType("text/html; charset=UTF8");
      PrintWriter out = response.getWriter();
      out.print(success);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private TestEnvironment createTestEnvironment(String[] pathComponents) {
    // TODO - AHK - Error handling
    // TODO - AHK - Validate that it's actually a TestEnvironment
    // TODO - AHK - Handle the case where there's no appropriate constructor
    String typeName = constructTypeName(pathComponents, 0, pathComponents.length - 2);
    String[] constructorArgs = splitArgs(pathComponents[pathComponents.length - 2]);
    IType testEnvironmentType = TypeSystem.getByFullName(typeName);
    TestEnvironment testEnvironment = null;
    for (IConstructorInfo cons : testEnvironmentType.getTypeInfo().getConstructors()) {
      if (cons.getParameters().length == constructorArgs.length) {
        Object[] convertedArgs = convertConstructorArgs(constructorArgs, cons.getParameters());
        testEnvironment = (TestEnvironment) cons.getConstructor().newInstance(convertedArgs);
        break;
      }
    }
    return testEnvironment;
  }

  private String[] splitArgs(String constructorArgs) {
    if (constructorArgs.equals(ForwardingTestEnvironment.NO_ARGS_STRING)) {
      return new String[0];
    } else {
      return GosuStringUtil.split(constructorArgs, ",");
    }
  }

  private Object[] convertConstructorArgs(String[] constructorArgs, IParameterInfo[] params) {
    Object[] results = new Object[constructorArgs.length];
    for (int i = 0; i < constructorArgs.length; i++) {
      results[i] = convertConstructorArg(constructorArgs[i], params[i].getFeatureType());
    }
    return results;
  }

  private Object convertConstructorArg(String arg, IType targetType) {
    if (targetType.equals(JavaTypes.BOOLEAN()) || targetType.equals(JavaTypes.pBOOLEAN())) {
      return Boolean.valueOf(arg);  
    } else {
      throw new IllegalArgumentException("Unhandled type TestEnvironment constructor argument type " + targetType);
    }
  }

  private static PrintStream _originalOut = System.out;
  private static PrintStream _originalErr = System.err;

  protected void handleRedirectSystemOutRequest(HttpServletResponse response) {
    redirectOutput(response, new SystemOutInfo());
  }

  private void handleRedirectSystemErrRequest(HttpServletResponse response) {
    redirectOutput(response, new SystemErrInfo());
  }
  
  private void handleStopRedirectingOutputRequest(HttpServletResponse response) {
    stopRedirectingOutput(new SystemOutInfo());
    stopRedirectingOutput(new SystemErrInfo());

    try {
      response.getOutputStream().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void stopRedirectingOutput(StreamInfo streamInfo) {
    if (!streamInfo.isCurrentStream(streamInfo.getOriginalStream())) {
      streamInfo.setStream(streamInfo.getOriginalStream());
      afterStreamRedirected(streamInfo);
    }
  }

  protected static abstract class StreamInfo {
    public abstract PrintStream getOriginalStream();
    public abstract void setStream(PrintStream newStream);
    public abstract boolean isCurrentStream(PrintStream stream);
  }

  protected static class SystemOutInfo extends StreamInfo {
    @Override
    public PrintStream getOriginalStream() {
      return _originalOut;
    }

    @Override
    public void setStream(PrintStream newStream) {
      System.setOut(newStream);
    }

    @Override
    public boolean isCurrentStream(PrintStream stream) {
      return System.out == stream;
    }
  }

  protected static class SystemErrInfo extends StreamInfo {
    @Override
    public PrintStream getOriginalStream() {
      return _originalErr;
    }

    @Override
    public void setStream(PrintStream newStream) {
      System.setErr(newStream);
    }

    @Override
    public boolean isCurrentStream(PrintStream stream) {
      return System.err == stream;
    }
  }

  private void redirectOutput(HttpServletResponse response, StreamInfo streamInfo) {
    // TODO - AHK - Soooo . . . this is a total hack to keep the thread handling this alive.  Is there some
    // better way to do this?  How does this ever get closed?
    try {
      RedirectingPrintStream redirectingStream = new RedirectingPrintStream(response.getOutputStream(), streamInfo.getOriginalStream());
      streamInfo.setStream(redirectingStream);
      afterStreamRedirected(streamInfo);
      while (true) {
        try {
          Thread.sleep(500);
          // We want this thread to die if either A) the system output has been directed elsewhere or B)
          // there's been an error in the redirected stream, which generally means that the socket has closed
          // on the other end.  If we're exiting while the output is still redirected, we want to change it back
          if (!streamInfo.isCurrentStream(redirectingStream) || redirectingStream.hasOutputStreamError()) {
            if (streamInfo.isCurrentStream(redirectingStream)) {
              streamInfo.setStream(streamInfo.getOriginalStream());
              afterStreamRedirected(streamInfo);
            }
            break;
          }
        } catch (InterruptedException e) {
          // Ignore
        }
      }
    } catch (IOException e) {
      // TODO - Handle this better somehow?
      e.printStackTrace();
    }
  }

  protected void afterStreamRedirected(StreamInfo streamInfo) {

  }

  private static class RedirectingPrintStream extends PrintStream {

    private PrintStream _s1;
    private PrintStream _s2;

    private RedirectingPrintStream(OutputStream out, PrintStream originalStream) {
      super(out, true);
      _s1 = new PrintStream(out, true);
      _s2 = originalStream;
    }

    public boolean hasOutputStreamError() {
      return _s1.checkError();
    }

    @Override
    public void flush() {
      _s1.flush();
      _s2.flush();
    }

    @Override
    public void close() {
      _s1.close();
      _s2.close();
    }

    @Override
    public void write(int b) {
      _s1.write(b);
      _s2.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
      _s1.write(buf, off, len);
      _s2.write(buf, off, len);
    }

    @Override
    public void print(boolean b) {
      _s1.print(b);
      _s2.print(b);
    }

    @Override
    public void print(char c) {
      _s1.print(c);
      _s2.print(c);
    }

    @Override
    public void print(int i) {
      _s1.print(i);
      _s2.print(i);
    }

    @Override
    public void print(long l) {
      _s1.print(l);
      _s2.print(l);
    }

    @Override
    public void print(float f) {
      _s1.print(f);
      _s2.print(f);
    }

    @Override
    public void print(double d) {
      _s1.print(d);
      _s2.print(d);
    }

    @Override
    public void print(char[] s) {
      _s1.print(s);
      _s2.print(s);
    }

    @Override
    public void print(String s) {
      _s1.print(s);
      _s2.print(s);
    }

    @Override
    public void print(Object obj) {
      _s1.print(obj);
      _s2.print(obj);
    }

    @Override
    public void println() {
      _s1.println();
      _s2.println();
    }

    @Override
    public void println(boolean x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(char x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(int x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(long x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(float x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(double x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(char[] x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(String x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void println(Object x) {
      _s1.println(x);
      _s2.println(x);
    }

    @Override
    public void write(byte[] b) throws IOException {
      _s1.write(b);
      _s2.write(b);
    }


  }
}
