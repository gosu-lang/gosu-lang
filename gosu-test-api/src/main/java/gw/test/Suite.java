/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.test.remote.ForwardingTestEnvironment;
import gw.fs.IDirectory;
import gw.test.remote.RemoteTestClassWrapper;
import gw.util.ILogger;
import gw.util.Predicate;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * This Suite class streamlines and simplifies the standard JUnit TestSuite.  It can be used in one of two ways:
 * you can add individual tests to the Suite via the withTest() methods, or you can use the filter methods to
 * pare down the tests that are found on the current classpath.
 */
public class Suite<T extends Suite> extends junit.framework.TestSuite {

  //suite status
  private boolean _testClassWrappersCreated;
  private boolean _suiteHasBeenSetUp;

  //filters
  private final List<Predicate<IType>> _typeFilters = new ArrayList<Predicate<IType>>();
  private final List<Predicate<IFile>> _iFileFilters = new ArrayList<Predicate<IFile>>();
  private final List<Predicate<String>> _packageFilters = new ArrayList<Predicate<String>>();
  private final List<String> _withPackages = new ArrayList<String>();

  //suite test data
  private final TreeSet<TestSpec> _testSpecs = new TreeSet<TestSpec>();
  private final List<IDirectory> _gosuClassSearchPath = new ArrayList<IDirectory>();
  private final List<IDirectory> _javaClassSearchPath = new ArrayList<IDirectory>();
  private final List<String> _modules = new ArrayList<String>();
  private boolean _logErrorInfo = false;

  private int _splitNum = 1;
  private int _splitPartition = 0;

  private TestExecutionManager _executionManager;
  private TestEnvironment _testEnvironment;
  public static final String GOSU_SUITE_INCLUDE_TYPES = "gs.suite.tests";
  public static final String SPLIT_PARTITION = "split.partition";

  public Suite() {
    Integer splitPartition = readIntegerSystemProperty(SPLIT_PARTITION);
    if(splitPartition != null && splitPartition > 1){
      throw new UnsupportedOperationException("V3 tests doesn't support more than one split");
    }

    // By default, use a standard TestEnvironment to set things up, in case the specific suite class doesn't
    // bother to set one up
    _testEnvironment = new TestEnvironment();
    _executionManager = new TestExecutionManager();
    _executionManager.setEnvironment(_testEnvironment);
  }

  public TestEnvironment getTestEnvironment() {
    return _testEnvironment;
  }

  //=============================================================================================
  // Suite builder support
  //=============================================================================================

  /**
   * Used to indicate that this suite does not require assertions be enabled.  In general
   * it is recommended that you not use this option.
   */
  public final T javaAssertionsNotEnabled() {
    _executionManager.setAssertionsMustBeEnabled(false);
    return thisAsT();
  }

  /**
   * Adds a test to this Suite.  This method is not to be combined with the module/filter methods.
   */
  public final T withTest(IType type, String... methods) {
    verifyNoFilters();
    _testSpecs.add(new TestSpec(type.getName(), methods));
    return thisAsT();
  }

  /**
   * Adds a test to this Suite.  This method is not to be combined with the module/filter methods.
   */
  public final T withTest(String type, String... methods) {
    verifyNoFilters();
    _testSpecs.add(new TestSpec(type, methods));
    return thisAsT();
  }

  /**
   * Adds a test to this Suite.  This method is not to be combined with the module/filter methods.
   */
  public final T withTest(Class type, String... methods) {
    verifyNoFilters();
    _testSpecs.add(new TestSpec(type.getName(), methods));
    return thisAsT();
  }

  private void verifyNoFilters() {
    if (!_packageFilters.isEmpty() || !_iFileFilters.isEmpty() ||
            !_typeFilters.isEmpty() || !_withPackages.isEmpty()) {
      throw new IllegalStateException("You cannot combine filters with explicitly specified tests");
    }
  }

  /**
   * Adds a type filter to this suite, allowing certain tests to be excluded.  Note
   * that this is a significantly slower option than {@link #withIFileFilter(gw.util.Predicate)}
   * or {@link #withPackageFilter(gw.util.Predicate)} If
   * possible, it is advisable to use a file filter instead.
   */
  public final T withTestTypeFilter(Predicate<IType> filter) {
    if (!_testSpecs.isEmpty()) {
      throw new IllegalStateException("You should add a test filter before any tests are added to the suite.");
    }
    _typeFilters.add(filter);
    return thisAsT();
  }

