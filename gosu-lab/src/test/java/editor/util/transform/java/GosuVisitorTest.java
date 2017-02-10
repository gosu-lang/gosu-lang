/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

@Ignore
public class GosuVisitorTest
{
  private String folder = "util/transform/java/javaClasses/";
  private GosuVisitorTestUtil helper = new GosuVisitorTestUtil();

  @Test
  public void testBasic() {
    verify("BasicClass");
  }

  @Test
  public void testBasicEnum() {
    verify("BasicEnum");
  }

  @Test
  public void testBasicInterface() {
    verify("BasicInterface");
  }

  @Test
  public void testVisitIf() {
    verify("TestIf");
  }

  @Test
  public void testVisitWhileLoopDoWhileLoop() {
    verify("TestWhileDoWhile");
  }

  @Test
  public void testVisitForLoop() {
    verify("TestFor");
  }

  @Test
  public void testScope() {
    verify("TestScope");
  }

  @Test
  public void testVisitEnhancedForLoop() {
    verify("TestEnhancedFor");
  }

  @Test
  public void testVisitAssert() {
    verify("TestAssert");
  }

  @Test
  public void testTryCatchFinallyThrow() {
    verify("TestTryCatchFinallyThrow");
  }

  @Test
  public void testSwitchCase() {
    verify("TestSwitchCase");
  }

  @Test
  public void testSynchronized() {
    verify("TestSynchronized");
  }

  @Test
  public void testExpressions() {
    verify("TestExpressions");
  }

  @Test
  public void testMethod() {
    verify("TestMethod");
  }

  @Test
  public void testEnum() {
    verify("TestEnum");
  }

  @Test
  public void testOuterMemberSelect() {
    verify("TestOuterMemberSelect");
  }

  @Test
  public void testVisitAnnotation() {
    verify("TestAnnotation");
  }

  @Test
  public void testJava8() {
    verify("TestJava8");
  }

  @Test
  public void testIllegalSyntax() {
    String out = null;
    try {
      out = helper.transform(folder + "TestIllegalSyntax.javax");
    } catch (IOException e) {
      fail();
    }
    assertEquals(out, "");
  }

  @Test
  public void testGenericsPair() {
    verify("MyGenericPair");
  }

  @Test
  public void testLocalVariableRename() {
    verify("TestLocalVariableRename");
  }

  @Test
  public void testGenerics() {
    verify("TestGenerics");
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
