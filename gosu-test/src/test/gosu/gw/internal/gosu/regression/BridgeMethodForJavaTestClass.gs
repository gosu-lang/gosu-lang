package gw.internal.gosu.regression

uses java.util.Date

class BridgeMethodForJavaTestClass extends JavaFoo {
  override function compare(b: Date, c: Date): int {
    return 0
  }
}