  /**
   * Adds a file filter to this suite, allowing certain tests to be excluded.  Consider
   * using {@link #withPackageFilter(gw.util.Predicate)} if you wish to more easily apply a package
   * filter of tests without dealing with ugly file paths.  Package filters are just as fast as
   * file filters, both of which are much faster than test type filters.
   */
  public final T withIFileFilter(Predicate<IFile> fileFilter) {
    ensureNoExplictlyAddedTests();
    _iFileFilters.add(fileFilter);
    return thisAsT();
  }

  /**
   * Adds a package filter that will only accept packages that are subpackages of the strings
   * passed in.
   */
  public final T withPackages(final String... packagePrefixes) {
    ensureNoExplictlyAddedTests();
    _withPackages.addAll(Arrays.asList(packagePrefixes));
    return thisAsT();
  }

  private void ensureNoExplictlyAddedTests() {
    if (!_testSpecs.isEmpty()) {
      throw new IllegalStateException("You cannot add filters to suites with explicitly added tests");
    }
  }

  /**
   * Adds a package filter to this suite, which allows you to exclude certain packages from consideration.
   * These filters are about as fast as file filters, but are easier to write and maintain.  For the
   * common case of simply wishing to test certain packages and their sub-packages, use {@link #withPackages(String...)} instead.
   */
  public final T withPackageFilter(Predicate<String> fileFilter) {
    ensureNoExplictlyAddedTests();
    _packageFilters.add(fileFilter);
    return thisAsT();
  }

  public final T withTimeout(long seconds) {
    _executionManager.setSuiteTimeoutInMillis(seconds * 1000L);
    return thisAsT();
  }

  public T withTestEnvironment(TestEnvironment testEnvironment) {
    if (_testEnvironment.isRemoteExecutionEnvironment()) {
      throw new IllegalStateException("For now, withTestEnvironment must be called BEFORE withRemoteServer is called");
    }
    _testEnvironment = testEnvironment;
    _executionManager.setEnvironment(_testEnvironment);
    return thisAsT();
  }

  public final T logErrors() {
    _logErrorInfo = true;
    return thisAsT();
  }

  /**
   * This limits the gosu tests that are run to gosu tests that are located
   * within the specified module in a gsrc or gtest directory AND all and java
   * tests in the classpath that are not in a module.
   * @param moduleNames
   * @return
   */
  public final T withModules(String... moduleNames) {
    _modules.addAll(Arrays.asList(moduleNames));
    return thisAsT();
  }

  // TODO - AHK - Differentiate between Gosu and Java at this level?
  // TODO - AHK - Change this to just take an IDirectory instead?
  public T withClasspathEntry(File srcDir) {
    _gosuClassSearchPath.add(CommonServices.getFileSystem().getIDirectory(srcDir));
    _javaClassSearchPath.add(CommonServices.getFileSystem().getIDirectory(srcDir));
    return thisAsT();
  }

  //=============================================================================================
  // Implementation details
  //=============================================================================================
  @Override
  public final int countTestCases() {
    maybeCreateTestClassWrappers();
    return _testSpecs.size();
  }

  @Override
  public final Test testAt(int index) {
    maybeCreateTestClassWrappers();
    return super.testAt(index);
  }

  @Override
  public final int testCount() {
    maybeCreateTestClassWrappers();
    return super.testCount();
  }

  @Override
  public final Enumeration<Test> tests() {
    maybeCreateTestClassWrappers();
    return super.tests();
  }

