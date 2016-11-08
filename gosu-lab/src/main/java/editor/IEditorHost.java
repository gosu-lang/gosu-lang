package editor;

import editor.search.SearchLocation;
import editor.undo.AtomicUndoManager;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.IType;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

/**
 */
public interface IEditorHost
{
  JTextComponent getEditor();
  void read( IScriptPartId partId, String strSource ) throws IOException;
  void refresh( String content );
  void parse();
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

  void gotoNextUsageHighlight();
  void gotoPrevUsageHighlight();
  void removeAllHighlights();

  void clipCut( Clipboard clipboard );
  void clipCopy( Clipboard clipboard );
  void clipPaste( Clipboard clipboard, boolean asGosu );
}