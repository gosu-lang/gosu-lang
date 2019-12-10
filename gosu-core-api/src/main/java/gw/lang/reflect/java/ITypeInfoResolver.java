/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

public interface ITypeInfoResolver {

  IJavaClassType resolveType(String relativeName, int ignoreFlags);

  IJavaClassType resolveType(String relativeName, IJavaClassInfo whosAskin, int ignoreFlags);

  IJavaClassType resolveImport(String relativeName);
}