  private void maybeCreateTestClassWrappers() {

    maybeInitSuite();

    if (!_testClassWrappersCreated) {
      long start = System.currentTimeMillis();

      if (_testSpecs.isEmpty()) {
        TestClassFinder finder = new TestClassFinder(_iFileFilters, _packageFilters, _withPackages, _typeFilters);
        _testSpecs.addAll(finder.findTests(_gosuClassSearchPath, _javaClassSearchPath));
      }

      restrictTestSpecsToSpecifiedNames();
      
      for (TestSpec spec : _testSpecs) {
        if (_testEnvironment.isRemoteExecutionEnvironment()) {
          String[] methods = spec.runAllMethods() ? null : spec.getMethods();
          RemoteTestClassWrapper remoteWrapper = new RemoteTestClassWrapper(_executionManager, spec.getTestTypeName(), methods);
          addTest(remoteWrapper);
        } else {
          IType type = spec.getTestType();
          addTest(new TestClassWrapper(_executionManager, type, spec.getMethods()));
        }
      }
      _testClassWrappersCreated = true;
      long end = System.currentTimeMillis();
      System.out.println("Test wrappers created in " + (end - start) + "ms");

      if (_testEnvironment.isDynamicallyDeterminedEnvironment()) {
        _testEnvironment = determineTestEnvironmentBasedOnTestDefaults();
        System.out.println("Dynamically determined the test environment to be " + _testEnvironment.getClass());
        // This is pretty hacky, but RemoteTestEnvironments need a chance to tell the remote server
        // what sort of environment to set up, and that actually needs to be done prior to beginning the suite
        if (_testEnvironment instanceof ForwardingTestEnvironment) {
          _testEnvironment.initializeTypeSystem();
        }
        _executionManager.setEnvironment(_testEnvironment);
      }
    }
  }

  private TestEnvironment determineTestEnvironmentBasedOnTestDefaults() {
    TestEnvironment defaultEnv = null;
    TestClass defaultEnvTest = null;

    for (Test t : testsAsList()) {
      if (t instanceof TestClassWrapper) {
        if (((TestClassWrapper) t).testCount() > 0) {
          Test firstTest = ((TestClassWrapper) t).tests().nextElement();
          if (firstTest instanceof TestClass) {
            TestEnvironment env = ((TestClass) firstTest).createDefaultEnvironment();
            if (defaultEnv == null) {
              defaultEnv = env;
              defaultEnvTest = (TestClass) firstTest;
            } else {
              if (!defaultEnv.getClass().equals(env.getClass())) {
                throw new IllegalStateException("The test " + defaultEnvTest.getClassName() + " requires the " +
                        defaultEnv.getClass() + " environment, while the " + ((TestClass) firstTest).getClassName() +
                        " test requires the " + env.getClass() + " environment.  Automatic resolution of the appropriate " +
                        "test environment will only work if all tests in the suite use the exact same default test environment.");
              }
            }
          } else {
            throw new IllegalStateException("Found a test that was a " + firstTest.getClass() + " instead of a TestClass");
          }
        } else {
          throw new IllegalStateException("Found a test class " + ((TestClassWrapper) t).getBackingType().getName() + " that has no tests.");
        }
      } else {
        throw new IllegalStateException("Found a test that was a " + t.getClass() + " instead of a TestClassWrapper");
      }
    }

    return defaultEnv;
  }

  private void maybeInitSuite() {
    if (!_suiteHasBeenSetUp) {

      initTypeSystem();
      if (_gosuClassSearchPath.isEmpty() && _javaClassSearchPath.isEmpty() && _testSpecs.isEmpty()) {
        _gosuClassSearchPath.addAll(createDefaultGosuClassSearchPath());
        _javaClassSearchPath.addAll(createDefaultJavaClassSearchPath());
      }

      _suiteHasBeenSetUp = true;
    }
  }

  protected List<IDirectory> createDefaultGosuClassSearchPath() {
    List<IDirectory> gosuClassSearchPath = new ArrayList<IDirectory>();
    List<? extends IDirectory> sourceEntries = TypeSystem.getCurrentModule().getSourcePath();
    for (IDirectory dir : sourceEntries) {
      if (_modules.isEmpty()) {
        gosuClassSearchPath.add(dir);
      } else {
        // TODO - AHK - This seems rather unnecessary:  we should have a way to already know what the module root is
        IDirectory moduleRoot = ClasspathToGosuPathEntryUtil.findModuleRootFromSourceEntry(dir);
        if (_modules.contains(moduleRoot.getName())) {
          gosuClassSearchPath.add(dir);
        }
      }
    }

    return gosuClassSearchPath;
  }

  protected List<IDirectory> createDefaultJavaClassSearchPath() {
    List<IDirectory> javaClassSearchPath = new ArrayList<IDirectory>();
    List<File> classPathParts = ClassPathUtil.constructClasspathFromSystemClasspath();

    for (File pathElement : classPathParts) {
      javaClassSearchPath.add(CommonServices.getFileSystem().getIDirectory(pathElement));
    }

    return javaClassSearchPath;
  }

