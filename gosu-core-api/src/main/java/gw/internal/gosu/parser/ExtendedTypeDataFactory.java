package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaType;

public interface ExtendedTypeDataFactory {

  /**
   * Creates a new instance of a type extension for the type with the given name.
   *
   * @param name the name of the type
   */
  ExtendedTypeData newTypeData(String name);

  /**
   * Creates a new instance of a property extension for a property with the given name.
   *
   * @param type the type
   * @param name the name of the property
   */
  ExtendedTypeData newPropertyData(IJavaType type, String name);
}
