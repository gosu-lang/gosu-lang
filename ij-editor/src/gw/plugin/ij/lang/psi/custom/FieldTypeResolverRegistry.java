/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.google.common.collect.Lists;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import gw.lang.reflect.IType;
import gw.plugin.ij.util.ClassNameExtensionBean;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public enum FieldTypeResolverRegistry implements FieldTypeResolver {

  INSTANCE;

  private final ClassNameExtensionBean[] extensions = Extensions.getExtensions(
          new ExtensionPointName<ClassNameExtensionBean>("com.guidewire.gosu.fieldTypeResolver"));

  private final List<FieldTypeResolver> resolvers;

  private FieldTypeResolverRegistry() {
    List<FieldTypeResolver> resolvers = Lists.newArrayList();
    for (ClassNameExtensionBean extension : extensions) {
      FieldTypeResolver resolver = extension.instantiate(FieldTypeResolver.class);
      if (resolver != null) {
        resolvers.add(resolver);
      }
    }
    this.resolvers = unmodifiableList(resolvers);
  }

  public IType resolve(IType type, String fieldName) {
    for (FieldTypeResolver resolver : resolvers) {
      IType resolvedType = resolver.resolve(type, fieldName);
      if (resolvedType != null) {
        return resolvedType;
      }
    }
    return null;
  }
}
