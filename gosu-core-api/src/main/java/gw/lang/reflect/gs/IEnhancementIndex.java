/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import manifold.api.host.RefreshRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IEnhancementIndex
{

  void maybeLoadEnhancementIndex();

  void refreshedTypes(RefreshRequest request);


  void addEnhancementMethods( IType typeToEnhance, Collection<IMethodInfo> methodsToAddTo );

  void addEnhancementProperties(IType typeToEnhance, Map<CharSequence, IPropertyInfo> propertyInfosToAddTo, boolean caseSensitive);

  List<? extends IGosuEnhancement> getEnhancementsForType( IType gosuClass );

  void removeEntry( IGosuEnhancement enhancement );

  void addEntry( IType enhancedType, IGosuEnhancement enhancement );

  String getOrphanedEnhancement(String typeName);
}
