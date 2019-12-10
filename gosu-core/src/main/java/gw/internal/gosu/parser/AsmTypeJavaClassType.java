/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaArrayClassInfo;
import gw.lang.reflect.java.JavaSourceElement;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.AsmWildcardType;
import gw.lang.reflect.java.asm.IAsmType;

import java.util.LinkedHashSet;

public abstract class AsmTypeJavaClassType extends JavaSourceElement implements IJavaClassType {
  private IAsmType _type;

  public AsmTypeJavaClassType( IAsmType type ) {
    _type = type;
  }

  protected IAsmType getType() {
    return _type;
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap ) {
    return TypeLord.getActualType( _type, typeMap );
  }

  @Override
  public IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars ) {
    return TypeLord.getActualType( _type, typeMap, bKeepTypeVars, new LinkedHashSet<>() );
  }

  public static IJavaClassType createType( IAsmType rawType ) {
    return createType( null, rawType );
  }
  public static IJavaClassType createType( IAsmType genType, IAsmType rawType ) {
    IJavaClassType type = null;
    if( rawType.isArray() && (rawType.isTypeVariable() || rawType.isParameterized()) ) {
      type = new AsmGenericArrayTypeJavaClassGenericArrayType( rawType );
    }
    else if( rawType.isTypeVariable() ) {
      type = new AsmTypeVariableJavaClassTypeVariable( rawType );
    }
    else if( rawType.isParameterized() ) {
      type = new AsmParameterizedTypeJavaClassParameterizedType( rawType );
    }
    else if( rawType instanceof AsmWildcardType ) {
      type = new AsmWildcardTypeJavaClassWildcardType( genType, (AsmWildcardType)rawType );
    }
    else if( rawType instanceof AsmClass ) {
      type = JavaSourceUtil.getClassInfo( (AsmClass)rawType );
    }
    else if( rawType instanceof AsmType ) {
      type = JavaSourceUtil.getClassInfo( rawType.getName() );
      while( rawType.getComponentType() != null ) {
        type = new JavaArrayClassInfo( (IJavaClassInfo)type );
        rawType = rawType.getComponentType();
      }
    }
    return type;
  }

  @Override
  public String getName() {
    return _type.toString();
  }

  @Override
  public String getNamespace() {
    return null;
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !(o instanceof AsmTypeJavaClassType) ) {
      return false;
    }

    AsmTypeJavaClassType that = (AsmTypeJavaClassType)o;

    if( !_type.equals( that._type ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _type.hashCode();
    return result;
  }

  @Override
  public boolean isArray() {
    return _type.isArray();
  }

  @Override
  public IJavaClassType getComponentType() {
    return null;
  }
}

