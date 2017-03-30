/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.function.Function0;
import gw.lang.function.Function1;
import gw.lang.function.Function10;
import gw.lang.function.Function11;
import gw.lang.function.Function12;
import gw.lang.function.Function13;
import gw.lang.function.Function14;
import gw.lang.function.Function15;
import gw.lang.function.Function16;
import gw.lang.function.Function2;
import gw.lang.function.Function3;
import gw.lang.function.Function4;
import gw.lang.function.Function5;
import gw.lang.function.Function6;
import gw.lang.function.Function7;
import gw.lang.function.Function8;
import gw.lang.function.Function9;
import gw.lang.function.Procedure0;
import gw.lang.function.Procedure1;
import gw.lang.function.Procedure10;
import gw.lang.function.Procedure11;
import gw.lang.function.Procedure12;
import gw.lang.function.Procedure13;
import gw.lang.function.Procedure14;
import gw.lang.function.Procedure15;
import gw.lang.function.Procedure16;
import gw.lang.function.Procedure2;
import gw.lang.function.Procedure3;
import gw.lang.function.Procedure4;
import gw.lang.function.Procedure5;
import gw.lang.function.Procedure6;
import gw.lang.function.Procedure7;
import gw.lang.function.Procedure8;
import gw.lang.function.Procedure9;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;

public class BlockWrapper {

  public static Object toBlock( FeatureReference ref ) {
    return toBlock( ref, ref.hasReturn() );
  }
  public static Object toBlock( FeatureReference ref, boolean hasReturn ) {
    int i = ref.getFullArgTypes().size();
    if( !hasReturn )
    {
      switch( i )
      {
        case 0:
          return new Procedure0Wrapper( ref );
        case 1:
          return new Procedure1Wrapper( ref );
        case 2:
          return new Procedure2Wrapper( ref );
        case 3:
          return new Procedure3Wrapper( ref );
        case 4:
          return new Procedure4Wrapper( ref );
        case 5:
          return new Procedure5Wrapper( ref );
        case 6:
          return new Procedure6Wrapper( ref );
        case 7:
          return new Procedure7Wrapper( ref );
        case 8:
          return new Procedure8Wrapper( ref );
        case 9:
          return new Procedure9Wrapper( ref );
        case 10:
          return new Procedure10Wrapper( ref );
        case 11:
          return new Procedure11Wrapper( ref );
        case 12:
          return new Procedure12Wrapper( ref );
        case 13:
          return new Procedure13Wrapper( ref );
        case 14:
          return new Procedure14Wrapper( ref );
        case 15:
          return new Procedure15Wrapper( ref );
        case 16:
          return new Procedure16Wrapper( ref );
      }
    }
    else
    {
      switch( i )
      {
        case 0:
          return new Function0Wrapper( ref );
        case 1:
          return new Function1Wrapper( ref );
        case 2:
          return new Function2Wrapper( ref );
        case 3:
          return new Function3Wrapper( ref );
        case 4:
          return new Function4Wrapper( ref );
        case 5:
          return new Function5Wrapper( ref );
        case 6:
          return new Function6Wrapper( ref );
        case 7:
          return new Function7Wrapper( ref );
        case 8:
          return new Function8Wrapper( ref );
        case 9:
          return new Function9Wrapper( ref );
        case 10:
          return new Function10Wrapper( ref );
        case 11:
          return new Function11Wrapper( ref );
        case 12:
          return new Function12Wrapper( ref );
        case 13:
          return new Function13Wrapper( ref );
        case 14:
          return new Function14Wrapper( ref );
        case 15:
          return new Function15Wrapper( ref );
        case 16:
          return new Function16Wrapper( ref );
      }
    }
    return null;
  }

  private static class Function0Wrapper extends Function0
  {
    private FeatureReference _ref;
    Function0Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke() {
      return _ref.evaluate( Collections.emptyList().iterator() );
    }
  }

