/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.test.remote.RemoteTestClass;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The TestExecutionManager class is responsible for the actual execution of tests, including executing the before/after
 * hooks.
 */
public class TestExecutionManager {

  private TestEnvironment _environment;
  private boolean _beforeTestSuiteRun = false;
  private boolean _typeSystemInitialized = false;
  private Throwable _beforeTestSuiteFailed;
  private Map<String, Throwable> _beforeTestClassFailed = new HashMap<String, Throwable>();
  private final Map<String, TestInfo> _testInfos = new HashMap<String, TestInfo>();
  private List<TestSuite> _testWrappers;
  private long _suiteStartTime = 0L;
  private long _suiteTimeoutInMillis = 0L;
  private boolean _suiteHasTimedOut = false;
  private boolean _assertionsMustBeEnabled = true;

  // Timing info
  private long _testClassStartTime = 0L;
  // TODO - AHK - Combine with _suiteStartTime
  private long _suiteStartTimeNs = 0L;

  private static boolean INCLUDE_TEST_TIMING_INFO = Boolean.getBoolean("gw.test.timing.info");

  public void setEnvironment(TestEnvironment environment) {
    _environment = environment;
  }

  public void setTestsFromSuite(List<TestSuite> testWrappers) {
    for (TestSuite suite : testWrappers) {
      _testInfos.put(suite.getName(), new TestInfo(suite.countTestCases()));
    }
    _testWrappers = new ArrayList<TestSuite>(testWrappers);
  }

  public void setSuiteTimeoutInMillis(long suiteTimeoutInMillis) {
    _suiteTimeoutInMillis = suiteTimeoutInMillis;
  }

  public boolean assertionsMustBeEnabled() {
    return _assertionsMustBeEnabled;
  }

  public void setAssertionsMustBeEnabled(boolean assertionsMustBeEnabled) {
    _assertionsMustBeEnabled = assertionsMustBeEnabled;
  }

  public TestEnvironment getEnvironment() {
    return _environment;
  }


  //=============================================================================================
  // Implementation details
  //=============================================================================================

  protected void runTestClass(TestClass testClass, TestResult result) {
    if (_suiteStartTime == 0) {
      _suiteStartTime = System.currentTimeMillis();
    }

    if (_suiteStartTimeNs == 0) {
      _suiteStartTimeNs = System.nanoTime();
    }

    // If we're configured to remotely execute and the test isn't already a remote test, then swap it out before we proceed.
    // This will happen if a test is run from IntelliJ, since IntelliJ will instantiate the test object before we get a chance
    // to intercept it
    if (_environment.isRemoteExecutionEnvironment() && !(testClass instanceof RemoteTestClass)) {
      TestInfo testInfo = _testInfos.get(testClass.getTypeName());
      if (testInfo == null) {
        // We could be running the whole test, or just one method, so we get the number of test instances that were created
        // out of the static map on TestClass; we have no access to the suite, so that's basically the only way to tell
        // how many test methods within a given test class are going to be run
        testInfo = new TestInfo(TestClass.getNumberOfInstancesOfTestClassCreated(testClass.getTypeName()));
        _testInfos.put(testClass.getTypeName(), testInfo);
      }
      testClass = new RemoteTestClassIDEExecutionWrapper(testClass.getTypeName(), testClass.getName(), testInfo._testCount, this, testClass);
    }

    try {
      maybeInitTypeSystem();

      long testStartTimeNs = -1;
      try {
        try {
          maybeCallBeforeTestSuite();
        } catch (Throwable e) {
          // Since the test result.startTest method only gets called if we get to reallyRun,
          // if we fail before then we need to poke the TestResult object so that listeners
          // get notified that the test is starting
          result.startTest(maybeUnwrapTestClass(testClass));
          if (_beforeTestSuiteFailed != null) {
            _beforeTestSuiteFailed = e;
          }
          result.addError(testClass, e);
          result.endTest(testClass);
          throw e;
        }
        try {
          maybeCallBeforeTestClass(testClass);
        } catch (Throwable e) {
          // Since the test result.startTest method only gets called if we get to reallyRun,
          // if we fail before then we need to poke the TestResult object so that listeners
          // get notified that the test is starting
          result.startTest(maybeUnwrapTestClass(testClass));
          if (!_beforeTestClassFailed.containsKey(testClass.getTypeName())) {
            _beforeTestClassFailed.put(testClass.getTypeName(), e);
          }
          result.addError(testClass, e);
          result.endTest(testClass);
          throw e;
        }
        testStartTimeNs = System.nanoTime();
        testClass.reallyRun(result);
      } finally {
        if (testStartTimeNs != -1) {
          printTestRunTime("Method " + testClass.getTypeName() + " " + testClass.getName(), System.nanoTime() - testStartTimeNs);
        }
        try {
          maybeCallAfterTestClass(testClass);
        } finally {
          maybeCallAfterTestSuite(testClass);
        }
      }
    } catch (AssertionFailedError e) {
      result.addFailure(maybeUnwrapTestClass(testClass), e);
    } catch (ThreadDeath e) { // don't catch ThreadDeath by accident
      throw e;
    } catch (Throwable e) {
      result.addError(maybeUnwrapTestClass(testClass), e);
    }
  }

