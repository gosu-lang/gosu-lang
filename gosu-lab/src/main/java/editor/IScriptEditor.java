package editor;

import editor.undo.AtomicUndoManager;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbolTable;

import javax.swing.*;
import java.io.IOException;

/**
 */
public interface IScriptEditor extends IEditorHost
{
  public JComponent getComponent();

  public SourceType getSourceType();

  public String getText();

  public void read( IScriptPartId ctx, String strSource ) throws IOException;

  public void parse();

  public ISymbolTable getSymbolTable();

  public IScriptPartId getScriptPart();

  public void handleCompleteCode();

  public void gotoNextError();

  public boolean displayValueCompletionAtCurrentLocation();

  public IParseTree getDeepestLocationAtCaret();

  public IParseTree getDeepestStatementLocationAtCaret();

  public IParseTree getStatementAtLineAtCaret();

  public IParseTree getStatementAtLine( int iLine );

  public void gotoDeclarationAtCursor();

  public String getSelectedText();

  IParseTree getStatementAtLineOrExpression( int iLine );

  IParseTree getStatementAtLineAtCaretOrExpression();

  AtomicUndoManager getUndoManager();

  EditorScrollPane getScroller();
}

