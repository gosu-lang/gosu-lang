package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IParseTree;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuRefactorUtil;
import gw.util.GosuStringUtil;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.CompoundEdit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides syntax-aware movements of code, including refactors, etc.
 *
 * @author cgross
 */
public class CodeRefactorManager
{
  private GosuEditor _gsEditor;

  public CodeRefactorManager( GosuEditor gsEditor )
  {
    _gsEditor = gsEditor;
  }

  public void moveSelectionUp()
  {
    moveSelection( true );
  }

  public void moveSelectionDown()
  {
    moveSelection( false );
  }

  private void moveSelection( final boolean up )
  {
    _gsEditor.waitForParser();
    moveSelectionNow( up );
  }

  private void moveSelectionNow( boolean up )
  {
    int start = _gsEditor.getEditor().getSelectionStart();
    int end = _gsEditor.getEditor().getSelectionEnd();
    String script = _gsEditor.getEditor().getText();

    //If the caret is positioned at the first point in a new line, treat it as if it is on the previous line only
    if( start != end && end > 0 && script.charAt( end - 1 ) == '\n' )
    {
      end = end - 1;
    }

    //Handle raw whitespace moves
    int rawStart = TextComponentUtil.getLineStart( script, start );
    int rawEnd = TextComponentUtil.getLineEnd( script, end );
    String line = script.substring( rawStart, rawEnd );
    if( GosuStringUtil.isWhitespace( line ) || line.trim().startsWith( "//" ) )
    {
      if( up )
      {
        if( rawStart == 0 )
        {
          return;
        }
        int lineStartBefore = TextComponentUtil.getLineStart( script, rawStart - 1 );
        GosuRefactorUtil.MoveInstruction moveInstruction = new GosuRefactorUtil.MoveInstruction( false, false, lineStartBefore );
        handleMoveInstruction( script, moveInstruction, rawStart, rawEnd, up );
        return;
      }
      else
      {
        if( rawEnd >= script.length() )
        {
          return;
        }
        int lineStartBefore = rawEnd + 1;
        GosuRefactorUtil.MoveInstruction moveInstruction = new GosuRefactorUtil.MoveInstruction( false, false, lineStartBefore );
        handleMoveInstruction( script, moveInstruction, rawStart, rawEnd, up );
        return;
      }
    }

    int peStart = TextComponentUtil.findNonWhitespacePositionAfter( script, rawStart );
    int peEnd = TextComponentUtil.findNonWhitespacePositionBefore( script, rawEnd );

    peStart = Math.min( peStart, script.length() - 1 );
    peEnd = Math.min( peEnd, script.length() - 1 );

    //Find the first statement at the selection start
    IParseTree firstStatement = GosuRefactorUtil.findFirstStatementAtLine( TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), peStart ),
                                                                           peStart,
                                                                           _gsEditor.getParser().getLocations() );

    //Find the last statement at the selection end
    IParseTree lastStatement = GosuRefactorUtil.findLastStatementAtLine( TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), peEnd ),
                                                                         peEnd,
                                                                         _gsEditor.getParser().getLocations() );

    //Find the spanning range of those two statements
    IParseTree[] boundingPair = GosuRefactorUtil.findSpanningLogicalRange( firstStatement, lastStatement );

    //If a bounding pair exists, do the move
    if( boundingPair != null )
    {
      int clipStart = Math.min( rawStart, TextComponentUtil.getLineStart( script, boundingPair[0].getOffset() ) );
      int clipEnd = Math.max( rawEnd, TextComponentUtil.getLineEnd( script, boundingPair[1].getExtent() ) );


      //if this is not a class element (that is, we are in an impl), handle moving into a white space line
      if( !GosuRefactorUtil.isClassElement( boundingPair[0] ) )
      {
        int lineStart = up ? TextComponentUtil.getWhiteSpaceOrCommentLineStartBefore( script, clipStart ) :
                        TextComponentUtil.getWhiteSpaceOrCommentLineStartAfter( script, clipEnd );
        if( lineStart != -1 )
        {
          GosuRefactorUtil.MoveInstruction moveInstruction = new GosuRefactorUtil.MoveInstruction( false, false, lineStart );
          handleMoveInstruction( script, moveInstruction, clipStart, clipEnd, up );
          return;
        }
      }

      //Not handling an into whitespace move, so do a syntax aware move
      GosuRefactorUtil.MoveInstruction moveInstruction = up ? GosuRefactorUtil.getMoveUpInstruction( boundingPair[0] ) :
                                                         GosuRefactorUtil.getMoveDownInstruction( boundingPair[1] );

      if( moveInstruction != null )
      {
        if( GosuRefactorUtil.isClassElement( boundingPair[1] ) )
        {
          IParseTree nextSibling = boundingPair[1].getNextSibling();
          if( nextSibling != null )
          {
            clipEnd = TextComponentUtil.getLineStart( script, nextSibling.getOffset() ) - 1;
            if( !up )
            {
              moveInstruction.position = TextComponentUtil.getDeepestWhiteSpaceLineStartAfter( script, moveInstruction.position );
            }
          }
        }

        handleMoveInstruction( script, moveInstruction, clipStart, clipEnd, up );
      }
    }
  }

  private void handleMoveInstruction( String script, GosuRefactorUtil.MoveInstruction moveInstruction, int startClip, int endClip, boolean up )
  {
    //Do nothing if we are moving down beyond the last position in the script
    if( endClip == script.length() - 1 && !up )
    {
      return;
    }

    if( endClip < script.length() )
    {
      endClip = endClip + 1;
    }

    String movedCodeAsString = script.substring( startClip, endClip );
    if( !movedCodeAsString.endsWith( "\n" ) )
    {
      movedCodeAsString += "\n";
    }
    int offset = moveInstruction.position;

    if( up )
    {
      offset = TextComponentUtil.getLineStart( script, offset );
    }
    else
    {
      offset = TextComponentUtil.getLineEnd( script, offset ) + 1 - movedCodeAsString.length();
    }

    StringBuilder movedCode = new StringBuilder();

    int caretPosition = _gsEditor.getEditor().getCaretPosition();
    int selectionStart = _gsEditor.getEditor().getSelectionStart();
    int selectionEnd = _gsEditor.getEditor().getSelectionEnd();

    int currentPosition = startClip;

    if( moveInstruction.indent )
    {
      String[] strings = movedCodeAsString.split( "\n" );
      for( String str : strings )
      {

        if( caretPosition > currentPosition )
        {
          caretPosition += 2;
        }
        if( selectionStart > currentPosition )
        {
          selectionStart += 2;
        }
        if( selectionEnd > currentPosition )
        {
          selectionEnd += 2;
        }

        movedCode.append( "  " );
        movedCode.append( str );
        movedCode.append( "\n" );
        currentPosition += str.length() + 1;
      }
    }
    else if( moveInstruction.outdent && movedCodeAsString.startsWith( "  " ) )
    {
      String[] strings = movedCodeAsString.split( "\n" );
      for( String str : strings )
      {
        if( str.startsWith( "  " ) )
        {
          if( caretPosition > currentPosition )
          {
            caretPosition -= 2;
          }
          if( selectionStart > currentPosition )
          {
            selectionStart -= 2;
          }
          if( selectionEnd > currentPosition )
          {
            selectionEnd -= 2;
          }
          str = str.substring( 2, str.length() );
        }

        movedCode.append( str );
        movedCode.append( "\n" );
        currentPosition += str.length() + 1;
      }
    }
    else
    {
      movedCode.append( movedCodeAsString );
    }

    CompoundEdit undoAtom = _gsEditor.getUndoManager().getUndoAtom();
    if( undoAtom != null && undoAtom.getPresentationName().equals( "Script Change" ) )
    {
      _gsEditor.getUndoManager().endUndoAtom();
    }
    _gsEditor.getUndoManager().beginUndoAtom( "moveLine" );
    try
    {

      _gsEditor.getEditor().getDocument().remove( startClip, endClip - startClip );
      _gsEditor.getEditor().getDocument().insertString( offset, movedCode.toString(), null );

      _gsEditor.getEditor().setSelectionStart( selectionStart - (startClip - offset) );
      _gsEditor.getEditor().setSelectionEnd( selectionEnd - (startClip - offset) );
      _gsEditor.getEditor().getCaret().moveDot( caretPosition - (startClip - offset) );

    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      _gsEditor.getUndoManager().endUndoAtom();
    }
  }

  public void addToUses( String strType, boolean bTemplate, boolean bProgram )
  {
    if( isTypeUsed( strType ) )
    {
      return;
    }

    Document doc = _gsEditor.getEditor().getDocument();
    int iPos = findUsesInsertionPosition( bProgram );
    int iIndex = doc.getDefaultRootElement().getElementIndex( iPos );
    int iInsertionPt;
    String strUsesStmt = Keyword.KW_uses + " " + strType;
    if( bTemplate )
    {
      strUsesStmt = "<% " + strUsesStmt + " %>";
    }
    strUsesStmt += "\n";

    if( iPos == 0 )
    {
      iInsertionPt = iPos;
      if( !bTemplate )
      {
        strUsesStmt = strUsesStmt + "\n";
      }
    }
    else
    {
      iInsertionPt = doc.getDefaultRootElement().getElement( iIndex ).getEndOffset();
    }

    try
    {
      doc.insertString( iInsertionPt, strUsesStmt, null );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  private boolean isTypeUsed( String strType )
  {
    String strRelativeName = TypePopup.getRelativeTypeName( strType );
    try
    {
      return _gsEditor.getParser().getTypeUsesMap().resolveType( strRelativeName ) != null ||
             TypeSystem.parseType( strRelativeName, _gsEditor.getParser().getTypeUsesMap().copy() ) != null;
    }
    catch( Exception e )
    {
      return false;
    }
  }

  private int findUsesInsertionPosition( boolean bProgram )
  {
    Set usesStmts = _gsEditor.getParser().getTypeUsesMap().getUsesStatements();
    int iPos = -1;
    for( Iterator iterator = usesStmts.iterator(); iterator.hasNext(); )
    {
      IUsesStatement usesStmt = (IUsesStatement)iterator.next();
      iPos = Math.max( usesStmt.getLocation().getOffset(), iPos );
    }
    if( iPos < 0 ) // No uses-stmts exists, check for a package-stmt
    {
      iPos = findPackageLocation();
      if( iPos < 0 )
      {
        if( bProgram )
        {
          // No uses-stms or package-stmt, check for a classpath-stmt
          iPos = findClasspathLocation();
          if( iPos < 0 )
          {
            // No uses-stmts, package-stmt, or classpath-stmt, insert the new uses-stmt at beginning of source
            iPos = 0;
          }
          else
          {
            // Insert after the classpath-stmt
            iPos++;
          }
        }
        else
        {
          // No uses-stmts or package-stmt, insert the new uses-stmt at beginning of source
          iPos = 0;
        }
      }
      else
      {
        // Insert after the package-stmt, that's why we increment the position
        iPos++;
      }
    }
    else if( iPos == 0 ) // Implies there exists at least one uses-stmt
    {
      // Append the new uses-statement instead of inserting it (adding 1 ensures this)
      iPos = 1;
    }
    return iPos;
  }

  private int findPackageLocation()
  {
    List<IParseTree> locations = _gsEditor.getParser().getLocations();
    List<INamespaceStatement> listOut = new ArrayList<INamespaceStatement>();
    IParseTree.Search.getContainedParsedElementsByType( locations, INamespaceStatement.class, listOut );
    if( listOut.size() > 0 )
    {
      return listOut.get( 0 ).getLocation().getOffset();
    }
    return -1;
  }

  private int findClasspathLocation()
  {
    List<IParseTree> locations = _gsEditor.getParser().getLocations();
    List<IClasspathStatement> listOut = new ArrayList<IClasspathStatement>();
    IParseTree.Search.getContainedParsedElementsByType( locations, IClasspathStatement.class, listOut );
    if( listOut.size() > 0 )
    {
      return listOut.get( 0 ).getLocation().getOffset();
    }
    return -1;
  }

  public void extractVariable()
  {
    ExtractVariablePopup extractVarPopup = new ExtractVariablePopup();
    extractVarPopup.showNow( _gsEditor );
  }

}
