/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.ContainerMoverSizer;
import editor.util.ContainerSizer;
import editor.util.EditorUtilities;
import editor.util.TextComponentUtil;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.IFeatureFilter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 */
public class BeanInfoPopup extends EditorBasedPopup implements ISelectionPopup
{
  private JTree _tree;
  private EventListenerList _nodeListenerList = new EventListenerList();
  private boolean _bLocked;
  IType[] _classes;
  private boolean _bShowBeanRoots;
  EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;
  private int _iInitialCaretPos;
  private boolean _bConstrainByLastPathElement;
  private String _strCompleteCodePrefix;
  String _strMemberPath;
  private boolean _bDOA;
  private IFeatureFilter _filter;
  private boolean _bExpansion;
  private boolean _bForFeatureLiteral;

  public BeanInfoPopup( IType classBean, String strMemberPath, GosuEditor editor ) throws ParseException
  {
    this( classBean, strMemberPath, false, editor, null );
  }

  public BeanInfoPopup( IType classBean, String strMemberPath, boolean bConstrainByLastPathElement,
                        GosuEditor editor, IFeatureFilter filter ) throws ParseException
  {
    this( classBean, strMemberPath, bConstrainByLastPathElement, editor, filter, false );
  }

  public BeanInfoPopup( IType classBean, String strMemberPath, boolean bConstrainByLastPathElement,
                        GosuEditor editor, IFeatureFilter filter, boolean bExpansion ) throws ParseException
  {
    this( classBean, strMemberPath, bConstrainByLastPathElement, editor, filter, bExpansion, false );
  }

  public BeanInfoPopup( IType classBean, String strMemberPath, boolean bConstrainByLastPathElement,
                        GosuEditor editor, IFeatureFilter filter, boolean bExpansion, boolean bForFeatureLiteral ) throws ParseException
  {
    super( editor );

    _bConstrainByLastPathElement = bConstrainByLastPathElement;
    _strMemberPath = strMemberPath;
    _filter = filter;
    _bExpansion = bExpansion;
    _bForFeatureLiteral = bForFeatureLiteral;

    // If the member path is not empty, then the _strCompleteCodePrefix is the member path.
    // Else if it is empty, _strCompleteCodePrefix is null.
    _strCompleteCodePrefix = !isMemberPathEmpty( strMemberPath )
                             ? strMemberPath
                             : null;

    initLayout( new IType[]{classBean}, false );
  }

  private boolean isMemberPathEmpty( String strMemberPath )
  {
    return strMemberPath == null || strMemberPath.length() == 0 || (strMemberPath.equalsIgnoreCase( "." ) || strMemberPath.equalsIgnoreCase( "#" ));
  }

  public boolean isForFeatureLiteral()
  {
    return _bForFeatureLiteral;
  }

