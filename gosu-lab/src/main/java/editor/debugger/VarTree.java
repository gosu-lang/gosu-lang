package editor.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import editor.LabFrame;
import editor.MessagesPanel;
import editor.Scheme;
import editor.util.EditorUtilities;
import static editor.util.EditorUtilities.hex;


import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 */
public class VarTree implements MutableTreeNode
{
  private final String _name;
  private final String _type;
  private final Value _value;
  private VarTree _parent;
  private List<VarTree> _children;

  public VarTree( StackFrame frame )
  {
    _name = null;
    _type = null;
    _value = null;
    _children = Collections.emptyList();
    if( frame == null )
    {
      return;
    }
    ObjectReference thisObj = frame.thisObject();
    if( thisObj != null )
    {
      insert( new VarTree( "this", thisObj.referenceType().name(), thisObj ) );
    }
    try
    {
      Map<LocalVariable, Value> values = frame.getValues( frame.visibleVariables() );
      for( LocalVariable v: frame.visibleVariables() )
      {
        insert( new VarTree( v.name(), v.typeName(), values.get( v ) ) );
      }
    }
    catch( AbsentInformationException e )
    {
      // eat
    }
  }

  public VarTree( String name, String type, Value value )
  {
    this( name, type, value, true );
  }
  private VarTree( String name, String type, Value value, boolean bExpand )
  {
    _name = name;
    _type = type;
    _value = value;
    _children = Collections.emptyList();
    if( bExpand )
    {
      expand();
    }
  }

  public String getName()
  {
    return _name;
  }

  public String getType()
  {
    return _type;
  }

  public Value getValue()
  {
    return _value;
  }

  private void expand()
  {
    if( _children == Collections.<VarTree>emptyList() )
    {
      _children = new ArrayList<>();
      showValue( _value );
    }
  }

