package editor.util;

import editor.CopyBuffer;
import editor.EditorHostTextPane;
import editor.GosuEditor;
import editor.PasteBufferSelectDialog;
import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.undo.CompoundEdit;

public class TextComponentUtil
{

  private static final Set<String> SIGNIFICANT_CHARS = new HashSet( Arrays.asList( ".", "#", ";", ")", "(", "\"" ) );
  private static final String SHOW_PASTE_BUFFER = "show-paste-buffer";

  public static enum Direction
  {
    FORWARD,
    BACKWARD
  }

  public static int findCharacterPositionOnLine( int startPosition, String string, char charToFind, Direction d )
  {
    int i = startPosition;
    char curr = string.charAt( i );
    while( curr != charToFind )
    {
      if( curr == '\n' && charToFind != '\n' )
      {
        return -1;
      }
      if( i < 0 || i >= string.length() )
      {
        return -1;
      }
      i += d == Direction.FORWARD ? 1 : -1;
      curr = string.charAt( i );
    }
    return i;
  }


  public static String getWordAtCaret( JTextComponent editor )
  {
    try
    {
      int iCaretPos = editor.getCaretPosition();
      int iStart = getWordStart( editor, iCaretPos );
      int iEnd = getWordEnd( editor, iCaretPos );
      return editor.getText( iStart, iEnd - iStart );
    }
    catch( BadLocationException e )
    {
      return getPartialWordBeforeCaret( editor );
    }
  }

  public static String getPartialWordBeforeCaret( JTextComponent editor )
  {
    return getPartialWordBeforePos( editor, editor.getCaretPosition() );
  }
  public static String getPartialWordBeforePos( JTextComponent editor, int iPos )
  {
    try
    {
      int iStart = getWordStart( editor, iPos );
      return editor.getText( iStart, iPos - iStart );
    }
    catch( BadLocationException e )
    {
      return "";
    }
  }

  public static Dimension getWordDimensionAtCaret( JTextComponent editor )
  {
    try
    {
      int iCaretPos = editor.getCaretPosition();
      int iStart = getWordStart( editor, iCaretPos );
      int iEnd = getWordEnd( editor, iCaretPos );

      editor.getText( iStart, iEnd - iStart );
      return new Dimension( iStart, iEnd );
    }
    catch( BadLocationException e )
    {
      return getWordDimensionBeforeCaret( editor );
    }
  }

  public static Dimension getWordDimensionBeforeCaret( JTextComponent editor )
  {
    try
    {
      int iEnd = editor.getCaretPosition();
      int iStart = getPreviousWord( editor, iEnd );

      editor.getText( iStart, iEnd - iStart );
      return new Dimension( iStart, iEnd );
    }
    catch( BadLocationException e )
    {
      return null;
    }
  }

  public static Dimension getMemberDimensionAtCaret( JTextComponent editor )
  {
    String strPath = getWordAtCaret( editor );
    if( strPath == null || strPath.length() == 0 )
    {
      return null;
    }

    Dimension dimWordAtCaret = getWordDimensionAtCaret( editor );

    int iCaretPos = editor.getCaretPosition();
    String strFirstPart = strPath.substring( 0, iCaretPos - dimWordAtCaret.width );

    int iFirstDot = strFirstPart.lastIndexOf( '.' );
    iFirstDot = iFirstDot < 0 ? 0 : iFirstDot + 1;
    int iLastDot = strPath.indexOf( '.', iCaretPos - dimWordAtCaret.width );
    iLastDot = iLastDot < 0 ? strPath.length() : iLastDot;

    dimWordAtCaret.height = dimWordAtCaret.width + iLastDot;
    dimWordAtCaret.width += iFirstDot;

    return dimWordAtCaret;
  }

  public static String getMemberAtCaret( JTextComponent editor )
  {
    String strPath = getWordAtCaret( editor );
    if( strPath == null || strPath.length() == 0 )
    {
      return null;
    }

    Dimension dimWordAtCaret = getWordDimensionAtCaret( editor );

    int iCaretPos = editor.getCaretPosition();
    String strFirstPart = strPath.substring( 0, iCaretPos - dimWordAtCaret.width );

    int iFirstDot = strFirstPart.lastIndexOf( '.' );
    iFirstDot = iFirstDot < 0 ? 0 : iFirstDot + 1;
    int iLastDot = strPath.indexOf( '.', iCaretPos - dimWordAtCaret.width );
    iLastDot = iLastDot < 0 ? strPath.length() : iLastDot;

    return strPath.substring( iFirstDot, iLastDot );
  }

  public static void selectWordAtCaret( JTextComponent editor )
  {
    int iStart = 0;
    int iEnd = 0;
    try
    {
      int iCaretPos = editor.getCaretPosition();
      iStart = getWordStart( editor, iCaretPos );
      iEnd = getWordEnd( editor, iCaretPos );
    }
    catch( BadLocationException e )
    {
      try
      {
        iEnd = editor.getCaretPosition();
        iStart = getPreviousWord( editor, iEnd );
      }
      catch( BadLocationException be )
      {
        // ignore
      }
    }

    if( iStart < iEnd )
    {
      editor.select( iStart, iEnd );
    }
  }

