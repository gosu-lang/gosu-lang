/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.test.TestClass;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jun 18, 2010
 * Time: 5:23:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleXmlNodeWriterTest extends TestClass {

  public void testSingleEmptyNode() {
    SimpleXmlNode node = node("foo", null, null);
    assertEquals("<foo/>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithText() {
    SimpleXmlNode node = node("foo", "text", null);
    assertEquals("<foo>text</foo>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithAttribute() {
    SimpleXmlNode node = node("foo", null, attributes("a", "aa"));
    assertEquals("<foo a=\"aa\"/>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithAttributes() {
    SimpleXmlNode node = node("foo", null, attributes("a", "aa", "b", "bb"));
    assertEquals("<foo a=\"aa\" b=\"bb\"/>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithTextAndAttributes() {
    SimpleXmlNode node = node("foo", "text", attributes("a", "aa", "b", "bb"));
    assertEquals("<foo a=\"aa\" b=\"bb\">text</foo>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithAttributeRequiringEscaping() {
    SimpleXmlNode node = node("foo", null, attributes("a", "\"bar\""));
    assertEquals("<foo a='\"bar\"'/>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithNullAttribute() {
    SimpleXmlNode node = node("foo", null, attributes("a", null));
    assertEquals("<foo a=\"\"/>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testSingleNodeWithTextRequiringEscaping() {
    SimpleXmlNode node = node("foo", "<>", null);
    assertEquals("<foo>&lt;&gt;</foo>", SimpleXmlNodeWriter.writeToString(node));
  }

  public void testNodeWithSingleChild() {
    SimpleXmlNode node = node("foo", null, null, node("child", "text", null));
    assertEquals(
        "<foo>\n" +
        "  <child>text</child>\n" +
        "</foo>",
        SimpleXmlNodeWriter.writeToString(node));
  }

  public void testNodeWithMultipleChildren() {
    SimpleXmlNode node = node("foo", null, null, node("child", "text", null), node("child2", null, attributes("a", "aa")));
    assertEquals(
        "<foo>\n" +
        "  <child>text</child>\n" +
        "  <child2 a=\"aa\"/>\n" +
        "</foo>",
        SimpleXmlNodeWriter.writeToString(node));
  }

  public void testTreeOfNodes() {
    SimpleXmlNode node = node("foo", null, null,
                                node("child", null, null,
                                    node("leaf", null, null), node("leaf2", null, null)),
                                node("child2", null, attributes("a", "aa"),
                                    node("leaf3", null, null), node("leaf4", null, null)));
    assertEquals(
        "<foo>\n" +
        "  <child>\n" +
        "    <leaf/>\n" +
        "    <leaf2/>\n" +
        "  </child>\n" +
        "  <child2 a=\"aa\">\n" +
        "    <leaf3/>\n" +
        "    <leaf4/>\n" +
        "  </child2>\n" +
        "</foo>",
        SimpleXmlNodeWriter.writeToString(node));
  }

  public void testNodeWithChildrenAndText() {
    SimpleXmlNode node = node("foo", "text", null, node("child", "text", null), node("child2", null, attributes("a", "aa")));
    assertEquals(
        "<foo>\n" +
        "  text\n" +
        "  <child>text</child>\n" +
        "  <child2 a=\"aa\"/>\n" +
        "</foo>",
        SimpleXmlNodeWriter.writeToString(node));
  }

  private SimpleXmlNode node(String name, String text, Map<String, String> attributes, SimpleXmlNode... children) {
    SimpleXmlNode node = new SimpleXmlNode(name);
    if (text != null) {
      node.setText(text);
    }
    if (attributes != null) {
      node.getAttributes().putAll(attributes);
    }
    for (SimpleXmlNode child : children) {
      node.getChildren().add(child);
    }
    return node;
  }

  private Map<String, String> attributes(String... attributes) {
    Map<String, String> result = new HashMap<String, String>();
    for (int i = 0; i < attributes.length; i+=2) {
      result.put(attributes[i], attributes[i + 1]);
    }
    return result;
  }
}
