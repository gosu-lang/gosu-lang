/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class LongPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final LongPHighPriorityCoercer INSTANCE = new LongPHighPriorityCoercer();

  public LongPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.LongPCoercer, MAX_PRIORITY );
  }

  public static LongPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}