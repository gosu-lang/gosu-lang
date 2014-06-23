/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:07:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class CodeGenerationContext {
  private Map<String, ClassFileGenerator> _filesToGenerate = new HashMap<String, ClassFileGenerator>();
  private String _outputPackage;
  private ClassFileGenerator _currentTestGenerator;

  public CodeGenerationContext(String outputPackage) {
    _outputPackage = outputPackage;
  }

  public ClassFileGenerator getClassFileGenerator(String className) {
    ClassFileGenerator cfg = _filesToGenerate.get(className);
    if (cfg == null) {
      throw new IllegalArgumentException("No class named " + className);
    }
    return cfg;
  }

  public GosuClassFileGenerator createGosuClassFileGenerator(String className) {
    if (_filesToGenerate.containsKey(className)) {
      throw new IllegalArgumentException("Already have a class named " + className);
    }
    GosuClassFileGenerator cfg = new GosuClassFileGenerator(className, _outputPackage);
    _filesToGenerate.put(className, cfg);
    return cfg;
  }

  public JavaClassFileGenerator createJavaClassFileGenerator(String className) {
    if (_filesToGenerate.containsKey(className)) {
      throw new IllegalArgumentException("Already have a class named " + className);
    }
    JavaClassFileGenerator cfg = new JavaClassFileGenerator(className, _outputPackage);
    _filesToGenerate.put(className, cfg);
    return cfg;
  }

  public void writeAllClassFiles(File rootDir) throws IOException {
    for (ClassFileGenerator cfg : _filesToGenerate.values()) {
      cfg.writeClassToDisk(rootDir);
    }
  }

  public ClassFileGenerator getCurrentTestGenerator() {
    return _currentTestGenerator;
  }

  public void setCurrentTestGenerator(ClassFileGenerator currentTestGenerator) {
    _currentTestGenerator = currentTestGenerator;
  }
}
