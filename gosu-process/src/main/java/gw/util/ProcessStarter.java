/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @deprecated use {@link gw.util.process.ProcessRunner} instead
 */
@Deprecated
public class ProcessStarter {

  @SuppressWarnings({"UnusedDeclaration"})
  public static final OutputHandler IGNORE = new NullOutputHandler();

  @SuppressWarnings("redundant")
  private static final String CONSOLE_NEWLINE = new String( "\n" ); //necessary for the == below, to distinguish between
                                                                     //constructed new lines and real newlines
  private final ProcessBuilder _pb;

  private boolean _withCMD = false;
  private boolean _inludeStdErrInOutput;
  private boolean _dontThrowOnNonZeroReturn;
  private final String _rawCmd;
  private Writer _stdOut;
  private Writer _stdErr;
  private String _charset = null;

  public ProcessStarter(String command) {
    _rawCmd = command;
    ArrayList<String> args = new ArrayList<String>();
    args.addAll( parseCommandLine( command ) );
    _pb = new ProcessBuilder(args);
  }

  private List<String> parseCommandLine( String str )
  {
    List<String> strs = new ArrayList<String>();
    StringBuilder currentToken = new StringBuilder();

    boolean inString = false;
    char stringStart = '0';

    for( int i = 0; i < str.length(); i++ )
    {
      char c = str.charAt( i );

      if( Character.isWhitespace( c ) )
      {
        //keep whitespace in strings
        if( inString )
        {
          currentToken.append( c );
        }
        //If there is an existing token, store it and begin a new one
        else if( currentToken.length() > 0 )
        {
          strs.add( currentToken.toString() );
          currentToken.setLength( 0 );
        }
      }
      //support single or double quoted strings, each supporting the other unquoted within it
      else if( '\'' == c || '"' == c )
      {
        if( inString )
        {
          if( stringStart == c )
          {
            strs.add( currentToken.toString() );
            currentToken.setLength( 0 );
            inString = false;
          }
          else
          {
            currentToken.append( c );
          }
        }
        else
        {
          stringStart = c;
          inString = true;
        }
      }
      else if( '\\' == c )
      {
        if( inString )
        {
          if( i + 1 < str.length() )
          {
            //handle backslash escaping in string start characters only
            char nextC = str.charAt( i + 1 );
            if( nextC == stringStart )
            {
              currentToken.append( nextC );
              i++;
            }
            else
            {
              currentToken.append( c );
            }
          }
        }
        else
        {
          currentToken.append( c );
        }
      }
      else
      {
        currentToken.append( c );
      }
    }

    if( currentToken.length() > 0 )
    {
      strs.add( currentToken.toString() );
    }
    return strs;
  }

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and returns all content sent to standard out as a string.  If the command
   * finishes with a non zero return value, a {@link CommandFailedException} is thrown.</p>
   *
   * <p>Content sent to standard error by the command will be forwarded to standard error for this JVM.</p>
   *
   * <p>This method blocks on the execution of the command.</p>
   *
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   var currentDir = Shell.exec( "dir" ) // windows
   *   var currentDir = Shell.exec( "ls" )  // *nix
   *   Shell.exec( "rm -rf " + directoryToNuke )
   * </pre>
   * </p>
   *
   * @return the content of standard out
   * @throws CommandFailedException if the process finishes with a non-zero return value
   */
  public String exec()
  {
    Writer stdOut = _stdOut != null ? _stdOut : new StringWriter();
    Writer stdErr = _stdErr != null ? _stdErr : new StdErrWriter( _inludeStdErrInOutput ? stdOut : new StringWriter() );
    handleCommand( stdOut, stdErr );
    flush( stdOut );
    flush( stdErr );
    return stdOut.toString();
  }

