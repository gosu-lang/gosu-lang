/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class IRMethodFactory {
  
  public static IRMethodFromMethodInfo createIRMethod(IMethodInfo originalMethodInfo, IFunctionType functionType) {
    if (originalMethodInfo == null) {
      return null;
    }

//    IMethodInfo resolvedMethodInfo = originalMethodInfo;
//
//    while( resolvedMethodInfo instanceof IMethodInfoDelegate) {
//      resolvedMethodInfo = ((IMethodInfoDelegate)resolvedMethodInfo).getSource();
//    }
//
//    // If the owning type is a Gosu proxy, we actually want the IMethodInfo off of the underlying Java type
//    IType owner = getTrueOwningType(resolvedMethodInfo);
//    if (IGosuClass.ProxyUtil.isProxy(owner)) {
//      owner = IGosuClass.ProxyUtil.getProxiedType(owner);
//      IType[] parameterTypes = getParameterTypes( resolvedMethodInfo );
//      resolvedMethodInfo = owner.getTypeInfo().getMethod(resolvedMethodInfo.getDisplayName(), parameterTypes);
//
//      if (resolvedMethodInfo == null) {
//        System.out.println("Huh?");
//      }
//
//      assert(resolvedMethodInfo != null);
//    }

    return new IRMethodFromMethodInfo(originalMethodInfo, functionType);
  }

  public static IRMethod createIRMethod( IConstructorInfo constructor ) {
    return new IRMethodFromConstructorInfo( constructor );
  }

  public static IRMethod createIRMethod(Class cls, String name, Class... paramTypes) {
    return createIRMethod(AbstractElementTransformer.getDeclaredMethod(cls, name, paramTypes));
  }

  public static IRMethod createIRMethod(IJavaClassInfo cls, String name, Class... paramTypes) {
    return createIRMethod(AbstractElementTransformer.getDeclaredMethod(cls, name, paramTypes));
  }

  public static IRMethod createIRMethod(Method method) {
    return new IRMethodFromMethod(method);  
  }

  public static IRMethod createIRMethod(IJavaClassMethod method) {
    return new IRMethodFromJavaMethodInfo(method);
  }

  public static IRMethod createConstructorIRMethod(IType gosuClass, DynamicFunctionSymbol dfs, int numberOfTypeParameters) {
    return new IRMethodForConstructorSymbol(gosuClass, dfs, numberOfTypeParameters);
  }

  public static IRMethod createIRMethod(IType owner, String name, IType returnType, IType[] parameterTypes, IRelativeTypeInfo.Accessibility accessibility, boolean bStatic) {
    return new SyntheticIRMethod( owner, name, IRTypeResolver.getDescriptor(returnType), convertToIRTypes(parameterTypes), accessibility, bStatic );
  }

  public static IRMethod createIRMethod(IType owner, String name, IRType returnType, List<IRType> parameterTypes, IRelativeTypeInfo.Accessibility accessibility, boolean bStatic) {
    return new SyntheticIRMethod( owner, name, returnType, parameterTypes, accessibility, bStatic );
  }

  public static IRMethod createConstructorIRMethod(IType owner, IRType[] parameterTypes ) {
    return new SyntheticIRMethod( owner, "<init>", IRTypeConstants.pVOID(), Arrays.asList(parameterTypes), IRelativeTypeInfo.Accessibility.PUBLIC, false );
  }

  private static List<IRType> convertToIRTypes(IType[] types) {
    List<IRType> result = new ArrayList<IRType>();
    for (IType type : types) {
      result.add(IRTypeResolver.getDescriptor(type));
    }
    return result;
  }

//  private static IType getTrueOwningType( IMethodInfo mi ) {
//    if( mi instanceof IJavaMethodInfo)
//    {
//      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
//      Method m = ((IJavaMethodInfo)mi).getMethod();
//      if( m != null )
//      {
//        return TypeSystem.get( m.getDeclaringClass() );
//      }
//    }
//    return mi.getOwnersType();
//  }
//
//  private static IType[] getParameterTypes( IMethodInfo mi ) {
//    if ( mi instanceof IGosuMethodInfo ) {
//      IDynamicFunctionSymbol dfs = ((IGosuMethodInfo)mi).getDfs();
//      while( dfs instanceof ParameterizedDynamicFunctionSymbol)
//      {
//        ParameterizedDynamicFunctionSymbol pdfs = (ParameterizedDynamicFunctionSymbol)dfs;
//        dfs = pdfs.getBackingDfs();
//      }
//      IType[] boundedTypes = new IType[dfs.getArgTypes().length];
//      for( int i = 0; i < dfs.getArgTypes().length; i++ )
//      {
//        boundedTypes[i] = TypeLord.getDefaultParameterizedTypeWithTypeVars( dfs.getArgTypes()[i] );
//      }
//      return boundedTypes;
//    } else {
//      IParameterInfo[] parameterInfos = mi.getParameters();
//      IType[] parameterTypes = new IType[parameterInfos.length];
//      for (int i = 0; i < parameterInfos.length; i++) {
//        parameterTypes[i] = parameterInfos[i].getFeatureType();
//      }
//      return parameterTypes;
//    }
//  }
}
