/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class IntPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final IntPHighPriorityCoercer INSTANCE = new IntPHighPriorityCoercer();

  public IntPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.IntPCoercer, MAX_PRIORITY );
  }

  public static IntPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}