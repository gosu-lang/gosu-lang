package gw.specContrib.generics

uses gw.test.TestClass
uses java.awt.Point
uses javax.swing.JButton
uses java.math.BigDecimal

class GenericStructuresTest extends TestClass {

  function testMe() {
    var value = max( 1, 2 )
    assertTrue( value typeis Integer )
    assertEquals( 2, value )
  }

  function max<T extends Comparable<T>>( p1: T, p2: T ) : T {
    return p1.compareTo( p2 ) == 1 ? p1 : p2
  }

  structure Comparable<T> {
    function compareTo( t: T ): int
  }

  //------------

  function testGenericStructureNotRecursiveOnlyCovariant() {
    var javaPt = new Point(1, 2)
    var javaPt_loc = reflectLocation( javaPt )
    assertSame( Location<Double>, statictypeof javaPt_loc )
    assertEquals( javaPt_loc.X, javaPt.X )
    assertEquals( javaPt_loc.Y, javaPt.Y )

    var javaComp = new JButton()
    javaComp.setLocation( 6, 7 )
    var javaComp_loc = reflectLocation( javaComp )
    assertSame( Location<Integer>, statictypeof javaComp_loc )
    assertEquals( javaPt_loc.X, javaPt.X )
    assertEquals( javaPt_loc.Y, javaPt.Y )

    var gosuPt = new GosuPoint() {:X = 7, :Y = 8}
    var gosuPt_loc = reflectLocation( gosuPt )
    assertSame( Location<BigDecimal>, statictypeof gosuPt_loc )
    assertEquals( gosuPt_loc.X, gosuPt.X )
    assertEquals( gosuPt_loc.Y, gosuPt.Y )
  }

  function reflectLocation<T extends Number>( loc: Location<T> ) : Location<T> {
    return loc
  }

  static class GosuPoint {
    var _x: BigDecimal as X
    var _y: BigDecimal as Y
  }

  structure Location<T extends Number>  {
    property get X() : T
    property get Y() : T
  }

  //------------

  function testFoo() {
    var l : List<Foo> = {null}
    var x = sort<Foo>( l )
    var y = sort( l )

    var z = sort( new Foo() )

    var w: Comparable<Foo> = new Foo()
    w.compareTo( new Foo() )

    var v1 = sort3( new Foo() )
    assertTrue( Foo == statictypeof v1 )

    var v2 = sort3( "helo" )
    assertTrue( String == statictypeof v2 )

    var v3 = sort3( new FooString() )
    assertTrue( String == statictypeof v3 )

    var v4 = sort3( new FooNominal() )
    assertTrue( FooNominal == statictypeof v4 )

    var v5 = sort3( new FooJavaNominal() )
    assertTrue( FooJavaNominal == statictypeof v5 )
  }

  static function sort<T extends Comparable<T>>( list: List<T> ): T {
    return null
  }
  static function sort<T extends Comparable<T>>( t: T ): T {
    return null
  }
  static function sort3<T>( t: Comparable<T> ): T {
    return null
  }

  static class Foo {
    function compareTo( f: Foo ): int {
      return 0
    }
  }

  static class FooString {
    function compareTo( f: String ): int {
      return 0
    }
  }

  static class FooNominal implements Comparable<FooNominal> {
    function compareTo( f: FooNominal ): int {
      return 0
    }
  }

  static class FooJavaNominal implements java.lang.Comparable<FooJavaNominal> {
    function compareTo( f: FooJavaNominal ): int {
      return 0
    }
  }

  //-------------

  function testStuff()  {
    var fooObject: List<FooObject>
    var v1 = sort( fooObject )
    assertTrue( FooObject == statictypeof v1 )

    var fooFoo: List<FooFoo>
    var v2 = sort( fooFoo )
    assertTrue( FooFoo == statictypeof v2 )

    var fooRunnable: List<FooRunnable>
    var v3 = sort( fooRunnable )
    assertTrue( FooRunnable == statictypeof v3 )

    var fooObjectRaw: List<FooObjectRaw>
    var v4 = sort( fooObjectRaw )
    assertTrue( FooObjectRaw == statictypeof v4 )

    var fooFooRaw: List<FooFooRaw>
    var v5 = sort( fooFooRaw )
    assertTrue( FooFooRaw == statictypeof v5 )

    var fooRunnableRaw: List<FooRunnableRaw>
    var v6 = sort( fooRunnableRaw )
    assertTrue( FooRunnableRaw == statictypeof v6 )
  }

