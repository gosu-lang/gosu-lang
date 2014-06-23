/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests.gwtest.annotations;

import gw.lang.parser.IUsageSiteValidator;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IUsageSiteValidatorReference;
import gw.lang.parser.resources.Res;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.BlockExpression;

/**
 */
public class HasMethodCallValidator implements IUsageSiteValidator
{	
  @IUsageSiteValidatorReference(HasMethodCallValidator.class)
  public static String test( Object target )
  {
    return target.toString();
  }

  @Override
  public void validate( IParsedElement pe )
  {
    if( pe instanceof BeanMethodCallExpression )
    {
      BeanMethodCallExpression expr = (BeanMethodCallExpression)pe;
      IExpression[] args = expr.getArgs();
      if( args != null && args.length == 1 )
      {
        if( !(args[0] instanceof BlockExpression) )
        {
          expr.addParseException( Res.MSG_ANY, "Must pass single argument that is a block expression with no arguments" );
        }
      }
    }
  }

}