  private static class Function1Wrapper extends Function1
  {
    private FeatureReference _ref;
    Function1Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1) {
      return _ref.evaluate(Collections.singletonList(arg1).iterator());
    }
  }

  private static class Function2Wrapper extends Function2
  {
    private FeatureReference _ref;
    Function2Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2) {
      return _ref.evaluate(Arrays.asList(arg1, arg2).iterator());
    }
  }

  private static class Function3Wrapper extends Function3
  {
    private FeatureReference _ref;
    Function3Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3).iterator());
    }
  }

  private static class Function4Wrapper extends Function4
  {
    private FeatureReference _ref;
    Function4Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4).iterator());
    }
  }

  private static class Function5Wrapper extends Function5
  {
    private FeatureReference _ref;
    Function5Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5).iterator());
    }
  }

  private static class Function6Wrapper extends Function6
  {
    private FeatureReference _ref;
    Function6Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6).iterator());
    }
  }

  private static class Function7Wrapper extends Function7
  {
    private FeatureReference _ref;
    Function7Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7).iterator());
    }
  }

  private static class Function8Wrapper extends Function8
  {
    private FeatureReference _ref;
    Function8Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8).iterator());
    }
  }

  private static class Function9Wrapper extends Function9
  {
    private FeatureReference _ref;
    Function9Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9).iterator());
    }
  }

  private static class Function10Wrapper extends Function10
  {
    private FeatureReference _ref;
    Function10Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10).iterator());
    }
  }

  private static class Function11Wrapper extends Function11
  {
    private FeatureReference _ref;
    Function11Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11).iterator());
    }
  }

  private static class Function12Wrapper extends Function12
  {
    private FeatureReference _ref;
    Function12Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12).iterator());
    }
  }

  private static class Function13Wrapper extends Function13 {
    private FeatureReference _ref;
    Function13Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13).iterator());
    }
  }

  private static class Function14Wrapper extends Function14 {
    private FeatureReference _ref;
    Function14Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14).iterator());
    }
  }

  private static class Function15Wrapper extends Function15 {
    private FeatureReference _ref;
    Function15Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15).iterator());
    }
  }

  private static class Function16Wrapper extends Function16 {
    private FeatureReference _ref;
    Function16Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16).iterator());
    }
  }
  


  private static class Procedure0Wrapper extends Procedure0
  {
    private FeatureReference _ref;
    Procedure0Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke() {
      _ref.evaluate( Collections.emptyList().iterator() );
    }
  }

  private static class Procedure1Wrapper extends Procedure1
  {
    private FeatureReference _ref;
    Procedure1Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1) {
      _ref.evaluate(Collections.singletonList(arg1).iterator());
    }
  }

  private static class Procedure2Wrapper extends Procedure2
  {
    private FeatureReference _ref;
    Procedure2Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2) {
      _ref.evaluate(Arrays.asList(arg1, arg2).iterator());
    }
  }

  private static class Procedure3Wrapper extends Procedure3
  {
    private FeatureReference _ref;
    Procedure3Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3).iterator());
    }
  }

  private static class Procedure4Wrapper extends Procedure4
  {
    private FeatureReference _ref;
    Procedure4Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4).iterator());
    }
  }

  private static class Procedure5Wrapper extends Procedure5
  {
    private FeatureReference _ref;
    Procedure5Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5).iterator());
    }
  }

  private static class Procedure6Wrapper extends Procedure6
  {
    private FeatureReference _ref;
    Procedure6Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6).iterator());
    }
  }

  private static class Procedure7Wrapper extends Procedure7
  {
    private FeatureReference _ref;
    Procedure7Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7).iterator());
    }
  }

  private static class Procedure8Wrapper extends Procedure8
  {
    private FeatureReference _ref;
    Procedure8Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8).iterator());
    }
  }

  private static class Procedure9Wrapper extends Procedure9
  {
    private FeatureReference _ref;
    Procedure9Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9).iterator());
    }
  }

  private static class Procedure10Wrapper extends Procedure10
  {
    private FeatureReference _ref;
    Procedure10Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10).iterator());
    }
  }

  private static class Procedure11Wrapper extends Procedure11
  {
    private FeatureReference _ref;
    Procedure11Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11).iterator());
    }
  }

  private static class Procedure12Wrapper extends Procedure12
  {
    private FeatureReference _ref;
    Procedure12Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12).iterator());
    }
  }

  private static class Procedure13Wrapper extends Procedure13 {
    private FeatureReference _ref;
    Procedure13Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13).iterator());
    }
  }

  private static class Procedure14Wrapper extends Procedure14 {
    private FeatureReference _ref;
    Procedure14Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14).iterator());
    }
  }

  private static class Procedure15Wrapper extends Procedure15 {
    private FeatureReference _ref;
    Procedure15Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15).iterator());
    }
  }

  private static class Procedure16Wrapper extends Procedure16 {
    private FeatureReference _ref;
    Procedure16Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public void invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16) {
      _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16).iterator());
    }
  }

  @SuppressWarnings("unused")
  public static Object wrapFunctionAsProcedure( Object function, Class<?> procedureInterface )
  {
    return
      function == null ? null :
      Proxy.newProxyInstance( function.getClass().getClassLoader(), new Class[] {procedureInterface},
                              (proxy, method, args) -> {
                                if( method.getName().equals( "invoke" ) )
                                {
                                  Method invoke = function.getClass().getMethod( "invoke", method.getParameterTypes() );
                                  invoke.invoke( function, args );
                                  return null;
                                }
                                return method.invoke( function, args );
                              } );
  }
}
