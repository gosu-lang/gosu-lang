package gw.internal.gosu.parser.classTests.gwtest.pkga

class GoodMainClassNoUses {
  static function testClassInPackageCanBeAccessed() : boolean {
    return SomeClass == gw.internal.gosu.parser.classTests.gwtest.pkga.SomeClass
  }

  static function testClassInDefaultCanBeAccessed() : boolean {
    return Returns == gw.lang.Returns
  }

  static function testClassInPackageOverridesDefault() : boolean {
    return Deprecated == gw.internal.gosu.parser.classTests.gwtest.pkga.Deprecated
  }
}
