/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public abstract class AbstractJavaClassInfo extends JavaSourceElement implements IJavaClassInfo {
  
  @Override
  public boolean isAssignableFrom(IJavaClassInfo that) {
    return isAssignableFrom(this, that);
  }

  @Override
  public boolean equals(Object that) {
    return equals(this, that);
  }

  @Override
  public int hashCode() {
    return hashCode(this);
  }

  public static boolean isAssignableFrom(IJavaClassInfo thisObj, IJavaClassInfo that) {
    if (thisObj.equals(that)) {
      return true;
    }
    if (thisObj.isArray() && that.isArray()) {
      return thisObj.getComponentType().isAssignableFrom(that.getComponentType());
    }
    IJavaClassInfo thatSuper = that.getSuperclass();
    if (thatSuper != null && thisObj.isAssignableFrom(thatSuper)) {
      return true;
    }
    for (IJavaClassInfo thatInterface : that.getInterfaces()) {
      if (thisObj.isAssignableFrom(thatInterface)) {
        return true;
      }
    }
    return false;    
  }
  
  public static boolean equals(IJavaClassInfo thisObj, Object that) {
    if (!(that instanceof IJavaClassInfo)) {
      return false;
    }
    if (thisObj.isArray()) {
      return ((IJavaClassInfo) that).isArray() && thisObj.getComponentType().equals(((IJavaClassInfo) that).getComponentType());
    }
    return thisObj.getName().equals(((IJavaClassInfo) that).getName());
  }
  
  public static int hashCode(IJavaClassInfo thisObj) {
    return thisObj.isArray() ?
            thisObj.getComponentType().hashCode() :
            thisObj.getName().hashCode();
  }
}
