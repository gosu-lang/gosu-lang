/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class FloatHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final FloatHighPriorityCoercer INSTANCE = new FloatHighPriorityCoercer();

  public FloatHighPriorityCoercer()
  {
    super( FloatCoercer.instance(), MAX_PRIORITY );
  }

  public static FloatHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}