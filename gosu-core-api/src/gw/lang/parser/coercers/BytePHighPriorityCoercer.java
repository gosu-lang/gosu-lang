/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class BytePHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final BytePHighPriorityCoercer INSTANCE = new BytePHighPriorityCoercer();

  public BytePHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.BytePCoercer, MAX_PRIORITY );
  }

  public static BytePHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}