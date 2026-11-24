/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * End-to-end test that the cast gosuc compiles around an argument of a Java type does not depend on the order
 * in which the source files are handed to the compiler.
 */
public class SourceFileOrderArgumentCastEndToEndIT extends SourceFileOrderEndToEndTestBase
{
  /**
   * A Gosu class {@code Derived} whose Gosu superclass {@code Base} extends the Java class
   * {@code VisibleAncestor}, and which passes itself to a Java method that takes a {@code HiddenAncestor}, the
   * superclass of {@code VisibleAncestor}. {@code HiddenAncestor} is not public.
   * <p>
   * The uses statement makes the compiler ask for the type hierarchy of {@code Derived} from the header of
   * {@code Base}. Compiled first, {@code Base} is at its uses statement and has no supertype yet, so the
   * hierarchy that is cached for {@code Derived} ends at {@code Base} and has none of its Java ancestors.
   * {@code JavaClassIRType.isAssignableFrom} looks for {@code HiddenAncestor} in that hierarchy, does not find
   * it, and {@code this} is passed with a {@code checkcast} to {@code HiddenAncestor}. Compiled after
   * {@code Derived}, {@code Base} is complete when the hierarchy is computed and there is no cast. Without the
   * type variables there is no cast in either order.
   * <p>
   * The cast is more than superfluous. Passing the argument needs no access to the class of the parameter, the
   * cast does: {@code Derived} is in another package than {@code HiddenAncestor}, so the function that has the
   * cast throws an {@code IllegalAccessError} when it is called.
   */
  @Test
  public void testCastOfGosuArgumentToItsJavaAncestorDoesNotDependOnSourceFileOrder() throws Exception
  {
    File base = createSourceFile( "example/Base.gs",
                                  "package example\n" +
                                  "\n" +
                                  "uses example.Derived\n" +
                                  "uses gw.internal.gosu.gosuc.argumentcast.VisibleAncestor\n" +
                                  "\n" +
                                  "class Base<T> extends VisibleAncestor {\n" +
                                  "}\n" );

    File derived = createSourceFile( "example/Derived.gs",
                                     "package example\n" +
                                     "\n" +
                                     "uses gw.internal.gosu.gosuc.argumentcast.VisibleAncestor\n" +
                                     "\n" +
                                     "class Derived<T> extends Base<T> {\n" +
                                     "  function name() : String {\n" +
                                     "    return VisibleAncestor.nameOf( this )\n" +
                                     "  }\n" +
                                     "\n" +
                                     "  static function main( args : String[] ) {\n" +
                                     "    print( new Derived<String>().name() )\n" +
                                     "  }\n" +
                                     "}\n" );

    Path baseFirst = compile( "base-first", base, derived );
    Path derivedFirst = compile( "derived-first", derived, base );

    assertEquals( "Derived", run( derivedFirst, "example.Derived" ) );
    assertEquals( "Derived", run( baseFirst, "example.Derived" ) );

    assertSameClassFile( "example/Derived.class", baseFirst, derivedFirst );
    assertSameClassFile( "example/Base.class", baseFirst, derivedFirst );
  }
}
