/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class DoubleHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final DoubleHighPriorityCoercer INSTANCE = new DoubleHighPriorityCoercer();

  public DoubleHighPriorityCoercer()
  {
    super( DoubleCoercer.instance(), MAX_PRIORITY );
  }

  public static DoubleHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}