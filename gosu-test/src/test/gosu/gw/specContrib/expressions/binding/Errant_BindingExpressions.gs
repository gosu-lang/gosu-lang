package gw.specContrib.expressions.binding


uses gw.test.TestClass
uses gw.lang.reflect.gs.IGosuClass
uses org.junit.Before
uses org.junit.After
uses org.junit.BeforeClass
uses org.junit.AfterClass

class Errant_BindingExpressions {
  function testNoBindings() {
    var no = new NoBindings()
    var no1 = no no  //## issuekeys: MSG_NOT_A_STATEMENT
    var no2 = no no no  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_NOT_A_STATEMENT
  }
  static class NoBindings {
  }  
  
  function testSelfPrefixBindings() {
    var selfPrefix = new SelfPrefix()
    var selfPrefix1 = selfPrefix selfPrefix
    var selfPrefix2 = selfPrefix selfPrefix selfPrefix
    var selfPrefix3 = selfPrefix selfPrefix selfPrefix selfPrefix
  }
  static class SelfPrefix {
    function prefixBind( selfPrefix: SelfPrefix ) : SelfPrefix {
      return new SelfPrefix()
    }
  }
  
  function testSelfPostfixBindings() {
    var selfPostfix = new SelfPostfix()
    var selfPostfix1 = selfPostfix selfPostfix
    var selfPostfix2 = selfPostfix selfPostfix selfPostfix
    var selfPostfix3 = selfPostfix selfPostfix selfPostfix selfPostfix
  }
  static class SelfPostfix {
    function postfixBind( selfPostfix: SelfPostfix ) : SelfPostfix {
      return new SelfPostfix()
    }
  }
  
  function testSelfBothBindings() {
    var selfBoth = new SelfBoth()
    var selfBoth1 = selfBoth selfBoth
    var selfBoth2 = selfBoth selfBoth selfBoth
    var selfBoth3 = selfBoth selfBoth selfBoth selfBoth
  }
  static class SelfBoth {
    function prefixBind( selfBoth: SelfBoth ) : SelfBoth {
      return new SelfBoth()
    }
    function postfixBind( selfBoth: SelfBoth ) : SelfBoth {
      return new SelfBoth()
    }
  }
  
  function testSelfPrefixNoBindings() {
    var selfPrefix = new SelfPrefixNo()
    var selfPrefix1 = selfPrefix selfPrefix
    var selfPrefix2 = selfPrefix selfPrefix selfPrefix  //## issuekeys: MSG_NOT_A_STATEMENT
    var selfPrefix3 = selfPrefix selfPrefix selfPrefix selfPrefix  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
  }
  static class SelfPrefixNo {
    function prefixBind( selfPrefix: SelfPrefixNo ) : NoBindings {
      return new NoBindings()
    }
  }

  function testSelfPostfixNoBindings() {
    var selfPostfix = new SelfPostfixNo()
    var selfPostfix1 = selfPostfix selfPostfix
    var selfPostfix2 = selfPostfix selfPostfix selfPostfix  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    var selfPostfix3 = selfPostfix selfPostfix selfPostfix selfPostfix  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
  }
  static class SelfPostfixNo {
    function postfixBind( selfPostfix: SelfPostfixNo ) : NoBindings {
      return new NoBindings()
    }
  }

  function testSelfBothNoBindings() {
    var selfBoth = new SelfBothNo()
    var selfBoth1 = selfBoth selfBoth
    var selfBoth2 = selfBoth selfBoth selfBoth  //## issuekeys: MSG_NOT_A_STATEMENT
    var selfBoth3 = selfBoth selfBoth selfBoth selfBoth  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
  }
  static class SelfBothNo {
    function prefixBind( selfBoth: SelfBothNo ) : NoBindings {
      return new NoBindings()
    }
    function postfixBind( selfBoth: SelfBothNo ) : NoBindings {
      return new NoBindings()
    }
  }

