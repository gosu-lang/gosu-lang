/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassParameterizedType;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.module.IModule;

public class JavaParameterizedType implements IJavaClassParameterizedType {
  private IJavaClassType[] _args;
  private IJavaClassType _rawType;

  public JavaParameterizedType( IJavaClassType[] args, IJavaClassType rawType ) {
    if (rawType == null) {
      throw new RuntimeException("Raw type of a parameterized type cannot be null");
    }
    for (IJavaClassType arg : args) {
      if (arg == null) {
        throw new RuntimeException("Arg type of a parameterized type cannot be null");
      }
    }
    _args = args;
    _rawType = rawType;
    for( IJavaClassType arg: args )
    {
      if( arg instanceof JavaWildcardType )
      {
        ((JavaWildcardType)arg).setOwnerType( this );
      }
    }
  }

  @Override
  public IJavaClassType[] getActualTypeArguments() {
    return _args;
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return getActualType(typeMap, false);
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    IType[] args = new IType[_args.length];
    for (int i = 0; i < _args.length; i++) {
      if (_args[i] != null) {
//        TypeSystem.pushModule(_args[i].getModule());
//        try {
          args[i] = _args[i].getActualType(typeMap, bKeepTypeVars);
//        } finally {
//          TypeSystem.popModule(_args[i].getModule());
//        }
      } else {
        args[i] = TypeSystem.getErrorType();
      }
    }
    IType actualType = _rawType.getActualType(typeMap);
    return actualType instanceof IErrorType ? actualType : actualType.getParameterizedType(args);
  }

  @Override
  public IJavaClassType getConcreteType() {
    return _rawType;
  }

  @Override
  public String getName() {
    String name = _rawType.getName() + "<";
    for (int i = 0; i < _args.length; i++) {
      IJavaClassType type = _args[i];
      name += type.getName();
      if (i < _args.length - 1) {
        name += ",";
      }
    }
    name += ">";
    return name;
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public IJavaClassType getComponentType() {
    return null;
  }

  @Override
  public IModule getModule() {
    return _rawType.getModule();
  }

  @Override
  public String getNamespace() {
    return _rawType.getNamespace();
  }

  public String toString() {
    String s = _rawType.getName() + "<";
    for (int i = 0; i < _args.length; i++) {
      s += _args[i].getName();
      if (i != _args.length - 1) {
        s += ", ";
      }
    }
    return s + ">";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof JavaParameterizedType)) {
      return false;
    }
    JavaParameterizedType t1 = this;
    JavaParameterizedType t2 = (JavaParameterizedType) obj;
    if (!t1._rawType.equals(t2._rawType)) {
      return false;
    }
    if (t1._args.length != t2._args.length) {
      return false;
    }
    for (int i = 0; i < t1._args.length; i++) {
      if (!t1._args[i].equals(t2._args[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
