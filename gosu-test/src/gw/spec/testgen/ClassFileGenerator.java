/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import gw.util.StreamUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 28, 2009
 * Time: 11:29:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ClassFileGenerator {
  protected String _className;
  protected String _package;
  protected String _superClass;
  protected boolean _static = false;
  protected boolean _interface = false;
  protected TreeSet<String> _usesStatements = new TreeSet<String>();
  protected List<String> _implementedInterfaces = new ArrayList<String>();
  protected List<String> _memberDeclarations = new ArrayList<String>();
  protected List<String> _memberFunctions = new ArrayList<String>();
  protected Map<String, ClassFileGenerator> _innerClasses = new HashMap<String, ClassFileGenerator>();

  public ClassFileGenerator(String className, String packageName) {
    _className = className;
    _package = packageName;
  }

  public String getClassName() {
    return _className;
  }

  public String getPackage() {
    return _package;
  }

  public void setPackage(String aPackage) {
    _package = aPackage;
  }

  public void setStatic(boolean aStatic) {
    _static = aStatic;
  }

  public void setInterface(boolean anInterface) {
    _interface = anInterface;
  }

  public void setSuperClass(String superClass) {
    _superClass = superClass;
  }

  public void addUses(String usesStatement) {
    _usesStatements.add(usesStatement);
  }

  public void addImplementedInterface(String interfaceName) {
    _implementedInterfaces.add(interfaceName);
  }

  public void addMember(String memberDeclaration) {
    _memberDeclarations.add(memberDeclaration);
  }

  public void addFunction(String functionDeclaration) {
    _memberFunctions.add(functionDeclaration);
  }

  public ClassFileGenerator getOrCreateInnerClass(String innerClass) {
    ClassFileGenerator cfg = _innerClasses.get(innerClass);
    if (cfg == null) {
      cfg = createInnerClassGenerator(innerClass, _package);
      _innerClasses.put(innerClass, cfg);
    }
    return cfg;
  }

  protected abstract ClassFileGenerator createInnerClassGenerator(String className, String packageName);

  public abstract StringBuilder generateClassText(int indent);

  protected void addIndent(StringBuilder sb, int numSpaces) {
    for (int i = 0; i < numSpaces; i++) {
      sb.append(" ");
    }
  }

  public void writeClassToDisk(File rootDir) throws IOException {
    File parentDir = new File(rootDir, _package.replace('.', '\\'));
    parentDir.mkdirs();
    File testFile = new File(parentDir, _className + "." + getClassFileSuffix());
    Writer writer = StreamUtil.getOutputStreamWriter(new FileOutputStream(testFile));
    writer.write(generateClassText(0).toString());
    writer.flush();
    writer.close();
  }

  protected abstract String getClassFileSuffix();


  private String determineByteCodeTestClassName() {
    return _className.replace("Test", "ByteCodeTest");
  }

  private StringBuilder genByteCodeTestCode() {
    StringBuilder sb = new StringBuilder();
    sb.append("package " + _package + ";\n\n");
    sb.append("import gw.test.TestBase;\n\n");
    sb.append("public class ").append(determineByteCodeTestClassName()).append(" extends TestClass {\n");
    sb.append("  public ").append(determineByteCodeTestClassName()).append("() {\n");
    sb.append("    super(\"").append(_package).append(".").append(_className).append("\");\n");
    sb.append("  }\n");
    sb.append("}");
    return sb;
  }
}
