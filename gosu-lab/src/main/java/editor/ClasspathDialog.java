package editor;

import editor.util.EditorUtilities;
import editor.util.Experiment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class ClasspathDialog extends JDialog implements IHandleCancel
{
  private JTextPane _pathsList;
  private File _dir;

  public ClasspathDialog( File dir )
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), "Dependencies", true );
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


    JPanel labelContainer = new JPanel( new BorderLayout() );
    JLabel label = new JLabel( "<html>Need to use classes outside your experiment? Configure a '<b>" + File.pathSeparator + "</b>' " +
                               "separated list of directories and/or jar files containing Gosu or Java classes your experiment uses." );
    label.setBorder( new EmptyBorder( 2, 0, 0, 0 ) );
    labelContainer.add( label, BorderLayout.NORTH );
    JLabel label2 = new JLabel( "Dependency List", EditorUtilities.loadIcon( "images/folder.png" ), SwingConstants.LEFT );
    labelContainer.add( label2, BorderLayout.SOUTH );
    label2.setBorder( new EmptyBorder( 10, 0, 2, 0 ) );
    mainPanel.add( labelContainer, BorderLayout.NORTH );

    _pathsList = new JTextPane();
    JScrollPane scroller = new JScrollPane( _pathsList );
    setPathsList();
    _pathsList.setBorder( BorderFactory.createEmptyBorder() );
    mainPanel.add( scroller, BorderLayout.CENTER );

    JPanel panel = new JPanel( new BorderLayout() );
    JPanel filler = new JPanel();
    filler.setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 0 ) );
    panel.add( filler, BorderLayout.CENTER );
    JButton btnPaths = new JButton( "..." );
    btnPaths.setToolTipText( "Find a directory or Jar file" );
    btnPaths.addActionListener( e -> updatePaths() );
    panel.add( btnPaths, BorderLayout.NORTH );
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
      e -> {
        saveClassPath();
        close();
      } );
    buttonPanel.add( btnFind );
    getRootPane().setDefaultButton( btnFind );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );

    mapCancelKeystroke( "Cancel", this::close );

    setSize( 600, 400 );

    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void setPathsList()
  {
    String strPaths = "";
    List<String> paths =  LabFrame.instance().getGosuPanel().getExperimentView().getExperiment().getSourcePath();
    for( int i = 0; i < paths.size(); i++ )
    {
      String strPath = paths.get( i );
      strPaths += strPath + (i == paths.size() - 1 ? "" : File.pathSeparator + "\n");
    }
    _pathsList.setText( strPaths );
  }

  private void saveClassPath()
  {
    String strPaths = _pathsList.getText();
    StringTokenizer tokenizer = new StringTokenizer( strPaths, File.pathSeparator + "\n\r\t" );
    List<String> paths = new ArrayList<>();
    for(; tokenizer.hasMoreTokens(); )
    {
      String strPath = tokenizer.nextToken();
      paths.add( strPath.trim() );
    }
    List<File> pathFiles = new ArrayList<>();
    for( String strPath : paths )
    {
      pathFiles.add( new File( strPath ).getAbsoluteFile() );
    }
    savePathsAndReopenExperiment( pathFiles );
  }

  private void savePathsAndReopenExperiment( List<File> pathFiles )
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    Experiment experiment = gosuPanel.getExperimentView().getExperiment();
    List<String> srcPaths = pathFiles.stream().map( File::getAbsolutePath ).collect( Collectors.toList() );
    experiment.setSourcePath( srcPaths );
    gosuPanel.openExperiment( experiment.getExperimentDir() );
  }

  private void updatePaths()
  {
    JFileChooser fc = new JFileChooser( getCurrentDir() );
    fc.setDialogTitle( "Add Dependencies" );
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
          return "Dependencies (directories or archive files)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      String strExisting = _pathsList.getText().trim();
      if( strExisting.endsWith( File.pathSeparator ) )
      {
        strExisting = strExisting.substring( 0, strExisting.length()-1 );
      }
      for( File f : fc.getSelectedFiles() )
      {
        strExisting += File.pathSeparatorChar + "\n" + f.getAbsolutePath();
      }
      _dir = fc.getCurrentDirectory();
      _pathsList.setText( strExisting );
    }
  }

  private File getCurrentDir()
  {
    return _dir;
  }

  private void close()
  {
    dispose();
  }
}
