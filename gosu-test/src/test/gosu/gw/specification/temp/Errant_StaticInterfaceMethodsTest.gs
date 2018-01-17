package gw.specification.temp

class Errant_StaticInterfaceMethodsTest implements IMyInterface {
  static function myOwnFunc() {
    IMyInterface.myStaticFunc()
    myStaticFunc()  //## issuekeys: MSG_NO_SUCH_FUNCTION
    MyClass.myStaticFunc()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
  }
}
