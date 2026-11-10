package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.LanguageFeature;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Script coverage: every place a script can name a type, run code, access a member, or use a language feature
 */
public class RestrictionsCoverageTest extends TestClass
{
  private static final String THREAD_BLOCKED =
    "Type 'java.lang.Thread' is blocked by the restrictions baseline (rule: java.lang.Thread).";
  private static final String SYSTEM_BLOCKED =
    "Type 'java.lang.System' is blocked by the restrictions baseline (rule: java.lang.System).";
  private static final String GET_STACK_TRACE_BLOCKED =
    "Member 'java.lang.Throwable#getStackTrace' is blocked by the restrictions baseline (rule: java.lang.Throwable#getStackTrace).";

  /**
   * %T is a type's qualified name, %N its simple name
   */
  private static final String[] TYPE_POSITIONS = {
      "var t: %T",
      "var a: %T[]",
      "var l: java.util.List<%T>",
      "var m: java.util.Map<String, java.util.List<%T>>",
      "var l = new java.util.ArrayList<%T>()",
      "function f(t: %T) {}",
      "function f(): %T { return null }",
      "function f<T extends %T>() {}",
      "function f<T extends java.util.List<%T>>() {}",
      "var b = \\ t: %T -> t",
      "var b: block(t: %T)",
      "var b: block(): %T",
      "class C { var _t: %T }",
      "class C { property get P(): %T { return null } }",
      "class C { construct(t: %T) {} }",
      "class C extends %T {}",
      "class C<T extends %T> {}",
      "interface I { function f(t: %T) }",
      "enum E { A  function f(t: %T) {} }",
      "var o: Object = null\nprint(o as %T)",
      "var o: Object = null\nprint(o typeis %T)",
      "try { print(1) } catch( e: java.lang.InterruptedException ) { } catch( e: java.lang.Throwable ) { var t: %T }",
      "uses %T\nvar t: %N",
  };

  public void testDeniedTypeInEveryTypePosition()
  {
    assertAllBlocked( Restrictions.baseline().build(), THREAD_BLOCKED, withType( "java.lang.Thread" ) );
    assertBlocked( "print(java.lang.Thread.currentThread())", THREAD_BLOCKED );
  }

  public void testAllowedTypeInEveryTypePosition()
  {
    assertAllAllowed( Restrictions.baseline().build(), withType( "java.util.Date" ) );
    assertAllowed( "print(java.util.Date.from(null))" );
  }

  private static String[] withType( String fqn )
  {
    String simpleName = fqn.substring( fqn.lastIndexOf( '.' ) + 1 );
    return Arrays.stream( TYPE_POSITIONS ).map( t -> t.replace( "%T", fqn ).replace( "%N", simpleName ) ).toArray( String[]::new );
  }

  public void testDeniedTypeInCatchClause()
  {
    assertBlocked( "try { print(1) } catch( e: java.io.IOException ) { }",
                   "Type 'java.io.IOException' is blocked by the restrictions baseline (rule: java.io.**)." );
  }

  public void testDeniedTypeImportedByPackage()
  {
    assertBlocked( "uses java.lang.reflect.*\nvar m: Method",
                   "Type 'java.lang.reflect.Method' is blocked by the restrictions baseline (rule: java.lang.reflect.**)." );
  }

  public void testAllowedPackageImport()
  {
    assertAllowed( "uses java.util.*\nvar l: ArrayList<String> = {}" );
  }

  public void testDeniedTypeInferredFromAllowedApi()
  {
    // Integer.TYPE is a Class
    assertBlocked( "var c = Integer.TYPE",
                   "Type 'java.lang.Class' is blocked by the restrictions baseline (rule: java.lang.Class)." );
    assertBlocked( "print(Integer.TYPE.Name)",
                   "Type 'java.lang.Class' is blocked by the restrictions baseline (rule: java.lang.Class)." );
  }

  /**
   * %s is a call returning long
   */
  private static final String[] CODE_CONTEXTS = {
      "%s",
      "var x = %s",
      "var x: long = %s",
      "function f(): long { return %s }",
      "class C { function f(): long { return %s } }",
      "class C { var _x = %s }",
      "class C { construct() { print(%s) } }",
      "class C { property get P(): long { return %s } }",
      "class C { property set P(v: long) { print(%s) } }",
      "class C { class D { function f(): long { return %s } } }",
      "enum E { A(%s)  private construct(l: long) {} }",
      "enum E { A  function f(): long { return %s } }",
      "var o = new Object() { function f(): long { return %s } }",
      "var b = \\-> %s",
      "var b = \\-> \\-> %s",
      "print(\"${%s}\")",
      "print(\"<%= %s %>\")",
      "print(true ? %s : 0)",
      "if( %s > 0 ) { print(1) }",
      "while( %s < 0 ) { print(1) }",
      "for( i in 0..|%s ) { print(i) }",
      "switch( %s ) { case 0: print(0) }",
      "throw new RuntimeException(\"\" + %s)",
      "try { print(%s) } catch( e: Exception ) { }",
      "try { print(1) } catch( e: Exception ) { print(%s) }",
      "try { print(1) } finally { print(%s) }",
      "assert %s > 0",
      "var x = 0L\nx += %s",
      "var l = {%s}",
      "var m = {\"a\" -> %s}",
      "var d = new java.util.Date() { :Time = %s }",
      "var a = new long[] { %s }",
      "var a = new int[1]\nprint(a[%s as int])",
  };

