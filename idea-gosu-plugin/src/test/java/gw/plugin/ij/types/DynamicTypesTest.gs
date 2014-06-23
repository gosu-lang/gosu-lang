/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.types

uses com.intellij.psi.impl.PsiDocumentManagerImpl
uses gw.lang.reflect.IType
uses gw.lang.reflect.TypeSystem
uses gw.plugin.ij.core.FileModificationManager
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.SmartTextRange
uses gw.plugin.ij.framework.generator.ResourceFactory
uses gw.plugin.ij.util.ExecutionUtil

uses java.lang.Runnable
uses gw.plugin.ij.util.UIUtil

abstract class DynamicTypesTest extends GosuTestCase {
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

  // utilities

  function testRemoving(code: String, getFeature(type: IType): Object) {
    test(code, " ",
      \type -> assertNotNull("Feature does not exist before removing", getFeature(type)),
      \type -> assertNull("Feature still exists after removal", getFeature(type))
    )
  }

  function testAdding(code: String, codeToAdd: String, getFeature(type: IType): Object) {
    test(code, codeToAdd,
      \type -> assertNull("Feature exists before being added", getFeature(type)),
      \type -> assertNotNull("Feature does not exist after being added", getFeature(type))
    )
  }

  function test(code: String, codeToPaste: String, assertBefore(type: IType), assertAfter(type: IType)) {
    var resource = ResourceFactory.create(code)
    var file = configureByText(resource.fileName, resource.content)
    var markers = getMarkers(file)
    var type = TypeSystem.getByFullNameIfValid(resource.qualifiedName, GosuModule)
    assertBefore(type)
    type(markers.Ranges[0], codeToPaste)
    assertAfter(type)
  }

  function type(range: SmartTextRange, text: String) {
    runWriteActionInDispatchThread(new Runnable() {
      function run() {
        TypeSystem.pushModule(GosuModule);
        try {
          range.Editor.SelectionModel.setSelection(range.StartOffset, range.EndOffset)
          type(text, range.Editor)
          PsiDocumentManagerImpl.getInstance(Project).commitAllDocuments()
        } finally {
          TypeSystem.popModule(GosuModule);
        }
      }
    }, true);
    UIUtil.settleModalEventQueue();
  }
}