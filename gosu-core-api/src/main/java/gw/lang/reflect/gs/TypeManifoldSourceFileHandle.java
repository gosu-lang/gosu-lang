package gw.lang.reflect.gs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import manifold.api.type.ContributorKind;
import manifold.api.type.ITypeManifold;
import manifold.internal.javac.JavacPlugin;
import manifold.internal.javac.SourceJavaFileObject;
import manifold.util.JavacDiagnostic;


import static manifold.api.type.ContributorKind.Partial;
import static manifold.api.type.ContributorKind.Primary;

/**
 */
public class TypeManifoldSourceFileHandle extends LazyStringSourceFileHandle
{
  private Set<ITypeManifold> _typeManifolds;

  public TypeManifoldSourceFileHandle( Set<ITypeManifold> typeManifolds, String fqn )
  {
    super( typeManifolds.stream().findFirst().get().getPackage( fqn ), fqn, () -> compoundProduce( typeManifolds, fqn, null ), typeManifolds.stream().findFirst().get().getClassType( fqn ) );
    _typeManifolds = typeManifolds;
  }

  public Set<ITypeManifold> getTypeManifolds()
  {
    return _typeManifolds;
  }

  @Override
  //## todo: this method needs to return a collection of files to work better with producers
  public IFile getFile()
  {
    List<IFile> files = _typeManifolds.stream().flatMap( e -> e.findFilesForType( getTypeName() ).stream() ).collect( Collectors.toList() );
    if( files != null && files.size() > 0 )
    {
      return files.get( 0 );
    }
    return null;
  }

  private static String compoundProduce( Set<ITypeManifold> sps, String fqn, DiagnosticListener<JavaFileObject> errorHandler )
  {
    ITypeManifold found = null;
    String result = "";
    for( ITypeManifold sp : sps )
    {
      if( sp.getContributorKind() == Primary ||
          sp.getContributorKind() == Partial )
      {
        if( found != null && (found.getContributorKind() == Primary || sp.getContributorKind() == Primary) )
        {
          List<IFile> files = sp.findFilesForType( fqn );
          JavaFileObject file = new SourceJavaFileObject( files.get( 0 ).toURI() );
          errorHandler.report( new JavacDiagnostic( file, Diagnostic.Kind.ERROR, 0, 1, 1,
                                                    "The type, " + fqn + ", has conflicting type manifolds:\n" +
                                                    "'" + found.getClass().getName() + "' and '" + sp.getClass().getName() + "'.\n" +
                                                    "Either two or more resource files have the same base name or the project depends on two or more type manifolds that target the same resource type.\n" +
                                                    "If the former, consider renaming one or more of the resource files.\n" +
                                                    "If the latter, you must remove one or more of the type manifold libraries." ) );
        }
        else
        {
          found = sp;
          result = sp.contribute( null, fqn, result, errorHandler );
        }
      }
    }
    for( ITypeManifold sp : sps )
    {
      if( sp.getContributorKind() == ContributorKind.Supplemental )
      {
        result = sp.contribute( null, fqn, result, errorHandler );
      }
    }

//    if( result != null )
//    {
//      addToJavac( null, fqn );
//    }

    return result;
  }

  private static void addToJavac(JavaFileManager.Location location, String fqn) {
    JavacPlugin javacPlugin = JavacPlugin.instance();
    if (javacPlugin != null && javacPlugin.isStaticCompile()) {
      javacPlugin.addClassForCompilation(location, fqn);
    }
  }

}