/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuEnhancementFile
uses gw.plugin.ij.framework.generator.GosuEnumFile
uses gw.plugin.ij.framework.generator.GosuInterfaceFile
uses gw.plugin.ij.framework.generator.GosuProgramFile
uses gw.plugin.ij.framework.generator.GosuTemplateFile
uses gw.plugin.ij.framework.generator.JavaAnnotationFile
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

class GotoDeclTest extends AbstractGotoDeclTest {

  //method -- local vars
  function testGotoLocalVarDeclInMethod() {
    test(
      "  function testFunction() {\n" +
      "    var ##local = 10 \n" +
      "    [[local]] = 11 \n" +
      "  }"
    )
  }

  //property getter  -- local vars
  function testGotoLocalVarDeclInPropertyGetter() {
    test(
      "  property get Prop() : int{\n" +
      "    var ##local = 10 \n" +
      "    return [[local]]\n" +
      "  }"
    )
  }

  //property setter  -- local vars
  function testGotoLocalVarDeclInPropertySetter() {
    test(
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var ##local = 10 \n" +
      "    [[local]] = 11 \n" +
      "    _x = i\n" +
      "  }"
    )
  }

  //block  -- local vars
  function testGoToLocalVarDeclInBlock() {
    test(
      "  function method() {\n" +
      "    var x : List<String>\n" +
      "    x.each(\\ b -> {var ##local = \"qa\"; print(b + [[local]])})\n" +
      "  }"
    )
  }

  //constructors  -- local vars
  function testGoToLocalVarDeclInConstruct() {
    test(
      "  construct() {\n" +
      "    var ##local : int\n" +
      "    [[local]] = 10\n" +
      "  }"
    )
  }

  //var in for -- for loop
  function testGoToForLoopVarsDecl() {
    test(
      "  function method() {\n" +
      "    for (##lvar in 1..10){\n" +
      "      print([[lvar]])\n" +
      "    }\n" +
      "  }"
    )
  }

  //var in index -- for loop
  function testGoToForLoopIndexVarsDecl() {
    test(
      "  function method() {\n" +
      "    for (s in \"a test string\" index ##ix){\n" +
      "      print([[ix]])\n" +
      "    }\n" +
      "  }"
    )
  }

  //catch clause
  function testGoToCatchVarsDecl() {
    test(
      "  function method() {\n" +
      "    try{\n" +
      "    }catch(##ex){\n" +
      "      throw [[ex]]\n" +
      "    }\n" +
      "  }\n"
    )
  }

  //using statement
  function testGoToUsingClauseArgumentListDecl1() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.io.FileWriter\n" +
      "uses java.io.FileReader\n\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    using( var ##reader = new FileReader ( \"c:\\temp\\usingfun.txt\" ),\n" +
      "           var writer = new FileWriter ( \"c:\\temp\\usingfun2.txt\" ) ){\n" +
      "      var char = [[reader]].read()\n" +
      "      writer.write( char )\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //method -- Paramters
  function testGoToParamVarsDeclInMethod() {
    test(
      "  function method(##arg1 : int, arg2 : int) : int {\n" +
      "    return [[arg1]]\n" +
      "  }"
    )
  }

  //property setter  -- Paramters
  function testGotoParamVarDeclInPropertySetter() {
    test(
      "  var _x : int\n" +
      "  property set Prop( ##i : int ) {\n" +
      "    _x = [[i]]\n" +
      "  }"
    )
  }

  //Blocks -- Paramters
  function testGoToParamVarsDeclInBlock() {
    test(
      "  var adder = \\ ##x : Number, y : Number -> { return [[x]] + y }"
    )
  }

  //maybe same case as testGoToParamVarsDeclInBlockWithImplicitCoercionReturnType()
  function testGoToGenericTypsParamVarsDeclInBlock() {
    test(
      "  function method() {\n" +
      "    var pods = {\"Web\", \"Studio\", \"BAT\"}\n" +
      "    pods.map(\\ ##pod -> [[pod]].length)" +
      "  }"
    )
  }

  //maybe same case as testGoToGenericTypsParamVarsDeclInBlock()
  function testGoToParamVarsDeclInBlockWithImplicitCoercionReturnType () {
    test(
      "var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, \\ ##s -> [[s]].toUpperCase() )"
    )
  }


  //constructors  -- Paramters
  function testGoToParamVarDeclInConstructor() {
    test(
      "  var _x : int\n" +
      "  construct(##local : int) {\n" +
      "    _x = [[local]]\n" +
      "  }"
    )
  }

  //==============================methods

  //instance method
  function testGoToFunctionDecl() {
    test(
      "  function ##method() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[method]]()\n" +
      "  }"
    )
  }

  //Static method
  function testGoToStaticFunctionDecl() {
    test(
      "  static function ##method() : int {\n" +
      "    return 0\n" +
      "  }\n" +
      "  function foo() : int {\n" +
      "    return [[method]]()\n" +
      "  }"
    )
  }

  //method with param's type as Type
  @Disabled("dpetrusca", "Corner case")
  function testGoToMethodDeclWithTypeAsParamType() {
    test(
      "  function bar() {\n" +
      "    [[method]](typeof \"123\")\n" +
      "  }\n" +
      "  function ##method(type : Type) {\n" +
      "  }\n"
    )
  }

  function testGoToMethodDeclWithGenericDefaultType() {
    test(
      "  function bar() {\n" +
      "    [[method]](null)\n" +
      "  }\n" +
      "  function ##method<T>(type : Type<T>) {\n" +
      "  }\n"
    )
  }

