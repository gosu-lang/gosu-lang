package editor.search;

import editor.undo.AtomicUndoManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.regex.PatternSyntaxException;

/**
 */
public abstract class BaseLocalSearchDialog extends JDialog
{
  public static boolean g_bReplaceMode = false;
  private static final DefaultComboBoxModel g_historySearch = new DefaultComboBoxModel();
  private static final DefaultComboBoxModel g_historyReplace = new DefaultComboBoxModel();
  private static boolean g_bCaseSensitive;
  private static boolean g_bRegex;
  private JComboBox _comboSearch;
  private JComboBox _comboReplace;
  private JCheckBox _cbCaseSensitive;
  private JCheckBox _cbRegex;
  private boolean _bReplaceMode;
  private boolean _bAllMode;
  private boolean _bAllModeFromTop;
  private boolean _bStartedReplacing;
  private boolean _bDisplayNotFound;
  private String _selectedText;
  private AtomicUndoManager _undoManager;
  private CompoundEdit _undoAtom;

  public BaseLocalSearchDialog( boolean modal, boolean bReplace, String selectedText, AtomicUndoManager undoManager )
  {
    super( (JFrame)KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(), bReplace ? "Replace" : "Find", modal );
    _selectedText = selectedText;
    _bDisplayNotFound = true;
    _bReplaceMode = g_bReplaceMode = bReplace;
    _undoManager = undoManager;
    setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
    configUI();
    addWindowListener( new WindowAdapter()
    {
      public void windowClosing( WindowEvent e )
      {
        dispose();
      }
    } );
  }

  public static boolean canRepeatFind()
  {
    return g_historySearch.getSize() > 0;
  }

  public boolean isReplaceMode()
  {
    return _bReplaceMode;
  }

  public void setReplaceMode( boolean bReplaceMode )
  {
    _bReplaceMode = bReplaceMode;
  }

  public boolean isStartedReplacing()
  {
    return _bStartedReplacing;
  }

  public void setStartedReplacing( boolean bStartedReplacing )
  {
    _bStartedReplacing = bStartedReplacing;
  }

  public void repeatFind()
  {
    _comboSearch.getEditor().setItem( g_historySearch.getElementAt( g_historySearch.getSize() - 1 ) );
    performFind( false );
  }

  public void repeatFindBackwards()
  {
    _comboSearch.getEditor().setItem( g_historySearch.getElementAt( g_historySearch.getSize() - 1 ) );
    performFind( true );
  }

  private void find( final String strText, final boolean backwards )
  {
    addTextHistory( strText, g_historySearch );
    if( isReplaceMode() )
    {
      String replaceTxt = (String)_comboReplace.getEditor().getItem();
      addTextHistory( replaceTxt, g_historyReplace );
      _comboReplace.getEditor().setItem( replaceTxt );
    }
    // Invoke later so dialog closes immediately.
    // Necessary for Replace to work write, otherwise dialog gets
    // in the way while prompting for replace.
    EventQueue.invokeLater(
      new Runnable()
      {
        public void run()
        {
          findNow( strText, backwards );
        }
      } );
  }

  /**
   * Provides a generic find method that toggles on the mode of the search dialog
   */
  protected java.util.List findInSource( String strSource, String strPattern, int iOffset, boolean backwards )
  {
    if( isRegEx() )
    {
      try
      {
        return RegExStringUtil.search( strSource, strPattern, !isCaseSensitive(), iOffset, backwards );
      }
      catch( PatternSyntaxException e )
      {
        MessageDisplay.displayError( "Invalid regular expression: " + e.getMessage() );
        return Collections.emptyList();
      }
    }
    else
    {
      return StringUtil.search( strSource, strPattern, !isCaseSensitive(), iOffset, backwards );
    }
  }

