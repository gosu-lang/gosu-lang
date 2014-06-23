package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.reflect.TypeSystem

class PL17056Test extends TestClass {

  // This test mimics the reflective call that's generated which is causing problems in PL-17056.
  // The ultimate problem turns out to be the lookup done to invoke the method info, which was done
  // incorrectly.  So this test makes sure that reflective invocation of a method with this type
  // of generic structure works.  It's not really the *best* regression test as a result, since it's
  // targeted at the specific bug we found this time instead of being totally end-to-end, but attempting
  // to force the Gosu compiler to make a reflective call in a test would be pretty difficult.
  function testUseOfGenerifiedBindMethod() {
    var classType = java.lang.Class.Type
    var result = gw.internal.gosu.runtime.GosuRuntimeMethods.invokeMethodInfo(PL17056JavaClass, "bind", {classType}, null, {TestClass.Type.BackingClass})
    assertNotNull(result)
  }

}
