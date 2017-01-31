/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;
import editor.util.TextComponentUtil;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;


public class TypePopup extends EditorBasedPopup implements ISelectionPopup
{
  private JPanel _pane = new JPanel();
  private JList<CharSequence> _list;
  private ArrayList<CharSequence> _allTypes;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private boolean _bLocked;
  private EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;
  private String _strPrefix;
  private String _title;
  private boolean _replaceWholeWord;

  public TypePopup( String strPrefix, GosuEditor editor, boolean annotationsOnly )
  {
    super( editor );

    _strPrefix = strPrefix;
    if( annotationsOnly )
    {
      _title = "Annotations";
    }
    else
    {
      _title = "Types";
    }

    initLayout();
  }

  @Override
  public void setSelection( String strSelection )
  {
    _list.setSelectedValue( strSelection, true );
  }

  public void handleSelection( String strType )
  {
    fireNodeChanged( _nodeListenerList, new ChangeEvent( strType ) );
    setVisible( false );
  }

  @Override
  public java.util.List<String> getPopupSuggestions()
  {
    ListModel popupList = _list.getModel();
    ArrayList<String> suggestionNames = new ArrayList<>();
    for( int i = 0; i < popupList.getSize(); i++ )
    {
      suggestionNames.add( popupList.getElementAt( i ).toString() );
    }
    return suggestionNames;
  }

  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    GridBagLayout gridBag = new GridBagLayout();
    _pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    Set<? extends CharSequence> allTypeNames = TypeSystem.getAllTypeNames();
    _allTypes = new ArrayList<>( allTypeNames );
    Collections.sort( _allTypes,
                      ( o1, o2 ) -> getRelativeTypeName( o1.toString() ).compareToIgnoreCase( getRelativeTypeName( o2.toString() ) ) );
    int iY = 0;
    JLabel labelTypeName = new JLabel( _title );
    labelTypeName.setOpaque( true );
    labelTypeName.setBackground( Scheme.active().getControl() );
    labelTypeName.setFont( labelTypeName.getFont().deriveFont( Font.BOLD ) );
    labelTypeName.setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    _pane.add( labelTypeName, c );

    //
    // The Type list
    //
    _list =
      new JList<CharSequence>( new TypeModel( _allTypes ) )
      {
        @Override
        public Dimension getPreferredScrollableViewportSize()
        {
          Dimension dim = super.getPreferredScrollableViewportSize();
          int iScreenWidth = getToolkit().getScreenSize().width;
          if( dim.width > iScreenWidth / 3 )
          {
            dim.width = iScreenWidth / 3;
          }
          return dim;
        }
      };

    _list.addMouseListener( new TypeListener() );
    _list.setCellRenderer( new TypeCellRenderer( _list ) );
    _list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _list.setVisibleRowCount( 10 );
    JScrollPane scrollPane = new JScrollPane( _list );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    _pane.add( scrollPane, c );

