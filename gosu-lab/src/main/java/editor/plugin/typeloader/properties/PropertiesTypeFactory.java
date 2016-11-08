package editor.plugin.typeloader.properties;

import editor.plugin.typeloader.INewFileParams;
import editor.plugin.typeloader.ITypeFactory;
import javax.swing.JComponent;

/**
 */
public class PropertiesTypeFactory implements ITypeFactory
{
  public PropertiesTypeFactory()
  {
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
}
