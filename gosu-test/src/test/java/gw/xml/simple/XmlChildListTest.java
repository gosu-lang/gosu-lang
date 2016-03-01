/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.test.TestClass;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jun 18, 2010
 * Time: 7:43:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class XmlChildListTest extends TestClass {

  private SimpleXmlNode _parent;
  private XmlChildList<SimpleXmlNode> _list;
  private List<SimpleXmlNode> _originalChildren;
  private SimpleXmlNode _child0;
  private SimpleXmlNode _child1;
  private SimpleXmlNode _child2;

  public void testSizeDelegates() {
    createList(0);
    assertEquals(0, _list.size());
    createList(2);
    assertEquals(2, _list.size());
  }

  public void testIsEmptyDelegates() {
    createList(0);
    assertEquals(true, _list.isEmpty());
    createList(2);
    assertEquals(false, _list.isEmpty());
  }

  public void testContainsDelegates() {
    createList(1);
    assertTrue(_list.contains(_child0));
    assertFalse(_list.contains(node()));
  }

  public void testIteratorBasics() {
    createList(3);
    Iterator<SimpleXmlNode> it = _list.iterator();
    assertTrue(it.hasNext());
    assertSame(_child0, it.next());
    assertTrue(it.hasNext());
    assertSame(_child1, it.next());
    assertTrue(it.hasNext());
    assertSame(_child2, it.next());
    assertFalse(it.hasNext());
  }

  public void testIteratorRemoveMethodUnsetsParentOnElement() {
    createList(3);
    Iterator<SimpleXmlNode> it = _list.iterator();
    assertSame(_child0, it.next());
    assertSame(_child1, it.next());
    it.remove();
    assertSame(_child2, it.next());
    assertNull(_child1.getParent());
  }

  public void testToArrayDelegates() {
    createList(2);
    Object[] arr = _list.toArray();
    assertSame(_child0, arr[0]);
    assertSame(_child1, arr[1]);
  }

  public void testToArrayWithArgDelegates() {
    createList(2);
    SimpleXmlNode[] arr = _list.toArray(new SimpleXmlNode[2]);
    assertSame(_child0, arr[0]);
    assertSame(_child1, arr[1]);
  }

  public void testAddSetsParentAndAddsToList() {
    createList(0);
    SimpleXmlNode child = node();
    _list.add(child);
    assertEquals(1, _list.size());
    assertSame(child, _list.get(0));
    assertSame(_parent, child.getParent());
  }

  public void testAddThrowsIllegalArgumentExceptionIfElementHasNonNullParent() {
    createList(1);
    SimpleXmlNode newParent = node();
    try {
      newParent.getChildren().add(_list.get(0));
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testAddThrowsIfArgumentIsNull() {
    createList(1);
    try {
      _list.add(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testRemoveDoesNothingIfElementIsNotPartOfThisList() {
    createList(1);
    SimpleXmlNode newParent = node();
    assertFalse(newParent.getChildren().remove(_child0));
    assertSame(_parent, _child0.getParent());
  }

  public void testRemoveUnsetsParentPointerAndRemovesFromList() {
    createList(1);
    assertTrue(_list.remove(_child0));
    assertEquals(0, _list.size());
    assertNull(_child0.getParent());
  }

  public void testContainsAllDelegates() {
    createList(2);
    assertTrue(_list.containsAll(_originalChildren));
    assertFalse(_list.containsAll(list(node())));
  }

  public void testAddAllThrowsAndDoesNothingElseIfAnyElementHasANonNullParent() {
    createList(2);

    SimpleXmlNode newNode = node();
    try {
      _list.addAll(list(newNode, _child0));
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertEquals(2, _list.size());
    assertNull(newNode.getParent());
  }

  public void testAddAllThrowsAndDoesNothingElseIfAnyElementIsNull() {
    createList(2);

    SimpleXmlNode newNode = node();
    try {
      _list.addAll(list(newNode, null));
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertEquals(2, _list.size());
    assertNull(newNode.getParent());
  }

  public void testAddAllAddsAllNodesAndSetsAllParents() {
    createList(2);

    SimpleXmlNode newNode1 = node();
    SimpleXmlNode newNode2 = node();
    _list.addAll(list(newNode1, newNode2));
    assertEquals(4, _list.size());
    assertSame(newNode1, _list.get(2));
    assertSame(newNode2, _list.get(3));
    assertSame(_parent, newNode1.getParent());
    assertSame(_parent, newNode2.getParent());
  }

  public void testAddAllWithIndexThrowsAndDoesNothingElseIfAnyElementHasANonNullParent() {
    createList(2);

    SimpleXmlNode newNode = node();
    try {
      _list.addAll(1, list(newNode, _child0));
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertEquals(2, _list.size());
    assertNull(newNode.getParent());
  }

  public void testAddAllWithIndexThrowsAndDoesNothingElseIfAnyElementIsNull() {
    createList(2);

    SimpleXmlNode newNode = node();
    try {
      _list.addAll(1, list(newNode, null));
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    assertEquals(2, _list.size());
    assertNull(newNode.getParent());
  }

  public void testAddAllWithIndexAddsAllNodesAndSetsAllParents() {
    createList(2);

    SimpleXmlNode newNode1 = node();
    SimpleXmlNode newNode2 = node();
    _list.addAll(1, list(newNode1, newNode2));
    assertEquals(4, _list.size());
    assertSame(newNode1, _list.get(1));
    assertSame(newNode2, _list.get(2));
    assertSame(_parent, newNode1.getParent());
    assertSame(_parent, newNode2.getParent());
  }

  public void testRemoveAllReturnsFalseIfListDidntChange() {
    createList(0);
    assertFalse(_list.removeAll(list(node())));
  }

  public void testRemoveAllRemovesParentsFromNodesInThisList() {
    createList(2);
    assertTrue(_list.removeAll(list(node(), _child0)));
    assertEquals(1, _list.size());
    assertSame(_child1, _list.get(0));
    assertNull(_child0.getParent());
  }

  public void testRemoveAllDoesNotAlterNodesNotInThisList() {
    createList(2);
    assertFalse(node().getChildren().removeAll(_originalChildren));
    assertSame(_parent, _list.get(0).getParent());
    assertSame(_parent, _list.get(1).getParent());
  }

  public void testRetainAllReturnsFalseIfListDidntChange() {
    createList(2);
    assertFalse(_list.retainAll(_originalChildren));
  }

  public void testRetainAllDoesNotAlterMembersNotInTheList() {
    createList(2);
    assertFalse(node().getChildren().retainAll(_originalChildren));
    assertSame(_parent, _list.get(0).getParent());
    assertSame(_parent, _list.get(1).getParent());
  }

  public void testRetainAllRemovesNodesNotInTheListAndNullsOutTheirParent() {
    createList(2);
    assertTrue(_list.retainAll(list(_child1)));
    assertEquals(1, _list.size());
    assertSame(_child1, _list.get(0));
    assertNull(_child0.getParent());
  }

  public void testClearNullsOutParentPointersAndClearsList() {
    createList(2);
    _list.clear();
    assertEquals(0, _list.size());
    assertNull(_child0.getParent());
    assertNull(_child1.getParent());
  }

  public void testGetDelegates() {
    createList(3);
    assertEquals(_child1, _list.get(1));
  }

  public void testSetNullsOutParentOnPreviousChildAndSetsParentOnNewChild() {
    createList(2);
    SimpleXmlNode newNode = node();
    assertSame(_child0, _list.set(0, newNode));
    assertNull(_child0.getParent());
    assertSame(newNode, _list.get(0));
    assertSame(_parent, newNode.getParent());
    assertEquals(2, _list.size());
  }
  
  public void testSetThrowsIfNodeAlreadyHasParentSetToDifferentNode() {
    createList(2);
    SimpleXmlNode newNode = node();
    node().getChildren().add(newNode);
    newNode.getChildren().add(node());
    try {
      _list.set(0, newNode);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected  
    }
  }
  
  public void testSetDoesNotThrowIfNodeAlreadyHasSameParent() {
    createList(2);
    _list.set(0, _child1);
    _list.set(1, _child0);
    
    assertSame(_child1, _list.get(0));
    assertSame(_child0, _list.get(1));
  }

  public void testSetThrowsIfArgumentIsNull() {
    createList(2);
    try {
      _list.set(0, null);
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testAddAddsAndSetsParent() {
    createList(2);
    SimpleXmlNode newNode = node();
    _list.add(0, newNode);
    assertSame(newNode, _list.get(0));
    assertSame(_child0, _list.get(1));
    assertSame(_child1, _list.get(2));
    assertSame(_parent, newNode.getParent());
  }

  public void testAddThrowsIllegalArgumentExceptionIfNodeAlreadyHasParent() {
    createList(2);
    try {
      node().getChildren().add(0, _child0);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testAddThrowsIllegalArgumentExceptionIfArgIsNull() {
    createList(2);
    try {
      _list.add(0, null);
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testRemoveNullsOutParentOnRemovedElement() {
    createList(2);
    assertSame(_child0, _list.remove(0));
    assertNull(_child0.getParent());
    assertEquals(1, _list.size());
    assertSame(_child1, _list.get(0));
  }

  public void testIndexOfDelegates() {
    createList(2);
    assertEquals(0, _list.indexOf(_child0));
    assertEquals(1, _list.indexOf(_child1));
  }

  public void testLastIndexOfDelegates() {
    createList(2);
    assertEquals(0, _list.lastIndexOf(_child0));
    assertEquals(1, _list.lastIndexOf(_child1));
  }

  public void testListIteratorAddThrowsIfElementIsNull() {
    createList(2);
    try {
      _list.listIterator().add(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testListIteratorAddThrowsIfElementAlreadyHasNonNullParent() {
    createList(2);

    try {
      _list.listIterator().add(nodeWithParent());
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testListIteratorAddSetsParent() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    SimpleXmlNode newNode = node();
    lit.add(newNode);
    assertEquals(3, _list.size());
    assertSame(_child0, _list.get(0));
    assertSame(newNode, _list.get(1));
    assertSame(_child1, _list.get(2));
    assertSame(_parent, newNode.getParent());
  }

  public void testListIteratorRemoveNullsOutParent() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    lit.remove();
    assertEquals(1, _list.size());
    assertSame(_child1, _list.get(0));
    assertNull(_child0.getParent());
  }

  public void testListIteratorSetThrowsIfElementIsNull() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    try {
      lit.set(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testListIteratorSetThrowsIfElementHasADifferentParent() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    try {
      lit.set(nodeWithParent());
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testListIteratorSetIsLegalIfElementAlreadyHasThisParent() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    lit.set(_child1);
    assertEquals(2, _list.size());
    assertSame(_child1, _list.get(0));
    assertSame(_child1, _list.get(1));
    assertNull(_child0.getParent());
  }

  public void testListIteratorSetSetsParent() {
    createList(2);
    ListIterator<SimpleXmlNode> lit = _list.listIterator();
    lit.next();
    SimpleXmlNode newNode = node();
    lit.set(newNode);
    assertEquals(2, _list.size());
    assertSame(newNode, _list.get(0));
    assertSame(_child1, _list.get(1));
    assertNull(_child0.getParent());
    assertSame(_parent, newNode.getParent());
  }

  public void testOtherListIteratorStuff() {
    // TODO - AHK
  }

  public void testListIteratorStartingAtIndex() {
    // TODO - AHK
  }

  public void testSubList() {
    // TODO - AHK
  }

  // ------------------------- Private Helpers

  private void createList(int numChildren) {
    _parent = new SimpleXmlNode("root");
    _list = (XmlChildList<SimpleXmlNode>) _parent.getChildren();
    _originalChildren = new ArrayList<SimpleXmlNode>();
    for (int i = 0; i < numChildren; i++) {
      SimpleXmlNode child = new SimpleXmlNode("child" + numChildren);
      _list.add(child);
      _originalChildren.add(child);
      if (i == 0) {
        _child0 = child;
      } else if (i == 1) {
        _child1 = child;
      } else if (i == 2) {
        _child2 = child;
      }
    }
  }

  private SimpleXmlNode node() {
    return new SimpleXmlNode("node");
  }

  private SimpleXmlNode nodeWithParent() {
    SimpleXmlNode parent = node();
    SimpleXmlNode child = node();
    parent.getChildren().add(child);
    return child;
  }

  private <T> List<T> list(T... args) {
    return new ArrayList<T>(Arrays.asList(args));
  }
}
