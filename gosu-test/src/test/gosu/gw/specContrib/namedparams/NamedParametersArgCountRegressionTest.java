package gw.specContrib.namedparams;


import gw.lang.parser.resources.Res;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

public class NamedParametersArgCountRegressionTest extends TestClass
{
  public void testNamedParametersWithMissingArgsMethod() {
    String sampleGosu = "class Foo {\n" +
                        "  function bar(a : String, b : String, c : String, d : String) {}\n" +
                        "}\n" +
                        "\n" +
                        "function bah() {\n" +
                        "  new Foo()\n" +
                        "    .bar(\n" +
                        "      :a = \"a\",\n" +
                        "      :b = \"b\",\n" +
                        "      :d = \"d\"\n" +
                        "  )\n" +
                        "}\n";
    GosuTestUtil.assertCausesPRE( sampleGosu, Res.MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION );
  }

  public void testNamedParametersWithMissingArgsRawFunction() {
    String sampleGosu = "function bar(a : String, b : String, c : String, d : String) {}\n" +
                        "\n" +
                        "bar(\n" +
                        "      :a = \"a\",\n" +
                        "      :b = \"b\",\n" +
                        "      :d = \"d\"\n" +
                        "  )\n" +
                        "";
    GosuTestUtil.assertCausesPRE( sampleGosu, Res.MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION );
  }

  public void testNamedParametersWithMissingArgsConstructor() {
    String sampleGosu = "class Foo {\n" +
                        "  construct(a : String, b : String, c : String, d : String) {}\n" +
                        "}\n" +
                        "\n" +
                        "function bah() {\n" +
                        "  new Foo(\n" +
                        "      :a = \"a\",\n" +
                        "      :b = \"b\",\n" +
                        "      :d = \"d\"\n" +
                        "  )\n" +
                        "}\n";
    GosuTestUtil.assertCausesPRE( sampleGosu, Res.MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR );
  }


}
