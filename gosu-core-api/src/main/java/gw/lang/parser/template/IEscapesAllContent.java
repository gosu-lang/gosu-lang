/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

public interface IEscapesAllContent extends StringEscaper {
  String escapeBody(String strContent);
}