  public static void replaceWordAtClosestDot( JTextComponent editor, String strText )
  {
    Dimension dim = getWordDimensionAtCaret( editor );
    try
    {
      String strReplace = editor.getText( dim.width, dim.height - dim.width );
      if( strReplace == null || (strReplace.length() > 0 && !Character.isJavaIdentifierStart( strReplace.charAt( 0 ) )) )
      {
        dim = getWordDimensionBeforeCaret( editor );
      }
    }
    catch( BadLocationException e )
    {
      dim = getWordDimensionBeforeCaret( editor );
    }

    if( dim == null )
    {
      try
      {
        int iLength = editor.getDocument().getLength();
        editor.getDocument().insertString( iLength - 1, strText, null );
        return;
      }
      catch( BadLocationException be2 )
      {
        EditorUtilities.handleUncaughtException( "", be2 );
        return;
      }
    }

    try
    {
      String strWholePath = editor.getText( dim.width, dim.height - dim.width );
      if( strWholePath != null && strWholePath.length() > 0 )
      {
        int iDotIndex = strWholePath.lastIndexOf( '.', editor.getCaretPosition() - dim.width - 1 );
        if( iDotIndex < 0 )
        {
          iDotIndex = strWholePath.lastIndexOf( '#', editor.getCaretPosition() - dim.width - 1 );
        }
        if( iDotIndex < 0 && dim.width != 0 &&
            (editor.getText( dim.width - 1, 1 ).equals( "." ) || editor.getText( dim.width - 1, 1 ).equals( "#" )) )
        {
          // There a dot immediately before the strWholePath, so include it so we can replace
          // the path after it. This happens in the case where a path starts with an expression
          // that is not a symbol e.g., foo( arg ). whatever or foo[i].whatever or (foo ? baz : bar).whatever etc.
          --dim.width;
          strWholePath = editor.getText( dim.width, dim.height - dim.width );
          iDotIndex = 0;
        }
        if( iDotIndex >= 0 )
        {
          if( iDotIndex != strWholePath.length() - 1 )
          {
            int nextDotIndex = strWholePath.indexOf( '.', iDotIndex + 1 );
            if( nextDotIndex < 0 )
            {
              nextDotIndex = strWholePath.indexOf( '#', iDotIndex + 1 );
              if( nextDotIndex < 0 )
              {
                nextDotIndex = dim.height - dim.width;
              }
            }
            editor.select( dim.width + iDotIndex + 1, dim.width + nextDotIndex );
          }
        }
      }

      strText = strText == null ? "" : strText;

      int initialSelectionStart = editor.getSelectionStart();
      editor.replaceSelection( strText );
      EditorUtilities.settleEventQueue();
      selectFirstArg( strText, initialSelectionStart, editor );
    }
    catch( BadLocationException be )
    {
      EditorUtilities.handleUncaughtException( "", be );
    }
  }

  //TODO cgross - this is hacky.  We need to implement something more IntelliJ like with argument selection
  private static void selectFirstArg( String strText, int initialSelectionStart, JTextComponent editor )
    throws BadLocationException
  {
    int firstParen = strText.indexOf( "(" );
    if( firstParen >= 0 && firstParen < strText.indexOf( ")" ) - 1 )
    {
      int startPos = initialSelectionStart + firstParen + 1;
      int wordStart = getWordStart( editor, startPos );
      editor.getCaret().setDot( wordStart );
      String atCaret = getWordAtCaret( editor );
      if( GosuStringUtil.isAlphanumeric( atCaret ) )
      {
        selectWordAtCaret( editor );
      }
      else if( "\\".equals( atCaret ) )
      {
        wordStart = getWordStart( editor, editor.getCaretPosition() );
        editor.getCaret().setDot( wordStart );
        atCaret = getWordAtCaret( editor );
        if( GosuStringUtil.isAlphanumeric( atCaret ) )
        {
          selectWordAtCaret( editor );
        }
      }
    }
  }

  /**
   * @return offset of replaced word
   */
  public static int replaceWordAtCaret( JTextComponent editor, String strText )
  {
    selectWordAtCaret( editor );
    int selectionStart = editor.getSelectionStart();
    editor.replaceSelection( strText == null ? "" : strText );
    return selectionStart;
  }

  /**
   * @return offset of replaced word
   */
  public static int replaceWordBeforeCaret( JTextComponent editor, String strText )
  {
    int iStart = 0;
    int iEnd = 0;
    try
    {
      iEnd = editor.getCaretPosition();
      iStart = getPreviousWord( editor, iEnd );
    }
    catch( BadLocationException be )
    {
      // ignore
    }

    if( iStart < iEnd )
    {
      editor.select( iStart, iEnd );
    }
    int initialSelectionStart = editor.getSelectionStart();
    editor.replaceSelection( strText == null ? "" : strText );
    return initialSelectionStart;
  }

