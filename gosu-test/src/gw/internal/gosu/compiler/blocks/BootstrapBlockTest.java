/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.blocks;

import gw.lang.function.IBlock;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.IStringLiteralExpression;
import gw.util.Predicate;
import gw.internal.gosu.compiler.ByteCodeTestBase;
import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.compiler.FunctionClassUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.Arrays;

/**
 * This is the bootstrap class for blocks in gosu: all assertions are written
 * in java and, therefore, do not depend on the gosu runtime.  This
 * classes directory also contains higher level test written in pure gosu
 */
public class BootstrapBlockTest extends ByteCodeTestBase
{
  public void testBlockMethodsHaveCorrectSignature() throws ClassNotFoundException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.blocks.FunctionTypes";
    Class<?> javaClass = getClassFromGosuClassLoader( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );

    //No arg block
    Method m = getMethodByName( javaClass, "takesABlockWithNoArgsOrReturnType" );
    Class<?> aClass = m.getParameterTypes()[0];
    assertEquals( FunctionClassUtil.FUNCTION_INTERFACE_PREFIX + 0, aClass.getName() );

    //No arg block with return type
    m = getMethodByName( javaClass, "takesABlockWithNoArgs" );
    aClass = m.getParameterTypes()[0];
    assertEquals( FunctionClassUtil.FUNCTION_INTERFACE_PREFIX + 0, aClass.getName() );

    //One arg block with return type
    m = getMethodByName( javaClass, "takesABlockWithOneArg" );
    aClass = m.getParameterTypes()[0];
    assertEquals( FunctionClassUtil.FUNCTION_INTERFACE_PREFIX + 1, aClass.getName() );

