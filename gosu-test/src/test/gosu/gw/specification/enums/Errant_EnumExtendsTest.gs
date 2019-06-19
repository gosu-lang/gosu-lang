package gw.specification.enums

class Errant_EnumExtendsTest {
  class A {}
  enum num3 extends A {}  //## issuekeys: MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF, MSG_UNEXPECTED_TOKEN
}  //## issuekeys: MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF

