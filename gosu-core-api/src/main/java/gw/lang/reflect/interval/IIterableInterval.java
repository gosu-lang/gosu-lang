/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import java.util.Iterator;

public interface IIterableInterval<E extends Comparable<E>, S, U, ME extends IIterableInterval<E, S, U, ME>> extends Iterable<E>, IInterval<E, ME>
{
  /**
   * @return An iterator that visits the elements in this interval in order, from left to right.
   *   Returns null if this interval does not support iteration.
   *
   * @see #iterateFromLeft()
   * @see #iterateFromRight()
   */
  Iterator<E> iterator();

  /**
   * @return An iterator that visits the elements in this interval in order, from left to right.
   *   Returns null if this interval does not support iteration.
   *
   * @see #iterator()
   * @see #iterateFromRight()
   */
  Iterator<E> iterateFromLeft();

  /**
   * @return An iterator that visits the elements in this interval in reverse order, from right to left.
   *   Returns null if this interval does not support iteration.
   *
   * @see #iterator()
   * @see #iterateFromLeft()
   */
  Iterator<E> iterateFromRight();

  /**
   * @return The step (or increment) by which this interval visits elements in its set. Returns null
   *   if this interval cannot iterate its elements.
   * <p>
   * For instance, if the interval is a set of decimal values, say [1..10], the step might be a decimal
   * increment, say 0.25. Similarly, if the interval is simply a set of integers the step might also be
   * an integer value, typically 1. Considering a date interval, say [4/5/10..5/20/10], the step could
   * be expressed in terms of a unit of time e.g., 10 seconds, 1 minute, 2 weeks, etc.
   * <p>
   * Note if non-null, the step is a <i>positive</i> (or absolute) increment. To iterate the interval
   * in reverse order use iterateFromRight().
   */
  S getStep();

  ME step( S s );

  U getUnit();
  ME unit( U u );

  /**
   * @param iStepIndex The index of the step from the left endpoint
   * @return The nth step from the left endpoint. Returns null if iStepIndex is out of bounds.
   * @thows IllegalArgumentException if iStepIndex is < 0
   */
  E getFromLeft( int iStepIndex );

  /**
   * @param iStepIndex The index of the step from the right endpoint
   * @return The nth step from the right endpoint. Returns null if iStepIndex is out of bounds.
   * @thows IllegalArgumentException if iStepIndex is < 0
   */
  E getFromRight( int iStepIndex );
}