  public static void replaceWordAtCaretNice( JTextComponent editor, String strText )
  {
    String strWordAtCaret = getWordAtCaret( editor );
    if( strWordAtCaret != null && strWordAtCaret.length() > 0 && Character.isLetterOrDigit( strWordAtCaret.charAt( 0 ) ) )
    {
      replaceWordAtCaret( editor, strText );
      return;
    }

    String strWordBeforeCaret = getPartialWordBeforeCaret( editor );
    if( strWordBeforeCaret != null && strWordBeforeCaret.length() > 0 && Character.isLetterOrDigit( strWordBeforeCaret.charAt( 0 ) ) )
    {
      replaceWordBeforeCaret( editor, strText );
      return;
    }

    editor.replaceSelection( strText == null ? "" : strText );
  }

  public static void replaceWordAtCaretDynamic( JTextComponent editor, String strText, IReplaceWordCallback replaceWordCallback, boolean selectFirstArg, boolean replaceWholeWord )
  {
    performCompoundUndableEdit( editor, () -> {
      int initialSelectionStart;
      int wordEnd;
      String strWordAtCaret = getWordAtCaret( editor );
      int caretPosition = editor.getCaretPosition();
      if( strWordAtCaret != null && strWordAtCaret.length() > 0 && replaceWordCallback.shouldReplace( strWordAtCaret ) )
      {
        try
        {
          initialSelectionStart = getWordStart( editor, caretPosition );
          wordEnd = replaceWholeWord ? getWordEnd( editor, initialSelectionStart ) : caretPosition;
        }
        catch( BadLocationException e )
        {
          initialSelectionStart = caretPosition;
          try
          {
            wordEnd = replaceWholeWord ? getWordEnd( editor, initialSelectionStart ) : caretPosition;
          }
          catch( BadLocationException e1 )
          {
            throw new RuntimeException( e1 );
          }
        }
        editor.setSelectionStart( initialSelectionStart );
        editor.setSelectionEnd( wordEnd );
        editor.replaceSelection( strText );
      }
      else
      {
        String strWordBeforeCaret = getPartialWordBeforeCaret( editor );
        if( strWordBeforeCaret != null && strWordBeforeCaret.length() > 0 && replaceWordCallback.shouldReplace( strWordBeforeCaret ) )
        {
          if( strWordBeforeCaret.endsWith( " " ) )
          {
            initialSelectionStart = caretPosition;
            try
            {
              editor.getDocument().insertString( initialSelectionStart, strText, null );
            }
            catch( BadLocationException e )
            {
              throw new RuntimeException( e );
            }
          }
          else
          {
            initialSelectionStart = replaceWordBeforeCaret( editor, strText );
          }
        }
        else
        {
          String strSeparationSpace = strWordAtCaret != null && strWordAtCaret.length() > 0 &&
                                      Character.isJavaIdentifierPart( strWordAtCaret.charAt( 0 ) ) ? " " : "";
          initialSelectionStart = editor.getSelectionStart();
          editor.replaceSelection( strText == null ? "" : strText + strSeparationSpace );
        }
      }

      if( selectFirstArg && strText != null )
      {
        EditorUtilities.settleEventQueue();
        try
        {
          selectFirstArg( strText, initialSelectionStart, editor );
        }
        catch( BadLocationException e )
        {
          EditorUtilities.handleUncaughtException( e );
        }
      }
    } );
  }

  public static void replaceWordAtCaretDynamicAndRemoveEmptyParens( JTextComponent editor, String strText, IReplaceWordCallback replaceWordCallback, boolean selectFirstArg, boolean replaceWholeWord )
  {
    performCompoundUndableEdit( editor, () -> {
      try
      {
        int caret = editor.getCaretPosition();
        if( editor.getDocument().getLength() >= caret + 2 && editor.getText( caret, 2 ).equals( "()" ) )
        {
          editor.setSelectionStart( caret );
          editor.setSelectionEnd( caret + 2 );
          editor.replaceSelection( "" );
        }
      }
      catch( BadLocationException e )
      {
        // ignore
      }
      replaceWordAtCaretDynamic( editor, strText, replaceWordCallback, selectFirstArg, replaceWholeWord );
    } );
  }

  private static void performCompoundUndableEdit( JTextComponent editor, Runnable edit )
  {
    CompoundEdit undoAtom = null;
    if( editor instanceof EditorHostTextPane )
    {
      undoAtom = ((EditorHostTextPane)editor).getEditor().getUndoManager().beginUndoAtom( "Duplicate Line" );
    }
    try
    {
      edit.run();
    }
    finally
    {
      if( undoAtom != null )
      {
        ((EditorHostTextPane)editor).getEditor().getUndoManager().endUndoAtom( undoAtom );
      }
    }
  }

  public static int getWordStart( JTextComponent editor, int iOffset ) throws BadLocationException
  {
    String text = editor.getText();
    iOffset = maybeAdjustOffsetToNextWord( text, iOffset );

    int iStart = iOffset;
    for( ;iStart > 0 && Character.isJavaIdentifierPart( text.charAt( iStart ) ); iStart-- );
    if( iStart != iOffset )
    {
      iStart++;
    }
    return iStart;
  }

