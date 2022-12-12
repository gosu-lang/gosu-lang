/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;


import junit.framework.TestCase;

import java.io.IOException;


public class GosuVisitorTest extends TestCase {
  private String folder = "util/transform/java/javaClasses/";
  private GosuVisitorTestUtil helper = new GosuVisitorTestUtil();

  public void testBasic() {
    verify("BasicClass");
  }

  public void testJavaDoc() {
    verify("JavaDoc");
  }

  public void testBasicEnum() {
    verify("BasicEnum");
  }

  public void testBasicInterface() {
    verify("BasicInterface");
  }

  public void testVisitIf() {
    verify("TestIf");
  }

  public void testVisitWhileLoopDoWhileLoop() {
    verify("TestWhileDoWhile");
  }

  public void testVisitForLoop() {
    verify("TestFor");
  }

  public void testScope() {
    verify("TestScope");
  }

  public void testVisitEnhancedForLoop() {
    verify("TestEnhancedFor");
  }

  public void testVisitAssert() {
    verify("TestAssert");
  }

  public void testTryCatchFinallyThrow() {
    verify("TestTryCatchFinallyThrow");
  }

  public void testSwitchCase() {
    verify("TestSwitchCase");
  }

  public void testSynchronized() {
    verify("TestSynchronized");
  }

  public void testExpressions() {
    verify("TestExpressions");
  }

  public void testMethod() {
    verify("TestMethod");
  }

  public void testEnum() {
    verify("TestEnum");
  }

  public void testOuterMemberSelect() {
    verify("TestOuterMemberSelect");
  }

  public void testVisitAnnotation() {
    verify("TestAnnotation");
  }

//  public void testJava8() {
//    verify("TestJava8");
//  }

  public void testIllegalSyntax() {
    String out = null;
    try {
      out = helper.transform(folder + "TestIllegalSyntax.javax");
    } catch (IOException e) {
      fail();
    }
    assertEquals(out, "");
  }

  public void testGenericsPair() {
    verify("MyGenericPair");
  }

  public void testLocalVariableRename() {
    verify("TestLocalVariableRename");
  }

  public void testGenerics() {
    verify("TestGenerics");
  }

  public void testAssignmentInLambda() {
    verify("AssignmentInLambda");
  }

  private void verifyNoOutput(String baseName) {
    try {
      String out = helper.transform(folder + baseName + ".javax");
      assertNull(out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void verify(String baseName) {
    try {
      String out = helper.transform(folder + baseName + ".javax");
      String path = folder + baseName + ".gsy";
      String reference = helper.readFile(path);
      assertEquals(reference, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
