/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.KnownBreak

class InitializerCompletionTest extends AbstractCodeCompletionTest {

  //can invoke
  function testCanInvokeOnColonInInitializerFirstPropertyNotYetClosed() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }


  // can invoke
  function testCanInvokeOnColonInInitializerFirstPropertyClosed() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  // java
  function testCanInvokeOnColonInJClassInitializerPropertyClosed() {
    testCanInvoke({
      "//JAVA \n" +
      "package some.pkg;\n" +
      "class JavaClass {\n" +
      "  public void setProp(String s) {} \n" +
      "  public String getProp() {return null;} \n" +
      "}"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo() {\n" +
      "      var x = new JavaClass(){:^^}\n" +
      "    }\n" +
      "}"
    })
  }

  //popup items
  function testItemsAreShownOnColonInInitializerFirstPropertyNotYetClosed() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : int) {}\n" +
      "  property set AProp3(x : List<java.lang.String>) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"}
    ,{
      "Prop1: String", "Prop2: int", "AProp3: List<String>"
      //"Prop1", "Prop2", "AProp3"
    })
  }

  //with filter
  function testFilteredItemsAreShownOnColonInInitializerFirstPropertyNotYetClosed() {
    test({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : int) {}\n" +
      "  property set AProp3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:P^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"}
    ,{
      "Prop1: String", "Prop2: int"
      //"Prop1", "Prop2"
    })
  }

  //negative with filter
  function testNonFilteredItemsArentShownOnColonInInitializerFirstPropertyNotYetClosed() {
    testNoItems({
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : int) {}\n" +
      "  property set AProp3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:P^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"}
    ,{
      "AProp3: List<java.lang.String>"
      //"AProp3"
    })
  }

  //==================

  function testCanInvokeOnColonInInitializerFirstPropertyPartiallyCompletedWordClosed() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set AProp3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^Pro}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInitializerFirstPropertyClosedAndValueSet() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^\"foo\"}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInitializerFirstPropertyClosedAndValueSet1() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^ \"foo\"}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInitializerFirstPropertyClosedAndValueSet2() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:^^= \"foo\"}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInitializerFirstPropertyClosedAndValueSet3() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){: ^^= \"foo\"}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyNotYetClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyPartiallyCompletedWordClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^Gr}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedAndValueSet() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^false}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedAndValueSet1() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^ false}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedAndValueSet2() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^= false}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedAndValueSet3() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:^^ = false}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInitializerSecondPropertyNotYetClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass() {:Prop1 = \"foo\", :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInitializerSecondPropertyClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass() {:Prop1 = \"foo\", :^^}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyNotYetClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:Red = true, :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyClosed() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:Red = true, :^^}\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInitializerFirstPropertyNotYetClosedNextLine() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){\n" +
      "      :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInitializerFirstPropertyClosedNextLine() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){\n" +
      "      :^^\n" +
      "    }\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyNotYetClosedNextLine() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){\n" +
      "      :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedNextLine() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){\n" +
      "      :^^\n" +
      "    }\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInitializerSecondPropertyClosedNextLine() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:Prop1 = \"foo\",\n" +
      "      :^^\n" +
      "    }\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInitializerSecondPropertyNotYetClosedNextLine() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:Prop1 = \"foo\",\n" +
      "      :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyClosedNextLine() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:Red = false,\n" +
      "      :^^\n" +
      "    }\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyNotYetClosedNextLine() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new OuterClass.StaticInnerClass(){:Red = false,\n" +
      "      :^^\n" +
      "    print(x)\n" +
      "  }\n" +
      "}\n"})
  }


  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInitializerFirstPropertyNotYetClosedInStringTemplate() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop(x : String) {}\n" +
      "  property get Prop() {return \"\"}\n" +
      "  function bar() {\n" +
      "    print(\"\${new GosuClass(){:^^}\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInitializerFirstPropertyClosedInStringTemplate() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop(x : String) {}\n" +
      "  property get Prop() {return \"\"}\n" +
      "  function bar() {\n" +
      "    print(\"\${new GosuClass(){:^^}}\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyNotYetClosedInStringTemplate() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    print(\"\${new OuterClass.StaticInnerClass(){:^^}\")\n" +
      "  }\n" +
      "}\n"})
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInnerClassInitializerFirstPropertyClosedInStringTemplate() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    print(\"\${new OuterClass.StaticInnerClass(){:^^}}\")\n" +
      "  }\n" +
      "}\n"})
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInitializerSecondPropertyNotYetClosedInStringTemplate() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    print(\"\${new GosuClass(){:Prop1 = \"foo\", :^^}\")\n" +
      "  }\n" +
      "}\n")

  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInitializerSecondPropertyClosedInStringTemplate() {
    testCanInvoke(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  property set Prop1(x : String) {}\n" +
      "  property set Prop2(x : String) {}\n" +
      "  property set Prop3(x : String) {}\n" +
      "  function bar() {\n" +
      "    print(\"\${new GosuClass(){:Prop1 = \"foo\", :^^}}\")\n" +
      "  }\n" +
      "}\n")
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyNotYetClosedInStringTemplate() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    print(\"\${var x = new OuterClass.StaticInnerClass(){:Red = false, :^^}\")\n" +
      "  }\n" +
      "}\n"})
  }

  @KnownBreak("PL-18696", "", "cgross")
  function testCanInvokeOnColonInInnerClassInitializerSecondPropertyClosedInStringTemplate() {
    testCanInvoke({
      "package some.pkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass {\n" +
      "        public var Green : boolean = false\n" +
      "        public var Grey : boolean = false\n" +
      "        public var Yellow : boolean = false\n" +
      "        public var Red : boolean = false\n" +
      "    }\n" +
      "}\n"
    ,
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    print(\"\${var x = new OuterClass.StaticInnerClass(){:Red = false, :^^}}\")\n" +
      "  }\n" +
      "}\n"})
  }

}