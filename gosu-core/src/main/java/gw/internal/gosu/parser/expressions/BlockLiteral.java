/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.parser.expressions.IBlockLiteralExpression;
import gw.internal.gosu.parser.MetaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Block literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public class BlockLiteral extends TypeLiteral implements IBlockLiteralExpression
{
  private ArrayList<IType> _argTypes;
  private ArrayList<String> _argNames;
  private ArrayList<IExpression> _defValues;
  private IType _returnType;
  private final LockingLazyVar<MetaType> _blockType =
    new LockingLazyVar<MetaType>()
    {
      @Override
      protected MetaType init()
      {
        IType type = new BlockType( _returnType, _argTypes.toArray( new IType[_argTypes.size()] ), _argNames, _defValues );
        return MetaType.get( type );
      }
    };

  public BlockLiteral()
  {
    super(JavaTypes.FUNCTION_TYPE());
  }

  /**
   * @param paramTypes If this is a parameterized type, these are the parameter types.
   */
  public void setArgTypes( List<IType> paramTypes )
  {
    _argTypes = new ArrayList<IType>();
    for( IType paramType : paramTypes )
    {
      _argTypes.add( paramType );
    }
  }

  private IType getTypeFromLiteral( TypeLiteral typeLiteral )
  {
    MetaType metaType = typeLiteral.getType();
    IType type = metaType.getType();
    return type;
  }

  @Override
  public Object clone()
  {
    BlockLiteral clone = new BlockLiteral();
    clone._argTypes = new ArrayList<>( _argTypes );
    clone._returnType = _returnType;
    clone.setLocation( getLocation() );
    return clone;
  }

  public void setReturnType( TypeLiteral returnType )
  {
    _returnType = getTypeFromLiteral( returnType );
  }

  @Override
  public MetaType getTypeImpl()
  {
    return _blockType == null ? null : _blockType.get();
  }

  @Override
  public String toString()
  {
    String returnStr = "block (";
    for( int i = 0; i < _argTypes.size(); i++ )
    {
      IType iIntrinsicType = _argTypes.get( i );
      String name = _argNames.get( i );
      if( name.length() > 0 )
      {
        returnStr += name + ":";
      }
      returnStr += iIntrinsicType.getName();
      if( i < _argTypes.size() - 1 )
      {
        returnStr += ",";
      }
    }
    returnStr += "):";
    returnStr += _returnType.getName();
    return returnStr;
  }

  public void setArgNames( ArrayList<String> argNames )
  {
    _argNames = argNames;
  }

  public void setDefValueExpressions( ArrayList<IExpression> defValues )
  {
    _defValues = defValues;
  }
}
