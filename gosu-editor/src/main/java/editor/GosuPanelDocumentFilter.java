package editor;

import editor.search.MessageDisplay;

import javax.swing.*;
import java.io.File;


/**
 */
public class GosuPanelDocumentFilter extends SimpleDocumentFilter
{
  private GosuEditor _editor;

  public GosuPanelDocumentFilter( GosuEditor editor )
  {
    _editor = editor;
  }

  @Override
  public boolean acceptEdit( String insertedText )
  {
    File file = (File)_editor.getClientProperty( "_file" );
    if( file.isFile() && !file.canWrite() )
    {
      if( MessageDisplay.displayConfirmation( file.getName() + " is not writable.  Do you want to make the file writable?",
                                              JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION )
      {
        if( !file.setWritable( true ) )
        {
          MessageDisplay.displayError( "Failed to make " + file.getName() + " writable." );
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
