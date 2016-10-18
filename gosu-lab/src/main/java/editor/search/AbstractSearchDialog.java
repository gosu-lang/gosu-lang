package editor.search;

import editor.FileTree;
import editor.FileTreeUtil;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.NodeKind;
import editor.RunMe;
import editor.Scheme;
import editor.util.AbstractDialog;
import editor.util.DirectoryEditor;
import editor.util.EditorUtilities;
import editor.util.ModalEventQueue;
import editor.util.ProgressFeedback;
import gw.lang.reflect.json.IJsonIO;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 */
public abstract class AbstractSearchDialog extends AbstractDialog
{
  private final FileTree _searchDir;
  private final boolean _bReplace;
  private JComboBox<String> _cbSearch;
  private JComboBox<String> _cbReplace;
  private JCheckBox _checkCase;
  private JCheckBox _checkWords;
  private JCheckBox _checkRegex;
  private JRadioButton _rbProject;
  private JRadioButton _rbDirectory;
  private DirectoryEditor _cbDir;
  private JRadioButton _rbScope;
  private JComboBox<SearchScope> _cbScope;
  private JCheckBox _checkFileMask;
  private JComboBox<String> _cbFileMasks;
  private DialogStateHandler _stateHandler;
  private List<FileTree> _prevSearchTree;
  private JPanel _separator2;
  private JPanel _separator3;

  public AbstractSearchDialog( FileTree searchDir, boolean bReplace, String title )
  {
    super( RunMe.getEditorFrame(), title, true );
    _searchDir = searchDir;
    _bReplace = bReplace;
    configUi();
  }

  protected abstract State getState();
  protected abstract void setState( State state );

  protected boolean isReplace()
  {
    return _bReplace;
  }

  protected void setScope( SearchScope scope )
  {
    _rbScope.setSelected( true );
    _cbScope.setSelectedItem( scope );
  }

  protected void setLocal()
  {
    setScope( SearchScope.CurrentFile );
    EventQueue.invokeLater( () -> setScope( SearchScope.CurrentFile ) );

    _rbProject.setVisible( false );
    _rbDirectory.setVisible( false );
    _cbDir.setVisible( false );
    _rbScope.setVisible( false );
    _cbScope.setVisible( false );
    _separator2.setVisible( false );
    _separator3.setVisible( false );
    _checkFileMask.setVisible( false );
    _cbFileMasks.setVisible( false );
  }

  private void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    _stateHandler = new DialogStateHandler();
    
    JPanel mainPanel = new JPanel( new BorderLayout() );
    mainPanel.setBorder( BorderFactory.createLineBorder( Scheme.active().getMenuBorder() ) );
    mainPanel.add( makeSearchPanel(), BorderLayout.CENTER );

    contentPane.add( mainPanel, BorderLayout.CENTER );

    JPanel south = new JPanel( new BorderLayout() );
    south.setBackground( Scheme.active().getMenu() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    filler.setBackground( Scheme.active().getMenu() );
    south.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
    buttonPanel.setBackground( Scheme.active().getMenu() );

    JButton btnFind = new JButton( _bReplace ? "Replace" : "Find" );
    btnFind.setMnemonic( 'F' );
    btnFind.addActionListener( e -> find() );
    buttonPanel.add( btnFind );
    getRootPane().setDefaultButton( btnFind );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );
    contentPane.setBackground( Scheme.active().getMenu() );

    mapCancelKeystroke( "Cancel", this::close );

    setSize( 400, _bReplace ? 420 : 400 );

    EditorUtilities.centerWindowInFrame( this, getOwner() );

