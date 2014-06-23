/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface ITypeRef extends IType
{
  boolean isDeleted();

  boolean _shouldReload();

  void _setStale(RefreshKind refreshKind);

  Class<? extends IType> _getClassOfRef();

  void setReloadable(boolean bReloadable);
  boolean isReloadable();

  boolean isTypeRefreshedOutsideOfLock(IType type);

  ITypeLoader getTypeLoaderDirectly();
}
