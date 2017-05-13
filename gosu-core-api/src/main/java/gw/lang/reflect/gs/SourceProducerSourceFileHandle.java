package gw.lang.reflect.gs;

import manifold.api.fs.IFile;
import manifold.api.sourceprod.ISourceProducer;

/**
 */
public class SourceProducerSourceFileHandle extends LazyStringSourceFileHandle
{
  private ISourceProducer _sourceProducer;

  public SourceProducerSourceFileHandle( String fqn, ISourceProducer sourceProducer )
  {
    super( sourceProducer.getPackage( fqn ), fqn, () -> sourceProducer.produce( fqn ), sourceProducer.getClassType( fqn ) );
    _sourceProducer = sourceProducer;
  }

  public ISourceProducer getSourceProducer()
  {
    return _sourceProducer;
  }

  @Override
  public IFile getFile()
  {
    return _sourceProducer.findFileForType( getTypeName() );
  }
}