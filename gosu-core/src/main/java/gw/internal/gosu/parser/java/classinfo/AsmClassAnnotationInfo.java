/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmAnnotation;

import gw.lang.reflect.module.IModule;
import java.lang.annotation.Annotation;
import gw.util.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsmClassAnnotationInfo implements IAnnotationInfo {
  private AsmAnnotation _annotation;
  private IJavaAnnotatedElement _owner;

  public AsmClassAnnotationInfo( AsmAnnotation annotation, IJavaAnnotatedElement owner ) {
    _annotation = annotation;
    _owner = owner;
  }

  public String getName() {
    return _annotation.getType().getName();
  }

  public IType getOwnersType() {
    return _owner.getEnclosingClass().getJavaType();
  }

  public String getDisplayName() {
    return getName();
  }

  public String getDescription() {
    return getName();
  }

  public Annotation getInstance() {
    throw new RuntimeException( "Not supported for source types" );
  }

  @Override
  public Object getFieldValue( String fieldName ) {
    return normalizeValue( fieldName, _annotation.getFieldValues().get( fieldName ) );
  }

  private Object normalizeValue( String fieldName, Object value ) {
    if( value instanceof List ) {
      value = makeArray( fieldName, (List)value );
    }
    else if( value instanceof AsmAnnotation ) {
      value = new AsmClassAnnotationInfo( (AsmAnnotation)value, _owner );
    }
    return value;
  }

  private Object makeArray( String fieldName, List l ) {
    IJavaClassInfo classInfo = TypeSystem.getJavaClassInfo( _annotation.getType().getName(), findModule( _owner ) );
    try {
      IJavaClassMethod method = classInfo.getDeclaredMethod( fieldName );
      IJavaClassInfo ci = method.getReturnClassInfo();
      return makeArray( ci, l, _owner );
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private IModule findModule( IJavaAnnotatedElement elem )
  {
    if( elem instanceof IJavaClassType )
    {
      return ((IJavaClassType)elem).getModule();
    }
    return findModule( elem.getEnclosingClass() );
  }

  public IType getType() {
    return TypeSystem.getByFullName( getName() );
  }

  public String toString() {
    return getName();
  }

  private static final Map<String, Class> SUPPORTED_TYPES = new HashMap<String, Class>();
  static {
    SUPPORTED_TYPES.put( byte.class.getName(), byte.class );
    SUPPORTED_TYPES.put( boolean.class.getName(), boolean.class );
    SUPPORTED_TYPES.put( char.class.getName(), char.class );
    SUPPORTED_TYPES.put( short.class.getName(), short.class );
    SUPPORTED_TYPES.put( int.class.getName(), int.class );
    SUPPORTED_TYPES.put( long.class.getName(), long.class );
    SUPPORTED_TYPES.put( float.class.getName(), float.class );
    SUPPORTED_TYPES.put( double.class.getName(), double.class );
    SUPPORTED_TYPES.put( void.class.getName(), void.class );
    SUPPORTED_TYPES.put( String.class.getName(), String.class );
    SUPPORTED_TYPES.put( AsmAnnotation.class.getName(), IAnnotationInfo.class );
  }
  public static Object makeArray( IJavaClassInfo ci, List l, IJavaAnnotatedElement owner ) throws ClassNotFoundException {
    Object array;
    IJavaClassInfo componentType = ci.getComponentType();
    if( !componentType.isArray() ) {
      Class compClass;
      if( componentType.isEnum() ) {
        compClass = String.class;
      }
      else {
        compClass = getClass( componentType );
      }
      if( compClass == null ) {
        throw new IllegalStateException();
      }
      array = Array.newInstance( compClass, l.size() );
      for( int i = 0; i < l.size(); i++ ) {
        Object elem = l.get( i );
        if( compClass == IAnnotationInfo.class ) {
          elem = new AsmClassAnnotationInfo( (AsmAnnotation)elem, owner );
        }
        try {
          Array.set( array, i, elem );
        }
        catch( RuntimeException e ) {
          throw e;
        }
      }
    }
    else {
      array = Array.newInstance( getClass( componentType ), l.size() );
      for( int i = 0; i < l.size(); i++ ) {
        Object elem = makeArray( componentType, (List)l.get( i ), owner );
        Array.set( array, i, elem );
      }
    }
    return array;
  }

  private static Class<?> getClass( IJavaClassInfo ci ) {
    if( ci.isArray() ) {
      return Array.newInstance( getClass( ci.getComponentType() ), 0 ).getClass();
    }
    if( ci.isAnnotation() ) {
      return IAnnotationInfo.class;
    }
    else if( ci.isEnum() ) {
      return String.class;
    }
    else {
      String componentName = ci.getName();
      Class cls = SUPPORTED_TYPES.get( componentName );
      return cls == null ? String.class : cls;
    }
  }

}