  protected void findNow( String strText, boolean backwards )
  {
    if( isReplaceMode() )
    {
      _undoAtom = null;
    }
    try
    {
      SearchLocation location = performFind( strText, backwards );
      if( location == null )
      {
        if( isDisplayNotFound() )
        {
          if( isStartedReplacing() )
          {
            MessageDisplay.displayInformation( "Finished replacing: " + strText );
          }
          else
          {
            MessageDisplay.displayInformation( "<html><pre>\"" + strText + "\"</pre> not found" );
          }
        }
      }
    }
    finally
    {
      if( isReplaceMode() && _undoAtom != null )
      {
        CompoundEdit endedAtom = null;
        while( endedAtom != _undoAtom )
        {
          endedAtom = getUndoManager().getUndoAtom();
          getUndoManager().endUndoAtom();
        }
      }
    }
  }

  protected void startUndoIfNecessary()
  {
    if( _undoAtom == null )
    {
      _undoAtom = getUndoManager().beginUndoAtom( "Replace All" );
    }
  }

  private AtomicUndoManager getUndoManager()
  {
    return _undoManager;
  }

  protected abstract SearchLocation performFind( String strText, boolean backwards );

  public boolean isDisplayNotFound()
  {
    return _bDisplayNotFound;
  }

  public void setDisplayNotFound( boolean bDisplayNotFound )
  {
    _bDisplayNotFound = bDisplayNotFound;
  }

  private void saveSettings()
  {
    g_bCaseSensitive = _cbCaseSensitive.isSelected();
    g_bRegex = _cbRegex.isSelected();
  }

  private void addTextHistory( String strText, DefaultComboBoxModel model )
  {
    if( strText == null || strText.length() == 0 )
    {
      return;
    }

    for( int i = 0; i < model.getSize(); i++ )
    {
      if( model.getElementAt( i ).equals( strText ) )
      {
        model.removeElementAt( i );
      }
    }

    model.addElement( strText );
  }

  private void initTextSettings()
  {
    _cbCaseSensitive.setSelected( g_bCaseSensitive );
    _cbRegex.setSelected( g_bRegex );
  }

