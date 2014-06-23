/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.GosuVersion;
import gw.test.TestClass;

import java.io.Reader;
import java.io.StringReader;

/**
 */
public class GosuVersionTest extends TestClass {

  public void testParseOldFormatWithEmptyAux() throws Exception {
    String content = "gosu.version.major=0.9\n" +
            "gosu.version.minor=2\n" +
            "gosu.version.aux=\n";
    Reader in = new StringReader(content);
    GosuVersion version = GosuVersion.parse(in);
    assertEquals(0, version.getMajor());
    assertEquals(9, version.getMinor());
    assertEquals(2, version.getIncremental());
    assertEquals(0, version.getBuildNum());
    assertNull(version.getQualifier());
  }

  public void testParseOldFormatWithAux() throws Exception {
    String content = "gosu.version.major=0.9\n" +
            "gosu.version.minor=2\n" +
            "gosu.version.aux=abc\n";
    Reader in = new StringReader(content);
    GosuVersion version = GosuVersion.parse(in);
    assertEquals(0, version.getMajor());
    assertEquals(9, version.getMinor());
    assertEquals(2, version.getIncremental());
    assertEquals(0, version.getBuildNum());
    assertEquals("abc", version.getQualifier());
  }

  public void testParseWithQualifier() throws Exception {
    String content = "gosu.version.major=0\n" +
            "gosu.version.minor=9\n" +
            "gosu.version.incremental=15\n" +
            "gosu.version.build=0\n" +
            "gosu.version.qualifier=SNAPSHOT\n";
    Reader in = new StringReader(content);
    GosuVersion version = GosuVersion.parse(in);
    assertEquals(0, version.getMajor());
    assertEquals(9, version.getMinor());
    assertEquals(15, version.getIncremental());
    assertEquals(0, version.getBuildNum());
    assertEquals("SNAPSHOT", version.getQualifier());
  }

  public void testParseWithBuildNum() throws Exception {
    String content = "gosu.version.major=0\n" +
            "gosu.version.minor=9\n" +
            "gosu.version.incremental=15\n" +
            "gosu.version.build=555\n" +
            "gosu.version.qualifier=\n";
    Reader in = new StringReader(content);
    GosuVersion version = GosuVersion.parse(in);
    assertEquals(0, version.getMajor());
    assertEquals(9, version.getMinor());
    assertEquals(15, version.getIncremental());
    assertEquals(555, version.getBuildNum());
    assertNull(version.getQualifier());
  }

  public void testConstructorWithNonZeroBuildNumAndNonEmptyQualifier() throws Exception {
    String content = "gosu.version.major=0\n" +
            "gosu.version.minor=9\n" +
            "gosu.version.incremental=15\n" +
            "gosu.version.build=555\n" +
            "gosu.version.qualifier=SNAPSHOT\n";
    Reader in = new StringReader(content);
    try {
      GosuVersion.parse(in);
      fail("expected IllegalArgumentException");
    }
    catch (IllegalArgumentException e) {
      assertEquals("build number non-zero and qualifier non-empty, can only have one or the other", e.getMessage());
    }
  }

  public void testToString() {
    GosuVersion version = new GosuVersion(0, 9, 1);
    assertEquals("0.9.1", version.toString());
  }

  public void testToStringWithBuildNum() {
    GosuVersion version = new GosuVersion(0, 9, 1, 555);
    assertEquals("0.9.1-555", version.toString());
  }

  public void testToStringWithZeroBuildNum() {
    GosuVersion version = new GosuVersion(0, 9, 1, 0);
    assertEquals("0.9.1", version.toString());
  }

  public void testToStringWithQualifier() {
    GosuVersion version = new GosuVersion(0, 9, 1, "foo");
    assertEquals("0.9.1-foo", version.toString());
  }

  public void testToStringWithEmptyQualifier() {
    GosuVersion version = new GosuVersion(0, 9, 1, "");
    assertNull(version.getQualifier());
    assertEquals("0.9.1", version.toString());
  }

  public void testToStringWithNullQualifier() {
    GosuVersion version = new GosuVersion(0, 9, 1, null);
    assertNull(version.getQualifier());
    assertEquals("0.9.1", version.toString());
  }

  public void testCompareWithDifferingMajors() {
    GosuVersion a = new GosuVersion(0, 9);
    GosuVersion b = new GosuVersion(1, 0);
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareWithDifferingMinors() {
    GosuVersion a = new GosuVersion(0, 9, 1);
    GosuVersion b = new GosuVersion(0, 10, 0);
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareWithDifferingIncrementals() {
    GosuVersion a = new GosuVersion(0, 9, 1);
    GosuVersion b = new GosuVersion(0, 9, 2);
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareWithDifferingBuildNums() {
    GosuVersion a = new GosuVersion(0, 9, 1);
    GosuVersion b = new GosuVersion(0, 9, 1, 1);
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareWithDifferingQualifiers() {
    GosuVersion a = new GosuVersion(0, 9, 1, "abc");
    GosuVersion b = new GosuVersion(0, 9, 1, "def");
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareWithOneNullQualifier() {
    GosuVersion a = new GosuVersion(0, 9, 1, null);
    GosuVersion b = new GosuVersion(0, 9, 1, "def");
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareShowsVersionWithBuildNumIsGreaterThanVersionWithQualifier() {
    GosuVersion a = new GosuVersion(0, 9, 1, "abc");
    GosuVersion b = new GosuVersion(0, 9, 1, 1);
    assertTrue(a.compareTo(b) < 0);
    assertTrue(b.compareTo(a) > 0);
    assertFalse(a.equals(b));
  }

  public void testCompareEqual() {
    GosuVersion a = new GosuVersion(0, 9, 1, 0);
    GosuVersion b = new GosuVersion(0, 9, 1, 0);
    assertTrue(a.compareTo(b) == 0);
    assertEquals(a, b);
  }

  public void testCompareEqualWithQualifiers() {
    GosuVersion a = new GosuVersion(0, 9, 1, "123");
    GosuVersion b = new GosuVersion(0, 9, 1, "123");
    assertTrue(a.compareTo(b) == 0);
    assertEquals(a, b);
  }
}
