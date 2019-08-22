/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IParserPart
{
  IGosuParser getOwner();

  void setDontOptimizeStatementLists( boolean dontOptimizeStatementLists );
  boolean isDontOptimizeStatementLists();

  void setValidator( IGosuValidator validator );

  void setLineNumShift( int lineNumShift );

  int getLineNumShift();

  int getOffsetShift();
  
  static IToken eatBlock( char cBegin, char cEnd, boolean bOperator, ISourceCodeTokenizer tokenizer )
  {
    return eatBlock( cBegin, cEnd, bOperator, false, tokenizer );
  }
  static IToken eatBlock( char cBegin, char cEnd, boolean bOperator, boolean bStopAtDeclarationKeyword, ISourceCodeTokenizer tokenizer )
  {
    int iBraceDepth = 1;
    boolean bNewMatched = false;
    boolean bArg = false;
    do
    {
      IToken token = tokenizer.getCurrentToken();

      final int type = token.getType();

      if( type == ISourceCodeTokenizer.TT_EOF )
      {
        tokenizer.nextToken();
        return null;
      }

      String value = token.getStringValue();

      if( bStopAtDeclarationKeyword )
      {
        // Total hack to handle an annotation with an anonymous new expression and blocks (they has declarations that need to be eaten)
        // We're hacking this because soon, real soon now, we'll be dropkicking support for an anonymous new expression or block
        // as an argument to a gosu annotation -- gosu annotations need to behave like java annotation, no more no less.
        bNewMatched = bNewMatched ||
                      match( null, Keyword.KW_new.toString(), ISourceCodeTokenizer.TT_KEYWORD, true, tokenizer ) ||
                      match( null, "->", ISourceCodeTokenizer.TT_OPERATOR, true, tokenizer ) ||
                      match( null, "#", ISourceCodeTokenizer.TT_OPERATOR, true, tokenizer );
        if( !bNewMatched && !bArg && matchDeclarationKeyword( null, true, tokenizer ) )
        {
          return null;
        }
      }

      bArg = ":".equals( value );

      int mark = tokenizer.mark();
      if( cEnd == type ||
          (bOperator && type == ISourceCodeTokenizer.TT_OPERATOR && value.equals( String.valueOf( cEnd ) )) )
      {
        tokenizer.nextToken();
        if( --iBraceDepth == 0 )
        {
          return (IToken)tokenizer.getTokenAt( mark ).copy();
        }
        continue;
      }
      if( cBegin == type ||
          (bOperator && type == ISourceCodeTokenizer.TT_OPERATOR && value.equals( String.valueOf( cBegin ) )) )
      {
        tokenizer.nextToken();
        iBraceDepth++;
        continue;
      }

      tokenizer.nextToken();
    } while( true );
  }

  static boolean match( IToken T, String strToken, int iType, boolean bPeek, ISourceCodeTokenizer tokenizer )
  {
    boolean bMatch = false;

    if( T != null )
    {
      tokenizer.copyInto( T );
    }

    IToken currentToken = tokenizer.getCurrentToken();
    int iCurrentType = currentToken.getType();
    if( strToken != null )
    {
      if( (iType == iCurrentType) || ((iType == 0) && (iCurrentType == ISourceCodeTokenizer.TT_WORD)) )
      {
        bMatch = strToken.equals( currentToken.getStringValue() );
      }
    }
    else
    {
      bMatch = (iCurrentType == iType) || (iType == ISourceCodeTokenizer.TT_WORD && currentToken.isValueKeyword());
    }

    if( bMatch && !bPeek )
    {
      tokenizer.nextToken();
    }

    return bMatch;
  }
  
  static boolean matchDeclarationKeyword( String[] ret, boolean bPeek, ISourceCodeTokenizer tokenizer )
  {
    boolean bMatch = false;
    IToken token = tokenizer.getCurrentToken();
    if( token.getType() == ISourceCodeTokenizer.TT_KEYWORD )
    {
      final Keyword keyword = token.getKeyword();
      if( Keyword.KW_construct == keyword ||
          Keyword.KW_function == keyword ||
          Keyword.KW_property == keyword ||
          Keyword.KW_var == keyword ||
          Keyword.KW_delegate == keyword ||
          Keyword.KW_class == keyword ||
          Keyword.KW_interface == keyword ||
          Keyword.KW_annotation == keyword ||
          Keyword.KW_structure == keyword ||
          Keyword.KW_enum == keyword )
      {
        if( ret != null )
        {
          ret[0] = token.getStringValue();
        }
        // We must allow an existing java package to have the same name as a gosu declaration keyword.
        // We check for that by looking for a following '.' after the package reference...
        int mark = tokenizer.mark();
        IToken nextToken = tokenizer.getTokenAt( mark + 1 );
        IToken priorToken = mark == 0 ? null : tokenizer.getTokenAt( mark - 1 );
        bMatch = (nextToken == null || nextToken.getType() != '.') && (priorToken == null || (priorToken.getType() != '.' && !"#".equals( priorToken.getStringValue() )));
        if( !bPeek )
        {
          tokenizer.nextToken();
        }
      }
    }
    return bMatch;
  }
  
}
