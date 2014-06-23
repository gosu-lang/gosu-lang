package gw.internal.gosu.regression

// FIXME: (PL-22163) does not verify with non-open source Gosu
@gw.testharness.DoNotVerifyResource
class ExtendsNastyJavaInterface2 implements NastyJavaInterface2 {

  override property get PropFoo() : int {
    return 1
  }

  override property get Propfoo() : int {
    return 2
  }

}
