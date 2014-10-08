/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ISymbol;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.IExpression;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;
import gw.util.GosuTestUtil;
import junit.framework.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

/**
 */
public class JavaGenericsTest extends TestClass
{
  public void testCanParseGenericType() throws ParseResultsException
  {
    IExpression expr = parseExpr( "java.util.HashMap<String, java.lang.StringBuilder>" );
    assertTrue( expr instanceof TypeLiteral);
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType type = typeLiteral.getType().getType();
    assertTrue( type.isParameterizedType() );
    IType[] paramTypes = type.getTypeParameters();
    assertEquals( 2, paramTypes.length );
    Assert.assertEquals( GosuParserTypes.STRING_TYPE(), paramTypes[0] );
    Assert.assertEquals( JavaTypes.STRING_BUILDER(), paramTypes[1] );
  }

  private IExpression parseExpr(String s) throws ParseResultsException {
    return GosuTestUtil.compileExpression(s);
  }
  

  public static List<? extends String> getWildCardList(){
    return Collections.emptyList();
  }

  public void testWildCardGenericIterationWorksAfterRefresh() throws ParseResultsException
  {
    IProgram p = (IProgram) parseExpr("var wildCardList = gw.internal.gosu.parser.generics.JavaGenericsTest.getWildCardList()\n" +
                                          "return wildCardList");
    IProgramInstance gosuProgram = p.getGosuProgram().getProgramInstance();
    TypeSystem.refresh(false);
    Object o = gosuProgram.evaluate(new ExternalSymbolMapForMap( new HashMap<String, ISymbol>() ));
    assertCollectionEquals(Arrays.asList(), (List) o);
  }

