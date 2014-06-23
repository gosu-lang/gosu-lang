/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.fs.IFile;
import gw.fs.IFileUtil;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

public class JavaFileImpl extends JavaResourceImpl implements IFile {

  public JavaFileImpl(File file) {
    super(file);
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return new FileInputStream(_file);
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return new FileOutputStream(_file);
  }

  @Override
  public OutputStream openOutputStreamForAppend() throws IOException {
    return new FileOutputStream(_file, true);
  }

  @Override
  public String getExtension() {
    return IFileUtil.getExtension(this);
  }

  @Override
  public String getBaseName() {
    return IFileUtil.getBaseName(this);
  }

  @Override
  public boolean create() {
    try {
      return _file.createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean exists() {
    return _file.isFile();
  }
}