  public static int getWordEnd( JTextComponent editor, int iOffset ) throws BadLocationException
  {
    String text = editor.getText();
    iOffset = maybeAdjustOffsetToNextWord( text, iOffset );

    int iEnd = iOffset;
    for( ;iEnd < text.length() && Character.isJavaIdentifierPart( text.charAt( iEnd ) ); iEnd++ );
    if( iEnd == iOffset && !Character.isWhitespace( text.charAt( iEnd ) ) )
    {
      // the word is a single, non-identifier character
      iEnd++;
    }
    return iEnd;
  }

  private static int maybeAdjustOffsetToNextWord( String text, int iOffset ) throws BadLocationException
  {
    if( text.length() < iOffset )
    {
      throw new BadLocationException( "Index out of bounds. Offset: " + iOffset + "  Length: " + text.length(), iOffset );
    }

    if( text.length() == iOffset || Character.isWhitespace( text.charAt( iOffset ) ) )
    {
      if( Character.isWhitespace( text.charAt( iOffset-1 ) ) )
      {
        while( iOffset < text.length() )
        {
          if( !Character.isWhitespace( text.charAt( iOffset ) ) )
          {
            return iOffset;
          }
          iOffset++;
        }
      }
      iOffset--;
    }
    return iOffset;
  }

  private static int getPreviousWord( JTextComponent editor, int iOffset ) throws BadLocationException
  {
    String text = editor.getText();
    int iStart = getWordStart( editor, iOffset );
    for( iOffset = iStart-1; iOffset >= 0 && Character.isWhitespace( text.charAt( iOffset ) ); iOffset-- );
    if( iOffset < 0 )
    {
      return iStart;
    }
    return getWordStart( editor, iOffset );
  }

  public static int adjustForLineComment( JTextComponent editor, int iStart ) throws BadLocationException
  {
    Document doc = editor.getDocument();
    Element root = doc.getDefaultRootElement();
    Element line = root.getElement( root.getElementIndex( iStart ) );
    int iLineOffset = line.getStartOffset();
    int iLength = iStart - iLineOffset;
    if( iLength <= 0 )
    {
      return iStart;
    }
    String strLine = doc.getText( iLineOffset, iLength );
    if( strLine.contains( "//" ) )
    {
      return iLineOffset;
    }
    return iStart;
  }

  public static boolean isNonWhitespaceBetween( JTextComponent editor, int iStart, int iEnd )
  {
    if( iStart > iEnd )
    {
      int iTemp = iEnd;
      iEnd = iStart;
      iStart = iTemp;
    }

    try
    {
      String strText = editor.getText( iStart, iEnd - iStart );
      return strText.trim().length() > 0;
    }
    catch( BadLocationException e )
    {
      // ignore
    }
    return false;
  }

//  private static int adjustForAdditionalSymbols( JTextComponent editor, int iStart, int iEnd )
//  {
//    try
//    {
//      int iPossibleStart = Utilities.getPreviousWord( editor, iStart );
//      String currWord = editor.getText( iStart, iEnd - iStart );
//      String strPossibleAdditionalSymbol = editor.getText( iPossibleStart, iStart - iPossibleStart );
//      while( isAdditionalSymbol( currWord, strPossibleAdditionalSymbol, "_" ) || isAdditionalSymbol( currWord, strPossibleAdditionalSymbol, "$" ) )
//      {
//        iStart = iPossibleStart;
//        iPossibleStart = Utilities.getPreviousWord( editor, iStart );
//        currWord = strPossibleAdditionalSymbol;
//        strPossibleAdditionalSymbol = editor.getText( iPossibleStart, iStart - iPossibleStart );
//      }
//    }
//    catch( BadLocationException e )
//    {
//      // Ignore
//    }
//    return iStart;
//  }

  private static boolean isAdditionalSymbol( String currWord, String strPossibleAdditionalSymbol, String symbol )
  {
    return (GosuObjectUtil.equals( currWord, symbol ) && !GosuStringUtil.isEmpty( strPossibleAdditionalSymbol ) && Character.isLetterOrDigit( strPossibleAdditionalSymbol.charAt( 0 ) ))
           || (GosuObjectUtil.equals( strPossibleAdditionalSymbol, symbol ) && !GosuStringUtil.isEmpty( currWord ) && Character.isLetterOrDigit( currWord.charAt( 0 ) ));
  }

  /**
   * This will return a two element array, start line pos and end line pos.
   * <p/>
   * The character from the document for the startPos will be the first character of the line
   * <p/>
   * The character from the document for the endPos will be the last character of the line, i.e. the char before the new
   * line if one is present.
   *
   * @return a two element array with the start and end positions of the given line
   */
  public static int[] getLineStartAndEndPositions( String text, int initialCaretPosition )
  {
    return new int[]{getLineStart( text, initialCaretPosition ), getLineEnd( text, initialCaretPosition )};
  }

