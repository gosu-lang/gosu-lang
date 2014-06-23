/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class ShortPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final ShortPHighPriorityCoercer INSTANCE = new ShortPHighPriorityCoercer();

  public ShortPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.ShortPCoercer, MAX_PRIORITY );
  }

  public static ShortPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}