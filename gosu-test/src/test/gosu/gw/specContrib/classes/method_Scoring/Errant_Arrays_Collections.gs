package gw.specContrib.classes.method_Scoring

class Errant_Arrays_Collections extends JavaClass {
  function foo() {
    JavaClass.union(JavaClass.newHashSet({"asdf"}), JavaClass.newHashSet({"asdf"}))
  }
  function foo2() {
    var blah : Set<String> = JavaClass.copyOf({"hi"})
  }
  function foo3() {
    var attrs : Attributes = null
    var blah = JavaClass.assertThat(attrs).containsValue("hi") // assertThat() must resolve to the Map one
  }

  static interface Attributes extends Map<String, Object> {
  }
}