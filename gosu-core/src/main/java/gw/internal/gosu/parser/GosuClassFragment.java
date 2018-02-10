/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.StringSourceFileHandle;
import manifold.api.type.ClassType;

public class GosuClassFragment extends GosuClass implements IGosuClassFragment
{
  public static final String FRAGMENT_EXT = "_fragment";

  public GosuClassFragment( IGosuClassInternal owningClass, String fullSource )
  {
    super( owningClass.getNamespace(),
           owningClass.getRelativeName() + FRAGMENT_EXT,
           (GosuClassTypeLoader)owningClass.getTypeLoader(),
           new StringSourceFileHandle( owningClass.getName(), fullSource, false,
                                       owningClass instanceof IGosuEnhancementInternal
                                       ? ClassType.Enhancement
                                       : ClassType.Class ),
           owningClass.getTypeUsesMap() );
  }
}
