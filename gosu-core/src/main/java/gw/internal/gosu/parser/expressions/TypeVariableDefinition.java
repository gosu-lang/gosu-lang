/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.GenericTypeVariable;
import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.ITypeVariableDefinitionExpression;
import gw.lang.parser.expressions.Variance;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;


/**
 */
public class TypeVariableDefinition extends Expression implements ITypeVariableDefinition, ITypeVariableDefinitionExpression
{
  //!! Note we delegate the ITypeVariableDefinition impl so we can separate it from the Expression
  // i.e., we don't want the memory attached to the Expression to hang around in the TypeVariableType
  TypeVariableDefinitionImpl _typeVarDef;

  public TypeVariableDefinition( IType enclosingType, boolean forFunction )
  {
    _typeVarDef = new TypeVariableDefinitionImpl();
    _typeVarDef._enclosingType = enclosingType;
    _type = new TypeVariableType( _typeVarDef, forFunction );
    _typeVarDef._type = (TypeVariableType)_type;
  }

  // This ctor is useful only when coming from a generic JavaType
  public TypeVariableDefinition( IType enclosingType, IGenericTypeVariable typeVar )
  {
    _typeVarDef = new TypeVariableDefinitionImpl();
    _typeVarDef._typeVar = (GenericTypeVariable)typeVar;
    _type = new TypeVariableType( _typeVarDef, false );
    _typeVarDef._enclosingType = enclosingType;
    _typeVarDef._strName = typeVar.getName();
    _typeVarDef._boundingType = typeVar.getBoundingType();
    _typeVarDef._type = (TypeVariableType)_type;
  }

  // Uses for recursive type vars and as a copy ctor
  public TypeVariableDefinition( IType enclosingType, IGenericTypeVariable typeVar, TypeVariableDefinitionImpl typeVarDef, TypeVariableType typeVarType )
  {
    _typeVarDef = typeVarDef;
    _typeVarDef._typeVar = (GenericTypeVariable)typeVar;
    _type = typeVarType;
    _typeVarDef._enclosingType = enclosingType;
    _typeVarDef._strName = typeVar.getName();
    _typeVarDef._boundingType = typeVar.getBoundingType();
    _typeVarDef._type = (TypeVariableType)_type;
  }

  // Copy constructor used for cloning
  public TypeVariableDefinition(TypeVariableDefinitionImpl typeVarDef, boolean isFunctionStatement) {
    _typeVarDef = typeVarDef;
    _type = new TypeVariableType( _typeVarDef, isFunctionStatement );
  }

  @Override
  public TypeVariableType getType()
  {
    return (TypeVariableType) super.getType();
  }

  @Override
  public TypeVariableType getTypeImpl()
  {
    return _typeVarDef.getType();
  }

  public void setType( IType type )
  {
    super.setType( type );
    _typeVarDef._type = (TypeVariableType)type;
  }

  public IType getEnclosingType()
  {
    return _typeVarDef.getEnclosingType();
  }
  public void setEnclosingType( IType enclosingType )
  {
    _typeVarDef.setEnclosingType( enclosingType );
  }

  public String getName()
  {
    return _typeVarDef.getName();
  }

  public void setName( String strName )
  {
    _typeVarDef.setName( strName );
  }

  public GenericTypeVariable getTypeVar()
  {
    return _typeVarDef.getTypeVar();
  }

  public Object evaluate()
  {
    return null;
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

    TypeVariableDefinitionImpl thatTypeVarDef = o instanceof TypeVariableDefinition ? ((TypeVariableDefinition)o)._typeVarDef : (TypeVariableDefinitionImpl)o;

    return !(_typeVarDef != null ? !_typeVarDef.equals( thatTypeVarDef ) : thatTypeVarDef != null);
  }

  @Override
  public int hashCode()
  {
    return _typeVarDef != null ? _typeVarDef.hashCode() : 0;
  }

  @Override
  public String toString()
  {
    return _typeVarDef.toString();
  }

  public static IGenericTypeVariable[] getTypeVars( List<ITypeVariableDefinitionExpression> typeVarDefs )
  {
    if( typeVarDefs.isEmpty() )
    {
      return GenericTypeVariable.EMPTY_TYPEVARS;
    }

    IGenericTypeVariable[] typeVars = new IGenericTypeVariable[typeVarDefs.size()];
    for( int i = 0; i < typeVarDefs.size(); i++ )
    {
      ITypeVariableDefinition typeVarDef = (ITypeVariableDefinition)typeVarDefs.get( i );
      typeVars[i] = typeVarDef.getTypeVar();
    }
    return typeVars;
  }

  @Override
  public int getNameOffset( String identifierName )
  {
    return getLocation().getOffset();
  }
  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    throw new UnsupportedOperationException();
  }

  public boolean declares( String identifierName )
  {
    return getType().getName().equals( identifierName );
  }

  public String[] getDeclarations() {
    return new String[] {getType().getName()};
  }

  @Override
  public ITypeVariableDefinition clone() {
    return new TypeVariableDefinition(_typeVarDef.clone(), ((TypeVariableType) _type).isFunctionStatement());
  }

  public IType getBoundingType()
  {
    return _typeVarDef._boundingType;
  }
  public void setBoundingType( IType type )
  {
    _typeVarDef._boundingType = type;
    if( _typeVarDef._typeVar != null )
    {
      _typeVarDef._typeVar = new GenericTypeVariable( _typeVarDef, type == null
                                                      ? JavaTypes.OBJECT()
                                                      : type );
    }
  }

  @Override
  public Variance getVariance()
  {
    return _typeVarDef.getVariance();
  }
  @Override
  public void setVariance( Variance variance )
  {
    _typeVarDef.setVariance( variance );
  }

  public ITypeVariableDefinition getTypeVarDef() {
    return _typeVarDef;
  }
  public void setTypeVarDef( TypeVariableDefinitionImpl typeVarDef )
  {
    _typeVarDef = typeVarDef;
  }
}
