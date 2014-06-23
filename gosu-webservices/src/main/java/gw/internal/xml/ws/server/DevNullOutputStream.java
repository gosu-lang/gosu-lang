/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import java.io.IOException;
import java.io.OutputStream;

public class DevNullOutputStream extends OutputStream {

  @Override
  public void write( int b ) throws IOException {

  }

  @Override
  public void write( byte[] b ) throws IOException {

  }

  @Override
  public void write( byte[] b, int off, int len ) throws IOException {

  }

}
