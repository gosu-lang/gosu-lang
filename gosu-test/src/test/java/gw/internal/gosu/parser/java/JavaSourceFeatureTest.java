/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.internal.gosu.parser.java.classinfo.JavaSourceField;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.lang.parser.IExpression;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.test.TestClass;


public class JavaSourceFeatureTest extends TestClass {

  public void testJava7LiteralSyntax() {
    JavaSourceType type = (JavaSourceType) createSourceType(
            "package foo; \n" +
                    "class TestClass { \n" +
                    "  static int a_ = 7; " +
                    "  static double s = 1e2 + 1_0.0e-1_0 + 5e+2 + 0b11L + 0B1010 + a_ + 1_0 + 1_0d + 20l + 0xC_A_F_E + 01_2_0 + 1_0d " +
                    "+ 1_0.5 + 1_0.5_0d; \n" +
                    "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(2, declaredFields.length);
    assertEquals("s", declaredFields[1].getName());

    String exprSrc = ((JavaSourceField) declaredFields[1]).getRhs();
    IJavaClassInfo typeSecondField = declaredFields[1].getType();

    IExpression expr = CompileTimeExpressionParser.parse(exprSrc, type, typeSecondField.getJavaType());
    Object o = expr.evaluate();

    assertEquals("double", typeSecondField.getName());
    assertTrue(o instanceof Double);
    double num = ((Double) o).doubleValue();
    assertEquals(num, 52737.000000001);
  }

  // class

  public void testCorrectClass() {
    createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "} \n"
    );
  }

  public void testWithNoClassKeyword() {
    createSourceType(
        "package foo; \n" +
            "public TestClass { \n" +
            "} \n"
    );
  }

  public void testWithNoClassName() {
    createSourceType(
        "package foo; \n" +
            "public class { \n" +
            "} \n"
    );
  }

  public void testWithMergedClassName() {
    createSourceType(
        "package foo; \n" +
            "public classTestClass { \n" +
            "} \n"
    );
  }