  protected TestClass maybeUnwrapTestClass(TestClass testClass) {
    if (testClass instanceof RemoteTestClassIDEExecutionWrapper) {
      return ((RemoteTestClassIDEExecutionWrapper) testClass)._wrapped;
    } else {
      return testClass;
    }
  }

  protected void runTestClassBare(TestClass testClass) throws Throwable {
    callBeforeTestMethod(testClass);
    try {
      testClass.reallyRunBare();
    } catch (Throwable e) {
      callAfterTestMethod(testClass, e);
      throw e;
    }
    callAfterTestMethod(testClass, null);
  }

  public void maybeInitTypeSystem() {
    if (!_typeSystemInitialized) {
      _typeSystemInitialized = true;
      _environment.initializeTypeSystem();
    }
  }

  private void callAfterTestMethod(TestClass testClass, Throwable e) {
    testClass.afterTestMethod(e);
    _environment.afterTestMethod();
  }

  private void callBeforeTestMethod(TestClass testClass) {
    _environment.beforeTestMethod();
    testClass.beforeTestMethod();
  }

  private void maybeCallBeforeTestSuite() {
    if (_beforeTestSuiteFailed != null) {
      throw new RuntimeException("beforeTestSuite() failed on a previous test", _beforeTestSuiteFailed);
    }

    if (!_beforeTestSuiteRun) {
      _beforeTestSuiteRun = true;
      _environment.beforeTestSuite();
    }
  }

  /**
   * A convenience method for running this suite from a main method.  Subclasses of
   * Suite can create a main method like so:
   * <pre>
   *   public static void main(String[] args) {
   *     System.exit( new GosuSuite().runSuite() ? 0 : 1 );
   *   }
   * </pre>
   *
   * @return a boolean saying if all tests passed
   */
  public final boolean runViaStaticSuiteMethod() {
    IType iType = TypeSystem.getTypeFromObject(this);
    IMethodInfo method = iType.getTypeInfo().getMethod("suite");
    Test test = (Test) method.getCallHandler().handleCall(null);
    return runImpl(test).wasSuccessful();
  }

  private Result runImpl(Test test) {
    JUnitCore runner = new JUnitCore();
    TextListener txtListener = new TextListener(System.out);
    runner.addListener(txtListener);
    return runner.run(test);
  }


