/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator

uses junit.framework.Assert

class GosuEnhancementFile extends GosuTestingResource {

  construct(_usesList: String, _packageName: String, _enhName: String, _enhType: String, _content: String) {
    super(_packageName + "/" + _enhName + ".gs",
    "package ${_packageName}\n" +
    "${_usesList}\n" +
    "enhancemnt ${_enhName} : ${_enhType} {\n" +
    "${_content}\n" +
      "}"
    )
  }

  construct(enhContent: String) {
    super(getFileName(enhContent), enhContent)
  }

    static function isEnhancement(text: String): boolean {
    text = removeMarkers(text)
    return text.contains("package") && text.contains("enhancement")
  }

    static function getFileName(text: String): String {
    text = removeMarkers(text)
    Assert.assertTrue("Your enhancemrnt does not contain a package statement.", text.contains("package"))
    Assert.assertTrue("Your enhancemrnt does not contain the word enhancement.", text.contains("enhancement"))
    var i1 = text.indexOf("package ") + 8
    var i2 = wordEnd(text, i1)
    var pkg = text.substring(i1, i2).trim()
    i1 = text.indexOf("enhancement ") + 12
    while (text.charAt(i1) == ' ') i1++
    i2 = wordEnd(text, i1)
    var cls = text.substring(i1, i2).trim()
    if (cls.endsWith(":")) cls = cls.substring(0, cls.length - 1)
    return pkg.replace('.', '/') + "/" + cls + ".gsx"
  }
}