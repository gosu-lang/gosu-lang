/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.fs.IFile;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DependencySink {
  private final Set<IFile> files = new HashSet<>();
  private final Set<String> namespaces = new HashSet<>();
  private final Set<String> displayKeys = new HashSet<>();

  public Set<IFile> getFiles() {
    return files;
  }

  public Set<String> getDisplayKeys() {
    return displayKeys;
  }

  public void addNamespace(String namespace) {
    namespaces.add(namespace);
  }

  public void addTypes(Set<? extends IType> types) {
    for (IType type : types) {
      addType(type);
    }
  }

  // Adding type
  public void addType(IType type) {
    if (type == null) {
      throw new IllegalArgumentException("Trying to add null type as a dependency");
    }

    // we don't care about these types
    if (type instanceof ITypeVariableType) {
      return;
    }

    // unwrap meta type
    if (type instanceof IMetaType) {
      type = ((IMetaType) type).getType();
    }

    if (type != TypeSystem.getErrorType() /*&& !(expression instanceof ITypeLiteralExpression && isTypeLiteralInAncestry(expression.getParent()))*/) {
      processType(type);
    }
  }

  private boolean isTypeLiteralInAncestry(IParsedElement expression) {
    if (expression == null) {
      return false;
    }
    if (expression instanceof ITypeLiteralExpression) {
      return true;
    }
    return isTypeLiteralInAncestry(expression.getParent());
  }

  private void processType(IType type) {
    //if (!FileUtil.getTypesForFile(file).contains(type.getName())) {
    // de-generify the type
    IType genericType = TypeSystem.getPureGenericType(type);
    if (genericType != null) {
      type = genericType;
    }

    if (type instanceof ICompoundType) {
      for (IType componentType : ((ICompoundType) type).getTypes()) {
        processType(componentType);
      }
    } else {
      // deproxy the type
      type = IGosuClass.ProxyUtil.getProxiedType(type);

      // get the outer-most enclosing type
      while (type.getEnclosingType() != null) {
        type = type.getEnclosingType();
      }

      // deproxy the type again
      type = IGosuClass.ProxyUtil.getProxiedType(type);

      //TODO-dp a Gosu class depends not only on concrete types but also on parameterized and array types, since they can be individually enhanced!
      while (type.isArray()) {
        type = type.getComponentType();
      }

      // deproxy the type not once, not twice, but thrice!
      type = IGosuClass.ProxyUtil.getProxiedType(type);

      files.addAll(getTypeFiles(type));
    }
  }

  public void addDisplayKey(String key) {
    displayKeys.add(key);
  }

  private List<IFile> getTypeFiles(IType type) {
    if (type == null) {
      throw new NullPointerException("Null type passes in.");
    }
    final ITypeLoader typeLoader = type.getTypeLoader();
    if (typeLoader == null) {
      return Collections.emptyList(); // E.g. namespace type
    }

    final IModule module = typeLoader.getModule();
    if (module instanceof IJreModule) {
      return Collections.emptyList(); // Type from JDK
    }

    List<IFile> result = Collections.EMPTY_LIST;
    if (type instanceof IFileBasedType) {
      result = new ArrayList<>();
      for (IFile file : ((IFileBasedType) type).getSourceFiles()) {
        if (!file.isInJar()) {
          result.add(file);
        }
      }
      }
    return result;
  }

}
