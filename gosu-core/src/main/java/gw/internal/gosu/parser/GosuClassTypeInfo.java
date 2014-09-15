/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.util.*;

/**
 */
@SuppressWarnings({"unchecked"})
public class GosuClassTypeInfo extends BaseTypeInfo implements IGosuClassTypeInfo
{
  private static final ThreadLocal<List<Boolean>> INCLUDE_ALL = new ThreadLocal<List<Boolean>>();

  private IGosuClassInternal _gsClass;
  private ArrayList<IGosuMethodInfo> _declaredMethods;
  private List<IPropertyInfo> _declaredProperties;
  private ArrayList _declaredConstructors;
  private LockingLazyVar<List<IAnnotationInfo>> _declaredAnnotations;
  private Map<GosuBaseAttributedFeatureInfo, IModifierInfo> _modifierInfoByFeature;
  private MyFeatureManager _fm;

  public static void pushIncludeAll()
  {
    List<Boolean> stack = INCLUDE_ALL.get();
    if( stack == null )
    {
      stack = new ArrayList<Boolean>();
      INCLUDE_ALL.set( stack );
    }
    stack.add( 0, Boolean.TRUE );
  }

  public static void popIncludeAll()
  {
    List stack = INCLUDE_ALL.get();
    if( stack == null || stack.isEmpty() )
    {
      throw new IllegalStateException( "Attempted to pop an empty IncludeAll stack" );
    }
    stack.remove( 0 );
  }

  public static boolean isIncludeAll()
  {
    List<Boolean> stack = INCLUDE_ALL.get();
    return stack != null && !stack.isEmpty() && stack.get( 0 );
  }

  public GosuClassTypeInfo( IGosuClassInternal gsClass )
  {
    super( gsClass );
    _gsClass = gsClass;
    _fm = new MyFeatureManager();
    _modifierInfoByFeature = new HashMap<GosuBaseAttributedFeatureInfo, IModifierInfo>();
    _declaredAnnotations =
      new LockingLazyVar<List<IAnnotationInfo>>()
      {
        @Override
        protected List<IAnnotationInfo> init()
        {
          List<? extends IGosuAnnotation> annotations = getGosuClass().getGosuAnnotations();
          List<IAnnotationInfo> result = Collections.emptyList();
          if( annotations != null )
          {
            result = new ArrayList<IAnnotationInfo>();
            for( int i = 0; i < annotations.size(); i++ )
            {
              IGosuAnnotation annotation = annotations.get( i );
              result.add( new GosuAnnotationInfo( annotation, GosuClassTypeInfo.this, getGosuClass() ) );
            }
          }
          return result;
        }
      };
  }

  public void forceInit() {
    _fm.forceInit();
  }

  public boolean isStatic()
  {
    return _gsClass.isStatic();
  }

  public boolean isPrivate()
  {
    return Modifier.isPrivate( _gsClass.getModifiers() );
  }

  public boolean isInternal()
  {
    return Modifier.isInternal( _gsClass.getModifiers() );
  }

  public boolean isProtected()
  {
    return Modifier.isProtected( _gsClass.getModifiers() );
  }

  public boolean isPublic()
  {
    return Modifier.isPublic( _gsClass.getModifiers() );
  }

  public boolean isDeprecated() {
    List<IAnnotationInfo> annotations = getAnnotationsOfType(TypeSystem.get(gw.lang.Deprecated.class, TypeSystem.getGlobalModule()));
    return (annotations != null) &&
           (annotations.size() > 0);
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return _declaredAnnotations.get();
  }

  public List<? extends IMethodInfo> getDeclaredMethods()
  {
    extractMethods();
    return _declaredMethods;
  }

