package gw.lang.reflect.gs;

import gw.fs.IFile;
import gw.lang.reflect.ITypeLoader;
import gw.util.concurrent.LocklessLazyVar;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A base class for a Java source producer that is based on a resource file of a specific extension.
 *
 * @param <M> The model you derive backing production of source code.
 */
public abstract class JavaSourceProducer<M> extends ResourceFileSourceProducer<M>
{
  public JavaSourceProducer( ITypeLoader typeLoader, String extension, BiFunction<String, IFile, M> modelMapper )
  {
    super( typeLoader, extension, modelMapper );
  }

  public JavaSourceProducer( ITypeLoader typeLoader, String extension, BiFunction<String, IFile, M> modelMapper, String typeFactoryFqn, Map<String, LocklessLazyVar<M>> peripheralTypes )
  {
    super( typeLoader, extension, modelMapper, typeFactoryFqn, peripheralTypes );
  }

  @Override
  public SourceKind getSourceKind()
  {
    return SourceKind.Java;
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.JavaClass;
  }
}
