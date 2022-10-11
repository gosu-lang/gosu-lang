package gw.specContrib.classes.method_Scoring

class Errant_Arrays_Collections extends JavaClass {
  function foo() {
    JavaClass.union(JavaClass.newHashSet({"asdf"}), JavaClass.newHashSet({"asdf"}))
  }
  function foo2() {
    var blah : Set<String> = JavaClass.copyOf({"hi"})
  }
}