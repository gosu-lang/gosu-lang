package gw.specification.expressions.conditionalExpressions

uses java.util.*
uses java.lang.*
uses java.io.Serializable
uses java.lang.Cloneable
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_ConditionalExpressionsTest {

  function testConditionalExpressions() {
    var c0 : int = true ? 1 : 0
    var c1 : int = new Boolean(true) ? 1 : 0
    var c2 : int = 8 ? 1 : 0  //## issuekeys: MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN
    var c3 : int = new Object() ? 1 : 0  //## issuekeys: MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN, MSG_TYPE_MISMATCH
    var c4 : int = "true" ? 1 : 0  //## issuekeys: MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN, MSG_TYPE_MISMATCH
    var c5 : Object = true ? new LinkedList() : 3.4
    var c6 : AbstractMap = true ? new TreeMap() : new HashMap()
    var c7 : Serializable = true ? new LinkedList() : new HashMap()
    var c8 : Serializable & Cloneable = true ? new LinkedList() : new HashMap()
    var run : Runnable =  \-> {}
    var c10 : Runnable = true ? run : \-> {}    //## issuekeys: MSG_TYPE_MISMATCH
    var c11 : int = 'c' ? 1 : 0   //## issuekeys: MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN
    var c12 : int[] = true ? 8 : {1, 2, 3}  //## issuekeys: MSG_TYPE_MISMATCH
    var c13 : Object = true ? 8 : {1, 2, 3}
    var r : int
    var x = 0
    var setX = \ y : int -> {
      x = y
      return 0
    }

    r = true ? setX(1) : setX(2)
    x = 0
    r = false ? setX(1) : setX(2)
  }

  function testConditionalExpressionsWithPrimitiveAndBoxedTypes() {
    var b : byte  = 0b
    var s : short = 0
    var L : long = 0L
    var i : int = 0
    var t : boolean = true
    var f : float = 1.0f
    var d : double = 0.0
    var c : char = ' '
    var o : Object = null
    var bi : BigInteger
    var bd : BigDecimal

    t = true ? t : new Boolean(t)
    o = true ? t : c  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : b  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : s  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : i  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : L  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : f  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : d  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Character(c)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Byte(b)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Short(s)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Integer(i)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Long(L)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? t : new Double(d)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? c : b  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? c : s  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? c : i
    L = true ? c : L
    f = true ? c : f
    d = true ? c : d
    c = true ? c : new Character(c)
    o = true ? c : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? c : new Byte(b)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? c : new Short(s)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? c : new Integer(i)
    L = true ? c : new Long(L)
    f = true ? c : new Float(f)
    d = true ? c : new Double(d)
    s = true ? b : s
    i = true ? b : i
    L = true ? b : L
    f = true ? b : f
    d = true ? b : d
    o = true ? b : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? b : new Character(c)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    b = true ? b : new Byte(b)
    s = true ? b : new Short(s)
    i = true ? b : new Integer(i)
    L = true ? b : new Long(L)
    f = true ? b : new Float(f)
    d = true ? b : new Double(d)
    i = true ? s : i
    L = true ? s : L
    f = true ? s : f
    d = true ? s : d
    o = true ? s : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? s : new Character(c)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    s = true ? s : new Byte(b)
    s = true ? s : new Short(s)
    i = true ? s : new Integer(i)
    L = true ? s : new Long(L)
    f = true ? s : new Float(f)
    d = true ? s : new Double(d)
    L = true ? i : L
    f = true ? i : f  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? i : d
    o = true ? i : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? i : new Character(c)
    i = true ? i : new Byte(b)
    i = true ? i : new Short(s)
    i = true ? i : new Integer(i)
    L = true ? i : new Long(L)
    f = true ? i : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? i : new Double(d)
    f = true ? L : f  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? L : d  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? L : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    L = true ? L : new Character(c)
    L = true ? L : new Byte(b)
    L = true ? L : new Short(s)
    L = true ? L : new Integer(i)
    L = true ? L : new Long(L)
    f = true ? L : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? L : new Double(d)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? f : d
    o = true ? f : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    f = true ? f : new Character(c)
    f = true ? f : new Byte(b)
    f = true ? f : new Short(s)
    f = true ? f : new Integer(i)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    f = true ? f : new Long(L)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    f = true ? f : new Float(f)
    d = true ? f : new Double(d)
    o = true ? d : new Boolean(t)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? d : new Character(c)
    d = true ? d : new Byte(b)
    d = true ? d : new Short(s)
    d = true ? d : new Integer(i)
    d = true ? d : new Long(L)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? d : new Float(f)
    d = true ? d : new Double(d)
    o = true ? new Boolean(t) : new Character(c)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Byte(b)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Short(s)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Integer(i)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Long(L)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    o = true ? new Boolean(t) : new Double(d)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? new Character(c) : new Byte(b)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    c = true ? new Character(c) : new Short(s)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    i = true ? new Character(c) : new Integer(i)
    L = true ? new Character(c) : new Long(L)
    f = true ? new Character(c) : new Float(f)
    d = true ? new Character(c) : new Double(d)
    s = true ? new Byte(b) : new Short(s)
    i = true ? new Byte(b) : new Integer(i)
    L = true ? new Byte(b) : new Long(L)
    f = true ? new Byte(b) : new Float(f)
    d = true ? new Byte(b) : new Double(d)
    i = true ? new Short(s) : new Integer(i)
    L = true ? new Short(s) : new Long(L)
    f = true ? new Short(s) : new Float(f)
    d = true ? new Short(s) : new Double(d)
    L = true ? new Integer(i) : new Long(L)
    f = true ? new Integer(i) : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? new Integer(i) : new Double(d)
    f = true ? new Long(L) : new Float(f)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? new Long(L) : new Double(d)  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    d = true ? new Float(f) : new Double(d)
    bi = true ? t : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bi = true ? t : new BigDecimal("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bi = true ? c : new BigInteger("1")
    bd = true ? c : new BigDecimal("1")
    bi = true ? b : new BigInteger("1")
    bd = true ? b : new BigDecimal("1")
    bi = true ? s : new BigInteger("1")
    bd = true ? s : new BigDecimal("1")
    bi = true ? i : new BigInteger("1")
    bd = true ? i : new BigDecimal("1")
    bi = true ? L : new BigInteger("1")
    bd = true ? L : new BigDecimal("1")
    bi = true ? f : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bd = true ? f : new BigDecimal("1")
    bd = true ? d : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bd = true ? d : new BigDecimal("1")
    bd = true ? new Boolean(t) : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bd = true ? new Boolean(t) : new BigDecimal("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bi = true ? new Character(c) : new BigInteger("1")
    bd = true ? new Character(c) : new BigDecimal("1")
    bi = true ? new Byte(b) : new BigInteger("1")
    bd = true ? new Byte(b) : new BigDecimal("1")
    bi = true ? new Short(s) : new BigInteger("1")
    bd = true ? new Short(s) : new BigDecimal("1")
    bi = true ? new Integer(i) : new BigInteger("1")
    bd = true ? new Integer(i) : new BigDecimal("1")
    bi = true ? new Long(L) : new BigInteger("1")
    bd = true ? new Long(L) : new BigDecimal("1")
    bd = true ? new Float(f) : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bd = true ? new Float(f) : new BigDecimal("1")
    bd = true ? new Double(d) : new BigInteger("1")  //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    bd = true ? new Double(d) : new BigDecimal("1")
    bd = true ? new BigInteger("1") : new BigDecimal("1")
    bi = true ? new BigInteger("1") : null
    bd = true ? new BigDecimal("1") : null
  }

  function testConditionalExpressionsWithNull() {
    var b : byte  = 0b
    var s : short = 0
    var L : long = 0L
    var i : int = 0
    var t : boolean = true
    var f : float = 1.0f
    var d : double = 0.0
    var c : char = ' '
    var o : Object = null
    var str : String

    i = null ? 1 : 0  //## issuekeys: MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN, MSG_TYPE_MISMATCH
    str = true ? "hello" : null
    t = true ? t : null
    c = true ? c : null
    b = true ? b : null
    s = true ? s : null
    i = true ? i : null
    L = true ? L : null
    f = true ? f : null
    d = true ? d : null
    t = true ? new Boolean(t) : null
    c = true ? new Character(c) : null
    b = true ? new Byte(b) : null
    s = true ? new Short(s) : null
    i = true ? new Integer(i) : null
    L = true ? new Long(L) : null
    f = true ? new Float(f) : null
    d = true ? new Double(d) : null
    var list : LinkedList = true ? new LinkedList() : null
    o  = true ? null : null
    var xx  = true ? null : null  //## issuekeys: MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE
    callMe(true ? null : null)
  }

  function callMe(s : String) {}

  function testShorthandConditionalExpressions() {
    var cond : String = "true"
    var s : String = "false"
    var r : String

    r = cond != null ? cond : s
    r = cond ?: s
    cond = null
    r = cond != null ? cond : s
    r = cond ?: s

    var i : int = 123
    var r1 = 123 ?: 1  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    r1 = i ?: 1  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
  }

  // IDE-327
  abstract class IDE_327 {

    function a() {
      var x = 5
      var y = 6
      var xxx = x > y ? 42 : "hello world!"
      var yyy = x > y ? false : "hello world!"
      var zzz = x > y ? false : 42      //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
    }

    abstract function foo<T>(p1: T, p2: T): T

    function test(x: int, y: int) {
      var a: int
      var b: String
      var c1: Serializable = foo(a, b)
      var c2: Serializable = x > y ? a : b
    }
  }

  // IDE-451
  class IDE_451 {
    class A {}
    class B extends A {}
    class C extends A {}

    var mytype = false ? new C() : new B()
    var someonestype = false ? new Float(42.5) : new ArrayList()
    var yourtype = true ? new Float(42.5) : new Integer(42)       //## issuekeys: MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP
  }

  // ISSTUDIO-445
  var blockThatShouldReturnObject : block() : Object = \ -> {
    if (true) {
      return Boolean.TRUE
    } else if (true) {
      return Integer.valueOf(0)
    } else {
      return ""
    }
  }
 

}
