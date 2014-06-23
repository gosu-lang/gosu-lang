/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.gs.IGosuClass
uses gw.plugin.ij.framework.generator.ResourceFactory

class ModificationTest extends DynamicTypesTest {

  override function runInDispatchThread(): boolean {
    return false;
  }

  //==========class

  //method return type
  function testSuperGosuClassFunctionReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass { \n" +
      "  function foo() : [[String]] {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  function testSuperJavaClassFunctionReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass { \n" +
      "  public [[String]] foo() {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  //method signature
  function testSuperGosuClassFunctionSignatureModified() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass { \n" +
      "  function foo(s : [[String]]) : String {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "int")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method foo(int).", subType.TypeInfo.getMethod("foo", {int}))
  }

  function testSuperJavaClassFunctionSignitureModified() {
    var superClass = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass { \n" +
      "  public String foo([[String]] s) {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "int")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method foo(int).", subType.TypeInfo.getMethod("foo", {int}))
  }

  //property type
  function testSuperGosuClassPropertyReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass { \n" +
      "  var _field : [[String]] as Field\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getProperty("Field").FeatureType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getProperty("Field").FeatureType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  function testSuperJavaClassPropertyReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass { \n" +
      "  public [[String]] getField() {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getProperty("Field").FeatureType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getProperty("Field").FeatureType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  function testSuperJavaClassMemberReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public  class SuperClass { \n" +
      "  public [[String]] field; \n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getProperty("field").FeatureType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getProperty("field").FeatureType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  //super class
  function testExtendingSuperClassModifiedFromG2G() {
    var superClass1 = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass1 { \n" +
      "  function foo(s : String) : String {}\n" +
      "} \n"
    )
    var superClass2 = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass2 { \n" +
      "  function bar(s : String) : String {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends [[SuperClass1]] { \n" +
      "} \n"
    )
    var range = getMarkers(subClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    subType.TypeInfo.getMethod("foo", {String}).Parameters.each( \ elt -> print(elt.Name))
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "SuperClass2")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method bar(String).", subType.TypeInfo.getMethod("bar", {String}))
  }

  function testExtendingSuperClassModifiedFromG2J() {
    var superClass1 = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass1 { \n" +
      "  function foo(s : String) : String {}\n" +
      "} \n"
    )
    var superClass2 = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass2 { \n" +
      "  public String bar(String s) {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends [[SuperClass1]] { \n" +
      "} \n"
    )
    var range = getMarkers(subClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "SuperClass2")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method bar(String).", subType.TypeInfo.getMethod("bar", {String}))
  }

  function testExtendingSuperClassModifiedFromJ2G() {
    var superClass1 = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass1 { \n" +
      "  public String foo(String s) {}\n" +
      "} \n"
    )
    var superClass2 = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "public class SuperClass2 { \n" +
      "  function bar(s : String) : String {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends [[SuperClass1]] { \n" +
      "} \n"
    )
    var range = getMarkers(subClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "SuperClass2")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method bar(String).", subType.TypeInfo.getMethod("bar", {String}))
  }

  function testExtendingSuperClassModifiedFromJ2J() {
    var superClass1 = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass1 { \n" +
      "  public String foo(String s) {}\n" +
      "} \n"
    )
    var superClass2 = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "public class SuperClass2 { \n" +
      "  public String bar(String s) {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends [[SuperClass1]] { \n" +
      "} \n"
    )
    var range = getMarkers(subClass).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertNotNull("Should contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    type(range, "SuperClass2")
    assertNull("Should not contain method foo(String).", subType.TypeInfo.getMethod("foo", {String}))
    assertNotNull("Should contain method bar(String).", subType.TypeInfo.getMethod("bar", {String}))
  }

  //==========enhancement
  function testSuperGosuClassEnhancementFunctionReturnTypeModified() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SuperClass { \n" +
      "} \n"
    )
    var superClassEnh = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "enhancement SuperClassEnh : SuperClass { \n" +
      "  function foo() : [[String]] {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClassEnh).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

  function testSuperJavaClassFunctionReturnTypeModification() {
    var superClass = ResourceFactory.createFile(this,
      "//JAVA \n" +
      "package some.pkg; \n" +
      "class SuperClass { \n" +
      "} \n"
    )
    var superClassEnh = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "enhancement SuperClassEnh : SuperClass { \n" +
      "  function foo() : [[String]] {}\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClassEnh).Ranges[0]
    setActiveEditor(range.Editor)

    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    assertTrue(subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName.contains("String"))
    type(range, "int")
    var atype = subType.TypeInfo.getMethod("foo", {}).ReturnType.DisplayName
    assertTrue("Expected type is int, but actually type is " + atype, atype.contains("int"))
  }

}