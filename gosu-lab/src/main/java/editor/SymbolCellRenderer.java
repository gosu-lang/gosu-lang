package editor;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;

import javax.swing.*;
import java.awt.*;


/**
 */
public class SymbolCellRenderer extends DefaultListCellRenderer
{

  public Component getListCellRendererComponent( JList list,
                                                 Object value,
                                                 int modelIndex,
                                                 boolean isSelected,
                                                 boolean cellHasFocus )
  {
    ISymbol symbol = (ISymbol)value;
    Icon icon;
    if( symbol.getType() instanceof IFunctionType )
    {
      icon = editor.util.EditorUtilities.loadIcon( "images/Function.png" );
    }
    else
    {
      icon = editor.util.EditorUtilities.loadIcon( "images/Properties.png" );
    }

    String text = getDisplayText( symbol );
    Component renderer = super.getListCellRendererComponent( list, text, modelIndex, isSelected, cellHasFocus );
    ((JLabel)renderer).setIcon( icon );

    return renderer;
  }

  private String getDisplayText( ISymbol symbol )
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

  private String getReturnTypeText( IType type )
  {
    return getTypeDisplayText( type );
  }

  private String getParamSignatureText( ISymbol symbol )
  {
    if( symbol instanceof IDynamicFunctionSymbol )
    {
      return getDynParamSignatureText( (IDynamicFunctionSymbol)symbol );
    }

    return getStaticParamSignatureText( (IFunctionType)symbol.getType() );
  }

  private String getDynParamSignatureText( IDynamicFunctionSymbol symbol )
  {
    java.util.List<ISymbol> args = symbol.getArgs();
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

  private String getStaticParamSignatureText( IFunctionType type )
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

  private String getTypeDisplayText( IType type )
  {
    String strType;
    if( type.isArray() )
    {
      strType = "List: " + getTypeDisplayText( type.getComponentType() );
    }
    else
    {
      strType = type.getRelativeName();
    }

    return strType;
  }

}
