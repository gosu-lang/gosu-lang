/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.util.ILogger;

import java.io.IOException;
import java.io.OutputStream;

public class DebugLoggingOutputStream extends OutputStream {

  private final OutputStream _delegate;
  private final ILogger _logger;

  public DebugLoggingOutputStream( OutputStream delegate, ILogger logger ) {
    _delegate = delegate;
    _logger = logger;
  }

  @Override
  public void write( int b ) throws IOException {
    try {
      _delegate.write( b );
    }
    catch ( IOException ex ) {
      _logger.debug( "Unable to write to stream", ex );
    }
  }

  @Override
  public void write( byte[] b ) throws IOException {
    try {
      _delegate.write( b );
    }
    catch ( IOException ex ) {
      _logger.debug( "Unable to write to stream", ex );
    }
  }

  @Override
  public void write( byte[] b, int off, int len ) throws IOException {
    try {
      _delegate.write( b, off, len );
    }
    catch ( IOException ex ) {
      _logger.debug( "Unable to write to stream", ex );
    }

  }

  @Override
  public void flush() throws IOException {
    try {
      _delegate.flush();
    }
    catch ( IOException ex ) {
      _logger.debug( "Unable to write to stream", ex );
    }
  }

  @Override
  public void close() throws IOException {
    try {
      _delegate.close();
    }
    catch ( IOException ex ) {
      _logger.debug( "Unable to write to stream", ex );
    }
  }

}
