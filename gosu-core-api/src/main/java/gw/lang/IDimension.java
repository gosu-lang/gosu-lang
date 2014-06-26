/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

public interface IDimension<S extends IDimension<S,T>, T extends Number> extends Comparable<S>
{
  /**
   * The Gosu runtime calls this method when performing default operations.
   * For instance, when adding two of the same dimension types, Gosu calls
   * this method on each operand, adds the numbers, and then calls fromNumber()
   * for the result.
   *
   * @return the number of units for this dimension instance.
   */
  T toNumber();

  /**
   * Returns a separate instance of this type with the given number of units.
   * <p>
   * The Gosu runtime calls this method when performing default operations.
   * For instance, when adding two of the same dimension types, Gosu calls
   * toNumber() on each operand, adds the numbers, and then calls fromNumber()
   * for the result.
   *
   * @return a separate instance of this type given the number of units.
   */
  S fromNumber( T units );

  /**
   * @return The static Number derivation for this class. Must be the same
   *   as the T parameter.  Note this is only useful in Java where the type
   *   is lost at runtime due to type erasure.
   */
  Class<T> numberType();
}