package gw.specContrib.typeinference

uses java.io.Writer
uses java.util.Date
uses java.util.List
uses java.util.Map
uses java.lang.Integer
uses java.math.BigDecimal

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
      l.get(0).foo1()                //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }

    var arr: Object[]
    if (arr[0] typeis A) {
      arr[0].foo1()                 //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
    var ind: int
    if (arr[ind] typeis A) {
      arr[ind].foo1()               //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }

    var map: Map<String, Object>
    if (map["key"] typeis A) {
      map["key"].foo1()             //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
    var key: String
    if (map[key] typeis A) {
      map[key].foo1()               //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
  }

  property get Prop(): Object { return null }

  function testProperties(c: Errant_IfTypeNarrowingJava) {
    if (c.nonProp() typeis Errant_IfTypeNarrowingJava.A) {
      c.nonProp().foo()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
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

    // Gosu property (no longer supporting fake getter calls, only Java now)
    if (getProp() typeis Errant_IfTypeNarrowingJava.A) {  //## issuekeys: MSG_NO_SUCH_FUNCTION
      getProp().foo()  //## issuekeys: MSG_NO_SUCH_FUNCTION
    }
    if (getProp() typeis Errant_IfTypeNarrowingJava.A) {  //## issuekeys: MSG_NO_SUCH_FUNCTION
      Prop.foo()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
    if (Prop typeis Errant_IfTypeNarrowingJava.A) {
      getProp().foo()  //## issuekeys: MSG_NO_SUCH_FUNCTION
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
  class A { function write() : int { return 8 } }
  class C extends A { }

  function testTypeisCompatibility() {
    var B : Boolean = true
    var x = B typeis Integer             //## issuekeys: THE TYPEIS OPERATOR CANNOT BE APPLIED: 'JAVA.LANG.BOOLEAN' TYPEIS 'JAVA.LANG.INTEGER'
    var y = B as Integer            // This is good

    var o : Object
    var b : boolean

    o = \ ->  {}
    b = o typeis Runnable
    b = o typeis  block()      // This is good

    o = new A()
    b = o typeis Writer
    b = o typeis dynamic.Dynamic

    var str : String = "123"
    b = str typeis dynamic.Dynamic    // This is good

    o = new LinkedList()
    b = o typeis Deque & List
  }

  function testTypeis() {
    var x: Dynamic = "Hello"
    var y = x typeis String   // This is good
    var b : boolean
    var c: String & Comparator
    b = c typeis Integer        //## issuekeys: THE TYPEIS OPERATOR CANNOT BE APPLIED: 'JAVA.LANG.STRING & JAVA.UTIL.COMPARATOR' TYPEIS 'JAVA.LANG.INTEGER'
    b = c typeis String         //## issuekeys: THE TYPEIS OPERATOR CANNOT BE APPLIED: 'JAVA.LANG.STRING & JAVA.UTIL.COMPARATOR' TYPEIS 'JAVA.LANG.STRING'
    b = c typeis  CharSequence

    var i: Integer
    b = i typeis String & Comparator        //## issuekeys: THE TYPEIS OPERATOR CANNOT BE APPLIED: 'JAVA.LANG.INTEGER' TYPEIS 'JAVA.LANG.STRING & JAVA.UTIL.COMPARATOR'

    var d : C
    var e : A
    b = d typeis C
    b = d typeis A
    b = e typeis A
    b = e typeis C
  }
}

// IDE-4028
  abstract class Ide4028 implements List<String[]> {

    function hello() {
        var i: List<Object>
        var j: int

        if (i typeis String) {}        //## issuekeys: THE TYPEIS OPERATOR CANNOT BE APPLIED: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>' TYPEIS 'JAVA.LANG.STRING'
        if (i typeis Set) {}
        if (i typeis Ide4028) {}  // Good
        if (i typeis BigDecimal) {}
        if (i typeis List<Object[]>) {}
    }

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
