/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IBooleanLiteralExpression;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

/**
 * A literal expression for Boolean values.
 */
public final class BooleanLiteral extends Literal implements IBooleanLiteralExpression
{
  public static final LockingLazyVar<BooleanLiteral> TRUE = new LockingLazyVar<BooleanLiteral>() {
    protected BooleanLiteral init() {
      return new BooleanLiteral( true );
    }
  };
  public static final LockingLazyVar<BooleanLiteral> FALSE = new LockingLazyVar<BooleanLiteral>() {
    protected BooleanLiteral init() {
      return new BooleanLiteral( false );
    }
  };
  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        TRUE.clear();
        FALSE.clear();
      }
    });
  }

  protected Boolean _bValue;

  /**
   * Note this is private to enforce the use of the TRUE and FALSE constants.
   */
  public BooleanLiteral( boolean bValue )
  {
    _bValue = bValue ? Boolean.TRUE : Boolean.FALSE;
    setType( JavaTypes.pBOOLEAN() );
  }

  public Boolean getValue()
  {
    return _bValue;
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public Object evaluate()
  {
    return getValue();
  }

  @Override
  public String toString()
  {
    return String.valueOf( getValue() );
  }

}
