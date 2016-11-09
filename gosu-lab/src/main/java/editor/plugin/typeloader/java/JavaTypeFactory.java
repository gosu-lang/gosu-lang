package editor.plugin.typeloader.java;

import editor.plugin.typeloader.INewFileParams;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.reflect.IType;
import javax.swing.JComponent;

/**
 */
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

  public void parse( IType type, JComponent editor )
  {
//    List<CompilationUnitTree> trees = new ArrayList<>();
//    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
//    JavaParser.instance().parse( ((IJavaType)type).getSourceFileHandle().getSource().getSource(), trees, errorHandler );
  }
}
