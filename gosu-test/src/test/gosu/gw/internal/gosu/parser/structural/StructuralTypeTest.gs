package gw.internal.gosu.parser.structural
uses gw.BaseVerifyErrantTest
uses java.lang.StringBuilder

class StructuralTypeTest extends BaseVerifyErrantTest {

  function testErrant_StructureAssignableToStructureTest() {
    processErrantType( Errant_StructureAssignableToStructureTest )
  }

  function testErrant_StructureAssignableToInterfaceTest() {
    processErrantType( Errant_StructureAssignableToInterfaceTest )
  }

  function testErrant_JavaTypeAssignabilityTest() {
    processErrantType( Errant_JavaTypeAssignabilityTest )
  }

  function testExtendsJavaInterface() {
    var f : FooStructure = new FooStructureImpl()
    assertEquals( "hello", f.doFoo() )
    assertEquals( "blah", f.Foo )
    f.Foo = "newValue"
    assertEquals( "newValue", f.Foo )
  }

  function testExtendsJavaInterfaceWithField() {
    var f : FooStructure = new FooStructureAsFieldImpl()
    assertEquals( "hello", f.doFoo() )
    assertEquals( "blah", f.Foo )
    f.Foo = "newValue"
    assertEquals( "newValue", f.Foo )
  }

  function testEcho() {
    var e : TestStructures.Echo = new EchoImpl()
    assertEquals( true, e.echo( true ) )
    assertEquals( 1 as byte, e.echo( 1 as byte ) )
    assertEquals( 2 as char, e.echo( 2 as char ) )
    assertEquals( 3 as short, e.echo( 3 as short ) )
    assertEquals( 4 as int, e.echo( 4 as int ) )
    assertEquals( 5 as long, e.echo( 5 as long ) )
    assertEquals( 6.1 as float, e.echo( 6.1 as float ), 0.01f )
    assertEquals( 7.1 as double, e.echo( 7.1 as double ), 0.01 )
    assertEquals( int[], typeof e.echo( new int[] {1,2} ) )
    assertEquals( "hi", e.echo( "hi" ) )
    assertEquals( {"hello"} as List<String>, e.echo( {"hello"} as List<String> ) )
  }

  function testGenericEcho() {
    var ge : TestStructures.GenericEcho<String> = new GenericEchoImpl<String>()
    assertEquals( "hi", ge.echo( "hi" ) )
    assertEquals( {"one" -> 1}, ge.echo( "one", 1 ) )
    assertEquals( 1, ge.echo( 1, "hi" ) )
  }

  function testPropertyStruct() {
    var propStruct : TestStructures.PropertyStruct = new PropertyStructImpl()
    propStruct.Foo = "hi"
    assertEquals( "hi", propStruct.Foo )

    propStruct = new PropertyStructAsVarImpl()
    propStruct.Foo = "fred"
    assertEquals( "fred", propStruct.Foo )
  }

  function testStructureExtendsIterableErasesToObjectArray() {
    var arr = toArray( {"A"} )
    assertArrayEquals( arr, {{"A"}} )
  }
  private function toArray(x: StructureExtendsIterable): StructureExtendsIterable[] {
    return {x}
  }

  static class TestContravarianceWorksWithNominalUsageOfStructure {
    structure Comparable<T> {
      function compareTo( t: T ) : int
    }

    structure Blah extends Comparable<String> {}

    static class Some implements Comparable<CharSequence> {
      override function compareTo( c: CharSequence ) : int { return 1 }
    }

    static class Thing implements Comparable<Object> {
      override function compareTo( c: Object ) : int { return 2 }
    }

    static class Foo {
      function foo( c: Blah ) : int {
        return c.compareTo(  "hi" )
      }
    }
  }

  function testContravarianceWorksWithNominalUsageOfStructure() {
    var f = new TestContravarianceWorksWithNominalUsageOfStructure.Foo()

    assertEquals( 1, f.foo( new TestContravarianceWorksWithNominalUsageOfStructure.Some() ) )
    assertEquals( 2, f.foo( new TestContravarianceWorksWithNominalUsageOfStructure.Thing() ) )
    assertEquals( 3, f.foo( new TestContravarianceWorksWithNominalUsageOfStructure.Comparable<CharSequence>() {
      override function compareTo( c: CharSequence ): int {
        return 3
      }
    }) )
    assertEquals( 4, f.foo( new TestContravarianceWorksWithNominalUsageOfStructure.Comparable<Object>() {
      override function compareTo( c: Object ): int {
        return 4
      }
    }) )
    assertEquals( 5, f.foo( new Comparable<CharSequence>() {
      override function compareTo( c: CharSequence ): int {
        return 5
      }
    }) )
    assertEquals( 6, f.foo( new Comparable<Object>() {
      override function compareTo( c: Object ): int {
        return 6
      }
    }) )
  }

