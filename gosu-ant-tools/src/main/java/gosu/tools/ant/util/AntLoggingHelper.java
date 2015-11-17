package gosu.tools.ant.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Helper to make logging from Ant easier.
 * 
 * Copied from org.codehaus.groovy.ant.LoggingHelper
 */
public class AntLoggingHelper {
  private final Task _owner;

  public AntLoggingHelper(final Task owner) {
    assert owner != null;
    _owner = owner;
  }

  public void error(final String msg) {
    _owner.log(msg, Project.MSG_ERR);
  }

  public void error(final String msg, final Throwable t) {
    _owner.log(msg, t, Project.MSG_ERR);
  }

  public void warn(final String msg) {
    _owner.log(msg, Project.MSG_WARN);
  }

  public void info(final String msg) {
    _owner.log(msg, Project.MSG_INFO);
  }

  public void verbose(final String msg) {
    _owner.log(msg, Project.MSG_VERBOSE);
  }

  public void debug(final String msg) {
    _owner.log(msg, Project.MSG_DEBUG);
  }
}