  public void setForFeatureLiteral( boolean bForFeatureLiteral )
  {
    _bForFeatureLiteral = bForFeatureLiteral;
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

  public IType[] getClasses()
  {
    return _classes;
  }

  public IType getRootOfTree()
  {
    BeanTree beanTree = (BeanTree)_tree.getModel().getRoot();
    return beanTree.getBeanNode().getType();
  }

  /**
   * Subclasses should return true
   *
   * @return True if the
   */
  public Boolean isForStaticAccess()
  {
    if( isForFeatureLiteral() )
    {
      // null here implies both static and non-static features are presented
      return null;
    }
    return false;
  }

  public boolean isDOA()
  {
    return _bDOA;
  }

  private void setDOA( boolean bDOA )
  {
    _bDOA = bDOA;
  }

  public IFeatureFilter getFeatureInfoFilter()
  {
    return _filter;
  }

  protected void initLayout( IType[] classBeans, final boolean bShowBeanRoots )
  {
    _iInitialCaretPos = getEditor().getEditor().getCaretPosition();

    boolean bFilterRootBeans = false;
    if( classBeans == null || classBeans.length == 0 || (classBeans.length == 1 && classBeans[0] == null) )
    {
      classBeans = getAllBeans();
      bFilterRootBeans = true;
    }

    _classes = classBeans;
    _bShowBeanRoots = bShowBeanRoots;

    setLayout( new BorderLayout() );
    setOpaque( false );
    setDoubleBuffered( true );

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    JPanel pane = new JPanel();
    GridBagLayout gridBag = new GridBagLayout();
    pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    int iY = 0;
    if( _classes.length == 1 )
    {
      JLabel labelTypeName = new JLabel( TypeSystem.getGenericRelativeName( _classes[0], true ) );
      labelTypeName.setOpaque( true );
      labelTypeName.setBackground( Scheme.active().getControl() );
      labelTypeName.setFont( labelTypeName.getFont().deriveFont( Font.BOLD ) );
      labelTypeName.setBorder( BorderFactory.createEmptyBorder( 0, 3, 3, 3 ) );
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 1;
      c.weighty = 0;
      pane.add( labelTypeName, c );
    }

    //
    // The Bean Tree
    //
    BeanTree beanTree;
    if( !bShowBeanRoots && classBeans.length == 1 )
    {
      beanTree = new BeanTree( classBeans[0], getWhosAskin(), "", false, isForStaticAccess(), _filter, _bExpansion );
    }
    else if( classBeans.length == 1 )
    {
      beanTree = new BeanTree( classBeans, getWhosAskin(), false, isForStaticAccess() );
    }
    else
    {
      beanTree = new BeanTree( classBeans, getWhosAskin() );
    }
    _tree = new JTree( new DefaultTreeModel( beanTree ) )
    {
      @Override
      public Dimension getPreferredScrollableViewportSize()
      {
        Dimension dim = super.getPreferredScrollableViewportSize();
        dim.width *= (bShowBeanRoots ? 3 : 1); // Make the tree a bit roomier.
        dim.width = Math.min( dim.width, 480 );
        dim.height *= (bShowBeanRoots ? 2 : 1); // Make the tree a bit roomier.
        return dim;
      }

      @Override
      public boolean isFocusable()
      {
        return false;
      }
    };
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.addMouseListener( new BeanTreeListener() );
    _tree.setCellRenderer( new BeanTreeCellRenderer( _tree ) );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setSelectionRow( 0 );
    _tree.setRowHeight( 20 );
    _tree.setVisibleRowCount( 10 );
    _tree.getActionMap().put( "scrollUpChangeSelection", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        int[] selectionRows = _tree.getSelectionRows();
        if( selectionRows != null )
        {
          int selectionRow = selectionRows[0];
          selectionRow = Math.max( 0, selectionRow - 10 );
          _tree.setSelectionRow( selectionRow );
          _tree.scrollRowToVisible( selectionRow );
        }
      }
    } );
    _tree.getActionMap().put( "scrollDownChangeSelection", new AbstractAction()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        int[] selectionRows = _tree.getSelectionRows();
        if( selectionRows != null )
        {
          int selectionRow = selectionRows[0];
          selectionRow = Math.min( _tree.getRowCount() - 1, selectionRow + 10 );
          _tree.setSelectionRow( selectionRow );
          _tree.scrollRowToVisible( selectionRow );
        }
      }
    } );
    JScrollPane scrollPane = new JScrollPane( _tree );
    scrollPane.setBorder( UIManager.getBorder( "TextField.border" ) );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    pane.add( scrollPane, c );

    _editorKeyListener = new EditorKeyListener();
    _docListener = e -> filterDisplay();

    content.add( pane, BorderLayout.CENTER );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );

    if( _strCompleteCodePrefix != null )
    {
      filterDisplay( bFilterRootBeans );
      //if nothing is visible, display all
      if( _tree.getModel().getChildCount( _tree.getModel().getRoot() ) == 0 )
      {
        _tree.setModel( new DefaultTreeModel( beanTree ) );
        _tree.setSelectionRow( 0 );
        _tree.revalidate();
        _tree.repaint();
      }
    }

    add( content, BorderLayout.CENTER );
  }

  private IType[] getAllBeans()
  {
    ArrayList<IType> beans = new ArrayList<IType>();
    Collection<ISymbol> list = getEditor().getSymbolTable().getSymbols().values();
    for( ISymbol sym : list )
    {
      if( TypeSystem.isBeanType( sym.getType() ) )
      {
        beans.add( sym.getType() );
      }
    }
    return beans.toArray( new IType[beans.size()] );
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      registerListeners();
      EditorUtilities.removePopupBorder( this );
    }
    else
    {
      unregisterListeners();

      getEditor().getEditor().requestFocus();
    }
  }

  @Override
  public void setSelection( String strMember )
  {
    setSelection( strMember, false );
  }

  public void setSelection( String strMember, boolean bSilent )
  {
    if( strMember == null )
    {
      return;
    }

    strMember = strMember.toLowerCase();

    BeanTree root = (BeanTree)_tree.getModel().getRoot();
    for( int i = 0; i < root.getChildCount(); i++ )
    {
      BeanTree child = (BeanTree)root.getChildAt( i );
      BeanInfoNode node = child.getBeanNode();
      String strNodeName = node.getName().toLowerCase();
      if( strNodeName.startsWith( strMember + (node instanceof MethodNode ? "(" : "") ) )
      {
        TreePath path = new TreePath( new BeanTree[]{root, child} );
        _tree.setSelectionPath( path );
        _tree.scrollPathToVisible( path );
        if( !bSilent )
        {
          _editorKeyListener.handleSelection();
        }
        return;
      }
    }
  }

  @Override
  public List<String> getPopupSuggestions()
  {
    ArrayList<String> suggestionNames = new ArrayList<>();
    BeanTree root = (BeanTree)_tree.getModel().getRoot();
    for( int i = 0; i < root.getChildCount(); i++ )
    {
      BeanTree child = (BeanTree)root.getChildAt( i );
      BeanInfoNode node = child.getBeanNode();
      String strNodeName = node.getName().toLowerCase();
      suggestionNames.add( strNodeName );
    }
    return suggestionNames;
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
    getEditor().setCompleteCode( true );
    GosuEditor.postTaskInParserThread( () -> {
      if( getEditor().isCompleteCode() )
      {
        try
        {
          filterDisplay( false );
        }
        finally
        {
          getEditor().setCompleteCode( false );
        }
      }
    } );
  }

  void filterDisplay( boolean bFilterRootBeans )
  {
    if( _bShowBeanRoots )
    {
      // Only filtering for "code completion" popups.
      return;
    }

    String strWholePath = TextComponentUtil.getPartialWordBeforeCaret( getEditor().getEditor() );
    if( isOutOfScope() )
    {
      setDOA( true );
      setVisible( false );
      return;
    }

    String strPrefix = strWholePath;
    if( strWholePath != null && strWholePath.length() > 0 )
    {
      int iDotIndex = strWholePath.lastIndexOf( '.' );
      if( iDotIndex < 0 )
      {
        iDotIndex = strWholePath.lastIndexOf( '#' );
      }
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

//    if( !StringUtils.isEmpty( strPrefix ) )
//    {
    BeanTree beanTree = null;
    if( _classes.length > 1 || bFilterRootBeans )
    {
      for( IType aClass : _classes )
      {
        String strClass = TypeSystem.getUnqualifiedClassName( aClass );
        List<IType> classList = new ArrayList<IType>();
        if( !shouldFilterClass( strClass, strPrefix ) )
        {
          classList.add( aClass );
        }
        beanTree = new BeanTree( classList.toArray( new IType[classList.size()] ), getWhosAskin() );
      }
    }
    else
    {
      beanTree = new BeanTree( _classes[0], getWhosAskin(), "", strPrefix, isForStaticAccess(), _filter, _bExpansion );
    }

    final BeanTree beanTree1 = beanTree;
    SwingUtilities.invokeLater( () -> {
      _tree.setModel( new DefaultTreeModel( beanTree1 ) );
      _tree.setSelectionRow( 0 );
      _tree.revalidate();
      _tree.repaint();
    } );
//    }
  }

  private boolean shouldFilterClass( String strClass, String strPrefix )
  {
    if( strPrefix != null && strPrefix.length() > 0 &&
        Character.isJavaIdentifierStart( strPrefix.charAt( 0 ) ) )
    {
      return !strClass.toLowerCase().startsWith( strPrefix.toLowerCase() );
    }

    return false;
  }

  boolean isOutOfScope()
  {
    return !_bConstrainByLastPathElement && getEditor().getEditor().getCaretPosition() <= _iInitialCaretPos;
  }

  @Override
  public void show( Component invoker, int iX, int iY )
  {
    _bLocked = true;

    try
    {
      BeanTree root = (BeanTree)_tree.getModel().getRoot();
      if( root == null || root.getChildCount() == 0 )
      {
        return;
      }

      if( root.getChildCount() == 1 )
      {
        TreePath path = _tree.getSelectionPath();
        if( path == null )
        {
          return;
        }
        BeanTree tree = (BeanTree)path.getLastPathComponent();
        String strBeanNodeName = tree.getBeanNode().getName();
        int iIndexParen = strBeanNodeName.indexOf( '(' );
        if( iIndexParen > 0 )
        {
          // This is a method, just get the name without the function stuff.
          strBeanNodeName = strBeanNodeName.substring( 0, iIndexParen );
        }

        String strWordAtCaret = TextComponentUtil.getWordAtCaret( getEditor().getEditor() );
        if( _strCompleteCodePrefix != null && !_strCompleteCodePrefix.equalsIgnoreCase( strBeanNodeName ) &&
            !_strCompleteCodePrefix.equalsIgnoreCase( strWordAtCaret ) )
        {
          // Auto-complete when the text in the editor is *not* identical to the one and only field/method name.
          setReplaceWholeWord( true );
          fireNodeChanged( _nodeListenerList, new ChangeEvent( tree ) );
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

  public void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  public boolean isModelEmpty()
  {
    BeanTree root = (BeanTree)_tree.getModel().getRoot();
    return root == null || root.getChildCount() == 0;
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

  static class DotWasTypedChangeEvent extends ChangeEvent
  {
    public DotWasTypedChangeEvent( Object source )
    {
      super( source );
    }
  }

  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( final KeyEvent e )
    {
      //ignoring keystrokes with control modifiers
      if( e.isControlDown() )
      {
        if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
            e.getKeyCode() == KeyEvent.VK_DELETE ||
            e.getKeyCode() == KeyEvent.VK_LEFT ||
            e.getKeyCode() == KeyEvent.VK_RIGHT ||
            e.getKeyCode() == KeyEvent.VK_V )
        {
          return;
        }
        else if( e.getKeyCode() == KeyEvent.VK_Z )
        {
          setVisible( false );
          return;
        }
        else
        {
          e.consume();
          return;
        }
      }

      //ignoring keystrokes with alt modifiers
      if( e.getModifiers() == InputEvent.ALT_MASK )
      {
        e.consume();
        return;
      }

      if( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP )
      {
        Action selectPrevious = _tree.getActionMap().get( "selectPrevious" );
        selectPrevious.actionPerformed( new ActionEvent( _tree, 0, "selectPrevious" ) );
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN )
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
        setReplaceWholeWord( e.getKeyCode() == KeyEvent.VK_TAB );
        handleSelection();
        e.consume();
      }
      else if( e.getKeyCode() == KeyEvent.VK_PERIOD )
      /* && ((BeanTree)_tree.getSelectionPath().getLastPathComponent()).getParent()._strNameConstant
      should match the current selection otherwise we are inserting null or a value unknown to intellisense */
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
    void handleSelectionForDot()
    {
      final TreePath path = _tree.getSelectionPath();
      if( path == null )
      {
        setVisible( false );
        return;
      }

      EventQueue.invokeLater(
        () -> {
          sendKeyEvent( KeyEvent.VK_BACK_SPACE );
          BeanTree tree = (BeanTree)path.getLastPathComponent();
          fireNodeChanged( _nodeListenerList, new DotWasTypedChangeEvent( tree ) );
          setVisible( false );
          EventQueue.invokeLater( () -> sendKeyEvent( KeyEvent.VK_PERIOD ) );
        } );
    }

    private void handleSelection()
    {
      TreePath path = _tree.getSelectionPath();
      if( path == null )
      {
        return;
      }
      handleSelection( path );
    }

    private void handleSelection( TreePath path )
    {
      BeanTree tree = (BeanTree)path.getLastPathComponent();

      fireNodeChanged( _nodeListenerList, new ChangeEvent( tree ) );
      setVisible( false );
    }

    private void sendKeyEvent( final int iKey )
    {
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, iKey, KeyEvent.CHAR_UNDEFINED ) );
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, iKey, KeyEvent.CHAR_UNDEFINED ) );
      getEditor().getEditor().dispatchEvent( new KeyEvent( getEditor().getEditor(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, (char)iKey ) );
    }
  }

  class BeanTreeListener extends MouseAdapter
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      if( _bLocked )
      {
        return;
      }

      TreePath path = _tree.getPathForLocation( e.getX(), e.getY() );
      if( path == null )
      {
        return;
      }

      BeanTree tree = (BeanTree)path.getLastPathComponent();

      setVisible( false );
      fireNodeChanged( _nodeListenerList, new ChangeEvent( tree ) );
      e.consume();
    }
  }

  static class BeanTreeCellRenderer extends JLabel implements TreeCellRenderer
  {
    private JTree _tree;
    protected boolean _bSelected;
    protected BeanTree _node;

    public BeanTreeCellRenderer( JTree tree )
    {
      _tree = tree;
    }

    @Override
    synchronized public Component getTreeCellRendererComponent( JTree tree, Object value,
                                                                boolean bSelected, boolean bExpanded,
                                                                boolean bLeaf, int iRow, boolean bHasFocus )
    {
      if( value instanceof BeanTree )
      {
        _node = (BeanTree)value;
        _bSelected = bSelected;
        configure();
      }

      return this;
    }

    public void update()
    {
      _tree.repaint();
    }

    public void configure()
    {
      if( _node == null )
      {
        return;
      }

      BeanInfoNode node = _node.getBeanNode();


      setText( node.getDisplayName() );

      ImageIcon icon;
      if( node instanceof MethodNode )
      {
        IMethodInfo mi = ((MethodNode)node).getMethodDescriptor();
        if( mi.isPrivate() )
        {
          icon = EditorUtilities.loadIcon( "images/Method.png" );
        }
        else if( mi.isInternal() )
        {
          icon = EditorUtilities.loadIcon( "images/Method.png" );
        }
        else if( mi.isProtected() )
        {
          icon = EditorUtilities.loadIcon( "images/Method.png" );
        }
        else
        {
          icon = EditorUtilities.loadIcon( "images/Method.png" );
        }

        if( mi.isDeprecated() )
        {
          setText( "<html><strike>" + getText() + "</strike></html>" );
        }
      }
      else if( node instanceof PropertyNode )
      {
        IPropertyInfo pi = ((PropertyNode)node).getPropertyDescriptor();
        if( pi.isPrivate() )
        {
          icon = EditorUtilities.loadIcon( "images/property.png" );
        }
        else if( pi.isInternal() )
        {
          icon = EditorUtilities.loadIcon( "images/property.png" );
        }
        else if( pi.isProtected() )
        {
          icon = EditorUtilities.loadIcon( "images/property.png" );

        }
        else
        {
          icon = EditorUtilities.loadIcon( "images/property.png" );
        }

        if( pi.isDeprecated() )
        {
          setText( "<html><strike>" + getText() + "</strike></html>" );
        }
      }
      else if( node.getType() instanceof IFunctionType )
      {
        icon = EditorUtilities.loadIcon( "images/Method.png" );
      }
      else
      {
        icon = EditorUtilities.loadIcon( "images/variable.png" );
      }
      setIcon( icon );
    }

    /** */
    public void paint( Graphics g )
    {
  //    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
  //                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
  //    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_RENDERING,
  //                                      RenderingHints.VALUE_RENDER_QUALITY );
      Color bkColor;

      if( _bSelected )
      {
        bkColor = _tree.isEnabled()
                  ? Scheme.active().getActiveCaption()
                  : Scheme.active().getControl();
      }
      else
      {
        bkColor = _tree.getBackground();
        if( bkColor == null )
        {
          bkColor = getBackground();
        }
      }

      if( bkColor != null )
      {
        g.setColor( bkColor );
        g.fillRect( 0, 0, getWidth() - 1, getHeight() - 1 );

        if( _bSelected )
        {
          g.setColor( _tree.isEnabled() ? Scheme.active().getXpBorderColor() : Scheme.active().getControlShadow() );
          g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
        }
        g.setColor( bkColor );
      }

      setForeground( Scheme.active().getWindowText() );

      super.paint( g );
    }

    @Override
    public Dimension getPreferredSize()
    {
      Dimension dim = super.getPreferredSize();

      if( dim != null )
      {
        dim = new Dimension( dim.width + 3, dim.height );
      }

      return dim;
    }
  }
}