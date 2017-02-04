package editor;

import editor.search.SearchLocation;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import gw.lang.GosuShop;
import gw.lang.parser.IParserPart;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import java.nio.file.Path;
import editor.util.TaskQueue;
import editor.util.TextComponentUtil;
import editor.util.transform.java.JavaToGosu;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuStringUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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

public abstract class EditorHost extends JPanel implements IEditorHost
{
  /**
   * The number of spacess assigned to a tab
   */
  public static final int TAB_SIZE = 2;
  public static final String INTELLISENSE_TASK_QUEUE = "_intellisenseParser";

  /**
   * Delay in millis for code completion to wait for key presses
   * before displaying.
   */
  static int COMPLETION_DELAY = 500;


  private JPopupMenu _completionPopup;
  private AtomicUndoManager _undoMgr;
  private UndoableEditListener _uel;
  private IScriptPartId _partId;
  private boolean _bParserSuspended;
  private boolean _bEnterPressedConsumed;
  private boolean _bAltDown;
  private boolean _bCompleteCode;
  private int _iTimerCount;
  private static TimerPool _timerPool = new TimerPool();
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

  public void parseAndWaitForParser()
  {
    parse();
    waitForParser();
  }

  public void waitForParser()
  {
    TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE ).postTaskAndWait(
      () -> {
        //do nothing
      } );
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

    editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " SLASH" ), "_bulkComment" );
    editor.getActionMap().put( "_bulkComment",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isCompletionPopupShowing() )
                                    {
                                      handleBulkComment();
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
    Path file = gosuPanel.getCurrentFile();

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
    Element elemAtLine = root.getElement( iLine );
    return elemAtLine == null ? 0 : elemAtLine.getStartOffset();
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
      JOptionPane.showMessageDialog( LabFrame.instance(), "Invalide line number: " + iLine, "Gosu Lab", JOptionPane.ERROR_MESSAGE );
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
        getEditor().getHighlighter().addHighlight( loc._iOffset, loc._iOffset + loc._iLength, EditorHost.LabHighlighter.TEXT );
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

  void handleEnter()
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "New Line" );
    try
    {
      _handleEnter();
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
      undoAtom = getUndoManager().getUndoAtom();
      if( undoAtom != null && undoAtom.getPresentationName().equals( "Text Change" ) )
      {
        getUndoManager().endUndoAtom();
      }
    }
  }

  void _handleEnter()
  {
    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    Element root = getEditor().getDocument().getRootElements()[0];
    int index = root.getElementIndex( getEditor().getCaretPosition() - 1 );
    Element line = root.getElement( index );
    int iStart = line.getStartOffset();
    int iEnd = line.getEndOffset();
    try
    {
      String strLine = line.getDocument().getText( iStart, iEnd - iStart );

      if( strLine.trim().endsWith( "{" ) )
      {
        if( handleOpenBrace( strLine ) )
        {
          return;
        }
      }

      StringBuilder strbIndent = new StringBuilder();
      for( int iIndent = 0; iIndent < strLine.length(); iIndent++ )
      {
        char c = strLine.charAt( iIndent );
        if( c != ' ' && c != '\t' )
        {
          break;
        }
        strbIndent.append( c );
      }
      if( strbIndent.length() > 0 )
      {
        getEditor().replaceSelection( strbIndent.toString() );
      }
      indentIfOpenBracePrecedes( strLine );

      fixCloseBraceIfNecessary( strLine );

      if( strLine.trim().startsWith( "/**" ) )
      {
        getEditor().replaceSelection( " * " );
        int caretPos = getEditor().getCaretPosition();
        getEditor().replaceSelection( "\n" + strbIndent.toString() + " */" );
        getEditor().setCaretPosition( caretPos );
      }
      else
      {
        boolean isJavadoc = false;
        while( strLine.trim().startsWith( "*" ) && !strLine.contains( "*/" ) )
        {
          index--;
          if( index >= 0 )
          {
            line = root.getElement( index );
            iStart = line.getStartOffset();
            iEnd = line.getEndOffset();
            strLine = line.getDocument().getText( iStart, iEnd - iStart );
            if( strLine.trim().startsWith( "/**" ) )
            {
              isJavadoc = true;
              break;
            }
          }
        }
        if( isJavadoc )
        {
          getEditor().replaceSelection( "* " );
        }
      }
    }
    catch( Exception ex )
    {
      EditorUtilities.handleUncaughtException( ex );
    }
  }

  private boolean handleOpenBrace( String strLine )
  {
    //SettleModalEventQueue.instance().run();

    int caretPos = getEditor().getCaretPosition() -1;
    String text = getEditor().getText();
    while( text.charAt( caretPos ) != '{' )
    {
      caretPos--;
    }

    ISourceCodeTokenizer tokenizer = GosuShop.createSourceCodeTokenizer( text );
    while( tokenizer.getCurrentToken().getTokenStart() < caretPos )
    {
      tokenizer.nextToken();
    }
    IToken startToken = tokenizer.getCurrentToken();
    if( startToken == null )
    {
      return false;
    }

    tokenizer.nextToken();
    IToken endToken = IParserPart.eatBlock( '{', '}', false, tokenizer );

    String trimLine = strLine.trim();
    int lineColumn = strLine.indexOf( trimLine ) + 1;

    if( endToken == null || endToken.getTokenColumn() != lineColumn )
    {
      final IToken tard = startToken;
      EventQueue.invokeLater( () -> {
        try
        {
          String fromOpenBrace = getEditor().getDocument().getText( tard.getTokenStart(), getEditor().getDocument().getLength() - tard.getTokenStart() );
          int newLineOffset = fromOpenBrace.indexOf( "\n" ) + 1;

          String spaces = String.join( "", Collections.nCopies( lineColumn - 1, " " ) );
          String emptyLine = spaces + "  ";
          String closeBraceLine = "\n" + spaces + "}";
          getEditor().getDocument().insertString( tard.getTokenStart() + newLineOffset, emptyLine + closeBraceLine, null );
          EventQueue.invokeLater( () -> getEditor().setCaretPosition( tard.getTokenStart() + newLineOffset + emptyLine.length() ) );
        }
        catch( BadLocationException e )
        {
          throw new RuntimeException( e );
        }
      } );
      return true;
    }
    return false;
  }

  private void fixCloseBraceIfNecessary( String previousLine ) throws BadLocationException
  {
    Element root = getEditor().getDocument().getRootElements()[0];
    int iStart = getEditor().getCaretPosition();
    Element line = root.getElement( root.getElementIndex( iStart ) );
    int iEnd = line.getEndOffset();
    if( iStart < getEditor().getDocument().getLength() )
    {
      String strLine = line.getDocument().getText( iStart, iEnd - iStart );
      if( strLine.trim().startsWith( "}" ) )
      {
        int offset = strLine.indexOf( '}' );
        boolean previousLineWasOpenBrace = previousLine.trim().endsWith( "{" );

        if( previousLineWasOpenBrace )
        {
          getEditor().getDocument().insertString( iStart, "\n", null );
          offset += 1;
        }

        parseAndWaitForParser();
        getEditor().setCaretPosition( iStart + offset );
        _handleBraceRightNow( getEditor().getCaretPosition(), false );

        if( previousLineWasOpenBrace )
        {
          getEditor().setCaretPosition( iStart );
        }
      }
    }
  }

  void handleBackspace()
  {
    int caretPosition = getEditor().getCaretPosition();
    try
    {
      if( caretPosition > 0 && (getEditor().getText( caretPosition - 1, 1 ).equals( "." ) || getEditor().getText( caretPosition - 1, 1 ).equals( "#" )) )
      {
        dismissCompletionPopup();
      }
    }
    catch( BadLocationException e1 )
    {
      // ignore
    }
  }

  private void indentIfOpenBracePrecedes( String strLine )
  {
    strLine = strLine.trim();
    if( strLine.length() > 0 && strLine.charAt( strLine.length() - 1 ) == '{' )
    {
      getEditor().replaceSelection( getIndentWhitespace() );
    }
  }

  private String getIndentWhitespace()
  {
    return GosuStringUtil.repeat( " ", TAB_SIZE );
  }

  void handleBulkComment()
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Comment" );
    try
    {
      _handleBulkComment();
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
    }
  }

  void _handleBulkComment()
  {
    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    int iSelectionStart = getEditor().getSelectionStart();
    int iSelectionEnd = getEditor().getSelectionEnd();
    Element root = getEditor().getDocument().getRootElements()[0];
    int iStartIndex = root.getElementIndex( iSelectionStart );
    int iEndIndex = root.getElementIndex( iSelectionEnd );
    if( iStartIndex != iEndIndex && root.getElement( iEndIndex ).getStartOffset() == iSelectionEnd )
    {
      iSelectionEnd--;
      iEndIndex = root.getElementIndex( iSelectionEnd );
    }

    try
    {
      boolean bHasLineWithoutLeadingComment = false;
      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = getEditor().getText( iStart, iEnd - iStart );
        String strLineTrimmed = strLine.trim();
        if( strLineTrimmed.length() > 0 && !strLineTrimmed.startsWith( "//" ) )
        {
          bHasLineWithoutLeadingComment = true;
          break;
        }
      }

      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = getEditor().getText( iStart, iEnd - iStart );
        if( bHasLineWithoutLeadingComment )
        {
          strLine = getLineCommentDelimiter() + strLine;
        }
        else
        {
          int iCommentIndex = strLine.indexOf( "//" );
          if( iCommentIndex >= 0 )
          {
            strLine = strLine.substring( 0, iCommentIndex ) + strLine.substring( iCommentIndex + 2 );
          }
        }
        iEnd = line.getEndOffset();
        getEditor().select( iStart, iEnd );
        getEditor().replaceSelection( strLine );
        iSelectionEnd = getEditor().getSelectionEnd();
      }
      getEditor().select( iSelectionStart, iSelectionEnd );
    }
    catch( Exception ex )
    {
      editor.util.EditorUtilities.handleUncaughtException( ex );
    }
  }

  void handleBraceRight()
  {
    final int caretPosition = getEditor().getCaretPosition();
    EventQueue.invokeLater(
      () -> postTaskInParserThread(
        () -> EventQueue.invokeLater(
          () -> handleBraceRightNow( caretPosition ) ) ) );
  }

  private void handleBraceRightNow( int caretPosition )
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Right Brace" );
    try
    {
      _handleBraceRightNow( caretPosition, true );
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
    }
  }

  private void _handleBraceRightNow( int caretPosition, boolean wasBraceTyped )
  {
    Document doc = getEditor().getDocument();
    Element root = doc.getRootElements()[0];
    Element line = root.getElement( root.getElementIndex( caretPosition ) );
    int iBraceLineStart = line.getStartOffset();
    int iBraceLineEnd = line.getEndOffset();
    try
    {
      String strLine = line.getDocument().getText( iBraceLineStart, Math.min( iBraceLineEnd, doc.getLength() ) - iBraceLineStart );
      iBraceLineEnd = strLine.endsWith( "\n" ) ? iBraceLineEnd - 1 : iBraceLineEnd;
      strLine = strLine.trim();
      if( strLine.length() > 1 )
      {
        return;
      }
      int iOffset = getOffsetOfDeepestStatementLocationAtPos( caretPosition, true );
      if( iOffset < 0 )
      {
        return;
      }

      line = root.getElement( root.getElementIndex( iOffset ) );
      int iStmtLineStart = line.getStartOffset();
      int iStmtLineEnd = line.getEndOffset();
      strLine = line.getDocument().getText( iStmtLineStart, iStmtLineEnd - iStmtLineStart );
      StringBuilder strbIndent = new StringBuilder();
      for( int iIndent = 0; iIndent < strLine.length(); iIndent++ )
      {
        char c = strLine.charAt( iIndent );
        if( c != ' ' && c != '\t' )
        {
          break;
        }
        strbIndent.append( c );
      }

      String newText = strbIndent.toString() + '}';
      getEditor().select( iBraceLineStart, iBraceLineEnd );
      getEditor().replaceSelection( newText );

      //restore the caret position if necessary
      if( getEditor().getCaretPosition() != caretPosition )
      {
        getEditor().setCaretPosition( iBraceLineStart + strbIndent.length() + (wasBraceTyped ? 1 : 0) );
      }

      revalidate();
    }
    catch( Exception e )
    {
      // ignore
    }
  }

  public void parse()
  {
    parse( false );
  }

  protected void parse( boolean forceCodeCompletion )
  {
    postTaskInParserThread( getParseTask( forceCodeCompletion ) );
  }

  public static void postTaskInParserThread( Runnable task )
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    tq.postTask( task );
  }

  public static TaskQueue getParserTaskQueue()
  {
    return TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
  }

  public boolean isParserSuspended()
  {
    return _bParserSuspended;
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setParserSuspended( boolean bParserSuspended )
  {
    _bParserSuspended = bParserSuspended;
  }

  private ParseTask getParseTask( boolean forceCodeCompletion )
  {
    return new ParseTask( getEditor().getText(), forceCodeCompletion, true );
  }

  @SuppressWarnings("UnusedDeclaration")
  public static boolean areAnyParserTasksPending()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask )
      {
        return true;
      }
    }
    return false;
  }

  protected boolean areMoreThanOneParserTasksPendingForThisEditor()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    int iCount = 0;
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask && ((ParseTask)task).getEditor() == this )
      {
        if( iCount > 0 )
        {
          // Note we don't count the first one assuming we're trying to determine
          // if there are any queued behind it.
          return true;
        }
        iCount++;
      }
    }
    return false;
  }

  protected boolean areMoreThanOneParserTasksGoingToUpdateContainingType()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    int iCount = 0;
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask )
      {
        EditorHost otherEditor = ((ParseTask)task).getEditor();
        if( otherEditor == this || (otherEditor.getScriptPart() != null && getScriptPart() != null && getScriptPart().getContainingType() == otherEditor.getScriptPart().getContainingType()) )
        {
          if( iCount > 0 )
          {
            // Note we don't count the first one assuming we're trying to determine
            // if there are any queued behind it.
            return true;
          }
          iCount++;
        }
      }
    }
    return false;
  }

  public boolean isCompleteCode()
  {
    return _bCompleteCode;
  }
  public void setCompleteCode( final boolean bCompleteCode )
  {
    _bCompleteCode = bCompleteCode;
  }

  public void handleDot()
  {
    // The completion delay is here mostly to prevent doing path completion during undo/redo.
    runIfNoKeyPressedInMillis( COMPLETION_DELAY,
                               () -> {
                                 setCompleteCode( true );
                                 parse( true );
                                 //handleDot( (ISymbolTable)null );
                               } );
  }

  public void handleColon()
  {
    // The completion delay is here mostly to prevent doing path completion during undo/redo.
    runIfNoKeyPressedInMillis( COMPLETION_DELAY,
                               () -> {
                                 setCompleteCode( true );
                                 parse( true );
                                 //handleDot( (ISymbolTable)null );
                               } );
  }

  public void handleCompleteCode()
  {
    setCompleteCode( true );
    postTaskInParserThread( () -> {
      if( isCompleteCode() )
      {
        try
        {
          final ISymbolTable atCursor = getSymbolTableAtCursor();
          SwingUtilities.invokeLater( () -> {
            if( isCompleteCode() )
            {
              try
              {
                handleDot( atCursor );
              }
              finally
              {
                setCompleteCode( false );
              }
            }
          } );
        }
        catch( RuntimeException e )
        {
          setCompleteCode( false );
          throw e;
        }
      }
    } );
  }

  protected abstract void handleDot( final ISymbolTable transientSymTable );

  public abstract ISymbolTable getSymbolTableAtCursor();

  void runIfNoKeyPressedInMillis( long lMillis, final Runnable task )
  {
    final boolean[] bKeyPressed = new boolean[]{false};
    final KeyListener keyListener =
      new KeyAdapter()
      {
        @Override
        public void keyPressed( KeyEvent e )
        {
          bKeyPressed[0] = true;
        }
      };
    getEditor().addKeyListener( keyListener );

    Timer timer = _timerPool.requestTimer( (int)lMillis,
     e -> {
       getEditor().removeKeyListener( keyListener );
       if( !bKeyPressed[0] )
       {
         EditorUtilities.invokeInDispatchThread( task );
       }
       _iTimerCount--;
     } );

    timer.setRepeats( false );
    _iTimerCount++;
    timer.start();
  }

  @SuppressWarnings("UnusedDeclaration")
  public static void waitOnParserThread()
  {
    TaskQueue queue = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    queue.postTaskAndWait( () -> { /*do nothing*/ } );
  }

  @SuppressWarnings("UnusedDeclaration")
  public int getTimerCount()
  {
    return _iTimerCount;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static void waitForIntellisenseTimers()
  {
    _timerPool.waitForAllTimersToFinish();
  }

  class ParseTask implements Runnable
  {
    private String _strSource;
    private boolean _forceCodeCompletion;
    private boolean _changed;

    public ParseTask( String strSource, boolean forceCodeCompletion, boolean changed )
    {
      _strSource = strSource;
      _forceCodeCompletion = forceCodeCompletion;
      _changed = changed;
    }

    public EditorHost getEditor()
    {
      return EditorHost.this;
    }

    @Override
    public void run()
    {
      if( !getEditor().isShowing() )
      {
        return;
      }
      if( !areMoreThanOneParserTasksPendingForThisEditor() || _forceCodeCompletion )
      {
        TypeSystem.lock();
        try
        {
          parse( _strSource, _forceCodeCompletion, _changed );
        }
        finally
        {
          //!! NOTE: do not refresh the type we just parsed in the editor, it will otherwise
          //!!       reparse the type from DISK, which will be stale compared with changes in the editor.
          TypeSystem.unlock();
          if( _forceCodeCompletion )
          {
            setCompleteCode( false );
          }
        }
      }
    }
  }

  public boolean isAltDown()
  {
    return _bAltDown;
  }

  /**
   */
  class EditorKeyHandler extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      setCompleteCode( false );
      _bAltDown = (e.getModifiers() & InputEvent.ALT_MASK) > 0;
      if( e.getKeyChar() == KeyEvent.VK_ENTER && e.getModifiers() == 0 )
      {
        _bEnterPressedConsumed = e.isConsumed() || isCompletionPopupShowing();
      }
      else if( e.getKeyChar() == KeyEvent.VK_SPACE && e.isControlDown() )
      {
        if( !isCompletionPopupShowing() )
        {
          handleCompleteCode();
          e.consume();
        }
      }
      else if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == '\b' )
      {
        handleBackspace();
        if( e.getModifiers() == InputEvent.SHIFT_MASK )
        {
          getEditor().dispatchEvent( new KeyEvent( (Component)e.getSource(), e.getID(), e.getWhen(), 0, e.getKeyCode(), e.getKeyChar(), e.getKeyLocation() ) );
        }
      }
    }

    @Override
    public void keyTyped( final KeyEvent e )
    {
      final boolean consumed = e.isConsumed();
      if( consumed || !getEditor().isEditable() )
      {
        return;
      }

      final char keyChar = e.getKeyChar();
      final int modifiers = e.getModifiers();

      postProcessKeystroke( consumed, keyChar, modifiers );
    }

    private void postProcessKeystroke( boolean consumed, char keyChar, int modifiers )
    {
      if( keyChar == '.' )
      {
        handleDot();
      }
      if( keyChar == ':' )
      {
        handleColon();
      }
      if( keyChar == '#' )
      {
        handleDot();
      }

      else
      {
        if( keyChar == KeyEvent.VK_ENTER && modifiers == 0 )
        {
          if( !consumed && !_bEnterPressedConsumed && !isCompletionPopupShowing() )
          {
            handleEnter();
          }
        }
        //      else if( e.getKeyChar() == KeyEvent.VK_SPACE && (e.getModifiers() & InputEvent.CTRL_MASK) > 0 )
        //      {
        //        if( !isIntellisenseShowing() )
        //        {
        //          handleCompleteCode();
        //        }
        //      }
        else if( keyChar == '}' )
        {
          if( !consumed && !isCompletionPopupShowing() )
          {
            handleBraceRight();
          }
        }
      }
    }
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

  private static class TimerPool
  {
    List<Object> _activeTimers = new ArrayList<>();

    Timer requestTimer( int millis, final ActionListener action )
    {
      synchronized( this )
      {
        final Object timerToken = new Object();
        Timer timer = new Timer( millis, new ActionListener()
        {
          @Override
          public void actionPerformed( ActionEvent e )
          {
            try
            {
              action.actionPerformed( e );
            }
            finally
            {
              synchronized( TimerPool.this )
              {
                _activeTimers.remove( timerToken );
                TimerPool.this.notify();
              }
            }
          }
        } );
        _activeTimers.add( timerToken );
        return timer;
      }
    }

    void waitForAllTimersToFinish()
    {
      synchronized( this )
      {
        while( !_activeTimers.isEmpty() )
        {
          try
          {
            wait();
          }
          catch( InterruptedException e )
          {
            // ?
          }
        }
      }
    }
  }
}
