/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public abstract class ITypeRef implements IType
{
  public abstract boolean isDeleted();

  public abstract boolean _shouldReload();

  public abstract void _setStale(RefreshKind refreshKind);

  public abstract Class<? extends IType> _getClassOfRef();

  public abstract void setReloadable(boolean bReloadable);
  public abstract boolean isReloadable();

  public abstract boolean isTypeRefreshedOutsideOfLock(IType type);

  public abstract ITypeLoader getTypeLoaderDirectly();
}
