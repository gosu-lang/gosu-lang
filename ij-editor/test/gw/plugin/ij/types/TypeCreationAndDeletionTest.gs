/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.reflect.java.IJavaType
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory

class TypeCreationAndDeletionTest extends GosuTestCase {

  function testCanDeleteGosuType() {
    testCanDeleteType(
      "package some.pkg \n" +
      "class SomeClass {}  \n"
    )
  }

  function testCanDeleteJavaType() {
    testCanDeleteType(
      "//JAVA \n" +
      "package some.pkg; \n" +
      "class SomeClass {}  \n"
    )
  }

  function testCanCreateAJavaClassToOverrideAPreviousGosuClass() {
    var text = "package some.pkg; class SomeClass {}"
    var file = ResourceFactory.createFile(this, text)
    var type = TypeSystem.getByFullNameIfValid("some.pkg.SomeClass")
    assertNotNull("Could not find type in TypeSystem", type)
    assertTrue("It was supposed to be a Gosu type (" + type.TypeLoader + ")", type typeis IGosuClass)

    deleteFile(file.VirtualFile)
    assertNull("Type is still in the TypeSystem", TypeSystem.getByFullNameIfValid("some.pkg.SomeClass"))

    file = ResourceFactory.createFile(this, "//JAVA \n" + text)
    type = TypeSystem.getByFullNameIfValid("some.pkg.SomeClass")
    assertNotNull("Could not find type in TypeSystem", type)
    assertTrue("It was supposed to be a Java type (" + type.TypeLoader + ")", type typeis IJavaType)
  }

  function testCanCreateAGosuClassToOverrideAPreviousJavaClass() {
    var text = "package some.pkg; class SomeClass {}"
    var file = ResourceFactory.createFile(this, "//JAVA \n" + text)
    var type = TypeSystem.getByFullNameIfValid("some.pkg.SomeClass")
    assertNotNull("Could not find type in TypeSystem", type)
    assertTrue("It was supposed to be a Java type (" + type.TypeLoader + ")", type typeis IJavaType)

    deleteFile(file.VirtualFile)
    assertNull("Type is still in the TypeSystem", TypeSystem.getByFullNameIfValid("some.pkg.SomeClass"))

    file = ResourceFactory.createFile(this, text)
    type = TypeSystem.getByFullNameIfValid("some.pkg.SomeClass")
    assertNotNull("Could not find type in TypeSystem", type)
    assertTrue("It was supposed to be a Gosu type (" + type.TypeLoader + ")", type typeis IGosuClass)
  }

  function testCanDeleteType(text: String) {
    var r = ResourceFactory.create(text)
    var file = configureByText(r.fileName, r.content)
    assertNotNull("Could not find type in TypeSystem", TypeSystem.getByFullNameIfValid(r.qualifiedName))
    deleteFile(file.VirtualFile)
    assertNull("Type is still in the TypeSystem after being deleted!", TypeSystem.getByFullNameIfValid(r.qualifiedName))
  }

}