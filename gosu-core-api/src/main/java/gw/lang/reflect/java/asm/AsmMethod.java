/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import gw.internal.ext.org.objectweb.asm.Type;

import gw.lang.reflect.java.Parameter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import manifold.api.gen.SrcAnnotated;

/**
 */
public class AsmMethod implements IGeneric {
  private AsmType _methodType;
  private int _modifiers;
  private AsmType _returnType;
  private AsmType _genericReturnType;
  private List<AsmType> _parameters;
  private List<Parameter> _paramInfos;
  private List<AsmType> _genericParameters;
  private List<AsmType> _exceptions;
  private List<AsmType> _genericExceptions;
  private List<AsmAnnotation> _annotations;
  private List<AsmAnnotation>[] _paramAnnotations;
  private AsmClass _owner;
  private boolean _bGeneric;
  private Object _defaultAnnoValue;
  private int _iLine;

  public AsmMethod( AsmClass owner, int access, String name, String desc, String[] exceptions ) {
    _owner = owner;
    _modifiers = access;
    _methodType = new AsmType( name );
    _annotations = Collections.emptyList();
    _exceptions = Collections.emptyList();
    _parameters = Collections.emptyList();
    _genericExceptions = Collections.emptyList();
    _genericParameters = Collections.emptyList();
    _paramInfos = Collections.emptyList();
    _iLine = -1;
    assignTypeFromDesc( desc );
    //noinspection unchecked
    _paramAnnotations = new List[_parameters.size()];
    assignExceptions( exceptions );
  }

  public AsmMethod( AsmClass owner, Symbol.MethodSymbol methodSymbol ) {
    _owner = owner;
    _modifiers = (int)SrcAnnotated.modifiersFrom( methodSymbol.getModifiers() );
    long flags = methodSymbol.flags();
    if( (flags & Flags.ACC_BRIDGE) != 0 )
    {
      _modifiers |= 0x00000040;  // Modifier.BRIDGE
    }
    if( (flags & Flags.ACC_VARARGS) != 0 )
    {
      _modifiers |= 0x00000080; // Modifier.VARARGS
    }
    _methodType = new AsmType( methodSymbol.name.toString() );
    _annotations = Collections.emptyList();
    _exceptions = Collections.emptyList();
    _parameters = Collections.emptyList();
    _genericExceptions = Collections.emptyList();
    _genericParameters = Collections.emptyList();
    _paramInfos = Collections.emptyList();
    _iLine = -1;
    ExecutableType type = (ExecutableType)methodSymbol.type;
    assignTypeFromDesc( type );
    //noinspection unchecked
    _paramAnnotations = new List[_parameters.size()];
    assignExceptions( type.getThrownTypes() );

    // assign type vars
    for( Symbol.TypeVariableSymbol member: methodSymbol.getTypeParameters() )
    {
      setGeneric();
      AsmType typeVar = AsmUtil.makeTypeVariable( member.name.toString(), true );
      _methodType.addTypeParameter( typeVar );
    }

    // assign annotations
    List<AsmAnnotation> annotations = new ArrayList<>();
    for( com.sun.tools.javac.code.Attribute.Compound annotationMirror: methodSymbol.getAnnotationMirrors() )
    {
      DeclaredType annotationType = annotationMirror.getAnnotationType();
      Retention retention = annotationType.getAnnotation( Retention.class );
      boolean isRuntime = retention != null && retention.value() == RetentionPolicy.RUNTIME;
      AsmAnnotation annotation = new AsmAnnotation( (com.sun.tools.javac.code.Type)annotationType, isRuntime );
      for( Map.Entry<Symbol.MethodSymbol, Attribute> entry: annotationMirror.getElementValues().entrySet() )
      {
        annotation.setValue( entry.getKey().flatName().toString(), entry.getValue().getValue() );
      }
      annotations.add( annotation );
    }
    _annotations = annotations;

    // assign parameter annotations
    com.sun.tools.javac.util.List<Symbol.VarSymbol> parameters = methodSymbol.getParameters();
    for( int i = 0; i < parameters.size(); i++ )
    {
      Symbol.VarSymbol parameter = parameters.get( i );
      // assign annotations
      List<AsmAnnotation> paramAnnos = new ArrayList<>();
      for( Attribute.Compound annotationMirror: parameter.getAnnotationMirrors() )
      {

        DeclaredType annotationType = annotationMirror.getAnnotationType();
        Retention retention = annotationType.getAnnotation( Retention.class );
        boolean isRuntime = retention != null && retention.value() == RetentionPolicy.RUNTIME;
        AsmAnnotation annotation = new AsmAnnotation( (com.sun.tools.javac.code.Type)annotationType, isRuntime );
        paramAnnos.add( annotation );
      }
      _paramAnnotations[i] = paramAnnos;
    }

  }

