package editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 */
public class DefaultContextMenuHandler implements IContextMenuHandler<IScriptEditor>
{

  public JPopupMenu getContextMenu( IScriptEditor editor )
  {
    JPopupMenu menu = new JPopupMenu();
    menu.add( CommonMenus.makeCut( () -> (GosuEditor)editor ) );
    menu.add( CommonMenus.makeCopy( () -> (GosuEditor)editor ) );
    menu.add( CommonMenus.makePaste( () -> (GosuEditor)editor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeCodeComplete( () -> (GosuEditor)editor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeParameterInfo( () -> (GosuEditor)editor ) );
    menu.add( CommonMenus.makeExpressionType( () -> (GosuEditor)editor ) );
    menu.add( CommonMenus.makeGotoDeclaration( () -> (GosuEditor)editor ) );

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
