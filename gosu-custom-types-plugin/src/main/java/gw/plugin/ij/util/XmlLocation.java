/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class XmlLocation
{
  public static class Segment {
    public final String tag;
    public final int index;

    public Segment(String tag, int index) {
      this.tag = tag;
      this.index = index;
    }
  }

  public final String rootTag;
  public final List<Segment> path;
  public final String attribute;

  public XmlLocation( String rootTag, List<Segment> path ) {
    this(rootTag, path, null);
  }

  public XmlLocation( String rootTag, List<Segment> path, String attribute ) {
    this.rootTag = checkNotNull(rootTag);
    this.path = ImmutableList.copyOf(checkNotNull(path));
    this.attribute = attribute;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(rootTag);
    for (Segment segment : path) {
      sb.append("_");
      sb.append(segment.tag);
      sb.append(segment.index);
    }
    if (attribute != null) {
      sb.append("__");
      sb.append(attribute);
    }
    return sb.toString();
  }
}
