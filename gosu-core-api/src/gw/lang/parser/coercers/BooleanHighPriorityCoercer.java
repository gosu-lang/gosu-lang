/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class BooleanHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final BooleanHighPriorityCoercer INSTANCE = new BooleanHighPriorityCoercer();

  public BooleanHighPriorityCoercer()
  {
    super( BooleanCoercer.instance(), MAX_PRIORITY );
  }

  public static BooleanHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}