/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.config.CommonServices;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.util.CaseInsensitiveSet;
import gw.util.GosuStringUtil;
import gw.util.IFeatureFilter;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.beans.IndexedPropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;


public class BeanTree implements MutableTreeNode, Comparable<BeanTree>
{
  private static final String ARRAY_LENGTH_PREFIX = "length = ";

  private BeanInfoNode _node;
  private TreeNode _parent;
  private List<BeanTree> _children;
  private String _strNameConstraint;
  private boolean _bIncludeOnlySimpleDescriptors;
  private boolean _bChildrenEvaluated;
  //  private IDebugger _debugger;
  private Boolean _bIncludeStaticMembers;
  private IFeatureFilter _filter;
  private IType _whosaskin;
  private boolean _bExpansion = true;

  /**
   */
  public BeanTree( IType classBean, IType whosaskin, String strDisplayName, String strNameConstraint, Boolean bIncludeStaticMembers )
  {
    this( classBean, whosaskin, strDisplayName, strNameConstraint, bIncludeStaticMembers, null, true );
  }

  public BeanTree( IType classBean, IType whosaskin, String strDisplayName, String strNameConstraint, Boolean bIncludeStaticMembers, IFeatureFilter filter, boolean bExpansion )
  {
    _whosaskin = whosaskin;
    _node = new BeanInfoNode( classBean );
    _node.setDisplayName( strDisplayName );
    _strNameConstraint = strNameConstraint;
    _bIncludeStaticMembers = bIncludeStaticMembers;
    _filter = filter;
    _bExpansion = bExpansion;
    initializeChildren();
  }

  public BeanTree( IType classBean, IType whosaskin, String strDisplayName, boolean bIncludeOnlySimpleDescriptors, Boolean bIncludeStaticMembers )
  {
    this( classBean, whosaskin, strDisplayName, bIncludeOnlySimpleDescriptors, bIncludeStaticMembers, null, true );
  }

  /**
   */
  public BeanTree( IType classBean, IType whosaskin, String strDisplayName, boolean bIncludeOnlySimpleDescriptors, Boolean bIncludeStaticMembers, IFeatureFilter filter, boolean bExpansion )
  {
    _whosaskin = whosaskin;
    _node = new BeanInfoNode( classBean );
    _node.setDisplayName( strDisplayName );
    _bIncludeOnlySimpleDescriptors = bIncludeOnlySimpleDescriptors;
    _bIncludeStaticMembers = bIncludeStaticMembers;
    _filter = filter;
    _bExpansion = bExpansion;

    initializeChildren();
  }

  /**
   * @param classBean
   */
  public BeanTree( IType[] classBean, IType whosaskin )
  {
    this( classBean, whosaskin, false, false );
  }

  /**
   * @param classBean
   */
  public BeanTree( IType[] classBean, IType whosaskin, boolean bIncludeOnlySimpleDescriptors, Boolean bIncludeStaticMembers )
  {
    _whosaskin = whosaskin;
    _bIncludeOnlySimpleDescriptors = bIncludeOnlySimpleDescriptors;
    _bIncludeStaticMembers = bIncludeStaticMembers;
    _node = new BeanInfoNode( "Root" );
    _children = new ArrayList<BeanTree>();

    for( int i = 0; i < classBean.length; i++ )
    {
      String strBeanClassName = classBean[i].getName();
      int iDotIndex = strBeanClassName.lastIndexOf( '.' );
      String strSymbolName = strBeanClassName.substring( iDotIndex + 1 );

      BeanTree beanTree = new BeanTree( classBean[i], whosaskin, strSymbolName, bIncludeOnlySimpleDescriptors, bIncludeStaticMembers );
      _children.add( beanTree );
      beanTree.setParent( this );
    }
  }

  BeanTree( IMethodInfo descriptor, TreeNode parent, IType whosAskin )
  {
    _whosaskin = whosAskin;
    _node = new MethodNode( descriptor );
    _parent = parent;
  }

  private BeanTree( IMethodInfo descriptor, BeanTree parent )
  {
    _whosaskin = parent._whosaskin;
    _node = new MethodNode( descriptor );
    _parent = parent;
    //## DO NOT PROPOGATE: I'm leaving this code commented out here to prevent
    //##   some clever person from making the mistake I made. Name constraints
    //##   should only be applied to the top level.
    //## _strNameConstraint = _parent._strNameConstraint;
  }

  BeanTree( ITypeInfo owner, IPropertyInfo pi, boolean arrayicize, IType whosAskin, TreeNode parent )
  {
    _whosaskin = whosAskin;
    _node = new PropertyNode( owner, pi, arrayicize, _whosaskin );
    _parent = parent;
  }