    EventQueue.invokeLater(
      () -> {
        applyState();
        if( _searchDir != FileTreeUtil.getRoot() )
        {
          _rbDirectory.setSelected( true );
          _cbDir.setText( _searchDir.getFileOrDir().getAbsolutePath() );
        }
        else if( !_rbScope.isSelected() )
        {
          _rbProject.setSelected( true );
        }
        _stateHandler.actionPerformed( null );
        _cbSearch.requestFocus();

        setTextFromEditor();
      } );

  }

  private void setTextFromEditor()
  {
    GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().getCurrentEditor();
    if( editor != null )
    {
      String selection = editor.getSelectedText();
      if( selection != null )
      {
        _cbSearch.setSelectedItem( selection );
        JTextComponent textField = (JTextComponent)_cbSearch.getEditor().getEditorComponent();
        textField.setText( selection );
        textField.select( 0, selection.length() );
      }
    }
  }

  private void applyState()
  {
    if( getState() != null )
    {
      getState().restore( this );
    }
  }

  protected String getPattern()
  {
    return (String)_cbSearch.getSelectedItem();
  }

  protected boolean isCaseSensitive()
  {
    return _checkCase.isSelected();
  }

  protected boolean isWholeWords()
  {
    return _checkWords.isSelected();
  }

  protected boolean isRegex()
  {
    return _checkRegex.isSelected();
  }

  protected void find()
  {
    setState( new State().save( this ) );

    close();

    _prevSearchTree = getSearchResults();

    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    gosuPanel.showSearches( false );
    SearchPanel searchPanel = gosuPanel.showSearches( true );
    searchPanel.showReplace( _bReplace );
    searchPanel.setReplacePattern( (String)_cbReplace.getSelectedItem() );

    boolean[] bFinished = {false};
    ProgressFeedback.runWithProgress( "Searching...",
                                      progress -> {
                                        EventQueue.invokeLater( () -> {
                                          progress.setLength( numOfFiles() );

                                          addReplaceInfo( searchPanel );

                                          String text = (String)_cbSearch.getSelectedItem();
                                          SearchTree results = new SearchTree( "<html><b>$count</b>&nbsp;occurrences&nbsp;of&nbsp;<b>'" + text + "'</b>&nbsp;in&nbsp;" + getScopeName(), NodeKind.Directory, SearchTree.empty() );
                                          searchPanel.add( results );

                                          TextSearcher searcher = new TextSearcher( text, !_checkCase.isSelected(), _checkWords.isSelected(), _checkRegex.isSelected() );
                                          searcher.searchTrees( getSelectedTrees(), results, ft -> include( ft, getFileMatchRegex() ), progress );
                                          selectFirstMatch( results );
                                          bFinished[0] = true;
                                        } );
                                      } );
    new ModalEventQueue( () -> !bFinished[0] ).run();
  }

  private int numOfFiles()
  {
    int count = 0;
    for( FileTree ft: getSelectedTrees() )
    {
      count += ft.getTotalFiles();
    }
    return count;
  }

  private List<FileTree> getSelectedTrees()
  {
    if( _rbScope.isSelected() )
    {
      switch( (SearchScope)_cbScope.getSelectedItem() )
      {
        case CurrentFile:
          return Collections.singletonList( FileTreeUtil.getRoot().find( RunMe.getEditorFrame().getGosuPanel().getCurrentFile() ) );
        case OpenFiles:
          return RunMe.getEditorFrame().getGosuPanel().getOpenFilesInProject();
        case SelectedFiles:
          return Collections.singletonList( RunMe.getEditorFrame().getGosuPanel().getExperimentView().getSelectedTree() );
        case PreviousSearchFiles:
          return _prevSearchTree;
        default:
          throw new IllegalStateException();
      }
    }
    else
    {
      return Collections.singletonList( getSearchDir() );
    }
  }

  private List<FileTree> getSearchResults()
  {
    SearchPanel searchPanel = RunMe.getEditorFrame().getGosuPanel().getSearchPanel();
    if( searchPanel == null )
    {
      return Collections.emptyList();
    }

    SearchTree root = (SearchTree)searchPanel.getTree().getModel().getRoot();
    ArrayList<FileTree> result = new ArrayList<>();
    findFilesFromPreviousSearch( root, result );
    return result;
  }

  private void findFilesFromPreviousSearch( SearchTree root, List<FileTree> result )
  {
    SearchTree.SearchTreeNode node = root.getNode();
    if( node != null && node.getFile() != null && node.getLocation() == null )
    {
      result.add( node.getFile() );
    }
    else
    {
      for( int i = 0; i < root.getChildCount(); i++ )
      {
        SearchTree child = root.getChildAt( i );
        findFilesFromPreviousSearch( child, result );
      }
    }
  }

  private void addReplaceInfo( SearchPanel searchPanel )
  {
    if( _bReplace )
    {
      String text = (String)_cbReplace.getSelectedItem();
      SearchTree results = new SearchTree( "<html>Replace occurrences with <b>'" + text + "'</b>", NodeKind.Info, SearchTree.empty() );
      searchPanel.add( results );
    }
  }

  private void selectFirstMatch( SearchTree results )
  {
    if( results.getChildCount() == 0 )
    {
      results.select();
    }
    else
    {
      selectFirstMatch( results.getChildAt( 0 ) );
    }
  }

  private List<String> getFileMatchRegex()
  {
    if( _checkFileMask.isSelected() )
    {
      String mask = (String)_cbFileMasks.getSelectedItem();
      if( mask != null && mask.isEmpty() )
      {
        List<String> list = new ArrayList<>();
        for( StringTokenizer tok = new StringTokenizer( mask, ";" ); tok.hasMoreTokens(); )
        {
          String ext = tok.nextToken().trim();
          list.add( StringUtil.wildcardToRegex( ext ) );
        }
        return list;
      }
    }
    return Collections.emptyList();
  }

  private boolean include( FileTree ft, List<String> fileMatchRegex )
  {
    if( !fileMatchRegex.isEmpty() )
    {
      for( String regex : fileMatchRegex )
      {
        if( ft.getName().toLowerCase().matches( regex ) )
        {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  private String getScopeName()
  {
    if( _rbProject.isSelected() )
    {
      return "Experiment";
    }
    if( _rbDirectory.isSelected() )
    {
      return _cbDir.getText();
    }
    if( _rbScope.isSelected() )
    {
      return ((SearchScope)_cbScope.getSelectedItem()).getLabel();
    }

    throw new IllegalStateException();
  }

  private JComponent makeSearchPanel()
  {
    JPanel configPanel = new JPanel( new GridBagLayout() );
    configPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 5 );
    JLabel label = new JLabel( "Text to find:" );
    configPanel.add( label, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 5, 0 );
    _cbSearch = new JComboBox<>();
    _cbSearch.setEditable( true );
    configPanel.add( _cbSearch, c );


    _cbReplace = new JComboBox<>();
    _cbReplace.setEditable( true );
    if( _bReplace )
    {
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.NONE;
      c.gridx = 0;
      c.gridy = iY;
      c.gridwidth = 1;
      c.gridheight = 1;
      c.weightx = 0;
      c.weighty = 0;
      c.insets = new Insets( 0, 0, 0, 5 );
      label = new JLabel( "Replace with:" );
      configPanel.add( label, c );

      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 1;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 1;
      c.weighty = 0;
      c.insets = new Insets( 0, 0, 0, 0 );
      configPanel.add( _cbReplace, c );
    }


    //---------------------------------------------------------------------------------------
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 5, 0 );
    JPanel separator1 = new JPanel();
    separator1.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getControlShadow() ) );
    configPanel.add( separator1, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    _checkCase = new JCheckBox( "Case sensitive" );
    _checkCase.setMnemonic( 'C' );
    configPanel.add( _checkCase, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _checkWords = new JCheckBox( "Whole words only" );
    _checkWords.setMnemonic( 'R' );
    configPanel.add( _checkWords, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _checkRegex = new JCheckBox( "Regular expression" );
    _checkRegex.setMnemonic( 'G' );
    _checkRegex.addActionListener( _stateHandler );
    configPanel.add( _checkRegex, c );

    //-----------------------------------------------------------------------------------
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 5, 0 );
    _separator2 = new JPanel();
    _separator2.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getControlShadow() ) );
    configPanel.add( _separator2, c );


    ButtonGroup group = new ButtonGroup();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    _rbProject = new JRadioButton( "Whole experiment" );
    _rbProject.setMnemonic( 'H' );
    _rbProject.addActionListener( _stateHandler );
    group.add( _rbProject );
    configPanel.add( _rbProject, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _rbDirectory = new JRadioButton( "Directory:" );
    _rbDirectory.setMnemonic( 'D' );
    _rbDirectory.addActionListener( _stateHandler );
    group.add( _rbDirectory );
    configPanel.add( _rbDirectory, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _cbDir = new DirectoryEditor( "Directory", _searchDir.getFileOrDir().getAbsolutePath(), RunMe::getEditorFrame );
    configPanel.add( _cbDir, c );


    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _rbScope = new JRadioButton( "Scope:" );
    _rbScope.setMnemonic( 'S' );
    _rbScope.addActionListener( _stateHandler );
    group.add( _rbScope);
    configPanel.add( _rbScope, c );

    _rbProject.setSelected( true );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _cbScope = new JComboBox<>( SearchScope.values() );
    configPanel.add( _cbScope, c );

    //-----------------------------------------------------------------------------------
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 5, 0 );
    _separator3 = new JPanel();
    _separator3.setBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getControlShadow() ) );
    configPanel.add( _separator3, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 0, 0, 0, 0 );
    _checkFileMask = new JCheckBox( "File mask(s):" );
    _checkFileMask.setMnemonic( 'M' );
    _checkFileMask.addActionListener( _stateHandler );
    configPanel.add( _checkFileMask, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 0 );
    _cbFileMasks= new JComboBox();
    _cbFileMasks.setEditable( true );
    configPanel.add( _cbFileMasks, c );

    return configPanel;
  }

  public FileTree getSearchDir()
  {
    if( _rbDirectory.isSelected() )
    {
      File file = new File( _cbDir.getText() );
      if( file.exists() )
      {
        FileTree fileTree = FileTreeUtil.getRoot().find( file );
        if( fileTree != null )
        {
          return fileTree;
        }
      }
    }
    return FileTreeUtil.getRoot();
  }

  private class DialogStateHandler implements ActionListener
  {
    @Override
    public void actionPerformed( ActionEvent e )
    {
      _checkWords.setEnabled( !_checkRegex.isSelected() );
      _cbDir.setEnabled( _rbDirectory.isSelected() );
      _cbScope.setEnabled( _rbScope.isSelected() );
      _cbDir.setEnabled( _rbDirectory.isSelected() );
      _cbFileMasks.setEnabled( _checkFileMask.isSelected() );
    }
  }

  public static class State implements IJsonIO
  {
    String[] _searchHistory;
    String[] _replaceHistory;

    boolean _case;
    boolean _words;
    boolean _regex;

    boolean _project;
    boolean _dir;
    boolean _scope;

    String _selectedDir;

    SearchScope _selectedScope;

    boolean _mask;
    String _selectedMask;
    String[] _masks;

    public State()
    {
    }

    public State save( AbstractSearchDialog dlg )
    {
      _searchHistory = makeArray( dlg._cbSearch );
      _replaceHistory = makeArray(dlg. _cbReplace );

      _case = dlg._checkCase.isSelected();
      _words = dlg._checkWords.isSelected();
      _regex = dlg._checkRegex.isSelected();

      _project = dlg._rbProject.isSelected();
      _dir = dlg._rbDirectory.isSelected();
      _scope = dlg._rbScope.isSelected();
      _selectedScope = (SearchScope)dlg._cbScope.getSelectedItem();
      _selectedDir = dlg._cbDir.getText();

      _mask = dlg._checkFileMask.isSelected();
      _selectedMask = (String)dlg._cbFileMasks.getSelectedItem();
      _masks = makeArray( dlg._cbFileMasks );

      return this;
    }

    public void restore( AbstractSearchDialog dlg )
    {
      dlg._cbSearch.setModel( new DefaultComboBoxModel<>( _searchHistory ) );
      dlg._cbSearch.getEditor().setItem( null );
      dlg._cbReplace.setModel( new DefaultComboBoxModel<>( _replaceHistory ) );
      dlg._cbReplace.getEditor().setItem( null );

      dlg._checkCase.setSelected( _case );
      dlg._checkWords.setSelected( _words );
      dlg._checkRegex.setSelected( _regex );

      dlg._rbProject.setSelected( _project );
      dlg._rbDirectory.setSelected( _dir );
      dlg._rbScope.setSelected( _scope );

      dlg._cbDir.setText( _selectedDir );

      dlg._cbScope.setSelectedItem( _selectedScope );

      dlg._checkFileMask.setSelected( _mask );
      dlg._cbFileMasks.setSelectedItem( _selectedMask );
      dlg._cbFileMasks.setModel( new DefaultComboBoxModel<>( _masks ) );
    }

    private String[] makeArray( JComboBox<String> cb )
    {
      DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)cb.getModel();
      int size = Math.min( 20, model.getSize() );
      String selectedItem = (String)cb.getSelectedItem();
      int extra = 0;
      if( selectedItem != null && !selectedItem.isEmpty() && model.getIndexOf( selectedItem ) < 0  )
      {
        extra = 1;
      }
      String[] array = new String[size+extra];
      if( extra > 0 )
      {
        array[0] = selectedItem;
      }
      for( int i = 0; i < size; i++ )
      {
        array[i+extra] = model.getElementAt( i );
      }
      return array;
    }
  }
}
