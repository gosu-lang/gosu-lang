/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IType;

import java.io.Serializable;
import java.util.Set;

public interface ITypeUsesMap extends Serializable
{
  /**
   * Returns the set of strings representing the types that are currently
   * used by this parser. The set of types includes both those declared in
   * #uses statements and those set via setDefaultTypeUses.
   */
  Set<String> getTypeUses();

  /**
   * Returns the set of strings representing the namespaces that are currently
   * used by this parser.
   */
  Set<String> getNamespaces();

  Set<String> getFeatureSpaces();
  Set<IFeatureInfo> getFeatureLiterals();

  /**
   * Adds a type to the current set of types. Can be a complete type or a
   * wildcard namespace e.g., java.util.HashMap and java.util.* are both legal.
   */
  void addToTypeUses( String strType );

  void addToFeatureSpaces( String strType );
  void addToFeatureLiterals( IFeatureInfo fi );

  /**
   * Adds a type to the current set of types. Can be a complete type or a
   * wildcard namespace e.g., java.util.HashMap and java.util.* are both legal.
   */
  void addToTypeUses( IUsesStatement usesStmt );

  /**
   * Return the set of uses-statements that participate in this map. Note these
   * are the uses-statements compiled from source.
   */
  Set<IUsesStatement> getUsesStatements();

  /**
   * Add the specified type to the set of default types.  NOTE:  The type is always treated as a package.  If it ends in .* then it will be stripped
   */
  void addToDefaultTypeUses( String strQualifiedType );

  /**
   * Resolve the type of a relative name via the type uses. if the relative type
   * matches uses-type, resolves the type as such. If the type matches, but does
   * not resove, throws an exception, otherwise returns null if there is no
   * match.
   */
  IType resolveType( String strRelativeName );

  /**
   * @param strRelativeName A relative path name. E.g., "lang" is a relative package name of "java.lang"
   * @return The absolute namespace type for the relative name or null if not found.
   */
  INamespaceType resolveRelativeNamespaceInAllNamespaces( String strRelativeName );

  /**
   * Clears all types not in the default set of types;
   */
  void clearNonDefaultTypeUses();

  /**
   * Returns a shallow copy of this map.
   */
  ITypeUsesMap copy();

  /**
   * Copies the type uses map but alias the global map from this.
   *
   * @return a copy of this type uses map.
   */
  ITypeUsesMap copyLocalScope();

  /**
   * Locks this ITypeUsesMap so that it cannot be mutated in the future
   *
   * @return this ITypeUsesMap, so that this method can be used in a builder-like manner.
   */
  ITypeUsesMap lock();

  /**
   * @return if a type wit the given name can be resolved via this type uses map.
   */
  boolean containsType(String qualifiedName);

  void addToSpecialTypeUses( String strType );

  boolean isSupportRelativePackageResolution();
  void setSupportRelativePackageResolution( boolean bSupportRelativePackageResolution );
}
