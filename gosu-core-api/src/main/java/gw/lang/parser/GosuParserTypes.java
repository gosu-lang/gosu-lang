/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

//
// Stock types
// NOTE: Users of this class should use these when assigning types to ISymbol instances
//
public class GosuParserTypes {

  public static IType NUMBER_TYPE() {
    return JavaTypes.DOUBLE();
  }

  public static IType STRING_TYPE() {
    return JavaTypes.STRING();
  }

  public static IType CHAR_TYPE() {
    return JavaTypes.pCHAR();
  }

  public static IType BOOLEAN_TYPE() {
    return JavaTypes.BOOLEAN();
  }

  public static IType DATETIME_TYPE() {
    return JavaTypes.DATE();
  }

  public static IType LIST_TYPE() {
    return JavaTypes.LIST();
  }

  public static IType NULL_TYPE() {
    return JavaTypes.pVOID();
  }

  public static IType GENERIC_BEAN_TYPE() {
    return JavaTypes.OBJECT();
  }

}
