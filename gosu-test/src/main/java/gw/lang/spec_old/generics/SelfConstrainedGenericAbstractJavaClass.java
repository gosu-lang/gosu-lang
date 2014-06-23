/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.spec_old.generics;

public abstract class SelfConstrainedGenericAbstractJavaClass<S extends SelfConstrainedGenericAbstractJavaClass>
{
  abstract S foo(); 
}
