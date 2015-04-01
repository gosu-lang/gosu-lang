package gw.internal.gosu.parser.classTests.gwtest.dynamic

uses dynamic.Dynamic
uses java.lang.*
uses gw.lang.reflect.IExpando
uses java.util.Map
uses java.util.HashMap
uses java.math.BigInteger
uses java.math.BigDecimal
uses gw.lang.reflect.IPlaceholder

class DynamicTypeTest extends gw.BaseVerifyErrantTest {
  //
  // Properties

  function testDeclaredPropertyFromJavaObject() {
    var dyn : Dynamic = "hello"
    assertEquals( 5, dyn.length )
  }

  function testDeclaredPropertyFromDeclaredMethod() {
    var dyn : Dynamic = "hello"
    assertEquals( 4, dyn.substring( 1 ).length )
  }

  function testDeclaredPropertyFromDeclaredProperty() {
    var dyn : Dynamic = "hello"
    assertEquals( 5, dyn.Bytes.length )
  }

  function testDeclaredPropertyFromMetaType() {
    var dyn : Dynamic = String
    assertEquals( String.Type, dyn.Type )
  }

  function testDeclaredPropertyFromArray() {
    var dyn : Dynamic = new String[] {"a", "b"}
    assertEquals( 2, dyn.length )
  }

  function testDeclaredPropertyFromArrayElement() {
    var dyn : Dynamic = new String[] {"a", "b"}
    assertEquals( true, dyn[0].Alpha )
  }

  function testArrayExpansionOnProperty() {
    var dyn : Dynamic = new String[] {"a", "ab", "abc"}
    assertArrayEquals( {1, 2, 3}, dyn*.length )
  }

  function testNullSafePropertyRef() {
    var dyn : Dynamic = null
    assertNull( dyn?.Foo )
  }


  //
  // Methods

  function testDeclaredMethodFromJavaObject() {
    var dyn : Dynamic = "hello"
    assertEquals( 'h', dyn.charAt( 0 ) )
  }

  function testDeclaredMethodFromDeclaredMethod() {
    var dyn : Dynamic = "hello"
    assertEquals( 'e', dyn.substring( 1 ).charAt( 0 ) )
  }

  function testDeclaredMethodFromDeclaredProperty() {
    var dyn : Dynamic = "hello"
    assertEquals( {'h'as byte,'e'as byte,'l'as byte,'l'as byte,'o'as byte}, dyn.Bytes.toList() )
  }

  function testDeclaredMethodFromMetaType() {
    var dyn : Dynamic = String
    assertEquals( String.Type, dyn.Type )
  }

  function testDeclaredMethodFromArray() {
    var dyn : Dynamic = new String[] {"a", "b"}
    assertEquals( 2, dyn.length )
  }

  function testDeclaredMethodFromArrayElement() {
    var dyn : Dynamic = new String[] {"a", "b"}
    assertEquals( true, dyn[0].Alpha )
  }

  function testNullSafeMethodRef() {
    var dyn : Dynamic = null
    assertNull( dyn?.Foo )
  }

  function testArrayExpansionOnMethod() {
    var dyn : Dynamic = new String[] {"a", "b", "c"}
    assertArrayEquals( {'a', 'b', 'c'}, dyn*.charAt( 0 ) )
  }

  function testNullSafeMethodCall() {
    var dyn : Dynamic = null
    assertNull( dyn?.foo() )
  }

  //
  // Arrays

  function testArrays() {
    var dyn : Dynamic
    dyn = new String[] {"hello", "bye"}
    assertEquals( 2, dyn.length )
    assertEquals( "hello", dyn[0] )
    assertEquals( "bye", dyn[1] )
    assertEquals( "ello", dyn[0].substring( 1 ) )
  }

  //
  // Generics

  function testGenerics() {
    var listOfDyn : List<Dynamic>
    listOfDyn = {"hello", new Integer( 1 ) }
    assertEquals( 2, listOfDyn.size() )
    assertEquals( 'e', listOfDyn[0].charAt( 1 ) )
    assertEquals( 1, listOfDyn[1].intValue() )
  }

  //
  // Assignment

