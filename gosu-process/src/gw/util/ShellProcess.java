/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class ShellProcess {
  private Process _javaProcess;
  private PrintWriter _stdin;

  ShellProcess(Process javaProcess) {
    _javaProcess = javaProcess;
    _stdin = new PrintWriter(StreamUtil.getOutputStreamWriter(_javaProcess.getOutputStream()));
  }

  /**
   * Reads a line of text from stdout.  This method will block until a newline(\n) character has been received.
   * This method is equivilent to calling <i>readUntil('\n', false)<\i>
   * @return The line of text that was read
   */
  public String readLine() {
    return readUntil(System.getProperty("line.separator"), false);
  }

  /**
   * Reads a group of text from stdout.  The line will start from the previous point until the read character.  This
   * method will block until the specified character is read.
   * @param character the character to read until
   * @param includeDeliminator true to include the deliminator in the returned text, false to not.
   * @return The text that was read
   */
  public String readUntil(String character, boolean includeDeliminator) {
    return readStreamUntil(_javaProcess.getInputStream(), includeDeliminator, character.toCharArray());
  }

  public String readChar() {
    try {
      int value = _javaProcess.getInputStream().read();
      return value == -1 ? "" : String.valueOf((char)value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Reads a line of text from stderr.  This method will block until a newline(\n) character has been received.
   * This method is equivilent to calling <i>readUntil('\n', false)<\i>
   * @return The line of text that was read
   */
  public String readStderrLine() {
    return readStderrUntil('\n', false);
  }

  /**
   * Reads a group of text from stderr.  The line will start from the previous point until the read character.  This
   * method will block until the specified character is read.
   * @param character the character to read until
   * @param includeDeliminator true to include the deliminator in the returned text, false to not.
   * @return The text that was read
   */
  public String readStderrUntil(char character, boolean includeDeliminator) {
    return readStreamUntil(_javaProcess.getErrorStream(), includeDeliminator, character);
  }

  /**
   * Writes the specified text to stdin
   * @param text the text to write
   */
  public void write(String text) {
    _stdin.write(text);
    _stdin.flush();
  }

  public void closeStdout() {
    try {
      _javaProcess.getInputStream().close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void closeStderr() {
    try {
      _javaProcess.getErrorStream().close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void closeStdin() {
    try {
      _javaProcess.getOutputStream().close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Causes the current thread to wait, if necessary, until the
   * process represented by this <code>Process</code> object has
   * terminated. This method returns
   * immediately if the subprocess has already terminated. If the
   * subprocess has not yet terminated, the calling thread will be
   * blocked until the subprocess exits.
   *
   * @return     the exit value of the process. By convention,
   *             <code>0</code> indicates normal termination.
   * @exception  InterruptedException  if the current thread is
   *             {@link Thread#interrupt() interrupted} by another thread
   *             while it is waiting, then the wait is ended and an
   *             {@link InterruptedException} is thrown.
   */
  public int waitFor() throws InterruptedException {
    return _javaProcess.waitFor();
  }

  /**
   * Returns the exit value for the subprocess.
   *
   * @return  the exit value of the subprocess represented by this
   *          <code>Process</code> object. by convention, the value
   *          <code>0</code> indicates normal termination.
   * @exception  IllegalThreadStateException  if the subprocess represented
   *             by this <code>Process</code> object has not yet terminated.
   */
  public int exitValue() {
    return _javaProcess.exitValue();
  }

  /**
   * Returns true if the process is done, false if not
   */
  public boolean getIsDone() {
    try {
      exitValue();
      return true;
    } catch (IllegalThreadStateException ex) {
      return false;
    }
  }

  /**
   * Kills the subprocess. The subprocess represented by this
   * <code>Process</code> object is forcibly terminated.
   */
  public void destroy() {
    _javaProcess.destroy();
  }

  private String readStreamUntil(InputStream stream, boolean includeDelim, char... character) {
    int bufPos = 0;
    try {
      StringBuilder sb = new StringBuilder();
      while (true) {
        int val = stream.read();
        if (val == -1) {
          break;
        }
        char ch = (char) val;
        if (ch == character[bufPos]) {
          bufPos++;
        } else {
          bufPos = 0;
        }
        sb.append(ch);
        if (bufPos >= character.length) {
          if (!includeDelim) {
            // truncate the delimiter
            sb.setLength(sb.length() - character.length);
          }
          break;
        }
      }

      return sb.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
