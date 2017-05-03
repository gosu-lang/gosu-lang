package gw.specContrib.typeinference

uses java.util.Date
uses java.util.List
uses java.util.Map
uses java.lang.Integer

class Errant_IfTypeNarrowing {
  interface I1 {
    function foo1()
  }

  interface I2 {
    function foo2()
  }

  class A implements I1 {
     override function foo1() {}

     property get Prop(): boolean { return false }
  }

  class B implements I2 {
    override function foo2() {}
  }

  function test() {
    var x: Object = "neat"

    if (x typeis String) {
      print(x.charAt(0))
    } else if (x typeis Date) {
      print(x.Time)
    }

    // IDE-2031
    if (x typeis I1) {
      if (x typeis I2) {
        x.foo1()
        x.foo2()
      }
    }

    if (x typeis I1 && x typeis I2) {
      x.foo1()
      x.foo2()
    }

    // IDE-2249
    if (x typeis I2 && x typeis A && x.Prop) {}

    var i1: I1
    if (i1 typeis I2) {
      i1.foo1()
      i1.foo2()
    }

    var l: List<Object>
    // IDE-2131
    if (l.get(0) typeis A &&
        l.get(1) typeis B) {
    }

    if (l.get(0) typeis A) {
      l.get(0).foo1()                //## issuekeys: CANNOT RESOLVE 'foo1()'
    }

    var arr: Object[]
    if (arr[0] typeis A) {
      arr[0].foo1()                 //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
    var ind: int
    if (arr[ind] typeis A) {
      arr[ind].foo1()               //## issuekeys: CANNOT RESOLVE 'foo1()'
    }

    var map: Map<String, Object>
    if (map["key"] typeis A) {
      map["key"].foo1()             //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
    var key: String
    if (map[key] typeis A) {
      map[key].foo1()               //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
  }

  property get Prop(): Object { return null }

  function testProperties(c: Errant_IfTypeNarrowingJava) {
    if (c.nonProp() typeis Errant_IfTypeNarrowingJava.A) {
      c.nonProp().foo()  //## issuekeys: CANNOT RESOLVE 'foo()'
    }

    // Java property
    // IDE-2278
    if (c.getProp() typeis Errant_IfTypeNarrowingJava.A) {
      c.getProp().foo()
    }
    if (c.getProp() typeis Errant_IfTypeNarrowingJava.A) {
      c.Prop.foo()
    }
    if (c.Prop typeis Errant_IfTypeNarrowingJava.A) {
      c.getProp().foo()
    }
    if (c.Prop typeis Errant_IfTypeNarrowingJava.A) {
      c.Prop.foo()
    }

    // Gosu property
    if (getProp() typeis Errant_IfTypeNarrowingJava.A) {
      getProp().foo()
    }
    if (getProp() typeis Errant_IfTypeNarrowingJava.A) {
      Prop.foo()
    }
    if (Prop typeis Errant_IfTypeNarrowingJava.A) {
      getProp().foo()
    }
    if (Prop typeis Errant_IfTypeNarrowingJava.A) {
      Prop.foo()
    }
  }
  // PL-35195
  class CA7HiredAutoScheduledItemExpression extends DataBuilderExpression<CA7LineSchedCovItemBuilder> {

    construct() {
      super(new CA7LineSchedCovItemBuilder())
    }

    function with(clauseExpression : DataBuilderExpression) {
      if (clauseExpression typeis DataBuilderExpression<CoverageBuilder>) {
        var foo: CoverageBuilder = clauseExpression.DataBuilder

      }
    }

  }

  // IDE-3196
  class Ide3196 {
    var b : Boolean = true
   var x = (b typeis Integer)       //## issuekeys: !INVALID.TYPEIS.NARROWING.CAST!
    var y = b as Integer // Good: Here we cast Boolean to Integer and OS Gosu says it's okay!!
  }

  static  abstract class DataBuilderExpression<T extends DataBuilder> {

    protected var _builder : T

    construct(builder : T) {
      _builder = builder
    }

    property get DataBuilder() : T {
      return _builder
    }
  }


  static public abstract class DataBuilder<B extends DataBuilder<B>> {}

  static  public class CA7LineSchedCovItemBuilder extends DataBuilder<CA7LineSchedCovItemBuilder> {}
  static  public class CoverageBuilder extends DataBuilder<CoverageBuilder>  {}

}
