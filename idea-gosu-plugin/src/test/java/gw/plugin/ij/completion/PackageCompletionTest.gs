/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion

uses gw.testharness.Disabled

class PackageCompletionTest extends AbstractCodeCompletionTest {

  override function shouldReturnType(): boolean {
    return false;
  }

  //package statement
  function testPackagePathIsShownInPackageStatement() {
    test(
      "package pkg.^^test.somepackage\n" +
      "class GosuClass { \n" +
      "}"
    , {
      "test"
    }
    )
  }

  //uses statement
  function testCanInvokeInUsesStatement() {
    testCanInvoke(
      "package pkg\n" +
      "uses java.^^\n" +
      "class GosuClass {\n" +
      "}\n")
  }

  function testCanntInvokeInUsesStatement() {
    testCanntInvoke(
      "package pkg\n" +
      "uses ja^^\n" +
      "class GosuClass {\n" +
      "}\n")
  }

  function testTypesInLibAreShownInUsesStatement() {
    test(
      "package pkg\n" +
      "uses java.applet.^^\n" +
      "class GosuClass {\n" +
      "}"
    , {
      "*", "Applet"
    }
    )
  }

  function testPathsAreShownInUsesStatement() {
    test(
      "package pkg\n" +
      "uses java.^^\n" +
      "class GosuClass {\n" +
      "}"
    , {
      "*", "applet"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInUsesStatement(){
    test({
      "package apkg\n" +
      "class AGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "uses apkg.^^\n" +
      "class GosuClass {\n" +
      "}"}
    , {
      "AGosuClass", "AJavaClass", "*"
    }
    )
  }

  //Replace mode in uses

  function testReplaceModeCompletionOnTypeInUsesStatement() {
    testReplaceModeCompletion({
      "package pkg\n" +
      "uses java.applet.^^\n" +
      "class GosuClass {\n" +
      "}"}
    , "Applet"
    , "package pkg\n" +
      "uses java.applet.Applet^^\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  function testReplaceModeCompletionOnPathInUsesStatement() {
    testReplaceModeCompletion({
      "package pkg\n" +
      "uses java.^^\n" +
      "class GosuClass {\n" +
      "}"}
    , "applet"
    , "package pkg\n" +
      "uses java.applet.^^\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  function testReplaceModeCompletionOnPathWithExistedPathInUsesStatement() {
    testReplaceModeCompletion({
      "package pkg\n" +
      "uses java.^^applet.Applet\n" +
      "class GosuClass {\n" +
      "}"}
    , "awt"
    , "package pkg\n" +
      "uses java.awt.^^Applet\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  function testReplaceModeCompletionOnTypeWithExistedTypeInUsesStatement() {
    testReplaceModeCompletion({
      "package pkg\n" +
      "uses java.applet.^^AppletContext\n" +
      "class GosuClass {\n" +
      "}"}
    , "Applet"
    , "package pkg\n" +
      "uses java.applet.Applet^^\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  //insert mode in uses

  function testInsertModeCompletionOnTypeInUsesStatement() {
    testInsertModeCompletion({
      "package pkg\n" +
      "uses java.^^applet.Applet\n" +
      "class GosuClass {\n" +
      "}"}
    , "applet"
    , "package pkg\n" +
      "uses java.applet.^^applet.Applet\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  function testInsertModeCompletionOnPathInUsesStatement() {
    testInsertModeCompletion({
      "package pkg\n" +
      "uses java.applet.^^Applet\n" +
      "class GosuClass {\n" +
      "}"}
    , "Applet"
    , "package pkg\n" +
      "uses java.applet.Applet^^Applet\n" +
      "class GosuClass {\n" +
      "}"
    )
  }

  //uses statement with inner class
  function testCanInvokeInUsesInnerClass() {
    test({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "    class InnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "uses test.OuterClass.^^\n" +
      "class GosuClass {\n" +
      "}\n"}
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  function testCanInvokeInUsesInnerClassInJava() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterJavaClass {\n" +
      "    public static class StaticInnerClass { }\n" +
      "    public class InnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "uses test.OuterJavaClass.^^\n" +
      "class GosuClass {\n" +
      "}\n"}
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  function testCanInvokeInUsesInnerClassInJavaInTemplate() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterClass {\n" +
      "    public static class StaticInnerClass { }\n" +
      "    public class InnerClass { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses test.OuterClass.^^\n" +
      "%>"
    }
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  function testCanInvokeInUsesInnerClassInJavaInProgram() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterClass {\n" +
      "    public static class StaticInnerClass { }\n" +
      "    public class InnerClass { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "uses test.OuterClass.^^\n"
    }
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  function testCanInvokeInUsesInnerClassDeclInJavaInProgram() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterJavaClass {\n" +
      "    public static class StaticInnerClass { }\n" +
      "    public class InnerClass { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "uses test.OuterJavaClass.^^\n"
    }
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  function testCanInvokeInUsesInnerClassDeclInJavaInTemplate() {
    test({
      "//JAVA \n" +
      "package test;\n" +
      "public class OuterJavaClass {\n" +
      "    public static class StaticInnerClass { }\n" +
      "    public class InnerClass { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "uses test.OuterJavaClass.^^\n" +
      "%>"
    }
    , {
      "StaticInnerClass", "InnerClass", "*"
    }
    )
  }

  //return types

  function testCanInvokeInReturnType() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : java.lang.^^ {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanntInvokeInReturnType() {
    testNoItems(
            {"package pkg\n" +
             "class GosuClass {\n" +
             "  function bar() : ja^^ {\n" +
             "  }\n" +
             "}\n"},
            {"java"}
    )
  }

  function testInnerTypesAreShownInReturnType() {
    test({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "    class InnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : test.OuterClass.^^ {\n" +
      "  }\n" +
      "}\n"}
    , {
      "StaticInnerClass", "InnerClass"
    }
    )
  }

  function testNoWildcardInnerTypesAreShownInReturnType() {
    testNoItems({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "    class InnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : test.OuterClass.^^ {\n" +
      "  }\n" +
      "}\n"}
    , {
      "*"
    }
    )
  }

  function testNoWildcardTypesInLibAreShownInReturnType() {
    testNoItems({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : java.applet.^^ {\n" +
      "  }\n" +
      "}"}
    , {
      "*"
    }
    )
  }

  function testNoWildcardPathsAreShownInReturnType() {
    testNoItems({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : java.^^ {\n" +
      "  }\n" +
      "}"}
    , {
      "*"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInReturnType(){
    test({
      "package apkg\n" +
      "class AGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : apkg.^^ {\n" +
      "  }\n" +
      "}"}
    , {
      "AGosuClass", "AJavaClass"
    }
    )
  }

  function testNoWildcardTypesDefinedInOtherPkgAreShownInReturnType(){
    testNoItems({
      "package apkg\n" +
      "class AGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() : apkg.^^ {\n" +
      "  }\n" +
      "}"}
    , {
      "*"
    }
    )
  }

  //local vars

  function testCanInvokeInParameterType() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar(x : java.lang.^^) {\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeInParameterTypeInnerClass() {
    testCanInvoke({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar(x : test.OuterClass.^^) {\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeInBlockParameterType() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : java.lang.^^\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeInBlockParameterTypeInnerClass() {
    testCanInvoke({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = \\ n : test.OuterClass.^^\n" +
      "  }\n" +
      "}\n"})
  }

  function testAnnotationTypesAreShownInAnnotationUsage() {
    test({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  @java.lang.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"}
    ,{
      "Deprecated"
    }
    )
  }

  function testNonAnnotationTypesArentShownInAnnotationUsage() {
    testNoItems({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  @java.lang.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"}
    ,{
      "String"
    }
    )
  }

  function testCanInvokeInAnnotationInnerClass() {
    testCanInvoke({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"})
  }

  function testCanInvokeInAnnotationDeclInnerClassInProgram() {
    testCanInvoke({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n"
    })
  }

  function testCanInvokeInAnnotationDeclInnerClassInTemplate() {
    testCanInvoke({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "%>"
    })
  }

  function testTypsAreShownInAnnotationDefinedInOtherPackage() {
    test({
      "package apkg\n" +
      "class AGosuClass implements IAnnotation {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public @interface AJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  @apkg.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"}
    ,{
      "AGosuClass", "AJavaClass"
    }
    )
  }

  function testTypeThatContainsStaticInnerAnnotationIsShownInAnnotation() {
    test({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "}\n"}
    ,{
      "StaticInnerClass"
    }
    )
  }

  function testTypeThatContainsStaticInnerAnnotationIsShownInAnnotationInProgram() {
    test({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//PROGRAM, pkg/MyProgram\n" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n"
    }
    ,{
      "StaticInnerClass"
    }
    )
  }

  function testTypeThatContainsStaticInnerAnnotationIsShownInAnnotationInTemplate() {
    test({
      "package apkg\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass implements IAnnotation { }\n" +
      "}\n"
    ,
      "//TEMPLATE, pkg/MyTemplate\n" +
      "\<%" +
      "  @apkg.OuterClass.^^\n" +
      "  function bar() {\n" +
      "  }\n" +
      "%>"
    }
    ,{
      "StaticInnerClass"
    }
    )
  }

  function testCanInvokeInTypeLiteralParam() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x : List<java.lang.^^>\n" +
      "  }\n" +
      "}\n")
  }



  function testCanInvokeInNewExpression() {
    testCanInvoke(
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new java.lang.^^\n" +
      "  }\n" +
      "}\n")
  }

  function testCanInvokeInNewExpressionInnerClass() {
    testCanInvoke({
      "package test\n" +
      "class OuterClass {\n" +
      "    static class StaticInnerClass { }\n" +
      "}\n"
    ,
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    var x = new test.OuterClass.^^\n" +
      "  }\n" +
      "}\n"})
  }

  function testItemThatIsOneOfThrowablesSubclassesCanbeTheArgumentTypeInCatchClause() {
    test({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "      var x = \"foo\"\n" +
      "    } catch(e : java.lang.^^\n" +
      "  }\n" +
      "}\n"}
    , {
      "NullPointerException"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testNoItemThatIsNotOneOfThrowablesSubclassesCanbeTheArgumentTypeInCatchClause() {
    testNoItems({
      "package pkg\n" +
      "class GosuClass {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "      var x = \"foo\"\n" +
      "    } catch(e : java.lang.^^\n" +
      "  }\n" +
      "}\n"}
    , {
      "String"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInThrowStatement(){
    test({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "      throw new apkg.^^\n" +
      "    }\n" +
      "  }\n" +
      "}"
      }
    , {
      "AGosuClass", "AJavaClass"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testNoTypesDefinedInOtherPkgAreShownInThrowStatement(){
    testNoItems({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "package apkg\n" +
      "class BGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class BJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "      throw new apkg.^^\n" +
      "    }\n" +
      "  }\n" +
      "}"
      }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testTypesDefinedInOtherPkgAreShownInCatchClause(){
    test({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : apkg.^^)\n" +
      "  }\n" +
      "}"
      }
    , {
      "AGosuClass", "AJavaClass"
    }
    )
  }

  @Disabled("dpetrusca", "fix in phase 2")
  function testNoTypesDefinedInOtherPkgAreShownInCatchClause(){
    testNoItems({
      "package apkg\n" +
      "class AGosuClass extends Exception {\n" +
      "  construct(){\n" +
      "  }\n" +
      "  construct(msg : String){\n" +
      "    super(msg)\n" +
      "  }\n" +
      "}"
    ,
      "package apkg\n" +
      "class BGosuClass {\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class AJavaClass extends Exception {\n" +
      "  public AJavaClass() {\n" +
      "  }\n" +
      "  public AJavaClass(String msg) {\n" +
      "    super(msg);\n" +
      "  }\n" +
      "}"
    ,
      "//JAVA \n" +
      "package apkg;\n" +
      "public class BJavaClass {\n" +
      "}"
    ,
      "package pkg\n" +
      "class GosuClass  {\n" +
      "  function bar() {\n" +
      "    try {\n" +
      "    } catch (e : apkg.^^)\n" +
      "  }\n" +
      "}"
      }
    , {
      "BGosuClass", "BJavaClass"
    }
    )
  }

  function testClassPathIsShownAfterExtendsInTemplateScriptletTag() {
    test({
      "package some.pkg1\n" +
      "class SuperClass {\n" +
      "    static function staticMethod(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}",
      "package some.pkg2\n" +
      "class SuperClass {\n" +
      "    static function staticMethod(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}",
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ extends some.^^ %>" }
    ,{
       "pkg1", "pkg2"
    }
    )
  }

  function testClassIsShownAfterExtendsInTemplateScriptletTag() {
    test({
      "package some.pkg1\n" +
      "class SuperClass1 {\n" +
      "    static function staticMethod(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}",
      "package some.pkg1\n" +
      "class SuperClass2 {\n" +
      "    static function staticMethod(x : String) : String {\n" +
      "        return \"static function with arg \" + x\n" +
      "    }\n" +
      "}",
      "//TEMPLATE, package some/pkg\n" +
      "\<%@ extends some.pkg1.^^ %>" }
    ,{
       "SuperClass1", "SuperClass2"
    }
    )
  }

  function testClassPathIsShownAsEnhancedTypeInEnhancement() {
    test ( {
      "package some.pkg1\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg2\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.^^ {\n" +
      "}"}
      ,{
      "pkg1", "pkg2"
    }
    )
  }

  function testNoWildcardPathIsShownAsEnhancedTypeInEnhancement() {
    testNoItems ( {
      "package some.pkg1\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg2\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.^^ {\n" +
      "}"}
      ,{
      "*"
    }
    )
  }

  function testNoWildcardTypeIsShownAsEnhancedTypeInEnhancement() {
    testNoItems ( {
      "package some.pkg1\n" +
      "class GosuClass1 {\n" +
      "}" ,
      "package some.pkg1\n" +
      "class GosuClass2 {\n" +
      "}" ,
      "package some.pkg\n" +
      "enhancement GosuClassEnhencement : some.pkg1.^^ {\n" +
      "}"}
      ,{
      "*"
    }
    )
  }

  function testClassPathIsShownAfterUsesInEnhancement() {
    test ( {
      "package some.pkg1\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg2\n" +
      "class GosuClass {\n" +
      "}" ,
      "package some.pkg\n" +
      "uses some.^^" +
      "enhancement GosuClassEnhencement : GosuClass {\n" +
      "}"}
      ,{
      "pkg1", "pkg2", "*"
    }
    )
  }

  function testTypesAreShownAfterUsesInEnhancement() {
    test ( {
      "package some.pkg1\n" +
      "class GosuClass1 {\n" +
      "}" ,
      "package some.pkg1\n" +
      "class GosuClass2 {\n" +
      "}" ,
      "package some.pkg\n" +
      "uses some.pkg1.^^" +
      "enhancement GosuClassEnhencement : GosuClass1 {\n" +
      "}"}
      ,{
      "GosuClass1", "GosuClass2", "*"
    }
    )
  }

}