  public List<? extends IPropertyInfo> getDeclaredProperties()
  {
    extractProperties();
    return _declaredProperties;
  }

  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    extractConstructors();
    return _declaredConstructors;
  }

  public String getName()
  {
    return getGosuClass().getRelativeName();
  }

  public Accessibility getAccessibilityForType( IType whosaskin )
  {
    return FeatureManager.getAccessibilityForClass(_gsClass, whosaskin == null ? getCompilingClass() : whosaskin);
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return getProperties( null );
  }

  public List<IPropertyInfo> getProperties( IType whosAskin )
  {
    return _fm.getProperties(getAccessibilityForType(whosAskin));
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    return getProperty( null, propName );
  }

  public IPropertyInfo getProperty( IType whosAskin, CharSequence propName )
  {
    return _fm.getProperty(getAccessibilityForType(whosAskin), propName);
  }

  public MethodList getMethods()
  {
    return getMethods( null );
  }

  public MethodList getMethods(IType whosAskin)
  {
    return _fm.getMethods(getAccessibilityForType(whosAskin));
  }

  public List<GosuConstructorInfo> getConstructors()
  {
    return getConstructors( null );
  }

  public List<GosuConstructorInfo> getConstructors( IType whosAskin )
  {
    return (List<GosuConstructorInfo>) _fm.getConstructors(getAccessibilityForType(whosAskin));
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params )
  {
    MethodList methods = getMethods(whosaskin);
    return FIND.method( methods, methodName, params );
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return FIND.constructor( getConstructors(), params );
  }

  public IConstructorInfo getConstructor( IType whosAskin, IType[] params )
  {
    List<GosuConstructorInfo> ctors = getConstructors( whosAskin );
    return FIND.constructor( ctors, params );
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  public IGosuClassInternal getGosuClass()
  {
    return _gsClass;
  }

  /**
   * We expose type info in a context sensitive manner. For instance, we only
   * expose private features from within the containing class. We can determine
   * the context class from the current compiling class. The compiling class is
   * obtained from either 1) the actual CompiledGosuClass stack the Gosu
   * Class TypeLoader manages or 2) from the class itself if it's not compiled
   * yet. Note the latter case happens only on the UI side when a class is being
   * edited.
   */
  private IType getCompilingClass()
  {
    if( isIncludeAll() )
    {
      return _gsClass;
    }

    IType type = GosuClassCompilingStack.getCurrentCompilingType();
    if( type != null )
    {
      return type;
    }
    ISymbolTable symTableCtx = CompiledGosuClassSymbolTable.getSymTableCtx();
    ISymbol thisSymbol = symTableCtx.getThisSymbolFromStackOrMap();
    if( thisSymbol != null )
    {
      IType thisSymbolType = thisSymbol.getType();
      if( thisSymbolType instanceof IGosuClassInternal )
      {
        return thisSymbolType;
      }
    }
    return !_gsClass.isDeclarationsCompiled() ? _gsClass : null;
  }

  private void extractProperties()
  {
    if( _declaredProperties != null )
    {
      return;
    }

    TypeSystem.lock();
    try
    {
      if( _declaredProperties != null )
      {
        return;
      }

      List<IPropertyInfo> declaredProperties = new ArrayList<IPropertyInfo>();
      if( _gsClass.isParameterizedType() )
      {
        makePropertiesFromGenericTypesFields( declaredProperties ); 
      }
      else
      {
        makePropertiesFromFields( declaredProperties, _gsClass.getStaticFields() );
        makePropertiesFromFields( declaredProperties, (Collection<IVarStatement>) _gsClass.getMemberFields());
      }

      addDefinedProperties( declaredProperties, _gsClass.getStaticProperties() );
      addDefinedProperties( declaredProperties, _gsClass.getMemberProperties() );

      _declaredProperties = declaredProperties;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void makePropertiesFromGenericTypesFields( List<IPropertyInfo> declaredPropertiesMap )
  {
    if( !_gsClass.isParameterizedType() )
    {
      throw new IllegalStateException( "Not a parameterized type" );
    }

    TypeSystem.lock();
    try
    {
      IGosuClassInternal cl = TypeLord.getPureGenericType( _gsClass );
      for( IPropertyInfo pi : cl.getTypeInfo().getDeclaredProperties() )
      {
        if( pi instanceof GosuVarPropertyInfo )
        {
          GosuVarPropertyInfo fieldProp = new GosuVarPropertyInfo( this, (GosuVarPropertyInfo)pi );
          declaredPropertiesMap.add( fieldProp );
        }
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }
  
  private void makePropertiesFromFields(List<IPropertyInfo> declaredPropertiesMap, Collection<IVarStatement> fields)
  {
    TypeSystem.lock();
    try
    {
      for( IVarStatement varStmt : fields )
      {
        GosuVarPropertyInfo fieldProp = new GosuVarPropertyInfo( this, varStmt );
        declaredPropertiesMap.add( fieldProp );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void addDefinedProperties( List<IPropertyInfo> declaredPropertiesMap, List<DynamicPropertySymbol> propertySymbols )
  {
    TypeSystem.lock();
    try
    {
      for( DynamicPropertySymbol dps : propertySymbols )
      {
        if( getOwnersType().isParameterizedType() )
        {
          dps = dps.getParameterizedVersion( (IGosuClassInternal)getOwnersType() );
        }
        GosuPropertyInfo property;
        if( dps.getDisplayName().equals( "IntrinsicType" ) )
        {
          property = new GosuPropertyInfo( this, new IntrinsicTypePropertySymbol( _gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
        }
        else
        {
          if (dps instanceof ParameterizedDynamicPropertySymbol) {
            property = new GosuPropertyInfo( this, dps );
          } else {
            property = new ParameterizedGosuPropertyInfo( this, dps, new GosuPropertyInfo( this, dps) );
          }
        }
        declaredPropertiesMap.add( property );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void extractMethods()
  {
    if( _declaredMethods != null )
    {
      return;
    }

    TypeSystem.lock();
    try
    {
      if( _declaredMethods != null )
      {
        return;
      }

      ArrayList<IGosuMethodInfo> declaredMethods = new ArrayList<IGosuMethodInfo>();
      List mapStaticFunctions = _gsClass.getStaticFunctions();
      if( mapStaticFunctions != null )
      {
        List<IGosuMethodInfo> staticMethodInfos = createMethodInfos( mapStaticFunctions );
        declaredMethods.addAll( staticMethodInfos );
      }

      List mapMemberFunctions = _gsClass.getMemberFunctions();
      if( mapMemberFunctions != null )
      {
        List methodInfos = createMethodInfos( mapMemberFunctions );
        declaredMethods.addAll( methodInfos );
      }

      declaredMethods.trimToSize();
      _declaredMethods = declaredMethods;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private List<IGosuMethodInfo> createMethodInfos( List mapFunctions )
  {
    List functionInfos = new ArrayList();
    for (Object mapFunction : mapFunctions) {
      DynamicFunctionSymbol dfs = (DynamicFunctionSymbol) mapFunction;
      if ( getOwnersType().isParameterizedType()) {
        dfs = dfs.getParameterizedVersion((IGosuClassInternal) getOwnersType());
      }
      GosuMethodInfo methodInfo = new GosuMethodInfo(this, dfs);
      functionInfos.add(methodInfo);
    }
    return functionInfos;
  }

  private void extractConstructors()
  {
    if( _declaredConstructors != null )
    {
      return;
    }

    TypeSystem.lock();
    try
    {
      if( _declaredConstructors != null )
      {
        return;
      }

      ArrayList constructors = new ArrayList();

      if( Modifier.isAnnotation( _gsClass.getModifiers() ) )
      {
        constructors.add( makeStandardAnnotationConstructor() );
      }
      else if( !(_gsClass instanceof IGosuEnhancementInternal) )
      {
        List<DynamicFunctionSymbol> mapConstructors = _gsClass.getConstructorFunctions();
        if( mapConstructors != null )
        {
          constructors.addAll( createConstructorInfos( mapConstructors ) );
        }
        constructors = new ArrayList( TypeInfoUtil.makeSortedUnmodifiableRandomAccessList( constructors ));
      }
      constructors.trimToSize();
      _declaredConstructors = constructors;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private IConstructorInfo makeStandardAnnotationConstructor()
  {
    ArrayList<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    ArrayList<ParameterInfoBuilder> paramsWDefaultValues = new ArrayList<ParameterInfoBuilder>();
    Collection<DynamicFunctionSymbol> methods = _gsClass.getParseInfo().getMemberFunctions().values();
    for( DynamicFunctionSymbol dfs : methods )
    {
      ParameterInfoBuilder pib = new ParameterInfoBuilder().withName( dfs.getDisplayName() ).withType( dfs.getReturnType() );
      IExpression annotationDefault = dfs.getAnnotationDefault();
      if( annotationDefault != null )
      {
        pib.withDefValue( annotationDefault );
        paramsWDefaultValues.add( pib );
      }
      else
      {
        params.add( pib );
      }
    }
    params.addAll( paramsWDefaultValues );

    return new ConstructorInfoBuilder()
      .withParameters( params.toArray( new ParameterInfoBuilder[params.size()] ) )
      .withUserData( AnnotationConstructorGenerator.STANDARD_CTOR_WITH_DEFAULT_PARAM_VALUES )
      .build( this );
  }

  private List createConstructorInfos( List<DynamicFunctionSymbol> functions )
  {
    List functionInfos = new ArrayList();
    for( DynamicFunctionSymbol function : functions )
    {
      GosuConstructorInfo methodInfo = new GosuConstructorInfo( this, function );
      if( getOwnersType().isParameterizedType() )
      {
        function = function.getParameterizedVersion( (IGosuClassInternal)getOwnersType() );
        methodInfo = new ParameterizedGosuConstructorInfo( this, function, methodInfo );
      }
      functionInfos.add( methodInfo );
    }
    return functionInfos;
  }

  public void unload()
  {
    _declaredConstructors = null;
    _declaredMethods = null;
    _declaredProperties = null;
    _fm.clear();
  }

  public String getDescription()
  {
    return getGosuClass().getFullDescription();
  }

  public IModifierInfo getModifierInfo(GosuBaseAttributedFeatureInfo featureInfo)
  {
    if( getOwnersType().isParameterizedType() )
    {
      return ((GosuClassTypeInfo)getOwnersType().getGenericType().getTypeInfo()).getModifierInfo( featureInfo );
    }
    return _modifierInfoByFeature.get(featureInfo);
  }

  public void setModifierInfo(GosuBaseAttributedFeatureInfo featureInfo, IModifierInfo modifierInfo)
  {
    if( getOwnersType().isParameterizedType() )
    {
      ((GosuClassTypeInfo)getOwnersType().getGenericType().getTypeInfo()).setModifierInfo( featureInfo, modifierInfo );
      return;
    }
    if( modifierInfo == null )
    {
      System.out.println("ERROR: Null modifier info for: " + featureInfo);
      //throw new NullPointerException( "modifierInfo is null" );
    }
    _modifierInfoByFeature.put(featureInfo, modifierInfo);
  }

  private class MyFeatureManager extends FeatureManager<String>
  {
    public MyFeatureManager() {
      super(GosuClassTypeInfo.this, true);
    }

    public void forceInit() {
      super.maybeInitConstructors();
      super.maybeInitMethods();
      super.maybeInitProperties();
      getAnnotations();
    }

    @Override
      protected IType convertType(IType type) {
      if( type instanceof IErrorType) {
        return type;
      }
      type = IGosuClassInternal.Util.getGosuClassFrom(type);
      return type == null ? ErrorType.getInstance() : type;
    }

    protected void addEnhancementMethods(List<IMethodInfo> privateMethods) {
      IType type = _gsClass;
      if (_gsClass.isProxy()) {
        type = _gsClass.getJavaType();
      }

      List<IType> parentTypes = new ArrayList<IType>();
      IType atype = type;
      while( atype != null )
      {
        parentTypes.add( atype );
        atype = atype.getSupertype();
      }

      for( IType parentType : parentTypes )
      {
        addEnhancementMethods(parentType, privateMethods );
        IType[] list = parentType.getInterfaces();
        for( IType ifaceType : list )
        {
          if (!TypeSystem.isDeleted(ifaceType)) {
            addEnhancementMethods(ifaceType, privateMethods );
          }
        }
      }

      if (!(_gsClass instanceof IGosuEnhancement)) {
        addEnhancementMethods(JavaTypes.OBJECT(), privateMethods);
      }
    }

    protected void addEnhancementProperties( PropertyNameMap<String> privateProps, boolean caseSensitive )
    {
      IType type = _gsClass;
      if( _gsClass.isProxy() )
      {
        type = _gsClass.getJavaType();
      }

      List<IType> parentTypes = new ArrayList<IType>();
      IType atype = type;
      while( atype != null )
      {
        parentTypes.add( atype );
        atype = atype.getSupertype();
      }

      for( IType parentType : parentTypes )
      {
        addEnhancementProperties( parentType, privateProps, true );
        IType[] list = parentType.getInterfaces();
        for( IType ifaceType : list )
        {
          addEnhancementProperties( ifaceType, privateProps, true );
        }
      }

      if( !(_gsClass instanceof IGosuEnhancement) )
      {
        addEnhancementProperties( JavaTypes.OBJECT(), privateProps, true );
      }
    }
  }
  
  public String toString() {
    return getOwnersType().getName();
  }

}
