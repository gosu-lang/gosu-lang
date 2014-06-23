/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix

uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuProgramFile
uses gw.plugin.ij.framework.generator.GosuTestingResource

class QuickfixImplicitCoercionTest extends AbstractQuickFixTest {
  function testCastIntToString() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s : String = 1^^[[[!! as String]]]\n" +
        "  }\n" +
        "}")
    test({f}, "Cast to 'java.lang.String'")
  }

  function testCastIntExpressionToString() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s : String = [[1 / 5^^]]\n" +
        "  }\n" + "}")
    test({f}, "Cast to 'java.lang.String'", "(1 / 5) as String")
  }

  function testCastFloatExpressionToInt() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s: int = (6 / 5) as [[float^^]]\n" +
        "  }\n" +
        "}")
    test({f}, "Cast to 'int'", "int")
  }

  function testCastIntExpressionToStringInGosuProgram() {
    var f = new GosuProgramFile ("some/pkg/Programabc.gsp",
        "function bar() {\n" +
        "  var s : String = 1^^[[[!! as String]]]\n" +
        "}\n")
    test({f}, "Cast to 'java.lang.String'")
  }

  function testCastExpressionInFunctionCall() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.Integer\n" +
        "uses java.lang.Double\n\n" +
        "class GosuClass {\n" +
        "   function withDefaultParams(x: Integer, y: String = \"default\", z: String = \"default\") {\n" +
        "   }\n\n" +
        "   function testwithDefaultParams() {\n" +
        "      withDefaultParams(:x = 1 [[as int!!]]^^, :y=1 as String, :z=\"edf\")\n" +
        "   }" +
        "}")
    testNoIntention({f})
  }

  // delear as int assign float as int
  // Primitive -> Primitive

  function testCastexpressionPrim2Prim() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s: int = (6 / 5) [[as int^^]]!!\n" +
        "  }\n" +
        "}")
    testNoIntention({f})
  }

  function testCastexpressionPrim2Boxed() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.Integer\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s = (6 / 5) [[as Integer^^]]!!\n" +
        "  }\n" +
        "}")
    testNoIntention({f})
  }

  function testCastexpressionBoxed2Prim() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.Integer\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s: double = 12bi [[as float^^]]!!\n" +
        "  }\n" +
        "}")
    testNoIntention({f})
  }

  function testCastexpressionpPrim2Prim() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var s = 0 [[as boolean^^]]!!\n" +
        "  }\n" +
        "}")
    testNoIntention({f})
  }

  function testCastExpressionBoxed2Boxed() {
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.Integer\n" +
        "uses java.lang.Double\n\n" +
        "class GosuClass {\n" +
        "  function bar() {\n" +
        "    var u:Double = 60000000000000000000000BI [[as Integer^^]]!!\n" +
        "  }\n" +
        "}")
    testNoIntention({f})
  }

  function testCastExpressionReferenceType_Super_VS_Sub_hasInstace() {
    // Boxed -> Boxed (the values must be compatible, you must explicitly cast if there is otherwise loss of precision or scale)
    // superclass = subclass // always valid
    // subclass = (subclass) superclass //valid at compile time, checked at run-time
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.StringBuffer\n" +
        "class GosuClass {\n" +
        " function examInstanceCreadted() {\n" +
        "    var sb: StringBuffer = new StringBuffer()\n" +
        "    var obj: Object = new Object()\n" +
        "    var obj1: Object = new Object()\n" +
        "    var obj2 = new Object()\n\n" +
        "    obj = sb\n" +
        "    sb = obj1 as StringBuffer\n" +
        "    sb = obj2 as StringBuffer\n" +
        "    sb = new Object() [[as StringBuffer^^]]!!\n" +
        "  }" +
        "}")
    testNoIntention({f})
  }

