/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface ITypeRefFactory
{
  public static final String SYSTEM_PROXY_SUFFIX = "_Proxy";
  public static final String USER_PROXY_SUFFIX = "_TypeProxy";

  ITypeRef create( IType type );
  ITypeRef get( IType type );
  ITypeRef get( String strTypeName );

  void clearCaches();

  boolean isClearing();

  List<String> getTypesWithPrefix(String namespace, String prefix);

  List<ITypeRef> getSubordinateRefs(String topLevelTypeName);

}
