package editor;

import javax.swing.*;
import java.io.File;


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
    File file = (File)_editor.getClientProperty( "_file" );
    if( file.isFile() && !file.canWrite() )
    {
      if( JOptionPane.showConfirmDialog( LabFrame.instance(), file.getName() + " is not writable.  Do you want to make the file writable?", "Gosu Lab",
                                         JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION )
      {
        if( !file.setWritable( true ) )
        {
          JOptionPane.showMessageDialog( LabFrame.instance(), "Failed to make " + file.getName() + " writable.", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
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
