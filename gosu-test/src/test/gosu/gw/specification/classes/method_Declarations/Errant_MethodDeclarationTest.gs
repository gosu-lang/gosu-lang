package gw.specification.classes.method_Declarations

uses java.util.*
uses java.lang.*

class Errant_MethodDeclarationTest {
  structure struct { }

  enum enumeration {ONE, TWO}

  // Syntax
  function m0(x : block(c : int)) { }
  function m01(x(c : int)) { }
  function m1(x: int): int { return 1 }
  function m2(x: int) { }
  function m3() { }
  function m4(x: int): void { return }
  function m5(x: int): void { }
  function m6(x: int) { return }
  function m7<T>(x: T) { }

  // final
  function m8(x: int, final y: int) {  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    x = 1
    y = 2  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }

  // Argument Types
  function m9(t : boolean) { }
  function m10(c : char) { }
  function m11(b : byte) { }
  function m12(s : short) { }
  function m13(i: int) { }
  function m14(l : long) { }
  function m15(f : float) { }
  function m16(d : double) { }
  function m17(str : String) { }
  function m18(obj : Object) { }
  function m19(lst : List) { }
  function m20(blk : block(s: String): int) { }
  function m21(arr : int[]) { }
  function m22(struct : struct) { }
  function m23(e: enumeration) { }

  //Scope
  function m24(x : int, y : int, x : int, z :int) { }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_VARIABLE_ALREADY_DEFINED
  var y : int = 1
  var w = z  //## issuekeys: MSG_BAD_IDENTIFIER_NAME

  // Default values
  function m25(i: int, j : int = 8) { }
  function m26(i: int, j : int = 8, k : int) { }  //## issuekeys: MSG_EXPECTING_DEFAULT_VALUE
  function m27(i: int, j : int = 8, k : int = 7) { }
  function m28(i: int = 8, j : int = 7) { }
  function m29(i: List = new LinkedList()) { }  //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
  function m30(i : enumeration = ONE) { }
  function m30(i : block(c : int) = \ x -> {}) { }  //## issuekeys: MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS, MSG_COMPILE_TIME_CONSTANT_REQUIRED
  function m31(i : block(c : int) = null) { }
  function m32(i : int[] = {1,2,3}) { }

  function m33(i: int, j = 8) { }
  function m34(i: int, j = 8, k= 7) { }
  function m35(i = 8, j = 7) { }
  function m36(i = enumeration.ONE) { }
  function m37(i = null) { }
  function m38(i = {1,2,3}) { }  //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED


  function defaultMethodCalls() {
    m25(1, 2)
    m25(1)
    m27(1, 2, 3)
    m27(1, 2)
    m27(1)
    m28()
    m28(1)
    m28(1, 2)
  }


  //Overloading
  class A<T> {}
  class B {}
  class C {}
  function m39(x : LinkedList) {}
  function m39(x : ArrayList) {}
  function m40(x : LinkedList<Integer>) {}
  function m40(x : LinkedList<Boolean>) {}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD
  function m41(x : A<B>) {}
  function m41(x : A<C>) {}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD
  function m24(i: int, j : int = 8) { }  //## issuekeys: MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS

  property get MyProp() : String { return ""}
  property set MyProp( s: String = "" ) {}  //## issuekeys: MSG_DEFAULT_VALUE_NOT_ALLOWED

  //Method Body
  function m42() {
    construct() {}  //## issuekeys: MSG_SYNTAX_ERROR
  }  //## issuekeys: MSG_EXPECTING_RIGHTBRACE_STMTBLOCK
  function m43() {
    function fun(i : int)  {}  //## issuekeys: MSG_SYNTAX_ERROR
  }  //## issuekeys: MSG_EXPECTING_RIGHTBRACE_STMTBLOCK
  function m44() {
    var _x : int
    property get X() : int { return _x }  //## issuekeys: MSG_SYNTAX_ERROR
    property set X(x : int) {  _x = x }
  }  //## issuekeys: MSG_EXPECTING_RIGHTBRACE_STMTBLOCK
  function m45() {
    delegate myList represents List = new ArrayList()  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
  }
  function m46() {
    class C {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
  }
  function m47() {
    interface I {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
  }
  function m48() {
    structure S {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
  }
  function m49() {
    enum E {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
  }
  function m50() : void { return 1 }  //## issuekeys: MSG_RETURN_VAL_FROM_VOID_FUNCTION
  function m51() { return 1 }  //## issuekeys: MSG_RETURN_VAL_FROM_VOID_FUNCTION
  function m52() : int {
    if(true) {
      return 0
    } else {
      return 1
    }
    var x = 0  //## issuekeys: MSG_UNREACHABLE_STMT
  }
  function m53() : int {
    if(true) {
      return 0
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  // Return Type
  function m54() : AbstractList {
    var a = 0
    if(a == 1) return new Object()  //## issuekeys: MSG_TYPE_MISMATCH
    if(a == 2) return new ArrayList()
    if(a == 3) return new LinkedList()
    return null
  }

  function testVar(var x: int): int { return 1 }  //## issuekeys: MSG_EXPECTING_ARGS, MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF, MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF
}  //## issuekeys: MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF
