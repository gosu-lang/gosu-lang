/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.util.GosuClassUtil;
import gw.util.ILogger;
import gw.util.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Dec 6, 2010
 * Time: 4:29:46 PM
 * To change this template use File | Settings | File Templates.
 */
class TestClassFinder {
  private enum TestClassFindType {
    JAVA, GOSU
  }

  private final List<Predicate<IFile>> _iFileFilters;
  private final List<Predicate<String>> _packageFilters;
  private final List<String> _withPackages;
  private final List<Predicate<IType>> _typeFilters;

  public TestClassFinder(List<Predicate<IFile>> iFileFilters,
                         List<Predicate<String>> packageFilters,
                         List<String> withPackages,
                         List<Predicate<IType>> typeFilters) {
    _iFileFilters = iFileFilters;
    _packageFilters = packageFilters;
    _withPackages = withPackages;
    _typeFilters = typeFilters;
  }

  public TreeSet<TestSpec> findTests(List<IDirectory> gosuSourceRoots, List<IDirectory> javaClassRoots) {
    TreeSet<TestSpec> results = new TreeSet<TestSpec>();
    for (IDirectory dir : gosuSourceRoots) {
      for (IType type : findTestTypes(dir, TestClassFindType.GOSU)) {
        results.add(new TestSpec(type.getName()));
      }
    }

    for (IDirectory dir : javaClassRoots) {
      for (IType type : findTestTypes(dir, TestClassFindType.JAVA)) {
        results.add(new TestSpec(type.getName()));
      }
    }

    return results;
  }

  private List<IType> findTestTypes(IDirectory entry, TestClassFindType findType) {
    ArrayList<IType> types = new ArrayList<IType>();
    findTestTypesImpl(entry, entry, findType, types);
    return types;
  }

  private void findTestTypesImpl(IDirectory root, IDirectory entry, TestClassFindType findType, ArrayList<IType> types) {
    if (entry.exists()) {
      for (IFile iFile : entry.listFiles()) {
        possiblyAddTest(iFile, root, findType, types);
      }

      for (IDirectory subDir : entry.listDirs()) {
        findTestTypesImpl(root, subDir, findType, types);
      }
    }
  }

  private void possiblyAddTest(IFile entry, IDirectory root, TestClassFindType findType, ArrayList<IType> types) {
    if (shouldConsiderFile(entry, findType)) {
      String relativeFileName = root.relativePath(entry);
      String typeName = relativeFileName.replaceFirst("\\..*$", "").replace("/", ".");
      if (shouldConsiderTypeName(typeName)) {
        try {
          IType type = TypeSystem.getByFullName(typeName);
          if (shouldConsiderType(type)) {
            types.add(type);
          }
        } catch (Exception e) {
          //Log rather than warn, because invalid resources can exist
          ILogger logger = CommonServices.getEntityAccess().getLogger();
          logger.warn("Could not load type " + typeName);

          // TODO - AHK
          //      if (isLoggingErrors()) {
          //        logError("Couldn't load ");
          //        logError(GosuExceptionUtil.getStackTraceAsString(e));
          //      }
        }
      }
    }
  }

  private boolean shouldConsiderFile(IFile entry, TestClassFindType findType) {
    // Skip everything other than Java and Gosu classes that end in "Test", depending on the find type
    if (findType == TestClassFindType.JAVA) {
      if (!entry.getName().endsWith("Test.class")) {
        return false;
      }
    } else if (findType == TestClassFindType.GOSU) {
      if (!entry.getName().endsWith("Test.gs")) {
        return false;
      }
    } else {
      throw new IllegalArgumentException("Unexpected find type " + findType);
    }

    for (Predicate<IFile> fileFilter : _iFileFilters) {
      if (!fileFilter.evaluate(entry)) {
        return false;
      }
    }

    // If it passed through all the filters, then we don't want to skip the type
    return true;
  }

  private boolean shouldConsiderTypeName(String typeName) {
    String pkg = GosuClassUtil.getPackage(typeName);
    return matchesPackageFilters(pkg) && matchesIncludedPackages(pkg);
  }

  private boolean matchesPackageFilters(String packageName) {
    for (Predicate<String> packageFilter : _packageFilters) {
      if (!packageFilter.evaluate(packageName)) {
        return false;
      }
    }

    return true;
  }

  private boolean matchesIncludedPackages(String packageName) {
    if (_withPackages.isEmpty()) {
      return true;
    }

    for (String withPackage : _withPackages) {
      if (packageName.startsWith(withPackage)) {
        return true;
      }
    }

    return false;
  }

  private boolean shouldConsiderType(IType type) {
    if (type.isAbstract() || !TypeSystem.get(TestClass.class).isAssignableFrom(type)) {
      return false;
    }

    for (Predicate<IType> typeFilter : _typeFilters) {
      if (!typeFilter.evaluate(type)) {
        return false;
      }
    }

    return true;
  }
}
