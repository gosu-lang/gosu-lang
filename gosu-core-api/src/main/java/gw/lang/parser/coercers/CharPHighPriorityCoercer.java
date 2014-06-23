/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class CharPHighPriorityCoercer extends BasePHighPriorityCoercer
{
  private static final CharPHighPriorityCoercer INSTANCE = new CharPHighPriorityCoercer();

  public CharPHighPriorityCoercer()
  {
    super( BasePrimitiveCoercer.CharPCoercer, MAX_PRIORITY );
  }

  public static CharPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}