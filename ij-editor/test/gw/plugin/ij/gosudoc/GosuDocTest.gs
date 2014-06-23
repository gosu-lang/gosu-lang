/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.gosudoc

uses com.intellij.codeInsight.documentation.DocumentationManager
uses com.intellij.psi.PsiClass
uses com.intellij.psi.PsiElement
uses com.intellij.psi.PsiField
uses com.intellij.psi.PsiMethod
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.ResourceFactory

uses java.util.List

class GosuDocTest extends GosuTestCase {
  function testClass() {
    testGosuDocContains(
        "package test \n" +
            "/** \n" +
            " * Created by IntelliJ IDEA. \n" +
            " */ \n" +
            "class Gosu^^Class {\n\n" +
            "}",
            "Created by IntelliJ IDEA."
    )
  }

  function testMethod() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "  /** \n" +
            "   * This is my method. \n" +
            "   */ \n" +
            "  function f^^oo() {} \n" +
            "}",
            "This is my method."
    )
  }

  function testMethodParameter() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n" +
            "  function f^^oo(a: String) {} \n" +
            "}",
            {"function", "(a:", "java.lang.String"}
    )
  }

  function testInterface() {
    testGosuDocContains(
        "package test \n" +
            "/** \n" +
            " * Created by IntelliJ IDEA. \n" +
            " */ \n" +
            "interface Gosu^^Class {\n\n" +
            "}",
            "Created by IntelliJ IDEA."
    )
  }

  function testEnhancement() {
    testGosuDocContains(
        "package test \n" +
            "/** \n" +
            " * Created by IntelliJ IDEA. \n" +
            " */ \n" +
            "enhancement StringTest^^Enhancement : java.lang.String {\n\n" +
            "}",
            {"Created by IntelliJ IDEA.", "Enhancement", "test.StringTestEnhancement", "java.lang.String"}
    )
  }

  /**
   * current output: function construct()
   */
  function testConstructor() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "  c^^onstruct() { } \n" +
            "}",
            {"construct", "()", "-function"}
    )
  }

  function testConstructorComments() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "  /** \n" +
            "   * This is the constructor. \n" +
            "   */ \n" +
            "  c^^onstruct() { } \n" +
            "}",
            "This is the constructor."
    )
  }

  function testFieldDeclarations() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "  /** \n" +
            "   * This is my field. \n" +
            "   */ \n" +
            "  var i^^: int \n" +
            "}",
            "This is my field."
    )
  }

  function testGetProperty() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "private var _field4: String \n" +
            "  /** \n" +
            "   * This is property getter. \n" +
            "   */ \n" +
            " property get F^^ield4(): String {" +
            "    return _field4  \n" +
            "}",
            "This is property getter."
    )
  }

  function testSetProperty() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "private var _field4: String \n" +
            "  /** \n" +
            "   * This is property setter. \n" +
            "   */ \n" +
            " property set F^^ield4(str: String): String {" +
            "    _field4 = str  \n" +
            "}",
            "This is property setter."
    )
  }

  /**
   * the test will pass if comment out the line contains @deprecated and remove "Deplicated", "Replaced by other class" from verification string list
   */
  function testClassTags() {
    testGosuDocContains(
        "package test \n" +
            "/** \n" +
            " * @since 1.4 \n" +
            " * @see  java.lang.Character#charValue() \n" +
            " * @deprecated  Replaced by something else \n" +
            " */ \n" +
            "class Gosu^^Class {\n\n" +
            "}",
            {"Since", "1.4", "See Also:", "java.lang.Character.charValue()", "Deprecated", "Replaced by something else"}
    )
  }

  /**
   * the test will pass if comment out the line contains @deprecated and remove "Deplicated", "Replaced by other class" from verification string list
   */
  function testInterfaceTags() {
    testGosuDocContains(
        "package test \n" +
            "/** \n" +
            " * @since 1.4 \n" +
            " * @see  java.lang.Character#charValue() \n" +
            " * @deprecated  Replaced by something else \n" +
            " */ \n" +
            "interface Gosu^^Interface {\n\n" +
            "}",
            {"Since", "1.4", "See Also:", "java.lang.Character.charValue()", "Deprecated", "Replaced by something else"}
    )
  }

  function testConstructorTags() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "/** \n" +
            " * {@link #assertEquals(float, float, float)}  method. \n" +
            " * @since 1.4. \n" +
            " * @return    the desired character. \n" +
            " * @throws StringIndexOutOfRangeException \n" +
            " * @see       java.lang.Character#charValue() \n" +
            " * @see          <a href=\"URL#value\">label</a> \n" +
            " */ \n" +
            "  const^^ruct() \n" +
            "}"
            ,
            {"assertEquals(float, float, float)", "Returns:", "the desired character.", "Throws:", "StringIndexOutOfRangeException", "Since", "1.4", "See Also:", "java.lang.Character.charValue()", "label"}
    )
  }

  function testMethodTags() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "/** \n" +
            " * {@link #assertEquals(float, float, float)}  method. \n" +
            " * @since 1.4. \n" +
            " * @param     index. \n" +
            " * @return    the desired character. \n" +
            " * @exception StringIndexOutOfRangeException \n" +
            " * @see       java.lang.Character#charValue() \n" +
            " * @see          <a href=\"URL#value\">label</a> \n" +
            " */ \n" +
            "  function f^^oo() \n" +
            "}"
            ,
            {"assertEquals(float, float, float)", "Returns:", "the desired character.", "Throws:", "StringIndexOutOfRangeException", "Since", "1.4", "See Also:", "java.lang.Character.charValue()", "label"}
    )
  }

  /**
   *    @serialField
   *    @value
   */
  function testFieldDeclarationsTags() {
    testGosuDocContains(
        "package test \n" +
            "class GosuClass {\n\n" +
            "  /** \n" +
            "   * {@link #assertEquals(float, float, float)} \n" +
            "   * @since   1.4 \n" +
            "   * @see   java.lang.Character#charValue() \n" +
            "   */ \n" +
            "  var i^^: int \n" +
            "}",
            {"assertEquals(float, float, float)", "Since", "1.4", "See Also:", "java.lang.Character.charValue()"}
    )
  }

  function testReuseInterfaceMethodComments() {
    testGosuDocContains({
        "package test \n" +
            "public interface GosuInterface {\n\n" +
            "/** \n" +
            " * Comments from Interface method foo. \n" +
            " */ \n" +
            "  function foo(a: String) \n" +
            "}"
            ,
            "package test \n" +
                "public class GosuClass implements GosuInterface {\n\n" +
                "  function f^^oo(a: String) {} \n" +
                "}"}
        ,
        {"Comments from Interface method foo."}
    )
  }

  /**
   * There's no issue if no class comments in subclass
   *
   *
   * Another failed scenario (inherit comment from super interface):
   * create interface GosuInterface1, function foo() with comments
   * create interface GosuInterface2 extends GosuInterface1, add comments for GosuInterface2, function foo() without comments
   * create interface GosuInterface3 extends GosuInterface2, function foo(), no comments for foo() and GosuInterface3
   * verify the doc for GosuInterface3.foo() inherit comments from GosuInterface1.foo()
   */
  function testReuseOverridenSuperclassMethodComments() {
    testGosuDocContains({
        "package test \n" +
            "public class GosuClass {\n\n" +
            "/** \n" +
            " * Comments from superclass method foo. \n" +
            " */ \n" +
            "  function foo() {} \n" +
            "}"
            ,
            "package test \n" +
                "/** \n" +
                " * Comments for GosuSubClass \n" +
                " */ \n" +
                "public class GosuSubClass extends GosuClass {\n\n" +
                "  function f^^oo() {} \n" +
                "}"}
        ,
        {"Description copied from class:", "Comments from superclass method foo", "Overrides:", "GosuClass"}
    )
  }

  function testReuseOverridenSuperinterfaceMethodComments() {
    testGosuDocContains({
        "package test \n" +
            "interface GosuSuperInterface {\n\n" +
            "/** \n" +
            " * Comments from super interface method foo. \n" +
            " */ \n" +
            "  function foo() \n" +
            "}"
            ,
            "package test \n" +
                "interface GosuInterface extends GosuSuperInterface {\n\n" +
                "  function f^^oo() {} \n" +
                "}"}
        ,
        {"Comments from super interface method foo."}
    )
  }

  function testGosuDocContains(body: String, doc: String) {
    testGosuDocContains({body}, {doc})
  }

  function testGosuDocContains(body: String, docs: List < String >) {
    testGosuDocContains({body}, docs)
  }

  function testGosuDocContains(body: List < String >, docs: List < String >) {
    testGosuDocContainsImpl(body.map(\ elt -> ResourceFactory.create(elt)).toList(), docs)
  }

  function testGosuDocContainsImpl(resources: List < GosuTestingResource >, docs: List < String >) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)).toTypedArray())
    var caret = markers.getCaret(MarkerType.CARET1)
    var docElement = findDocElement(caret.File.findElementAt(caret.offset))
    var provider = DocumentationManager.getProviderFromElement(docElement)
    var gosudoc = provider.generateDoc(docElement, null)
    assertNotNull("No documentation found", gosudoc)
    for (doc in docs) {
      if (doc.startsWith("-")) {
        doc = doc.substring(1)
        assertFalse("Documentation should not contain: " + doc + "\n" + gosudoc, gosudoc.contains(doc))
      } else {
        assertTrue("Documentation should contain: " + doc + "\n" + gosudoc, gosudoc.contains(doc))
      }
    }
  }

  function findDocElement(psi: PsiElement): PsiElement {
    while (psi != null && !(psi typeis PsiClass) && !(psi typeis PsiMethod) && !(psi typeis PsiField)) {
      psi = psi.Parent
    }
    return psi
  }
}