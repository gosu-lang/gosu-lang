package editor.util;

import editor.GosuLabFileChooser;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Supplier;

/**
 */
public class DirectoryEditor extends JPanel
{
  private JTextField _editDir;
  private LabToolbarButton _btnDir;

  public DirectoryEditor( String title, String dir, Supplier<Frame> frame )
  {
    super( new GridBagLayout() );
    configUi( title, dir, frame );
  }

  public Document getDocument()
  {
    return _editDir.getDocument();
  }

  public String getText()
  {
    return _editDir.getText();
  }
  public void setText( String path )
  {
    _editDir.setText( path );
  }

  public void setEnabled( boolean enabled )
  {
    super.setEnabled( enabled );
    _editDir.setEnabled( enabled );
    _btnDir.setEnabled( enabled );
  }

  private void configUi( String title, String dir, Supplier<Frame> frame )
  {
    final GridBagConstraints c = new GridBagConstraints();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 3 );
    _editDir = new JTextField();
    if( dir != null )
    {
      _editDir.setText( dir );
    }
    add( _editDir, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    _btnDir = new LabToolbarButton(
      new AbstractAction( title, EditorUtilities.loadIcon( "images/folder.png" ) ) {
        public void actionPerformed( ActionEvent e )
        {
          File dir = getDirectory( title, frame.get() );
          if( dir != null )
          {
            _editDir.setText( dir.getAbsolutePath() );
          }
        }
      } );
    add( _btnDir, c );

  }

  File getDirectory( String title, Frame frame )
  {
    JFileChooser chooser = new GosuLabFileChooser();
    String dirText = _editDir.getText();
    if( dirText != null && dirText.isEmpty() )
    {
      File dir = new File( dirText );
      if( !dir.isDirectory() )
      {
        dirText = ".";
      }
    }
    else
    {
      dirText = ".";
    }
    chooser.setCurrentDirectory( new File( dirText ) );
    chooser.setDialogTitle( title );
    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    chooser.setAcceptAllFileFilterUsed( false );
    if( JFileChooser.APPROVE_OPTION == chooser.showDialog( frame, "OK" ) )
    {
      return chooser.getSelectedFile();
    }
    return null;
  }
}
