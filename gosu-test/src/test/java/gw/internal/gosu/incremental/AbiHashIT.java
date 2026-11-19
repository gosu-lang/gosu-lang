package gw.internal.gosu.incremental;

import gw.internal.ext.com.google.gson.JsonObject;
import gw.internal.ext.com.google.gson.JsonParser;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.tree.AnnotationNode;
import gw.internal.ext.org.objectweb.asm.tree.ClassNode;
import gw.internal.ext.org.objectweb.asm.tree.FieldNode;
import gw.internal.ext.org.objectweb.asm.tree.MethodNode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the ABI hash gosuc records for a compiled Gosu class: it must be blind to
 * everything a separately compiled consumer cannot observe, and sensitive to everything it can.
 * Each test compiles a small {@code p.Fixture} with gosuc in incremental mode into a fresh
 * directory and reads the class's hash from the dependency file, so the hash under test is the
 * one the incremental driver compares: bytecode surface plus the Gosu compile-time surface
 * (constant values, parameter names, default parameter values) that never reaches the class file.
 * Member visibility is judged the way gosuc emits it: a Gosu-private member is written
 * package-private and is not ABI, a member declared {@code internal} is package-private plus
 * {@code @gw.lang.ir.Internal} and is ABI.
 *
 * <p>Runs under failsafe, in its own JVM, because every test initializes gosuc in-process and
 * gosuc refuses to initialize in a JVM where the Gosu runtime is already up, as it is in the
 * surefire JVM once any harness-based test has run.
 */
public class AbiHashIT
{
  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private int _compiles;

  private static final String TAG_ANNOTATION =
    "package p\n" +
    "uses java.lang.annotation.ElementType\n" +
    "uses java.lang.annotation.Target\n" +
    "uses java.lang.annotation.Retention\n" +
    "uses java.lang.annotation.RetentionPolicy\n" +
    "\n" +
    "@Target({ElementType.TYPE, ElementType.METHOD})\n" +
    "@Retention(RetentionPolicy.RUNTIME)\n" +
    "annotation Tag {\n" +
    "  function value() : String\n" +
    "}\n";

  /** {@link #TAG_ANNOTATION} with an array-valued member. */
  private static final String TAG_ARRAY_ANNOTATION = TAG_ANNOTATION.replace( "function value() : String\n", "function value() : String[]\n" );

  /** An annotation with two named members, for argument-order checks. */
  private static final String COLUMN_ANNOTATION =
    "package p\n" +
    "uses java.lang.annotation.ElementType\n" +
    "uses java.lang.annotation.Target\n" +
    "uses java.lang.annotation.Retention\n" +
    "uses java.lang.annotation.RetentionPolicy\n" +
    "\n" +
    "@Target({ElementType.TYPE})\n" +
    "@Retention(RetentionPolicy.RUNTIME)\n" +
    "annotation Column {\n" +
    "  function name() : String\n" +
    "  function email() : String\n" +
    "}\n";

  /** {@link #COLUMN_ANNOTATION} with its two members declared in the other order. */
  private static final String COLUMN_ANNOTATION_EMAIL_FIRST =
    COLUMN_ANNOTATION.replace( "  function name() : String\n  function email() : String\n",
                               "  function email() : String\n  function name() : String\n" );

  /** A fixture annotated with both of {@link #COLUMN_ANNOTATION}'s members, by name. */
  private static final String COLUMN_USAGE = "package p\n\n@Column(:name = \"username\", :email = \"a@bar.com\")\nclass Fixture {}\n";

  /** {@link #COLUMN_USAGE} spelled with positional arguments, which bind to the members in declaration order. */
  private static final String COLUMN_POSITIONAL_USAGE = "package p\n\n@Column(\"username\", \"a@bar.com\")\nclass Fixture {}\n";

  private static final String BASE =
    "  public static final var LIMIT : int = 3\n" +
    "  public var count : int\n" +
    "  var _secret : int = 1\n" +
    "  function names() : List<String> { return new java.util.ArrayList<String>() }\n" +
    "  protected function touch() { _secret++ }\n" +
    "  private function hidden() {}\n";

  /** The output of one gosuc run: the fixture's hash, its class file, and the dependency file every hash is read from. */
  private static final class Compiled
  {
    final String hash;
    final Path classFile;
    final Path depFile;

    Compiled( String hash, Path classFile, Path depFile )
    {
      this.hash = hash;
      this.classFile = classFile;
      this.depFile = depFile;
    }
  }

  /** Compiles {@code p.Fixture} with the given class body. */
  private Compiled compileBody( String body ) throws IOException
  {
    return compile( "p/Fixture.gs",
                    "package p\n" +
                    "\n" +
                    "uses java.util.List\n" +
                    "\n" +
                    "class Fixture {\n" + body + "\n}\n" );
  }

  /**
   * Compiles the given sources, alternating relative path and content, with gosuc in incremental
   * mode into a fresh directory, and returns {@code p.Fixture}'s hash from the dependency file.
   */
  private Compiled compile( String... pathsAndSources ) throws IOException
  {
    Path dir = tempFolder.getRoot().toPath().resolve( "compile" + (_compiles++) );
    Path srcDir = dir.resolve( "src" );
    Path outDir = dir.resolve( "out" );
    Path depFile = dir.resolve( "deps.json" );
    Files.createDirectories( outDir );

    List<String> args = new ArrayList<>();
    args.add( "-classpath" );
    args.add( System.getProperty( "java.class.path" ) );
    args.add( "-d" );
    args.add( outDir.toAbsolutePath().toString() );
    args.add( "-sourcepath" );
    args.add( srcDir.toAbsolutePath().toString() );
    args.add( "-incremental" );
    args.add( "-dependency-file" );
    args.add( depFile.toAbsolutePath().toString() );
    for( int i = 0; i + 1 < pathsAndSources.length; i += 2 )
    {
      Path file = srcDir.resolve( pathsAndSources[i] );
      Files.createDirectories( file.getParent() );
      Files.write( file, pathsAndSources[i + 1].getBytes( StandardCharsets.UTF_8 ) );
      args.add( file.toAbsolutePath().toString() );
    }

    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    PrintStream originalErr = System.err;
    int exitCode;
    try
    {
      System.setOut( new PrintStream( captured ) );
      System.setErr( new PrintStream( captured ) );
      exitCode = gw.lang.gosuc.cli.CommandLineCompiler.runCompiler( args.toArray( new String[0] ) );
      originalOut.println(captured);
    }
    finally
    {
      System.setOut( originalOut );
      System.setErr( originalErr );
    }
    if( exitCode != 0 )
    {
      throw new IllegalStateException( "Fixture failed to compile:\n" + captured );
    }

    return new Compiled( abiHashOf( depFile, "p.Fixture" ), outDir.resolve( "p/Fixture.class" ), depFile );
  }

  /** The ABI hash {@code depFile} records for {@code fqcn}. */
  private static String abiHashOf( Path depFile, String fqcn ) throws IOException
  {
    JsonObject depGraph = JsonParser.parseString( Files.readString( depFile ) ).getAsJsonObject().getAsJsonObject( "dep_graph" );
    return depGraph.getAsJsonObject( fqcn ).get( "abi_hash" ).getAsString();
  }

