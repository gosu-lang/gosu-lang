package gw.lang.reflect.gs;

import java.util.List;
import manifold.api.fs.IFile;
import manifold.api.sourceprod.ISourceProducer;

/**
 */
public class SourceProducerSourceFileHandle extends LazyStringSourceFileHandle
{
  private ISourceProducer _sourceProducer;

  public SourceProducerSourceFileHandle( String fqn, ISourceProducer sourceProducer )
  {
    super( sourceProducer.getPackage( fqn ), fqn, () -> sourceProducer.produce( fqn, null ), sourceProducer.getClassType( fqn ) );
    _sourceProducer = sourceProducer;
  }

  public ISourceProducer getSourceProducer()
  {
    return _sourceProducer;
  }

  @Override
  //## todo: this method needs to return a collection of files to work better with producers
  public IFile getFile()
  {
    List<IFile> files = _sourceProducer.findFilesForType( getTypeName() );
    if( files != null && files.size() > 0 )
    {
      return files.get( 0 );
    }
    return null;
  }
}