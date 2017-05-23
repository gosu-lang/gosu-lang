package editor.plugin.typeloader.json;

import editor.EditorHost;
import editor.plugin.typeloader.INewFileParams;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import manifold.api.json.JsonIssueContainer;
import manifold.api.sourceprod.ISourceProducer;
import gw.lang.reflect.json.Json;
import java.awt.EventQueue;
import javax.script.ScriptException;
import javax.swing.JComponent;
import javax.swing.text.StyledEditorKit;
import manifold.internal.javac.IIssueContainer;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class JsonTypeFactory implements ITypeFactory
{
  public JsonTypeFactory()
  {
  }

  @Override
  public boolean canCreate()
  {
    return true;
  }

  @Override
  public String getFileExtension()
  {
    return ".json";
  }

  @Override
  public String getName()
  {
    return "Json";
  }

  @Override
  public String getIcon()
  {
    return "images/json_type.png";
  }

  @Override
  public JsonFileParams makeDefaultParams( String fqn )
  {
    return new JsonFileParams( fqn );
  }

  @Override
  public String createNewFileContents( INewFileParams params )
  {
    String eol = System.getProperty( "line.separator" );
    return "{"+eol +
           "  \"Name\": \"Sample Guy\","+eol +
           "  \"Age\": 39,"+eol +
           "  \"Address\": {"+eol +
           "    \"Street\": \"Palm Dr\","+eol +
           "    \"City\": \"Golden Shores\","+eol +
           "    \"State\": \"FL\""+eol +
           "  },"+eol +
           "  \"Hobby\": ["+eol +
           "    {"+eol +
           "      \"Category\": \"Sport\","+eol +
           "      \"Name\": \"Baseball\""+eol +
           "    },"+eol +
           "    {"+eol +
           "      \"Category\": \"Recreation\","+eol +
           "      \"Name\": \"Fishing\""+eol +
           "    }"+eol +
           "  ]"+eol +
           "}";
  }

  @Override
  public JComponent makePanel( INewFileParams params )
  {
    return null;
  }

  @Override
  public StyledEditorKit makeEditorKit()
  {
    //## todo: editor kit for json
    return null;
  }

  @Override
  public void parse( IType type, String strText, boolean forceCodeCompletion, boolean changed, EditorHost editor )
  {
    try
    {
      Json.fromJson( strText );

      // notify of no errors
      JsonIssueContainer issues = new JsonIssueContainer();
      editor.getDocument().putProperty( "issues", issues );
    }
    catch( Exception e )
    {
      Throwable cause = e.getCause();
      if( cause instanceof ScriptException )
      {
        // notify of errors
        JsonIssueContainer issues = new JsonIssueContainer( (ScriptException)cause, ((IFileRepositoryBasedType)type).getSourceFileHandle().getFile() );
        editor.getDocument().putProperty( "issues", issues );
      }
    }
    EventQueue.invokeLater( () -> editor.getEditor().repaint() );
  }

  @Override
  public boolean canAddBreakpoint( IType type, int line )
  {
    return false;
  }

  @Override
  public String getTypeAtOffset( IType type, int offset )
  {
    return type.getName();
  }

  @Override
  public String getTooltipMessage( int iPos, EditorHost editor )
  {
    //## todo: get error message for iPos
    return null;
  }

  @Override
  public IIssueContainer getIssueContainer( EditorHost editor )
  {
    IIssueContainer issues = (IIssueContainer)editor.getDocument().getProperty( "issues" );
    return issues == null ? new JsonIssueContainer() : issues;
  }

  @Override
  public boolean handlesType( IType type )
  {
    if( type instanceof IGosuClass )
    {
      ISourceFileHandle sfh = ((IGosuClass)type).getSourceFileHandle();
      if( sfh != null )
      {
        ISourceProducer sourceProducer = sfh.getSourceProducer();
        return sourceProducer != null && sourceProducer.getExtensions().contains( "json" );
      }
    }
    return false;
  }
}
