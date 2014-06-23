/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomPsiClassCache extends AbstractTypeSystemListener {
  private static final CustomPsiClassCache INSTANCE = new CustomPsiClassCache();

  @NotNull
  public static CustomPsiClassCache instance() {
    return INSTANCE;
  }

  private final ConcurrentHashMap<String, CustomGosuClass> _psi2Class = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<IModule, Map<String, CustomGosuClass>> _type2Class = new ConcurrentHashMap<>();

  private CustomPsiClassCache() {
    TypeSystem.addTypeLoaderListenerAsWeakRef(this);
  }

  public CustomGosuClass getPsiClass(@NotNull IType type) {
    if (!(type instanceof IFileBasedType)) {
      return null;
    }
    List<VirtualFile> typeResourceFiles = FileUtil.getTypeResourceFiles(type);
    if (typeResourceFiles.isEmpty()) {
      return null;
    }

    IModule module = type.getTypeLoader().getModule();
    String name = type.getName();
    Map<String, CustomGosuClass> map = _type2Class.get(module);
    if (map == null) {
      map = new ConcurrentHashMap<>();
      _type2Class.put(module, map);
    }
    CustomGosuClass psiClass = map.get(name);
    if (psiClass == null) {
      psiClass = createPsiClass(type);
      map.put(name, psiClass);
      VirtualFile virtualFile = psiClass.getContainingFile().getVirtualFile();
      _psi2Class.put(virtualFile.getPath(), psiClass);
    }
    return psiClass;
  }

  public CustomGosuClass getPsiClass(@NotNull PsiFile file) {
    String virtualFilePath = file.getVirtualFile().getPath();
    CustomGosuClass psiClass = _psi2Class.get(virtualFilePath);
    if (psiClass == null) {
      psiClass = new CustomGosuClass(file);
      _psi2Class.put(virtualFilePath, psiClass);
    }
    return psiClass;
  }

  @NotNull
  private CustomGosuClass createPsiClass(@NotNull IType type) {
    if (!(type instanceof IFileBasedType)) {
      throw new RuntimeException("Only file-based types can have custom PsiClasses: " + type.getClass().getName());
    }

    verifyArgs(type);
    return new CustomGosuClass((IFileBasedType) type);
  }

  private void verifyArgs(@NotNull IType type) {
    if (type instanceof IGosuClass || type instanceof IJavaType || type instanceof IBlockType) {
      throw new RuntimeException("It is wrong to cache these types: " + type.getClass().getSimpleName());
    }
  }

  public Collection<? extends String> getAllClassNames() {
//    long t1 = System.nanoTime();

    Set<String> classes = new HashSet<>();
    for (ITypeLoader loader : TypeSystem.getAllTypeLoaders()) {
      if (!(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader)) {
        IModule module = loader.getModule();
        TypeSystem.pushModule(module);
        try {
          for (CharSequence cs : loader.getAllTypeNames()) {
            String s = cs.toString();
            int i = s.lastIndexOf('.');
            if (i > 0) {
              s = s.substring(i + 1);
            }
            classes.add(s);
          }
        } finally {
          TypeSystem.popModule(module);
        }
      }
    }

//    System.out.println((System.nanoTime() - t1)*1e-6);
    return classes;
  }

  @NotNull
  public Collection<PsiClass> getByShortName(String shortName) {
    Set<PsiClass> classes = new HashSet<>();
    String prefix = "." + shortName;
    for (ITypeLoader loader : TypeSystem.getAllTypeLoaders()) {
      if (loader.showTypeNamesInIDE() && !(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader)) {
        IModule module = loader.getModule();
        TypeSystem.pushModule(module);
        try {
          for (CharSequence cs : loader.getAllTypeNames()) {
            String typeName = cs.toString();
            if (typeName.endsWith(prefix)) {
              IType type = TypeSystem.getByFullNameIfValid(typeName, module);
              if (type instanceof IFileBasedType) {
                CustomGosuClass psiClass = getPsiClass(type);
                if (psiClass != null) {
                  classes.add(psiClass);
                }
              }
            }
          }
        } finally {
          TypeSystem.popModule(module);
        }
      }
    }
    return classes;
  }

  @Override
  public void refreshedTypes(RefreshRequest request) {
    Map<String, CustomGosuClass> map = _type2Class.get(request.module);
    if (map != null) {
      for (String type : request.types) {
        map.remove(type);
      }
    }
    if (request.file != null) {
      String pathString = request.file.getPath().getPathString();
      _psi2Class.remove(pathString);
    }
  }

  @Override
  public void refreshed() {
    _psi2Class.clear();
    _type2Class.clear();
  }
}
