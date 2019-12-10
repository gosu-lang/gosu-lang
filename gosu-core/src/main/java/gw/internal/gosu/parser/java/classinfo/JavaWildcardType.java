/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.TypeVariableType;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.Variance;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;


public class JavaWildcardType implements IJavaClassWildcardType {
  private JavaParameterizedType _ownerType;
  private IJavaClassType _bound;
  private boolean _bSuper;

  public JavaWildcardType( IJavaClassType bound, boolean bSuper ) {
    _bound = bound;
    _bSuper = bSuper;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getUpperBound();
  }

  @Override
  public String getNamespace() {
    return null;
  }

  @Override
  public String getName() {
    return "? extends " + _bound.getName();
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  public String toString() {
    return getName();
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap ) {
    return getActualType( typeMap, false );
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars )
  {
    if( _bound != null && isContravariant() )
    {
      IJavaClassType[] typeArgs = getOwnerType().getActualTypeArguments();
      if( typeArgs != null && typeArgs.length > 0 )
      {
        IType genType = TypeSystem.getByFullNameIfValid( getOwnerType().getConcreteType().getName() );
        if( genType instanceof IJavaType )
        {
          // For functional interfaces we keep the lower bound as an upper bound so that blocks maintain contravariance wrt the single method's parameters
          if( FunctionToInterfaceCoercer.getSingleMethodFromJavaInterface( (IJavaType)genType ) == null )
          {
            _bound = null;
          }
        }
      }
    }
    if( _bound == null )
    {
      return JavaTypes.OBJECT();
    }
    IType retType = _bound.getActualType( typeMap, bKeepTypeVars );
    if( retType instanceof TypeVariableType )
    {
      ITypeVariableDefinition tvd = ((ITypeVariableType)retType).getTypeVarDef().clone();
      retType = new TypeVariableType( tvd, ((ITypeVariableType)retType).isFunctionStatement() );
      ((TypeVariableType)retType).getTypeVarDef().setVariance( isContravariant() ? Variance.WILD_CONTRAVARIANT : Variance.WILD_COVARIANT );
    }
    return retType;
  }

  @Override
  public IJavaClassType getUpperBound() {
    return _bound;
  }

  public void setBound(IJavaClassType bound) {
    _bound = bound;
  }

  public boolean isContravariant()
  {
    return _bSuper;
  }

  public void setOwnerType( JavaParameterizedType ownerType )
  {
    _ownerType = ownerType;
  }
  public JavaParameterizedType getOwnerType()
  {
    return _ownerType;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public IJavaClassType getComponentType() {
    return null;
  }
}
