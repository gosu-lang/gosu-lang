/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.quickfix;

import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.framework.GosuTestCase;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;

import java.util.*;

import static gw.plugin.ij.util.ClassLord.simplifyTypes;

public class SimplifyTypeTest extends GosuTestCase {

  public void testSingleType() {
    assertEqualsNoSpaces("List<String>", simplify("java.util.List<String>"));
  }

  public void testGenericType() {
    assertEqualsNoSpaces("List<Map<String, String>>",
        simplify("java.util.List<java.util.Map<java.lang.String, java.lang.String>>"));
  }

  public void testErrorType() {
    assertEqualsNoSpaces("List<Map<java.util.String, String>>",
        simplify("java.util.List<java.util.Map<java.util.String, java.lang.String>>"));
  }

  public void testConflict() {
    String type = "java.util.Set<java.awt.List>>";
    type = simplifyTypes(type, parse("import java.util.List"), new HashMap<String, String>());
    assertEqualsNoSpaces("Set<java.awt.List>", type);
  }

  public void testSimplifiedCollected() {
    String type = "java.util.Map<java.awt.List, java.util.List<String>>";
    Map<String, String> simplified = new LinkedHashMap<>();
    type = simplifyTypes(type, parse("import java.util.List"), simplified);

    assertEqualsNoSpaces("Map<java.awt.List, List<String>>", type);

    assertEquals(2, simplified.size());

    List<String> simple = new ArrayList<>(simplified.keySet());
    List<String> fqn = new ArrayList<>(simplified.values());

    assertEquals("List", simple.get(0));
    assertEquals("Map", simple.get(1));

    assertEquals("java.util.List", fqn.get(0));
    assertEquals("java.util.Map", fqn.get(1));
  }

  private String simplify(String type) {
    return simplifyTypes(type, parse(""), new HashMap<String, String>());
  }
  
  private void assertEqualsNoSpaces(String expected, String actual) {
    expected = expected.replaceAll("\\s+", "");
    actual = actual.replaceAll("\\s+", "");
    assertEquals(expected, actual);
  }
  
  protected IGosuFileBase parse(CharSequence src) {
    return GosuPsiParseUtil.parse(src, getProject(), TypeSystem.getGlobalModule());
  }
  
  
}
