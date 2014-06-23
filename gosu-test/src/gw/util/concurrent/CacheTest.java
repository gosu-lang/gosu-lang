/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/* Guidewire Software
 *
 * Creator information:
 * User: <YOUR-USERID>
 * Date: Tue May 26 17:06:32 PDT 2009
 *
 * Revision information:
 */

package gw.util.concurrent;

import gw.test.TestClass;

/**
 * Tests the {@link Cache} implementation.
 *
 * @author <YOUR-NAME>
 */
public class CacheTest extends TestClass {

  final static String[] LOAD = { "Zero", "One", "Two", "Three", "Four" };
  final static Integer ZERO = 0;
  final static Integer ONE = 1;
  final static Integer TWO = 2;
  final static Integer THREE = 3;
  final static Integer FOUR = 4;

  public void testCache() {
    Cache<Integer,String> cache = new Cache<Integer,String>("testCache", 2, new Cache.MissHandler<Integer,String>() {
     @Override
     public String load(Integer key) {
       return LOAD[key];
     }
   });
    assertEquals(0, cache.getUtilizedSize());

    int counts[] = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(1, cache.getUtilizedSize());
    assertMiss(counts, cache);
    assertEquals(1, cache.getUtilizedSize());

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(2, cache.getRequests());
//    assertEquals(1, cache.getSize());
    assertEquals(2, cache.getRequests());
    assertEquals(1, cache.getUtilizedSize());
    assertHit(counts, cache);
    assertEquals(1, cache.getUtilizedSize());

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(2, cache.getUtilizedSize());
    assertMiss(counts, cache);
    // @KnownBreak(jira="PL-65090", targetBranch = "eng/pl/carbon/active/entity", targetUser = "dandrews")
//    assertEquals(1, cache.getUtilizedSize());

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(2, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(2, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[2], cache.get(TWO));
    assertEquals(2, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[2], cache.get(TWO));
    // @KnownBreak(jira="PL-65090", targetBranch = "eng/pl/carbon/active/entity", targetUser = "dandrews")
//    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(2, cache.getUtilizedSize());
    // @KnownBreak(jira="PL-65090", targetBranch = "eng/pl/carbon/active/entity", targetUser = "dandrews")
//    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    // @KnownBreak(jira="PL-65090", targetBranch = "eng/pl/carbon/active/entity", targetUser = "dandrews")
//    assertHit(counts, cache);
  }

  public void testCacheOverlookOccasionalMiss() {
    Cache<Integer,String> cache = new Cache<Integer,String>("testCache", 2, new Cache.MissHandler<Integer,String>() {
     @Override
     public String load(Integer key) {
       return LOAD[key];
     }
   });
    assertEquals(0, cache.getUtilizedSize());

    int counts[] = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(1, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(1, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(2, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(2, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(2, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[2], cache.get(TWO));
    assertEquals(2, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[2], cache.get(TWO));
//    assertHit(counts, cache); // this should have been a hit

    counts = getCounts(cache);
    assertEquals(LOAD[3], cache.get(THREE));
    assertEquals(2, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[3], cache.get(THREE));
    assertHit(counts, cache);
  }

  public void testRequestIncrementRegardlessOfHitOrMiss() {
    Cache<Integer,String> cache = new Cache<Integer,String>("testCache", 1, new Cache.MissHandler<Integer,String>() {
     @Override
     public String load(Integer key) {
       return LOAD[key];
     }
   });
    int counts[] = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(1, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[0], cache.get(ZERO));
    assertEquals(1, cache.getUtilizedSize());
    assertHit(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(1, cache.getUtilizedSize());
    assertMiss(counts, cache);

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(1, cache.getUtilizedSize());
//    assertHit(counts, cache); // this should have been a hit

    counts = getCounts(cache);
    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(1, cache.getUtilizedSize());
    assertHit(counts, cache);
  }

  public void testManualEviction() {
    Cache<Integer,String> cache = new Cache<Integer,String>("testCache", 1, new Cache.MissHandler<Integer,String>() {
     @Override
     public String load(Integer key) {
       return LOAD[key];
     }
   });
    assertEquals(LOAD[2], cache.get(TWO));
    assertEquals(1, cache.getUtilizedSize());
    assertEquals(1, cache.getMisses());
    assertEquals(0, cache.getHits());

    cache.evict(ZERO);
    assertEquals(1, cache.getUtilizedSize());
    assertEquals(1, cache.getMisses());
    assertEquals(0, cache.getHits());

    cache.evict(TWO);
    assertEquals(0, cache.getUtilizedSize());
    assertEquals(1, cache.getMisses());
    assertEquals(0, cache.getHits());

    assertEquals(LOAD[2], cache.get(TWO));
    assertEquals(1, cache.getUtilizedSize());
    assertEquals(2, cache.getMisses());
    assertEquals(0, cache.getHits());
  }

  public void testManualPut() {
    Cache<Integer,String> cache = new Cache<Integer,String>("testCache", 1, new Cache.MissHandler<Integer,String>() {
     @Override
     public String load(Integer key) {
       return LOAD[key];
     }
   });
    cache.put(ONE, LOAD[1]);
    assertEquals(1, cache.getUtilizedSize());
    assertEquals(0, cache.getMisses());
    assertEquals(0, cache.getHits());

    assertEquals(LOAD[1], cache.get(ONE));
    assertEquals(1, cache.getUtilizedSize());
    assertEquals(0, cache.getMisses());
    assertEquals(1, cache.getHits());
  }

  private void assertHit(int[] counts, Cache cache) {
    assertEquals(counts[0] + 1, cache.getRequests());
    assertEquals(counts[1], cache.getMisses());
    assertEquals(counts[2] + 1, cache.getHits());
  }

  private void assertMiss(int[] counts, Cache cache) {
    assertEquals(counts[0] + 1, cache.getRequests());
    assertEquals(counts[1] + 1, cache.getMisses());
    assertEquals(counts[2], cache.getHits());
  }

  private int[] getCounts(Cache cache) {
    int counts[] = new int[3];
    counts[0] = cache.getRequests();
    counts[1] = cache.getMisses();
    counts[2] = cache.getHits();
    return counts;
  }

}