  private void initTypeSystem() {
    _executionManager.maybeInitTypeSystem();
  }

  public void logError(String o) {
    if( _logErrorInfo) {
      ILogger logger = CommonServices.getEntityAccess().getLogger();
      logger.warn(o);
    }
  }

  @Override
  public void run(TestResult result) {
    maybeCreateTestClassWrappers();
    _executionManager.setTestsFromSuite(testsAsListOfSuites());
    beforeSuite();
    super.run(result);
  }

  protected void beforeSuite() {
    // hook point for subclasses
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

  /**
   * Runs this Suite with a TextListener tied to System.out and returns the result.
   */
  public final Result run() {
    return runImpl(this);
  }

  public List<Test> testsAsList() {
    Enumeration<Test> testEnumeration = tests();
    ArrayList<Test> testArrayList = new ArrayList<Test>();
    while (testEnumeration.hasMoreElements()) {
      Test test = testEnumeration.nextElement();
      testArrayList.add(test);
    }
    return testArrayList;
  }

  public List<TestSuite> testsAsListOfSuites() {
    Enumeration<Test> testEnumeration = tests();
    List<TestSuite> testArrayList = new ArrayList<TestSuite>();
    while (testEnumeration.hasMoreElements()) {
      Test test = testEnumeration.nextElement();
      testArrayList.add((TestSuite) test);
    }
    return testArrayList;
  }

  public boolean isLoggingErrors() {
    return _logErrorInfo;
  }

  /**
   * Private method that casts the this value as a T to avoid unchecked cast errors all over the place
   *
   * @return this
   */
  @SuppressWarnings({"unchecked"})
  private T thisAsT() {
    return (T) this;
  }

  // TODO - AHK - Maybe kill everything around partitioning and splitting in Suite, or at least move it to PLSuite


  private Integer readIntegerSystemProperty(String name) {
    String splitString = System.getProperty(name);
    if (splitString != null) {
      try {
        return Integer.valueOf(splitString);
      } catch (NumberFormatException e) {
        System.err.println("Invalid System Property value for '" + name + "': \"" + splitString + "\". " + e.getMessage());
      }
    }

    return null;
  }

  private void restrictTestSpecsToSpecifiedPartition() {
    int splitStart = 0;
    int splitEnd = _testSpecs.size();

    if(_splitNum > 1) {
      float splitSize = (float)_testSpecs.size() / (float)_splitNum;
      splitStart = Math.round(splitSize * _splitPartition);
      if(_splitPartition != _splitNum - 1) {
        splitEnd = Math.round(splitSize * (_splitPartition + 1)) - 1;
      }
    }
    // PL-13675: To play nice with TH
    System.out.println("Number of classes = " + _testSpecs.size());
    System.out.println("Number of splits  = " + (_splitNum <= 0 ? 1: _splitNum));
    int i = 0;
    for (Iterator<TestSpec> it = _testSpecs.iterator(); it.hasNext();) {
      TestSpec spec = it.next();
      if (i < splitStart || i >= splitEnd) {
        it.remove();
      }
      i++;
    }
  }

  // TODO - AHK - This should be killed and re-implemented as a test type filter in PLSuite

  /**
   * This is for troubleshooting suite failures by running a subset of tests within a suite
   * without requiring any Java or Gosu code changes
   */
  private void restrictTestSpecsToSpecifiedNames() {
    String includedTests = System.getProperty(GOSU_SUITE_INCLUDE_TYPES);
    if(includedTests != null){
      System.out.println("System property " + GOSU_SUITE_INCLUDE_TYPES + " used, so only running tests specified:");
      System.out.println(includedTests);
      String[] includedTestsArray = includedTests.replace(" ", "").split(",");
      HashSet<String> includedTestSet = new HashSet<String>(Arrays.asList(includedTestsArray));
      for (Iterator<TestSpec> it = _testSpecs.iterator(); it.hasNext();) {
        TestSpec spec = it.next();
        if(!includedTestSet.contains(spec.getTestType().getName())){
          it.remove();
        }
      }
    }
  }
}
