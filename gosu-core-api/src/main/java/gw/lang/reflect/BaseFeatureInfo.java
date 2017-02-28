/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuCollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;


public abstract class BaseFeatureInfo implements IAttributedFeatureInfo
{
  private transient IType _intrType;
  private transient IFeatureInfo _container;
  private volatile List<IAnnotationInfo> _deprecated;

  transient private volatile List<IAnnotationInfo> _annotations;

  transient private volatile Boolean _internalAPI;


  public BaseFeatureInfo( IFeatureInfo container )
  {
    _container = container;
  }

  public BaseFeatureInfo( IType intrType )
  {
    _intrType = intrType;
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return getName();
  }

  public IType getOwnersType()
  {
    if (_intrType != null) {
      return _intrType;
    } else {
      IFeatureInfo container = getContainer();
      return container != null ? container.getOwnersType() : null;
    }
  }


  /**
   * Returns the list of annotations <b>exactly matching</b> the annotation passed in.  If the annotation is a sub type
   * of the type passed in, this will not return those annotations.
   * <p/>
   * This is equivilent to calling getAnnotations().get(type).
   *
   * @param type the type to look for
   */
  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return ANNOTATION_HELPER.getAnnotationsOfType(type, getAnnotations());
  }

  public boolean hasAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation(type, getAnnotations());
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return ANNOTATION_HELPER.getAnnotation(type, getAnnotations(), type.getName());
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation( type, getDeclaredAnnotations() );
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    if( _annotations == null )
    {
      TypeSystem.lock();
      try
      {
        if( _annotations == null )
        {
          List<IAnnotationInfo> annotations = new ArrayList<IAnnotationInfo>();
          addAnnotations( annotations, this, new IdentityHashMap());
          _annotations = GosuCollectionUtil.compactAndLockList(annotations);
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _annotations;
  }

  private void addAnnotations( List<IAnnotationInfo> annotations, BaseFeatureInfo fi, Map visitedFeatures )
  {
    if( !visitedFeatures.containsKey( fi ) )
    {
      for( IAnnotationInfo annotationInfo : fi.getDeclaredAnnotations() )
      {
        if( fi == this || (ANNOTATION_HELPER.isInherited( annotationInfo.getType() ) &&
                         ANNOTATION_HELPER.shouldAddInheritedAnnotation( this, annotations, annotationInfo )))
        {
          if (annotationInfo == null) {
            throw new NullPointerException("Null annotation found on " + fi + " on " + fi.getOwnersType());
          }
          annotations.add( annotationInfo );
        }
      }
      visitedFeatures.put( fi, null );
      for( BaseFeatureInfo baseFeatureInfo : fi.getSuperAnnotatedElements() )
      {
        addAnnotations( annotations, baseFeatureInfo, visitedFeatures );
      }
    }
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return !isInternalAPI();
  }

  public boolean isScriptable()
  {
    return isVisible( ScriptabilityModifiers.SCRIPTABLE );
  }

  public boolean isHidden()
  {
    try {
      return isInternalAPI();
    } catch (Exception e) {
      return false;
    }
  }

  // The package prefixes that may have classes using the @InternalAPI.
  // This list exists for performance; it is otherwise expensive to load up annotations to find the goddamn @InternalAPI tag
  private static final String[] notInternalNs = {"gw.", "com.guidewire.", "entity."};

  public boolean isInternalAPI()
  {
    if( _internalAPI == null )
    {
      if( ILanguageLevel.Util.STANDARD_GOSU() ||
          Arrays.stream( notInternalNs ).noneMatch( ns -> getOwnersType() != null && getOwnersType().getNamespace() != null && getOwnersType().getNamespace().startsWith( ns ) ) )
      {
        return _internalAPI = false;
      }

      TypeSystem.lock();
      try
      {
        if( _internalAPI == null )
        {
          _internalAPI = !getAnnotationsOfType(JavaTypes.INTERNAL_API()).isEmpty();
        }
        assert _internalAPI != null;
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _internalAPI;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isDefaultImpl()
  {
    return false;
  }

  public boolean isDeprecated()
  {
    return !getDeprecatedAnnotation().isEmpty();
  }

  public String getDeprecatedReason()
  {
    List<IAnnotationInfo> annotation = getDeprecatedAnnotation();
    if (annotation.isEmpty()) {
      return null;
    } else {
      return (String) annotation.get(0).getFieldValue("value");
    }
  }

  private List<IAnnotationInfo> getDeprecatedAnnotation() {
    if (_deprecated == null) {
      TypeSystem.lock();
      try {
        if (_deprecated == null) {
          IType container = _intrType == null ? getContainer().getOwnersType() : _intrType;
          if( container instanceof ITypeRef ) {
            _deprecated = getAnnotationsOfType(
                    TypeSystem.getByFullName("gw.lang.Deprecated", container.getTypeLoader().getModule().getExecutionEnvironment().getGlobalModule()));
          }
          else {
            _deprecated = Collections.emptyList();
          }
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    return _deprecated;
  }

  public boolean isPrivate()
  {
    return false;
  }

  public boolean isInternal()
  {
    return false;
  }

  public boolean isProtected()
  {
    return false;
  }

  public boolean isPublic()
  {
    return true;
  }

  protected Collection<BaseFeatureInfo> getSuperAnnotatedElements()
  {
    List<BaseFeatureInfo> infos = new ArrayList<BaseFeatureInfo>();
    addAnnotationSuperElement( infos, getOwnersType().getSupertype() );
    if( !(this instanceof IConstructorInfo) )
    {
      IType[] interfaces = getOwnersType().getInterfaces();
      if( interfaces != null )
      {
        for( IType anInterface : interfaces )
        {
          if (!TypeSystem.isDeleted(anInterface)) {
            addAnnotationSuperElement( infos, anInterface );
          }
        }
      }
    }
    return infos;
  }

  private void addAnnotationSuperElement( List<BaseFeatureInfo> infos, IType type )
  {
    if( type != null && !(type instanceof IErrorType) )
    {
      IFeatureInfo featureInfo;
      if( this instanceof IConstructorInfo )
      {
        featureInfo = type.getTypeInfo().getConstructor( getParamTypes( ((IConstructorInfo)this).getParameters() ) );
      }
      else if( this instanceof IMethodInfo)
      {
        featureInfo = type.getTypeInfo().getMethod( getDisplayName(), getParamTypes( ((IMethodInfo)this).getParameters() ) );
      }
      else if( this instanceof IPropertyInfo)
      {
        featureInfo = type.getTypeInfo().getProperty( getName() );
      }
      else
      {
        assert this instanceof ITypeInfo;
        featureInfo = type.getTypeInfo();
      }
      if( featureInfo != null )
      {
        if( featureInfo instanceof BaseFeatureInfo )
        {
          BaseFeatureInfo baseFeatureInfo = (BaseFeatureInfo)featureInfo;
          infos.add( baseFeatureInfo );
        }
      }
    }
  }

  public String toString() {
    return getName();
  }

  static <T> List<T> compactAndLockList(List<T> list) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }

    if (list.size() == 1) {
      return Collections.singletonList(list.get(0));
    }

    if (list instanceof ArrayList) {
      ((ArrayList<T>)list).trimToSize();
    }

    return Collections.unmodifiableList(list);
  }

  public static IType[] getParamTypes( IParameterInfo[] parameters )
  {
    List<IType> retValue = new ArrayList<IType>();
    if( parameters != null )
    {
      for( IParameterInfo parameterInfo : parameters )
      {
        retValue.add( parameterInfo.getFeatureType() );
      }
    }

    return retValue.toArray( new IType[retValue.size()] );
  }
}
