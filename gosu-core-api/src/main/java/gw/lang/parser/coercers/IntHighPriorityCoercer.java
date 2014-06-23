/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class IntHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final IntHighPriorityCoercer INSTANCE = new IntHighPriorityCoercer();

  public IntHighPriorityCoercer()
  {
    super( IntCoercer.instance(), MAX_PRIORITY );
  }

  public static IntHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}