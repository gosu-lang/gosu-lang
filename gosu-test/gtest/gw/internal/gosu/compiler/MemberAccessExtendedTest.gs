package gw.internal.gosu.compiler

uses gw.test.TestClass

class MemberAccessExtendedTest extends TestClass {

  //====================================================
  //  Helper classes
  //====================================================
  static class Base {
    static property get Foo() : String {
      return "from Class1"
    }
  }

  static class ExtendsBase extends Base {
  }

  static class ExtendsExtendsBase extends ExtendsBase {
    static property get Foo() : String {
      return "from ExtendsExtendsBase"
    }
  }
  
  static class ExtendsExtendsExtendsBase extends ExtendsExtendsBase {
  }
  
  //====================================================
  //  Tests
  //====================================================

  function testBasicStaticMemberAccessWorks() {
    assertEquals( "from Class1", Base.Foo )
    assertEquals( "from Class1", ExtendsBase.Foo )
    assertEquals( "from ExtendsExtendsBase", ExtendsExtendsBase.Foo )
    assertEquals( "from ExtendsExtendsBase", ExtendsExtendsExtendsBase.Foo )
  }

  function testStaticMemberAccessWorksWithNonStaticRootExpr() {
    assertEquals( "from Class1", new Base().Foo )
    assertEquals( "from Class1", new ExtendsBase().Foo )
    assertEquals( "from ExtendsExtendsBase", new ExtendsExtendsBase().Foo )
    assertEquals( "from ExtendsExtendsBase", new ExtendsExtendsExtendsBase().Foo )
  }

  function testStaticMemberAccessResolvesStaticallyEvenWithDynamicExpression() {
    assertEquals( "from Class1", new Base().Foo )
    assertEquals( "from Class1", (new ExtendsBase() as Base).Foo )
    assertEquals( "from Class1", (new ExtendsExtendsBase() as Base).Foo )
    assertEquals( "from Class1", (new ExtendsExtendsBase() as ExtendsBase).Foo )
    assertEquals( "from ExtendsExtendsBase", new ExtendsExtendsBase().Foo )
    assertEquals( "from Class1", (new ExtendsExtendsExtendsBase() as Base).Foo )
    assertEquals( "from Class1", (new ExtendsExtendsExtendsBase() as ExtendsBase).Foo )
    assertEquals( "from ExtendsExtendsBase", (new ExtendsExtendsExtendsBase() as ExtendsExtendsBase).Foo )
    assertEquals( "from ExtendsExtendsBase", new ExtendsExtendsExtendsBase().Foo )
  }

  function testStaticMemberAccessEvaluatesBaseExpressionIfItIsNonStatic() {
    var created = 0
    
    // blocks to track how many times they are evaluated 
    var makesBase = \-> { created++ return new Base() }
    var makesExtends = \-> { created++ return new ExtendsBase() }
    var makesExtendExtends = \-> { created++ return new ExtendsExtendsBase() }
    var makesExtendExtendExtends = \-> { created++ return new ExtendsExtendsExtendsBase() }

    assertEquals( "from Class1", makesBase().Foo )
    assertEquals( 1, created )
    assertEquals( "from Class1", (makesBase() as Base).Foo )
    assertEquals( 2, created )
    assertEquals( "from Class1", (makesExtends() as Base).Foo )
    assertEquals( 3, created )
    assertEquals( "from Class1", (makesExtendExtends() as ExtendsBase).Foo )
    assertEquals( 4, created )
    assertEquals( "from ExtendsExtendsBase", makesExtendExtends().Foo )
    assertEquals( 5, created )
    assertEquals( "from Class1", (makesExtendExtendExtends() as Base).Foo )
    assertEquals( 6, created )
    assertEquals( "from Class1", (makesExtendExtendExtends() as ExtendsBase).Foo )
    assertEquals( 7, created )
    assertEquals( "from ExtendsExtendsBase", (makesExtendExtendExtends() as ExtendsExtendsBase).Foo )
    assertEquals( 8, created )
    assertEquals( "from ExtendsExtendsBase", makesExtendExtendExtends().Foo )
    assertEquals( 9, created )
  }

  function testStaticAccessOfTypeInfoPropertiesWorks() {
    assertEquals( Base, Base.Type )
    assertTrue( Base.Valid )         // deprecated direct access
    assertTrue( Base.getValid() )         // deprecated direct access
    assertTrue( Base.Type.Valid )    // non-deprecated access through Type prop
    assertTrue( Base.Type.getValid() )    // non-deprecated access through Type prop
    assertTrue( Base.Type.Static )   // type specific property through Type prop
    assertTrue( Base.Type.getStatic() )   // type specific property through Type prop
  }

  //TODO AHK - this should be part of the test suite for method invocation
  function testStaticAccessOfTypeInfoMethodsWorks() {
    assertTrue( Base.isAssignableFrom( Base ) )         // deprecated direct access
    assertTrue( Base.Type.isAssignableFrom( Base ) )    // non-deprecated access through Type prop
    assertFalse( Base.Type.hasError() )                 // type specific method through Type prop
  }

}