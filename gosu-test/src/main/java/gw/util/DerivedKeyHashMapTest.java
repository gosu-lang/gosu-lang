/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.test.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: 9/9/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class DerivedKeyHashMapTest extends TestClass {

  private DerivedKeyHashMap<String, String> map(String... values) {
    DerivedKeyHashMap<String, String> result = new PrefixMap();
    for (String s : values) {
      result.put(s.substring(0, 3), s);
    }
    return result;
  }

  private HashMap<String, String> hashMap(String... values) {
    HashMap<String, String> map = new HashMap<String, String>();
    for (String s : values) {
      map.put(s.substring(0, 3), s);
    }
    return map;
  }

  // --------------------------- size() ----------------------------------------

  public void testSizeReturnsZeroForEmptyMap() {
    assertEquals(0, map().size());
  }

  public void testSizeReturnsSizeForMapWithKeys() {
    assertEquals(3, map("foo23", "bar12", "baz56").size());
  }

  // --------------------------- isEmpty() ------------------------------------

  public void testIsEmptyReturnsTrueForEmptyMap() {
    assertTrue(map().isEmpty());
  }

  public void testIsEmptyReturnsFalseForNonEmptyMap() {
    assertFalse(map("foo23", "bar12", "baz56").isEmpty());
  }

  // --------------------------- containsKey(Object) --------------------------

  public void testContainsKeyThrowsNullPointerExceptionForNullKey() {
    try {
      map("foo23", "bar12", "baz56").containsKey(null);
      fail("Expected a NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testContainsKeyReturnsFalseForEmptyMap() {
    assertFalse(map().containsKey("foo"));
  }

  public void testContainsKeyReturnsFalseForKeyNotInMap() {
    assertFalse(map("foo23", "bar12", "baz56").containsKey("other"));
  }

  public void testContainsKeyReturnsTrueForKeyInMap() {
    assertTrue(map("foo23", "bar12", "baz56").containsKey("foo"));
    assertTrue(map("foo23", "bar12", "baz56").containsKey("bar"));
    assertTrue(map("foo23", "bar12", "baz56").containsKey("baz"));
  }

  public void testContainsKeyReliesOnOverridenKeyMatchingFunction() {
    assertFalse(map("foo23", "bar12", "baz56").containsKey("foo23"));
  }

  // TODO - AHK
  // After creating it with the values in the constructor
  // After puts that cause the map to be resized
  // After compacting the map
  // After lots of removals
  // With a very large map

  // --------------------------- containsValue(Object) ----------------------------------

  public void testContainsValueThrowsNullPointerExceptionForNullValue() {
    try {
      map("foo23", "bar12", "baz56").containsValue(null);
      fail("Expected a NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testContainsValueReturnsFalseForEmptyMap() {
    assertFalse(map().containsValue("value"));
  }

  public void testContainsValueReturnsFalseForMapWithoutThatValue() {
    assertFalse(map("foo23", "bar12", "baz56").containsValue("other"));
  }

  public void testContainsValueReturnsFalseForArgumentThatMatchesAKeyButNotAValue() {
    assertFalse(map("foo23", "bar12", "baz56").containsValue("foo"));
  }

  public void testContainsValueReturnsTrueForArgumentThatMatchesAValue() {
    assertTrue(map("foo23", "bar12", "baz56").containsValue("foo23"));
  }

  private static class PrefixMap extends DerivedKeyHashMap<String, String> {
    @Override
    protected boolean keyMatches(Object key, String value) {
      return value.substring(0, 3).equals(key);
    }

    @Override
    protected String getKeyForValue(String value) {
      return value.substring(0, 3);
    }

    @Override
    protected double loadFactor() {
      return 0.75;
    }
  }

  // --------------------------- get(Object) ----------------------------------

  public void testGetThrowsNullPointerExceptionForNullKey() {
    try {
      map("foo23", "bar12", "baz56").get(null);
      fail("Expected a NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testGetReturnsNullForEmptyMap() {
    assertNull(map().get("foo"));
  }

  public void testGetReturnsNullForKeyNotInMap() {
    assertNull(map("foo23", "bar12", "baz56").get("other"));
  }

  public void testGetReturnsValueForKeyInMap() {
    assertEquals("foo23", map("foo23", "bar12", "baz56").get("foo"));
  }

  public void testGetReliesOnOverriddenKeyMatchingFunction() {
    assertNull(map("foo23", "bar12", "baz56").get("foo23"));
  }

  // --------------------------- put(K, V) ------------------------------------

  public void testPutWithNullKeyThrowsIllegalArgumentException() {
    try {
      map().put(null, "foo23");
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testPutWithNullValueThrowsIllegalArgumentException() {
    try {
      map().put("foo", null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testPutWithKeyThatDoesntMatchValueThrowsIllegalArgumentException() {
    try {
      map().put("f", "foo23");
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testPutWithNewKeyAddsKeyToMap() {
    DerivedKeyHashMap<String, String> map = map();
    assertNull(map.put("foo", "foo23"));
    assertContentsOfMap(map, "foo23");
  }

  public void testPutWithExistingKeyOverridesExistingValueInMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12");
    assertEquals("foo23", map.put("foo", "foo45"));
    assertContentsOfMap(map, "foo45", "bar12");
    assertFalse(map.containsValue("foo23"));
  }

  // --------------------------- remove(Object) -------------------------------

  public void testRemoveDoesNothingOnEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    assertNull(map.remove("other"));
    assertContentsOfMap(map);
  }

  public void testRemoveDoesNothingWithNullKey() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    assertNull(map.remove(null));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testRemoveDoesNothingIfMapDoesNotContainKey() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    assertNull(map.remove("other"));
    assertEquals(3, map.size());
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testRemoveRemovesKeyAndValue() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    assertEquals("foo23", map.remove("foo"));
    assertContentsOfMap(map, "bar12", "baz56");
    assertNotInMap(map, "foo23");
  }

  public void testRemoveReliesOnKeyMatchingRatherThanValue() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    assertNull(map.remove("foo23"));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testRemoveAndThenReAddWorksAsExpected() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    assertEquals("foo23", map.remove("foo"));
    assertContentsOfMap(map, "bar12", "baz56");
    assertNull(map.put("foo", "foo23"));
    assertEquals(3, map.size());
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  // ---------------------------- putAll() ------------------------------------

  public void testPutAllWithNullArgumentThrowsIllegalArgumentException() {
    try {
      map().putAll(null);
      fail("Expected an IllegalArgumentException");
    } catch (Exception e) {
      // Expected
    }
  }

  public void testPutAllWithEmptyMapHasNoAffectOnExistingKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    map.putAll(hashMap());
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testPutAllOnEmptyMapAddsKeys() {
    DerivedKeyHashMap<String, String> map = map();
    map.putAll(hashMap("foo23", "bar12", "baz56"));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testPutAllWithOnlyNewKeysAddsKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    map.putAll(hashMap("aaa1", "bbb2", "ccc3"));
    assertContentsOfMap(map, "foo23", "bar12", "baz56", "aaa1", "bbb2", "ccc3");
  }

  public void testPutAllWithOnlyExistingKeysUpdatesKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    map.putAll(hashMap("aaa1", "foo45", "ccc3"));
    assertContentsOfMap(map, "foo45", "bar12", "baz56", "aaa1", "ccc3");
  }

  public void testPutAllWithSomeNewAndSomeExistingKeysBothAddsAndUpdates() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    map.putAll(hashMap("foo45", "baz01"));
    assertContentsOfMap(map, "foo45", "bar12", "baz01");
  }

  public void testPutAllWithNullKeyValueThrowsIllegalArgumentExceptionBeforeDoingAnyUpdates() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    HashMap<String, String> hashMap = hashMap("aaa1", "bbb2");
    hashMap.put(null, "ccc3");
    try {
      map.putAll(hashMap);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
    assertNotInMap(map, "aaa1", "bbb2", "ccc3");
  }

  public void testPutAllWithNullValueThrowsIllegalArgumentExceptionBeforeDoingAnyUpdates() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    HashMap<String, String> hashMap = hashMap("aaa1", "bbb2");
    hashMap.put("ccc", null);
    try {
      map.putAll(hashMap);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
    assertNotInMap(map, "aaa1", "bbb2", "ccc3");
  }

  public void testPutAllWithKeyThatDoesntMatchValueThrowsIllegalArgumentExceptionBeforeDoingAnyUpdates() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    HashMap<String, String> hashMap = hashMap("aaa1", "bbb2");
    hashMap.put("ccc3", "ccc3");
    try {
      map.putAll(hashMap);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
    assertNotInMap(map, "aaa1", "bbb2", "ccc3");
  }

  // --------------------------- clear() --------------------------------------

  public void testClearOnEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    map.clear();
    assertContentsOfMap(map);
  }

  public void testClearOnMapRemovesAllEntries() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    map.clear();
    assertContentsOfMap(map);
    assertNotInMap(map, "foo23", "bar12", "baz56");
  }

  // --------------------------- keySet().add(K) ------------------------------

  public void testKeySetAddThrowsUnsupportedOperationException() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    try {
      map.keySet().add("bap");
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // --------------------------- keySet().addAll(Collection<K>) ---------------

  public void testKeySetAddAllThrowsUnsupportedOperationException() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    try {
      map.keySet().addAll(Arrays.asList("bap"));
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // --------------------------- keySet().clear() -----------------------------

  public void testKeySetClearOnEmptyMapDoesNothing() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    keySet.clear();
    assertEquals(0, keySet.size());
    assertContentsOfMap(map);
  }

  public void testKeySetClearClearsUnderlyingMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    keySet.clear();
    assertEquals(0, keySet.size());
    assertContentsOfMap(map);
  }

  // --------------------------- keySet().contains(Object) --------------------

  public void testKeySetContainsReturnsFalseForEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertFalse(keySet.contains("foo"));
  }

  public void testKeySetContainsReturnsFalseForKeyNotInMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.contains("other"));
  }

  public void testKeySetContainsReturnsTrueForKeyInMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertTrue(keySet.contains("foo"));
    assertTrue(keySet.contains("bar"));
    assertTrue(keySet.contains("baz"));
  }

  public void testKeySetContainsReliesOnOverriddenKeyMatching() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.contains("foo23"));
  }

  // --------------------------- keySet().containsAll(Collection) -------------

  public void testKeySetContainsAllReturnsTrueForEmptyCollectionAndEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertTrue(keySet.containsAll(Collections.<Object>emptyList()));
  }

  public void testKeySetContainsAllReturnsTrueForEmptyCollectionAndNonEmptyMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertTrue(keySet.containsAll(Collections.<Object>emptyList()));
  }

  public void testKeySetContainsAllThrowsNullPointerExceptionForNullArgument() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.containsAll(null);
      fail("Expected a NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testKeySetContainsAllThrowsNullPointerExceptionForNullElementOfCollectionArgument() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.containsAll(Arrays.asList("foo", null));
      fail("Expected a NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testKeySetContainsAllReturnsFalseForCollectionWithKeysAndEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertFalse(keySet.containsAll(Arrays.asList("foo", "bar")));
  }

  public void testKeySetContainsAllReturnsFalseForCollectionWithNoMatchingKeysInMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.containsAll(Arrays.asList("other", "notthere")));
  }

  public void testKeySetContainsAllReturnsFalseIfOnlySomeKeysAreInTheMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.containsAll(Arrays.asList("foo", "other")));
  }

  public void testKeySetContainsAllReturnsTrueIfAllKeysAreInTheMap() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertTrue(keySet.containsAll(Arrays.asList("foo", "bar")));
  }

  // --------------------------- keySet().isEmpty() ---------------------------

  public void testKeySetIsEmptyReturnsTrueIfMapIsEmpty() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertTrue(keySet.isEmpty());
  }

  public void testKeySetIsEmptyReturnsFalseIfMapHasKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.isEmpty());
  }

  // --------------------------- keySet().iterator() --------------------------

  public void testKeySetIteratorOnEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertIteratorContents(keySet.iterator());
  }

  public void testKeySetIteratorThrowsNoSuchElementExceptionIfAtEndOfIterator() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    try {
      keySet.iterator().next();
      fail("Expected a NoSuchElementException");
    } catch (NoSuchElementException e) {
      // Expected
    }
  }

  public void testKeySetIteratorOnMapWithKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertIteratorContents(keySet.iterator(), "foo", "bar", "baz");
  }

  public void testKeySetIteratorRemove() {
    // TODO - AHK
//    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
//    Set<String> keySet = map.keySet();
//    Iterator<String> it = keySet.iterator();
//    it.next();
//    it.remove();
//    assertContentsOfMap(map, "bar12", "baz56");
  }

  public void testKeySetIteratorRemoveThrowsIllegalStateExceptionIfNextHasNeverBeenCalled() {
    // TODO - AHK
//    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
//    Set<String> keySet = map.keySet();
//    try {
//      keySet.iterator().remove();
//      fail("Expected an IllegalStateException to be thrown");
//    } catch (IllegalStateException e) {
//      // Expected
//    }
  }

  public void testKeySetIteratorRemoveThrowsIllegalStateExceptionIfCalledTwiceInARow() {
    // TODO - AHK
//    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
//    Set<String> keySet = map.keySet();
//    Iterator<String> it = keySet.iterator();
//    it.next();
//    it.next();
//    it.remove();
//    try {
//      it.remove();
//      fail("Expected an IllegalStateException to be thrown");
//    } catch (IllegalStateException e) {
//      // Expected
//    }
  }

  public void testKeySetIteratorOnLargeMap() {

  }
  // TODO

  // --------------------------- keySet().remove(Object) ----------------------

  public void testKeySetRemoveDoesNothingOnEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertFalse(keySet.remove("other"));
    assertContentsOfMap(map);
  }

  // TODO - AHK - Maybe this should throw?
  public void testKeySetRemoveDoesNothingWithNullKey() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.remove(null));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRemoveDoesNothingIfMapDoesNotContainKey() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.remove("other"));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRemoveRemovesKeyAndValue() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertTrue(keySet.remove("foo"));
    assertContentsOfMap(map, "bar12", "baz56");
    assertNotInMap(map, "foo23");
  }

  // --------------------------- keySet().removeAll(Collection) ---------------

  public void testKeySetRemoveAllThrowsNPEIfArgIsNull() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.removeAll(null);
      fail("Expected an NPE");
    } catch (NullPointerException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRemoveAllThrowsNPEBeforeModifyingTheSetIfAnyElementInArgIsNull() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.removeAll(Arrays.asList("foo", "bar", null));
      fail("Expected an NPE");
    } catch (NullPointerException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRemoveAllReturnsFalseAndDoesNothingToEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertFalse(keySet.removeAll(Arrays.asList("foo", "bar")));
    assertContentsOfMap(map);
  }

  public void testKeySetRemoveAllRemovesAllMatchingKeys() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertTrue(keySet.removeAll(Arrays.asList("foo", "bar")));
    assertContentsOfMap(map, "baz56");
    assertNotInMap(map, "foo23", "bar12");
  }

  // --------------------------- keySet().retainAll(Collection) ---------------

  public void testKeySetRetainAllThrowsNPEIfArgIsNull() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.retainAll(null);
      fail("Expected an NPE");
    } catch (NullPointerException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRetainAllThrowsNPEBeforeModifyingTheSetIfAnyElementInArgIsNull() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    try {
      keySet.retainAll(Arrays.asList("foo", "bar", null));
      fail("Expected an NPE");
    } catch (NullPointerException e) {
      // Expected
    }
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRetainAllDoesNothingToEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertFalse(keySet.retainAll(Arrays.asList("foo", "bar")));
    assertContentsOfMap(map);
  }

  public void testKeySetRetainAllDoesNothingIfAllKeysAreRetained() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertFalse(keySet.retainAll(Arrays.asList("foo", "bar", "baz")));
    assertContentsOfMap(map, "foo23", "bar12", "baz56");
  }

  public void testKeySetRetainAllRemovesNonRetainedKeysFromMap() {
    // TODO - AHK
//    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
//    Set<String> keySet = map.keySet();
//    assertTrue(keySet.retainAll(Arrays.asList("foo", "bar")));
//    assertContentsOfMap(map, "foo23", "bar12");
//    assertNotInMap(map, "baz56");
  }

  // --------------------------- keySet().size() ------------------------------

  public void testKeySetSizeReturnsZeroForEmptyMap() {
    DerivedKeyHashMap<String, String> map = map();
    Set<String> keySet = map.keySet();
    assertEquals(0, keySet.size());
  }

  public void testKeySetSizeReturnsProperValueForMapWithValuesInIt() {
    DerivedKeyHashMap<String, String> map = map("foo23", "bar12", "baz56");
    Set<String> keySet = map.keySet();
    assertEquals(3, keySet.size());
  }

  // --------------------------- keySet().toArray() ---------------------------

  // TODO

  // --------------------------- keySet().toArray(Object[]) -------------------

  // TODO

  // TODO - Test that changes to the map update the KeySet in real time
  // TODO - Test concurrent modification during iteration

  // --------------------------- values().add(K) ------------------------------

  // TODO

  // --------------------------- values().addAll(Collection<K>) ---------------

  // TODO

  // --------------------------- values().clear() -----------------------------

  // TODO

  // --------------------------- values().contains(Object) --------------------

  // TODO

  // --------------------------- values().containsAll(Collection) -------------

  // TODO

  // --------------------------- values().isEmpty() ---------------------------

  // TODO

  // --------------------------- values().iterator() --------------------------

  // TODO

  // --------------------------- values().remove(Object) ----------------------

  // TODO

  // --------------------------- values().removeAll(Collection) ---------------

  // TODO

  // --------------------------- values().retainAll(Collection) ---------------

  // TODO

  // --------------------------- values().size() ------------------------------

  // TODO

  // --------------------------- values().toArray() ---------------------------

  // TODO

  // --------------------------- values().toArray(Object[]) -------------------

  // TODO

  // --------------------------- entrySet().add(K) ----------------------------

  // TODO

  // --------------------------- entrySet().addAll(Collection<K>) -------------

  // TODO

  // --------------------------- entrySet().clear() ---------------------------

  // TODO

  // --------------------------- entrySet().contains(Object) ------------------

  // TODO

  // --------------------------- entrySet().containsAll(Collection) -----------

  // TODO

  // --------------------------- entrySet().isEmpty() -------------------------

  // TODO

  // --------------------------- entrySet().iterator() ------------------------

  // TODO

  // --------------------------- entrySet().remove(Object) --------------------

  // TODO

  // --------------------------- entrySet().removeAll(Collection) -------------

  // TODO

  // --------------------------- entrySet().retainAll(Collection) -------------

  // TODO

  // --------------------------- entrySet().size() ----------------------------

  // TODO

  // --------------------------- entrySet().toArray() -------------------------

  // TODO

  // --------------------------- entrySet().toArray(Object[]) -----------------

  // TODO

  // --------------------------- Compound/Large Size Tests --------------------

  public void testLotsOfAddsAndRemovesOnLargeMap() {
    List<String> values = new ArrayList<String>();
    for (int i = 0; i < 1000; i++) {
      values.add(i + "aaa");
    }

    DerivedKeyHashMap<String, String> map = map();
    for (String v : values) {
      map.put(v.substring(0, 3), v);
    }
    assertContentsOfMap(map, values.toArray(new String[values.size()]));

    for (int i = 0; i < values.size(); i++) {
      map.remove(values.get(i).substring(0, 3));
      assertEquals(values.size() - (i + 1), map.size());
    }
    assertContentsOfMap(map);

    for (String v : values) {
      map.put(v.substring(0, 3), v);
    }
    assertContentsOfMap(map, values.toArray(new String[values.size()]));
  }

  public void testLotsOfAddsAndRemovesOnLargeMapViaKeySetIterator() {
    List<String> values = new ArrayList<String>();
    List<String> keys = new ArrayList<String>();
    for (int i = 0; i < 1000; i++) {
      values.add(i + "aaa");
      keys.add((i + "aaa").substring(0, 3));
    }

    DerivedKeyHashMap<String, String> map = map();
    for (String v : values) {
      map.put(v.substring(0, 3), v);
    }
    assertContentsOfMap(map, values.toArray(new String[values.size()]));

    assertIteratorContents(map.keySet().iterator(), keys.toArray(new String[keys.size()]));
    Iterator<String> it = map.keySet().iterator();
    while (it.hasNext()) {
      it.next();
      it.remove();
    }

    // TODO - AHK
//    assertContentsOfMap(map);
  }

  // --------------------------- Private Helper Methods -----------------------

  private void assertContentsOfMap(DerivedKeyHashMap<String, String> map, String... values) {
    assertEquals(values.length, map.size());
    if (values.length == 0) {
      assertTrue(map.isEmpty());
    }else {
      assertFalse(map.isEmpty());
    }

    for (String value : values) {
      assertTrue(map.containsKey(value.substring(0, 3)));
      assertFalse(map.containsKey(value));
      assertEquals(value, map.get(value.substring(0, 3)));
      assertTrue(map.containsValue(value));
    }

    assertFalse(map.containsKey("other"));
    assertNull(map.get("other"));
    assertFalse(map.containsValue("other"));
  }

  private void assertNotInMap(DerivedKeyHashMap<String, String> map, String... values) {
    for (String value : values) {
      assertFalse(map.containsKey(value.substring(0, 3)));
      assertNull(map.get(value.substring(0, 3)));
      assertFalse(map.containsValue(value));
    }
  }

  private void assertIteratorContents(Iterator<String> iterator, String... values) {
    Set<String> iteratorContents = new HashSet<String>();
    for (int i = 0; i < values.length; i++) {
      assertTrue(iterator.hasNext());
      iteratorContents.add(iterator.next());
    }
    assertFalse(iterator.hasNext());
    assertCollectionEquals(iteratorContents, Arrays.asList(values));
  }

  /*

  @Override
  public Set<Entry<K, V>> entrySet() {
    // TODO - AHK
    return null;
  }

  public Set<K> keySet() {
    Set<K> keys = new HashSet<K>();
    for (int i = 0; i < _table.length; i++) {
      if (_table[i] != null) {
        if (_table[i] instanceof ChainedEntry) {
          ChainedEntry<V> entry = (ChainedEntry) _table[i];
          keys.add(getKeyForValue(entry._entry));
          while (entry._next instanceof ChainedEntry) {
            entry = (ChainedEntry<V>) entry._next;
            keys.add(getKeyForValue(entry._entry));
          }
          keys.add(getKeyForValue((V) entry._next));
        } else {
          keys.add(getKeyForValue((V)_table[i]));
        }
      }
    }

    return keys;
  }

  public Collection<V> values() {
    List<V> values = new ArrayList<V>();
    for (int i = 0; i < _table.length; i++) {
      if (_table[i] != null) {
        if (_table[i] instanceof ChainedEntry) {
          ChainedEntry<V> entry = (ChainedEntry) _table[i];
          values.add(entry._entry);
          while (entry._next instanceof ChainedEntry) {
            entry = (ChainedEntry<V>) entry._next;
            values.add(entry._entry);
          }
          values.add((V) entry._next);
        } else {
          values.add((V)_table[i]);
        }
      }
    }

    return values;
  }*/
}
