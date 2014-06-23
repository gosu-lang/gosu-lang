/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 */
public class RepeatableContainerAnnotationInfo implements IAnnotationInfo {
  private IType _owner;
  private IAnnotationInfo[] _annotations;
  private IType _container;

  public RepeatableContainerAnnotationInfo( IAnnotationInfo annotations[], IType container, IType owner ) {
    _annotations = annotations;
    _container = container;
    _owner = owner;
  }

  public String getName() {
    return _container.getName();
  }

  public IType getOwnersType() {
    return _owner;
  }

  public String getDisplayName() {
    return getName();
  }

  public String getDescription() {
    return getName();
  }

  public Annotation getInstance() {
    return makeAnnotationInfoProxy();
  }

  private Annotation makeAnnotationInfoProxy() {
    throw new IllegalStateException( "Should not need to get runtime version of this annotation, use the IAnnotationInfo API." );
//    //## todo: don't load the class if the annotationType doesn't correspond with a Class object i.e., don't load project classes during parsing/compilation of said project!
//    Class annotationClass = ((IHasJavaClass)_container).getBackingClass();
//
//    return (Annotation)Proxy.newProxyInstance( annotationClass.getClassLoader(), new Class[]{annotationClass},
//                                               new AnnotationInfoInvocationHandler( this ) );
  }

  @Override
  public Object getFieldValue( String fieldName ) {
    assert fieldName.equals( "value" );
    try {
      return _annotations;
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public IType getType() {
    return _container;
  }

  public String toString() {
    return getName();
  }

  public boolean retainInBytecode() {
    List<IAnnotationInfo> annotations = _container.getTypeInfo().getAnnotationsOfType( JavaTypes.getJreType( Retention.class ) );
    if( annotations != null )
    {
      for( IAnnotationInfo annotationInfo : annotations )
      {
        String fieldValue = (String) annotationInfo.getFieldValue( "value" );
        if( !fieldValue.equals( RetentionPolicy.SOURCE.name() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

}
