/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 */
public class AsmMethod implements IGeneric {
  private AsmType _methodType;
  private int _modifiers;
  private AsmType _returnType;
  private AsmType _genericReturnType;
  private List<AsmType> _parameters;
  private List<AsmType> _genericParameters;
  private List<AsmType> _exceptions;
  private List<AsmType> _genericExceptions;
  private List<AsmAnnotation> _annotations;
  private List<AsmAnnotation>[] _paramAnnotations;
  private AsmClass _owner;
  private boolean _bGeneric;
  private Object _defaultAnnoValue;

  public AsmMethod( AsmClass owner, int access, String name, String desc, String[] exceptions ) {
    _owner = owner;
    _modifiers = access;
    _methodType = new AsmType( name );
    _annotations = Collections.emptyList();
    _exceptions = Collections.emptyList();
    _parameters = Collections.emptyList();
    _genericExceptions = Collections.emptyList();
    _genericParameters = Collections.emptyList();
    assignTypeFromDesc( desc );
    //noinspection unchecked
    _paramAnnotations = new List[_parameters.size()];
    assignExceptions( exceptions );
  }

  public void update( List<DeclarationPartSignatureVisitor> paramTypes, DeclarationPartSignatureVisitor returnType, List<DeclarationPartSignatureVisitor> exceptionTypes  ) {
    for( DeclarationPartSignatureVisitor v: paramTypes ) {
      if( _genericParameters.isEmpty() ) {
        _genericParameters = new ArrayList<AsmType>();
      }
      _genericParameters.add( v.getCurrentType() );
    }
    _genericReturnType = returnType.getCurrentType();
    for( DeclarationPartSignatureVisitor v: exceptionTypes ) {
      if( _genericExceptions.isEmpty() ) {
        _genericExceptions = new ArrayList<AsmType>();
      }
      _genericExceptions.add( v.getCurrentType() );
    }
  }

  public String getName() {
    return _methodType.getName();
  }

  public AsmType getMethodType() {
    return _methodType;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public List<AsmType> getParameters() {
    return _parameters;
  }
  public List<AsmType> getGenericParameters() {
    return _genericParameters.isEmpty() ? _parameters : _genericParameters;
  }

  public AsmType getReturnType() {
    return _returnType;
  }
  void setReturnType( AsmType returnType ) {
    _returnType = returnType;
  }
  public AsmType getGenericReturnType() {
    return _genericReturnType == null ? _returnType : _genericReturnType;
  }
  void initGenericReturnType() {
    _genericReturnType = _returnType.copyNoArrayOrParameters();
  }

  public AsmClass getDeclaringClass() {
    return _owner;
  }

  public boolean isGeneric() {
    return _bGeneric;
  }
  public void setGeneric() {
    _bGeneric = true;
  }

  public boolean isSynthetic() {
    return (_modifiers & 0x00001000) != 0;
  }

  public boolean isBridge() {
    return (_modifiers & 0x00000040) != 0;
  }

  public boolean isConstructor() {
    return getName().equals( "<init>" );
  }

  public List<AsmType> getExceptions() {
    return _exceptions;
  }
  public List<AsmType> getGenericExceptions() {
    return _genericExceptions.isEmpty() ? _exceptions : _genericExceptions;
  }
  void initGenericExceptions() {
    _genericExceptions = new ArrayList<AsmType>( _exceptions.size() );
    for( AsmType exception : _exceptions ) {
      _genericExceptions.add( exception.copyNoArrayOrParameters() );
    }
  }

  public List<AsmAnnotation> getAnnotations() {
    return _annotations;
  }
  public List<AsmAnnotation>[] getParameterAnnotations() {
    return _paramAnnotations;
  }
  public Object getAnnotationDefaultValue() {
    return _defaultAnnoValue;
  }
  public void setAnnotationDefaultValue( Object value ) {
    value = AsmAnnotation.makeAppropriateValue( value );
    _defaultAnnoValue = value;
  }
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return getAnnotation( annotationClass ) != null;
  }
  public AsmAnnotation getAnnotation( Class annotationClass ) {
    for( AsmAnnotation anno: getAnnotations() ) {
      if( annotationClass.getName().equals( anno.getType().getName() ) ) {
        return anno;
      }
    }
    return null;
  }

