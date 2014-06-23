/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import java.util.Map;

/**
 * Metadata (e.g. annotation info) for a test method.
 */
public interface ITestMetadata {
  /**
   * @return The name of the test method.
   */
  String getName();

  /**
   * @return A map from attribute names to attribute values.
   */
  Map<String, String> getAttributes();

  boolean shouldNotRunTest();
}