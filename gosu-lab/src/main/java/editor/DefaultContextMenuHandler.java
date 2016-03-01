package editor;

import gw.lang.reflect.gs.IGosuProgram;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.function.Supplier;

/**
 */
public class DefaultContextMenuHandler implements IContextMenuHandler<IScriptEditor>
{

  public JPopupMenu getContextMenu( IScriptEditor editor )
  {
    JPopupMenu menu = new JPopupMenu();
    Supplier<GosuEditor> contextEditor = () -> (GosuEditor)editor;
    menu.add( CommonMenus.makeCut( contextEditor ) );
    menu.add( CommonMenus.makeCopy( contextEditor ) );
    menu.add( CommonMenus.makePaste( contextEditor ) );
    menu.add( CommonMenus.makePasteJavaAsGosu( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeCodeComplete( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeParameterInfo( contextEditor ) );
    menu.add( CommonMenus.makeExpressionType( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeGotoDeclaration( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeQuickDocumentation( contextEditor ) );
    if( editor.getScriptPart() != null &&
        editor.getScriptPart().getContainingType() != null )
    {
      menu.add( new JSeparator() );
      menu.add( CommonMenus.makeViewBytecode() );
    }
    if( editor.getScriptPart() != null &&
        editor.getScriptPart().getContainingType() instanceof IGosuProgram )
    {
      menu.add( new JSeparator() );
      menu.add( CommonMenus.makeRun( () -> editor.getScriptPart().getContainingType() ) );
    }
    return menu;
  }

  public void displayContextMenu( IScriptEditor editor, int iXPos, int iYPos, Component eventSource )
  {
    editor.getEditor().requestFocus();
    try
    {
      Rectangle rcCaretBounds = editor.getEditor().modelToView( editor.getEditor().getCaretPosition() );
      getContextMenu( editor ).show( editor.getEditor(), rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }
}
