/*
 * Copyright 2014 Guidewire Software, Inc.
 */

/* Guidewire Software
 *
 * Creator information:
 * User: <YOUR-USERID>
 * Date: Sat Oct 06 11:22:54 PDT 2007
 *
 * Revision information:
 */

package gw.internal.gosu.parser;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Arrays;

/**
 * Tests the {@link gw.lang.reflect.IType} implementation.
 *
 * @author <YOUR-NAME>
 */
public class TypeLordTest extends TestClass
{
  //=======================================================
  // Plain old classes
  //=======================================================
  public static class A{}
  public static class B extends A{}
  public static class C extends A{}
  public static class C2 extends C{}

  //=======================================================
  // Plain old interfaces
  //=======================================================
  public static interface IA{}
  public static interface IB{}
  public static interface IC{}
  public static class D implements IA{}
  public static class E implements IA{}
  public static class F implements IA, IB{}
  public static class G implements IA, IB{}
  public static class H implements IA, IB, IC{}

  //=======================================================
  // Generic classes and friends
  //=======================================================
  class GenA<T>{}
  class GenB<T> extends GenA<T>{}
  class GenC<T> extends GenA<T>{}
  class ParamD extends GenC<String>{}
  class ParamE extends GenB<Integer>{}
  class GenRec extends GenA<GenRec>{}

  //=======================================================
  // Generic interfaces and friends
  //=======================================================
  interface IGenA1<T>{}
  interface IGenA2<T>{}
  interface IGenB<T> extends IGenA1<T> {}
  interface IGenC<T> extends IGenA1<T> {}
  interface IGenE<T> extends IGenB<String>, IGenC<String>{}
  interface IGenE2<T> extends IGenB<T>, IGenC<T>{}
  interface IGenE3<T> extends IGenA1<T>{}
  interface IParamF extends IGenA1<String>, IGenA2<String>{}
  interface IParamG extends IGenA1<Object>, IGenA2<String>{}
  interface IParamH extends IGenA1<String>, IGenA2<Object>{}
  interface IParamI extends IGenB<Object>, IGenA2<Object>{}