  function testSimpleTypesAssignableToDynamic() {
    var dyn : Dynamic
    dyn = 1
    assertTrue( typeof dyn === Integer && dyn == 1 )
    dyn = 1S
    assertTrue( typeof dyn === Short && dyn == 1S )
    dyn = 1B
    assertTrue( typeof dyn === Byte && dyn == 1B )
    dyn = true
    assertTrue( typeof dyn === Boolean && dyn == true )
    dyn = 1F
    assertTrue( typeof dyn === Float && dyn == 1F )
    dyn = 1D
    assertTrue( typeof dyn === Double && dyn == 1D )
    dyn = 1bi
    assertTrue( typeof dyn === BigInteger && dyn == 1bi )
    dyn = 1bd
    assertTrue( typeof dyn === BigDecimal && dyn == 1bd )
    dyn = "hi"
    assertTrue( typeof dyn === String && dyn == "hi" )
    dyn = 'A'
    assertTrue( typeof dyn === Character && dyn == 'A' )
  }

  function testSimpleTypesAssignableFromDynamic() {
    var dyn : Dynamic
    dyn = 1
    var int_box : Integer = dyn
    assertEquals( 1, int_box )
    dyn = true
    var bool_box : Boolean = dyn
    assertEquals( true, bool_box )
    dyn = 1S
    var short_box : Short = dyn
    assertEquals( 1S, short_box )
    dyn = 1B
    var byte_box : Byte = dyn
    assertEquals( 1B, byte_box )
    dyn = 1F
    var float_box : Float = dyn
    assertEquals( 1F, float_box )
    dyn = 1D
    var double_box : Double = dyn
    assertEquals( 1D, double_box )
    dyn = 1bi
    var bi : BigInteger = dyn
    assertEquals( 1bi, bi )
    dyn = 1bd
    var bd : BigDecimal = dyn
    assertEquals( 1bd, bd )
  }

  function testArraysAssignableToDynamic() {
    var dyn : Dynamic
    dyn = new String[] {"hi", "bye"}
    assertEquals( String[], typeof dyn )
    assertArrayEquals( {"hi", "bye"}, dyn )

    var intarray = new int[] {1,2,3}
    dyn = intarray
    assertEquals( int[], typeof dyn )
    assertEquals( intarray, dyn )
  }

  function testAutoUnboxingInAssignment() {
    var dyn : Dynamic
    dyn = 1 // Dynamic is erased to Object at runtime, therefore the runtime type of dyn here is Integer
    var i : int = dyn // Integer is unboxed to int
    assertEquals( 1, i )
  }

  function testAutoUnboxingOfArgs() {
    var dyn : Dynamic
    dyn = 1       // Integer is unboxed to int
    assertEquals( TestCls.passInt( dyn ), 2 )
  }

  //
  // Operators
  
  function testAddition() {
    var dyn : Dynamic = 22
    assertEquals( 42, dyn + 20 )
    dyn = 22F
    assertEquals( 42F, dyn + 20F )
    dyn = 22bd
    assertEquals( 42bd, dyn + 20bd )
    dyn = 22bi
    assertEquals( 42bi, dyn + 20bi )
  }
  
  function testSubtraction() {
    var dyn : Dynamic = 22
    assertEquals( 2, dyn - 20 )
    dyn = 22F
    assertEquals( 2F, dyn - 20F )
    dyn = 22bd
    assertEquals( 2bd, dyn - 20bd )
    dyn = 22bi
    assertEquals( 2bi, dyn - 20bi )
  }
  
  function testMultiplication() {
    var dyn : Dynamic = 22
    assertEquals( 44, dyn * 2 )
    dyn = 22F
    assertEquals( 44F, dyn * 2F )
    dyn = 22bd
    assertEquals( 44bd, dyn * 2bd )
    dyn = 22bi
    assertEquals( 44bi, dyn * 2bi )
  }

  function testDivision() {
    var dyn : Dynamic = 22
    assertEquals( 11, dyn / 2 )
    dyn = 22F
    assertEquals( 11F, dyn / 2F )
    dyn = 22bd
    assertEquals( 11bd, dyn / 2bd )
    dyn = 22bi
    assertEquals( 11bi, dyn / 2bi )
  }

  function testModulo() {
    var dyn : Dynamic = 22
    assertEquals( 1, dyn % 3 )
    dyn = 22F
    assertEquals( 1F, dyn % 3F )
    dyn = 22bd
    assertEquals( 1bd, dyn % 3bd )
    dyn = 22bi
    assertEquals( 1bi, dyn % 3bi )
  }

