package gw.internal.gosu.incremental;

import gw.internal.ext.com.google.gson.JsonObject;
import gw.internal.ext.com.google.gson.JsonParser;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.Opcodes;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the ABI hash gosuc records for a compiled Gosu class: it must be blind to
 * everything a separately compiled consumer cannot observe, and sensitive to everything it can.
 * Each test compiles a small {@code p.Fixture} with gosuc in incremental mode into a fresh
 * directory and reads the class's hash from the dependency file, so the hash under test is the
 * one the incremental driver compares: bytecode surface plus the Gosu compile-time surface
 * (constant values, parameter names, default parameter values) that never reaches the class file.
 * Member visibility is judged the way gosuc emits it: a Gosu-private member is written
 * package-private and is not ABI, a member declared {@code internal} is package-private plus
 * {@code @gw.lang.ir.Internal} and is ABI.
 */
public class AbiHashTest
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

  private static final String BASE =
    "  public static final var LIMIT : int = 3\n" +
    "  public var count : int\n" +
    "  var _secret : int = 1\n" +
    "  function names() : List<String> { return new java.util.ArrayList<String>() }\n" +
    "  protected function touch() { _secret++ }\n" +
    "  private function hidden() {}\n";

  /** The output of one gosuc run: the fixture's hash and its class file. */
  private static final class Compiled
  {
    final String hash;
    final Path classFile;

    Compiled( String hash, Path classFile )
    {
      this.hash = hash;
      this.classFile = classFile;
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

    JsonObject depGraph = JsonParser.parseString( Files.readString( depFile ) ).getAsJsonObject().getAsJsonObject( "dep_graph" );
    String hash = depGraph.getAsJsonObject( "p.Fixture" ).get( "abi_hash" ).getAsString();
    return new Compiled( hash, outDir.resolve( "p/Fixture.class" ) );
  }

  private static void assertSameAbi( String why, Compiled before, Compiled after )
  {
    assertEquals( why, before.hash, after.hash );
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
}