  public void testGetLUBReturnsClassAsItsOwnLUB() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( A.class ) ) ) );
    assertEquals( TypeSystem.get( B.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( B.class ) ) ) );
    assertEquals( TypeSystem.get( C.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C.class ) ) ) );
    assertEquals( TypeSystem.get( C2.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C2.class ) ) ) );
  }

  public void testGetLUBReturnsSuperAsItsLUB() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( A.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy1() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( A.class ),
                                                                 TypeSystem.get( C.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy2() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( A.class ),
                                                                 TypeSystem.get( C.class ),
                                                                 TypeSystem.get( C2.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy3() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy4() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C.class ),
                                                                 TypeSystem.get( C2.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy5() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( A.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C2.class ),
                                                                 TypeSystem.get( B.class ) ) ) );
  }

  public void testLUBHierarchy6() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( C.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( C.class ),
                                                                 TypeSystem.get( C2.class ) ) ) );
  }

  public void testLUBIFaceHierarchy1() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( IA.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( IA.class ) ) ) );
  }

  public void testLUBIFaceHierarchy2() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( IA.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( IA.class ),
                                                                 TypeSystem.get( D.class ) ) ) );
  }

  public void testLUBIFaceHierarchy3() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( IA.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( IA.class ),
                                                                 TypeSystem.get( D.class ),
                                                                 TypeSystem.get( E.class ) ) ) );
  }

  public void testLUBIFaceHierarchy4() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( IA.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( D.class ),
                                                                 TypeSystem.get( E.class ) ) ) );
  }

  public void testLUBIFaceHierarchy5() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( IA.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( D.class ),
                                                                 TypeSystem.get( F.class ) ) ) );
  }

  public void testAmbiguousLUBTwoRootInterfacesReturnsCompoundTypeAsLUB() throws ParseResultsException
  {
    assertEquals( CompoundType.get( TypeSystem.get( IA.class ), TypeSystem.get( IB.class ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( F.class ),
                                                                 TypeSystem.get( G.class ) ) ) );
  }

  public void testAmbiguousLUBTwoRootInterfacesReturnsCompoundTypeAsLUB2() throws ParseResultsException
  {
    assertEquals( CompoundType.get( TypeSystem.get( IA.class ), TypeSystem.get( IB.class ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( F.class ),
                                                                 TypeSystem.get( H.class ) ) ) );
  }

  public void testNullTypeIsRemovedFromLUBSet() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( F.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( TypeSystem.get( F.class ),
                                                                 GosuParserTypes.NULL_TYPE() ) ) );
  }

  public void testNullTypeIsRemovedFromLUBSet2() throws ParseResultsException
  {
    assertEquals( TypeSystem.get( F.class ),
                  TypeLord.findLeastUpperBound( Arrays.asList( GosuParserTypes.NULL_TYPE(), TypeSystem.get( F.class ) ) ) );
  }

  public void testNullTypesReturnObjectAsLUB() throws ParseResultsException
  {
    assertEquals( GosuParserTypes.NULL_TYPE(),
                  TypeLord.findLeastUpperBound( Arrays.asList( GosuParserTypes.NULL_TYPE(),
                                                                 GosuParserTypes.NULL_TYPE() ) ) );
  }

  public void testSameGenericTypeRetunsSelfAsLUB()
  {
    IType type = TypeSystem.get( GenA.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type, type, type ) );
    assertEquals( type, leastUpperBound );
  }

  public void testSameGenericTypeRetunsSelfAsLUBWhenSelfParameterized()
  {
    IType a = TypeSystem.get( GenA.class ).getParameterizedType( TypeSystem.get( GenA.class ) );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( a, a, a ) );
    assertEquals( a, leastUpperBound );
  }

  public void testGenericSubTypesReturnLUBParameterization()
  {
    IType bParamedOnString = TypeSystem.get( GenB.class ).getParameterizedType( JavaTypes.STRING() );
    IType cParamedOnString = TypeSystem.get( GenC.class ).getParameterizedType( JavaTypes.STRING() );
    IType aParamedOnString = TypeSystem.get( GenA.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( bParamedOnString, cParamedOnString ) );
    assertEquals( aParamedOnString, leastUpperBound );
  }

  public void testGenericSubTypesReturnLUBParameterizationWithParameterTypeLUBAsWell()
  {
    IType bParamedOnCharSequence = TypeSystem.get( GenB.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType cParamedOnString = TypeSystem.get( GenC.class ).getParameterizedType( JavaTypes.STRING() );
    IType aParamedOnCharSequence = TypeSystem.get( GenA.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( bParamedOnCharSequence, cParamedOnString ) );
    assertEquals( aParamedOnCharSequence, leastUpperBound );
  }

  public void testClassExtendingParameterizedSubTypesReturnCorrectLUBWhenComparedWithItsSuper()
  {
    IType d = TypeSystem.get( ParamD.class );
    IType cParamedOnString = TypeSystem.get( GenC.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( d, cParamedOnString ) );
    assertEquals( cParamedOnString, leastUpperBound );
  }

  public void testClassesExtendingParamterizedSubtypesReturnCorrectLUB()
  {
    IType d = TypeSystem.get( ParamD.class );
    IType e = TypeSystem.get( ParamE.class );
    IType serializableAndComparable = CompoundType.get( TypeSystem.get( Serializable.class ),
                                                  TypeSystem.get( Comparable.class ).getParameterizedType( JavaTypes.OBJECT() ) );
    IType aParameterizedOnSerializableAndComparable = TypeSystem.get( GenA.class ).getParameterizedType( serializableAndComparable );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( d, e ) );
    assertEquals( aParameterizedOnSerializableAndComparable, leastUpperBound );
  }

  public void testRecursiveTypeReturnsSelfAsLUB() {
    IType type = TypeSystem.get( GenRec.class );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type, type, type ) );
    assertEquals( type, leastUpperBound );
  }

  public void testRecursiveTypeAndNonRecursiveTypeReturnProperLUB() {
    IType recType = TypeSystem.get( GenRec.class );
    IType bParamedOnString = TypeSystem.get( GenB.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( recType, bParamedOnString ) );
    assertEquals( TypeSystem.get( GenA.class ).getParameterizedType( JavaTypes.OBJECT() ), leastUpperBound );
  }

  public void testBothOuterTypeAndParameterizedTypeResolveToUpperBounds()
  {
    IType listOfStrings = JavaTypes.LIST().getParameterizedType( JavaTypes.STRING() );
    IType arrayListOfCharSequences = JavaTypes.ARRAY_LIST().getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType listOfCharSequences = JavaTypes.LIST().getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    assertEquals( listOfCharSequences, TypeLord.findLeastUpperBound( Arrays.asList( listOfStrings, arrayListOfCharSequences ) ) );
  }

  public void testGenericInterfaceReturnsItselfAsALUB() {
    IType type = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type, type, type ) );
    assertEquals( type, leastUpperBound );
  }

  public void testTwoGenericInterfacesWithNoCommonInterfaceReturnObject() {
    IType type1 = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( JavaTypes.OBJECT(), leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB() {
    IType type1 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenC.class ).getParameterizedType( JavaTypes.STRING() );
    IType type3 = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB2() {
    IType type1 = TypeSystem.get( IGenE.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenC.class ).getParameterizedType( JavaTypes.STRING() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type2, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB3() {
    IType type1 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType type2 = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.STRING() );
    IType type3 = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB4() {
    IType type1 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenC.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType type3 = TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB5() {
    IType type1 = TypeSystem.get( IGenE.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenC.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType type3 = TypeSystem.get( IGenC.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB6() {
    IType type1 = TypeSystem.get( IGenE.class ).getParameterizedType( JavaTypes.STRING() );
    IType type2 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType type3 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testTwoDistinctExtensionsOfAGenericInterfaceReturnTheCorrectLUB7() {
    IType type1 = TypeSystem.get( IGenE2.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType type2 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.STRING() );
    IType type3 = TypeSystem.get( IGenB.class ).getParameterizedType( JavaTypes.CHAR_SEQUENCE() );
    IType leastUpperBound = TypeLord.findLeastUpperBound( Arrays.asList( type1, type2 ) );
    assertEquals( type3, leastUpperBound );
  }

  public void testDisjointGenericInterfacesProduceProperLUBs1()
  {
    IType paramf = TypeSystem.get( IParamF.class );
    IType paramg = TypeSystem.get( IParamG.class );
    assertEquals( CompoundType.get( TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.OBJECT() ),
                                    TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.STRING() ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( paramf, paramg ) ) );
  }

  public void testDisjointGenericInterfacesProduceProperLUBs2()
  {
    IType paramf = TypeSystem.get( IParamF.class );
    IType paramh = TypeSystem.get( IParamH.class );
    assertEquals( CompoundType.get( TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.STRING() ),
                                    TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.OBJECT() ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( paramf, paramh ) ) );
  }

  public void testDisjointGenericInterfacesProduceProperLUBs3()
  {
    IType paramf = TypeSystem.get( IParamF.class );
    IType paramg = TypeSystem.get( IParamG.class );
    IType paramh = TypeSystem.get( IParamH.class );
    assertEquals( CompoundType.get( TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.OBJECT() ),
                                    TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.OBJECT() ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( paramf, paramg, paramh ) ) );
  }

  public void testDisjointGenericInterfacesProduceProperLUBs4()
  {
    IType paramf = TypeSystem.get( IParamF.class );
    IType parami = TypeSystem.get( IParamI.class );
    assertEquals( CompoundType.get( TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.OBJECT() ),
                                    TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.OBJECT() ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( paramf, parami ) ) );
  }

  public void testDisjointGenericInterfacesProduceProperLUBs5()
  {
    IType paramg = TypeSystem.get( IParamG.class );
    IType parami = TypeSystem.get( IParamI.class );
    assertEquals( CompoundType.get( TypeSystem.get( IGenA1.class ).getParameterizedType( JavaTypes.OBJECT() ),
                                    TypeSystem.get( IGenA2.class ).getParameterizedType( JavaTypes.OBJECT() ) ),
                  TypeLord.findLeastUpperBound( Arrays.asList( paramg, parami ) ) );
  }

  public void testTypeParameterizedOnPackageTypeDoesNotCauseNPE() {
    try {
      GosuTestUtil.compileExpression("new java.util.List<java>");
    } catch (ParseResultsException e) {
      // pass
    }
  }

  public void testPureGenericTypeAndNonGenericTypeReturnAsIfDefaultParameterizedTypeWereUsed() {
    IType pureList = JavaTypes.LIST();
    IType listOfString = pureList.getParameterizedType(JavaTypes.STRING());
    assertEquals(JavaTypes.LIST().getParameterizedType(JavaTypes.OBJECT()), TypeLord.findLeastUpperBound(Arrays.asList(pureList, listOfString)));
  }

  public List<? extends String> returnsListOfString()
  {
    return null;
  }

  public List<? extends CharSequence> returnsListOfCharSequence()
  {
    return null;
  }

  public void testWildcardTypesWorkCorrectlyWithLUBCalculation() {
    IType type = TypeSystem.get( this.getClass() );
    IType listOfWildcardOfString = type.getTypeInfo().getMethod( "returnsListOfString" ).getReturnType();
    IType listOfWildcardOfCharSequence = type.getTypeInfo().getMethod( "returnsListOfCharSequence" ).getReturnType();
    IType actual = TypeLord.findLeastUpperBound( Arrays.asList( listOfWildcardOfString, listOfWildcardOfCharSequence ) );
    assertEquals(JavaTypes.LIST().getParameterizedType(JavaTypes.CHAR_SEQUENCE()), actual );
  }

  private void setCacheSize(int value) {
    System.setProperty("assignabilityCacheSize", String.valueOf(value));
  }

  private int getAssignabilityCacheSizeFromTypeLord() {
    int size = 0;
    TypeLord typeLord = new TypeLord();
    try {
      Method method = typeLord.getClass().getDeclaredMethod("getAssignabilityCacheSize");
      method.setAccessible(true);
      size = (int) method.invoke(typeLord);

    } catch (Exception e) {
      fail("Exception: " + e.getMessage());
    }

    return size;
  }

  public void testAssignabilityCacheSizeOveride() {

    String originalPropertyValue = System.getProperty("assignabilityCacheSize");
    try {
      final int DEFAULT_CACHE_SIZE = 1000;
      final int CACHE_SIZE_LESS_THAN_DEFAULT = 500;
      final int CACHE_SIZE_GREATER_THAN_DEFAULT = 2000;

      setCacheSize(CACHE_SIZE_LESS_THAN_DEFAULT);
      assertEquals(DEFAULT_CACHE_SIZE, getAssignabilityCacheSizeFromTypeLord());

      setCacheSize(CACHE_SIZE_GREATER_THAN_DEFAULT);
      assertEquals(CACHE_SIZE_GREATER_THAN_DEFAULT, getAssignabilityCacheSizeFromTypeLord());

    } finally {
      if (originalPropertyValue == null)
        System.clearProperty("assignabilityCacheSize");
      else
        System.setProperty("assignabilityCacheSize", originalPropertyValue);
    }
  }


}