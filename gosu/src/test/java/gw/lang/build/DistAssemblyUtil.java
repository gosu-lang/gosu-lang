/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.build;

import gw.test.util.ITCaseUtils;

import java.io.File;

/**
 */
public class DistAssemblyUtil extends ITCaseUtils {

  static DistAssemblyUtil _instance;
  static DistAssemblyUtil getInstance() {
    if (_instance == null) {
      _instance = new DistAssemblyUtil();
    }
    return _instance;
  }

  private File _pom;
  private File _assemblyDir;
  private File _assemblyBinDir;
  private File _assemblyLibDir;
  private File _assemblyExtDir;
  private String _gosuVersion;

  public DistAssemblyUtil() {
    _pom = findPom(getClass());
    _gosuVersion = getPomVersion(_pom);
    File targetDir = new File(_pom.getParent(), "target");
    if (!targetDir.exists()) {
      throw new IllegalStateException("probably haven't run 'package' yet");
    }
    _assemblyDir = new File(new File(targetDir, "gosu-" + _gosuVersion + "-full"), "gosu-" + _gosuVersion);
    _assemblyBinDir = new File(_assemblyDir, "bin");
    _assemblyLibDir = new File(_assemblyDir, "lib");
    _assemblyExtDir = new File(_assemblyDir, "ext");
    System.out.println("gosu version: " + _gosuVersion);
  }

  public File getPom() {
    return _pom;
  }

  public File getDir() {
    return _assemblyDir;
  }

  public File getBinDir() {
    return _assemblyBinDir;
  }

  public File getLibDir() {
    return _assemblyLibDir;
  }

  public File getExtDir() {
    return _assemblyExtDir;
  }

  public String getGosuVersion() {
    return _gosuVersion;
  }

  public File getJar(String name) {
    File dir;
    if (name.equals("gosu-xml") || name.equals("gosu-webservices")) {
      dir = _assemblyExtDir;
    }
    else {
      dir = _assemblyLibDir;
    }
    return new File(dir, name + "-" + _gosuVersion + ".jar");
  }

}
