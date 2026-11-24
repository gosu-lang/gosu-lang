/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

/**
 * End-to-end test that the coercer gosuc compiles into a cast to a type variable does not depend on the order
 * in which the source files are handed to the compiler.
 */
public class SourceFileOrderTypeVariableCoercerEndToEndIT extends SourceFileOrderEndToEndTestBase
{
  /**
   * Two classes that have nothing in common but the name of a type variable. Each narrows a value of its own
   * {@code T} with {@code typeis Date}, and {@code Minimizer} casts the narrowed value back with
   * {@code item as T}.
   * <p>
   * Inside the {@code if} the type of {@code item} is the compound type {@code T & java.util.Date}. Compound
   * types are cached by name and every type variable named T has the name "T", so the class compiled second is
   * handed the compound type made from the {@code T} of the class compiled first. For {@code Minimizer} that
   * decides the coercer {@code StandardCoercionManager.resolveCoercerStatically} picks for the cast: compiled
   * first, {@code item} is a value of its own {@code T} and the cast gets no coercer; compiled after
   * {@code Cell}, {@code item} is a value of the {@code T} of {@code Cell} and the cast gets
   * {@code TypeVariableCoercer}.
   * <p>
   * The difference is in the class file only. The value is a {@code T} in both orders, and
   * {@code TypeVariableCoercer} returns such a value as it is, so both class files compile and run alike.
   */
  @Test
  public void testCoercerOfCastOfNarrowedValueToItsTypeVariableDoesNotDependOnSourceFileOrder() throws Exception
  {
    File cell = createSourceFile( "example/Cell.gs",
                                  "package example\n" +
                                  "\n" +
                                  "uses java.util.Date\n" +
                                  "\n" +
                                  "class Cell<T> {\n" +
                                  "  function isDate( value : T ) : boolean {\n" +
                                  "    return value typeis Date\n" +
                                  "  }\n" +
                                  "}\n" );

    File minimizer = createSourceFile( "example/Minimizer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "uses java.util.Date\n" +
                                       "\n" +
                                       "class Minimizer {\n" +
                                       "  reified function minimize<T>( item : T ) : T {\n" +
                                       "    if( item typeis Date ) {\n" +
                                       "      return item as T\n" +
                                       "    }\n" +
                                       "    return item\n" +
                                       "  }\n" +
                                       "}\n" );

    Path cellFirst = compile( "cell-first", cell, minimizer );
    Path minimizerFirst = compile( "minimizer-first", minimizer, cell );

    assertSameClassFile( "example/Cell.class", cellFirst, minimizerFirst );
    assertSameClassFile( "example/Minimizer.class", cellFirst, minimizerFirst );
  }
}