  function testPrefix() {
    var a = new A_BinaryPrefix()
    var binaryPrefix_self_a = a a  //## issuekeys: MSG_NOT_A_STATEMENT
    var b = new NoBindings()
    var binaryPrefix_ab: NoBindings = a b
    var binaryPrefix_ba = b a  //## issuekeys: MSG_NOT_A_STATEMENT
  }
  static class A_BinaryPrefix {
    function prefixBind( b: NoBindings ) : NoBindings {
      return new NoBindings()
    }
  }
  
  function testPostfix() {
    var a = new A_BinaryPostfix()
    var binaryPostfix_self_a = a a  //## issuekeys: MSG_NOT_A_STATEMENT
    var b = new NoBindings()
    var binaryPostfix_ab = a b  //## issuekeys: MSG_NOT_A_STATEMENT
    var binaryPostfix_ba: NoBindings = b a
  }
  static class A_BinaryPostfix {
    function postfixBind( b: NoBindings ) : NoBindings {
      return new NoBindings()
    }
  }
  
  function testBoth() {
    var a = new A_BinaryBoth()
    var binaryPostfix_self_a = a a  //## issuekeys: MSG_NOT_A_STATEMENT
    var b = new NoBindings()
    var binaryPostfix_ab: NoBindings = a b
    var binaryPostfix_ba: NoBindings = b a
  }
  static class A_BinaryBoth {
    function prefixBind( b: NoBindings ) : NoBindings {
      return new NoBindings()
    }
    function postfixBind( b: NoBindings ) : NoBindings {
      return new NoBindings()
    }
  }
  
  function testBinaryPrefix() {
    var a = new A_BinaryPrefix_B()
    var b = new B_BinaryPrefix_A()
    
    var binaryPrefix_ab: B_BinaryPrefix_A = a b
    var binaryPrefix_ba: A_BinaryPrefix_B = b a
    
    var binaryPrefix_aba: A_BinaryPrefix_B = a b a
    var binaryPrefix_bab: B_BinaryPrefix_A = b a b
    var binaryPrefix_aab: B_BinaryPrefix_A = a a b
    var binaryPrefix_baa = b a a  //## issuekeys: MSG_NOT_A_STATEMENT
    var binaryPrefix_bba: A_BinaryPrefix_B = b b a
    var binaryPrefix_abb = a b b  //## issuekeys: MSG_NOT_A_STATEMENT
    
    var binaryPrefix_abab: B_BinaryPrefix_A = a b a b
    var binaryPrefix_baba: A_BinaryPrefix_B = b a b a   
  }
  static class A_BinaryPrefix_B {
    function prefixBind( b: B_BinaryPrefix_A ) : B_BinaryPrefix_A {
      return new B_BinaryPrefix_A()
    }
  } 
  static class B_BinaryPrefix_A {
    function prefixBind( a: A_BinaryPrefix_B ) : A_BinaryPrefix_B {
      return new A_BinaryPrefix_B()
    }
  }
  
  function testBinaryPostfix() {
    var a = new A_BinaryPostfix_B()
    var b = new B_BinaryPostfix_A()
    
    var binaryPostfix_ab: A_BinaryPostfix_B = a b
    var binaryPostfix_ba: B_BinaryPostfix_A = b a
    
    var binaryPostfix_aba: A_BinaryPostfix_B = a b a
    var binaryPostfix_bab: B_BinaryPostfix_A = b a b
    var binaryPostfix_aab = a a b  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    var binaryPostfix_baa: B_BinaryPostfix_A = b a a
    var binaryPostfix_bba = b b a  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    var binaryPostfix_abb: A_BinaryPostfix_B = a b b
    
    var binaryPostfix_abab: A_BinaryPostfix_B = a b a b
    var binaryPostfix_baba: B_BinaryPostfix_A = b a b a    
  }
  static class A_BinaryPostfix_B {
    function postfixBind( b: B_BinaryPostfix_A ) : B_BinaryPostfix_A {
      return new B_BinaryPostfix_A()
    }
  } 
  static class B_BinaryPostfix_A {
    function postfixBind( a: A_BinaryPostfix_B ) : A_BinaryPostfix_B {
      return new A_BinaryPostfix_B()
    }
  }
  
