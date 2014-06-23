package gw.internal.gosu.parser.classTests.gwtest.ctor

class FooSuite extends gw.test.Suite<FooSuite> {
  construct() {
    super()
  }


  static function suite() : junit.framework.Test {
    var s = new FooSuite()
    return null
  }


}
