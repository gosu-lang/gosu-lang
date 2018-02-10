/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs;

import gw.test.TestClass;
import manifold.util.DynamicArray;
import manifold.api.fs.IDirectoryUtil;

public class IDirectoryUtilTest extends TestClass {
  public void testRootPathSplitsIntoEmptyArray() {
    assertSplitProduces("/", new String[0]);
  }

  public void testLeadingSlashIsRemoved() {
    assertSplitProduces("/com", new String[]{"com"});
  }

  public void testTrailingSlashIsRemoved() {
    assertSplitProduces("com/", new String[]{"com"});
  }

  public void testTwoLeadingSlashesBothGetRemoved() {
    assertSplitProduces("//com", new String[]{"com"});
  }

  public void testTwoTrailingSlashesBothGetRemoved() {
    assertSplitProduces("com//", new String[]{"com"});
  }

  public void testTwoEnclosedSlashesBothGetRemoved() {
    assertSplitProduces("com//guidewire", new String[]{"com", "guidewire"});
  }

  public void testLeadingDotIsRetained() {
    assertSplitProduces("./com", new String[]{".", "com"});
  }

  public void testTrailingDotIsRemoved() {
    assertSplitProduces("com/.", new String[]{"com"});
  }

  public void testEnclosedDotIsRemoved() {
    assertSplitProduces("com/./guidewire", new String[]{"com", "guidewire"});
  }

  public void testLeadingDoubleDotsAreRetained() {
    assertSplitProduces("../com", new String[]{"..", "com"});
  }

  public void testMoreDoubleDotsThanPathComponentsLeavesRemainingDots() {
    assertSplitProduces("com/guidewire/../../..", new String[]{".."});
  }

  public void testTrailingDoubleDotsRollUpToEmpty() {
    assertSplitProduces("com/..", new String[0]);
  }

  public void testTrailingDoubleDotsAreRolledp() {
    assertSplitProduces("com/guidewire/..", new String[]{"com"});
  }

  public void testEnclosedDoubleDotsAreRolledUp() {
    assertSplitProduces("com/../guidewire", new String[]{"guidewire"});
  }

  private void assertSplitProduces(String relativePath, String[] expectedComponents) {
    DynamicArray<String> elements = IDirectoryUtil.splitPath( relativePath);
    assertEquals(expectedComponents.length, elements.size());
    for (int i = 0, expectedComponentsLength = expectedComponents.length; i < expectedComponentsLength; i++) {
      assertEquals(expectedComponents[i], elements.get(i));
    }
  }
}