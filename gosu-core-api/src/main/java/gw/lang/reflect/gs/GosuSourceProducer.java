package gw.lang.reflect.gs;

import manifold.api.sourceprod.ResourceFileSourceProducer;

/**
 * A base class for a Gosu source producer that is based on a resource file of a specific extension.
 *
 * @param <M> The model you derive backing production of source code.
 */
public abstract class GosuSourceProducer<M extends ResourceFileSourceProducer.IModel> extends ResourceFileSourceProducer<M>
{
  @Override
  public SourceKind getSourceKind()
  {
    return SourceKind.Gosu;
  }
}