  function testBinaryBoth() {
    var a = new A_BinaryBoth_B()
    var b = new B_BinaryBoth_A()
    
    var binaryBoth_ab: A_BinaryBoth_B = a b
    var binaryBoth_ba: B_BinaryBoth_A = b a
    
    var binaryBoth_aba: A_BinaryBoth_B = a b a
    var binaryBoth_bab: B_BinaryBoth_A = b a b
    var binaryBoth_aab: B_BinaryBoth_A = a a b
    var binaryBoth_baa: B_BinaryBoth_A = b a a
    var binaryBoth_bba: A_BinaryBoth_B = b b a
    var binaryBoth_abb: A_BinaryBoth_B = a b b
    
    var binaryBoth_aaab: A_BinaryBoth_B = a a a b
    var binaryBoth_bbba: B_BinaryBoth_A = b b b a
    var binaryBoth_baaa: B_BinaryBoth_A = b a a a
    var binaryBoth_abbb: B_BinaryBoth_A = b b b a
  }
  static class A_BinaryBoth_B {
    function prefixBind( b: B_BinaryBoth_A ) : B_BinaryBoth_A {
      return new B_BinaryBoth_A()
    }
    function postfixBind( b: B_BinaryBoth_A ) : B_BinaryBoth_A {
      return new B_BinaryBoth_A()
    }
  } 
  static class B_BinaryBoth_A {
    function prefixBind( a: A_BinaryBoth_B ) : A_BinaryBoth_B {
      return new A_BinaryBoth_B()
    }
    function postfixBind( a: A_BinaryBoth_B ) : A_BinaryBoth_B {
      return new A_BinaryBoth_B()
    }
  }
  
  function testBinaryBothSep() {
    var a = new A_BinaryBothSep_B()
    var b = new B_BinaryBothSep_A()
    
    var binaryBoth_ab: A_BinaryBothSep_B = a \ b
    var binaryBoth_ab_: B_BinaryBothSep_A = a / b
    var binaryBoth_ab_nosep = a b  //## issuekeys: MSG_NOT_A_STATEMENT
    var binaryBoth_ba: B_BinaryBothSep_A = b \ a
    var binaryBoth_ba_: A_BinaryBothSep_B = b / a
    var binaryBoth_ba_nosep = b a  //## issuekeys: MSG_NOT_A_STATEMENT
    
    var binaryBoth_aba: A_BinaryBothSep_B = a \ b \ a
    var binaryBoth_aba_: B_BinaryBothSep_A = a / b \ a
    var binaryBoth_aba_bad = a / b / a  //## issuekeys: MSG_NOT_A_STATEMENT
    
    var binaryBoth_bab: B_BinaryBothSep_A = b \ a \ b
    var binaryBoth_bab_: A_BinaryBothSep_B = b / a \ b
    var binaryBoth_bab_bad = b / a / b  //## issuekeys: MSG_NOT_A_STATEMENT
    
    var binaryBoth_aab: B_BinaryBothSep_A = a / a / b
    var binaryBoth_aab_: A_BinaryBothSep_B = a \ a / b
    var binaryBoth_aab_bad = a \ a \ b  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    
    var binaryBoth_baa: B_BinaryBothSep_A = b \ a \ a
    var binaryBoth_baa_: A_BinaryBothSep_B = b \ a / a
    var binaryBoth_baa_bad = b / a \ a  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    
    var binaryBoth_bba: A_BinaryBothSep_B = b / b / a
    var binaryBoth_bba_: B_BinaryBothSep_A = b \ b / a
    var binaryBoth_bba_bad = b \ b \ a  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT
    
    var binaryBoth_abb: A_BinaryBothSep_B = a \ b \ b
    var binaryBoth_abb_: B_BinaryBothSep_A = a \ b / b
    var binaryBoth_abb_bad = a / b / b  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    
    var binaryBoth_aaab: A_BinaryBothSep_B = a \ a / a / b
    var binaryBoth_bbba: B_BinaryBothSep_A = b \ b / b / a
    var binaryBoth_baaa: B_BinaryBothSep_A = b \ a \ a \ a
    var binaryBoth_abbb: B_BinaryBothSep_A = b \ b / b / a
  }
  static class A_BinaryBothSep_B {
    @BinderSeparators( :required = {"/"} )
    function prefixBind( b: B_BinaryBothSep_A ) : B_BinaryBothSep_A {
      return new B_BinaryBothSep_A()
    }
    @BinderSeparators( :required = {"\\"} )
    function postfixBind( b: B_BinaryBothSep_A ) : B_BinaryBothSep_A {
      return new B_BinaryBothSep_A()
    }
  } 
  static class B_BinaryBothSep_A {
    @BinderSeparators( :required = {"/"} )
    function prefixBind( a: A_BinaryBothSep_B ) : A_BinaryBothSep_B {
      return new A_BinaryBothSep_B()
    }
    @BinderSeparators( :required = {"\\"} )
    function postfixBind( a: A_BinaryBothSep_B ) : A_BinaryBothSep_B {
      return new A_BinaryBothSep_B()
    }
  }
  
