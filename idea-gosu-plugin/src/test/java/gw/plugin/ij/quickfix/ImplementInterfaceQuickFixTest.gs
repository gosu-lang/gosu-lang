/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuInterfaceFile
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.plugin.ij.framework.generator.JavaInterfaceFile
uses gw.testharness.KnownBreak

class ImplementInterfaceQuickFixTest extends AbstractQuickFixTest {

  function testImplementSimpleJavaInterfaceMethodWithoutParams() {
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public void someMethod();\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod() {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithNativeParam() {
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public void someMethod( int foo );\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: int) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithNativeReturnType() {
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public int someMethod();\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(): int {!!\n" +
      "    return 0\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithObjectParam() {
    var obj = new JavaClassFile (
      "package some.pkg;\n" +
      "class ArgType {\n" +
      "}"
    )
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public void someMethod( ArgType foo );\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: ArgType) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ obj, iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithObjectParamFromDiffPkg() {
    var iface = new JavaInterfaceFile (
      "package some.other.pkg;\n" +
      "import java.io.File;\n" +
      "interface IBase {\n" +
      "  public void someMethod( File foo );\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "uses some.other.pkg.IBase\n" +
      "[[[uses java.io.File\n" +
      "\n]]]" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: File) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  @KnownBreak("", "", "")
  function testImplementJavaInterfaceMethodWithObjectParamFromDiffPkgWithConflictingImport() {
    var obj = new JavaClassFile (
      "package some.other.pkg;\n" +
      "class Runnable {\n" +
      "}"
    )
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "import some.other.pkg.Runnable;\n" +
      "interface IBase {\n" +
      "  public void someMethod( Runnable runner );\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Runnable;\n" +
      "class ImplClass implements IB^^ase, Runnable {\n" +
      "  function run() {\n" +
      "  }\n" +
      "  [[[override function someMethod(runner: some.other.pkg.Runnable) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ obj, iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithObjectParamFromDiffPkgUsingImplicitImport() {
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public void someMethod( String foo );\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: String) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithGetterSetter01() {
    var iface = new JavaInterfaceFile (
        "package some.pkg;\n" +
            "interface IBase {\n" +
            "public int getT();\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override property get T(): int {!!\n" +
            "    return 0\n  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithGetterSetter02() {
    var iface = new JavaInterfaceFile (
        "package some.pkg;\n" +
            "interface IBase {\n" +
            "public int getT();\n" +
            "void setT(int x);\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override property get T(): int {!!\n" +
            "    return 0\n  }\n\n" +
            "  override property set T(x: int) {\n" +
            "  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithGetterSetter03() {
    var iface = new JavaInterfaceFile (
        "package some.pkg;\n" +
            "interface IBase {\n" +
            "public int T=1;\n" +
            "void setT(int x);\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override function setT(x: int) {!!\n" +
            "  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementJavaInterfaceMethodWithGetterSetter04() {
    var iface = new JavaInterfaceFile (
        "package some.pkg;\n" +
            "interface IBase {\n" +
            "public int T=1;\n" +
            "public int getT();\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override property get T(): int {!!\n" +
            "    return 0\n  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithGetterSetter01() {
    var iface = new GosuInterfaceFile (
        "package some.pkg\n" +
            "interface IBase {\n" +
            "  property get T() : int\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override property get T(): int {!!\n" +
            "    return 0\n  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithGetterSetter02() {
    var iface = new GosuInterfaceFile (
        "package some.pkg\n" +
            "interface IBase {\n" +
            "  property get T() : int\n" +
            "  property set T(x : int)\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override property get T(): int {!!\n" +
            "    return 0\n  }\n\n" +
            "  override property set T(x: int) {\n" +
            "  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithGetterSetter03() {
    var iface = new GosuInterfaceFile (
        "package some.pkg\n" +
            "interface IBase {\n" +
            "  static var T : int = 0\n" +
            "  function setT(x : int)\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override function setT(x: int) {!!\n" +
            "  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithGetterSetter04() {
    var iface = new GosuInterfaceFile (
        "package some.pkg\n" +
            "interface IBase {\n" +
            "  static var T : int = 0\n" +
            "  function getT() : int\n" +
            "  function setT(x : int)\n" +
            "}"
    )
    var implCls = new GosuClassFile (
        "package some.pkg\n" +
            "class ImplClass implements IB^^ase {\n" +
            "  [[[override function getT(): int {!!\n" +
            "    return 0\n  }\n\n" +
            "  override function setT(x: int) {\n" +
            "  }\n]]]" +
            "}"
    )
    test({ iface, implCls })
  }

  function testImplementSimpleJavaInterfaceWithTwoMethodsWithoutParams() {
    var iface = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBase {\n" +
      "  public void first();\n" +
      "  public void second();\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function first() {!!\n" +
      "  }\n\n" +
      "  override function second() {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementTwoJavaInterfacesEachWithMethods() {
    var iface1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IFirst {\n" +
      "  public void first();\n" +
      "}"
    )
    var iface2 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface ISecond {\n" +
      "  public void second();\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IF^^irst, ISecond {\n" +
      "  [[[override function first() {!!\n" +
      "  }\n\n" +
      "  override function second() {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface1, iface2, implCls })
  }

  function testImplementTwoJavaInterfacesEachWithMethodsWithConflictingTypeImports() {
    var type1 = new JavaClassFile (
      "package some.first;\n" +
      "class Conflicting {\n" +
      "}"
    )
    var type2 = new JavaClassFile (
      "package some.second;\n" +
      "class Conflicting {\n" +
      "}"
    )
    var iface1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "import some.first.Conflicting;\n" +
      "interface IFirst {\n" +
      "  public void first(Conflicting arg);\n" +
      "}"
    )
    var iface2 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "import some.second.Conflicting;\n" +
      "interface ISecond {\n" +
      "  public void second(Conflicting arg);\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "[[[\n" +
      "uses some.first.Conflicting\n" +
      "\n]]]" +
      "class ImplClass implements IFirst, IS^^econd {\n" +
      "  [[[override function first(arg: Conflicting) {!!\n" +
      "  }\n\n" +
      "  override function second(arg: some.second.Conflicting) {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ type1, type2, iface1, iface2, implCls })
  }

  function testImplementSimpleGosuInterfaceMethodWithoutParams() {
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod()\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod() {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithNativeParam() {
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod(foo:int)\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: int) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithNativeReturnType() {
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod() : int\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(): int {!!\n" +
      "    return 0\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithObjectParam() {
    var obj = new GosuClassFile (
      "package some.pkg\n" +
      "class ArgType {\n" +
      "}"
    )
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod(foo:ArgType)\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: ArgType) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ obj, iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithObjectParamFromDiffPkg() {
    var iface = new GosuInterfaceFile (
      "package some.other.pkg;\n" +
      "uses java.io.File\n" +
      "interface IBase {\n" +
      "  function someMethod(foo:File);\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "uses some.other.pkg.IBase\n" +
      "[[[uses java.io.File\n" +
      "\n]]]" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: File) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  @KnownBreak("", "", "")
  function testImplementGosuInterfaceMethodWithObjectParamFromDiffPkgWithConflictingImport() {
    var obj = new GosuClassFile (
      "package some.other.pkg;\n" +
      "class Runnable {\n" +
      "}"
    )
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "uses some.other.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod(runner:Runnable);\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "class ImplClass implements IB^^ase, Runnable {\n" +
      "  function run() {\n" +
      "  }\n" +
      "  [[[override function someMethod( runner: some.other.pkg.Runnable ) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ obj, iface, implCls })
  }

  function testImplementGosuInterfaceMethodWithObjectParamFromDiffPkgUsingImplicitImport() {
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function someMethod(foo:String)\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function someMethod(foo: String) {!!\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementSimpleGosuInterfaceWithTwoMethodsWithoutParams() {
    var iface = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBase {\n" +
      "  function first()\n" +
      "  function second()\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IB^^ase {\n" +
      "  [[[override function first() {!!\n" +
      "  }\n\n" +
      "  override function second() {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface, implCls })
  }

  function testImplementTwoGosuInterfacesEachWithMethods() {
    var iface1 = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IFirst {\n" +
      "  function first()\n" +
      "}"
    )
    var iface2 = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface ISecond {\n" +
      "  function second()\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IF^^irst, ISecond {\n" +
      "  [[[override function first() {!!\n" +
      "  }\n\n" +
      "  override function second() {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface1, iface2, implCls })
  }

  function testImplementTwoGosuInterfacesEachWithMethodsWithConflictingTypeImports() {
    var type1 = new GosuClassFile (
      "package some.first;\n" +
      "class Conflicting {\n" +
      "}"
    )
    var type2 = new GosuClassFile (
      "package some.second;\n" +
      "class Conflicting {\n" +
      "}"
    )
    var iface1 = new GosuInterfaceFile (
      "package some.pkg\n" +
      "uses some.first.Conflicting\n" +
      "interface IFirst {\n" +
      "  function first(arg:Conflicting)\n" +
      "}"
    )
    var iface2 = new GosuInterfaceFile (
      "package some.pkg\n" +
      "uses some.second.Conflicting\n" +
      "interface ISecond {\n" +
      "  function second(arg:Conflicting)\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "[[[\n" +
      "uses some.first.Conflicting\n" +
      "\n]]]" +
      "class ImplClass implements IF^^irst, ISecond {\n" +
      "  [[[override function first(arg: Conflicting) {!!\n" +
      "  }\n\n" +
      "  override function second(arg: some.second.Conflicting) {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ type1, type2, iface1, iface2, implCls })
  }

  function testImplementOneEachJavaAndGosuInterfacesWithMethods() {
    var iface1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IFirst {\n" +
      "  public void first();\n" +
      "}"
    )
    var iface2 = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface ISecond {\n" +
      "  function second()\n" +
      "}"
    )
    var implCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ImplClass implements IF^^irst, ISecond {\n" +
      "  [[[override function first() {!!\n" +
      "  }\n\n" +
      "  override function second() {\n" +
      "  }\n]]]" +
      "}"
    )
    test({ iface1, iface2, implCls })
  }

  function test(resources: GosuTestingResource[]) {
    test(resources, "Implement Methods")
  }

}