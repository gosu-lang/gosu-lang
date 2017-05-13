package gw.lang.reflect.json;

import manifold.api.fs.IFile;
import javax.script.Bindings;
import javax.script.SimpleBindings;
import manifold.api.sourceprod.ResourceFileSourceProducer;

/**
 */
class Model implements ResourceFileSourceProducer.IModel
{
  String _fqn;
  IFile _file;
  JsonStructureType _type;

  public Model( String fqn, IFile file )
  {
    _fqn = fqn;
    _file = file;
    Bindings bindings;
    try
    {
      bindings = Json.fromJson( ResourceFileSourceProducer.getContent( file ) );
    }
    catch( Exception e )
    {
      bindings = new SimpleBindings();
    }

    _type = (JsonStructureType)Json.transformJsonObject( file.getBaseName(), null, bindings );
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

  public JsonStructureType getType()
  {
    return _type;
  }
}
