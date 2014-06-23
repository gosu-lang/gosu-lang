/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPropertyInfo;

import java.util.List;

public interface IQueryPathExpression extends IExpression, IQueryPartAssembler
{
  IExpression getDelegate();

  IType getRootType();

  String getRootName();

  List<String> getAccessPath();

  ISymbol getSymbol();

  IPropertyInfo getPropertyInfo();

  public class Util
  {
    public static boolean isQueryPathExpressionPart( IFieldAccessExpression ma )
    {
      while( true )
      {
        IParsedElement parent = ma.getParent();
        if( parent instanceof IQueryPathExpression )
        {
          return true;
        }
        if( parent instanceof IFieldAccessExpression )
        {
          ma = (IFieldAccessExpression)parent;
        }
        else
        {
          return false;
        }
      }
    }
  }
}
