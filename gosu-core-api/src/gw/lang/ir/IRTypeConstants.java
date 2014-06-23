/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.util.concurrent.LocklessLazyVar;

import java.util.Iterator;

@UnstableAPI
public class IRTypeConstants {

  final public static IRType ITYPE() {
    return ITYPE.get();
  }

  final public static IRType OBJECT() {
    return OBJECT.get();
  }

  final public static IRType STRING() {
    return STRING.get();
  }

  final public static IRType CLASS() {
    return CLASS.get();
  }

  final public static IRType pVOID() {
    return pVOID.get();
  }

  final public static IRType pBOOLEAN() {
    return pBOOLEAN.get();
  }

  final public static IRType pBYTE() {
    return pBYTE.get();
  }

  final public static IRType pSHORT() {
    return pSHORT.get();
  }

  final public static IRType pCHAR() {
    return pCHAR.get();
  }

  final public static IRType pINT() {
    return pINT.get();
  }

  final public static IRType pLONG() {
    return pLONG.get();
  }

  final public static IRType pFLOAT() {
    return pFLOAT.get();
  }

  final public static IRType pDOUBLE() {
    return pDOUBLE.get();
  }

  final public static IRType ITERATOR() {
    return ITERATOR.get();
  }

  final public static IRType NUMBER() {
    return NUMBER.get();
  }

  private static LocklessLazyVar<IRType> ITYPE = make(IType.class);
  private static LocklessLazyVar<IRType> OBJECT = make(Object.class);
  private static LocklessLazyVar<IRType> STRING = make(String.class);
  private static LocklessLazyVar<IRType> CLASS = make(Class.class);
  private static LocklessLazyVar<IRType> pVOID = make(void.class);
  private static LocklessLazyVar<IRType> pBOOLEAN = make(boolean.class);
  private static LocklessLazyVar<IRType> pBYTE = make(byte.class);
  private static LocklessLazyVar<IRType> pSHORT = make(short.class);
  private static LocklessLazyVar<IRType> pCHAR = make(char.class);
  private static LocklessLazyVar<IRType> pINT = make(int.class);
  private static LocklessLazyVar<IRType> pLONG = make(long.class);
  private static LocklessLazyVar<IRType> pFLOAT = make(float.class);
  private static LocklessLazyVar<IRType> pDOUBLE = make(double.class);
  private static LocklessLazyVar<IRType> ITERATOR = make(Iterator.class);
  private static LocklessLazyVar<IRType> NUMBER = make(Number.class);

  private static LocklessLazyVar<IRType> make(final Class<?> clazz) {
    return new LocklessLazyVar<IRType>() {
      @Override
      protected IRType init() {
        return GosuShop.getIRTypeResolver().getDescriptor( clazz );
      }
    };
  }

  static {
    // It is necessary to clear those since they keep references to JRE module.
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        ITYPE.clear();
        OBJECT.clear();
        STRING.clear();
        pVOID.clear();
        pBOOLEAN.clear();
        pBYTE.clear();
        pSHORT.clear();
        pCHAR.clear();
        pINT.clear();
        pLONG.clear();
        pFLOAT.clear();
        pDOUBLE.clear();
        ITERATOR.clear();
        NUMBER.clear();
      }
    });
  }
}
