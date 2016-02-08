/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.test.Suite;
import gw.test.ZZLastTest;
import gw.test.AAFirstTest;
import gw.test.TestEnvironment;
import junit.framework.Test;

public class GosuSuite extends Suite {

  public GosuSuite() {
    withTestEnvironment(new GosuSuiteTestEnvironment());
  }

  public static Test suite() {
    return new GosuSuite();
  }

  public static void main(String[] args) {
    System.exit( new GosuSuite().runViaStaticSuiteMethod() ? 0 : 1 );
  }

  private static class GosuSuiteTestEnvironment extends TestEnvironment {
    @Override
    public void beforeTestSuite() {
      assert !AAFirstTest.hasStarted() : "Should have run this before any other tests!";
      System.out.println( "\n\n****Gosu Suite Setup OK****\n\n" );
    }

    @Override
    public void afterTestSuite() {
      assert ZZLastTest.hasRunEntirely() : "The final test should have been run before this was called!";
      System.out.println( "\n\n****Gosu Suite Teardown OK****\n\n" );
    }
  }
}
