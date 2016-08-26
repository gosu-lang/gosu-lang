package gw.specContrib.classes.property_Declarations.new_syntax

uses gw.test.TestClass

class PropertyRuntimeTest extends TestClass
{
  static class Stuff
  {
    property Prop1: String

    property Prop2: String = "hi"

    property get Prop3: String = "aaa"

    property set Prop4: String = "setonly"

    property Prop5: String = "test"
    property get Prop5() : String {
      _Prop5 += "#"
       return _Prop5
    }

    property Prop6: String = "test"
    property set Prop6( v: String ) {
      _Prop6 = "#" + v
    }

    property Prop7: String = "test"
    property get Prop7() : String {
      _Prop7 += "#"
       return _Prop7
    }
    property set Prop7( v: String ) {
      _Prop7 = "#" + v
    }
  }

  function testStuff()
  {
    var stuff = new Stuff()

    assertNull( stuff.Prop1 )
    stuff.Prop1 = "aaa"
    assertEquals( "aaa", stuff.Prop1 )
    assertEquals( "aaa", stuff._Prop1 )
    assertTrue( Stuff.Type.TypeInfo.getProperty( Stuff, "_Prop1" ).Private )

    assertEquals( "hi", stuff.Prop2 )
    stuff.Prop2 = "bye"
    assertEquals( "bye", stuff.Prop2 )

    assertEquals( "aaa", stuff.Prop3 )
    stuff._Prop3 = "bbb"
    assertEquals( "bbb", stuff.Prop3 )

    assertEquals( "setonly", stuff._Prop4 )
    stuff.Prop4 = "bbb"
    assertEquals( "bbb", stuff._Prop4 )

    assertEquals( "test", stuff._Prop5 )
    assertEquals( "test#", stuff.Prop5 )
    stuff.Prop5 = "foo"
    assertEquals( "foo", stuff._Prop5 )
    assertEquals( "foo#", stuff.Prop5 )

    assertEquals( "test", stuff._Prop6 )
    assertEquals( "test", stuff.Prop6 )
    stuff.Prop6 = "foo"
    assertEquals( "#foo", stuff._Prop6 )
    assertEquals( "#foo", stuff.Prop6 )

    assertEquals( "test", stuff._Prop7 )
    assertEquals( "test#", stuff.Prop7 )
    stuff.Prop7 = "foo"
    assertEquals( "#foo", stuff._Prop7 )
    assertEquals( "#foo#", stuff.Prop7 )


  }

  interface IFace
  {
    property Prop1: String

    property Prop2: String = "hi"

    property get Prop3: String = "aaa"

    property set Prop4: String = "setonly"
  }

  static class FooImpl implements IFace
  {
    override property Prop1: String
    override property set Prop2: String
    override property set Prop4: String
  }

  function testIFace()
  {
    var stuff = new FooImpl()

    assertNull( stuff.Prop1 )
    stuff.Prop1 = "aaa"
    assertEquals( "aaa", stuff.Prop1 )
    assertEquals( "aaa", stuff._Prop1 )
    assertTrue( FooImpl.Type.TypeInfo.getProperty( FooImpl, "_Prop1" ).Private )

    assertEquals( "hi", stuff.Prop2 )
    stuff.Prop2 = "bye"
    assertEquals( "bye", stuff._Prop2 )
    assertEquals( "hi", stuff.Prop2 )

    assertEquals( "aaa", stuff.Prop3 )
    assertNull( FooImpl.Type.TypeInfo.getProperty( FooImpl, "_Prop3" ) )

    assertEquals( null, stuff._Prop4 )
    stuff.Prop4 = "bbb"
    assertEquals( "bbb", stuff._Prop4 )
  }

  static abstract class AbstractClass
  {
    abstract property Foo: String
  }

  static class ImplClass extends AbstractClass
  {
    override property Foo: String = "hi"
  }

  function testOverride()
  {
    var impl = new ImplClass()
    assertEquals( "hi", impl.Foo )
    assertEquals( "hi", impl._Foo )
    impl.Foo = "bye"
    assertEquals( "bye", impl.Foo )
    assertEquals( "bye", impl._Foo )
  }
}