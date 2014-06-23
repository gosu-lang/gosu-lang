/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses gw.lang.parser.IHasInnerClass
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.gs.IGosuClass
uses gw.plugin.ij.framework.generator.ResourceFactory

class DynamicGosuTypesTest extends DynamicTypesTest {

  // adding

  function testAddingField() {
    testAdding(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[ ]]\n" +
      "} \n",
      "public var x: String",
      \type -> type.TypeInfo.getProperty("x")
    )
  }

  function testAddingMethod() {
    testAdding(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[ ]]\n" +
      "} \n",
      "public function someMethod(x: String): String { return x }",
      \type -> type.TypeInfo.getMethod("someMethod", {String})
    )
  }

  // removing

  function testRemovingField() {
    testRemoving(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[public var x: String]]\n" +
      "} \n",
      \type -> type.TypeInfo.getProperty("x")
    )
  }

  function testRemovingMethod() {
    testRemoving(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[public function someMethod(x: String): String { return x }]]\n" +
      "} \n",
      \type -> type.TypeInfo.getMethod("someMethod", {String})
    )
  }

  // removing

  function testModifyingField() {
    test(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[public var x: String]]\n" +
      "} \n",
      "public var y: String",
      \type -> assertNotNull(type.TypeInfo.getProperty("x")),
      \type -> assertNotNull(type.TypeInfo.getProperty("y"))
    )
  }

  function testModifyingMethod() {
    test(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[public function someMethod(x: String): String { return x }]]\n" +
      "} \n",
      "public function someMethodNew(x: String): String { return x }",
      \type -> assertNotNull(type.TypeInfo.getMethod("someMethod", {String})),
      \type -> assertNotNull(type.TypeInfo.getMethod("someMethodNew", {String}))
    )
  }

  // super class/interface modification

  function testModifyingSuperClass() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "class SuperClass { \n" +
      "  [[ ]]\n" +
      "} \n"
    )
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "class SubClass extends SuperClass { \n" +
      "} \n"
    )
    var subGosuClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "class SubGosuClass extends SubClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var superType = TypeSystem.getByFullNameIfValid("some.pkg.SuperClass", GosuModule) as IGosuClass
    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    var subGosuType = TypeSystem.getByFullNameIfValid("some.pkg.SubGosuClass", GosuModule) as IGosuClass
    assertNull("Should be null in SuperClass before modification", superType.TypeInfo.getMethod("someMethod", {String}))
    assertNull("Should be null in SubClass before modification", subType.TypeInfo.getMethod("someMethod", {String}))
    assertNull("Should be null in SubGosuClass before modification", subGosuType.TypeInfo.getMethod("someMethod", {String}))

    type(range, "public function someMethod(x: String): String { return x }")

    assertNotNull("Should be non-null in SuperClass after modification", superType.TypeInfo.getMethod("someMethod", {String}))
    assertNotNull("Should be non-null in SubClass after modification", subType.TypeInfo.getMethod("someMethod", {String}))
    assertNotNull("Should be non-null in SubGosuClass after modification", subGosuType.TypeInfo.getMethod("someMethod", {String}))
  }

  function testModifyingSuperInterface() {
    var superClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "interface SuperClass { \n" +
      "  [[ ]]\n" +
      "} \n"
    )
    var x = superClass.VirtualFile.Name
    var subClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "interface SubClass extends SuperClass { \n" +
      "} \n"
    )
    var subGosuClass = ResourceFactory.createFile(this,
      "package some.pkg; \n" +
      "interface SubGosuClass extends SubClass { \n" +
      "} \n"
    )
    var range = getMarkers(superClass).Ranges[0]
    setActiveEditor(range.Editor)

    var superType = TypeSystem.getByFullNameIfValid("some.pkg.SuperClass", GosuModule) as IGosuClass
    var subType = TypeSystem.getByFullNameIfValid("some.pkg.SubClass", GosuModule) as IGosuClass
    var subGosuType = TypeSystem.getByFullNameIfValid("some.pkg.SubGosuClass", GosuModule) as IGosuClass
    assertNull("Should be null in SuperClass before modification", superType.TypeInfo.getMethod("someMethod", {String}))
    assertNull("Should be null in SubClass before modification", subType.TypeInfo.getMethod("someMethod", {String}))
    assertNull("Should be null in SubGosuClass before modification", subGosuType.TypeInfo.getMethod("someMethod", {String}))

    type(range, "function someMethod(x: String): String ")

    assertNotNull("Should be non-null in SuperClass after modification", superType.TypeInfo.getMethod("someMethod", {String}))
    assertNotNull("Should be non-null in SubClass after modification", subType.TypeInfo.getMethod("someMethod", {String}))
    assertNotNull("Should be non-null in SubGosuClass after modification", subGosuType.TypeInfo.getMethod("someMethod", {String}))
  }

  @gw.testharness.Disabled("verastov", "functionality does not implemented")
  function testCanDeleteAnInnerTypeOfAGosuType() {
    assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass"))
    assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
    test(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[public class Inner {}]]\n" +
      "} \n",
      " ",
      \type -> {
        assertEquals(1, (type as IHasInnerClass).InnerClasses.size())
        assertEquals("some.pkg.SomeClass.Inner", (type as IHasInnerClass).InnerClasses[0].Name)
        assertNotNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
      },
      \type -> {
        assertEquals(0, (type as IHasInnerClass).InnerClasses.size())
        assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
      }
    )
  }

  function testCanAddAnInnerTypeToAGosuType() {
    assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass"))
    assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
    test(
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "  [[ ]]\n" +
      "} \n",
      "public class Inner {}",
      \type -> {
        assertEquals(0, (type as IHasInnerClass).InnerClasses.size())
        assertNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
      },
      \type -> {
        assertEquals(1, (type as IHasInnerClass).InnerClasses.size())
        assertEquals("some.pkg.SomeClass.Inner", (type as IHasInnerClass).InnerClasses[0].Name)
        assertNotNull(TypeSystem.getByFullNameIfValid("some.pkg.SomeClass.Inner"))
      }
    )
  }

}