  function testBinaryBothAccSep() {
    var a = new A_BinaryBothAccSep_B()
    var b = new B_BinaryBothAccSep_A()
    
    var binaryBoth_ab: A_BinaryBothAccSep_B = a \ b
    var binaryBoth_ab_n: A_BinaryBothAccSep_B = a b
    var binaryBoth_ab_: B_BinaryBothAccSep_A = a / b

    var binaryBoth_ba: B_BinaryBothAccSep_A = b \ a
    var binaryBoth_ba_n: B_BinaryBothAccSep_A = b a
    var binaryBoth_ba_: A_BinaryBothAccSep_B = b / a
    
    var binaryBoth_aba: A_BinaryBothAccSep_B = a \ b \ a
    var binaryBoth_aba_n: A_BinaryBothAccSep_B = a b a
    var binaryBoth_aba_: B_BinaryBothAccSep_A = a / b \ a
    
    var binaryBoth_bab: B_BinaryBothAccSep_A = b \ a \ b
    var binaryBoth_bab_: A_BinaryBothAccSep_B = b / a \ b
    
    var binaryBoth_aab: B_BinaryBothAccSep_A = a / a / b
    var binaryBoth_aab_: A_BinaryBothAccSep_B = a \ a / b
    
    var binaryBoth_baa: B_BinaryBothAccSep_A = b \ a \ a
    var binaryBoth_baa_: A_BinaryBothAccSep_B = b \ a / a
    
    var binaryBoth_bba: A_BinaryBothAccSep_B = b / b / a
    var binaryBoth_bba_: B_BinaryBothAccSep_A = b \ b / a
    
    var binaryBoth_abb: A_BinaryBothAccSep_B = a \ b \ b
    var binaryBoth_abb_: B_BinaryBothAccSep_A = a \ b / b
    
    var binaryBoth_aaab: A_BinaryBothAccSep_B = a \ a / a / b
    var binaryBoth_bbba: B_BinaryBothAccSep_A = b \ b / b / a
    var binaryBoth_baaa: B_BinaryBothAccSep_A = b \ a \ a \ a
    var binaryBoth_abbb: B_BinaryBothAccSep_A = b \ b / b / a
  }
  static class A_BinaryBothAccSep_B {
    @BinderSeparators( :accepted = {"/"} ) 
    function prefixBind( b: B_BinaryBothAccSep_A ) : B_BinaryBothAccSep_A {
      return new B_BinaryBothAccSep_A()
    }
    @BinderSeparators( :accepted = {"\\"} )
    function postfixBind( b: B_BinaryBothAccSep_A ) : B_BinaryBothAccSep_A {
      return new B_BinaryBothAccSep_A()
    }
  } 
  static class B_BinaryBothAccSep_A {
    @BinderSeparators( :accepted = {"/"} )
    function prefixBind( a: A_BinaryBothAccSep_B ) : A_BinaryBothAccSep_B {
      return new A_BinaryBothAccSep_B()
    }
    @BinderSeparators( :accepted = {"\\"} )
    function postfixBind( a: A_BinaryBothAccSep_B ) : A_BinaryBothAccSep_B {
      return new A_BinaryBothAccSep_B()
    }
  } 
}
