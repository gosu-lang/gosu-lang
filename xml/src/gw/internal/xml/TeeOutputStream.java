/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends OutputStream {

  private OutputStream _out1;
  private OutputStream _out2;

  public TeeOutputStream( OutputStream out1, OutputStream out2 ) {
    _out1 = out1;
    _out2 = out2;
  }

  @Override
  public void write( int b ) throws IOException {
    _out1.write( b );
    _out2.write( b );
  }

  @Override
  public void write( byte[] b ) throws IOException {
    _out1.write( b );
    _out2.write( b );
  }

  @Override
  public void write( byte[] b, int off, int len ) throws IOException {
    _out1.write( b, off, len );
    _out2.write( b, off, len );
  }

  @Override
  public void flush() throws IOException {
    _out1.flush();
    _out2.flush();
  }

  @Override
  public void close() throws IOException {
    _out1.close();
    _out2.close();
  }
  
}