  private BeanTree( ITypeInfo owner, IPropertyInfo pi, boolean arrayicize, BeanTree parent )
  {
    _whosaskin = parent._whosaskin;
    _node = new PropertyNode( owner, pi, arrayicize, _whosaskin );
    _parent = parent;
    //## DO NOT PROPOGATE: I'm leaving this code commented out here to prevent
    //##   some clever person from making the mistake I made. Name constraints
    //##   should only be applied to the top level.
    //## _strNameConstraint = _parent._strNameConstraint;
  }

  BeanTree( BeanInfoNode node, IType whosAskin, TreeNode parent )
  {
    _whosaskin = whosAskin;
    _node = node;
    _parent = parent;
  }

  private BeanTree( BeanInfoNode node, BeanTree parent )
  {
    _whosaskin = parent._whosaskin;
    _node = node;
    _parent = parent;
    //## DO NOT PROPOGATE: I'm leaving this code commented out here to prevent
    //##   some clever person from making the mistake I made. Name constraints
    //##   should only be applied to the top level.
    //## _strNameConstraint = _parent._strNameConstraint;
  }

  //----------------------------------------------------------------------------
  public BeanInfoNode getBeanNode()
  {
    return _node;
  }

  //----------------------------------------------------------------------------
  public void setBeanNode( BeanInfoNode node )
  {
    _node = node;
  }

  /**
   *
   */
  protected void initializeChildren()
  {
    if( _children != null )
    {
      return;
    }

    _children = new ArrayList<BeanTree>();

    IType type = _node.getType();

    if( !TypeSystem.isBeanType( type ) &&
        !(type instanceof IMetaType) &&
        type != JavaTypes.STRING() &&
        type != JavaTypes.NUMBER() &&
        !type.isArray() )
    {
      return;
    }

    try
    {
      boolean wasArray = _bExpansion && TypeSystem.isExpandable( type );
      if( wasArray )
      {
        if( type.isArray() && getBeanNode().getValue() != null )
        {
          addArrayElementNodes();
          return;
        }
        else
        {
          type = CommonServices.getTypeSystem().getExpandableComponentType( type );
        }
      }

      ITypeInfo beanInfo = null;
      try
      {
        // Note retrieving the typeinfo may throw an exception if the corresponding
        // class cannot be loaded (e.g., when the class is not in Studio's local
        // classpath, but is on the server's). We must handle this gracefully.
        beanInfo = type.getTypeInfo();
      }
      catch( Throwable t )
      {
        // ignore
      }
      if( beanInfo == null )
      {
        return;
      }

      addFeatureNodes( beanInfo, wasArray );

      Collections.sort( _children );
    }
    catch( Exception e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
      String message = e.getMessage();
      if( GosuStringUtil.isEmpty( message ) )
      {
        editor.util.EditorUtilities.displayError( e );
      }
      else
      {
        editor.util.EditorUtilities.displayError( message );
      }
    }
  }


  private void addFeatureNodes( ITypeInfo beanInfo, boolean arrayicize )
  {
    addPropertyNodes( beanInfo, arrayicize );
    if( !excludeMethodDescriptors() && !includeOnlySimpleDescriptors() )
    {
      addMethodNodes( beanInfo );
    }
  }

  /**
   *
   */
  private void addArrayElementNodes()
  {
    String strLengthValue = getBeanNode().getValue();
    if( strLengthValue == null )
    {
      return;
    }

    int iIndex = strLengthValue.indexOf( ARRAY_LENGTH_PREFIX );
    if( iIndex < 0 )
    {
      return;
    }

    strLengthValue = strLengthValue.substring( iIndex + ARRAY_LENGTH_PREFIX.length() );
    try
    {
      int iArrayLength = Integer.parseInt( strLengthValue );
      IType atomicType = _node.getType().getComponentType();
      for( int i = 0; i < iArrayLength; i++ )
      {
        _children.add( new BeanTree( new ArrayElementNode( atomicType, i ), this ) );
      }
    }
    catch( NumberFormatException nfe )
    {
      // Eat this
    }

    evaluateProperties();
  }

  /**
   * @param beanInfo
   */
  private void addMethodNodes( ITypeInfo beanInfo )
  {
    List<? extends IMethodInfo> methods = beanInfo instanceof IRelativeTypeInfo
                                          ? ((IRelativeTypeInfo)beanInfo).getMethods( _whosaskin )
                                          : beanInfo.getMethods();
    Set<String> signatures = new CaseInsensitiveSet<String>();
    if( !methods.isEmpty() )
    {
      for( IMethodInfo method : methods )
      {
        if( shouldFilter( method.getDisplayName() ) )
        {
          // Filter this method -- it doesn't match the name constraint.
          continue;
        }

        if( isHidden( beanInfo, method ) )
        {
          // Don't expose hidden methods in the composer.
          continue;
        }

        if( !mutualExclusiveStaticFilter( beanInfo, method ) )
        {
          continue;
        }

        String signature = TypeInfoUtil.getMethodSignature( method );
        if( !signatures.contains( signature ) )
        {
          _children.add( new BeanTree( method, this ) );
          signatures.add( signature );
        }
      }
    }
  }

