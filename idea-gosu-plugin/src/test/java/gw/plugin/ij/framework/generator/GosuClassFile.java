/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

public class GosuClassFile extends GosuTestingResource {

  public GosuClassFile(String usesList, String packageName, String className, String classContent) {
    super(packageName.replace('.', '/') + "/" + className + ".gs",
        "package " + packageName + "\n" +
            usesList + "\n" +
            "class " + className + " {\n" +
            classContent + "\n" +
            "}"
    );
  }

  public GosuClassFile(String classContent) {
    super(getFileName(classContent), classContent);
  }

  public static boolean isClass(String text) {
    text = removeMarkers(text);
    return text.contains("package") && text.contains("class");
  }

  static String getFileName(String text) {
    text = removeMarkers(text);
    int i1 = text.indexOf("package ") + 8;
    int i2 = wordEnd(text, i1);
    String pkg = text.substring(i1, i2).trim();
    i1 = text.indexOf("class ") + 5;
    while (text.charAt(i1) == ' ') i1++;
    i2 = wordEnd(text, i1);
    String cls = text.substring(i1, i2).trim();
    return pkg.replace('.', '/') + "/" + cls + ".gs";
  }
}
