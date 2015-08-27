/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.fs.IFile;
import gw.lang.reflect.IType;
import gw.test.Suite;
import gw.test.TestEnvironment;
import gw.util.Predicate;
import junit.framework.Test;
import junit.framework.TestResult;

public class PLGosuBytecodeSuite extends Suite
{
  public PLGosuBytecodeSuite() {
    withTestEnvironment(new TestEnvironment());
    withPackages( getPackages() )
    .withIFileFilter(new Predicate<IFile>() {
      @Override
      public boolean evaluate(IFile o) {
        return !o.getPath().getPathString().contains("app-px");
      }
    })
    .withTestTypeFilter(new Predicate<IType>() {
      @Override
      public boolean evaluate(IType type) {
        return isByteCodeTest(type) &&
          !type.getName().startsWith("gw.test.SuiteTest") &&
          !type.getName().equals("gw.test.SuiteTest") &&
          !type.getName().contains("BenchmarkTest");
      }
    });
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

  public static String[] getPackages()
  {
    return new String[]{"gw.config", "gw.lang", "gw.util", "gw.test", "gw.internal", "gw.spec", "gw.xml" };
  }
}