  /**
   * @param beanInfo
   * @param arrayicize
   */
  private void addPropertyNodes( ITypeInfo beanInfo, boolean arrayicize )
  {
    List<? extends IPropertyInfo> properties = beanInfo instanceof IRelativeTypeInfo
                                               ? ((IRelativeTypeInfo)beanInfo).getProperties( _whosaskin )
                                               : beanInfo.getProperties();
    Set<String> propertyNames = new CaseInsensitiveSet<String>();
    for( IPropertyInfo property : properties )
    {
      if( shouldFilter( property.getName() ) )
      {
        // Filter this property -- it doesn't match the name constraint.
        continue;
      }

      if( property instanceof IndexedPropertyDescriptor )
      {
        IndexedPropertyDescriptor indexedProp = (IndexedPropertyDescriptor)property;
        if( indexedProp.getPropertyType() == null )
        {
          // Indexed properties must provide non-indexed access.
          continue;
        }
      }

      if( isHidden( beanInfo, property ) )
      {
        // Don't expose hidden properties in the composer.
        continue;
      }

      if( !mutualExclusiveStaticFilter( beanInfo, property ) )
      {
        continue;
      }
      if( includeOnlySimpleDescriptors() )
      {
        IType propType = property.getFeatureType();
        if( propType.isArray() || !isSimple( propType ) )
        {
          continue;
        }
      }

      String displayName = property.getDisplayName();
      if( !propertyNames.contains( displayName ) )
      {
        _children.add( new BeanTree( beanInfo, property, arrayicize && !TypeSystem.isExpandable( property.getFeatureType() ), this ) );
        propertyNames.add( displayName );
      }
    }

    evaluateProperties();
  }

  private boolean isSimple( IType cls )
  {
    return cls.isPrimitive() ||
           JavaTypes.STRING().isAssignableFrom( cls ) ||
           JavaTypes.NUMBER().isAssignableFrom( cls ) ||
           JavaTypes.BOOLEAN().isAssignableFrom( cls ) ||
           JavaTypes.DATE().isAssignableFrom( cls );
  }


  private boolean isHidden( ITypeInfo beanInfo, IAttributedFeatureInfo feature )
  {
    return feature.isHidden() ||
           !feature.isScriptable() ||
           hideDeprecated( feature ) ||
           isInternal( feature ) ||
           (getFeatureFilter() != null && !getFeatureFilter().acceptFeature( beanInfo.getOwnersType(), feature ));
  }

  private boolean mutualExclusiveStaticFilter( ITypeInfo beanInfo, IAttributedFeatureInfo descriptor )
  {
    if( includeStaticMembers() == null )
    {
      // Include both static and non-static members if null (a tri-state flag)
      return true;
    }

    if( beanInfo.getOwnersType() instanceof IMetaType ||
        includeStaticMembers() )
    {
      return descriptor.isStatic();
    }
    return !descriptor.isStatic();
  }

  private boolean isInternal( IAttributedFeatureInfo feature )
  {
    return feature instanceof IMethodInfo && feature.getDisplayName().startsWith( "@" );
  }

  private boolean shouldFilter( String strMemberName )
  {
    if( _strNameConstraint != null &&
        _strNameConstraint.length() > 0 &&
        Character.isJavaIdentifierStart( _strNameConstraint.charAt( 0 ) ) )
    {
      return !strMemberName.toLowerCase().startsWith( _strNameConstraint.toLowerCase() );
    }

    return false;
  }