    //Two arg block with return type
    m = getMethodByName( javaClass, "takesABlockWithTwoArgs" );
    aClass = m.getParameterTypes()[0];
    assertEquals( FunctionClassUtil.FUNCTION_INTERFACE_PREFIX + 2, aClass.getName() );
  }

  /*
  // TODO cgross - figure out some other way to test this
  public void testBlockTypesAreReloadable() throws ClassNotFoundException
  {
    Object obj = ReflectUtil.construct( "gw.internal.gosu.compiler.sample.expression.blocks.FunctionTypes" );
    Object iType = ReflectUtil.invokeMethod( obj, "returnsABlock" );

    // cast to a block and store a value from it
    assertTrue( iType instanceof IBlockClass );
    IBlockClass blkType = (IBlockClass)iType;
    ITypeLoader tloader = blkType.getTypeLoader();

    TypeSystem.refresh();

    // force a reload
    assertEquals( tloader, blkType.getTypeLoader() );
  }
*/

  public void testMostBasicBlock()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "mostBasicBlock" ) );
  }

  public void testOneArg()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "blockWithOneArg" ) );
  }

  public void testCapturedSymbol()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "blockWithCapturedSymbol" ) );
  }

  public void testArgsAreProperlyDowncast()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "argIsProperlyDowncast" ) );
  }

  public void testArgsAreProperlyDowncast2()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "argIsProperlyDowncast2" ) );
  }

  public void testNestedBlocksWork1()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "nestedBlocksWork1" ) );
  }

  public void testNestedBlocksWork2()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "nestedBlocksWork2" ) );
  }

  public void testNestedBlocksWork3()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "nestedBlocksWork3" ) );
  }

  public void testMostBasicBlockInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "mostBasicBlockInt" ));
  }

  public void testOneArgInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "blockWithOneArgInt" ) );
  }

  public void testCapturedSymbolInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "blockWithCapturedSymbolInt" ) );
  }

  public void testArgsAreProperlyDowncastInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "argIsProperlyDowncastInt" ) );
  }

  public void testArgsAreProperlyDowncast2Int()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "argIsProperlyDowncast2Int" ) );
  }

  public void testNestedBlocksWork1Int()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "nestedBlocksWork1Int" ) );
  }

  public void testNestedBlocksWork2Int()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "nestedBlocksWork2Int" ) );
  }

  public void testNestedBlocksWork3Int()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "nestedBlocksWork3Int" ) );
  }

  public void testMostBasicBlockDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "mostBasicBlockDouble" ));
  }

  public void testOneArgDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "blockWithOneArgDouble" ) );
  }

  public void testCapturedSymbolDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "blockWithCapturedSymbolDouble" ) );
  }

  public void testArgsAreProperlyDowncastDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "argIsProperlyDowncastDouble" ) );
  }

  public void testArgsAreProperlyDowncast2Double()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "argIsProperlyDowncast2Double" ) );
  }

  public void testNestedBlocksWork1Double()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "nestedBlocksWork1Double" ) );
  }

  public void testNestedBlocksWork2Double()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "nestedBlocksWork2Double" ) );
  }

  public void testNestedBlocksWork3Double()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "nestedBlocksWork3Double" ) );
  }

  public void testMultiTypeBlockArgsWorks()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testBlockWithMultiArgsOfDifferingTypes" ) );
  }
  
  public void testDifferentTypeCapture1()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture1" ) );
  }

  public void testDifferentTypeCapture2()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture2" ) );
  }

  public void testDifferentTypeCapture3()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture3" ) );
  }

  public void testDifferentTypeCapture4()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture4" ) );
  }

  public void testDifferentTypeCapture5()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture5" ) );
  }

  public void testDifferentTypeCapture6()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "The answer is 42", invokeMethod( obj, "testDifferentTypeCapture6" ) );
  }

  public void testMethodThatCallsBlockNoCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsBlockNoCapture" ) );
  }

  public void testMethodThatCallsBlockWithCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsBlockWithCapture" ) );
  }

  public void testMethodThatCallsBlockWithCaptureUsingSideEffects()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsBlockWithCaptureUsingSideEffects" ) );
  }

  public void testMethodThatCallsObjectBlockNoCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsObjBlockNoCapture" ) );
  }

  public void testMethodThatCallsObjectBlockWithCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsObjBlockWithCapture" ) );
  }

  public void testMethodThatCallsObjectBlockWithCaptureUsingSideEffects()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsObjBlockWithCaptureUsingSideEffects" ) );
  }

  public void testMethodThatCallsGenericfunBlockNoCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsGenericfuncBlockNoCapture" ) );
  }

  public void testMethodThatCallsGenericfunBlockWithCapture()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCapture" ) );
  }

  public void testMethodThatCallsObjectGenericfunWithCaptureUsingSideEffects()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffects" ) );
  }

  public void testMethodThatCallsBlockNoCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsBlockNoCaptureInt" ) );
  }

  public void testMethodThatCallsBlockWithCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsBlockWithCaptureInt" ) );
  }

  public void testMethodThatCallsBlockWithCaptureUsingSideEffectsInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsBlockWithCaptureUsingSideEffectsInt" ) );
  }

  public void testMethodThatCallsObjectBlockNoCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsObjBlockNoCaptureInt" ) );
  }

  public void testMethodThatCallsObjectBlockWithCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsObjBlockWithCaptureInt" ) );
  }

  public void testMethodThatCallsObjectBlockWithCaptureUsingSideEffectsInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsObjBlockWithCaptureUsingSideEffectsInt" ) );
  }

  public void testMethodThatCallsGenericfunBlockNoCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockNoCaptureInt" ) );
  }

  public void testMethodThatCallsGenericfunBlockWithCaptureInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCaptureInt" ) );
  }

  public void testMethodThatCallsObjectGenericfunWithCaptureUsingSideEffectsInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffectsInt" ) );
  }

  public void testMethodThatCallsBlockNoCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsBlockNoCaptureDouble" ) );
  }

  public void testMethodThatCallsBlockWithCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsBlockWithCaptureDouble" ) );
  }

  public void testMethodThatCallsBlockWithCaptureUsingSideEffectsDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsBlockWithCaptureUsingSideEffectsDouble" ) );
  }

  public void testMethodThatCallsObjectBlockNoCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsObjBlockNoCaptureDouble" ) );
  }

  public void testMethodThatCallsObjectBlockWithCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsObjBlockWithCaptureDouble" ) );
  }

  public void testMethodThatCallsObjectBlockWithCaptureUsingSideEffectsDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsObjBlockWithCaptureUsingSideEffectsDouble" ) );
  }

  public void testMethodThatCallsGenericfunBlockNoCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockNoCaptureDouble" ) );
  }

  public void testMethodThatCallsGenericfunBlockWithCaptureDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCaptureDouble" ) );
  }

  public void testMethodThatCallsObjectGenericfunWithCaptureUsingSideEffectsDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffectsDouble" ) );
  }

  public void testUpwardBlocksWorkWithString()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callsProducesStringBlock" ) );
  }

  public void testUpwardBlocksWorkWithInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callsProducesIntBlock" ) );
  }

  public void testUpwardBlocksWorkWithDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callsProducesDoubleBlock" ) );
  }

  public void testUpwardCaptureBlocksWorkWithString()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( "yay", invokeMethod( obj, "callsProducesCapturedStringBlock" ) );
  }

  public void testUpwardCaptureBlocksWorkWithInt()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42, invokeMethod( obj, "callsProducesCapturedIntBlock" ) );
  }

  public void testUpwardCaptureBlocksWorkWithDouble()
  {
    Object obj = makeSimpleBlockObj();
    assertEquals( 42.0, invokeMethod( obj, "callsProducesCapturedDoubleBlock" ) );
  }

  public void testStringInvocationWithNoArgsWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesStringBlock" );
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testIntInvocationWithNoArgsWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesIntBlock" );
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testDoubleInvocationWithNoArgsWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesDoubleBlock" );
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testStringInvocationWithCapturedVarWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesCapturedStringBlock", "yay" );
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testStringInvocationWithArgWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesOneArgStringBlock" );
    assertEquals( "yay", blk.invokeWithArgs( "yay" ) );
  }

  public void testIntInvocationWithArgWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesOneArgIntBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testDoubleInvocationWithArgWorksWithIBlock()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesOneArgDoubleBlock" );
    assertEquals( 42.0, blk.invokeWithArgs( 42.0 ) );
  }

  public void testBlockCanProvideItsParsedElement()
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "producesStringBlock" );
    IBlockExpression expression = blk.getParsedElement();
    IParsedElement body = expression.getBody();
    assertTrue( body instanceof IStringLiteralExpression );
    assertEquals( "yay", ((IStringLiteralExpression)body).evaluate() );
  }

  //===============================================================================================

  public void testParamThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testParamThenInnerThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenInnerThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenInnerThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenInnerThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenInnerThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenFieldThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenFieldThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenLocalThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenLocalThenBlock" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testParamThenBlockThenInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "callParamThenBlockTheInner" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( "yay", callable.call() );
  }

  public void testLocalThenBlockThenInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "localThenBlockThenInner" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( "yay", callable.call() );
  }

  public void testBlockThenLocalThenInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "blockThenLocalThenInner" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( "yay", callable.call() );
  }

  public void testParamThenBlockThenInnerThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "callParamThenBlockThenInnerThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenInnerThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testBlockThenLocalThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenLocalThenInnerThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenFieldThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenFieldThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenLocalThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenLocalThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenLocalThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenLocalThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenFieldThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenFieldThenBlock" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testParamThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenBlockInt" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenBlockInt" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenBlockInt" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenBlockInt" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testParamThenInnerThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenInnerThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenInnerThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenInnerThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenInnerThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenFieldThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenFieldThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenLocalThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenLocalThenBlockInt" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testParamThenBlockThenInnerInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "callParamThenBlockTheInnerInt" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42, callable.call() );
  }

  public void testLocalThenBlockThenInnerInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "localThenBlockThenInnerInt" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42, callable.call() );
  }

  public void testBlockThenLocalThenInnerInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "blockThenLocalThenInnerInt" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42, callable.call() );
  }

  public void testParamThenBlockThenInnerThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "callParamThenBlockThenInnerThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenInnerThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testBlockThenLocalThenInnerThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenLocalThenInnerThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenFieldThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenFieldThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenLocalThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenLocalThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenLocalThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenLocalThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenFieldThenBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenFieldThenBlockInt" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42, blk2.invokeWithArgs() );
  }

  public void testParamThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenBlockDouble" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenBlockDouble" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenBlockDouble" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenBlockDouble" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testParamThenInnerThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "callParamThenInnerThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testLocalThenInnerThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenFieldThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenFieldThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenLocalThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenLocalThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenFieldThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenFieldThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testInnerThenInnerThenLocalThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "innerThenInnerThenLocalThenBlockDouble" );
    Callable callable2 = (Callable)callable.call();
    IBlock blk = (IBlock)callable2.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testParamThenBlockThenInnerDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "callParamThenBlockTheInnerDouble" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42.0, callable.call() );
  }

  public void testLocalThenBlockThenInnerDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "localThenBlockThenInnerDouble" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42.0, callable.call() );
  }

  public void testBlockThenLocalThenInnerDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "blockThenLocalThenInnerDouble" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( 42.0, callable.call() );
  }

  public void testParamThenBlockThenInnerThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "callParamThenBlockThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testBlockThenLocalThenInnerThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenLocalThenInnerThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenFieldThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenFieldThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testBlockThenInnerThenLocalThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "blockThenInnerThenLocalThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenLocalThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenLocalThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testLocalThenBlockThenLocalThenInnerThenFieldThenBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk1 = (IBlock)invokeMethod( obj, "localThenBlockThenLocalThenInnerThenFieldThenBlockDouble" );
    Callable callable2 = (Callable)blk1.invokeWithArgs();
    IBlock blk2 = (IBlock)callable2.call();
    assertEquals( 42.0, blk2.invokeWithArgs() );
  }

  public void testLocalThenInnerCreatesBlockInvokedByInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesBlockInvokedByInner" );
    Callable callable2 = (Callable)callable.call();
    assertEquals( "yay", callable2.call() );
  }

  public void testLocalThenInnerCreatesInnerInvokedByBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesInnerInvokedByBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testLocalThenInnerCreatesBlockInvokedByInnerInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesBlockInvokedByInnerInt" );
    Callable callable2 = (Callable)callable.call();
    assertEquals( 42, callable2.call() );
  }

  public void testLocalThenInnerCreatesInnerInvokedByBlockInt() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesInnerInvokedByBlockInt" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42, blk.invokeWithArgs() );
  }

  public void testLocalThenInnerCreatesBlockInvokedByInnerDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesBlockInvokedByInnerDouble" );
    Callable callable2 = (Callable)callable.call();
    assertEquals( 42.0, callable2.call() );
  }

  public void testLocalThenInnerCreatesInnerInvokedByBlockDouble() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "localThenInnerCreatesInnerInvokedByBlockDouble" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( 42.0, blk.invokeWithArgs() );
  }

  public void testOuterCaptureWorksBlockThenInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "outerSymbolTestBlockThenInner" );
    Callable callable = (Callable)blk.invokeWithArgs();
    assertEquals( "yay", callable.call() );
  }

  public void testOuterCaptureWorksInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "outerSymbolTestInnerThenBlock" );
    IBlock blk = (IBlock)callable.call();
    assertEquals( "yay", blk.invokeWithArgs() );
  }

  public void testOuterCaptureWorksBlockThenBlockThenInner() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "outerSymbolTestBlockThenBlockThenInner" );
    IBlock blk2 = (IBlock)blk.invokeWithArgs();
    Callable callable = (Callable)blk2.invokeWithArgs();
    assertEquals( "yay", callable.call() );
  }

  public void testOuterCaptureWorksInnerThenBlockThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    Callable callable = (Callable)invokeMethod( obj, "outerSymbolTestInnerThenBlockThenBlock" );
    IBlock blk = (IBlock)callable.call();
    IBlock blk2 = (IBlock)blk.invokeWithArgs();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testOuterCaptureWorksBlockThenInnerThenBlock() throws Exception
  {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "outerSymbolTestBlockThenInnerThenBlock" );
    Callable callable = (Callable)blk.invokeWithArgs();
    IBlock blk2 = (IBlock)callable.call();
    assertEquals( "yay", blk2.invokeWithArgs() );
  }

  public void testStaticFieldNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testStaticFieldOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFieldOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFieldOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldOneArgBlockWithStringCapture" );
    assertEquals( "barStaticString", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFieldOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFieldOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFieldOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testStaticFunctionNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testStaticFunctionOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFunctionOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFunctionOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionOneArgBlockWithStringCapture" );
    assertEquals( "barStaticString", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFunctionOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticFunctionOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "staticFunctionOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testStaticPropertyNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testStaticPropertyOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticPropertyOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticPropertyOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyOneArgBlockWithStringCapture" );
    assertEquals( "barStaticString", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticPropertyOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testStaticPropertyOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getStaticPropertyOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testFieldNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testFieldOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testFieldOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testFieldOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldOneArgBlockWithStringCapture" );
    assertEquals( "barString", blk.invokeWithArgs( "bar" ) );
  }

  public void testFieldOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testFieldOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FieldOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testFunctionNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testFunctionOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testFunctionOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testFunctionOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionOneArgBlockWithStringCapture" );
    assertEquals( "barString", blk.invokeWithArgs( "bar" ) );
  }

  public void testFunctionOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testFunctionOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "FunctionOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testPropertyNoArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyNoArgBlock" );
    assertEquals( "foo", blk.invokeWithArgs() );
  }

  public void testPropertyOneArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyOneArgBlock" );
    assertEquals( "bar", blk.invokeWithArgs( "bar" ) );
  }

  public void testPropertyOneArgBlockWithIntCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyOneArgBlockWithIntCapture" );
    assertEquals( "bar42", blk.invokeWithArgs( "bar" ) );
  }

  public void testPropertyOneArgBlockWithStringCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyOneArgBlockWithStringCapture" );
    assertEquals( "barString", blk.invokeWithArgs( "bar" ) );
  }

  public void testPropertyOneArgBlockWithBlockCapture() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyOneArgBlockWithBlockCapture" );
    assertEquals( "barbar", blk.invokeWithArgs( "bar" ) );
  }

  public void testPropertyOnePrimitiveArgBlock() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getPropertyOnePrimitiveArgBlock" );
    assertEquals( 42, blk.invokeWithArgs( 42 ) );
  }

  public void testVoidBlockWorks1() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock1" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks2() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock2" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks3() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock3" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks4() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock4" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks5() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock5" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks6() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock6" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testVoidBlockWorks7() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "getVoidBlock7" );
    Object actual = blk.invokeWithArgs();
    assertNull( actual );
  }

  public void testPrimitiveReturnBlockWorks1() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "testPrimitiveReturnBlockWorks1" );
    assertEquals( 10, blk.invokeWithArgs() );
  }

  public void testPrimitiveReturnBlockWorks2() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "testPrimitiveReturnBlockWorks2" );
    assertEquals( 10.0, blk.invokeWithArgs() );
  }

  public void testPrimitiveReturnBlockWorks3() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "testPrimitiveReturnBlockWorks3" );
    assertEquals( true, blk.invokeWithArgs() );
  }

  public void testPrimitiveReturnBlockWorks4() {
    Object obj = makeSimpleBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "testPrimitiveReturnBlockWorks4" );
    assertEquals( new Long(10), blk.invokeWithArgs() );
  }

  public void testBlockToCallableCoercionWorks1() throws Exception {
    Object obj = makeSimpleBlockObj();
    Callable blk = (Callable)invokeMethod( obj, "testBlockToCallableCoercionWorks1" );
    assertEquals( "block as callable", blk.call() );
  }

  public void testBlockToCallableCoercionWorks2() throws Exception {
    Object obj = makeSimpleBlockObj();
    Object obj2 = invokeMethod( obj, "testBlockToCallableCoercionWorks2" );
    assertEquals( String.class, obj2 );
  }

  public void testBlockToPredicateCoercionWorks() throws Exception {
    Object obj = makeSimpleBlockObj();
    Predicate<String> predicate = (Predicate<String>)invokeMethod( obj, "testBlockToPredicateCoercionWorks" );
    assertEquals( true, predicate.evaluate( "test" ) );
    assertEquals( false, predicate.evaluate( "tset" ) );
  }

  public void testToStringOnBlocksDoesntSuck() {
    Object obj = makeSimpleBlockObj();
    assertEquals( "\\  -> 10", invokeMethod( obj, "testToString1" ).toString() );
    assertEquals( "\\  -> print( true )", invokeMethod( obj, "testToString2" ).toString() );
    assertEquals( "\\  -> {\n" +
                  "  var x = 10\n" +
                  "}\n",
                  invokeMethod( obj, "testToString3" ).toString() );
    assertEquals( "\\  -> for( x in new java.util.ArrayList<java.lang.Object>(){})\n" +
                  "print( x )", invokeMethod( obj, "testToString4" ).toString() );
  }

  public void testIdentityGenericBlock() {
    Object obj = makeGenericsBlockObj();
    IBlock blk = (IBlock)invokeMethod( obj, "callsSimpleGenericIdentity" );
    assertEquals( "test", blk.invokeWithArgs() );
  }

  public void testMapWorks() {
    Object obj = makeGenericsBlockObj();
    assertEquals( Arrays.asList( 1, 2, 3 ), invokeMethod( obj, "callsMap" ) );
  }

  public void testIntLiteralInBodyCompiles() {
    Object obj = makeSimpleBlockObj();
    assertEquals( 10, invokeMethod( obj, "invokeIntBlock" ) );
  }

  public void testCoercionBetweenBlocksWithCoercableReturnTypesWorks() {
    Object obj = makeSimpleBlockObj();
    assertEquals( "10", invokeMethod( obj, "invokesIntBlockInStringBlockContext" ) );
  }

  private Object makeSimpleBlockObj()
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.expression.blocks.SimpleBlocks" );
  }

  private Object makeGenericsBlockObj()
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.expression.blocks.GenericsBlocks" );
  }

  private Method getMethodByName( Class<?> javaClass, String s )
  {
    for( Method method : javaClass.getMethods() )
    {
      if( method.getName().equals( s ) )
      {
        return method;
      }
    }
    throw new RuntimeException( );
  }
}