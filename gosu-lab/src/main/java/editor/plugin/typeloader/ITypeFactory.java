package editor.plugin.typeloader;

import editor.EditorHost;
import gw.lang.IIssueContainer;
import gw.lang.reflect.IType;
import javax.swing.JComponent;
import javax.swing.text.StyledEditorKit;

/**
 */
public interface ITypeFactory
{
  boolean canCreate();
  String getFileExtension();
  String getName();
  String getIcon();
  INewFileParams makeDefaultParams( String fqn );
  JComponent makePanel( INewFileParams params );
  CharSequence createNewFileContents( INewFileParams params );
  StyledEditorKit makeEditorKit();
  void parse( IType type, String strText, boolean forceCodeCompletion, boolean changed, EditorHost editor );
  boolean canAddBreakpoint( IType type, int line );

  String getTypeAtOffset( IType type, int offset );

  String getTooltipMessage( int iPos, EditorHost editor );

  IIssueContainer getIssueContainer( EditorHost editor );

  boolean handlesType( IType type );
}
