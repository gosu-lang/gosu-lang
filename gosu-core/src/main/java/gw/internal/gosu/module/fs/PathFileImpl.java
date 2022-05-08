package gw.internal.gosu.module.fs;

import gw.fs.IFile;
import manifold.api.fs.IFileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class PathFileImpl extends PathResourceImpl implements IFile
{
  PathFileImpl( Path path )
  {
    super( path );
  }

  @Override
  public String getContent() throws IOException {
    try (FileChannel channel = new RandomAccessFile(get_Path().toFile(), "r").getChannel()) {
      return StandardCharsets.UTF_8.decode(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())).toString();
    }
  }

  @Override
  public InputStream openInputStream() throws IOException
  {
    return Files.newInputStream( get_Path() );
  }

  @Override
  public OutputStream openOutputStream() throws IOException
  {
    return Files.newOutputStream( get_Path() );
  }

  @Override
  public OutputStream openOutputStreamForAppend() throws IOException
  {
    return Files.newOutputStream( get_Path(), StandardOpenOption.CREATE, StandardOpenOption.APPEND );
  }

  @Override
  public String getExtension()
  {
    return IFileUtil.getExtension( get_Path().getFileName().toString() );
  }

  @Override
  public String getBaseName()
  {
    return IFileUtil.getBaseName( get_Path().getFileName().toString() );
  }

  @Override
  public boolean exists()
  {
    return Files.exists( get_Path() );
  }
}
