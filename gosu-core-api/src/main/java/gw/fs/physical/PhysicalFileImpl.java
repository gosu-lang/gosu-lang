/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical;

import gw.fs.IFile;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@UnstableAPI
public class PhysicalFileImpl extends PhysicalResourceImpl implements IFile {

  public PhysicalFileImpl(ResourcePath path, IPhysicalFileSystem backingFileSystem) {
    super(path, backingFileSystem);
  }

  @Override
  public String getContent() throws IOException {
    try (FileChannel channel = new RandomAccessFile(toJavaFile(), "r").getChannel()) {
      return StandardCharsets.UTF_8.decode(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())).toString();
    }
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return Channels.newInputStream(new RandomAccessFile(toJavaFile(), "r").getChannel());
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return Channels.newOutputStream(new RandomAccessFile(toJavaFile(), "w").getChannel());
  }

  @Override
  public OutputStream openOutputStreamForAppend() throws IOException {
    return new FileOutputStream(toJavaFile(), true);
  }

  @Override
  public String getExtension() {
    int lastDot = getName().lastIndexOf(".");
    if (lastDot != -1) {
      return getName().substring(lastDot + 1);
    } else {
      return "";
    }
  }

  @Override
  public String getBaseName() {
    int lastDot = getName().lastIndexOf(".");
    if (lastDot != -1) {
      return getName().substring(0, lastDot);
    } else {
      return getName();
    }
  }

  @Override
  public boolean create() {
    try {
      return toJavaFile().createNewFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
