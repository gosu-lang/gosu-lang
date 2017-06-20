/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IIntrinsicTypeReference
{
  /**
   * The type of this feature e.g., for a property this is the property's type.
   */
  IType getFeatureType();

  /**
   * The type this feature can be assigned from.  Most of the time this is the
   * same type as getFeatureType(), however it can be different and when it is
   * it should almost always be contravariant with getFeatureType().
   * <p/>
   * A property setter method is the primary use-case.  For instance:
   * <pre>
   *   interface Foo {
   *     property Name: CharSequence
   *   }
   *
   *   class FooImpl implements Foo {
   *     var _name: String
   *     override property get Name(): String {
   *       return _name
   *     }
   *     override property set Name( cs: CharSequence ) {
   *       _name = cs.toString()
   *     }
   *   }
   * </pre>
   * Here FooImpl overrides Foo's Name property with covariance on the
   * getter's return type.  This establishes that Name wrt FooImpl is of
   * type String.  Yet Foo's setter method must be overridden as-is,
   * a method cannot override another method with covariant parameters.
   * So, in terms of FooImpl's Name property, the setter method is
   * <i>contravariant</i> with String, which again is Name's type wrt
   * of FooImpl.  Therefore, FooImpl's Name property should implement
   * getAssignableFeatureType() and return CharSequence.
   */
  default IType getAssignableFeatureType()
  {
    return getFeatureType();
  }
}
