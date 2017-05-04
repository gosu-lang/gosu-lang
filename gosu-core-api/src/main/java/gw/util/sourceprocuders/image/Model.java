package gw.util.sourceprocuders.image;

import gw.fs.IFile;
import gw.lang.reflect.gs.ResourceFileSourceProducer;
import java.net.MalformedURLException;

/**
 */
class Model implements ResourceFileSourceProducer.IModel
{
  String _fqn;
  String _url;
  IFile _file;

  public Model( String fqn, IFile file )
  {
    _fqn = fqn;
    _file = file;
    try
    {
      _url = file.toURI().toURL().toString();
    }
    catch( MalformedURLException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String getFqn()
  {
    return _fqn;
  }

  @Override
  public IFile getFile()
  {
    return _file;
  }

  public String getUrl()
  {
    return _url;
  }
}
