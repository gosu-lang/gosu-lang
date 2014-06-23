/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.Keyword;
import gw.lang.parser.expressions.INullExpression;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.util.concurrent.LockingLazyVar;

/**
 * An expression representing a 'null' expression i.e., the null keyword.
 */
public final class NullExpression extends Literal implements INullExpression
{
  private static LockingLazyVar<NullExpression> NULL = new LockingLazyVar<NullExpression>() {
    protected NullExpression init() {
      return new NullExpression();
    }
  };
  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        NULL.clear();
      }
    });
  }


  public NullExpression()
  {
    setType( GosuParserTypes.NULL_TYPE() );
  }

  public static NullExpression instance()
  {
    return NULL.get();
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public Object evaluate()
  {
    return null;
  }

  @Override
  public String toString()
  {
    return Keyword.KW_null.toString();
  }

}
