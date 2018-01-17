package gw.specification.interfaces.interfaceDeclarations

class Errant_InterfaceDeclarationsTest {
  interface I0 {
    var i : int = 0
    var j : int = 0

    function m0() : int {
      this.hello()
      return 0
    }

    function hello() {    }
    function m1() : int
    static function ms() : boolean { return false }
    class A {}
    interface I01 {
      function m00(a : int)
    }
  }

    interface IStatic0  {
      function ms() : int
    }
    interface IStatic1 extends IStatic0 {
      static function ms() : int {  //## issuekeys: MSG_STATIC_METHOD_CANNOT_OVERRIDE
        return 0
      }
    }


    function foo() {
      var top = TopLevelInterface.top
      interface I1 {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
    }

    construct() {
      interface I2 {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
    }

  static interface I3 {
    var i  = 8  //## issuekeys: MSG_NON_PRIVATE_MEMBERS_MUST_DECLARE_TYPE
  }

  private interface I4 {
    var i : int = 8
  }

  protected interface I6 {
    var i : int = 8
    var j : int = (\ -> 3)()
  }

    public protected interface I7 {  //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER, MSG_ILLEGAL_USE_OF_MODIFIER
      var i : int = 8
    }

  internal interface I8 {
    var i : int = 8
    function m8() : int { return 8 }
  }

  interface I9 extends I0, I8  {
    function m9() : int { return 9 }
    static function ms() : boolean { return true }
  }

    interface I10 extends   {  }  //## issuekeys: MSG_EXPECTING_TYPE_NAME
    interface I11 extends I0  {
      var i : int = 9  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      class A {}
      override function m0(a : int) : int { return 0 }  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE
    }

  class Impl implements  I9 {
    override function m1() : int {
      return 100
    }
  }


  interface Int0 {
    var num : int = 0
  }
  interface Int1 {
    var num : int = 1
  }
  static class Class0 implements Int0, Int1 {
    function getNum() : int {
      return num  //## issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE, MSG_TYPE_MISMATCH
    }
  }
  static class Class1 implements Int0, Int1 {
  }

  function testAmbiguousReference() {
     var x : int = new Class1().num   //## KB(PL-34344): Ambiguous reference  //## issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
  }

  function testExtends() {
    var impl : Impl = new Impl()
    var i9 : I9 = impl
    var i0 : I0 = impl
    var i8 : I8 = impl

    var x = 0
    impl.m0()
    impl.m1()
    impl.m8()
    impl.m9()
    impl.ms()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD

    i9.m0()
    i9.m1()
    i9.m8()
    i9.m9()
    x = i9.i  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i9.ms()

    i0.m0()
    i0.m1()
    i0.m8()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    i0.m9()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    x = i0.i  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    x = I0.i
    i0.ms()

    i8.m0()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    i8.m1()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    i8.m8()
    x = i8.i  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    x = I8.i
    i8.m9()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    i8.ms()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
  }

  interface Top  {
    function name() : String {
      return "unnamed"
    }
  }
  interface Left extends Top {
    override function name() : String {
      return "fromLeft"
    }
  }
  interface Right extends Top {
  }
  interface Bottom extends Left, Right {
  }


  function testDefaultMethods() {
    var r : Right = new Right() {}
    var b : Bottom = new Bottom() {}
    r.name()
    b.name()

  }


    public interface A2  {
      function hello() : void {

      }
    }
    public interface B2 {
      function hello() : void {

      }
    }
    static public class C2 implements B2, A2{  //## issuekeys: MSG_INHERITS_UNRELATED_DEFAULTS

    }

    public interface A3  {
      function hello() : void {
      }
    }
    public interface B3 {
      function hello()
    }
    static public class C3 implements B3, A3{  //## issuekeys: MSG_UNIMPLEMENTED_METHOD

    }

    interface IObjectOverride  {
      function toString() : String {  //## issuekeys: MSG_OVERRIDES_OBJECT_METHOD
        return ""
      }
    }

    interface Colorable  {
      function setColor(color : int) : void
      function getColor() : int
    }
    class Point  {
      internal var x : int
      internal var y : int
    }
    class ColoredPoint extends Point implements Colorable {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD
      internal var color : int
    }

    interface Fish  {
      function getNumberOfScales() : int
    }
    interface StringBass  {
      function getNumberOfScales() : double
    }
    class Bass implements Fish, StringBass {
      public function getNumberOfScales() : int {  //## issuekeys: MSG_FUNCTION_CLASH
        return 91
      }
    }

  static class Diamond0 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
    }
    interface C extends A {
    }
    static class D implements B, C {
    }
  }

  static class Diamond1 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return 1
      }
    }
    interface C extends A {
    }
    static class D implements B, C {
    }
  }

    static class Diamond11 {
      interface A  {
        function m() : int {
          return 0
        }
      }
      interface B extends A {
        override function m() : int {
          return 1
        }
      }
      interface C extends A {
      }
      static class D implements B, C {
        override function m() : int {
           return super[A].m()  //## issuekeys: MSG_NOT_A_SUPERTYPE
        }
      }
    }

   static class Diamond2 {
      interface A  {
        function m() : int {
          return 0
        }
      }
      interface B extends A {
        override function m() : int {
          return 1
        }
      }
      interface C extends A {
        override function m() : int {
          return 2
        }
      }
      static class D implements B, C {  //## issuekeys: MSG_INHERITS_UNRELATED_DEFAULTS
      }
    }

  static class Diamond22 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return 1
      }
    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        super[C].m()
        return super[B].m()
      }

    }
  }

  static class Diamond222 {
    interface A  {
      function m() : int {
        return 0
      }
    }
    interface B extends A {
      override function m() : int {
        return super[A].m()
      }
    }
    interface C extends A {

    }
    static class D implements B, C {

    }
  }

   static class Diamond3 {
      interface A  {

      }
      interface B extends A {
        function m() : int {
          return 1
        }
      }
      interface C extends A {
        function m() : int {
          return 2
        }
      }
      static class D implements B, C {  //## issuekeys: MSG_INHERITS_UNRELATED_DEFAULTS
      }
    }

  static class Diamond33 {
    interface A  {

    }
    interface B extends A {
      function m() : int {
        return 1
      }
    }
    interface C extends A {
      function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        super[C].m()
        return super[B].m()
      }
    }
  }


  static class Diamond4 {
    interface A  {
      function m() : int {
        return 1
      }
    }
    interface B extends A {

    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
    }
  }

  static class Diamond5 {
    interface A  {
      function m() : int {
        return 1
      }
    }
    interface B extends A {

    }
    interface C extends A {
      override function m() : int {
        return 2
      }
    }
    static class D implements B, C {
      override function m() : int {
        return 3
      }
    }
  }

  function testDiamond() {
    new Diamond0.D().m()
    new Diamond1.D().m()
    new Diamond4.D().m()
    new Diamond5.D().m()
    new Diamond22.D().m()
    new Diamond33.D().m()
    new Diamond222.D().m()
  }

  interface Superinterface  {
    function foo() : int {
      return 0
    }
  }
  class Subclass2 implements Superinterface {
    override function foo() : int {
      return 1
    }
    function tweak() : int {
      return super[Superinterface].foo()
    }
  }

  function testSuperSyntaxt() {
    new Subclass2().tweak()
    new Subclass2().foo()
  }
}