    content.add( _pane, BorderLayout.CENTER );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );

    _editorKeyListener = new EditorKeyListener();

    add( content );

    if( getEditor() != null )
    {
      _editorKeyListener = new EditorKeyListener();
      _docListener = e -> filterDisplay();
    }
    if( _strPrefix != null )
    {
      filterDisplay( _strPrefix );
    }
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( getEditor() == null )
    {
      return;
    }

    if( bVisible )
    {
      registerListeners();
      editor.util.EditorUtilities.removePopupBorder( this );
    }
    else
    {
      unregisterListeners();
      getEditor().getEditor().requestFocus();
    }
  }

  void registerListeners()
  {
    unregisterListeners();

    getEditor().getEditor().addKeyListener( _editorKeyListener );
    getEditor().getEditor().getDocument().addUndoableEditListener( _docListener );
  }

  void unregisterListeners()
  {
    getEditor().getEditor().getDocument().removeUndoableEditListener( _docListener );
    getEditor().getEditor().removeKeyListener( _editorKeyListener );
  }

  void filterDisplay()
  {
    filterDisplay( null );
  }

  void filterDisplay( String strWholePath )
  {
    if( strWholePath == null )
    {
      strWholePath = TextComponentUtil.getWordAtCaret( getEditor().getEditor() );
      if( strWholePath != null && strWholePath.length() > 0 && Character.isWhitespace( strWholePath.charAt( 0 ) ) )
      {
        strWholePath = TextComponentUtil.getPartialWordBeforeCaret( getEditor().getEditor() );
      }
    }

    String strPrefix = strWholePath;
    if( strWholePath != null && strWholePath.length() > 0 )
    {
      int iDotIndex = strWholePath.lastIndexOf( '.' );
      if( iDotIndex >= 0 )
      {
        strPrefix = strWholePath.substring( iDotIndex + 1 );
      }
    }

    if( endsWithInvalidChar( strPrefix ) )
    {
      setVisible( false );
      return;
    }

    ArrayList<CharSequence> allTypes = getAllTypes();
    ArrayList<CharSequence> filteredTypes = new ArrayList<>();
    for( CharSequence type : allTypes )
    {
      String strType = type.toString();
      String strRelativeType = getRelativeTypeName( strType );
      if( strPrefix != null && strRelativeType.toLowerCase().startsWith( strPrefix.toLowerCase() ) )
      {
        filteredTypes.add( strType );
      }
    }

    _list.setModel( new TypeModel( filteredTypes ) );
    _list.setSelectedIndex( 0 );
    _list.revalidate();
    _list.repaint();
  }

  @Override
  public void show( Component invoker, int iX, int iY )
  {
    _bLocked = true;

    try
    {
      TypeModel model = (TypeModel)_list.getModel();
      int iSize = model.getSize();
      if( iSize == 0 )
      {
        return;
      }

      //## Don't auto-complete types unless the type is already visible in the uses-statements
      if( iSize == 1 )
      {
        String strType = (String)model.getElementAt( 0 );
        if( _strPrefix != null && !_strPrefix.equalsIgnoreCase( strType ) )
        {
          if( isTypeUsed( strType ) )
          {
            // Auto-complete if only one matching type
            _replaceWholeWord = true;
            fireNodeChanged( _nodeListenerList, new ChangeEvent( strType ) );
            return;
          }
        }
      }

      super.show( invoker, iX, iY );
    }
    finally
    {
      _bLocked = false;
    }
  }

  private boolean isTypeUsed( String strType )
  {
    String strRelativeName = getRelativeTypeName( strType );
    try
    {
      return getEditor().getParser().getTypeUsesMap().resolveType( strRelativeName ) != null ||
             TypeSystem.parseType( strRelativeName, getEditor().getParser().getTypeUsesMap().copy() ) != null;
    }
    catch( Exception e )
    {
      return false;
    }
  }

  @Override
  public void addNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.add( ChangeListener.class, l );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  protected void fireNodeChanged( final EventListenerList list, final ChangeEvent e )
  {
    EventQueue.invokeLater( () -> fireNodeChangedNow( list, e ) );
  }

  protected void fireNodeChangedNow( EventListenerList list, ChangeEvent e )
  {
    // Guaranteed to return a non-null array
    Object[] listeners = list.getListenerList();

    // Process the listeners last to first,
    // notifying those that are interested in this event
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }

  private ArrayList<CharSequence> getAllTypes()
  {
    return _allTypes;
  }

  static String getRelativeTypeName( String strType )
  {
    int iIndex = strType.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      return strType.substring( iIndex + 1 );
    }
    return strType;
  }

  public void setReplaceWholeWord( boolean replaceWholeWord )
  {
    _replaceWholeWord = replaceWholeWord;
  }
  public boolean isReplaceWholeWord()
  {
    return _replaceWholeWord;
  }

  /**
   */
  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP ||
          e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT )
      {
        Action selectPrevious = _list.getActionMap().get( "selectPreviousRow" );
        selectPrevious.actionPerformed( new ActionEvent( _list, 0, "selectPreviousRow" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
               e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT )
      {
        Action selectNext = _list.getActionMap().get( "selectNextRow" );
        selectNext.actionPerformed( new ActionEvent( _list, 0, "selectNextRow" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PAGE_UP )
      {
        Action scrollUpChangeSelection = _list.getActionMap().get( "scrollUp" );
        scrollUpChangeSelection.actionPerformed( new ActionEvent( _list, 0, "scrollUp" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PAGE_DOWN )
      {
        Action scrollDownChangeSelection = _list.getActionMap().get( "scrollDown" );
        scrollDownChangeSelection.actionPerformed( new ActionEvent( _list, 0, "scrollDown" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ENTER ||
               e.getKeyCode() == KeyEvent.VK_SPACE ||
               e.getKeyCode() == KeyEvent.VK_TAB )
      {
        String strType = (String)_list.getSelectedValue();
        if( strType != null )
        {
          setReplaceWholeWord( e.getKeyCode() == KeyEvent.VK_TAB );
          fireNodeChanged( _nodeListenerList, new ChangeEvent( strType ) );
        }
        setVisible( false );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PERIOD )
      {
        handleSelectionForDot();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );
        e.consume();
      }
    }

    // Backspace over dot, handle the selection, then re-type the dot
    private void handleSelectionForDot()
    {
      final String strType = (String)_list.getSelectedValue();
      if( strType == null )
      {
        setVisible( false );
        return;
      }

      EventQueue.invokeLater(
        () -> {
          sendKeyEvent( KeyEvent.VK_BACK_SPACE );
          handleSelection( strType );
          EventQueue.invokeLater( () -> sendKeyEvent( KeyEvent.VK_PERIOD ) );
        } );
    }

    private void sendKeyEvent( final int iKey )
    {
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, iKey, KeyEvent.CHAR_UNDEFINED ) );
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, iKey, KeyEvent.CHAR_UNDEFINED ) );
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, (char)iKey ) );
    }
  }

  /**
   */
  class TypeListener extends MouseAdapter
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      if( _bLocked )
      {
        return;
      }

      int iIndex = _list.locationToIndex( e.getPoint() );
      if( iIndex < 0 )
      {
        return;
      }

      _list.setSelectedIndex( iIndex );

      String strType = (String)_list.getSelectedValue();

      // BeanInfoNode node = tree.getBeanNode();
      // if( !(node.getType() instanceof BeanType) )
      // {
      //
      fireNodeChanged( _nodeListenerList, new ChangeEvent( strType ) );
      setVisible( false );
      // }
    }
  }

  /**
   */
  class TypeModel extends AbstractListModel<CharSequence>
  {
    ArrayList<CharSequence> _allTypes;

    TypeModel( ArrayList<CharSequence> allTypes )
    {
      _allTypes = allTypes;
    }

    @Override
    public int getSize()
    {
      return _allTypes.size();
    }

    @Override
    public CharSequence getElementAt( int i )
    {
      return _allTypes.get( i );
    }
  }
}

