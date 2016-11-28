package editor.plugin.typeloader.java;

import editor.EditorHost;
import editor.util.HTMLEscapeUtil;
import gw.lang.javac.JavaIssueContainer;
import gw.lang.IIssueContainer;
import editor.plugin.typeloader.INewFileParams;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.javac.IJavaParser;
import gw.lang.javac.StringJavaFileObject;
import gw.lang.parser.GosuParserFactory;
import gw.lang.reflect.IType;
import java.awt.EventQueue;
import java.util.Collections;
import javax.swing.JComponent;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class JavaTypeFactory implements ITypeFactory
{
  @Override
  public boolean canCreate()
  {
    return false;
  }

  @Override
  public String getFileExtension()
  {
    return ".java";
  }

  @Override
  public String getName()
  {
    return "Class";
  }

  @Override
  public String getIcon()
  {
    return "images/javaclass.png";
  }

  @Override
  public INewFileParams makeDefaultParams( String fqn )
  {
    return new JavaFileParams( fqn );
  }

  @Override
  public JComponent makePanel( INewFileParams params )
  {
    return null;
  }

  @Override
  public CharSequence createNewFileContents( INewFileParams params )
  {
//    String fqn = params.getFqn();
//    String pkg = GosuClassUtil.getPackage( fqn );
//    String eol = System.getProperty( "line.separator" );
//    return "package " + pkg + ";" + eol +
//           eol +
//           "public class " + GosuClassUtil.getShortClassName( fqn ) + " {" + eol +
//           eol +
//           "}" + eol;
    throw new UnsupportedOperationException();
  }

  @Override
  public JavaEditorKit makeEditorKit()
  {
    return new JavaEditorKit();
  }

  public void parse( IType type, String strText, boolean forceCodeCompletion, boolean changed, EditorHost editor )
  {
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    StringJavaFileObject fileObj = new StringJavaFileObject( type.getName(), strText );
    javaParser.compile( fileObj, type.getName(), Collections.singleton( "-Xlint:unchecked" ), errorHandler );
    EventQueue.invokeLater(
      () ->
      {
        ((JavaDocument)editor.getDocument()).setErrorHandler( errorHandler );
        editor.getEditor().repaint();
      } );
  }

  @Override
  public boolean canAddBreakpoint( IType type, int line )
  {
    //## todo: maybe store the results of the parse() above in the document and then have the document determine if the line can have a breakpoint
    return true;
  }

  @Override
  public String getTooltipMessage( int iPos, EditorHost editor )
  {
    return ((JavaDocument)editor.getDocument()).findErrorMessage( iPos );
  }

  @Override
  public IIssueContainer getIssueContainer( EditorHost editor )
  {
    return new JavaIssueContainer( ((JavaDocument)editor.getDocument()).getErrorHandler() );
  }
}
