package editor.plugin.typeloader.properties;

import editor.EditorHost;
import editor.plugin.typeloader.INewFileParams;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ISourceFileHandle;
import manifold.api.sourceprod.ISourceProducer;
import gw.lang.reflect.java.IJavaType;
import javax.swing.JComponent;
import javax.swing.text.StyledEditorKit;
import manifold.internal.javac.IIssueContainer;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class PropertiesTypeFactory implements ITypeFactory
{
  public PropertiesTypeFactory()
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
    return ".properties";
  }

  @Override
  public String getName()
  {
    return "Properties";
  }

  @Override
  public String getIcon()
  {
    return "images/properties_type.png";
  }

  @Override
  public PropertiesFileParams makeDefaultParams( String fqn )
  {
    return new PropertiesFileParams( fqn );
  }

  @Override
  public String createNewFileContents( INewFileParams params )
  {
    String eol = System.getProperty( "line.separator" );
    return "# " + params.getFqn() + eol + eol + "MyProperty = My value" + eol;
  }

  @Override
  public JComponent makePanel( INewFileParams params )
  {
    return null;
  }

  @Override
  public StyledEditorKit makeEditorKit()
  {
    return null;
  }

  @Override
  public void parse( IType type, String strText, boolean forceCodeCompletion, boolean changed, EditorHost editor )
  {
    //## todo: write a properties file parser
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
    return new PropertiesIssueContainer();
  }

  @Override
  public boolean handlesType( IType type )
  {
    if( type instanceof IJavaType )
    {
      ISourceFileHandle sfh = ((IJavaType)type).getSourceFileHandle();
      if( sfh != null )
      {
        ISourceProducer sourceProducer = sfh.getSourceProducer();
        return sourceProducer != null && sourceProducer.getExtensions().contains( "properties" );
      }
    }
    return false;
  }
}