  function testNegation() {
    var dyn : Dynamic = 22
    assertEquals( -22, -dyn )
    dyn = true
    assertEquals( false, !dyn )
    //## not allowing this in the parser now, too much overhead in compiler
    //dyn = 1
    //assertEquals( 0, ~dyn )
  }

  //
  // Function/Closure calling

  function testDirectMethodCall() {
    // no arg
    var dyn : Dynamic = \->"hello"
    assertEquals( "hello", dyn() )

    // w/ args
    dyn = \ x: int -> x
    assertEquals( 1, dyn( 1 ) )

    // w/ inferred param type
    dyn = \ x -> x
    assertEquals( 1, dyn( 1 ) )

    // w/ statements
    dyn = \ x: int -> {x *= x return x}
    assertEquals( 4, dyn( 2 ) )

    // w/ capture
    var local = 0
    dyn = \-> {local = 8}
    assertEquals( 0, local )
    dyn()
    assertEquals( 8, local )
  }

  //
  // Dynamic dispatch

  function testInvokeMethod() {
    var testObj = new TestClassForDynDispatch()
    assertEquals( "zig", testObj.wavyGravy() )
    assertEquals( "zag", (testObj as Dynamic).wavyGravy() )
  }

  function testInvokeMethodUnhandled() {
    var testObj = new TestClassForDynDispatch()
    assertEquals( "straightup", testObj.unwavering() )
    assertEquals( "straightup", (testObj as Dynamic).unwavering() )
  }

  function testInvokeMissingMethod() {
    var testObj = new TestClassForDynDispatch()
    var missingMethod = (typeof testObj).TypeInfo.getMethod( "chickenButt", {} )
    assertNull( missingMethod )
    assertEquals( "hawk", (testObj as Dynamic).chickenButt() )  }

  function testInvokeMissingMethodUnhandled() {
    var testObj = new TestClassForDynDispatch()
    try {
      (testObj as Dynamic).notThereAtAll()
      fail()
    }
    catch( e: Exception ) {
      // expected
    }
  }

  function testInvokeProperty() {
    var testObj = new TestClassForDynDispatch()
    assertEquals( "Zig", testObj.WavyGravy )
    assertEquals( "Zag", (testObj as Dynamic).WavyGravy )
  }

  function testInvokePropertyUnhandled() {
    var testObj = new TestClassForDynDispatch()
    assertEquals( "Straightup", testObj.Unwavering )
    assertEquals( "Straightup", (testObj as Dynamic).Unwavering )
  }

  function testInvokeMissingProperty() {
    var testObj = new TestClassForDynDispatch()
    var missingProperty = (typeof testObj).TypeInfo.getProperty( "ChickenButt" )
    assertNull( missingProperty )
    assertEquals( "Hawk", (testObj as Dynamic).ChickenButt )  }

  function testInvokeMissingPropertyUnhandled() {
    var testObj = new TestClassForDynDispatch()
    try {
      (testObj as Dynamic).NotThereAtAll = "boing"
      fail()
    }
    catch( e: Exception ) {
      // expected
    }
  }

  static class TestClassForDynDispatch {
    var _unwavering : String as Unwavering = "Straightup"
    var _wavyGravy : String as WavyGravy = "Zig"

    function unwavering() : String {
      return "straightup"
    }

    function wavyGravy() : String {
      return "zig"
    }

    function $getProperty( name: String ) : Object {
      if( name.startsWith( "Wavy" ) ) {
        return "Zag"
      }
      return IPlaceholder.UNHANDLED
    }
    function $setProperty( name: String, value: Object ) : Object {
      if( name.startsWith( "Wavy" ) ) {
        return "Zag"
      }
      return IPlaceholder.UNHANDLED
    }
    function $getMissingProperty( name: String ) : Object {
      if( name.startsWith( "Chicken" ) ) {
        return "Hawk"
      }
      return IPlaceholder.UNHANDLED
    }
    function $setMissingProperty( name: String, value: Object ) : Object {
      if( name.startsWith( "Chicken" ) ) {
        return "Hawk"
      }
      return IPlaceholder.UNHANDLED
    }

    function $invokeMethod( name: String, args: Object[] ) : Object {
      if( name.startsWith( "wavy" ) ) {
        return "zag"
      }
      return IPlaceholder.UNHANDLED
    }
    function $invokeMissingMethod( name: String, args: Object[] ) : Object {
      if( name.startsWith( "chicken" ) ) {
        return "hawk"
      }
      return IPlaceholder.UNHANDLED
    }
  }
}