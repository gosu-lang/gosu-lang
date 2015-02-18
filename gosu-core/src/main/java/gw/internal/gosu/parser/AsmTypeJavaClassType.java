/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaArrayClassInfo;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.AsmWildcardType;
import gw.lang.reflect.java.asm.IAsmType;
import gw.lang.reflect.module.IModule;

import java.util.HashSet;

public abstract class AsmTypeJavaClassType implements IJavaClassType {
  private IAsmType _type;
  protected IModule _module;

  public AsmTypeJavaClassType( IAsmType type, IModule module ) {
    _type = type;
    _module = module;
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
    return TypeLord.getActualType( _type, typeMap, bKeepTypeVars, new HashSet<IAsmType>() );
  }

  public static IJavaClassType createType( IAsmType rawType, IModule module ) {
    IJavaClassType type = null;
    if( rawType.isArray() && (rawType.isTypeVariable() || rawType.isParameterized()) ) {
      type = new AsmGenericArrayTypeJavaClassGenericArrayType( rawType, module );
    }
    else if( rawType.isTypeVariable() ) {
      type = new AsmTypeVariableJavaClassTypeVariable( rawType, module );
    }
    else if( rawType.isParameterized() ) {
      type = new AsmParameterizedTypeJavaClassParameterizedType( rawType, module );
    }
    else if( rawType instanceof AsmWildcardType ) {
      type = new AsmWildcardTypeJavaClassWildcardType( (AsmWildcardType)rawType, module );
    }
    else if( rawType instanceof AsmClass ) {
      type = JavaSourceUtil.getClassInfo( (AsmClass)rawType, module );
    }
    else if( rawType instanceof AsmType ) {
      type = JavaSourceUtil.getClassInfo( rawType.getName(), module );
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
  public IModule getModule() {
    return _module;
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

    if( _module != null ? !_module.equals( that._module ) : that._module != null ) {
      return false;
    }
    if( !_type.equals( that._type ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _type.hashCode();
    result = 31 * result + (_module != null ? _module.hashCode() : 0);
    return result;
  }
}

