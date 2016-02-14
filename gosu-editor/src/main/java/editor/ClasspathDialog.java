package editor;

import editor.search.StudioUtilities;
import editor.util.Experiment;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class ClasspathDialog extends JDialog
{
  private JTextPane _pathsList;
  private File _dir;

  public ClasspathDialog( File dir )
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), "Classpath", true );
    _dir = dir;
    configUI();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        dispose();
      }
    } );
  }


  protected void configUI()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    JPanel mainPanel = new JPanel( new BorderLayout() );
    mainPanel.setBorder( BorderFactory.createCompoundBorder( UIManager.getBorder( "TextField.border" ),
                                                             BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );
    JPanel panel = new JPanel( new BorderLayout() );
    _pathsList = new JTextPane();
    JScrollPane scroller = new JScrollPane( _pathsList );
    setPathsList();
    _pathsList.setBorder( BorderFactory.createEmptyBorder() );
    JButton btnPaths = new JButton( "..." );
    btnPaths.addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          updatePaths();
        }
      } );
    panel.add( btnPaths, BorderLayout.NORTH );
    JPanel filler = new JPanel();
    filler.setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 0 ) );
    panel.add( filler, BorderLayout.CENTER );
    mainPanel.add( scroller, BorderLayout.CENTER );
    mainPanel.add( panel, BorderLayout.EAST );

    contentPane.add( mainPanel, BorderLayout.CENTER );

    JPanel south = new JPanel( new BorderLayout() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    filler = new JPanel();
    south.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    JButton btnFind = new JButton( "OK" );
    btnFind.setMnemonic( 'O' );
    btnFind.addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          saveClassPath();
          close();
        }
      } );
    buttonPanel.add( btnFind );
    getRootPane().setDefaultButton( btnFind );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        close();
      }
    } );
    buttonPanel.add( btnCancel );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );

    mapCancelKeystroke();

    setSize( 600, 400 );

    StudioUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void setPathsList()
  {
    String strPaths = "";
    List<String> paths = GosuPanel.getLocalClasspath();
    for( int i = 0; i < paths.size(); i++ )
    {
      String strPath = paths.get( i );
      strPaths += strPath + (i == paths.size() - 1 ? "" : File.pathSeparator);
    }
    _pathsList.setText( strPaths );
  }

  private void saveClassPath()
  {
    String strPaths = _pathsList.getText();
    StringTokenizer tokenizer = new StringTokenizer( strPaths, File.pathSeparator + "\n\r\t" );
    List<String> paths = new ArrayList<String>();
    for(; tokenizer.hasMoreTokens(); )
    {
      String strPath = tokenizer.nextToken();
      paths.add( strPath );
    }
    List<File> pathFiles = new ArrayList<File>();
    for( String strPath : paths )
    {
      pathFiles.add( new File( strPath ).getAbsoluteFile() );
    }
    savePathsAndReopenExperiment( pathFiles );
  }

  private void savePathsAndReopenExperiment( List<File> pathFiles )
  {
    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    Experiment experiment = gosuPanel.getExperimentView().getExperiment();
    List<String> srcPaths = pathFiles.stream().map( File::getAbsolutePath ).collect( Collectors.toList() );
    experiment.setSourcePath( srcPaths );
    gosuPanel.openExperiment( experiment.getExperimentDir() );
  }

  private void updatePaths()
  {
    JFileChooser fc = new JFileChooser( getCurrentDir() );
    fc.setDialogTitle( "Add Paths" );
    fc.setDialogType( JFileChooser.OPEN_DIALOG );
    fc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
    fc.setMultiSelectionEnabled( true );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || f.getName().endsWith( ".zip" ) || f.getName().endsWith( ".jar" );
        }

        public String getDescription()
        {
          return "Classpath (directories or archive files)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      String strExisting = _pathsList.getText();
      for( File f : fc.getSelectedFiles() )
      {
        strExisting += File.pathSeparatorChar + f.getAbsolutePath();
      }
      _dir = fc.getCurrentDirectory();
      _pathsList.setText( strExisting );
    }
  }

  private File getCurrentDir()
  {
    return _dir;
  }


  private void mapCancelKeystroke()
  {
    Object key = getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Cancel";
      getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    getRootPane().getActionMap().put( key,
                                      new AbstractAction()
                                      {
                                        public void actionPerformed( ActionEvent e )
                                        {
                                          close();
                                        }
                                      } );
  }

  private void close()
  {
    dispose();
  }
}