  public static int getLineStart( String text, int initialCaretPosition )
  {
    int pos = Math.min( Math.max( 0, initialCaretPosition ), text.length() );
    while( pos > 0 )
    {
      if( text.charAt( pos - 1 ) == '\n' )
      {
        break;
      }
      pos--;
    }
    return pos;
  }

  public static int getLineEnd( String text, int initialCaretPosition )
  {
    int pos = Math.min( Math.max( 0, initialCaretPosition ), text.length() );
    while( pos < text.length() )
    {
      if( text.charAt( pos ) == '\n' )
      {
        break;
      }
      pos++;
    }
    return pos;
  }

  public static void deleteWordAtCaret( JTextComponent editor ) throws BadLocationException
  {
    int caretPosition = editor.getCaretPosition();
    String s = findPreviousTextChunk( caretPosition, editor.getText() );
    editor.getDocument().remove( caretPosition - s.length(), s.length() );
  }

  protected static String findPreviousTextChunk( int startPosition, String text )
  {

    if( startPosition <= 0 )
    {
      return "";
    }
    if( startPosition > text.length() )
    {
      return "";
    }

    boolean foundNonWhitespace = false;
    boolean foundNewline = false;
    int endPosition = startPosition;

    while( startPosition > 0 )
    {
      String s = text.substring( startPosition - 1, startPosition );

      //break on significant chars
      if( SIGNIFICANT_CHARS.contains( s ) )
      {
        if( startPosition == endPosition || !foundNonWhitespace )
        {
          startPosition--;
        }
        break;
      }
      else if( !GosuStringUtil.isWhitespace( s ) )
      {
        foundNonWhitespace = true;
        if( foundNewline )
        {
          break;
        }
      }
      else if( foundNonWhitespace )
      {
        break;
      }
      else if( "\n".equals( s ) )
      {
        foundNewline = true;
      }

      startPosition--;
    }

    return text.substring( startPosition, endPosition );
  }

