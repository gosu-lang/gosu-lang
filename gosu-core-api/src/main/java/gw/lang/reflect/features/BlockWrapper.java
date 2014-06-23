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

import java.util.ArrayList;
import java.util.Arrays;

public class BlockWrapper {

  public static Object toBlock(FeatureReference ref) {
    int i = ref.getFullArgTypes().size();
    if (i == 0) {
      return new Function0Wrapper(ref);
    } else if (i == 1) {
      return new Function1Wrapper(ref);
    } else if (i == 2) {
      return new Function2Wrapper(ref);
    } else if (i == 3) {
      return new Function3Wrapper(ref);
    } else if (i == 4) {
      return new Function4Wrapper(ref);
    } else if (i == 5) {
      return new Function5Wrapper(ref);
    } else if (i == 6) {
      return new Function6Wrapper(ref);
    } else if (i == 7) {
      return new Function7Wrapper(ref);
    } else if (i == 8) {
      return new Function8Wrapper(ref);
    } else if (i == 9) {
      return new Function9Wrapper(ref);
    } else if (i == 10) {
      return new Function10Wrapper(ref);
    } else if (i == 11) {
      return new Function11Wrapper(ref);
    } else if (i == 12) {
      return new Function12Wrapper(ref);
    } else if (i == 13) {
      return new Function13Wrapper(ref);
    } else if (i == 14) {
      return new Function14Wrapper(ref);
    } else if (i == 15) {
      return new Function15Wrapper(ref);
    } else if (i == 16) {
      return new Function16Wrapper(ref);
    } 
    return null;
  }

  private static class Function0Wrapper extends Function0
  {
    private FeatureReference _ref;
    public Function0Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke() {
      return _ref.evaluate(new ArrayList().iterator());
    }
  }

  private static class Function1Wrapper extends Function1
  {
    private FeatureReference _ref;
    public Function1Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1) {
      return _ref.evaluate(Arrays.asList(arg1).iterator());
    }
  }

  private static class Function2Wrapper extends Function2
  {
    private FeatureReference _ref;
    public Function2Wrapper(FeatureReference ref) {
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
    public Function3Wrapper(FeatureReference ref) {
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
    public Function4Wrapper(FeatureReference ref) {
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
    public Function5Wrapper(FeatureReference ref) {
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
    public Function6Wrapper(FeatureReference ref) {
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
    public Function7Wrapper(FeatureReference ref) {
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
    public Function8Wrapper(FeatureReference ref) {
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
    public Function9Wrapper(FeatureReference ref) {
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
    public Function10Wrapper(FeatureReference ref) {
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
    public Function11Wrapper(FeatureReference ref) {
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
    public Function12Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12).iterator());
    }
  }

  private static class Function13Wrapper extends Function13 {
    private FeatureReference _ref;
    public Function13Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13).iterator());
    }
  }

  private static class Function14Wrapper extends Function14 {
    private FeatureReference _ref;
    public Function14Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14).iterator());
    }
  }

  private static class Function15Wrapper extends Function15 {
    private FeatureReference _ref;
    public Function15Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15).iterator());
    }
  }

  private static class Function16Wrapper extends Function16 {
    private FeatureReference _ref;
    public Function16Wrapper(FeatureReference ref) {
      _ref = ref;
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16) {
      return _ref.evaluate(Arrays.asList(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16).iterator());
    }
  }
}
