package editor;


import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;
import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Generic implementation of a popup. Refactored from original GotoTypePopup.
 */
public abstract class AbstractGotoPopup<T> extends JPopupMenu
{
  //----- Internal workings of popup -----//
  protected GenerifiedJList _list;
  protected JTextField _nameField;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private boolean _bLocked;
  private EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;
  private DocumentListener _docListenerForEdits;
  private JScrollPane _scrollPane;

  //----- Exposed pieces of the popup implementation -----//
  /**
   * Wait time between keystrokes
   */
  private final int _waitTime;
  private Timer _timer;
  /**
   * The visible row count for the popup
   */
  private final int _rowCount;
  private final String _title;
  private List<T> _allData;
  private String _strPrefix;
  private final boolean _takesInput;
  private final boolean _centerInFrame;
  private Object _dataLock = new Object();
  private boolean _dataInitialized;
  private JLabel _spinner;


  public AbstractGotoPopup( int waitTime, int rowCount, String title, String strPrefix, boolean takesInput, boolean centerInFrame )
  {
    _waitTime = waitTime;
    _timer = new Timer();
    _rowCount = rowCount;
    _title = title;
    _strPrefix = strPrefix;
    _takesInput = takesInput;
    _centerInFrame = centerInFrame;
    initLayout();
  }

  private void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    GridBagLayout gridBag = new GridBagLayout();
    JPanel pane = new JPanel();
    pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    initializeDataInWaitMode();

    int iY = 0;
    JLabel labelName = new JLabel( _title );
    labelName.setOpaque( true );
    labelName.setBackground( Scheme.active().getControl() );
    labelName.setFont( labelName.getFont().deriveFont( Font.BOLD ) );
    labelName.setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    pane.add( labelName, c );

    if( _takesInput )
    {
      _nameField = new JTextField( 32 );
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 1;
      c.weighty = 0;
      c.insets = new Insets( 2, 0, 2, 0 );
      pane.add( _nameField, c );
    }

    //
    // The list
    //
    _scrollPane = new JScrollPane();
    _list =
      new GenerifiedJList( new DefaultListModel() )
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
    _scrollPane.setViewportView( _list );
    _scrollPane.setVisible( false );
    _list.addMouseListener( new PopupMouseListener() );
    _list.setCellRenderer( constructCellRenderer() );
    _list.setFixedCellHeight( 22 );
    _list.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _list.setVisibleRowCount( _rowCount );
    _scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    pane.add( _scrollPane, c );

    _spinner = new JLabel( EditorUtilities.loadIcon( "images/wait.gif" ) );
    _spinner.setBorder( UIManager.getBorder( "TextField.border" ) );
    _spinner.setBackground( Scheme.active().getWindow() );
    _spinner.setOpaque( true );
    _spinner.setVisible( false );
    c.gridy = iY;
    pane.add( _spinner, c );