  public void testWithWrongModifier() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "pub class TestClass { \n" +
            "} \n"
    );
    assertFalse(type.isPublic());
    assertTrue(type.isInternal());
  }

  public void testMisspelledClassKeyword() {
    createSourceType(
        "package foo; \n" +
            "foo TestClass { \n" +
            "} \n"
    );
  }

  // class

  public void testCorrectClass_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  public class Inner { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(1, declaredClasses.length);
    assertEquals("Inner", declaredClasses[0].getSimpleName());
  }

  public void testWithNoClassKeyword_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  public Inner { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(0, declaredClasses.length);
  }

  public void testWithNoClassName_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  public class { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(0, declaredClasses.length);
  }

  public void testWithMergedClassName_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  public classInner { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(0, declaredClasses.length);
  }

  public void testWithWrongModifier_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  pub class Inner { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(1, declaredClasses.length);
  }

  public void testMisspelledClassKeyword_Inner() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass { \n" +
            "  public classq Inner { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassInfo[] declaredClasses = type.getDeclaredClasses();
    assertEquals(0, declaredClasses.length);
  }

  // package

  public void testMisspelledPackageKeyword() {
    createSourceType(
        "pa.ge foo; \n" +
            "class TestClass { \n" +
            "} \n"
    );
  }

  // import

  public void testCorrectImport() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.lang.String; \n" +
            "class TestClass { \n" +
            "} \n"
    );
    assertArrayEquals(new String[]{"java.lang.*", "java.lang.String"}, type.getImportList().toArray());
  }

  public void testMisspelledImportKeyword() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "imp.ort java.lang.String; \n" +
            "class TestClass { \n" +
            "} \n"
    );
    assertArrayEquals(new String[]{"java.lang.*"}, type.getImportList().toArray());
  }

  // field

  public void testCorrectFieldDecl() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "class TestClass { \n" +
            "  String s = \"aaa\"; \n" +
            "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(1, declaredFields.length);
    assertEquals("s", declaredFields[0].getName());
    assertEquals("java.lang.String", declaredFields[0].getType().getName());
  }

  public void testFieldWithBrokenModifier() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "class TestClass { \n" +
            "  priv String s = \"aaa\"; \n" +
            "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(1, declaredFields.length);
    assertEquals("s", declaredFields[0].getName());
    assertEquals("java.lang.String", declaredFields[0].getType().getName());
  }

  public void testFieldWithMissingType() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "class TestClass { \n" +
            "  MyString s = \"aaa\"; \n" +
            "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(1, declaredFields.length);
    assertEquals("s", declaredFields[0].getName());
    assertEquals(IJavaClassType.ERROR_TYPE, declaredFields[0].getGenericType());
    assertEquals(IJavaClassType.ERROR_TYPE, declaredFields[0].getType());
  }

  public void testFieldWithWrongName() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "class TestClass { \n" +
            "  MyString s.s = \"aaa\"; \n" +
            "} \n"
    );
    assertEquals(0, type.getDeclaredFields().length);
  }

  public void testFieldWithDiamond() {
    JavaSourceType type = (JavaSourceType) createSourceType(
            "package foo; \n" +
                    "import java.util.*; \n" +
                    "class TestClass { \n" +
                    "  List<String> x = new LinkedList<>();\n" +
            "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(1, declaredFields.length);
    assertEquals("x", declaredFields[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredFields[0].getGenericType().getName());
  }

  public void testFieldWithoutDiamond() {
    JavaSourceType type = (JavaSourceType) createSourceType(
            "package foo; \n" +
                    "import java.util.*; \n" +
                    "class TestClass { \n" +
                    "  List<String> x = new LinkedList<String>();\n" +
                    "} \n"
    );
    IJavaClassField[] declaredFields = type.getDeclaredFields();
    assertEquals(1, declaredFields.length);
    assertEquals("x", declaredFields[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredFields[0].getGenericType().getName());
  }
    // method

  public void testCorrectMethod() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.util.*; \n" +
            "class TestClass { \n" +
            "  public String getValue(int x, List<String> y) { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassMethod[] declaredMethods = type.getDeclaredMethods();
    assertEquals(1, declaredMethods.length);
    assertEquals("getValue", declaredMethods[0].getName());
    assertEquals("java.lang.String", declaredMethods[0].getReturnType().getName());
    assertEquals("int", declaredMethods[0].getGenericParameterTypes()[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredMethods[0].getGenericParameterTypes()[1].getName());
  }

  public void testMethodWithMissingName() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.util.*; \n" +
            "class TestClass { \n" +
            "  public void \n" +
            "} \n"
    );
    IJavaClassMethod[] declaredMethods = type.getDeclaredMethods();
    assertEquals(0, declaredMethods.length);
  }

  public void testMethodWithWrongModifier() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.util.*; \n" +
            "class TestClass { \n" +
            "  pub String getValue(int x, List<String> y) { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassMethod[] declaredMethods = type.getDeclaredMethods();
    assertEquals(1, declaredMethods.length);
    assertEquals("getValue", declaredMethods[0].getName());
    assertEquals("java.lang.String", declaredMethods[0].getReturnType().getName());
    assertEquals("int", declaredMethods[0].getGenericParameterTypes()[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredMethods[0].getGenericParameterTypes()[1].getName());
  }

  public void testMethodWithMissingReturnType() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.util.*; \n" +
            "class TestClass { \n" +
            "  getValue(int x, List<String> y) { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassMethod[] declaredMethods = type.getDeclaredMethods();
    assertEquals(1, declaredMethods.length);
    assertEquals("getValue", declaredMethods[0].getName());
    assertEquals(IJavaClassInfo.ERROR_TYPE, declaredMethods[0].getReturnClassInfo());
    assertEquals("int", declaredMethods[0].getGenericParameterTypes()[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredMethods[0].getGenericParameterTypes()[1].getName());
  }

  public void testMethodWithMissingParameterType() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "import java.util.*; \n" +
            "class TestClass { \n" +
            "  String getValue(intMissing x, List<String> y) { \n" +
            "  } \n" +
            "} \n"
    );
    IJavaClassMethod[] declaredMethods = type.getDeclaredMethods();
    assertEquals(1, declaredMethods.length);
    assertEquals("getValue", declaredMethods[0].getName());
    assertEquals("java.lang.String", declaredMethods[0].getReturnClassInfo().getName());
    assertEquals("ErrorType", declaredMethods[0].getGenericParameterTypes()[0].getName());
    assertEquals("java.util.List<java.lang.String>", declaredMethods[0].getGenericParameterTypes()[1].getName());
  }

  //cyclic inheritance
  public void testCyclicInheritance() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass extends TestClass{\n" +
            "}"

    );
    assertNull(type.getSuperclass());
  }

  public void testMergedClassName_InnerExtendsCausesCyclicInheritance() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public classTestClass {\n" +
            "    private class Derived extends TestClass {\n" +
            "    }\n" +
            "}"

    );
    assertNull(type.getSuperclass());
  }

  public void testInnerCyclicInheritance() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass {\n" +
            "    class Inner extends Inner {}\n" +
            "}"

    );
    IJavaClassInfo[] javaClassInfos = type.getDeclaredClasses();
    assertEquals(1, javaClassInfos.length);
    assertNull(javaClassInfos[0].getSuperclass());
  }

  public void testInnerCyclicInheritance1() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass {\n" +
            "    class Inner extends Inner1 {}\n" +
            "    class Inner1 extends Inner {}\n" +
            "}"

    );
    IJavaClassInfo[] javaClassInfos = type.getDeclaredClasses();
    assertEquals(2, javaClassInfos.length);
    assertEquals("Inner1", javaClassInfos[0].getSuperclass().getSimpleName());
    assertNull(javaClassInfos[1].getSuperclass());
  }

  public void testInnerCyclicInheritance2() {
    JavaSourceType type = (JavaSourceType) createSourceType(
        "package foo; \n" +
            "public class TestClass {\n" +
            "    class Inner extends Inner1 {}\n" +
            "    class Inner1 extends Inner2 {}\n" +
            "    class Inner2 extends Inner {}\n" +
            "}"

    );
    IJavaClassInfo[] javaClassInfos = type.getDeclaredClasses();
    assertEquals(3, javaClassInfos.length);
    assertEquals("Inner1", javaClassInfos[0].getSuperclass().getSimpleName());
    assertEquals("Inner2", javaClassInfos[1].getSuperclass().getSimpleName());
    assertNull(javaClassInfos[2].getSuperclass());
  }

  /*public void testTheCrapOutOfItBySingleTokenDeletion() throws IOException {
    Pair<String, List<Token>> pair = loadSource();
    String code = pair.getFirst();
    List<Token> tokens = pair.getSecond();
    for (int i = 0; i < tokens.size() - 1; i++) {
      CommonToken token = (CommonToken) tokens.get(i);
      String s = code.substring(0, token.getStartIndex()) + code.substring(token.getStopIndex() + 1);
      try {
        walkTheType(createSourceType(s));
      } catch (Exception e) {
        System.err.println("Failure while replacing token: " + token);
        e.printStackTrace();
        fail();
      }
    }
  }

  public void testTheCrapOutOfItByDoubleTokenDeletion() throws IOException {
    Pair<String, List<Token>> pair = loadSource();
    String code = pair.getFirst();
    List<Token> tokens = pair.getSecond();
    for (int i = 0; i < tokens.size() - 2; i++) {
      CommonToken token1 = (CommonToken) tokens.get(i + 0);
      CommonToken token2 = (CommonToken) tokens.get(i + 1);
      String s = code.substring(0, token1.getStartIndex()) + code.substring(token2.getStopIndex() + 1);
      try {
        walkTheType(createSourceType(s));
      } catch (Exception e) {
        System.err.println("Failure while replacing tokens: " + token1 + " - " + token2);
        e.printStackTrace();
        fail();
      }
    }
  }

  public void testTheCrapOutOfItBySingleTokenReplacement() throws IOException {
    Pair<String, List<Token>> pair = loadSource();
    String code = pair.getFirst();
    List<Token> tokens = pair.getSecond();
    for (int i = 0; i < tokens.size() - 1; i++) {
      CommonToken token = (CommonToken) tokens.get(i);
      String s = code.substring(0, token.getStartIndex()) + "!GARBAGE@" + code.substring(token.getStopIndex() + 1);
      try {
        walkTheType(createSourceType(s));
      } catch (Exception e) {
        System.err.println("Failure while replacing token: " + token);
        e.printStackTrace();
        fail();
      }
    }
  }*/

