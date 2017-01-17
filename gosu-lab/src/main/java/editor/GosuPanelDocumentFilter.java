package editor;

import java.nio.file.Path;
import gw.util.PathUtil;
import javax.swing.*;


/**
 */
public class GosuPanelDocumentFilter extends SimpleDocumentFilter
{
  private EditorHost _editor;

  public GosuPanelDocumentFilter( EditorHost editor )
  {
    _editor = editor;
  }

  @Override
  public boolean acceptEdit( String insertedText )
  {
    Path file = (Path)_editor.getClientProperty( "_file" );
    if( PathUtil.isFile( file ) && !PathUtil.canWrite( file ) )
    {
      if( JOptionPane.showConfirmDialog( LabFrame.instance(), PathUtil.getName( file ) + " is not writable.  Do you want to make the file writable?", "Gosu Lab",
                                         JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION )
      {
        if( !PathUtil.setWritable( file, true ) )
        {
          JOptionPane.showMessageDialog( LabFrame.instance(), "Failed to make " + PathUtil.getName( file ) + " writable.", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
          return false;
        }
        return true;
      }
      else
      {
        return false;
      }
    }
    return true;
  }
}
