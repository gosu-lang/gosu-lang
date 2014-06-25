/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.IDimension;

/**
 */
public final class DimensionOperandResolver
{
  private IType _numType;
  private IType _lhsType;
  private IType _rhsType;
  private Object _lhsValue;
  private Object _rhsValue;
  private IDimension _base;

  public static DimensionOperandResolver resolve( IType exprType, IType lhsType, Object lhsValue, IType rhsType, Object rhsValue )
  {
    return new DimensionOperandResolver( exprType, lhsType, lhsValue, rhsType, rhsValue ).resolve();
  }

  private DimensionOperandResolver( IType exprType, IType lhsType, Object lhsValue, IType rhsType, Object rhsValue )
  {
    _numType = exprType;
    _lhsType = lhsType;
    _lhsValue = lhsValue;
    _rhsType = rhsType;
    _rhsValue = rhsValue;
  }

  private DimensionOperandResolver resolve()
  {
    resolveNumberTypeAndValues();
    return this;
  }

  private void resolveNumberTypeAndValues()
  {
    if( _lhsType == _numType && JavaTypes.IDIMENSION().isAssignableFrom( _lhsType ) )
    {
      _base = (IDimension)_lhsValue;
      if( _rhsType == _numType )
      {
        _rhsValue = ((IDimension)_rhsValue).toNumber();
      }
      _lhsValue = _base.toNumber();
      _numType = TypeSystem.get( _base.numberType() );
    }
    else
    {
      _base = (IDimension)_rhsValue;
      _rhsValue = _base.toNumber();
      _numType = TypeSystem.get( _base.numberType() );
    }
  }

  public IDimension getBase()
  {
    return _base;
  }

  public IType getRawNumberType()
  {
    return _numType;
  }

  public Object getLhsValue()
  {
    return _lhsValue;
  }

  public Object getRhsValue()
  {
    return _rhsValue;
  }
}
