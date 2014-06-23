/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.parser.*;
import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IPropertyInfo;

public class IRPropertyFactory {

  public static IRProperty createIRProperty(IPropertyInfo propertyInfo) {
    if (propertyInfo == null) {
      return null;
    }
    
    return new IRPropertyFromPropertyInfo(propertyInfo);
  }

  public static IRProperty createIRProperty(DynamicPropertySymbol dps ) {
    return new IRPropertyFromDynamicPropertySymbol( dps );
  }

  public static IRProperty createIRProperty(ReducedDynamicPropertySymbol rdps ) {
    return new IRPropertyFromReducedDynamicPropertySymbol( rdps );
  }

  public static IRProperty createIRProperty(IReducedSymbol ds) {
    return new IRPropertyFromDynamicSymbol( ds );
  }

  public static IRProperty createIRProperty(ICompilableTypeInternal owningType, IReducedSymbol cs) {
    return new IRPropertyFromCapturedSymbol( owningType, cs );
  }
}
