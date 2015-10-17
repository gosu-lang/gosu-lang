package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.MatchingTask;

import java.util.List;

abstract public class GosuMatchingTask extends MatchingTask {

  protected void buildError(String message) {
    throw new BuildException(message, getLocation());
  }

}
