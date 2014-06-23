/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class ScriptabilityModifier implements IScriptabilityModifier
{
  private static final IScriptabilityModifier[] EMPTY = new IScriptabilityModifier[0];

  private IVisibilityModifierType _type;
  transient private Boolean _hasConstraintQualifiers;
  private IScriptabilityModifier[] _qualifiers;

  public ScriptabilityModifier( IVisibilityModifierType type )
  {
    _type = type;
    _qualifiers = EMPTY;
  }

  public ScriptabilityModifier( IVisibilityModifierType type, IScriptabilityModifier[] qualifiers )
  {
    _type = type;
    _qualifiers = qualifiers;
  }

  public IVisibilityModifierType getType()
  {
    return _type;
  }

  public IScriptabilityModifier[] getQualifiers()
  {
    return _qualifiers;
  }

  public IScriptabilityModifier[] getQualifiersAsArray()
  {
    return _qualifiers;
  }

  public boolean hasConstraintQualifiers()
  {
    if( _hasConstraintQualifiers != null )
    {
      return _hasConstraintQualifiers;
    }

    if( _qualifiers.length == 0 )
    {
      return _hasConstraintQualifiers = Boolean.FALSE;
    }

    //noinspection ForLoopReplaceableByForEach
    for( int i = 0; i < _qualifiers.length; i++ )
    {
      IScriptabilityModifier vm = _qualifiers[i];
      if( vm.getType().isConstraint() )
      {
        return _hasConstraintQualifiers = Boolean.TRUE;
      }
    }

    return _hasConstraintQualifiers = Boolean.FALSE;
  }

  public boolean hasModifierWithType( IVisibilityModifierType type )
  {
    return getModifierWithType( type ) != null;
  }

  public IScriptabilityModifier getModifierWithType( IVisibilityModifierType type )
  {
    if( _type.equals( type ) )
    {
      return this;
    }

    IScriptabilityModifier[] qualifiers = getQualifiersAsArray();
    //noinspection ForLoopReplaceableByForEach
    for( int i = 0; i < qualifiers.length; i++ )
    {
      IScriptabilityModifier vm = qualifiers[i].getModifierWithType( type );
      if( vm != null )
      {
        return vm;
      }
    }

    return null;
  }

  public boolean satisfiesConstraint( IScriptabilityModifier constraint )
  {
    // Find the modifier within this modifier that satisfies the constraint...

    IScriptabilityModifier modifier = getModifierWithType( constraint.getType() );
    if( modifier == null )
    {
      // This modifier does not satisfy the constraint, if this modifier does not
      // have (or contain) the constraint's type.
      return false;
    }

    // A modifier in this constraint has the constraint's type.. so far so good...
    if( !constraint.hasConstraintQualifiers() )
    {
      // The modifier satisfies the constraint, if the constraint has no constraint qualifiers.
      return true;
    }

    if( !modifier.hasConstraintQualifiers() )
    {
      // The modifier satisfies the constraint, if the modifier has no constraint qualifiers.
      return true;
    }

    // The modifier satisfies the constraint, iff the constraint's qualifiers are each
    // satisfied by the modifier's qualifiers.
    IScriptabilityModifier[] modifierQualifiers = modifier.getQualifiers();
    IScriptabilityModifier[] constraintQualifiers = constraint.getQualifiers();
    outer:
    //noinspection ForLoopReplaceableByForEach
    for( int i = 0; i < constraintQualifiers.length; i++ )
    {
      IScriptabilityModifier qualifierConstraint = constraintQualifiers[i];
      //noinspection ForLoopReplaceableByForEach
      for( int j = 0; j < modifierQualifiers.length; j++ )
      {
        IScriptabilityModifier qualifierModifier = modifierQualifiers[j];
        if( qualifierModifier.satisfiesConstraint( qualifierConstraint ) )
        {
          continue outer;
        }
      }
      return false;
    }

    return true;
  }
}