    content.add( pane, BorderLayout.CENTER );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );

    _editorKeyListener = new EditorKeyListener();

    add( content );

    _docListener = new UndoableEditListener()
    {
      @Override
      public void undoableEditHappened( UndoableEditEvent e )
      {
        resetTimer();
      }
    };
    _docListenerForEdits = new DocumentListener()
    {
      @Override
      public void insertUpdate( DocumentEvent e )
      {
        resetTimer();
      }

      @Override
      public void removeUpdate( DocumentEvent e )
      {
        resetTimer();
      }

      @Override
      public void changedUpdate( DocumentEvent e )
      {
        resetTimer();
      }
    };
    if( _strPrefix != null )
    {
      filterDisplay( _strPrefix, false );
    }
    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          if( _takesInput )
          {
            _nameField.requestFocus();
          }
          else
          {
            _list.requestFocus();
          }
        }
      } );
  }

  JTextField getNameField()
  {
    return _nameField;
  }

  GenerifiedJList getList()
  {
    return _list;
  }

  protected abstract ListCellRenderer constructCellRenderer();

  protected abstract void handleEdit();

  private void initializeDataInWaitMode()
  {
    editor.util.EditorUtilities.doBackgroundOp(
      () -> {
        try
        {
          _allData = initializeData();
        }
        finally
        {
          _dataInitialized = true;
          synchronized( _dataLock )
          {
            _dataLock.notifyAll();
          }
          SwingUtilities.invokeLater( () -> _spinner.setVisible( false ) );
        }
      } );
  }

  protected abstract List<T> initializeData();

  protected abstract AbstractPopupListModel<T> reconstructModel( String prefix );

  @Override
  public final void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      registerListeners();
      editor.util.EditorUtilities.removePopupBorder( this );
    }
    else
    {
      unregisterListeners();
      if( _takesInput )
      {
        _nameField.requestFocus();
      }
    }
  }

  private void registerListeners()
  {
    unregisterListeners();

    if( _takesInput )
    {
      _nameField.addKeyListener( _editorKeyListener );
      _nameField.getDocument().addUndoableEditListener( _docListener );
      _nameField.getDocument().addDocumentListener( _docListenerForEdits );
    }
    else
    {
      _list.addKeyListener( _editorKeyListener );
    }
  }

  private void unregisterListeners()
  {
    if( _takesInput )
    {
      _nameField.getDocument().removeDocumentListener( _docListenerForEdits );
      _timer.cancel();
      _nameField.getDocument().removeUndoableEditListener( _docListener );
      _nameField.removeKeyListener( _editorKeyListener );
    }
    else
    {
      _list.removeKeyListener( _editorKeyListener );
    }
  }

  protected void filterDisplay( final String prefix, boolean showSpinner )
  {
    if( showSpinner )
    {
      synchronized( _dataLock )
      {
        if( !_dataInitialized )
        {
          SwingUtilities.invokeLater( new Runnable()
          {
            @Override
            public void run()
            {
              _spinner.setVisible( true );
              pack();
              _nameField.requestFocus();
            }
          } );
        }
      }
    }
    if( _takesInput )
    {
      editor.util.EditorUtilities.doBackgroundOp( new Runnable()
      {
        @Override
        public void run()
        {
          final AbstractListModel model = reconstructModel( prefix );
          SwingUtilities.invokeLater( new Runnable()
          {
            @Override
            public void run()
            {
              filterSynchronously( model );
            }
          } );
        }
      } );
    }
    else
    {
      final AbstractListModel model = reconstructModel( prefix );
      filterSynchronously( model );
    }
  }

  private void filterSynchronously( AbstractListModel model )
  {
    _list.setModel( model );
    _list.setSelectedIndex( 0 );

    int iListCount = _list.getModel().getSize();
    _list.setVisibleRowCount( Math.min( iListCount, _rowCount ) );
    _scrollPane.setVisible( iListCount > 0 );

    _list.revalidate();
    _list.repaint();
    pack();

    if( _takesInput )
    {
      _nameField.requestFocus();
      SwingUtilities.invokeLater( new Runnable()
      {
        @Override
        public void run()
        {
          _nameField.requestFocus();
        }
      } );
    }
  }


  @Override
  public final void show( Component invoker, int iX, int iY )
  {
    _bLocked = true;

    try
    {
      super.show( invoker, iX, iY );
      if( _centerInFrame )
      {
        editor.util.EditorUtilities.centerWindowInFrame( this, KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() );
      }
    }
    finally
    {
      _bLocked = false;
    }
  }

  public final void addNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.add( ChangeListener.class, l );
  }

  public final void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  public void setSelection( String strSelection )
  {
    //iterate over _list and search for a match by name
    List<String> suggestionNames = getPopupSuggestions();
    for( int currentIndex = 0; currentIndex < suggestionNames.size(); currentIndex++ )
    {
      if( suggestionNames.get( currentIndex ).equals( strSelection ) )
      {
        //select the matching suggestion's index
        _list.setSelectedIndex( currentIndex );
        break;
      }
    }
  }

  public List<String> getPopupSuggestions()
  {
    ListModel popupModel = _list.getModel();
    ArrayList<String> suggestionNames = new ArrayList<String>();
    for( int i = 0; i < popupModel.getSize(); i++ )
    {
      suggestionNames.add( popupModel.getElementAt( i ).toString() );
    }
    return suggestionNames;
  }

  private void fireNodeChanged( final EventListenerList list, final ChangeEvent e )
  {
    EventQueue.invokeLater( new Runnable()
    {
      @Override
      public void run()
      {
        fireNodeChangedNow( list, e );
      }
    } );
  }

  private void fireNodeChangedNow( EventListenerList list, ChangeEvent e )
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

  /**
   */
  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP )
      {
        Action selectPrevious = _list.getActionMap().get( "selectPreviousRow" );
        selectPrevious.actionPerformed( new ActionEvent( _list, 0, "selectPreviousRow" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN )
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
      else if( e.getKeyCode() == KeyEvent.VK_ENTER )
      {
        T datum = _list.getSelectedValue();
        if( datum != null )
        {
          fireNodeChanged( _nodeListenerList, new ChangeEvent( datum ) );
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

  protected List<T> getInitializedAllData()
  {
    synchronized( _dataLock )
    {
      if( !_dataInitialized )
      {
        try
        {
          _dataLock.wait();
        }
        catch( InterruptedException e )
        {
          throw new RuntimeException( e );
        }
      }
    }
    return _allData;
  }

  /**
   */
  private class PopupMouseListener extends MouseAdapter
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

      T datum = _list.getSelectedValue();

      // BeanInfoNode node = tree.getBeanNode();
      // if( !(node.getType() instanceof BeanType) )
      // {
      //
      fireNodeChanged( _nodeListenerList, new ChangeEvent( datum ) );
      setVisible( false );
      // }
    }
  }

  public static abstract class AbstractPopupListModel<T> extends AbstractListModel
  {
    @Override
    public abstract T getElementAt( int index );
  }

  protected abstract class GenerifiedJList extends JList
  {
    protected GenerifiedJList( ListModel dataModel )
    {
      super( dataModel );
    }

    protected GenerifiedJList( final T[] listData )
    {
      super( listData );
    }

    @Override
    public T getSelectedValue()
    {
      return (T)super.getSelectedValue();
    }
  }

  private void resetTimer()
  {
    DisplayListTimerTask timerTask = new DisplayListTimerTask();
    if( _waitTime > 0 )
    {
      _timer.cancel();
      _timer = new Timer();
      _timer.schedule( timerTask, _waitTime );
    }
    else
    {
      timerTask.run();
    }
  }

  private class DisplayListTimerTask extends TimerTask
  {
    @Override
    public void run()
    {
      handleEdit();
    }
  }
}
