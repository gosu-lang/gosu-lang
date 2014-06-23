/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

/**
 * Typeloaders that implement this interface can perform additional resource verification that can be triggered
 * by verification tools.
 */
public interface IResourceVerifier {

  /**
   * Verify resources specific to this typeloader.
   * @return a list of errors that occurred
   * @throws Exception if an exception occurs
   */
  public abstract List<String> verifyResources() throws Exception;

}
