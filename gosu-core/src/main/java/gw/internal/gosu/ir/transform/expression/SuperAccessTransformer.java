/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.SuperAccess;
import gw.lang.ir.IRExpression;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.gs.ICompilableType;

/**
 */
public class SuperAccessTransformer extends AbstractExpressionTransformer<SuperAccess>
{
  public static IRExpression compile( TopLevelTransformationContext cc, SuperAccess expr )
  {
    SuperAccessTransformer compiler = new SuperAccessTransformer( cc, expr );
    return compiler.compile();
  }

  private SuperAccessTransformer( TopLevelTransformationContext cc, SuperAccess expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    ICompilableType gsClass = getGosuClass();
    if( _cc().isBlockInvoke() && _cc().currentlyCompilingBlock() )
    {
      while( gsClass instanceof IBlockClass )
      {
        gsClass = gsClass.getEnclosingType();
      }
      return pushOuter( gsClass );
    }
    else
    {
      return pushThis();
    }
  }
}
