/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.reflect.IType;
import gw.test.Suite;
import gw.util.Predicate;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * Typical JVM args:
 *   -Dgw.tests.skip.knownbreak=true
 *   -DcheckedArithmetic=true
 */
public class PLGosuBytecodeSuite extends Suite
{
  public PLGosuBytecodeSuite() 
  {
    withTestTypeFilter(getTestTypeFilter());
  }

  @Override
  public void run( TestResult result )
  {
    super.run( result );
    //StringPool.printStats();
  }

  public static Test suite() {
    return new PLGosuBytecodeSuite();
  }

  static boolean isByteCodeTest(IType type)
  {
    return !type.getNamespace().equals("gw.internal.gosu.parser.java") &&
           !isInstrumentationSuiteOnlyTest( type );
  }

  static boolean isInstrumentationSuiteOnlyTest( IType type )
  {
    return false;
  }

  static Predicate<IType> getTestTypeFilter() 
  {
    return type -> isByteCodeTest(type) &&
        !type.getName().startsWith("gw.test.SuiteTest") &&
        !type.getName().contains("BenchmarkTest");
  }
}
