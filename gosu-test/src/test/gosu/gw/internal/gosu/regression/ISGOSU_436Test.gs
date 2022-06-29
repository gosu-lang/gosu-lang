package gw.internal.gosu.regression
uses gw.test.TestClass

class ISGOSU_436Test extends TestClass {
  construct() {}

  function testISGOSU_436() {
    var m = new JavaClass_ISGOSU_436()
    assertEquals(1, m.foo({1, 2}))
    assertEquals(2, m.foo({1, 2}, {"ji"}))

    assertEquals(1, m.checkNotNull(null, "hi", "hi"))
    assertEquals(2, m.checkNotNull(null, "hi", {"hi"}))
  }

}