  private void showValue( Value value )
  {
    switch( getValueKind( value ) )
    {
      case Null:
        break;

      case Primitive:
        break;

      case Array:
      {
        showArrayElements( (ArrayReference)value );
        break;
      }

      case Object:
      {
        ObjectReference ref = (ObjectReference)value;
        ReferenceType type = ref.referenceType();
        showFields( ref, type );
        break;
      }

      case Collection:
      {
        ObjectReference ref = (ObjectReference)value;
        ReferenceType type = ref.referenceType();
        Method toArray = type.methodsByName( "toArray" ).stream().filter( e -> e.argumentTypeNames().size() == 0 ).collect( Collectors.toList() ).get( 0 );
        ThreadReference thread = LabFrame.instance().getGosuPanel().getDebugger().getSuspendedThread();
        try
        {
          ArrayReference elements = (ArrayReference)ref.invokeMethod( thread, toArray, Collections.emptyList(), 0 );
          showValue( elements );
          break;
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }

      case Map:
      {
        ObjectReference ref = (ObjectReference)value;
        ReferenceType type = ref.referenceType();
        Method toArray = type.methodsByName( "entrySet" ).stream().filter( e -> e.argumentTypeNames().size() == 0 ).collect( Collectors.toList() ).get( 0 );
        ThreadReference thread = LabFrame.instance().getGosuPanel().getDebugger().getSuspendedThread();
        try
        {
          ObjectReference entries = (ObjectReference)ref.invokeMethod( thread, toArray, Collections.emptyList(), 0 );
          showValue( entries );
          break;
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }

      default:
        throw new UnsupportedOperationException();
    }
  }

  static ValueKind getValueKind( Value value )
  {
    if( value == null )
    {
      return ValueKind.Null;
    }
    if( value instanceof PrimitiveValue )
    {
      return ValueKind.Primitive;
    }
    if( value instanceof ArrayReference )
    {
      return ValueKind.Array;
    }
    ClassType classType = (ClassType)((ObjectReference)value).referenceType();
    List<String> interfaces = classType.allInterfaces().stream().map( ReferenceType::name ).collect( Collectors.toList() );
    if( interfaces.contains( "java.util.Collection" ) )
    {
      return ValueKind.Collection;
    }
    if( interfaces.contains( "java.util.Map" ) )
    {
      return ValueKind.Map;
    }
    return ValueKind.Object;
  }

  private void showFields( ObjectReference ref, ReferenceType type )
  {
    for( Field field : type.allFields() )
    {
      if( field.isFinal() && field.isStatic() )
      {
        continue;
      }
      Value fvalue = ref.getValue( field );
      insert( new VarTree( field.name(), field.typeName(), fvalue, false ) );
    }
  }

  private void showArrayElements( ArrayReference ref )
  {
    List<Value> elems = ref.getValues();
    for( int i = 0; i < elems.size(); i++ )
    {
      Value elem = elems.get( i );
      if( elem != null )
      {
        insert( new VarTree( "[<font color=#" + hex( Scheme.active().debugVarRedText() ) + ">" + i + "</font>]", elem.type().name(), elem, false ) );
      }
    }
  }

  public boolean isTerminal()
  {
    return !isRoot() && (_value == null || !(_value instanceof ObjectReference));
  }

  public boolean isRoot()
  {
    return _parent == null;
  }

  public List<VarTree> getChildren()
  {
    expand();
    return _children;
  }

  public void insert( MutableTreeNode child )
  {
    insert( child, getChildCount() );
  }
  public void insert( MutableTreeNode child, int index )
  {
    if( _children.isEmpty() )
    {
      _children = new ArrayList<>();
    }
    _children.add( index, (VarTree)child );
    child.setParent( this );
  }

  public void addViaModel( MutableTreeNode child )
  {
    ((DefaultTreeModel)getTree().getModel()).insertNodeInto( child, this, getChildCount() );
  }

  public void insertViaModel( MutableTreeNode child, int index )
  {
    ((DefaultTreeModel)getTree().getModel()).insertNodeInto( child, this, index );
  }

  @Override
  public void remove( int index )
  {
    remove( getChildren().get( index ) );
  }

  @Override
  public void remove( MutableTreeNode node )
  {
    //noinspection SuspiciousMethodCalls
    getChildren().remove( node );
  }

  @Override
  public void setUserObject( Object object )
  {

  }

  @Override
  public void removeFromParent()
  {
    _parent.remove( this );
  }

  @Override
  public void setParent( MutableTreeNode newParent )
  {
    _parent = (VarTree)newParent;
  }

  @Override
  public TreeNode getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public VarTree getParent()
  {
    return _parent;
  }

  @Override
  public int getIndex( TreeNode node )
  {
    //noinspection SuspiciousMethodCalls
    return getChildren().indexOf( node );
  }

  @Override
  public boolean getAllowsChildren()
  {
    return !isTerminal();
  }

  @Override
  public boolean isLeaf()
  {
    return isTerminal();
  }

  @Override
  public Enumeration children()
  {
    Iterator iter = getChildren().iterator();
    return new Enumeration()
    {
      @Override
      public boolean hasMoreElements()
      {
        return iter.hasNext();
      }

      @Override
      public Object nextElement()
      {
        return iter.next();
      }
    };
  }

  public String toString()
  {
    return "Name: " + _name + "\n Type: " + _type + "\n Value: " + _value;
  }

  public void select()
  {
    JTree tree = getMessagesPanel().getTree();
    TreePath path = getPath();
    tree.expandPath( path );
    tree.setSelectionPath( path );
    tree.scrollPathToVisible( path );
  }

  public TreePath getPath()
  {
    List<VarTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new VarTree[path.size()] ) );
  }

  private List<VarTree> makePath( List<VarTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  private MessagesPanel getMessagesPanel()
  {
    return LabFrame.instance().getGosuPanel().getMessagesPanel();
  }

  public Icon getIcon()
  {
    return findIcon();
  }

  private Icon findIcon()
  {
    return _value instanceof ArrayReference
            ? EditorUtilities.loadIcon( "images/array.png" )
            : _value == null || _value instanceof ObjectReference
              ? EditorUtilities.loadIcon( "images/object.png" )
              : EditorUtilities.loadIcon( "images/primitive.png" );
  }

  public JTree getTree()
  {
    return LabFrame.instance().getGosuPanel().getMessagesPanel().getTree();
  }

  public static boolean hasSuperClass( ReferenceType referenceType, String typeName )
  {
    if( referenceType == null )
    {
      return false;
    }

    if( referenceType.name().equals( typeName ) )
    {
      return true;
    }

    if( !(referenceType instanceof ClassType) )
    {
      return false;
    }

    return hasSuperClass( ((ClassType)referenceType).superclass(), typeName );
  }
}