  /**
   * Gets the line at a given position in the editor
   */
  public static int getLineAtPosition( JTextComponent editor, int position )
  {
    if( position <= 0 )
    {
      return 1;
    }

    String s = editor.getText();
    if( position > s.length() )
    {
      position = s.length();
    }

    try
    {
      return GosuStringUtil.countMatches( editor.getText( 0, position ), "\n" ) + 1;
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static int getColumnAtPosition( JTextComponent editor, int caretPosition )
  {
    if( editor.getDocument().getLength() == 0 )
    {
      return 0;
    }

    if( caretPosition >= editor.getDocument().getLength() - 1 )
    {
      caretPosition = editor.getDocument().getLength() - 1;
    }

    while( caretPosition < 0 )
    {
      try
      {
        String s = editor.getDocument().getText( caretPosition, 1 );
        if( "\n".equals( s ) )
        {
          break;
        }
        caretPosition--;
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }

    return caretPosition;
  }

  /**
   * Returns the start of the previous line if that line is only whitespace.  Returns -1 otherwise.
   */
  public static int getWhiteSpaceLineStartBefore( String script, int start )
  {
    int startLine = getLineStart( script, start );
    if( startLine > 0 )
    {
      int nextLineEnd = startLine - 1;
      int previousLineStart = getLineStart( script, nextLineEnd );
      boolean whitespace = GosuStringUtil.isWhitespace( script.substring( previousLineStart, nextLineEnd ) );
      if( whitespace )
      {
        return previousLineStart;
      }
    }
    return -1;
  }

  /**
   * Returns the start of the next line if that line is only whitespace.  Returns -1 otherwise.
   */
  public static int getWhiteSpaceLineStartAfter( String script, int end )
  {
    int endLine = getLineEnd( script, end );
    if( endLine < script.length() - 1 )
    {
      int nextLineStart = endLine + 1;
      int nextLineEnd = getLineEnd( script, nextLineStart );
      boolean whitespace = GosuStringUtil.isWhitespace( script.substring( nextLineStart, nextLineEnd ) );
      if( whitespace )
      {
        return nextLineStart;
      }
    }
    return -1;
  }

  /**
   * Returns the start of the previous line if that line is only whitespace.  Returns -1 otherwise.
   */
  public static int getWhiteSpaceOrCommentLineStartBefore( String script, int start )
  {
    int startLine = getLineStart( script, start );
    if( startLine > 0 )
    {
      int nextLineEnd = startLine - 1;
      int previousLineStart = getLineStart( script, nextLineEnd );
      String line = script.substring( previousLineStart, nextLineEnd );
      boolean whitespace = GosuStringUtil.isWhitespace( line ) || line.trim().startsWith( "//" );
      if( whitespace )
      {
        return previousLineStart;
      }
    }
    return -1;
  }

  /**
   * Returns the start of the next line if that line is only whitespace.  Returns -1 otherwise.
   */
  public static int getWhiteSpaceOrCommentLineStartAfter( String script, int end )
  {
    int endLine = getLineEnd( script, end );
    if( endLine < script.length() - 1 )
    {
      int nextLineStart = endLine + 1;
      int nextLineEnd = getLineEnd( script, nextLineStart );
      String line = script.substring( nextLineStart, nextLineEnd );
      boolean whitespace = GosuStringUtil.isWhitespace( line ) || line.trim().startsWith( "//" );
      if( whitespace )
      {
        return nextLineStart;
      }
    }
    return -1;
  }

  /**
   * Eats whitespace lines after the given offset until it finds a non-whitespace line and
   * returns the start of the last whitespace line found, or the initial value if none was.
   */
  public static int getDeepestWhiteSpaceLineStartAfter( String script, int offset )
  {
    if( offset < 0 )
    {
      return offset;
    }

    int i = offset;

    while( true )
    {
      int lineStartAfter = getWhiteSpaceLineStartAfter( script, i );
      if( lineStartAfter == -1 )
      {
        return i;
      }
      else
      {
        i = lineStartAfter;
      }
    }
  }

  /**
   * @return the next non-whitespace position in the given script
   */
  public static int findNonWhitespacePositionAfter( String script, int position )
  {
    if( position < 0 )
    {
      return position;
    }

    while( position < script.length() )
    {
      if( !Character.isWhitespace( script.charAt( position ) ) )
      {
        break;
      }
      position++;
    }
    return position;
  }

  /**
   * @return the first non-whitespace position in the given script before the given position
   */
  public static int findNonWhitespacePositionBefore( String script, int position )
  {
    if( position > script.length() - 1 )
    {
      return position;
    }

    while( position > 0 )
    {
      if( !Character.isWhitespace( script.charAt( position ) ) )
      {
        break;
      }
      position--;
    }
    return position;
  }

  public static int getNextWordPostition( int pos, String source, boolean consumeWhitespaceFirst )
  {
    if( pos < source.length() )
    {

      if( consumeWhitespaceFirst )
      {
        while( pos < source.length() && Character.isWhitespace( source.charAt( pos ) ) && source.charAt( pos ) != '\n' )
        {
          pos++;
        }
      }

      int initialCharClass = getCharClass( source.charAt( pos ) );
      while( pos < source.length() )
      {
        if( getCharClass( source.charAt( pos ) ) == initialCharClass )
        {
          pos++;
        }
        else
        {
          break;
        }
      }

      if( !consumeWhitespaceFirst )
      {
        while( pos < source.length() && Character.isWhitespace( source.charAt( pos ) ) && source.charAt( pos ) != '\n' )
        {
          pos++;
        }
      }

      return pos;
    }
    return -1;
  }

  private static int getCharClass( char c )
  {
    if( GosuStringUtil.isAsciiAlphanumeric( c ) )
    {
      return 0;
    }
    else if( Character.isWhitespace( c ) )
    {
      return 1;
    }
    else
    {
      return 2;
    }
  }

  public static void fixTextComponentKeyMap( JTextComponent editor )
  {
    KeyMapController keyMapController = new KeyMapController( editor );

    keyMapController.bindKeyToAction( EditorUtilities.CONTROL_KEY_NAME + " X", new CustomCutAction() );
    keyMapController.bindKeyToAction( EditorUtilities.CONTROL_KEY_NAME + " C", new CustomCopyAction() );

    keyMapController.bindKeyToAction(
      KeyEvent.VK_HOME,
      DefaultEditorKit.beginLineAction,
      new CustomHomeAction() );

    keyMapController.bindKeyToAction(
      KeyEvent.VK_RIGHT, EditorUtilities.CONTROL_KEY_MASK,
      DefaultEditorKit.nextWordAction,
      new JumpRightAction() );

    keyMapController.bindKeyToAction(
      KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, EditorUtilities.CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ),
      DefaultEditorKit.selectionNextWordAction,
      new SelectRightAction() );

    keyMapController.bindKeyToAction(
      KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, EditorUtilities.CONTROL_KEY_MASK ),
      DefaultEditorKit.previousWordAction,
      new JumpLeftAction() );

    keyMapController.bindKeyToAction(
      KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, EditorUtilities.CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ),
      DefaultEditorKit.selectionPreviousWordAction,
      new SelectLeftAction() );

    keyMapController.bindKeyToAction(
      KeyStroke.getKeyStroke( KeyEvent.VK_V, EditorUtilities.CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ),
      SHOW_PASTE_BUFFER,
      new ShowPasteBufferAction() );
  }

  public static void expandSelectionIfNeeded( JTextComponent editor )
  {
    if( GosuStringUtil.isEmpty( editor.getSelectedText() ) )
    {
      selectLineAtCaret( editor );
    }
  }

  public static void selectLineAtCaret( JTextComponent editor )
  {
    int lineStart = getLineStart( editor.getText(), editor.getCaretPosition() );
    int lineEnd = getLineEnd( editor.getText(), editor.getCaretPosition() );
    if( lineEnd < editor.getText().length() )
    {
      lineEnd++;
    }
    editor.getCaret().setDot( lineEnd );
    editor.getCaret().moveDot( lineStart );
  }

