/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.IService;
import gw.fs.IDirectory;
import gw.internal.gosu.parser.ExtendedTypeDataFactory;
import gw.lang.UnstableAPI;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IAttributeSource;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.expressions.IQueryExpression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.reflect.gs.ICompilableType;
import gw.util.IFeatureFilter;
import gw.util.ILogger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@UnstableAPI
public interface IEntityAccess extends IService
{
  /**
   * Get a set of type names that are automatically imported
   *
   * @return Set of type names that are automatically imported
   */
  public ITypeUsesMap getDefaultTypeUses();

  public boolean isDomainInstance( Object value );

  public boolean isEntityClass( IType type );

  public Object getEntityInstanceFrom( Object entity, IType classDomain );

  public boolean areBeansEqual( Object bean1, Object bean2 );

  public boolean verifyValueForType( IType type, Object value );

  public String makeStringFrom( Object obj );

  public long getHashedEntityId( String strId, IType classEntity );

  public boolean isInternal( IType type );

  /**
   * @return the main logger for all Gosu subsystems.  This logger must be available at all times during Gosu
   * startup and execution.
   */
  public ILogger getLogger();

  public Locale getLocale();

  public Date getCurrentTime();

  public void addEnhancementMethods(IType typeToEnhance, Collection methodsToAddTo);

  public void addEnhancementProperties(IType typeToEnhance, Map propertyInfosToAddTo, boolean caseSensitive);

  public IQueryExpressionEvaluator getQueryExpressionEvaluator( IQueryExpression queryExpression );

  public IFeatureFilter getQueryExpressionFeatureFilter();

  public ClassLoader getPluginClassLoader();

  public IAttributeSource getAttributeSource( GlobalScope scope );

  public Object[] convertToExternalIfNecessary( Object[] args, Class[] argTypes, Class methodOwner );

  public Object convertToInternalIfNecessary( Object obj, Class methodOwner );

  public boolean isExternal( Class methodOwner );

  public StringBuilder getPluginRepositories();

  public String getWebServerPaths();

  public boolean isUnreachableCodeDetectionOn();

  public boolean isWarnOnImplicitCoercionsOn();

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
