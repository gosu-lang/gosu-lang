/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IExpression;

import gw.lang.parser.TypeVarToTypeMap;
import java.util.List;

public final class MethodScore implements Comparable<MethodScore>
{
  private double _score;
  private boolean _bValid;
  private IInvocableType _rawFuncType;
  private IInvocableType _inferredFuncType;
  private IType _receiverType;
  private IRelativeTypeInfo.Accessibility _acc;
  private List<IExpression> _exprs;
  private List _parserStates;
  private int[] _namedArgOrder;
  private TypeVarToTypeMap _inferenceMap;

  public MethodScore( IRelativeTypeInfo.Accessibility acc, IType receiverType )
  {
    _receiverType = receiverType;
    _acc = acc;
  }

  public MethodScore( IType receiverType, IType callsiteEnclosingType )
  {
    _receiverType = receiverType;
    if( receiverType != null && callsiteEnclosingType != null )
    {
      if( receiverType instanceof IMetaType )
      {
        receiverType = ((IMetaType)receiverType).getType();
      }
      _acc = FeatureManager.getAccessibilityForClass( receiverType, callsiteEnclosingType );
    }
    else
    {
      _acc = IRelativeTypeInfo.Accessibility.NONE;
    }
  }

  /**
   * @return true if this score represents an actual matching method score rather than
   * just a placeholder indicating that no method matched
   */
  public boolean isValid()
  {
    return _bValid;
  }

  public double getScore()
  {
    return _score;
  }
  public void setScore( double score )
  {
    _score = score;
  }
  public void incScore( double amount )
  {
    _score += amount;
  }

  public void setValid( boolean valid )
  {
    _bValid = valid;
  }

  public IInvocableType getRawFunctionType()
  {
    return _rawFuncType;
  }
  public void setRawFunctionType( IInvocableType funcType )
  {
    _rawFuncType = funcType;
  }

  public IInvocableType getInferredFunctionType()
  {
    return _inferredFuncType;
  }
  public void setInferredFunctionType( IInvocableType funcType )
  {
    _inferredFuncType = funcType;
  }

  public IType getReceiverType()
  {
    return _receiverType;
  }

  public IRelativeTypeInfo.Accessibility getAccessibility()
  {
    return _acc;
  }

  public int compareTo( MethodScore o )
  {
    // if the scores are the same, compare their signatures for great stability justice
    if( _score == o._score )
    {
      return o._rawFuncType.getParamSignature().toString().compareTo( _rawFuncType.getParamSignature().toString() );
    }
    else
    {
      return _score > o._score ? 1 : -1;
    }
  }

  public List<IExpression> getArguments()
  {
    return _exprs;
  }
  public void setArguments( List<IExpression> argExpressions )
  {
    _exprs = argExpressions;
  }

  public List getParserStates()
  {
    return _parserStates;
  }
  public void setParserStates( List parserStates )
  {
    _parserStates = parserStates;
  }

  public boolean matchesArgSize()
  {
    return _rawFuncType.getParameterTypes().length == _exprs.size();
  }

  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
  public void setNamedArgOrder( List<Integer> namedArgOrder )
  {
    if( namedArgOrder != null )
    {
      _namedArgOrder = new int[namedArgOrder.size()];
      for( int i = 0; i < _namedArgOrder.length; i++ )
      {
        _namedArgOrder[i] = namedArgOrder.get( i );
      }
    }
    else
    {
      _namedArgOrder = null;
    }
  }

  public TypeVarToTypeMap getInferenceMap()
  {
    return _inferenceMap;
  }
  public void setInferenceMap( TypeVarToTypeMap inferenceMap )
  {
    _inferenceMap = inferenceMap;
  }
}