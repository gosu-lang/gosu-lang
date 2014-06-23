/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class FloatPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final FloatPHighPriorityCoercer INSTANCE = new FloatPHighPriorityCoercer();

  public FloatPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.FloatPCoercer, MAX_PRIORITY );
  }

  public static FloatPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}