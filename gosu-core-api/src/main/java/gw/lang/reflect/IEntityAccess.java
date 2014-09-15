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
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.expressions.IQueryExpression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.util.IFeatureFilter;
import gw.util.ILogger;

import java.util.Date;
import java.util.List;


@UnstableAPI
public interface IEntityAccess extends IService
{

  public boolean isDomainInstance( Object value );

  public boolean isEntityClass( IType type );

  public boolean isViewEntityClass( IType type );

  public IType getPrimaryEntityClass( IType type );

  public boolean areBeansEqual( Object bean1, Object bean2 );

  public boolean verifyValueForType( IType type, Object value );

  public ILogger getLogger();

  public IQueryExpressionEvaluator getQueryExpressionEvaluator( IQueryExpression queryExpression );

  public IFeatureFilter getQueryExpressionFeatureFilter();

  public ClassLoader getPluginClassLoader();

  public IAttributeSource getAttributeSource( GlobalScope scope );

  public boolean isExternal( Class methodOwner );

  public StringBuilder getPluginRepositories();

  public String getWebServerPaths();

  IType getKeyType();

  IPropertyInfo getEntityIdProperty( IType rootType );

  List<IGosuClassLoadingObserver> getGosuClassLoadingObservers();

  List<IDirectory> getAdditionalSourceRoots();

  ILanguageLevel getLanguageLevel();

  // Runtime

  public Object getEntityInstanceFrom( Object entity, IType classDomain );

  public long getHashedEntityId( String strId, IType classEntity );

  public Date getCurrentTime();

  public Object[] convertToExternalIfNecessary( Object[] args, Class[] argTypes, Class methodOwner );

  public Object convertToInternalIfNecessary( Object obj, Class methodOwner );

  void reloadedTypes(String[] types);

  String getLocalizedTypeName(IType type);

  String getLocalizedTypeInfoName(IType type);

  ExtendedTypeDataFactory getExtendedTypeDataFactory(String typeName);
}
