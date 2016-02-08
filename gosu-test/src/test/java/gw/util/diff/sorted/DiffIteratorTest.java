/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.diff.sorted;

import gw.test.TestClass;

import java.util.Arrays;
import java.util.NoSuchElementException;


public class DiffIteratorTest extends TestClass{

  public void testDiff() {
    String[] a = { "a", "b", "c",      "e", "f",      "h", "i", "j", "k", "l" };
    String[] b = {      "b", "c", "d", "e",      "g", "h",                "l" };
    DiffIterator<String> diffIterator = new DiffIterator<String>(Arrays.asList(a).iterator(), Arrays.asList(b).iterator());

    assertTrue(diffIterator.hasNext());

    Diff<String> diff = diffIterator.next();
    assertEquals("a", diff.getOldItem());
    assertNull(diff.getNewItem());

    try {
      diffIterator.remove();
      fail("Expected UnsupportedOperationException");
    }
    catch (UnsupportedOperationException ex) {
      /* ok */
    }

    diff = diffIterator.next();
    assertEquals("b", diff.getOldItem());
    assertEquals("b", diff.getNewItem());

    assertTrue(diffIterator.hasNext());
    diff = diffIterator.next();
    assertEquals("c", diff.getOldItem());
    assertEquals("c", diff.getNewItem());

    diff = diffIterator.next();
    assertNull(diff.getOldItem());
    assertEquals("d", diff.getNewItem());

    diff = diffIterator.next();
    assertEquals("e", diff.getOldItem());
    assertEquals("e", diff.getNewItem());

    assertTrue(diffIterator.hasNext());
    diff = diffIterator.next();
    assertEquals("f", diff.getOldItem());
    assertNull(diff.getNewItem());

    diff = diffIterator.next();
    assertNull(diff.getOldItem());
    assertEquals("g", diff.getNewItem());

    assertTrue(diffIterator.hasNext());
    diff = diffIterator.next();
    assertEquals("h", diff.getOldItem());
    assertEquals("h", diff.getNewItem());

    diff = diffIterator.next();
    assertEquals("i", diff.getOldItem());
    assertNull(diff.getNewItem());

    diff = diffIterator.next();
    assertEquals("j", diff.getOldItem());
    assertNull(diff.getNewItem());

    assertTrue(diffIterator.hasNext());
    diff = diffIterator.next();
    assertEquals("k", diff.getOldItem());
    assertNull(diff.getNewItem());

    diff = diffIterator.next();
    assertEquals("l", diff.getOldItem());
    assertEquals("l", diff.getNewItem());

    assertFalse(diffIterator.hasNext());

    try {
      diffIterator.next();
      fail("Expected NoSuchElementException");
    }
    catch (NoSuchElementException ex) {
      /* ok */
    }
  }

}