  /** The consumers {@code depFile} records for {@code fqcn}. */
  private static List<String> consumersOf( Path depFile, String fqcn ) throws IOException
  {
    JsonObject depGraph = JsonParser.parseString( Files.readString( depFile ) ).getAsJsonObject().getAsJsonObject( "dep_graph" );
    List<String> consumers = new ArrayList<>();
    depGraph.getAsJsonObject( fqcn ).getAsJsonArray( "consumers" ).forEach( consumer -> consumers.add( consumer.getAsString() ) );
    return consumers;
  }

  /** The raw bytes of a class file as text, for looking up string constants a compile baked into it. */
  private static String classFileText( Path classFile ) throws IOException
  {
    return new String( Files.readAllBytes( classFile ), StandardCharsets.ISO_8859_1 );
  }

  private static void assertSameAbi( String why, Compiled before, Compiled after )
  {
    assertEquals( why, before.hash, after.hash );
  }

  /** Both versions are classes whose surface names a structure, which the walk reads as changed whenever recompiled. */
  private static void assertStructural( String what, Compiled before, Compiled after )
  {
    assertEquals( what + " is not hashed", IncrementalCompilationManager.CANNOT_COMPUTE_ABI_HASH, before.hash );
    assertEquals( what + " is not hashed", IncrementalCompilationManager.CANNOT_COMPUTE_ABI_HASH, after.hash );
  }

  private static void assertDifferentAbi( String why, Compiled before, Compiled after )
  {
    assertNotEquals( why + " (both hashed to " + before.hash + ")", before.hash, after.hash );
  }

  private static ClassNode readClass( Path classFile ) throws IOException
  {
    ClassNode node = new ClassNode();
    new ClassReader( Files.readAllBytes( classFile ) ).accept( node, ClassReader.SKIP_CODE );
    return node;
  }

  private static int fieldAccess( ClassNode node, String name )
  {
    for( FieldNode field : node.fields )
    {
      if( field.name.equals( name ) )
      {
        return field.access;
      }
    }
    throw new AssertionError( "no field " + name + " in " + node.name );
  }

  /** The descriptor gosuc wrote for the field {@code name} on {@code node}. */
  private static String fieldDescriptor( ClassNode node, String name )
  {
    for( FieldNode field : node.fields )
    {
      if( field.name.equals( name ) )
      {
        return field.desc;
      }
    }
    throw new AssertionError( "no field " + name + " in " + node.name );
  }

  /** Every method gosuc wrote on {@code node}, as name, descriptor and generic signature. */
  private static List<String> methodSignatures( ClassNode node )
  {
    List<String> methods = new ArrayList<>();
    for( MethodNode method : node.methods )
    {
      methods.add( method.name + method.desc + " " + method.signature );
    }
    return methods;
  }

  private static int methodAccess( ClassNode node, String name )
  {
    for( MethodNode method : node.methods )
    {
      if( method.name.equals( name ) )
      {
        return method.access;
      }
    }
    throw new AssertionError( "no method " + name + " in " + node.name );
  }

  /**
   * The value gosuc wrote for {@code member} of the runtime-visible annotation {@code annoDesc} on {@code node}, as
   * ASM's tree API represents it: a {@code List} for an array member, in class-file order.
   */
  private static Object annotationMember( ClassNode node, String annoDesc, String member )
  {
    if( node.visibleAnnotations != null )
    {
      for( AnnotationNode anno : node.visibleAnnotations )
      {
        if( annoDesc.equals( anno.desc ) && anno.values != null )
        {
          // anno.values alternates member name and value.
          for( int i = 0; i + 1 < anno.values.size(); i += 2 )
          {
            if( member.equals( anno.values.get( i ) ) )
            {
              return anno.values.get( i + 1 );
            }
          }
        }
      }
    }
    throw new AssertionError( "no " + annoDesc + "." + member + " on " + node.name );
  }

  /** The member names of the runtime-visible annotation {@code annoDesc} on {@code node}, in class-file order. */
  private static List<String> annotationMemberNames( ClassNode node, String annoDesc )
  {
    if( node.visibleAnnotations != null )
    {
      for( AnnotationNode anno : node.visibleAnnotations )
      {
        if( annoDesc.equals( anno.desc ) && anno.values != null )
        {
          List<String> names = new ArrayList<>();
          for( int i = 0; i + 1 < anno.values.size(); i += 2 )
          {
            names.add( (String)anno.values.get( i ) );
          }
          return names;
        }
      }
    }
    throw new AssertionError( "no " + annoDesc + " on " + node.name );
  }

  // ---------------------------------------------------------------------------------------------
  // Blind to what a consumer cannot observe
  // ---------------------------------------------------------------------------------------------

  @Test
  public void testRecompilingTheSameSourceHashesIdentically() throws IOException
  {
    assertSameAbi( "Two compiles of one source must agree", compileBody( BASE ), compileBody( BASE ) );
  }

  @Test
  public void testHashIsHexSha1() throws IOException
  {
    String hash = compileBody( BASE ).hash;
    assertTrue( "Expected 40 lowercase hex digits, got " + hash, hash.matches( "[0-9a-f]{40}" ) );
  }

  @Test
  public void testMethodBodyChangeIsNotAnAbiChange() throws IOException
  {
    assertSameAbi( "A body edit leaves the ABI alone",
                   compileBody( BASE ),
                   compileBody( BASE.replace( "_secret++", "_secret += 2" ) ) );
  }

  @Test
  public void testDebugInfoIsNotAnAbiChange() throws IOException
  {
    assertSameAbi( "Comments and line numbers leave the ABI alone",
                   compileBody( BASE ),
                   compileBody( "\n\n  // moved everything down two lines\n" + BASE ) );
  }

  @Test
  public void testPrivateMembersAreNotAbi() throws IOException
  {
    assertSameAbi( "Adding, renaming and retyping private members leaves the ABI alone",
                   compileBody( BASE ),
                   compileBody( BASE.replace( "var _secret : int = 1", "var _hush : long = 2\n  var _extra : String" )
                                    .replace( "_secret++", "_hush++" )
                                    .replace( "private function hidden() {}", "private function hidden(x : int) : int { return x }" ) ) );
  }

  @Test
  public void testPrivateStaticMembersAreNotAbi() throws IOException
  {
    // Written package-private with the static bit set; still nameable by nothing outside the source file.
    assertSameAbi( "Private static members leave the ABI alone",
                   compileBody( BASE ),
                   compileBody( BASE + "  static var _hits : int = 0\n" +
                                       "  private static function trim(key : String) : String { return key.trim() }\n" ) );
  }

