/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import gw.lang.GosuShop;
import gw.lang.parser.ISourceCodeTokenizer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GosuLexer extends LexerBase {
  private ISourceCodeTokenizer _tokenizer;
  private CharSequence _buffer;
  private int _iBufferIndex;
  private int _iBufferEndOffset;
  @Nullable
  private IElementType _tokenType;
  private int _iTokenEndOffset; // Positioned after the last symbol of the current token
  private Method _goToPosition;


  public final void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
    _buffer = buffer;
    _iBufferIndex = startOffset;
    _iBufferEndOffset = endOffset;
    _tokenType = null;
    _iTokenEndOffset = startOffset;
    _tokenizer = GosuShop.createSourceCodeTokenizer(buffer);
    _tokenizer.setWhitespaceSignificant(true);
    _tokenizer.setCommentsSignificant(true);
    _tokenizer.setParseDotsAsOperators(true);
    _tokenizer.wordChars('_', '_');
    if (isForTemplate()) {
      _tokenizer.setInstructor(GosuShop.createTemplateTokenizerInstructor(_tokenizer));
    }

    _tokenizer.nextToken();
  }

  protected boolean isForTemplate() {
    return false;
  }

  public int getState() {
    return 0;
  }

  public final IElementType getTokenType() {
    locateToken();

    return _tokenType;
  }

  public final int getTokenStart() {
    return _iBufferIndex;
  }

  public final int getTokenEnd() {
    locateToken();
    return _iTokenEndOffset;
  }


  public final void advance() {
    locateToken();
    _tokenType = null;
  }

  private void locateToken() {
    if (_tokenType != null) {
      return;
    }

    if (_iTokenEndOffset == _iBufferEndOffset) {
      _tokenType = null;
      _iBufferIndex = _iBufferEndOffset;
      return;
    }

    // System.out.println( "INDEX: " + _iBufferIndex + "  END: " + _iBufferEndOffset );

    _iBufferIndex = _iTokenEndOffset;

    nextTokenFromTokenizer();

    if (_iTokenEndOffset > _iBufferEndOffset) {
      _iTokenEndOffset = _iBufferEndOffset;
    }
  }

  private void nextTokenFromTokenizer() {
    goToHack(); // hack: until we expose the goToPosition() method in the api
    _tokenType = GosuIjTokenMap.instance().getTokenType(_tokenizer.getType());
    if (_iTokenEndOffset == _tokenizer.getTokenEnd()) {
      _tokenizer.nextToken();
    }
    _iTokenEndOffset = _tokenizer.getTokenEnd();
  }

  private void goToHack() {
    if (_tokenizer.isEOF()) {
      return;
    }
    try {
      if (_goToPosition == null) {
        Class tok = Class.forName("gw.internal.gosu.parser.SourceCodeTokenizer");
        _goToPosition = tok.getDeclaredMethod("goToPosition", int.class);
        _goToPosition.setAccessible(true);
      }
      try {
        _goToPosition.invoke(_tokenizer, _iBufferIndex);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IOException) {
          // Handle read past EOF e.g., unterminated comment
          return;
        }
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CharSequence getBufferSequence() {
    return _buffer;
  }

  public final int getBufferEnd() {
    return _iBufferEndOffset;
  }
}
