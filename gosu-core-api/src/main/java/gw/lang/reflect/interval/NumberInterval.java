/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

public abstract class NumberInterval<E extends Number & Comparable<E>, ME extends NumberInterval<E, ME>> extends IterableInterval<E, E, Void, ME>
{
  @SuppressWarnings({"UnusedDeclaration"})
  public NumberInterval( E left, E right, E step )
  {
    this( left, right, step, true, true, false );
  }

  public NumberInterval( E left, E right, E step, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, step, null, bLeftClosed, bRightClosed, bReverse );
  }
}