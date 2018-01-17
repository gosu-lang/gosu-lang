/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.GosuShop;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.testharness.IncludeInTestResults;
import gw.testharness.KnownBreak;
import gw.testharness.KnownBreakQualifier;
import gw.util.GosuExceptionUtil;
import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;
import gw.util.Predicate;
import junit.framework.TestCase;
import junit.framework.TestResult;

import java.lang.annotation.Annotation;
import gw.util.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TestClass extends TestCase implements ITestWithMetadata {
  private String _pkgName;
  private String _className;
  private TestExecutionManager _executionManager;
  private static final ThreadLocal<TestExecutionManager> THREAD_LOCAL_EXECUTION_MANAGER = new ThreadLocal<TestExecutionManager>();
  private List<TestMetadata> _metadata = new ArrayList<TestMetadata>();
  private boolean _doNotRun;
  private boolean _knownBreak;
  private static final Map<String, Integer> _numberOfInstancesCreatedByTypeName = new HashMap<String, Integer>();
  private boolean _isGosuTest;

  protected TestClass() {
    super();
    initInternalData();
  }

  protected TestClass(String s) {
    super(s);
    initInternalData();
  }

  // This is a bit hacky:  some subclasses might need to delay the call
  // to the initInternalData() method until after the class performed some additional work
  protected TestClass(boolean shouldInit) {
    if (shouldInit) {
      initInternalData();
    }
  }

  // This is a bit hacky:  some subclasses might need to delay the call
  // to the init() method until after the class performed some additional work
  protected TestClass(String s, boolean shouldInit) {
    super(s);
    if (shouldInit) {
      initInternalData();
    }
  }

  public boolean isGosuTest() {
    return _isGosuTest;
  }

  public void setGosuTest(boolean gosuTest) {
    _isGosuTest = gosuTest;
  }

  protected void initInternalData() {
    String fullName = getFullClassNameInternal();
    int lastDot = fullName.lastIndexOf(".");
    _pkgName = fullName.substring(0, lastDot).replace("_proxy_.", "");
    _className = fullName.substring(lastDot + 1, fullName.length()).replace('$', '.');

    // When running from an IDE, we could be running just one method out of a class, or we could be running
    // the entire class.  Since we have no way to get ahold of the suite that the IDE has created, the only
    // way for us to tell how many methods we're running out of a given class is to track how many instances
    // of each class are created.  We can then use that to determine when to run the afterClass() hook.
    Integer numberOfInstances = _numberOfInstancesCreatedByTypeName.get(getTypeName());
    if (numberOfInstances == null) {
      _numberOfInstancesCreatedByTypeName.put(getTypeName(), 1);
    } else {
      _numberOfInstancesCreatedByTypeName.put(getTypeName(), numberOfInstances + 1);
    }
  }

  protected String getFullClassNameInternal() {
    return getClass().getName();
  }

  public static Integer getNumberOfInstancesOfTestClassCreated(String typeName) {
    return _numberOfInstancesCreatedByTypeName.get(typeName);
  }

  @Override
  protected final void setUp() throws Exception {
    super.setUp();
    if(!getType().getName().endsWith("Test")) {
      throw new IllegalStateException("All subclasses of TestClass must have a name that ends with \"Test\"");
    }
    if ( _executionManager == null || _executionManager.assertionsMustBeEnabled()) {
      try {
        assert false;
        throw new IllegalStateException("Assertions must be enabled for tests to be run properly.");
      } catch (AssertionError ae) {
        //ignore
      }
    }
  }

  public void setExecutionManager(TestExecutionManager executionManager) {
    _executionManager = executionManager;
  }

  @Override
  protected final void tearDown() throws Exception {
    super.tearDown();
  }

  public void beforeTestClass(){
  }

  public void beforeTestMethod(){
  }

  public void afterTestMethod(Throwable possibleException){
  }

  public void afterTestClass(){
  }

  @Override
  public void run(TestResult result) {
    getExecutionManager().runTestClass(this, result);
  }

  public void reallyRun(TestResult result) {
    super.run(result);
  }
  
  @Override
  public void runBare() throws Throwable {
    getExecutionManager().runTestClassBare(this);
  }

  public void reallyRunBare() throws Throwable {
    initMetadata(getName());
    super.runBare();
  }

  @Override
  public String toString() {
    return this.getName() + "(" + getTypeName() + ")";
  }

  @Override
  public void setName(String name) {
    super.setName(name);
  }

  @Override
  public String getName() {
    return super.getName();
  }

  protected TestExecutionManager getExecutionManager() {
    if (_executionManager == null) {
      return getThreadLocalExecutionManager();
    } else {
      return _executionManager;
    }
  }

  //Provides a simple thread local execution manager for all the tests being run
  //presumably from an IDE environment
  private TestExecutionManager getThreadLocalExecutionManager() {
    TestExecutionManager executionManager = THREAD_LOCAL_EXECUTION_MANAGER.get();
    if (executionManager == null) {
      executionManager = new TestExecutionManager();
      executionManager.setEnvironment(createDefaultEnvironment());
      THREAD_LOCAL_EXECUTION_MANAGER.set(executionManager);
      // TODO - AHK - Set up the default classpath?
    }
    return executionManager;
  }

  public TestEnvironment createDefaultEnvironment() {
    return new TestEnvironment();
  }

  @Override
  protected final void runTest() throws Throwable {
    // TODO - AHK - More properly log this, rather than using System.out
    if (_knownBreak) {
      System.out.println("**** Test method " + getName() + " is marked as a known break.  Run tests with -Dgw.tests.skip.knownbreak=true to skip known breaks.");
    }

    if (_doNotRun) {
      System.out.println("*** Skipping test method " + getName() + ", as it's marked @Disabled, @ManualTest, or @InProgress");
    } else if (_knownBreak && skipKnownBreakTests()) {
      System.out.println("*** Skipping test method " + getName() + ", as it's marked @KnownBreak and the gw.tests.skip.knownbreak system parameter is set to true.");
    } else {
      doRunTest( getName() );
    }
  }

  private static Boolean _skipKnownBreakTests = null;
  private static boolean skipKnownBreakTests() {
    // And no, I don't really care about thread-safety here
    if (_skipKnownBreakTests == null) {
      String propValue = System.getProperty("gw.tests.skip.knownbreak");
      if (propValue != null) {
        _skipKnownBreakTests = Boolean.valueOf(propValue);
      } else {
        _skipKnownBreakTests = false;
      }
    }

    return _skipKnownBreakTests;
  }

  protected void doRunTest( String name ) throws Throwable
  {
    IType type = getType();
    Method runMethod;

    if( type instanceof IJavaType && ((IHasJavaClass) getType()).getBackingClass() == null ) {
      // Handle case where we are getting IJavaClassInfo from source (getBackingClass() returns null)
      ClassLoader cl = type.getTypeLoader().getModule().getModuleTypeLoader().getDefaultTypeLoader().getGosuClassLoader().getActualLoader();
      Class<?> testClass = Class.forName( type.getName(), true, cl );
      runMethod = testClass.getMethod( name );
    }
    else {
      runMethod = ((IHasJavaClass) getType()).getBackingClass().getMethod( name );
    }
    if (runMethod == null) {
      fail("Method \"" + name + "\" not found");
    }

    if (!Modifier.isPublic(runMethod.getModifiers())) {
      fail("Method \"" + name + "\" should be public");
    }

    try
    {
      runMethod.invoke(this);
    }
    catch( InvocationTargetException e )
    {
      throw GosuExceptionUtil.forceThrow( e.getTargetException() );
    }

//    IMethodInfo runMethod = null;
//    runMethod = getType().getTypeInfo().getMethod( name );
//    if (runMethod == null) {
//      fail("Method \"" + name + "\" not found");
//    }
//    if (!runMethod.isPublic()) {
//      fail("Method \"" + name + "\" should be public");
//    }
//    runMethod.getCallHandler().handleCall(this);
  }

  public IType getType() {
    return TypeSystem.getFromObject(this);
  }

  public String getTypeName() {
    return _pkgName + "." + _className;
  }

  //================================================================
  // Utility Methods
  //================================================================

  public String getClassName() {
    return _className;
  }

  public String getPackageName() {
    return _pkgName;
  }

  //================================================================
  // Assertion extensions
  //================================================================

  public interface EqualityTester {
    boolean equals(Object expected, Object got);
  }

  public static void assertArrayEquals(Object[] expected, Object[] got) {
    assertArrayEquals(expected, got, new EqualityTester() {
      @Override
      public boolean equals(Object expected, Object got) {
        if (expected != null && got != null && expected.getClass().isArray() && got.getClass().isArray() && Array.getLength(expected) == Array.getLength(got)) {
          int length = Array.getLength(expected);
          for (int i = 0; i < length; i++) {
            if (!equals(Array.get(expected, i), Array.get(got, i))) {
              return false;
            }
          }
          return true;
        }
        return GosuObjectUtil.equals(expected, got);
      }
    });
  }

  /**
   * Compare two byte arrays, first the size then each byte.
   * @param expected
   * @param actual
   */
  public static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
    if (expected.length != actual.length) {
      fail(message+" - expected array length of "+expected.length+" but got "+actual.length);
    for (int i=0; i<expected.length; i++) {
      assertEquals(message, expected[i], actual[i]);
      }
    }
  }


  /**
   * Verifies that all elements in the first array are present in the second
   * array and match the elements in the first array. Uses EqualityUtil to
   * determine equality and is order-insensitive.
   *
   * @param expected the expected result (reference)
   * @param got      the obtained result (what to compare against the reference)
   */
  public static void assertArrayEquals(Object[] expected, Object[] got, EqualityTester tester) {
    if (expected == null) {
      if (got == null) {
        return;
      } else {
        fail("Expected null, got non-null");
      }
    } else {
      if (got == null) {
        fail("Expected non-null, got null");
      }
    }
    boolean[] expectedFound = makeFoundArray(expected.length);
    boolean[] gotFound = makeFoundArray(got.length);
    for (int i = 0; i < expected.length; i++) {
      Object expectedObject = expected[i];
      for (int j = 0; j < got.length; j++) {
        if (tester.equals(expectedObject, got[j]) && !gotFound[j]) {
          expectedFound[i] = true;
          gotFound[j] = true;
          break;
        }
      }
    }
    if (!allTrue(expectedFound) || !allTrue(gotFound)) {
      StringBuffer sb = new StringBuffer();
      sb.append("\nExpected:\n");
      appendFoundStatus(sb, expected, expectedFound);
      sb.append("\nGot:\n");
      appendFoundStatus(sb, got, gotFound);
      fail(sb.toString());
    }
  }

  private static void appendFoundStatus(StringBuffer /*INOUT*/ sb, Object[] expected, boolean[] expectedFound) {
    sb.append("[\n");
    for (int i = 0; i < expected.length; i++) {
      Object o = expected[i];
      sb.append(expectedFound[i] ? "  " : "! ");  // "! " means we didn't find it
      sb.append(o);
      sb.append("\n");
    }
    sb.append("]\n");
  }

  private static boolean[] makeFoundArray(int length) {
    boolean[] found = new boolean[length];
    for (int i = 0; i < found.length; i++) {
      found[i] = false;
    }
    return found;
  }

  private static boolean allTrue(boolean[] booleans) {
    for (boolean b : booleans) {
      if (!b) {
        return false;
      }
    }
    return true;
  }

  // TODO - AHK - This should be using the other variant
  public static void assertArrayEquals(String message, Object[] o1, Object[] o2) {
    boolean equals = false;
    if(o1.length == o2.length){
      equals = true;
      for (int i = 0; i < o1.length; i++) {
        if(!GosuObjectUtil.equals(o1[i], o2[i]) ){
          equals = false;
          break;
        }
      }
    }
    assertTrue(message + " Arrays were not equal. Expected \n[" + GosuStringUtil.join(o1, ",") + "] but found \n[" + GosuStringUtil.join(o2, ",") + "]",  equals);
  }

  public static void assertSetsEqual(Set o1, Set o2)
  {
    boolean equals = GosuObjectUtil.equals( o1, o2 );
    assertTrue( "Sets were not equal.  Expected \n[" + GosuStringUtil.join( o1, "," ) + "] but found \n[" + GosuStringUtil.join( o2, "," ) + "]", equals );
  }

  public static void assertCollectionEquals(Collection o1, Collection o2) {
    assertIterableEqualsIgnoreOrder(o1, o2);
  }

  public static void assertListEquals(List o1, List o2) {
    assertIterableEquals(o1, o2, "Lists");
  }

  public static void assertIterableEquals(Iterable o1, Iterable o2) {
    assertIterableEquals(makeList(o1), makeList(o2), "Iterables");
  }

  public static void assertCollectionEquals(Collection o1, Collection o2, Comparator c) {
    assertIterableEquals(o1, o2, c, "Collections");
  }

  public static void assertListEquals(List o1, List o2, Comparator c) {
    assertIterableEquals(o1, o2, c, "Lists");
  }

  public static void assertIterableEquals(Iterable o1, Iterable o2, Comparator c) {
    assertIterableEquals(makeList(o1), makeList(o2), c, "Iterables");
  }

  public static void assertIterableEqualsIgnoreOrder(Iterable i1, Iterable i2) {
    Map count1 = makeHistogram( i1 );
    Map count2 = makeHistogram( i2 );
    if (!count1.equals(count2)) {
      assertTrue( "Iterators were not equal ignoring order.  Expected [" + GosuStringUtil.join(i1.iterator(), ",") + "] but found [" + GosuStringUtil.join(i2.iterator(), ",") + "]",  false);
    }
  }

  public static void assertZero(int i) {
    assertTrue("Should be zero, but found " + i, i == 0);
  }

  public static void assertZero(long i) {
    assertTrue("Should be zero, but found " + i, i == 0);
  }

  public static void assertMatchRegex(String message, String pattern, String result) {
    assertTrue(message + ": " + pattern + " does not match " + result, result.matches(pattern));
  }
  
  private static Map makeHistogram(Iterable o1) {
    HashMap<Object, Integer> hist = new HashMap<Object, Integer>();
    if( o1 != null )
    {
      for (Object o : o1) {
        Integer integer = hist.get(o);
        if (integer == null) {
          hist.put(o, 0);
        } else {
          hist.put(o, ++integer);
        }
      }
    }
    return hist;
  }

  private static void assertIterableEquals(Iterable i1, Iterable i2, String s) {
    boolean equals = true;

    if (i1 == i2) return;

    Iterator e1 = i1.iterator();
    Iterator e2 = i2.iterator();
    while (e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      Object o2 = e2.next();
      if (o1 == null) {
        if (o2 != null) {
          equals = false;
          break;
        }
      } else if (!o1.equals(o2)) {
        equals = false;
        break;
      }
    }
    if (equals) {
      equals = !(e1.hasNext() || e2.hasNext());
    }
    assertTrue( s + " were not equal.  Expected \n[" + GosuStringUtil.join(i1.iterator(), ",") + "] but found \n[" + GosuStringUtil.join(i2.iterator(), ",") + "]",  equals);
  }

  private static void assertIterableEquals(Iterable i1, Iterable i2, Comparator c, String s) {
    boolean equals = true;

    if (i1 == i2) return;

    Iterator e1 = i1.iterator();
    Iterator e2 = i2.iterator();
    while (e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      Object o2 = e2.next();
      if (o1 == null) {
        if (o2 != null) {
          equals = false;
          break;
        }
      } else if (c.compare(o1, o2) != 0) {
        equals = false;
        break;
      }
    }
    if (equals) {
      equals = !(e1.hasNext() || e2.hasNext());
    }
    assertTrue( s + " were not equal.  Expected \n[" + GosuStringUtil.join(i1.iterator(), ",") + "] but found \n[" + GosuStringUtil.join(i2.iterator(), ",") + "]",  equals);
  }

  private static List makeList(Iterable o1) {
    ArrayList lst = new ArrayList();
    for (Object o : o1) {
      lst.add(o);
    }
    return lst;
  }

  public int getTotalNumTestMethods() {
    List<? extends IMethodInfo> methods = getType().getTypeInfo().getMethods();
    int count = 0;
    for (IMethodInfo method : methods) {
      if (!method.isStatic() && method.getName().startsWith("test") && method.getParameters().length == 0) {
        count++;
      }
    }
    return count;
  }

  @Override
  public List<TestMetadata> getMetadata() {
    return _metadata;
  }

  protected void addMetadata(Collection<TestMetadata> metadata) {
    _metadata.addAll(metadata);
    for (TestMetadata testMetadata : metadata) {
      if (testMetadata.shouldNotRunTest()) {
        _doNotRun = true;
      } else if (testMetadata.getName().equals(KnownBreak.class.getName())) {
        _knownBreak = true;
      }
    }
  }

  public Collection<TestMetadata> createMethodMetadata( String method )
  {
    if (getType() instanceof IJavaType) {
      // For Java types, we have to do things based on Method instead of MethodInfo, since Java TypeInfo isn't currently
      // reloadable, but the DCEVM means that Java classes themselves are
      try {
        Method testMethod = ((IJavaType) getType()).getBackingClass().getMethod(method);
        return createMetadata(testMethod.getAnnotations());
      } catch (NoSuchMethodException e) {
        throw new IllegalStateException( "Method not found: " + getType().getDisplayName() + "." + method);
      }
    } else {
      IMethodInfo testMethod = getType().getTypeInfo().getMethod(method);
      if(testMethod == null) {
        throw new IllegalStateException( "Method not found: " + getType().getDisplayName() + "." + method);
      }

      return createMetadata(testMethod.getAnnotations());
    }
  }


  public Collection<TestMetadata> createClassMetadata()
  {
    if (getType() instanceof IJavaType) {
      return createMetadata(((IJavaType) getType()).getBackingClass().getAnnotations());
    } else {
      return createMetadata(getType().getTypeInfo().getAnnotations());
    }
  }

  protected Collection<TestMetadata> createMetadata(List<IAnnotationInfo> annotationInfos) {
    Map<IType, TestMetadata> map = new HashMap<IType, TestMetadata>();
    for (IAnnotationInfo ai : annotationInfos) {
      if (isMetaAnnotationInfo(ai)) {
        map.put(ai.getType(), new TestMetadata(ai));
      }
    }
    if (map.containsKey(TypeSystem.get( KnownBreak.class ))) {
      for (IAnnotationInfo ai : annotationInfos) {
        if (isKnownBreakQualifier(ai)) {
          Predicate<? super IAnnotationInfo> qualifierPredicate;
          try {
            Object t = ai.getType().getTypeInfo().getAnnotation( TypeSystem.get( KnownBreakQualifier.class ) ).getFieldValue( "value" );
            IType type = t instanceof Class ? TypeSystem.get( (Class)t ) : (IType)t;
            qualifierPredicate = (Predicate<? super IAnnotationInfo>)type.getTypeInfo().getConstructor().getConstructor().newInstance();
          } catch (RuntimeException e) {
            throw e;
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
          if (!qualifierPredicate.evaluate(ai)) {
            map.remove(TypeSystem.get( KnownBreak.class ));
            break;
          }
        }
      }
    }
    return map.values();
  }

  private boolean isKnownBreakQualifier(IAnnotationInfo ai) {
    boolean isQualifier = false;
    for (IAnnotationInfo a : ai.getType().getTypeInfo().getAnnotations()) {
      if (a.getName().equals(KnownBreakQualifier.class.getName())) {
        isQualifier = true;
        break;
      }
    }
    return isQualifier;
  }

  protected boolean isMetaAnnotationInfo(IAnnotationInfo ai) {
    boolean isMetadata = false;
    for (IAnnotationInfo a : ai.getType().getTypeInfo().getAnnotations()) {
      if (a.getName().equals(IncludeInTestResults.class.getName())) {
        isMetadata = true;
        break;
      }
    }
    return isMetadata;
  }

  protected Collection<TestMetadata> createMetadata(Annotation[] annotations) {
    Map<Class<? extends Annotation>, TestMetadata> map = new HashMap<Class<? extends Annotation>, TestMetadata>();
    for (Annotation a : annotations) {
      if (isMetaAnnotation(a)) {
        map.put(a.annotationType(), new TestMetadata( GosuShop.getAnnotationInfoFactory().createJavaAnnotation( a, getType().getTypeInfo() ) ) );
      }
    }
    if (map.containsKey(KnownBreak.class)) {
      for (Annotation a : annotations) {
        if (isKnownBreakQualifier(a)) {
          Predicate<? super IAnnotationInfo> qualifierPredicate;
          try {
            qualifierPredicate = a.annotationType().getAnnotation(KnownBreakQualifier.class).value().newInstance();
          } catch (RuntimeException e) {
            throw e;
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
          if (!qualifierPredicate.evaluate( GosuShop.getAnnotationInfoFactory().createJavaAnnotation( a, getType().getTypeInfo() ) ) ) {
            map.remove(KnownBreak.class);
            break;
          }
        }
      }
    }
    return map.values();
  }

  private boolean isKnownBreakQualifier(Annotation a) {
    return a.annotationType().getAnnotation(KnownBreakQualifier.class) != null;
  }

  protected boolean isMetaAnnotation(Annotation ai) {
    boolean isMetadata = false;
    for (Annotation a : ai.annotationType().getAnnotations()) {
      if (a instanceof IncludeInTestResults) {
        isMetadata = true;
        break;
      }
    }
    return isMetadata;
  }

  public void initMetadata( String method )
  {
    addMetadata( createClassMetadata() );
    addMetadata( createMethodMetadata( method ) );
  }

  public static void assertCausesException( Runnable r, Class<? extends Throwable> c )
  {
    try {
      r.run();
    } catch( Throwable t )
    {
      if( c.isAssignableFrom( t.getClass() ) )
      {
        return;
      }
      else
      {
        fail( "Expecting exception of type " + c + ", but got exception of type " + t.getClass() );
      }
    }
    fail( "No exception was thrown when executing " + r + ".  Expected exception of type " + c );
  }

  public static TestClass createTestClass(IType testType) {
    IConstructorInfo noArgConstructor = testType.getTypeInfo().getConstructor();
    if (noArgConstructor != null) {
      return (TestClass) noArgConstructor.getConstructor().newInstance();
    } else {
      IConstructorInfo oneArgConstructor = testType.getTypeInfo().getConstructor(JavaTypes.STRING());
      if (oneArgConstructor != null) {
        return (TestClass) oneArgConstructor.getConstructor().newInstance("temp");
      } else {
        throw new IllegalArgumentException("Type " + testType.getName() + " does not have either a no-arg constructor or a one-arg constructor that takes a String");
      }
    }
  }

  public static <T extends TestClass>junit.framework.Test _suite(Class<T> clazz) {
    return TestClassHelper.createTestSuite(clazz, TestSpec.extractTestMethods(clazz));
  }
}
