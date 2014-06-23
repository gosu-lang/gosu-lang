/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class NoReferenceFoundException extends RuntimeException
{
  private IType _type;

  public NoReferenceFoundException( IType type )
  {
    super( type.getName() );
    _type = type;
  }

  public IType getType()
  {
    return _type;
  }
}
