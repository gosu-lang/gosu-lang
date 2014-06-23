/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses gw.lang.reflect.IType
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.module.IModule
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory

class ModularizationTest extends GosuTestCase {

  override function beforeClass() {
    initModules(new String[] {"Module1", "Module2<Module1", "Module3"})
    super.beforeClass();
  }

  function beforeMethod() {
    deleteAllFiles("Module1")
    deleteAllFiles("Module2")
    deleteAllFiles("Module3")
    super.beforeMethod()
  }

  function testJavaClass() {
    var file = ResourceFactory.createFile(this, Module1.Name,
                                          "//JAVA \n" +
                                              "package some.pkg; \n" +
                                              "class JavaClass {} \n"
        )
    assertTypeExists("some.pkg.JavaClass", Module1)
    assertTypeExists("some.pkg.JavaClass", Module2)
    assertTypeDoesntExist("some.pkg.JavaClass", Module3)
    /*assertNotNull("Type must exist in Module1", TypeSystem.getByFullNameIfValid("some.pkg.JavaClass", Module1))
    assertNotNull("Type must exist in Module2", TypeSystem.getByFullNameIfValid("some.pkg.JavaClass", Module2))
    assertNull("Type must not exist in Module3", TypeSystem.getByFullNameIfValid("some.pkg.JavaClass", Module3))*/
  }

  function testGosuClass() {
    var file = ResourceFactory.createFile(this, Module1.Name,
                                          "package some.pkg; \n" +
                                              "class GosuClass {} \n"
        )
    assertTypeExists("some.pkg.GosuClass", Module1)
    assertTypeExists("some.pkg.GosuClass", Module2)
    assertTypeDoesntExist("some.pkg.GosuClass", Module3)
  }

  function testEnhancementOnJavaClass() {
    ResourceFactory.createFile(this, Module1.Name,
                               "//JAVA \n" +
                                   "package some.pkg; \n" +
                                   "class JavaClass {} \n"
        )
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "enhancement JavaClassEnh : JavaClass { \n" +
                                   " function foo() {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.JavaClass", "foo", {}, Module1)
    assertMethodExists("some.pkg.JavaClass", "foo", {}, Module2)
  }

  function testEnhancementOnGosuClass() {
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "class GosuClass {} \n"
        )
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "enhancement GosuClassEnh : GosuClass { \n" +
                                   " function foo() {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module1)
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module2)
  }

  function testEnhancementMethodsVisibilityOnMultiModules() {
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "class GosuClass {} \n"
        )
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "enhancement GosuClassEnh : GosuClass { \n" +
                                   " function foo() {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module1)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module1)
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module2)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module2)

    ResourceFactory.createFile(this, Module2.Name,
                               "package some.pkg; \n" +
                                   "enhancement GosuClass1Enh : GosuClass { \n" +
                                   " function bar() : String {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module1)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module1)
    assertMethodDoesntExist("some.pkg.GosuClass", "bar", {}, Module1)

    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module2)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module2)
    assertMethodExists("some.pkg.GosuClass", "bar", {}, Module2)
    assertMethodReturnType("some.pkg.GosuClass", "bar", {}, "String", Module2)
  }

  function testEnhancementMethodsVisibilityOnMultiModuleWithSameSignitureDifferentReturnTypeMethods() {
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "class GosuClass {} \n"
        )
    ResourceFactory.createFile(this, Module1.Name,
                               "package some.pkg; \n" +
                                   "enhancement GosuClassEnh : GosuClass { \n" +
                                   " function foo() {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module1)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module1)
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module2)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module2)

    ResourceFactory.createFile(this, Module2.Name,
                               "package some.pkg; \n" +
                                   "enhancement GosuClass1Enh : GosuClass { \n" +
                                   " function foo() : String {} \n" +
                                   "} \n"
        )
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module1)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "void", Module1)
    assertMethodExists("some.pkg.GosuClass", "foo", {}, Module2)
    assertMethodReturnType("some.pkg.GosuClass", "foo", {}, "String", Module2)
  }

  property get Module1(): IModule {
    return TypeSystem.getExecutionEnvironment().getModule("Module1")
  }

  property get Module2(): IModule {
    return TypeSystem.getExecutionEnvironment().getModule("Module2")
  }

  property get Module3(): IModule {
    return TypeSystem.getExecutionEnvironment().getModule("Module3")
  }

  function assertTypeExists(typeName: String, m: IModule) {
    TypeSystem.pushModule(m)
    try {
      assertNotNull("Method must exist in Module1", TypeSystem.getByFullNameIfValid(typeName))
    } finally {
      TypeSystem.popModule(m)
    }
  }

  function assertTypeDoesntExist(typeName: String, m: IModule) {
    TypeSystem.pushModule(m)
    try {
      assertNull("Method must exist in Module1", TypeSystem.getByFullNameIfValid(typeName))
    } finally {
      TypeSystem.popModule(m)
    }
  }

  function assertMethodExists(typeName: String, methodName: String, args: IType[], m: IModule) {
    TypeSystem.pushModule(m)
    try {
      assertNotNull("Method must exist in Module1", TypeSystem.getByFullNameIfValid(typeName).TypeInfo.getMethod(methodName, args))
    } finally {
      TypeSystem.popModule(m)
    }
  }

  function assertMethodDoesntExist(typeName: String, methodName: String, args: IType[], m: IModule) {
    TypeSystem.pushModule(m)
    try {
      assertNull("Method must exist in Module1", TypeSystem.getByFullNameIfValid(typeName).TypeInfo.getMethod(methodName, args))
    } finally {
      TypeSystem.popModule(m)
    }
  }

  function assertMethodReturnType(typeName: String, methodName: String, args: IType[], returnType: String, m: IModule) {
    TypeSystem.pushModule(m)
    try {
      var method = TypeSystem.getByFullNameIfValid(typeName).TypeInfo.getMethod(methodName, args)
      assertNotNull("Method must exist in Module1", method)
      assertTrue("Method return type should be '" + returnType + "', but was '" + method.ReturnType.DisplayName + "'",
        method.ReturnType.DisplayName.contains(returnType))
    } finally {
      TypeSystem.popModule(m)
    }
  }

}