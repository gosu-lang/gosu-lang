/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses gw.testharness.Disabled

class ScratchpadGotoDeclTest extends AbstractGotoDeclTest {
  function testField() {
    testNavigationInScratchpad(
        "var ##field = 10 \n" +
            "[[field]] = 11 \n"
    )
  }

  function testMethod() {
    testNavigationInScratchpad(
        "function ##foo() { \n" +
            " [[foo]]() \n" +
            "} \n"
    )
  }

  function testVarDeclFromPropertySetter() {
    testNavigationInScratchpad(
        "private var ##_x : int \n" +
            " property set Prop( i : int ) {  \n" +
            "  [[_x]] = i    \n" +
            "  }   \n"
    )
  }

  function testGoToEnumFromVarValue() {
    testNavigationInScratchpad(
        "enum ##FruitType {  Orange, Banana, Kiwi} \n" +
            "var myFruitType = [[FruitType]].Banana"
    )
  }

  function testGoToFunctionFromFinallyBlock() {
    testNavigationInScratchpad(
        "  function ##doo() { }\n" +
            "  function foo() {\n " +
            "    try{}\n" +
            "    finally{[[doo]]()}\n" +
            "  }\n"
    )
  }

  function testGoToInterfaceFromAsType() {
    testNavigationInScratchpad(
        "class GosuClass implements GosuInterface {} \n" +
            "interface ##GosuInterface{} \n" +
            "class GosuClass1 extends GosuClass {} \n" +
            "var dd: GosuClass1 \n" +
            "var d = dd as [[GosuInterface]] \n"
    )
  }

  function testGoToSuperClassProperty() {
    testNavigationInScratchpad(
        "interface IA{ \n" +
            "property get A():int \n" +
            "}\n" +
            "class SuperCA implements IA{ \n" +
            "property get ##A():int {return 3} \n" +
            "} \n" +
            "class CA extends SuperCA{ \n" +
            "property get A():int {return 5 + super.[[A]]} \n" +
            "} \n"
    )
  }

  function testGoToAnnotationClass() {
    testNavigationInScratchpad(
        "class Submarine{} \n" +
            " @[[Logme]] \n" +
            "function a() {} \n" +
            " } \n" +
            "static class ##Logme implements IAnnotation {} \n"
    )
  }

  function testGoToMethodInInnerClass() {
    testNavigationInScratchpad(
      "static class Greeting{ \n" +
            "static class FrenchGreeting { \n" +
              "static  public function ##sayWhat() : String { \n" +
                 "return \"bonjour\" \n" +
              "} \n" +
          "} \n" +
          "public property get Hello() : String { \n" +
              "return FrenchGreeting.[[sayWhat]]() \n" +
          "} \n" +
        "} \n"
    )
  }

  @Disabled("dpetrusca", "corner case")
  function testGoToDeclFromObjectInitializers(){
    testNavigationInScratchpad(
    "class FileContainer{ \n" +
       "var ##destFile: String \n" +
    "} \n" +
    "var myFileContainer = new FileContainer(){ \n" +
    ":[[destFile]]= \"aaa\"}"
    )
  }
  //==================================================
  //================================================

  function testNavigationInScratchpad(text: String) {
    NavigationUtil.navigate(configureScratchpad(text))
  }
}