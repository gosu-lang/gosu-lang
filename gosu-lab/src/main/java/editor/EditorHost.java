package editor;

import editor.search.MessageDisplay;
import editor.search.SearchLocation;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import editor.util.TextComponentUtil;
import editor.util.transform.java.JavaToGosu;
import gw.lang.parser.IScriptPartId;
import gw.lang.reflect.IType;
import gw.util.GosuStringUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;


import static editor.util.EditorUtilities.handleUncaughtException;

/**
 */
public abstract class EditorHost extends JPanel implements IEditorHost
{
  /**
   * The number of spacess assigned to a tab
   */
  public static final int TAB_SIZE = 2;

  private JPopupMenu _completionPopup;
  private AtomicUndoManager _undoMgr;
  private UndoableEditListener _uel;
  private IScriptPartId _partId;
  private HighlightMode _highlightMode = HighlightMode.SEARCH;


  protected enum HighlightMode
  {
    SEARCH,
    USAGES
  }

  EditorHost( AtomicUndoManager atomicUndoManager )
  {
    _undoMgr = atomicUndoManager;
  }

  @Override
  public IScriptPartId getScriptPart()
  {
    return _partId;
  }
  @Override
  public void setScriptPart( IScriptPartId partId )
  {
    _partId = partId;
  }

  @Override
  public AtomicUndoManager getUndoManager()
  {
    return _undoMgr;
  }

  @Override
  public void read( IScriptPartId partId, String strSource ) throws IOException
  {
    setScriptPart( partId );
    setLabel( "" );

    JTextComponent editor = getEditor();
    AbstractDocument doc = (AbstractDocument)editor.getDocument();
    if( doc != null )
    {
      doc.removeDocumentListener( getDocHandler() );
    }

    // Replace the Windows style new-line sequence with just a new-line.
    // Otherwise, the editor thinks new lines are one character
    strSource = GosuStringUtil.replace( strSource, "\r\n", "\n" );

    editor.read( new StringReader( strSource ), "" );

    addDocumentListener();

    parse();
  }

