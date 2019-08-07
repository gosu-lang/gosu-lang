package gw.specContrib.statements.usesStatement

uses ClassWithGenericMethods#f1
uses ClassWithGenericMethods#f2(Object)
uses ClassWithGenericMethods#f2(String) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f3
uses ClassWithGenericMethods#f4(List<String>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f5(List)
uses ClassWithGenericMethods#f6(List<List<List>>) //## issuekeys: MSG_FL_METHOD_NOT_FOUND,MSG_USES_STMT_DUPLICATE
uses ClassWithGenericMethods#f7(List<List>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f8(List)
uses ClassWithGenericMethods#f9(List<List>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f10(List<List<List<String>>>)
uses ClassWithGenericMethods#f11(List<List<List>>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f12(List<List<List<Object>>>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f13(List<List<List<Object>>>)
uses ClassWithGenericMethods#f14(List<List<List<Number>>>)
uses ClassWithGenericMethods#f15(List<List<List<Object>>>) //## issuekeys: MSG_NO_SUCH_FUNCTION
uses ClassWithGenericMethods#f16(List<List<List<Runnable & Serializable>>>)

uses java.io.Serializable

class Errant_StaticImportsGenericMethods {
  function foo() {
    f1(123)
    f1("")
    f2(123)
    f2("")
    f3(new ArrayList<String>())
    f5(new ArrayList<String>())
    f8(new ArrayList<List<List<String>>>())
  }
}
