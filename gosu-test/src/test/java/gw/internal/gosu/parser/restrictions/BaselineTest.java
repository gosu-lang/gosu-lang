package gw.internal.gosu.parser.restrictions;

import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.restrictions.Restrictions;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class BaselineTest extends TestClass
{
  public void testBaselineAllowedTypes() throws ParseResultsException
  {
    ParserOptions options = baselineRestrictions();

//      .allowPackage( "java.lang" )
//      .allowPackage( "java.math" )
//      .allowPackage( "java.util" )
//      .allowPackage( "java.util.function" )
//      .allowPackage( "java.util.stream" )
//      .allowPackage( "java.util.regex" )
//      .allowPackage( "java.text" )
//      .allowPackageTree( "java.time" )
//      .allowPackage( "gw.lang.function" );

    GosuTestUtil.evalGosu( "uses java.math.BigDecimal\n" +
                           "static class Foo { function m() {} }\n" +
                           "new Foo().m()\n" +
                           "var s = \"hi\"\n" +
                           "var ss: String = \"hi\"\n" +
                           "var bd: BigDecimal = BigDecimal.valueOf(1)\n" +
                           "var list = {1, 2}\n" +
                           "var f = \\i: int -> i * 2\n f(2)\n" +
                           "list.stream().anyMatch(\\e -> e == 2)\n" +
                           "new StringBuilder(\"asdf\").append(Double.valueOf(1))\n" +
                           "var date = java.time.LocalDate.of(2026, 11, 6)\n var month = date.getMonth()\n",
                           options );
  }

  private static ParserOptions baselineRestrictions()
  {
    ParserOptions options = new ParserOptions();
    Restrictions baselineRestrictions = Restrictions
      .baseline()
      .build();

    options.withAstProcessorProviders( baselineRestrictions );
    return options;
  }
}
