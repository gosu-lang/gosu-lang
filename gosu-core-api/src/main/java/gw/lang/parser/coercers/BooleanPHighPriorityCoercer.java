/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class BooleanPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final BooleanPHighPriorityCoercer INSTANCE = new BooleanPHighPriorityCoercer();

  public BooleanPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.BooleanPCoercer, MAX_PRIORITY );
  }

  public static BooleanPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}