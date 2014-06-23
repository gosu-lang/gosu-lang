/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import static gw.util.OSPlatform.Platform;

import java.util.Map;

/**
 * @deprecated use {@link gw.util.process.ProcessRunner} instead
 */
@Deprecated
public class Shell {

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and returns all content sent to standard out as a string.  If the command
   * finishes with a non zero return value, a {@link CommandFailedException} is thrown.</p>
   * <p/>
   * <p>Content sent to standard error by the command will be forwarded to standard error for this JVM.  If you wish
   * to capture StdErr as well, use {@link #buildProcess(String)} to create a ProcessStarter and call the {@link ProcessStarter#withStdErrHandler(gw.util.ProcessStarter.OutputHandler)}
   * method.</p>
   * <p/>
   * <p>This method blocks on the execution of the command.</p>
   * <p/>
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   var currentDir = Shell.exec( "dir" ) // windows
   *   var currentDir = Shell.exec( "ls" )  // *nix
   *   Shell.exec( "rm -rf " + directoryToNuke )
   * </pre>
   * </p>
   *
   * @param command the command to execute
   * @return the content of standard out
   * @throws CommandFailedException if the process finishes with a non-zero return value
   *                                <p/>
   *                                <p><b>Note:<b> On windows, CMD.EXE will be used to interpret "command" so that commands like "dir" work as expected.  This can
   *                                occasionally cause problems due to limitations of CMD.EXE (e.g. a command string may be too long for it.)  In these cases consider
   *                                using the {@link #buildProcess(String)}
   */
  public static String exec(String command) {
    return buildProcess(command).withCMD().exec();
  }

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and returns all content sent to standard out as a string.  If the command
   * finishes with a non zero return value, a {@link CommandFailedException} is thrown.</p>
   * <p/>
   * <p>Content sent to standard error by the command will be forwarded to standard error for this JVM.  If you wish
   * to capture StdErr as well, use {@link #buildProcess(String)} to create a ProcessStarter and call the {@link ProcessStarter#withStdErrHandler(gw.util.ProcessStarter.OutputHandler)}
   * method.</p>
   * <p/>
   * <p>This method blocks on the execution of the command.</p>
   * <p/>
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   var currentDir = Shell.exec( "dir" ) // windows
   *   var currentDir = Shell.exec( "ls" )  // *nix
   *   Shell.exec( "rm -rf " + directoryToNuke )
   * </pre>
   * </p>
   *
   * @param command the command to execute
   * @return the content of standard out
   * @throws CommandFailedException if the process finishes with a non-zero return value
   *                                <p/>
   *                                <p><b>Note:<b> On windows, CMD.EXE will be used to interpret "command" so that commands like "dir" work as expected.  This can
   *                                occasionally cause problems due to limitations of CMD.EXE (e.g. a command string may be too long for it.)  In these cases consider
   *                                using the {@link #buildProcess(String)}
   */
  public static String exec(String command, String cs) {
    return buildProcess(command).withCharset(cs).withCMD().exec();
  }


  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and pipes all data sent to this processes stdout, stderr, and stdin.  If the command
   * finishes with a non zero return value, a {@link CommandFailedException} is thrown.</p>
   * <p>Stdout and Stderr from the sub-process will be piped to the current process' stdout and stderr.  Any input in
   * this processes stdin will be piped to the sub-process' stdin
   * <p/>
   * <p>Content sent to standard error by the command will be forwarded to standard error for this JVM.</p>
   * <p/>
   * <p>This method blocks on the execution of the command.</p>
   * <p/>
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   Shell.exec( "read \"are you there?\"" )
   * </pre>
   * </p>
   *
   * @param command the command to execute
   * @throws CommandFailedException if the process finishes with a non-zero return value
   */
  public void execWithPipe(String command) {
    buildProcess(command).execWithPipe();
  }

  /**
   * Builds a process starter given a command.  Single parameters in the command can be grouped by using double quotes or single
   * quotes.  I.e.  ls "~/my file with spaces" works as well as ls '~/my file with spaces'.
   *
   * @param command the command to create a process starter for
   * @return the process start.
   */
  public static ProcessStarter buildProcess(String command) {
    return new ProcessStarter(command);
  }

  /**
   * @return the Platform enum corresponding to the current platform.
   * @deprecated use {@link gw.util.OSPlatform#getPlatform()} instead
   */
  @Deprecated
  public static Platform getPlatform() {
    return OSPlatform.getPlatform();
  }

  /**
   * @deprecated use {@link gw.util.OSPlatform#isWindows()} instead
   */
  @Deprecated
  public static boolean isWindows() {
    return OSPlatform.isWindows();
  }

  /**
   * @deprecated use {@link gw.util.OSPlatform#isMac()} instead
   */
  @Deprecated
  public static boolean isMac() {
    return OSPlatform.isMac();
  }

  /**
   * @deprecated use {@link gw.util.OSPlatform#isSolaris()} instead
   */
  @Deprecated
  public static boolean isSolaris() {
    return OSPlatform.isSolaris();
  }

  /**
   * @deprecated use {@link gw.util.OSPlatform#isLinux()} instead
   */
  @Deprecated
  public static boolean isLinux() {
    return OSPlatform.isLinux();
  }

  /**
   * Prints the given prompt and then reads a line from standard in.  This can be used for basic
   * user interactions in command line environments.  This method uses the JLine library to provide
   * a reasonable line-editing experience for the user.  For example, the delete key, arrow keys,
   * and home and end keys work as they would expect, and the prompt is not editable.
   *
   * @param prompt the prompt to show the user
   * @return the string entered
   */
  public static String readLine(String prompt) {
    return System.console().readLine(prompt);
  }

  /**
   * Prints the given prompt and then reads a line from standard in, but with the input
   * masked with asterisks
   *
   * @param prompt the prompt to show the user
   * @return the string entered
   */
  public static String readMaskedLine(String prompt) {
    return new String(System.console().readPassword(prompt));
  }

  public static String getAllThreadStacks() {
    StringBuilder b = new StringBuilder();
    for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
      b.append(entry.getKey().getName()).append('\n');
      for (StackTraceElement stackTraceElement : entry.getValue()) {
        b.append(stackTraceElement).append('\n');
      }
      b.append('\n');
    }
    String str = b.toString();
    return str;
  }

  public static void dumpStacks() {
    System.err.print(getAllThreadStacks());
  }

}
