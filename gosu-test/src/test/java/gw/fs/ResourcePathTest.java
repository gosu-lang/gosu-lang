/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs;

import gw.test.TestClass;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 19, 2010
 * Time: 10:15:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResourcePathTest extends TestClass {

  public void testParseWithWindowsDriveLetter() {
    ResourcePath path = ResourcePath.parse("C:");
    assertEquals("C:\\", path.getPathString("\\"));
    assertPathMatches(path, "C:");
  }

  public void testParseWithLowerCaseWindowsDriveLetter() {
    ResourcePath path = ResourcePath.parse("c:");
    assertEquals("C:\\", path.getPathString("\\"));
    assertPathMatches(path, "C:");
  }

  public void testParseWithWindowsDriveLetterAndWindowsSlash() {
    ResourcePath path = ResourcePath.parse("C:\\");
    assertEquals("C:\\", path.getPathString("\\"));
    assertPathMatches(path, "C:");
  }

  public void testParseWithWindowsDriveLetterAndUnixSlash() {
    ResourcePath path = ResourcePath.parse("C:/");
    assertEquals("C:\\", path.getPathString("\\"));
    assertPathMatches(path, "C:");
  }

  public void testParseWithWindowsDriveLetterAndOneComponent() {
    ResourcePath path = ResourcePath.parse("C:\\foo");
    assertEquals("C:\\foo", path.getPathString("\\"));
    assertPathMatches(path, "foo", "C:");
  }

  public void testParseWithWindowsDriveLetterAndThreeComponents() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar\\baz.txt");
    assertEquals("C:\\foo\\bar\\baz.txt", path.getPathString("\\"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "C:");
  }

  public void testParseWithWindowsDriveLetterAndThreeComponentsAndMixedSlashes() {
    ResourcePath path = ResourcePath.parse("C:/foo\\bar/baz.txt");
    assertEquals("C:\\foo\\bar\\baz.txt", path.getPathString("\\"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "C:");
  }

  public void testParseWithAlternativeWindowsDriveLetter() {
    ResourcePath path = ResourcePath.parse("k:/foo\\bar/baz.txt");
    assertEquals("K:\\foo\\bar\\baz.txt", path.getPathString("\\"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "K:");
  }

  // TODO - AHK - The tests here aren't 100% right . . .
  public void testParseWithWindowsNetworkStart() {
    ResourcePath path = ResourcePath.parse("\\\\");
    assertEquals("\\\\", path.getPathString("\\"));
    assertPathMatches(path, "\\\\");
  }

  public void testParseWithWindowsNetworkStartAndOneComponent() {
    ResourcePath path = ResourcePath.parse("\\\\foo");
    assertEquals("\\\\foo", path.getPathString("\\"));
    assertPathMatches(path, "foo", "\\\\");
  }

  public void testParseWithWindowsNetworkStartAndThreeComponents() {
    ResourcePath path = ResourcePath.parse("\\\\foo\\bar\\baz.txt");
    assertEquals("\\\\foo\\bar\\baz.txt", path.getPathString("\\"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "\\\\");
  }

  public void testParseWithWindowsNetworkStartAndThreeComponentsAndMixedSlashes() {
    ResourcePath path = ResourcePath.parse("\\\\foo/bar\\baz.txt");
    assertEquals("\\\\foo\\bar\\baz.txt", path.getPathString("\\"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "\\\\");
  }

  public void testParseWithUnixRoot() {
    ResourcePath path = ResourcePath.parse("/");
    assertEquals("/", path.getPathString("/"));
    assertPathMatches(path, "");
  }

  public void testParseWithUnixRootAndOneComponent() {
    ResourcePath path = ResourcePath.parse("/foo");
    assertEquals("/foo", path.getPathString("/"));
    assertPathMatches(path, "foo", "");
  }

  public void testParseWithUnixRootAndThreeComponents() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz.txt");
    assertEquals("/foo/bar/baz.txt", path.getPathString("/"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "");
  }

  public void testParseWithUnixRootAndThreeComponentsAndMixedSlashes() {
    ResourcePath path = ResourcePath.parse("/foo\\bar/baz.txt");
    assertEquals("/foo/bar/baz.txt", path.getPathString("/"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "");
  }

  public void testParseIgnoresDots() {
    ResourcePath path = ResourcePath.parse("/./foo/bar/././baz.txt");
    assertEquals("/foo/bar/baz.txt", path.getPathString("/"));
    assertPathMatches(path, "baz.txt", "bar", "foo", "");
  }

  public void testParseTraversesBackwardsWithDoubleDots() {
    ResourcePath path = ResourcePath.parse("/foo/bar/../../baz.txt");
    assertEquals("/baz.txt", path.getPathString("/"));
    assertPathMatches(path, "baz.txt", "");
  }

  public void testParseIgnoresTrailingSlashes() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz/");
    assertEquals("/foo/bar/baz", path.getPathString("/"));
    assertPathMatches(path, "baz", "bar", "foo", "");
  }

  // TODO - Error conditions:  just dot, double dots traversing backwards,, etc.

  // tests for join(String)

  public void testJoinWithEmptyStringIsANoOp() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("");
    assertEquals("/foo/bar/baz", path.getPathString("/"));
    assertPathMatches(path, "baz", "bar", "foo", "");
  }

  public void testJoinWithDotIsANoOp() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join(".");
    assertEquals("/foo/bar/baz", path.getPathString("/"));
    assertPathMatches(path, "baz", "bar", "foo", "");
  }

  public void testJoinWithWindowsSlashIsANoOp() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("\\");
    assertEquals("/foo/bar/baz", path.getPathString("/"));
    assertPathMatches(path, "baz", "bar", "foo", "");
  }

  public void testJoinWithUnixSlashIsANoOp() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("/");
    assertEquals("/foo/bar/baz", path.getPathString("/"));
    assertPathMatches(path, "baz", "bar", "foo", "");
  }

  public void testJoinWithSimpleName() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("test");
    assertEquals("/foo/bar/baz/test", path.getPathString("/"));
    assertPathMatches(path, "test", "baz", "bar", "foo", "");
  }

  public void testJoinWithDotDot() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("..");
    assertEquals("/foo/bar", path.getPathString("/"));
    assertPathMatches(path, "bar", "foo", "");
  }

  public void testJoinWithThreePathComponentsWithWindowsSlashes() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("test1\\test2\\test3");
    assertEquals("/foo/bar/baz/test1/test2/test3", path.getPathString("/"));
    assertPathMatches(path, "test3", "test2", "test1", "baz", "bar", "foo", "");
  }

  public void testJoinWithThreePathComponentsWithUnixSlashes() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("test1/test2/test3");
    assertEquals("/foo/bar/baz/test1/test2/test3", path.getPathString("/"));
    assertPathMatches(path, "test3", "test2", "test1", "baz", "bar", "foo", "");
  }

  public void testJoinWithThreePathComponentsWithMixedSlashes() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("test1/test2\\test3");
    assertEquals("/foo/bar/baz/test1/test2/test3", path.getPathString("/"));
    assertPathMatches(path, "test3", "test2", "test1", "baz", "bar", "foo", "");
  }

  public void testJoinWithThreePathComponentsWithLeadingSlash() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("/test1/test2/test3");
    assertEquals("/foo/bar/baz/test1/test2/test3", path.getPathString("/"));
    assertPathMatches(path, "test3", "test2", "test1", "baz", "bar", "foo", "");
  }

  public void testJoinWithThreePathComponentsWithTrailingSlash() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("/test1/test2/test3/");
    assertEquals("/foo/bar/baz/test1/test2/test3", path.getPathString("/"));
    assertPathMatches(path, "test3", "test2", "test1", "baz", "bar", "foo", "");
  }

  public void testJoinWithConfusingMixOfDotsAndDotDotsAndSlashes() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("/./../test1/.././test2/test3/.././");
    assertEquals("/foo/bar/test2", path.getPathString("/"));
    assertPathMatches(path, "test2", "bar", "foo", "");
  }

  public void testJoinWithNullArgumentThrowsIllegalArgumentException() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    try {
      path.join(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testJoinThatTraversesBackToTheRoot() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    path = path.join("../../..");
    assertEquals("/", path.getPathString("/"));
    assertPathMatches(path, "");
  }

  public void testJoinThatTraversesBackPastTheRootThrowsIllegalArgumentException() {
    ResourcePath path = ResourcePath.parse("/foo/bar/baz");
    try {
      path.join("../../../..");
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // TODO - AHK - Error conditions around join:  mangled string, .. that recurses back past the root, etc.

  // tests for relativePath(ResourcePath)
   // TODO - AHK - Duplicate tests for use with alternative or mixed slashes?

  public void testRelativePathReturnsNullForIdenticalPath() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar");
    assertNull(path.relativePath(path));
  }

  public void testRelativePathReturnsNullForPathWithNoCommonRoot() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar");
    ResourcePath path2 = ResourcePath.parse("C:\\baz\\other");
    assertNull(path.relativePath(path2));
    assertNull(path2.relativePath(path));
  }

  public void testRelativePathReturnsNullForAncestorPath() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar\\baz");
    ResourcePath path2 = ResourcePath.parse("C:\\foo");
    assertNull(path.relativePath(path2));
  }

  public void testRelativePathReturnsNullForParentPath() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar\\baz");
    ResourcePath path2 = ResourcePath.parse("C:\\foo\\bar");
    assertNull(path.relativePath(path2));
  }

  public void testRelativePathReturnsOneElementPathForChildPath() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar\\baz");
    ResourcePath path2 = ResourcePath.parse("C:\\foo\\bar");
    assertEquals("baz", path2.relativePath(path));
  }

  public void testRelativePathReturnsCorrectPathForDescendantPath() {
    ResourcePath path = ResourcePath.parse("C:\\foo\\bar\\baz\\boo\\other");
    ResourcePath path2 = ResourcePath.parse("C:\\foo\\bar");
    assertEquals("baz/boo/other", path2.relativePath(path, "/"));
  }

  // TODO - AHK - Make sure relativePath respects the option passed in, and that the default is the file system separator

  // --------------------- Private Helper Methods

  private void assertPathMatches(ResourcePath path, String... reversedPath) {
    ResourcePath currentPath = path;
    for (String s : reversedPath) {
      if (currentPath == null) {
        fail("Resource path did not contain " + s);
      }
      assertEquals(s, currentPath.getName());
      currentPath = currentPath.getParent();     
    }
  }
}
