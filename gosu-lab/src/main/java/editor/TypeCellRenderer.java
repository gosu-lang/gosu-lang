package editor;


import editor.util.EditorUtilities;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;


/**
 */
public class TypeCellRenderer extends AbstractListCellRenderer<CharSequence>
{
  public TypeCellRenderer( JComponent list )
  {
    super( list, true );
  }

  @Override
  public void configure()
  {
    setText( getDisplayText( getNode() ) );
    setIcon( EditorUtilities.findIcon( TypeSystem.getByFullNameIfValid( getNode().toString() ) ) );
  }

  protected String getDisplayText( Object value )
  {
    String strType = (String)value;
    String strRelativeType = TypePopup.getRelativeTypeName( strType );
    return (strRelativeType + "  (" + strType + ")").replace( '\u2024', '.' );
  }
}
