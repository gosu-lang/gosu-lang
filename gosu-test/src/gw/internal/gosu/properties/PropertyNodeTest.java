/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import gw.test.TestClass;
import junit.framework.TestCase;

public class PropertyNodeTest extends TestClass {
  
  private static class TestPropertySet implements PropertySet {
    
    private final Set<String> _keys;

    public TestPropertySet(String... keys) {
      _keys = new TreeSet<String>(Arrays.asList(keys));
    }

    @Override
    public Set<String> getKeys() {
      return _keys;
    }

    @Override
    public String getName() {
      return "test.set";
    }

    @Override
    public String getValue(String key) {
      return _keys.contains(key) ? "valueOf(" + key + ")" : null;
    }
  }
  
  public void testFlatPropertySetStructure() {
    TestPropertySet propertySet = new TestPropertySet("a", "b", "c");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    assertEquals(3, root.getChildren().size());
    for (PropertyNode child : root.getChildren()) {
      assertEquals(0, child.getChildren().size());
    }
  }
  
  public void testDeepPropertySetStructure() {
    TestPropertySet propertySet = new TestPropertySet("a.b.c", "a.b.d");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    assertEquals(1, root.getChildren().size());
    PropertyNode child = root.getChildren().get(0);
    assertEquals(1, child.getChildren().size());
    PropertyNode grandChild = child.getChildren().get(0);
    assertEquals(2, grandChild.getChildren().size());
    for (PropertyNode greatGrandChild : grandChild.getChildren()) {
      assertEquals(0, greatGrandChild.getChildren().size());
    }
  }
  
  public void testHasValueTrueIfUnderlyingPropertyExists() {
    TestPropertySet propertySet = new TestPropertySet("a.b", "a.b.c", "a.b.d");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    PropertyNode child = getOnlyChild(root);
    assertFalse(child.hasValue());
    PropertyNode grandChild = getOnlyChild(child);
    assertTrue(grandChild.hasValue());
    assertEquals(2, grandChild.getChildren().size());
    for (PropertyNode greatGrandChild : grandChild.getChildren()) {
      assertEquals(0, greatGrandChild.getChildren().size());
      assertTrue(greatGrandChild.hasValue());
    }       
  }
  
  public void testFullName() {
    TestPropertySet propertySet = new TestPropertySet("a.b.c");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    assertEquals("test.set", root.getFullName());
    PropertyNode child = getOnlyChild(root);
    assertEquals("test.set.a", child.getFullName());
    PropertyNode grandChild = getOnlyChild(child);
    assertEquals("test.set.a.b", grandChild.getFullName());
    PropertyNode greatGrandChild = getOnlyChild(grandChild);
    assertEquals("test.set.a.b.c", greatGrandChild.getFullName());    
  }

  public void testRelativeName() {
    TestPropertySet propertySet = new TestPropertySet("a.b.c");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    assertEquals("set", root.getRelativeName());
    PropertyNode child = getOnlyChild(root);
    assertEquals("a", child.getRelativeName());
    PropertyNode grandChild = getOnlyChild(child);
    assertEquals("b", grandChild.getRelativeName());
    PropertyNode greatGrandChild = getOnlyChild(grandChild);
    assertEquals("c", greatGrandChild.getRelativeName());    
  }

  public void testNonGosuNamesNotInTree() {
    TestPropertySet propertySet = new TestPropertySet("a.23.c", "a.b", "a.x.##");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    PropertyNode child = getOnlyChild(root);
    assertEquals("test.set.a", child.getFullName());
    assertEquals(1, child.getChildren().size());
    PropertyNode grandChild = getOnlyChild(child);
    assertEquals("test.set.a.b", grandChild.getFullName());
    assertEquals(0, grandChild.getChildren().size());
    assertTrue(grandChild.hasValue());
  }

  public void testDollarNotAllowedInNodeName() {
    TestPropertySet propertySet = new TestPropertySet("$a", "b$");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    assertTrue(root.isLeaf());
  }

  public void testUseOnlyFirstOfMultipleNamesThatDifferOnlyInCase() {
    if( gw.config.CommonServices.getEntityAccess().getLanguageLevel().isStandard() ) {
      // OS Gosu is case sensitive :)
      return;
    }
    TestPropertySet propertySet = new TestPropertySet("a.Joe", "a.joe", "a.jOe", "a.JOE");
    PropertyNode root = PropertyNode.buildTree(propertySet);
    PropertyNode child = getOnlyChild(root);
    assertEquals("test.set.a", child.getFullName());
    assertEquals(1, child.getChildren().size());
    PropertyNode grandChild = getOnlyChild(child);
    assertEquals("test.set.a.JOE", grandChild.getFullName());
    assertEquals(0, grandChild.getChildren().size());
    assertTrue(grandChild.hasValue());
  }

  private PropertyNode getOnlyChild(PropertyNode node) {
    List<PropertyNode> children = node.getChildren();
    assertEquals(1, children.size());
    return children.get(0);
  }
}
