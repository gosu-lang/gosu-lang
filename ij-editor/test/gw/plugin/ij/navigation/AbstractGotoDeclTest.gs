/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
uses gw.plugin.ij.framework.FileMarkers
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.framework.generator.ClassGenerator
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.typeinfo.GosuScratchpadAction

abstract class AbstractGotoDeclTest extends GosuTestCase {

  public var generator : ClassGenerator = new ClassGenerator ("some.pkg", "GosuClass")

  function test(body: String ) {
    test( generator.generate(body))
  }

  function test(testResource : GosuTestingResource ) {
    test({testResource})
  }

  //    function test(resources: GosuTestingResource[], target : GosuTestingResource) {
  //        var x = resources.toList()
  //        x.add(target)
  //        var a = x.toArray(new GosuTestingResource[x.size()])
  //        test(a)
  //    }


  function testGotoNonProjectFile(resources: GosuTestingResource , targetFQN: String , targetText: String ) {
    var markers = getAllMarkers({configureByText(resources.fileName, resources.content)})
    var ranges = markers.getRanges()
    assertEquals("Range marker is not defined.", 1, ranges.size())

    for (offset in ranges[0].StartOffset..ranges[0].EndOffset) {
      var realTarget = GotoDeclarationAction .findTargetElement(getProject(), ranges[0].Editor, offset)
      assertNotNull("Navigation failed with ${ranges.first()} at ${offset}", realTarget)
      assertEquals(targetFQN, NavigationUtil.getFQN(realTarget))
      assertEquals(targetText, realTarget.Text.substring(0, targetText.length))
    }
  }

  function testGotoTemplateFile(resources: GosuTestingResource[] , targetFQN: String ) {
    testGotoFile(resources, targetFQN)
  }

  function testGotoProgramFile(resources: GosuTestingResource[] , targetFQN: String ) {
    testGotoFile(resources, targetFQN)
  }

  function testGotoFile(resources: GosuTestingResource[] , targetFQN: String) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var ranges = markers.getRanges()
    assertEquals("Range marker is not defined.", 1, ranges.size())

    for (offset in ranges[0].StartOffset..ranges[0].EndOffset) {
      var realTarget = GotoDeclarationAction .findTargetElement(getProject(), ranges[0].Editor, offset)
      assertNotNull("Navigation failed with ${ranges.first()} at ${offset}", realTarget)
      assertEquals(targetFQN, NavigationUtil.getFQN(realTarget))
    }
  }

  function test(resources: GosuTestingResource[]) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    NavigationUtil.navigate(markers)
  }

  function configureScratchpad(text: String): FileMarkers {
    var markers = new FileMarkers();
    var fileText = configureMarker(text, markers);
    var editor = GosuScratchpadAction.openScratchpadFile(Project, fileText)
    markers.setEditor(editor)
    return markers
  }
}
