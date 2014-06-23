/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.util;

import gw.fs.IResource;
import gw.xml.simple.SimpleXmlNode;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Brian Chang
 */
public class ITCaseUtils {

  public static List<String> toNamesSorted(List<? extends IResource> resources) {
    ArrayList<String> names = new ArrayList<String>();
    for (IResource resource : resources) {
      names.add(resource.getName());
    }
    Collections.sort(names);
    return names;
  }

  public static File findPom(Class clazz) {
    try {
      URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
      File classFile = new File(location.toURI());
      String fixedPath = classFile.getPath().replaceFirst("[\\\\/]projects[\\\\/][^\\\\/]+([\\\\/])", "$1");
      File root = findPomRoot(new File(fixedPath));
      return root != null ? new File(root, "pom.xml") : null;
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static File findPomRoot(File file) {
    if (file.isDirectory() && new File(file, "pom.xml").exists()) {
      return file;
    }
    if (file.getParentFile() != null) {
      return findPomRoot(file.getParentFile());
    }
    return null;
  }

  public static SimpleXmlNode getChild(SimpleXmlNode node, String nodeName) {
    for (SimpleXmlNode child : node.getChildren()) {
      if (nodeName.equals(child.getName())) {
        return child;
      }
    }
    return null;
  }

  public static String getPomVersion(File pom) {
    SimpleXmlNode node = SimpleXmlNode.parse(pom);
    SimpleXmlNode versionNode = getChild(node, "version");
    if (versionNode == null) {
      SimpleXmlNode parentNode = getChild(node, "parent");
      versionNode = getChild(parentNode, "version");
    }
    return versionNode != null ? versionNode.getText() : null;
  }
}
