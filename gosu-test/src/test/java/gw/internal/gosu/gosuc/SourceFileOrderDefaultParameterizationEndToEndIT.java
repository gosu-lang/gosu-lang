/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

/**
 * End-to-end test that the type arguments gosuc gives a generic type that is referenced without any, its default
 * parameterization, do not depend on the order in which the source files are handed to the compiler.
 */
public class SourceFileOrderDefaultParameterizationEndToEndIT extends SourceFileOrderEndToEndTestBase
{
  /**
   * {@code Outer<T extends Mid>} where {@code Mid<T extends Leaf>} is generic in turn, and a method of
   * {@code Mid} that returns {@code Outer} without type arguments.
   * <p>
   * The uses statement makes the compiler parse the header of {@code Outer} from the header of {@code Mid}.
   * Compiled first, {@code Mid} is at its uses statement and has no type variables yet, so {@code Outer} takes
   * the raw {@code Mid} as the bound of its {@code T}, and {@code TypeLord.makeDefaultParameterizedType} makes
   * the return type of {@code outer()} {@code Outer<Mid>}. Compiled after {@code Outer}, the header of
   * {@code Mid} is complete when the bound is resolved, the bound is {@code Mid<Leaf>} and the return type is
   * {@code Outer<Mid<Leaf>>}. The difference is in the generic signature of the method.
   * <p>
   * It is also in what compiles. {@code TheMid} of an {@code Outer<Mid<Leaf>>} is a {@code Mid<Leaf>}, whose
   * {@code accept} takes a {@code Leaf}. {@code TheMid} of an {@code Outer<Mid>} is the raw {@code Mid}, whose
   * {@code accept} takes the type variable {@code T} of {@code Mid}: the call in {@code feed} is an error,
   * "The type example.Leaf cannot be converted to example.Mid.T", and {@code Mid} does not compile when it is
   * compiled first.
   */
  @Test
  public void testDefaultParameterizationOfATypeWithAGenericBoundDoesNotDependOnSourceFileOrder() throws Exception
  {
    createSourceFile( "example/Leaf.gs",
                      "package example\n" +
                      "\n" +
                      "class Leaf {\n" +
                      "}\n" );

    File mid = createSourceFile( "example/Mid.gs",
                                 "package example\n" +
                                 "\n" +
                                 "uses example.Outer\n" +
                                 "\n" +
                                 "class Mid<T extends Leaf> {\n" +
                                 "  function outer() : Outer {\n" +
                                 "    return null\n" +
                                 "  }\n" +
                                 "\n" +
                                 "  function accept( leaf : T ) {\n" +
                                 "  }\n" +
                                 "\n" +
                                 "  function feed( leaf : Leaf ) {\n" +
                                 "    outer().TheMid.accept( leaf )\n" +
                                 "  }\n" +
                                 "}\n" );

    File outer = createSourceFile( "example/Outer.gs",
                                   "package example\n" +
                                   "\n" +
                                   "class Outer<T extends Mid> {\n" +
                                   "  property get TheMid() : T {\n" +
                                   "    return null\n" +
                                   "  }\n" +
                                   "}\n" );

    Path outerFirst = compile( "outer-first", outer, mid );
    Path midFirst = compile( "mid-first", mid, outer );

    assertSameClassFile( "example/Mid.class", midFirst, outerFirst );
    assertSameClassFile( "example/Outer.class", midFirst, outerFirst );
  }
}
