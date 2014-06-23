/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages

uses com.intellij.codeInsight.TargetElementUtilBase
uses com.intellij.psi.PsiFile
uses com.intellij.psi.search.searches.ReferencesSearch
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.testharness.Disabled
uses gw.testharness.KnownBreak

uses java.util.ArrayList
uses java.util.List

class FindUsagesTest extends GosuTestCase {

  function testFindEnumConst1() {
    test(
      "package test\n" +
      "enum ComplexEnum { \n" +
      "  D^^og( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: ComplexEnum = [[Dog]] \n" +
      "  } \n" +
      "}"
    )
  }

  function testFindEnumConst2() {
    test(
      "package test\n" +
      "enum ComplexEnum { \n" +
      "  Dog( 17 ), Cat( 15 ), Mouse( 5 )\n\n" +
      "  private construct( iAge: int ) { \n" +
      "    var s: ComplexEnum = [[D^^og]] \n" +
      "  } \n" +
      "}"
    )
  }

  function testFindUsageEnumConstr() {
    test(
      "package some.pkg\n" +
      "enum ComplexEnum {\n" +
      "  [[Dog( 17 )]], [[Cat( 15 )]], [[Mouse( 5 )]]\n" +
      "  private const^^ruct( iAge: int ) {} \n" +
      "}"
    )
  }

  function testFindUsageInnerEnumConstr() {
    test(
      "package some.pkg\n" +
      "class HasInnerEnum {\n" +
      "  enum InnerEnum {\n" +
      "    [[Dog( 17 )]], [[Cat( 15 )]], [[Mouse( 5 )]]\n" +
      "    private const^^ruct( iAge: int ) {} \n" +
      "  }\n" +
      "}"
    )
  }

  function testFindUsageInnerEnumConstr2() {
    test(
      "package some.pkg\n" +
      "class InnerSimpleEnumWithCtorAndOuterWithCtor {\n" +
      "  enum InnerEnum {\n" +
      "    [[Dog]], [[Cat]], [[Mouse]]\n" +
      "    private const^^ruct() {}\n" +
      "  }\n" +
      "  construct(){}\n" +
      "}"
    )
  }

