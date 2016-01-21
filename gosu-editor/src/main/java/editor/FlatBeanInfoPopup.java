/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.TextComponentUtil;
import gw.config.CommonServices;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

public class FlatBeanInfoPopup extends JPopupMenu
{
  private static final Map<IType, Set<IType>> BEAN_REFERENCE_MAP = new HashMap<IType, Set<IType>>();

  private JTree _tree;
  private EventListenerList _nodeListenerList;
  private boolean _bLocked;
  private JTextComponent _editor;
  private IType[] _classes;
  private EditorKeyListener _editorKeyListener;
  private UndoableEditListener _docListener;
  private int _iWidth;

  //--------------------------------------------------------------------------------------------------
  public FlatBeanInfoPopup( IType[] classBeans, JTextComponent editor, boolean bIncludeOnlySimpleDescriptors )
  {
    this( classBeans, editor, bIncludeOnlySimpleDescriptors, true );
  }

  //--------------------------------------------------------------------------------------------------
  public FlatBeanInfoPopup( IType[] classBeans, JTextComponent editor, boolean bIncludeOnlySimpleDescriptors, boolean bBeanGraph )
  {
    super();
    _editor = editor;
    _nodeListenerList = new EventListenerList();
    initLayout( classBeans, bIncludeOnlySimpleDescriptors, bBeanGraph );
  }

  //--------------------------------------------------------------------------------------------------
  protected void initLayout( IType[] classBeans,
                             boolean bIncludeOnlySimpleDescriptors, boolean bBeanGraph )
  {
    _classes = classBeans;

    setLayout( new BorderLayout() );
    setOpaque( false );
    setDoubleBuffered( true );

    JPanel pane = new JPanel();
    GridBagLayout gridBag = new GridBagLayout();
    pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    BeanTree beanTree;
    if( bBeanGraph )
    {
      //classBeans = getReferences( classBeans );
      Arrays.sort( classBeans,
                   new Comparator<IType>()
                   {
                     public int compare( IType o1, IType o2 )
                     {
                       String s1 = o1.getRelativeName();
                       String s2 = o2.getRelativeName();
                       return s1.compareToIgnoreCase( s2 );
                     }
                   } );
    }
    beanTree = new BeanTree( classBeans, null, bIncludeOnlySimpleDescriptors, false );
    _tree = new JTree( new DefaultTreeModel( beanTree ) )
    {
      @Override
      public Dimension getPreferredScrollableViewportSize()
      {
        Dimension dim = super.getPreferredScrollableViewportSize();
        dim.width = _iWidth; // Make the tree a bit roomier.
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
    _tree.setCellRenderer( new BeanTreeCellRenderer() );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setSelectionRow( 0 );
    _tree.setVisibleRowCount( 10 );
    _tree.expandRow( 0 );
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
    _docListener = new UndoableEditListener()
    {
      public void undoableEditHappened( UndoableEditEvent e )
      {
        filterDisplay();
      }
    };

    add( BorderLayout.CENTER, pane );
  }

  //--------------------------------------------------------------------------------------------------
  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      registerListeners();
    }
    else
    {
      unregisterListeners();
      _editor.requestFocus();
    }
  }

  //--------------------------------------------------------------------------------------------------
  void registerListeners()
  {
    unregisterListeners();

    _editor.addKeyListener( _editorKeyListener );
    _editor.getDocument().addUndoableEditListener( _docListener );
  }

  //--------------------------------------------------------------------------------------------------
  void unregisterListeners()
  {
    _editor.getDocument().removeUndoableEditListener( _docListener );
    _editor.removeKeyListener( _editorKeyListener );
  }

  //--------------------------------------------------------------------------------------------------
  IType[] getReferences( IType[] classBeans )
  {
    Set<IType> setAggregate = new HashSet<IType>();
    for( IType classBean : classBeans )
    {
      Set<IType> s = getClassReferenceSet( classBean );
      setAggregate.addAll( s );
    }

    return setAggregate.toArray( new IType[setAggregate.size()] );
  }

  //--------------------------------------------------------------------------------------------------
  void filterDisplay()
  {
//    if( _bShowBeanRoots )
//    {
//      // Only filtering for "code completion" popups.
//      return;
//    }

    String strWholePath = TextComponentUtil.getWordBeforeCaret( _editor );
    String strPrefix = strWholePath;
    if( strWholePath != null && strWholePath.length() > 0 )
    {
      int iDotIndex = strWholePath.lastIndexOf( '.' );
      if( iDotIndex >= 0 )
      {
        strPrefix = strWholePath.substring( iDotIndex + 1 );
      }
    }

    BeanTree beanTree = new BeanTree( _classes[0], null, "", strPrefix, false );

    _tree.setModel( new DefaultTreeModel( beanTree ) );
    _tree.setSelectionRow( 0 );
    _tree.revalidate();
    _tree.repaint();
  }