/*  private Pair<String, List<Token>> loadSource() throws IOException {
    URL resource = getClass().getClassLoader().getResource("gw/internal/gosu/parser/java/data.txt");
    String file = resource.getFile();
    String code = StreamUtil.getContent(new FileReader(file));
    CharStream cs = new ANTLRStringStream(code);
    JavaLexer lexer = new JavaLexer(cs);
    TokenRewriteStream tokenRewriteStream = new TokenRewriteStream(lexer, Token.DEFAULT_CHANNEL);
    tokenRewriteStream.fill();
    return new Pair<String, List<Token>>(code, tokenRewriteStream.getTokens());
  }*/

 /* private void walkTheType(IJavaClassInfo sourceType) {
    sourceType.getNamespace();
    sourceType.getArrayType();
    sourceType.getEnclosingType();
    sourceType.getEnumConstants();
    sourceType.getGenericInterfaces();
    sourceType.getGenericSuperclass();
    sourceType.getModifiers();
    sourceType.getNameSignature();
    sourceType.getSuperclass();
    sourceType.getInterfaces();

    for (IJavaClassMethod method : sourceType.getDeclaredMethods()) {
      method.getReturnClassInfo();
      method.getGenericParameterTypes();
      method.getName();
      method.getParameterTypes();
      method.getExceptionTypes();
      method.getReturnType();
      method.getReturnTypeName();
      method.getModifiers();
      if (sourceType.isAnnotation()) {
        method.getDefaultValue();
      }
    }

    for (IJavaClassConstructor constructor : sourceType.getDeclaredConstructors()) {
      constructor.getParameterTypes();
      constructor.getExceptionTypes();
      constructor.getModifiers();
      constructor.isDefault();
    }

    for (IJavaClassField field : sourceType.getDeclaredFields()) {
      field.getName();
      field.getType();
      field.getGenericType();
      field.getModifiers();
      field.isEnumConstant();
      field.isSynthetic();
    }

    for (IJavaPropertyDescriptor property : sourceType.getPropertyDescriptors()) {
      property.getDisplayName();
      property.getName();
      property.getPropertyClassInfo();
      property.getPropertyType();
      property.getReadMethod();
      property.getShortDescription();
      property.getWriteMethod();
      property.isDeprecated();
      property.isHidden();
      property.isHiddenViaFeatureDescriptor();
    }

    for (IJavaClassTypeVariable typeParameter : sourceType.getTypeParameters()) {
      typeParameter.getBounds();
      typeParameter.getName();
      typeParameter.getConcreteType();
      typeParameter.getModule();
      typeParameter.getNamespace();
      typeParameter.getSimpleName();
    }

    for (IJavaClassInfo c : sourceType.getDeclaredClasses()) {
      walkTheType(c);
    }
  }*/

  // private

  public IJavaClassInfo createSourceType(String source) {
    StringSourceFileHandle handle = new StringSourceFileHandle("foo.TestClass", source, false, ClassType.JavaClass);
    IJavaClassInfo type = JavaSourceType.createTopLevel(handle, TypeSystem.getCurrentModule());
    assertEquals("foo.TestClass", type.getName());
    assertEquals("foo", type.getNamespace());
    return type;
  }

}
