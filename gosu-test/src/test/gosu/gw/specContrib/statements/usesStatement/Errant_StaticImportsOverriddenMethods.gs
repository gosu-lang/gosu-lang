package gw.specContrib.statements.usesStatement

uses ClassWithOverriddenMethods#f1(int)
uses ClassWithOverriddenMethods#f1(Long) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithOverriddenMethods#f2
uses ClassWithOverriddenMethods#f3(int)
uses ClassWithOverriddenMethods#f3(String)


class Errant_StaticImportsOverriddenMethods {
  function foo() {
    f1(123)
    f1("hi") //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    f1(new Long(123)) //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    f2(123)
    f2("hi")
    f3(123)
    f3("hi")
  }
}
