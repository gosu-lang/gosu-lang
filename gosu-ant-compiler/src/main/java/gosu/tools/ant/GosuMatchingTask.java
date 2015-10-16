package gosu.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.MatchingTask;

import java.util.List;

abstract public class GosuMatchingTask extends MatchingTask {

  protected final String MainClass = "gw.lang.gosuc.simple.GosuCompiler";
  
  protected void buildError(String message) {
    throw new BuildException(message, getLocation());
  }

  void execWithArgFiles(Java java, List<String> paths) {
    paths.forEach((p) -> java.createArg().setValue('@' + p));
    
    //String debugString = paths.stream().map((p) -> )
    log(java.getCommandLine().getCommandline().toString(), Project.MSG_VERBOSE);
    java.executeJava();
  }
  
}
