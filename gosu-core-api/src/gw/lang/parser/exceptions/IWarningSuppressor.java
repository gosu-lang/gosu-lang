/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

/**
 */
public interface IWarningSuppressor
{
  boolean isSuppressed( String warningCode );
}
