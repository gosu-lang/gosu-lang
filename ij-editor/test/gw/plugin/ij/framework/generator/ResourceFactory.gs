/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator

uses com.intellij.psi.PsiFile
uses gw.plugin.ij.framework.GosuTestCase
uses junit.framework.Assert

uses java.lang.RuntimeException
uses java.util.List

class ResourceFactory {

    static function create(text: String): GosuTestingResource {
    var cleanText = GosuTestingResource.removeMarkers(text.trim())
    var uncommentedText : String
    var tags: List<String> = {}
    if (cleanText.startsWith("//")) {
      uncommentedText = text.substring(text.indexOf('\n') + 1, text.length)
      var propsString = cleanText.substring(2, cleanText.indexOf('\n')).trim()
      tags = propsString.split(",").map(\s -> s.trim()).toList()
      var validValues = ResourceTags.values().map( \ elt -> elt.name())
      for (tag in tags) {
        if (!tag.contains("/")) {
          Assert.assertTrue("Invalid tag " + tag, validValues.contains(tag))
        }
      }
    }
    if (tags.contains(ResourceTags.JAVA.name()) && JavaClassFile.isJavaClass(cleanText)) {
      return new JavaClassFile(text)
    } else if (tags.contains(ResourceTags.JAVA.name()) && JavaInterfaceFile.isJavaInterface(cleanText)) {
      return new JavaInterfaceFile(text)
    } else if (tags.contains(ResourceTags.JAVA.name()) && JavaEnumFile.isEnum(cleanText)) {
      return new JavaEnumFile(text)
    } else if (GosuAnnotationFile.isGosuAnnotation(cleanText)) {
      return new GosuAnnotationFile(text)
    } else if (GosuClassFile.isClass(cleanText)) {
      return new GosuClassFile(text)
    } else if (GosuInterfaceFile.isInterface(cleanText)) {
      return new GosuInterfaceFile(text)
    } else if (GosuEnumFile.isEnum(cleanText)) {
      return new GosuEnumFile(text)
    } else if (GosuEnhancementFile.isEnhancement(cleanText)) {
      return new GosuEnhancementFile(text)
    } else if (tags.contains(ResourceTags.PROGRAM.name())) {
      tags.remove(ResourceTags.PROGRAM.name())
      return new GosuProgramFile(tags.single() + ".gsp", uncommentedText)
    } else if (tags.contains(ResourceTags.TEMPLATE.name())) {
      tags.remove(ResourceTags.TEMPLATE.name())
      return new GosuTemplateFile(tags.single() + ".gst", uncommentedText)
    } else if (JavaAnnotationFile.isJavaAnnotation(cleanText)) {
      return new JavaAnnotationFile(text)
    } else {
      throw new RuntimeException("Cannot derive resource type from text: " + text);
    }
  }

  static function createFile(test: GosuTestCase, text: String): PsiFile {
    var resource = create(text)
    return test.configureByText(resource.fileName, resource.content)
  }

  static function createFile(test: GosuTestCase, module: String, text: String): PsiFile {
    var resource = create(text)
    return test.configureByText(module, resource.fileName, resource.content)
  }

}