  public void testCodeInEveryContextIsChecked()
  {
    assertAllBlocked( Restrictions.baseline().build(), SYSTEM_BLOCKED, withCall( "java.lang.System.currentTimeMillis()" ) );
  }

  public void testAllowedCodeInEveryContext()
  {
    assertAllAllowed( Restrictions.baseline().build(), withCall( "java.lang.Math.abs(1L)" ) );
  }

  private static String[] withCall( String call )
  {
    return Arrays.stream( CODE_CONTEXTS ).map( c -> c.replace( "%s", call ) ).toArray( String[]::new );
  }

  /**
   * Access forms of Throwable's StackTrace property, replaced with Message for the allowed counterparts
   */
  private static final String[] MEMBER_ACCESS_FORMS = {
      "print(new Throwable().getStackTrace())",
      "print(new Throwable().StackTrace)",
      "var t = new Throwable()\nprint(t.StackTrace)",
      "var t: Throwable = null\nprint(t?.StackTrace)",
      "var t: Throwable = null\nprint(t?.getStackTrace())",
      "print((new Exception() as Throwable).StackTrace)",
      "print(new RuntimeException().StackTrace)",
      "class E extends Exception {}\nprint(new E().StackTrace)",
      "class E extends Exception { function f(): Object { return this.StackTrace } }",
      "class E extends Exception { function f(): Object { return super.StackTrace } }",
      "class E extends Exception { function f(): Object { return super.getStackTrace() } }",
      "class E extends Exception { function f(): Object { return getStackTrace() } }",
      "class E extends Exception { class Inner { function f(): Object { return outer.StackTrace } } }",
      "class E extends Exception { class Inner { function f(): Object { return StackTrace } } }",
      "var o = new Throwable() { function f(): Object { return StackTrace } }",
      "var b = \\-> new Throwable().StackTrace",
      "function f<T extends Throwable>(t: T): Object { return t.StackTrace }",
      "var l = {new Throwable()}.map(\\ t -> t.StackTrace)",
      "var l = {new Throwable()}\nprint(l.get(0).StackTrace)",
  };

  public void testDeniedMemberInEveryAccessForm()
  {
    assertAllBlocked( Restrictions.baseline().build(), GET_STACK_TRACE_BLOCKED, MEMBER_ACCESS_FORMS );
  }

  public void testAllowedMemberInEveryAccessForm()
  {
    assertAllAllowed( Restrictions.baseline().build(),
      Arrays.stream( MEMBER_ACCESS_FORMS ).map( f -> f.replace( "StackTrace", "Message" ) ).toArray( String[]::new ) );
  }

  public void testOwnMembersNamedLikeDeniedMembers()
  {
    // the rules apply to Throwable and its subtypes, not to unrelated types
    assertAllAllowed( Restrictions.baseline().build(),
      "class C { function getStackTrace(): int { return 1 } }\nprint(new C().getStackTrace())",
      "class C { property get StackTrace(): int { return 1 } }\nprint(new C().StackTrace)",
      "class C { function getStackTrace(): int { return 1 }  function f(): int { return getStackTrace() } }",
      "interface I { function getStackTrace(): int }\nvar i: I = null\nprint(i?.getStackTrace())",
      "var m = {\"StackTrace\" -> 1}\nprint(m[\"StackTrace\"])" );
  }

  private static final String[] DATE_CONSTRUCTIONS = {
      "var d = new java.util.Date()",
      "var d = new java.util.Date(10)",
      "var d = new java.util.Date() { function f() {} }",
      // a subclass constructor calls the denied constructor
      "class D extends java.util.Date {}\nvar d = new D()",
      "class D extends java.util.Date { construct() { super() } }",
      "class D extends java.util.Date { construct() { super(10) } }",
      "class D extends java.util.Date { construct() { this(10) }  construct(s: long) { super(s) } }",
  };

