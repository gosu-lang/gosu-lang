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
}