  public void testParameterizationOfMethodInfo() throws ParseResultsException
  {
    IExpression expr = parseExpr( "java.util.HashMap<java.lang.String, java.lang.StringBuilder>" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType type = typeLiteral.getType().getType();

    IMethodInfo mi = type.getTypeInfo().getMethod( "get", JavaTypes.OBJECT() );
    assertTrue( mi.getReturnType() == JavaTypes.STRING_BUILDER() );

    mi = type.getTypeInfo().getMethod( "put", JavaTypes.STRING(), JavaTypes.STRING_BUILDER() );
    assertTrue( mi.getReturnType() == JavaTypes.STRING_BUILDER() );
    assertTrue( mi.getParameters()[0].getFeatureType() == JavaTypes.STRING() );
    assertTrue( mi.getParameters()[1].getFeatureType() == JavaTypes.STRING_BUILDER() );
  }

  public void testParameterizationOfPropertyInfo() throws ParseResultsException
  {
    IExpression expr = parseExpr( "gw.internal.gosu.parser.generics.TestGenericClass<java.lang.Integer>" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType type = typeLiteral.getType().getType();

    IPropertyInfo pi = type.getTypeInfo().getProperty( "Member" );
    assertTrue( pi.getFeatureType() == JavaTypes.INTEGER() );

    pi = type.getTypeInfo().getProperty( "CharSequenceList" );
    assertTrue( pi.getFeatureType().getTypeParameters()[0].getName().equals(
      JavaTypes.CHAR_SEQUENCE().getName() ) );
  }

  public void testParameterizationOfConstructorInfo() throws ParseResultsException
  {
    IExpression expr = parseExpr( "gw.internal.gosu.parser.generics.TestGenericClass<java.lang.Integer>" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType type = typeLiteral.getType().getType();

    IConstructorInfo ci = type.getTypeInfo().getConstructor( JavaTypes.INTEGER() );
    assertTrue( ci.getType() == type );
    assertTrue( ci.getParameters()[0].getFeatureType() == JavaTypes.INTEGER() );

    ci = type.getTypeInfo().getConstructor( JavaTypes.INTEGER(), JavaTypes.STRING() );
    assertTrue( ci.getType() == type );
    assertTrue( ci.getParameters()[0].getFeatureType() == JavaTypes.INTEGER() );
    assertTrue( ci.getParameters()[1].getFeatureType() == JavaTypes.STRING() );
  }

  public void testNonParameterizedGenericTypeInfoAssumesObjectTypeParams() throws ParseResultsException
  {
    IExpression expr = parseExpr( "java.util.ArrayList" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType type = typeLiteral.getType().getType();

    IMethodInfo mi = type.getTypeInfo().getMethod( "add", JavaTypes.OBJECT() );
    assertTrue( mi.getParameters()[0].getFeatureType() == JavaTypes.OBJECT() );

    mi = type.getTypeInfo().getMethod( "add", JavaTypes.pINT(), JavaTypes.OBJECT() );
    assertTrue( mi.getParameters()[0].getFeatureType() == JavaTypes.pINT() );
    assertTrue( mi.getParameters()[1].getFeatureType() == JavaTypes.OBJECT() );

    mi = type.getTypeInfo().getMethod( "get", JavaTypes.pINT() );
    assertTrue( mi.getParameters()[0].getFeatureType() == JavaTypes.pINT() );
    assertTrue( mi.getReturnType() == JavaTypes.OBJECT() );
  }

  public void testNonParameterizedClassNotEqualsParameterizedClass() throws ParseResultsException
  {
    IExpression expr = parseExpr( "java.util.ArrayList" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType typeNonParam = typeLiteral.getType().getType();

    expr = parseExpr( "java.util.ArrayList<java.lang.CharSequence>" );
    typeLiteral = (TypeLiteral)expr;
    IType typeString = typeLiteral.getType().getType();

    expr = parseExpr( "java.util.ArrayList<java.lang.CharSequence>" );
    typeLiteral = (TypeLiteral)expr;
    IType typeWild = typeLiteral.getType().getType();

    expr = parseExpr( "java.util.ArrayList<Object>" );
    typeLiteral = (TypeLiteral)expr;
    IType typeWildest = typeLiteral.getType().getType();

    assertTrue( typeNonParam != typeString );
    assertTrue( typeNonParam != typeWild );
    assertTrue( typeString == typeWild );
    assertTrue( typeNonParam == typeWildest );
    assertTrue( typeWild != typeWildest );

    expr = parseExpr( "java.util.List" );
    typeLiteral = (TypeLiteral)expr;
    typeNonParam = typeLiteral.getType().getType();

    expr = parseExpr( "java.util.List<java.lang.CharSequence>" );
    typeLiteral = (TypeLiteral)expr;
    typeString = typeLiteral.getType().getType();

    expr = parseExpr( "java.util.List<java.lang.CharSequence>" );
    typeLiteral = (TypeLiteral)expr;
    typeWild = typeLiteral.getType().getType();

    assertTrue( typeNonParam != typeString );
    assertTrue( typeNonParam != typeWild );
    assertTrue( typeString == typeWild );
  }

  public void testGenericMethodExplicitParameterization() throws ParseResultsException
  {
    Object type = GosuTestUtil.eval("uses java.lang.StringBuilder;\n" +
            "uses java.util.ArrayList;\n" +
            "\n" +
            "var l = new ArrayList<StringBuilder>();\n" +
            "l.add( new StringBuilder( \"hello\" ) );\n" +
            "l.add( new StringBuilder( \"bye\" ) );\n" +
            "var al = l.toArray( new StringBuilder[l.size()] );\n" +
            "for( e in al )\n" +
            "{\n" +
            "  // Call a StringBuilder declared method" +
            "  e.append( \"u\" );\n" +
            "  return typeof e;\n" +
            "}");
    assertTrue( type == TypeSystem.get( StringBuilder.class ) );
  }

  public void testGenericMethodImplicitParameterizationViaArgumentTypeInference() throws ParseResultsException
  {
    Object type = GosuTestUtil.eval("uses java.lang.StringBuilder;\n" +
            "uses java.util.ArrayList;\n" +
            "\n" +
            "var l = new ArrayList<StringBuilder>();\n" +
            "l.add( new StringBuilder( \"hello\" ) );\n" +
            "l.add( new StringBuilder( \"bye\" ) );\n" +
            "var al = l.toArray( new StringBuilder[l.size()] );\n" +
            "for( e in al )\n" +
            "{\n" +
            "  // Call a StringBuilder declared method" +
            "  e.append( \"u\" );\n" +
            "  return typeof e;\n" +
            "}");
    assertTrue( type == TypeSystem.get( StringBuilder.class ) );
  }

  public void testComplexGenericMethodImplicitParameterizationViaArgumentTypeInference() throws ParseResultsException
  {
    Object result = GosuTestUtil.eval("uses java.lang.StringBuilder;\n" +
            "uses java.util.ArrayList;\n" +
            "\n" +
            "var l = new ArrayList<String>();\n" +
            "l.add( \"hello\" );\n" +
            "l.add( \"bye\" );\n" +
            "var al = java.util.Collections.max( l );\n" +
            "return (typeof al) == String && " +
            "  al.indexOf( \"hello\" ) == 0;\n");
    assertTrue( result instanceof Boolean && ((Boolean)result).booleanValue() );
  }

  public void testInnerGenericTypeParsingWorks() throws ParseResultsException
  {
    parseExpr( "var map = new java.util.HashMap();\n" +
                  "return map.entrySet();" );
  }

  public void testGenericMethodHasProperUpperBoundingType() throws ParseResultsException
  {
    IExpression expr = parseExpr( "gw.internal.gosu.parser.generics.TestGenericClass" );
    TypeLiteral typeLiteral = (TypeLiteral)expr;
    IType typeNonParam = typeLiteral.getType().getType().getGenericType();
    IMethodInfo mi = typeNonParam.getTypeInfo().getMethod( "genericMethod", JavaTypes.CHAR_SEQUENCE() );
    assertNotNull( mi );
    IGenericTypeVariable[] tvs = ((IGenericMethodInfo)mi).getTypeVariables();
    assertEquals( 1, tvs.length );
    IType boundingType = tvs[0].getBoundingType();
    assertTrue( boundingType == JavaTypes.CHAR_SEQUENCE() );
    assertTrue( mi.getReturnType() == JavaTypes.CHAR_SEQUENCE() );
  }

  public void testCannotParameterizeGenericMethodWithIncompatibleType()
  {
    try
    {
      parseExpr(
        "var test = new gw.internal.gosu.parser.generics.TestGenericClass<int>( 3 );" +
        "test.genericMethod<Object>( \"hello\" );" );
    }
    catch( ParseResultsException e )
    {
      // expected
      return;
    }
    fail();
  }

  public void testAssignability() throws ParseResultsException
  {
    parseExpr(
      "var test = new gw.internal.gosu.parser.generics.MultiTypeVarsOnArrayList<java.lang.Integer, java.lang.StringBuilder>();" +
      "var l : java.util.ArrayList<java.lang.StringBuilder> = test;" );
  }

  public void testNotAssignability() throws ParseResultsException
  {
    try
    {
      parseExpr(
        "var test = new gw.internal.gosu.parser.generics.MultiTypeVarsOnArrayList<java.lang.Integer, java.lang.StringBuilder>();" +
        "var l : java.util.ArrayList<java.lang.StringBuffer> = test;" );
    }
    catch( ParseResultsException e )
    {
      // expected
      return;
    }
    fail();
  }
}
