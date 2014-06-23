/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.fs.IDirectory;
import gw.lang.reflect.IType;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.module.IModule;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 */
public class DynamicTypeLoader extends TypeLoaderBase {
  private static final Set<String> TYPE_NAMES = Collections.singleton( DynamicType.QNAME );

  public DynamicTypeLoader( IModule module ) {
    super( module );
  }

  @Override
  public boolean isCaseSensitive() {
    return true;
  }

  @Override
  public IType getType( String fullyQualifiedName ) {
    if( fullyQualifiedName != null &&
        fullyQualifiedName.equals( DynamicType.QNAME ) ) {
      return new DynamicType( DynamicTypeLoader.this ).getOrCreateTypeReference();
    }
    return null;
  }

  @Override
  public Set<String> getAllNamespaces() {
    return Collections.singleton( DynamicType.PKG );
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.singletonList( DynamicType.PKG );
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return false;
  }

  @Override
  public void refreshedNamespace( String namespace, IDirectory dir, RefreshKind kind ) {
  }

  @Override
  public boolean hasNamespace( String namespace ) {
    return DynamicType.PKG.equals( namespace );
  }

  @Override
  public Set<String> computeTypeNames() {
    return TYPE_NAMES;
  }
}
