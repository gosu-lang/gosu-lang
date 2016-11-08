package editor.plugin.typeloader;

import javax.swing.JComponent;

/**
 */
public interface ITypeFactory
{
  String getFileExtension();
  String getName();
  String getIcon();
  INewFileParams makeDefaultParams( String fqn );
  JComponent makePanel( INewFileParams params );
  CharSequence createNewFileContents( INewFileParams params );
}
