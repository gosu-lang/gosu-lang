/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;


public interface IType extends Serializable
{
  ArrayList<IType> EMPTY_TYPE_LIST = new ArrayList<>(0);
  IType[] EMPTY_TYPE_ARRAY = new IType[0];
  IType[] EMPTY_ARRAY = new IType[0];

  /**
   * The fully qualified name of this intrinsic type.
   */
  String getName();

  /**
   * The display name of this intrinsic type.  Often this is simply the same as
   * {@link #getName()}, but in some cases a type may want to modify it's name
   * for presentation to the user.
   */
  String getDisplayName();

  /**
   * The relative or unqualified name. For a class this should be just the class
   * name without the package name.
   */
  String getRelativeName();

  /**
   * The namespace for this class.  For a java class this will be the package, while
   * for other types of intrinsic types it could be another logical name.
   */
  String getNamespace();

  /**
   * Returns the type loader responsible for loading this intrinsic type.
   */
  ITypeLoader getTypeLoader();

  /**
   * Returns the type representing the supertype of this type. Returns null if
   * this type has no supertype.
   */
  IType getSupertype();

  /**
   * Returns the type immediately enclosing this type. If this type is not
   * enclosed, returns null.
   */
  IType getEnclosingType();

  /**
   * If this is a parameterized type, returns the generic type this type
   * parameterizes. Otherwise, returns null.
   */
  IType getGenericType();

  /**
   * True if this type cannot be extended.
   */
  boolean isFinal();

  /**
   * Returns true if this type is an interface.
   */
  boolean isInterface();

  /**
   * Returns true if this type is an enumeration.
   */
  boolean isEnum();

  /**
   * @return If this is a class, returns a list of all the interfaces this type
   *         <i>implements</i>. Similarly, if this is an interface, returns a list of
   *         all the interfaces this type <i>extends</i>. In any case, returns an
   *         empty list if this type neither implements nor extends interfaces.
   */
  IType[] getInterfaces();

  /**
   * Returns true if this ia a Parameterized Type.
   * <p/>
   * Note a Parameterzied Type is not the same as a Generic Type. The difference
   * is that a Parameterized Type is a concrete version of a Generic Type. For
   * instance, the class ArrayList<T> is a Generic Type, while the class
   * ArrayList<String> is a Parameterized Type of the the Generic Type ArrayList<T>.
   */
  boolean isParameterizedType();

  /**
   * Returns true if this ia a Generic Type.
   *
   * @see #isParameterizedType()
   */
  boolean isGenericType();

  /**
   * Returns an array of GenericTypeVariables declared with this Generic Type.
   * Otherwise, returns null if this is not a Generic Type.
   */
  IGenericTypeVariable[] getGenericTypeVariables();

  /**
   * Assuming this intrinsic type is a Generic type, return the parameterized
   * type associated with the given list of type parameters. If the type has not
   * been created yet, creates the parameterized type. Successive calls to this
   * method having identical parameter types must return the identical
   * parameterized type.
   *
   * @param ofType The type parameters.
   *
   * @return The concrete type associated with the type parameters.
   */
  IType getParameterizedType( IType... ofType );

  /**
   * If this is a parameterized type, returns the specific types used to create
   * this type, null otherwies.
   */
  IType[] getTypeParameters();

  /**
   * Returns a Set of all IIntrinsicTypes that this class is assignable from,
   * including this class. This includes implemented interfaces and
   * superclasses, recursively up the hieararchy. For array types, this will be
   * a Set of all types that its component type is assignable from.
   */
  Set<? extends IType> getAllTypesInHierarchy();

  /**
   * True if this is an array.
   */
  boolean isArray();

  /**
   * True if this type represents a primitive type e.g., Java int, char, etc.
   */
  boolean isPrimitive();

  /**
   * Make an array type from this type. E.g., String -> String[]
   */
  IType getArrayType();

  /**
   * Construct an array instance of specified length.
   */
  Object makeArrayInstance( int iLength );

  /**
   * Returns the value of the indexed component in the specified
   * array object.
   *
   * @param array  An array instance of this intrinsic type.
   * @param iIndex The index of the component to get.
   *
   * @return The value of the indexed component in the specified array.
   *
   * @throws NullPointerException           If the specified object is null,
   * @throws IllegalArgumentException       If the specified object is not an array.
   * @throws ArrayIndexOutOfBoundsException If the specified index argument
   *                                        is negative, or if it is greater than or equal to the length of the
   *                                        specified array
   */
  Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

  /**
   * Sets the value of the indexed component in the specified array object.
   *
   * @param array  An array instance of this intrinsic type.
   * @param iIndex The index of the component to set.
   * @param value  The new value of the indexed component.
   *
   * @throws NullPointerException           If the specified object is null,
   * @throws IllegalArgumentException       If the specified object is not an array.
   * @throws ArrayIndexOutOfBoundsException If the specified index argument
   *                                        is negative, or if it is greater than or equal to the length of the
   *                                        specified array
   */
  void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

  /**
   * Returns the length of the specified array object.
   *
   * @param array An array instance of this intrinsic type.
   *
   * @return The length of the array.
   *
   * @throws IllegalArgumentException If the object argument is not an array.
   */
  int getArrayLength( Object array ) throws IllegalArgumentException;


  /**
   * If this is an array type, a type representing the component type of the
   * array. Otherwise null.
   */
  IType getComponentType();

  /**
   * Determines if the type represented by this intrinsic type is either the
   * same as, or is a super-type of the type represented by the specified type
   * parameter.
   */
  boolean isAssignableFrom( IType type );

  /**
   * Are intances of this type mutable? Note sometimes it's difficult to
   * determine. For instance, java classes don't contain any information about
   * mutability. In which case we always assume mutable, even when the
   * underlying type may in fact be immutable. E.g., even though java.lang.String
   * is not mutable, its corresponding JavaIntrinsicType will say it is.
   *
   * @return True if this type is mutable.
   */
  boolean isMutable();

  /**
   * Get the type information for this intrinsic type.
   *
   * @see ITypeInfo
   */
  ITypeInfo getTypeInfo();

  /**
   * Unload or nullify any references to this IType's ITypeInfo.
   */
  void unloadTypeInfo();

  /**
   * IType requires this method be implemented to ensure IType
   * instances can be centrally defined and cached.
   */
  Object readResolve() throws ObjectStreamException;

  /**
   * Defines this type if it is not yet fully defined. For instance, if this
   * type has source code associated with it, it must fully compile the source
   * for it to be fully defined.
   *
   * @return true if this type is valid.
   */
  boolean isValid();

  /**
   * Returns the modifiers for this type, encoded in an integer.
   * The modifiers consist of the constants for <code>public</code>, <code>protected</code>,
   * <code>private</code>, <code>final</code>, <code>static</code>,
   * <code>abstract</code> they should be decoded
   * using the methods of class <code>Modifier</code>.
   *
   * @see gw.lang.reflect.Modifier
   */
  int getModifiers();

  boolean isAbstract();

  /**
   * True if this type has been replaced with a newer version of the same name in the type system.
   */
  boolean isDiscarded();
  void setDiscarded( boolean bDiscarded );

  boolean isCompoundType();

  Set<IType> getCompoundTypeComponents();

  IMetaType getMetaType();

  IMetaType getLiteralMetaType();
  
  default IFile[] getSourceFiles() {
    return IFile.EMPTY_ARRAY;
  }
}

