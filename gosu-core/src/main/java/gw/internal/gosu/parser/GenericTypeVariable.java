/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.TypeVariableDefinition;
import gw.internal.gosu.parser.expressions.TypeVariableDefinitionImpl;
import gw.lang.parser.Keyword;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.IJavaClassTypeVariable;


/**
 */
public class GenericTypeVariable implements IGenericTypeVariable
{
  public static final GenericTypeVariable[] EMPTY_TYPEVARS = new GenericTypeVariable[0];

  private String _strName;
  private TypeVariableDefinitionImpl _typeVariableDefinition;
  private IType _boundingType;


  public GenericTypeVariable( String strName, IType boundingType )
  {
    _strName = strName;
    _typeVariableDefinition = null;
    setBoundingType( boundingType );
    if( boundingType == null )
    {
      throw new IllegalArgumentException( "bounding type is null" );
    }
  }

  public GenericTypeVariable( TypeVariableDefinitionImpl typeVariableDefinition, IType boundingType )
  {
    _strName = typeVariableDefinition.getName();
    _typeVariableDefinition = typeVariableDefinition;
    setBoundingType( boundingType );
    if( boundingType == null )
    {
      throw new IllegalArgumentException( "bounding type is null" );
    }
  }

  public GenericTypeVariable( IType enclosingType, IJavaClassTypeVariable typeVar, TypeVarToTypeMap actualParamByVarName )
  {
    _strName = typeVar.getName();
    IJavaClassType[] fromBounds = typeVar.getBounds();
    IType[] boundingTypes = new IType[fromBounds.length];

    TypeVariableDefinitionImpl typeVarDef = new TypeVariableDefinitionImpl( null, _strName, enclosingType, null, this );
    TypeVariableType typeVarType = new TypeVariableType( typeVarDef, enclosingType instanceof IFunctionType );

    actualParamByVarName.put( typeVarType, typeVarType );

    for( int j = 0; j < fromBounds.length; j++ )
    {
      if( fromBounds[j] != null )
      {
        boundingTypes[j] = fromBounds[j].getActualType( actualParamByVarName, true );
      }
      else
      {
        boundingTypes[j] = TypeSystem.getErrorType();
      }

      if( boundingTypes[j] == null )
      {
        throw new IllegalArgumentException( "bounding type [" + j + "] is null" );
      }
      if( boundingTypes[j].isPrimitive() )
      {
        boundingTypes[j] = TypeSystem.getBoxType( boundingTypes[j] );
      }
    }
    setBoundingType( boundingTypes.length == 1 ? boundingTypes[0] : CompoundType.get( boundingTypes ) );

    if( enclosingType != null )
    {
      TypeVariableDefinition typeVariableDefinition;
      if( typeVarType != null )
      {
        typeVariableDefinition = new TypeVariableDefinition( enclosingType, this, (TypeVariableDefinitionImpl)typeVarType.getTypeVarDef(), typeVarType );
      }
      else
      {
        typeVariableDefinition = new TypeVariableDefinition( enclosingType, this );
      }
      _typeVariableDefinition = (TypeVariableDefinitionImpl) typeVariableDefinition.getTypeVarDef();
    }
  }

  // Copy-constructor used for clone operations
  private GenericTypeVariable( String strName, TypeVariableDefinitionImpl typeVariableDefinition, IType boundingType )
  {
    _strName = strName;
    _typeVariableDefinition = typeVariableDefinition;
    _typeVariableDefinition.setTypeVar( this );
    _boundingType = boundingType;
  }

  public String getName()
  {
    return _strName;
  }

  public void setName( String strName )
  {
    _strName = strName;
  }

  public String getNameWithBounds( boolean bRelative )
  {
    return _boundingType == JavaTypes.OBJECT()
           ? getName()
           : (getName() + " " + Keyword.KW_extends + " " + (bRelative
                                                            ? _boundingType.getRelativeName()
                                                            : _boundingType.getName()));
  }

  public ITypeVariableDefinition getTypeVariableDefinition()
  {
    return _typeVariableDefinition;
  }

  public IType getBoundingType()
  {
    return _boundingType;
  }
  private void setBoundingType( IType type )
  {
    if( type != null && type.isPrimitive() )
    {
      // Never ever let a type var be bounded by a primitive.
      // Instead we autobox the type.
      type = TypeSystem.getBoxType( type );
    }
    _boundingType = type;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    GenericTypeVariable that = (GenericTypeVariable)o;
    return _boundingType.equals( that._boundingType ) &&
           _strName.equals( that._strName );
  }

  public int hashCode()
  {
    return _strName.hashCode();
  }
  
  public static GenericTypeVariable[] convertTypeVars( IType enclosingType, IType classType, IJavaClassTypeVariable[] fromVars )
  {
    TypeVarToTypeMap paramByVarNameIncludingMethod = new TypeVarToTypeMap();
    GenericTypeVariable[] toVars = new GenericTypeVariable[fromVars.length];
    for( int i = 0; i < toVars.length; i++ )
    {
      TypeVarToTypeMap classTypeVars = classType == null ? new TypeVarToTypeMap() : TypeLord.mapTypeByVarName( classType, classType );
      if( classTypeVars == TypeVarToTypeMap.EMPTY_MAP )
      {
        classTypeVars = new TypeVarToTypeMap();
      }
      toVars[i] = new GenericTypeVariable( enclosingType, fromVars[i], classTypeVars );
      paramByVarNameIncludingMethod.put( toVars[i].getTypeVariableDefinition().getType(), toVars[i].getTypeVariableDefinition().getType() );
    }
    return toVars.length == 0 ? EMPTY_TYPEVARS : toVars;
  }

  public IGenericTypeVariable copy()
  {
    return new GenericTypeVariable( _strName, _typeVariableDefinition.clone(), _boundingType );
  }

  public IGenericTypeVariable copy( IType boundingType )
  {
    TypeVariableDefinitionImpl tvd = _typeVariableDefinition.cloneShallow( boundingType );
    return new GenericTypeVariable( _strName, tvd, boundingType );
  }

  @Override
  public IGenericTypeVariable remapBounds( TypeVarToTypeMap actualParamByVarName )
  {
    IType boundingType = TypeLord.getActualType( _boundingType, actualParamByVarName, true );
    if( boundingType == _boundingType )
    {
      return this;
    }
    return copy( boundingType );
  }

  public void createTypeVariableDefinition( IType enclosingType )
  {
    _typeVariableDefinition = (TypeVariableDefinitionImpl)new TypeVariableDefinition( enclosingType, this ).getTypeVarDef();
  }
}
