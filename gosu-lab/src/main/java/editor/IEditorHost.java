package editor;

import editor.search.SearchLocation;
import editor.undo.AtomicUndoManager;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.IType;
import gw.lang.IIssueContainer;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;

/**
 */
public interface IEditorHost
{
  EditorHostTextPane getEditor();
  void read( IScriptPartId partId, String strSource ) throws IOException;
  void refresh( String content );
  void parse( String strText, boolean forceCodeCompletion, boolean changed );
  AtomicUndoManager getUndoManager();
  DocumentListener getDocHandler();
  void setUndoableEditListener( UndoableEditListener uel );
  IType getParsedClass();
  IScriptPartId getScriptPart();
  void setScriptPart( IScriptPartId partId );
  String getText();
  void setLabel( String label );
  AbstractDocument getDocument();
  EditorScrollPane getScroller();
  JComponent getFeedbackPanel();
  void highlightLocations( List<SearchLocation> locations );
  String getLineCommentDelimiter();
  int getOffsetOfDeepestStatementLocationAtPos( int caretPosition, boolean strict );
  String getTooltipMessage( MouseEvent event );

  IIssueContainer getIssues();

  boolean canAddBreakpoint( int line );

  void gotoDeclaration();
  void gotoNextUsageHighlight();
  void gotoPrevUsageHighlight();
  void removeAllHighlights();

  void clipCut( Clipboard clipboard );
  void clipCopy( Clipboard clipboard );
  void clipPaste( Clipboard clipboard, boolean asGosu );
}