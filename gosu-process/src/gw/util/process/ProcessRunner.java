/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.process;

import gw.util.OSPlatform;
import gw.util.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessRunner {

  private final List<String> _rawCmd = new ArrayList<String>();

  private OutputBuffer _buffer = null;
  private Integer _exitCode = null;

  // properties set by builders:
  private boolean _withCMD;
  private File _workingDirectory;
  private Map<String, String> _env = new HashMap<String, String>();
  private boolean _echo;
  private String _input;
  private boolean _bufferStdOut;
  private boolean _bufferStdErr;
  private List<OutputHandler> _stdOutHandlers = new ArrayList<OutputHandler>();
  private List<OutputHandler> _stdErrHandlers = new ArrayList<OutputHandler>();
  private String _charset = "UTF-8";

  public static String execWithCharset(String charset, String... command) {
    return new ProcessRunner(command)
            .withStdOutBuffered()
            .withCharset(charset)
            .withCMD()
            .exec()
            .getBuffer();
  }

  public static String exec(String... command) {
    return new ProcessRunner(command)
            .withStdOutBuffered()
            .withCMD()
            .exec()
            .getBuffer();
  }

  public ProcessRunner(String... command) {
    this(Arrays.asList(command));
  }

  public ProcessRunner(List<String> command) {
    for (String elt : command) {
      _rawCmd.add(elt);
    }
  }

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix).  The resulting exit code and the output buffer are
   * accessible after this call, with {@link #getExitCode()} and {@link #getBuffer()}, respectively.
   *
   * <p>This method blocks on the execution of the command.</p>
   *
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   var currentDir = new ProcessRunner("dir").exec() // windows
   *   var currentDir = new ProcessRunner("ls").exec() // *nix
   *   new ProcessRunner( "rm -rf " + directoryToNuke ).exec()
   * </pre>
   * </p>
   *
   * @return this object for chaining
   */
  public ProcessRunner exec()
  {
    List<String> command = new ArrayList<String>();
    if (OSPlatform.isWindows() && _withCMD) {
      command.add("CMD.EXE");
      command.add("/C");
    }
    command.addAll(_rawCmd);
    ProcessBuilder pb = new ProcessBuilder(command);

    if (_workingDirectory != null) {
      pb.directory(_workingDirectory);
    }
    for (Map.Entry<String, String> entry : _env.entrySet()) {
      if (entry.getValue() != null) {
        pb.environment().put(entry.getKey(), entry.getValue());
      }
      else {
        pb.environment().remove(entry.getKey());
      }
    }

    ChainedOutputHandler stdOut = new ChainedOutputHandler();
    ChainedOutputHandler stdErr = new ChainedOutputHandler();
    if (_bufferStdOut || _bufferStdErr) {
      _buffer = new OutputBuffer();
      if (_bufferStdOut) {
        stdOut.add(_buffer);
      }
      if (_bufferStdErr) {
        stdErr.add(_buffer);
      }
    }
    EchoOutputEmitter echo = null;
    if (_echo) {
      echo = new EchoOutputEmitter(getRawCmdStr(), System.out, System.err);
      stdOut.add(echo.getStdOutHandler());
      stdErr.add(echo.getStdErrHandler());
    }
    for (OutputHandler handler : _stdOutHandlers) {
      stdOut.add(handler);
    }
    for (OutputHandler handler : _stdErrHandlers) {
      stdErr.add(handler);
    }

    try {
      Process process = pb.start();
      feedInput(process, _input, _charset);
      if (echo != null) {
        echo.processStarted();
      }
      _exitCode = nomNomNom(process, stdOut.maybeReduce(), stdErr.maybeReduce(), _charset);
      if (echo != null) {
        echo.processFinished();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        StreamUtil.close(stdOut, stdErr);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return this;
  }

  private static void feedInput(Process process, String input, String charset) throws IOException {
    Writer inputEmitter = null;
    try {
      if (input != null) {
        inputEmitter = new OutputStreamWriter(process.getOutputStream(), charset);
        inputEmitter.write(input);
      }
    } finally {
      try {
        StreamUtil.close(inputEmitter);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static Integer nomNomNom(Process process, OutputHandler stdOut, OutputHandler stdErr, String charset) {
    Gobbler outputGobbler = new Gobbler( process.getInputStream(), stdOut, charset );
    Gobbler errorGobbler = new Gobbler( process.getErrorStream(), stdErr, charset );

    // kick off the gobblers
    errorGobbler.start();
    outputGobbler.start();

    try {
      int exitCode = process.waitFor();
      errorGobbler.join();
      outputGobbler.join();
      return exitCode;
    } catch (InterruptedException e) {
      //ignore
    } finally {
      try {
        StreamUtil.close(process.getErrorStream(), process.getInputStream());
      } catch ( IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public String getRawCmdStr() {
    StringBuilder rawCmdStr = new StringBuilder();
    for (String item : _rawCmd) {
      if (rawCmdStr.length() > 0) {
        rawCmdStr.append(' ');
      }
      if (item.contains(" ")) {
        rawCmdStr.append('"').append(item).append('"');
      }
      else {
        rawCmdStr.append(item);
      }
    }
    return rawCmdStr.toString();
  }

  //=================================================================================
  // Process results accessors
  //=================================================================================

  /**
   * Returns any output buffered from the process' stdout or stderr, depending on if
   * {@link #withStdOutBuffered()} and/or {@link #withStdErrBuffered()} were used.
   *
   * If a buffer was desired and the process printed nothing out, an empty string is
   * returned.
   *
   * @return the buffer, or null if nothing was to be buffered
   */
  public String getBuffer() {
    return _buffer == null ? null : _buffer.toString();
  }

  /**
   * Returns the process' exit code, if it finished.
   *
   * @return the exit code, or null if the process never completed
   */
  public Integer getExitCode() {
    return _exitCode;
  }

  //=================================================================================
  // Builder methods
  //=================================================================================

  /**
   * Sets this process' working directory.
   *
   * @param dir this process' working directory
   * @return this object for chaining
   */
  public ProcessRunner withWorkingDirectory(File dir) {
    _workingDirectory = dir;
    return this;
  }

  /**
   * Adds an argument to the command.
   *
   * @param arg the command line argument to add
   * @return this object for chaining
   */
  public ProcessRunner withArg(String arg) {
    _rawCmd.add(arg);
    return this;
  }

  /**
   * Adds a name-value pair into this process' environment.
   * This can be called multiple times in a chain to set multiple
   * environment variables.
   *
   * @param name the variable name
   * @param value the variable value
   * @return this object for chaining
   *
   * @see     ProcessBuilder
   * @see     System#getenv()
   */
  public ProcessRunner withEnvironmentVariable(String name, String value) {
    _env.put(name, value);
    return this;
  }

  /**
   * Sets the charset with which to write to this process' input and read its output.
   *
   * If unused, this process will by default use UTF-8.
   *
   * @param cs the charset to use
   * @return this object for chaining
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public ProcessRunner withCharset(String cs) {
    _charset = cs;
    return this;
  }

  /**
   * Sets this process' stdout stream to be stored in the buffer accessible by {@link #getBuffer()}.
   *
   * @return this object for chaining
   */
  public ProcessRunner withStdOutBuffered() {
    _bufferStdOut = true;
    return this;
  }

  /**
   * Sets this process' stdout stream to be stored in the buffer accessible by {@link #getBuffer()}.
   *
   * @return this object for chaining
   */
  public ProcessRunner withStdErrBuffered() {
    _bufferStdErr = true;
    return this;
  }

  /**
   * Sets this process' output to be displayed the parent process' stdout and stderr.
   *
   * @return this object for chaining
   */
  public ProcessRunner withEcho() {
    _echo = true;
    return this;
  }

  /**
   * Sets the text to be directed into this process' stdin.
   *
   * @param input the text to direct into stdin
   * @return this object for chaining
   */
  public ProcessRunner input(String input) {
    _input = input;
    return this;
  }

  /**
   * The process built up will used CMD.EXE if this is a windows platform.  This is necessary because on windows certain
   * basic commands such as "dir" are not programs, but rather are built into CMD.  Thanks, Microsoft.
   * 
   * @return this object for chaining
   */
  @SuppressWarnings({"UnusedDeclaration"})
  public ProcessRunner withCMD()
  {
    _withCMD = true;
    return this;
  }

  /**
   * Adds a block to handle lines output this process' stderr.
   *
   * This can be called multiple times in a chain to add multiple handlers.
   *
   * @param stdErrHandler handler that will be called with every line of output to stderr
   * @return this object for chaining
   */
  public ProcessRunner withStdErrHandler( OutputHandler stdErrHandler )
  {
    _stdErrHandlers.add(stdErrHandler);
    return this;
  }

  /**
   * Adds a block to handle lines output this process' stdout.
   *
   * This can be called multiple times in a chain to add multiple handlers.
   *
   * @param stdOutHandler handler that will be called with every line of output to stdout
   * @return this object for chaining
   */
  public ProcessRunner withStdOutHandler( OutputHandler stdOutHandler )
  {
    _stdOutHandlers.add(stdOutHandler);
    return this;
  }
}
