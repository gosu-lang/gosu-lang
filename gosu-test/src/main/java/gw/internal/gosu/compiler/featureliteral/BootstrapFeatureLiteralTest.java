/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.featureliteral;

import gw.lang.function.IBlock;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.features.BoundMethodReference;
import gw.lang.reflect.features.BoundPropertyReference;
import gw.lang.reflect.features.MethodReference;
import gw.lang.reflect.features.PropertyReference;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class BootstrapFeatureLiteralTest extends TestClass
{
  public void testBasicFeatureLiteralsParse() throws ParseResultsException
  {
    assertFalse( GosuTestUtil.compileExpression( "String#length" ).hasParseIssues() );
    assertFalse( GosuTestUtil.compileExpression( "String#length()" ).hasParseIssues() );
    assertFalse( GosuTestUtil.compileExpression( "String#substring(int)" ).hasParseIssues() );
    assertFalse( GosuTestUtil.compileExpression( "'test'#length" ).hasParseIssues() );
    assertFalse( GosuTestUtil.compileExpression( "'test'#length()" ).hasParseIssues() );
    assertFalse( GosuTestUtil.compileExpression( "'test'#substring(int)" ).hasParseIssues() );
  }

  public void testBasicFeatureLiteralsEvaluate() throws ParseResultsException
  {
    PropertyReference pr = (PropertyReference)eval( "String#length" );
    assertEquals( 4, pr.get( "asdf" ) );
    BoundPropertyReference bpr = (BoundPropertyReference)eval( "'asdf'#length" );
    assertEquals( 4, bpr.get() );

    MethodReference mr = (MethodReference)eval( "String#length()" );
    assertEquals( 4, mr.getMethodInfo().getCallHandler().handleCall("asdf", null));
    BoundMethodReference bmr = (BoundMethodReference)eval( "'asdf'#length()" );
    assertEquals( 4, ((IBlock) bmr.toBlock()).invokeWithArgs() );

    mr = (MethodReference)eval( "String#substring(int)" );
    assertEquals( "df", mr.getMethodInfo().getCallHandler().handleCall("asdf", 2));
    bmr = (BoundMethodReference)eval( "'asdf'#substring(int)" );
    assertEquals( "df", ((IBlock) bmr.toBlock()).invokeWithArgs(2) );
  }

  public void testEqualityFarLessExtensivelyThanIShould() {

    // method refs
    assertTrue( eval( "String#substring(int) as Object == String#substring(int)" ) );
    assertFalse( eval( "String#substring(1)  as Object == String#substring(int)" ) );
    assertFalse( eval( "\"foo\"#substring(int) as Object == String#substring(int)" ) );
    assertFalse( eval( "\"foo\"#substring(1) as Object == String#substring(int)" ) );
    assertTrue( eval( "String#substring(1) as Object == String#substring(1)" ) );
    assertFalse( eval( "\"foo\"#substring(int) as Object == String#substring(1)" ) );
    assertFalse( eval( "\"foo\"#substring(1) as Object == String#substring(1)" ) );
    assertTrue( eval( "\"foo\"#substring(int) as Object == \"foo\"#substring(int)" ) );
    assertFalse( eval( "\"foo\"#substring(int) as Object == String#substring(1)" ) );
    assertTrue( eval( "\"foo\"#substring(1) as Object == \"foo\"#substring(1)" ) );
    assertFalse( eval( "\"bar\"#substring(1) as Object == \"foo\"#substring(1)" ) );
    
    // property refs
    assertTrue( eval( "String#Bytes as Object == String#Bytes" ) );
    assertFalse( eval( "\"foo\"#Bytes as Object == String#Bytes" ) );
    assertTrue( eval( "\"foo\"#Bytes as Object == \"foo\"#Bytes" ) );
    assertFalse( eval( "\"bar\"#Bytes as Object == \"foo\"#Bytes" ) );
  }

  public void testConstructorReferencesInProgram() {
    Object object = eval( "java.lang.String#construct()" );
    assertNotNull( object );
  }

  private void assertTrue( Object o )
  {
    assertTrue( ((Boolean)o).booleanValue() );
  }

  private void assertFalse( Object o )
  {
    assertFalse( ((Boolean)o).booleanValue() );
  }

  private Object eval( String foo )
  {
    return GosuTestUtil.eval( foo );
  }
}
