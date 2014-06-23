/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;
import gw.util.StreamUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A provider of binary data, for XSD base64Binary datatypes. The backing content can be read or assigned as
 * an input stream or a byte array. If the backing content is an input stream, and the data is read as
 * an input stream, the backing content becomes invalid for additional reads. For example, if an MTOM web
 * service is invoked, the backing content will be an input stream for any XOP-encoded base64Binary data.
 * This data can be read in its input stream form, but only once, to avoid the need to read the data into
 * memory or store it to disk. If the content is known to be small in size, the Bytes property can be used
 * instead, at which point this BinaryData will read the entire input stream into memory, after which
 * it can be read multiple times. The current multi-read state of this binary data can be inspected
 * using the MultiRead property.
 */
@PublishInGosu
public final class BinaryData {

  private byte[] _bytes = {};
  private InputStream _inputStream;

  /**
   * Creates a new binary data, backed by a zero-length byte array.
   */
  public BinaryData() {
  }

  /**
   * Creates a new binary data, backed by the specified input stream.
   * @param inputStream the input stream
   */
  public BinaryData(InputStream inputStream) {
    setInputStream(inputStream);
  }

  /**
   * Creates a new binary data, backed by the specified byte array.
   *
   * @param bytes the backing bytes
   */
  public BinaryData(byte[] bytes) {
    setBytes(bytes);
  }

  /**
   * Returns the backing content as a byte array. If the backing content was previously an input stream,
   * invoking this property will cause the entire content of the input stream to be read into memory, and
   * the backing content will thereafter be the resulting byte array.
   * @return the backing content as a byte array
   * @throws IOException if an I/O error occurs
   * @throws XmlException If the backing data is invalid
   */
  public byte[] getBytes() throws IOException {
    if ( _bytes == null ) {
      _bytes = StreamUtil.getContent( getInputStream() );
    }
    return _bytes;
  }

  /**
   * Returns the backing content as an input stream. If the backing content is an input stream, invoking
   * this property will cause the backing content to become invalid.
   * @return the backing content as an input stream
   * @throws XmlException If the backing data is invalid
   */
  public InputStream getInputStream() {
    if ( _inputStream == null ) {
      if ( _bytes == null ) {
        throw new XmlException( "Binary data has already been read" );
      }
      return new ByteArrayInputStream( _bytes );
    }
    InputStream ret = _inputStream;
    _inputStream = null; // can only be called once if backed by an actual input stream
    return ret;
  }

  /**
   * Sets the backing content as a byte array. If the backing content was previously invalid, setting
   * this property will cause it to become valid.
   * @param bytes the new backing byte array
   */
  public void setBytes( byte[] bytes ) {
    if ( bytes == null ) {
      throw new IllegalArgumentException( "bytes cannot be null" );
    }
    _bytes = bytes;
    _inputStream = null;
  }

  /**
   * Sets the backing content as an input stream. If the backing content was previously invalid, setting
   * this property will cause it to become valid.
   * @param inputStream the new backing input stream
   */
  public void setInputStream( InputStream inputStream ) {
    if ( inputStream == null ) {
      throw new IllegalArgumentException( "inputStream cannot be null" );
    }
    _inputStream = inputStream;
    _bytes = null;
  }

  /**
   * Returns true if the backing content can be accessed multiple times via the InputStream property. This is
   * only true if the backing content is currently a byte array. Keep in mind that accessing the Bytes property
   * on a previously non-multi-read provider will cause the entire contents of the backing input stream to
   * be read into memory and stored, causing the provider to become a multi-read provider.
   * @return true if the backing content can be accessed multiple times via the InputStream property
   */
  public boolean isMultiRead() {
    return _inputStream == null;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    BinaryData that = (BinaryData) o;

    if ( ! isMultiRead() || ! that.isMultiRead() ) {
      return false;
    }

    return Arrays.equals( _bytes, that._bytes );

  }

  @Override
  public int hashCode() {
    return _bytes != null ? Arrays.hashCode( _bytes ) : 0;
  }
}
