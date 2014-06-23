/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.test.TestClass;
import gw.testharness.KnownBreak;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Dec 14, 2010
 * Time: 6:04:08 PM
 */
public class RemoteTestResultTest extends TestClass {

  // TODO - AHK - Test a stack trace will null method or file names
  // TODO - AHK - Lots of other tests

  public void testSerializeAndDeserializeWithoutCause() {
    Exception e = new RuntimeException("My message");
    RemoteTestResult original = new RemoteTestResult();
    original.setException(e);

    RemoteTestResult recreated = RemoteTestResult.fromXML(original.toXML());
    assertEquals(original.successful(), recreated.successful());
    assertExceptionInfoIsEqual(original.getExceptionInfo(), recreated.getExceptionInfo());

    assertStackTracesAreIdentical(e, recreated.recreateException());
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  public void testSerializeAndDeserializeWithAssertionFailedException() {
    Throwable assertionFailure = null;
    try {
      fail("Fail test");
    } catch (Throwable t) {
      //This test checks that test result with failure WITH message
      //is processed properly,
      //See testSerializeAndDeserializeWithAssertionFailedExceptionWithoutMessage.
      Assert.assertNotNull(t.getMessage());
      assertionFailure = t;
    }
    RemoteTestResult original = new RemoteTestResult();
    original.setException(assertionFailure);

    RemoteTestResult recreated = RemoteTestResult.fromXML(original.toXML());
    assertEquals(original.successful(), recreated.successful());
    assertExceptionInfoIsEqual(original.getExceptionInfo(), recreated.getExceptionInfo());

    assertStackTracesAreIdentical(assertionFailure, recreated.recreateException());
    assertTrue(recreated.recreateException() instanceof AssertionFailedError);
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  @KnownBreak(jira = "PL-24597")
  public void testSerializeAndDeserializeWithAssertionFailedExceptionWithoutMessage() {
    Throwable assertionFailure = null;
    try {
      //We use no message here.
      fail();
    } catch (Throwable t) {
      //This test checks that test result with failure WITHOUT message (with null message).
      //is processed properly,
      Assert.assertNull(t.getMessage());
      assertionFailure = t;
    }
    RemoteTestResult original = new RemoteTestResult();
    original.setException(assertionFailure);

    RemoteTestResult recreated = RemoteTestResult.fromXML(original.toXML());
    assertEquals(original.successful(), recreated.successful());
    assertExceptionInfoIsEqual(original.getExceptionInfo(), recreated.getExceptionInfo());

    assertStackTracesAreIdentical(assertionFailure, recreated.recreateException());
    assertTrue(recreated.recreateException() instanceof AssertionFailedError);
  }


  public void testSerializeAndDeserializeWithCause() {
    Exception e = new RuntimeException("My message", new IllegalArgumentException("My cause"));
    RemoteTestResult original = new RemoteTestResult();
    original.setException(e);

    RemoteTestResult recreated = RemoteTestResult.fromXML(original.toXML());
    assertEquals(original.successful(), recreated.successful());
    assertExceptionInfoIsEqual(original.getExceptionInfo(), recreated.getExceptionInfo());

    assertStackTracesAreIdentical(e, recreated.recreateException());
  }

  public void testSerializeAndDeserializeWithNestedCauses() {
    Exception e = new RuntimeException("My message", new IllegalArgumentException("My cause", new IllegalStateException("Root cause")));
    RemoteTestResult original = new RemoteTestResult();
    original.setException(e);

    RemoteTestResult recreated = RemoteTestResult.fromXML(original.toXML());
    assertEquals(original.successful(), recreated.successful());
    assertExceptionInfoIsEqual(original.getExceptionInfo(), recreated.getExceptionInfo());

    assertStackTracesAreIdentical(e, recreated.recreateException());
  }

  private void assertExceptionInfoIsEqual(RemoteTestResult.ExceptionInfo originalExceptionInfo, RemoteTestResult.ExceptionInfo recreatedExceptionInfo) {
    assertEquals(originalExceptionInfo.getClassName(), recreatedExceptionInfo.getClassName());
    assertEquals(originalExceptionInfo.getMessage(), recreatedExceptionInfo.getMessage());
    assertEquals(originalExceptionInfo.getStackTrace().length, recreatedExceptionInfo.getStackTrace().length);
    for (int i = 0; i < originalExceptionInfo.getStackTrace().length; i++) {
      assertEquals(originalExceptionInfo.getStackTrace()[i], recreatedExceptionInfo.getStackTrace()[i]);
    }

    if (originalExceptionInfo.getCause() == null) {
      assertNull(recreatedExceptionInfo.getCause());
    } else {
      assertNotNull(recreatedExceptionInfo.getCause());
      assertExceptionInfoIsEqual(originalExceptionInfo.getCause(), recreatedExceptionInfo.getCause());
    }
  }

  private void assertStackTracesAreIdentical(Throwable expected, Throwable actual) {
    StringWriter sw1 = new StringWriter();
    expected.printStackTrace(new PrintWriter(sw1));

    StringWriter sw2 = new StringWriter();
    expected.printStackTrace(new PrintWriter(sw2));

    assertEquals(sw1.toString(), sw2.toString());
  }
}
