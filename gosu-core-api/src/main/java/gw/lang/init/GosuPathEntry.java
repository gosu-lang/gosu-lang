/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import manifold.api.fs.IDirectory;
import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class GosuPathEntry {

  private IDirectory _root;
  private List<? extends IDirectory> _srcs;

  /**
   * Constructs a new GosuPathEntry with the specified data.  None of the arguments
   * are allowed to be null.  Typeloader names should be fully-qualified Java class names.
   *
   * @param root the root IDirectory for this path entry
   * @param srcs the set of IDirectories for this entry that should be considered to be source directories
   */
  public GosuPathEntry(IDirectory root, List<? extends IDirectory> srcs) {
    if (root == null) {
      throw new IllegalArgumentException("The root argument cannot be null");
    }
    if (srcs == null) {
      throw new IllegalArgumentException("The srcs argument cannot be null");
    }
    _root = root;
    _srcs = srcs;
  }

  /**
   * Returns the root directory for this GosuPathEntry.  This method will never return null.
   *
   * @return the root directory
   */
  public IDirectory getRoot() {
    return _root;
  }

  /**
   * Returns the source directories for this path entry.  This method will never return null.
   *
   * @return the source directories
   */
  public List<? extends IDirectory> getSources() {
    return _srcs;
  }

  /**
   * Returns a String representation of this path entry suitable for use in debugging.
   *
   * @return a debug String representation of this object
   */
  public String toDebugString() {
    StringBuilder sb = new StringBuilder();
    sb.append("GosuPathEntry:\n");
    sb.append("  root: ").append(_root.toJavaFile().getAbsolutePath()).append("\n");
    for (IDirectory src : _srcs) {
      sb.append("  src: ").append(src.toJavaFile().getAbsolutePath()).append("\n");
    }
    return sb.toString();
  }

  public String toString() {
    return _root.toString(); 
  }
}