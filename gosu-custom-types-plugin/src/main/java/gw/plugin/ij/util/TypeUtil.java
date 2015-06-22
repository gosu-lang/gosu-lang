/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TypeUtil
{
  public static IType getTrueEnclosingType(@NotNull IType type) {
    IType enclosingType = type.getEnclosingType();
    while (enclosingType instanceof IBlockClass ) {
      enclosingType = enclosingType.getEnclosingType();
    }
    return enclosingType;
  }

  public static IType getConcreteType(IType type) {
    while (type instanceof IMetaType ) {
      type = ((IMetaType) type).getType();
    }

    while (type.isArray()) {
      type = type.getComponentType();
    }

    while (type.isParameterizedType()) {
      type = type.getGenericType();
    }

    if (type instanceof ITypeVariableType ) {
      type = getConcreteType(((ITypeVariableType) type).getBoundingType());
    }

    return type;
  }

  public static IType getType(PsiClass psiClass) {
    final String qualifiedName = psiClass.getQualifiedName();
    final IModule module = GosuModuleUtil.findModuleForPsiElement( psiClass );
    TypeSystem.pushModule( module );
    try {
      return TypeSystem.getByFullNameIfValid( qualifiedName, module );
    } finally {
      TypeSystem.popModule( module );
    }
  }

  public static IType getType(PsiClass psiClass, IModule module) {
    TypeSystem.pushModule( module );
    try {
      return TypeSystem.getByFullNameIfValid( psiClass.getQualifiedName(), module );
    } catch (NoClassDefFoundError e) {
      // Probably, can't load dependencies of the class, just ignore the type itself
      // FIXME: better way of doing that?
      return null;
    }
    finally {
      TypeSystem.popModule( module );
    }
  }

  @NotNull
  public static List<String> getTypesForFile(IModule module, VirtualFile file) {
    return Arrays.asList( TypeSystem.getTypesForFile( module, FileUtil.toIFile( file ) ));
  }

  @NotNull
  public static List<String> getTypesForFiles(IModule module, List<VirtualFile> files) {
    final List<String> types = Lists.newArrayList();
    for (VirtualFile file : files) {
      types.addAll(getTypesForFile(module, file));
    }
    return types;
  }
}