  protected void addKeyHandlers()
  {
    JTextComponent editor = getEditor();

    editor.getInputMap().put( KeyStroke.getKeyStroke( "TAB" ), "_bulkIndent" );
    editor.getActionMap().put( "_bulkIndent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isCompletionPopupShowing() )
                                    {
                                      handleBulkIndent( false );
                                    }
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( "shift TAB" ), "_bulkOutdent" );
    editor.getActionMap().put( "_bulkOutdent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isCompletionPopupShowing() )
                                    {
                                      handleBulkIndent( true );
                                    }
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "_removeHighlights" );
    editor.getActionMap().put( "_removeHighlights",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    setHighlightMode( HighlightMode.SEARCH );
                                    removeAllHighlights();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( "F3" ), "_gotoNextHighlight" );
    editor.getActionMap().put( "_gotoNextHighlight",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( getHighlightMode() == HighlightMode.USAGES )
                                    {
                                      gotoNextUsageHighlight();
                                    }
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( "shift F3" ), "_gotoPrevHighlight" );
    editor.getActionMap().put( "_gotoPrevHighlight",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( getHighlightMode() == HighlightMode.USAGES )
                                    {
                                      gotoPrevUsageHighlight();
                                    }
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, EditorUtilities.CONTROL_KEY_MASK ), "_deleteWord" );
    editor.getActionMap().put( "_deleteWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteWord();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, EditorUtilities.CONTROL_KEY_MASK ), "_deleteWordForward" );
    editor.getActionMap().put( "_deleteWordForward",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteWordForwards();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_Y, EditorUtilities.CONTROL_KEY_MASK ), "_deleteLine" );
    editor.getActionMap().put( "_deleteLine",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteLine();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift J" ), "_joinLines" );
    editor.getActionMap().put( "_joinLines",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    joinLines();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " G" ), "_gotoLine" );
    editor.getActionMap().put( "_gotoLine",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayGotoLinePopup();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK ), "_unindent" );
    editor.getActionMap().put( "_unindent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    unindent();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " Z" ), "_undo" );
    editor.getActionMap().put( "_undo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    AtomicUndoManager undoManager = getUndoManager();
                                    if( undoManager.canUndo() )
                                    {
                                      undoManager.undo();
                                    }
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( "alt F1" ), "_selectFileInTree" );
    editor.getActionMap().put( "_selectFileInTree",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    showFileInTree();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_L, EditorUtilities.CONTROL_KEY_MASK ), "_centerView" );
    editor.getActionMap().put( "_centerView",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    centerView();
                                  }
                                } );

    editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " D" ), "_duplicate" );
    editor.getActionMap().put( "_duplicate",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    duplicate();
                                  }
                                } );

    TextComponentUtil.fixTextComponentKeyMap( getEditor() );
  }

  public void showFileInTree()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    File file = gosuPanel.getCurrentFile();

    FileTree root = FileTreeUtil.getRoot();
    FileTree fileTree = root.find( file );
    if( fileTree != null )
    {
      fileTree.select();
    }
  }

  public int getLineNumberAtCaret()
  {
    return getEditor().getDocument().getDefaultRootElement().getElementIndex( getEditor().getCaretPosition() ) + 1;
  }

  public int getLineOffset( int iLine )
  {
    Element root = getDocument().getRootElements()[0];
    iLine = root.getElementCount() < iLine ? root.getElementCount() : iLine;
    return root.getElement( iLine ).getStartOffset();
  }

  void handleBulkIndent( boolean bOutdent )
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( bOutdent ? "Outdent" : "Indent" );
    try
    {
      _handleBulkIndent( bOutdent );
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void _handleBulkIndent( boolean bOutdent )
  {
    JTextComponent editor = getEditor();
    String strTabSpaces = getIndentWhitespace();
    int iSelectionStart = editor.getSelectionStart();
    int iSelectionEnd = editor.getSelectionEnd();
    if( iSelectionStart == iSelectionEnd )
    {
      editor.replaceSelection( strTabSpaces );
      return;
    }

    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    Element root = editor.getDocument().getRootElements()[0];
    int iStartIndex = root.getElementIndex( iSelectionStart );
    int iEndIndex = root.getElementIndex( iSelectionEnd );
    if( iStartIndex != iEndIndex && root.getElement( iEndIndex ).getStartOffset() == iSelectionEnd )
    {
      iSelectionEnd--;
      iEndIndex = root.getElementIndex( iSelectionEnd );
    }

    try
    {
      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = editor.getText( iStart, iEnd - iStart );
        if( strLine.trim().length() == 0 )
        {
          continue;
        }
        else if( strLine.length() > 0 && bOutdent )
        {
          if( strLine.startsWith( strTabSpaces ) )
          {
            strLine = strLine.substring( 2 );
          }
          else if( Character.isWhitespace( strLine.charAt( 0 ) ) )
          {
            strLine = strLine.substring( 1 );
          }
        }
        else
        {
          strLine = strTabSpaces + strLine;
        }
        iEnd = line.getEndOffset();
        editor.select( iStart, iEnd );
        editor.replaceSelection( strLine );
        iSelectionEnd = editor.getSelectionEnd();
      }
      editor.select( iSelectionStart, iSelectionEnd );
    }
    catch( Exception ex )
    {
      EditorUtilities.handleUncaughtException( ex );
    }
  }

  private String getIndentWhitespace()
  {
    return GosuStringUtil.repeat( " ", TAB_SIZE );
  }

  public void setLabel( String label )
  {
    //## nothing?
  }

  @Override
  public AbstractDocument getDocument()
  {
    return (AbstractDocument)getEditor().getDocument();
  }

  /**
   * Sets the one and only undoable edit listener for this editor section.
   * The primary use case for this method is to establish an undo manager
   * connection.
   *
   * @param uel The UndoableEditListener to connect to this section's document.
   */
  @Override
  public void setUndoableEditListener( UndoableEditListener uel )
  {
    if( _uel != null )
    {
      getEditor().getDocument().removeUndoableEditListener( _uel );
    }

    _uel = uel;

    if( _uel != null )
    {
      getEditor().getDocument().addUndoableEditListener( _uel );
    }
  }

  protected void addDocumentListener()
  {
    AbstractDocument doc;
    doc = (AbstractDocument)getEditor().getDocument();
    if( doc != null )
    {
      doc.addDocumentListener( getDocHandler() );
    }
  }

  @Override
  public String getText()
  {
    return getEditor().getText();
  }

  @Override
  public IType getParsedClass()
  {
    IScriptPartId scriptPart = getScriptPart();
    return scriptPart == null ? null : scriptPart.getContainingType();
  }

  void dismissCompletionPopup()
  {
    if( _completionPopup != null )
    {
      _completionPopup.setVisible( false );
      _completionPopup = null;
    }
  }

  public JPopupMenu getCompletionPopup()
  {
    return _completionPopup;
  }
  public void setCompletionPopup( JPopupMenu completionPopup )
  {
    _completionPopup = completionPopup;
  }
  public boolean isCompletionPopupShowing()
  {
    return _completionPopup != null && _completionPopup.isShowing();
  }

  public void displayGotoLinePopup()
  {
    dismissCompletionPopup();
    StringPopup popup = new StringPopup( "", "Line number:", getEditor() );
    popup.addNodeChangeListener(
      e -> {
        try
        {
          int iLine = Integer.parseInt( e.getSource().toString() );
          if( iLine > 0 )
          {
            gotoLine( iLine );
          }
        }
        catch( NumberFormatException nfe )
        {
          // ignore
        }
        getEditor().requestFocus();
        EditorUtilities.fixSwingFocusBugWhenPopupCloses( EditorHost.this );
        getEditor().repaint();
      } );
    popup.show( this, 0, 0 );
    editor.util.EditorUtilities.centerWindowInFrame( popup, editor.util.EditorUtilities.getWindow() );
  }

  public void gotoLine( int iLine )
  {
    gotoLine( iLine, 0 );
  }

  public void gotoLine( int iLine, int iColumn )
  {
    Element root = getEditor().getDocument().getRootElements()[0];
    iLine = root.getElementCount() < iLine ? root.getElementCount() : iLine;
    if( iLine < 1 )
    {
      MessageDisplay.displayError( "Invalide line number: " + iLine );
      return;
    }
    Element line = root.getElement( iLine - 1 );
    gotoOffset( line.getStartOffset() + iColumn );
  }

  public void gotoOffset( int offset )
  {
    int length = getEditor().getDocument().getLength();
    if( offset > length )
    {
      offset = length - 1;
    }
    if( offset < 0 )
    {
      offset = 0;
    }
    getEditor().setCaretPosition( offset );
  }

  public void duplicate()
  {
    DocumentFilter documentFilter = getDocument().getDocumentFilter();
    if( documentFilter == null || ((SimpleDocumentFilter)documentFilter).acceptEdit( "" ) )
    {
      CompoundEdit undoAtom = _undoMgr.getUndoAtom();
      if( undoAtom != null && undoAtom.getPresentationName().equals( "Text Change" ) )
      {
        _undoMgr.endUndoAtom();
      }
      undoAtom = getUndoManager().beginUndoAtom( "Duplicate Line" );
      try
      {
        _duplicate();
      }
      finally
      {
        getUndoManager().endUndoAtom( undoAtom );
      }
    }
  }

  private void _duplicate()
  {
    JTextComponent editor = getEditor();

    //No selection, duplicate line
    String selectedText = editor.getSelectedText();
    if( GosuStringUtil.isEmpty( selectedText ) )
    {
      String currentText = editor.getText();

      int initialCaretPosition = editor.getCaretPosition();
      int lineStart = TextComponentUtil.getLineStart( editor.getText(), initialCaretPosition );
      int lineEnd = TextComponentUtil.getLineEnd( editor.getText(), initialCaretPosition );

      try
      {
        recordCaretPositionForUndo();
        String insertedLine = "\n" + currentText.substring( lineStart, lineEnd );
        editor.getDocument().insertString( lineEnd, insertedLine, null );
        if( initialCaretPosition < lineEnd )
        {
          editor.setCaretPosition( editor.getCaretPosition() + insertedLine.length() );
          recordCaretPositionForUndo();
        }
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
    else
    {
      try
      {
        int initialSelectionEnd = editor.getSelectionEnd();
        editor.getDocument().insertString( initialSelectionEnd, selectedText, null );
        editor.getCaret().setDot( initialSelectionEnd );
        editor.getCaret().moveDot( initialSelectionEnd + selectedText.length() );
        recordCaretPositionForUndo();
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private void recordCaretPositionForUndo() throws BadLocationException
  {
    JTextComponent editor = getEditor();
    editor.getDocument().insertString( editor.getCaretPosition(), "8", null );
    editor.getDocument().remove( editor.getCaretPosition() - 1, 1 );
  }

  /**
   * delete the currently selected text, or the current line if nothing is selected
   */
  public void delete()
  {
    TextComponentUtil.expandSelectionIfNeeded( getEditor() );
    getEditor().replaceSelection( "" );
  }

  /**
   * @return the selected text in the editor, expanding to the entire current line if no selection exists
   */
  public String getExpandedSelection()
  {
    TextComponentUtil.expandSelectionIfNeeded( getEditor() );
    return getEditor().getSelectedText();
  }

  void deleteWord()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Word" );
    try
    {
      if( !GosuStringUtil.isEmpty( getEditor().getSelectedText() ) )
      {
        delete();
      }
      else
      {
        try
        {
          TextComponentUtil.deleteWordAtCaret( getEditor() );
        }
        catch( BadLocationException e )
        {
          //ignore
        }
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void deleteWordForwards()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Word" );
    try
    {
      if( !GosuStringUtil.isEmpty( getEditor().getSelectedText() ) )
      {
        delete();
      }
      else
      {
        int start = getEditor().getCaretPosition();
        jumpRight();
        int end = getEditor().getCaretPosition();
        getEditor().select( start, end );
        getEditor().replaceSelection( "" );
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void deleteLine()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Line" );
    try
    {
      recordCaretPositionForUndo();
      TextComponentUtil.selectLineAtCaret( getEditor() );
      getEditor().replaceSelection( "" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void unindent()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Unindent" );
    try
    {
      try
      {
        TextComponentUtil.unindentLineAtCaret( getEditor() );
      }
      catch( BadLocationException e )
      {
        //ignore
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  public void joinLines()
  {
    DocumentFilter documentFilter = getDocument().getDocumentFilter();
    if( documentFilter == null || ((SimpleDocumentFilter)documentFilter).acceptEdit( "" ) )
    {
      CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Join Lines" );
      try
      {
        _joinLines();
      }
      finally
      {
        getUndoManager().endUndoAtom( undoAtom );
      }
    }
  }

  private void _joinLines()
  {
    try
    {
      JTextComponent editor = getEditor();
      Document document = editor.getDocument();
      int start = editor.getSelectionStart();
      int end = editor.getSelectionEnd();
      if( start == end )
      {
        int i = editor.getCaret().getDot();
        while( i < document.getLength() )
        {
          if( document.getText( i, 1 ).equals( "\n" ) )
          {
            document.remove( i, 1 );
            swallowSpaces( document, i );
            break;
          }
          i++;
        }
      }
      else
      {
        while( start < end )
        {
          if( document.getText( start, 1 ).equals( "\n" ) )
          {
            document.remove( start, 1 );
            end--;
            int spacesRemoved = swallowSpaces( document, start );
            end -= spacesRemoved;
            start -= spacesRemoved;
          }
          else
          {
            start++;
          }
        }
      }
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private int swallowSpaces( Document document, int i ) throws BadLocationException
  {
    int removedChars = 0;
    while( i < document.getLength() )
    {
      if( document.getText( i, 1 ).equals( " " ) )
      {
        document.remove( i, 1 );
        removedChars++;
      }
      else
      {
        break;
      }
    }
    if( !document.getText( i, 1 ).equals( "." ) && !document.getText( i, 1 ).equals( "#" ) )
    {
      document.insertString( i, " ", null );
      removedChars--;
    }
    return removedChars;
  }

  void jumpRight()
  {
    TextComponentUtil.jumpRight( getEditor() );
  }

  public void centerView()
  {
    try
    {
      Point caretPos = getEditor().modelToView( getEditor().getCaretPosition() ).getLocation();
      Dimension size = getScroller().getBounds().getSize();
      getEditor().scrollRectToVisible( new Rectangle( caretPos.x, caretPos.y - size.height / 2, 0, size.height ) );
    }
    catch( BadLocationException e )
    {
      // can't center
    }
  }

  public void highlightLocations( List<SearchLocation> locations )
  {
    setHighlightMode( HighlightMode.USAGES );

    for( SearchLocation loc : locations )
    {
      try
      {
        getEditor().getHighlighter().addHighlight( loc._iOffset, loc._iOffset + loc._iLength, GosuEditor.LabHighlighter.TEXT );
      }
      catch( BadLocationException e )
      {
        //throw new RuntimeException( e );
      }
    }
  }

  public void gotoNextUsageHighlight()
  {
    if( isCompletionPopupShowing() )
    {
      return;
    }

    JTextComponent editor = getEditor();
    int caretPosition = editor.getCaretPosition();
    Highlighter.Highlight[] highlights = editor.getHighlighter().getHighlights();
    Arrays.sort( highlights, ( o1, o2 ) -> o1.getStartOffset() - o2.getStartOffset() );
    int i = -1;
    do
    {
      i++;
      while( (i < highlights.length) && (highlights[i].getStartOffset() <= caretPosition) )
      {
        i++;
      }
    } while( (i < highlights.length) && !(highlights[i].getPainter() instanceof LabHighlighter) );

    if( i == highlights.length )
    {
      i = 0;
    }
    editor.setCaretPosition( highlights[i].getEndOffset() );
    editor.moveCaretPosition( highlights[i].getStartOffset() );
  }

  public void gotoPrevUsageHighlight()
  {
    if( isCompletionPopupShowing() )
    {
      return;
    }

    JTextComponent editor = getEditor();
    int caretPosition = editor.getCaretPosition();
    Highlighter.Highlight[] highlights = editor.getHighlighter().getHighlights();
    Arrays.sort( highlights, ( o1, o2 ) -> o2.getStartOffset() - o1.getStartOffset() );
    int i = -1;
    do
    {
      i++;
      while( (i < highlights.length) && (highlights[i].getStartOffset() >= caretPosition) )
      {
        i++;
      }
    } while( (i < highlights.length) && !(highlights[i].getPainter() instanceof LabHighlighter) );

    if( i == highlights.length )
    {
      i = 0;
    }
    editor.setCaretPosition( highlights[i].getEndOffset() );
    editor.moveCaretPosition( highlights[i].getStartOffset() );
  }

  public void removeAllHighlights()
  {
    if( isCompletionPopupShowing() )
    {
      return;
    }

    JTextComponent editor = getEditor();
    removeHightlights();
    editor.setCaretPosition( editor.getCaretPosition() );
    hideMiscPopups();
    getFeedbackPanel().repaint();
  }

  private void removeHightlights()
  {
    Highlighter highlighter = getEditor().getHighlighter();

    for( Highlighter.Highlight highlight : highlighter.getHighlights() )
    {
      highlighter.removeHighlight( highlight );
    }
  }

  protected HighlightMode getHighlightMode()
  {
    return _highlightMode;
  }
  protected void setHighlightMode( HighlightMode mode )
  {
    _highlightMode = mode;
  }

  protected void hideMiscPopups()
  {
    editor.util.EditorUtilities.hideToolTip( getEditor() );
  }

  public void clipCut( Clipboard clipboard )
  {
    getUndoManager().beginUndoAtom( "Cut" );
    try
    {
      clipCopy( clipboard );
      delete();
    }
    finally
    {
      getUndoManager().endUndoAtom();
    }
  }

  public void clipCopy( Clipboard clipboard )
  {
    try
    {
      Transferable contents = getClipCopyContents();
      if( contents == null )
      {
        return;
      }

      clipboard.setContents( contents, null );
    }
    catch( Exception e )
    {
      handleUncaughtException( e );
    }
  }

  public void clipPaste( Clipboard clipboard, boolean asGosu )
  {
    Transferable t = clipboard.getContents( this );
    if( t == null )
    {
      return;
    }

    if( t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
    {
      try
      {
        String strContents = (String)t.getTransferData( DataFlavor.stringFlavor );
        if( asGosu )
        {
          strContents = JavaToGosu.convertString( strContents );
          if( "".equals( strContents ) )
          {
            JOptionPane.showMessageDialog( getEditor(), "The copied Java code has errors, only valid Java 8 code can be transformed", "Paste Java as Gosu", JOptionPane.ERROR_MESSAGE );
            return;
          }
        }
        getEditor().replaceSelection( strContents );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private Transferable getClipCopyContents()
  {
    Transferable contents = null;
    String strSelection = getExpandedSelection();
    if( strSelection != null && strSelection.length() > 0 )
    {
      contents = new StringSelection( strSelection );
    }
    return contents;
  }

  public static class LabHighlighter implements Highlighter.HighlightPainter
  {
    public static final LabHighlighter TEXT = new LabHighlighter( Scheme.active().usageReadHighlightColor() );
    public static LabHighlighter USAGE = new LabHighlighter( Scheme.active().usageReadHighlightColor() );

    private Highlighter.HighlightPainter _delegate;

    public LabHighlighter( Color color )
    {
      _delegate = new DefaultHighlighter.DefaultHighlightPainter( color );
    }

    @Override
    public void paint( Graphics g, int p0, int p1, Shape bounds, JTextComponent c )
    {
      _delegate.paint( g, p0, p1, bounds, c );
    }
  }

}
