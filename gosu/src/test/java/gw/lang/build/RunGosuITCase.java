/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.build;

import gw.test.util.ITCaseUtils;
import gw.test.util.TestOutputHandler;
import gw.util.GosuStringUtil;
import gw.util.OSPlatform;
import gw.util.StreamUtil;
import gw.util.process.ProcessRunner;
import org.fest.assertions.Assertions;
import org.fest.assertions.ListAssert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 */
public class RunGosuITCase {

//  private static DistAssemblyUtil _assembly;
//  private static String _gosu;
//  @Rule public TestName _name = new TestName();
//
//
//  @BeforeClass
//  public static void beforeTestClass() throws Exception {
//    _assembly = DistAssemblyUtil.getInstance();
//    File gosuHome = _assembly.getDir();
//    File gosuHomeWithSpaces = new File(gosuHome.getParentFile(), "go su");
//    copy(gosuHome, gosuHomeWithSpaces);
//    File binDir = new File(gosuHomeWithSpaces, "bin");
//    _gosu = new File(binDir, OSPlatform.isWindows() ? "gosu.cmd" : "gosu").getPath();
//  }
//
//  private static void copy(File orig, File copy) {
//    if (orig.isDirectory()) {
//      copy.mkdir();
//      File[] children = orig.listFiles();
//      if (children != null) {
//        for (File child : children) {
//          copy(child, new File(copy, child.getName()));
//        }
//      }
//    }
//    else {
//      InputStream in = null;
//      OutputStream out = null;
//      try {
//        in = new FileInputStream(orig);
//        out = new FileOutputStream(copy);
//        StreamUtil.copy(in, out);
//      } catch (FileNotFoundException e) {
//        throw new RuntimeException(e);
//      } catch (IOException e) {
//        throw new RuntimeException(e);
//      } finally {
//        try {
//          StreamUtil.close(in, out);
//        } catch (IOException e) {
//          System.err.println("caught exception while closing streams");
//          e.printStackTrace(System.err);
//        }
//      }
//      if (copy.getName().equals("gosu")) {
//        copy.setExecutable(true);
//      }
//    }
//  }
//
//  @Test
//  public void dashHArgShowsHelp() {
//    GosuRunner gosu = new GosuRunner()
//            .run("-h");
//    assertThat(gosu)
//            .hasZeroExitCode();
//    assertThat(gosu._stdout.getLines()).startsWith(
//            "Usage:",
//            "        gosu [options] [program [args...]]");
//  }
//
///*  @Test
//  public void unknownArgShowsHelp() {
//    GosuRunner gosu = new GosuRunner()
//            .run("-foobar");
//    assertThat(gosu)
//            .hasExitCode(1)
//            .hasStdErr("Unknown option: -foobar");
//    assertThat(gosu._stdout.getLines()).startsWith(
//            "Usage:",
//            "        gosu [options] [program [args...]]");
//  }*/
//
//  @Test
//  public void noArgsStartsInteractiveShell() {
//    GosuRunner gosu = new GosuRunner()
//            .withInput("help\nquit")
//            .run();
//    assertThat(gosu)
//            .hasZeroExitCode()
//            .hasStdOut(
//                    "Type \"help\" to see available commands\n" +
//                    "gs> " /* + "help\n" */ +
//                    "The following commands are available:\n" +
//                    "\n" +
//                    "  help - show this message\n" +
//                    "  exit - exit this interpreter\n" +
//                    "  quit - exit this interpreter\n" +
//                    "  ls - lists all the local variables\n" +
//                    "  clear - clears all local variables and functions\n" +
//                    "  rm [var_name] - clears the given variable\n" +
//                    "\n" +
//                    "gs> " /* + "quit" */);
//  }
//
//  @Test
//  public void hello_world() {
//    GosuRunner gosu = new GosuRunner()
//            .withWorkingDir(_name.getMethodName().replace('_', ' '))
//            .run("hello.gsp");
//    assertThat(gosu)
//            .hasZeroExitCode()
//            .hasNoStderr()
//            .hasStdOut("hello!");
//  }
//
//  @Test
//  public void with_submodules() {
//    GosuRunner gosu = new GosuRunner()
//            .withWorkingDir(_name.getMethodName().replace('_', ' '))
//            .run("runfoo.gsp");
//    assertThat(gosu)
//            .hasZeroExitCode()
//            .hasNoStderr()
//            .hasStdOut("baz");
//  }
//
//  @Test
//  public void with_submodules_fromGosuHome() {
//    GosuRunner gosu = new GosuRunner()
//            .run("src" + File.separator + "test" + File.separator + "test-projects" + File.separator + "with submodules" + File.separator + "runfoo.gsp");
//    assertThat(gosu)
//            .hasZeroExitCode()
//            .hasNoStderr()
//            .hasStdOut("baz");
//  }
//
//  @Test
//  public void withCLA() {
//    GosuRunner gosu = new GosuRunner()
//            .withWorkingDir(_name.getMethodName().replace('_', ' '))
//            .run("runwithcla.gsp", "-foo", "baz", "-bar");
//    assertThat(gosu)
//            .hasZeroExitCode()
//            .hasNoStderr()
//            .hasStdOut("TestOptions.Foo: baz\nTestOptions.Bar: true");
//  }
//
//  private static GosuAssert assertThat(GosuRunner gosu) {
//    return new GosuAssert(gosu);
//  }
//
//  private static ListAssert assertThat(List<?> actual) {
//    return Assertions.assertThat(actual);
//  }
//
//  private static class GosuAssert {
//    private final GosuRunner _gosu;
//    private GosuAssert(GosuRunner gosu) {
//      _gosu = gosu;
//    }
//    GosuAssert hasStdOut(String expected) {
//      String actual = GosuStringUtil.join("\n", _gosu._stdout.getLines());
//      Assertions.assertThat(actual).isEqualTo(expected);
//      return this;
//    }
//    GosuAssert hasStdErr(String expected) {
//      String actual = GosuStringUtil.join("\n", _gosu._stderr.getLines());
//      Assertions.assertThat(actual).isEqualTo(expected);
//      return this;
//    }
//    GosuAssert hasNoStderr() {
//      Assertions.assertThat(_gosu._stderr.getLines()).isEmpty();
//      return this;
//    }
//    GosuAssert hasZeroExitCode() {
//      Assertions.assertThat(_gosu._runner.getExitCode()).isZero();
//      return this;
//    }
//    GosuAssert hasExitCode(int expected) {
//      Assertions.assertThat(_gosu._runner.getExitCode()).isEqualTo(expected);
//      return this;
//    }
//  }
//
//  private class GosuRunner {
//    final TestOutputHandler _stdout = new TestOutputHandler();
//    final TestOutputHandler _stderr = new TestOutputHandler();
//    final ProcessRunner _runner = new ProcessRunner(_gosu);
//    final File _pom;
//    final File _testProjectsDir;
//    File _workingDir;
//    String _input;
//
//    private GosuRunner() {
//      _pom = ITCaseUtils.findPom(getClass());
//      _workingDir = _pom.getParentFile();
//      _testProjectsDir = new File(_pom.getParentFile(), "src" + File.separator + "test" + File.separator + "test-projects");
//    }
//
//    GosuRunner withWorkingDir(String workingDirName) {
//      _workingDir = new File(_testProjectsDir, workingDirName);
//      return this;
//    }
//
//    GosuRunner withInput(String input) {
//      _input = input;
//      return this;
//    }
//
//    GosuRunner run(String... args) {
//      // when running tests in Windows, jline by default would choose jline.WindowsTerminal,
//      // which can hang due to there not actually being a physical terminal
//      _runner.withArg("-Djline.terminal=jline.UnsupportedTerminal");
//
//      for (String arg : args) {
//        _runner.withArg(arg);
//      }
//      _runner.withEcho()
//              .input(_input)
//              .withStdOutHandler(_stdout)
//              .withStdErrHandler(_stderr)
//              .withWorkingDirectory(_workingDir)
//              .withEnvironmentVariable("CLASSPATH", null).exec();
//      return this;
//    }
//  }

}