  private void maybeCallBeforeTestClass(TestClass testClass) {
    if (_beforeTestClassFailed.containsKey(testClass.getTypeName())) {
      throw new RuntimeException("beforeTestClass() failed on a previous test of test class " + testClass.getTypeName(), _beforeTestClassFailed.get(testClass.getTypeName()));
    }
    TestInfo testInfo = _testInfos.get(testClass.getTypeName());

    if (testInfo == null) {
      Integer testsCount = TestClass.getNumberOfInstancesOfTestClassCreated(testClass.getTypeName());
      testInfo = new TestInfo(testsCount);
      _testInfos.put(testClass.getTypeName(), testInfo);
    }

    if (testInfo._testsRun == 0) {
      _testClassStartTime = System.nanoTime();
      _environment.beforeTestClass();
      testClass.beforeTestClass();
    }
    testInfo._testsRun++;
  }

  private void maybeCallAfterTestClass(TestClass testClass) {
    TestInfo testInfo = _testInfos.get(testClass.getTypeName());
    if (testInfo != null && testInfo.isAtLastTest()) {
      testClass.afterTestClass();
      _environment.afterTestClass();
      printTestRunTime("Class " + testClass.getTypeName(), (System.nanoTime() - _testClassStartTime));
    }
  }

  private void maybeCallAfterTestSuite(TestClass test) {
    TestInfo testInfo = _testInfos.get(test.getTypeName());
    if (testInfo != null
            && testInfo.isAtLastTest()
            && isLastTestInSuite(test)) {
      _environment.afterTestSuite();
      printTestRunTime("Suite", (System.nanoTime() - _suiteStartTimeNs));
    }
  }

  private void printTestRunTime(String msg, long nanoTime) {
    if (INCLUDE_TEST_TIMING_INFO) {
      System.out.println("***** TestRunTime [" + msg + "] " + nanoTime + " *****");
    }
  }

  private boolean isLastTestInSuite(TestClass test) {
    boolean isLastTestInSuite = false;

    if (_testWrappers == null) {
      isLastTestInSuite = true;
    } else if (_testWrappers.size() > 0) {
      final String testTypeName = test.getTypeName();
      final String testClassName = test.getClass().getName();
      final String testWrapperName = _testWrappers.get(_testWrappers.size() - 1).getName();
      isLastTestInSuite = testTypeName.equals(testWrapperName) || testClassName.equals(testWrapperName);
    }

    return isLastTestInSuite;
  }

  public final boolean hasTimeOut() {
    return _suiteTimeoutInMillis != 0L;
  }

  public final long getTimeoutForCurrentTest() {
    long timeSoFar = System.currentTimeMillis() - _suiteStartTime;
    return Math.max(1L, _suiteTimeoutInMillis - timeSoFar);
  }

  final void markTimedOut() {
    _suiteHasTimedOut = true;
  }

  public final boolean hasTimedOut() {
    return _suiteHasTimedOut;
  }

  public static class TestInfo {

    public int _testsRun;
    private int _testCount;

    public TestInfo(int testCount) {
      _testCount = testCount;
    }

    public boolean isAtLastTest() {
      return _testsRun == _testCount;
    }
  }

  // In order for the IDE to be happy about how results are reported when running a single class remotely, we need
  // to report the results on the original TestClass object, even though we really want to be executing all the methods
  // remotely.  To that end, we extend the normal RemoteTestClass so that it overrides the reallyRun method and simulates
  // what TestResult.run() does
  private static class RemoteTestClassIDEExecutionWrapper extends RemoteTestClass {

    private TestClass _wrapped;

    private RemoteTestClassIDEExecutionWrapper(String typeName, String methodName, int totalNumMethods, TestExecutionManager executionManager, TestClass wrapped) {
      super(typeName, methodName, totalNumMethods, executionManager);
      _wrapped = wrapped;
    }

    void reallyRun(TestResult result) {
      result.startTest(_wrapped);
      try {
        runBare();
      } catch (AssertionFailedError e) {
        result.addFailure(_wrapped, e);
      } catch (ThreadDeath e) { // don't catch ThreadDeath by accident
        throw e;
      } catch (Throwable e) {
        result.addError(_wrapped, e);
      }
      result.endTest(_wrapped);
    }


  }
}
