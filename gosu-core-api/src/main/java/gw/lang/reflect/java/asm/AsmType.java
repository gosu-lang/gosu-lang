/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class AsmType implements IAsmType {
  private String _name;
  // Type parameters represent either Type Variables (for generic type), Type Arguments (for parameterized type), or Type Bounds (for type variable type)
  protected List<AsmType> _typeParameters;
  private int _iArrayDims;
  private boolean _bTypeVariable;
  private boolean _bFunctionTypeVar;


  AsmType( String normalDotName ) {
    _name = AsmUtil.makeBaseName( normalDotName );
    verifyNoArrayBrackets();
    _typeParameters = Collections.emptyList();
  }

  AsmType( String normalDotName, int iDims ) {
    _name = normalDotName;
    verifyNoArrayBrackets();
    _iArrayDims = iDims;
    _typeParameters = Collections.emptyList();
  }

  private AsmType() {
  }

  AsmType copy() {
    AsmType copy = new AsmType();
    copy._name = _name;
    copy._typeParameters = _typeParameters.isEmpty() ? Collections.<AsmType>emptyList() : new ArrayList<AsmType>( _typeParameters );
    copy._iArrayDims = _iArrayDims;
    copy._bTypeVariable = _bTypeVariable;
    copy._bFunctionTypeVar = _bFunctionTypeVar;
    return copy;
  }

  AsmType copyNoArrayOrParameters() {
    AsmType copy = new AsmType();
    copy._name = _name;
    copy._typeParameters = Collections.emptyList();
    copy._iArrayDims = 0;
    copy._bTypeVariable = _bTypeVariable;
    copy._bFunctionTypeVar = _bFunctionTypeVar;
    return copy;
  }

  public AsmType getRawType() {
    AsmType copy = new AsmType();
    copy._name = _name;
    copy._iArrayDims = _iArrayDims;
    copy._typeParameters = Collections.emptyList();
    return copy;
  }

  @Override
  public String getName() {
    return _name;
  }

  public void setName( String name ) {
    verifyNoArrayBrackets();
    _name = name;
  }
  public String getNameWithArrayBrackets() {
    String name = getName();
    for( int i = 0; i < _iArrayDims; i++ ) {
      name += "[]";
    }
    return name;
  }

  public String getSimpleName() {
    String name = getName();
    int iDot = name.lastIndexOf( '.' );
    if( iDot < 0 ) {
      return name;
    }
    else {
      return name.substring( iDot + 1 );
    }
  }

  @Override
  public List<AsmType> getTypeParameters() {
    return _typeParameters;
  }

  public void addTypeParameter( AsmType type ) {
    if( _typeParameters.isEmpty() ) {
      _typeParameters = new ArrayList<AsmType>( 2 );
    }
    _typeParameters.add( type );
  }
  @Override
  public boolean isParameterized() {
    return !_typeParameters.isEmpty();
  }

  @Override
  public boolean isArray() {
    return _iArrayDims > 0;
  }

  public AsmType getComponentType() {
    if( isArray() ) {
      AsmType copy = copy();
      copy._iArrayDims--;
      if( copy._iArrayDims < 0 ) {
        throw new IllegalStateException();
      }
      return copy;
    }
    return null;
  }

  @Override
  public boolean isTypeVariable() {
    return _bTypeVariable;
  }
  void setTypeVariable() {
    _bTypeVariable = true;
  }

  public void setFunctionTypeVariable( boolean bFunctionTypeVariable ) {
    _bFunctionTypeVar = bFunctionTypeVariable;
  }
  public boolean isFunctionTypeVariable() {
    return _bFunctionTypeVar;
  }

  public void incArrayDims() {
    _iArrayDims++;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public String getFqn() {
    String name = getName();
    if( !getTypeParameters().isEmpty() ) {
      name += "<";
      for( int i = 0; i < getTypeParameters().size(); i++ ) {
        if( i > 0 ) {
          name += ", ";
        }
        name += getTypeParameters().get( i ).toString();
      }
      name += ">";
    }
    for( int i = 0; i < _iArrayDims; i++ ) {
      name += "[]";
    }
    return name;
  }

  @Override
  public String toString() {
    return getFqn();
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !(o instanceof AsmType) ) {
      return false;
    }

    AsmType asmType = (AsmType)o;

    if( _bTypeVariable != asmType._bTypeVariable ) {
      return false;
    }
    if( _bFunctionTypeVar != asmType._bFunctionTypeVar ) {
      return false;
    }
    if( _iArrayDims != asmType._iArrayDims ) {
      return false;
    }
    if( !_name.equals( asmType._name ) ) {
      return false;
    }
    if( !_typeParameters.equals( asmType._typeParameters ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _name.hashCode();
    result = 31 * result + _typeParameters.hashCode();
    result = 31 * result + _iArrayDims;
    result = 31 * result + (_bTypeVariable ? 1 : 0);
    result = 31 * result + (_bFunctionTypeVar ? 1 : 0);
    return result;
  }

  private void verifyNoArrayBrackets() {
    if( _name.indexOf( '[' ) >= 0  ) {
      throw new IllegalArgumentException( "Name must not contain array brackets" );
    }
  }
}
