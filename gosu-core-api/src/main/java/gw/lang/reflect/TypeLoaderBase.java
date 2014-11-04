/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class TypeLoaderBase extends BaseService implements ITypeLoader {
  protected IModule _module;
  protected Set<String> _typeNames;

  /**
   * @deprecated use TypeLoaderBase( IModule )
   */
  protected TypeLoaderBase() {
    this(TypeSystem.getCurrentModule());
    System.out.println("WARNING: " + getClass().getName() + " was constructed without specifying a module!");
  }

  protected TypeLoaderBase(IModule module) {
    _module = module;
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  @Override
  public boolean isCaseSensitive() {
    return false;
  }

  @Override
  public boolean handlesFile(IFile file) {
    return false;
  }

  @Override
  public boolean handlesDirectory(IDirectory dir) {
    return false;
  }

  @Override
  public String getNamespaceForDirectory(IDirectory dir) {
    return null;
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    return NO_TYPES;
  }

  @Override
  public RefreshKind refreshedFile(IFile file, String[] types, RefreshKind kind) {
    return kind;
  }

  @Override
  public URL getResource(String name) {
    return null;
  }

  @Override
  public final void refreshedTypes(RefreshRequest request) {
    if (shouldCacheTypeNames()) {
      // get the type name set initialized first
      getAllTypeNames();

      if (request.kind == RefreshKind.CREATION) {
        for (String type : request.types) {
          _typeNames.add(type);
        }
      } else if (request.kind == RefreshKind.DELETION) {
        for (String type : request.types) {
          _typeNames.remove(type);
        }
      } else if (request.kind == RefreshKind.MODIFICATION) {
        for (String type : request.types) {
          _typeNames.add(type);
        }
      }
    } else {
      clearTypeNames();
    }

    refreshedTypesImpl(request);
  }

  protected void refreshedTypesImpl(RefreshRequest request) {
    // for subclasses to do their refresh work
  }

  @Override
  public final void refreshed() {
    clearTypeNames();
    refreshedImpl();
  }

  protected void clearTypeNames() {
    _typeNames = null;
  }

  protected void refreshedImpl() {
    // for subclasses to do their refresh work
  }

  public String toString() {
    return this.getClass().getSimpleName() + " for module " + getModule().getName();
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    if (hasNamespace(namespace)) {
      return TypeLoaderBase.getTypeNames(namespace, this);
    } else {
      return Collections.emptySet();
    }
  }

  public static Set<TypeName> getTypeNames(String parentNamespace, ITypeLoader loader) {
    Set<TypeName> typeNames = new HashSet<TypeName>();
    for (CharSequence typeNameCS : loader.getAllTypeNames()) {
      String typeName = typeNameCS.toString();
      String packageName = GosuClassUtil.getPackage(typeName);
      if (packageName.equals(parentNamespace)) {
        typeNames.add(new TypeName(typeName, loader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC));
      }
    }
    for (CharSequence namespaceCs : loader.getAllNamespaces()) {
      String namespace = namespaceCs.toString();
      String containingPackageName = GosuClassUtil.getPackage(namespace);
      if (containingPackageName.equals(parentNamespace)) {
        typeNames.add(new TypeName(GosuClassUtil.getNameNoPackage(namespace), loader, TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC));
      }
    }
    return typeNames;
  }

  @Override
  public boolean showTypeNamesInIDE() {
    return true;
  }

  @Override
  public void shutdown() {
    if (shouldCacheTypeNames()) {
      saveTypeNames();
    }
  }

  private String getId() {
    return _module.getName() + "$" + getClass().getSimpleName();
  }

  protected void deleteIndexFile() {
    File indexFile = CommonServices.getPlatformHelper().getIndexFile(getId());
    try {
      indexFile.delete();
    } catch (Exception e) {
    }
    return;
  }

  public void saveTypeNames() {
    final File ideaCorruptionMarkerFile = CommonServices.getPlatformHelper().getIDEACorruptionMarkerFile();
    if (ideaCorruptionMarkerFile.exists()) { // clean typenames cache
      deleteIndexFile();
      return;
    }

    File indexFile = CommonServices.getPlatformHelper().getIndexFile(getId());

    PrintWriter writer = null;
    try {
      Set<String> allTypeNames = getAllTypeNames();
      writer = new PrintWriter(new FileWriter(indexFile));
      for (CharSequence typeName : allTypeNames) {
        writer.println(typeName);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error while saving Gosu Type Index for " + this);
    } finally {
      try {
        writer.close();
      } catch (Throwable e) {
      }
    }
  }

  public Set<String> loadTypeNames() {
    File indexFile = CommonServices.getPlatformHelper().getIndexFile(getId());
    if (indexFile.exists()) {
      LineNumberReader reader = null;
      try {
        reader = new LineNumberReader(new FileReader(indexFile));
        Set<String> names = new HashSet<String>();
        for (String typeName = reader.readLine(); typeName != null; typeName = reader.readLine()) {
          names.add(typeName);
        }
        return names;
      } catch (IOException e) {
        // will return null
      } finally {
        try {
          reader.close();
        } catch (Throwable e) {
        }
      }
    }
    return null;
  }

  @Override
  public final Set<String> getAllTypeNames() {
    if (_typeNames == null) {
      Set<String> names = shouldCacheTypeNames() ? loadTypeNames() : null;
      if (names != null) {
        _typeNames = names;
      } else {
        _typeNames = ExecutionMode.isIDE()
                     ? new HashSet<String>( computeTypeNames() )
                     : new HashSet<String>( computeTypeNames() );
      }
    }
    return _typeNames;
  }

  protected boolean shouldCacheTypeNames() {
    return CommonServices.getPlatformHelper().shouldCacheTypeNames();
  }
}