  private void flush( Writer stdOut )
  {
    try
    {
      stdOut.flush();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and pipes all data sent to this processes stdout, stderr, and stdin.  If the command
   * finishes with a non zero return value, a {@link CommandFailedException} is thrown.</p>
   * <p>Stdout and Stderr from the sub-process will be piped to the current process' stdout and stderr.  Any input in
   * this processes stdin will be piped to the sub-process' stdin
   *
   * <p>Content sent to standard error by the command will be forwarded to standard error for this JVM. </p>
   *
   * <p>This method blocks on the execution of the command.</p>
   *
   * <p>
   * <b>Example Usages:</b>
   * <pre>
   *   Shell.execWithPipe( "read \"are you there?\"" )
   * </pre>
   * </p>
   *
   * @throws CommandFailedException if the process finishes with a non-zero return value
   */
  public void execWithPipe() {
    final Process process;
    try
    {
      process = startImpl();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }

    Thread outThread = new Thread(new StreamPipe(process.getInputStream(), System.out, false), "stdout");
    Thread errThread = new Thread(new StreamPipe(process.getErrorStream(), System.err, false), "stderr");
    StreamPipe inPipe = new StreamPipe(System.in, process.getOutputStream(), true);
    Thread inThread = new Thread(inPipe, "stdin");
    // kick off the gobblers
    errThread.start();
    outThread.start();
    inThread.start();

    try {
      int i = process.waitFor();
      inPipe.setDone();
      errThread.join();
      outThread.join();
      inThread.join();

      if( i != 0 )
      {
        String command = "";
        for (String c : _pb.command()) {
          command += c + " ";
        }
        StringBuilder s = new StringBuilder().append( "The command \"" ).append( command ).append("\" failed with code " ).append( i ).append( "." );

        throw new CommandFailedException( i, s.toString() );
      }
    } catch (InterruptedException e) {
      //ignore
    } finally {
      // close all three streams.
      try {
        process.getErrorStream().close();
      } catch ( IOException e) {
        e.printStackTrace();
      }
      try {
        process.getInputStream().close();
      } catch( IOException e ) {
        e.printStackTrace();
      }
      try {
        process.getOutputStream().close();
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }

  }

  private Process startImpl()
    throws IOException
  {
    if( OSPlatform.isWindows() && _withCMD )
    {
      _pb.command().clear();
      _pb.command().add( "CMD.EXE" );
      _pb.command().add( "/C" );
      _pb.command().add(_rawCmd);
    }
    return _pb.start();
  }

  /**
   * <p>Executes the given command as if it had been executed from the command line of the host OS
   * (cmd.exe on windows, /bin/sh on *nix) and calls the provided handler with the newly created process.
   *
   * <p><b>NOTE:</b> In gosu, you should take advantage of the block-to-interface coercion provided and pass
   * blocks in as the handler.  See the examples below.</p>
   * </p>

   * @param handler the process handler for this process.
   */
  public void processWithHandler(ProcessHandler handler) {
    Process process = null;
    try
    {
      process = startImpl();
      ShellProcess shellProcess = new ShellProcess(process);
      handler.run(shellProcess);
      try {
        process.exitValue();
      } catch (IllegalThreadStateException e) {
        // Process not yet dead so kill
        process.destroy();
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    } finally {
      if (process != null) {
        // close all three streams.
        try {
          process.getErrorStream().close();
        } catch ( IOException e) {
          e.printStackTrace();
        }
        try {
          process.getInputStream().close();
        } catch( IOException e ) {
          e.printStackTrace();
        }
        try {
          process.getOutputStream().close();
        } catch( IOException e ) {
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * Starts a new process using the attributes of this process starter.
   *
   * <p>The new process will
   * invoke the command and arguments given by {@link ProcessBuilder#command()},
   * in a working directory as given by {@link #getDirectory()},
   * with a process environment as given by {@link #getEnvironment()}.
   *
   * <p>This method calls directly to {@link ProcessBuilder#start() ProcessBuilder.start}.
   *
   * @return  A new {@link Process} object for managing the subprocess
   *
   * @throws  NullPointerException
   *          If an element of the command list is null
   *
   * @throws  IndexOutOfBoundsException
   *          If the command is an empty list (has size <code>0</code>)
   *
   * @throws  SecurityException
   *          If a security manager exists and its
   *          {@link SecurityManager#checkExec checkExec}
   *          method doesn't allow creation of the subprocess
   *
   * @throws  IOException
   *          If an I/O error occurs
   *
   * @see     Runtime#exec(String[], String[], java.io.File)
   * @see     SecurityManager#checkExec(String)
   */
  public Process start() throws IOException {
    return startImpl();
  }

  /**
   * Returns a modifiable string map view of this process' environment.
   *
   * Whenever a process starter is created, the environment is
   * initialized to a copy of the current process environment (see
   * {@link System#getenv()}).  Subprocesses subsequently started by
   * this object will use this map as their environment.
   *
   * <p>The returned object may be modified using ordinary {@link
   * java.util.Map Map} operations.  These modifications will be
   * visible to subprocesses.  Two <code>ProcessStarter</code> instances always
   * contain independent process environments, so changes to the
   * returned map will never be reflected in any other
   * <code>ProcessStarter</code> instance or the values returned by
   * {@link System#getenv System.getenv}.
   *
   * There are many system-dependant restrictions placed on the returned map.
   * See {@link ProcessBuilder ProcessBuilder} for more information
   *
   * @return  This process environment
   *
   * @throws  SecurityException
   *          If a security manager exists and its
   *          {@link SecurityManager#checkPermission checkPermission}
   *          method doesn't allow access to the process environment
   *
   * @see     ProcessBuilder
   * @see     System#getenv()
   */
  public Map<String, String> getEnvironment() {
    return _pb.environment();
  }

  /**
   * Returns this process' working directory.
   *
   * Subprocesses subsequently started by this object will use this as their working directory.
   * The returned value may be <code>null</code> -- this means to use
   * the working directory of the current Java process, usually the
   * directory named by the system property <code>user.dir</code>,
   * as the working directory of the child process.</p>
   *
   * @return  This process's working directory
   */
  public File getDirectory() {
    return _pb.directory();
  }

  /**
   * Sets this process' working directory.
   *
   * Subprocesses subsequently started by this object will use this as their working directory.
   * The returned value may be <code>null</code> -- this means to use
   * the working directory of the current Java process, usually the
   * directory named by the system property <code>user.dir</code>,
   * as the working directory of the child process.</p>
   *
   * @param directory This process' working directory
   */
  public void setDirectory(File directory) {
    _pb.directory(directory);
  }

  private void handleCommand( Writer stdOut, Writer stdErr )
  {
    Process process;
    try
    {
      process = startImpl();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    Gobbler outputGobbler = new Gobbler( process.getInputStream(), stdOut, _charset );
    Gobbler errorGobbler = new Gobbler( process.getErrorStream(), stdErr, _charset );

    // kick off the gobblers
    errorGobbler.start();
    outputGobbler.start();

    try {
      int i = process.waitFor();
      errorGobbler.join();
      outputGobbler.join();

      if( i != 0 )
      {
        String command = "";
        for (String c : _pb.command()) {
          command += c + " ";
        }
        StringBuilder s = new StringBuilder().append( "The command \"" ).append( command ).append("\" failed with code " ).append( i ).append( "." );

        if( stdErr instanceof StdErrWriter )
        {
          s.append( "  StdErr was : \n").append("\n" ).append( indent( ((StdErrWriter)stdErr).getString() ) );
        }

        if( !_dontThrowOnNonZeroReturn )
        {
          throw new CommandFailedException( i, s.toString() );
        }
      }
    } catch (InterruptedException e) {
      //ignore
    } finally {
      // close all three streams.
      try {
        process.getErrorStream().close();
      } catch ( IOException e) {
        e.printStackTrace();
      }
      try {
        process.getInputStream().close();
      } catch( IOException e ) {
        e.printStackTrace();
      }
      try {
        process.getOutputStream().close();
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
  }

  private static String indent( String string )
  {
    String[] strings = string.split( System.getProperty( "line.separator" ) );
    StringBuilder sb = new StringBuilder();
    for( String s : strings )
    {
      sb.append( "  " ).append( s );
    }
    return sb.toString();
  }

  public ProcessStarter withCharset(String cs) {
    _charset = cs;
    return this;
  }

  private static class Gobbler extends Thread {
    private InputStream _streamToGobble;
    private Writer _buffer;
    private String _gobblerCharset = null;

    public Gobbler( InputStream streamToGobble, Writer buffer, String charSet )
    {
      _streamToGobble = streamToGobble;
      _buffer = buffer;
      _gobblerCharset = charSet;
    }

    @Override
    public void run()
    {
      try
      {
        Reader inputStreamReader;
        if (_gobblerCharset == null) {
          inputStreamReader = StreamUtil.getInputStreamReader(_streamToGobble);
        } else {
          inputStreamReader = StreamUtil.getInputStreamReader(_streamToGobble, _gobblerCharset);
        }
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line;
        while( (line = br.readLine()) != null )
        {
          _buffer.append( line ).append( CONSOLE_NEWLINE );
        }
      } catch (IOException ioe) {
        //ignore
      }
    }
  }

  private static class StdErrWriter extends Writer
  {
    Writer _str;

    public StdErrWriter( Writer stringWriter )
    {
      _str = stringWriter;
    }

    public void write( char cbuf[], int off, int len ) throws IOException
    {
      System.err.print( new String( cbuf, off, len ) );
      _str.write( cbuf, off, len );
    }

    public void flush() throws IOException
    {
      _str.flush();
    }

    public void close() throws IOException
    {
      _str.close();
    }

    public String getString() {
      return _str.toString();
    }
  }

  public interface OutputHandler {
    public void handleLine( String line );
  }

  public interface ProcessHandler {
    public void run( ShellProcess proc );
  }

  private static class HandlerWriter extends Writer
  {
    private final OutputHandler _handler;

    public HandlerWriter( OutputHandler handler )
    {
      _handler = handler;
    }

    @Override
      public void write( String str, int off, int len ) throws IOException
    {
      //ignore this warning
      if( str != CONSOLE_NEWLINE )
      {
        _handler.handleLine( str );
      }
    }

    public void write( char cbuf[], int off, int len ) throws IOException
    {
    }

    public void flush() throws IOException
    {
    }

    public void close() throws IOException
    {
    }

    @Override
    public String toString()
    {
      return ""; //returns no output
    }
  }

  private static class StreamPipe implements Runnable {
    private OutputStream _outStream;
    private InputStream _inStream;
    private boolean _poll;
    private boolean _done = false;

    public StreamPipe(InputStream inStream, OutputStream outStream, boolean poll) {
      _inStream = inStream;
      _outStream = outStream;
      _poll = poll;
    }

    public void setDone() {
      _done = true;
    }
    
    public void run() {
      try {
        byte buf[] = new byte[4096];
        BufferedInputStream bis = new BufferedInputStream(_inStream);
        while (!_done) {
          int avail = bis.available();
          if (avail > 0) {
            if (avail > 4096) {
              avail = 4096;
            }

            int numRead = bis.read(buf, 0, avail);
            if (numRead == 0) {
              break;
            }
            try {
              _outStream.write(buf, 0, numRead);
              _outStream.flush();
            } catch (IOException ex) {
              break;
            }
          } else if (!_poll) {
            int ch = bis.read();
            if (ch == -1) {
              break;
            }
            try {
              _outStream.write(ch);
              _outStream.flush();
            } catch (IOException ex) {
              break;
            }
          } else {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              break;
            }
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //=================================================================================
  // Builder methods
  //=================================================================================

  /**
   * The process built up will use CMD if this is a windows platform.  This is necessary because on windows certain
   * basic commands such as "dir" are not programs, but rather are built into CMD.  Thanks, Microsoft.
   */
  public ProcessStarter withCMD()
  {
    _withCMD = true;
    return this;
  }

  /**
   * If called, this ProcessStarter will include the StdErr output in the return string of this
   * process.  Note that this has no effect if {@link #withStdErrHandler(OutputHandler)} is called.
   * 
   * @return this object for chaining
   */
  public ProcessStarter includeStdErrInOutput()
  {
    _inludeStdErrInOutput = true;
    return this;
  }

  /**
   * If called, this ProcessStarter will not throw an exception if the underlying process exits with
   * a non-zero return code
   *
   * @return this object for chaining
   */
  public ProcessStarter doNotThrowOnNonZeroReturnVal()
  {
    _dontThrowOnNonZeroReturn = true;
    return this;
  }

  /**
   * @param stdErrHandler handler that will be called with every line of output to stderr
   *
   * @return this object for chaining
   */
  public ProcessStarter withStdErrHandler( OutputHandler stdErrHandler )
  {
    _stdErr = new HandlerWriter( stdErrHandler );
    return this;
  }

  /**
   * @param stdOutHandler handler that will be called with every line of output to stdout
   *
   * @return this object for chaining
   */
  public ProcessStarter withStdOutHandler( OutputHandler stdOutHandler )
  {
    _stdOut = new HandlerWriter( stdOutHandler );
    return this;
  }
  
  public static class NullOutputHandler implements OutputHandler
  {
    public void handleLine( String line ) {}
  }
}
