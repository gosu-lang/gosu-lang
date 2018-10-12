/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.IService;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.gosuc.ICustomParser;
import gw.lang.gosuc.IGosuc;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.IProject;

import java.util.List;
import java.util.Set;

public interface ITypeSystem extends IService
{
  /**
   * Gets the intrinsic type for a given class.<p>
   * <p/>
   * <b>Note:</b> you should use this method only if you do not have an
   * Object of class <code>javaClass</code> to get the type from. If you
   * do have such an object, use {@link #getFromObject} instead.
   *
   * @param javaClass the Class to convert to an intrinsic type
   *
   * @return the IType that corresponds to that class
   *
   * @see #getFromObject(Object)
   */
  IType get( Class<?> javaClass );

  /**
   * Gets the intrinsic type for a given class info object.<p>
   *
   * @param javaClassInfo the Class info object to convert to an intrinsic type
   *
   * @return the IType that corresponds to that class
   */
  IType get(IJavaClassInfo javaClassInfo);

  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   *
   * @return the IType for the object
   *
   * @see #get(Class)
   */
  IType getFromObject( Object object );

  IType getByRelativeName( String relativeName ) throws ClassNotFoundException;

  /**
   * Gets an intrinsic type based on a relative name.  This could either be the name of an entity,
   * like "User", the name of a typekey, like "SystemPermission", or a class name, like
   * "java.lang.String" (relative and fully qualified class names are the same as far as this factory
   * is concerned).  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param relativeName the relative name of the type
   * @param typeUses     the map of used types to use when resolving
   *
   * @return the corresponding IType
   *
   * @throws ClassNotFoundException if the specified name doesn't correspond to any type
   */
  IType getByRelativeName( String relativeName, ITypeUsesMap typeUses ) throws ClassNotFoundException;

  /**
   * Gets an intrinsic type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   *
   * @return the corresponding IType
   *
   * @throws RuntimeException if the specified name doesn't correspond to any type
   */
  IType getByFullName( String fullyQualifiedName );

  /**
   * Gets a type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * This method behaves the same as getByFullName execept instead of throwing it returns null.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   *
   * @return the corresponding IType or null if the type does not exist
   */
  IType getByFullNameIfValid( String fullyQualifiedName );

  IType getByFullNameIfValidNoJava( String fullyQualifiedName );

  void refresh(ITypeRef typeRef);

  void refresh( boolean bRefreshCaches );
  void refresh(IModule module);

  /**
   * @return true if any types were refreshed for this file
   */
  void refreshed(IResource file, String typeName, RefreshKind refreshKind);
  void shutdown();

  String[] getTypesForFile(IModule module, IFile file);

  int getRefreshChecksum();
  int getSingleRefreshChecksum();

  /**
   * Converts a String name of a type into an IType.
   *
   * @throws IllegalArgumentException if the type string doesn't correspond to any known IType
   */
  IType parseType( String typeString ) throws IllegalArgumentException;

  IType parseType( String typeString, ITypeUsesMap typeUsesMap ) throws IllegalArgumentException;

  IType parseType( String typeString, TypeVarToTypeMap actualParamByVarName );
  IType parseType( String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap );

  ITypeLiteralExpression parseTypeExpression( String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap ) throws ParseResultsException;

  IType getComponentType( IType valueType );

  INamespaceType getNamespace( String strFqNamespace );


  /**
   * Returns all type names in the system for all type loaders.
   * @return all type names in the system.
   */
  Set<? extends CharSequence> getAllTypeNames();

  ITypeVariableType getOrCreateTypeVariableType( String strName, IType boundingType, IType enclosingType );

  IFunctionType getOrCreateFunctionType( IMethodInfo mi );
  IFunctionType getOrCreateFunctionType( String strFunctionName, IType retType, IType[] paramTypes );

  TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType );

  IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars );

  void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap map );
  void inferTypeVariableTypesFromGenParamTypeAndConcreteType_Reverse( IType genParamType, IType argType, TypeVarToTypeMap map );

  IErrorType getErrorType();
  IErrorType getErrorType( String strErrantName );
  IErrorType getErrorType( ParseResultsException pe );

  IDefaultTypeLoader getDefaultTypeLoader();

  IType findParameterizedType( IType type, IType rhsType );

  void addTypeLoaderListenerAsWeakRef( ITypeLoaderListener listener );

  Set<String> getNamespacesFromTypeNames( Set<? extends CharSequence> allTypeNames, Set<String> namespaces );

  void pushTypeLoader( ITypeLoader loader );
  void pushTypeLoader( IModule module, ITypeLoader loader );
  void removeTypeLoader( Class<? extends ITypeLoader> loader );

  void pushIncludeAll();
  void popIncludeAll();
  boolean isIncludeAll();

  IType getCurrentCompilingType();
  IType getCompilingType( String strName );
  void pushCompilingType(IType type);
  void popCompilingType();

  void pushSymTableCtx( ISymbolTable ctx );
  void popSymTableCtx();
  ISymbolTable getSymTableCtx();

  <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderClass, IModule module );

  String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType );

  ISymbolTable getCompiledGosuClassSymbolTable();

  List<ITypeLoader> getAllTypeLoaders();

  IType getJavaType(Class javaClass);

  String getNameWithQualifiedTypeVariables(IType type);

  IType getDefaultParameterizedType(IType type);
  IType getDefaultParameterizedTypeWithTypeVars(IType type);

  boolean canCast(IType lhsType, IType rhsType);

  void removeTypeLoaderListener(ITypeLoaderListener listener);

  IJavaType getPrimitiveType(String name);

  IType getPrimitiveType(IType boxType);

  IType getBoxType(IType primitiveType);

  IType getExpandableComponentType( IType type );

  IExecutionEnvironment getExecutionEnvironment();
  IExecutionEnvironment getExecutionEnvironment( IProject project );

  IModule getCurrentModule();

  ITypeRef getOrCreateTypeReference( IType type );

  ITypeRef getTypeReference( IType type );

  IType getTypeFromObject( Object obj );

  boolean isExpandable( IType type );

  void clearErrorTypes();

  IType boundTypes(IType parameterType, List<IType> inferringTypes);

  IGosuClassLoader getGosuClassLoader();

  void dumpGosuClassLoader();

  IMetaType getDefaultType();

  void addShutdownListener(TypeSystemShutdownListener listener);

  void pushModule(IModule gosuModule);

  void popModule(IModule gosuModule);

  IType replaceTypeVariableTypeParametersWithBoundingTypes( IType iType, IType type );

  IGosuc makeGosucCompiler( String gosucProjectFile, ICustomParser custParser );

  boolean isParameterizedWith( IType type, ITypeVariableType... typeVar );

  IType getCompoundType( Set<IType> types );

  IType getFunctionalInterface( IFunctionType type );
}
