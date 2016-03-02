/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package editor.util.transform.java;

import junit.framework.TestCase;

public class JavaToGosuFragmentTest extends TestCase {

  public void testExpressionFragment() {
    String src = "2";
    String reference = "2\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testStatementFragment() {
    String src = "int x = 2;";
    String reference = "var x = 2\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testStatementWithLineCommentFragment() {
    String src = "int x = 2; // hello";
    String reference = "var x = 2\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testMethodFragment() {
    String src = "  void B() {\n" +
            "    int x = 2;\n" +
            "    LinkedList<Integer> l = new LinkedList<Integer>();\n" +
            "  }";
    String reference = "function B() : void {\n" +
            "  var x = 2\n" +
            "  var l = new LinkedList<Integer>()\n" +
            "}\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testMethodDeclarationFragment() {
    String src = "void add(int index, E element);";
    String reference = "function add(index_0 : int, element : E) : void\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testClassFragment() {
    String src = "public class A {\n" +
            "  void B() {\n" +
            "    int x = 2;\n" +
            "    LinkedList<Integer> l = new LinkedList<Integer>();\n" +
            "  }\n" +
            "}";
    String reference = "public class A  {\n" +
            "  function B() : void {\n" +
            "    var x = 2\n" +
            "    var l = new LinkedList<Integer>()\n" +
            "  }\n" +
            "}\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testClassWithInstanceVariableFragment() {
    String src = "private int x;";
    String reference = "private var x : int\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }

  public void testCompilationUnit() {
    String src = "import java.util.LinkedList;\n" +
            "\n" +
            "public class A {\n" +
            "  void B() {\n" +
            "    int x = 2;\n" +
            "    LinkedList<Integer> l = new LinkedList<Integer>();\n" +
            "  }\n" +
            "}";
    String reference = "uses java.lang.*\n" +
            "uses java.util.LinkedList\n" +
            "\n" +
            "public class A  {\n" +
            "\n" +
            "  function B() : void {\n" +
            "    var x = 2\n" +
            "    var l = new LinkedList<Integer>()\n" +
            "  }\n" +
            "\n" +
            "}\n";
    String out = JavaToGosu.convertString(src);
    assertEquals(out, reference);
  }
}
