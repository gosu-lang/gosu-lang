package editor.debugger;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import editor.AbstractTreeCellRenderer;
import editor.LabFrame;
import editor.Scheme;
import editor.util.TaskQueue;


import static editor.util.EditorUtilities.hex;


import java.awt.*;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
*/
class VarTreeCellRenderer extends AbstractTreeCellRenderer<VarTree>
{
  private static final String FETCHING_VALUE = "<Fetching value...>";

  VarTreeCellRenderer( JTree tree )
  {
    super( tree );
  }

  public void configure()
  {
    VarTree node = getNode();
    if( node == null )
    {
      return;
    }

    setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

    String displayValue = node.getDisplayValue();
    if( displayValue == null )
    {
      displayValue = makeDisplayValue( node );
      node.setDisplayValue( displayValue );
    }

    setText( displayValue );
    setIcon( node.getIcon() );
  }

  private String makeDisplayValue( VarTree node )
  {
    String localDisplayValue = makeLocalDisplayValue( node );
    if( localDisplayValue != null )
    {
      return localDisplayValue;
    }

    return makeRemoteDisplayValue( node );
  }

  private String makeLocalDisplayValue( VarTree node )
  {
    Value value = node.getValue();
    String strValue;
    String address;
    String valueType = value == null ? "" : value.type().name();

    if( value == null )
    {
      address = "";
      strValue = "<font color=#" + hex( Scheme.active().debugVarBlueText() ) + "><b>null</b></font>";
    }
    else if( value instanceof PrimitiveValue )
    {
      address = "";
      strValue = value.toString();
      if( valueType.equals( char.class.getName() ) )
      {
        strValue = "<font face=monospaced color=#" + hex( Scheme.active().debugVarGreenText() ) + ">'" + strValue + "'</font> <font color=#" + hex( Scheme.active().getWindowText() ) + ">" + (int)strValue.charAt( 0 ) + "</font>";
      }
    }
    else if( value instanceof ArrayReference )
    {
      address = "";
      strValue = "[" + ((ArrayReference)value).length() + "] " + makeIdValue( value );
    }
    else
    {
      return null;
    }
    return makeDisplayValue( node, strValue, address );
  }

  private String makeRemoteDisplayValue( VarTree node )
  {
    // Must invoke remote JDI method asynchronously to avoid deadlock with event thread
    TaskQueue.getInstance( "jdiInvoker" ).postTask( () -> {
      Value value = node.getValue();
      String strValue;
      String address;
      String valueType = value == null ? "" : value.type().name();

      String idValue = makeIdValue( value );
      address = null;
      if( VarTree.getValueKind( value ).isSpecial() )
      {
        strValue = "<font color=#C0C0C0>" + idValue + "</font>";
        address = "";
      }
      else
      {
        strValue = invokeToString( (ObjectReference)value );
        if( strValue.startsWith( "\"" + value.type().name() + "@" ) )
        {
          // This is the default Object#toString() impl, which is the same as idValue, no need to repeat it
          strValue = idValue;
          address = "";
        }
      }

      if( address == null )
      {
        address = "<font color=#C0C0C0>" + idValue + "</font>";
      }

      if( valueType.equals( String.class.getName() ) )
      {
        strValue = "<font color=#" + hex( Scheme.active().debugVarGreenText() ) + "><b>" + strValue + "</b></font>";
      }
      strValue = handleSpecialValue( (ObjectReference)value, strValue );
      String displayValue = makeDisplayValue( node, strValue, address );
      node.setDisplayValue( displayValue );
      EventQueue.invokeLater( () -> node.getTree().getModel().valueForPathChanged( node.getPath(), node.getValue() ) );
    } );
    return node.getDisplayValue() == null ? FETCHING_VALUE : node.getDisplayValue();
  }

  private String makeDisplayValue( VarTree node, String strValue, String address )
  {
    return "<html><font color=#" + hex( Scheme.active().debugVarRedText() ) + ">" + node.getName() + "</font> "
           + address + " = " + strValue;
  }

  private String handleSpecialValue( ObjectReference value, String strValue )
  {
    ValueKind valueKind = VarTree.getValueKind( value );
    if( valueKind == ValueKind.Collection ||
        valueKind == ValueKind.Map )
    {
      Value size;
      Debugger debugger = LabFrame.instance().getGosuPanel().getDebugger();
      if( debugger == null )
      {
        return "";
      }
      ThreadReference thread = debugger.getSuspendedThread();
      ReferenceType refType = value.referenceType();
      try
      {
        size = value.invokeMethod( thread, refType.methodsByName( "size" ).get( 0 ), Collections.emptyList(), 0 );
      }
      catch( Exception e )
      {
        throw new RuntimeException( "THREAD: " + thread.name(), e );
      }
      strValue += " size = "+ ((PrimitiveValue)size).intValue();
    }

    return strValue;
  }

  private String invokeToString( ObjectReference value )
  {
    Value displayValue;
    Debugger debugger = LabFrame.instance().getGosuPanel().getDebugger();
    if( debugger == null )
    {
      return "";
    }
    ThreadReference thread = debugger.getSuspendedThread();
    if( thread == null )
    {
      return "";
    }

    ReferenceType refType = value.referenceType();
    try
    {
      Method toString = refType.methodsByName( "toString" ).stream().filter( e -> e.argumentTypeNames().size() == 0 ).collect( Collectors.toList() ).get( 0 );
      displayValue = value.invokeMethod( thread, toString, Collections.emptyList(), 0 );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return displayValue == null ? "null" : displayValue.toString();
  }

  private String makeIdValue( Value value )
  {
    return "{" + value.type().name() + "@" + ((ObjectReference)value).uniqueID() + "}";
  }
}
