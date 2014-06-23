/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import gw.test.TestClass;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 */
public class IntegerIntervalTest extends TestClass
{
  public void testGoodLeftGoodRight()
  {
    IntegerInterval ivl = new IntegerInterval( 1, 10 );
    assertEquals( (Integer)1, ivl.getLeftEndpoint() );
    assertEquals( (Integer)10, ivl.getRightEndpoint() );
    assertEquals( (Integer)1, ivl.getStep() );
  }

  public void testClosedEndpoints()
  {
    IntegerInterval ivl = new IntegerInterval( 1, 10 );
    assertEquals( true, ivl.isLeftClosed() );
    assertEquals( true, ivl.isRightClosed() );
  }

  public void testBadLeftGoodRightFails()
  {
    try
    {
      new IntegerInterval( null, 10 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGoodLeftBadRightFails()
  {
    try
    {
      new IntegerInterval( 1, null );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testBadLeftBadRightFails()
  {
    try
    {
      new IntegerInterval( null, null );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testLeftGreaterThanRightFails()
  {
    try
    {
      new IntegerInterval( 10, 1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testLeftEqualsRight()
  {
    IntegerInterval ivl = new IntegerInterval( 1, 1 );
    assertEquals( (Integer)1, ivl.getLeftEndpoint() );
    assertEquals( (Integer)1, ivl.getRightEndpoint() );
  }

  public void testLeftEqualsRightEqualsZero()
  {
    IntegerInterval ivl = new IntegerInterval( 0, 0 );
    assertEquals( (Integer)0, ivl.getLeftEndpoint() );
    assertEquals( (Integer)0, ivl.getRightEndpoint() );
  }

  public void testLeftNegRightPos()
  {
    IntegerInterval ivl = new IntegerInterval( -1, 1 );
    assertEquals( (Integer)(-1), ivl.getLeftEndpoint() );
    assertEquals( (Integer)1, ivl.getRightEndpoint() );
  }

  public void testLeftNegRightNeg()
  {
    IntegerInterval ivl = new IntegerInterval( -3, -1 );
    assertEquals( (Integer)(-3), ivl.getLeftEndpoint() );
    assertEquals( (Integer)(-1), ivl.getRightEndpoint() );
  }

  public void testGoodLeftGoodRightWithStep()
  {
    IntegerInterval ivl = new IntegerInterval( 1, 10, 2 );
    assertEquals( (Integer)1, ivl.getLeftEndpoint() );
    assertEquals( (Integer)10, ivl.getRightEndpoint() );
    assertEquals( (Integer)2, ivl.getStep() );
  }

  public void testGoodLeftGoodRightZeroStepFails()
  {
    try
    {
      new IntegerInterval( 1, 10, 0 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGoodLeftGoodRightNegativeStepFails()
  {
    try
    {
      new IntegerInterval( 1, 10, -1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGetFromLeftStep1()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3 );

    for( int i = 0; i <= 6; i++ )
    {
      Integer e = ivl.getFromLeft( i );
      assertEquals( (Integer)(-3 + i), e );
    }
  }

  public void testGetFromLeftStep1NegStepIndex()
  {
    try
    {
      IntegerInterval ivl = new IntegerInterval( -3, 3 );
      ivl.getFromLeft( -1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGetFromLeftStep1OutOfBoundsStepIndex()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3 );

    Integer e = ivl.getFromLeft( 7 );
    assertEquals( null, e );
  }
  

  public void testGetFromRightStep1()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3 );

    for( int i = 0; i <= 6; i++ )
    {
      Integer e = ivl.getFromRight( i );
      assertEquals( (Integer)(3 - i), e );
    }
  }

  public void testGetFromRightStep1NegStepIndex()
  {
    try
    {
      IntegerInterval ivl = new IntegerInterval( -3, 3 );
      ivl.getFromRight( -1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGetFromRightStep1OutOfBoundsStepIndex()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3 );

    Integer e = ivl.getFromRight( 7 );
    assertEquals( null, e );
  }

  public void testIteratorStep1()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Integer e : new IntegerInterval( -3, 3 ) )
    {
      result.add( e );
    }
    assertListEquals( Arrays.asList( -3,-2,-1,0,1,2,3 ), result );
  }

  public void testIterateFromLeftStep1()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Iterator<Integer> iter = new IntegerInterval( -3, 3 ).iterateFromLeft(); iter.hasNext(); )
    {
      result.add( iter.next() );
    }
    assertListEquals( Arrays.asList( -3,-2,-1,0,1,2,3 ), result );
  }

  public void testIterateFromLeftStep1NoSuchElement()
  {
    Iterator<Integer> iter = new IntegerInterval( -3, 3 ).iterateFromLeft();
    for( ;iter.hasNext(); )
    {
      iter.next();
    }
    try
    {
      iter.next();
    }
    catch( NoSuchElementException e )
    {
      return;
    }
    fail();
  }

  public void testIterateFromRightStep1()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Iterator<Integer> iter = new IntegerInterval( -3, 3 ).iterateFromRight(); iter.hasNext(); )
    {
      result.add( iter.next() );
    }
    assertListEquals( Arrays.asList( 3,2,1,0,-1,-2,-3 ), result );
  }

  public void testIterateFromRightStep1NoSuchElement()
  {
    Iterator<Integer> iter = new IntegerInterval( -3, 3 ).iterateFromRight();
    for( ;iter.hasNext(); )
    {
      iter.next();
    }
    try
    {
      iter.next();
    }
    catch( NoSuchElementException e )
    {
      return;
    }
    fail();
  }
  
  
  
  public void testGetFromLeftStep2()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );

    for( int i = 0; i <= 6/2; i++ )
    {
      Integer e = ivl.getFromLeft( i );
      assertEquals( (Integer)(-3 + i*2), e );
    }
  }

  public void testGetFromLeftStep2NegStepIndex()
  {
    try
    {
      IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );
      ivl.getFromLeft( -1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGetFromLeftStep2OutOfBoundsStepIndex()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );

    Integer e = ivl.getFromLeft( 4 );
    assertEquals( null, e );
  }
  

  public void testGetFromRightStep2()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );

    for( int i = 0; i <= 6/2; i++ )
    {
      Integer e = ivl.getFromRight( i );
      assertEquals( (Integer)(3 - i*2), e );
    }
  }

  public void testGetFromRightStep2NegStepIndex()
  {
    try
    {
      IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );
      ivl.getFromRight( -1 );
    }
    catch( IllegalArgumentException e )
    {
      return;
    }
    fail();
  }

  public void testGetFromRightStep2OutOfBoundsStepIndex()
  {
    IntegerInterval ivl = new IntegerInterval( -3, 3, 2 );

    Integer e = ivl.getFromRight( 4 );
    assertEquals( null, e );
  }

  public void testIteratorStep2()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Integer e : new IntegerInterval( -3, 3, 2 ) )
    {
      result.add( e );
    }
    assertListEquals( Arrays.asList( -3,-1,1,3 ), result );
  }

  public void testIteratorStep2OpenRight()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Integer e : new IntegerInterval( -4, 5, 2, true, false, false ) )
    {
      result.add( e );
    }
    assertListEquals( Arrays.asList( -4,-2,0,2,4 ), result );
  }

  public void testIteratorStep2OpenLeft()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Integer e : new IntegerInterval( -5, 5, 2, false, true, false ) )
    {
      result.add( e );
    }
    assertListEquals( Arrays.asList( -3,-1,1,3,5 ), result );
  }

  public void testIteratorStep2OpenLeftOpenRight()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Integer e : new IntegerInterval( -5, 5, 2, false, false, false ) )
    {
      result.add( e );
    }
    assertListEquals( Arrays.asList( -3,-1,1,3 ), result );
  }

  public void testIterateFromLeftStep2()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Iterator<Integer> iter = new IntegerInterval( -3, 3, 2 ).iterateFromLeft(); iter.hasNext(); )
    {
      result.add( iter.next() );
    }
    assertListEquals( Arrays.asList( -3,-1,1,3 ), result );
  }

  public void testIterateFromLeftStep2NoSuchElement()
  {
    Iterator<Integer> iter = new IntegerInterval( -3, 3, 2 ).iterateFromLeft();
    for( ;iter.hasNext(); )
    {
      iter.next();
    }
    try
    {
      iter.next();
    }
    catch( NoSuchElementException e )
    {
      return;
    }
    fail();
  }

  public void testIterateFromRightStep2()
  {
    List<Integer> result = new ArrayList<Integer>();
    for( Iterator<Integer> iter = new IntegerInterval( -3, 3, 2 ).iterateFromRight(); iter.hasNext(); )
    {
      result.add( iter.next() );
    }
    assertListEquals( Arrays.asList( 3,1,-1,-3 ), result );
  }

  public void testIterateFromRightStep2NoSuchElement()
  {
    Iterator<Integer> iter = new IntegerInterval( -3, 3, 2 ).iterateFromRight();
    for( ;iter.hasNext(); )
    {
      iter.next();
    }
    try
    {
      iter.next();
    }
    catch( NoSuchElementException e )
    {
      return;
    }
    fail();
  }
}
