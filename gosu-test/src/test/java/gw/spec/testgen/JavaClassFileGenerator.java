/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 28, 2009
 * Time: 11:29:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class JavaClassFileGenerator extends ClassFileGenerator {

  public JavaClassFileGenerator(String className, String packageName) {
    super(className, packageName);
  }

  @Override
  public JavaClassFileGenerator getOrCreateInnerClass(String innerClass) {
    return (JavaClassFileGenerator) super.getOrCreateInnerClass(innerClass);
  }

  @Override
  protected ClassFileGenerator createInnerClassGenerator(String className, String packageName) {
    return new JavaClassFileGenerator(className, packageName);
  }

  @Override
  protected String getClassFileSuffix() {
    return "java";
  }

  public StringBuilder generateClassText(int indent) {
    StringBuilder sb = new StringBuilder();
    if (indent == 0) {
      sb.append("package ").append(_package).append(";\n");
      for (String usesStatement : _usesStatements) {
        // TODO - AHK - Also pull up uses statements from inner classes?
        sb.append("import ").append(usesStatement).append(";\n");
      }
      sb.append("\n");
    }
    addIndent(sb, indent);
    if (_static) {
      sb.append("static ");
    }
    if (_interface) {
      sb.append("public interface ").append(_className);
    } else {
      sb.append("public class ").append(_className);
    }
    if (_superClass != null) {
      sb.append(" extends ").append(_superClass);
    }
    sb.append(" {\n\n");
    appendDeclarations(_memberDeclarations, indent, sb);
    if (!_memberDeclarations.isEmpty()) {
      sb.append("\n\n");
    }
    appendDeclarations(_memberFunctions, indent, sb);

    for (ClassFileGenerator innerClass : _innerClasses.values()) {
      sb.append(innerClass.generateClassText(indent + 2));
      sb.append("\n");
    }
    addIndent(sb, indent);
    sb.append("}");
    return sb;
  }

  private void appendDeclarations(List<String> declarations, int indent, StringBuilder sb) {
    for (String memberDeclaration : declarations) {
      String[] parts = memberDeclaration.split("\n");
      addIndent(sb, indent + 2);
      sb.append(parts[0]).append("\n");
      for (int i = 1; i < parts.length -1; i++) {
        addIndent(sb, indent + 2);
        sb.append(parts[i]).append("\n");
      }
      if (parts.length > 1) {
        addIndent(sb, indent + 2);
        sb.append(parts[parts.length - 1]).append("\n\n");
      }
    }
  }

}