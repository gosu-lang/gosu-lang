/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.loader;

import gw.internal.gosu.compiler.GosuClassLoader;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.module.TypeSystemLockHelper;

/**
 * FIXME: Duplicates {@link gw.internal.gosu.compiler.protocols.gosuclass.GosuClassesUrlConnection}
 */
public class Loader {
  private static final String[] JAVA_NAMESPACES_TO_IGNORE = {
          "java.", "javax.", "sun."
  };

  public static Object getBytesOrClass(String strType) {
    ICompilableType type = null;

    if (!ignoreJavaClass(strType)) {
      strType = strType.replace('$', '.');
      type = maybeAssignGosuType(strType);
    }

    if (type != null) {
      return GosuClassLoader.instance().getBytes(type);
    }

    return null;
  }

  private static ICompilableType maybeAssignGosuType(String strType) {
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    TypeSystemLockHelper.getTypeSystemLockWithMonitor(loader);
    try {
      IType type = TypeSystem.getByFullNameIfValid(strType);
      if (type instanceof ICompilableType) {
        return (ICompilableType) type;
      }
    } finally {
      TypeSystem.unlock();
    }
    return null;
  }

  private static boolean ignoreJavaClass(String strClass) {
    for (String namespace : JAVA_NAMESPACES_TO_IGNORE) {
      if (strClass.startsWith(namespace)) {
        return true;
      }
    }
    return false;
  }
}
