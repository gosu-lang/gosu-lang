package gw.specContrib.expressions

class Errant_DefaultParamValue_Class {
  // Ok, Runnable is just a class, not generic, not parameterized
  function f1(a: java.lang.Class = java.lang.Runnable) {}

  // Ok because List == List<Object>, which is the default parameterized type
  function f2(a: java.lang.Class = java.util.List) {}

  // Ok because List<Object> is default parameterized type
  function f3(a: java.lang.Class = java.util.List<Object>) {}

  // Error because a non-default parameterized type is not compile-time constant; it cannot be represented as a constant value in the JVM
  function f4(a: java.lang.Class = java.util.List<String>) {}  //## issuekeys: MSG_COMPILE_TIME_CONSTANT_REQUIRED
}