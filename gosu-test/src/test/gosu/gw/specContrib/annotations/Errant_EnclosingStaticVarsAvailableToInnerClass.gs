package gw.specContrib.annotations

class Errant_EnclosingStaticVarsAvailableToInnerClass {
  annotation MyAnnoInt {
    function foo(): int
  }

  @MyAnnoInt(42)
  class AInt {
  }

  public static final var sfint1: int = 42
  @MyAnnoInt(sfint1)
  class AStaticIntConst {
  }

  final var fint1 = 42
  @MyAnnoInt(fint1)  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  class ANonStaticIntConst {
  }

  static var sint1 = 42
  @MyAnnoInt(sint1) //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
  class AStaticIntNonConst {
  }
  
  final static var fsstr1 = "42"
  @MyAnnoInt(fsstr1) //## issuekeys: MSG_TYPE_MISMATCH
  class AStaticStringConst {
  }
  
  final static var fslong1 = 42l
  @MyAnnoInt(fslong1) //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  class AStaticLongConst {
  }
}
