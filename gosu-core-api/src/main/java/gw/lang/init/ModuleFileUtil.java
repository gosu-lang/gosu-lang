/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.lang.UnstableAPI;
import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import gw.xml.simple.SimpleXmlNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class ModuleFileUtil {

  /**
   * Reads a pom.xml file into a GosuPathEntry object
   *
   * @param moduleFile the pom.xml file to convert to GosuPathEntry
   * @return an ordered list of GosuPathEntries created based on the algorithm described above
   */
  public static GosuPathEntry createPathEntryForModuleFile(IFile moduleFile) {
    try {
      InputStream is = moduleFile.openInputStream();
      try {
        SimpleXmlNode moduleNode = SimpleXmlNode.parse(is);
        IDirectory rootDir = moduleFile.getParent();

        List<IDirectory> sourceDirs = new ArrayList<IDirectory>();
        for (String child : new String[] { "gsrc", "gtest" }) {
          IDirectory dir = rootDir.dir(child);
          if (dir.exists()) {
            sourceDirs.add(dir);
          }
        }
        return new GosuPathEntry(rootDir, sourceDirs);
      } finally {
        is.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
