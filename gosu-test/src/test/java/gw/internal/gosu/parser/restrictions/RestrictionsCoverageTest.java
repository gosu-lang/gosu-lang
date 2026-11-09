package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.LanguageFeature;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;

import java.util.ArrayList;
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

  public void testDeniedTypeInEveryTypePosition()
  {
    String[] scripts = {
      "var t: java.lang.Thread",
      "var a: java.lang.Thread[]",
      "var l: java.util.List<java.lang.Thread>",
      "var m: java.util.Map<String, java.util.List<java.lang.Thread>>",
      "var l = new java.util.ArrayList<java.lang.Thread>()",
      "function f(t: java.lang.Thread) {}",
      "function f(): java.lang.Thread { return null }",
      "function f<T extends java.lang.Thread>() {}",
      "function f<T extends java.util.List<java.lang.Thread>>() {}",
      "var b = \\ t: java.lang.Thread -> t",
      "var b: block(t: java.lang.Thread)",
      "var b: block(): java.lang.Thread",
      "class C { var _t: java.lang.Thread }",
      "class C { property get P(): java.lang.Thread { return null } }",
      "class C { construct(t: java.lang.Thread) {} }",
      "class C extends java.lang.Thread {}",
      "class C<T extends java.lang.Thread> {}",
      "interface I { function f(t: java.lang.Thread) }",
      "enum E { A  function f(t: java.lang.Thread) {} }",
      "var o: Object = null\nprint(o as java.lang.Thread)",
      "var o: Object = null\nprint(o typeis java.lang.Thread)",
      "try { print(1) } catch( e: java.lang.InterruptedException ) { } catch( e: java.lang.Throwable ) { var t: java.lang.Thread }",
      "uses java.lang.Thread\nvar t: Thread",
      "print(java.lang.Thread.currentThread())",
    };
    assertAllBlocked( Restrictions.baseline().build(), THREAD_BLOCKED, scripts );
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

  public void testCodeInEveryContextIsChecked()
  {
    String call = "java.lang.System.currentTimeMillis()";
    String[] scripts = {
      call,
      "var x = " + call,
      "var x: long = " + call,
      "function f(): long { return " + call + " }",
      "class C { function f(): long { return " + call + " } }",
      "class C { var _x = " + call + " }",
      "class C { construct() { print(" + call + ") } }",
      "class C { property get P(): long { return " + call + " } }",
      "class C { property set P(v: long) { print(" + call + ") } }",
      "class C { class D { function f(): long { return " + call + " } } }",
      "enum E { A(" + call + ")  private construct(l: long) {} }",
      "enum E { A  function f(): long { return " + call + " } }",
      "var o = new Object() { function f(): long { return " + call + " } }",
      "var b = \\-> " + call,
      "var b = \\-> \\-> " + call,
      "print(\"${" + call + "}\")",
      "print(\"<%= " + call + " %>\")",
      "print(true ? " + call + " : 0)",
      "if( " + call + " > 0 ) { print(1) }",
      "while( " + call + " < 0 ) { print(1) }",
      "for( i in 0..|" + call + " ) { print(i) }",
      "switch( " + call + " ) { case 0: print(0) }",
      "throw new RuntimeException(\"\" + " + call + ")",
      "try { print(" + call + ") } catch( e: Exception ) { }",
      "try { print(1) } catch( e: Exception ) { print(" + call + ") }",
      "try { print(1) } finally { print(" + call + ") }",
      "assert " + call + " > 0",
      "var x = 0L\nx += " + call,
      "var l = {" + call + "}",
      "var m = {\"a\" -> " + call + "}",
      "var d = new java.util.Date() { :Time = " + call + " }",
      "var a = new long[] { " + call + " }",
      "var a = new int[1]\nprint(a[" + call + " as int])",
    };
    assertAllBlocked( Restrictions.baseline().build(), SYSTEM_BLOCKED, scripts );
  }

  public void testDeniedMemberInEveryAccessForm()
  {
    String[] scripts = {
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
    assertAllBlocked( Restrictions.baseline().build(), GET_STACK_TRACE_BLOCKED, scripts );
  }

  public void testConstructorRuleCoverage()
  {
    Restrictions restrictions = Restrictions.baseline()
      .denyMember( "java.util.Date", "<init>" )
      .build();
    String error = "Member 'java.util.Date#<init>' is blocked by the restrictions profile (rule: java.util.Date#<init>).";
    assertAllBlocked( restrictions, error,
      "var d = new java.util.Date()",
      "var d = new java.util.Date(10)",
      "var d = new java.util.Date() { function f() {} }",
      // a subclass constructor calls the denied constructor
      "class D extends java.util.Date {}\nvar d = new D()",
      "class D extends java.util.Date { construct() { super() } }",
      "class D extends java.util.Date { construct() { super(10) } }",
      "class D extends java.util.Date { construct() { this(10) }  construct(s: long) { super(s) } }" );
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

  public void testEveryLanguageFeatureIsGated()
  {
    Map<LanguageFeature, String> samples = new EnumMap<>( LanguageFeature.class );
    samples.put( LanguageFeature.TYPEOF, "print(typeof \"x\")" );
    samples.put( LanguageFeature.FEATURE_LITERAL, "print(String#length())" );
    samples.put( LanguageFeature.EVAL, "print(eval(\"1\"))" );
    samples.put( LanguageFeature.DYNAMIC, "var d: dynamic.Dynamic = \"x\"\nprint(d.length())" );
    samples.put( LanguageFeature.STRUCTURAL, "structure S { function charAt(i: int): char }\nvar s: S = \"abc\"" );

    for( LanguageFeature feature : LanguageFeature.values() )
    {
      String script = samples.get( feature );
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
    List<String> errors = compile( Restrictions.baseline().build(), script );
    assertTrue( script + " " + errors, errors.isEmpty() );
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
