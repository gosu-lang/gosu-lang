package gw.spec.core.expressions.arithmetic.addition
uses gw.test.TestClass
uses java.lang.Character
uses java.lang.CharSequence
uses java.util.Date
uses java.lang.RuntimeException
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_CharSequenceTest extends ArithmeticTestBase {
  
  private static class MyCharSequence implements CharSequence {
    private var _value : String
    
    construct(value : String) {
      _value = value + "bonus"
    }

    override function charAt(p0 : int) : char {
      return _value.charAt(p0)
    }

    override function length() : int {
      return _value.length()
    }

    override function subSequence(p0 : int, p1 : int) : CharSequence {
      return _value.subSequence(p0, p1)
    }
    
    override function toString() : String {
      return _value
    }
  }
  
  private static enum MyEnum {
    FOO, BAR  
  }
  
  private function cs(value : String) : CharSequence {
    return new MyCharSequence(value)  
  }

  function testStringStringAddition() {
    assertEquals("foobar", "foo" + "bar")  
    assertEquals("foo", "foo" + "")    
    assertEquals("bar", "" + "bar")  
    assertEquals("", "" + "")      
    assertEquals(String, statictypeof("foo" + "bar"))
  }
  
  function testStringNullAddition() {
    assertEquals("foonull", "foo" + null)  
    assertEquals("null", "" + null)
    assertEquals(String, statictypeof("foo" + null))
  }
  
  function testStringCharSequenceAddition() {
    assertEquals("foobarbonus", "foo" + cs("bar"))  
    assertEquals("foobonus", "foo" + cs(""))  
    assertEquals(String, statictypeof("foo" + cs("bar")))
  }
  
  function testStringBigDecimalAddition() {
    assertEquals("foo123.45", "foo" + big_decimal("123.45"))  
    assertEquals("foo-123.45", "foo" + big_decimal("-123.45"))  
    assertEquals("foo123.0", "foo" + big_decimal("123.0"))
    assertEquals("foo-123.0", "foo" + big_decimal("-123.0"))
    assertEquals(String, statictypeof("foo" + big_decimal("123.45")))
  }
  
  function testStringBigIntegerAddition() {
    assertEquals("foo123", "foo" + big_int(123))  
    assertEquals("foo-123", "foo" + big_int(-123))  
    assertEquals(String, statictypeof("foo" + big_int(-123)))
  }
  
  function testStringDoubleAddition() {
    assertEquals("foo123.45", "foo" + b_double(123.45))  
    assertEquals("foo-123.45", "foo" + b_double(-123.45))  
    assertEquals("foo123.0", "foo" + b_double(123))
    assertEquals("foo-123.0", "foo" + b_double(-123))
    assertEquals(String, statictypeof("foo" + b_double(123.45)))
  }
  
  function testStringPDoubleAddition() {
    assertEquals("foo123.45", "foo" + p_double(123.45))  
    assertEquals("foo-123.45", "foo" + p_double(-123.45))  
    assertEquals("foo123.0", "foo" + p_double(123))
    assertEquals("foo-123.0", "foo" + p_double(-123))
    assertEquals(String, statictypeof("foo" + p_double(123.45)))
  }
  
  function testStringFloatAddition() {
    assertEquals("foo123.45", "foo" + b_float(123.45))  
    assertEquals("foo-123.45", "foo" + b_float(-123.45))  
    assertEquals("foo123.0", "foo" + b_float(123))
    assertEquals("foo-123.0", "foo" + b_float(-123))
    assertEquals(String, statictypeof("foo" + b_float(123.45)))
  }
  
  function testStringPFloatAddition() {
    assertEquals("foo123.45", "foo" + p_float(123.45))  
    assertEquals("foo-123.45", "foo" + p_float(-123.45))  
    assertEquals("foo123.0", "foo" + p_float(123))
    assertEquals("foo-123.0", "foo" + p_float(-123))
    assertEquals(String, statictypeof("foo" + p_float(123.45)))
  }
  
  function testStringLongAddition() {
    assertEquals("foo123", "foo" + b_long(123))  
    assertEquals("foo-123", "foo" + b_long(-123))  
    assertEquals(String, statictypeof("foo" + b_long(123)))
  }
  
  function testStringPLongAddition() {
    assertEquals("foo123", "foo" + p_long(123))  
    assertEquals("foo-123", "foo" + p_long(-123))  
    assertEquals(String, statictypeof("foo" + p_long(123)))
  }
  
  function testStringIntegerAddition() {
    assertEquals("foo123", "foo" + b_int(123))  
    assertEquals("foo-123", "foo" + b_int(-123))  
    assertEquals(String, statictypeof("foo" + b_int(123)))
  }
  
  function testStringPIntAddition() {
    assertEquals("foo123", "foo" + p_int(123))  
    assertEquals("foo-123", "foo" + p_int(-123))  
    assertEquals(String, statictypeof("foo" + p_int(123)))
  }
  
  function testStringShortAddition() {
    assertEquals("foo123", "foo" + b_short(123))  
    assertEquals("foo-123", "foo" + b_short(-123))  
    assertEquals(String, statictypeof("foo" + b_short(123)))
  }
  
  function testStringPShortAddition() {
    assertEquals("foo123", "foo" + p_short(123))  
    assertEquals("foo-123", "foo" + p_short(-123))  
    assertEquals(String, statictypeof("foo" + p_short(123)))
  }
  
  function testStringCharacterAddition() {
    assertEquals("fooB", "foo" + 'B')
    assertEquals(String, statictypeof("foo" + 'B'))
  }
  
  function testStringPCharAddition() {
    assertEquals("fooB", "foo" + 'B')
    assertEquals(String, statictypeof("foo" + 'B'))
  }

  function testStringByteAddition() {
    assertEquals("foo123", "foo" + b_byte(123))  
    assertEquals("foo-123", "foo" + b_byte(-123))  
    assertEquals(String, statictypeof("foo" + b_byte(123)))
  }
  
  function testStringPByteAddition() {
    assertEquals("foo123", "foo" + p_byte(123))  
    assertEquals("foo-123", "foo" + p_byte(-123))  
    assertEquals(String, statictypeof("foo" + p_byte(123)))
  }
  
  function testStringDateAddition() {
    assertEquals("fooThu Jan 01 00:00:00 PST 2009", "foo" + new Date("1/1/2009"))
    assertEquals(String, statictypeof("foo" + new Date("1/1/2009")))  
  }
  
  function testStringEnumAddition() {
    assertEquals("fooBAR", "foo" + MyEnum.BAR)
    assertEquals(String, statictypeof("foo" + MyEnum.BAR))  
  }
  
  function testStringObjectAddition() {
    assertEquals("foo[1, 2, 3]", "foo" + {1, 2, 3})
    assertEquals(String, statictypeof("foo" + {1, 2, 3}))  
  }
  
  function testStringDateCastAsObjectAddition() {
    assertEquals("fooThu Jan 01 00:00:00 PST 2009", "foo" + (new Date("1/1/2009") as Object))
    assertEquals(String, statictypeof("foo" + (new Date("1/1/2009") as Object)))  
  }

  // String on RHS

  function testNullStringAddition() {
    assertEquals("nullfoo", null + "foo")
    assertEquals("null", null + "")
    assertEquals(String, statictypeof(null + "foo"))
  }

  function testBigDecimalStringAddition() {
    assertEquals("123.45foo", big_decimal("123.45") + "foo")
    assertEquals("-123.45foo", big_decimal("-123.45") + "foo")
    assertEquals("123.0foo", big_decimal("123.0") + "foo")
    assertEquals("-123.0foo", big_decimal("-123.0") + "foo")
    assertEquals(String, statictypeof(big_decimal("123.45") + "foo"))
  }

  function testBigIntegerStringAddition() {
    assertEquals("123foo", big_int(123) + "foo")
    assertEquals("-123foo", big_int(-123) + "foo")
    assertEquals(String, statictypeof(big_int(-123)  + "foo"))
  }

  function testDoubleStringAddition() {
    assertEquals("123.45foo", b_double(123.45) + "foo")
    assertEquals("-123.45foo", b_double(-123.45) + "foo")
    assertEquals("123.0foo", b_double(123) + "foo")
    assertEquals("-123.0foo", b_double(-123) + "foo")
    assertEquals(String, statictypeof(b_double(123.45) + "foo"))
  }

  function testPDoubleStringAddition() {
    assertEquals("123.45foo", p_double(123.45) + "foo")
    assertEquals("-123.45foo", p_double(-123.45) + "foo")
    assertEquals("123.0foo", p_double(123) + "foo")
    assertEquals("-123.0foo", p_double(-123) + "foo")
    assertEquals(String, statictypeof(p_double(123.45) + "foo"))
  }

  function testFloatStringAddition() {
    assertEquals("123.45foo", b_float(123.45) + "foo")
    assertEquals("-123.45foo", b_float(-123.45) + "foo")
    assertEquals("123.0foo", b_float(123) + "foo")
    assertEquals("-123.0foo", b_float(-123) + "foo")
    assertEquals(String, statictypeof(b_float(123.45) + "foo"))
  }

  function testPFloatStringAddition() {
    assertEquals("123.45foo", p_float(123.45) + "foo")
    assertEquals("-123.45foo", p_float(-123.45) + "foo")
    assertEquals("123.0foo", p_float(123) + "foo")
    assertEquals("-123.0foo", p_float(-123) + "foo")
    assertEquals(String, statictypeof(p_float(123.45) + "foo"))
  }

  function testLongStringAddition() {
    assertEquals("123foo", b_long(123) + "foo")
    assertEquals("-123foo", b_long(-123) + "foo")
    assertEquals(String, statictypeof(b_long(123) + "foo"))
  }

  function testPLongStringAddition() {
    assertEquals("123foo", p_long(123) + "foo")
    assertEquals("-123foo", p_long(-123) + "foo")
    assertEquals(String, statictypeof(p_long(123) + "foo"))
  }

  function testIntegerStringAddition() {
    assertEquals("123foo", b_int(123) + "foo")
    assertEquals("-123foo", b_int(-123) + "foo")
    assertEquals(String, statictypeof(b_int(123) + "foo"))
  }

  function testPIntStringAddition() {
    assertEquals("123foo", p_int(123) + "foo")
    assertEquals("-123foo", p_int(-123) + "foo")
    assertEquals(String, statictypeof(p_int(123) + "foo"))
  }

  function testShortStringAddition() {
    assertEquals("123foo", b_short(123) + "foo")
    assertEquals("-123foo", b_short(-123) + "foo")
    assertEquals(String, statictypeof(b_short(123) + "foo"))
  }

  function testPShortStringAddition() {
    assertEquals("123foo", p_short(123) + "foo")
    assertEquals("-123foo", p_short(-123) + "foo")
    assertEquals(String, statictypeof(p_short(123) + "foo"))
  }

  function testCharacterStringAddition() {
    assertEquals("Bfoo", 'B' + "foo")
    assertEquals(String, statictypeof('B' + "foo"))
  }

  function testPCharStringAddition() {
    assertEquals("Bfoo", ('B') + "foo")
    assertEquals(String, statictypeof('B' + "foo"))
  }

  function testByteStringAddition() {
    assertEquals("123foo", b_byte(123) + "foo")
    assertEquals("-123foo", b_byte(-123) + "foo")
    assertEquals(String, statictypeof(b_byte(123) + "foo"))
  }

  function testPByteStringAddition() {
    assertEquals("123foo", p_byte(123) + "foo")
    assertEquals("-123foo", p_byte(-123) + "foo")
    assertEquals(String, statictypeof(p_byte(123) + "foo"))
  }

  function testDateStringAddition() {
    assertEquals("Thu Jan 01 00:00:00 PST 2009foo", new Date("1/1/2009") + "foo")
    assertEquals(String, statictypeof(new Date("1/1/2009") + "foo"))
  }

  function testEnumStringAddition() {
    assertEquals("BARfoo", MyEnum.BAR + "foo")
    assertEquals(String, statictypeof(MyEnum.BAR + "foo"))
  }

  function testObjectStringAddition() {
    assertEquals("[1, 2, 3]foo", {1, 2, 3} + "foo")
    assertEquals(String, statictypeof({1, 2, 3} + "foo"))
  }

  function testDateCastAsObjectStringAddition() {
    assertEquals("Thu Jan 01 00:00:00 PST 2009foo", (new Date("1/1/2009") as Object) + "foo")
    assertEquals(String, statictypeof((new Date("1/1/2009") as Object) + "foo"))  
  }
  
  // CharSequence on the LHS
  
  function testCharSequenceStringAddition() {
    assertEquals("foobonusbar", cs("foo") + "bar")  
    assertEquals("foobonus", cs("foo") + "")    
    assertEquals("bonusbar", cs("") + "bar")  
    assertEquals("bonus", cs("") + "")      
    assertEquals(String, statictypeof(cs("foo") + "bar"))
  }
  
  function testCharSquenceNullAddition() {
    assertEquals("foobonusnull", cs("foo") + null)  
    assertEquals("bonusnull", cs("") + null)
    assertEquals(String, statictypeof(cs("foo") + null))
  }
  
  function testCharSequenceCharSequenceAddition() {
    assertEquals("foobonusbarbonus", cs("foo") + cs("bar"))  
    assertEquals("foobonusbonus", cs("foo") + cs(""))  
    assertEquals(String, statictypeof("foo" + cs("bar")))
  }
  
  function testCharSequenceBigDecimalAddition() {
    assertEquals("foobonus123.45", cs("foo") + big_decimal("123.45"))
    assertEquals("foobonus-123.45", cs("foo") + big_decimal("-123.45"))
    assertEquals("foobonus123.0", cs("foo") + big_decimal("123.0"))
    assertEquals("foobonus-123.0", cs("foo") + big_decimal("-123.0"))
    assertEquals(String, statictypeof(cs("foo") + big_decimal("123.45")))
  }
  
  function testCharSequenceBigIntegerAddition() {
    assertEquals("foobonus123", cs("foo") + big_int(123))
    assertEquals("foobonus-123", cs("foo") + big_int(-123))
    assertEquals(String, statictypeof(cs("foo") + big_int(-123)))
  }
  
  function testCharSequenceDoubleAddition() {
    assertEquals("foobonus123.45", cs("foo") + b_double(123.45))
    assertEquals("foobonus-123.45", cs("foo") + b_double(-123.45))
    assertEquals("foobonus123.0", cs("foo") + b_double(123))
    assertEquals("foobonus-123.0", cs("foo") + b_double(-123))
    assertEquals(String, statictypeof(cs("foo") + b_double(123.45)))
  }
  
  function testCharSequencePDoubleAddition() {
    assertEquals("foobonus123.45", cs("foo") + p_double(123.45))
    assertEquals("foobonus-123.45", cs("foo") + p_double(-123.45))
    assertEquals("foobonus123.0", cs("foo") + p_double(123))
    assertEquals("foobonus-123.0", cs("foo") + p_double(-123))
    assertEquals(String, statictypeof(cs("foo") + p_double(123.45)))
  }
  
  function testCharSequenceFloatAddition() {
    assertEquals("foobonus123.45", cs("foo") + b_float(123.45))
    assertEquals("foobonus-123.45", cs("foo") + b_float(-123.45))
    assertEquals("foobonus123.0", cs("foo") + b_float(123))
    assertEquals("foobonus-123.0", cs("foo") + b_float(-123))
    assertEquals(String, statictypeof(cs("foo") + b_float(123.45)))
  }
  
  function testCharSequencePFloatAddition() {
    assertEquals("foobonus123.45", cs("foo") + p_float(123.45))
    assertEquals("foobonus-123.45", cs("foo") + p_float(-123.45))
    assertEquals("foobonus123.0", cs("foo") + p_float(123))
    assertEquals("foobonus-123.0", cs("foo") + p_float(-123))
    assertEquals(String, statictypeof(cs("foo") + p_float(123.45)))
  }
  
  function testCharSequenceLongAddition() {
    assertEquals("foobonus123", cs("foo") + b_long(123))
    assertEquals("foobonus-123", cs("foo") + b_long(-123))
    assertEquals(String, statictypeof(cs("foo") + b_long(123)))
  }
  
  function testCharSequencePLongAddition() {
    assertEquals("foobonus123", cs("foo") + p_long(123))
    assertEquals("foobonus-123", cs("foo") + p_long(-123))
    assertEquals(String, statictypeof(cs("foo") + p_long(123)))
  }
  
  function testCharSequenceIntegerAddition() {
    assertEquals("foobonus123", cs("foo") + b_int(123))
    assertEquals("foobonus-123", cs("foo") + b_int(-123))
    assertEquals(String, statictypeof(cs("foo") + b_int(123)))
  }
  
  function testCharSequencePIntAddition() {
    assertEquals("foobonus123", cs("foo") + p_int(123))
    assertEquals("foobonus-123", cs("foo") + p_int(-123))
    assertEquals(String, statictypeof(cs("foo") + p_int(123)))
  }
  
  function testCharSequenceShortAddition() {
    assertEquals("foobonus123", cs("foo") + b_short(123))
    assertEquals("foobonus-123", cs("foo") + b_short(-123))
    assertEquals(String, statictypeof(cs("foo") + b_short(123)))
  }
  
  function testCharSequencePShortAddition() {
    assertEquals("foobonus123", cs("foo") + p_short(123))
    assertEquals("foobonus-123", cs("foo") + p_short(-123))
    assertEquals(String, statictypeof(cs("foo") + p_short(123)))
  }
  
  function testCharSequenceCharacterAddition() {
    assertEquals("foobonusB", cs("foo") + 'B' )
    assertEquals(String, statictypeof(cs("foo") + 'B'))
  }
  
  function testCharSequencePCharAddition() {
    assertEquals("foobonusB", cs("foo") + ('B'))
    assertEquals(String, statictypeof(cs("foo") + 'B'))
  }

  function testCharSequenceByteAddition() {
    assertEquals("foobonus123", cs("foo") + b_byte(123))
    assertEquals("foobonus-123", cs("foo") + b_byte(-123))
    assertEquals(String, statictypeof(cs("foo") + b_byte(123)))
  }
  
  function testCharSequencePByteAddition() {
    assertEquals("foobonus123", cs("foo") + p_byte(123))
    assertEquals("foobonus-123", cs("foo") + p_byte(-123))
    assertEquals(String, statictypeof(cs("foo") + p_byte(123)))
  }
  
  function testCharSequenceDateAddition() {
    assertEquals("foobonusThu Jan 01 00:00:00 PST 2009", cs("foo") + new Date("1/1/2009"))
    assertEquals(String, statictypeof(cs("foo") + new Date("1/1/2009")))  
  }
  
  function testCharSequenceEnumAddition() {
    assertEquals("foobonusBAR", cs("foo") + MyEnum.BAR)
    assertEquals(String, statictypeof(cs("foo") + MyEnum.BAR))  
  }
  
  function testCharSequenceObjectAddition() {
    assertEquals("foobonus[1, 2, 3]", cs("foo") + {1, 2, 3})
    assertEquals(String, statictypeof(cs("foo") + {1, 2, 3}))  
  }
  
  function testCharSequenceDateCastAsObjectAddition() {
    assertEquals("foobonusThu Jan 01 00:00:00 PST 2009", cs("foo") + (new Date("1/1/2009") as Object))
    assertEquals(String, statictypeof(cs("foo") + (new Date("1/1/2009") as Object)))  
  }
  
  // String on RHS

  function testNullCharSequenceAddition() {
    assertEquals("nullfoobonus", null + cs("foo"))
    assertEquals("nullbonus", null + cs(""))
    assertEquals(String, statictypeof(null + cs("foo")))
  }

  function testBigDecimalCharSequenceAddition() {
    assertEquals("123.45foobonus", big_decimal("123.45") + cs("foo"))
    assertEquals("-123.45foobonus", big_decimal("-123.45") + cs("foo"))
    assertEquals("123.0foobonus", big_decimal("123.0") + cs("foo"))
    assertEquals("-123.0foobonus", big_decimal("-123.0") + cs("foo"))
    assertEquals(String, statictypeof(big_decimal("123.45") + cs("foo")))
  }

  function testBigIntegerCharSequenceAddition() {
    assertEquals("123foobonus", big_int(123) + cs("foo"))
    assertEquals("-123foobonus", big_int(-123) + cs("foo"))
    assertEquals(String, statictypeof(big_int(-123)  + cs("foo")))
  }

  function testDoubleCharSequenceAddition() {
    assertEquals("123.45foobonus", b_double(123.45) + cs("foo"))
    assertEquals("-123.45foobonus", b_double(-123.45) + cs("foo"))
    assertEquals("123.0foobonus", b_double(123) + cs("foo"))
    assertEquals("-123.0foobonus", b_double(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_double(123.45) + cs("foo")))
  }

  function testPDoubleCharSequenceAddition() {
    assertEquals("123.45foobonus", p_double(123.45) + cs("foo"))
    assertEquals("-123.45foobonus", p_double(-123.45) + cs("foo"))
    assertEquals("123.0foobonus", p_double(123) + cs("foo"))
    assertEquals("-123.0foobonus", p_double(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_double(123.45) + cs("foo")))
  }

  function testFloatCharSequenceAddition() {
    assertEquals("123.45foobonus", b_float(123.45) + cs("foo"))
    assertEquals("-123.45foobonus", b_float(-123.45) + cs("foo"))
    assertEquals("123.0foobonus", b_float(123) + cs("foo"))
    assertEquals("-123.0foobonus", b_float(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_float(123.45) + cs("foo")))
  }

  function testPFloatCharSequenceAddition() {
    assertEquals("123.45foobonus", p_float(123.45) + cs("foo"))
    assertEquals("-123.45foobonus", p_float(-123.45) + cs("foo"))
    assertEquals("123.0foobonus", p_float(123) + cs("foo"))
    assertEquals("-123.0foobonus", p_float(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_float(123.45) + cs("foo")))
  }

  function testLongCharSequenceAddition() {
    assertEquals("123foobonus", b_long(123) + cs("foo"))
    assertEquals("-123foobonus", b_long(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_long(123) + cs("foo")))
  }

  function testPLongCharSequenceAddition() {
    assertEquals("123foobonus", p_long(123) + cs("foo"))
    assertEquals("-123foobonus", p_long(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_long(123) + cs("foo")))
  }

  function testIntegerCharSequenceAddition() {
    assertEquals("123foobonus", b_int(123) + cs("foo"))
    assertEquals("-123foobonus", b_int(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_int(123) + cs("foo")))
  }

  function testPIntCharSequenceAddition() {
    assertEquals("123foobonus", p_int(123) + cs("foo"))
    assertEquals("-123foobonus", p_int(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_int(123) + cs("foo")))
  }

  function testShortCharSequenceAddition() {
    assertEquals("123foobonus", b_short(123) + cs("foo"))
    assertEquals("-123foobonus", b_short(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_short(123) + cs("foo")))
  }

  function testPShortCharSequenceAddition() {
    assertEquals("123foobonus", p_short(123) + cs("foo"))
    assertEquals("-123foobonus", p_short(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_short(123) + cs("foo")))
  }

  function testCharacterCharSequenceAddition() {
    assertEquals("Bfoobonus", 'B' + cs("foo"))
    assertEquals(String, statictypeof('B' + cs("foo")))
  }

  function testPCharCharSequenceAddition() {
    assertEquals("Bfoobonus", ('B') + cs("foo"))
    assertEquals(String, statictypeof('B' + cs("foo")))
  }

  function testByteCharSequenceAddition() {
    assertEquals("123foobonus", b_byte(123) + cs("foo"))
    assertEquals("-123foobonus", b_byte(-123) + cs("foo"))
    assertEquals(String, statictypeof(b_byte(123) + cs("foo")))
  }

  function testPByteCharSequenceAddition() {
    assertEquals("123foobonus", p_byte(123) + cs("foo"))
    assertEquals("-123foobonus", p_byte(-123) + cs("foo"))
    assertEquals(String, statictypeof(p_byte(123) + cs("foo")))
  }

  function testDateCharSequenceAddition() {
    assertEquals("Thu Jan 01 00:00:00 PST 2009foobonus", new Date("1/1/2009") + cs("foo"))
    assertEquals(String, statictypeof(new Date("1/1/2009") + cs("foo")))
  }

  function testEnumCharSequenceAddition() {
    assertEquals("BARfoobonus", MyEnum.BAR + cs("foo"))
    assertEquals(String, statictypeof(MyEnum.BAR + cs("foo")))
  }

  function testObjectCharSequenceAddition() {
    assertEquals("[1, 2, 3]foobonus", {1, 2, 3} + cs("foo"))
    assertEquals(String, statictypeof({1, 2, 3} + cs("foo")))
  }

  function testDateCastAsObjectCharSequenceAddition() {
    assertEquals("Thu Jan 01 00:00:00 PST 2009foobonus", (new Date("1/1/2009") as Object) + cs("foo"))
    assertEquals(String, statictypeof((new Date("1/1/2009") as Object) + cs("foo")))  
  }

  function testChainedStringConcatenation() {
    assertEquals("foo_bar_baz", "foo_" + "bar_" + "baz")
    assertEquals("3bar", 1 + 2 + "bar")
    assertEquals("bar12", "bar" + 1 + 2)
    assertEquals("bar12", "bar" + 1 + 2)
    
    assertException(1, \ -> e1() + e2())
  }
     
  private function e1() : String {
    throw new RuntimeException("1")
  } 
  
  private function e2() : String {
    throw new RuntimeException("2")
  } 
  
  private function assertException(number : int, exp()) {
    try {
      exp()
      fail()
    } catch (e : RuntimeException) {
      assertEquals(number as String, e.Message)  
    }
  }
}