  public void testConstructorRuleCoverage()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Date", "<init>" )
      .build();
    String error = "Member 'java.util.Date#<init>' is blocked by the restrictions profile (rule: java.util.Date#<init>).";
    assertAllBlocked( restrictions, error, DATE_CONSTRUCTIONS );
  }

  public void testConstructionWithoutConstructorRule()
  {
    assertAllAllowed( Restrictions.baseline().build(), DATE_CONSTRUCTIONS );
    // a rule on another type's constructor
    assertAllAllowed( Restrictions.baseline().denyMember( "java.util.ArrayList", "<init>" ).build(), DATE_CONSTRUCTIONS );
  }

  public void testTypeNameBoundaries()
  {
    // a denied type's name is not a prefix rule
    assertAllowed( "print(new RuntimeException())" );
    assertAllowed( "print(new ClassCastException())" );
    assertAllowed( "print(new IllegalThreadStateException())" );

    // nested types of a denied type are denied
    assertBlocked( "print(java.lang.Thread.State.NEW)", THREAD_BLOCKED );
    assertBlocked( "var h: java.lang.Thread.UncaughtExceptionHandler", THREAD_BLOCKED );

    // allowPackage does not include subpackages, allowPackageTree does
    assertBlocked( "var m: java.util.concurrent.ConcurrentHashMap<String, String>",
                   "Type 'java.util.concurrent.ConcurrentHashMap' is not an allowed type or package in the restrictions profile." );
    assertAllowed( "var f = java.time.format.DateTimeFormatter.ISO_DATE" );

    // a denied package tree within an allowed package
    assertBlocked( "var h: java.lang.invoke.MethodHandle",
                   "Type 'java.lang.invoke.MethodHandle' is blocked by the restrictions baseline (rule: java.lang.invoke.**)." );
  }

  private static final Map<LanguageFeature, String> FEATURE_SAMPLES = new EnumMap<>( LanguageFeature.class );
  static
  {
    FEATURE_SAMPLES.put( LanguageFeature.TYPEOF, "print(typeof \"x\")" );
    FEATURE_SAMPLES.put( LanguageFeature.FEATURE_LITERAL, "print(String#length())" );
    FEATURE_SAMPLES.put( LanguageFeature.EVAL, "print(eval(\"1\"))" );
    FEATURE_SAMPLES.put( LanguageFeature.DYNAMIC, "var d: dynamic.Dynamic = \"x\"\nprint(d.length())" );
    FEATURE_SAMPLES.put( LanguageFeature.STRUCTURAL, "structure S { function charAt(i: int): char }\nvar s: S = \"abc\"" );
  }

  public void testEveryLanguageFeatureIsGated()
  {
    for( LanguageFeature feature : LanguageFeature.values() )
    {
      String script = FEATURE_SAMPLES.get( feature );
      assertNotNull( "no sample script for " + feature, script );
      String error = featureError( feature );

      // disabled in the baseline
      assertBlocked( script, error );

      // enabled: no error for the feature; other rules may still apply
      Restrictions enabled = Restrictions.empty( feature )
        .allowPrimitives()
        .allowPackage( "java.lang" )
        .allowPackageTree( "gw.lang.reflect" )
        .allowType( "dynamic.Dynamic" )
        .build();
      List<String> errors = compile( enabled, script );
      assertFalse( script + " " + errors, errors.contains( error ) );
    }
  }

  public void testCommonScriptsAllowed()
  {
    assertAllAllowed( Restrictions.baseline().build(),
      "uses java.lang.Math#*\nprint(max(1, 2))",
      "var l = {3, 1, 2}\nprint(l.where(\\ i -> i > 1).orderBy(\\ i -> i).map(\\ i -> i * 2))",
      "var l = {\"a\", \"b\"}\nl.each(\\ s -> print(s.toUpperCase()))",
      "var m = {\"a\" -> 1}\nm[\"b\"] = 2\nprint(m.Keys)",
      "var s = \"abc\"\nprint(s.HasContent && s.length() > 0)",
      "var n = 1\nprint(\"n is ${n} and <%= n + 1 %>\")",
      "var b: block(i: int): int = \\ i -> i + 1\nprint(b(1))",
      "function f<T>(t: T): T { return t }\nprint(f(\"x\"))",
      "var x = 10\nx += 2\nx *= 3\nx++\nprint(x)",
      "class C { var _p: int as P }\nvar c = new C()\nc.P = 1\nc.P += 1\nprint(c.P)",
      "class C { var _p: int as P  function f() { P += 1 } }",
      "class C implements Comparable<C> { override function compareTo(o: C): int { return 0 } }",
      "class C { override function toString(): String { return \"C\" }  override function hashCode(): int { return 1 } }",
      "class E extends Exception { construct(m: String) { super(m) } }\ntry { throw new E(\"x\") } catch( e: E ) { print(e.Message) }",
      "enum E { A, B }\nprint(E.A.Name)\nprint(E.A.ordinal())\nprint(E.values())",
      "var d = new java.util.Date() { :Time = 0 }",
      "var bd = java.math.BigDecimal.valueOf(1).add(java.math.BigDecimal.ONE)",
      "var r = java.util.regex.Pattern.compile(\"a+\").matcher(\"aa\").matches()",
      "var t = java.time.LocalDate.now().plusDays(1)",
      "var o: Object = \"x\"\nif( o typeis String ) { print(o.length()) }",
      "var o: Object = \"x\"\nprint((o as String).length())",
      "var a = new int[] { 1, 2 }\nfor( i in a index j ) { print(i + j) }",
      "var sb = new StringBuilder()\nsb.append(\"a\").append(1)\nprint(sb)",
      "var s = {1, 2}.stream().map(\\ i -> i + 1).count()" );
  }

  public void testEnabledFeatureCompilesCleanly()
  {
    for( LanguageFeature feature : LanguageFeature.values() )
    {
      Restrictions enabled = Restrictions.empty( feature )
        .allowPrimitives()
        .allowPackage( "java.lang" )
        .allowPackageTree( "gw.lang.reflect" )
        .allowType( "dynamic.Dynamic" )
        .build();
      assertAllAllowed( enabled, FEATURE_SAMPLES.get( feature ) );
    }
  }

  public void testEmptyProfileDisablesAllLanguageFeatures()
  {
    Restrictions none = Restrictions.empty().allowPrimitives().allowPackage( "java.lang" ).build();
    assertBlocked( none, "print(typeof \"x\")", featureError( LanguageFeature.TYPEOF ) );
    assertBlocked( none, "print(eval(\"1\"))", featureError( LanguageFeature.EVAL ) );
  }

  private static String featureError( LanguageFeature feature )
  {
    return "Language feature '" + feature.getDisplayName() + "' is not permitted by the restrictions profile.";
  }

  /**
   * Reports every script that is not blocked, not just the first
   */
  private static void assertAllBlocked( Restrictions restrictions, String expected, String... scripts )
  {
    List<String> failures = new ArrayList<>();
    for( String script : scripts )
    {
      try
      {
        assertBlocked( restrictions, script, expected );
      }
      catch( AssertionError e )
      {
        failures.add( e.getMessage() );
      }
    }
    assertTrue( failures.size() + " of " + scripts.length + " failed:\n" + String.join( "\n", failures ), failures.isEmpty() );
  }

  /**
   * Reports every script that has errors, not just the first
   */
  private static void assertAllAllowed( Restrictions restrictions, String... scripts )
  {
    List<String> failures = new ArrayList<>();
    for( String script : scripts )
    {
      List<String> errors = compile( restrictions, script );
      if( !errors.isEmpty() )
      {
        failures.add( "flagged: " + script + " " + errors );
      }
    }
    assertTrue( failures.size() + " of " + scripts.length + " failed:\n" + String.join( "\n", failures ), failures.isEmpty() );
  }

  private static void assertBlocked( String script, String expected )
  {
    assertBlocked( Restrictions.baseline().build(), script, expected );
  }

  /**
   * The script's errors include the expected error and are all restriction errors, so the script itself is valid
   */
  private static void assertBlocked( Restrictions restrictions, String script, String expected )
  {
    List<String> errors = compile( restrictions, script );
    List<String> other = errors.stream().filter( e -> !isRestrictionError( e ) ).collect( Collectors.toList() );
    assertTrue( "invalid sample script: " + script + " " + other, other.isEmpty() );
    assertTrue( "not blocked: " + script + " " + errors, errors.contains( expected ) );
  }

  private static void assertAllowed( String script )
  {
    assertAllAllowed( Restrictions.baseline().build(), script );
  }

  private static boolean isRestrictionError( String message )
  {
    return message.contains( "the restrictions baseline" ) || message.contains( "the restrictions profile" );
  }

  /**
   * Parses (does not run) the script, returns the parse error messages
   */
  private static List<String> compile( Restrictions restrictions, String script )
  {
    ParserOptions options = new ParserOptions().withAstProcessorProviders( restrictions );
    try
    {
      GosuParserFactory.createProgramParser().parseExpressionOrProgram( script, new StandardSymbolTable( true ), options );
      return Collections.emptyList();
    }
    catch( ParseResultsException e )
    {
      return e.getParseExceptions().stream().map( pe -> pe.getPlainMessage() ).collect( Collectors.toCollection( ArrayList::new ) );
    }
  }
}
