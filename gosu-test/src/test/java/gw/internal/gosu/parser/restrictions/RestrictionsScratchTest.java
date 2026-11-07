/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

/**
 */
public class RestrictionsScratchTest extends TestClass
{
  public void testScratch()
  {
    ParserOptions options = new ParserOptions();
    Restrictions restrictions = Restrictions
      .baseline()
      .denyMember( "java.lang.StringBuilder", "append" )
      .denyMember( "java.lang.Integer", "<init>" )
      .allowType( "java.util.concurrent.atomic.AtomicInteger" ) // allows a specific type regardless of depth in not allowed packages
      .allowType( "java.lang.reflect.Method" )
      .denyType( "java.lang.Double" )
      .build();

    options.withRestrictions( restrictions );

    try
    {
      GosuTestUtil.evalGosu( "uses java.lang.reflect.Method\n" +
                             "static class Foo {\n" +
                             "  var _x: int\n" +
                             "  function foo(i: int): int {return i}\n" +
                             "  function hi() : Double {return 2}\n" +
                             "}\n" +
                             "var s: Double = 2\n" +
                             "var ss = new Double('1')\n" +
                             "for(x in {new Double('1')}) {print(x)}\n" +
                             "new StringBuilder(\"asdf\").append(Double.valueOf(1))\n" +
                             "new StringBuilder(\"asdf\").getClass()\n" +
                             "var dyn : dynamic.Dynamic = ss\n dyn.floatValue()\n" +
                             "var r : gw.lang.reflect.ReflectUtil\n" +
                             "var t: Type; var m = t.Mutable\n" +
                             "var result = eval(\"1 + 2\")\n" +
                             "var map = new HashMap<String, String>()\n map[\"a\"] = \"aa\"\n print(map[\"a\"])\n" +
                             "var date = java.time.LocalDate.of(2026, 11, 6)\n var month = date[\"month\"]\n" +
                             " var fooMethod = Foo#foo(int)\n" +
                             " Foo#foo(int).invoke(new Foo(), 9)\n" +
                             " var xProperty = Foo#_x\n" +
                             " var boundFoo = (new Foo())#foo(int)\n" +
                             " var atomic = new java.util.concurrent.atomic.AtomicInteger(1)\n" +
                             " atomic.incrementAndGet()\n" +
                             " print(typeof atomic)\n" +
                             " print(new Integer(1))\n" +
                             "return new Foo().hi()", options );
      fail( "Expected ParseResultsException" );
    }
    catch( ParseResultsException pre )
    {
      assertEquals( 27, pre.getParseExceptions().size() );
    }
  }
}