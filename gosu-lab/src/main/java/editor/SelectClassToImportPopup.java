package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;


/**
 * @author cgross
 */
public class SelectClassToImportPopup extends JPopupMenu
{
  private static SelectClassToImportPopup _instance = new SelectClassToImportPopup();
  private JList _list;
  private JPanel _pane = new JPanel();
  private ClassSelectionCallback _callBack = null;
  private JLabel _labelTypeName;

  private SelectClassToImportPopup()
  {
    super( "Select Class To Import" );
    initLayout();
  }

  public static SelectClassToImportPopup instance()
  {
    return _instance;
  }

  public JList getList()
  {
    return _list;
  }

  public void show( Component invoker,
                    Rectangle rectangle,
                    Set<String> possibleTypesToImport,
                    ClassSelectionCallback callback, String label, ListCellRenderer cellRenderer )
  {
    setLabel( label );
    _list.setCellRenderer( cellRenderer );
    _labelTypeName.setText( label );
    DefaultListModel listModel = new DefaultListModel();
    for( String s : possibleTypesToImport )
    {
      listModel.addElement( s );
    }
    _list.setModel( listModel );
    _list.setSelectedIndex( 0 );
    if( rectangle != null )
    {
      show( invoker, rectangle.x, rectangle.y );
      SwingUtilities.invokeLater( () -> _list.requestFocus( true ) );
    }
    _callBack = callback;
  }

  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    GridBagLayout gridBag = new GridBagLayout();
    _pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();


    int iY = 0;
    _labelTypeName = new JLabel( "Select Class To Import" );
    _labelTypeName.setOpaque( true );
    _labelTypeName.setBackground( Scheme.active().getControl() );
    _labelTypeName.setFont( _labelTypeName.getFont().deriveFont( Font.BOLD ) );
    _labelTypeName.setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    _pane.add( _labelTypeName, c );

    //
    // The list
    //
    _list = new JList();
    _list.addMouseListener( new MenuMouseListener() );
    _list.setCellRenderer( new TypeCellRenderer( _list ) );
    _list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _list.setVisibleRowCount( 10 );
    JScrollPane scrollPane = new JScrollPane( _list );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    _pane.add( scrollPane, c );

    add( _pane );

    EditorKeyListener listener = new EditorKeyListener();
    addKeyListener( listener );
    _list.addKeyListener( listener );
  }

  public void setSelection( String strSelection )
  {
    //iterate over _list and search for a match by name
    java.util.List<String> suggestionNames = getPopupSuggestions();
    for( int currentIndex = 0; currentIndex < suggestionNames.size(); currentIndex++ )
    {
      if( suggestionNames.get( currentIndex ).equals( strSelection ) )
      {
        //select the matching suggestion's index
        _list.setSelectedIndex( currentIndex );
        notifyOfSelection( (String)_list.getSelectedValue() );
        break;
      }
    }
  }

  public java.util.List<String> getPopupSuggestions()
  {
    ArrayList<String> suggestionList = new ArrayList<String>();
    ListModel listObjects = _list.getModel();
    for( int i = 0; i < listObjects.getSize(); i++ )
    {
      suggestionList.add( listObjects.getElementAt( i ).toString() );
    }
    return suggestionList;
  }

  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
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
        String typeName = (String)_list.getSelectedValue();
        if( typeName != null )
        {
          notifyOfSelection( typeName );
        }
        setVisible( false );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );
        e.consume();
      }
    }
  }

  class MenuMouseListener extends MouseAdapter
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      int iIndex = _list.locationToIndex( e.getPoint() );
      if( iIndex < 0 )
      {
        return;
      }

      _list.setSelectedIndex( iIndex );

      String classToImport = (String)_list.getSelectedValue();
      notifyOfSelection( classToImport );
    }
  }

  private void notifyOfSelection( String classToImport )
  {
    _callBack.onSelection( classToImport );
  }

  public static interface ClassSelectionCallback
  {
    public void onSelection( String className );
  }
}