  public void update( List<DeclarationPartSignatureVisitor> paramTypes, DeclarationPartSignatureVisitor returnType, List<DeclarationPartSignatureVisitor> exceptionTypes  ) {
    for( DeclarationPartSignatureVisitor v: paramTypes ) {
      if( _genericParameters.isEmpty() ) {
        _genericParameters = new ArrayList<>();
      }
      _genericParameters.add( v.getCurrentType() );
    }
    _genericReturnType = returnType.getCurrentType();
    for( DeclarationPartSignatureVisitor v: exceptionTypes ) {
      if( _genericExceptions.isEmpty() ) {
        _genericExceptions = new ArrayList<>();
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
  @SuppressWarnings("UnusedDeclaration")
  public List<AsmType> getGenericExceptions() {
    return _genericExceptions.isEmpty() ? _exceptions : _genericExceptions;
  }
  @SuppressWarnings("UnusedDeclaration")
  void initGenericExceptions() {
    _genericExceptions = new ArrayList<>( _exceptions.size() );
    //noinspection Convert2streamapi
    for( AsmType exception : _exceptions ) {
      _genericExceptions.add( exception.copyNoArrayOrParameters() );
    }
  }

  public List<AsmAnnotation> getAnnotations() {
    return _annotations;
  }
  @SuppressWarnings("UnusedDeclaration")
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
        _exceptions = new ArrayList<>( exceptions.length );
      }
      _exceptions.add( AsmUtil.makeType( exception ) );
    }
  }

  private void assignExceptions( List<? extends TypeMirror> exceptions ) {
    if( exceptions == null ) {
      return;
    }
    for( TypeMirror exception : exceptions ) {
      if( _exceptions.isEmpty() ) {
        _exceptions = new ArrayList<>( exceptions.size() );
        _genericExceptions = new ArrayList<>( exceptions.size() );
      }
      _genericExceptions.add( AsmUtil.makeType( (com.sun.tools.javac.code.Type)exception ) );
      _exceptions.add( AsmUtil.makeErasedType( (com.sun.tools.javac.code.Type)exception ) );
    }
  }

  private void assignTypeFromDesc( String desc ) {
    Type returnType = Type.getReturnType( desc );
    _returnType = AsmUtil.makeType( returnType );

    Type[] params = Type.getArgumentTypes( desc );
    for( Type param : params ) {
      if( _parameters.isEmpty() ) {
        _parameters = new ArrayList<>( params.length );
      }
      _parameters.add( AsmUtil.makeType( param ) );
    }
  }

  private void assignTypeFromDesc( ExecutableType type ) {
    _returnType = AsmUtil.makeErasedType( (com.sun.tools.javac.code.Type)type.getReturnType() );
    _genericReturnType = AsmUtil.makeType( (com.sun.tools.javac.code.Type)type.getReturnType() );

    List<? extends TypeMirror> parameterTypes = type.getParameterTypes();
    for( TypeMirror param : parameterTypes ) {
      if( _parameters.isEmpty() ) {
        _parameters = new ArrayList<>( parameterTypes.size() );
        _genericParameters = new ArrayList<>( parameterTypes.size() );
      }
      _parameters.add( AsmUtil.makeErasedType( (com.sun.tools.javac.code.Type)param ) );
      _genericParameters.add( AsmUtil.makeType( (com.sun.tools.javac.code.Type)param ) );
    }
  }

  public void addAnnotation( AsmAnnotation asmAnnotation ) {
    if( _annotations.isEmpty() ) {
      _annotations = new ArrayList<>( 2 );
    }
    _annotations.add( asmAnnotation );
  }

  public void addParameterAnnotation( int iParam, AsmAnnotation asmAnnotation ) {
    if( _paramAnnotations[iParam] == null ) {
      _paramAnnotations[iParam] = new ArrayList<>( 2 );
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
    StringBuilder paramString = new StringBuilder( "(" );
    for( AsmType param : _genericParameters.isEmpty() ? _parameters : _genericParameters ) {
      if( paramString.length() > 1 ) {
        paramString.append( ", " );
      }
      paramString.append( param.getFqn() );
    }
    paramString.append( ")" );
    return paramString.toString();
  }

  private String makeTypeVarsString() {
    StringBuilder tvString = new StringBuilder();
    if( isGeneric() ) {
      tvString = new StringBuilder( "<" );
      for( AsmType tv : getMethodType().getTypeParameters() ) {
        if( tvString.length() > 1 ) {
          tvString.append( ", " );
        }
        tvString.append( tv.getFqn() );
      }
      tvString.append( ">" );
    }
    return tvString.toString();
  }

  void assignLineNumber( int iLine ) {
    if( _iLine < 0 ) {
      _iLine = iLine;
    }
  }
  public int getLineNumber() {
    return _iLine;
  }

  public AsmType findTypeVariable( String tv ) {
    for( AsmType p: _methodType.getTypeParameters() ) {
      if( p.getName().equals( tv ) ) {
        return p;
      }
    }
    return null;
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
    return _returnType.equals( asmMethod._returnType );
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

  void assignParameter( String name, int access )
  {
    if( _paramInfos.isEmpty() )
    {
      _paramInfos = new ArrayList<>( 2 );
    }

    _paramInfos.add( new Parameter( name, access ) );
  }

  public List<Parameter> getParameterInfos()
  {
    return _paramInfos;
  }
}
