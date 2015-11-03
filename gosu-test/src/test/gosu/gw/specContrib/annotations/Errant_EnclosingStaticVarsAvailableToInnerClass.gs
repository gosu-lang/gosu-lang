package gw.specContrib.annotations

class Errant_EnclosingStaticVarsAvailableToInnerClass {
  annotation MyAnnoInt {
    function foo(): int
  }

  @MyAnnoInt(42)
  class AInt {
  }

  public static final var fint1: int = 42

  @MyAnnoInt(fint1)
  class AIntConst {
  }

  var int1 = 42

  @MyAnnoInt(int1)  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  class AIntNonConst {
  }
}