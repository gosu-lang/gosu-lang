/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListPopup extends AbstractPopup
{
  private JList _list;
  protected PopupListModel _model;
  private EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;


  public ListPopup( JTextComponent editor, PopupListModel model )
  {
    super( editor );
    _model = model;
    initLayout();
  }

  @Override
  public void show( Component invoker, int x, int y )
  {
    String strFilter = getModel().getFilterPrefix();
    if( strFilter != null && strFilter.length() > 0 &&
        _list.getModel().getSize() == 1 )
    {
      // Completes the value instead of showing popup
      fireNodeChanged( new ChangeEvent( _model.getInsertionTextFrom( _list.getModel().getElementAt( 0 ) ) ) );
      setVisible( false );
      return;
    }
    super.show( invoker, x, y );
  }

  @Override
  public void setValue( Object value )
  {
    _list.setSelectedValue( value, true );
  }

  public PopupListModel getModel()
  {
    return _model;
  }

  public JList getJList()
  {
    return _list;
  }

  protected void initLayout()
  {
    setOpaque( false );
    setDoubleBuffered( true );

    JPanel pane = new JPanel( new GridBagLayout() );
    GridBagConstraints c = new GridBagConstraints();

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    PopupListModel listModel = _model;

    int iY = 0;
    if( listModel != null && listModel.getModelUpdatedOrFilteredByPredicate().size() > 0 )
    {
      JComponent caption = getCaptionComponent( listModel );
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 1;
      c.weighty = 0;
      pane.add( caption, c );
    }


    //
    // The list
    //
    _list = new JList( listModel );
    _list.addMouseListener( new MouseHandler() );
    _list.setCellRenderer( makeCellRenderer() );
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
    pane.add( scrollPane, c );

    content.add( pane, BorderLayout.CENTER );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );

    add( content );

    pane.setPreferredSize( new Dimension( (int)Math.max( getEditor().getWidth(), _list.getPreferredSize().getWidth() ), 150 ) );

    _editorKeyListener = new EditorKeyListener();
    if( getEditor() != null )
    {
      _editorKeyListener = new EditorKeyListener();
      _docListener = new UndoableEditListener()
      {
        @Override
        public void undoableEditHappened( UndoableEditEvent e )
        {
          filterDisplay();
        }
      };
    }
  }

  public JComponent getCaptionComponent( PopupListModel listModel )
  {
    JLabel labelTypeName = new JLabel( listModel.getTypeName() );
    labelTypeName.setOpaque( true );
    labelTypeName.setBackground( Scheme.active().getControl() );
    labelTypeName.setFont( labelTypeName.getFont().deriveFont( Font.BOLD ) );
    labelTypeName.setBorder( BorderFactory.createEmptyBorder( 0, 3, 3, 3 ) );

    JPanel sortedByPanel = getSortedByPanel();
    if( sortedByPanel == null )
    {
      return labelTypeName;
    }
    else
    {
      JPanel panel = new JPanel( new BorderLayout() );
      panel.add( labelTypeName, BorderLayout.WEST );
      panel.add( sortedByPanel, BorderLayout.EAST );
      return panel;
    }
  }

  protected JPanel getSortedByPanel()
  {
    return null;
  }

  @Override
  public void refresh()
  {
    _model = (PopupListModel)_model.getFilteredModel( _model.getFilterPrefix() );
    _list.setModel( _model );
    _list.setSelectedIndex( _model.getSize() > 0 ? 0 : -1 );
    revalidate();
    repaint();
  }

  @Override
  protected void registerListeners()
  {
    unregisterListeners();

    getEditor().addKeyListener( _editorKeyListener );
    getEditor().getDocument().addUndoableEditListener( _docListener );
  }

  @Override
  protected void unregisterListeners()
  {
    getEditor().getDocument().removeUndoableEditListener( _docListener );
    getEditor().removeKeyListener( _editorKeyListener );
  }

  @Override
  protected String filterDisplay()
  {
    String strPrefix = super.filterDisplay();

    _list.setModel( (PopupListModel)_model.getFilteredModel( strPrefix ) );
    _list.setSelectedIndex( 0 );
    _list.revalidate();
    _list.repaint();

    return strPrefix;
  }

  public void fireSelectionAndDismiss()
  {
    Object selection = _list.getSelectedValue();
    if( selection != null )
    {
      fireNodeChanged(
        new ChangeEvent( _model.getInsertionTextFrom( selection ) ) );
      setVisible( false );
    }
  }

  protected ListCellRenderer makeCellRenderer()
  {
    return new DefaultCellRenderer();
  }

  public class DefaultCellRenderer extends DefaultListCellRenderer
  {
    @Override
    public Component getListCellRendererComponent( JList list,
                                                   Object value,
                                                   int modelIndex,
                                                   boolean isSelected,
                                                   boolean cellHasFocus )
    {
      String text = _model.getDisplayText( value );
      return super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );
    }

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
        fireSelectionAndDismiss();
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );
        e.consume();
      }
    }
  }

  public void selectElement( int i )
  {
    _list.setSelectedIndex( i );
    fireSelectionAndDismiss();
  }

  /**
   */
  class MouseHandler extends MouseAdapter
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
      fireSelectionAndDismiss();
    }
  }
}