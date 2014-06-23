/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@UnstableAPI
public interface IFile extends IResource {
  IFile[] EMPTY_ARRAY = new IFile[0];

  InputStream openInputStream() throws IOException;

  OutputStream openOutputStream() throws IOException;

  OutputStream openOutputStreamForAppend() throws IOException;

  String getExtension();

  String getBaseName();
}
