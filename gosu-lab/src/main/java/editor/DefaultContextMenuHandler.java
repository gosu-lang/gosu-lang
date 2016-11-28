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
    Supplier<EditorHost> contextEditor = () -> (EditorHost)editor;
    Supplier<GosuEditor> contextGosuEditor = () -> editor instanceof GosuEditor ? (GosuEditor)editor : null;
    menu.add( CommonMenus.makeCut( contextEditor ) );
    menu.add( CommonMenus.makeCopy( contextEditor ) );
    menu.add( CommonMenus.makePaste( contextEditor ) );
    menu.add( CommonMenus.makePasteJavaAsGosu( contextGosuEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeFindUsages( FileTreeUtil::getRoot ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeCodeComplete( contextGosuEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeParameterInfo( contextGosuEditor ) );
    menu.add( CommonMenus.makeExpressionType( contextGosuEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeGotoDeclaration( contextGosuEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeShowFileInTree( contextEditor ) );
    menu.add( new JSeparator() );
    menu.add( CommonMenus.makeQuickDocumentation( contextGosuEditor ) );
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
    return LabFrame.instance().getGosuPanel().getExperiment();
  }

  public void displayContextMenu( IScriptEditor editor, int iXPos, int iYPos, Component eventSource )
  {
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