  //method with param's type as List<T>
  function testGoToMethodDeclWithListAsParamType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function ##myF(texts : List<String>) {}\n" +
      "    function test(){\n" +
      "        [[myF]]({\"123\", \"456\"})\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  //method with param's type as Map<K, V>
  function testGoToMethodDeclWithMapAsParamType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.Map\n" +
      "uses java.util.HashMap\n" +
      "class GosuClass {\n" +
      "    function ##myF(myMap : Map<String, String>) {}\n" +
      "    function test(){\n" +
      "        [[myF]](new  java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" })\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  //method with block argument
  function testGoToMethodDeclWithBlockAsParamType() {
    test(
      "  function test() {\n" +
      "    var lst = [[getStringList]]( \\-> null )\n" +
      "  }\n\n" +
      "  function ##getStringList( foo():List<String> ) : List<String> {\n" +
      "    return null \n" +
      "  }"
    )
  }

  //method with default params
  function testGoToDefaultParamDeclInMethod() {
    test(
      "   function withDefaultParams(##x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "   }\n" +
      "   function test(){\n" +
      "       withDefaultParams(:[[x]] = 1, :y = \"y\")\n" +
      "   }"
    )
  }

  function testGoToDefaultParamDeclInMethod2() {
    test(
      "   function withDefaultParams(x: int, y: String = \"default\", ##z: String = \"default\") {\n" +
      "   }\n" +
      "   function test(){\n" +
      "       withDefaultParams(:[[z]] = \"z\", :x = 1)\n" +
      "   }"
    )
  }

  function testGoToDefaultParamFunctionDecl() {
    test(
      "   function ##withDefaultParams(x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "   }\n" +
      "   function test(){\n" +
      "       [[withDefaultParams]](:z = \"z\", :x = 1)\n" +
      "   }"
    )
  }

  function testGoToBlockOverridenFunctionFromBlockInvocation() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var b1 = \\-> new Callable() {\n" +
      "      override function ##call() : block():Callable<block():block():int> {\n" +
      "        return \\-> new Callable<block():block():int> (){\n" +
      "          override function call() : block():block():int {\n" +
      "            return \\->\\-> outer.outer.IntProperty\n" +
      "          }\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "    var c1 = b1()\n" +
      "    var b2 = c1.[[call]]()\n" +
      "    var c2 = b2()\n" +
      "    var blk1 = c2.call()\n" +
      "  }\n"  +
      "  property get IntProperty() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  @Disabled("dpetrusca", "broken test due to method info for c2.call() referring to Callable.call interface method instead of override")
  function testGoToNestedBlockOverridenFunctionFromNestedBlockInvocation() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var b1 = \\-> new Callable() {\n" +
      "      override function call() : block():Callable<block():block():int> {\n" +
      "        return \\-> new Callable<block():block():int> (){\n" +
      "          override function ##call() : block():block():int {\n" +
      "            return \\->\\-> outer.outer.IntProperty\n" +
      "          }\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "    var c1 = b1()\n" +
      "    var b2 = c1.call()\n" +
      "    var c2 = b2()\n" +
      "    var blk1 = c2.[[call]]()\n" +
      "  }\n"  +
      "  property get IntProperty() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //Constructor
  function testGoToCtorDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  ##construct(arg : int) {}\n" +
      "  function method() {\n" +
      "    var o = new [[GosuClass]](0)\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //constructor with default params
  // TODO broken due to IJ behavior that is not easily fixable.  See GosuReferenceExpressionImpl.getElement()
  function testGoToDefaultParamDeclInCtor() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  var _y: String\n" +
      "  var _z: String\n" +
      "  construct(##x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "    _x = x\n" +
      "    _y = y\n" +
      "    _z = z\n" +
      "  }\n" +
      "  function hi() {\n" +
      "    var o = new GosuClass(:[[x]] = 1, :y = \"y\")\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  // TODO broken due to IJ behavior that is not easily fixable.  See GosuReferenceExpressionImpl.getElement()
  function testGoToDefaultParamDeclInCtor2() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  var _y: String\n" +
      "  var _z: String\n" +
      "  construct(x: int, y: String = \"default\", ##z: String = \"default\") {\n" +
      "    _x = x\n" +
      "    _y = y\n" +
      "    _z = z\n" +
      "  }\n" +
      "  function hi() {\n" +
      "    var o = new GosuClass(:[[z]] = \"z\", :x = 1)\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToDefaultParamCtor() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _x : int\n" +
      "  var _y: String\n" +
      "  var _z: String\n" +
      "  ##construct(x: int, y: String = \"default\", z: String = \"default\") {\n" +
      "    _x = x\n" +
      "    _y = y\n" +
      "    _z = z\n" +
      "  }\n" +
      "  function hi() {\n" +
      "    var o = new [[GosuClass]](:z = \"z\", :x = 1)\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToGenericTypeCtorDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.Map\n" +
      "class GosuClass {\n" +
      "  function method() {\n" +
      "    var map: Map<String, String> = null\n" +
      "    var me = new java.util.[[HashMap]]<String, String>(map)" +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.util.HashMap", "public HashMap(java.util.Map<? extends K,? extends V>")
  }

  //==============================fields

  //delegate
  function testGoToDelegateFromInvocationFromThis() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass<E> implements java.util.List<E> {\n" +
      "  delegate _map represents java.util.List<E>\n" +
      "  function addSpecial( e : E ) {\n" +
      "    this.[[add]]( e )\n" +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.util.List", "boolean add(E e)")
  }

  //fields with property
  function testGoToPropFieldDecl() {
    test(
      "  var _propField : String as ##PropField\n" +
      "  construct(s : String) {\n" +
      "    [[PropField]] = s\n" +
      "  }"
    )
  }

  //class field
  function testGoToClassFieldDecl() {
    test(
      "  var ##_propField : String\n" +
      "  construct(s : String) {\n" +
      "    [[_propField]] = s\n" +
      "  }"
    )
  }

  //enum constants
  function testGoToEnumDecl() {
    test(
      "  var y: TestEnum = [[First]] \n" +
      "  enum TestEnum { \n" +
      "    ##First, Second \n" +
      "  }"
    )
  }

  //property getter
  function testGotoPropertyGetterDecl() {
    test(
      "  property get ##Prop() : int{\n" +
      "    return 10\n" +
      "  }\n" +
      "  function method() : int{\n" +
      "    return [[Prop]]\n" +
      "  }"
    )
  }

  //property getter   Note: only handle the getter case for backward compatibility  per Scott
  function testGotoPropertyGetterDeclFromBackwardCompatibilityGetterInvocation() {
    test(
      "  property get ##Prop() : int{\n" +
      "    return 10\n" +
      "  }\n" +
      "  function method() : int {\n" +
      "    return [[getProp]]()\n" +
      "  }"
    )
  }

  //property setter
  function testGotoPropertySetterDecl() {
    test(
      "  var _x : int\n" +
      "  property set ##Prop( i : int ) {\n" +
      "    _x = i\n" +
      "  }\n" +
      "  function method() {\n" +
      "    [[Prop]] = 5\n" +
      "  }"
    )
  }

  //==============================initializers

  //====Object initializers

  function testGoToPropertyDeclThroughClassInitializer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as ##POD\n" +
      "  function foo() {\n" +
      "    var anObj = new GosuClass(){ : [[POD]] = \"Studio\"}\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToClassFieldDeclThroughClassInitializer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var ##_pod : String\n" +
      "  function foo() {\n" +
      "    var anObj = new GosuClass(){ : [[_pod]] = \"Studio\"}\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToLocalVariableDeclThroughUsuageInPropInitializer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _prop : int as Prop\n" +
      "  function bar() {\n" +
      "    var ##i = 5\n" +
      "    var x = new GosuClass(){:Prop = [[i]]}\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToParamVarsDeclThroughUsuageInPropInitializer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _prop : int as Prop\n" +
      "  function bar(##i : int) {\n" +
      "    var x = new GosuClass(){:Prop = [[i]]}\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToPropertySetterDeclThroughInitializer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _prop : String\n" +
      "  property set ##Prop(x : String) {_prop = x}\n" +
      "  function bar() {\n" +
      "    var x = new GosuClass(){:[[Prop]] = \"123\"}\n" +
      "  }"  +
      "}"
    )
    test(f)
  }

  function testGotoPropertySetterDeclThroughInitializer() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SupJavaClass {\n" +
      "  public void ##setProp(String s) {} \n" +
      "  public String getProp() {return null;} \n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "    function foo() {\n" +
      "    var x = new GosuClass(){:[[Prop]] = \"123\"}\n" +
      "    }\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  //====array/list/map initializers
  function testGoToFieldPropertyThroughArrayInitializer() {
    test(
      "  var _prop1 : String as ##Prop1\n" +
      "  var _prop2 : String as Prop2\n\n" +
      "  function foo(){\n" +
      "    var myStrings = new String[][] {{[[Prop1]]}, {Prop2}}\n" +
      "  }"
    )
  }

  function testGoToClassFieldThroughArrayInitializer() {
    test(
      "  var ##_prop1 : String as Prop1\n" +
      "  var _prop2 : String as Prop2\n\n" +
      "  function foo(){\n" +
      "    var myStrings = new String[][] {{[[_prop1]]}, {_prop2}}\n" +
      "  }"
    )
  }

  function testGoToPropertyGetterThroughArrayListInitializer() {
    test(
      "  property get ##Prop1() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  property get Prop2() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  function foo(){\n" +
      "    var myStrings = {{[[Prop1]]}, {Prop2}}\n" +
      "  }"
    )
  }

  function testGoToPropertyGetterThroughMapInitializer() {
    test(
      "  property get ##Prop1() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  property get Prop2() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  function foo(){\n" +
      "    var myMap = new  java.util.HashMap<String, String>(){ [[Prop1]] -> Prop2 }\n" +
      "  }"
    )
  }

  function testGoToPropertyGetterThroughMapInitializer2() {
    test(
      "  property get Prop1() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  property get ##Prop2() : String {\n" +
      "    return \"\"\n" +
      "  }\n\n" +
      "  function foo(){\n" +
      "    var myMap = new  java.util.HashMap<String, String>(){ Prop1 -> [[Prop2]] }\n" +
      "  }"
    )
  }

  //==============================literals

  //string template
  @KnownBreak("dpetrusca", "", "")
  function testGoToPropFieldDeclThroughStringTemplate() {
    test(
      "  var _pod : String as ##POD\n" +
      "  function foo() {\n" +
      "    print(\"\${[[POD]]}\")\n" +
      "  }"
    )
  }

  @KnownBreak("dpetrusca", "", "")
  function testGoToClassFieldDeclThroughStringTemplate() {
    test(
      "  var ##_pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\${[[_pod]]}\")\n" +
      "  }"
    )
  }

  @KnownBreak("dpetrusca", "", "")
  function testGoToPropertyGetterDeclThroughStringTemplate() {
    test(
      "  property get ##Prop() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\${[[Prop]]}\")\n" +
      "  }"
    )
  }

  @KnownBreak("dpetrusca", "", "")
  function testGoToFunctionDeclThroughStringTemplate() {
    test(
      "  function ##method() : String{\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\${[[method]]()}\")\n" +
      "  }"
    )
  }

  // TODO broken because template strings not supported yet
  @KnownBreak("dpetrusca", "", "")
  function testGoToPropFieldDeclThroughAlternateTemplateExpression () {
    test(
      "  var _pod : String as ##POD\n" +
      "  function foo() {\n" +
      "    print(\"\<%=[[POD]]%>\")\n" +
      "  }"
    )
  }


  // TODO broken because template strings not supported yet
  @KnownBreak("dpetrusca", "", "")
  function testGoToClassFieldDeclThroughAlternateTemplateExpression(){
    test(
      "  var ##_pod : String as POD\n" +
      "  function foo() {\n" +
      "    print(\"\<%=[[_pod]]%>\")\n" +
      "  }"
    )
  }

  // TODO broken because template strings not supported yet
  @KnownBreak("dpetrusca", "", "")
  function testGoToPropertyGetterDeclThroughAlternateTemplateExpression() {
    test(
      "  property get ##Prop() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\<%=[[Prop]]%>\")\n" +
      "  }"
    )
  }

  // TODO broken because template strings not supported yet
  @KnownBreak("dpetrusca", "", "")
  function testGoToFunctionDeclThroughAlternateTemplateExpression() {
    test(
      "  function ##method() : String{\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo() {\n" +
      "    print(\"\<%=[[method]]()%>\")\n" +
      "  }"
    )
  }

  //block literals
  @Disabled("dpetrusca", "to implement this, a block expression would have to show up as a reference to the underlying implemented interface, not sure if that's a good idea or not")
  function testGoToOneMethodInterfacesWithCompatibleSignaturesThroughBlockLiteral() {
     var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function foo() {\n" +
      "    var myCache = new gw.util.concurrent.Cache<String, String>( \"My Uppercase Cache\", 100, [[\\ s -> s.toUpperCase()]] )" +
      "  }" +
      "}"
    )

    testGotoNonProjectFile(f, "gw.util.concurrent.Cache.MissHandler", "public interface MissHandler<K, V>")
  }

  //feature literals
  function testGoToPropFieldDeclThroughFeatureAccess() {
    test(
      "  var _pod : String as ##POD\n" +
      "  function foo() {\n" +
      "    var myProperty = this#[[POD]]\n" +
      "  }"
    )
  }


  function testGoToPropGetterDeclThroughFeatureAccess() {
    test(
      "  property get ##Pod() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo1() {\n" +
      "    var myProperty = this#[[Pod]]\n" +
      "  }"
    )
  }

  function testGoToFunctionDeclThroughFeatureAccess() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var _pod : String as POD\n" +
      "  function ##foo() {\n" +
      "    var anObj = new GosuClass(){ : POD = \"Studio\"}\n" +
      "    var fooFuncForClass = anObj#[[foo]]()\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //==============================type parameters

  //method
  function testGoToFunctionTypeParamDecl() {
    test(
      "  function method<##T>(s : [[T]]) : T {\n" +
      "    return s\n" +
      "  }"
    )
  }

  function testGoToFunctionTypeParamDeclBlockOfBlockToT() {
    test(
      "  function method<##T>( t : Type<T> ) : block():Type {\n" +
      "    return \\->[[T]]\n" +
      "  }"

    )
  }

  function testGoToTypeParamDeclWithGenericDefaultType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method<##T>(type : Type<[[T]]>) {\n" +
      "  }\n" +
      "}"
    )
  }

  //class
  function testGoToClassTypeParamDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <##T extends GosuClass<T>> { \n" +
      "  function fromNumber( p0: Integer ) : [[T]] { \n" +
      "    return null \n" +
      "  }\n\n" +
      "}"
    )
    test(f)
  }

  function testGoToTypeVarDeclThroughNewTypeParamExpression() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <##T extends GosuClass<T>> { \n" +
      "  construct( value : Integer ) {} \n" +
      "  function fromNumber( p0: Integer ) : T {\n" +
      "    return new [[T]]( p0 )\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGotoCtorThroughGenericSuper() {
    var fsim = new GosuClassFile (
      "package some.pkg\n" +
      "class SimpleGenericType<T> { \n" +
      "  ##construct( member : T ) {} \n" +
      "}"
    )
    var fcom = new GosuClassFile (
      "package some.pkg\n" +
      "class ComplexGenericType<A,B> extends SimpleGenericType<B> { \n" +
      "  construct( a : A, b : B ) { \n" +
      "    [[super]]( b ) \n" +
      "  }\n" +
      "}"
    )
    test({fcom, fsim})
  }

  function testGoToGenericClassNameThroughSuperIsBounded() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.CharSequence\n\n" +
      "class ##GosuClass<T extends CharSequence> {\n" +
      "  function superIsBounded() : [[GosuClass]]<? super T>\n" +
      "  {\n" +
      "    return null\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //==============================method/constructor calls

  //super(...)
  function TestGotoCtorThroughSuper() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  ##construct(s:String) {} \n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  construct(s:String) {[[super]](s)}\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoJCtorThroughSuper() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SupJavaClass {\n" +
      "    public ##SupJavaClass(String s) {}" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "  construct(s:String) {[[super]](s)}\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  //this(...)
  function testGoToCtorThroughThis() {
    test(
      "  construct() {\n" +
      "    [[this]](2, 2)\n" +
      "  }\n\n" +
      "  ##construct(a : int, b : int) {} \n"
    )
  }

  @Disabled("dpetrusca", "Corner case")
  function testGoToCtorThroughThisWithBlockParam() {
    test(
      "  ##construct( blk:block() ) {\n\n" +
      "  }\n\n" +
      "  construct(n: int, o:int, p:int) {\n" +
      "    [[this]]( \\->print('foo') )\n" +
      "  }"
    )
  }

  //block
  function testGoToBlockDeclThroughBlockInvocation() {
    test(
      "   var ##adder = \\ x : Number, y : Number -> { return x + y }\n" +
      "   function foo(){\n" +
      "     print([[adder]](2,3))\n" +
      "   }"
    )
  }

  //==============================field access

  function testGoToFieldThroughDirectInvokation() {
    test(
      "  var ##iMember = 10\n" +
      "  function bar()  { \n" +
      "    print([[iMember]])\n" +
      "  }"
    )
  }

  //this
  function testGoToFieldThroughThis() {
    test(
      "  var ##x : int = 10\n" +
      "  function getXUsingThis() : block(int) { \n" +
      "    return \\ y : int -> { this.[[x]] = y }\n" +
      "  }"
    )
  }

  //super
  function testGotoPropThroughSuper() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    var _local : String as ##Prop\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "    function foo() {\n" +
      "        var s = super.[[Prop]]\n" +
      "        super.Prop = \"\"\n" +
      "    }\n"  +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoPropThroughSuper2() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    var _local : String as ##Prop\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "    function foo() {\n" +
      "        var s = super.Prop\n" +
      "        super.[[Prop]] = \"\"\n" +
      "    }\n"  +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoPropGetterThroughSuper() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    property get ##Prop() : String {\n" +
      "        return \"\"\n" +
      "    }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "    function foo() {\n" +
      "        var s = super.[[Prop]]\n" +
      "    }\n"  +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoPropSetterThroughSuper() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    var _local : String\n\n" +
      "    property set ##Prop(s : String) {\n" +
      "        _local = s\n" +
      "    }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "    function foo() {\n" +
      "        super.[[Prop]] = \"\"\n" +
      "    }\n"  +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoBackwardCompatibilityPropGetThroughSuper() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    property get ##Prop() : String {\n" +
      "        return \"\"\n" +
      "    }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "    function foo() {\n" +
      "        var s = super.[[getProp]]()\n" +
      "    }\n"  +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoJPropGetThroughSuper() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SupJavaClass {\n" +
      "    public String ##getProp() {\n" +
      "        return \"\";\n" +
      "    }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "    function foo() {\n" +
      "        var s = super.[[Prop]]\n" +
      "    }\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoJPropSetThroughSuper() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SupJavaClass {\n" +
      "  public void ##setProp(String s) {} \n" +
      "  public String getProp() {return null;} \n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "    function foo() {\n" +
      "        super.[[Prop]] = \"123\"\n" +
      "    }\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  function testGotoJPropThroughSuper() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SupJavaClass {\n" +
      "    public boolean ##isProp() {\n" +
      "        return true;\n" +
      "    }" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "    function foo() {\n" +
      "        var b = super.[[Prop]]\n" +
      "    }\n" +
      "}"
    )
    test( {fsub, fsup})
  }

  //outer
  function testGoToFieldThroughOuter() {
    test(
      "  var ##myVar : int = 10\n" +
      "  class Inner {\n" +
      "    var myVar = \"inner field\"\n" +
      "    function getSomething() : int {\n" +
      "      return outer.[[myVar]]\n" +
      "    }\n" +
      "  }"
    )
  }

  //map access
  function testGoToFieldThroughMapAccess() {
    test(
      "  var ##myMap = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]][\"1\"]\n" +
      "  }"
    )
  }

  //=============================uses statement
  function testGoToClassDeclThroughUses() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.[[HashMap]]\n" +
      "class GosuClass {\n" +
      "    var myMap = new  java.util.HashMap<String, String>(){ \"1\" -> \"Monday\" }\n" +
      "}"
    )
    testGotoNonProjectFile(f,  "java.util.HashMap", "public class HashMap")
  }

  //=============================types

  //outer
  function testGoToClassNameFromOuter() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function refOuter() : String {\n" +
      "      return [[outer]].something()\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToClassNameFromOuterAsAnonymousPropertyPath() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "  function something() {\n" +
      "    new java.lang.Runnable(){\n" +
      "      override function run() {}\n" +
      "    }.[[outer]].something()\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //====annotations

  function testGoToAnnotationCtor1() {
    var fanno = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  ##construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@[[GosuAnnotation]]\n" +
      "class GosuClass {\n\n" +
      "}"
    )
    test({fusage, fanno})
  }

  function testGoToAnnotationCtor2() {
    var fanno = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  ##construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@[[GosuAnnotation]]()\n" +
      "class GosuClass {\n\n" +
      "}"
    )
    test({fusage, fanno})
  }

  function testGoToAnnotationCtor3() {
    var fanno = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  ##construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@[[GosuAnnotation]](\"qa\")\n" +
      "class GosuClass {\n\n" +
      "}"
    )
    test({fusage, fanno})
  }

  function testGoToAnnotationDecl() {
    var fanno = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuAnnotation implements IAnnotation{\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@[[GosuAnnotation]]\n" +
      "class GosuClass {\n\n" +
      "}"
    )
    test({fusage, fanno})
  }

  function testGoToJAnnotationDecl() {
    var fanno = new JavaAnnotationFile (
      "package some.pkg;\n" +
      "public @interface ##MyJavaAnnotation {\n" +
      "    String str();\n" +
      "    int val();\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@[[MyJavaAnnotation]](\"test\", 100)\n" +
      "class GosuClass { \n" +
      "}"
    )
    test({fusage, fanno})
  }

  function testGoToJAnnotationNamedAnnotationFields() {
    var fanno = new JavaAnnotationFile (
      "package some.pkg;\n" +
      "public @interface MyJavaAnnotation {\n" +
      "    int ##value();\n" +
      "}"
    )

    var fusage = new GosuClassFile (
      "package some.pkg\n" +
      "@MyJavaAnnotation(:[[value]] = 100)\n" +
      "class GosuClass { \n" +
      "}"
    )
    test({fusage, fanno})
  }

  //====enums

  function testGoToEnumCtor() {
    var f = new GosuEnumFile (
      "package some.pkg\n" +
      "enum ComplexEnum {\n" +
      "  Dog( 17 ), [[Cat]]( 15 ), Mouse( 5 )\n" +
      "  private ##construct( iAge: int ) {} \n" +
      "}"
    )
    test(f)
  }

  function testGoToInnerEnumCtor() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class HasInnerEnum {\n" +
      "  enum InnerEnum {\n" +
      "    Dog( 17 ), Cat( 15 ), [[Mouse]]( 5 )\n" +
      "    private ##construct( iAge: int ) {} \n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToInnerEnumCtor2() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class InnerSimpleEnumWithCtorAndOuterWithCtor {\n" +
      "  enum InnerEnum {\n" +
      "    [[Dog]], Cat, Mouse\n" +
      "    private ##construct() {}\n" +
      "  }\n" +
      "  construct(){}\n" +
      "}"
    )
    test(f)
  }

  //====program

  //top-level var
  function testGotoTopLevelVarDeclInProgram() {
    var f = new GosuProgramFile ("some/pkg/Program.gsp",
      "    var ##local = 10 \n" +
      "    print([[local]]) \n" )
    test(f)
  }

  //top-level property
  function testGotoTopLevelPropertyDeclInMethodInProgram() {
    var f = new GosuProgramFile ("some/pkg/Program.gsp",
      "    property get ##Prop() : String {return \"\"} \n" +
      "    print([[Prop]]) \n" )
    test(f)
  }

  //top-level method
  function testGotoTopLevelMethodDeclInMethodInProgram() {
    var f = new GosuProgramFile ("some/pkg/Program.gsp",
      "    function ##bar() : String {return \"\"} \n" +
      "    print([[bar]]()) \n" )
    test(f)
  }

  //====template
  function testGoToVarDecl() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "var ##local = \"local variable\"\n" +
        "print([[local]])\n" +
        "%>"
    )
    test(f)
  }

  function testGoToVarDec2() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "var ##local = \"local variable\"\n" +
        "%>\n" +
        "\<%\n" +
        "print([[local]])\n" +
        "%>"

    )
    test(f)
  }

  function testGoToVarDeclFromEmbedTemplateExpression() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%var ##myString = \"Hello\" %>\n" +
      "local var : \${[[myString]]}\n"
    )
    test(f)
  }

  function testGoToVarDeclFromAlternateTemplateExpression() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%var ##myString = \"Hello\" %>\n" +
      "local var :  \<%= [[myString]] %>"
    )
    test(f)
  }

  function testGoToFunctionDecl1() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "function ##method() {\n" +
        "}\n" +
        "function method1() {\n" +
        "  [[method]]()\n" +
        "}\n" +
        "%>"
    )
    test(f)
  }

  function testGoToFunctionDecl2() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "function ##method() {\n" +
        "}\n" +
        "%>\n" +
        "\<%\n" +
        "function method1() {\n" +
        "  [[method]]()\n" +
        "}\n" +
        "%>"

    )
    test(f)
  }

  function testGoToFunctionDecl3() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "function ##method() : String {\n" +
        "  return \"Hello\"\n" +
        "}\n" +
        "%>\n" +
        "the return value of the method : \<%= [[method]]()%>"
    )
    test(f)
  }

  function testGoToFunctionDecl4() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
        "\<%\n" +
        "function ##method() : String {\n" +
        "  return \"Hello\"\n" +
        "}\n" +
        "%>\n" +
        "the return value of the method : \${[[method]]()}"
    )
    test(f)
  }

  @Disabled("dpetrusca", "Template")
  function testGoToTemplateParamDeclFromAlternateTemplateExpression() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%@ params(_city : String, _state : String, ##_today : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \<%= [[_today]] %>"
    )
    test(f)
  }

  function testGoToExtendsClassFunctionDeclFromAlternateTemplateExpression() {
    var fEC = new GosuClassFile (
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "    static function ##a(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}"
    )
    var fTmpl = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%@ extends some.pkg.SuperClass %>\<%= [[a]](\"foo\") %>"
    )
    test({fEC, fTmpl})
  }

  @Disabled("dpetrusca", "Template")
  function testGoToTemplateParamDeclFromEmbedTemplateExpression() {
    var f = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%@ params(_city : String, _state : String, ##_today : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \${[[_today]]}\n"
    )
    test(f)
  }

  function testGoToExtendsClassFunctionDeclFromEmbedTemplateExpression() {
    var fEC = new GosuClassFile (
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "    static function ##a(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}"
    )
    var fTmpl = new GosuTemplateFile ("some/pkg/Template.gst",
      "\<%@ extends some.pkg.SuperClass %>\${[[a]](\"foo\")}"
    )
    test({fEC, fTmpl})
  }

  //====enhancement

  //template enhancement
  function testGoToTemplateDeclFromTemplateEnhancement() {
    var fTmpl = new GosuTemplateFile ("some/pkg/SomeTemplate.gst",
      "Great Enhancement Justice!"
    )
    var fEnh = new GosuEnhancementFile (
      "package some.pkg\n" +
      "enhancement SomeTemplateEnh : SomeTemplate {\n" +
      "  static function passThroughToRender() : String {\n" +
      "    return [[SomeTemplate]].renderToString()\n" +
      "  }\n" +
      "}"
    )
    testGotoTemplateFile({fTmpl, fEnh}, "some.pkg.SomeTemplate")
  }

  //program enhancement
  function testGoToProgramDeclFromProgramEnhancement() {
    var fPrg = new GosuTemplateFile ("some/pkg/GosuProgram.gsp",
      "classpath \"../\""
    )
    var fEnh = new GosuEnhancementFile (
      "package some.pkg\n" +
      "enhancement EnhancesGosuProgram : some.pkg.[[GosuProgram]] {\n" +
      "}"
    )
    testGotoProgramFile({fPrg, fEnh}, "some.pkg.GosuProgram")
  }

  //class enhancement
  function testGoToClassDeclFromClassEnhancement() {
    var fCls = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "}"
    )
    var fEnh = new GosuEnhancementFile (
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.pkg.[[GosuClass]] {\n" +
      "}"
    )
    test({fCls, fEnh})
  }

  function testGoToMethodDeclInJavaSuperClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class BaseA {\n" +
      "  public void ##baseFoo( String a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends BaseA {\n" +
      "  function caller( a : String ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({fsup, fsub})
  }

  function testGoToMethodDeclInJavaSuperClassTwoLevelsDeep() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "public class BaseA {\n" +
      "  public void ##baseFoo( String a ) {\n" +
      "  }\n" +
      "}"

    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "public class BaseB extends BaseA {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends BaseB {\n" +
      "  function caller( a : String ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({fsup1, fsup2, fsub})
  }

  function testGoToMethodDeclInSuperClassFromEnhancement() {
    var fSCls = new GosuClassFile (
      "package some.pkg\n" +
      "class SuperGosuClass {\n" +
      "    function ##bar() {\n" +
      "    }\n" +
      "}"
    )
    var fCCls = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SuperGosuClass {\n" +
      "}"
    )
    var fEnh = new GosuEnhancementFile (
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.pkg.GosuClass {\n" +
      "    function foo() {\n" +
      "        this.[[bar]]()\n" +
      "    }\n" +
      "}"
    )
    test({fSCls, fCCls, fEnh})
  }

  // TODO works for real, test fails because enhancement does not parse --
  //   "No function descriptor found for function, bar, on class, GosuClass"
  function testGoToMethodDeclInJSuperClassFromEnhancement() {
    var fSCls = new JavaClassFile (
      "package some.pkg;\n" +
      "public class SuperJavaClass {\n" +
      "    public void ##bar() {\n\n" +
      "    }\n" +
      "}"
    )
    var fCCls = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass extends SuperJavaClass {\n" +
      "}"
    )
    var fEnh = new GosuEnhancementFile (
      "package some.pkg\n" +
      "enhancement GosuClassEnhancement : some.pkg.GosuClass {\n" +
      "    function foo() {\n" +
      "        this.[[bar]]()\n" +
      "    }\n" +
      "}"
    )
    test({fSCls, fCCls, fEnh})
  }

  // TODO works for real, test fails because enhancement does not parse --
  //   "No function descriptor found for function, each, on class, ArrayList<String> [line:5 col:11]"
  function testGoToCoreEnhancementMethodDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function test() {\n" +
      "    var x = {\"123\", \"456\"}\n" +
      "    x.[[each]]( \\ elt -> print(elt))"  +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "gw.lang.enhancements.CoreIterableEnhancement" , "function each")
  }

  function testGoToCoreEnhancementMethodWhereDecl() {
      var f = new GosuClassFile (
        "package some.pkg\n" +
        "class GosuClass {\n" +
        "  function test() {\n" +
        "    var list = {1, 2, 3}\n" +
        "    list.[[where]](\\ elt -> elt >= 2)\n" +
        "  }\n" +
        "}"
      )
      testGotoNonProjectFile(f, "gw.lang.enhancements.CoreIterableEnhancement" , "function where")
    }

  // TODO works for real, test fails because enhancement does not parse --
  //  "No property descriptor found for property, size, on class, java.lang.String [line:5 col:13]"
  function testGoToCoreEnhancementPropertyDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function test() {\n" +
      "    var x = \"123\"\n" +
      "    print(x.[[HasContent]])\n"  +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "gw.lang.enhancements.CoreStringEnhancement" , "property get HasContent")
  }

  //====inner class

  function testGoToOuterMethod() {
    test(
      "  function ##something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[something]]()\n" +
      "    }\n" +
      "  }"
    )
  }

  function testGoToInnerMethodInnerMethodShadowsOuter() {
    test(
      "  function something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return [[something]]()\n" +
      "    }\n" +
      "    function ##something() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }"
    )
  }

  function testGoToOuterMethodFromOuter() {
    test(
      "  function ##something() : String {\n" +
      "    return \"something\"\n" +
      "  }\n" +
      "  class Inner {\n" +
      "    function getSomething() : String {\n" +
      "      return outer.[[something]]()\n" +
      "    }\n" +
      "    function something() : String {\n" +
      "      return \"inner something\"\n" +
      "    }\n" +
      "  }"
    )
  }

  function testGoToInnerPrivateCtorFromOuter() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "    var _inner : Inner\n" +
      "    construct() {\n" +
      "        _inner = new [[Inner]]()\n" +
      "    }\n" +
      "    class Inner {\n" +
      "        var _privateData : String\n" +
      "        private ##construct() {\n" +
      "            _privateData = \"privateData\"\n" +
      "        }\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToInnerPrivateFunctionFromOuter() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "    var _inner : Inner\n" +
      "    function accessPrivateInnerFunction() : String {\n" +
      "        return _inner.[[privateFunction]]()\n" +
      "    }\n" +
      "    class Inner {\n" +
      "        private function ##privateFunction() : String {\n" +
      "            return \"privateFunction\"\n" +
      "        }\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToInnerPrivateClassFieldFromOuter() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class CanReferenceInnerPrivateMembersFromOuter {\n" +
      "    var _inner : Inner\n" +
      "    function accessPrivateInnerData() : String {\n" +
      "        return _inner.[[_privateData]]\n" +
      "    }\n" +
      "    class Inner {\n" +
      "        var ##_privateData : String\n\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToInnerInnerClassNameFromInner() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ResolveInnerFromInner {\n" +
      "  class Inner {\n" +
      "    function makeInnerInner() : InnerInner {\n" +
      "      return new [[InnerInner]]()\n" +
      "    }\n" +
      "    class ##InnerInner { }\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToSiblingInnerClassNameFromInner() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ResolveInnerFromOuter {\n" +
      "  class Inner {\n" +
      "    function makeSecondInner() : SecondInner {\n" +
      "      return new [[SecondInner]]()\n" +
      "    }\n" +
      "  }\n" +
      "  class ##SecondInner {}\n" +
      "}"
    )
    test(f)
  }


  //====Anonymous

  function testGoToFieldDeclInJAnonymousClass() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    new Runnable(){\n" +
      "      var ##x = 5\n" +
      "      override function run(){\n" +
      "        [[x]] = 10\n" +
      "      }\n" +
      "    }.run()\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToFieldDeclInGAnonymousClass() {
    var f0 = new GosuClassFile (
      "package some.pkg\n" +
      "class FooClass {\n" +
      "}"
    )
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new FooClass(){\n" +
      "      var ##x = 5\n" +
      "      function bar(){\n" +
      "        [[x]] = 10\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
    test({f0, f})
  }

  function testGoToClassNameFromOuterInAnonymousClass() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var me : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = [[outer]]\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToClassNameFromNestedBlockThenOuterThenBlockOuterPointer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class ##GosuClass {\n" +
      "  function bar() {\n" +
      "    var b1 = \\-> new Callable() {\n" +
      "      override function call() : block():Callable<block():block():int> {\n" +
      "        return \\-> new Callable<block():block():int> (){\n" +
      "          override function call() : block():block():int {\n" +
      "            return \\->\\-> outer.[[outer]].IntProperty\n" +
      "          }\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n"  +
      "  property get IntProperty() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToNestedBlockOuterFromNestedBlockThenOuterThenBlockOuterPointer() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var b1 = \\-> new Callable() ##{\n" +
      "      override function call() : block():Callable<block():block():int> {\n" +
      "        return \\-> new Callable<block():block():int> (){\n" +
      "          override function call() : block():block():int {\n" +
      "            return \\->\\-> [[outer]].outer.IntProperty\n" +
      "          }\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n"  +
      "  property get IntProperty() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToLocalVarDeclFromAnonymousClassReference() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var ##me : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        [[me]] = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return me\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  function testGoToLocalVarDeclWithItsValueAssignedInAnonymousClass() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testOuter() : GosuClass {\n" +
      "    var ##me : GosuClass = null\n" +
      "    new java.lang.Runnable() {\n" +
      "      override function run() {\n" +
      "        me = outer\n" +
      "      }\n" +
      "    }.run()\n" +
      "    return [[me]]\n" +
      "  }\n" +
      "}"
    )
    test(f)
  }

  //====arrays
  function testGoToClassNameDeclFromNewExpression() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "    function makeGosuArray() : GosuClass[] {\n" +
      "        return new GosuClass[] {new [[GosuClass]]()}\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToClassNameDeclFromNewArrayExpression() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "    function makeGosuArray() : GosuClass[] {\n" +
      "        return new [[GosuClass]][] {new GosuClass()}\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToClassNameDeclFromArrayClassType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class ##GosuClass {\n" +
      "    function makeGosuArray() : [[GosuClass]][] {\n" +
      "        return new GosuClass[] {new GosuClass()}\n" +
      "    }\n" +
      "}"
    )
    test(f)
  }

  function testGoToArrayDecl() {
    test(
      "  var ##myStrings = new String[][] {{\"Mr. \", \"Mrs. \", \"Ms. \"}, {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[myStrings]][0][0]\n" +
      "  }"
    )
  }

  function testGoToArrayListDecl() {
    test(
      "  var ##myStrings = {{\"Mr. \", \"Mrs. \", \"Ms. \"},\n" +
      "                            {\"Smith\", \"Jones\"}}\n" +
      "  function foo(){\n" +
      "    var s = [[myStrings]][0][0]\n" +
      "  }"
    )
  }

  function testGoToArrayListDecl2() {
    test(
      "  var ##places = new java.util.ArrayList<String> ({\"Buenos Aires\", \"Córdoba\", \"La Plata\"})\n" +
      "  function foo(){\n" +
      "    var s = [[places]][0]\n" +
      "  }"
    )
  }

  function testGoToMapDecl() {
    test(
      "  var ##myMap = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]].get(\"1\")\n" +
      "  }"
    )
  }

  //====compound types
  function testGoToMethodDeclInCompoundType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.io.Closeable\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "  var compoundType : Runnable & Closeable\n" +
      "  function bar() {\n" +
      "    compoundType.[[run]]()\n\n" +
      "  }" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Runnable", "void run();")
  }

