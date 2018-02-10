package gw.lang.reflect.json;

import java.util.Set;
import javax.script.ScriptException;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import manifold.api.fs.IFile;
import javax.script.Bindings;
import javax.script.SimpleBindings;
import manifold.api.type.AbstractSingleFileModel;
import manifold.api.type.ResourceFileTypeManifold;
import manifold.internal.javac.IIssue;
import manifold.internal.javac.SourceJavaFileObject;
import manifold.util.JavacDiagnostic;

/**
 */
class Model extends AbstractSingleFileModel
{
  private JsonStructureType _type;
  private JsonIssueContainer _issues;

  public Model( String fqn, Set<IFile> files )
  {
    super( fqn, files );

    Bindings bindings;
    try
    {
      bindings = Json.fromJson( ResourceFileTypeManifold.getContent( getFile() ) );
    }
    catch( Exception e )
    {
      Throwable cause = e.getCause();
      if( cause instanceof ScriptException )
      {
        _issues = new JsonIssueContainer( (ScriptException)cause, getFile() );
      }
      bindings = new SimpleBindings();
    }

    _type = (JsonStructureType)Json.transformJsonObject( getFile().getBaseName(), null, bindings );
  }

  public JsonStructureType getType()
  {
    return _type;
  }

  public void report( DiagnosticListener<JavaFileObject> errorHandler )
  {
    if( _issues == null || errorHandler == null )
    {
      return;
    }

    JavaFileObject file = new SourceJavaFileObject( getFile().toURI() );
    for( IIssue issue: _issues.getIssues() )
    {
      Diagnostic.Kind kind = issue.getKind() == IIssue.Kind.Error ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
      errorHandler.report( new JavacDiagnostic( file, kind, issue.getStartOffset(), issue.getLine(), issue.getColumn(), issue.getMessage() ) );
    }
  }

}
