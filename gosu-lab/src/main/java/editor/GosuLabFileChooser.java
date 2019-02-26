package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class GosuLabFileChooser extends JFileChooser
{
  public GosuLabFileChooser()
  {
    super();
  }

  public GosuLabFileChooser( String currentDirectoryPath )
  {
    super( currentDirectoryPath );
  }

  public GosuLabFileChooser( File currentDirectory )
  {
    super( currentDirectory );
  }

  public GosuLabFileChooser( FileSystemView fsv )
  {
    super( fsv );
  }

  public GosuLabFileChooser( File currentDirectory, FileSystemView fsv )
  {
    super( currentDirectory, fsv );
  }

  public GosuLabFileChooser( String currentDirectoryPath, FileSystemView fsv )
  {
    super( currentDirectoryPath, fsv );
  }

  // Use the system's L&F for the file chooser, otherwise JFileChooser will not work with GosuLabLAF
  public void updateUI()
  {
    LookAndFeel prevLAF = UIManager.getLookAndFeel();
    try
    {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    }
    catch( Exception e )
    {
      prevLAF = null;
    }

    GosuLabLAF.makeNice();

    // Always use system L&F for file chooser
    super.updateUI();


    if( prevLAF != null )
    {
      try
      {
        UIManager.setLookAndFeel( prevLAF );
      }
      catch( Exception ignore )
      {
      }
    }
  }
}