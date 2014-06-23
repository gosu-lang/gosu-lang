/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import java.util.Arrays;

import junit.framework.AssertionFailedError;

public class TestClassTest extends TestClass {

  public void testAssertArrayEquals() {
    assertArrayEquals( new Object[]{}, new Object[]{} );
    assertArrayEquals( new Object[]{"a"}, new Object[]{"a"} );
    assertArrayEquals( new Object[]{"a", "b"}, new Object[]{"a", "b"} );
    try {
      assertArrayEquals( new Object[]{"a"}, new Object[]{"b"} );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertArrayEquals( new Object[]{"a"}, new Object[]{} );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertArrayEquals( new Object[]{}, new Object[]{"a"} );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertArrayEquals( new Object[]{"a", "a"}, new Object[]{"a"} );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertArrayEquals( new Object[]{"a", "b"}, new Object[]{"b", "a"} );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
  }

  public void testAssertCollectionEquals() {
    assertCollectionEquals( Arrays.asList(), Arrays.asList() );
    assertCollectionEquals( Arrays.asList("a"), Arrays.asList("a") );
    assertCollectionEquals( Arrays.asList("a", "b"), Arrays.asList("a", "b") );
    try {
      assertCollectionEquals( Arrays.asList("a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertCollectionEquals( Arrays.asList("a"), Arrays.asList() );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertCollectionEquals( Arrays.asList(), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertCollectionEquals( Arrays.asList("a", "a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertCollectionEquals( Arrays.asList("a", "b"), Arrays.asList("b", "a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
  }

  public void testAssertListEquals() {
    assertListEquals( Arrays.asList(), Arrays.asList() );
    assertListEquals( Arrays.asList("a"), Arrays.asList("a") );
    assertListEquals( Arrays.asList("a", "b"), Arrays.asList("a", "b") );
    try {
      assertListEquals( Arrays.asList("a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertListEquals( Arrays.asList("a"), Arrays.asList() );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertListEquals( Arrays.asList(), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertListEquals( Arrays.asList("a", "a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertListEquals( Arrays.asList("a", "b"), Arrays.asList("b", "a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
  }

  public void testAssertIterableEquals() {
    assertIterableEquals( Arrays.asList(), Arrays.asList() );
    assertIterableEquals( Arrays.asList("a"), Arrays.asList("a") );
    assertIterableEquals( Arrays.asList("a", "b"), Arrays.asList("a", "b") );
    try {
      assertIterableEquals( Arrays.asList("a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEquals( Arrays.asList("a"), Arrays.asList() );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEquals( Arrays.asList(), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEquals( Arrays.asList("a", "a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEquals( Arrays.asList("a", "b"), Arrays.asList("b", "a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
  }

  public void testAssertIterableEqualsIgnoreOrder() {
    assertIterableEqualsIgnoreOrder( Arrays.asList(), Arrays.asList() );
    assertIterableEqualsIgnoreOrder( Arrays.asList("a"), Arrays.asList("a") );
    assertIterableEqualsIgnoreOrder( Arrays.asList("a", "b"), Arrays.asList("a", "b") );
    assertIterableEqualsIgnoreOrder( Arrays.asList("a", "b"), Arrays.asList("b", "a") );
    try {
      assertIterableEqualsIgnoreOrder( Arrays.asList("a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEqualsIgnoreOrder( Arrays.asList("a"), Arrays.asList() );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEqualsIgnoreOrder( Arrays.asList(), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
    try {
      assertIterableEqualsIgnoreOrder( Arrays.asList("a", "a"), Arrays.asList("a") );
      fail( "Shouldn't have been equal" );
    } catch( AssertionFailedError e ) {}
  }

}
