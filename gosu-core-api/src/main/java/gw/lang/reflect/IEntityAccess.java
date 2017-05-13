/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import manifold.api.fs.IDirectory;
import gw.internal.gosu.parser.ExtendedTypeDataFactory;
import gw.lang.UnstableAPI;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ILanguageLevel;
import gw.lang.reflect.gs.ICompilableType;
import gw.util.ILogger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import manifold.api.service.IService;


@UnstableAPI
public interface IEntityAccess extends IService
{
  /**
   * Get a set of type names that are automatically imported
   *
   * @return Set of type names that are automatically imported
   */
  ITypeUsesMap getDefaultTypeUses();

  boolean isDomainInstance( Object value );

  boolean isEntityClass( IType type );

  Object getEntityInstanceFrom( Object entity, IType classDomain );

  boolean areBeansEqual( Object bean1, Object bean2 );

  boolean verifyValueForType( IType type, Object value );

  String makeStringFrom( Object obj );

  long getHashedEntityId( String strId, IType classEntity );

  boolean isInternal( IType type );

  /**
   * @return the main logger for all Gosu subsystems.  This logger must be available at all times during Gosu
   * startup and execution.
   */
  ILogger getLogger();

  Locale getLocale();

  Date getCurrentTime();

  TimeZone getTimeZone();

  void addEnhancementMethods(IType typeToEnhance, Collection methodsToAddTo);

  void addEnhancementProperties(IType typeToEnhance, Map propertyInfosToAddTo, boolean caseSensitive);

  ClassLoader getPluginClassLoader();

  StringBuilder getPluginRepositories();

  String getWebServerPaths();

  boolean isUnreachableCodeDetectionOn();

  IType getKeyType();

  IPropertyInfo getEntityIdProperty( IType rootType );

  boolean shouldAddWarning( IType type, IParseIssue warning );

  ILanguageLevel getLanguageLevel();

  List<IGosuClassLoadingObserver> getGosuClassLoadingObservers();

  boolean areUsesStatementsAllowedInStatementLists(ICompilableType gosuClass);

  List<IDirectory> getAdditionalSourceRoots();

  void reloadedTypes(String[] types);

  ExtendedTypeDataFactory getExtendedTypeDataFactory(String typeName);

  String getLocalizedTypeName(IType type);

  String getLocalizedTypeInfoName(IType type);
}
