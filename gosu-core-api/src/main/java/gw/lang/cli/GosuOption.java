/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import gw.internal.ext.org.apache.commons.cli.Option;

public class GosuOption extends Option {

  public GosuOption(String opt, String description) throws IllegalArgumentException {
    super(opt, description);
  }

  public GosuOption(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
    super(opt, longOpt, hasArg, description);
  }

  private boolean _hidden = false;

  /**
   * Set this option as hidden
   *
   * @param value hidden value
   */
  public void setHidden( Boolean value ) {
    _hidden = value;
  }

  /**
   * Is this a hidden option?
   * 
   * @return whether this option is hidden
   */
  public Boolean isHidden() {
    return _hidden;
  }
}
