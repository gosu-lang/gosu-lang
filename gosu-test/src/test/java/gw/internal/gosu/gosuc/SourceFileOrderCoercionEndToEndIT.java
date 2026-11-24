/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * End-to-end test that the code gosuc compiles for a value that {@code typeis} has narrowed, and for the cast
 * of that value to its type variable, does not depend on the order in which the source files are handed to the
 * compiler.
 */
public class SourceFileOrderCoercionEndToEndIT extends SourceFileOrderEndToEndTestBase
{
  /**
   * Two classes that have nothing in common but the name of a type variable: the {@code T} of {@code Cell} is a
   * {@code Runnable}, the {@code T} of {@code Minimizer.minimize} is a {@code Comparable}. Each narrows a value
   * of its own {@code T} with {@code typeis Date}, and {@code Minimizer} casts the narrowed value back with
   * {@code item as T}.
   * <p>
   * Inside the {@code if} the type of {@code item} is the compound type {@code T & java.util.Date}. Compound
   * types are cached by name and every type variable named T has the name "T", so the class compiled second is
   * handed the compound type made from the {@code T} of the class compiled first. For {@code Minimizer} that
   * decides two things. Compiled first, {@code item} is a value of its own {@code T}: it is read with a
   * {@code checkcast} to {@code Comparable}, and {@code StandardCoercionManager.resolveCoercerStatically} picks
   * no coercer for the cast. Compiled after {@code Cell}, {@code item} is a value of the {@code T} of
   * {@code Cell}: it is read with a {@code checkcast} to {@code Runnable}, and the cast gets
   * {@code TypeVariableCoercer}.
   * <p>
   * A {@code Date} is not a {@code Runnable}, so {@code minimize} of the class file that was compiled after
   * {@code Cell} throws a {@code ClassCastException} when it is called with one. The {@code checkcast} comes from
   * the compound type that {@code CompoundType.get} returns from its cache, not from the coercer: picking the
   * same coercer in both orders does not remove it.
   */
  @Test
  public void testCastOfNarrowedValueToItsTypeVariableDoesNotDependOnSourceFileOrder() throws Exception
  {
    File cell = createSourceFile( "example/Cell.gs",
                                  "package example\n" +
                                  "\n" +
                                  "uses java.util.Date\n" +
                                  "\n" +
                                  "class Cell<T extends Runnable> {\n" +
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
                                       "  reified function minimize<T extends Comparable>( item : T ) : T {\n" +
                                       "    if( item typeis Date ) {\n" +
                                       "      return item as T\n" +
                                       "    }\n" +
                                       "    return item\n" +
                                       "  }\n" +
                                       "\n" +
                                       "  static function main( args : String[] ) {\n" +
                                       "    print( new Minimizer().minimize( new Date( 0 ) ).Time )\n" +
                                       "  }\n" +
                                       "}\n" );

    Path cellFirst = compile( "cell-first", cell, minimizer );
    Path minimizerFirst = compile( "minimizer-first", minimizer, cell );

    assertEquals( "0", run( minimizerFirst, "example.Minimizer" ) );
    assertEquals( "0", run( cellFirst, "example.Minimizer" ) );

    assertSameClassFile( "example/Minimizer.class", cellFirst, minimizerFirst );
    assertSameClassFile( "example/Cell.class", cellFirst, minimizerFirst );
  }
}
