package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RestrictionsTest extends TestClass
{
  private static final String SYSTEM_BLOCKED =
    "Type 'java.lang.System' is blocked by the restrictions baseline (rule: java.lang.System).";

  public void testChainedCallIsChecked()
  {
    assertErrors( "print(new Throwable().getStackTrace().toString())",
                  "Member 'java.lang.Throwable#getStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#getStackTrace)." );
  }

  public void testTopLevelVarWithLambdaInitializerIsChecked()
  {
    assertErrors( "var r: Runnable = \\-> java.lang.System.exit(0)", SYSTEM_BLOCKED );
    assertErrors( "var r: java.util.function.Supplier<Long> = \\-> java.lang.System.currentTimeMillis()", SYSTEM_BLOCKED );
  }

  public void testSameErrorReportedOncePerPosition()
  {
    assertErrors( "java.lang.System.exit(0)", SYSTEM_BLOCKED );
  }

  public void testFunctionAndClassBodiesAreChecked()
  {
    assertErrors( "function g() { java.lang.System.exit(0) }", SYSTEM_BLOCKED );
    assertErrors( "class Foo { function f() { java.lang.System.exit(0) } }", SYSTEM_BLOCKED );
    assertErrors( "var r = \\-> { java.lang.System.exit(0) }", SYSTEM_BLOCKED );
  }

  public void testProgramTypesAreAllowed()
  {
    assertErrors( "class Foo { var _x: int\n function f() { _x = 1; print(_x) } }\n new Foo().f()" );
    assertErrors( "var o = new Object() { function x(): int { return 1 } }" );
  }

  public void testAnonymousClassBodyIsChecked()
  {
    assertErrors( "var o = new Object() { function x() { java.lang.System.exit(0) } }", SYSTEM_BLOCKED );
  }

  public void testGenericsAndBlockTypes()
  {
    assertErrors( "function f<T>(t: T): T { return t }\n f(\"a\")" );
    assertErrors( "var r: block(): long = \\-> 1" );
    assertErrors( "function f<T extends java.lang.Thread>(t: T) {}",
                  "Type 'java.lang.Thread' is blocked by the restrictions baseline (rule: java.lang.Thread)." );
  }

  public void testCompoundInferredTypeIsAllowedIfAnyComponentIs()
  {
    assertErrors( "var x = true ? 1 : \"s\"" );
  }

  public void testPropertyReadIsDeniedByGetterRule()
  {
    assertErrors( "print(new Throwable().StackTrace)",
                  "Member 'java.lang.Throwable#getStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#getStackTrace)." );
    assertErrors( "print(new Object().Class)",
                  "Member 'java.lang.Object#getClass' is blocked by the restrictions baseline (rule: java.lang.Object#getClass)." );
  }

  public void testPropertyWriteIsDeniedBySetterRule()
  {
    assertErrors( "java.util.Locale.Default = java.util.Locale.US",
                  "Member 'java.util.Locale#setDefault' is blocked by the restrictions baseline (rule: java.util.Locale#setDefault)." );
    assertErrors( "var loc = java.util.Locale.Default" );
  }

  public void testCompoundAssignmentReadsAndWritesProperty()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Date", "getTime" )
      .build();
    String error = "Member 'java.util.Date#getTime' is blocked by the restrictions profile (rule: java.util.Date#getTime).";
    assertErrors( restrictions, "var d = new Date()\nd.Time += 1", error );
    assertErrors( restrictions, "var d = new Date()\nd.Time = 1" );
  }

  public void testPropertyIsDeniedByPropertyRule()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Locale", "Default" )
      .build();
    String error = "Member 'java.util.Locale#Default' is blocked by the restrictions profile (rule: java.util.Locale#Default).";
    assertErrors( restrictions, "var loc = java.util.Locale.Default", error );
    assertErrors( restrictions, "java.util.Locale.Default = java.util.Locale.US", error );
  }

  public void testBaselineDeniesFileSystemPropertiesAndGlobalState()
  {
    assertErrors( "new java.util.Formatter(\"x.txt\")",
                  "Type 'java.util.Formatter' is blocked by the restrictions baseline (rule: java.util.Formatter)." );
    assertErrors( "print(Integer.getInteger(\"x\"))",
                  "Member 'java.lang.Integer#getInteger' is blocked by the restrictions baseline (rule: java.lang.Integer#getInteger)." );
    assertErrors( "print(Long.getLong(\"x\"))",
                  "Member 'java.lang.Long#getLong' is blocked by the restrictions baseline (rule: java.lang.Long#getLong)." );
    assertErrors( "print(Boolean.getBoolean(\"x\"))",
                  "Member 'java.lang.Boolean#getBoolean' is blocked by the restrictions baseline (rule: java.lang.Boolean#getBoolean)." );
    assertErrors( "java.time.zone.ZoneRulesProvider.refresh()",
                  "Type 'java.time.zone.ZoneRulesProvider' is blocked by the restrictions baseline (rule: java.time.zone.ZoneRulesProvider)." );
  }

  public void testEnhancementMembersAreCheckedAgainstEnhancedType()
  {
    assertErrors( "var l = {1,2}.map(\\e -> e * 2)\nvar c = {1,2}.Count\nvar s = \"abc\".capitalize()" );

    Restrictions restrictions = Restrictions.baseline()
      .denyType( "java.lang.Iterable" )
      .denyType( "java.util.ArrayList" )
      .build();
    assertErrors( restrictions, "print({1,2}.Count)",
                  "Type 'java.lang.Iterable' is blocked by the restrictions profile (rule: java.lang.Iterable)." );
    // Count is declared on Iterable (CoreIterableEnhancement), so denying only the receiver's type has no effect
    assertErrors( Restrictions.baseline().denyType( "java.util.ArrayList" ).build(), "print({1,2}.Count)" );
  }

  public void testEnhancementMemberDenyRule()
  {
    assertErrors( "print(new Throwable().StackTraceAsString)",
                  "Member 'java.lang.Throwable#StackTraceAsString' is blocked by the restrictions baseline (rule: java.lang.Throwable#StackTraceAsString)." );

    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.lang.Iterable", "map" )
      .build();
    assertErrors( restrictions, "var l = {1,2}.map(\\e -> e * 2)",
                  "Member 'java.lang.Iterable#map' is blocked by the restrictions profile (rule: java.lang.Iterable#map)." );
  }

  public void testInheritedMemberIsDeniedByReceiverTypeRule()
  {
    // setLength is declared in java.lang.AbstractStringBuilder
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.lang.StringBuilder", "setLength" )
      .build();
    String error = "Member 'java.lang.StringBuilder#setLength' is blocked by the restrictions profile (rule: java.lang.StringBuilder#setLength).";
    assertErrors( restrictions, "var sb = new StringBuilder()\nsb.setLength(0)", error );
    assertErrors( restrictions, "var sb = new StringBuilder()\nsb.Length = 0", error );
  }

  public void testInheritedEnhancementMemberIsDeniedByReceiverTypeRule()
  {
    // map is declared in CoreIterableEnhancement on java.lang.Iterable
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.ArrayList", "map" )
      .build();
    assertErrors( restrictions, "var l = {1,2}.map(\\e -> e * 2)",
                  "Member 'java.util.ArrayList#map' is blocked by the restrictions profile (rule: java.util.ArrayList#map)." );
  }

  public void testOverrideIsDeniedBySupertypeRule()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.lang.Object", "toString" )
      .build();
    String error = "Member 'java.lang.Object#toString' is blocked by the restrictions profile (rule: java.lang.Object#toString).";
    assertErrors( restrictions, "print(new Object().toString())", error );
    // String overrides toString
    assertErrors( restrictions, "print(\"abc\".toString())", error );
    assertErrors( restrictions, "class Foo { override function toString(): String { return \"foo\" } }\n print(new Foo().toString())", error );
  }

  public void testInterfaceMemberIsDeniedOnImplementations()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Collection", "add" )
      .denyMember( "java.lang.CharSequence", "length" )
      .build();
    String addError = "Member 'java.util.Collection#add' is blocked by the restrictions profile (rule: java.util.Collection#add).";
    assertErrors( restrictions, "var l: java.util.Collection<String> = {}\nl.add(\"x\")", addError );
    // List redeclares add
    assertErrors( restrictions, "var l: java.util.List<String> = {}\nl.add(\"x\")", addError );
    assertErrors( restrictions, "var l = new java.util.ArrayList<String>()\nl.add(\"x\")", addError );
    assertErrors( restrictions, "print(\"a\".length())",
                  "Member 'java.lang.CharSequence#length' is blocked by the restrictions profile (rule: java.lang.CharSequence#length)." );
  }

  public void testStaticMemberIsNotDeniedBySupertypeRule()
  {
    // each enum's valueOf(String) is unrelated to Enum.valueOf(Class, String)
    assertErrors( "print(java.time.DayOfWeek.valueOf(\"MONDAY\"))" );
    assertErrors( "enum Color { RED }\n print(Color.valueOf(\"RED\"))" );
    String error = "Member 'java.lang.Enum#valueOf' is blocked by the restrictions baseline (rule: java.lang.Enum#valueOf).";
    assertErrors( "print(Enum.valueOf(java.time.DayOfWeek, \"MONDAY\"))", error );
    // the inherited static is still matched by its declaring type
    assertErrors( "print(java.time.DayOfWeek.valueOf(java.time.DayOfWeek, \"MONDAY\"))", error );
    // an inherited static field is matched by its declaring type
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Calendar", "YEAR" )
      .build();
    assertErrors( restrictions, "print(java.util.GregorianCalendar.YEAR)",
                  "Member 'java.util.Calendar#YEAR' is blocked by the restrictions profile (rule: java.util.Calendar#YEAR)." );
    // instance member of an enum is matched on its supertype
    assertErrors( "print(java.time.DayOfWeek.MONDAY.DeclaringClass)",
                  "Member 'java.lang.Enum#getDeclaringClass' is blocked by the restrictions baseline (rule: java.lang.Enum#getDeclaringClass)." );
  }

  public void testUnqualifiedMethodCallIsChecked()
  {
    // a call with no qualifier is a MethodCallExpression, not a member access
    assertErrors( "class X extends Throwable { function f(): Object { return getStackTrace() } }",
                  "Member 'java.lang.Throwable#getStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#getStackTrace)." );
    assertErrors( "class X { function f(): Object { return getClass() } }",
                  "Member 'java.lang.Object#getClass' is blocked by the restrictions baseline (rule: java.lang.Object#getClass)." );
    assertErrors( "class X { function f() { wait() } }",
                  "Member 'java.lang.Object#wait' is blocked by the restrictions baseline (rule: java.lang.Object#wait)." );
  }

  public void testStaticImportIsChecked()
  {
    // a statically imported member is called with no qualifier
    String error = "Member 'java.lang.Integer#getInteger' is blocked by the restrictions baseline (rule: java.lang.Integer#getInteger).";
    assertErrors( "uses java.lang.Integer#getInteger\nprint(getInteger(\"x\"))", error );
    assertErrors( "uses java.lang.Integer#*\nprint(getInteger(\"x\"))", error );
    assertErrors( "uses java.lang.Long#*\nprint(getLong(\"x\"))",
                  "Member 'java.lang.Long#getLong' is blocked by the restrictions baseline (rule: java.lang.Long#getLong)." );
  }

  public void testStaticImportOfAllowedMember()
  {
    assertErrors( "uses java.lang.Math#max\nprint(max(1, 2))" );
    assertErrors( "uses java.lang.Math#PI\nprint(PI)" );
    assertErrors( "uses java.lang.Math#*\nprint(max(1, 2) + PI)" );
  }

  public void testUnqualifiedPropertyReadIsChecked()
  {
    // a property read with no qualifier is an identifier, not a member access
    assertErrors( "class X extends Throwable { function f(): Object { return StackTrace } }",
                  "Member 'java.lang.Throwable#getStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#getStackTrace)." );
    assertErrors( "class X extends Throwable { function f() { StackTrace = {} } }",
                  "Member 'java.lang.Throwable#setStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#setStackTrace)." );
  }

  public void testUnqualifiedPropertyCompoundAssignmentReadsAndWrites()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Date", "getTime" )
      .build();
    assertErrors( restrictions, "class D extends java.util.Date { function f() { Time += 1 } }",
                  "Member 'java.util.Date#getTime' is blocked by the restrictions profile (rule: java.util.Date#getTime)." );
    assertErrors( restrictions, "class D extends java.util.Date { function f() { Time++ } }",
                  "Member 'java.util.Date#getTime' is blocked by the restrictions profile (rule: java.util.Date#getTime)." );
    assertErrors( restrictions, "class D extends java.util.Date { function f() { Time = 1 } }" );
  }

  public void testStructuralTypesAreGated()
  {
    String error = "Language feature 'structural types' is not permitted by the restrictions profile.";
    assertErrors( "structure S { function charAt(i: int): char }\nvar s: S = \"abc\"", error );
    // reported at the type reference and at the call, whose declaring type is S
    assertErrors( "structure S { function charAt(i: int): char }\nprint((\"abc\" as S).charAt(0))", error, error );
    assertErrors( Restrictions.baseline().allowFeature( gw.lang.parser.restrictions.LanguageFeature.STRUCTURAL ).build(),
                  "structure S { function charAt(i: int): char }\nvar s: S = \"abc\"\nprint(s.charAt(0))" );
  }

  public void testObjectInitializerAssignmentIsChecked()
  {
    // `:P = value` in an object initializer is an InitializerAssignment, not a member access
    assertErrors( "print(new Throwable() { :StackTrace = {} })",
                  "Member 'java.lang.Throwable#setStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#setStackTrace)." );
  }

  public void testStaticTypeOfIsGatedByTypeOf()
  {
    assertErrors( "print(statictypeof \"x\")",
                  "Language feature 'typeof expressions' is not permitted by the restrictions profile." );
  }

  private static void assertErrors( String script, String... expected )
  {
    assertErrors( Restrictions.baseline().build(), script, expected );
  }

  private static void assertErrors( Restrictions restrictions, String script, String... expected )
  {
    assertEquals( script, Arrays.asList( expected ), compile( restrictions, script ) );
  }

  /**
   * Parses (does not run) the script, returns the parse error messages
   */
  private static List<String> compile( Restrictions restrictions, String script )
  {
    ParserOptions options = new ParserOptions().withRestrictions( restrictions );
    try
    {
      GosuParserFactory.createProgramParser().parseExpressionOrProgram( script, new StandardSymbolTable( true ), options );
      return Collections.emptyList();
    }
    catch( ParseResultsException e )
    {
      return e.getParseExceptions().stream().map( pe -> pe.getPlainMessage() ).collect( Collectors.toList() );
    }
  }
}
