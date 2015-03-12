/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.internal.gosu.parser.DefaultTypeLoader;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.reflect.ImplicitPropertyUtil;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.module.IModule;


public class JavaSourceUtil {

  public static IJavaClassInfo getClassInfo( AsmClass cls, IModule module ) {
    if( isProxy( cls ) ) {
      return getJavaClassInfo( cls, module );
    } else {
      if( !ExecutionMode.isIDE() ) {
        // Don't try to load from source unless we have to, this saves a load of time esp. for case
        // where we're loading an inner java class where replacing the '$' below with '.' we bascially
        // put the type system through a load of unnecessary work.
        IJavaClassInfo classInfo = getJavaClassInfo( cls, module );
        if( classInfo != null ) {
          return classInfo;
        }
      }
      return getClassInfo( cls.getName().replace( '$', '.' ), module );
    }
  }

  private static IJavaClassInfo getJavaClassInfo( AsmClass asmClass, IModule module ) {
    for( IModule m : module.getModuleTraversalList() ) {
      TypeSystem.pushModule( m );
      try {
        DefaultTypeLoader defaultTypeLoader = (DefaultTypeLoader)m.getModuleTypeLoader().getDefaultTypeLoader();
        if( defaultTypeLoader != null ) {
          IJavaClassInfo javaClassInfo = defaultTypeLoader.getJavaClassInfo( asmClass, module );
          if( javaClassInfo != null ) {
            return javaClassInfo;
          }
        }
      }
      finally {
        TypeSystem.popModule( m );
      }
    }
    return null;
  }

  public static IJavaClassInfo getClassInfo(Class aClass, IModule gosuModule) {
    DefaultTypeLoader loader = (DefaultTypeLoader) gosuModule.getModuleTypeLoader().getDefaultTypeLoader();
    if (isProxy(aClass)) {
      return loader.getJavaClassInfo( aClass, gosuModule );
    } else if (aClass.isArray()) {
      IJavaClassInfo classInfo = getClassInfo(aClass.getComponentType(), gosuModule);
      IModule module = classInfo.getModule();
      return loader.getJavaClassInfo(aClass, module);
    } else {
      if( !ExecutionMode.isIDE() ) {
        // Don't try to load from source unless we have to, this saves a load of time esp. for case
        // where we're loading an inner java class where replacing the '$' below with '.' we bascially
        // put the type system through a load of unnecessary work.
        IJavaClassInfo javaClassInfo = loader.getJavaClassInfo( aClass, gosuModule );
        if (javaClassInfo != null) {
          return javaClassInfo;
        }
      }
      return getClassInfo(aClass.getName().replace('$', '.'), gosuModule);
    }
  }

  private static boolean isProxy(AsmClass aClass) {
    String name = aClass.getName();
    return
        name.endsWith(ITypeRefFactory.USER_PROXY_SUFFIX) ||
        name.endsWith(ITypeRefFactory.SYSTEM_PROXY_SUFFIX);
  }

  private static boolean isProxy(Class aClass) {
    String name = aClass.getName();
    return
        name.endsWith(ITypeRefFactory.USER_PROXY_SUFFIX) ||
        name.endsWith(ITypeRefFactory.SYSTEM_PROXY_SUFFIX);
  }

  public static IJavaClassInfo getClassInfo(String qualifiedName, IModule gosuModule) {
    for (IModule module : gosuModule.getModuleTraversalList()) {
      IDefaultTypeLoader loader = module.getModuleTypeLoader().getDefaultTypeLoader();
      if (loader != null) {
        IJavaClassInfo javaClassInfo = loader.getJavaClassInfo(qualifiedName);
        if (javaClassInfo != null) {
          return javaClassInfo;
        }
      }
    }
    return null;
  }

  public static ImplicitPropertyUtil.ImplicitPropertyInfo getImplicitProperty(IJavaClassMethod method, boolean simplePropertyProcessing) {
    IJavaClassType returnType = method.getGenericReturnType();
    if (returnType != null) {
      String returnTypeName = returnType.getName();
      int argCount = method.getParameterTypes().length;
      String name = method.getName();
      if (argCount == 0 && !returnTypeName.equals("void")) {
        if (name.startsWith(ImplicitPropertyUtil.GET) && (name.length() > ImplicitPropertyUtil.GET.length())) {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo(false, true, ImplicitPropertyUtil.capitalizeFirstChar(name.substring(3), simplePropertyProcessing));
        } else if (name.startsWith(ImplicitPropertyUtil.IS) &&
            (name.length() > ImplicitPropertyUtil.IS.length()) &&
            (returnTypeName.equals("boolean") || returnTypeName.equals("java.lang.Boolean"))) {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo(false, true, ImplicitPropertyUtil.capitalizeFirstChar(name.substring(2), simplePropertyProcessing));
        }
      } else if (argCount == 1) {
        if (returnTypeName.equals("void") && name.startsWith(ImplicitPropertyUtil.SET) && (name.length() > ImplicitPropertyUtil.SET.length())) {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo(true, false, ImplicitPropertyUtil.capitalizeFirstChar(name.substring(3), simplePropertyProcessing));
        }
      }
    }
    return null;
  }

  public static IJavaClassType resolveInnerClass(IJavaClassInfo rootType, String innerName, IJavaClassInfo whosAskin) {
    for (IJavaClassInfo innerCls : rootType.getDeclaredClasses()) {
      if (innerName.equals(innerCls.getSimpleName()) && isVisible(rootType, innerCls, whosAskin)) {
        return innerCls;
      }
    }
    return null;
  }

  private static boolean isVisible(IJavaClassInfo rootType, IJavaClassInfo innerClass, IJavaClassInfo whosAskin) {
    if ((whosAskin == rootType) || Modifier.isPublic(innerClass.getModifiers()) || (isEnclosed(rootType, whosAskin))) {
      return true;
    }
    if( innerClass.isProtected() && isDescendant(rootType, whosAskin) ) {
      return true;
    }
    if (whosAskin.getNamespace().equals(rootType.getNamespace()) && innerClass.isInternal()) {
      return true;
    }
    return false;
  }

  public static boolean isEnclosed(IJavaClassInfo enclosingClass, IJavaClassInfo nestedClass) {
    IJavaClassInfo enclosingType = nestedClass.getEnclosingClass();
    if (enclosingType == null) {
      return false;
    }
    if (enclosingType == enclosingClass) {
      return true;
    }
    return isEnclosed(enclosingClass, enclosingType);
  }

  public static boolean isDescendant(IJavaClassInfo ancestorClassInfo, IJavaClassInfo descendantClassInfo) {
    IJavaClassInfo superclass = descendantClassInfo.getSuperclass();
    if (superclass != null) {
      if (superclass == ancestorClassInfo) {
        return true;
      }
      if (isDescendant(ancestorClassInfo, superclass)) {
        return true;
      }
    }
    for (IJavaClassInfo iface : descendantClassInfo.getInterfaces()) {
      if (iface == ancestorClassInfo) {
        return true;
      }
      if (isDescendant(ancestorClassInfo, iface)) {
        return true;
      }
    }
    return false;
  }

}


