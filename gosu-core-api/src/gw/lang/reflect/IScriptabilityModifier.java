/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.UnstableAPI;

import java.io.Serializable;

@UnstableAPI
public interface IScriptabilityModifier extends Serializable
{
  public IVisibilityModifierType getType();

  public IScriptabilityModifier[] getQualifiers();

  public boolean hasConstraintQualifiers();

  public boolean hasModifierWithType( IVisibilityModifierType type );

  public IScriptabilityModifier getModifierWithType( IVisibilityModifierType type );

  public boolean satisfiesConstraint( IScriptabilityModifier constraint );
}
