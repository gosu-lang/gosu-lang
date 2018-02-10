package gw.lang.reflect.gs;

import manifold.api.type.IModel;
import manifold.api.type.ITypeManifold;
import manifold.api.type.ResourceFileTypeManifold;

/**
 * A base class for a Gosu type manifold that is based on a resource file of a specific extension.
 *
 * @param <M> The model you derive backing production of source code.
 */
public abstract class GosuTypeManifold<M extends IModel> extends ResourceFileTypeManifold<M>
{
  @Override
  public ITypeManifold.SourceKind getSourceKind()
  {
    return ITypeManifold.SourceKind.Gosu;
  }
}