  @Test
  public void testPrivateConstructorsAreNotAbi() throws IOException
  {
    // A Gosu-private constructor is not ABI, and it is decided twice over by two independent predicates: the
    // bytecode half drops it through isSourceCodePrivate, since getModifiers writes Gosu-private as
    // package-private rather than ACC_PRIVATE, and the Gosu compile-time surface drops it through
    // IConstructorInfo.isPrivate, which keeps its parameter names and default values out too -- consistent,
    // because no caller outside this file can omit an argument on a constructor it cannot call. This pins that
    // the two agree.
    //
    // It takes two fixture families, because Gosu rejects "Overloading not allowed with optional parameters":
    // a constructor carrying a default cannot sit beside another one. So the addition is checked with a private
    // constructor that has no default, beside a public one; and the parameter name and default value are
    // checked on a sole private constructor. Within each family the number of declared constructors is
    // constant, so the implicit default constructor is suppressed in every fixture and cannot confound the
    // comparison. The last assertion is the control: it proves a constructor does reach the hash once it is
    // accessible, so the ones above are not passing vacuously.
    String ctor = "  public construct( n : int ) {}\n";
    assertSameAbi( "Adding a private constructor leaves the ABI alone",
                   compileBody( BASE + ctor ),
                   compileBody( BASE + ctor + "  private construct( tag : String ) {}\n" ) );

    String priv = "  private construct( tag : String = \"x\" ) {}\n";
    Compiled sole = compileBody( BASE + priv );
    assertSameAbi( "Renaming a private constructor's parameter leaves the ABI alone",
                   sole, compileBody( BASE + priv.replace( "tag", "label" ) ) );
    assertSameAbi( "Changing a private constructor's default value leaves the ABI alone",
                   sole, compileBody( BASE + priv.replace( "\"x\"", "\"y\"" ) ) );
    assertDifferentAbi( "The same constructor declared public is ABI",
                        sole, compileBody( BASE + priv.replace( "private construct", "public construct" ) ) );
  }

  @Test
  public void testGosuPrivateMembersAreWrittenPackagePrivate() throws IOException
  {
    // The premise of the privacy rule: gosuc never emits ACC_PRIVATE, so package-private without
    // @Internal is what a Gosu-private member looks like in bytecode.
    ClassNode node = readClass( compileBody( BASE ).classFile );
    int field = fieldAccess( node, "_secret" );
    int method = methodAccess( node, "hidden" );
    assertEquals( "A private var is written package-private", 0, field & (Opcodes.ACC_PRIVATE | Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED) );
    assertEquals( "A private function is written package-private", 0, method & (Opcodes.ACC_PRIVATE | Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED) );
  }

