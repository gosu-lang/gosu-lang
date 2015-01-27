/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;
import junit.framework.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Attempt to test all of the coercions supported by Gosu.  Yikes.
 *
 * @author cgross
 */
public class StandardCoercionManagerTest extends TestClass
{
  //
  // regression test CC-44423
  //
  // the following code would previously compile without warning:
  // var s : String = new Object()
  // because coercionRequiresWarningIfImplicit had lhs and rhs swapped in one of its checks.
  //
  public void testCoercionFromObjectToStringIsNotImplicit() {
    StandardCoercionManager cm = (StandardCoercionManager) CommonServices.getCoercionManager();
    Assert.assertTrue(cm.canCoerce(JavaTypes.STRING(), JavaTypes.OBJECT()));
    Assert.assertTrue(cm.coercionRequiresWarningIfImplicit(JavaTypes.STRING(), JavaTypes.OBJECT()));
  }

  public void testCoercionFromPrimitiveIntToComparableDoesNotHaveWarning() {
    StandardCoercionManager cm = (StandardCoercionManager) CommonServices.getCoercionManager();
    Assert.assertFalse( cm.coercionRequiresWarningIfImplicit( JavaTypes.COMPARABLE(), JavaTypes.pINT() ) );
  }

  public void testMetaIntrinsicTypeCoercesToClass()
  {
    Boolean val = (Boolean)eval( "java.beans.Beans.isInstanceOf( \"foo\", java.lang.String ) \n");
    Assert.assertEquals( Boolean.TRUE, val );
  }

  public void testNonJavaTypesCoercableToObject()
  {
    Object val = eval( "var x : Object\n" +
                       "x = String\n" +
                       "return x");
    Assert.assertEquals( JavaTypes.STRING(), val );
  }

  public void testStringCoercionWorksCorrectly()
  {
    Object val = eval( "var x = new gw.internal.gosu.parser.StandardCoercionManagerTest.ToStringTest()\n" +
                       "return x as String");
    Assert.assertEquals( "test", val );
  }

  public void testStringCoercionWorksCorrectly2()
  {
    Object val = eval( "var x = true\n" +
                       "return x as String");
    Assert.assertEquals( "true", val );
  }

  public void testStringCoercionWorksCorrectly3()
  {
    Object val = eval( "var x = 1\n" +
                       "return x as String");
    Assert.assertEquals( "1", val );
  }

