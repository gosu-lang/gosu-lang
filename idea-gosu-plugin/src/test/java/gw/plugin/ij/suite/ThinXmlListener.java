/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.suite;

import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;
import gw.xml.simple.SimpleXmlNode;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Beleve it or not, it was easier to write this class than try to adapt the
 * formatter to JUnit4's programmatic API.
 */
public class ThinXmlListener extends RunListener {
  final SimpleXmlNode _root = new SimpleXmlNode("testsuite");
  private final File _outputFile;
  private SimpleXmlNode _currentTestCase;
  private int _disabled;
  private int _errors;
  private int _failures;

  public ThinXmlListener(File outputFile, String changeList, String name, String testMachine) {
    _outputFile = outputFile;
    _root.getAttributes().put("changelist", changeList);
    _root.getAttributes().put("name", name);
    _root.getAttributes().put("starttime", new Date().toLocaleString());
    _root.getAttributes().put("testmachine", testMachine);
  }

  @Override
  public void testRunStarted(@NotNull Description description) throws Exception {
    log("Starting TestRun: " + description.getDisplayName());
    super.testRunStarted(description);    //To change body of overridden methods use File | Settings | File Templates.
  }

  private void log(String displayName) {
    System.out.println(displayName);
  }

  @Override
  public void testRunFinished(@NotNull Result result) throws Exception {
    log("finished TestRun");
    _root.getAttributes().put("disabled", Integer.toString(_disabled));
    _root.getAttributes().put("errors", Integer.toString(_errors));
    _root.getAttributes().put("failures", Integer.toString(_failures));
    _root.getAttributes().put("tests", Integer.toString(_root.getChildren().size()));
    _root.getAttributes().put("time", Long.toString(result.getRunTime() / 1000));

    log("Saving IntelliJ test results to " + _outputFile.getAbsolutePath());
    try {
      FileWriter fileWriter = new FileWriter(_outputFile);
      String str = _root.toXmlString();
      log("Output content: " + str);
      fileWriter.write(str);
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      log("Failed to save test data : " + e.getMessage());
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    super.testRunFinished(result);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  public void testStarted(@NotNull Description description) throws Exception {
    log("\nTest Started : " + description.getDisplayName());
    log("Free Memory : " + Runtime.getRuntime().freeMemory());
    String displayName = description.getDisplayName();
    String testMethodName = displayName.substring(0, displayName.indexOf('('));
    String classname = displayName.substring(displayName.indexOf('(') + 1, displayName.indexOf(')'));
    String pkgName = GosuClassUtil.getPackage(classname);
    String relativeName = GosuClassUtil.getShortClassName(classname);

    _currentTestCase = new SimpleXmlNode("testcase");
    _currentTestCase.getAttributes().put("critical", "false");
    _currentTestCase.getAttributes().put("gosu", "true");
    _currentTestCase.getAttributes().put("class", relativeName);
    _currentTestCase.getAttributes().put("package", pkgName);
    _currentTestCase.getAttributes().put("name", testMethodName);

    super.testStarted(description);    //To change body of overridden methods use File | Settings | File Templates.
  }

  //testVariableSymbolCompletion(gw.plugin.ij.completion.CoreCompTest)
  @Override
  public void testFinished(@NotNull Description description) throws Exception {
    log("\nTest Finished : " + description.getDisplayName());
    log("Free Memory : " + Runtime.getRuntime().freeMemory());
    _currentTestCase.getAttributes().put("time", "0.0"); //TODO cgross implement timing

    _root.getChildren().add(_currentTestCase);
    super.testFinished(description);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  public void testFailure(@NotNull Failure failure) throws Exception {
    handleTestFailure(failure);
    super.testFailure(failure);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  public void testAssumptionFailure(@NotNull Failure failure) {
    handleTestFailure(failure);
    super.testAssumptionFailure(failure);    //To change body of overridden methods use File | Settings | File Templates.
  }

  private void handleTestFailure(@NotNull Failure failure) {
    log("Failure : " + failure.getMessage());
    _failures++;
    SimpleXmlNode error = new SimpleXmlNode("error");
    error.getAttributes().put("message", failure.getMessage());
    error.getAttributes().put("type", failure.getException().getClass().getName());
    error.setText(GosuExceptionUtil.getStackTraceAsString(failure.getException()));
    _currentTestCase.getChildren().add(error);
  }
}
