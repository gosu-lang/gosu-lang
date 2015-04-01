/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.GosuShop;
import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IConstructorNode;
import gw.lang.javadoc.IExceptionNode;
import gw.lang.javadoc.IParamNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.java.*;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class JavaConstructorInfo extends JavaBaseFeatureInfo implements IJavaConstructorInfo
{
  private IJavaClassConstructor _ctor;
  private IParameterInfo[] _params;
  private IConstructorHandler _ctorHandler;
  private List<IExceptionInfo> _exceptions;
  private IDocRef<IConstructorNode> _docs = new IDocRef<IConstructorNode>() {
    @Override
    public IConstructorNode get() {
      IClassDocNode classDocs = ((JavaTypeInfo)getContainer()).getDocNode().get();
      return classDocs == null ? null : classDocs.getConstructor(_ctor);
    }
  };

  /**
   * @param container Typically this will be the containing ITypeInfo
   * @param ctor      The java ctor
   */
  public JavaConstructorInfo(IFeatureInfo container, IJavaClassConstructor ctor) {
    super(container);
    _ctor = ctor;
    if (_ctor instanceof ConstructorJavaClassConstructor) {
      ((ConstructorJavaClassConstructor)_ctor).setAccessible(true);
    }
  }

  @Override
  public IJavaClassConstructor getJavaConstructor()
  {
    return _ctor;
  }

  public Constructor getRawConstructor()
  {
    return ((ConstructorJavaClassConstructor)_ctor).getJavaConstructor();
  }

  @Override
  public boolean isDefault() {
    return _ctor.isDefault();
  }

  @Override
  public IType getType()
  {
    return getOwnersType();
  }

  @Override
  public IParameterInfo[] getGenericParameters()
  {
    return getParameters();
  }

  @Override
  public IParameterInfo[] getParameters()
  {
    if( _params != null )
    {
      return _params;
    }
    TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( getOwnersType(), getType() );
    IParameterInfo[] params = _ctor.convertGenericParameterTypes( this, actualParamByVarName );
    return _params = params;
  }

  @Override
  public IConstructorHandler getConstructor()
  {
    if( _ctorHandler == null )
    {
      _ctorHandler = new ConstructorHandlerAdapter();
    }
    return _ctorHandler;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    List<IAnnotationInfo> annotations = super.getDeclaredAnnotations();
    if (getConstructorDocs().get() != null && getConstructorDocs().get().isDeprecated()) {
      annotations.add(GosuShop.getAnnotationInfoFactory().createJavaAnnotation(makeDeprecated(getConstructorDocs().get().getDeprecated()), this));
    }
    return annotations;
  }

  private IDocRef<IConstructorNode> getConstructorDocs() {
    return _docs;
  }

  @Override
  public List<IExceptionInfo> getExceptions() {
    if (_exceptions == null) {
      IJavaClassInfo[] classes = _ctor.getExceptionTypes();
      _exceptions = new ArrayList<IExceptionInfo>();
      for ( final IJavaClassInfo exceptionClass : classes ) {
        _exceptions.add( new JavaExceptionInfo( this, exceptionClass, new IDocRef<IExceptionNode>() {
          @Override
          public IExceptionNode get() {
            return getConstructorDocs().get() == null ? null : getConstructorDocs().get().getException( exceptionClass );
          }
        } ) );
      }
    }

    // merge in methods exceptions with the annotations
    return _exceptions;
  }

  @Override
  public String getName() {
    return makeSignature();
  }

  private String makeSignature() {
    String name = getDisplayName();
    name += "(";
    IParameterInfo[] parameterInfos = getParameters();
    if (parameterInfos.length > 0) {
      name += " ";
      for (int i = 0; i < parameterInfos.length; i++) {
        IParameterInfo iParameterInfo = getParameters()[i];
        if (i != 0) {
          name += ", ";
        }
        name += iParameterInfo.getFeatureType().getName();
      }
      name += " ";
    }
    name += ")";
    return name;
  }

  @Override
  public String getDisplayName()
  {
    return getOwnersType().getRelativeName();
  }

  public String getShortDescription()
  {
    return getConstructorDocs().get() != null ? getConstructorDocs().get().getDescription() : null;
  }

  @Override
  public String getDescription()
  {
    return getConstructorDocs().get() != null ? getConstructorDocs().get().getDescription() : null;
  }

  @Override
  public boolean isStatic()
  {
    return true;
  }

  @Override
  public boolean isPrivate()
  {
    return java.lang.reflect.Modifier.isPrivate( _ctor.getModifiers() );
  }

  @Override
  public boolean isInternal()
  {
    return !isPrivate() && !isPublic() && !isProtected();
  }

  @Override
  public boolean isProtected()
  {
    return Modifier.isProtected( _ctor.getModifiers() );
  }

  @Override
  public boolean isPublic()
  {
    return Modifier.isPublic( _ctor.getModifiers() );
  }

  @Override
  public boolean isAbstract()
  {
    return Modifier.isAbstract( _ctor.getModifiers() );
  }

  @Override
  public IDocRef<IParamNode> getDocsForParam(final int paramIndex) {
    return new IDocRef<IParamNode>() {
      @Override
      public IParamNode get() {
        return getConstructorDocs().get() == null ? null : getConstructorDocs().get().getParams().get(paramIndex);
      }
    };
  }

  @Override
  public boolean isFinal()
  {
    return Modifier.isFinal( _ctor.getModifiers() );
  }

  public boolean isSynthetic()
  {
    return _ctor instanceof IJavaClassBytecodeConstructor && ((IJavaClassBytecodeConstructor)_ctor).isSynthetic();
  }

  private final class ConstructorHandlerAdapter implements IConstructorHandler
  {
    @Override
    public Object newInstance( Object... args )
    {
      ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
      if(TypeSystem.getCurrentModule() != null) {
        Thread.currentThread().setContextClassLoader( TypeSystem.getGosuClassLoader().getActualLoader() ); //_ctor.getDeclaringClass().getClassLoader() );
      }
      try
      {
        if( args == null || args.length == 0 )
        {
          return _ctor.newInstance( null );
        }
        return _ctor.newInstance( args );
      }
      catch( IllegalArgumentException e )
      {
        GosuExceptionUtil.throwArgMismatchException( e, "a constructor", ((ConstructorJavaClassConstructor)_ctor).getJavaParameterTypes(), args );
        return null;
      }
      catch( InvocationTargetException ex )
      {
        throw GosuExceptionUtil.forceThrow( ex.getTargetException() );
      }
      catch( Throwable t )
      {
        throw GosuExceptionUtil.forceThrow( t );
      }
      finally
      {
        Thread.currentThread().setContextClassLoader( previousClassLoader );
      }
    }
  }

  @Override
  protected IJavaAnnotatedElement getAnnotatedElement()
  {
    return _ctor;
  }

  @Override
  protected boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return true;
  }

  @Override
  protected boolean isHiddenViaFeatureDescriptor() {
    return false;
  }

  @Override
  protected boolean isDefaultEnumFeature()
  {
    return true;
  }
}
