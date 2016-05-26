package gw.internal.gosu.parser;

import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

public class JavaTypeExtensionsTest extends TestClass {

  public void testSimpleExtension() {
    IJavaTypeInternal extendedType = JavaTypeExtensions.newExtendedType(
            TestTypeExtension.class, (IJavaTypeInternal) JavaTypes.STRING(), new TestTypeExtensionImpl());
    TestTypeExtension type = (TestTypeExtension) extendedType;
    assertEquals("42", type.intToString(42));
  }

  public void testExceptionsHandledCorrectly() {
    IJavaTypeInternal extendedType = JavaTypeExtensions.newExtendedType(
            TestTypeExtension.class, (IJavaTypeInternal) JavaTypes.STRING(), new TestTypeExtensionImpl());
    TestTypeExtension type = (TestTypeExtension) extendedType;
    try {
      type.doThrow();
      fail();
    } catch (Exception e) {
      assertEquals("bad", e.getMessage());
    }
  }

  public void testToStringDelegatesToPrimaryObject() {
    IJavaTypeInternal extendedType = JavaTypeExtensions.newExtendedType(
            TestTypeExtension.class, (IJavaTypeInternal) JavaTypes.STRING(), new TestTypeExtensionImpl());
    assertEquals(JavaTypes.STRING().toString(), extendedType.toString());
  }
}