  @Test
  public void testStaticInitializerIsWrittenPackagePrivate() throws IOException
  {
    // The premise of <clinit>'s exclusion, which is a side effect rather than a rule of its own: nothing filters
    // it by name, it simply arrives with the static bit alone, so isSourceCodePrivate reads it as Gosu-private
    // and visitEnd drops it like any other private method. That is the right outcome -- nothing about <clinit>
    // is consumer-visible -- but it rests on the access value, and emitting it public would quietly add it to
    // every class's hashed method list. Its presence is part of the premise too: gosuc seeds the body with a
    // Bootstrap.init call before the guard that would otherwise omit it, so every class has one.
    ClassNode node = readClass( compileBody( BASE ).classFile );
    assertEquals( "<clinit> is written neither public nor protected, which is what keeps it out of the ABI",
                  0, methodAccess( node, "<clinit>" ) & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED) );
  }

  @Test
  public void testMemberOrderIsNotAnAbiChange() throws IOException
  {
    String reordered =
      "  private function hidden() {}\n" +
      "  protected function touch() { _secret++ }\n" +
      "  function names() : List<String> { return new java.util.ArrayList<String>() }\n" +
      "  var _secret : int = 1\n" +
      "  public var count : int\n" +
      "  public static final var LIMIT : int = 3\n";
    assertSameAbi( "Declaration order leaves the ABI alone", compileBody( BASE ), compileBody( reordered ) );
  }

  @Test
  public void testAnonymousClassInsideABodyIsNotAnAbiChange() throws IOException
  {
    // gosuc lists the anonymous class in Fixture's InnerClasses attribute; that entry is not ABI.
    assertSameAbi( "An anonymous class added inside a method body leaves the ABI alone",
                   compileBody( BASE ),
                   compileBody( BASE.replace( "_secret++", "_secret++\n    new Runnable() { override function run() {} }.run()" ) ) );
  }

  @Test
  public void testBlockInsideABodyIsNotAnAbiChange() throws IOException
  {
    // Same for the block class gosuc compiles a lambda to.
    assertSameAbi( "A block added inside a method body leaves the ABI alone",
                   compileBody( BASE ),
                   compileBody( BASE.replace( "_secret++", "var twice = \\ x : int -> x * 2\n    _secret += twice( 1 )" ) ) );
  }

  // ---------------------------------------------------------------------------------------------
  // Sensitive to what a consumer can observe
  // ---------------------------------------------------------------------------------------------

  @Test
  public void testAddingAPublicMethodChangesTheAbi() throws IOException
  {
    assertDifferentAbi( "A new public method is ABI",
                        compileBody( BASE ),
                        compileBody( BASE + "  function more() : int { return 1 }\n" ) );
  }

  @Test
  public void testInternalMembersAreAbi() throws IOException
  {
    // gosuc marks members declared `internal` with @gw.lang.ir.Internal; same-package consumers can name
    // them, so they are ABI even though the JVM sees them as package-private like a private member.
    assertDifferentAbi( "An internal function is ABI",
                        compileBody( BASE ),
                        compileBody( BASE + "  internal function more() : int { return 1 }\n" ) );
    // This assertion is the one guard on the visitor's rendered-text match for @Internal: an internal
    // function would move the hash anyway through its type-info surface line, but a plain var has no such
    // line, so only the bytecode half can make this pair differ. Keep it.
    assertDifferentAbi( "An internal var is ABI",
                        compileBody( BASE ),
                        compileBody( BASE + "  internal var shared : int\n" ) );
  }

  @Test
  public void testChangingAReturnTypeChangesTheAbi() throws IOException
  {
    assertDifferentAbi( "A descriptor change is ABI",
                        compileBody( BASE ),
                        compileBody( BASE.replace( "protected function touch() { _secret++ }",
                                                   "protected function touch() : int { _secret++\n    return _secret }" ) ) );
  }

  @Test
  public void testChangingAGenericSignatureChangesTheAbi() throws IOException
  {
    // Same erased descriptor (List), different Signature attribute.
    assertDifferentAbi( "A generic signature change is ABI even when the erasure is unchanged",
                        compileBody( BASE ),
                        compileBody( BASE.replace( "names() : List<String> { return new java.util.ArrayList<String>() }",
                                                   "names() : List<Integer> { return new java.util.ArrayList<Integer>() }" ) ) );
  }

  @Test
  public void testChangingAConstantValueChangesTheAbi() throws IOException
  {
    // gosuc writes no ConstantValue attribute, yet consumers fold the value; it comes from type info.
    assertDifferentAbi( "Consumers inline static final constants, so their values are ABI",
                        compileBody( BASE ),
                        compileBody( BASE.replace( "LIMIT : int = 3", "LIMIT : int = 4" ) ) );
  }

  @Test
  public void testChangingAccessFlagsChangesTheAbi() throws IOException
  {
    assertDifferentAbi( "Widening a protected function to public is ABI",
                        compileBody( BASE ),
                        compileBody( BASE.replace( "protected function touch()", "public function touch()" ) ) );
  }

  @Test
  public void testChangingSuperTypesChangesTheAbi() throws IOException
  {
    assertDifferentAbi( "An added interface is ABI",
                        compile( "p/Fixture.gs", "package p\n\nclass Fixture {}\n" ),
                        compile( "p/Fixture.gs", "package p\n\nclass Fixture implements java.io.Serializable {}\n" ) );
  }

  @Test
  public void testAnnotationsAreAbi() throws IOException
  {
    String plain = "package p\n\nclass Fixture {\n  function m() {}\n}\n";
    Compiled none = compile( "p/Tag.gs", TAG_ANNOTATION, "p/Fixture.gs", plain );
    Compiled onClass = compile( "p/Tag.gs", TAG_ANNOTATION, "p/Fixture.gs", plain.replace( "class Fixture", "@Tag(\"a\")\nclass Fixture" ) );
    Compiled onMethod = compile( "p/Tag.gs", TAG_ANNOTATION, "p/Fixture.gs", plain.replace( "function m()", "@Tag(\"a\")\n  function m()" ) );
    Compiled otherValue = compile( "p/Tag.gs", TAG_ANNOTATION, "p/Fixture.gs", plain.replace( "class Fixture", "@Tag(\"b\")\nclass Fixture" ) );

    assertDifferentAbi( "A class annotation is ABI", none, onClass );
    assertDifferentAbi( "A method annotation is ABI", none, onMethod );
    assertDifferentAbi( "An annotation's values are ABI", onClass, otherValue );
  }

  @Test
  public void testParameterNamesAreAbi() throws IOException
  {
    // Named-argument call sites bind against parameter names, which gosuc never writes into the class file.
    assertDifferentAbi( "Parameter names are ABI",
                        compileBody( "  function m(first : int) {}\n" ),
                        compileBody( "  function m(second : int) {}\n" ) );
  }

  @Test
  public void testDefaultParameterValuesAreAbi() throws IOException
  {
    // The parser splices the default expression into every call site that omits the argument.
    assertDifferentAbi( "Default parameter values are ABI",
                        compileBody( "  static function greet(name : String = \"world\") : String { return \"hi \" + name }\n" ),
                        compileBody( "  static function greet(name : String = \"there\") : String { return \"hi \" + name }\n" ) );
  }

  @Test
  public void testGivingAParameterANullDefaultIsAnAbiChange() throws IOException
  {
    // A parameter without a default and one whose default is the null literal have the same descriptor, but a
    // caller may omit the argument only in the second case. The hashed surface renders them as "none" and
    // "null", so adding or removing a `= null` default reaches the callers that omit the argument.
    assertDifferentAbi( "A null default is ABI",
                        compileBody( "  static function greet(name : String) : String { return \"hi \" + name }\n" ),
                        compileBody( "  static function greet(name : String = null) : String { return \"hi \" + name }\n" ) );
  }

  /**
   * A method's own type variables reach the Gosu surface only by name: {@code typeText} renders a type variable as
   * {@code T} and {@code parameterSurface} does not render the method's type variables at all, so moving {@code T}'s
   * bound leaves the type-info lines unchanged. The bound decides the erasure, though, and with it the descriptor a
   * caller's invocation names; only the bytecode half of the hash sees that. A stale caller would fail with
   * NoSuchMethodError. This is why the two halves are not consolidated into the type-info surface alone.
   */
  @Test
  public void testMethodTypeVariableBoundIsAbi() throws IOException
  {
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.pick( \"a\" ) }\n}\n";
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static function pick<T extends CharSequence>( x : T ) : T { return x }\n" +
                     "}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "T extends CharSequence", "T extends Comparable<T>" ),
                              "p/Caller.gs", caller );
    assertTrue( "precondition: the caller invokes the CharSequence erasure",
                classFileText( before.classFile.resolveSibling( "Caller.class" ) ).contains( "(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;" ) );
    assertTrue( "precondition: the caller invokes the Comparable erasure",
                classFileText( after.classFile.resolveSibling( "Caller.class" ) ).contains( "(Ljava/lang/Comparable;)Ljava/lang/Comparable;" ) );
    assertDifferentAbi( "A method type variable's bound is ABI: it decides the descriptor callers invoke", before, after );
  }

  // ---------------------------------------------------------------------------------------------
  // Default values baked by value
  // ---------------------------------------------------------------------------------------------

  /** Calls {@code Fixture.greet()} without its argument, so it bakes greet's default. */
  private static final String GREET_CALLER = "package p\n\nclass Caller {\n  function go() : String { return Fixture.greet() }\n}\n";

  @Test
  public void testDefaultValueFoldedFromAPrivateConstantIsAbi() throws IOException
  {
    // A default must be a compile-time constant, and a caller that omits the argument bakes its evaluated value
    // (DefaultArgLiteral), not its expression. Here the default names a private constant: its expression text, DEF,
    // does not move when DEF's value does, and DEF itself is private and so not on the surface. The preconditions
    // show the value, not a reference to DEF, is what the caller carries.
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static final var DEF : String = \"world\"\n" +
                     "  static function greet(name : String = DEF) : String { return \"hi \" + name }\n" +
                     "}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", GREET_CALLER );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "\"world\"", "\"there\"" ), "p/Caller.gs", GREET_CALLER );
    assertTrue( "precondition: the caller bakes the default's value",
                classFileText( before.classFile.resolveSibling( "Caller.class" ) ).contains( "world" ) );
    assertTrue( "precondition: the caller bakes the default's value",
                classFileText( after.classFile.resolveSibling( "Caller.class" ) ).contains( "there" ) );
    assertDifferentAbi( "A default's value is ABI, even when it is folded from a private constant", before, after );
  }

  @Test
  public void testDefaultValueFoldedFromAnotherTypesConstantIsAbi() throws IOException
  {
    // As above, with the default naming another type's constant: only Consts changes, the fixture's source and its
    // default's expression text, Consts.DEF, do not. Consts lists the fixture as a consumer, so the fixture is
    // recompiled when Consts changes; its hash has to move then, or the cascade stops before the caller that baked
    // the old value.
    String consts = "package p\n\nclass Consts {\n  public static final var DEF : String = \"world\"\n}\n";
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static function greet(name : String = Consts.DEF) : String { return \"hi \" + name }\n" +
                     "}\n";
    Compiled before = compile( "p/Consts.gs", consts, "p/Fixture.gs", fixture, "p/Caller.gs", GREET_CALLER );
    Compiled after = compile( "p/Consts.gs", consts.replace( "\"world\"", "\"there\"" ), "p/Fixture.gs", fixture, "p/Caller.gs", GREET_CALLER );
    assertTrue( "precondition: the caller bakes the default's value",
                classFileText( after.classFile.resolveSibling( "Caller.class" ) ).contains( "there" ) );
    assertTrue( "precondition: a change to Consts recompiles the fixture",
                consumersOf( after.depFile, "p.Consts" ).contains( "p.Fixture" ) );
    assertDifferentAbi( "A default's value is ABI, even when it is folded from another type's constant", before, after );
  }

  @Test
  public void testBlockTypeDefaultValuesAreAbi() throws IOException
  {
    // A block type can declare defaults of its own, and a caller that invokes a block omitting the argument bakes the
    // default exactly as it does for a method. The block type reaches the class file only as IFunction1<String,
    // String>, which carries neither its parameter names nor its defaults.
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static function greeter() : block(name : String = \"world\") : String { return \\ name -> \"hi \" + name }\n" +
                     "}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.greeter()() }\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "\"world\"", "\"there\"" ), "p/Caller.gs", caller );
    assertTrue( "precondition: the caller bakes the block type's default",
                classFileText( before.classFile.resolveSibling( "Caller.class" ) ).contains( "world" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two defaults apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "A block type's default values are ABI", before, after );
  }

  @Test
  public void testBlockTypeParameterNamesAreAbi() throws IOException
  {
    // Likewise a named argument to a block binds against the block type's parameter names. The caller compiles only
    // against the first spelling, so it is compiled alongside that one alone.
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static function greeter() : block(name : String) : String { return \\ name -> \"hi \" + name }\n" +
                     "}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.greeter()( :name = \"x\" ) }\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "block(name : String)", "block(who : String)" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two names apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "A block type's parameter names are ABI", before, after );
  }

  @Test
  public void testBlockTypeParameterTypesAreAbi() throws IOException
  {
    // A block parameter typed int and one typed Integer are written alike, as an IFunction1 whose generic signature
    // boxes the primitive. A caller's block literal compiles against the declared parameter type, so it is compiled
    // alongside the first spelling alone.
    String fixture = "package p\n\nclass Fixture {\n" +
                     "  static function run( b : block( x : int ) : int ) : int { return b( 1 ) }\n" +
                     "}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : int { return Fixture.run( \\ x -> x + 1 ) }\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "block( x : int )", "block( x : Integer )" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two types apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "A block type's parameter types are ABI", before, after );
  }

  @Test
  public void testDynamicTypeIsAbi() throws IOException
  {
    // dynamic.Dynamic is not a bytecode type: gosuc writes it as Object, so a member typed Dynamic and one typed
    // Object have the same descriptor. A caller that dispatches dynamically on the result compiles against the first
    // and not against the second, so it is compiled alongside the first alone.
    String fixture = "package p\n\nclass Fixture {\n  static function make() : dynamic.Dynamic { return \"x\" }\n}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : Object { return Fixture.make().length() }\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "dynamic.Dynamic", "Object" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two types apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "A member's Gosu type is ABI where its descriptor cannot tell it apart", before, after );
  }

  /** Two structures with nothing in common, so a caller binds to the members of one of them only. */
  private static final String[] SHAPES = {
    "p/Named.gs", "package p\n\nstructure Named {\n  function name() : String\n}\n",
    "p/Sized.gs", "package p\n\nstructure Sized {\n  function size() : int\n}\n" };

  /** {@link #SHAPES} followed by the given paths and sources. */
  private static String[] withShapes( String... pathsAndSources )
  {
    String[] all = Arrays.copyOf( SHAPES, SHAPES.length + pathsAndSources.length );
    System.arraycopy( pathsAndSources, 0, all, SHAPES.length, pathsAndSources.length );
    return all;
  }

  @Test
  public void testStructureReturnTypeIsAbi() throws IOException
  {
    // gosuc writes a structure-typed member as Object (GosuClassTransformer.getDescriptorNoStructures) and, the type
    // not being parameterized, with no generic signature, so retyping a return value from one structure to another
    // leaves the class file as it was. A caller calls the structure's members on the result, so it is compiled
    // alongside the first type alone. A class whose surface names a structure is not hashed: it reports a change
    // whenever it is recompiled, which covers the retyping.
    String fixture = "package p\n\nclass Fixture {\n  static function make() : Named { return null }\n}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.make().name() }\n}\n";
    Compiled before = compile( withShapes( "p/Fixture.gs", fixture, "p/Caller.gs", caller ) );
    Compiled after = compile( withShapes( "p/Fixture.gs", fixture.replace( ": Named", ": Sized" ) ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two types apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertStructural( "A class with a structure-typed return value", before, after );
  }

  @Test
  public void testStructurePropertyTypeIsAbi() throws IOException
  {
    // Likewise for a property.
    String fixture = "package p\n\nclass Fixture {\n  static property get Shape() : Named { return null }\n}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.Shape.name() }\n}\n";
    Compiled before = compile( withShapes( "p/Fixture.gs", fixture, "p/Caller.gs", caller ) );
    Compiled after = compile( withShapes( "p/Fixture.gs", fixture.replace( ": Named", ": Sized" ) ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two types apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertStructural( "A class with a structure-typed property", before, after );
  }

  @Test
  public void testClassNamingAStructureAnywhereOnItsSurfaceIsNotHashed() throws IOException
  {
    // Every way a class's consumer-visible surface can name a structure leaves the class unhashed, so that any
    // recompile of it reaches its consumers (see DependenciesClassVisitor.namesStructure).
    String[][] fixtures = {
      { "a method parameter", "class Fixture {\n  function take( x : Named ) {}\n}\n" },
      { "a method's type variable bound", "class Fixture {\n  function take<T extends Named>( x : T ) {}\n}\n" },
      { "a property", "class Fixture {\n  property get Shape() : Named { return null }\n}\n" },
      { "a constructor parameter", "class Fixture {\n  construct( x : Named ) {}\n}\n" },
      { "a class type variable bound", "class Fixture<T extends Named> {}\n" },
      { "a type argument", "class Fixture {\n  function all() : List<Named> { return null }\n}\n" },
      { "a block type", "class Fixture {\n  function each( b : block( n : Named ) ) {}\n}\n" },
      { "an inherited member", "class Fixture extends Base {}\n" } };
    String base = "package p\n\nclass Base {\n  function take( x : Named ) {}\n}\n";
    for( String[] fixture : fixtures )
    {
      Compiled compiled = compile( withShapes( "p/Fixture.gs", "package p\n\nuses java.util.List\n\n" + fixture[1],
                                               "p/Base.gs", base ) );
      assertEquals( "A class naming a structure through " + fixture[0] + " is not hashed",
                    IncrementalCompilationManager.CANNOT_COMPUTE_ABI_HASH, compiled.hash );
    }
    assertEquals( "A structure is not hashed", IncrementalCompilationManager.CANNOT_COMPUTE_ABI_HASH,
                  compile( "p/Fixture.gs", "package p\n\nstructure Fixture {\n  function name() : String\n}\n" ).hash );
  }

  @Test
  public void testStructureNamedOnlyInABodyOrAPrivateMemberIsHashed() throws IOException
  {
    // Neither a method body nor a private member is on the consumer-visible surface, so naming a structure there leaves
    // the class hashed like any other: its consumers compile against nothing that depends on the structure.
    String[][] fixtures = {
      { "a method body", "class Fixture {\n  function go() : Object { var n : Named = null\n return n }\n}\n" },
      { "a private method", "class Fixture {\n  private function take( x : Named ) {}\n}\n" },
      { "a private field", "class Fixture {\n  var _n : Named\n}\n" } };
    for( String[] fixture : fixtures )
    {
      Compiled compiled = compile( withShapes( "p/Fixture.gs", "package p\n\n" + fixture[1] ) );
      assertTrue( "A class naming a structure only in " + fixture[0] + " is hashed: " + compiled.hash,
                  compiled.hash.matches( "[0-9a-f]{40}" ) );
    }
  }

  @Test
  public void testPropertyVersusGetterFunctionIsAbi() throws IOException
  {
    // A property and a getter function compile to the same getName() method, but a caller can read Name only from the
    // property, so it is compiled alongside the first spelling alone.
    String fixture = "package p\n\nclass Fixture {\n  static property get Name() : String { return \"x\" }\n}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return Fixture.Name }\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "property get Name()", "function getName()" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "Whether a member is a property or a function is ABI", before, after );
  }

  @Test
  public void testEnhancedTypeArgumentsAreAbi() throws IOException
  {
    // An enhancement's methods are static, taking the enhanced type as an implicit first parameter that the generic
    // signature renders raw, so the enhanced type's arguments do not reach the class file. A caller resolves the
    // enhancement only on a matching receiver, so it is compiled alongside the first spelling alone.
    String box = "package p\n\nclass Box<T> {}\n";
    String fixture = "package p\n\nenhancement Fixture : Box<Integer> {\n  function total() : int { return 0 }\n}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : int { return new Box<Integer>().total() }\n}\n";
    Compiled before = compile( "p/Box.gs", box, "p/Fixture.gsx", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Box.gs", box, "p/Fixture.gsx", fixture.replace( "Box<Integer>", "Box<String>" ) );
    assertEquals( "precondition: the fixture's method descriptors and signatures do not tell the two apart",
                  methodSignatures( readClass( before.classFile ) ), methodSignatures( readClass( after.classFile ) ) );
    assertDifferentAbi( "An enhancement's enhanced type, with its arguments, is ABI", before, after );
  }

  @Test
  public void testStructureVersusInterfaceIsAbi() throws IOException
  {
    // Declaring a type a structure rather than an interface changes how every consumer compiles against it: a
    // member typed with a structure is written as Object, which the preconditions show on Taker. Unless the
    // fixture's hash moves, Taker is not recompiled and keeps take(Fixture), while a caller compiled against the
    // new source invokes take(Object).
    String fixture = "package p\n\ninterface Fixture {\n  function foo() : int\n}\n";
    String taker = "package p\n\nclass Taker {\n  static function take( f : Fixture ) {}\n}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Taker.gs", taker );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "interface Fixture", "structure Fixture" ), "p/Taker.gs", taker );
    assertTrue( "precondition: an interface-typed parameter is written as the interface",
                methodSignatures( readClass( before.classFile.resolveSibling( "Taker.class" ) ) ).contains( "take(Lp/Fixture;)V null" ) );
    assertTrue( "precondition: a structure-typed parameter is written as Object",
                methodSignatures( readClass( after.classFile.resolveSibling( "Taker.class" ) ) ).contains( "take(Ljava/lang/Object;)V null" ) );
    assertDifferentAbi( "Whether a type is a structure is ABI", before, after );
  }

  @Test
  public void testProtectedMemberClassIsAbi() throws IOException
  {
    // gosuc writes a protected member class as public; Gosu still forbids naming it outside a subclass. A consumer
    // naming Fixture.Inner consumes Fixture$Inner, and the walk gives Fixture$Inner its own turn when Fixture's
    // source is recompiled, so it is Inner's own hash that has to move.
    Compiled before = compileBody( "  public static class Inner {}\n" );
    Compiled after = compileBody( "  protected static class Inner {}\n" );
    String innerBefore = abiHashOf( before.depFile, "p.Fixture$Inner" );
    assertNotEquals( "A member class's protected access is ABI (both hashed to " + innerBefore + ")",
                     innerBefore, abiHashOf( after.depFile, "p.Fixture$Inner" ) );
  }

  @Test
  public void testProtectedEnhancementMethodIsAbi() throws IOException
  {
    // gosuc writes a protected enhancement method as public; Gosu still forbids calling it from an unrelated class.
    String person = "package p\n\nclass Person {}\n";
    String fixture = "package p\n\nenhancement Fixture : Person {\n  function greet() : String { return \"hi\" }\n}\n";
    assertDifferentAbi( "An enhancement method's protected access is ABI",
                        compile( "p/Person.gs", person, "p/Fixture.gsx", fixture ),
                        compile( "p/Person.gs", person, "p/Fixture.gsx", fixture.replace( "function greet", "protected function greet" ) ) );
  }

  @Test
  public void testDeclarationSiteVarianceIsAbi() throws IOException
  {
    // A type variable declared out (or in) changes which parameterizations of the type a caller may assign to one
    // another, but the class signature has no notion of variance. The caller compiles against the first spelling
    // alone.
    String fixture = "package p\n\nclass Fixture<out T> {\n  function get() : T { return null }\n}\n";
    String caller = "package p\n\nclass Caller {\n" +
                    "  function go( f : Fixture<String> ) : Fixture<CharSequence> { return f }\n" +
                    "}\n";
    Compiled before = compile( "p/Fixture.gs", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Fixture.gs", fixture.replace( "<out T>", "<T>" ) );
    assertEquals( "precondition: the fixture's class signature does not tell the two apart",
                  readClass( before.classFile ).signature, readClass( after.classFile ).signature );
    assertDifferentAbi( "A type variable's declared variance is ABI", before, after );
  }

  @Test
  public void testEnhancementDefaultParameterValuesAreAbi() throws IOException
  {
    // An enhancement's methods are called like members of the enhanced type, and a call that omits an argument bakes
    // the default as it does for a class.
    String person = "package p\n\nclass Person {}\n";
    String fixture = "package p\n\nenhancement Fixture : Person {\n" +
                     "  function greet(name : String = \"world\") : String { return \"hi \" + name }\n" +
                     "}\n";
    String caller = "package p\n\nclass Caller {\n  function go() : String { return new Person().greet() }\n}\n";
    Compiled before = compile( "p/Person.gs", person, "p/Fixture.gsx", fixture, "p/Caller.gs", caller );
    Compiled after = compile( "p/Person.gs", person, "p/Fixture.gsx", fixture.replace( "\"world\"", "\"there\"" ), "p/Caller.gs", caller );
    assertTrue( "precondition: the caller bakes the default",
                classFileText( before.classFile.resolveSibling( "Caller.class" ) ).contains( "world" ) );
    assertDifferentAbi( "An enhancement method's default values are ABI", before, after );
  }

  // ---------------------------------------------------------------------------------------------
  // Annotation arguments
  // ---------------------------------------------------------------------------------------------

  @Test
  public void testAnnotationArrayElementOrderIsAbi() throws IOException
  {
    // The elements of an array-valued argument are part of its value: {"b", "a"} and {"a", "b"} are different
    // arguments. gosuc writes the elements in source order, which the preconditions check, and the hasher keeps that
    // order rather than sorting it away.
    String ba = "package p\n\n@Tag({\"b\", \"a\"})\nclass Fixture {}\n";
    Compiled before = compile( "p/Tag.gs", TAG_ARRAY_ANNOTATION, "p/Fixture.gs", ba );
    Compiled after = compile( "p/Tag.gs", TAG_ARRAY_ANNOTATION, "p/Fixture.gs", ba.replace( "{\"b\", \"a\"}", "{\"a\", \"b\"}" ) );
    assertEquals( "precondition: gosuc writes the elements in source order",
                  Arrays.asList( "b", "a" ), annotationMember( readClass( before.classFile ), "Lp/Tag;", "value" ) );
    assertEquals( "precondition: gosuc writes the elements in source order",
                  Arrays.asList( "a", "b" ), annotationMember( readClass( after.classFile ), "Lp/Tag;", "value" ) );
    assertDifferentAbi( "The order of an annotation array's elements is ABI", before, after );
  }

  @Test
  public void testAnnotationArrayElementMultiplicityIsAbi() throws IOException
  {
    // Likewise {"a", "a"} and {"a"}: a set would collapse them into the same text.
    String aa = "package p\n\n@Tag({\"a\", \"a\"})\nclass Fixture {}\n";
    Compiled before = compile( "p/Tag.gs", TAG_ARRAY_ANNOTATION, "p/Fixture.gs", aa );
    Compiled after = compile( "p/Tag.gs", TAG_ARRAY_ANNOTATION, "p/Fixture.gs", aa.replace( "{\"a\", \"a\"}", "{\"a\"}" ) );
    assertEquals( "precondition: gosuc writes every element",
                  Arrays.asList( "a", "a" ), annotationMember( readClass( before.classFile ), "Lp/Tag;", "value" ) );
    assertEquals( "precondition: gosuc writes every element",
                  Arrays.asList( "a" ), annotationMember( readClass( after.classFile ), "Lp/Tag;", "value" ) );
    assertDifferentAbi( "The number of an annotation array's elements is ABI", before, after );
  }

  @Test
  public void testAnnotationArgumentOrderIsNotAbi() throws IOException
  {
    // Named arguments are unordered. gosuc writes an annotation's members in the order its type declares them and
    // looks each value up by name, so the usage site's order never reaches the class file and the two spellings hash
    // the same whichever way the hasher orders named members. This pins that property of the annotation writer.
    Compiled before = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", COLUMN_USAGE );
    Compiled after = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs",
                              COLUMN_USAGE.replace( ":name = \"username\", :email = \"a@bar.com\"",
                                                    ":email = \"a@bar.com\", :name = \"username\"" ) );
    ClassNode swapped = readClass( after.classFile );
    assertEquals( "precondition: both members reach the class file", "username", annotationMember( swapped, "Lp/Column;", "name" ) );
    assertEquals( "precondition: both members reach the class file", "a@bar.com", annotationMember( swapped, "Lp/Column;", "email" ) );
    assertSameAbi( "The order of an annotation's named arguments is not ABI", before, after );
  }

  @Test
  public void testAnnotationMemberDeclarationOrderIsNotAbi() throws IOException
  {
    // Nor is the order in which the annotation type declares its members: the two Fixtures below carry the same
    // name and email values. gosuc writes the members in declaration order, which the preconditions check, so this
    // is the case where the hasher itself has to sort named members for the two to hash the same.
    Compiled before = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", COLUMN_USAGE );
    Compiled after = compile( "p/Column.gs", COLUMN_ANNOTATION_EMAIL_FIRST, "p/Fixture.gs", COLUMN_USAGE );
    assertEquals( "precondition: gosuc writes the members in the annotation type's declaration order",
                  Arrays.asList( "name", "email" ), annotationMemberNames( readClass( before.classFile ), "Lp/Column;" ) );
    assertEquals( "precondition: gosuc writes the members in the annotation type's declaration order",
                  Arrays.asList( "email", "name" ), annotationMemberNames( readClass( after.classFile ), "Lp/Column;" ) );
    assertSameAbi( "The declaration order of an annotation type's members is not ABI", before, after );
  }

  @Test
  public void testAnnotationMemberDeclarationOrderIsTheAnnotationTypesOwnAbi() throws IOException
  {
    // The mirror image: the same reorder is an ABI change for Column itself, because a positional usage such as
    // @Column("username", "a@bar.com") binds by member order. gosuc exposes the members as the parameters of the
    // annotation type's standard constructor, in declaration order, and parameter names are hashed.
    assertNotEquals( "precondition: the two declarations differ", COLUMN_ANNOTATION, COLUMN_ANNOTATION_EMAIL_FIRST );
    String before = abiHashOf( compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", COLUMN_USAGE ).depFile, "p.Column" );
    String after = abiHashOf( compile( "p/Column.gs", COLUMN_ANNOTATION_EMAIL_FIRST, "p/Fixture.gs", COLUMN_USAGE ).depFile, "p.Column" );
    assertNotEquals( "The declaration order of an annotation type's members is the type's own ABI (both hashed to " + before + ")",
                     before, after );
  }

  @Test
  public void testPositionalAnnotationArgumentsBindByMemberOrder() throws IOException
  {
    // Why the previous test holds: a positional usage binds its arguments to the members in declaration order, so
    // the same @Column("username", "a@bar.com") means name = "username" under one declaration and
    // name = "a@bar.com" under the other. The fixture's annotation values change with it, and so does its ABI,
    // where the named usage of testAnnotationMemberDeclarationOrderIsNotAbi kept both the same.
    Compiled named = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", COLUMN_USAGE );
    Compiled before = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", COLUMN_POSITIONAL_USAGE );
    Compiled after = compile( "p/Column.gs", COLUMN_ANNOTATION_EMAIL_FIRST, "p/Fixture.gs", COLUMN_POSITIONAL_USAGE );
    ClassNode nameFirst = readClass( before.classFile );
    assertEquals( "precondition: positional arguments bind in declaration order", "username", annotationMember( nameFirst, "Lp/Column;", "name" ) );
    assertEquals( "precondition: positional arguments bind in declaration order", "a@bar.com", annotationMember( nameFirst, "Lp/Column;", "email" ) );
    ClassNode emailFirst = readClass( after.classFile );
    assertEquals( "precondition: the reorder rebinds the positional arguments", "a@bar.com", annotationMember( emailFirst, "Lp/Column;", "name" ) );
    assertEquals( "precondition: the reorder rebinds the positional arguments", "username", annotationMember( emailFirst, "Lp/Column;", "email" ) );
    assertSameAbi( "Positional and named spellings of the same values are not ABI", named, before );
    assertDifferentAbi( "Reordering the members changes what a positional usage says, so the fixture's ABI moves", before, after );
  }

  @Test
  public void testAnnotationMemberDefaultFoldedFromAnotherTypesConstantIsAbi() throws IOException
  {
    // gosuc bakes an annotation member's default into every user that omits the member, so the default's value is
    // part of the annotation type's ABI. The constructor line on the Gosu surface renders the default's expression
    // text, which does not move when that expression folds a constant declared elsewhere whose value changed; the
    // AnnotationDefault attribute carries the folded value, and the hasher reads it there.
    String consts = "package p\n\nclass Consts {\n  public static final var K : int = 4\n}\n";
    String tag =
      "package p\n" +
      "uses java.lang.annotation.ElementType\n" +
      "uses java.lang.annotation.Target\n" +
      "uses java.lang.annotation.Retention\n" +
      "uses java.lang.annotation.RetentionPolicy\n" +
      "\n" +
      "@Target({ElementType.TYPE})\n" +
      "@Retention(RetentionPolicy.RUNTIME)\n" +
      "annotation Tag {\n" +
      "  function value() : int = Consts.K\n" +
      "}\n";
    String usage = "package p\n\n@Tag\nclass Fixture {}\n";
    Compiled before = compile( "p/Consts.gs", consts, "p/Tag.gs", tag, "p/Fixture.gs", usage );
    Compiled after = compile( "p/Consts.gs", consts.replace( "= 4", "= 5" ), "p/Tag.gs", tag, "p/Fixture.gs", usage );
    assertDifferentAbi( "precondition: the default is baked into the fixture that omits the member", before, after );
    String tagBefore = abiHashOf( before.depFile, "p.Tag" );
    assertNotEquals( "An annotation member's default value is the annotation type's ABI (both hashed to " + tagBefore + ")",
                     tagBefore, abiHashOf( after.depFile, "p.Tag" ) );
  }

  // ---------------------------------------------------------------------------------------------
  // Separators in the canonical text
  // ---------------------------------------------------------------------------------------------

  @Test
  public void testAnnotationArgumentValueContainingTheMemberSeparatorIsAbi() throws IOException
  {
    // The hasher renders an annotation's named members as "<name> <value>" and joins them with an unescaped comma,
    // so a value holding ",<member> " impersonates that join. Both fixtures below render
    //   [email a,name b,name c,]
    // while binding different values for both members, which a consumer reading either one observes.
    String usage = "package p\n\n@Column(:name = \"b,name c\", :email = \"a\")\nclass Fixture {}\n";
    String shifted = "package p\n\n@Column(:name = \"c\", :email = \"a,name b\")\nclass Fixture {}\n";
    Compiled before = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", usage );
    Compiled after = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", shifted );
    assertEquals( "precondition: the two fixtures bind different values", "b,name c",
                  annotationMember( readClass( before.classFile ), "Lp/Column;", "name" ) );
    assertEquals( "precondition: the two fixtures bind different values", "c",
                  annotationMember( readClass( after.classFile ), "Lp/Column;", "name" ) );
    assertDifferentAbi( "An annotation argument whose value contains the member separator is still ABI",
                        before, after );
  }

  @Test
  public void testAnnotationArgumentValueContainingAQuoteIsAbi() throws IOException
  {
    // The guard on the fix for the case above. The hasher renders a member as "<name> '<value>'", quoting the
    // value but not the name, so the sequence a value must hold to impersonate the join is "',name '": a closing
    // quote, the comma, the next member's bare name, its space and its opening quote. Quoting a value without
    // escaping a quote inside it leaves both fixtures below rendering
    //   [email 'a',name 'b',name 'c',]
    // so a quote within a value has to be escaped, not merely surrounded.
    String usage = "package p\n\n@Column(:name = \"b',name 'c\", :email = \"a\")\nclass Fixture {}\n";
    String shifted = "package p\n\n@Column(:name = \"c\", :email = \"a',name 'b\")\nclass Fixture {}\n";
    Compiled before = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", usage );
    Compiled after = compile( "p/Column.gs", COLUMN_ANNOTATION, "p/Fixture.gs", shifted );
    assertEquals( "precondition: the two fixtures bind different values", "b',name 'c",
                  annotationMember( readClass( before.classFile ), "Lp/Column;", "name" ) );
    assertEquals( "precondition: the two fixtures bind different values", "c",
                  annotationMember( readClass( after.classFile ), "Lp/Column;", "name" ) );
    assertDifferentAbi( "An annotation argument whose value contains a quote is still ABI", before, after );
  }

  @Test
  public void testConstantValueContainingTheLineSeparatorIsAbi() throws IOException
  {
    // The Gosu compile-time surface renders one "const <name> : <type> = <value>" line per constant and joins the
    // lines with an unescaped newline, so a value holding that prefix impersonates the join. Both fixtures below
    // render
    //   const A : java.lang.String = p\nconst B : java.lang.String = q\nconst B : java.lang.String = r\n
    // while binding different values for A and B. gosuc writes no ConstantValue attribute for either constant, so
    // the field entries are identical and cannot mask the collision. The crafted values spell the type the way the
    // line does, as the feature type's full name.
    String body = "  public static final var A : String = \"p\"\n" +
                  "  public static final var B : String = \"q\\nconst B : java.lang.String = r\"\n";
    String shifted = "  public static final var A : String = \"p\\nconst B : java.lang.String = q\"\n" +
                     "  public static final var B : String = \"r\"\n";
    assertDifferentAbi( "A constant whose value contains the line separator is still ABI",
                        compileBody( body ), compileBody( shifted ) );
  }

  // ---------------------------------------------------------------------------------------------
  // Interface declaration order
  // ---------------------------------------------------------------------------------------------

  @Test
  public void testInterfaceDeclarationOrderIsAbi() throws IOException
  {
    // Gosu resolves an inherited member by merging each interface in getInterfaces() order and keeping the first
    // match -- FeatureManager.mergeProperty through shouldReplace, and mergeMethod with replace=false -- and
    // getInterfaces() is the declaration order the parser appends in (GosuClass.addInterface). Fixture below
    // inherits Foo from both A, as Object, and B, as String, and declares neither, so the order of its extends
    // clause alone decides the property's type and Consumer bakes that type into its own bytecode. The
    // preconditions are the proof that the two orders are not interchangeable -- they name the property each
    // order resolves to, read back off the descriptor Consumer gave its own inferred field -- and the assertion
    // is the requirement that follows from it, which sorting the interfaces into the hash text would defeat.
    String a = "package p\n\ninterface A {\n  property get Foo() : Object\n}\n";
    String b = "package p\n\ninterface B {\n  property get Foo() : String\n}\n";
    String consumer = "package p\n\nclass Consumer {\n" +
                      "  static var x = make().Foo\n" +
                      "  static function make() : Fixture { return null }\n" +
                      "}\n";
    Compiled before = compile( "p/A.gs", a, "p/B.gs", b,
                               "p/Fixture.gs", "package p\n\ninterface Fixture extends A, B {}\n",
                               "p/Consumer.gs", consumer );
    Compiled after = compile( "p/A.gs", a, "p/B.gs", b,
                              "p/Fixture.gs", "package p\n\ninterface Fixture extends B, A {}\n",
                              "p/Consumer.gs", consumer );
    assertEquals( "precondition: with A declared first, Foo resolves to A's Object-typed property",
                  "Ljava/lang/Object;",
                  fieldDescriptor( readClass( before.classFile.resolveSibling( "Consumer.class" ) ), "x" ) );
    assertEquals( "precondition: with B declared first, Foo resolves to B's String-typed property",
                  "Ljava/lang/String;",
                  fieldDescriptor( readClass( after.classFile.resolveSibling( "Consumer.class" ) ), "x" ) );
    assertDifferentAbi( "The order an interface declares its supertypes in is ABI", before, after );
  }
}