  public static void selectRight( JTextComponent editor )
  {
    String source = editor.getText();
    int pos = editor.getCaretPosition();
    pos = TextComponentUtil.getNextWordPostition( pos, source, false );
    if( pos != -1 )
    {
      editor.getCaret().moveDot( pos );
    }
  }

  public static void jumpRight( JTextComponent editor )
  {
    String source = editor.getText();
    int pos = editor.getCaretPosition();
    pos = TextComponentUtil.getNextWordPostition( pos, source, false );
    if( pos != -1 )
    {
      editor.setCaretPosition( pos );
    }
  }

  public static void jumpLeft( JTextComponent editor )
  {
    String source = GosuStringUtil.reverse( editor.getText() );
    int pos = source.length() - editor.getCaretPosition();
    pos = TextComponentUtil.getNextWordPostition( pos, source, true );
    if( pos != -1 )
    {
      editor.setCaretPosition( source.length() - pos );
    }
  }

  public static void selectLeft( JTextComponent editor )
  {
    String source = GosuStringUtil.reverse( editor.getText() );
    int pos = source.length() - editor.getCaretPosition();
    pos = TextComponentUtil.getNextWordPostition( pos, source, true );
    if( pos != -1 )
    {
      editor.getCaret().moveDot( source.length() - pos );
    }
  }