function testGoToMethodDeclInCompoundType1() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.io.Closeable\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "  var compoundType : Runnable & Closeable\n" +
      "  function bar() {\n" +
      "    compoundType.[[close]]()\n\n" +
      "  }" +
      "}"
    )
    testGotoNonProjectFile(f, "java.io.Closeable", "void close()")
  }

  function testGoToCompoundTypeDecl() {
    var fIBar = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IBar {\n" +
      "  function bar() : String\n" +
      "  function bar2( p1 : boolean ) : boolean\n" +
      "}"
    )
    var fIFoo = new GosuInterfaceFile (
      "package some.pkg\n" +
      "interface IFoo {\n" +
      "  function foo() : String\n" +
      "  function foo2( p1 : int ) : int\n" +
      "}"
    )
    var fBarImpl = new GosuClassFile (
      "class BarImpl implements IBar {\n" +
      "  var _bar: String\n" +
      "  construct( bar: String ) {\n" +
      "    _bar = bar\n" +
      "  }\n" +
      "  override function bar() : String {\n" +
      "    return _bar\n" +
      "  }\n" +
      "  override function bar2( p1: boolean ) : boolean {\n" +
      "    return p1\n" +
      "  }\n" +
      "}"
    )
    var fFooBarImpl = new GosuClassFile (
      "class FooBarImpl implements IFoo, IBar {\n" +
      "  var _foo: String\n" +
      "  delegate _bar represents IBar\n" +
      "  construct( foo: String, bar: String ) {\n" +
      "    _foo = foo\n" +
      "    _bar = new BarImpl( bar )\n" +
      "  }\n" +
      "  override function foo() : String {\n" +
      "    return _foo\n" +
      "  }\n" +
      "  override function foo2( p1: int ) : int {\n" +
      "    return p1\n" +
      "  }\n" +
      "}"
    )
    var f = new GosuClassFile (
      "class GosuClass {\n" +
      "  function testCaptureOfCompoundTypeWorksProperly() {\n" +
      "    var ##val : IFoo&IBar = new FooBarImpl(\"a\", \"b\")\n" +
      "    var blk = \\-> [[val]]\n" +
      "    print( val== blk() )\n" +
      "  }\n" +
      "}"
    )
    test({fIBar, fIFoo, fBarImpl, fFooBarImpl, f})
  }

  //=============================default types
  //====Number
  function testGotoNumberDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myNumber : [[Number]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Double", "public final class Double")
  }

  //====String
  function testGotoStringDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myString : [[String]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.String", "public final class String")
  }

  //====Boolean
  function testGotoBooleanDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myBoolean : [[Boolean]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Boolean", "public final class Boolean")
  }

  //====java.util.Date
  function testGotoDateTimeDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myDateTime : [[java.util.Date]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.util.Date", "public class Date")
  }

  //====List
  function testGotoListDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myList : [[List]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.util.List", "public interface List <E>")
  }

  //====Object
  function testGotoObjectDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myObject : [[Object]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Object", "public class Object")
  }

  //====Array
  function testGotoArrayDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myArray : [[Object[]]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Object", "public class Object")
  }

  //====Bean
  function testGotoBeanDecl() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myBean : [[Bean]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.Object", "public class Object")
  }

  @Disabled("dpetrusca", "Corner case")
  function testGotoTypeDecl() {
    var s: Type
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  var myType : [[Type]]\n" +
      "}"
    )
    testGotoNonProjectFile(f, "gw.internal.gosu.parser.MetaType", "public class MetaType")
  }

  @Disabled("dpetrusca", "Corner case")
  function testGoToTypeDeclWithGenericDefaultType() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method<T>(type : [[Type]]<T>) {\n" +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "gw.internal.gosu.parser.MetaType", "public class MetaType")
  }

  //====some methods

  function testGotoStringContainsMethod() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function testFunction() {\n" +
      "    \"qaz\".[[contains]](\"qa\") \n" +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.lang.String",  "public boolean contains(java.lang.CharSequence")
  }

  function testGotoSetIteratorMethod() {
    var f = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.HashMap\n" +
      "class GosuClass {\n" +
      "  function testFunction() {\n" +
      "    var map = new HashMap<String, String>() \n" +
      "    map.keySet().[[iterator]]() \n" +
      "  }\n" +
      "}"
    )
    testGotoNonProjectFile(f, "java.util.Set",  "java.util.Iterator<E> iterator()")
  }

  function testGotoBoundedGenericMethodInJavaSuperClassFromMoreNarrowlyBoundedGenericGosuClassUsingUncheckedBoundingTypes() {
    var fsup = new JavaClassFile (
        "package some.pkg;\n" +
            "public class BaseA {\n" +
            "  public <U> void foo( U u ) {\n" +
            "  }\n" +
            "  public <T extends java.util.Collection> void ##foo( T t ) {\n" +
            "  }\n" +
            "}"
    )
    var fsub = new GosuClassFile (
        "package some.pkg\n" +
            "class DerivedD<T extends java.util.List> extends BaseA {\n" +
            "  function caller( t : T ) {\n" +
            "    [[foo]]( t )\n" +
            "  }\n" +
            "}"
    )
    test({ fsup, fsub })
  }

  function testGotoRegularMethodInJavaSuperClassFromBoundedGenericGosuClassWhenLesserGenericMatchExists() {
    var fsup = new JavaClassFile (
        "package some.pkg;\n" +
            "public class BaseA {\n" +
            "  public <U> void foo( U u ) {\n" +
            "  }\n" +
            "  public <T extends java.util.Collection<String>> void foo( T t ) {\n" +
            "  }\n" +
            "  public void ##foo( java.util.List<String> r ) {\n" +
            "  }\n" +
            "}"
    )
    var fsub = new GosuClassFile (
        "package some.pkg\n" +
            "class DerivedD<T extends java.util.List<String>> extends BaseA {\n" +
            "  function caller( t : T ) {\n" +
            "    [[foo]]( t )\n" +
            "  }\n" +
            "}"
    )
    test({ fsup, fsub })
  }
}
