/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.test.TestClass;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jun 17, 2010
 * Time: 6:32:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleXmlNodeTest extends TestClass {

  public void testConstructorThrowsIllegalArgumentExceptionIfNameIsNull() {
    try {
      new SimpleXmlNode(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testParseSimpleString() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo/>");
    assertNodeBasics(node, "foo", null, null, 0, 0);
  }

  public void testParseSimpleStringWithAttributes() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo att1='a' att2='b'/>");
    assertNodeBasics(node, "foo", null, null, 0, 2);
    assertEquals("a", node.getAttributes().get("att1"));
    assertEquals("b", node.getAttributes().get("att2"));
  }

  public void testParseSimpleStringWithText() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo>text</foo>");
    assertNodeBasics(node, "foo", null, "text", 0, 0);
  }

  public void testElementPrefixesAreIgnored() {
    SimpleXmlNode node = SimpleXmlNode.parse("<bar:foo/>");
    assertNodeBasics(node, "foo", null, null, 0, 0);
  }

  public void testAttributePrefixesAreIgnored() {
    SimpleXmlNode node = SimpleXmlNode.parse("<foo aa:att1='a' bb:att2='b'/>");
    assertNodeBasics(node, "foo", null, null, 0, 2);
    assertEquals("a", node.getAttributes().get("att1"));
    assertEquals("b", node.getAttributes().get("att2"));
  }

  public void testParsingXmlWithDuplicateLocalPartAttributesThrowsIllegalStateException() {
    try {
      SimpleXmlNode.parse("<foo aa:att1='a' bb:att1='b'/>");
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  public void testParseXmlNodeWithChildren() {
    SimpleXmlNode root = SimpleXmlNode.parse("<foo><bar/><baz/></foo>");
    assertNodeBasics(root, "foo", null, null, 2, 0);
    assertNodeBasics(root.getChildren().get(0), "bar", root, null, 0, 0);
    assertNodeBasics(root.getChildren().get(1), "baz", root, null, 0, 0);
  }

  public void testParseXmlNodeWithNestedChildren() {
    SimpleXmlNode root = SimpleXmlNode.parse("<foo><bar><bar2/></bar><baz><baz2/><baz3/></baz></foo>");
    assertNodeBasics(root, "foo", null, null, 2, 0);
    assertNodeBasics(root.getChildren().get(0), "bar", root, null, 1, 0);
    assertNodeBasics(root.getChildren().get(0).getChildren().get(0), "bar2", root.getChildren().get(0), null, 0, 0);
    assertNodeBasics(root.getChildren().get(1), "baz", root, null, 2, 0);
    assertNodeBasics(root.getChildren().get(1).getChildren().get(0), "baz2", root.getChildren().get(1), null, 0, 0);
    assertNodeBasics(root.getChildren().get(1).getChildren().get(1), "baz3", root.getChildren().get(1), null, 0, 0);
  }

  public void testShallowCopy() {
    SimpleXmlNode root = SimpleXmlNode.parse("<foo><bar att1='a'>SomeText<bar2/></bar><baz><baz2/><baz3/></baz></foo>");
    SimpleXmlNode barCopy = root.getChildren().get(0).shallowCopy();
    assertNodeBasics(barCopy, "bar", null, "SomeText", 0, 1);
    assertEquals("a", barCopy.getAttributes().get("att1"));
  }

  public void testDeepCopy() {
    SimpleXmlNode root = SimpleXmlNode.parse("<foo><bar att1='a'>SomeText<bar2/></bar><baz><baz2/><baz3/></baz></foo>");

    SimpleXmlNode rootCopy = root.deepCopy();
    assertNodeBasics(rootCopy, "foo", null, null, 2, 0);
    assertNodeBasics(rootCopy.getChildren().get(0), "bar", rootCopy, "SomeText", 1, 1);
    assertEquals("a", rootCopy.getChildren().get(0).getAttributes().get("att1"));
    assertNodeBasics(rootCopy.getChildren().get(0).getChildren().get(0), "bar2", rootCopy.getChildren().get(0), null, 0, 0);
    assertNodeBasics(rootCopy.getChildren().get(1), "baz", rootCopy, null, 2, 0);
    assertNodeBasics(rootCopy.getChildren().get(1).getChildren().get(0), "baz2", rootCopy.getChildren().get(1), null, 0, 0);
    assertNodeBasics(rootCopy.getChildren().get(1).getChildren().get(1), "baz3", rootCopy.getChildren().get(1), null, 0, 0);
  }

  public void testGetDescendentsReturnsPreOrderedTraversal() {
    // See additional tests in DescendentsIteratorTest
    SimpleXmlNode root = SimpleXmlNode.parse("<foo><bar att1='a'>SomeText<bar2/></bar><baz><baz2/><baz3/></baz></foo>");
    Iterator<SimpleXmlNode> it = root.getDescendents().iterator();
    assertEquals("bar", it.next().getName());
    assertEquals("bar2", it.next().getName());
    assertEquals("baz", it.next().getName());
    assertEquals("baz2", it.next().getName());
    assertEquals("baz3", it.next().getName());
    assertFalse(it.hasNext());
  }

  // ------------------------------ Private helpers

  private void assertNodeBasics(SimpleXmlNode node, String name, SimpleXmlNode parent, String text, int numChildren, int numAttributes) {
    assertEquals(name, node.getName());
    assertEquals(parent, node.getParent());
    assertEquals(text, node.getText());
    assertEquals(numChildren, node.getChildren().size());
    assertEquals(numAttributes, node.getAttributes().size());
  }
}