  //----------------------------------------------------------------------------------------------
  public void show( Component invoker, int iX, int iY, int iWidth )
  {
    _bLocked = true;

    try
    {
      _iWidth = iWidth;
      BeanTree root = (BeanTree)_tree.getModel().getRoot();
      if( root == null || root.getChildCount() == 0 )
      {
        return;
      }

      super.show( invoker, iX, iY );
    }
    finally
    {
      _bLocked = false;
    }
  }

  //----------------------------------------------------------------------------------------------
  public void addNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.add( ChangeListener.class, l );
  }

  //----------------------------------------------------------------------------------------------
  public void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  //--------------------------------------------------------------------------------------------------
  protected void fireNodeChanged( final EventListenerList list, final ChangeEvent e )
  {
    EventQueue.invokeLater( new Runnable()
    {
      public void run()
      {
        fireNodeChangedNow( list, e );
      }
    } );
  }

  //--------------------------------------------------------------------------------------------------
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

  static void initBeans()
  {
  }

  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
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
        TreePath path = _tree.getSelectionPath();
        if( path == null )
        {
          e.consume();
          return;
        }
        BeanTree tree = (BeanTree)path.getLastPathComponent();

        fireNodeChanged( _nodeListenerList, new ChangeEvent( tree ) );

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


  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  class BeanTreeListener extends MouseAdapter
  {
    //----------------------------------------------------------------------------------------------
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


  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  class BeanTreeCellRenderer extends JLabel implements TreeCellRenderer
  {
    protected boolean _bSelected;
    protected BeanTree _node;


    //----------------------------------------------------------------------------------------------
    synchronized public Component getTreeCellRendererComponent( JTree tree, Object value,
                                                                boolean bSelected, boolean bExpanded,
                                                                boolean bLeaf, int iRow, boolean bHasFocus )
    {
      if( value != null )
      {
        _node = (BeanTree)value;
        _bSelected = bSelected;
        configure();
      }

      return this;
    }

    //----------------------------------------------------------------------------------------------
    public void update()
    {
      _tree.repaint();
    }

    //----------------------------------------------------------------------------------------------
    public void configure()
    {
      if( _node == null )
      {
        return;
      }

      BeanInfoNode node = _node.getBeanNode();

      ImageIcon icon = null;

      setText( node.getDisplayName() );

      if( node instanceof MethodNode )
      {
        IMethodInfo mi = ((MethodNode)node).getMethodDescriptor();
        if( mi.isPrivate() )
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Method_Private.png" );
        }
        else if( mi.isInternal() )
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Method_Sealed.png" );
        }
        else if( mi.isProtected() )
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Method_Protected.png" );
        }
        else
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Method.png" );
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
          icon = editor.util.EditorUtilities.loadIcon( "images/Field_Private.png" );
        }
        else if( pi.isInternal() )
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Field_Sealed.png" );
        }
        else if( pi.isProtected() )
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Field_Protected.png" );
        }
        else
        {
          icon = editor.util.EditorUtilities.loadIcon( "images/Field.png" );
        }

        if( pi.isDeprecated() )
        {
          setText( "<html><strike>" + getText() + "</strike></html>" );
        }
      }

      setIcon( icon );
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void paint( Graphics g )
    {
      Color bkColor;

      if( _bSelected )
      {
        bkColor = _tree.isEnabled() ? UIManager.getColor( "textHighlight" ) : editor.util.EditorUtilities.CONTROL_SHADOW;
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
        Icon currentIcon = getIcon();

        g.setColor( bkColor );
        if( currentIcon != null && getText() != null )
        {
          int offset = (currentIcon.getIconWidth() + getIconTextGap() - 1);

          g.fillRect( offset, 0, getWidth() - 1 - offset, getHeight() - 1 );

          if( _bSelected && _tree.hasFocus() )
          {
            g.setColor( _tree.isEnabled() ? UIManager.getColor( "textHighlightText" ) : editor.util.EditorUtilities.CONTROL_LIGHT );
            BasicGraphicsUtils.drawDashedRect( g, offset, 0, getWidth() - 1 - offset, getHeight() - 1 );
          }

        }
        else
        {
          g.fillRect( 0, 0, getWidth() - 1, getHeight() - 1 );

          if( _bSelected && _tree.hasFocus() )
          {
            g.setColor( _tree.isEnabled() ? UIManager.getColor( "textHighlightText" ) : editor.util.EditorUtilities.CONTROL_LIGHT );
            BasicGraphicsUtils.drawDashedRect( g, 0, 0, getWidth() - 1, getHeight() - 1 );
          }
        }
        g.setColor( bkColor );
      }

      setForeground( _bSelected ? _tree.isEnabled() ? UIManager.getColor( "textHighlightText" )
                                                    : editor.util.EditorUtilities.CONTROL_LIGHT
                                : _tree.isEnabled() ? UIManager.getColor( "textText" )
                                                    : editor.util.EditorUtilities.CONTROL_SHADOW );
      super.paint( g );
    }

    //----------------------------------------------------------------------------------------------
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


  /**
   * From the specified source class builds a set of classes derived from Bean.
   * The source class's reference graph is navigated exhaustively.
   *
   * @param classSource The class of the bean to traverse.
   *
   * @return The set of Bean derived classes referencable from the source class.
   */
  public static Set<IType> getClassReferenceSet( IType classSource )
  {
    Set<IType> setBeanReferences = BEAN_REFERENCE_MAP.get( classSource );
    if( setBeanReferences == null )
    {
      setBeanReferences = new HashSet<IType>();
    }

    getClassReferenceSet( classSource, setBeanReferences, new HashSet<IType>() );

    BEAN_REFERENCE_MAP.put( classSource, setBeanReferences );

    return setBeanReferences;
  }

  private static void getClassReferenceSet( IType classSource, Set<IType> setBeanReferences, Set<IType> setCyclicPrevention )
  {
    try
    {
      if( classSource.isArray() )
      {
        classSource = classSource.getComponentType();
        getClassReferenceSet( classSource, setBeanReferences, setCyclicPrevention );
        return;
      }

      if( setCyclicPrevention.contains( classSource ) )
      {
        // Short circuit cyclic bean info references
        return;
      }
      else
      {
        setCyclicPrevention.add( classSource );
      }


      if( !CommonServices.getEntityAccess().isEntityClass( classSource ) )
      {
        return;
      }

      setBeanReferences.add( classSource );

      //
      // Properties
      //

      ITypeInfo beanInfo = classSource.getTypeInfo();
      if( beanInfo == null )
      {
        return;
      }

      java.util.List<? extends IPropertyInfo> properties = TypeSystem.getProperties( beanInfo, classSource );
      for( IPropertyInfo pi : properties )
      {
        if( pi.isHidden() )
        {
          continue;
        }

        getClassReferenceSet( pi.getFeatureType(), setBeanReferences, setCyclicPrevention );
      }

      //
      // Methods
      //

      java.util.List<? extends IMethodInfo> methods = TypeSystem.getMethods( beanInfo, beanInfo.getOwnersType() );
      for( IMethodInfo method : methods )
      {
        if( method.isHidden() )
        {
          continue;
        }

        getClassReferenceSet( method.getReturnType(), setBeanReferences, setCyclicPrevention );
        IParameterInfo[] paramTypes = method.getParameters();
        if( paramTypes != null )
        {
          for( IParameterInfo paramType : paramTypes )
          {
            getClassReferenceSet( paramType.getFeatureType(), setBeanReferences, setCyclicPrevention );
          }
        }
      }

      //
      // Interfaces
      //

      Class[] interfaces = getImplementedInterfaces( classSource );
      for( Class anInterface : interfaces )
      {
        getClassReferenceSet( TypeSystem.get( anInterface ), setBeanReferences, setCyclicPrevention );
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }


  /**
   * Get the complete array of interfaces implemented/extended by the specified
   * class or interface.
   *
   * @param cls The class or interface to inspect.
   *
   * @return An array of <i>all</i> the interfaces the specified class/interface
   * implements/extends.
   */
  public static Class[] getImplementedInterfaces( IType cls )
  {
    if( !(cls instanceof IJavaType) )
    {
      return new Class[0];
    }
    HashSet<Class> interfacesSet = new HashSet<Class>();
    getImplementedInterfaces( ((IJavaType)cls).getIntrinsicClass(), interfacesSet );
    return interfacesSet.toArray( new Class[interfacesSet.size()] );
  }

  /**
   * Get the complete set of interfaces implemented/extended by the specified
   * class or interface.
   *
   * @param cls           The class or interface to inspect.
   * @param interfacesSet The resulting set of implemented/extended interfaces.
   *
   * @see #getImplementedInterfaces(gw.lang.reflect.IType)
   */
  private static void getImplementedInterfaces( Class cls, Set<Class> interfacesSet )
  {
    Class[] interfaces = cls.getInterfaces();
    for( Class anInterface : interfaces )
    {
      if( !interfacesSet.contains( anInterface ) )
      {
        if( !CommonServices.getEntityAccess().isInternal( TypeSystem.get( anInterface ) ) )
        {
          interfacesSet.add( anInterface );
          getImplementedInterfaces( anInterface, interfacesSet );
        }
      }
    }
  }
}

