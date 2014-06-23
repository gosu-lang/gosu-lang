/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author cgross
 */
public class InitializerSyntaxTest extends TestClass
{

  public void testEmptyInitializationSyntaxWorks() throws ParseResultsException
  {
    List list = (List)eval( "new java.util.ArrayList(){}" );
    assertEquals(0, list.size());

    Set set = (Set)eval( "new java.util.HashSet(){}" );
    assertEquals(0, set.size());

    Map map = (Map)eval( "new java.util.HashMap(){}" );
    assertEquals(0, map.size());
  }

  private Object eval( String s ) throws ParseResultsException
  {
    return GosuTestUtil.evalGosu( s );
  }

  public void testBasicListInitializationSyntaxWorks() throws ParseResultsException
  {
    List list = (List)eval( "new java.util.ArrayList(){\"a\", \"b\", \"c\"}" );
    assertListEquals( Arrays.asList( "a", "b", "c"), list);

    list = (List)eval( "new java.util.ArrayList(){true, false, null}" );
    assertListEquals( Arrays.asList( Boolean.TRUE, Boolean.FALSE, null), list);
  }

  public void testBasicListInitializationSyntaxWorksWithOtherLists() throws ParseResultsException
  {
    List list = (List)eval( "new java.util.LinkedList(){\"a\", \"b\", \"c\"}" );
    assertListEquals( Arrays.asList( "a", "b", "c" ), list);

    list = (List)eval( "new java.util.LinkedList(){true, false, null}" );
    assertListEquals( Arrays.asList( Boolean.TRUE, Boolean.FALSE, null ), list);
  }

  public void testListInitializationSyntaxWorksWithGenerics() throws ParseResultsException
  {
    List list = (List)eval( "new java.util.ArrayList<String>(){\"a\", \"b\", \"c\"}" );
    assertListEquals( Arrays.asList( "a", "b", "c"), list);

    list = (List)eval( "new java.util.ArrayList<Boolean>(){true, false, null}" );
    assertListEquals( Arrays.asList( Boolean.TRUE, Boolean.FALSE, null), list);
  }

  public void testListInitializationSyntaxIsTypeSafe()
  {
    try
    {
      eval( "new java.util.ArrayList<Boolean>(){new Object(), false, null}" );
      fail();
    }
    catch( ParseResultsException e )
    {
      //e.printStackTrace();
      //pass
    }
  }

  public void testCoercionsOccurCorrectlyInInitList() throws ParseResultsException
  {
    Object obj = eval( "var x = new java.util.ArrayList<java.lang.Integer>(){1, 2, 3}\n" +
                       "return x[0]" );
    assertEquals( new Integer( 1 ), obj );
  }

  public void testCoercionsOccurCorrectlyInInitMap() throws ParseResultsException
  {
    Object obj = eval( "var x = new java.util.HashMap<java.lang.Integer, java.lang.Integer>(){1 -> 1, 2 -> 2, 3 -> 3}\n" +
                       "return x[1]" );
    assertEquals( new Integer( 1 ), obj );
  }

  public void testBasicSetInitializationSyntaxWorks() throws ParseResultsException
  {
    Set set = (Set)eval( "new java.util.HashSet(){\"a\", \"b\", \"c\"}" );
    assertSetsEqual( new HashSet(Arrays.asList( "a", "b", "c")), set);

    set = (Set)eval( "new java.util.HashSet(){true, false, null}" );
    assertSetsEqual( new HashSet(Arrays.asList( Boolean.TRUE, Boolean.FALSE, null)), set);
  }

  public void testBasicSetInitializationSyntaxWorksWithOtherSets() throws ParseResultsException
  {
    Set set = (Set)eval( "new java.util.LinkedHashSet(){\"a\", \"b\", \"c\"}" );
    assertCollectionEquals( Arrays.asList( "a", "b", "c"), set);

    set = (Set)eval( "new java.util.LinkedHashSet(){true, false, null}" );
    assertCollectionEquals( Arrays.asList( Boolean.TRUE, Boolean.FALSE, null), set);
  }

  public void testSetInitializationSyntaxWorksWithGenerics() throws ParseResultsException
  {
    Set set = (Set)eval( "new java.util.HashSet<String>(){\"a\", \"b\", \"c\"}" );
    assertSetsEqual( new HashSet(Arrays.asList( "a", "b", "c")), set);

    set = (Set)eval( "new java.util.HashSet<Boolean>(){true, false, null}" );
    assertSetsEqual( new HashSet(Arrays.asList( Boolean.TRUE, Boolean.FALSE, null)), set);
  }

  public void testSetInitializationSyntaxIsTypeSafe()
  {
    try
    {
      eval( "new java.util.HashSet<Boolean>(){new Object(), false, null}" );
      fail();
    }
    catch( ParseResultsException e )
    {
      //e.printStackTrace();
      //pass
    }
  }

  public void testBasicMapInitializationSyntaxWorks() throws ParseResultsException
  {
    Map map = (Map)eval( "new java.util.HashMap(){\"a\" -> true , \"b\" -> false, \"c\" -> true}");
    assertEquals( Boolean.TRUE, map.get( "a" ) );
    assertEquals( Boolean.FALSE, map.get( "b" ) );
    assertEquals( Boolean.TRUE, map.get( "c" ) );
  }

  public void testMapInitializationSyntaxWorksWithGenerics() throws ParseResultsException
  {
    Map map = (Map)eval( "new java.util.HashMap<String, Boolean>(){\"a\" -> true , \"b\" -> false, \"c\" -> true}");
    assertEquals( Boolean.TRUE, map.get( "a" ) );
    assertEquals( Boolean.FALSE, map.get( "b" ) );
    assertEquals( Boolean.TRUE, map.get( "c" ) );
  }

  public void testMapInitializationSyntaxIsTypeSafeWithRespectToKeys()
  {
    try
    {
      eval( "new java.util.HashMap<Boolean, Boolean>(){new Object() -> true}");
      fail();
    }
    catch( ParseResultsException e )
    {
      //e.printStackTrace();
      //pass
    }
  }

  public void testMapInitializationSyntaxIsTypeSafeWithRespectToValues()
  {
    try
    {
      eval( "new java.util.HashMap<Boolean, Boolean>(){ true -> new Object() }");
      fail();
    }
    catch( ParseResultsException e )
    {
      //e.printStackTrace();
      //pass
    }
  }
  public static Object testMapMethod(Map<String, Object> testMap) {
    return testMap;
  }
  public void testMapInitializationSyntaxWorksWithEmptyArrayToTypedMethod() throws ParseResultsException {
    Map m = (Map) eval("gw.internal.gosu.parser.InitializerSyntaxTest.testMapMethod({})");
    assertEquals(0, m.size());
  }

  public void testTypesWithNoInitializerSyntaxReportErrors() throws ParseResultsException
  {
    try
    {
      eval( "new java.lang.Object(){null, null}" );
      fail();
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testNonInitializableTypesCannotBeUsedWithInitializerSyntax() throws ParseResultsException
  {
    try
    {
      eval( "new java.util.Set(){null, null}" );
      fail();
    }
    catch( ParseResultsException e )
    {
    }

    try
    {
      eval( "new java.util.AbstractSet(){null, null}" );
      fail();
    }
    catch( ParseResultsException e )
    {
    }
  }

}
