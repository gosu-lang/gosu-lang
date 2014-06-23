/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class ShortHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final ShortHighPriorityCoercer INSTANCE = new ShortHighPriorityCoercer();

  public ShortHighPriorityCoercer()
  {
    super( ShortCoercer.instance(), MAX_PRIORITY );
  }

  public static ShortHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}