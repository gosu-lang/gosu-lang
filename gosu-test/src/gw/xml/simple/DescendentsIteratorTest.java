/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.test.TestClass;

import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jun 18, 2010
 * Time: 4:30:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DescendentsIteratorTest extends TestClass {

  public void testIterateEmptyListOfChildren() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo/>");
    DescendentsIterator iterator = new DescendentsIterator(node);
    assertFalse(iterator.hasNext());
  }

  public void testNextThrowsNoSuchElementExceptionIfNoMoreElements() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo/>");
    DescendentsIterator iterator = new DescendentsIterator(node);
    try {
      iterator.next();
      fail();
    } catch (NoSuchElementException e) {
      // Expected
    }
  }

  public void testIterateSimpleListOfChildren() {
    SimpleXmlNode node = SimpleXmlNode.parse(
        "<foo>\n" +
        "  <bar1/>\n" +
        "  <bar2/>\n" +
        "</foo>");

    DescendentsIterator iterator = new DescendentsIterator(node);
    assertTrue(iterator.hasNext());
    assertEquals("bar1", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("bar2", iterator.next().getName());
    assertFalse(iterator.hasNext());
  }

  public void testIterateComplicatedTreeOfChildren() {
    SimpleXmlNode node = SimpleXmlNode.parse(
        "<foo>\n" +
        "  <bar1>\n" +
        "    <baz1>\n" +
        "       <leaf1/>\n" +
        "       <leaf2/>\n" +
        "    </baz1>\n" +
        "    <baz2>\n" +
        "      <leaf3/>\n" +
        "      <leaf4/>\n" +
        "    </baz2>\n" +
        "  </bar1>\n" +
        "  <bar2>\n" +
        "    <baz3/>\n" +
        "    <baz4/>\n" +
        "  </bar2>\n" +
        "</foo>"
    );

    DescendentsIterator iterator = new DescendentsIterator(node);
    assertTrue(iterator.hasNext());
    assertEquals("bar1", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("baz1", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("leaf1", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("leaf2", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("baz2", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("leaf3", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("leaf4", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("bar2", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("baz3", iterator.next().getName());
    assertTrue(iterator.hasNext());
    assertEquals("baz4", iterator.next().getName());
    assertFalse(iterator.hasNext());
  }

  public void testRemoveThrowsUnsupportedOperationException() {
    SimpleXmlNode node = SimpleXmlNode.parse(
        "<foo>\n" +
        "  <bar1/>\n" +
        "  <bar2/>\n" +
        "</foo>");

    DescendentsIterator iterator = new DescendentsIterator(node);
    iterator.next();
    try {
      iterator.remove();
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }
}
