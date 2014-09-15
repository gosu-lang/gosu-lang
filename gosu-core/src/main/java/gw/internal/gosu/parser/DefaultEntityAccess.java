/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IAttributeSource;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.exceptions.IncompatibleTypeException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.expressions.IQueryExpression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IGosuClassLoadingObserver;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.java.IJavaType;
import gw.util.IFeatureFilter;
import gw.util.ILogger;
import gw.util.SystemOutLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 */
public class DefaultEntityAccess extends BaseService implements IEntityAccess
{
  public static final SystemOutLogger DEFAULT_LOGGER = new SystemOutLogger(SystemOutLogger.LoggingLevel.WARN);
  private static DefaultEntityAccess g_instance;
  private static final ITypeUsesMap EMPTY_TYPE_USES = new TypeUsesMap( Collections.<String>emptyList() ).lock();
  private List<IGosuClassLoadingObserver> _classLoadingObservers;

  public static DefaultEntityAccess instance()
  {
    return g_instance == null ? g_instance = new DefaultEntityAccess() : g_instance;
  }

  private Map _scopes = new HashMap();

  /**
   */
  public DefaultEntityAccess()
  {
    _classLoadingObservers = Collections.synchronizedList(new ArrayList<IGosuClassLoadingObserver>());
  }

  public ITypeLoader getDefaultTypeLoader()
  {
    return DefaultTypeLoader.instance();
  }

  /**
   * @return x
   */
  @Override
  public boolean isDomainInstance( Object value )
  {
    return false;
  }

  /**
   * @return x
   */
  @Override
  public boolean isEntityClass( IType cls )
  {
    return false;
  }

  @Override
  public boolean isViewEntityClass( IType type )
  {
    return false;
  }

  @Override
  public IType getPrimaryEntityClass( IType type )
  {
    return null;
  }

  /**
   * @return x
   */
  @Override
  public Object getEntityInstanceFrom( Object entity, IType classEntity )
  {
    return null;
  }

  /**
   * @return x
   */
  @Override
  public boolean areBeansEqual( Object bean1, Object bean2 )
  {
    return bean1.equals( bean2 );
  }

  /**
   * @param type
   * @param value
   * @return
   * @throws RuntimeException
   */
  @Override
  public boolean verifyValueForType( IType type, Object value ) throws RuntimeException
  {
    try
    {
      IType valueType = TypeLoaderAccess.instance().getIntrinsicTypeFromObject(value);
      CommonServices.getCoercionManager().verifyTypesComparable(type, valueType, false);
    }
    catch( ParseIssue pe )
    {
      throw new IncompatibleTypeException( "Value of type: " + TypeLoaderAccess.instance().getIntrinsicTypeFromObject( value ).getName() +
                                           " is not compatible with symbol type: " + type.getName() );
    }
    return true;
  }

  /**
   * @return x
   */
  @Override
  public long getHashedEntityId( String strId, IType classEntity )
  {
    return -1;
  }

  @Override
  public ILogger getLogger()
  {
    return DEFAULT_LOGGER;
  }

  @Override
  public Date getCurrentTime()
  {
    return new Date();
  }

  @Override
  public IQueryExpressionEvaluator getQueryExpressionEvaluator( IQueryExpression queryExpression )
  {
    return null;
  }

  @Override
  public IFeatureFilter getQueryExpressionFeatureFilter()
  {
    return new IFeatureFilter() {
      @Override
      public boolean acceptFeature(IType beanType, IFeatureInfo fi) {
        return false;
      }
    };
  }

  @Override
  public ClassLoader getPluginClassLoader()
  {
    return DefaultEntityAccess.class.getClassLoader();
  }

  @Override
  public IAttributeSource getAttributeSource( GlobalScope scope )
  {
    IAttributeSource source = (IAttributeSource)_scopes.get( scope );
    if( source == null )
    {
      source = new ThreadLocalAttributeSource( scope );
      _scopes.put( scope, source );
    }

    return source;
  }

  public void clearAttributeScopes()
  {
    _scopes.clear();
  }

  private static class ThreadLocalAttributeSource extends AbstractTypeSystemListener implements IAttributeSource
  {
    private GlobalScope _scope;

    private ThreadLocal _values = new ThreadLocal();

    public ThreadLocalAttributeSource( GlobalScope scope )
    {
      _scope = scope;
      TypeLoaderAccess.instance().addTypeLoaderListenerAsWeakRef( this );
    }

    public GlobalScope getScope()
    {
      return _scope;
    }

    @Override
    public boolean hasAttribute( String strAttr )
    {
      Map map = getMap();
      return map.containsKey( strAttr );
    }

    @Override
    public Object getAttribute( String strAttr )
    {
      Map map = getMap();
      return map.get(strAttr);
    }

    @Override
    public void setAttribute( String strAttr, Object value )
    {
      Map map = getMap();
      map.put( strAttr, value );
    }

    @Override
    public boolean equals( Object o )
    {
      if( this == o )
      {
        return true;
      }
      if( o == null || getClass() != o.getClass() )
      {
        return false;
      }

      final ThreadLocalAttributeSource that = (ThreadLocalAttributeSource)o;

      return _scope.equals( that._scope );
    }

    @Override
    public int hashCode()
    {
      return _scope.hashCode();
    }

    private Map getMap()
    {
      if( _values.get() == null )
      {
        _values.set( new HashMap() );
      }
      return (Map)_values.get();
    }

    @Override
    public void refreshedTypes(RefreshRequest request)
    {
    }

    @Override
    public void refreshed()
    {
    }
  }

  @Override
  public Object[] convertToExternalIfNecessary( Object[] args, Class[] argTypes, Class methodOwner )
  {
    return args;
  }

  @Override
  public Object convertToInternalIfNecessary( Object obj, Class methodOwner )
  {
    return obj;
  }

  @Override
  public boolean isExternal( Class methodOwner )
  {
    return false;
  }

  @Override
  public StringBuilder getPluginRepositories()
  {
    return new StringBuilder();
  }

  @Override
  public String getWebServerPaths()
  {
    return "";
  }

  @Override
  public IType getKeyType()
  {
    return null;
  }

  @Override
  public IPropertyInfo getEntityIdProperty( IType rootType )
  {
    return null;
  }

  @Override
  public ILanguageLevel getLanguageLevel()
  {
    return new StandardLanguageLevel();
  }

  @Override
  public List<IGosuClassLoadingObserver> getGosuClassLoadingObservers() {
    return _classLoadingObservers;
  }

  @Override
  public List<IDirectory> getAdditionalSourceRoots() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public void reloadedTypes(String[] types) {
    //nothing to do
  }

  @Override
  public String getLocalizedTypeName(IType type) {
    return type.getName();
  }

  @Override
  public String getLocalizedTypeInfoName(IType type) {
    String result;
    if (type instanceof IJavaType) {
      result = ((IJavaType) type).getBackingClassInfo().getDisplayName();
    } else {
      result = getLocalizedTypeName(type);
    }
    return result;
  }

  @Override
  public ExtendedTypeDataFactory getExtendedTypeDataFactory(String typeName) {
    return null;
  }

}
