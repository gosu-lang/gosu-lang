/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

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


public class JavaSourceUtil {

  public static IJavaClassInfo getClassInfo( AsmClass cls ) {
    if( isProxy( cls ) ) {
      return getJavaClassInfo( cls );
    } else {
      if( !ExecutionMode.isIDE() ) { //## todo: refine this so that a compiler, for example, could cross-compile java source with gosu source
        // Don't try to load from source unless we have to, this saves a load of time esp. for case
        // where we're loading an inner java class where replacing the '$' below with '.' we bascially
        // put the type system through a load of unnecessary work.
        IJavaClassInfo classInfo = getJavaClassInfo( cls );
        if( classInfo != null ) {
          return classInfo;
        }
      }
      return getClassInfo( cls.getName().replace( '$', '.' ) );
    }
  }

  private static IJavaClassInfo getJavaClassInfo( AsmClass asmClass ) {
    DefaultTypeLoader defaultTypeLoader = (DefaultTypeLoader)TypeSystem.getModule().getModuleTypeLoader().getDefaultTypeLoader();
    if( defaultTypeLoader != null ) {
      return defaultTypeLoader.getJavaClassInfo( asmClass );
    }
    return null;
  }

  public static IJavaClassInfo getClassInfo(Class aClass) {
    DefaultTypeLoader loader = (DefaultTypeLoader) TypeSystem.getModule().getModuleTypeLoader().getDefaultTypeLoader();
    if (isProxy(aClass)) {
      return loader.getJavaClassInfo( aClass );
    } else if (aClass.isArray()) {
      return loader.getJavaClassInfo(aClass);
    } else {
      if( ExecutionMode.isRuntime() ) {
        // Don't try to load from source unless we have to, this saves a load of time esp. for case
        // where we're loading an inner java class where replacing the '$' below with '.' we bascially
        // put the type system through a load of unnecessary work.
        IJavaClassInfo javaClassInfo = loader.getJavaClassInfo( aClass );
        if (javaClassInfo != null) {
          return javaClassInfo;
        }
      }
      return getClassInfo(aClass.getName().replace('$', '.'));
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

  public static IJavaClassInfo getClassInfo(String qualifiedName) {
    IDefaultTypeLoader loader = TypeSystem.getModule().getModuleTypeLoader().getDefaultTypeLoader();
    if (loader != null) {
      return loader.getJavaClassInfo(qualifiedName);
    }
    return null;
  }

  public static ImplicitPropertyUtil.ImplicitPropertyInfo getImplicitProperty( IJavaClassMethod method, boolean simplePropertyProcessing )
  {
    IJavaClassType returnType = method.getGenericReturnType();
    if( returnType != null )
    {
      String returnTypeName = returnType.getName();
      int argCount = method.getParameterTypes().length;
      String name = method.getName();
      if( argCount == 0 && !returnTypeName.equals( "void" ) )
      {
        if( isGetterName( name, ImplicitPropertyUtil.GET, simplePropertyProcessing ) )
        {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo( false, true, ImplicitPropertyUtil.capitalizeFirstChar( name.substring( 3 ), simplePropertyProcessing ) );
        }
        else if( isGetterName( name, ImplicitPropertyUtil.IS, simplePropertyProcessing) &&
                 (returnTypeName.equals( "boolean" ) || returnTypeName.equals( "java.lang.Boolean" )) )
        {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo( false, true, ImplicitPropertyUtil.capitalizeFirstChar( name.substring( 2 ), simplePropertyProcessing ) );
        }
      }
      else if( argCount == 1 )
      {
        if( isSetterName( returnTypeName, name, simplePropertyProcessing ) )
        {
          return new ImplicitPropertyUtil.ImplicitPropertyInfo( true, false, ImplicitPropertyUtil.capitalizeFirstChar( name.substring( 3 ), simplePropertyProcessing ) );
        }
      }
    }
    return null;
  }

  private static boolean isSetterName( String returnTypeName, String name, boolean simplePropertyProcessing )
  {
    if( !returnTypeName.equals( "void" ) || !name.startsWith( ImplicitPropertyUtil.SET ) || name.length() <= ImplicitPropertyUtil.SET.length() )
    {
      return false;
    }

    if( simplePropertyProcessing )
    {
      return true;
    }
    
    char firstChar = name.charAt( ImplicitPropertyUtil.SET.length() );
    return firstChar == Character.toUpperCase( firstChar );
  }

  private static boolean isGetterName( String name, String prefix, boolean simplePropertyProcessing )
  {
    if( !name.startsWith( prefix ) || name.length() <= prefix.length() )
    {
      return false;
    }

    if( simplePropertyProcessing ) 
    {
      return true;
    }
    
    char firstChar = name.charAt( prefix.length() );
    return firstChar == Character.toUpperCase( firstChar );
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


