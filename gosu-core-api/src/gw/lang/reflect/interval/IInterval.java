/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;


public interface IInterval<E extends Comparable<E>, ME extends IInterval<E, ME>>
{
  /**
   * @return The left endpoint of this interval where the left <= right
   */
  E getLeftEndpoint();

  /**
   * @return The right endpoint of this interval where the left <= right
   */
  E getRightEndpoint();

  /**
   * @return True if this interval <i>includes</i> the left endpoint.
   * E.g., x >= foo indicates a left-closed interval starting with and including
   * foo. Conversely, x > foo is said to be left-open because the interface
   * starts with, but excludes foo i.e., there is no minimum value defined in
   * the interval, rather the interval is open with foo as the limit of minimum
   * values.
   */
  boolean isLeftClosed();

  /**
   * @return True if this interval <i>includes</i> the right endpoint.
   * E.g., x <= foo indicates a right-closed interval ending with and including
   * foo. Conversely, x < foo is said to be right-open because the interface
   * ends with, but excludes foo i.e., there is no maximum value defined in the
   * interval, rather the interval is open with foo as the limit of maximum
   * values.
   */
  boolean isRightClosed();

  /**
   * @param elem An element to test
   * @return True if elem is a proper element in the set of elements defining this interval.
   */
  boolean contains( E elem );

  /**
   * @param interval An interval to test for containment
   * @return True if interval's endpoints are proper elements in the set of elements defining this interval.
   */
  boolean contains( ME interval );

  /**
   * @return True if this interval iterates from the right by default e.g.,
   *   if the interval is specified in reverse order: 10..1, Gosu will
   *   create a reverse intervall
   */
  boolean isReverse();
}