  private void assignExceptions( String[] exceptions ) {
    if( exceptions == null ) {
      return;
    }
    for( String exception : exceptions ) {
      if( _exceptions.isEmpty() ) {
        _exceptions = new ArrayList<AsmType>( exceptions.length );
      }
      _exceptions.add( AsmUtil.makeType( exception ) );
    }
  }

  private void assignTypeFromDesc( String desc ) {
    Type returnType = Type.getReturnType( desc );
    _returnType = AsmUtil.makeType( returnType );

    Type[] params = Type.getArgumentTypes( desc );
    for( Type param : params ) {
      if( _parameters.isEmpty() ) {
        _parameters = new ArrayList<AsmType>( params.length );
      }
      _parameters.add( AsmUtil.makeType( param ) );
    }
  }

  public void addAnnotation( AsmAnnotation asmAnnotation ) {
    if( _annotations.isEmpty() ) {
      _annotations = new ArrayList<AsmAnnotation>( 2 );
    }
    _annotations.add( asmAnnotation );
  }

  public void addParameterAnnotation( int iParam, AsmAnnotation asmAnnotation ) {
    if( _paramAnnotations[iParam] == null ) {
      _paramAnnotations[iParam] = new ArrayList<AsmAnnotation>( 2 );
    }
    _paramAnnotations[iParam].add( asmAnnotation );
  }

  public String toString() {
    int mod = getModifiers();
    return ((mod == 0) ? "" : (Modifier.toString( mod ) + " "))
        + makeTypeVarsString()
        + (getGenericReturnType() == null ? getReturnType().getFqn() : getGenericReturnType().getFqn()) + " "
        + getName()
        + makeParameterString();
  }

  private String makeParameterString() {
    String paramString = "(";
    for( AsmType param : _genericParameters.isEmpty() ? _parameters : _genericParameters ) {
      if( paramString.length() > 1 ) {
        paramString += ", ";
      }
      paramString += param.getFqn();
    }
    paramString += ")";
    return paramString;
  }

  private String makeTypeVarsString() {
    String tvString = "";
    if( isGeneric() ) {
      tvString = "<";
      for( AsmType tv : getMethodType().getTypeParameters() ) {
        if( tvString.length() > 1 ) {
          tvString += ", ";
        }
        tvString += tv.getFqn();
      }
      tvString += ">";
    }
    return tvString;
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null || getClass() != o.getClass() ) {
      return false;
    }

    AsmMethod asmMethod = (AsmMethod)o;

    if( _bGeneric != asmMethod._bGeneric ) {
      return false;
    }
    if( _modifiers != asmMethod._modifiers ) {
      return false;
    }
    if( !_annotations.equals( asmMethod._annotations ) ) {
      return false;
    }
    if( _defaultAnnoValue != null ? !_defaultAnnoValue.equals( asmMethod._defaultAnnoValue ) : asmMethod._defaultAnnoValue != null ) {
      return false;
    }
    if( !_exceptions.equals( asmMethod._exceptions ) ) {
      return false;
    }
    if( !_genericExceptions.equals( asmMethod._genericExceptions ) ) {
      return false;
    }
    if( !_genericParameters.equals( asmMethod._genericParameters ) ) {
      return false;
    }
    if( !_genericReturnType.equals( asmMethod._genericReturnType ) ) {
      return false;
    }
    if( !_methodType.equals( asmMethod._methodType ) ) {
      return false;
    }
    if( !_owner.equals( asmMethod._owner ) ) {
      return false;
    }
    if( !Arrays.equals( _paramAnnotations, asmMethod._paramAnnotations ) ) {
      return false;
    }
    if( !_parameters.equals( asmMethod._parameters ) ) {
      return false;
    }
    if( !_returnType.equals( asmMethod._returnType ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _methodType.hashCode();
    result = 31 * result + _modifiers;
    result = 31 * result + _returnType.hashCode();
    result = 31 * result + _genericReturnType.hashCode();
    result = 31 * result + _parameters.hashCode();
    result = 31 * result + _genericParameters.hashCode();
    result = 31 * result + _exceptions.hashCode();
    result = 31 * result + _genericExceptions.hashCode();
    result = 31 * result + _annotations.hashCode();
    result = 31 * result + Arrays.hashCode( _paramAnnotations );
    result = 31 * result + _owner.hashCode();
    result = 31 * result + (_bGeneric ? 1 : 0);
    result = 31 * result + (_defaultAnnoValue != null ? _defaultAnnoValue.hashCode() : 0);
    return result;
  }
}