  function testFindUsagesOfLocalVarDeclInMethod() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var f^^oo : String;\n" +
      "  function test() {" +
      "    [[foo]] = \"some text\"; \n" +
      "  }\n" +
      "}"
    );
  }


  function testFindLocalVarReturnedFromMethod() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  private static var _f^^oo : String \n"+
      "  function getFoo() : String{ \n"+
      "    return [[_foo]] \n"+
      "  }\n"+
      "}"
    );
  }

  function testFindUsagesOfLocalVarDeclInPropertyGetter() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  property get Prop() : int{\n" +
      "    var lo^^cal : int = 10 \n" +
      "    return [[local]]\n" +
      "  }\n" +
      "}"
    )
  }


  function testFindUsagesOfLocalVarDeclInPropertySetter() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( i : int ) {\n" +
      "    var lo^^cal = 10 \n" +
      "    [[local]] = i \n" +
      "  }\n" +
      "}"
    )
  }

  function testFindUsagesOfVarDeclInForLoop() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function test() {" +
      "    for (it^^em in 1..10) { \n"+
      "      print([[item]])\n"+
      "    }\n" +
      "  }\n" +
      "}"
    );
  }

  function testFindUsagesOfVarDeclInForLoopIndex() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function method() {\n" +
      "    for (s in \"a test string\" index i^^x){\n" +
      "      print([[ix]])\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindUsagesOfVarDeclInTryCatch() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function method() {\n" +
      "    try{\n" +
      "    }catch(e^^x){\n" +
      "      throw [[ex]]\n" +
      "    }\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindUsagesOfVarDeclInConst() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  cons^^truct() {}\n" +
      "  construct(x:int) {\n" +
      "    var a = new [[MyClass]]() \n" +
      "  }\n" +
      "}"
    );
  }

  function testFindUsagesOfConstrCallFromThis() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  cons^^truct() {}\n" +
      "  construct(x:int) {\n" +
      "    [[this()]] \n" +
      "  }\n" +
      "}"
    );
  }

  @KnownBreak("dpetrusca", "", "PL-22795")
  function testFindUsagesOfConstWithSuper() {
    test({
      "package test\n" +
      "class GosuClass extends MyClass {\n" +
      "  construct() {" +
      "    [[super()]] \n" +
      "  }\n" +
      "  construct(x: int) {" +
      "    this() \n" +
      "  }\n" +
      "}"
    ,
      "package test\n" +
      "class MyClass {\n" +
      "  cons^^truct() {} \n" +
      "}"
    });
  }

  @Disabled("dpetrusca", "Corner case")
  function testFindConstrThroughThisWithBlockParam() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "con^^struct( blk:block() ) {\n\n" +
      "  }\n\n" +
      "  construct(n: int, o:int, p:int) {\n" +
      "    [[this( \\->print('foo') )]] \n" +
      "  }\n"+
      "}"
    )
  }





  function testFindFunctionTypeParamDeclBlockOfBlockToT() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function method<^^T>( t : Type<[[T]]> ) : block():Type {\n" +
      "    return \\->[[T]]\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindTypeParamDeclWithGenericDefaultType() {
    test(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "  function method<^^T>(type : Type<[[T]]>) {\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindClassTypeParamDecl() {
    test(
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <^^T extends GosuClass<[[T]]>> { \n" +
      "  function fromNumber( p0: Integer ) : [[T]] { \n" +
      "    return null \n" +
      "  }\n" +
      "}"
    )
  }

  function testFindClassTypeFromExtends() {
    test(
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <^^T extends GosuClass<[[T]]>> { \n" +
      "  function fromNumber( p0: Integer ) : [[T]] { \n" +
      "    return null \n" +
      "  }\n" +
      "}"
    )
  }

  function testFindTypeVarThroughGenericNewTypeParamExpression() {
    test(
      "package some.pkg\n" +
      "uses java.lang.Integer\n\n" +
      "class GosuClass <^^T extends GosuClass<[[T]]>> { \n" +
      "  construct( value : Integer ) {} \n" +
      "  function fromNumber( p0: Integer ) : [[T]] {\n" +
      "    return new [[T]]( p0 )\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindConstrThroughGenericSuper() {
    test(
    {
      "package some.pkg\n" +
      "class ComplexGenericType<A,B> extends SimpleGenericType<B> { \n" +
      "  construct( a : A, b : B ) { \n" +
      "    [[super( b )]] \n" +
      "  }\n" +
      "}",
      "package some.pkg\n" +
      "class SimpleGenericType<T> { \n" +
      "  const^^ruct( member : T ) {} \n" +
      "}"
    })
  }

  function testFindGenericClassNameThroughSuperIsBounded() {
    test(
      "package some.pkg\n" +
      "uses java.lang.CharSequence\n\n" +
      "class Gosu^^Class<T extends CharSequence> {\n" +
      "  function superIsBounded() : [[GosuClass<? super T>]]\n" +
      "  {\n" +
      "    return null\n" +
      "  }\n" +
      "}"
    )
  }

  function TestFindConstrThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  construct(s:String) {[[super]](s)}\n" +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  const^^ruct(s:String) {} \n" +
      "}"
    })
  }

  function testFindJConstrThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "  construct(s:String) {[[super(s)]]}\n" +
      "}",
      "//JAVA\n" +
      "package some.pkg;\n" +
      "class SupJavaClass {\n" +
      "  public SupJava^^Class(String s) {}\n" +
      "}"
    })
  }

  function testFindConstrThroughThis() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  construct() {\n" +
      "    [[this(2, 2)]]\n" +
      "  }\n\n" +
      "  const^^ruct(a : int, b : int) {} \n"+
      "}"
    )
  }

  function testFindBlockDeclThroughBlockInvocation() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var ad^^der = \\ x : Number, y : Number -> { return x + y }\n" +
      "  function foo(){\n" +
      "    print([[adder]](2,3))\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindFieldThroughThis() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var ^^x : int = 10\n" +
      "  function getXUsingThis() : block(int) { \n" +
      "    return \\ y : int -> { [[this.x]] = y }\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindPropThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = [[super.Prop]]\n" +
      "    [[super.Prop]] = \"\"\n" +
      "  }\n"  +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  var _local : String as Pro^^p\n" +
      "}"
    })
  }

  function testFindPropThroughSuper2() {
    test(
    {
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "    var _local : String as Prop\n" +
      "}",
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = [[super.Pr^^op]]\n" +
      "    [[super.Prop]] = \"\"\n" +
      "  }\n"  +
      "}"
    })
  }

  function testFindPropGetterThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = [[super.Prop]]\n" +
      "  }\n"  +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  property get Pro^^p() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindPropSetterThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    [[super.Prop]] = \"\"\n" +
      "  }\n"  +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  var _local : String\n\n" +
      "  property set Pro^^p(s : String) {\n" +
      "    _local = s\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindBackwardCompatibilityPropGetThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupGosuClass {\n" +
      "  function foo() {\n" +
      "    var s = [[super.getProp()]]\n" +
      "  }\n"  +
      "}",
      "package some.pkg\n" +
      "class SupGosuClass {\n" +
      "  property get Pr^^op() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindJPropGetThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "  function foo() {\n" +
      "    var s = [[super.Prop]]\n" +
      "  }\n" +
      "}",
      "//JAVA\n" +
      "package some.pkg;\n" +
      "class SupJavaClass {\n" +
      "  public String get^^Prop() {\n" +
      "    return \"\";\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindJPropSetThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "  function foo() {\n" +
      "    [[super.Prop]] = \"123\"\n" +
      "  }\n" +
      "}",
      "//JAVA\n" +
      "package some.pkg;\n" +
      "class SupJavaClass {\n" +
      "  public void set^^Prop(String s) {} \n" +
      "  public String getProp() {return null;} \n" +
      "}"
    })
  }

  function testFindJPropThroughSuper() {
    test(
    {
      "package some.pkg\n" +
      "class GosuClass extends SupJavaClass {\n" +
      "  function foo() {\n" +
      "    var b = [[super.Prop]]\n" +
      "  }\n" +
      "}",
      "//JAVA\n" +
      "package some.pkg;\n" +
      "class SupJavaClass {\n" +
      "  public boolean is^^Prop() {\n" +
      "    return true;\n" +
      "  }" +
      "}"
    })
  }

  function testFindUsageFieldThroughOuter() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var my^^Var : int = 10\n" +
      "  class Inner {\n" +
      "    var myVar = \"inner field\"\n" +
      "    function getSomething() : int {\n" +
      "      return [[outer.myVar]]\n" +
      "    }\n" +
      "  }\n"+
      "}"
    )
  }

  //map access
  function testFindUsageFieldThroughMapAccess() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var my^^Map = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]][\"1\"]\n" +
      "  }\n"+
      "}"
    )
  }

  //delegates
  /*
  function testFindUsageDelegateFromInvocationFromThis() {
    test(
    {
      "package test"+
      "interface IFoo {\n" +
      "  function canCopy() : boolean\n" +
      "  function copy() : void\n" +
      "}\n",
      "package test\n" +
      "class Foo implements IFoo {\n" +
      "  var _myOwner : Object\n" +
      "  construct(owner : Object) {\n" +
      "    _myOwner = owner\n" +
      "  }\n" +
      "  override function canCopy() : boolean { return true }\n"+
      "}",
      "package test\n" +
      "class MyWindow implements IFoo {\n" +
      "  delegate _foo represents IFoo \n" +
      "  construct() {\n" +
      "    _foo = new Foo( this )\n" +
      "  }\n" +
      "}"
    })
  } */


  //====annotations
  function testFindUsageAnnotationConstr1() {
    test(
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]]\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  const^^ruct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    })
  }

  function testFindUsageAnnotationConstr2() {
    test(
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]]()\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  cons^^truct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    })
  }

  function testFindUsageAnnotationConstr3() {
    test(
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]](\"qa\")\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  con^^struct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n\n" +
      "}"
    })
  }

  function testFindUsageAnnotationDecl() {
    test(
    {
      "package some.pkg\n" +
      "@[[GosuAnnotation]]\n" +
      "class GosuClass {\n\n" +
      "}",
      "package some.pkg\n" +
      "class Gosu^^Annotation implements IAnnotation{\n" +
      "}"
    })

  }

  function testFindUsageJAnnotationDecl() {
    test(
    {
      "package some.pkg\n" +
      "@[[MyJavaAnnotation]](\"test\", 100)\n" +
      "class GosuClass { \n" +
      "}",
      "package some.pkg;\n" +
      "public @interface MyJava^^Annotation {\n" +
      "  String str();\n" +
      "  int val();\n" +
      "}"
    })
  }








  function testFindConstUsageWithStaticVarParam() {
    test(
      "package test\n" +
      "class MyClass<T> {\n" +
      "  static var _staticVar : String as StaticProperty\n\n" +
      "  con^^struct(o : Object) {}\n\n" +
      "  construct(i:int) {[[this(_staticVar)]]}\n" +
      "}"
    )
  }

  function testFindConstUsageWithTypeParamParam() {
    test(
      "package test\n" +
      "class MyClass<T> {\n" +
      "  con^^struct(o : Object) {}\n\n" +
      "  construct(i:int,j:int,k:int,l:int,m:int,n:int) {[[this(T)]]}\n\n" +
      "}"
    )

  }


  function testFindUsageOfInnerPrivateConstrFromOuter() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "    var _inner : Inner\n\n" +
      "    construct()\n" +
      "    {\n" +
      "        _inner = new [[Inner]]()\n" +
      "    }\n\n" +
      "    class Inner\n" +
      "    {\n" +
      "        var _privateData : String\n\n" +
      "        private con^^struct()\n" +
      "        {\n" +
      "            _privateData = \"privateData\"\n" +
      "        }\n\n" +
      "    }\n" +
      "}"
    )
  }

  function testFindUsagesOfConstrCallWithParam() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var te^^st:int \n"+
      "  construct(x:int) {\n" +
      "    [[test]] =x \n" +
      "    var a = new MyClass(23);\n" +
      "  }\n" +
      "}"
    );
  }

  function testFindMethodFromConstr() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  construct() {\n" +
      "    [[myF()]]\n" +
      "  }\n" +
      "  function ^^myF() {\n" +
      "  }\n" +
      "}"
    );
  }

  function testFindMethodCalledWithinSameMethod() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function my^^F() {\n" +
      "    if (false) {\n" +
      "      [[myF()]]" +
      "    }" +
      "  }\n" +
      "}"
    );
  }

  function testFindMethodDeclStaticMethod() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  static public function say^^What() : String { \n"+
      "    return \"Quoi?\" \n"+
      "  } \n" +
      "  function callStatic(){ \n"+
      "    return [[sayWhat()]]"+
      "  }\n"+
      "}\n"
    );
  }

  function testFindUsagesOfMethodsWithParams() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  construct() {" +
      "    [[myF(89,67)]]" +
      "  }\n" +
      "  function my^^F(arg1 : int, arg2 : int) {\n" +
      "    if (false) {\n" +
      "      [[myF(23,45)]]\n" +
      "    }\n" +
      "  }\n" +
      "}"
    );
  }

  function testFindUsagesOfClass() {
    test({
      "package test\n" +
      "class MyClass {\n" +
      "  var classA = new [[OuterClass]]()\n" +
      "}"
    ,
      "package test\n" +
      "class Outer^^Class {\n" +
      "  construct() {" +
      "  }\n" +
      "}"
    });
  }


  function testFindUsagesOfInnerClass() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  construct() {" +
      "  }\n" +
      "  class Inner^^Class {\n" +
      "    construct() {" +
      "    }\n" +
      "  }\n" +
      "  function myF() {\n" +
      "    var inner = new [[InnerClass]]() \n"+
      "  }\n" +
      "}"
    );
  }

  function testFindClassNameUsagesForArrayClassTypeFromClassDecl() {
    test(
      "package test\n" +
      "class My^^Class {\n" +
      "    function makeGosuArray() : [[MyClass[]~]] {\n" +
      "        return new [[MyClass]]~[] {new [[MyClass]]()}\n" +
      "    }\n" +
      "}"
    )
  }

  function testFindOuterClassFieldInInnerClass() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var my^^Var : int = 10\n" +
      "  class Inner {\n" +
      "    var myVar = \"inner field\"\n" +
      "    function getSomething() : int {\n" +
      "      return [[outer.myVar]]\n" +
      "    }\n" +
      "  }"+
      "}"
    )
  }

  function testFindUsagesOfAnonClass() {
    test({
      "package test\n" +
      "class MyClass2 {\n" +
      "  function test() { [[MyClass.runme()]] }\n"+
      "}",
      "package test\n" +
      "class MyClass {\n" +
      "  static public function run^^me() {\n" +
      "    var counter = new Object() {" +
      "    private var i = 0\n" +
      "    public function incrementMe () {\n"+
      "      i = i + 1\n"+
      "    }\n"+
      "  }\n" +
      "  }\n" +
      "}"
    });
  }

  function testFindFieldDeclInGAnonClass() {
    test({
      "package test\n" +
      "class FooClass {\n" +
      "}"
    ,
      "package test\n" +
      "class GosuClass {\n" +
      "  function doit() {\n" +
      "    var o = new FooClass(){\n" +
      "      var gfx = 5\n" +
      "      function bar(){\n" +
      "        [[g^^fx]] = 10\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindFieldUsagesInMapAccess() {
    test(
      "package test\n" +
      "class MyClass2 {\n" +
      "  var my^^Map = new  java.util.HashMap<String, String>(){ \"1\" -> \"one\", \"2\" -> \"two\" }\n\n" +
      "  function foo(){\n" +
      "    var s = [[myMap]][\"1\"]\n" +
      "  }\n"+
      "}"
    )
  }


  function testFindUsagesOfMethodAnnotations() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  construct() {\n" +
      "    myF1()\n" +
      "  }\n" +
      "@[[java.lang.Depre^^cated]]\n" +
      "  function myF1() {\n" +
      "  }\n" +
      "@[[java.lang.Deprecated]]\n" +
      "  function myF2() {\n" +
      "  }\n" +
      "@[[java.lang.Deprecated]]\n" +
      "  function myF3() {\n" +
      "  }\n" +
      "}\n"
    );
  }


  function testFindUsagesOfClassAnnotations() {
    test({
      "package test\n" +
      "@[[java.lang.Deprecated]]\n" +
      "class MyClass {\n" +
      "}\n"
    ,
      "package test\n" +
      "@[[java.lang.Depr^^ecated]]\n" +
      "class MyClass2 {\n" +
      "}\n"
    });
  }


  function testFindUsagesOfAnnotationsWithArguments() {
    test({
      "package test\n" +
      "@[[Author]](\"Clarke\")\n" +
      "class test1{\n" +
      "  }\n",
      "package test\n" +
      "@[[Author]](\"Hawking\")\n" +
      "class test2{\n" +
      "  }\n",
      "package test\n" +
      "class test1{\n" +
      "  }\n",
      "package test\n" +
      "class Aut^^hor implements IAnnotation {\n" +
      "  private var _author : String as AuthorName\n" +
      "  construct(a : String) {\n" +
      "    _author = a;\n" +
      "  }\n" +
      "}\n"
    });
  }


  function testFindAnnotationConstr() {
    test({
      "package test\n" +
      "@[[GosuAnnotation]](\"qa\")\n" +
      "class GosuClass {\n\n" +
      "}"
    ,
      "package test\n" +
      "class GosuAnnotation implements IAnnotation{\n" +
      "  private var _value : String\n\n" +
      "  con^^struct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n" +
      "  construct() {\n" +
      "    this(\"EMPTY\")\n" +
      "  }\n" +
      "}"
    })
  }

  function testFindUsagesOfEnumFromDecl() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "enum Fruit^^Type {\n" +
      "  Apple, Orange, Banana, Kiwi, Passionfruit\n"+
      "}\n"+
      "  var foo = [[FruitType]].Banana\n" +
      "}"
    );
  }

  function testFindUsagesOfEnumElements1() {
    test(
      "package test\n" +
      "class GosuClass {\n" +
      "  var y: TestEnum = [[First]] \n" +
      "  enum TestEnum { \n" +
      "    Fi^^rst, Second \n" +
      "  }\n"+
      "}"
    )
  }

  function testFindUsagesOfEnumElements2() {
    test(
      "package test\n" +
      "class GosuClass {\n" +
      "  var y: TestEnum = [[Fi^^rst]] \n" +
      "  enum TestEnum { \n" +
      "    First, Second \n" +
      "  }\n"+
      "}"
    )
  }

  function testFindInnerEnumConstr() {
    test(
      "package test\n" +
      "class HasInnerEnum { \n" +
      "  construct(){}\n" +
      "  enum InnerEnum { \n" +
      "    [[Dog( 17 )]], [[Cat( 15 )]], [[Mouse( 5 )]]\n\n" +
      "    var _iAge : int as Age\n\n" +
      "    private con^^struct( iAge: int ) { \n" +
      "    }\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindUsagesOfLocalVarDeclInBlock() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function test1( myMethod (String) : void) {\n" +
      "    var x: List\n" +
      "    x.each( \\ l^^t -> print([[lt]]))\n" +
      "  }\n" +
      "}"
    );
  }


  function testFindUsagesOfVarDeclInBlockParam() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function myMethod( my^^call (T : String) : String) {\n" +
      "    [[mycall]](\"Test\")\n" +
      "  }\n" +
      "}\n"
    )
  }

  function testFindClassFieldDeclThroughClassInitializer() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _p^^od : String\n" +
      "  function foo() {\n" +
      "    var anObj = new GosuClass(){ : [[_pod]] = \"Studio\"}\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindLocalVariableDeclThroughUsuageInPropInitializer() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _prop : int as Prop\n" +
      "  function bar() {\n" +
      "    var ^^i = 5\n" +
      "    var x = new GosuClass(){:Prop = [[i]]}\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindFieldPropertyThroughArrayInitializer() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _prop1 : String as Pr^^op1\n" +
      "  var _prop2 : String as Prop2\n\n" +
      "  function foo(){\n" +
      "    var myStrings = new String[][] {{[[Prop1]]}, {Prop2}}\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindPropertyGetterThroughMapInitializer() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  property get Pro^^p1() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  property get Prop2() : String {\n" +
      "    return \"\"\n" +
      "  }\n" +
      "  function foo(){\n" +
      "    var myMap = new  java.util.HashMap<String, String>(){ [[Prop1]] -> Prop2 }\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindUsagesOfEnhInMethod() {
    test({
      "package test\n" +
      "class MyClass {\n" +
      "  function myMethod( mycall (T : String) : String) {\n"+
      "    mycall(\"Test\")\n"+
      "    print([[\"This is my string\".myMethod()]])\n"+
      "  } \n" +
      "\n}",
      "package test\n" +
      "enhancement StringTestEnhancement : java.lang.String {\n"+
      "  public function my^^Method(): String {\n"+
      "    return \"Secret message!\" \n"+
      "  } \n"+
      "}\n"
    });
  }

  function testFindUsagesOfEnhInProperty() {
    test({
      "package test\n" +
      "class MyClass {\n" +
      "  function myMethod( mycall (T : String) : String) {\n"+
      "  mycall(\"Test\")\n"+
      "  print([[\"This is my string\".myProperty]])\n"+
      "\n}"
    ,
      "package test\n" +
      "enhancement StringTestEnhancement : java.lang.String {\n"+
      "  public property get my^^Property() : String { \n"+
      "    return \"length : \" + this.length()\n"+
      "  }\n"+
      "}\n"
    });
  }

  function testFindUsagesOfVarDeclInPropertySetter() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _x : int\n" +
      "  property set Prop( ^^i : int ) {\n" +
      "    _x = [[i]]\n" +
      "  }"
    )
  }

  function testFindUsagesOfPropertySetter() {
    test({
      "package test\n" +
      "class propertySetterClass { \n"+
      "  var f = new MyClass()\n"+
      "  function test() { \n" +
      "    [[f.Field3]] = \"Yes\"\n"+
      "    var g = f.Field3\n" +
      "  }\n" +
      "}\n",
      "package test\n" +
      "class MyClass {\n" +
      "private var _field3 : String\n"+
      "  property get Field3() : String {\n"+
      "    return _field3 \n"+
      "  }\n"+
      "  property set Field^^3(str : String) {\n"+
      "    _field3 = str \n"+
      "  }\n"+
      "}\n"
    });
  }

  function testFindUsagesOfPropertyGetter() {
    test({
      "package test\n" +
      "class propertyGetterClass { \n"+
      "  var f = new MyClass()\n"+
      "  function test() { \n" +
      "    f.Field3 = \"Yes\"\n"+
      "    var g = [[f.Field3]]\n" +
      "  }\n" +
      "}\n",
      "package test\n" +
      "class MyClass {\n" +
      "private var _field3 : String\n"+
      "  property get Field^^3() : String {\n"+
      "    return _field3 \n"+
      "  }\n"+
      "  property set Field3(str : String) {\n"+
      "    _field3 = str \n"+
      "  }\n"+
      "}\n"
    });
  }

    @KnownBreak("Studio Team", "", "")
  function testFindUsagesInStringTemplate() {
    test(
      "package test\n" +
      "class MyClass {\n" +
    "  var My^^Calc = \"One plus two is <%= 1 + 2 %>\"\n"+
      "  function myMethod() \n{"+
      "    print(\"testing \${ [[MyCalc]] }\")\n"+
      "  } \n" +
      "}\n"
    );
  }

  function testFindUsagesInInProgram() {
    test({
      "//PROGRAM, test/MyProg \n" +
      "var x = new test.MyClass() \n" +
      "[[x.myMethod()]] \n"
    ,
      "package test\n" +
      "class MyClass {\n" +
      "  function myMe^^thod() {} \n"+
      "}\n"
    });
  }

  function testFindUsagesInInTemplates() {
    test({
      "//TEMPLATE, test/MyTemplate \n" +
      "\<%var x = new test.MyClass() \n" +
      "[[x.myMethod()]] %>\n"
    ,
      "package test\n" +
      "class MyClass {\n" +
      "  function myMe^^thod() {} \n"+
      "}\n"
    });
  }

    @KnownBreak("", "", "")
  function testFindTemplateParamDeclUsage() {
    test(
      "//TEMPLATE, test/MyTemplate \n"+
      "\<%@ params(_city : String, _state : String, _to^^day : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \<%= [[_today]] %>"
    )
  }

    @KnownBreak("", "", "")
  function testFindUsagesTemplateParamDeclFromAlternateTemplateExpression() {
    test(
      "//TEMPLATE, some/pkg/Template \n"+
      "\<%@ params(_city : String, _state : String, ^^_today : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \<%= [[_today]] %>"
    )
  }

  @Disabled("dpetrusca", "templates not fully supported")
  function testFindUsagesExtendsClassFunctionDeclFromAlternateTemplateExpression() {
    test(
    {
      "//TEMPLATE, some/pkg/Template \n"+
      "\<%@ extends some.pkg.SuperClass %>\<%= [[a]](\"foo\") %>",
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "  static function ^^a(x : String) : String {\n" +
      "    return \"static function with arg \" + x\n" +
      "  }\n" +
      "}"
    })
  }

    @KnownBreak("", "", "")
  function testFindUsagesTemplateParamDeclFromEmbedTemplateExpression() {
    test(
      "//TEMPLATE, some/pkg/Template \n"+
      "\<%@ params(_city : String, _state : String, ^^_today : java.util.Date) %>\n" +
      "City : \${_city}\n" +
      "State : \${_state}\n" +
      "Generated on \${[[_today]]}\n"
    )
  }


  @Disabled("dpetrusca", "templates not fully supported")
  function testFindUsagesExtendsClassFunctionDeclFromEmbedTemplateExpression() {
    test(
    {
      "//TEMPLATE, some/pkg/Template \n"+
      "\<%@ extends some.pkg.SuperClass %>\${[[a]](\"foo\")}",
      "package some.pkg\n" +
      "class SuperClass {\n" +
      "    static function ^^a(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}"
    })
  }


  function testTypeFindUsagesInFunctionTypeParamDecl() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  function method<^^T>(s : [[T]]) : [[T]] {\n" +
      "    return s\n" +
      "  }\n" +
      "}"
    )
  }

  function testFindUsagesInFindPropFieldDecl() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _propField : String as Prop^^Field\n" +
      "  construct(s : String) {\n" +
      "    [[PropField]] = s\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindUsagesInDefaultParameterAssignment1() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _propField : String as PropField\n" +
      "  function withDefaultParams(field^^x: int, fieldy: String = \"default\"){\n" +
      "    \n" +
      "  }\n"+
      "  function test(){\n" +
      "    withDefaultParams(:fieldy = \"y\", :[[fieldx]] = 1)\n" +
      "  }\n"+
      "}"
    )
  }

  function testFindUsagesInDefaultParameterAssignment2() {
    test(
      "package test\n" +
      "class MyClass {\n" +
      "  var _propField : String as PropField\n" +
      "  function withDefaultParams(fieldx: int, field^^y: String = \"default\"){\n" +
      "    \n" +
      "  }\n"+
      "  function test(){\n" +
      "    withDefaultParams(:[[fieldy]] = \"y\", :fieldx = 1)\n" +
      "  }\n"+
      "}"
    )
  }

  //negative case of find field usage when both field and type have same name
  function testFindUsageOfFieldNotShowTypeWhenBothFieldAndTypeHaveSameName() {
    test ({
      "package pkg5\n" +
      "interface animal {\n" +
      "  function getGender() : String\n" +
      "}\n",
      "package pkg5\n\n" +
        "class dog implements animal {\n\n" +
        "  private var _gender : String\n" +
        "  construct(g : String){\n" +
        "    _gender = g\n" +
        "  }\n" +
        "  function getGender() : String {\n" +
        "    return _gender\n" +
        "  }\n" +
        "}",
        "package pkg5\n\n" +
        "uses junit.framework.TestCase\n\n" +
        "class MyTest extends TestCase{\n" +
        "  var d^^og : dog =  new dog( \"M\")\n" +
        "  function testMethods() {\n" +
        "    print (\"the dog's gender is \" + [[dog]].getGender() \n" +
        "  }\n" +
        "}"
    })
  }

  // negative case of find type usage when both field and type have same name
  // fail, but pass if doing the test manually
  function testFindUsageOfTypeNotShowFieldWhenBothFieldAndTypeHaveSameName() {
    test ({
      "package pkg5\n" +
      "interface animal {\n" +
      "  function getGender() : String\n" +
      "}\n",
      "package pkg5\n\n" +
      "class dog implements animal {\n\n" +
      "  private var _gender : String\n" +
      "  construct(g : String){\n" +
      "    _gender = g\n" +
      "  }\n" +
      "  function getGender() : String {\n" +
      "    return _gender\n" +
      "  }\n" +
      "}",
      "package pkg5\n\n" +
      "uses junit.framework.TestCase\n\n" +
      "class MyTest extends TestCase{\n" +
      "  var dog : [[d^^og]] =  new [[dog]]( \"M\")\n" +
      "  function testMethods() {\n" +
      "    print (\"the dog's gender is \" + dog.getGender()) \n" +
      "  }\n" +
      "}"
    })
  }

  function test(text: String) {
    test({text})
  }

  function test(texts: List<String>) {
    testImpl(texts.map(\elt -> ResourceFactory .create(elt)))
  }

  function testImpl(files: List<GosuTestingResource>) {
    var psiFiles = new ArrayList<PsiFile>()
    for (f in files) {
      psiFiles.add(configureByText(f.fileName, f.content))
    }
    var caretOffset = getMarkers(psiFiles.last()).getCaretOffset();
    getCurrentEditor().getCaretModel().moveToOffset(caretOffset);
    var flags = TargetElementUtilBase.getInstance().getReferenceSearchFlags()
    var elementAt = TargetElementUtilBase.getInstance().findTargetElement(getCurrentEditor(), flags, caretOffset)
    assertNotNull("Target element not found.", elementAt)
    var query = ReferencesSearch.search(elementAt).findAll();
    var result = query.toTypedArray().map(\elt -> elt.Element).toList()
    if (result.Empty) {
      result = AdvancedSearcher.search(elementAt)
    }
    gw.test.AssertUtil.assertCollectionEquals(getAllMarkers(psiFiles.toArray(new PsiFile[psiFiles.size()])).getRanges(), psis2Ranges(result))
  }
}