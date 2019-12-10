/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IDynamicType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.LockingLazyVar;

import java.util.HashMap;
import java.util.Map;

public class GosuTypes {
  private static LockingLazyVar<IFunctionType> DEF_CTOR_TYPE = new LockingLazyVar<IFunctionType>() {
    protected IFunctionType init() {
      return new FunctionType( "__def_ctor", JavaTypes.pVOID(), null );
    }
  };
  public static final String IMONITORLOCK_NAME = "gw.lang.IMonitorLock";
  public static Map<String, IType> CACHE = new HashMap<>();

  static {
    TypeSystem.addShutdownListener( () -> {
      DEF_CTOR_TYPE.clear();
      CACHE.clear();
    } );
  }

  public static IType AUTOCREATE() {
    return getType("gw.lang.Autocreate");
  }

  public static IType IDISPOSABLE() {
    return getType("gw.lang.IDisposable");
  }

  public static IType IMONITORLOCK() {
    return getType(IMONITORLOCK_NAME);
  }

  public static IType DYNAMIC() {
    return getType( IDynamicType.QNAME );
  }

  public static IFunctionType DEF_CTOR_TYPE() {
    return DEF_CTOR_TYPE.get();
  }

  public static IType getType(String fqn) {
    IType type = CACHE.get(fqn);
    if (type == null) {
      type = TypeSystem.getByFullNameIfValid( fqn );
      CACHE.put(fqn, type);
    }
    return type;
  }
}
