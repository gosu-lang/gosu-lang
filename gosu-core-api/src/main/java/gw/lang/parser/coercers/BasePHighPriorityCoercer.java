/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.util.concurrent.LockingLazyVar;

public class BasePHighPriorityCoercer extends BaseCoercer
{
  private final LockingLazyVar<BasePrimitiveCoercer> _delegate;
  private final int _priority;

  public BasePHighPriorityCoercer(LockingLazyVar<BasePrimitiveCoercer> delegate, int priority)
  {
    _delegate = delegate;
    _priority = priority;
  }

  public final Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return _delegate.get().coerceValue(typeToCoerceTo, value);
  }

  public boolean isExplicitCoercion()
  {
    return _delegate.get().isExplicitCoercion();
  }

  public boolean handlesNull()
  {
    return _delegate.get().handlesNull();
  }

  public int getPriority( IType to, IType from )
  {
    return _priority;
  }
}
