/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.reflect.java.JavaTypes;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.framework.AssertionFailedError;
import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.IGosuClass;
import gw.testharness.IncludeInTestResults;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.Set;
import java.util.HashSet;
import java.lang.annotation.Annotation;

public class TestClassWrapper extends TestSuite {
  private IType _type;
  private TestExecutionManager _executionManager;

  public TestClassWrapper(TestExecutionManager executionManager, IType type, String... methods) {
    _executionManager = executionManager;
    _type = type;
    for (String method : methods) {
      addTest(makeTest(_type, method));
    }
  }

  @Override
  public void runTest(Test test, TestResult result) {
    if (_executionManager.hasTimedOut()) {
      result.addFailure(test, new AssertionFailedError(String.format("tests timed out")));
    } else if (_executionManager.hasTimeOut()) {
      long timeout = _executionManager.getTimeoutForCurrentTest();
      runWithTimeout(test, result, timeout);
    } else {
      super.runTest(test, result);
    }
  }

  private void runWithTimeout(final Test test, final TestResult result, long timeout) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    Callable<Object> callable = new Callable<Object>() {
      public Object call() throws Exception {
        test.run(result);
        return null;
      }
    };
    Future<Object> future = service.submit(callable);
    service.shutdown();
    try {
      boolean terminated = service.awaitTermination(timeout, TimeUnit.MILLISECONDS);
      if (!terminated){
        service.shutdownNow();
      }
      future.get(0, TimeUnit.MILLISECONDS); // throws the exception if one occurred during the invocation
    } catch (TimeoutException e) {
      result.addFailure(test, new AssertionFailedError(String.format("test timed out after %d milliseconds", timeout)));
      result.endTest(test);
      _executionManager.markTimedOut();
    } catch (Throwable e) {
      if (e instanceof AssertionFailedError) {
        result.addFailure(test, (AssertionFailedError) e);
      } else {
        result.addFailure(test, new AssertionFailedError(e.getMessage()));
      }
    }
  }


  @Override
  public String getName() {
    return _type.getName();
  }

  private TestCase makeTest(final IType type, String method) {
    try {
      TestClass test;
      if (type.isValid()) {
        ITypeInfo typeInfo = type.getTypeInfo();
        IConstructorInfo noArgCons = typeInfo.getConstructor();
        if (noArgCons != null) {
          test = (TestClass) noArgCons.getConstructor().newInstance();
        } else {
          IConstructorInfo oneArgCons = typeInfo.getConstructor(JavaTypes.STRING());
          if (oneArgCons != null) {
            test = (TestClass) oneArgCons.getConstructor().newInstance(method);
          } else {
            throw new IllegalStateException("Test type " + type + " does not have either a no-arg constructor or a one-arg constructor taking a String");
          }
        }
      } else {
        test = new InvalidTestClass(_type);
      }
      test.setExecutionManager( _executionManager );
      test.setName(method);
      test.setGosuTest(_type instanceof IGosuClass);
      test.initMetadata( method );
      return test;
    } catch (final Exception e) {
      e.printStackTrace();
      return new ExceptionTestClass(type, e.getMessage());
    }
  }

//  static private Set<TestMetadata> getTestMethodMetadata(TestClass test, String method) {
//    Set<TestMetadata> set = new HashSet<TestMetadata>();
//    IMethodInfo testMethod = test.getType().getTypeInfo().getMethod(method);
//    if(testMethod == null) {
//      throw new IllegalStateException( "Method not found: " + test.getName() + "." + method);
//    }
//    for (IAnnotationInfo ai : testMethod.getAnnotations()) {
//      if (isMetaAnnotationInfo(ai)) {
//        set.add(new TestMetadata((Annotation) ai.getInstance()));
//      }
//    }
//    return set;
//  }

//  static private Set<TestMetadata> getTestClassMetadata(TestClass test) {
//    Set<TestMetadata> set = new HashSet<TestMetadata>();
//    for (IAnnotationInfo ai : test.getType().getTypeInfo().getAnnotations()) {
//      if (isMetaAnnotationInfo(ai)) {
//        set.add(new TestMetadata((Annotation) ai.getInstance()));
//      }
//    }
//    return set;
//  }

  private static boolean isMetaAnnotationInfo(IAnnotationInfo ai) {
    boolean isMetadata = false;
    for (IAnnotationInfo a : ai.getType().getTypeInfo().getAnnotations()) {
      if (a.getName().equals(IncludeInTestResults.class.getName())) {
        isMetadata = true;
        break;
      }
    }
    return isMetadata;
  }

  public IType getBackingType() {
    return _type;
  }

  private static class InvalidTestClass extends TestClass {
    private IType _type;

    private InvalidTestClass(IType type) {
      super(false);
      _type = type;
      initInternalData();
    }

    @Override
    public void run(TestResult result) {
      result.addError(this, getCompileError(_type));
    }

    @Override
    public IType getType() {
      return _type;
    }

    private Throwable getCompileError(IType type) {
      if (type instanceof IGosuClass) {
        type.isValid(); // just in case there has been a typesystem refresh, to ensure there is a PRE
        return ((IGosuClass)type).getParseResultsException();
      } else {
        return new IllegalStateException("Test type " + type + " is not valid.");
      }
    }

    @Override
    protected String getFullClassNameInternal() {
      return _type.getName();
    }
  };

  private static class ExceptionTestClass extends TestClass {
    private IType _type;
    private String _message;

    private ExceptionTestClass(IType type, String message) {
      super(false);
      _type = type;
      _message = message;
      initInternalData();
    }

    @Override
    public void run(TestResult result) {
      result.addError(this, new RuntimeException("Could not construct test " + _type.getName() + ". Reason : " + _message) );
    }

    @Override
    public IType getType() {
      return _type;
    }

    @Override
    protected String getFullClassNameInternal() {
      return _type.getName();
    }
  };
}
