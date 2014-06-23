/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiFieldsTest extends GosuPsiTestCase
{
  function testIncremental_ClassWithFields()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  var _field1 : String\n" +
      "  var _field2 : int\n" +
      "  var _field3 : String[]\n" +
      "  var _field4 : byte[]\n" +
      "  var _field5 : List<String>\n" +
      "}" )
  }
  function testIncremental_ClassWithAssignedFields()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n\n" +
      "uses java.util.ArrayList\n\n" +
      "class TestClass1 {\n" +
      "  var _field1 : String = 'hello'\n" +
      "  var _field2 : int = 8\n" +
      "  var _field3 : String[] = new String[] {'one'}\n" +
      "  var _field4 : byte[] = {1,2}\n" +
      "  var _field5 : List<String> = new ArrayList<String>()\n" +
      "}" )
  }
  function testIncremental_ClassWithAssignedFieldsImplicitType()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n\n" +
      "uses java.util.ArrayList\n\n" +
      "class TestClass1 {\n" +
      "  var _field1 = 'hello'\n" +
      "  var _field2 = 8\n" +
      "  var _field3 = new String[] {'one'}\n" +
      "  var _field4 = {1,2}\n" +
      "  var _field5 = new ArrayList<String>()\n" +
      "}" )
  }

  function testIncremental_ClassWithPropertyFields()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  var _field1 : String as Field1\n" +
      "  var _field2 : int as Field2\n" +
      "  var _field3 : String[] as Field3\n" +
      "  var _field4 : byte[] as Field4\n" +
      "  var _field5 : List<String> as Field5\n" +
      "}" )
  }

  function testIncremental_ClassWithReadonlyPropertyFields()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  var _field1 : String as readonly Field1\n" +
      "  var _field2 : int as readonly Field2\n" +
      "  var _field3 : String[] as readonly Field3\n" +
      "  var _field4 : byte[] as readonly Field4\n" +
      "  var _field5 : List<String> as readonly Field5\n" +
      "}" )
  }

  function testIncremental_ClassWithBadFieldHavingNonDeclKeywordAsName()
  {
    assertIncremental_PsiTextEqualsSource(
      "some/pkg/TestClass1.gs",
      "package some.pkg\n" +
      "class TestClass1 {\n" +
      "  var new : String" +
      "  function foo() {}\n" +
      "}" )
  }
}