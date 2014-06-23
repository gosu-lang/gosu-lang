/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.UnstableAPI;

@UnstableAPI
public interface IRTypeResolver {

  IRType getDescriptor( IType type );

  IRType getDescriptor( Class cls );

  IRType getDescriptor( IJavaClassInfo cls );
}
