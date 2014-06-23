/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectClient {
  private static boolean matches(byte[] buffer, int offset, byte[] search) {
    final int len = buffer.length;
    for (int i = 0; i < len; ++i) {
      if (search[i] != buffer[(offset + i) % len]) {
        return false;
      }
    }
    return true;
  }

  private static void skipMarker(InputStream stream) throws IOException {
    final byte[] buf = new byte[MARKER.length];
    int cnt = stream.read(buf);
    if (cnt != buf.length) {
      throw new EOFException("Cannot skip marker");
    }

    // Continuously match cyclic buffer against the marker
    int offset = 0;
    while (true) {
      if (matches(buf, offset, MARKER)) {
        break;
      }
      int c = stream.read();
      if (c == -1) {
        throw new EOFException("Cannot skip marker");
      }
      buf[offset] = (byte) c;
      offset = (offset + 1) % buf.length;
    }
  }

  private static final byte[] MARKER = "###STREAM_MARKER###".getBytes();

  private ObjectInputStream in;
  private ObjectOutputStream out;

  private InputStream rawIn;
  private OutputStream rawOut;

  public ObjectClient(InputStream rawIn, OutputStream rawOut) {
    this.rawIn = rawIn;
    this.rawOut = rawOut;
  }

  public Object read() throws IOException {
    if (in == null) {
      skipMarker(rawIn);
      in = new ObjectInputStream(new BufferedInputStream(rawIn,1<<16));  //64K buffer
    }

    try {
      return in.readObject();
    } catch (Exception e) {
      throw new IOException("Error while reading object from the socket.", e);
    }
  }

  public void write(Object obj, boolean flush) throws IOException {
    if (out == null) {
      rawOut.write(MARKER);
      out = new ObjectOutputStream(new BufferedOutputStream(rawOut,1<<16)); //64K buffer
    }

    try {
      out.writeObject(obj);
    } catch (IOException e) {
      throw new IOException(String.format("Error while writing object '%s' to the socket.", obj), e);
    }

    if (flush) {
      out.flush();
    }
  }
}
