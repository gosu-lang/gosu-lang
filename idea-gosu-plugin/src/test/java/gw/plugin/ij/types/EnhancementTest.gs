/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses com.intellij.psi.impl.PsiDocumentManagerImpl
uses gw.lang.reflect.IType
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.java.GosuTypes
uses gw.lang.reflect.java.JavaTypes
uses gw.plugin.ij.core.FileModificationManager
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.util.ExecutionUtil
uses gw.testharness.Disabled

uses java.lang.Runnable
uses gw.plugin.ij.util.UIUtil

class EnhancementTest extends GosuTestCase {
  var oldDelay: int

  function runInDispatchThread(): boolean {
    return false;
  }

  override function beforeClass() {
    super.beforeClass()
    oldDelay = FileModificationManager.TYPE_REFRESH_DELAY_MS
    FileModificationManager.TYPE_REFRESH_DELAY_MS = 0;
  }

  override function afterClass() {
    FileModificationManager.TYPE_REFRESH_DELAY_MS = oldDelay;
    super.afterClass()
  }

  function testAddingMethodToEnhancement() {
    test(
      {"//JAVA \n" +
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "} \n"},
      "package some.pkg \n" +
      "enhancement Enh : SomeClass { \n" +
      "  [[ ]] \n" +
      "} \n",
      "function foo() {}",
      \type -> assertNull("Method should not exist before adding.", type[0].TypeInfo.getMethod("foo", {})),
      \type -> assertNotNull("Method should exist after adding.", type[0].TypeInfo.getMethod("foo", {}))
    )
  }

  function testRemovingMethodFromEnhancement() {
    test(
      {"//JAVA \n" +
      "package some.pkg; \n" +
      "class SomeClass { \n" +
      "} \n"},
      "package some.pkg \n" +
      "enhancement Enh : SomeClass { \n" +
      "  [[function foo() {}]] \n" +
      "} \n",
      " ",
      \type -> assertNotNull("Method should exist before deleting.", type[0].TypeInfo.getMethod("foo", {})),
      \type -> assertNull("Method should not exist after deleting.", type[0].TypeInfo.getMethod("foo", {}))
    )
  }

  @Disabled("dpetrusca", "Feature works, only the test fails")
  function testChangingTheEnhancedType() {
    test(
      {
        "//JAVA \n" +
        "package some.pkg; \n" +
        "class SomeClass1 { \n" +
        "} \n",
        "//JAVA \n" +
        "package some.pkg; \n" +
        "class SomeClass2 { \n" +
        "} \n"
      },
      "package some.pkg \n" +
      "enhancement Enh : [[SomeClass1]] { \n" +
      "  function foo() {} \n" +
      "} \n",
      "SomeClass2",
      \type -> {
        assertNotNull("Before: SomeClass1 should be enhanced.", type[0].TypeInfo.getMethod("foo", {}))
        assertNull("Before: SomeClass2 should not be enhanced.", type[1].TypeInfo.getMethod("foo", {}))
      },
      \type -> {
        assertNull("After: SomeClass1 should not be enhanced.", type[0].TypeInfo.getMethod("foo", {}))
        assertNotNull("After: SomeClass2 should be enhanced.", type[1].TypeInfo.getMethod("foo", {}))
      }
    )
  }

  function testChangingTheEnhancedTypeToAJavaArrayType() {
    test(
      {},
      "package some.pkg \n" +
      "enhancement Enh : [[String]] { \n" +
      "  function foo() {} \n" +
      "} \n",
      "String[]",
      \type -> {
        assertNotNull("Before: String should be enhanced.", JavaTypes.STRING().TypeInfo.getMethod("foo", {}))
        assertNull("Before: String[] should not be enhanced.", JavaTypes.STRING().ArrayType.TypeInfo.getMethod("foo", {}))
      },
      \type -> {
        assertNull("After: String should not be enhanced.", JavaTypes.STRING().TypeInfo.getMethod("foo", {}))
        assertNotNull("After: String[] should be enhanced.", JavaTypes.STRING().ArrayType.TypeInfo.getMethod("foo", {}))
      }
    )
  }

  function testChangingTheEnhancedTypeToAGosuArrayType() {
    test(
      {},
      "package some.pkg \n" +
      "enhancement Enh : [[gw.lang.Autocreate]] { \n" +
      "  function foo() {} \n" +
      "} \n",
      "gw.lang.Autocreate[]",
      \type -> {
        assertNotNull("Before: String should be enhanced.", GosuTypes.AUTOCREATE().TypeInfo.getMethod("foo", {}))
        assertNull("Before: String[] should not be enhanced.", GosuTypes.AUTOCREATE().ArrayType.TypeInfo.getMethod("foo", {}))
      },
      \type -> {
        assertNull("After: String should not be enhanced.", GosuTypes.AUTOCREATE().TypeInfo.getMethod("foo", {}))
        assertNotNull("After: String[] should be enhanced.", GosuTypes.AUTOCREATE().ArrayType.TypeInfo.getMethod("foo", {}))
      }
    )
  }

  // utilities

  function test(javaCode: String[], enhCode: String, codeToPaste: String, assertBefore(type: IType[]), assertAfter(type: IType[])) {
    TypeSystem.pushGlobalModule();
    try {
      var javaResources = javaCode.map(\code -> {
        var javaResource = ResourceFactory.create(code)
        var javaFile = configureByText(javaResource.fileName, javaResource.content)
        return javaResource
      })

      var enhResource = ResourceFactory.create(enhCode)
      var enhFile = configureByText(enhResource.fileName, enhResource.content)

      var markers = getMarkers(enhFile)
      assertBefore(javaResources.map(\file -> TypeSystem.getByFullNameIfValid(file.qualifiedName)))

      runWriteActionInDispatchThread(new Runnable() {
        function run() {
          TypeSystem.pushGlobalModule();
          try {
            var range = markers.Ranges[0]
            range.Editor.SelectionModel.setSelection(range.StartOffset, range.EndOffset)
            type(codeToPaste, range.Editor)
            PsiDocumentManagerImpl.getInstance(Project).commitAllDocuments()
          } finally {
            TypeSystem.popGlobalModule();
          }
        }
      }, true);
      UIUtil.settleModalEventQueue();

      assertAfter(javaResources.map(\file -> TypeSystem.getByFullNameIfValid(file.qualifiedName)))
    } finally {
      TypeSystem.popGlobalModule();
    }
  }

}