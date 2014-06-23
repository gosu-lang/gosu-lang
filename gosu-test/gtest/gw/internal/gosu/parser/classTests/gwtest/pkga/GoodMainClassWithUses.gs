package gw.internal.gosu.parser.classTests.gwtest.pkga

uses gw.internal.gosu.parser.classTests.gwtest.pkgb.Deprecated
uses gw.internal.gosu.parser.classTests.gwtest.pkgb.SomeClassB
uses gw.internal.gosu.parser.classTests.gwtest.pkgb.SameClassInPkgs
uses gw.internal.gosu.parser.classTests.gwtest.pkgb.Returns

class GoodMainClassWithUses {
  static function testClassInUsesCanBeAccessed() : boolean {
    return SomeClassB == gw.internal.gosu.parser.classTests.gwtest.pkgb.SomeClassB
  }

  static function testClassInUsesOverridesPackage() : boolean {
    return SameClassInPkgs == gw.internal.gosu.parser.classTests.gwtest.pkgb.SameClassInPkgs
  }

  static function testClassInUsesOverridesDefault() : boolean {
    return Returns == gw.internal.gosu.parser.classTests.gwtest.pkgb.Returns
  }

  static function testClassInUsesOverridesPackageAndDefault() : boolean {
    return Deprecated == gw.internal.gosu.parser.classTests.gwtest.pkgb.Deprecated
  }

}
