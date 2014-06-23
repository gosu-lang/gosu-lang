/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

import com.intellij.psi.PsiFile;
import gw.plugin.ij.framework.GosuTestCase;

import java.util.ArrayList;
import java.util.List;

public class ResourceFactory2 {

  public static GosuTestingResource create(String text) {
    String cleanText = GosuTestingResource.removeMarkers(text.trim());
    String uncommentedText = null;
    List<String> tags = new ArrayList<>();
    if (cleanText.startsWith("//")) {
      uncommentedText = text.substring(text.indexOf('\n') + 1, text.length());
      String propsString = cleanText.substring(2, cleanText.indexOf('\n')).trim();
      String[] split = propsString.split(",");
      for (String s : split) {
        tags.add(s.trim());
      }
      for (String tag : tags) {
        if (!tag.contains("/")) {
          ResourceTags.valueOf(tag);
        }
      }
    }
//    if (tags.contains(ResourceTags.JAVA.name()) && JavaClassFile.isJavaClass(cleanText)) {
//      return new JavaClassFile(text);
//    } else if (tags.contains(ResourceTags.JAVA.name()) && JavaInterfaceFile.isJavaInterface(cleanText)) {
//      return new JavaInterfaceFile(text);
//    } else if (tags.contains(ResourceTags.JAVA.name()) && JavaEnumFile.isEnum(cleanText)) {
//      return new JavaEnumFile(text);
//    } else if (GosuAnnotationFile.isGosuAnnotation(cleanText)) {
//      return new GosuAnnotationFile(text);
//    } else
    if (GosuClassFile.isClass(cleanText)) {
      return new GosuClassFile(text);
//    } else if (GosuInterfaceFile.isInterface(cleanText)) {
//      return new GosuInterfaceFile(text);
//    } else if (GosuEnumFile.isEnum(cleanText)) {
//      return new GosuEnumFile(text);
//    } else if (GosuEnhancementFile.isEnhancement(cleanText)) {
//      return new GosuEnhancementFile(text);
//    } else if (tags.contains(ResourceTags.PROGRAM.name())) {
//      tags.remove(ResourceTags.PROGRAM.name());
//      return new GosuProgramFile(tags.single() + ".gsp", uncommentedText);
//    } else if (tags.contains(ResourceTags.TEMPLATE.name())) {
//      tags.remove(ResourceTags.TEMPLATE.name());
//      return new GosuTemplateFile(tags.single() + ".gst", uncommentedText);
//    } else if (JavaAnnotationFile.isJavaAnnotation(cleanText)) {
//      return new JavaAnnotationFile(text);
    } else {
      throw new RuntimeException("Cannot derive resource type from text: " + text);
    }
  }

  static PsiFile createFile(GosuTestCase test, String text) {
    GosuTestingResource resource = create(text);
    return test.configureByText(resource.fileName, resource.content);
  }

  static PsiFile createFile(GosuTestCase test, String module, String text) {
    GosuTestingResource resource = create(text);
    return test.configureByText(module, resource.fileName, resource.content);
  }

}