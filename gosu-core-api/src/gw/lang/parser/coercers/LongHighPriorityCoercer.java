/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class LongHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final LongHighPriorityCoercer INSTANCE = new LongHighPriorityCoercer();

  public LongHighPriorityCoercer()
  {
    super( LongCoercer.instance(), MAX_PRIORITY );
  }

  public static LongHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}