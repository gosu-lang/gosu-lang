package gw.specContrib.annotations

public class Errant_InheretFromJavaAnnoArrayTest extends UsesAnnoWithArrayValue {
  override function doSomething(arg : String) : String {
    return "Hello, got the argument '${arg}'"
  }
}
