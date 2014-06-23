/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

public final class ComparableInterval<E extends Comparable<E>> extends AbstractInterval<E, ComparableInterval<E>>
{
  public ComparableInterval( E left, E right )
  {
    this( left, right, true, true, false );
  }

  public ComparableInterval( E left, E right, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, bLeftClosed, bRightClosed, bReverse );
  }
}