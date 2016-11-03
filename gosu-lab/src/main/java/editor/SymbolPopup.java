/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;
import editor.util.TextComponentUtil;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class SymbolPopup extends EditorBasedPopup implements ISelectionPopup
{
  private JPanel _pane = new JPanel();
  private JTree _tree;
  private final IType _expectedType;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private boolean _bLocked;
  private EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;
  private ISymbol[] _symbols;
  private String _strPrefix;
  private boolean _autoDismissed;


  public SymbolPopup( ISymbol[] symbols, String strPrefix, GosuEditor editor, IType expectedType )
  {
    super( editor );

    _symbols = symbols;
    _strPrefix = strPrefix;
    _expectedType = expectedType;

    initLayout();
  }

  public void setSelection( ISymbol value )
  {
    _tree.setSelectionRow( Arrays.asList( _symbols ).indexOf( value ) );
  }

  protected void initLayout()
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setDoubleBuffered( true );

    Border border = BorderFactory.createEmptyBorder( 2, 2, 2, 2 );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    _pane.setLayout( new BorderLayout() );
    _pane.add( content, BorderLayout.CENTER );


    if( _symbols != null && _symbols.length > 0 )
    {
      JLabel labelTypeName = new JLabel( "Symbols" );
      labelTypeName.setOpaque( true );
      labelTypeName.setBackground( Scheme.active().getControl() );
      labelTypeName.setFont( labelTypeName.getFont().deriveFont( Font.BOLD ) );
      labelTypeName.setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
      content.add( labelTypeName, BorderLayout.NORTH );
    }

    //
    // The Symbol list
    //
    _tree = new JTree( new SymbolRoot( _symbols ) );
    _tree.addMouseListener( new SymbolListener() );
    _tree.setCellRenderer( new BeanInfoPopup.BeanTreeCellRenderer( _tree ) );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 10 );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    JScrollPane scrollPane = new JScrollPane( _tree );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    content.add( scrollPane, BorderLayout.CENTER );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );

    _editorKeyListener = new EditorKeyListener();

    add( _pane, BorderLayout.CENTER );

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
        strWholePath = TextComponentUtil.getWordBeforeCaret( getEditor().getEditor() );
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

    ISymbol[] symbols = getSymbols();

    ArrayList<ISymbol> listSymbols = new ArrayList<>();
    for( ISymbol symbol : symbols )
    {
      if( strPrefix != null && symbol.getDisplayName() != null && symbol.getDisplayName().toLowerCase().startsWith( strPrefix.toLowerCase() ) )
      {
        listSymbols.add( symbol );
      }
    }

    _tree.setModel( new DefaultTreeModel( new SymbolRoot( listSymbols.toArray( new ISymbol[listSymbols.size()] ) ) ) );
    _tree.setSelectionRow( 0 );
    _tree.revalidate();
    _tree.repaint();
  }


  @Override
  public void show( Component invoker, int iX, int iY )
  {
    _bLocked = true;

    try
    {
      SymbolRoot model = (SymbolRoot)_tree.getModel().getRoot();
      int iSize = model.getChildCount();
      if( iSize == 0 )
      {
        return;
      }
      if( iSize == 1 )
      {
        BeanTree symbol = (BeanTree)model.getChildAt( 0 );
        if( _strPrefix != null && !_strPrefix.equalsIgnoreCase( symbol.getBeanNode().getName() ) )
        {
          // Auto-complete if only one matching symbol
          fireNodeChanged( _nodeListenerList, new ChangeEvent( symbol.makePath( false ) ) );
          _autoDismissed = true;
          return;
        }
      }

      super.show( invoker, iX, iY );
    }
    finally
    {
      _bLocked = false;
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

  ISymbol[] getSymbols()
  {
    return _symbols;
  }

  ISymbol[] getFilteredSymbols()
  {
    return ((SymbolRoot)_tree.getModel().getRoot())._symbols;
  }

  private String getScriptText( ISymbol symbol )
  {
    if( symbol.getType() instanceof IFunctionType && !(symbol.getType() instanceof IBlockType) )
    {
      return editor.util.EditorUtilities.buildFunctionIntellisenseString( false, (IFunctionType)symbol.getType() );
    }
    else
    {
      return symbol.getDisplayName();
    }
  }

  @Override
  public void setSelection( String strSelection )
  {
    //iterate over _list and search for a match by name
    List<String> suggestionNames = getPopupSuggestions();
    for( int currentIndex = 0; currentIndex < suggestionNames.size(); currentIndex++ )
    {
      if( suggestionNames.get( currentIndex ).equals( strSelection ) )
      {
        //select the matching suggestion's index
        _tree.setSelectionRow( currentIndex );
        break;
      }
    }
  }

  @Override
  public List<String> getPopupSuggestions()
  {
    ArrayList<String> suggestionList = new ArrayList<String>();
    SymbolRoot listObjects = (SymbolRoot)_tree.getModel().getRoot();
    for( int i = 0; i < listObjects.getChildCount(); i++ )
    {
      Object value = listObjects.getChildAt( i );
      if( value instanceof ISymbol )
      {
        suggestionList.add( getScriptText( (ISymbol)value ) );
      }
      else if( value instanceof BeanTree )
      {
        suggestionList.add( ((BeanTree)value).getBeanNode().getName() );
      }
      else
      {
        suggestionList.add( value.toString() );
      }
    }
    return suggestionList;
  }

  private void handleSelection( ISymbol symbol )
  {
    fireNodeChanged( _nodeListenerList, new ChangeEvent( getScriptText( symbol ) ) );
    setVisible( false );
  }

  private void handleSelection( BeanTree selection )
  {
    fireNodeChanged( _nodeListenerList, new ChangeEvent( selection.makePath( false ) ) );
    setVisible( false );
  }

  void fireSelection( String s )
  {
    setSelection( s );
    handleSelection( (BeanTree)_tree.getSelectionPath().getLastPathComponent() );
  }

  boolean wasAutoDismissed()
  {
    return _autoDismissed;
  }

  private IType getWhosAskin()
  {
    IScriptPartId scriptPart = getEditor().getScriptPart();
    if( scriptPart != null )
    {
      return scriptPart.getContainingType();
    }
    return null;
  }

  static String getDisplayText( ISymbol symbol )
  {
    if( symbol.getType() instanceof IFunctionType )
    {
      IFunctionType type = (IFunctionType)symbol.getType();
      return type.getName() + getParamSignatureText( symbol ) + " : " + getReturnTypeText( type.getReturnType() );
    }
    else
    {
      return symbol.getName() + " : " + getTypeDisplayText( symbol.getType() );
    }

  }

  static String getReturnTypeText( IType type )
  {
    return getTypeDisplayText( type );
  }

  static String getParamSignatureText( ISymbol symbol )
  {
    if( symbol instanceof IDynamicFunctionSymbol )
    {
      return getDynParamSignatureText( (IDynamicFunctionSymbol)symbol );
    }

    return getStaticParamSignatureText( (IFunctionType)symbol.getType() );
  }

  static String getDynParamSignatureText( IDynamicFunctionSymbol symbol )
  {
    List<ISymbol> args = symbol.getArgs();
    if( args == null || args.size() == 0 )
    {
      return "()";
    }

    String strParams = "(";
    for( int i = 0; i < args.size(); i++ )
    {
      strParams += (i == 0 ? "" : ", ") + args.get( i ).getName() + " : " + getTypeDisplayText( args.get( i ).getType() );
    }
    strParams += ")";

    return strParams;
  }

  static String getStaticParamSignatureText( IFunctionType type )
  {
    IType[] argTypes = type.getParameterTypes();
    if( argTypes == null || argTypes.length == 0 )
    {
      return "()";
    }

    String strParams = "(";
    for( int i = 0; i < argTypes.length; i++ )
    {
      strParams += (i == 0 ? "" : ", ") + getTypeDisplayText( argTypes[i] );
    }
    strParams += ")";

    return strParams;
  }

  static String getTypeDisplayText( IType type )
  {
    return type.getRelativeName();
  }

  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP ||
          e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT )
      {
        Action selectPrevious = _tree.getActionMap().get( "selectPrevious" );
        selectPrevious.actionPerformed( new ActionEvent( _tree, 0, "selectPrevious" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN ||
               e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT )
      {
        Action selectNext = _tree.getActionMap().get( "selectNext" );
        selectNext.actionPerformed( new ActionEvent( _tree, 0, "selectNext" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PAGE_UP )
      {
        Action scrollUpChangeSelection = _tree.getActionMap().get( "scrollUpChangeSelection" );
        scrollUpChangeSelection.actionPerformed( new ActionEvent( _tree, 0, "scrollUpChangeSelection" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PAGE_DOWN )
      {
        Action scrollDownChangeSelection = _tree.getActionMap().get( "scrollDownChangeSelection" );
        scrollDownChangeSelection.actionPerformed( new ActionEvent( _tree, 0, "scrollDownChangeSelection" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_ENTER ||
               e.getKeyCode() == KeyEvent.VK_SPACE ||
               e.getKeyCode() == KeyEvent.VK_TAB )
      {
        TreePath selectionPath = _tree.getSelectionPath();
        if( selectionPath != null )
        {
          BeanTree selection = (BeanTree)selectionPath.getLastPathComponent();
          if( selection != null )
          {
            handleSelection( selection );
          }
        }
        setVisible( false );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PERIOD )
      {
        handleSelectionForDot();
        e.consume();
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
      TreePath path = _tree.getSelectionPath();
      final BeanTree selection = path == null ? null : (BeanTree)path.getLastPathComponent();
      if( selection == null )
      {
        setVisible( false );
        return;
      }

      EventQueue.invokeLater(
        () -> {
          sendKeyEvent( KeyEvent.VK_BACK_SPACE );
          handleSelection( selection );
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

  class SymbolListener extends MouseAdapter
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      if( _bLocked )
      {
        return;
      }

      int iIndex = _tree.getRowForLocation( e.getX(), e.getY() );
      if( iIndex < 0 )
      {
        return;
      }

      _tree.setSelectionRow( iIndex );

      BeanTree selection = (BeanTree)_tree.getSelectionPath().getLastPathComponent();
      if( selection != null )
      {
        handleSelection( selection );
      }
    }
  }

  private class SymbolRoot implements TreeNode
  {

    private ISymbol[] _symbols;
    private List<BeanTree> _children;

    public SymbolRoot( ISymbol[] symbols )
    {
      _symbols = symbols;
      _children = new ArrayList<BeanTree>();
      for( final ISymbol symbol : symbols )
      {
        BeanTree child = null;
        if( symbol.getType() instanceof IFunctionType )
        {
          IMethodInfo mi = ((IFunctionType)symbol.getType()).getMethodInfo();
          if( mi != null )
          {
            child = new BeanTree( mi, this, getWhosAskin() );
          }
        }
        else if( symbol instanceof IDynamicPropertySymbol )
        {
          IScriptPartId scriptPart = symbol.getScriptPart();
          if( scriptPart != null )
          {
            IType type = scriptPart.getContainingType();
            if( type != null )
            {
              IPropertyInfo pi = type.getTypeInfo().getProperty( symbol.getName() );
              if( pi != null )
              {
                child = new BeanTree( type.getTypeInfo(), pi, false, getWhosAskin(), this );
              }
              else
              {
                int i = 0;
              }
            }
          }
        }
        if( child == null )
        {
          BeanInfoNode node = new BeanInfoNode( symbol.getType(), getDisplayText( symbol ) )
          {
            @Override
            public String getName()
            {
              return getScriptText( symbol );
            }

            @Override
            public String getPathComponent( boolean bFeatureLiteralCompletion )
            {
              return getScriptText( symbol );
            }
          };
          child = new BeanTree( node, getWhosAskin(), this );
        }
        _children.add( child );
      }
      sortBy_AssignableToExpectedType_ThenBy_IsFunctionType_ThenBy_Name();
    }

    private void sortBy_AssignableToExpectedType_ThenBy_IsFunctionType_ThenBy_Name()
    {
      Collections.sort( _children, ( o1, o2 ) -> {
        if( _expectedType != null )
        {
          if( isExectedTypeAssignableFrom( o1 ) )
          {
            if( !isExectedTypeAssignableFrom( o2 ) )
            {
              if( !(o1.getBeanNode().getType() instanceof IFunctionType) )
              {
                if( o2.getBeanNode().getType() instanceof IFunctionType )
                {
                  return -1;
                }
              }
              else if( !(o2.getBeanNode().getType() instanceof IFunctionType) )
              {
                return 1;
              }
              return -1;
            }
          }
          else if( isExectedTypeAssignableFrom( o2 ) )
          {
            return 1;
          }
        }
        if( !(o1.getBeanNode().getType() instanceof IFunctionType) )
        {
          if( o2.getBeanNode().getType() instanceof IFunctionType )
          {
            return -1;
          }
        }
        else if( !(o2.getBeanNode().getType() instanceof IFunctionType) )
        {
          return 1;
        }
        return o1.getBeanNode().getName().compareTo( o2.getBeanNode().getName() );
      } );
    }

    private boolean isExectedTypeAssignableFrom( BeanTree beanTree )
    {
      return _expectedType.isAssignableFrom( beanTree.getBeanNode().getType() ) ||
          (beanTree.getBeanNode().getType() instanceof IFunctionType && _expectedType.isAssignableFrom( ((IFunctionType)beanTree.getBeanNode().getType()).getReturnType() ));
    }

    @Override
    public TreeNode getChildAt( int childIndex )
    {
      return _children.get( childIndex );
    }

    @Override
    public int getChildCount()
    {
      return _symbols.length;
    }

    @Override
    public TreeNode getParent()
    {
      return null;
    }

    @Override
    public int getIndex( TreeNode node )
    {
      return _children.indexOf( node );
    }

    @Override
    public boolean getAllowsChildren()
    {
      return true;
    }

    @Override
    public boolean isLeaf()
    {
      return false;
    }

    @Override
    public Enumeration children()
    {
      return new Enumeration()
      {
        int i = 0;

        @Override
        public boolean hasMoreElements()
        {
          return _children.size() > i;
        }

        @Override
        public Object nextElement()
        {
          return _children.get( i++ );
        }
      };
    }
  }
}

