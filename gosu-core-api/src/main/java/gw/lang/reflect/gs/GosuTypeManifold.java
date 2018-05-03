package gw.lang.reflect.gs;

import manifold.api.type.IModel;
import manifold.api.type.ISourceKind;
import manifold.api.type.ResourceFileTypeManifold;

/**
 * A base class for a Gosu type manifold that is based on a resource file of a specific extension.
 *
 * @param <M> The model you derive backing production of source code.
 */
public abstract class GosuTypeManifold<M extends IModel> extends ResourceFileTypeManifold<M>
{
  public static final ISourceKind Gosu = new ISourceKind() {};

  @Override
  public ISourceKind getSourceKind()
  {
    return Gosu;
  }
}
