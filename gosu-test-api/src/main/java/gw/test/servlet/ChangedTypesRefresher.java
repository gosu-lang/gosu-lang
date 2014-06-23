/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.servlet;

import gw.classredefiner.ClassRedefinerAgent;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.watcher.DirectoryWatcher;
import gw.fs.watcher.DirectoryWatcher.FileEvent;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UnstableAPI
public class ChangedTypesRefresher {

  private DirectoryWatcher _directoryWatcher;
  private static ChangedTypesRefresher _instance = new ChangedTypesRefresher();

  public static ChangedTypesRefresher getInstance() {
    return _instance;
  }

  public void initWatching() {
    _directoryWatcher = new DirectoryWatcher();
    List<? extends IDirectory> sourceEntries = TypeSystem.getCurrentModule().getSourcePath();
    for (IDirectory sourceEntry : sourceEntries) {
      // Ignore /classes directories and jar files
      if (!sourceEntry.getPath().getName().equals("classes") && !sourceEntry.getPath().getName().endsWith(".jar") && sourceEntry.isJavaFile()) {
        _directoryWatcher.watchDirectoryTree(sourceEntry.toJavaFile().toPath());
      }
    }
  }

  public void reloadChangedTypes() {
    // Assume that the first time this is called, nothing will have changed that we care about
    if (_directoryWatcher == null) {
      initWatching();
    } else {
      Map<Path, FileEvent> changes = _directoryWatcher.getChangesSinceLastTime();
      if (changes.isEmpty()) {
        return;
      }

      boolean canRefreshSelectively = true;
      Set<String> changedGosuTypes = new HashSet<String>();
      for (Map.Entry<Path, FileEvent> change : changes.entrySet()) {
        Path changedFilePath = change.getKey();
        FileEvent changeType = change.getValue();
        String fileName = changedFilePath.getFileName().toString();

        // Only worry about .gs or .gsx files for now
        if (fileName.endsWith(".gs") || fileName.endsWith(".gsx")) {
          // If a file was added, it'll get picked up anyway.  If it was deleted, there should be no remaining
          // references to it.  So only worry about modifications.
          if (changeType == FileEvent.MODIFY) {
            IFile changedFile = CommonServices.getFileSystem().getIFile(changedFilePath.toFile());
            for (IDirectory possibleSourceDir : TypeSystem.getCurrentModule().getSourcePath()) {
              if (changedFile.isDescendantOf(possibleSourceDir)) {
                String relativeFilePath = possibleSourceDir.getPath().relativePath(changedFile.getPath(), "/");
                String typeName = relativeFilePath.replace('/', '.');
                typeName = typeName.substring(0, typeName.lastIndexOf("."));
                changedGosuTypes.add(typeName);
                System.out.println("DEBUG: Found changes to " + typeName);
              }
            }
          }
        } else if (fileName.endsWith(".pcf") || fileName.endsWith(".xsd") || fileName.endsWith(".wsdl")){
          // Force a full refresh
          canRefreshSelectively = false;
        } else {
          // Assume it's a directory, and just ignore it:  we'll have specific file changes for the files within it anyway
        }
      }

      // TODO - AHK - We may need some system flag here
      boolean shouldReloadBytecode = true;
      List<TypeClassPair> changedGosuClasses = new ArrayList<TypeClassPair>();
      if (shouldReloadBytecode) {
        // For each changed Gosu type, see if it needs to be swapped.  Note that we do this before any type system refresh
        // happens, since we need to see if the class needs to be swapped
        for (String typeName : changedGosuTypes) {
          try {
            IType type = TypeSystem.getByFullName(typeName);
            if (type instanceof IGosuClass) {
              List<IGosuClass> allInnerClasses = new ArrayList<IGosuClass>();
              addInnerClassTreeToList((IGosuClass) type, allInnerClasses);
              for (IGosuClass gosuClass : allInnerClasses) {
                changedGosuClasses.add(new TypeClassPair(gosuClass, gosuClass.getBackingClass()));
              }
            }
          } catch (Exception e) {
            // Hmm . . . just swallow it for now and keep going
            System.out.println("WARN: Exception during reload of " + typeName);
            e.printStackTrace();
          }
        }
      }

      if (canRefreshSelectively && changedGosuTypes.size() < 20) {
        refreshSpecifiedFiles(changedGosuTypes);
      } else {
        System.out.println("DEBUG: Refreshing all types");
        TypeSystem.refresh(false);
      }

      if (shouldReloadBytecode) {
        reloadBytecodeForClasses(changedGosuClasses);
      }
    }
  }

  private class TypeClassPair {
    private IGosuClass _gsClass;
    private Class _javaClass;

    private TypeClassPair(IGosuClass gsClass, Class javaClass) {
      _gsClass = gsClass;
      _javaClass = javaClass;
    }
  }

  private void reloadBytecodeForClasses(List<TypeClassPair> changedGosuClasses) {

    TypeSystem.lock();
    try {
      ClassDefinition[] classDefinitions = new ClassDefinition[changedGosuClasses.size()];
      for (int i = 0; i < changedGosuClasses.size(); i++) {
        IGosuClass gsClass = changedGosuClasses.get(i)._gsClass;
        System.out.println("DEBUG: Recompiling " + gsClass.getName());
        byte[] bytes = TypeSystem.getGosuClassLoader().getBytes(gsClass);
        classDefinitions[i] = new ClassDefinition(changedGosuClasses.get(i)._javaClass, bytes);
      }

      try {
        ClassRedefinerAgent.redefineClasses(classDefinitions);
      } catch (ClassNotFoundException e) {
        System.out.println("ERROR: Class redefinition failed:");
        e.printStackTrace();
      } catch (UnmodifiableClassException e) {
        System.out.println("ERROR: Class redefinition failed:");
        e.printStackTrace();
      }
    } finally {
      TypeSystem.unlock();
    }
  }

  private void addInnerClassTreeToList(IGosuClass gosuClass, List<IGosuClass> classes) {
    classes.add(gosuClass);
    for (IGosuClass innerClass : gosuClass.getInnerClasses()) {
      addInnerClassTreeToList(innerClass, classes);
    }
  }

  private void refreshSpecifiedFiles(Set<String> types) {
    // TODO - AHK - Bail out and refresh everything if it doesn't work out?
    for (String typeName : types) {
      try {
        IType type = TypeSystem.getByFullName(typeName);
        TypeSystem.refresh((ITypeRef) type);
      } catch (Exception e) {
        System.out.println("Error refreshing " + typeName);
        e.printStackTrace();
      }

      System.out.println("DEBUG: Refreshing type " + typeName);
    }
  }
}
