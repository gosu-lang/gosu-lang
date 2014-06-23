/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;

import gw.lang.reflect.IType;

public final class HashedObjectLiteral
{
  private final IType _class;
  private final long _id;

  /**
   * @param id A unique id for the object.
   */
  public HashedObjectLiteral( IType cls, long id )
  {
    _class = cls;
    _id = id;
  }

  /**
   * @return The class assignable to this literal.
   */
  public IType getAssignableClass()
  {
    return _class;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HashedObjectLiteral that = (HashedObjectLiteral) o;

    if (_id != that._id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (_id ^ (_id >>> 32));
  }

  public long getId() {
    return _id;
  }
}
