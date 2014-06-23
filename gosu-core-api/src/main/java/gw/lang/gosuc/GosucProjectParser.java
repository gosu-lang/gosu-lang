/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.lang.parser.ISourceCodeTokenizer;
import gw.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 */
public class GosucProjectParser {

  private ISourceCodeTokenizer _tokenizer;

  public static GosucProject parse( InputStream file, ICustomParser custParser ) {
    if( file == null ) {
      return null;
    }
    String strContent;
    try {
      strContent = StreamUtil.getContent( StreamUtil.getInputStreamReader( file ) );
    }
    catch( IOException e ) {
      throw new RuntimeException( e );
    }
    return parse( strContent, custParser );
  }

  public static GosucProject parse( String strContent, ICustomParser custParser ) {
    if( strContent == null || strContent.length() == 0 ) {
      return null;
    }

    GosucProjectParser parser = new GosucProjectParser( strContent );
    parser.initTokenizer();
    return GosucProject.parse( parser, custParser );
  }

  private GosucProjectParser( String strContent ) {
    _tokenizer = makeTokenizer(strContent);
    _tokenizer.wordChars( '_', '_' );
    _tokenizer.wordChars( '-', '-' );
    _tokenizer.setSupportsKeywords( false );
  }

  private ISourceCodeTokenizer makeTokenizer(String strContent) {
    try {
      Class<?> srcCodeTokenizerClass = Class.forName("gw.internal.gosu.parser.SourceCodeTokenizer");
      return (ISourceCodeTokenizer) srcCodeTokenizerClass.getConstructor( CharSequence.class ).newInstance( strContent );
    } catch (Exception e) {
      throw new RuntimeException( e );
    }
  }

  public ISourceCodeTokenizer getTokenizer() {
    return _tokenizer;
  }

  private void initTokenizer() {
    _tokenizer.nextToken();
  }

  public boolean matchWord( String word, boolean bPeek ) {
    return match( word, ISourceCodeTokenizer.TT_WORD, bPeek ) ||
           match( word, ISourceCodeTokenizer.TT_KEYWORD, bPeek );
  }

  public boolean matchOperator( String token, boolean bPeek ) {
    boolean bMatch = _tokenizer.getType() == ISourceCodeTokenizer.TT_OPERATOR &&
                     token.equalsIgnoreCase( _tokenizer.getStringValue() );
    if( bMatch && !bPeek ) {
      _tokenizer.nextToken();
    }
    return bMatch;
  }

  public boolean match( String token, int iType, boolean bPeek ) {
    boolean bMatch = false;

    if( token != null ) {
      if( (iType == _tokenizer.getType()) || ((iType == 0) && (_tokenizer.getType() == ISourceCodeTokenizer.TT_WORD)) ) {
        bMatch = token.equalsIgnoreCase( _tokenizer.getStringValue() );
      }
    }
    else {
      bMatch = (_tokenizer.getType() == iType);
    }

    if( bMatch && !bPeek ) {
      _tokenizer.nextToken();
    }

    return bMatch;
  }

  public void verify( boolean bAssertion, String errorMsg ) {
    if( !bAssertion ) {
      throw new IllegalStateException( "Gosuc Project Parsing Error on Line: " + _tokenizer.getLineNumber() + "\n" + errorMsg );
    }
  }
}
