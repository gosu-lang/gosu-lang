/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

/**
 * This will extract the header from the input stream
 */
public class ExtractHeaderFromIS extends InputStream {
  final private InputStream _wrapped;
  final private byte[] _buf = new byte[1024];
  private int _len;
  private int _pos;

  public ExtractHeaderFromIS(InputStream inputStream) throws IOException {
    _wrapped = inputStream;
    _len = _wrapped.read(_buf);
  }

  public InputStream getHeader() {
    int len = 0;
    while (len != _buf.length) {
      if (_buf[len] == '>' && _buf[len -1] != '?' ) {
        break;
      }
      len++;
    }
    _buf[len++] = '/';
    _buf[len++] = '>';
    return new ByteArrayInputStream(_buf, 0, len);
  }

  @Override
  public int available() throws IOException {
    return _wrapped.available() + (_len - _pos);
  }

  @Override
  public void close() throws IOException {
    _wrapped.close();
  }

  @Override
  public void mark(int readAheadLimit) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean markSupported() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int read() throws IOException {
    if (_pos < _len) {
      return _buf[_pos++];
    }
    return _wrapped.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (_pos < _len) {
      return move(b, off, len);
    }
    return _wrapped.read(b, off, len);
  }

  @Override
  public void reset()  {
    throw new UnsupportedOperationException();
  }

  @Override
  public long skip(long n) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int read(byte[] b) throws IOException {
    if (_pos < _len) {
      return move(b, 0, b.length);
    }
    return _wrapped.read(b);
  }

  private int move(byte[] b, int off, int len) {
    int cnt = (_len - _pos);
    len = len < cnt ? len : cnt;
    System.arraycopy(_buf, _pos, b, off, len);
    _pos += len;
    return len;
  }

}
