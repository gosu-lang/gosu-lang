/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class CharHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final CharHighPriorityCoercer INSTANCE = new CharHighPriorityCoercer();

  public CharHighPriorityCoercer()
  {
    super( CharCoercer.instance(), MAX_PRIORITY );
  }

  public static CharHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}