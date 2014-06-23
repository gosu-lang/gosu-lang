/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.parameterinfo

uses com.intellij.codeInsight.hint.ShowParameterInfoContext
uses com.intellij.codeInsight.hint.ShowParameterInfoHandler
uses com.intellij.lang.parameterInfo.ParameterInfoHandler
uses com.intellij.openapi.actionSystem.IdeActions
uses com.intellij.psi.PsiElement
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuEnhancementFile
uses gw.plugin.ij.framework.generator.GosuInterfaceFile
uses gw.plugin.ij.framework.generator.GosuProgramFile
uses gw.plugin.ij.framework.generator.GosuTemplateFile
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.plugin.ij.lang.GosuLanguage
uses gw.plugin.ij.lang.psi.api.IGosuResolveResult
uses gw.testharness.Disabled

class ParameterInfoTest extends GosuTestCase {

  function testNoArgMethodMethod() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo() {} \n" +
      " function bar() { foo(^^) } \n" +
      "} \n"
    , {
      "<no parameters>"
    }, -1
    )
  }

  function testSDKMethodInvocationWithNoArgs() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " var s = \"\".indexOf(^^) \n" +
      "} \n"
    , {
      "ch: int",
      "ch: int, fromIndex: int",
      "str: String",
      "str: String, fromIndex: int",
      "source: char[], sourceOffset: int, sourceCount: int, target: char[], targetOffset: int, targetCount: int, fromIndex: int"
    }, -1
    )
  }

  function testSDKMethodInvocationWithArgs1() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " var s = \"\".indexOf('q'^^, 1) \n" +
      "} \n"
    , {
      "ch: int",
      "ch: int^^, fromIndex: int",
      "str: String",
      "str: String, fromIndex: int",
      "source: char[], sourceOffset: int, sourceCount: int, target: char[], targetOffset: int, targetCount: int, fromIndex: int"
    }, 0
    )
  }

  function testSDKMethodInvocationWithArgs2() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " var s = \"\".indexOf('q', 1^^) \n" +
      "} \n"
    , {
          "ch: int",
          "ch: int, fromIndex: int^^",
          "str: String",
          "str: String, fromIndex: int",
          "source: char[], sourceOffset: int, sourceCount: int, target: char[], targetOffset: int, targetCount: int, fromIndex: int"
      }, 1
    )
  }

  function testSDKMethodInvocationHighlightedNoArgSigniture() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      "  function bar() { \n" +
      "     var o = new GosuClass() \n" +
      "     o.wait(^^) \n" +
      "  } \n" +
      "} \n"
    , {
      "<no parameters>^^",
      "timeout: long",
      "timeout: long, nanos: int"
    }, 0
    )
  }

  function testSDKMethodInvocationHighlightedOneArgSigniture() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      "  function bar() { \n" +
      "     var o = new GosuClass() \n" +
      "     o.wait(1^^) \n" +
      "  } \n" +
      "} \n"
    , {
      "<no parameters>",
      "timeout: long^^",
      "timeout: long, nanos: int"
    }, 0
    )
  }

  function testSDKMethodInvocationHighlightedFirstArgSigniture() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      "  function bar() { \n" +
      "     var o = new GosuClass() \n" +
      "     o.wait(1^^) \n" +
      "  } \n" +
      "} \n"
    , {
      "<no parameters>",
      "timeout: long^^",
      "timeout: long, nanos: int"
    }, 0
    )
  }

  function testSDKMethodInvocationHighlightedSecondArgSigniture() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      "  function bar() { \n" +
      "     var o = new GosuClass() \n" +
      "     o.wait(1, 1^^) \n" +
      "  } \n" +
      "} \n"
    , {
      "<no parameters>",
      "timeout: long",
      "timeout: long, nanos: int^^"
    }, 1
    )
  }

  function testSDKMethodInvocationHighlightedSigniture() {
    test(
      "package some.pkg \n" +
      "uses java.lang.StringBuffer \n" +
      "class GosuClass { \n" +
      "  function bar() { \n" +
      "    var sb = new StringBuffer() \n" +
      "    sb.append({'a'},1^^ ) \n" +
      "  } \n" +
      "} \n"
    , {
    "s: CharSequence",
    "s: CharSequence, start: int, end: int",
    "c: char",
    "d: double",
    "f: float",
    "lng: long",
    "i: int",
    "b: boolean",
    "str: char[]",
    "obj: Object",
    "str: String",
    "sb: StringBuffer",
    "str: char[], offset: int, len: int"
  }, 1
    )
  }

  function testLocalClassMethodWithArgs() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^) } \n" +
      "}"
    , {
      "x: int, y: String"
    }, -1
    )
  }

  function testLocalClassMethodWithArgsCharSequence() {
    test(
      "package some.pkg \n" +
      "uses java.lang.CharSequence \n" +
      "class GosuClass { \n" +
      "  function bar(d: double) { }\n" +
      "  function bar(a: CharSequence, i: int, j: int) { }\n" +
      "  function bar(s: char[], i: int, j: int) { }\n" +
      "  function foo() {\n" +
      "    var cs : CharSequence = \"123\"\n" +
      "    bar(cs, ^^)\n" +
      "  }" +
      "}"
    , {
      "d: double",
      "a: CharSequence, i: int, j: int^^",
      "s: char[], i: int, j: int"
    }, 1
    )
  }

  function testLocalClassMethodWithArgsCharArray() {
    test(
      "package some.pkg \n" +
      "uses java.lang.CharSequence \n" +
      "class GosuClass { \n" +
      "  function bar(d: double) { }\n" +
      "  function bar(a: CharSequence, i: int, j: int) { }\n" +
      "  function bar(s: char[], i: int, j: int) { }\n" +
      "  function foo() {\n" +
      "    var cs : char[] = {'1'}\n" +
      "    bar(cs, ^^)\n" +
      "  }" +
      "}"
    , {
      "d: double",
      "a: CharSequence, i: int, j: int",
      "s: char[], i: int, j: int^^"
    }, 1
    )
  }

  function testLocalEnhancementMethodWithArgs1() {
    var f = new GosuEnhancementFile(
      "package some.pkg \n" +
      "enhancement SomeEnh: String { \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^) } \n" +
      "}")
    test({f} , {
      "x: int, y: String"
    }, -1
    )
  }

  function testLocalEnhancementMethodWithArgs2() {
    var f = new GosuEnhancementFile(
      "package some.pkg \n" +
      "enhancement SomeEnh: java.lang.Integer { \n" +
      " function test() { var x = this.parseInt(\"\", ^^1) } \n" +
      "} \n"
    )
    test({f}, {
      "s: String, radix: int^^",
      "s: String"
    }, 1
    )
  }

  function testLocalProgram() {
    var f = new GosuProgramFile("some/pkg/SomeProgram.gsp",
      " function foo(x: int, y: String) {} \n" +
      " function foo(x: int) {} \n" +
      " function test() { foo(1, nu^^ll) } \n"
    )
    test({f}, {
      "x: int, y: String^^",
      "x: int"
    }, 1
    )
  }

  function testLocalTemplate() {
    var f = new GosuTemplateFile("some/pkg/SomeTemplate.gst",
      "\<% function foo(x: int, y: String) {} \n" +
      " function test() { foo(1, nu^^ll) } %>\n"
    )
    test({f}, {
      "x: int, y: String"
    }, 1
    )
  }

  function testWithOnlyTheOpenBrace() {
    test(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^ } \n" +
      "}" , {
      "x: int, y: String"
    }, -1
    )
  }

  function testOverloadedMethodsInSameGosuClass() {
    test("package some.pkg \n" +
      "class GosuClass \n" +
      " function foo(x: int) {} \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^) } \n" +
      "}"
    , {
      "x: int",
      "x: int, y: String"
    }, -1)
  }

  function testOverloadedMethodsInDifferentGosuClasses() {
    var f1 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass1 { \n" +
      " function foo(x: int, y: String) {} \n" +
      "} \n"
    )
    var f2 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass2 extends GosuClass1 { \n" +
      " function foo(x: int) {} \n" +
      " function test() { foo(^^) } \n" +
      "} \n"
    )
    test({f1, f2} , {
      "x: int",
      "x: int, y: String"
    }, -1)
  }

  function testOverloadedMethodsInInterface() {
    var f1 = new GosuInterfaceFile(
      "package some.pkg \n" +
      "interface SomeInterfac1 \n" +
      " function foo(x: int) \n" +
      "} \n"
    )
    var f2 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass extends SomeInterface \n" +
      " function foo(x: int) {} \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^) } \n" +
      "} \n"
    )
    test({f1, f2} , {
      "x: int",
      "x: int, y: String"
    }, -1)
  }

  function testOverloadedMethodsInEnhancemntOnClass() {
    var f1 = new GosuEnhancementFile(
      "package some.pkg \n" +
      "enhancement SomeEnh: GosuClass { \n" +
      " function foo(x: int) {} \n" +
      "} \n"
    )
    var f2 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: String) {} \n" +
      " function test() { foo(^^) } \n" +
      "} \n"
    )
    test({f1, f2} , {
      "x: int, y: String",
      "x: int"
    }, -1)
  }

  function testOverloadedMethodsInEnhancemntOnSuperclass() {
    var f1 = new GosuEnhancementFile(
      "package some.pkg \n" +
      "enhancement SomeEnh: GosuClass1 { \n" +
      " function foo(x: int) {} \n" +
      "} \n"
    )
    var f2 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass1 { \n" +
      " function foo(x: int, y: String) {} \n" +
      "} \n"
    )
    var f3 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass2 extends GosuClass1 { \n" +
      " function foo(x: int, y: String, z: double) {} \n" +
      " function test() { foo(^^) } \n" +
      "} \n"
    )
    test({f1, f2, f3} , {
      "x: int, y: String, z: double",
      "x: int, y: String",
      "x: int"
    }, -1)
  }

  function testOverloadedMethodsInInnerClassAndEnclosingClass() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo(x: int) {}\n" +
      "    class Inner {\n" +
      "        function foo(x: int, y: int) {}\n" +
      "        function test(x: int) {\n" +
      "            foo(^^)\n" +
      "        }\n" +
      "    }\n" +
      "}"
    )
    test({f} , {
      "x: int",
      "x: int, y: int"
    }, -1)
  }

  function testOverloadedMethodsInAnonymousClassAndEnclosingClass() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "uses java.lang.Runnable\n" +
      "class GosuClass {\n" +
      "    function foo(x: int) {}\n" +
      "    function test(x: int) {\n" +
      "        var r = new Runnable() {\n" +
      "            function foo(x: int, y: int) {}\n" +
      "            function run() { foo(^^) }\n" +
      "        }\n" +
      "    }\n" +
      "}"
    )
    test({f} , {
      "x: int",
      "x: int, y: int"
    }, -1)
  }

  function testOnSuperConstructorCall() {
    var f1 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass1 \n" +
      " construct(x: int, y: String) {} \n" +
      "} \n"
    )
    var f2 = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass2 extends GosuClass1 { \n" +
      " construct() { \n" +
      "   super(^^) \n" +
      " } \n" +
      "} \n"
    )
    test({f1, f2} , {
      "x: int, y: String"
    }, -1)
  }

  function testOnThisConstructorCall() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " construct(x: int, y: List<String>) {} \n" +
      " construct() { \n" +
      "   this(1, nu^^ll) \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "<no parameters>",
      "x: int, y: List<String>"
    }, 1)
  }

  function testOnBlockInvocation() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " var b: block(x: int, y: block(y: int): String) \n" +
      " function test() { \n" +
      "   b(^^) \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "x: int, y: block(y: int): String"
    }, -1)
  }

  function testOnAnnotationConstructor() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " @java.lang.SuppressWarnings(^^) \n" +
      " function test() { \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "String[] value()"
    }, -1)
  }

  function testOnClassConstructor() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function test() { \n" +
      "   var myCache = new javax.swing.JButton(^^) \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "text: String", "text: String, icon: Icon", "icon: Icon",  "a: Action", "<no parameters>"
    }, -1)
  }

  function testOnGenericClassConstructor() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function test() { \n" +
      "   var myMap = new java.util.HashMap<String, String>(^^) \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "initialCapacity: int",
      "initialCapacity: int, loadFactor: float",
      "m: Map<? extends String,? extends String>",
      "<no parameters>"
    }, -1)
  }

  function testParamWithDefaultValue() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: int = 234) { \n" +
      "   foo(^^) \n" +
      " } \n" +
      "} \n"
    )
    test({f} , {
      "x: int, y: int = 234"
    }, -1)
  }

  function testOnFirstParameter() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo(x: int, y: int, z: int) {\n" +
      "      foo(1^^, 2, 3) \n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      "x: int, y: int, z: int"
    }, 0)
  }

  function testOnSecondParameter() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo(x: int, y: int, z: int) {\n" +
      "      foo(1, 2 +^^ 2, 3) \n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      "x: int, y: int, z: int"
    }, 1)
  }

  function testOnThirdParameter() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo(x: int, y: int, z: int) {\n" +
      "      foo(1, 2 + 2, 3^^) \n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      "x: int, y: int, z: int"
    }, 2)
  }

  function testHighlightedParameter1() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: int = 234, z: int = 432) { \n" +
      "   foo(^^1, :z = 1, :y = 1) \n" +
      " } \n" +
      "} \n"
    )
    test({f}, {"x: int, y: int = 234, z: int = 432"}, 0)
  }

  function testHighlightedParameter2() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: int = 234, z: int = 432) { \n" +
      "   foo(1^^, :z = 1, :y = 1) \n" +
      " } \n" +
      "} \n"
    )
    test({f}, {"x: int, y: int = 234, z: int = 432"}, 0)
  }

  function testHighlightedParameter3() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: int = 234, z: int = 432) { \n" +
      "   foo(1, :z = 1^^, :y = 1) \n" +
      " } \n" +
      "} \n"
    )
    test({f}, {"x: int, y: int = 234, z: int = 432"}, 2)
  }

  function testHighlightedParameter4() {
    var f = new GosuClassFile(
      "package some.pkg \n" +
      "class GosuClass { \n" +
      " function foo(x: int, y: int = 234, z: int = 432) { \n" +
      "   foo(1, :z = 1, ^^:y = 1) \n" +
      " } \n" +
      "} \n"
    )
    test({f}, {"x: int, y: int = 234, z: int = 432"}, 1)
  }

  function testOnMethodWithBlockParam() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass<T> {\n" +
      "    function foo(x: List<java.lang.Integer>) {\n" +
      "      map(\\i -> ^^ i.toString()) \n" +
      "    } \n " +
      "    function map<Q>( mapper(elt : T):Q ) : List<Q> {return null} \n" +
      "}"
    )
    test({f} , {
      "mapper: block(T):Q"
    }, 0)
  }

  function testOnArrayListIndexOf() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo(x: java.util.ArrayList<String>) {\n" +
      "      x.indexOf(\"\"^^) \n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      "o: Object"
    }, 0)
  }

  //This test tried to catch the issue that fixed in changelist#430254. But it failed to catch the issue before it was fixed.
  function testOnObjectEquals() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo() {\n" +
      "      var o = new GosuClass()\n" +
      "      o.equals(^^)\n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      "obj: Object"
    }, 0)
  }

  function testOnGenericJavaMethodFromGenericGosuSameTypeVar() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class Base<A> {\n" +
      "  public void baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class Derived<A> extends Base<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    baseFoo(^^)\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub }, {
      "a: A"
    }, 0)
  }

  function testOnGenericGosuMethodFromGenericGosuSameTypeVar() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "public class BaseA<A> {\n" +
      "  public void baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "public class DerivedF<A> extends BaseA<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    baseFoo(^^)\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub }, {
      "a: A"
    }, 0)
  }

  @Disabled("dpetrusca", "unimportant")
  function testOnSpecialMethod() {
    var f = new GosuClassFile(
      "package some.pkg\n" +
      "class GosuClass {\n" +
      "    function foo() {\n" +
      "      print(^^)\n" +
      "    } \n " +
      "}"
    )
    test({f} , {
      ""
    }, 0)
  }

  @Disabled("dpetrusca", "maybe later")
  function testOnTemplateTypeRenderToStringMethodWithoutParam() {
    var f0 = new GosuTemplateFile( "some/pkg/SomeTemplate.gst",
        "\<%var x = \"hello\" %>\n" +
        "Template text in an expression: \<%= x %>"

    )
    var f1 = new GosuProgramFile( "some/pkg/SomeProgram.gsp",
        "  var d  = new java.util.Date()\n" +
        "  print(OneTemplate.renderToString(^^))"
    )
    test({f0, f1} , {
      "<no parameters>"
    }, -1)
  }

  @Disabled("dpetrusca", "maybe later")
  function testOnTemplateTypeRenderToStringMethodWithParam() {
    var f0 = new GosuTemplateFile( "some/pkg/SomeTemplate.gst",
        "\<%@ params(x : String, y : java.util.Date) %>\n" +
        "Template text in an expression: \<%= x %>\n" +
        "Template date in an expression: \<%= y %>"

    )
    var f1 = new GosuProgramFile( "some/pkg/SomeProgram.gsp",
        "  var d  = new java.util.Date()\n" +
        "  print(OneTemplate.renderToString(^^))"
    )
    test({f0, f1} , {
      "x: String, d: Date"
    }, 0)
  }

  function test(text: String, result: String[], paramIndex: int) {
    test({new GosuClassFile(text)}, result, paramIndex)
  }

  function test(resources: GosuTestingResource[], result: String[], paramIndex: int) {
    var signatureIndex = -1
    for (i in 0..result.length - 1) {
      var j = result[i].indexOf("^^")
      if (j >= 0) {
        result[i] = result[i].replace("^^", "")
        signatureIndex = i
      }
    }

    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var caret = markers.getCaretOffset()
    var psiFile = getCurrentFile()
    var context = new ShowParameterInfoContext(getCurrentEditor(), getProject(), psiFile, caret, 0)
    var element: Object
    var handler: ParameterInfoHandler
    var handlers = ShowParameterInfoHandler.getHandlers(getProject(), {GosuLanguage.instance()} )
    for (h in handlers) {
      element = h.findElementForParameterInfo(context)
      if (element != null) {
        handler = h
        break
      }
    }
    assertNotNull("No items to show found.", context.ItemsToShow)

    var updateContext = new UpdateContext(psiFile, caret);
    var forUpdate = handler.findElementForUpdatingParameterInfo(updateContext)
    updateContext.ParameterOwner = forUpdate as PsiElement
    handler.updateParameterInfo(forUpdate, updateContext)
    if (paramIndex >= 0) {
      assertEquals("Wrong highlighted parameter.", paramIndex, updateContext.CurrentParameter)
    }
    var uiContext = new UIContext(updateContext.CurrentParameter)
    context.ItemsToShow.each(\i -> handler.updateUI(i, uiContext))
    gw.test.AssertUtil.assertArrayEquals(result, uiContext.texts.toArray())

    if (signatureIndex >= 0) {
      var sIndex = uiContext.texts.indexOf(result[signatureIndex])
      var a = (context.ItemsToShow[sIndex] as IGosuResolveResult).Element.Text
      var highlightedParameter = (updateContext.highlightedParameter as IGosuResolveResult)
      assertNotNull("No signature being highlighted.", highlightedParameter)
      var b = highlightedParameter.Element.Text
      assertEquals("Wrong selected signature.", a, b)
    }

    runAction(IdeActions.ACTION_EDITOR_ESCAPE, CurrentEditor)
  }
}