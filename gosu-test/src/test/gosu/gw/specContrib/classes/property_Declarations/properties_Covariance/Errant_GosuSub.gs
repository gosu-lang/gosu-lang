package gw.specContrib.classes.property_Declarations.properties_Covariance

class Errant_GosuSub
{
  static class GosuSub implements IJavaSub
  {
    override property get Parent(): GosuSub { return null }
    override property set Parent( j: IJavaBase ) {}

    override function setParent( j: IJavaSub ) {}

    override property set Goober( j: IJavaSub ) {}
    
    function testMe()
    {
      var b: IJavaBase
      var j: IJavaSub
      var g: GosuSub
      
      Parent = b
      Parent = j
      Parent = g
      
      b = Parent
      j = Parent
      g = Parent
    }
  }

  static class GosuSub2 implements IJavaSub
  {
    override property Parent: IJavaBase

    override function setParent( j: IJavaSub ) {}

    override property set Goober( j: IJavaSub ) {}
    
    function testMe()
    {
      var b: IJavaBase
      var j: IJavaSub
      var g: GosuSub2
      
      Parent = b
      Parent = j
      Parent = g
      
      b = Parent
      j = Parent  //## issuekeys: MSG_TYPE_MISMATCH
      g = Parent  //## issuekeys: MSG_TYPE_MISMATCH
    }
  }

  function testJavaInterfaces()
  {
    var b: IJavaBase
    var j: IJavaSub
    var g: GosuSub

    b.Parent = b
    b.Parent = j
    b.Parent = g

    j.Parent = b
    j.Parent = j
    j.Parent = g
    j.setParent( b ) // goes to property in IJavaBase
    j.setParent( j ) // goes to method in IJavaSub

    g.Parent = b // contravariance
    g.Parent = j // contravariance
    g.Parent = g

    g.setParent( b )  //## issuekeys: MSG_TYPE_MISMATCH
    g.setParent( j )
    g.setParent( g )
  }

  class GosuSubClass extends JavaSub
  {
    override property get Foo() : GosuSubClass { return null }
    override property set Foo( js: JavaBase ) {}
  }

  function testJavaClasses()
  {
    var b: JavaBase
    var s: JavaSub
    var g: GosuSubClass

    b.Foo = b
    b.Foo = s
    b.Foo = g

    s.Foo = b // goes to property in JavaBase
    s.Foo = s // "
    s.Foo = g // "
    s.setFoo( b ) // "
    s.setFoo( s ) // goes to method in JavaSub

    g.Foo = b  // contravariance
    g.Foo = s  // contravariance
    g.Foo = g
    g.setFoo( b )  //## issuekeys: MSG_TYPE_MISMATCH
    g.setFoo( s ) // goes to method in JavaSub
    g.setFoo( g ) // goes to method in JavaSub
  }
}
