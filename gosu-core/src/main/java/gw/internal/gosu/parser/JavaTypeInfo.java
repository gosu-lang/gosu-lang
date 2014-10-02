/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.lang.GosuShop;
import gw.lang.annotation.Annotations;
import gw.lang.SimplePropertyProcessing;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IDocRef;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.Keyword;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaTypeInfo;
import gw.util.concurrent.LockingLazyVar;

import java.beans.IndexedPropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class JavaTypeInfo extends JavaBaseFeatureInfo implements IJavaTypeInfo
{
  private final IType _type;
  private final IJavaClassInfo _backingClass;
  private WeakReference<IClassDocNode> _classDocumentation;
  private LockingLazyVar<List<IPropertyInfo>> _declaredProperties;
  private LockingLazyVar<List<IJavaMethodInfo>> _declaredMethods;
  private LockingLazyVar<List<IConstructorInfo>> _allCtors;
  private JavaFeatureManager _fm;
  private final ReentrantLock _myLock = new ReentrantLock();

  private static class JavaFeatureManager extends FeatureManager {
    public JavaFeatureManager(IRelativeTypeInfo type) {
      super(type, true, type.getOwnersType().isInterface() &&
                         type.getOwnersType().getSupertype() == null);
    }
  }

  public JavaTypeInfo(IType intrType, IJavaClassInfo backingClass) {
    super(intrType);
    _type = intrType;
    _backingClass = backingClass;
    _fm = new JavaFeatureManager(this);
    _declaredProperties = new LockingLazyVar<List<IPropertyInfo>>() {
        @Override
        protected List<IPropertyInfo> init() {
          List<IPropertyInfo> ret;
          IJavaPropertyDescriptor[] properties = _backingClass.getPropertyDescriptors();
          if( properties == null ) {
            ret = Collections.emptyList();
          } else {
            ret = new ArrayList<IPropertyInfo>();
            for( IJavaPropertyDescriptor property : properties )
            {
              if( property instanceof IndexedPropertyDescriptor )
              {
                IndexedPropertyDescriptor indexedProp = (IndexedPropertyDescriptor)property;
                if( indexedProp.getPropertyType() == null )
                {
                  // Indexed properties must provide non-indexed access.
                  continue;
                }
              }

              IJavaPropertyInfo pi = JavaPropertyInfo.newInstance(JavaTypeInfo.this, property);
              // We only want properties that are at least readable
              if (pi.isReadable()) {
                ret.add(pi);
              }
            }
            addFieldProperties(ret);
            addArrayProperties(ret);
            ret = Collections.unmodifiableList(ret);
          }
          return ret;
        }

      private void addArrayProperties(List<IPropertyInfo> ret) {
        if ( getOwnersType().isArray()) {
          for (Iterator<IPropertyInfo> iterator = ret.iterator(); iterator.hasNext();) {
            IPropertyInfo propertyInfo = iterator.next();
            if (propertyInfo.getName().equals(Keyword.KW_length.toString())) {
              iterator.remove();
            }
          }
          IPropertyInfo lengthProperty = GosuShop.createPropertyDelegate(JavaTypeInfo.this, GosuShop.createLengthProperty(JavaTypeInfo.this));
          ret.add(lengthProperty);
        }
      }

      private void addFieldProperties( List<IPropertyInfo> properties )
        {
          TypeSystem.lock();
          try
          {
            boolean simplePropertyProcessing = _backingClass.getAnnotation(SimplePropertyProcessing.class) != null;
            IJavaClassField[] fields = _backingClass.getDeclaredFields();
            for( IJavaClassField field : fields )
            {
              if( field.isSynthetic() )
              {
                // E.g., $assertionsDisabled
                continue;
              }

              if( Modifier.isStatic( field.getModifiers() ) )
              {
                TypeVarToTypeMap actualParamByVarName =
                        TypeLord.mapTypeByVarName( getOwnersType(), getOwnersType(), true );
                JavaFieldPropertyInfo staticProp = new JavaFieldPropertyInfo( JavaTypeInfo.this, field.getGenericType().getActualType( actualParamByVarName, true ), field, true, simplePropertyProcessing );
                int pos = getPosition(properties, staticProp.getName());
                // We favor non-static over static
                if( pos == -1 || properties.get(pos).isStatic() )
                {
                  if (pos == -1) {
                    properties.add(staticProp);
                  } else {
                    properties.set(pos, staticProp);
                  }
                }
                else
                {
                  staticProp.changeNameForNonStaticCollision();
                  properties.add(staticProp);
                }
              }
              else if( !_backingClass.hasCustomBeanInfo() )
              {
                TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOwnersType(), getOwnersType(), true );
                if (field == null) {
                  throw new IllegalStateException("A null field was found for " + _backingClass.getName() + " : " + Arrays.toString(fields));
                }
                if (field.getGenericType() == null) {
                  throw new IllegalStateException("The generic type for the field " + field.getName() + " on " + _backingClass.getName() + " was null");
                }
                JavaFieldPropertyInfo prop = new JavaFieldPropertyInfo( JavaTypeInfo.this, field.getGenericType().getActualType( actualParamByVarName, true ), field, false, simplePropertyProcessing );
                int pos = getPosition(properties, prop.getName());
                // We favor non-static over static
                if( pos == -1 || properties.get(pos) instanceof JavaFieldPropertyInfo )
                {
                  //## todo: Really we should preserve the field name if it differs by case, and even if it's the same,
                  //## we should have two separate features to reflect the actual structure of the type. Essentially,
                  //## we don't want to lose type information.
                  if (pos == -1) {
                    properties.add(prop);
                  } else {
                    properties.set(pos, prop);
                  }
                }
              }
            }
          }
          finally
          {
            TypeSystem.unlock();
          }
        }

        private int getPosition(List<IPropertyInfo> properties, String name) {
          for (int i = 0; i < properties.size(); i++) {
            IPropertyInfo propertyInfo = properties.get(i);
//            if( ILanguageLevel.Util.STANDARD_GOSU() ) {
              if (propertyInfo.getName().equals(name)) {
                return i;
              }
//            }
//            else {
//              if (propertyInfo.getName().equals(name)) {
//                return i;
//              }
//            }
          }

          return -1;
        }
      };

    _declaredMethods = new LockingLazyVar<List<IJavaMethodInfo>>() {
        @Override
        protected List<IJavaMethodInfo> init() {
          List<IJavaMethodInfo> ret;
          IJavaMethodDescriptor[] methods = _backingClass.getMethodDescriptors();
          if (methods == null) {
            ret = Collections.emptyList();
          } else {
            ret = new ArrayList<IJavaMethodInfo>(methods.length);
            for (IJavaMethodDescriptor method : methods) {
              ret.add(new JavaMethodInfo(JavaTypeInfo.this, method, !Modifier.isStatic( method.getMethod().getModifiers() ) && isPropertyMethod(method)));
            }
            ((ArrayList)ret).trimToSize();
            ret = Collections.unmodifiableList(ret);
          }
          return ret;
        }
    };

    _allCtors = new LockingLazyVar<List<IConstructorInfo>>() {
      @Override
      protected List<IConstructorInfo> init() {
        List<IConstructorInfo> constructors;
        if( _backingClass.isAnnotation() )
        {
          return AnnotationConstructorGenerator.generateAnnotationConstructors( JavaTypeInfo.this );
        }
        IJavaClassConstructor[] ctors = _backingClass.getDeclaredConstructors();
        if( ctors == null )
        {
          constructors = Collections.emptyList();
        } else {
          constructors = new ArrayList<IConstructorInfo>( ctors.length );

          for (IJavaClassConstructor ctor : ctors) {
            try {
              constructors.add(new JavaConstructorInfo(JavaTypeInfo.this, ctor));
            } catch (SecurityException ex) {
              // ok, don't add.  This happens for things like java.lang.Class
            }
          }
          constructors = TypeInfoUtil.makeSortedUnmodifiableRandomAccessList( constructors );
        }
        return constructors;
      }
    };
  }

  private boolean isPropertyMethod( IJavaMethodDescriptor md )
  {
    String name = md.getName();
    if( md.getMethod().getParameterTypes().length == 0 )
    {
      String propName = null;
      if( name.startsWith( "get" ) || name.startsWith( "set" ) )
      {
        propName = name.substring( 3 );
      }
      else if( name.startsWith( "is" ) )
      {
        propName = name.substring( 2 );
      }

      if( propName != null )
      {
        for( IPropertyInfo propertyInfo : _declaredProperties.get() )
        {
          if( propertyInfo.getName().equals( propName ) &&
              (!(propertyInfo instanceof IJavaPropertyInfo) ||
               ((IJavaPropertyInfo)propertyInfo).getPropertyDescriptor().getReadMethod().equals(md.getMethod())) )
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public void unload()
  {
    TypeSystem.lock();
    try
    {
      _declaredMethods.clear();
      _declaredProperties.clear();
      _fm = new JavaFeatureManager(this);
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  @Override
  protected IJavaAnnotatedElement getAnnotatedElement()
  {
    return _backingClass;
  }

  @Override
  protected boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _backingClass.isVisibleViaFeatureDescriptor(constraint);
  }

  @Override
  protected boolean isHiddenViaFeatureDescriptor() {
    return _backingClass.isHiddenViaFeatureDescriptor();
  }

  @Override
  public Accessibility getAccessibilityForType( IType whosaskin )
  {
    return getAccessibilityForClass( _type, whosaskin == null ? getCompilingClass( _type ) : whosaskin );
  }

  @Override
  public List<? extends IPropertyInfo> getProperties()
  {
    return getProperties( null );
  }

  @Override
  public List<IPropertyInfo> getProperties( IType whosAskin )
  {
    return _fm.getProperties(getAccessibilityForType(whosAskin));
  }

  @Override
  public IPropertyInfo getProperty( CharSequence propName )
  {
    return getProperty( null, propName );
  }

  @Override
  public IPropertyInfo getProperty( IType whosAskin, CharSequence propName )
  {
    return _fm.getProperty(getAccessibilityForType(whosAskin), propName);
  }

  @Override
  public MethodList getMethods()
  {
    return getMethods(null);
  }

  @Override
  public MethodList getMethods(IType whosAskin)
  {
    return _fm.getMethods(getAccessibilityForType(whosAskin));
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors()
  {
    return getConstructors( null );
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors( IType whosAskin )
  {
    return _fm.getConstructors(getAccessibilityForType(whosAskin));
  }

  @Override
  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  @Override
  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params )
  {
    MethodList methods = getMethods( whosaskin );
    return FIND.method( methods, methodName, params );
  }

  @Override
  public IConstructorInfo getConstructor( IType... params )
  {
    return FIND.constructor( getConstructors(), params );
  }

  @Override
  public IConstructorInfo getConstructor( IType whosAskin, IType[] params )
  {
    List<? extends IConstructorInfo> ctors = getConstructors( whosAskin );
    return FIND.constructor( ctors, params );
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    return _declaredProperties.get();
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    return _declaredMethods.get();
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return _allCtors.get();
  }

  @Override
  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  @Override
  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  @Override
  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  @Override
  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  @Override
  public String getName()
  {
    return _backingClass.getRelativeName();
  }

  @Override
  public String getDisplayName()
  {
    return CommonServices.getEntityAccess().getLocalizedTypeInfoName(_type);
  }

  @Override
  public String getDescription() {
    if (getDocNode().get() != null && getDocNode().get().getDescription() != null) {
      return getDocNode().get().getDescription();
    }

    return null;
  }

  @Override
  public boolean isDeprecated()
  {
    return super.isDeprecated() || _backingClass.isAnnotationPresent( java.lang.Deprecated.class );
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    List<IAnnotationInfo> annotations = super.getDeclaredAnnotations();
    String deprecatedWarningToAdd = null;
    if (getDocNode().get() != null && getDocNode().get().isDeprecated()) {
      deprecatedWarningToAdd = getDocNode().get().getDeprecated();
    } else {
//## todo: Diamond: re-enable this and provide some means to escape it in the language e.g., provide an internal modifier for the uses-statement: internal uses com.abc.Foo
//      IJavaType typeToCheck = (IJavaType) getOwnersIntrinsicType();
//      if (!typeToCheck.isArray() &&
//              !typeToCheck.isPrimitive() &&
//              isFilteredType(typeToCheck) &&
//              typeToCheck.getIntrinsicClass().getAnnotation(PublishInGosu.class) == null) {
//        deprecatedWarningToAdd = CommonServices.getGosuLocalizationService().localize(Res.USING_INTERNAL_CLASS);
//      }
    }
    if (deprecatedWarningToAdd != null) {
      annotations.add(GosuShop.getAnnotationInfoFactory().createJavaAnnotation(makeDeprecated(deprecatedWarningToAdd), this));
    }
    return annotations;
  }

  @Override
  public boolean isHidden() {
    return false;
  }

  @Override
  protected boolean isDefaultEnumFeature()
  {
    return false;
  }

  @Override
  public boolean isStatic()
  {
    return Modifier.isStatic( _type.getModifiers() );
  }

  public boolean isPrivate()
  {
    return Modifier.isPrivate( _type.getModifiers() );
  }

  @Override
  public boolean isInternal()
  {
    return !isPublic() && !isProtected() && !isPrivate();
  }

  public boolean isProtected()
  {
    return Modifier.isProtected( _type.getModifiers() );
  }

  public boolean isPublic()
  {
    return Modifier.isPublic( _type.getModifiers() );
  }

  private Accessibility getAccessibilityForClass( IType ownersClass, IType whosAskin )
  {
    if( GosuClassTypeInfo.isIncludeAll() )
    {
      return Accessibility.PRIVATE;
    }

    if( ownersClass == null || whosAskin == null )
    {
      return Accessibility.PUBLIC;
    }

    if (whosAskin instanceof IGosuClassInternal && ((IGosuClassInternal)whosAskin).isProxy()) {
      IJavaType javaType = ((IGosuClassInternal)whosAskin).getJavaType();
      if( javaType != null )
      {
        whosAskin = javaType;
      }
    }

    if( ownersClass == whosAskin )
    {
      return Accessibility.PRIVATE;
    }
    if( FeatureManager.isInSameNamespace(ownersClass, whosAskin))
    {
      return Accessibility.INTERNAL;
    }
    if( IGosuClass.ProxyUtil.isProxyStart( whosAskin.getNamespace() ) )
    {
      IType genOwnerType = TypeLord.getPureGenericType( ownersClass );
      String strGenericOwnerClass = genOwnerType.getName();
      if( IGosuClass.ProxyUtil.getNameSansProxy( whosAskin ).equals( strGenericOwnerClass ) )
      {
        return Accessibility.INTERNAL;
      }
      if( genOwnerType.getRelativeName().startsWith( IGosuClass.SUPER_PROXY_CLASS_PREFIX ) )
      {
        return Accessibility.INTERNAL;
      }
    }
    if (FeatureManager.isInEnclosingClassHierarchy(ownersClass, whosAskin))
    {
      return Accessibility.PROTECTED;
    }
    
    return Accessibility.PUBLIC;
  }

  public static IType getCompilingClass( IType type )
  {
    if( GosuClassTypeInfo.isIncludeAll() )
    {
      return type;
    }

    IType compilingClass = GosuClassCompilingStack.getCurrentCompilingType();
    if( compilingClass == null )
    {
      ISymbolTable symTableCtx = CompiledGosuClassSymbolTable.getSymTableCtx();
      ISymbol thisSymbol = symTableCtx.getThisSymbolFromStackOrMap();
      if( thisSymbol != null )
      {
        IType thisSymbolType = thisSymbol.getType();
        if( thisSymbolType instanceof IGosuClassInternal )
        {
          compilingClass = thisSymbolType;
        }
      }
    }
    if( IGosuClass.ProxyUtil.isProxy( compilingClass ) )
    {
      return compilingClass;
    }
    return null;
  }

  private IDocRef<IClassDocNode> _docRef = new IDocRef<IClassDocNode>() {
    @Override
    public IClassDocNode get() {
      _myLock.lock();
      try {
        IClassDocNode classDoc = _classDocumentation == null ? null : _classDocumentation.get();
        if (classDoc == null) {
          classDoc = _backingClass.createClassDocNode();
          if( classDoc == null )
          {
            classDoc = GosuShop.getJavaDocFactory().create();//so we do not reenter this
          }

          _classDocumentation = new WeakReference<IClassDocNode>(classDoc);
        }
        return classDoc;
      } finally {
        _myLock.unlock();
      }
    }
  };

  IDocRef<IClassDocNode> getDocNode() {
    return _docRef;
  }

}