  protected void configUI()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    JPanel mainPanel = new JPanel( new BorderLayout() );
    mainPanel.setBorder( BorderFactory.createCompoundBorder( UIManager.getBorder( "TextField.border" ),
                                                             BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) ) );
    configTextSearchUI( mainPanel );

    contentPane.add( mainPanel, BorderLayout.CENTER );

    JPanel south = new JPanel( new BorderLayout() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    south.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    JButton btnFind = new JButton( "Find" );
    btnFind.setMnemonic( 'F' );
    btnFind.addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          performFind( false );
        }
      } );
    addFindEnabler( btnFind );
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

    setSize( 400, 300 );
    setResizable( false );

    StudioUtilities.centerWindowInFrame( this, getOwner() );

    EventQueue.invokeLater(
      new Runnable()
      {
        public void run()
        {
          if( _comboSearch.isShowing() )
          {
            _comboSearch.requestFocus();
          }
        }
      } );
  }

  private void addFindEnabler( final JButton btnFind )
  {
    enableFindButton( btnFind );

    _comboSearch.getEditor().addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          enableFindButton( btnFind );
          if( btnFind.isEnabled() && !_comboSearch.isPopupVisible() )
          {
            btnFind.doClick();
          }
        }
      } );
    _comboSearch.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        enableFindButton( btnFind );
      }
    } );

    if( _comboReplace != null )
    {
      _comboReplace.getEditor().addActionListener(
        new ActionListener()
        {
          public void actionPerformed( ActionEvent e )
          {
            enableFindButton( btnFind );
            if( btnFind.isEnabled() && !_comboReplace.isPopupVisible() )
            {
              btnFind.doClick();
            }
          }
        } );
      _comboReplace.addActionListener( new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          enableFindButton( btnFind );
        }
      } );
    }

    ((JTextField)_comboSearch.getEditor().getEditorComponent()).getDocument().addDocumentListener(
      new DocumentListener()
      {
        public void changedUpdate( DocumentEvent e )
        {
        }

        public void insertUpdate( DocumentEvent e )
        {
          enableFindButton( btnFind );
        }

        public void removeUpdate( DocumentEvent e )
        {
          enableFindButton( btnFind );
        }
      } );
  }

  private void enableFindButton( final JButton btnFind )
  {
    String strText = (String)_comboSearch.getEditor().getItem();
    btnFind.setEnabled( strText != null && strText.length() > 0 );
  }

  private void performFind( boolean backwards )
  {
    findText( backwards );
    saveSettings();
    close();
  }

  /**
   * @param contentPane
   */
  private void configTextSearchUI( JComponent contentPane )
  {
    JPanel north = new JPanel( new BorderLayout() );

    JPanel textSearchPanel = new JPanel();
    textSearchPanel.setLayout( new BoxLayout( textSearchPanel, BoxLayout.Y_AXIS ) );
    JPanel nameValue = new JPanel( new BorderLayout() );
    JLabel label = new JLabel( "Text to find" );
    nameValue.add( label, BorderLayout.NORTH );
    for( ListDataListener listener : g_historySearch.getListDataListeners() )
    {
      g_historySearch.removeListDataListener( listener );
    }
    _comboSearch = new JComboBox( g_historySearch );
    _comboSearch.setEditable( true );
    JTextField textField = (JTextField)_comboSearch.getEditor().getEditorComponent();

    if( _selectedText != null && !_selectedText.isEmpty() )
    {
      textField.setText( _selectedText );
    }
    else if( g_historySearch.getSize() > 0 )
    {
      _comboSearch.getEditor().setItem( g_historySearch.getElementAt( g_historySearch.getSize() - 1 ) );
    }
    textField.setSelectionStart( 0 );
    textField.setSelectionEnd( textField.getText().length() );

    nameValue.add( _comboSearch, BorderLayout.SOUTH );
    textSearchPanel.add( nameValue );

    if( isReplaceMode() )
    {
      nameValue = new JPanel( new BorderLayout() );
      label = new JLabel( "Replace with" );
      nameValue.add( label, BorderLayout.NORTH );
      _comboReplace = new JComboBox( g_historyReplace );
      _comboReplace.setEditable( true );
      if( g_historyReplace.getSelectedItem() != null )
      {
        ((JTextField)_comboReplace.getEditor().getEditorComponent()).setText( g_historyReplace.getSelectedItem().toString() );
      }
      nameValue.add( _comboReplace, BorderLayout.SOUTH );
      textSearchPanel.add( nameValue );
    }

    north.add( textSearchPanel, BorderLayout.SOUTH );

    contentPane.add( north, BorderLayout.NORTH );

    JPanel center = new JPanel();
    center.setBorder( BorderFactory.createEmptyBorder( 10, 0, 0, 0 ) );
    center.setLayout( new BoxLayout( center, BoxLayout.Y_AXIS ) );

    center.add( new JSeparator() );

    _cbCaseSensitive = new JCheckBox( "Case sensitive" );
    _cbCaseSensitive.setMnemonic( 'C' );
    center.add( _cbCaseSensitive );

    _cbRegex = new JCheckBox( "Regular expression" );
    _cbRegex.setMnemonic( 'R' );
    center.add( _cbRegex );

    contentPane.add( center, BorderLayout.CENTER );

    initTextSettings();
  }

  private final void close()
  {
    dispose();
  }

  private void findText( boolean backwards )
  {
    String strText = (String)_comboSearch.getEditor().getItem();
    if( (strText == null || strText.length() == 0) )
    {
      return;
    }
    find( strText, backwards );
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

  public boolean isCaseSensitive()
  {
    return _cbCaseSensitive.isSelected();
  }

  public boolean isRegEx()
  {
    return _cbRegex.isSelected();
  }

  public boolean isAllMode()
  {
    return _bAllMode;
  }

  public void setAllMode( boolean bAllMode )
  {
    _bAllMode = bAllMode;
  }

  public boolean hasAllModeRecycledFromTop()
  {
    return _bAllModeFromTop;
  }

  public void setAllModeRecyclingFromTop( boolean bAllModeFromTop )
  {
    _bAllModeFromTop = bAllModeFromTop;
  }

  public String getDialogReplaceText()
  {
    return (String)_comboReplace.getEditor().getItem();
  }


}
