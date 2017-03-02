package editor;

import editor.run.IRunConfig;
import editor.util.EditorUtilities;
import editor.util.Experiment;

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
    menu.add( CommonMenus.makeFindUsages( FileTreeUtil::getRoot ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeCodeComplete( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeParameterInfo( contextEditor ) );
    menu.add( CommonMenus.makeExpressionType( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeGotoDeclaration( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeShowFileInTree( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeQuickDocumentation( contextEditor ) );
    if( editor.getScriptPart() != null &&
        editor.getScriptPart().getContainingType() != null )
    {
      menu.add( new JSeparator() );
      menu.add( CommonMenus.makeViewBytecode() );
    }
    if( editor.getScriptPart() != null && EditorUtilities.isRunnable( editor.getScriptPart().getContainingType() ) )
    {
      menu.add( new JSeparator() );
      menu.add( CommonMenus.makeRun( () -> getOrCreateRunConfig( editor ) ) );
      menu.add( CommonMenus.makeDebug( () -> getOrCreateRunConfig( editor ) ) );
    }
    return menu;
  }

  private IRunConfig getOrCreateRunConfig( IScriptEditor editor )
  {
    return getExperiment().getOrCreateRunConfig( editor.getScriptPart().getContainingType() );
  }

  private Experiment getExperiment()
  {
    return RunMe.getEditorFrame().getGosuPanel().getExperiment();
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
