package gw.lang.spec_old.expressions
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res
uses java.lang.Integer
uses java.math.BigInteger
uses java.math.BigDecimal

class MemberAccessTest extends gw.test.TestClass
{
  function testSimpleMemberAccess()
  {
    var s = new MemberAccessTestClass( "gosu" )
    assertEquals( "gosu", s.X )
  }

  function testSequentialMemberAccess()
  {
    var s = new MemberAccessTestClass( "gosu" )
    assertEquals( 1.0, s.Bounds.X, 0 )
  }

  function testArrayExpansionMemberAccess()
  {
    var s : MemberAccessTestClass[] = { new MemberAccessTestClass( "a" ), new MemberAccessTestClass( "b" ) }
    assertArrayEquals( new String[] {"a", "b"}, s*.X )
  }

  function testNullElementArrayExpansionMemberAccess()
  {
    var s : MemberAccessTestClass[] = { new MemberAccessTestClass( "a" ), null, new MemberAccessTestClass( "b" ) }
    assertArrayEquals( new String[] {"a", null, "b"}, s*.X )
  }

  function testSequentialArrayExpansionMemberAccess()
  {
    var s : MemberAccessTestClass[] = { new MemberAccessTestClass( "a" ), new MemberAccessTestClass( "b" ) }
    assertEquals( {1.0, 1.0}, s*.Bounds*.Height.toList() )
  }

  function testErrant_ArrayMembersNotAvailableAfterExpansionOperator()
  {
    assertFalse( Errant_ArrayMembersNotAvailableAfterExpansionOperator.Type.Valid )
    var errs = (Errant_ArrayMembersNotAvailableAfterExpansionOperator as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errs.size() )
    assertTrue( errs[0].PlainMessage.contains( "No property" ) )
  }

  function testErrant_NakedSinglePackageName()
  {
    assertFalse( Errant_NakedSinglePackageName.Type.Valid )
    var errors = (Errant_NakedSinglePackageName as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME, errors[0].MessageKey )
  }

  function testErrant_PackageTypeMemberAccess()
  {
    assertFalse( Errant_PackageTypeMemberAccess.Type.Valid )
    var errors = (Errant_PackageTypeMemberAccess as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME, errors[0].MessageKey )
  }

  function testVarHasPrecedentOverPackage()
  {
    var java = "i am not a package"
    assertEquals( String, typeof java )
  }

  function testArrayAccessWithErrantIndexDoesNotThrowUnexpectedError()
  {
    assertFalse( Errant_ArrayAccessWithErrantIndex.Type.Valid )
    var errors = (Errant_ArrayAccessWithErrantIndex as IGosuClass).ParseResultsException.ParseExceptions
    assertTrue( errors as String, errors*.MessageKey.contains( Res.MSG_SYNTAX_ERROR ) )
  }

  function testAssociativeArrayPolymorphism()
  {
    var statically = new MemberAccessTestClass( "gosu" )
    assertEquals( 1.0, statically.Bounds.X, 0 )
    var polymorphically : Object = new MemberAccessTestClass( "gosu" )
    assertEquals( 1.0, polymorphically["Bounds"]["X"] as double, 0 )
  }
  
  function testMethodCallExpansionWithArrayReturnTypeIsFlattened()
  {
    var res = new TestForMethodCallExpansion[] {new TestForMethodCallExpansion( 2 ), new TestForMethodCallExpansion( 3 )}
    assertEquals( String[], statictypeof res*.foo() ) 
    assertArrayEquals( new String[] {"a", "a", "a", "a", "a"}, res*.foo() )
  }
  
  function testErrant_ReadAccessToWriteOnlyPropertyFailsWithParseException()
  {
    assertFalse( Errant_ReadAccessToWriteOnlyPropertyFailsWithParseException.Type.Valid )
    var errors = (Errant_ReadAccessToWriteOnlyPropertyFailsWithParseException as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_CLASS_PROPERTY_NOT_READABLE, errors[0].MessageKey )
  }  
  
  function testErrant_ReadAccessToWriteOnlyPropertyFromDpsFailsWithParseException()
  {
    assertFalse( Errant_ReadAccessToWriteOnlyPropertyFromDpsFailsWithParseException.Type.Valid )
    var errors = (Errant_ReadAccessToWriteOnlyPropertyFromDpsFailsWithParseException as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_CLASS_PROPERTY_NOT_READABLE, errors[0].MessageKey )
  }

  class NullSafeTest
  {
    var iValue : int
    var bValue : boolean
    var cValue : char
    var sValue : short
    var lValue : long
    var byValue : byte
    var fValue : float
    var dValue : double
    
    var strValue : String
    var boxiValue : Integer
    var biValue : BigInteger
    var bdValue : BigDecimal
    
    function iValue() : int { return 0 }
    function bValue() : boolean { return 0 as boolean }
    function cValue() : char { return 0 as char }
    function sValue() : short { return 0 }
    function lValue() : long { return 0 }
    function byValue() : byte { return 0 }
    function fValue() : float { return 0 }
    function dValue() : double { return 0 }
    
    function strValue() : String { return 0 as String }
    function boxiValue() : Integer { return 0 }
    function biValue() : BigInteger { return 0 }
    function bdValue() : BigDecimal { return 0 }

  }
  function testNullSafeOnPrimitiveProperties()
  {
    var t : NullSafeTest
    assertEquals( 0, t?.iValue )
    assertEquals( false, t?.bValue )
    assertEquals( 0 as char, t?.cValue )
    assertEquals( 0s, t?.sValue )
    assertEquals( 0L, t?.lValue )
    assertEquals( 0b, t?.byValue )
    assertTrue( 0f == t?.fValue )
    assertTrue( 0D == t?.dValue )
  }
  
  function testNullSafeOnNonPrimitiveProperties()
  {
    var t : NullSafeTest
    assertEquals( null, t?.strValue )    
    assertEquals( null, t?.boxiValue )
    assertEquals( null, t?.biValue )
    assertEquals( null, t?.bdValue )
  }
  
  function testNullSafeOnPrimitiveMethods()
  {
    var t : NullSafeTest
    assertEquals( 0, t?.iValue() )
    assertEquals( false, t?.bValue() )
    assertEquals( 0 as char, t?.cValue() )
    assertEquals( 0s, t?.sValue() )
    assertEquals( 0L, t?.lValue() )
    assertEquals( 0b, t?.byValue() )
    assertTrue( 0f == t?.fValue() )
    assertTrue( 0D == t?.dValue() )
  }
  
  function testNullSafeOnNonPrimitiveMethods()
  {
    var t : NullSafeTest
    assertEquals( null, t?.strValue() )    
    assertEquals( null, t?.boxiValue() )
    assertEquals( null, t?.biValue() )
    assertEquals( null, t?.bdValue() )
  }
}