  structure TestStructure {
    function foo(): int
  }
  class SatisfiesTestStructure {
    function foo(): int {
      return 8
    }
  }
  interface ITestCovariantReturnWithStructure {
    property get X() : TestStructure
    function doY() : TestStructure
  }
  class TestCovariantReturnWithStructure implements ITestCovariantReturnWithStructure {
    override property get X() : SatisfiesTestStructure { return new SatisfiesTestStructure () }
    override function doY() : SatisfiesTestStructure { return new SatisfiesTestStructure () }
  }
  function testCovariantReturns() {
    var testMe: ITestCovariantReturnWithStructure = new TestCovariantReturnWithStructure ()
    assertEquals( 8, testMe.doY().foo() )
    assertEquals( 8, testMe.X.foo() )
  }

  interface ITestStructureGenericBound<T extends TestStructure> {
    function foo(t: T) : int {
      return t.foo()
    }
  }
  class TestStructureGenericBound implements ITestStructureGenericBound<SatisfiesTestStructure> {
  }
  function testStructureGenericBound() {
    var testMe = new TestStructureGenericBound()
    assertEquals( 8, testMe.foo( new SatisfiesTestStructure() ) )
  }

  function testReflection() {
    var test = new TestReflection()
    var param = new ReflectionStructureImpl()
    var res = TestReflection.Type.TypeInfo
    .getMethod( "callMe", {ReflectionStructure} )
    .CallHandler.handleCall( test, {param} ) as ReflectionStructure
    assertEquals( param, res )
  }

  structure ReflectionStructure {
    function foo() : ReflectionStructure
  }
  static class ReflectionStructureImpl {
    function foo() : ReflectionStructure {
      return this
    }
  }
  static class TestReflection {
    function callMe( t: ReflectionStructure ): ReflectionStructure {
      return t.foo()
    }
  }

  structure ReturnParameterizedType_Property { property get X() : List<String> }
  class ImplReturnParameterizedType_Property { property get X() : List<String> { return {"hi"} } }
  function testReturnParameterizedType_Property() {
    var obj: ReturnParameterizedType_Property = new ImplReturnParameterizedType_Property()
    assertEquals( {"hi"}, obj.X )
  }

  structure ReturnParameterizedType_Function { function getX() : List<String> }
  class ImplReturnParameterizedType_Function { function getX() : List<String> { return {"hi"} } }
  function testReturnParameterizedType_Function() {
    var obj: ReturnParameterizedType_Function = new ImplReturnParameterizedType_Function()
    assertEquals( {"hi"}, obj.getX() )
  }

  function testDefaultMethodTest() {
    DefaultMethodTest.test()
  }
  static class DefaultMethodTest {
    structure MyStructure {
      function foo() : String {
        return "I am in structure"
      }
    }

    static class OverridesMethod {
      function foo() : String {
        return "I am in class"
      }
    }

    static class NotOverridesMethod {
    }

    static function test() {
      var t: MyStructure = new OverridesMethod()
      assertEquals( "I am in class", t.foo() )

      t = new NotOverridesMethod()
      assertEquals( "I am in structure", t.foo() )
    }
  }
  
  function testDefaultPropertyTest() {
    DefaultPropertyTest.test()
  }
  static class DefaultPropertyTest {
    static var _state: String

    structure MyStructure {
      property get Hi() : String { return "Get Structure" }
      property set Hi(s: String) { _state = s + " Set Structure" }
    }

    static class OverridesAll {
      property get Hi() : String { return "Get Class" }
      property set Hi(s: String) { _state = s + " Set Class" }
    }

    static class OverridesNone {
    }

    static class OverridesSet {
      property set Hi(s: String) { _state = s + " Set Class" }
    }

    static class OverridesGet {
      property get Hi() : String { return "Get Class" }
    }

    static function test() {
      var t: MyStructure = new OverridesAll()
      assertEquals( "Get Class", t.Hi )
      t.Hi = "foo"
      assertEquals( "foo Set Class", _state )

      t = new OverridesNone()
      assertEquals( "Get Structure", t.Hi )
      t.Hi = "bar"
      assertEquals( "bar Set Structure", _state )

      t = new OverridesSet()
      assertEquals( "Get Structure", t.Hi )
      t.Hi = "baz"
      assertEquals( "baz Set Class", _state )

      t = new OverridesGet()
      assertEquals( "Get Class", t.Hi )
      t.Hi = "bom"
      assertEquals( "bom Set Structure", _state )
    }
  }
}