  @SuppressWarnings({"BooleanConstructorCall", "UnnecessaryBoxing"})
  public void testBooleanPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Boolean(true), eval( "1 as boolean"));
    Assert.assertEquals( new Boolean(true), eval( "true as boolean"));
    Assert.assertEquals( new Boolean(false), eval( "false as boolean"));
    Assert.assertEquals( new Boolean(true), eval( "2.0 as boolean"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testBytePrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Byte( (byte)1 ), eval( "1 as byte"));
    Assert.assertEquals( new Byte( (byte)1 ), eval( "true as byte"));
    Assert.assertEquals( new Byte( (byte)0 ), eval( "false as byte"));
    Assert.assertEquals( new Byte( (byte)1.0 ), eval( "1.0 as byte"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testCharacterPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Character((char) 1.0), eval( "1 as char"));
    Assert.assertEquals( new Character((char) 1.0), eval( "true as char"));
    Assert.assertEquals( new Character((char) 0.0), eval( "false as char"));
    Assert.assertEquals( new Character((char) 2.0), eval( "2.0 as char"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testDoublePrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Double(1), eval( "1 as double"));
    Assert.assertEquals( new Double(1), eval( "true as double"));
    Assert.assertEquals( new Double(0), eval( "false as double"));
    Assert.assertEquals( new Double(2), eval( "2.0 as double"));
  }


  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testDoubleCoercesFromInt()
  {
    Assert.assertEquals( new Double(10), eval( "var x : double\n" +
                                       "x = 10;\n" +
                                       "return x"));
    Assert.assertEquals( new Double(10), eval( "var x = 10\n" +
                                        "function foo(y : double) : double\n" +
                                        "{ \n" +
                                        "  return y" +
                                        "}\n" +
                                        " return foo(10)"));
//    assertEquals( new Double(10), eval( "function foo(x : double) : double\n" +
//                                        "{ \n" +
//                                        "  return x\n" +
//                                        "}\n" +
//                                        "return foo(10)"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testFloatPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Float(1), eval( "1 as float"));
    Assert.assertEquals( new Float(1), eval( "true as float"));
    Assert.assertEquals( new Float(0), eval( "false as float"));
    Assert.assertEquals( new Float(2), eval( "2.0 as float"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testIntegerPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Integer(1), eval( "1 as int"));
    Assert.assertEquals( new Integer(1), eval( "true as int"));
    Assert.assertEquals( new Integer(0), eval( "false as int"));
    Assert.assertEquals( new Integer(2), eval( "2.0 as int"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testLongPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Long(1), eval( "1 as long"));
    Assert.assertEquals( new Long(1), eval( "true as long"));
    Assert.assertEquals( new Long(0), eval( "false as long"));
    Assert.assertEquals( new Long(2), eval( "2.0 as long"));
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testShortPrimitivesConvertCorrectly()
  {
    Assert.assertEquals( new Short( (short)1 ), eval( "1 as short"));
    Assert.assertEquals( new Short( (short)1 ), eval( "true as short"));
    Assert.assertEquals( new Short( (short)0 ), eval( "false as short"));
    Assert.assertEquals( new Short( (short)2.0 ), eval( "2.0 as short"));
}

  public void testBigDecimalConvertsToPrimitiveNumerics()
  {
    Assert.assertEquals( new Integer("1"), eval( "new java.math.BigDecimal(1) as int"));
    Assert.assertEquals( new Long("1"), eval( "new java.math.BigDecimal(1) as long"));
    Assert.assertEquals( new Short("1"), eval( "new java.math.BigDecimal(1) as short"));
    Assert.assertEquals( new Double("1"), eval( "new java.math.BigDecimal(1) as double"));
    Assert.assertEquals( new Double("1.0"), eval( "new java.math.BigDecimal(1.0) as double"));
    Assert.assertEquals( new Double("1.3"), eval( "new java.math.BigDecimal(1.3) as double"));
    Assert.assertEquals( new Double("1.35"), eval( "new java.math.BigDecimal(1.35) as double"));
  }

  public void testPrimitiveNumericsToBigDecimal()
  {
    Assert.assertEquals( new BigDecimal( "1" ), eval( "(new java.math.BigDecimal(1) as int) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1" ), eval( "(new java.math.BigDecimal(1) as long) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1" ), eval( "(new java.math.BigDecimal(1) as short) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1.0" ), eval( "(new java.math.BigDecimal(1) as double) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1.0" ), eval( "(new java.math.BigDecimal(1.0) as double) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1.3" ), eval( "(new java.math.BigDecimal(1.3) as double) as java.math.BigDecimal" ) );
    Assert.assertEquals( new BigDecimal( "1.35" ), eval( "(new java.math.BigDecimal(1.35) as double) as java.math.BigDecimal" ) );
  }

  public void testBigIntegerConvertsToPrimitiveNumerics()
  {
    Assert.assertEquals( new Integer("1"), eval( "new java.math.BigInteger(\"1\") as int"));
    Assert.assertEquals( new Long("1"), eval( "new java.math.BigInteger(\"1\") as long"));
    Assert.assertEquals( new Short("1"), eval( "new java.math.BigInteger(\"1\") as short"));
    Assert.assertEquals( new Double("1"), eval( "new java.math.BigInteger(\"1\") as double"));
  }

  public void testPrimitiveNumericsToBigInteger()
  {
    Assert.assertEquals( new BigInteger( "1" ), eval( "(new java.math.BigInteger(\"1\") as int) as java.math.BigInteger" ) );
    Assert.assertEquals( new BigInteger( "1" ), eval( "(new java.math.BigInteger(\"1\") as long) as java.math.BigInteger" ) );
    Assert.assertEquals( new BigInteger( "1" ), eval( "(new java.math.BigInteger(\"1\") as short) as java.math.BigInteger" ) );
    Assert.assertEquals( new BigInteger( "1" ), eval( "(new java.math.BigInteger(\"1\") as double) as java.math.BigInteger" ) );
  }

  @SuppressWarnings({"UnnecessaryBoxing"})
  public void testNumericToVariousImpls()
  {
    Assert.assertEquals( new Double(2), eval( "var x : Number = 2;\nvar y : double = x as double;\nreturn y;" ) );
    Assert.assertEquals( new Integer(2), eval( "var x : Number = 2;\nvar y : int = x as int;\nreturn y;" ) );
  }

  public void testCoerceArrayToIncompatiblyTypedCollectionsFails()
  {
    assertDoesNotCompile( "new String[]{\"a\", \"b\", \"c\"} as List<Boolean>" );
    assertDoesNotCompile( "new String[]{\"a\", \"b\", \"c\"} as java.util.List<Boolean>" );
    assertDoesNotCompile( "new String[]{\"a\", \"b\", \"c\"} as java.util.Set<Boolean>" );
    assertDoesNotCompile( "new String[]{\"a\", \"b\", \"c\"} as java.util.Collection<Boolean>" );
    assertDoesNotCompile( "new String[]{\"a\", \"b\", \"c\"} as java.util.ArrayList<Boolean>" );
  }

  public void testClassCoercionFromJavaMetaWorksCorrectly()
  {
    Object o = eval( "function foo() : Object { return java.lang.String as java.lang.Class } return foo()" );
    Assert.assertEquals( String.class, o );

    o = eval( "function foo() : Object { return java.lang.String as java.lang.Class<Object> } return foo()" );
    Assert.assertEquals( String.class, o );

    o = eval( "function foo() : Object { return java.lang.String as java.lang.Class<String> } return foo()" );
    Assert.assertEquals( String.class, o );
  }

  public void testClassCoercionFromNonJavaMetaFails()
  {
    IType byFullName = TypeSystem.getByFullName("gw.lang.SystemProperties");
    assertNotNull(byFullName);
    assertDoesNotCompile( "gw.lang.SystemProperties as java.lang.Class" );
  }

  public void testJavaIntrinsicTypeCoercionFromJavaMetaWorksCorrectly()
  {
    Object o = eval( "java.lang.String as gw.lang.reflect.java.IJavaType" );
    Assert.assertEquals( JavaTypes.STRING(), o );
  }

  public void testJavaArrayToGosuArrayThrowsRuntimeExceptionIfCoercionNotPossible()
  {
    try
    {
      eval( "new List(){true}.toArray() as gw.internal.gosu.parser.gosuclasses.SampleGosuClass[]" );
      Assert.fail("Should have been a runtime exception as we attempted to create a " +
           "Gosu array from a java object, the true value in the list.");
    }
    catch( ClassCastException e )
    {
      //pass
    }
  }

  public void testJavaIntrinsicTypeCoercionFromNonJavaMetaFails()
  {
    //TODO: fix AS coercions to not be bidirectional!!!
//    assertDoesNotCompile( "gw.lang.Returns as gw.internal.gosu.parser.JavaType" );
  }

  //TODO: fix equality coercions.  Need to order coercions so that one preferred coercion wins
  //      in order to preserve symmetry across
  public void testEqualitySymettric()
  {
//    Boolean val = (Boolean)eval(
//      "var x = \"foo\" \n" +
//      "var y = new java.lang.StringBuffer(\"foo\") \n" +
//      "return x == y\n"
//    );
//    assertEquals( Boolean.TRUE, val );
//
//    val = (Boolean)eval(
//      "var x = \"foo\" \n" +
//      "var y = new java.lang.StringBuffer(\"foo\") \n" +
//      "return y == x\n"
//    );
//    assertEquals( Boolean.TRUE, val );
  }

  private static boolean testBool = false;
  public static void setTestBoolean( boolean b )
  {
    testBool = b;
  }

  public static interface ParamInterface<T>
  {
    public T get();
  }

  public static interface MultiMethodInterface
  {
    public Object get();
    public void set();
  }

  public void testBlockCoercesToSimpleInterface()
  {
    Runnable runnable = (Runnable)eval( "var blk = \\-> gw.internal.gosu.parser.StandardCoercionManagerTest.setTestBoolean(true) \n" +
                                        "return blk as java.lang.Runnable" );
    testBool = false;
    Assert.assertFalse( "The boolean has yet to be set", testBool );
    runnable.run();
    Assert.assertTrue( "The boolean should have been set", testBool );
  }

  public void testInterfaceCoercesToSimpleBlock()
  {
    Boolean updated = (Boolean)eval( "var iface : java.lang.Runnable\n" +
                                     "function foo( blk() ) {}\n" +
                                     "foo( iface )\n" +
                                     "return true" );
    Assert.assertTrue( updated );
  }

  public void testGosuInterfaceCoercesToSimpleBlock()
  {
    Boolean result = (Boolean)eval( "return foo( \\ -> \"hello\" ) == 'hello'\n" +
                                     "\n" +
                                     "function foo( f: ICallable<String> ) : String {\n" +
                                     "   return f.call()\n" +
                                     "}\n" +
                                     "interface ICallable<T> {\n" +
                                     "  function call() : T\n" +
                                     "}" );
    Assert.assertTrue( result );
  }

  public void testBlockDoesNotCoerceToSimpleInterfaceThatItDoesNotMatch()
  {
    try
    {
      GosuTestUtil.compileExpression( "var blk = \\ x : String -> gw.internal.gosu.parser.StandardCoercionManagerTest.setTestBoolean(true) \n" +
                                          "return blk as java.lang.Runnable" );
      Assert.fail( "Should not have been able to compile since the block takes an argument, and runnable does not" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testBlockDoesNotCoerceToMultiMethodInterface()
  {
    try
    {
      GosuTestUtil.compileExpression( "var blk = \\ x : String -> gw.internal.gosu.parser.StandardCoercionManagerTest.setTestBoolean(true) \n" +
                                          "return blk as gw.internal.gosu.parser.StandardCoercionManagerTest.MultiMethodInterface" );
      Assert.fail( "Should not have been able to compile since the block takes an argument, and runnable does not" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testBlockCoercesToParameterizedInterface()
  {
    @SuppressWarnings({"unchecked"})
    ParamInterface<Boolean> pi = (ParamInterface<Boolean>)eval( "var blk = \\-> true \n" +
                                                                "return blk as gw.internal.gosu.parser.StandardCoercionManagerTest.ParamInterface<Boolean>" );
    Assert.assertTrue( pi.get() );
  }

  public void testBlockCoercesToParameterizedInterface2()
  {
    @SuppressWarnings({"unchecked"})
    Iterable<String> updated = (Iterable<String>)eval( "( \\-> new java.util.ArrayList<String>(){\"a\"}.iterator() ) as java.lang.Iterable<String>" );
    Iterator<String> iterator = updated.iterator();
    Assert.assertTrue( iterator.hasNext() );
    Assert.assertEquals( "a", iterator.next() );
    Assert.assertFalse( iterator.hasNext() );
  }


  public void testSimpleInterfaceDoesNotCoerceToIncompatibleBlockReturnType()
  {
    try
    {
      GosuTestUtil.compileExpression( "var b = runnable as block():Integer", "runnable", TypeSystem.get( Runnable.class ) );
      Assert.fail( "Should not be able to coerce to a block with an incompatible return type" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testSimpleInterfaceDoesNotCoerceToIncompatibleBlockParamType()
  {
    try
    {
      GosuTestUtil.compileExpression( "var b = runnable as block(Integer):void", "runnable", TypeSystem.get( Runnable.class ) );
      Assert.fail( "Should not be able to coerce to a block with an incompatible return type" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testComparatorCanProperlyBeInferred() throws ParseResultsException
  {
    @SuppressWarnings({"unchecked"})
    List<Integer> results = (List<Integer>)GosuTestUtil.eval( "var lst = new List<int>(){5, 4, 3, 2, 1}\n" +
                                               "java.util.Collections.sort(lst, \\ i1, i2 -> (i1 as java.lang.Integer) - (i2 as java.lang.Integer) )\n" +
                                               "return lst" );
    Assert.assertEquals( Arrays.asList( 1, 2, 3, 4, 5 ), results );
  }


  public void testParameterizedInterfaceDoesNotCoerceToIncompatibleBlock()
  {
    try
    {
      @SuppressWarnings({"UnusedDeclaration"})
      Boolean b = (Boolean)GosuTestUtil.evalGosu( "var x = foo as gw.internal.gosu.parser.StandardCoercionManagerTest.ParamInterface<Object>\n" +
                                                        "var y = x as block():Boolean;" +
                                                        "return y()", "foo", new ParamInterface<Object>(){
        public Boolean get()
        {
          return Boolean.TRUE;
        }
      });
      Assert.fail( "Should not have compiled, since block():Boolean is not compatible with an Object return type");
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testFeatureLiteralCoercesToCompatibleBlock() throws ParseResultsException
  {
    Integer b = (Integer) GosuTestUtil.evalGosu("var x = 'gosu rocks'#length()\n" +
      "var y : block():int = x;" +
      "return y()");
    assertEquals(10, b.intValue());
  }

  public void testFeatureLiteralDoesNotCoerceToIncompatibleBlock()
  {
    try
    {
      Integer b = (Integer) GosuTestUtil.evalGosu("var x = 'gosu rocks'#length()\n" +
        "var y : block():String = x;" +
        "return y()");
      Assert.fail( "Should not have compiled, since feature literal is not");
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testMultiMethodInterfaceDoesNotCoerceToBlock()
  {
    try
    {
      GosuTestUtil.compileExpression( "var y : gw.internal.gosu.parser.StandardCoercionManagerTest.MultiMethodInterface\n" +
                                         "return y as block():Object" );
      Assert.fail( "Should not have been able to compile since the interface has two methods on it" );
    }
    catch( ParseResultsException e )
    {
      //pass
    }
  }

  public void testGosuClassContextIsRestoredInBlockCoercedToInterface()
  {
    IGosuObject instance = ReflectUtil.constructGosuClassInstance( "gw.internal.gosu.parser.coercion.BlockCoercionTest" );
    Runnable runnable = (Runnable) ReflectUtil.invokeMethod( instance, "getTrueSetter" );
    Assert.assertFalse("Should be false after construction",
               (Boolean) ReflectUtil.getProperty( instance, "BoolVal" ));
    runnable.run();
    Assert.assertTrue("Should be true after running block coerced to runnable",
               (Boolean) ReflectUtil.getProperty( instance, "BoolVal" ));

  }

  public void testMetaTypeCoercedToTypeTypeStatically() {
    GosuTestUtil.eval("var x = String as gw.lang.reflect.java.IJavaType");
  }

  public void testMetaTypeCoercedToTypeTypeDynamically() {
    Object obj = GosuTestUtil.eval( "return gw.lang.reflect.TypeSystem.getByFullNameIfValid(\"java.lang.String\") as gw.lang.reflect.java.IJavaType" );
    Assert.assertEquals( JavaTypes.STRING(), obj );
  }

  public void testMetaTypeNotCoercedToWrongTypeTypeStatically() throws ParseResultsException {
    try {
      GosuTestUtil.evalGosu("var x = String as gw.internal.gosu.parser.GosuClass");
      Assert.fail("Should not have been able to coerce Type<String> to GosuClass");
    } catch (ParseResultsException e) {
      // pass
    }
  }

  public void testMetaTypeNotCoercedToWrongTypeTypeDynamically() {
    try {
      GosuTestUtil.eval("var x = gw.lang.reflect.TypeSystem.getByFullNameIfValid(\"java.lang.String\") as gw.internal.gosu.parser.GosuClass");
      Assert.fail("Should not have been able to coerce Type<String> to GosuClass");
    } catch (ClassCastException e) {
      // pass
    }
  }

//  public void testEndToEndComparableTest() throws ParseResultsException
//  {
//    List sorted = (List)GosuTestUtil.evalGosu( "var strs = new java.util.ArrayList<String>(){\"a\", \"ab\", \"abc\"}\n" +
//                                                     "java.util.Collections.sort<String>( strs, \\ x : String, y : String -> x.length - y.length  )\n" +
//                                                     "return strs;" );
//  }
//
  private Object eval( String s )
  {
    try
    {
      return GosuTestUtil.evalGosu( s );
    }
    catch( ParseResultsException e )
    {
      throw new RuntimeException( e.getFeedback(), e );
    }
  }

  private void assertDoesNotCompile( String script )
  {
    try
    {
      eval( script );
      Assert.fail( "Should not be able to compile \"" + script + "\"");
    }
    catch( Exception e )
    {
      Assert.assertTrue( e.getCause() instanceof ParseResultsException );
    }
  }

  public static class ToStringTest {
    public String toString()
    {
      return "test";
    }
  }

  public static class ToDateStringTest {
    public String toString()
    {
      return "2000-01-01";
    }
  }

}
