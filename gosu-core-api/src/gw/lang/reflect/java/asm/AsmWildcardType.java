/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

/**
 */
public class AsmWildcardType extends AsmType {
  private AsmType _bound;
  private boolean _bCovariant;

  public AsmWildcardType( AsmType bound, boolean bCovariant ) {
    super( "?" );
    _bCovariant = bCovariant;
    _bound = bound;
  }

  public AsmType getBound() {
    return _bound;
  }

  public boolean isCovariant() {
    return _bCovariant;
  }

  @Override
  public void addTypeParameter( AsmType type ) {
    _bound.addTypeParameter( type );
  }

  @Override
  public void incArrayDims() {
    _bound.incArrayDims();
  }

  public String toString() {
    String name = getName();
    if( _bound != null ) {
      name += _bCovariant ? " extends " : " super ";
      name += _bound.toString();
    }
    return name;
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null || getClass() != o.getClass() ) {
      return false;
    }
    if( !super.equals( o ) ) {
      return false;
    }

    AsmWildcardType that = (AsmWildcardType)o;

    if( _bCovariant != that._bCovariant ) {
      return false;
    }
    if( _bound != null ? !_bound.equals( that._bound ) : that._bound != null ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (_bound != null ? _bound.hashCode() : 0);
    result = 31 * result + (_bCovariant ? 1 : 0);
    return result;
  }
}
