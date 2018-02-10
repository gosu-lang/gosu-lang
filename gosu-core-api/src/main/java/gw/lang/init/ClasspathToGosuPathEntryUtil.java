/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.config.CommonServices;
import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UnstableAPI
public class ClasspathToGosuPathEntryUtil {
  static final String GW_FORBID_GOSU_JARS = "gw.forbid.gosu.jars";

  /**
   * Converts a set of Files into a list of GosuPathEntries.  The algorithm is as follows.  For each File in the list,
   * if that file is a directory or a jar file, see if there's a module.xml file in the root.  If so, parse that file
   * and add in a GosuPathEntry based on the module definition.  If not, if the file is a directory, look for
   * a module.xml file in the parent directory.  If such a file exists, add in a GosuPathEntry based on that file.
   * If no module.xml file has been found in either case, add a GosuPath entry for the directory or jar file using
   * the directory or jar as the root and as the only source directory, and with an empty list of custom typeloaders.
   * If the file is not a directory and not a jar file, ignore it entirely.
   *
   * In all cases, duplicate entries are ignored; if a GosuPathEntry is created previously for a File and the root
   * directory for that previous path entry is equal to or an ancestor of the root directory of the new path entry,
   * the new path entry is not added to the list.
   *
   * @param classpath the list of Files to turn into GosuPathEntries
   * @return an ordered list of GosuPathEntries created based on the algorithm described above
   */
  public static List<GosuPathEntry> convertClasspathToGosuPathEntries(List<File> classpath) {
    // this is a hack to prevent loading of gosu directly from jar files in Diamond,
    // which prevents people from just embedding it in src and thus possibly having ND problems
    // it can be removed once "modules" are figured out in Emerald
    boolean loadingGosuFromJarsForbidden = Boolean.getBoolean(GW_FORBID_GOSU_JARS);
    ClassPathToGosuPathConverterBlock pathConverterBlock = new ClassPathToGosuPathConverterBlock();
    for (File f : classpath) {
      IDirectory dir = CommonServices.getFileSystem().getIDirectory(f);
      if (!f.getName().endsWith(".jar") || !loadingGosuFromJarsForbidden) {
        executeOnSourceDirectory(dir, pathConverterBlock);
      } else {
        // Ignore anything that's not a directory or jar file
      }      
    }
    return (List<GosuPathEntry>) pathConverterBlock.getPathEntries();
  }

  public static IDirectory findModuleRootFromSourceEntry(IDirectory dir){
    ModuleFindFromSourceEntryBlock block = new ModuleFindFromSourceEntryBlock();
    executeOnSourceDirectory(dir, block);
    return block.getModuleDir();
  }

  private static IDirectory executeOnSourceDirectory(IDirectory dir, SourceDirectoryBlock block){
    IFile moduleFile = dir.file("pom.xml");
    IDirectory foundModule = null;
      if (moduleFile != null && moduleFile.exists()) {
        // This entry is itself a module, so return that
        block.doIt(dir, moduleFile);
      } else if (!dir.getName().endsWith(".jar")) {
        IDirectory parentDir = dir.getParent();
        if (parentDir != null) {
          IFile parentModuleFile = parentDir.file("pom.xml");
          if (parentModuleFile.exists() && moduleContainsSourceDir(parentModuleFile, dir)) {
            // Module.xml file in the parent directory, so that directory is a module
            block.doIt(parentDir, parentModuleFile);
          } else {
            // No module.xml file in the parent directory either, the original directory is the module
            block.doIt(dir, null);
          }
        } else {
          // No parent directory at all, so just the original directory is the module
          block.doIt(dir, null);
        }
      } else {
        // It's a jar file with no module.xml inside, so the jar is the module
        block.doIt(dir, null);
      }
    return foundModule;
  }

  private static boolean moduleContainsSourceDir(IFile moduleFile, IDirectory dir) {
    for (IDirectory iDirectory : ModuleFileUtil.createPathEntryForModuleFile(moduleFile).getSources()) {
      if(iDirectory.equals(dir)){
        return true;
      }
    }
    return false;
  }

  private interface SourceDirectoryBlock {
    void doIt(IDirectory dir, IFile moduleFile);
  }

  private static class ModuleFindFromSourceEntryBlock implements SourceDirectoryBlock {
    public IDirectory _moduleDir;

    @Override
    public void doIt(IDirectory dir, IFile moduleFile) {
      _moduleDir = dir;
    }

    public IDirectory getModuleDir(){
      return _moduleDir;
    }
  }

  private static class ClassPathToGosuPathConverterBlock implements SourceDirectoryBlock {
    private final List<GosuPathEntry> _pathEntries;

    public ClassPathToGosuPathConverterBlock() {
      _pathEntries = new ArrayList<GosuPathEntry>();
    }

    @Override
    public void doIt(IDirectory dir, IFile moduleFile) {
      if (!moduleAlreadyIncluded(dir, _pathEntries)) {
        if (moduleFile == null) {
          _pathEntries.add(new GosuPathEntry(dir, Collections.singletonList(dir)));
        } else {
          _pathEntries.add(GosuShop.createPathEntryFromModuleFile(moduleFile));
        }
      }
    }

    public List<? extends GosuPathEntry> getPathEntries() {
      return _pathEntries;
    }

    private boolean moduleAlreadyIncluded(IDirectory rootDir, List<GosuPathEntry> pathEntries) {
      for (GosuPathEntry entry : pathEntries) {
        if (rootDir.equals(entry.getRoot()) || rootDir.isDescendantOf(entry.getRoot())) {
          return true;
        }
      }

      return false;
    }
  }
}
