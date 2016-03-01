/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.gs.IGosuProgram;
import gw.testharness.KnownBreak;

/**
 */
public class EvalBenchmarkTest extends ByteCodeTestBase {
  public void testNonCachedEvalExpressionIsNotSlow() throws Exception {
    long ret =
            (Long) IGosuProgram.Runner.runProgram(
                    "var start = java.lang.System.nanoTime()\n" +
                            "for( c in 0..|10000 ) {\n" +
                            "  var y = eval( \"c + \" + c )\n" +
                            "}\n" +
                            "var total = java.lang.System.nanoTime() - start\n" +
                            "var nanosPerEval = total / 10000" +
                            "print( nanosPerEval )\n" +
                            "return nanosPerEval");
    // Tested at ~2 millis per eval, so make sure it stays under 4
    assertTrue("Expected a value under 8000000 ns per iteration, but was " + ret, ret < 8000000);
  }

  public void testCachedEvalExpressionIsFast() throws Exception {
    long ret =
            (Long) IGosuProgram.Runner.runProgram(
                    "var start = java.lang.System.nanoTime()\n" +
                            "for( c in 0..|10000 ) {\n" +
                            "  var y = eval( \"c\" )\n" +
                            "}\n" +
                            "var total = java.lang.System.nanoTime() - start\n" +
                            "var nanosPerEval = total / 10000" +
                            "print( nanosPerEval )\n" +
                            "return nanosPerEval");
    // Tested at ~.006 millis per eval, so make sure it stays under .012
    assertTrue("Expected a value under 24000 ns per iteration, but was " + ret, ret < 35000);
  }

  public void testCachedEvalExpressionHandlesEviction() throws Exception {
    long ret =
            (Long) IGosuProgram.Runner.runProgram(
                    "var start = java.lang.System.nanoTime()\n" +
                            "for( c in 0..|100 ) {\n" +
                            "  for( cc in 0..|100 ) {\n" +
                            "    var y = eval( \"cc\" )\n" +
                            "    if( y != cc ) throw 'hello'" +
                            "  }\n" +
                            "}\n" +
                            "var total = java.lang.System.nanoTime() - start\n" +
                            "var nanosPerEval = total / 10000" +
                            "print( nanosPerEval )\n" +
                            "return nanosPerEval");
    // Tested at ~.006 millis per eval, so make sure it stays under .012
    assertTrue("Expected a value under 24000 ns per iteration, but was " + ret, ret < 35000);
  }
}
