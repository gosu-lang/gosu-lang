package gw.internal.gosu.parser.classTests.gwtest.ctor

@gw.testharness.DoNotVerifyResource
class Errant_BadJavaConstructors {
  // Test that private constructors cannot be called
  var a = new gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.PrivateConstructorClass();  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS
  // Test that protected constructors cannot be called
  var b = new gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.ProtectedConstructorClass();  //## issuekeys: MSG_CTOR_HAS_XXX_ACCESS
  // Test that abstract classes cannot be called
  var c = new gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.AbstractClassForJavaConstructorsTest("foo");  //## issuekeys: MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS

  // Test inaccessible ctor cannot be accessed by default via anonymous class
  var d = new gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.PrivateConstructorClass() {}  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_DEFAULT_CTOR_IN

  // Test subclass cannot access inaccessible constructor via implicit super() call
  class Sub extends gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.PrivateConstructorClass {
    construct() {}  //## issuekeys: MSG_NO_DEFAULT_CTOR_IN
  }

  // Test subclass cannot access inaccessible constructor via default ctor
  class Sub2 extends gw.internal.gosu.parser.classTests.gwtest.JavaConstructorsClass.PrivateConstructorClass {
  }  //## issuekeys: MSG_NO_DEFAULT_CTOR_IN
}
