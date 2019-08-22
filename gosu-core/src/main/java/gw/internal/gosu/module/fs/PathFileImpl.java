package gw.internal.gosu.module.fs;

import gw.fs.IFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import manifold.api.fs.IFileUtil;

public class PathFileImpl extends PathResourceImpl implements IFile
{
  PathFileImpl( Path path )
  {
    super( path );
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
