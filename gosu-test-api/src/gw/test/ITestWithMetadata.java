/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import java.util.List;

/**
 * A test implementing this interface provides metadata (e.g. annotation info) for each of its test methods.
 */
public interface ITestWithMetadata {
  /**
   * Returns any applicable metadata for the tests in this class.
   * @return A list of @link{gw.test.ITestMetadata} objects, one for each test method providing metadata.
   */
  List<? extends ITestMetadata> getMetadata();
}