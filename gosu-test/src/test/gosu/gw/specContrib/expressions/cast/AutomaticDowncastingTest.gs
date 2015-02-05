package gw.specContrib.expressions.cast


uses gw.BaseVerifyErrantTest
uses gw.specContrib.expressions.cast.res.C
uses gw.specContrib.expressions.cast.res.B
uses gw.specContrib.expressions.cast.res.A
uses java.lang.Integer
uses java.util.ArrayList

/**
 * Created by Sky on 2015/02/02 with IntelliJ IDEA.
 */

class AutomaticDowncastingTest extends BaseVerifyErrantTest{



  function testAutoDowncastingInDepreatedProperty() {
    var h = new C()
    if(h.deprecatedProp typeis B) {
      assertEquals(statictypeof(h.deprecatedProp), gw.specContrib.expressions.cast.res.B)
      assertEquals(h.deprecatedProp.bar(), "test")
    }
    assertEquals(statictypeof(h.deprecatedProp), gw.specContrib.expressions.cast.res.A)
  }


  function testAutoDowncastingInDepreatedMethod() {
    var h = new C()
    var h1 = h.deprecatedMethod()
    if(h1 typeis B){
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.B)
      assertEquals(h1.bar(), "test")
    }
    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)

    if(h.deprecatedMethod() typeis B) {
      assertEquals(statictypeof(h.deprecatedMethod()), gw.specContrib.expressions.cast.res.A)    // By design, auto-downcasting works for property and field, but not method
    }
  }


  function testAutoDowncastingInDepreatedField() {
    var h = new C()

    if(h.deprecatedField typeis B) {
      assertEquals(statictypeof(h.deprecatedField), gw.specContrib.expressions.cast.res.B)
      assertEquals(h.deprecatedField.bar(), "test")
    }
    assertEquals(statictypeof(h.deprecatedField), gw.specContrib.expressions.cast.res.A)
  }


  function testAutoDowncastingBasic() {
    var h1 : A = new B()
    if(h1 typeis B) {
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.B)
      assertEquals(h1.bar(), "test")
    }
    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)


  }


  function testAutoDowncastingInElseClause() {
    var h1 : A = new B()
    if(h1 typeis B && 1 != 0 ) {
    }else{
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)         // By design, automatic downcasting only works in IF statement and downcasting ends when reaches the end of IF statement
    }

    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)
  }


  function testAutoDowncastingWithOrInLogicalExpression() {
    var h1 : A = new B()
    if(h1 typeis B || 1 == 1) {
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)         // By design, automatic downcasting will be cancelled with OR keyword in the logical expressions
    }

    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)
  }


  function testAutoDowncastingWithNotInLogicalExpression() {
    var h1 : A = new B()

    if(!(h1 typeis B && 1==0)) {
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)         // By design, automatic downcasting will be cancelled with NOT keyword in the logical expression
    }else{
    }
    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)


    if(!(h1 typeis B && 1==1)) {
    }else{
      assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)         // By design, automatic downcasting will be cancelled with NOT keyword in the logical expression
    }
    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)
  }


  function testAutoDowncastingInTernaryConditionalExpression(){
    var h1 : A = new B()
    assertEquals(statictypeof(h1), gw.specContrib.expressions.cast.res.A)

    var str = h1 typeis B ? h1.bar() : "automatic downcasting is not working"
    assertEquals(str, "test")

    var objStaticType1 = h1 typeis B ? statictypeof(h1) : statictypeof(h1)
    assertEquals(objStaticType1, gw.specContrib.expressions.cast.res.B)

    var objStaticType5 = (h1 typeis B && 1 == 1)? statictypeof(h1) : statictypeof(h1)
    assertEquals(objStaticType5, gw.specContrib.expressions.cast.res.B)

    // 'not' keyword
    var objStaticType4 = (!(h1 typeis B && 1 == 0)) ? statictypeof(h1) : statictypeof(h1)
    assertEquals(objStaticType4, gw.specContrib.expressions.cast.res.A)

    // else clause
    var objStaticType2 = (h1 typeis B && 1 == 0)? statictypeof(h1) : statictypeof(h1)
    assertEquals(objStaticType2, gw.specContrib.expressions.cast.res.A)

    // 'or' keyword
    var objStaticType3 = (h1 typeis B || 1 == 1) ? statictypeof(h1) : statictypeof(h1)
    assertEquals(objStaticType3, gw.specContrib.expressions.cast.res.A)
  }


  // test 'typeof' in switch statement
  function testAutoDowncastingInSwitchStatement(){
    var x : Object = "neat"
    switchStatementForTest(x)

    var y : Object = new ArrayList<String>({"one", "two", "three"})
    switchStatementForTest(y)
  }


  // test 'typeof' in switch statement with no 'break'
  function testAutoDowncastingWithNoBreakInSwitchStatement(){
    var x : Object = "neat"
    switchStatementForTest(x)
  }





  private function switchStatementForTest(x : Object){
    switch( typeof(x) ){
      case String :
          assertEquals(statictypeof(x), java.lang.String)
          assertEquals(x.charAt(0), 'n')
          break
      case ArrayList<Integer> :
          assertEquals(statictypeof(x), java.util.ArrayList)
          assertEquals(x.size(), 3)
          break
    }

    assertEquals(statictypeof(x), java.lang.Object)
  }


  private function switchStatementWithNoBreakForTest(x : Object){
    switch( typeof(x) ){
      case String :
          assertEquals(statictypeof(x), java.lang.String)
          assertEquals(x.charAt(0), 'n')
      case ArrayList<Integer> :
          assertEquals(statictypeof(x), java.lang.String)
          break
    }

    assertEquals(statictypeof(x), java.lang.Object)
  }



}

