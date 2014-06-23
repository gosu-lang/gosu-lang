package gw.internal.gosu.parser.annotation.gwtest.annotation.param

class ParamDoc {
  @Param("foo", "foo doc")
  @Param("bar", "bar doc")
  function varFunction(foo : String, bar : String) : String { return "" }
  @Param("foz", "foz doc")
  @Param("baz", "baz doc")
  function ParamDoc(foz : String, baz : String) {}
}