  public static void handleHomeKey( JTextComponent editor )
  {
    int dot = editor.getCaretPosition();
    try
    {

      String text = editor.getText();
      int initialCaretPosition = editor.getCaretPosition();
      int lineStart = TextComponentUtil.getLineStart( text, initialCaretPosition );
      int lineEnd = TextComponentUtil.getLineEnd( text, initialCaretPosition );
      int start = lineStart;

      while( GosuStringUtil.isWhitespace( editor.getDocument().getText( start, 1 ) ) && start < lineEnd )
      {
        start++;
      }

      if( dot == start )
      {
        editor.setCaretPosition( lineStart );
      }
      else
      {
        editor.setCaretPosition( start );
      }

    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  public static void showPasteBufferDialogForComponent( JTextComponent component )
  {
    PasteBufferSelectDialog dialog = new PasteBufferSelectDialog( component );
    dialog.setVisible( true );
  }

  public static void unindentLineAtCaret( JTextComponent editor ) throws BadLocationException
  {
    int start = editor.getSelectionStart();
    int end = editor.getSelectionEnd();
    String text = editor.getText();

    //If the caret is positioned at the first point in a new line, treat it as if it is on the previous line only
    if( start != end && end > 0 && text.charAt( end - 1 ) == '\n' )
    {
      end = end - 1;
    }

    int lineStart = getLineStart( text, start );
    int lineEnd = getLineEnd( text, end );

    editor.setSelectionStart( lineStart );
    editor.setSelectionEnd( lineEnd );

    String[] selectedLines = text.substring( lineStart, lineEnd ).split( "\n" );
    int originalLinePosition = lineStart;
    for( int i = 0; i < selectedLines.length; i++ )
    {
      int originalLength = selectedLines[i].length();
      int tab = GosuEditor.TAB_SIZE;
      while( tab > 0 && selectedLines[i].length() > 0 && Character.isWhitespace( selectedLines[i].charAt( 0 ) ) )
      {
        selectedLines[i] = selectedLines[i].substring( 1 );
        if( originalLinePosition < start )
        {
          start--;
        }
        if( originalLinePosition < end )
        {
          end--;
        }
        tab--;
      }
      originalLinePosition += originalLength + 1;
    }
    editor.replaceSelection( GosuStringUtil.join( selectedLines, '\n' ) );
    editor.setSelectionStart( start );
    editor.setSelectionEnd( end );
  }

  public static String getIdentifierAtCaret( EditorHostTextPane editor )
  {
    String wordAtCaret = getWordAtCaret( editor );
    if( isValidIdentifier( wordAtCaret, false ) )
    {
      return wordAtCaret;
    }
    else
    {
      String wordBeforeCaret = getPartialWordBeforeCaret( editor );
      if( isValidIdentifier( wordBeforeCaret, false ) )
      {
        return wordBeforeCaret;
      }
      else
      {
        return "";
      }
    }
  }

  public static boolean isValidIdentifier( CharSequence seqId, boolean acceptDot )
  {
    if( seqId.length() == 0 || !Character.isJavaIdentifierStart( seqId.charAt( 0 ) ) )
    {
      return false;
    }
    for( int i = 1, iLen = seqId.length(); i < iLen; i++ )
    {
      if( acceptDot && (seqId.charAt( i ) == '.' || seqId.charAt( i ) == '#') )
      {
        continue;
      }

      if( !Character.isJavaIdentifierPart( seqId.charAt( i ) ) )
      {
        return false;
      }
    }
    return true;
  }

  public static String makeValidIdentifier( String str, boolean acceptDot, boolean bAcceptUnderscore )
  {
    StringBuilder rtn = new StringBuilder( str );
    while( rtn.length() > 0 && (!Character.isJavaIdentifierStart( rtn.charAt( 0 ) ) || (!bAcceptUnderscore && rtn.charAt( 0 ) == '_')) )
    {
      rtn.deleteCharAt( 0 );
    }

    for( int i = 1; i < rtn.length(); )
    {
      char c = rtn.charAt( i );
      if( acceptDot && (c == '.' || c == '#') )
      {
        i++;
        continue;
      }

      if( !Character.isJavaIdentifierPart( c ) || (!bAcceptUnderscore && c == '_') )
      {
        rtn.deleteCharAt( i );
        continue;
      }

      i++;
    }
    return rtn.toString();
  }

  //=======================================================================================
  // Custom text component actions
  //=======================================================================================
  private static class CustomCutAction extends DefaultEditorKit.CutAction
  {
    @Override
    public void actionPerformed( ActionEvent e )
    {
      JTextComponent comp = (JTextComponent)e.getSource();
      TextComponentUtil.expandSelectionIfNeeded( comp );
      super.actionPerformed( e );
      CopyBuffer.instance().captureState();
    }
  }

  private static class CustomCopyAction extends DefaultEditorKit.CopyAction
  {
    @Override
    public void actionPerformed( ActionEvent e )
    {
      JTextComponent comp = (JTextComponent)e.getSource();
      TextComponentUtil.expandSelectionIfNeeded( comp );
      super.actionPerformed( e );
      CopyBuffer.instance().captureState();
    }
  }

  private static class CustomHomeAction extends AbstractAction
  {
    public CustomHomeAction()
    {
      super( DefaultEditorKit.beginLineAction );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      TextComponentUtil.handleHomeKey( (JTextComponent)e.getSource() );
    }
  }

  private static class JumpRightAction extends AbstractAction
  {
    public JumpRightAction()
    {
      super( DefaultEditorKit.nextWordAction );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      TextComponentUtil.jumpRight( (JTextComponent)e.getSource() );
    }
  }

  private static class SelectRightAction extends AbstractAction
  {
    public SelectRightAction()
    {
      super( DefaultEditorKit.selectionNextWordAction );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      TextComponentUtil.selectRight( (JTextComponent)e.getSource() );
    }
  }

  private static class JumpLeftAction extends AbstractAction
  {
    public JumpLeftAction()
    {
      super( DefaultEditorKit.previousWordAction );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      TextComponentUtil.jumpLeft( (JTextComponent)e.getSource() );
    }
  }

  private static class SelectLeftAction extends AbstractAction
  {
    public SelectLeftAction()
    {
      super( DefaultEditorKit.selectionPreviousWordAction );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      TextComponentUtil.selectLeft( (JTextComponent)e.getSource() );
    }
  }

  private static class ShowPasteBufferAction extends AbstractAction
  {
    public ShowPasteBufferAction()
    {
      super( TextComponentUtil.SHOW_PASTE_BUFFER );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      JTextComponent component = (JTextComponent)e.getSource();
      TextComponentUtil.showPasteBufferDialogForComponent( component );
    }
  }

  private static class KeyMapController
  {
    private JTextComponent _textComponent;

    public KeyMapController( JTextComponent textComponent )
    {
      _textComponent = textComponent;
    }

    public void bindKeyToAction( String keystrokeDescription, AbstractAction action )
    {
      KeyStroke keyStroke = KeyStroke.getKeyStroke( keystrokeDescription );
      Object actionMapKey = getActionMapKey( keyStroke );
      bindActionKeyToAction( actionMapKey, action );
    }

    public void bindKeyToAction( KeyStroke keyStroke, String actionMapKey, AbstractAction action )
    {
      bindKeyStrokeToActionMapKey( keyStroke, actionMapKey );
      bindActionKeyToAction( actionMapKey, action );
    }

    public void bindActionKeyToAction( Object actionMapKey, AbstractAction action )
    {
      _textComponent.getActionMap().put( actionMapKey, action );
    }

    private Object getActionMapKey( KeyStroke keyStroke )
    {
      return _textComponent.getInputMap().get( keyStroke );
    }

    private void bindKeyStrokeToActionMapKey( KeyStroke keyStroke, String actionMapKey )
    {
      _textComponent.getInputMap().put( keyStroke, actionMapKey );
    }

    public void bindKeyToAction( int keyCode, String actionMapKey, AbstractAction action )
    {
      bindKeyToAction( keyCode, 0, actionMapKey, action );
    }

    public void bindKeyToAction( int keyCode, int keyMask, String actionMapKey, AbstractAction action )
    {
      bindKeyToAction( KeyStroke.getKeyStroke( keyCode, keyMask ), actionMapKey, action );
    }

    private int flipControlToMeta( int keyMask )
    {
      if( (keyMask & EditorUtilities.CONTROL_KEY_MASK) == 0 )
      {
        return keyMask;
      }
      else
      {
        return keyMask & ~EditorUtilities.CONTROL_KEY_MASK | KeyEvent.META_DOWN_MASK;
      }
    }

    private String flipControlToMeta( String keyStrokeDescription )
    {
      return GosuStringUtil.replace( keyStrokeDescription, "control", "meta" );
    }
  }
}