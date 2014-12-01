/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IExpression;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.perf.objectsize.UnmodifiableSizeTwoSet;

import java.util.Arrays;
import java.util.List;

/**
 */
public class BlockType extends FunctionType implements IBlockType
{
  private String _relativeSignature;
  private String _relativeSignatureSansBlock;
  private String[] _argNames;
  private IExpression[] _defValues;

  public BlockType( IType returnType, IType[] iIntrinsicTypes, List<String> argNames, List<IExpression> defValues )
  {
    super( "block", returnType, iIntrinsicTypes );
    _allTypesInHierarchy = new UnmodifiableSizeTwoSet<IType>( this, JavaTypes.IBLOCK() );
    _argNames = argNames.toArray( new String[argNames.size()] );
    _defValues = defValues.toArray( new IExpression[defValues.size()] );
  }

  @SuppressWarnings({"UnusedDeclaration"}) // Used via reflection from compiler
  public BlockType( IType returnType, IType[] iIntrinsicTypes, String[] argNames, IExpression[] defValues )
  {
    super( "block", returnType, iIntrinsicTypes );
    _allTypesInHierarchy = new UnmodifiableSizeTwoSet<IType>( this, JavaTypes.IBLOCK() );
    _argNames = new String[argNames.length];
    System.arraycopy( argNames, 0, _argNames, 0, argNames.length );
    _defValues = new IExpression[defValues.length];
    System.arraycopy( defValues, 0, _defValues, 0, defValues.length );
  }

  private BlockType( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, List<String> argNames, List<IExpression> defValues )
  {
    super( source, actualParamByVarName, bKeepTypeVars );
    _argNames = argNames.toArray( new String[argNames.size()] );
    _defValues = defValues.toArray( new IExpression[defValues.size()] );
  }

  protected void copyFields( FunctionType source )
  {
    super.copyFields( source );
  }

  public FunctionType parameterize( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return new BlockType( source, actualParamByVarName, bKeepTypeVars, Arrays.asList( _argNames ), Arrays.asList( _defValues ) );
  }

  @Override
  public String getName()
  {
    return getParamSignature().toString() + ":" + getReturnType().getName();
  }

  public String getRelativeName()
  {
    return getRelativeParamSignature( false ).toString() + ":" + getReturnType().getRelativeName();
  }

  public String getRelativeNameSansBlock()
  {
    return getRelativeParamSignature( true ).toString() + ":" + getReturnType().getRelativeName();
  }

  public String getRelativeParamSignature( boolean bSansBlock )
  {
    if( _relativeSignature != null )
    {
      return bSansBlock ? _relativeSignatureSansBlock : _relativeSignature;
    }

    if( getParameterTypes().length == 0 )
    {
      _relativeSignature = getParamSignature();
      _relativeSignatureSansBlock = "()";
    }
    else
    {
      String strParams = "(";
      IType[] paramTypes = getParameterTypes();
      for( int i = 0; i < paramTypes.length; i++ )
      {
        strParams += (i == 0 ? "" : ", " );
        String name = i < _argNames.length ? _argNames[i] : "arg" + i;
        strParams += (name.length() == 0 ? "" : name + ":") + (paramTypes[i] == null ? "" : paramTypes[i].getRelativeName());
      }
      strParams += ")";

      _relativeSignature = super.getName() + strParams;
      _relativeSignatureSansBlock = strParams;
    }
    return bSansBlock ? _relativeSignatureSansBlock : _relativeSignature;
  }

  public String[] getParameterNames()
  {
    return _argNames;
  }

  @Override
  protected boolean areReturnTypesAssignable( FunctionType that ) {
    IType thisType = getReturnType();
    IType thatType = that.getReturnType();
    return
      thisType == thatType ||
      thatType != JavaTypes.pVOID() && thisType.isAssignableFrom( thatType ) ||
      StandardCoercionManager.arePrimitiveTypesAssignable( thisType, thatType ) ||
      thisType == GosuParserTypes.NULL_TYPE();
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }

    if( !(o instanceof BlockType) )
    {
      return false;
    }

    return super.equals( o );
  }

  @Override
  public IExpression[] getDefaultValueExpressions()
  {
    return _defValues;
  }

  @Override
  public boolean hasOptionalParams()
  {
    for( IExpression o : getDefaultValueExpressions() )
    {
      if( o != null )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public IType newInstance( IType[] paramTypes, IType returnType )
  {
    return new BlockType( returnType, paramTypes, Arrays.asList( _argNames ), Arrays.asList( _defValues ) );
  }
}
