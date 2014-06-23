package gw.internal.gosu.parser.classTests.gwtest.pkga

uses gw.internal.gosu.parser.classTests.gwtest.pkgb.*

class GoodMainClassWithWildCardUses {
  static function testClassInUsesCanBeAccessed() : boolean {
    return SomeClassB == gw.internal.gosu.parser.classTests.gwtest.pkgb.SomeClassB
  }

  static function testClassInPackageOverridesUses() : boolean {
    return SameClassInPkgs == gw.internal.gosu.parser.classTests.gwtest.pkga.SameClassInPkgs
  }

  static function testClassInUsesOverridesDefault() : boolean {
    return Returns == gw.internal.gosu.parser.classTests.gwtest.pkgb.Returns
  }

  static function testClassInPackageOverridesUsesAndDefault() : boolean {
    return Deprecated == gw.internal.gosu.parser.classTests.gwtest.pkga.Deprecated
  }
}
