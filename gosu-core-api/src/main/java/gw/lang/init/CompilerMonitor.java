package gw.lang.init;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.BasicJavacTask;
import javax.lang.model.element.TypeElement;
import manifold.api.type.ICompilerComponent;
import manifold.internal.javac.IssueReporter;
import manifold.internal.javac.JavacPlugin;
import manifold.internal.javac.TypeProcessor;

public class CompilerMonitor implements ICompilerComponent, TaskListener
{
  private static final boolean DEBUG = false;

  @Override
  public void init( BasicJavacTask javacTask, TypeProcessor typeProcessor )
  {
    if( !DEBUG )
    {
      return;
    }

    if( JavacPlugin.instance() != null )
    {
      javacTask.addTaskListener( this );
    }
  }

  @Override
  public void started( TaskEvent e )
  {
    TypeElement elem = e.getTypeElement();
    if( elem != null )
    {
      new IssueReporter( () -> JavacPlugin.instance().getContext() ).reportInfo( e.getKind() + " : " + elem.getQualifiedName().toString() );
    }
  }

  @Override
  public void finished( TaskEvent e )
  {

  }
}
