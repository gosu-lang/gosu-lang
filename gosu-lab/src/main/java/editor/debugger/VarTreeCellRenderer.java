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


import static editor.util.EditorUtilities.hex;


import java.util.Collections;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
*/
class VarTreeCellRenderer extends AbstractTreeCellRenderer<VarTree>
{
  public VarTreeCellRenderer( JTree tree )
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
      strValue = "["+ ((ArrayReference)value).length() + "] " + makeIdValue( value );
    }
    else
    {
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
    }
    setText( "<html><font color=#" + hex( Scheme.active().debugVarRedText() ) + ">" + node.getName() + "</font> " + address + " = " + strValue );
    setIcon( node.getIcon() );
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
        throw new RuntimeException( e );
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
    return displayValue.toString();
  }

  private String makeIdValue( Value value )
  {
    return "{" + value.type().name() + "@" + ((ObjectReference)value).uniqueID() + "}";
  }
}
