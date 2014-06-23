/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.java.JavaTypes;

public class TypeInfoReflectionTest extends ByteCodeTestBase
{
  private static final String SIMPLE_REFLECTION_CLASS = "gw.internal.gosu.compiler.sample.reflection.SampleReflectionClass";
  private static final String SIMPLE_SUB_REFLECTION_CLASS = "gw.internal.gosu.compiler.sample.reflection.SubSampleReflectionClass";
  private static final String GENERIC_REFLECTION_CLASS = "gw.internal.gosu.compiler.sample.reflection.GenericReflectionClass";

  public void testBasicReflectiveConstruction()
  {
    makeSimpleReflectionClass();
  }

  public void testReflectiveConstructionWithArgs()
  {
    assertEquals( "foo", ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, "foo" ), "StringProp" ) );
    assertEquals( true, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, true ), "BoolProp" ) );
    assertEquals( (byte) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (byte) 1 ), "ByteProp" ) );
    assertEquals( (char) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (char) 1 ), "CharProp" ) );
    assertEquals( (short) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (short) 1 ), "ShortProp" ) );
    assertEquals( (int) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (int) 1 ), "IntProp" ) );
    assertEquals( (long) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (long) 1 ), "LongProp" ) );
    assertEquals( (float) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (float) 1 ), "FloatProp" ) );
    assertEquals( (double) 1, ReflectUtil.getProperty( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, (double) 1 ), "DoubleProp" ) );
  }

  public void testConstructorAccessiblity() {
    assertNotNull( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, 1, 1 ) ); // private
    assertNotNull( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, 1, 1, 1 ) ); // internal
    assertNotNull( ReflectUtil.construct( SIMPLE_REFLECTION_CLASS, 1, 1, 1, 1 ) ); // protected
  }

  public void testSimpleFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "plainFunction" ));
  }

  public void testMethodAccessiblity() {
    Object o = makeSimpleReflectionClass();
    assertEquals( "private function", ReflectUtil.invokeMethod( o, "privateFunction" ));
    assertEquals( "internal function", ReflectUtil.invokeMethod( o, "internalFunction" ));
    assertEquals( "protected function", ReflectUtil.invokeMethod( o, "protectedFunction" ));
  }

  public void testFunctionWithStringArgInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "functionWStringArg", "test" ));
  }

  public void testFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "functionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "functionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "functionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "functionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "functionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "functionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "functionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "functionWDoubleArg", (double) 1 ));
  }

  public void testSetters()
  {
    Object o = makeSimpleReflectionClass();
    ReflectUtil.invokeMethod( o, "setWStringArg", "foo" );
    assertEquals( "foo", ReflectUtil.getProperty(o, "StringProp" ));
    ReflectUtil.invokeMethod( o, "setWBooleanArg", true );
    assertEquals( true, ReflectUtil.getProperty(o, "BoolProp" ));
    ReflectUtil.invokeMethod( o, "setWByteArg", (byte) 1 );
    assertEquals( (byte) 1, ReflectUtil.getProperty(o, "ByteProp" ));
    ReflectUtil.invokeMethod( o, "setWCharArg", (char) 1 );
    assertEquals( (char) 1, ReflectUtil.getProperty(o, "CharProp" ));
    ReflectUtil.invokeMethod( o, "setWShortArg", (short) 1 );
    assertEquals( (short) 1, ReflectUtil.getProperty(o, "ShortProp" ));
    ReflectUtil.invokeMethod( o, "setWIntArg", (int) 1 );
    assertEquals( (int) 1, ReflectUtil.getProperty(o, "IntProp" ));
    ReflectUtil.invokeMethod( o, "setWLongArg", (long) 1 );
    assertEquals( (long) 1, ReflectUtil.getProperty(o, "LongProp" ));
    ReflectUtil.invokeMethod( o, "setWFloatArg", (float) 1 );
    assertEquals( (float) 1, ReflectUtil.getProperty(o, "FloatProp" ));
    ReflectUtil.invokeMethod( o, "setWDoubleArg", (double) 1 );
    assertEquals( (double) 1, ReflectUtil.getProperty(o, "DoubleProp" ));
  }

  public void testStaticSimpleFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "staticPlainFunction" ));
  }

  public void testStaticFunctionWithStringArgInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "staticFunctionWStringArg", "test" ));
  }

  public void testStaticFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "staticFunctionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "staticFunctionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "staticFunctionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "staticFunctionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "staticFunctionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "staticFunctionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "staticFunctionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "staticFunctionWDoubleArg", (double) 1 ));
  }

  public void testGenericFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "genericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "genericFunctionReturnsT", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "genericFunctionReturnsBoundedT", "foo" ));
  }

  public void testStaticGenericFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "staticGenericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "staticGenericFunctionReturnsT", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "staticGenericFunctionReturnsBoundedT", "foo" ));
  }

  public void testSimplePropertyGetInvocation() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "StringProp" ));
  }

  public void testPropertyAccessibility() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Private Property", ReflectUtil.getProperty( o, "PrivateProperty" ));
    assertEquals( "Internal Property", ReflectUtil.getProperty( o, "InternalProperty" ));
    assertEquals( "Protected Property", ReflectUtil.getProperty( o, "ProtectedProperty" ));
  }

  public void testPropertySetterAccessibility() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    ReflectUtil.setProperty( o, "PrivateProperty", "foo" );
    assertEquals( "foo", ReflectUtil.getProperty( o, "PrivateProperty" ));
    ReflectUtil.setProperty( o, "InternalProperty", "bar" );
    assertEquals( "bar", ReflectUtil.getProperty( o, "InternalProperty" ));
    ReflectUtil.setProperty( o, "ProtectedProperty", "doh" );
    assertEquals( "doh", ReflectUtil.getProperty( o, "ProtectedProperty" ));
  }

  public void testPropertyAccessibilityOnSub() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Private Property", ReflectUtil.getProperty( o, "PrivateProperty" ));
    assertEquals( "Internal Property", ReflectUtil.getProperty( o, "InternalProperty" ));
    assertEquals( "Protected Property", ReflectUtil.getProperty( o, "ProtectedProperty" ));
  }

  public void testSimplePropertySetInvocation() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "StringProp" ));
    ReflectUtil.setProperty( o, "StringProp", "New Value" );
    assertEquals( "New Value", ReflectUtil.getProperty( o, "StringProp" ));
  }

  public void testEnhancementSimpleFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "e_plainFunction" ));
  }

  public void testEnhancementFunctionWithStringArgInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "e_functionWStringArg", "test" ));
  }

  public void testEnhancementFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "e_functionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "e_functionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "e_functionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "e_functionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "e_functionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "e_functionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "e_functionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "e_functionWDoubleArg", (double) 1 ));
  }

  public void testEnhancementSetters()
  {
    Object o = makeSimpleReflectionClass();
    ReflectUtil.invokeMethod( o, "e_setWStringArg", "foo" );
    assertEquals( "foo", ReflectUtil.getProperty(o, "e_StringProp" ));
    ReflectUtil.invokeMethod( o, "e_setWBooleanArg", true );
    assertEquals( true, ReflectUtil.getProperty(o, "e_BoolProp" ));
    ReflectUtil.invokeMethod( o, "e_setWByteArg", (byte) 1 );
    assertEquals( (byte) 1, ReflectUtil.getProperty(o, "e_ByteProp" ));
    ReflectUtil.invokeMethod( o, "e_setWCharArg", (char) 1 );
    assertEquals( (char) 1, ReflectUtil.getProperty(o, "e_CharProp" ));
    ReflectUtil.invokeMethod( o, "e_setWShortArg", (short) 1 );
    assertEquals( (short) 1, ReflectUtil.getProperty(o, "e_ShortProp" ));
    ReflectUtil.invokeMethod( o, "e_setWIntArg", (int) 1 );
    assertEquals( (int) 1, ReflectUtil.getProperty(o, "e_IntProp" ));
    ReflectUtil.invokeMethod( o, "e_setWLongArg", (long) 1 );
    assertEquals( (long) 1, ReflectUtil.getProperty(o, "e_LongProp" ));
    ReflectUtil.invokeMethod( o, "e_setWFloatArg", (float) 1 );
    assertEquals( (float) 1, ReflectUtil.getProperty(o, "e_FloatProp" ));
    ReflectUtil.invokeMethod( o, "e_setWDoubleArg", (double) 1 );
    assertEquals( (double) 1, ReflectUtil.getProperty(o, "e_DoubleProp" ));
  }

  public void testEnhancementStaticSimpleFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "e_staticPlainFunction" ));
  }

  public void testEnhancementStaticFunctionWithStringArgInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "e_staticFunctionWStringArg", "test" ));
  }

  public void testEnhancementStaticFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "e_staticFunctionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWDoubleArg", (double) 1 ));
  }

  public void testEnhancementGenericFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "e_genericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "e_genericFunctionReturnsT", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "e_genericFunctionReturnsBoundedT", "foo" ));
  }

  public void testEnhancementStaticGenericFunctionInvocation()
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "e_staticGenericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "e_staticGenericFunctionReturnsT", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "e_staticGenericFunctionReturnsBoundedT", "foo" ));
  }

  public void testEnhancementSimplePropertyGetInvocation() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "e_StringProp" ));
  }

  public void testEnhancementSimplePropertySetInvocation() throws ClassNotFoundException
  {
    Object o = makeSimpleReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "e_StringProp" ));
    ReflectUtil.setProperty( o, "e_StringProp", "New Value" );
    assertEquals( "New Value", ReflectUtil.getProperty( o, "e_StringProp" ));
  }

  public void testGenericReflectiveConstruction()
  {
    makeGenericReflectionClass();
  }

  public void testGenericMethodInvocationWorks()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.STRING(), ReflectUtil.invokeMethod( o, "getT" ) );
  }

  public void testGenericEnhancementMethodInvocationWorks()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.STRING(), ReflectUtil.invokeMethod( o, "e_getT" ) );
  }

  public void testGenericEnhancementMethodInvocationWorksWhenUnboundNoUpperBound()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "e_getQ" ) );
  }

  public void testGenericEnhancementMethodInvocationWorksWhenUnboundWUpperBound()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.STRING(), ReflectUtil.invokeMethod( o, "e_getR" ) );
  }

  public void testGenericEnhancementPropertyInvocationWorks()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.STRING(), ReflectUtil.getProperty( o, "e_PropT" ) );
  }

  public void testGenericEnhancementPropertyInvocationWorksWhenUnboundNoUpperBound()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.getProperty( o, "e_PropQ" ) );
  }

  public void testGenericEnhancementPropertyInvocationWorksWhenUnboundWUpperBound()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( JavaTypes.STRING(), ReflectUtil.getProperty( o, "e_PropR" ) );
  }

  public void testGenericReflectiveConstructionWithArgs()
  {
    assertEquals( "foo", ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, "foo" ), "StringProp" ) );
    assertEquals( true, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, true ), "BoolProp" ) );
    assertEquals( (byte) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (byte) 1 ), "ByteProp" ) );
    assertEquals( (char) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (char) 1 ), "CharProp" ) );
    assertEquals( (short) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (short) 1 ), "ShortProp" ) );
    assertEquals( (int) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (int) 1 ), "IntProp" ) );
    assertEquals( (long) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (long) 1 ), "LongProp" ) );
    assertEquals( (float) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (float) 1 ), "FloatProp" ) );
    assertEquals( (double) 1, ReflectUtil.getProperty( ReflectUtil.construct( GENERIC_REFLECTION_CLASS, (double) 1 ), "DoubleProp" ) );
  }

  public void testGenericSimpleFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "plainFunction" ));
  }

  public void testGenericFunctionWithStringArgInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "functionWStringArg", "test" ));
  }

  public void testGenericFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "functionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "functionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "functionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "functionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "functionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "functionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "functionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "functionWDoubleArg", (double) 1 ));
  }

  public void testGenericSetters()
  {
    Object o = makeGenericReflectionClass();
    ReflectUtil.invokeMethod( o, "setWStringArg", "foo" );
    assertEquals( "foo", ReflectUtil.getProperty(o, "StringProp" ));
    ReflectUtil.invokeMethod( o, "setWBooleanArg", true );
    assertEquals( true, ReflectUtil.getProperty(o, "BoolProp" ));
    ReflectUtil.invokeMethod( o, "setWByteArg", (byte) 1 );
    assertEquals( (byte) 1, ReflectUtil.getProperty(o, "ByteProp" ));
    ReflectUtil.invokeMethod( o, "setWCharArg", (char) 1 );
    assertEquals( (char) 1, ReflectUtil.getProperty(o, "CharProp" ));
    ReflectUtil.invokeMethod( o, "setWShortArg", (short) 1 );
    assertEquals( (short) 1, ReflectUtil.getProperty(o, "ShortProp" ));
    ReflectUtil.invokeMethod( o, "setWIntArg", (int) 1 );
    assertEquals( (int) 1, ReflectUtil.getProperty(o, "IntProp" ));
    ReflectUtil.invokeMethod( o, "setWLongArg", (long) 1 );
    assertEquals( (long) 1, ReflectUtil.getProperty(o, "LongProp" ));
    ReflectUtil.invokeMethod( o, "setWFloatArg", (float) 1 );
    assertEquals( (float) 1, ReflectUtil.getProperty(o, "FloatProp" ));
    ReflectUtil.invokeMethod( o, "setWDoubleArg", (double) 1 );
    assertEquals( (double) 1, ReflectUtil.getProperty(o, "DoubleProp" ));
  }

  public void testGenericStaticSimpleFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "staticPlainFunction" ));
  }

  public void testGenericStaticFunctionWithStringArgInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "staticFunctionWStringArg", "test" ));
  }

  public void testGenericStaticFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "staticFunctionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "staticFunctionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "staticFunctionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "staticFunctionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "staticFunctionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "staticFunctionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "staticFunctionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "staticFunctionWDoubleArg", (double) 1 ));
  }

  public void testGenericGenericFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "genericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "genericFunctionReturnsQ", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "genericFunctionReturnsBoundedQ", "foo" ));
  }

  public void testGenericStaticGenericFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "staticGenericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "staticGenericFunctionReturnsQ", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "staticGenericFunctionReturnsBoundedQ", "foo" ));
  }

  public void testGenericSimplePropertyGetInvocation() throws ClassNotFoundException
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "StringProp" ));
  }

  public void testGenericSimplePropertySetInvocation() throws ClassNotFoundException
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "StringProp" ));
    ReflectUtil.setProperty( o, "StringProp", "New Value" );
    assertEquals( "New Value", ReflectUtil.getProperty( o, "StringProp" ));
  }

  public void testGenericEnhancementSimpleFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "e_plainFunction" ));
  }

  public void testGenericEnhancementFunctionWithStringArgInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "e_functionWStringArg", "test" ));
  }

  public void testGenericEnhancementFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "e_functionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "e_functionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "e_functionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "e_functionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "e_functionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "e_functionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "e_functionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "e_functionWDoubleArg", (double) 1 ));
  }

  public void testGenericEnhancementSetters()
  {
    Object o = makeGenericReflectionClass();
    ReflectUtil.invokeMethod( o, "e_setWStringArg", "foo" );
    assertEquals( "foo", ReflectUtil.getProperty(o, "e_StringProp" ));
    ReflectUtil.invokeMethod( o, "e_setWBooleanArg", true );
    assertEquals( true, ReflectUtil.getProperty(o, "e_BoolProp" ));
    ReflectUtil.invokeMethod( o, "e_setWByteArg", (byte) 1 );
    assertEquals( (byte) 1, ReflectUtil.getProperty(o, "e_ByteProp" ));
    ReflectUtil.invokeMethod( o, "e_setWCharArg", (char) 1 );
    assertEquals( (char) 1, ReflectUtil.getProperty(o, "e_CharProp" ));
    ReflectUtil.invokeMethod( o, "e_setWShortArg", (short) 1 );
    assertEquals( (short) 1, ReflectUtil.getProperty(o, "e_ShortProp" ));
    ReflectUtil.invokeMethod( o, "e_setWIntArg", (int) 1 );
    assertEquals( (int) 1, ReflectUtil.getProperty(o, "e_IntProp" ));
    ReflectUtil.invokeMethod( o, "e_setWLongArg", (long) 1 );
    assertEquals( (long) 1, ReflectUtil.getProperty(o, "e_LongProp" ));
    ReflectUtil.invokeMethod( o, "e_setWFloatArg", (float) 1 );
    assertEquals( (float) 1, ReflectUtil.getProperty(o, "e_FloatProp" ));
    ReflectUtil.invokeMethod( o, "e_setWDoubleArg", (double) 1 );
    assertEquals( (double) 1, ReflectUtil.getProperty(o, "e_DoubleProp" ));
  }

  public void testGenericEnhancementStaticSimpleFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "plain function", ReflectUtil.invokeMethod( o, "e_staticPlainFunction" ));
  }

  public void testGenericEnhancementStaticFunctionWithStringArgInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "test", ReflectUtil.invokeMethod( o, "e_staticFunctionWStringArg", "test" ));
  }

  public void testGenericEnhancementStaticFunctionWithPrimitiveArgInvocations()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( true, ReflectUtil.invokeMethod( o, "e_staticFunctionWBooleanArg", true ));
    assertEquals( (byte) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWByteArg", (byte) 1 ));
    assertEquals( (char) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWCharArg", (char) 1 ));
    assertEquals( (short) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWShortArg", (short) 1 ));
    assertEquals( (int) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWIntArg", (int) 1 ));
    assertEquals( (long) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWLongArg", (long) 1 ));
    assertEquals( (float) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWFloatArg", (float) 1 ));
    assertEquals( (double) 1, ReflectUtil.invokeMethod( o, "e_staticFunctionWDoubleArg", (double) 1 ));
  }

  public void testGenericEnhancementGenericFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "e_genericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "e_genericFunctionReturnsS", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "e_genericFunctionReturnsBoundedS", "foo" ));
  }

  public void testGenericEnhancementStaticGenericFunctionInvocation()
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "foo", ReflectUtil.invokeMethod( o, "e_staticGenericFunction", "foo" ));
    assertEquals( JavaTypes.OBJECT(), ReflectUtil.invokeMethod( o, "e_staticGenericFunctionReturnsS", "foo" ));
    assertEquals( JavaTypes.CHAR_SEQUENCE(), ReflectUtil.invokeMethod( o, "e_staticGenericFunctionReturnsBoundedS", "foo" ));
  }

  public void testGenericEnhancementSimplePropertyGetInvocation() throws ClassNotFoundException
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "e_StringProp" ));
  }

  public void testGenericEnhancementSimplePropertySetInvocation() throws ClassNotFoundException
  {
    Object o = makeGenericReflectionClass();
    assertEquals( "Default Value", ReflectUtil.getProperty( o, "e_StringProp" ));
    ReflectUtil.setProperty( o, "e_StringProp", "New Value" );
    assertEquals( "New Value", ReflectUtil.getProperty( o, "e_StringProp" ));
  }

  public void testArrayCreationAndManipulation()
  {
    IType clazz = TypeSystem.getByFullName( SIMPLE_REFLECTION_CLASS );
    Object o = clazz.makeArrayInstance( 3 );
    assertTrue( o instanceof Object[] );
    assertEquals( 3, clazz.getArrayLength( o ) );
    for( int i = 0; i < 3; i++ )
    {
      assertNull( clazz.getArrayComponent( o, i ) );
    }
    for( int i = 0; i < 3; i++ )
    {
      clazz.setArrayComponent( o, i, makeSimpleReflectionClass() );
    }
    for( int i = 0; i < 3; i++ )
    {
      assertNotNull( clazz.getArrayComponent( o, i ) );
    }
  }

  public void testArrayOfArraysCreationAndManipulation()
  {
    IType clazz = TypeSystem.getByFullName( SIMPLE_REFLECTION_CLASS );
    IType arrayType = clazz.getArrayType();
    Object o = arrayType.makeArrayInstance( 3 );
    assertTrue( o instanceof Object[][] );
    assertEquals( 3, arrayType.getArrayLength( o ) );
    for( int i = 0; i < 3; i++ )
    {
      assertTrue( arrayType.getArrayComponent( o, i ) instanceof Object[] );
    }
  }

  public void testArrayOfArrayOfArraysCreationAndManipulation()
  {
    IType clazz = TypeSystem.getByFullName( SIMPLE_REFLECTION_CLASS );
    IType arrayType = clazz.getArrayType();
    IType arrayArrayType = arrayType.getArrayType();
    Object o = arrayArrayType.makeArrayInstance( 3 );
    assertTrue( o instanceof Object[][][] );
    assertEquals( 3, arrayType.getArrayLength( o ) );
    for( int i = 0; i < 3; i++ )
    {
      assertTrue( arrayType.getArrayComponent( o, i ) instanceof Object[][] );
    }
  }

  private Object makeSimpleReflectionClass()
  {
    Object o = ReflectUtil.construct( SIMPLE_REFLECTION_CLASS );
    assertNotNull( o );
    Class<?> aClass = null;
    try
    {
      aClass = GosuClassLoader.instance().findClass( SIMPLE_REFLECTION_CLASS );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
    assertTrue( aClass.isInstance( o ) );
    return o;
  }

  private Object makeGenericReflectionClass()
  {
    IType genericClass = TypeSystem.getByFullName( GENERIC_REFLECTION_CLASS );
    IType parameterizedType = genericClass.getParameterizedType( JavaTypes.STRING(), JavaTypes.OBJECT(), JavaTypes.STRING() );
    IConstructorHandler iConstructorHandler = parameterizedType.getTypeInfo().getConstructor().getConstructor();
    Object o = iConstructorHandler.newInstance();
    Class<?> aClass = null;
    try
    {
      aClass = GosuClassLoader.instance().findClass( GENERIC_REFLECTION_CLASS );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
    assertTrue( aClass.isInstance( o ) );
    return o;
  }

}