//  Code in this function is invalid and can't be compiled, so it contradicts with testNoIntention() call
//  function testCastExpressionReferenceType_Super_VS_Sub_noInstace() {
//    // Boxed -> Boxed (the values must be compatible, you must explicitly cast if there is otherwise loss of precision or scale)
//		// superclass = subclass // always valid
//		// subclass = (subclass) superclass // valid at compile time, checked at run-time
//    var f = new GosuClassFile (
//        "package some.pkg\n\n" +
//        "uses java.lang.StringBuffer\n" +
//        "uses java.lang.AbstractStringBuilder\n" +
//        "class GosuClass {\n" +
//        "  function examInstanceCreadted() {\n" +
//        "    var obj = new Object();\n" +
//        "    var obj1= new Object() as Object;\n\n" +
//        "    var sb: StringBuffer;\n" +
//        "    var abStringBuilder: AbstractStringBuilder\n\n" +
//        "    sb = obj\n\n" +
//        "    obj = obj1;\n" +
//        "    sb = abStringBuilder as StringBuffer // abstract super downCast\n" +
//        "    abStringBuilder = sb" +
//        "  }" +
//        "}")
//    testNoIntention({f})
//  }

  function testCastExpressionReferenceType_Sub_vs_Suber_noInstace() {
    // Boxed -> Boxed (the values must be compatible, you must explicitly cast if there is otherwise loss of precision or scale)
    // subclass = superclass // not valid as written, requires a cast to compile
    var f = new GosuClassFile (
        "package some.pkg\n\n" +
        "uses java.lang.StringBuffer\n" +
        "uses java.lang.AbstractStringBuilder\n" +
        "class GosuClass {\n" +
        "  function exam2_Super2Sub() {\n" +
        "    var obj = new Object();\n" +
        "    var obj1= new Object() as Object;\n\n" +
        "    var sb: StringBuffer;\n" +
        "    var abStringBuilder: AbstractStringBuilder\n\n" +
        "    sb = [[obj^^]]\n\n" + //subclass = superclass // not valid as written, requires a cast to compile
        "    obj = obj1;\n" +
        "    sb = abStringBuilder as StringBuffer // abstract super downCast\n" +
        "    abStringBuilder = sb\n" +
        "  }" +
        "}")
    test({f}, "Cast to 'java.lang.StringBuffer'", "obj as StringBuffer")
  }

  function testCastExpressionReferenceType_NotRelated() {
    // Boxed -> Boxed (the values must be compatible, you must explicitly cast if there is otherwise loss of precision or scale)
    // thisclass = anotherclass as Object // not valid as written, requires a cast to compile
    var f = new GosuClassFile (
         "package some.pkg\n\n" +
         "uses java.lang.StringBuffer\n" +
         "uses java.lang.AbstractStringBuilder\n" +
         "uses gw.lang.parser.coercers.StringBufferCoercer \n" +
         "uses org.apache.commons.lang.math.Range \n" +
         "class GosuClass {\n" +
         "  function exam3_notRelated() {\n" +
         "    var thisType: StringBufferCoercer\n" +
         "    var stringBufferType: StringBufferCoercer\n" +
         "    var anotherType: Range\n" +
         "    thisType = stringBufferType\n" +
         "    thisType = [[anotherType as Object^^]]\n"  +
         "   }\n" +
         "}")
    test({f}, "Cast to 'gw.lang.parser.coercers.StringBufferCoercer'", "anotherType as StringBufferCoercer")
  }

  function test(resources: GosuTestingResource[], intention: String, replace: String) {
    printText("intention ${intention}, and replace with ${replace}.")

    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var initialCaret = markers.getCaret(MarkerType.CARET1)
    assertTrue("No initial caret location defined.", initialCaret != null)
    assertEquals("No range carets location defined.", 1, markers.Ranges.size())

    var content = initialCaret.Editor.Document.Text
    var expected = content.substring(0, markers.Ranges.get(0).StartOffset) + replace + content.substring(markers.Ranges.get(0).EndOffset)

    initialCaret.Editor.CaretModel.moveToOffset(initialCaret.offset);
    findAndInvokeIntentionAction(highlightErrors(), intention, initialCaret.Editor, initialCaret.File)

    var found = initialCaret.Editor.Document.Text
    assertEquals("Texts do not match, auto edit failed", expected, found)
  }
}