package editor.debugger;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.Value;
import editor.AbstractTreeCellRenderer;
import editor.Scheme;
import editor.VarTree;
import static editor.util.EditorUtilities.hex;

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
    if( value instanceof PrimitiveValue )
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
    else if( value == null )
    {
      address = "";
      strValue = "<font color=#" + hex( Scheme.active().debugVarBlueText() ) + "><b>null</b></font>";
    }
    else
    {
      String idValue = makeIdValue( value );
      address = null;
      strValue = value.toString();
      if( strValue.startsWith( "instance of" ) )
      {
        strValue = idValue;
        address = "";
      }

      if( address == null )
      {
        address = "<font color=#C0C0C0>" + idValue + "</font>";
      }

      if( valueType.equals( String.class.getName() ) )
      {
        strValue = "<font color=#" + hex( Scheme.active().debugVarGreenText() ) + "><b>" + strValue + "</b></font>";
      }
    }
    setText( "<html><font color=#" + hex( Scheme.active().debugVarRedText() ) + ">" + node.getName() + "</font> " + address + " = " + strValue );
    setIcon( node.getIcon() );
  }

  private String makeIdValue( Value value )
  {
    return "{" + value.type().name() + "@" + ((ObjectReference)value).uniqueID() + "}";
  }
}