  static class FooObject implements Comparable<Object> {
    override function compareTo(o: Object) : int {
      return 0;
    }
  }

  static class FooFoo implements Comparable<FooFoo> {
    override function compareTo(o: FooFoo) : int {
      return 0;
    }
  }

  static class FooRunnable implements Runnable, Comparable<Runnable> {
    override function compareTo(o: Runnable): int {
      return 0;
    }
    override function run() {
    }
  }

  static class FooObjectRaw {
    function compareTo(o: Object) : int {
      return 0;
    }
  }

  static class FooFooRaw {
    function compareTo(o: FooFooRaw) : int {
      return 0;
    }
  }

  static class FooRunnableRaw implements Runnable {
    function compareTo(o: Runnable): int {
      return 0;
    }
    override function run() {
    }
  }

  //------------

  function testInferenceAcrossArgsStructureUsesContravariance()  {
    var v1 = addAll( new HiObject(), {new Fap()} )
    assertTrue( Fap == statictypeof v1 ) // contravariance Object + Fap = Fap

    var v2 = addAll( new HiFap(), {new Fap()} )
    assertTrue( Fap == statictypeof v2 ) // contravariance Fap + Fap = fFap
  }

  reified function addAll<T>( c: Comparable<T>, l: List<T> ): T {
    l.each( \e-> c.compareTo( e ) )
    return l.first()
  }

  class HiObject implements Comparable<Object> {
    override function compareTo(t: Object): int {
      return 0
    }
  }

  class HiFap implements Comparable<Fap> {
    override function compareTo(t: Fap): int {
      return 0
    }
  }

  function testInferenceAcrossArgsInterfaceNoContravariance()  {
    var v1 = addAll_java( new HiObject_java(), {new Fap()} )
    assertTrue( Object == statictypeof v1 ) // covariance Object + Fap = Object

    var v2 = addAll_java( new HiFap_java(), {new Fap()} )
    assertTrue( Fap == statictypeof v2 ) // covariance Fap + Fap == Fap
  }

  reified function addAll_java<T>( c: java.lang.Comparable<T>, l: List<T> ): T {
    l.each( \e-> c.compareTo( e ) )
    return l.first()
  }

  class HiObject_java implements java.lang.Comparable<Object> {
    override function compareTo(t: Object): int {
      return 0
    }
  }

  class HiFap_java implements java.lang.Comparable<Fap> {
    override function compareTo(t: Fap): int {
      return 0
    }
  }

  class Fap {
  }

  //------------

  function testOptimalStructuralInference() {
    var v1 = new SampleImpl()
    var v2 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )

    var v3 = new SampleImpl2()
    var v4 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )

    var v5 = new SampleImpl3()
    var v6 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )

    var v7 = new SampleImpl4()
    var v8 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )

    var v9 = new SampleImpl5()
    var v10 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )

    var v11 = new SampleImpl6()
    var v12 = callme( v1 )
    assertTrue( Sample<CharSequence> == statictypeof v2 )
  }

  function callme<T>( t: Sample<T> ) : Sample<T> {
    return t
  }

  class SampleImpl {
    function foo( o: Object ) {}
    function baz() : CharSequence { return null }
    function bar() : String { return null }
  }

  class SampleImpl2 {
    function baz() : CharSequence { return null }
    function foo( o: Object ) {}
    function bar() : String { return null }
  }

  class SampleImpl3 {
    function baz() : CharSequence { return null }
    function bar() : String { return null }
    function foo( o: Object ) {}
  }

  class SampleImpl4 {
    function bar() : String { return null }
    function baz() : CharSequence { return null }
    function foo( o: Object ) {}
  }

  class SampleImpl5 {
    function bar() : String { return null }
    function foo( o: Object ) {}
    function baz() : CharSequence { return null }
  }

  class SampleImpl6 {
    function foo( o: Object ) {}
    function bar() : String { return null }
    function baz() : CharSequence { return null }
  }

  structure Sample<T> {
    function foo( t: T )
    function bar() : T
    function baz() : T
  }


  structure FooArrayComponent {
    function foo()
  }
  class BarArrayComponent { // implements FooArrayComponent structurally
    function foo() {}
  }
  function testDynamicArrayCreationErasesStructureType() {
    var list : List<FooArrayComponent> = {}
    var bar = new BarArrayComponent()
    list.add( bar )
    assertArrayEquals( new Object[]{bar}, list.toTypedArray() )
  }
}