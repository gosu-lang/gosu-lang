/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

public class ByteHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final ByteHighPriorityCoercer INSTANCE = new ByteHighPriorityCoercer();

  public ByteHighPriorityCoercer()
  {
    super( ByteCoercer.instance(), MAX_PRIORITY );
  }

  public static ByteHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}