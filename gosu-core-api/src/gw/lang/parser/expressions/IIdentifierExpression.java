/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IFeatureInfo;

public interface IIdentifierExpression extends IExpression
{
  ISymbol getSymbol();

  void setSymbol( ISymbol symbol, ISymbolTable symTable );
  
  boolean isLocalVariable();

  static final class FeatureInfoWrapper
  {
    public IFeatureInfo owningFeatureInfo;

    public FeatureInfoWrapper(IFeatureInfo owningFeatureInfo) {
      this.owningFeatureInfo = owningFeatureInfo;
    }

    public static <T extends IParsedElement> T findEldestAncestor( Class<T> type, IParsedElement parsedElement )
    {
      T ancestor = null;
      IParsedElement parent = parsedElement.getParent();
      while( parent != null && parent.getParent() != null )
      {
        parent = parent.getParent();
        if( type.isAssignableFrom( parent.getClass() ) )
        {
          //noinspection unchecked
          ancestor = (T)parent;
        }
      }
      return ancestor;
    }

  }
}
