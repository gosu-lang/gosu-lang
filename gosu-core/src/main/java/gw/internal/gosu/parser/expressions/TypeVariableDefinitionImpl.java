/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.GenericTypeVariable;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class TypeVariableDefinitionImpl implements ITypeVariableDefinition
{
  TypeVariableType _type;
  String _strName;
  IType _enclosingType;
  IType _boundingType;
  GenericTypeVariable _typeVar;

  public TypeVariableDefinitionImpl() { }

  public TypeVariableDefinitionImpl(TypeVariableType type, String strName, IType enclosingType, IType boundingType, GenericTypeVariable typeVar) {
    _type = type;
    _strName = strName;
    _enclosingType = enclosingType;
    _boundingType = boundingType;
    _typeVar = typeVar;
  }

  public TypeVariableType getType()
  {
    return _type;
  }
  public void setType( TypeVariableType type )
  {
    _type = type;
  }

  public IType getEnclosingType()
  {
    return _enclosingType;
  }
  public void setEnclosingType( IType enclosingType )
  {
    _enclosingType = enclosingType;
  }

  public String getName()
  {
    return _strName;
  }
  void setName( String strName )
  {
    _strName = strName == null ? null : strName.intern();
  }

  public GenericTypeVariable getTypeVar()
  {
    if( _typeVar == null )
    {
      _typeVar = new GenericTypeVariable( this, _boundingType == null
                                                ? JavaTypes.OBJECT()
                                                : _boundingType );
    }
    return _typeVar;
  }
  public void setTypeVar( GenericTypeVariable typeVar )
  {
    _typeVar = typeVar;
  }

  public IType getBoundingType()
  {
    return _boundingType;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }

    // Note the interface, not the class...
    if( !(o instanceof ITypeVariableDefinition) )
    {
      return false;
    }

    TypeVariableDefinitionImpl that = o instanceof TypeVariableDefinitionImpl ? (TypeVariableDefinitionImpl)o : ((TypeVariableDefinition)o)._typeVarDef;

    if( _enclosingType != null ? !_enclosingType.equals( that._enclosingType ) : that._enclosingType != null )
    {
      return false;
    }
    if( _strName != null ? !_strName.equals( that._strName ) : that._strName != null )
    {
      return false;
    }
    if( _type != null ? !_type.equals( that._type ) : that._type != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _type != null ? _type.hashCode() : 0;
    result = 31 * result + (_strName != null ? _strName.hashCode() : 0);
    result = 31 * result + (_enclosingType != null ? _enclosingType.hashCode() : 0);
    result = 31 * result + (_boundingType != null ? _boundingType.hashCode() : 0);
    result = 31 * result + (_typeVar != null ? _typeVar.hashCode() : 0);
    return result;
  }

  public String toString()
  {
    return _strName + (_boundingType == null ? "" : " " + Keyword.KW_extends + " " + _boundingType.toString());
  }

  @Override
  public TypeVariableDefinitionImpl clone() {
    return new TypeVariableDefinitionImpl( _type, _strName, _enclosingType, _boundingType, _typeVar );
  }

  public TypeVariableDefinitionImpl cloneShallow( IType boundingType ) {
    return new TypeVariableDefinitionImpl( _type, _strName, _enclosingType, boundingType, _typeVar );
  }

  public TypeVariableDefinitionImpl clone( IType boundingType ) {
    IGenericTypeVariable gtv = _typeVar.clone( boundingType );
    return (TypeVariableDefinitionImpl)gtv.getTypeVariableDefinition();
  }
}
