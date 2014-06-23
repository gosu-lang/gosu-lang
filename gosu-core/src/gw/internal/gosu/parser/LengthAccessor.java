/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.LoopStatement;
import gw.lang.parser.EvaluationException;
import gw.lang.reflect.IPropertyAccessor;

public class LengthAccessor implements IPropertyAccessor
{

  public static final LengthAccessor INSTANCE = new LengthAccessor();

  private LengthAccessor()
  {
  }

  @Override
  public Object getValue( Object ctx )
  {
    if( ctx == null )
    {
      return null;
    }
    return LoopStatement.getArrayLength(ctx);
  }

  @Override
  public void setValue( Object ctx, Object value )
  {
    throw new EvaluationException( "\"length\" is a read-only property." );
  }
}
