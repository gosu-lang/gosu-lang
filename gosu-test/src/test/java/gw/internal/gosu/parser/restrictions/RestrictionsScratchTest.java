/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
      .allowType( "java.lang.reflect.Method" ) // no effect: deny rules trump allow rules (baseline denies java.lang.reflect.**)
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
      List<String> errors = pre.getParseExceptions().stream()
        .map( e -> e.getLine() + ":" + e.getColumn() + " " + e.getPlainMessage() )
        .sorted( Comparator.comparingInt( (String e) -> Integer.parseInt( e.substring( 0, e.indexOf( ':' ) ) ) ) )
        .collect( Collectors.toList() );
      assertEquals( Arrays.asList(
        "1:6 Type 'java.lang.reflect.Method' is blocked by the restrictions baseline (rule: java.lang.reflect.**).",
        "5:19 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "7:8 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "8:1 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "8:14 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "9:5 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "9:15 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "10:1 Member 'java.lang.StringBuilder#append' is blocked by the restrictions profile (rule: java.lang.StringBuilder#append).",
        "10:34 Type 'java.lang.Double' is blocked by the restrictions profile (rule: java.lang.Double).",
        "11:1 Member 'java.lang.Object#getClass' is blocked by the restrictions baseline (rule: java.lang.Object#getClass).",
        "12:11 Type 'dynamic.Dynamic' is blocked by the restrictions baseline (rule: dynamic.Dynamic).",
        "13:2 Type 'dynamic.Dynamic' is blocked by the restrictions baseline (rule: dynamic.Dynamic).",
        "14:9 Type 'gw.lang.reflect.ReflectUtil' is blocked by the restrictions baseline (rule: gw.lang.reflect.**).",
        "15:8 Type 'Type' is not an allowed type or package in the restrictions profile.",
        "16:14 Language feature 'eval expressions' is not permitted by the restrictions profile.",
        "21:14 Language feature 'dynamic expressions / types' is not permitted by the restrictions profile.",
        "22:2 Type 'gw.lang.reflect.features.MethodReference' is blocked by the restrictions baseline (rule: gw.lang.reflect.**).",
        "22:18 Language feature 'feature literals (#method(), #property)' is not permitted by the restrictions profile.",
        "23:2 Type 'gw.lang.reflect.features.MethodReference' is blocked by the restrictions baseline (rule: gw.lang.reflect.**).",
        "23:2 Language feature 'feature literals (#method(), #property)' is not permitted by the restrictions profile.",
        "24:2 Type 'gw.lang.reflect.features.PropertyReference' is blocked by the restrictions baseline (rule: gw.lang.reflect.**).",
        "24:18 Language feature 'feature literals (#method(), #property)' is not permitted by the restrictions profile.",
        "25:2 Type 'gw.lang.reflect.features.BoundMethodReference' is blocked by the restrictions baseline (rule: gw.lang.reflect.**).",
        "25:17 Language feature 'feature literals (#method(), #property)' is not permitted by the restrictions profile.",
        "28:8 Language feature 'typeof expressions' is not permitted by the restrictions profile.",
        "29:8 Member 'java.lang.Integer#<init>' is blocked by the restrictions profile (rule: java.lang.Integer#<init>)." ),
        errors );
    }
  }
}