  final boolean methodsEqual( Method m1, Method m2 )
  {
    if( m1 == null || m2 == null )
    {
      return false;
    }

    if( m1.getName().equals( m2.getName() ) )
    {
      Class[] params1 = m1.getParameterTypes();
      Class[] params2 = m2.getParameterTypes();
      if( params1.length == params2.length )
      {
        for( int i = 0; i < params1.length; i++ )
        {
          if( params1[i] != params2[i] )
          {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public TreeNode getChildAt( int iChildIndex )
  {
    return _children.get( iChildIndex );
  }

  @Override
  public int getChildCount()
  {
    initializeChildren();
    return _children.size();
  }

  @Override
  public TreeNode getParent()
  {
    return _parent;
  }

  public void setParent( TreeNode parent )
  {
    _parent = parent;
  }

  @Override
  public int getIndex( TreeNode node )
  {
    BeanTree beanTree = (BeanTree)node;
    return _children.indexOf( beanTree );
  }

  @Override
  public boolean getAllowsChildren()
  {
    return false;
  }

  @Override
  public boolean isLeaf()
  {
    if( _children == null )
    {
      return false;
    }
    return getChildCount() == 0;
  }

  @Override
  public Enumeration children()
  {
    initializeChildren();
    return new BeanTreeEnumeration();
  }

  @Override
  public int compareTo( BeanTree o )
  {
    if( _node == null )
    {
      return o._node == null ? 0 : -1;
    }
    else
    {
      if( o._node == null )
      {
        return 1;
      }
      return _node.compareTo( o._node );
    }
  }

  @Override
  public void insert( MutableTreeNode child, int index )
  {
    MutableTreeNode oldParent = (MutableTreeNode)child.getParent();

    if( oldParent != null )
    {
      oldParent.remove( child );
    }
    child.setParent( this );
    _children.add( index, (BeanTree)child );
  }

  @Override
  public void remove( int index )
  {
    MutableTreeNode child = (MutableTreeNode)getChildAt( index );
    _children.remove( index );
    child.setParent( null );
  }

  @Override
  public void remove( MutableTreeNode node )
  {
    //noinspection SuspiciousMethodCalls
    _children.remove( node );
    node.setParent( null );
  }

  @Override
  public void setUserObject( Object object )
  {
  }

  @Override
  public void removeFromParent()
  {
    MutableTreeNode parent = (MutableTreeNode)getParent();
    if( parent != null )
    {
      parent.remove( this );
    }
  }

  @Override
  public void setParent( MutableTreeNode newParent )
  {
    _parent = (BeanTree)newParent;
  }

  class BeanTreeEnumeration implements Enumeration
  {
    int _iCount;
    int _iChildCount = _children.size();

    public BeanTreeEnumeration()
    {
    }

    @Override
    public boolean hasMoreElements()
    {
      return (_iCount < _iChildCount);
    }

    @Override
    public Object nextElement()
    {
      if( _iCount >= _iChildCount )
      {
        return null;
      }
      else
      {
        return _children.get( _iCount++ );
      }
    }
  }

  public String makePath( boolean bFeatureLiteralCompletion )
  {
    StringBuilder path = new StringBuilder();
    TreeNode tree = this;
    while( true )
    {
      if( tree instanceof BeanTree )
      {
        BeanInfoNode node = ((BeanTree)tree).getBeanNode();
        if( node.getType() != null )
        {
          if( path.length() == 0 )
          {
            path.append( node.getPathComponent( bFeatureLiteralCompletion ) );
          }
          else
          {
            if( node.getName() != null && node.getName().length() > 0 )
            {
              path.insert( 0, '.' );
            }
            path.insert( 0, node.getName() );
          }
          tree = tree.getParent();
          continue;
        }
      }

      break;
    }

    return path.toString();
  }

  public List<BeanTree> getChildren()
  {
    return _children;
  }

  private boolean includeOnlySimpleDescriptors()
  {
    if( _bIncludeOnlySimpleDescriptors )
    {
      return true;
    }

    if( !(_parent instanceof BeanTree) )
    {
      return false;
    }
    return ((BeanTree)_parent).includeOnlySimpleDescriptors();
  }

  private boolean excludeMethodDescriptors()
  {
    if( !(_parent instanceof BeanTree) )
    {
      return false;
    }
    return ((BeanTree)_parent).excludeMethodDescriptors();
  }

  private IFeatureFilter getFeatureFilter()
  {
    if( _filter != null )
    {
      return _filter;
    }

    if( !(_parent instanceof BeanTree) )
    {
      return null;
    }
    return ((BeanTree)_parent).getFeatureFilter();
  }


  private Boolean includeStaticMembers()
  {
    return _bIncludeStaticMembers;
  }

  private void evaluateProperties()
  {
    if( isChildrenEvaluated() )
    {
      return;
    }

    setChildrenEvaluated( true );
  }

  private String getRootPath( BeanTree tree )
  {
    if( tree.getParent().getParent() == null )
    {
      return tree.getBeanNode().getName();
    }
    if( tree.getBeanNode() instanceof ArrayElementNode )
    {
      return getRootPath( (BeanTree)tree.getParent() ) + tree.getBeanNode().getName();
    }
    else
    {
      return getRootPath( (BeanTree)tree.getParent() ) + "." + tree.getBeanNode().getName();
    }
  }

  private boolean isChildrenEvaluated()
  {
    return _bChildrenEvaluated;
  }

  private void setChildrenEvaluated( boolean bChildrenEvaluated )
  {
    _bChildrenEvaluated = bChildrenEvaluated;
  }

  private boolean hideDeprecated( IAttributedFeatureInfo descriptor )
  {
    return isHideDeprecatedMembers() &&
           descriptor.isDeprecated();
  }

  public static boolean isHideDeprecatedMembers()
  {
    return true;
  }

}
