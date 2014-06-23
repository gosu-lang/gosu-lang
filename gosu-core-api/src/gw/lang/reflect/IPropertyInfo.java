/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;


public interface IPropertyInfo extends IAttributedFeatureInfo, IIntrinsicTypeReference
{
  /**
   * @return true if the property is readable given the visibility constraint passed in,
   *         false otherwise.
   */
  public boolean isReadable();

  /**
   * @return True if the property is writable given the visibility constraint passed in,
   *         false otherwise.
   */
  public boolean isWritable();

  /**
   * @param whosAskin The type querying the property writability.  For example, passing in the type the property is on will
   *   allow properties that have private access in the setter to be written to.  Passing in an unrelated type will only
   *   allow properties that are public to be written to.
   *
   * @return True if the property is writable given the visibility constraint passed in,
   *         false otherwise.
   */
  public boolean isWritable( IType whosAskin );

  /**
   * @return the PropertyAccessor (for getting and setting) this property.
   */
  public IPropertyAccessor getAccessor();

  public IPresentationInfo getPresentationInfo();
}
